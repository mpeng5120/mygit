package wifiProtocol;
import android.app.Activity;

import com.explain.HexDecoding;
import com.wifiexchange.ChatListener;
/**
 * 该类用于对发送的文件进行通讯协议设定
 * 首先通过构造函数填写发送的数据以及发送的首地址
 * 然后调用sendWriteFormat函数，输入需要发送的次数，便可以发送数据
 * @author 李婷婷
 *
 */
public class WifiSendDataFormat {

	//需要发送的全部数据
	private byte[] dataSource;
	// 确定这段数据需要发送的总次数
	private int sendTimesAll;
	//当前已经发送的次数
	private int sendTimesNow=1;
	//内部地址的偏移地址，byte为单位（用来记录每次开始发送的数据在数据源的位置）
	//private int dataAddress=0;使用dataAddress时，数据重发时出错，所以删掉！！！！！
	//发送数据的偏移地址
	private int sendAddress;

	//和校验检查位，默认正确，和校验错误时，此变量置为false
	private boolean error_flag=true;
	/*
	 * wifi识别位
	 */
	static byte padTomain_write=0x02;
	static byte padTomain_read=0x01;
	static byte mainTopad_write=0x12;
	static byte mainTopad_read=0x11;
	//开始接受校验位,STS1和STS2共用一个字节（STS），字节第一位表示STS1，0表示信息无误，1表示重发
	//字节第二位表示STS2，0表示没有发完，1表示完成
	//STS共有四种状态，0，1，2，3
	//0表示尚未发完，数据无误（大规模发送数据时，如烧写程序，更新固件等），无错未发完
	//1，不存在
	//2表示无错发送结束
	//3表示接收的数据和校验不满足，请求重发
	static int STS_Send_Begin=0;
	static int STS_Send_Finish=2;
	//和校验错误，重发
	static int STS_Error_Back=1;
	//一次性发送最大数据长度
	//private static int data_maxlength=240;  //这里调整发送数据的长度，可以减少模具数据下载的时间2013-7-30	
	private static int data_maxlength=1000;  //这里调整发送数据的长度，可以减少模具数据下载的时间2013-7-30	
	
	private Activity targetActivity ; //发送数据对应的目标Activity
	private ChatListener listener ; //对应下位机返回数据的处理监听
	private boolean isHighPriority; //是否优先发送

	public boolean isHighPriority() {
		return isHighPriority;
	}

	public void setHighPriority(boolean isHighPriority) {
		this.isHighPriority = isHighPriority;
	}
	
	public ChatListener getListener() {
		return listener;
	}

	public void setListener(ChatListener listener) {
		this.listener = listener;
	}

	public Activity getTargetActivity() {
		return targetActivity;
	}

	public void setTargetActivity(Activity targetActivity) {
		this.targetActivity = targetActivity;
	}

/**	 * 获取所需要发送的总的次数
	 * @return
	 */
	public int getSendTimesAll() {
		return sendTimesAll;
	}

	/**
	 * 获取当前已经发送的总的次数
	 * @return
	 */
	public int getSendTimesNow() {
		return sendTimesNow;
	}

	public void sendTimesNowplus() {
		sendTimesNow++;
	}

	public void sendTimesNowminus() {
		sendTimesNow--;
	}

	/**
	 * 构造函数，每次发送数据前，获得数据后必须使用该构造函数进行初始化
	 * @param data	发送给下位机的总数据
	 * @param address 需要发送给下位机的初始地址
	 */
	public WifiSendDataFormat(byte[] data,int address) {
		// TODO Auto-generated constructor stub
		dataSource=data.clone();
		//计算所需要发送的次数
		if (data.length==0) {
			sendTimesAll=1;
		} else {
			if(data.length%data_maxlength==0){
				sendTimesAll=data.length/data_maxlength;
			}else {
				sendTimesAll=data.length/data_maxlength+1;
			}
		}
		//确定系统发送数据的地址
		sendAddress=address;
	}

