package wifiProtocol;

import android.app.Activity;
import android.util.Log;

import com.explain.HexDecoding;
import com.wifiexchange.ChatListener;

public class WifiReadDataFormat {

	// 由于读取数据，只有一次，一次发送完成后，只要确认发送成功，便不再发送
	// 每一个读取数据过程必须新建不同的类的实例，并且调用构造函数进行初始话
	// 发送结束标志
	private boolean finish_status = false;

	// 读取数据的地址
	private int readDataAddress;
	// 读取数据的长度
	private int byteLength;
	// 和校验检查位，默认正确，和校验错误时，此变量置为false
	private boolean error_flag = true;

	private byte[] getData = null;
	
	private Activity targetActivity ; //发送数据对应的目标Activity
	private ChatListener listener ; //对应下位机返回数据的处理监听
	private boolean isHighPriority; //是否优先发送
	
	private final String TAG ="WifiReadDataFormat";

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
	 * 构造函数（该类型数据封装由于只需要一次就能读完数据，
	 * 因此不需要每次建立通讯都新生成一个对象）
	 * 
	 * @param readDataAddress
	 *            需要读取数据的地址
	 * @param byteLength
	 *            需要读取数据的长度
	 */
	public WifiReadDataFormat(int readDataAddress, int byteLength) {
		// 赋值
		this.readDataAddress = readDataAddress;
		this.byteLength = byteLength;
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
		System.arraycopy(HexDecoding.int2byteArray2(byteLength), 0, temp8, 2, 2);

		// 小端法写入数据偏移地址的4个字节
		System.arraycopy(HexDecoding.int2byteArray4(readDataAddress), 0, temp8,
				4, 4);
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
		System.arraycopy(HexDecoding.int2byteArray2(byteLength), 0, temp8, 2, 2);

		// 小端法写入数据偏移地址的4个字节
		System.arraycopy(HexDecoding.int2byteArray4(readDataAddress), 0, temp8,
				4, 4);
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
		if (finish_status == true)
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
		//System.out.println("byteLength="+byteLength+"     HexDecoding.Array2Toint(temp_length)="+HexDecoding.Array2Toint(temp_length));
		if (byteLength != HexDecoding.Array2Toint(temp_length)) {
			System.out.println("数据偏移错误!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			error_flag = false;
			return;
		}
		getData = new byte[HexDecoding.Array2Toint(temp_length)];
		System.arraycopy(backmessage, 8, getData, 0,
				HexDecoding.Array2Toint(temp_length));
		// 2. 对数据计算其和校验数
		byte sumTemp = 0;
		for (int k = 0; k < 8 + HexDecoding.Array2Toint(temp_length); k++) {
			sumTemp += backmessage[k];
		}

		sumTemp = (byte) (0xff - (byte) (sumTemp));

		// 3. 检查和校验是否正确
		if (backmessage[8 + HexDecoding.Array2Toint(temp_length)] == sumTemp) {
			// 和校验正确
			// 检查对方反馈数据，确定是否需要下位机重发
			error_flag = true;

			if ((backmessage[1] & 0x01) == 0) {
				// STS1为正确，不需要重发
				finish_status = true;
			}

		} else {
			// 和校验错误
			System.out.println("一次性读取和校验错误");
			error_flag = false;
		}

	}

	/**
	 * 返回最终得到的数据
	 * 
	 * @return
	 */
	public byte[] getFinalData() {
		return getData;
	}

}
