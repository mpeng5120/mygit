package wifiRunnablesAndChatlistener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dataInAddress.AddressPublic;
import com.dataInAddress.Define;
import com.dbutils.ArrayListBound;
import com.explain.HexDecoding;
import com.explain.NCTranslate;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.programming.Config;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;
import wifiProtocol.WifiReadDataFormat;
public class ledRunnable implements Runnable{

	private WifiReadDataFormat formatReadMessage=new WifiReadDataFormat(0x10007034,32);//0x10007053
	private byte[] getData;
	private Activity targetActivity;
    //private  SharedPreferences sPinfo;
	private boolean destroyPositionQueryFlag=false;
	private boolean selfDestroy_flag=true;
	private boolean chatlistener_flag=true;
	//private Button wifiled;
	//private Button zdled;
   /* public AlarmQueryRunnable(Activity targetActivity){
    	//System.out.println("警报线程开始");
    	this.targetActivity=targetActivity;
    	//WifiSetting_Info.almPinfo=targetActivity.getSharedPreferences("spinfo",0);
    	//WifiSetting_Info.almPinfo.edit().putString("spinfo","").commit();
    }*/
    
   /* public ledRunnable(Activity targetActivity,Button wifiled){
    	this.targetActivity=targetActivity;
    	this.wifiled=wifiled;
    	//this.zdled=zdled;
    }*/
    public ledRunnable(Activity targetActivity){
    	this.targetActivity=targetActivity;
 
    	//this.zdled=zdled;
    }
	//销毁该线程
	public void destroy() {
		//System.out.println("警报线程关闭");
		destroyPositionQueryFlag=true;
		selfDestroy_flag=false;
		chatlistener_flag=false;
	}

	/** 读取伺服参数时的通信线程收到信息时的处理函数. */
	private final ChatListener ReadDataFeedback = new ChatListener() {
		@Override
		public void onChat(byte[] message) {
			try{
			if(!chatlistener_flag)
				return;
			//返回的标志位STS1处的判断以及和校验
			formatReadMessage.backMessageCheck(message);
			if(!formatReadMessage.finishStatus()){
				WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
			
			}else {
				//发送正确且完成
				//处理返回的数据
				getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(message, 8, getData, 0, formatReadMessage.getLength());
				//对所收集到到的数据进行处理
				//HexDecoding.printHexString("警报信息", getData);
				Runnable infoShowRunnable=new Runnable() {
					public void run() {
						if(((int)((getData[0]>>5)&0x01))==1){
							WifiSetting_Info.ydfgFlag[0]=1;
					    }else{
					    	WifiSetting_Info.ydfgFlag[0]=0;
					    }
						if(((int)((getData[2]>>5)&0x01))==1){
							WifiSetting_Info.ydfgFlag[1]=1;
					    }else{
					    	WifiSetting_Info.ydfgFlag[1]=0;
					    }
						if(((int)((getData[4]>>5)&0x01))==1){
							WifiSetting_Info.ydfgFlag[2]=1;
					    }else{
					    	WifiSetting_Info.ydfgFlag[2]=0;
					    }
						if(((int)((getData[6]>>5)&0x01))==1){
							WifiSetting_Info.ydfgFlag[3]=1;
					    }else{
					    	WifiSetting_Info.ydfgFlag[3]=0;
					    }
						if(((int)((getData[8]>>5)&0x01))==1){
							WifiSetting_Info.ydfgFlag[4]=1;
					    }else{
					    	WifiSetting_Info.ydfgFlag[4]=0;
					    }
//						if(TR_Main_Activity.zd_led!=null&&TR_Main_Activity.alarm_led!=null){
							if(((int)((getData[31]>>2)&0x01))==1){
								//TR_Main_Activity.zd_led.setBackgroundDrawable(TR_Main_Activity.drawablezdled);
								if(!Config.AutoState)
								{
									Config.AutoState = true;
									Intent intent = new Intent();
									intent.setAction("updateBtnBg");
									intent.putExtra("button", "auto");
									targetActivity.sendBroadcast(intent);
								}
								
						    }else{
						    	//TR_Main_Activity.zd_led.setBackgroundDrawable(TR_Main_Activity.drawablenoled);
						    	if(Config.AutoState){
							    	Config.AutoState = false;
							    	Intent intent = new Intent();
									intent.setAction("updateBtnBg");
									intent.putExtra("button", "auto");
							    	targetActivity.sendBroadcast(intent);
						    	}
						    	
						    }
							
							if(((int)((getData[31]>>4)&0x01))==1){
								WifiSetting_Info.alarmFlag=1;
								//TR_Main_Activity.alarm_led.setBackgroundDrawable(TR_Main_Activity.drawablealarmled);
						    }else{
						    	WifiSetting_Info.alarmFlag=0;
						    	//TR_Main_Activity.alarm_led.setBackgroundDrawable(TR_Main_Activity.drawablenoled);
						    }
//						}
						
					}
				};
				targetActivity.runOnUiThread(infoShowRunnable);
			}} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!destroyPositionQueryFlag) {
			if(WifiSetting_Info.LOCK == null)
				WifiSetting_Info.LOCK = new Object();
			synchronized(WifiSetting_Info.LOCK)
			{
					WifiSetting_Info.LOCK.notify();

					if (WifiSetting_Info.blockFlag&&selfDestroy_flag&&WifiSetting_Info.mClient!=null) {
						try {

							//System.out.println("警报线程。。。");
							Thread.sleep(350);	
							Log.d("mpeng","run~~~~ led~~~~~~~~~~");
							WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
					
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					 try {				
						 WifiSetting_Info.LOCK.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
			
			}
	}

	}

}

