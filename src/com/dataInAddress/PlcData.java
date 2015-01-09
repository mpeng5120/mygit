package com.dataInAddress;

public class PlcData {

	// plcData在下位机的数据结构及其顺序不得有误，发生改变，此处应当及时修改
	// 下面为各结构体的各自的头地址(均为偏移地址，不对应下位机的实际地址)

	public static int IN_Head = 0;

	private static int OUT_Head = IN_Head + (Define.MAX_IOS_NUM + 31) / 32 * 4;
	private static int MLR_Head = OUT_Head + (Define.MAX_IOS_NUM + 31) / 32 * 4;

	private static int MRI_Head = MLR_Head + 4;

	private static int MRO_Head = MRI_Head + 8 * 4;

	private static int MCNTInc_Head = MRO_Head + 8 * 4;
	private static int MCNTUp_Head = MCNTInc_Head   ////////////////////////////
			+ (Define.MAX_COUNTER_NUM + 31) / 32 * 4;
	private static int MCNTClr_Head = MCNTUp_Head
			+ (Define.MAX_COUNTER_NUM + 31) / 32 * 4;

	private static int MTMUp_Head = MCNTClr_Head + (Define.MAX_COUNTER_NUM + 31)
			/ 32 * 4;
	private static int MTMEx_Head = MTMUp_Head + (Define.MAX_TIMER_NUM + 31)
			/ 32 * 4;

	private static int SDI_Head = MTMEx_Head + (Define.MAX_TIMER_NUM + 31) / 32
			* 4;
	private static int SDO_Head = SDI_Head + 128 * 4;

	private static int MLD_Head = SDO_Head + 128 * 4;
	private static int MCNT_Head = MLD_Head + 32 * 4;// 计数器

	private static int MTM_Head = MCNT_Head + Define.MAX_TIMER_NUM * 4;// 定时器

	public static int End = MTM_Head + Define.MAX_TIMER_NUM * 4;

	/**
	 * X开头，实际输入
	 * 
	 * @param n
	 * @return
	 */
	public static int getINoffsetAddress(int n) {
		return (n / 32) * 4 + IN_Head;
	}

	public static int getOUToffsetAddress(int n) {
		return (n / 32) * 4 + OUT_Head;
	}

	public static int getMRIoffsetAddress(int n) {
		return (n / 32) * 4 + MRI_Head;
	}

	public static int getMROoffsetAddress(int n) {
		return (n / 32) * 4 + MRO_Head;
	}

	public static int getMTMExoffsetAddress(int n) {
		return (n / 32) * 4 + MTMEx_Head;
	}

	public static int getMTMUpoffsetAddress(int n) {
		return (n / 32) * 4 + MTMUp_Head;
	}
	
	public static int getMLRoffsetAddress(int n) {
		return (n / 32) * 4 + MLR_Head;
	}
	
	public static int getCNToffsetAddress(int n) {
		return (n / 32) * 4 + MTMUp_Head;
	}
	
	public static int getMCNTUpoffsetAddress(int n) {
		return (n / 32) * 4 + MCNTUp_Head;
	}

	/**
	 * 此处每个地址对应四个字节，并不是对应某一个bit
	 * 
	 * @param n
	 * @return
	 */
	public static int getSDIoffsetAddress(int n) {
		return n * 4 + SDI_Head;
	}

	/**
	 * 此处每个地址对应四个字节，并不是对应某一个bit
	 * 
	 * @param n
	 * @return
	 */
	public static int getSDOoffsetAddress(int n) {

		return n * 4 + SDO_Head;
	}



	public static int getMLDoffsetAddress(int n) {
		return n * 4 + MLD_Head;
	}

	/**
	 * 获取计数器设备的地址
	 * 
	 * @param n
	 *            位置编号
	 * @return
	 */
	public static int getMCNToffsetAddress(int n) {
		return n * 4 + MCNT_Head;
	}

	/**
	 * 获取定时器设备的地址
	 * 
	 * @param n
	 *            位置编号
	 * @return
	 */
	public static int getMTMoffsetAddress(int n) {
		return n * 4 + MTM_Head;
	}
	
/*	public static void printAllAddress(){
		System.out.println("OUT head"+OUT_Head+"OUT End"+MLR_Head);
		System.out.println("MRO head"+MRO_Head+"MRo End"+MCNTInc_Head);
		System.out.println("MCNTINC head"+MCNTInc_Head+"MRo End"+MCNTUp_Head);
		System.out.println("MCNTCLR head"+MCNTClr_Head+"MCNTCLR End"+MTMUp_Head);
		System.out.println("SDO head"+SDO_Head+"SDO End"+MLD_Head);
	}*/

}
