package wifiRunnablesAndChatlistener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.tr.main.Sentence;
import com.tr.main.TR_Main_Activity;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

import wifiProtocol.WifiReadDataFormat;

public class AlarmQueryRunnable implements Runnable{

	private WifiReadDataFormat formatReadMessage=new WifiReadDataFormat(0x1000B248,40);
	private byte[] getData;
	private Activity targetActivity;
    private String[] info=new String[]{"","","","","","","","","",""};
    private String infoall="";
    private int count=0;
    private int showcount=0;
    //private  SharedPreferences sPinfo;
	private boolean destroyPositionQueryFlag=false;
	private boolean selfDestroy_flag=true;
	private boolean chatlistener_flag=true;
	ArrayList<HashMap<String, Object>> list_alarmzb= ArrayListBound.getAlarmzbListData();
	//private Button wifiled;
   /* public AlarmQueryRunnable(Activity targetActivity){
    	//System.out.println("警报线程开始");
    	this.targetActivity=targetActivity;
    	//WifiSetting_Info.almPinfo=targetActivity.getSharedPreferences("spinfo",0);
    	//WifiSetting_Info.almPinfo.edit().putString("spinfo","").commit();
    }*/
    
    public AlarmQueryRunnable(Activity targetActivity){
    	this.targetActivity=targetActivity;
    	//this.wifiled=wifiled;
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
			if(!chatlistener_flag)
				return;
			//返回的标志位STS1处的判断以及和校验
			formatReadMessage.backMessageCheck(message);
			if(!formatReadMessage.finishStatus()){
				WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
			
			}else {
				//发送正确且完成
				//处理返回的数据
				 //System.out.println("111111111almMSG="+WifiSetting_Info.almMSG+"   infoall="+infoall);
				info=new String[]{"","","","","","","","","",""};
				infoall="";
				count=0;
				showcount=0;
				getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(message, 8, getData, 0, formatReadMessage.getLength());
				//对所收集到到的数据进行处理
				//HexDecoding.printHexString("警报信息", getData);
				Runnable infoShowRunnable=new Runnable() {
					public void run() {
						//if(WifiSetting_Info.alarmFlag==1){
							//mpeng TR_Main_Activity.alarm_led.setBackgroundDrawable(TR_Main_Activity.drawablealarmled);
					     if(HexDecoding.Array4Toint(getData,0)!=0){
					       info[0]="动作警报   "+HexDecoding.Array4Toint(getData,0);
						   infoall+=info[0];
						   if(!info[0].equals(WifiSetting_Info.almMSGs[0]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,4)!=0){
						   info[1]="系统警报   "+HexDecoding.Array4Toint(getData,4);
						   infoall+=info[1];
						   if(!info[1].equals(WifiSetting_Info.almMSGs[1]))count++;}
					 
						   if(HexDecoding.Array4Toint(getData,8)!=0){
						   info[2]="伺服警报一  "+HexDecoding.Array4Toint(getData,8);
						   infoall+=info[2];
						   if(!info[2].equals(WifiSetting_Info.almMSGs[2]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,12)!=0){
						   info[3]="伺服警报二  "+HexDecoding.Array4Toint(getData,12);
						   infoall+=info[3];
						   if(!info[3].equals(WifiSetting_Info.almMSGs[3]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,16)!=0){
						   info[4]="伺服警报三  "+HexDecoding.Array4Toint(getData,16);
						   infoall+=info[4];
						   if(!info[4].equals(WifiSetting_Info.almMSGs[4]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,20)!=0){
						   info[5]="伺服警报四  "+HexDecoding.Array4Toint(getData,20);
						   infoall+=info[5];
						   if(!info[5].equals(WifiSetting_Info.almMSGs[5]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,24)!=0){
						   info[6]="伺服警报五  "+HexDecoding.Array4Toint(getData,24);
						   infoall+=info[6];
						   if(!info[6].equals(WifiSetting_Info.almMSGs[6]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,28)!=0){
						   info[7]="伺服警报六  "+HexDecoding.Array4Toint(getData,28);
						   infoall+=info[7];
						   if(!info[7].equals(WifiSetting_Info.almMSGs[7]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,32)!=0){
						   info[8]="伺服警报七  "+HexDecoding.Array4Toint(getData,32);
						   infoall+=info[8];
						   if(!info[8].equals(WifiSetting_Info.almMSGs[8]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,36)!=0){
						   info[9]="伺服警报八  "+HexDecoding.Array4Toint(getData,36);
						   infoall+=info[9];
						   if(!info[9].equals(WifiSetting_Info.almMSGs[9]))count++;}
						   showcount=count;
						   
					   //System.out.println("比较   almMSG="+WifiSetting_Info.almMSG+"   infoall="+infoall);
					   if(!WifiSetting_Info.almMSG.equals(infoall)){
						  try{
						   //TR_Main_Activity.lst.clear();
						  System.out.println("不同  almMSG="+WifiSetting_Info.almMSG+"   infoall="+infoall);
					      for(int j1=0;j1<10;j1++){
							if(info[j1].equals("")||info[j1].equals(WifiSetting_Info.almMSGs[j1])){
								   continue;
							} 
							
							if(info[j1].substring(0,2).startsWith("动作")){
								for(int i1=0;i1<ArrayListBound.getDeviceAlarmListData().size();i1++){
									if(ArrayListBound.getDeviceAlarmListData().get(i1).get("symbolNameEditText").toString().trim().equals("")){
										continue;
									}
				 					if(String.valueOf(Integer.parseInt(ArrayListBound.getDeviceAlarmListData().get(i1).get("symbolNameEditText").toString().trim())).equals(info[j1].substring(7, info[j1].length()))){
				 						
				 							String notemsg=ArrayListBound.getDeviceAlarmListData().get(i1).get("noteEditText").toString().trim();
				 							
												/*Sentence sen=new Sentence((count-showcount)*4,info[j1]);
												TR_Main_Activity.lst.add((count-showcount)*4, sen);
												
												if(notemsg.length()<=10){
													sen=new Sentence((count-showcount)*4+1,notemsg);
													TR_Main_Activity.lst.add((count-showcount)*4+1, sen);
													
													sen=new Sentence((count-showcount)*4+2,"");
												    TR_Main_Activity.lst.add((count-showcount)*4+2, sen);
										    
												    sen=new Sentence((count-showcount)*4+3,"");
												    TR_Main_Activity.lst.add((count-showcount)*4+3, sen);	
												}else if(notemsg.length()<=20){
													sen=new Sentence((count-showcount)*4+1,notemsg.substring(0, 10));
													TR_Main_Activity.lst.add((count-showcount)*4+1, sen);
													
													sen=new Sentence((count-showcount)*4+2,notemsg.substring(10,notemsg.length()));
												    TR_Main_Activity.lst.add((count-showcount)*4+2, sen);
										    
												    sen=new Sentence((count-showcount)*4+3,"");
												    TR_Main_Activity.lst.add((count-showcount)*4+3, sen);
												}else{
													sen=new Sentence((count-showcount)*4+1,notemsg.substring(0, 10));
													TR_Main_Activity.lst.add((count-showcount)*4+1, sen);
													
													sen=new Sentence((count-showcount)*4+2,notemsg.substring(10,20));
												    TR_Main_Activity.lst.add((count-showcount)*4+2, sen);
										    
												    sen=new Sentence((count-showcount)*4+3,notemsg.substring(20,notemsg.length()));
												    TR_Main_Activity.lst.add((count-showcount)*4+3, sen);	
												}*/
												
				 							new AlertDialog.Builder(targetActivity).setTitle("提示("+(showcount--)+"/"+count+")")
				 						   .setMessage(info[j1]+"\n"+notemsg)
				 						   .setPositiveButton("知道了", null).show();
				 						
				 						break;
				 					}
							      }
							}else{
								for(int i1=0;i1<list_alarmzb.size();i1++){
									if(list_alarmzb.get(i1).get("alarmzbnum").toString().trim().equals(info[j1].substring(0,4)+info[j1].substring(7, info[j1].length()))){
										if(info[j1].contains("伺服")){
											info[j1]=info[j1].substring(0,2)+info[j1].substring(4,5)+info[j1].substring(2,4)+info[j1].substring(5, info[j1].length());
										}
												/*Sentence sen=new Sentence((count-showcount)*4,info[j1]);
												TR_Main_Activity.lst.add((count-showcount)*4, sen);
												
												sen=new Sentence((count-showcount)*4+1,list_alarmzb.get(i1).get("alarmzbName").toString().trim());
												TR_Main_Activity.lst.add((count-showcount)*4+1, sen);
												
												if(list_alarmzb.get(i1).get("alarmzbmsg").toString().trim().length()>10){
												    sen=new Sentence((count-showcount)*4+2,list_alarmzb.get(i1).get("alarmzbmsg").toString().trim().substring(0, 10));
												    TR_Main_Activity.lst.add((count-showcount)*4+2, sen);
										    
												    sen=new Sentence((count-showcount)*4+3,list_alarmzb.get(i1).get("alarmzbmsg").toString().trim().substring(10, list_alarmzb.get(i1).get("alarmzbmsg").toString().trim().length()));
												    TR_Main_Activity.lst.add((count-showcount)*4+3, sen);	
												}else{
													sen=new Sentence((count-showcount)*4+2,list_alarmzb.get(i1).get("alarmzbmsg").toString().trim());
													TR_Main_Activity.lst.add((count-showcount)*4+2, sen);
											    
													sen=new Sentence((count-showcount)*4+3,"");
													TR_Main_Activity.lst.add((count-showcount)*4+3, sen);	
												}*/
												
				 							new AlertDialog.Builder(targetActivity).setTitle("提示("+(showcount--)+"/"+count+")")
				 						   .setMessage(info[j1]+"\n"+list_alarmzb.get(i1).get("alarmzbName").toString().trim()
					 								+"\n"+list_alarmzb.get(i1).get("alarmzbmsg").toString().trim())
				 						   .setPositiveButton("知道了", null).show();
				 						
				 						break;
				 					}
							      }
							}
							
		 				}
					      /*for(int i=0;i<TR_Main_Activity.lst.size();i++){
								if(((Sentence)(TR_Main_Activity.lst.get(i))).getName().equals("无警报信息")){
									TR_Main_Activity.lst.remove(i);
									i--;
								}	
							}
							if(TR_Main_Activity.lst.size()==0){
								Sentence sen=new Sentence(0,"无警报信息");
								TR_Main_Activity.lst.add(0, sen);
							}
							//给View传递数据
							TR_Main_Activity.mSampleView.setList(TR_Main_Activity.lst);*/
					   } catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					   }
					   
					   WifiSetting_Info.almMSG=infoall;
					   for(int i=0;i<10;i++){
						   WifiSetting_Info.almMSGs[i]=info[i];  
					   }
					   
					/*}else{
						TR_Main_Activity.lst.clear();
					    Sentence sen=new Sentence(0,"无警报信息");
						TR_Main_Activity.lst.add(0, sen);
						TR_Main_Activity.mSampleView.setList(TR_Main_Activity.lst);
						TR_Main_Activity.alarm_led.setBackgroundDrawable(TR_Main_Activity.drawablenoled);
					}*/
				}
				
				
				};
				targetActivity.runOnUiThread(infoShowRunnable);
			}
		}
	};


	@Override
	public void run() {
		while (!destroyPositionQueryFlag) {
			
			if(WifiSetting_Info.LOCK == null)
				WifiSetting_Info.LOCK = new Object();
			synchronized(WifiSetting_Info.LOCK)
			{
				WifiSetting_Info.LOCK.notify();
			try {
				Log.d("mpeng","run~~~~ alarm~~~~~~~~~~");
				if(WifiSetting_Info.blockFlag&&selfDestroy_flag&&WifiSetting_Info.mClient!=null&&WifiSetting_Info.alarmFlag==1) {
					Thread.sleep(1100);					
					WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
				}
			
				}catch (Exception e){
				e.printStackTrace();
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
