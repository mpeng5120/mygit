package wifiRunnablesAndChatlistener;

import java.util.ArrayList;
import java.util.HashMap;

import wifiProtocol.WifiReadDataFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dataInAddress.AddressPublic;
import com.dataInAddress.Define;
import com.dbutils.ArrayListBound;
import com.explain.HexDecoding;
import com.tr.R;
import com.tr.crash.CrashApplication;
import com.tr.main.Sentence;
import com.tr.main.TR_Main_Activity;
import com.tr.programming.Config;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

/**
 * 当前位置点的监视线程
 * @author 李婷婷
 *
 */
public class posccalmQueryRunnable implements Runnable{

	//暂时只要前五轴信息
	private WifiReadDataFormat formatReadMessage=new WifiReadDataFormat(0x1000B200, 112);
	private byte[] getData;
	private boolean destroyPositionQueryFlag=false;
	private boolean selfDestroy_flag=true;
	private boolean chatlistener_flag=true;
	
	private Activity targetActivity;
	
	private TextView X; //走行
	private TextView Y; //制品前后
	private TextView Z; //料道前后
	private TextView Ys; //制品上下
	private TextView Zs; //料道上下
	private Button movingled;//移动中指示灯
	
	private TextView Cnt1; //取出次数
	private TextView Cnt2; //不良品次数
	
	 private String[] info=new String[]{"","","","","","","","","",""};
	    private String infoall="";
	    private int count=0;
	    private int showcount=0;
		ArrayList<HashMap<String, Object>> list_alarmzb= ArrayListBound.getAlarmzbListData();
		//private Button wifiled;
	//确保一次只能存在一个
	public static boolean existFlag=false;
	private long beforetime;
	private long aftertime;
	/***
	 * 主界面的位置显示
	 * @param targetActivity
	 * @param X
	 * @param Y
	 * @param Z
	 * @param Ys
	 * @param Zs
	 */

	public posccalmQueryRunnable(Activity targetActivity, View X, View Y, View Z, View Ys, View Zs,Button moving_led
			                    ,View Cnt1, View Cnt2) {
		
		existFlag=true;
		this.targetActivity=targetActivity;
			this.X=(TextView)X;
			this.Y=(TextView)Y;
			this.Z=(TextView)Z;
			this.Ys=(TextView)Ys;
			this.Zs=(TextView)Zs;
			this.movingled=moving_led;
			
			this.Cnt1=(TextView)Cnt1;
	    	this.Cnt2=(TextView)Cnt2;

			//this.wifiled=wifiled;
		
	}

	public posccalmQueryRunnable(Activity targetActivity) {
		
		existFlag=true;
		this.targetActivity=targetActivity;
			this.X=  null;
			this.Y=  null;
			this.Z=  null;
			this.Ys= null;
			this.Zs= null;
			this.movingled=null;
			
			this.Cnt1=null;
	    	this.Cnt2=null;

			//this.wifiled=wifiled;
		
	}
	/**
	 * 获取当前位置点信息
	 * @return
	 */
	public String[] getCurrentPos() {
		
		String[] temp = new String[5];
		temp[0]=X.getText().toString();
		temp[1]=Y.getText().toString();
		temp[2]=Z.getText().toString();
		temp[3]=Ys.getText().toString();
		temp[4]=Zs.getText().toString();
		return temp;
	}

	//销毁该线程
	public void destroy() {
		existFlag=false;
		destroyPositionQueryFlag=true;
		selfDestroy_flag=false;
		chatlistener_flag=false;
	}

