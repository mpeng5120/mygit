package com.dataInAddress;

import com.explain.HexDecoding;

/**
 *  数据基本单位为byte
 * @author 李婷婷
 *
 */

public class Machinedata {

	//这个参数万万万万不得设置为静态变量
	public int arrayIndex=0;

	/**
	 * 机械参数转化为发送的数组
	 * @return
	 */
	public byte[] structMechanicalByteArray() {

		byte[] temp=new byte[16+52*Define.MAX_AXIS_NUM+Define.MAX_IO_NUM+Define.MAX_AXIS_NUM+4+2*8*Define.MAX_AXIS_NUM+8*2+5+3*Define.MAX_AXIS_NUM];
		// 参数识别描述信息 uint8_t   version[16];	
		//16个字节	
		System.arraycopy(version,0 , temp, 0, 16);
		arrayIndex+=16;
		
		// 1马达属性  马达相关
		/*struct
		motorPara[MAX_AXIS_NUM];*/
		//28×MAX_AXIS_NUM字节
		System.arraycopy(motorParaDataBytes(),0, temp, arrayIndex,motorParaDataBytes().length);
		arrayIndex+=motorParaDataBytes().length;
		
		// IO数及ID情况uint8_t 	ioIDPara[MAX_IO_NUM];		//MAX_IO_NUM个字节 
		for(int i=0;i<ioIDPara.length;i++){
			System.arraycopy(HexDecoding.int2byte(ioIDPara[i]), 0, temp, arrayIndex, 1);
			arrayIndex+=1;
		}		
		// 伺服数及伺服ID情况 uint8_t 	servoIDPara[MAX_AXIS_NUM];		//MAX_IO_NUM个字节   
		for(int i=0;i<servoIDPara.length;i++){
			System.arraycopy(HexDecoding.int2byte(servoIDPara[i]), 0, temp, arrayIndex, 1);
			arrayIndex+=1;
		}
		// IO基板的AD使用标记union 	_BIT		ADuseFlg;		//  4字节
		System.arraycopy(HexDecoding.int2byteArray4(ADuseFlg), 0, temp, arrayIndex, 4);
		arrayIndex+=4;
		// 4 wifi设定 

		// 1 原点复归相关参数 struct orgPara[MAX_AXIS_NUM]; //8*MAX_AXIS_NUM字节
		for (int i = 0; i < orgParas.length; i++) {
			//原点偏移
			System.arraycopy(HexDecoding.int2byteArray4(orgParas[i].orgOffset), 0, temp, arrayIndex, 4);
			arrayIndex+=4;
			//原点复归速度1
			System.arraycopy(HexDecoding.int2byte(orgParas[i].orgSpeedPre), 0, temp, arrayIndex,1);
			arrayIndex+=1;
			//原点复归速度2
			System.arraycopy(HexDecoding.int2byte(orgParas[i].orgSpeedAft), 0, temp, arrayIndex,1);
			arrayIndex+=1;
			//原点方向
			System.arraycopy(HexDecoding.int2byte(orgParas[i].orgDir), 0, temp, arrayIndex,1);
			arrayIndex+=1;
			//原点限位信号编号
			System.arraycopy(HexDecoding.int2byte(orgParas[i].orgLmtNo), 0, temp, arrayIndex,1);
			arrayIndex+=1;
		}

		// 2 行程限位功能x dz struct axisStroke[MAX_AXIS_NUM]; //8×MAX_AXIS_NUM
		for (int i = 0; i < axisStrokes.length; i++) {
			//最小行程
			System.arraycopy(HexDecoding.int2byteArray4(axisStrokes[i].minStroke), 0, temp, arrayIndex, 4);
			arrayIndex+=4;
			//最大行程
			System.arraycopy(HexDecoding.int2byteArray4(axisStrokes[i].maxStroke), 0, temp, arrayIndex, 4);
			arrayIndex+=4;
		}

		// 3 干涉防止相关 struct collisionOpt[2]; 		// 8*2个字节
		for (int i = 0; i < collisionOpts.length; i++) {
			//是否使用干涉防止
			System.arraycopy(HexDecoding.int2byteArray2(collisionOpts[i].useFlag), 0, temp, arrayIndex,2);
			arrayIndex+=2;
			//轴参数1
			System.arraycopy(HexDecoding.int2byte(collisionOpts[i].axisNo[0]), 0, temp, arrayIndex,1);
			arrayIndex+=1;
			//轴参数2
			System.arraycopy(HexDecoding.int2byte(collisionOpts[i].axisNo[1]), 0, temp, arrayIndex,1);
			arrayIndex+=1;
			//原点限位信号编号
			System.arraycopy(HexDecoding.int2byteArray4(collisionOpts[i].minPitch), 0, temp, arrayIndex,4);
			arrayIndex+=4;
		}
		// 5档速度表 uint8_t	allSpeed[5];	// 5字节
		for (int i = 0; i < allSpeed.length; i++) {
			//5档速度表
			System.arraycopy(HexDecoding.int2byte(allSpeed[i]), 0, temp, arrayIndex,1);
			arrayIndex+=1;
		}
		//每轴JOG速度3档大小 uint8_t	jogSpeed[MAX_AXIS_NUM][3];	// 3×MAX_AXIS_NUM字节
		for (int i = 0; i < jogSpeed.length; i++) {
			//i表示轴数
			for (int j = 0; j < jogSpeed[i].length; j++) {
				//每轴jog速度大小
				System.arraycopy(HexDecoding.int2byte(jogSpeed[i][j]), 0, temp, arrayIndex,1);
				arrayIndex+=1;
			}
		}
		// 和校验

		return temp;
	}

