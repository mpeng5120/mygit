package wifiProtocol;

import android.app.Activity;
import android.util.Log;

import com.explain.HexDecoding;
import com.wifiexchange.ChatListener;

/**
 * 该类暂时用于读取大量数据，每次读取最大240字节长度数据，读取的同时，在验证无误后 在该类成员字节数组中整合并且保存返回数据
 * 
 * @author 李婷婷
 * 
 */
public class WifiReadMassiveDataFormat {

	// 每一个读取数据过程必须新建不同的类的实例，并且调用构造函数进行初始话

	private int data_maxlength =1024;
	//private int data_maxlength =500;
	// 读取数据的地址
	private int readDataAddress;
	// 读取数据的长度
	private int byteLength;
	// 和校验检查位，默认正确，和校验错误时，此变量置为false
	private boolean error_flag = true;

	private int sendTimesNow = 1;
	private int srcPosition = 0;// 存入getData数组时使用的偏移标志
	private int getDataByteLenth;

	private int sendTimesAll;

	private byte[] getData;
	
	private Activity targetActivity ; //发送数据对应的目标Activity
	private ChatListener listener ; //对应下位机返回数据的处理监听
	private boolean isHighPriority; //是否优先发送

	public boolean isHighPriority() {
		return isHighPriority;
	}

	public void setHighPriority(boolean isHighPriority) {
		this.isHighPriority = isHighPriority;
	}
	
	public Activity getTargetActivity() {
		return targetActivity;
	}

	public void setTargetActivity(Activity targetActivity) {
		this.targetActivity = targetActivity;
	}

	public ChatListener getListener() {
		return listener;
	}

	public void setListener(ChatListener listener) {
		this.listener = listener;
	}
	
	public int getLength() {
		return this.byteLength;
	}

	/**
	 * 返回最终得到的数据
	 * 
	 * @return
	 */
	public byte[] getFinalData() {
		return getData;
	}

	/**
	 * 构造函数
	 * 
	 * @param readDataAddress
	 *            需要读取数据的地址
	 * @param byteLength
	 *            需要读取数据的长度
	 */
	public WifiReadMassiveDataFormat(int readDataAddress, int byteLength) {
		// 赋值
		this.readDataAddress = readDataAddress;
		this.byteLength = byteLength;
		// 根据要求初始化字节数组
		getData = new byte[byteLength];
		// 初始化需要发送的次数
		if (byteLength % data_maxlength == 0) {
			sendTimesAll = byteLength / data_maxlength;
		} else {
			sendTimesAll = byteLength / data_maxlength + 1;
		}

	}

	/**
	 * 对象可见的可以用来调用发送信息的函数
	 * 
	 * @return
	 */
	public byte[] sendDataFormat() {
		// 检查和校验是否正确
		if (error_flag) {
			return sendReadFormat();
		} else {
			return sendAskAgainDataFormat();
		}
	}

	/**
	 * 任何的读取数据，必须在协议封装类外填写地址
	 * 
	 * @return
	 */
	private byte[] sendReadFormat() {
		// 一次发送，data里最多包含240个字节
		/*
		 * 数据包头8字节写入
		 */
		byte[] temp8 = new byte[8];
		// 识别位 ID+TYPE
		temp8[0] = WifiSendDataFormat.padTomain_read;

		// STS1 发送正确标志位(暂时先不管，目前是平板发，下位机收)，0表示无错(无需重发)
		// STS2 发送是否结束标志，0表示未发完
		// STS1和STS2共同使用一个字节
		temp8[1] = (byte)0x02; //WifiSendDataFormat.STS_Send_Begin;
		// 写入长度，长度现在为2字节
		// 长度在初始化时已经确定
		if (sendTimesNow != sendTimesAll) {
			System.arraycopy(HexDecoding.int2byteArray2(data_maxlength), 0,
					temp8, 2, 2);
			getDataByteLenth = data_maxlength;
		} else {
			System.arraycopy(
					HexDecoding.int2byteArray2(byteLength % data_maxlength), 0,
					temp8, 2, 2);
			getDataByteLenth = byteLength % data_maxlength;
		}

		// 小端法写入数据偏移地址的4个字节
		System.arraycopy(
				HexDecoding.int2byteArray4(readDataAddress + (sendTimesNow - 1)
						* data_maxlength), 0, temp8, 4, 4);
		/*
		 * 头8字节写入完成，不填写数据
		 */
		byte sumTemp = 0;
		for (int k = 0; k < temp8.length; k++) {
			sumTemp += temp8[k];
		}

		byte[] sumBytes = new byte[1];
		sumBytes[0] = (byte) (0xff - (byte) (sumTemp));

		// 写入最终返回数据
		// 后期修改
		byte[] finalResultBytes = new byte[temp8.length + sumBytes.length];

		System.arraycopy(temp8, 0, finalResultBytes, 0, temp8.length);

		System.arraycopy(sumBytes, 0, finalResultBytes, temp8.length,
				sumBytes.length);
		return finalResultBytes;

	}

