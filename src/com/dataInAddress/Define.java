package com.dataInAddress;


public class Define {
	// 先要给系统定义一些最大值，来界定系统的能力，先定义这些范围，再来定义数组的大小才有意义，
	// 而且直接修改这里的数值，可以更容易修改系统中用到数组的大小。

	// 系统能控制最大的轴数
	public static int MAX_AXIS_NUM=8;
	// 系统最多可用定时器数
	public static int MAX_TIMER_NUM=64;
	// 系统最多可用计数器数
	public static int MAX_COUNTER_NUM=64;
	// 系统最多带IO基板数
	public static int MAX_IOBORAD_NUM=	6	;		// ID 从1~6  SPU占用1个
	// 系统最多可用标准位置数
	public static int MAX_STDPOINT_NUM=200;//256;
	// 系统最多可用标准装箱数
	public static int MAX_STDPACK_NUM=8;
	// 系统最多可用自由装箱数
	public static int MAX_FREEPACK_NUM=2;//8;
	// 系统最多可用自由装箱位置数
	public static int MAX_FREEPOINT_NUM=200;//8;

	// 系统最多可用输入
	public static int MAX_IOS_NUM	= 128	;		// 128个I，128个O
	
	public static int MAX_IO_NUM=8;

	// // 系统最多可存储磨具数据数

	// 系统定义用户最多可用bit输入数，可以通过平板电脑输入
	public static int MAX_USER_MODE=	32	;
	// 系统定义用户最多可用32bit数据的输入数，可以通过平板电脑输入
	public static int MAX_USER_PARA=16	;
	
	// 显示所有位置，如主界面
	public static int POSITION_ALL = 0; 
	//制品的位置
	public static int POSITION_GOODS = 1;
	//料道的位置
	public static int POSITION_MATERIAL = 2;





}