	/**
	 * 伺服参数转化为发送的数组
	 * @return
	 */
	public byte[] structServoByteArray() {
		
		byte[] temp=new byte[256*4*8];
		
		//总共有八轴
		for(int i=0;i<servoPara.length;i++){
			
			//每轴有256个参数
			for (int j = 0; j < servoPara[i].length; j++) {
				//每一个参数只有4个字节
				System.arraycopy(HexDecoding.int2byteArray4(servoPara[i][j]), 0, temp, arrayIndex, 4);
				arrayIndex+=4;
			}
		}
		
		return temp;
	}
	
	
	
	
	
	//马达属性数据转字符数组
	//马达属性转换数据
	public byte[] motorParaDataBytes() {
		byte[] temp=new byte[52*Define.MAX_AXIS_NUM];

		int arrayIndex_inner=0;
		for (int i=0;i<motoParas.length;i++) {
			System.arraycopy(HexDecoding.int2byteArray2(motoParas[i].plsPerCirclel),0, temp,arrayIndex_inner,2);
			arrayIndex_inner+=2;

			//参数错位调整
			System.arraycopy(HexDecoding.int2byteArray2(motoParas[i].maxPRM), 0, temp,arrayIndex_inner,2);
			//System.arraycopy(HexDecoding.int2byteArray2(motoParas[i].um10PerPls), 0, temp,arrayIndex_inner,2);
			arrayIndex_inner+=2;

			System.arraycopy(HexDecoding.int2byteArray4(motoParas[i].um10PerPls), 0, temp,arrayIndex_inner,4);
			//System.arraycopy(HexDecoding.int2byteArray4(motoParas[i].maxPRM), 0, temp,arrayIndex_inner,4);
			arrayIndex_inner+=4;

			for (int j = 0; j < motoParas[i].accTimeTbl.length; j++) {

				System.arraycopy(HexDecoding.int2byteArray4(motoParas[i].accTimeTbl[j]), 0, temp, arrayIndex_inner,4);
				arrayIndex_inner+=4;
			}

			for (int j = 0; j < motoParas[i].accTimeTbl.length; j++) {

				System.arraycopy(HexDecoding.int2byteArray4(motoParas[i].dccTimeTbl[j]), 0, temp, arrayIndex_inner,4);
				arrayIndex_inner+=4;
			}

			System.arraycopy(HexDecoding.int2byteArray2(motoParas[i].initDir), 0, temp, arrayIndex_inner,2);
			arrayIndex_inner+=2;

			System.arraycopy(HexDecoding.int2byteArray2(motoParas[i].stopDecTime), 0, temp, arrayIndex_inner,2);
			arrayIndex_inner+=2;

		}

		return temp;
	}

	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//下面的是machineData.h文件的数据结构
	//中间转换了2次，虽然效率低了，但是计算起来基本不会费时间，直接转换的话怕出错了的话永远也找不到哪个地方错了
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