	/**
	 * 对象可见的可以用来调用发送信息的函数
	 * @return
	 */
	public byte[] sendDataFormat() {
		return sendMassiveDataFormat();	
	}
	
	/**
	 * 请求重发函数
	 * @return
	 */
	private byte[] sendAskAgainDataFormat() {
		//头八个字节数据
		byte[] temp8=new byte[8];

		//识别位 ID+TYPE
		temp8[0]=padTomain_write;
		//STS1 发送正确标志位(暂时先不管，目前是平板发，下位机收)，0表示无错(无需重发)
		//STS2 发送是否结束标志，这里特殊情况为1
		//STS1和STS2共同使用一字节
		temp8[1]=(byte)STS_Error_Back;
		//写入长度，长度为2字节，这里特殊情况为0
		System.arraycopy(HexDecoding.int2byteArray2(0), 0, temp8, 2, 2);

		//小端法写入数据偏移地址的4个字节,此处偏移地址无所谓
		//System.arraycopy(HexDecoding.int2byteArray4(sendAddress+(sendTimesNow-1)*data_maxlength), 0, temp8, 4, 4);			
		/*
		 * 头8字节写入完成，不填写数据
		 */
		byte sumTemp=0;
		for(int k=0;k<temp8.length;k++){
			sumTemp+=temp8[k];
		}

		byte[] sumBytes=new byte[1];
		sumBytes[0]=(byte) (0xff-(byte)(sumTemp));

		//写入最终返回数据
		//后期修改
		byte[] finalResultBytes=new byte[temp8.length+sumBytes.length];

		System.arraycopy(temp8, 0, finalResultBytes, 0, temp8.length);

		System.arraycopy(sumBytes, 0, finalResultBytes, temp8.length, sumBytes.length);
		return finalResultBytes;

	}



