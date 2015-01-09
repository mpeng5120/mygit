package com.dbutils;

import java.io.File;
import java.util.HashMap;

import android.os.Environment;

/*
 	保存一切初始设定的参数
 */
public class Constans {

	//device的一个数组类型和enum类型应当完全一致
	//	public static String[] device_list= { "实际输入", "实际输出", "中间输入", "中间输出", "数据输入",
	//		"数据输出", "掉电可保持寄存器", "掉电可保持数据寄存器", "定时器", "计数器", "位置","警报"};
	
	public static enum device_defineEnum{实际输入, 实际输出, 中间输入, 中间输出, 数据输入,
		数据输出, 掉电可保持寄存器, 掉电可保持数据寄存器, 定时器, 计数器, 位置, 警报, 选件操作;

		public static device_defineEnum getDevice(String device){  
			return valueOf(device.toString());  
		}   	
	}
	public static enum nc_defineEnum{原点, 主进程, 进程1, 进程2, 进程3,
		进程4, 进程5, 进程6;

		public static nc_defineEnum getDevice(String device){  
			return valueOf(device.toString());  
		}   	
	}
	public static enum table_defineEnum{T编辑1, T编辑2, T编辑3, T编辑4, T编辑5,
		T编辑6, T编辑7, T编辑8;

		public static table_defineEnum getDevice(String device){  
			return valueOf(device.toString());  
		}   	
	}
	
	//order的数组内容和enum的内容完全一致
	 public static String[] nc_orders = {"MOVE                          移动指令(等待移动到位)",
		                                 "MOVEP                       移动指令(不等待移动到位)", 
		                                 "IF                                  条件指令", 
		                                 "OUT                             信号输出指令", 
		                                 "WAIT                           等待指令",
		                                 "COUNTER                  计数器指令",
		                                 "SEQUENTIAL           程序控制指令",
		                                 "IFThen                        如果就跳转指令",
	    	                             "OUTP                          脉冲输出指令", 
	    	                             "ACC                             加减速指令", 
	    	                             "ALARM                       警报指令", 
	    	                             "RET                              返回指令",
	    	                             "PAUSE                        暂停指令",
	    	                             "PARALLEL                 启动进程指令", 
	    	                             "END                             结束指令" };
    
    public static enum NCorders_enum {
    	 MOVE,MOVEP,IF, OUT, WAIT, COUNTER, SEQUENTIAL,IFThen,   
    	 OUTP,ACC, ALARM, RET, PAUSE, PARALLEL, END;
    	
    	public static NCorders_enum getOrder(String order){  
    	            return valueOf(order.toString());  
    	        }     
        }
    
    /** The Constant PORT. */
    public static final int PORT = 8080;

	//table指令对应代码
	public static final int Code_AND=5;
	public static final int Code_OR=6;
	public static final int Code_MOVE_IMM=24;
	public static final int Code_MOVE=23;
	public static final int Code_THROUGH =4;
	
	public static final int Code_CPEQ=9;//相等
	public static final int Code_CPEQ_IMM=10;//相等立即数
	public static final int Code_CPNE=11;//不等于
	public static final int Code_CPNE_IMM=12;
	public static final int Code_CPGT=13;//大于
	public static final int Code_CPGT_IMM=14;
	public static final int Code_CPLT=15;//小于
	public static final int Code_CPLT_IMM=16;
	public static final int Code_CPGE=17;//大于等于
	public static final int Code_CPGE_IMM=18;
	public static final int Code_CPLE=19;//小于等于
	public static final int Code_CPLE_IMM=20;
	
	public static final int Code_INC=58;
	public static final int Code_DEC=59;
	
	
	public static final int Code_SEQ_END=60;//对应NC代码中的END
	public static final int Code_SEQ_RET=55;//对应函数中的程序返回RET
	
	public static final int Code_A_1=7;
	public static final int Code_B_1=8;
	public static final int Code_OUT_ON=21;
	public static final int Code_OUT_OFF=22;
	public static final int Code_JUMP=27;
	public static final int Code_JUMP_ON=28;
	public static final int Code_CALL=29;
    public static final int Code_CALL_ON=30;
	public static final int Code_QON=25;
    public static final int Code_QOFF=26;
    public static final int Code_ORDER_LABEL=2;
    public static final int Code_ORDER_SUBRoutine=1;
    public static final int Code_ORDER_Q=3;
    public static final int Label_Offset=65536;
    public static final int Subroutine_Offset=256;
    
    //fragment标志位
    public static final String[] FRAGMENT_DeviceString={"Device_Edit_1","Device_Edit_2","Device_Edit_3","Device_Edit_4","Device_Edit_5","Device_Edit_6","Device_Edit_7","Device_Edit_8","Device_Edit_9","Device_Edit_10","Device_Edit_11","Device_Edit_12","Device_Edit_13"};
    public static final String[] FRAGMENT_NCString={"NC_1","NC_2","NC_3","NC_4","NC_5","NC_6","NC_7","NC_8"};
    public static final String[] FRAGMENT_TableString={"Table_1","Table_2","Table_3","Table_4","Table_5","Table_6","Table_7","Table_8"};
    
    //seekbard当前状态数值
    public static int currentSeekbarValue=0;
    
	/**
	 * 文件目录
	 * 最终应转到strings中去
	 */
	/*文件目录结构*/
	 public static String trFolder="TR";//一级目录   根目录   
	 	 public static String mechanicalParameterFolder="机械参数";//二级目录  
	 	 	public static String 机械参数 ="mechanicalParameter.set";
	 	 	public static String 伺服参数 ="servoParameter.set";
	 	 public static String otherParameterFolder="其他参数";
	 	 public static String zkDataFolder="字库";
	 	 public static String alarmDataFolder="警报信息";
	     public static String mouldDataFolder="模具数据";
	     	public static String mouldData1Folder="模具数据1";//三级目录  
	     	public static String mouldData2Folder="模具数据2";
	     	public static String cacheFolder="cache";
	     		public static String table文本数据="Userlog.trt";//四级目录  
	     		public static String nc文本数据="userprg.trt";
	     		public static String 设备名称数据="device.trt";
	     		public static String 模具数据="mould.trt";
	     		