	public Machinedata()
	{
		//所有对象数组需要进行初始化

		//马达属性初始化
		for (int i = 0; i < motoParas.length; i++) {
			motoParas[i]=new motorPara();
		}
		//行程限位功能初始化
		for (int i = 0; i < orgParas.length; i++) {
			orgParas[i]=new orgPara();
		}
		//行程限位功能初始化
		for (int i = 0; i < axisStrokes.length; i++) {
			axisStrokes[i]=new axisStroke();
		}
		//干涉防止相关	
		for (int i = 0; i < collisionOpts.length; i++) {
			collisionOpts[i]=new collisionOpt();
		}

	}

	//参数识别描述信息 16字节
	public byte[] version=new byte[16];

	//马达属性
	public motorPara[] motoParas=new motorPara[Define.MAX_AXIS_NUM];
	//马达属性数据结构
	//马达属性数据格式
	public class motorPara{

		public motorPara() {
			// TODO Auto-generated constructor stub
			plsPerCirclel=0;
			um10PerPls=0;
			maxPRM=0;
			for ( int i: accTimeTbl) {
				i=0;
			}
			for ( int i: dccTimeTbl) {
				i=0;
			}
			initDir=0;
			stopDecTime=0;
		}
		//每一圈脉冲数 2字节
		public int plsPerCirclel;
		//一圈多少 0.01mm
		public int um10PerPls;
		//设定100%时候的最大速度 2字节
		public int maxPRM;
		//加速度表 单位：0.001ms 2字节
		public int accTimeTbl[]=new int[5];
		//减速度表 单位：0.001ms 2字节
		public int dccTimeTbl[]=new int[5];
		//马达初始方向 2字节
		public int initDir;
		//突然停止时候减速时间 2字节
		public int stopDecTime;

	}

	//伺服属性，每轴256参数 4字节
	public int servoPara[][]=new int[Define.MAX_AXIS_NUM][256];
	//原点复归相关参数
	public orgPara[] orgParas=new orgPara[Define.MAX_AXIS_NUM];
	//2 行程限位功能x dz
	public axisStroke[] axisStrokes=new axisStroke[Define.MAX_AXIS_NUM];
	// 3 干涉防止相关	
	public collisionOpt[] collisionOpts=new collisionOpt[2];


	// 1 原点复归相关参数
	public class orgPara
	{
		public orgPara() {
			// TODO Auto-generated constructor stub
			orgOffset=0;
			orgSpeedPre=0;
			orgSpeedAft=0;
			orgDir=0;
			orgLmtNo=0;
		}
		// 原点偏移,4字节
		public int orgOffset;			// 单位 0.01mm		
		// 原点复归速度1，8位
		public int  orgSpeedPre;		// 单位： 1%
		// 原点复归速度2，8位
		public int  orgSpeedAft;		// 单位： 1%
		// 原点方向，8位
		public int  orgDir;			    // 单位： 0：CW		1：CCW
		// 原点限位信号编号，8位
		public int  orgLmtNo;			  // 单位： org对应的限位传感器编号 （ 0 ~ 127 ）
	}  

	// 2 行程限位功能x dz
	public class axisStroke
	{
		// 最小行程，4字节
		public int 	minStroke;			// 	单位 0.01mm
		// 最大行程，4字节
		public int  	maxStroke;		// 	单位 0.01mm
	} 

	// 3 干涉防止相关	
	public class collisionOpt
	{
		//是否使用干涉防止,1字节
		public int useFlag;
		//轴参数，1字节
		public int axisNo[]=new int[2];		// 干涉的两轴编号，  1~8，  
		//最小距离，4字节
		public int minPitch;		// 单位 0.01mm 
	} ; 		// 两个干涉设定参数

	// 3 控制器属性

	// IO数及ID情况,8位
	public int ioIDPara[]=new int[Define.MAX_IO_NUM];		// ID必须为非0， 为0表示没有该IO。

	// 伺服数及伺服ID情况,8位
	public int servoIDPara[]=new int[Define.MAX_AXIS_NUM];		// ID必须为非0， 为0表示没有该轴。  

	// IO基板的AD使用标记
	public int ADuseFlg;		//  8个IO，最多 16个AD，所以该标记为低16个bit有效，为0表示不使用。  
	// 4 wifi设定

	// 机械功能相关

	// 4 全体速度功能
	// 5档速度表
	public int allSpeed[]=new int[5];	// 单位： 1%

	// 自由操作相关
	//每轴JOG速度3档大小,8位
	public int	jogSpeed[][]=new int[Define.MAX_AXIS_NUM][3];	// 单位： 1%

	//  uint8_t   checkSum;   // 和校验




}