	/**
	 * 发送格式转化函数.
	 * 发送大量数据时通讯协议对数据封装
	 * @return	 进行数据格式整理后返回的数据，数据为一次发送的量
	 */
	private byte[] sendMassiveDataFormat(){
		//一次发送，data里最多包含240个字节
		/*
		 * 数据包头8字节写入
		 */
		byte[] temp8=new byte[8];
		//识别位 ID+TYPE
		temp8[0]=padTomain_write;

		if(sendTimesNow!=sendTimesAll){
			//STS1 发送正确标志位(暂时先不管，目前是平板发，下位机收)，0表示无错(无需重发)
			//STS2 发送是否结束标志，0表示未发完
			//STS1和STS2共同使用一个字节
			temp8[1]=(byte)STS_Send_Begin;
			//写入长度，长度现在为2字节
			System.arraycopy(HexDecoding.int2byteArray2(data_maxlength), 0, temp8, 2, 2);
		}else {
			//STS1 发送正确标志位(暂时先不管，目前是平板发，下位机收)，0表示无错(无需重发)
			//STS2 发送是否结束标志，1表示发送完成
			//STS1和STS2共同使用一字节
			temp8[1]=(byte)STS_Send_Finish;
			//写入长度，长度为2字节
			
			System.arraycopy(HexDecoding.int2byteArray2(dataSource.length-(sendTimesNow-1)*data_maxlength), 0, temp8, 2, 2);
		
		}
		//小端法写入数据偏移地址的4个字节
		System.arraycopy(HexDecoding.int2byteArray4(sendAddress+(sendTimesNow-1)*data_maxlength), 0, temp8, 4, 4);			
		/*
		 * 头8字节写入完成，填写数据
		 */
		//System.out.println("sendTimesNow="+sendTimesNow+"      sendTimesAll="+sendTimesAll);
		if(sendTimesNow!=sendTimesAll){
			byte[] temp_source=new byte[data_maxlength];

			System.arraycopy(dataSource, (sendTimesNow-1)*data_maxlength, temp_source, 0, data_maxlength);

			//dataAddress+=data_maxlength;			
			/*
			 * 数据写入完成，添加和校验
			 */
			byte sumTemp=0;
			for(int k=0;k<temp8.length;k++){
				sumTemp+=temp8[k];
			}
			for (int j = 0; j < temp_source.length; j++) {
				sumTemp+=temp_source[j];
			}
			byte[] sumBytes=new byte[1];
			sumBytes[0]=(byte) (0xff-(byte)(sumTemp));


			//写入最终返回数据
			byte[] finalResultBytes=new byte[temp8.length+temp_source.length+sumBytes.length];
			System.arraycopy(temp8, 0, finalResultBytes, 0, temp8.length);
			System.arraycopy(temp_source, 0, finalResultBytes, temp8.length, temp_source.length);
			System.arraycopy(sumBytes, 0, finalResultBytes, temp8.length+temp_source.length, sumBytes.length);
			return finalResultBytes;				
		}else {
			//最后一次应当注意区分两种情况
			byte[] temp_source;
			
			if(dataSource.length%data_maxlength==0){
				//为了可以发送空数据，故在这里添加一些判断
				if(dataSource.length!=0){
					temp_source=new byte[data_maxlength];

					System.arraycopy(dataSource, (sendTimesNow-1)*data_maxlength, temp_source, 0, data_maxlength);
				}else {
					//空数据时的处理
					temp_source=new byte[0];
				}
				
			}else {
				temp_source=new byte[dataSource.length%data_maxlength];

				System.arraycopy(dataSource, (sendTimesNow-1)*data_maxlength, temp_source, 0, dataSource.length%data_maxlength);
			}
			/*
			 * 数据写入完成，添加和校验
			 */
			byte sumTemp=0;
			for(int k=0;k<temp8.length;k++){
				sumTemp+=temp8[k];
			}
			for (int j = 0; j < temp_source.length; j++) {
				sumTemp+=temp_source[j];
			}
			byte[] sumBytes=new byte[1];
			sumBytes[0]=(byte) (0xff-(byte)(sumTemp));

			//写入最终返回数据
			//后期修改
			byte[] finalResultBytes=new byte[temp8.length+temp_source.length+sumBytes.length];

			System.arraycopy(temp8, 0, finalResultBytes, 0, temp8.length);
			System.arraycopy(temp_source, 0, finalResultBytes, temp8.length, temp_source.length);
			System.arraycopy(sumBytes, 0, finalResultBytes, temp8.length+temp_source.length, sumBytes.length);
			

			return finalResultBytes;
		}	
	}
	/**
	 * 检查是否已经发送完毕
	 * @return	true表示发送完毕，false 表示还没完成
	 */
	public boolean finishStatus() {
		if(sendTimesNow==sendTimesAll+1)
			return true;
		else 
			return false;					
	}

	/**
	 * 进行和校验，检查重发标志位
	 * @param backmessage
	 */
	public boolean backMessageCheck(byte[] backmessage) {


		//首先进行和校验，然后检查返回数据中是否用重发标志

		//1. 计算数据的长度，长度为0，表示该数据为应答数据
		byte[] temp_length=new byte[2];
		System.arraycopy(backmessage, 2, temp_length, 0, 2);
		//System.out.println(HexDecoding.Array2Toint(temp_length));
		//2. 对数据计算其和校验数
		byte sumTemp=0;
		for(int k=0;k<8+HexDecoding.Array2Toint(temp_length);k++){
			sumTemp+=backmessage[k];
		}

		sumTemp=(byte) (0xff-(byte)(sumTemp));

		//3. 检查和校验是否正确
		if(backmessage[8+HexDecoding.Array2Toint(temp_length)]==sumTemp){
			//和校验正确
			//检查对方反馈数据，确定是否需要下位机重发
			error_flag=true;

			if((backmessage[1]&0x01)==0){
				//STS1为正确，不需要重发
				sendTimesNow++;
				return true;
			}else {
				return false;
			}
		}else {
			//和校验错误
			System.out.println("和校验错误");
			error_flag=false;
			return false;
		}

	}



}