	     		public static String ListData="Data.xml"; //add by mpeng
	     		
	     		public static String dataToDownloadFolder="需要下载的数据";
	     			public static String ncToTableFolder="nc转成table后的data数据"; //五级目录  
	     			public static String tableToDataFolder="table转换后的data数据";
	     			public static String positionSetDataFolder="位置设定等数据";
	     			public static String otherDataFolder="其他什么数据";
			public static String mouldData3Folder="模具数据3";//三级目录
			public static String mouldData4Folder="模具数据4";
		 public static String otherData1Folder="其他数据1";//二级目录  
		 public static String otherData2Folder="其他数据2";
		 public static String downloadFolder="上下载管理";//二级目录  	
		 //public static String messageFolder="信息管理";//二级目录  	
 
    /*sdPATH取得机器的SD卡位置，File.separator为分隔符“/”*/  
	 public final static String sdPATH=Environment.getExternalStorageDirectory()+File.separator;//sd卡路径名
	 	public final static String trPATH=sdPATH+trFolder+File.separator;//一级路径
	 		public final static String mechanicalParameterPATH=trPATH+mechanicalParameterFolder+File.separator;//二级路径  
	 		public final static String otherParameterPATH=trPATH+otherParameterFolder+File.separator;
	 		public final static String rawPATH=trPATH+"raw"+File.separator;
	 		public final static String getdataPATH=trPATH+"getdata"+File.separator;
	 		public final static String zkDataPATH=trPATH+zkDataFolder+File.separator;
	 		public final static String alarmDataPATH=trPATH+alarmDataFolder+File.separator;
	 		public final static String mouldDataPATH=trPATH+mouldDataFolder+File.separator;
	 			public final static String mouldData1PATH=mouldDataPATH+mouldData1Folder+File.separator;//三级路径
	 			public final static String mouldData2PATH=mouldDataPATH+mouldData2Folder+File.separator;
	 			public final static String cachePATH=mouldDataPATH+cacheFolder+File.separator;
//	 				public final static String tableTextPATH=mouldData2PATH+tableTextFolder+File.separator;//四级数据
//	 				public final static String ncTextPATH=mouldData2PATH+ncTextFolder+File.separator;
//	 				public final static String deviceNamePATH=mouldData2PATH+deviceNameFolder+File.separator;
//	 				public final static String dataToDownloadPATH=mouldData2PATH+dataToDownloadFolder+File.separator;
//	 					public final static String ncToTablePATH=dataToDownloadPATH+ncToTableFolder+File.separator;//五级目录
//	 					public final static String tableToDataPATH=dataToDownloadPATH+tableToDataFolder+File.separator;
//	 					public final static String positionSetDataPATH=dataToDownloadPATH+positionSetDataFolder+File.separator;
//	 					public final static String otherDataPATH=dataToDownloadPATH+otherDataFolder+File.separator;     					
    			
				public final static String mouldData3PATH=mouldDataPATH+mouldData3Folder+File.separator;//三级路径
				public final static String mouldData4PATH=mouldDataPATH+mouldData4Folder+File.separator;
    		
			public final static String otherData1PATH=trPATH+otherData1Folder+File.separator;//二级路径  
			public final static String otherData2PATH=trPATH+otherData2Folder+File.separator;
    
			public final static String XmlFolder =  mouldDataPATH+cacheFolder;
			
			
			/*
      * 文件新建类   
      */
			public static TRFileHandler mechanicalParameter=TRFileHandler.create();
			public static TRFileHandler otherParameter=TRFileHandler.create();
			public static TRFileHandler alarmData=TRFileHandler.create();
			public static TRFileHandler mouldData=TRFileHandler.create();
			public static TRFileHandler cache=TRFileHandler.create();
			public static TRFileHandler mouldData2=TRFileHandler.create();
			public static TRFileHandler tableText=TRFileHandler.create();
			public static TRFileHandler ncText=TRFileHandler.create();
			public static TRFileHandler deviceName=TRFileHandler.create();
			public static TRFileHandler dataToDownload=TRFileHandler.create();
			public static TRFileHandler ncToTable=TRFileHandler.create();
			public static TRFileHandler tableToData=TRFileHandler.create();
			public static TRFileHandler positionSetData=TRFileHandler.create();
			public static TRFileHandler otherData=TRFileHandler.create();
			public static TRFileHandler mouldData3=TRFileHandler.create();
			public static TRFileHandler mouldData4=TRFileHandler.create();
			public static TRFileHandler otherData1=TRFileHandler.create();
			public static TRFileHandler otherData2=TRFileHandler.create();
			public static TRFileHandler raw=TRFileHandler.create();
			public static TRFileHandler getdata=TRFileHandler.create();
			public static TRFileHandler download=TRFileHandler.create();
			//public static TRFileHandler message=TRFileHandler.create();
			/**
			 * 维护界面
			 */
			public static final int  ERROR_LOG=0;
			public static final int  ERROR_REGIST=1;
			public static final int  LOG=3;
			public static final int  CREATE_AGAIN=6;
			
			/*public static final int sevoparas= Constans.mechanicalParameter
 					.readOutFile(Constans.mechanicalParameterPATH +"勿删/"+ Constans.伺服参数).length/19+1;*/
			
		
			
}
