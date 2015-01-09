package com.dataInAddress;

//暂时填写这么多，以后随功能增加补充
public class AddressPublic {

	/****************************已确认无误地址********************************************/
	

	//内部FLASH ROM	boot程序区域
	public static int bootProgData_Head=0x08000000;	

	//内部FLASH ROM	机械参数区域(用户保存区）(同时用于machineData中伺服参数(servo)中的开始地址)
	public static int machineROMParam_Head=0x0800C000; 

	//内部FLASH ROM 机械参数区域(同时用于machineData中机械参数中的开始地址)
	//256*Define.MAX_AXIS_NUM*4为伺服参数的大小	
	public static int machineROMData_mechanical_Head=0x0800E000;

	//内部RAM 机械参数区域(同时用于machineData中伺服参数(servo)中的开始地址)
	public static int machineRAMData_Head=0x20004000;

	//内部FLASH ROM	NC程序
	public static int NCProg_Head=0x08040000;
	
	//系统table 地址
	public static int SysTable_Head=0x08010000;
	
	
	
	//=====================================================================
	//sys_sts.h 状态信息
	//=====================================================================
	//读取伺服参数时，有个先写入的过程
	//uint32_t		servoRdNo;	在sys_sts.h文件中
	// 当为负值的时候表示无效，PPU会根据这个值反复的从SPU读取，****	
//	public static int SysSts_servoPara=0x10008000+(Define.MAX_IOS_NUM+31)/32*4+(2+4*8)*Define.MAX_AXIS_NUM;
//	public static int SysSts_servoRdNo=SysSts_servoPara+(256*4+2+4*4)*Define.MAX_AXIS_NUM+(Define.MAX_IOS_NUM+31)/32*4+(4+4*4)*4;
	// 地址变更了，所以显示出现错误，chen 2013-9-12====================================
	public static int SysSts_servoPara= 0x10008200;		// 0x10008000+(Define.MAX_IOS_NUM+31)/32*4+(2+4*8)*Define.MAX_AXIS_NUM;
	public static int SysSts_servoRdNo= 0x1000A350;		// SysSts_servoPara+(256*4+2+4*4)*Define.MAX_AXIS_NUM+(Define.MAX_IOS_NUM+31)/32*4+(4+4*4)*4;

	public static int SysSts_curPosition=0x10008020; // SysSts_servoPara-4*8*Define.MAX_AXIS_NUM;
	

	//=====================================================================
	//模具数据信息头地址,model.h
	//=====================================================================
	
	//内部FLASH ROM	模具数据(用户保存区)，保存model数据到Flash
	public static int userModelData_Head=0x08008000;
	
	//位置信息存放地址
	public static int model_Head=0x20000000;

	public static int model_Timer_Head=model_Head+2+18+8+128+16+(Define.MAX_USER_MODE+31)/32*4+
			4*Define.MAX_USER_PARA;
	public static int model_Counter_Head=model_Timer_Head+4*Define.MAX_TIMER_NUM;
	
	public static int model_P_point_Head=model_Counter_Head+4*Define.MAX_COUNTER_NUM;
/*	public static int model_P_point_Head=model_Head+2+18+8+128+16+(Define.MAX_USER_MODE+31)/32*4+
			4*Define.MAX_USER_PARA+4*Define.MAX_TIMER_NUM+4*Define.MAX_COUNER_NUM;*/
	
	public static int  model_SP_point_Head= 0x20001F10;	//	public static int  model_SP_point_Head=model_P_point_Head+Define.MAX_STDPOINT_NUM*(4*Define.MAX_AXIS_NUM+1+1+1+1);
	
	public static int model_FP_point_Head= 0x200021B0;//model_SP_point_Head+Define.MAX_STDPACK_NUM*((4+2+2+2)*Define.MAX_AXIS_NUM+1+1+1+1);

	public static int model_End = 0x20003AEC;	/// model_FP_point_Head+Define.MAX_AXIS_NUM*2+Define.MAX_FREEPACK_NUM*2+Define.MAX_FREEPOINT_NUM*Define.MAX_AXIS_NUM*4
			//+4+(4+4+4)+4+1;
//	public static int model_allspeed=model_End-5-12;//bar的地址
	public static int model_allspeed=0x200000AA;	
	
	
	//keycode 发送地址控制
	public static int keycode_Head=0x1000B000;
	//警报信息地址
	public static int alm_Head=keycode_Head+5;
	
	//最终_PLC_DATA执行的数据区域
	//读取数据时从此处获得
	public static int finalPlcData_Head=0x10007000;	
	
	
	
	//内部FLASH ROM	系统程序区域(烧写hex程序文件就往这发送)
	public static int sysProg_Head_Main=0x08060000;
	public static int sysProg_Head_PPU=0x0B000000;
	
	private static int IO1=0x09000000;
	private static int SPU1=0x0A000000;
	//获取IO No 地址
	public static int sysIOGetNum(int n){
		return IO1+(n-1)*0x100000;
	}
	//获取SPU No 地址
	public static int sysSPUGetNum(int n) {
		return SPU1+(n-1)*0x100000;
	}
	/*************************************************************************************/


}