	public byte[] getData() {
		return getData;
	}
	
	
	/***
	 * 读取伺服参数时的通信线程收到信息时的处理函数. 
	 */
	private final ChatListener ReadDataFeedback = new ChatListener() {
		@Override
		public void onChat(byte[] message) {
			if(!chatlistener_flag)
				return;
			//返回的标志位STS1处的判断以及和校验
			//formatReadMessage.backMessageCheck(message);
			formatReadMessage.backMessageCheck(message);
			if(!formatReadMessage.finishStatus()){
				try {
					Thread.sleep(20);
					WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				try{
				//发送正确且完成
				//处理返回的数据	
				//getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				//System.arraycopy(message, 8, getData, 0, formatReadMessage.getLength());
				//对所收集到到的数据进行处理
					info=new String[]{"","","","","","","","","",""};
					infoall="";
					count=0;
					showcount=0;
				getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(message, 8, getData, 0, formatReadMessage.getLength());
				//
//				position				
//				alarm
//				counter
//				LED
//				周期
//				KEYCODE
				
					if(getData[108]!=0)
					{
						byte[] keyCode = new byte[2];
						System.arraycopy(getData, 108, keyCode, 0, 2);						
						Intent intent = new Intent();
						intent.setAction("KeyMsg");
						intent.putExtra("keycode", keyCode);					
						CrashApplication.getInstance().sendBroadcast(intent);
					}	
					if(X==null&&Y==null&&Z==null)
						return;
				aftertime=System.currentTimeMillis();				
				Runnable pointShowRunnable=new Runnable() {
					public void run() {
						info=new String[]{"","","","","","","","","",""};
						infoall="";
						count=0;
						showcount=0;
						String x=String.valueOf(((Double.valueOf(String.valueOf(HexDecoding.Array4Toint(getData,0))))/100));
						String y=String.valueOf(((Double.valueOf(String.valueOf(HexDecoding.Array4Toint(getData,4))))/100));
						String z=String.valueOf(((Double.valueOf(String.valueOf(HexDecoding.Array4Toint(getData,2*4))))/100));
						String ys=String.valueOf(((Double.valueOf(String.valueOf(HexDecoding.Array4Toint(getData,3*4))))/100));
						String zs=String.valueOf(((Double.valueOf(String.valueOf(HexDecoding.Array4Toint(getData,4*4))))/100));
						if(movingled!=null){
						if(!X.getHint().toString().trim().equals(x.trim())||!Y.getHint().toString().trim().equals(y.trim())||
								!Z.getHint().toString().trim().equals(z.trim())||!Ys.getHint().toString().trim().equals(ys.trim())||
								!Zs.getHint().toString().trim().equals(zs.trim())){
							if(!(X.getHint().toString().trim().equals("*****.*"))){
//								targetActivity.sendBroadcast(intent);
								if(!Config.MoveState){
								Config.MoveState = true;
								Intent intent = new Intent();
								intent.setAction("updateBtnBg");
								intent.putExtra("button", "move");
						    	targetActivity.sendBroadcast(intent);
								}
//								movingled.setBackgroundDrawable(TR_Main_Activity.drawablemovingled);
							}
						}else{
//								movingled.setBackgroundDrawable(TR_Main_Activity.drawablenoled);
//								targetActivity.sendBroadcast(intent);
								if(Config.MoveState){
								Config.MoveState = false;
								Intent intent = new Intent();
								intent.setAction("updateBtnBg");
								intent.putExtra("button", "move");
						    	targetActivity.sendBroadcast(intent);
								}
						}
						}
						    X.setHint(x);
							Y.setHint(y);
							Z.setHint(z);
							Ys.setHint(ys);
							Zs.setHint(zs);
						
						if(WifiSetting_Info.ydfgFlag[0]==1){
						    X.setText(x);
						}else{
						    X.setText("*****.*");
						}
							if(WifiSetting_Info.ydfgFlag[1]==1){
								Y.setText(y);
							}else{
								Y.setText("*****.*");
							}
							
							if(WifiSetting_Info.ydfgFlag[2]==1){
								Z.setText(z);
							}else{
								Z.setText("*****.*");
							}
							if(WifiSetting_Info.ydfgFlag[3]==1){
								Ys.setText(ys);
							}else{
								Ys.setText("*****.*");
							}
							if(WifiSetting_Info.ydfgFlag[4]==1){
								Zs.setText(zs);
							}else{
								Zs.setText("*****.*");
							}
						
					
							Cnt1.setText(String.valueOf((String.valueOf(HexDecoding.Array4Toint(getData,32+0)))));
							Cnt2.setText(String.valueOf((String.valueOf(HexDecoding.Array4Toint(getData,32+4)))));
					
						if(WifiSetting_Info.alarmFlag==1){
//							TR_Main_Activity.alarm_led.setBackgroundDrawable(TR_Main_Activity.drawablealarmled);
							if(!Config.AlarmState){
								Config.AlarmState = true;
								Intent intent = new Intent();
								intent.setAction("updateBtnBg");
								intent.putExtra("button", "alarm");
						    	targetActivity.sendBroadcast(intent);
							}
						if(HexDecoding.Array4Toint(getData,72+0)!=0){
					       info[0]="动作警报   "+HexDecoding.Array4Toint(getData,72+0);
						   infoall+=info[0];
						   if(!info[0].equals(WifiSetting_Info.almMSGs[0]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,72+4)!=0){
						   info[1]="系统警报   "+HexDecoding.Array4Toint(getData,72+4);
						   infoall+=info[1];
						   if(!info[1].equals(WifiSetting_Info.almMSGs[1]))count++;}
					 
						   if(HexDecoding.Array4Toint(getData,72+8)!=0){
						   info[2]="伺服警报一  "+HexDecoding.Array4Toint(getData,72+8);
						   infoall+=info[2];
						   if(!info[2].equals(WifiSetting_Info.almMSGs[2]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,72+12)!=0){
						   info[3]="伺服警报二  "+HexDecoding.Array4Toint(getData,72+12);
						   infoall+=info[3];
						   if(!info[3].equals(WifiSetting_Info.almMSGs[3]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,72+16)!=0){
						   info[4]="伺服警报三  "+HexDecoding.Array4Toint(getData,72+16);
						   infoall+=info[4];
						   if(!info[4].equals(WifiSetting_Info.almMSGs[4]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,72+20)!=0){
						   info[5]="伺服警报四  "+HexDecoding.Array4Toint(getData,72+20);
						   infoall+=info[5];
						   if(!info[5].equals(WifiSetting_Info.almMSGs[5]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,72+24)!=0){
						   info[6]="伺服警报五  "+HexDecoding.Array4Toint(getData,72+24);
						   infoall+=info[6];
						   if(!info[6].equals(WifiSetting_Info.almMSGs[6]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,72+28)!=0){
						   info[7]="伺服警报六  "+HexDecoding.Array4Toint(getData,72+28);
						   infoall+=info[7];
						   if(!info[7].equals(WifiSetting_Info.almMSGs[7]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,72+32)!=0){
						   info[8]="伺服警报七  "+HexDecoding.Array4Toint(getData,72+32);
						   infoall+=info[8];
						   if(!info[8].equals(WifiSetting_Info.almMSGs[8]))count++;}
						   
						   if(HexDecoding.Array4Toint(getData,72+36)!=0){
						   info[9]="伺服警报八  "+HexDecoding.Array4Toint(getData,72+36);
						   infoall+=info[9];
						   if(!info[9].equals(WifiSetting_Info.almMSGs[9]))count++;}
						   showcount=count;
						   
					   //System.out.println("比较   almMSG="+WifiSetting_Info.almMSG+"   infoall="+infoall);
					   if(!WifiSetting_Info.almMSG.equals(infoall)){
						  try{
						   TR_Main_Activity.lst.clear();
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
				 							
												Sentence sen=new Sentence((count-showcount)*4,info[j1]);
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
												}
												
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
												Sentence sen=new Sentence((count-showcount)*4,info[j1]);
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
												}
												
				 							new AlertDialog.Builder(targetActivity).setTitle("提示("+(showcount--)+"/"+count+")")
				 						   .setMessage(info[j1]+"\n"+list_alarmzb.get(i1).get("alarmzbName").toString().trim()
					 								+"\n"+list_alarmzb.get(i1).get("alarmzbmsg").toString().trim())
				 						   .setPositiveButton("知道了", null).show();
				 						
				 						break;
				 					}
							      }
							}
							
		 				}
					      for(int i=0;i<TR_Main_Activity.lst.size();i++){
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
							TR_Main_Activity.mSampleView.setList(TR_Main_Activity.lst);
					   } catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					   }
					   
					   WifiSetting_Info.almMSG=infoall;
					   for(int i=0;i<10;i++){
						   WifiSetting_Info.almMSGs[i]=info[i];  
					   }
					   
					}else{
						TR_Main_Activity.lst.clear();
					    Sentence sen=new Sentence(0,"无警报信息");
						TR_Main_Activity.lst.add(0, sen);
						TR_Main_Activity.mSampleView.setList(TR_Main_Activity.lst);
//						TR_Main_Activity.alarm_led.setBackgroundDrawable(TR_Main_Activity.drawablenoled);
						if(Config.AlarmState){
							Config.AlarmState = false;
							Intent intent = new Intent();
							intent.setAction("updateBtnBg");
							intent.putExtra("button", "alarm");
					    	targetActivity.sendBroadcast(intent);
						}
					}
					}
				};
				targetActivity.runOnUiThread(pointShowRunnable);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				if (WifiSetting_Info.blockFlag&&selfDestroy_flag&&WifiSetting_Info.mClient!=null) {
					try {
						Thread.sleep(200);
						if(WifiSetting_Info.mClient!=null)
							WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
					    
					} catch (Exception e) {
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