	/**
	 * 请求重发函数
	 * 
	 * @return
	 */
	private byte[] sendAskAgainDataFormat() {
		System.out.println("使用重发机制");
		// 头八个字节数据
		byte[] temp8 = new byte[8];

		// 识别位 ID+TYPE，此处无所谓
		temp8[0] = WifiSendDataFormat.padTomain_read;
		// STS1 发送正确标志位(暂时先不管，目前是平板发，下位机收)，0表示无错(无需重发)
		// STS2 发送是否结束标志，这里特殊情况为1
		// STS1和STS2共同使用一字节
		temp8[1] = (byte) WifiSendDataFormat.STS_Error_Back;
		// 写入长度，长度现在为2字节
		// 长度在初始化时已经确定
		if (sendTimesNow != sendTimesAll) {
			System.arraycopy(HexDecoding.int2byteArray2(data_maxlength), 0,
					temp8, 2, 2);
			getDataByteLenth = data_maxlength;
		} else {
			System.arraycopy(
					HexDecoding.int2byteArray2(byteLength % data_maxlength), 0,
					temp8, 2, 2);
			getDataByteLenth = byteLength % data_maxlength;
		}

		// 小端法写入数据偏移地址的4个字节
		System.arraycopy(
				HexDecoding.int2byteArray4(readDataAddress + (sendTimesNow - 1)
						* data_maxlength), 0, temp8, 4, 4);
		/*
		 * 头8字节写入完成，不填写数据
		 */
		byte sumTemp = 0;
		for (int k = 0; k < temp8.length; k++) {
			sumTemp += temp8[k];
		}

		byte[] sumBytes = new byte[1];
		sumBytes[0] = (byte) (0xff - (byte) (sumTemp));

		// 写入最终返回数据
		// 后期修改
		byte[] finalResultBytes = new byte[temp8.length + sumBytes.length];

		System.arraycopy(temp8, 0, finalResultBytes, 0, temp8.length);

		System.arraycopy(sumBytes, 0, finalResultBytes, temp8.length,
				sumBytes.length);
		return finalResultBytes;

	}

	/**
	 * 检查是否已经发送完毕
	 * 
	 * @return true表示发送完毕，false 表示还没完成
	 */
	public boolean finishStatus() {
		if (sendTimesNow == sendTimesAll + 1)
			return true;
		else
			return false;
	}

	/**
	 * 进行和校验，检查重发标志位
	 * 
	 * @param backmessage
	 */
	public void backMessageCheck(byte[] backmessage) {
		// 首先进行和校验，然后检查返回数据中是否用重发标志

		// 1. 计算数据的长度，长度为0，表示该数据为应答数据
		byte[] temp_length = new byte[2];
		System.arraycopy(backmessage, 2, temp_length, 0, 2);

		// System.out.println(HexDecoding.Array2Toint(temp_length));
		// System.out.println("应该获取的长度" + getDataByteLenth);
		// 检查数组中的一个位置，如果不为11或12就直接跳出(int)(temp[0]&0xff)
		if (getDataByteLenth != HexDecoding.Array2Toint(temp_length)) {
			System.out.println("头地址偏移！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
			error_flag = false;
			return;
		}

		// System.out.println("下位机的校验码"
		// + backmessage[8 + HexDecoding.Array2Toint(temp_length)]);

		// 2. 对数据计算其和校验数
		byte sumTemp = 0;
		for (int k = 0; k < 8 + HexDecoding.Array2Toint(temp_length); k++) {
			sumTemp += backmessage[k];
		}

		sumTemp = (byte) (0xff - (byte) (sumTemp));
		// System.out.println("平板计算得到的" + sumTemp);
		// 3. 检查和校验是否正确
		if (backmessage[8 + HexDecoding.Array2Toint(temp_length)] == sumTemp) {
			// 和校验正确
			// 检查对方反馈数据，确定是否需要下位机重发
			error_flag = true;

			if ((backmessage[1] & 0x01) == 0) {
				// STS1为正确，不需要重发
				sendTimesNow++;
				// 数据正确后保存数据,修改数据偏移,返回的数据的前8字节为
				System.arraycopy(backmessage, 8, getData, srcPosition,
						getDataByteLenth);
				srcPosition += getDataByteLenth;
			}
		} else {
			// 和校验错误
			System.out.println("和校验错误");
			error_flag = false;
		}

	}

}
