package com.tr.programming;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.PlcDataQueryRunnable;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.WatchRunnable;

import com.dataInAddress.AddressPublic;
import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.dbutils.OpenFiles;
import com.dbutils.TRFileHandler;
import com.explain.HexDecoding;
import com.explain.NCTranslate;
import com.explain.TableToBinary;
import com.explain.TableTranslate;
import com.tr.ExitTR;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.main.VerticalSeekBar;
import com.tr.main.VerticalSeekBarListener;
import com.tr.maintainguide.TR_MaintainGuide_Activity;
import com.tr.newpragram.NewPragramActivity;
import com.tr.parameter_setting.TR_Parameter_Setting_Activity;
import com.wifiexchange.ChatListener;
import com.wifiexchange.Client;
import com.wifiexchange.ServerUtils;
import com.wifiexchange.WifiSetting_Info;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.method.NumberKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @ClassName: TR_Programming_Activity
 * @Description: 编程类界面
 * @author 李婷婷
 * @date 2013-4-11 上午9:19:17
 */

public class TR_Programming_Activity extends Activity {
	public static Menu menu;

	private  int[] alength;

	MenuItem menu_note;
	MenuItem menu_saveCache;
	MenuItem menu_download;
	MenuItem menu_watchStart;
	MenuItem menu_wantchStop;	
	MenuItem menu_lock;
	MenuItem menu_unlock;
	
	 private String ioallString="";
	 private byte[] iobyteArray=null;
	 public  static WatchRunnable watchRunnable;
	private boolean watchflag=false;
	private boolean newsaveflag = true;// 保存按钮判断是新的保存还是保存打开的
	private String foldername_opened = null;// 保存按钮判断打开的文件名以便保存
	//private AlarmQueryRunnable alarmQueryRunnable;
	public VerticalSeekBar seekbar;

	private String errorString;

	private boolean hasError;

	private SharedPreferences sharedPreference_password;
	public  static Boolean alreadyChecked_programmingPassword = false;
	private String password_programming;
	private static String title_lock_programming = "已锁定";

	/*******************************************************/
	// 读取数据时的等待提示
	private int[] overflag=new int[]{0,0,0,0,0,0,0,0};
	// 烧写程序的特殊性，暂时使用的一个迫不得已的标志位
	//private boolean dataflag = true;

	HashMap<Integer, Integer> PositionList=new HashMap<Integer, Integer>();
	private SendDataRunnable sendDataRunnable;
	private WifiReadDataFormat formatReadMessage;
	private  ChatListener DataFeedback ;
	//private FinishRunnable finishRunnable;
	private PlcDataQueryRunnable plcquery;
	private ArrayList<SendDataRunnable> sendDataRunnables=new ArrayList<SendDataRunnable>();
	/*******************************************************/

	public static int noteflag=0;
	/*******************************************************/

	//编程界面中F1和停止按钮的响应
	//private Button f1Button;
	//private ToggleButton btn_stop = null;
	private int m_count = 0;
	public static Button wifi_led;
	/*******************************************************/
	private  byte[] getData;
	private  WifiReadDataFormat formatReadusermode;
	private  SendDataRunnable sendDatausermodeRunnable;
	private  FinishRunnable sendDataFinishRunnable;
	private  WifiSendDataFormat formatSendMessage;
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("TR_Programming_Activity  onPause");
		if(watchRunnable!=null){
			try{
				//menu_watchStart.setVisible(true);
				//menu_wantchStop.setVisible(false);
				//menu_saveCache.setEnabled(true);
				//menu_download.setEnabled(true);
				watchRunnable.destroy();
				if (getFragmentManager().findFragmentByTag("NC_1") instanceof Fragments_NCedit1) {
					 Fragments_NCedit1.NCedit_Adapter.setSelectItem(-1);
				   Fragments_NCedit1.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_2") instanceof Fragments_NCedit2) {
					 Fragments_NCedit2.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit2.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_3") instanceof Fragments_NCedit3) {
					Fragments_NCedit3.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit3.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_4") instanceof Fragments_NCedit4) {
					Fragments_NCedit4.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit4.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_5") instanceof Fragments_NCedit5) {
					Fragments_NCedit5.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit5.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_6") instanceof Fragments_NCedit6) {
					Fragments_NCedit6.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit6.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_7") instanceof Fragments_NCedit7) {
					Fragments_NCedit7.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit7.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_8") instanceof Fragments_NCedit8) {
					Fragments_NCedit8.NCedit_Adapter.setSelectItem(-1);
					Fragments_NCedit8.NCedit_Adapter.notifyDataSetChanged();
				}
				 
				}catch(Exception e){
					e.printStackTrace();
				}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(WifiSetting_Info.mClient!=null){
			wifi_led.setVisibility(View.VISIBLE);
        }
		System.out.println("TR_Programming_Activity  onResume");
	}
	
	/**
	 * setContentView的lauout中调用class"Device_ListFragment.java"来实现选项卡的切换
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		super.onCreate(savedInstanceState);
		System.out.println("TR_Programming_Activity  onCreate");
		ExitTR.getInstance().addActivity(this);
		sharedPreference_password = getSharedPreferences("password",
				Context.MODE_PRIVATE);
		// password_programming=sharedPreference_password.getString("password_programming",
		// "");

		setContentView(R.layout.tr_programming);// lauout中调用class"Device_ListFragment.java"
		// 创建悬浮窗口
		seekbar = (VerticalSeekBar) findViewById(R.id.seekbar);
		if(seekbar==null){return;}
		seekbar.setMax(500);// 最大档位
		seekbar.initProgress();// 默认档位
		seekbar.setOnSeekBarChangeListener(new VerticalSeekBarListener(
				TR_Programming_Activity.this));// 可以把这些全局量放在一个文件里

		new ArrayListBound();

		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setSubtitle(title_lock_programming);

		// 直接读取缓存中文件显示
		/*if (firstOpen) {// 开机第一次进入该界面
			openAllData("cache",0,TR_Programming_Activity.this);
		}*/

		wifi_led=(Button)findViewById(R.id.wifi_led);
		if(wifi_led==null){return;}
		
		ImageButton btn_stop = (ImageButton) findViewById(R.id.stop);
		if(btn_stop==null){return;}
		ButtonListener b = new ButtonListener();           
		btn_stop.setOnClickListener(b);  
		btn_stop.setOnTouchListener(b);  
		
		ImageButton btn_f1 = (ImageButton) findViewById(R.id.f1);
		if(btn_f1==null){return;}
		btn_f1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openThisFileFromSD("programmingactivity.pdf");
				KeyCodeSend.send(6, TR_Programming_Activity.this);
			}
		});
		
		DataFeedback = new ChatListener() {
			@Override
			public void onChat(byte[] message) {

				//返回的标志位STS1处的判断以及和校验
				formatReadMessage.backMessageCheck(message);
				if(!formatReadMessage.finishStatus()){
					runOnUiThread(sendDataRunnable);			
				}else {
					//发送正确且完成
					//处理返回的数据	
					WifiSetting_Info.blockFlag = true;
					byte[] getData=new byte[formatReadMessage.getLength()];
					//获取返回的数据，从第八位开始拷贝数据
					System.arraycopy(message, 8, getData, 0, formatReadMessage.getLength());
				
							// TODO Auto-generated method stub
					/*		for(int i=0;i<formatReadMessage.getLength();i++){
								System.out.println("速度"+i+"的值："+getData[i]);
							}
							System.out.println("请系统输出速度的值："+HexDecoding.Array4Toint(getData));*/
							if(HexDecoding.Array2Toint(getData)==1){
								Constans.currentSeekbarValue=0;
							    seekbar.setProgress(0);
							}
							if(HexDecoding.Array2Toint(getData)==2){
								Constans.currentSeekbarValue=125;
								seekbar.setProgress(125);
								}
							if(HexDecoding.Array2Toint(getData)==3){
								Constans.currentSeekbarValue=250;
								seekbar.setProgress(250);
								}
							if(HexDecoding.Array2Toint(getData)==4){
								Constans.currentSeekbarValue=375;
								seekbar.setProgress(375);
								}
							if(HexDecoding.Array2Toint(getData)==5){
								Constans.currentSeekbarValue=500;
								seekbar.setProgress(500);
								}
						}
					}
			
		};
		
	}
	
	class ButtonListener implements OnClickListener, OnTouchListener{  
		   public boolean onTouch(View v, MotionEvent event) {  
		            if(v.getId() == R.id.stop){    
		                if(event.getAction() == MotionEvent.ACTION_DOWN){ 
		                	KeyCodeSend.send(999, TR_Programming_Activity.this);
		                }  
		            }  
		            return false;  
		        }

		@Override
		public void onClick(View v) {
		}  
		          
	}
	/**
	 * 
	* @Title: openThisFile 
	* @Description: 通过调用OpenFiles类返回的Intent，打开相应的文件;文件暂时都默认放在 rawPATH下
	* @param string    
	* @return void    
	* @throws
	 */
	public void openThisFileFromSD(String filename) {

		File currentPath = new File(Constans.rawPATH + filename);
		
		if (currentPath != null && currentPath.isFile()){// 存在该文件
			String fileName = currentPath.toString();
			Intent intent;
			try{
			if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingPdf))) {
				intent = OpenFiles.getPdfFileIntent(currentPath);
				startActivity(intent);
			} else {
				Toast.makeText(TR_Programming_Activity.this, "无法打开该文件，请安装相应的软件！",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
				// TODO: handle exception
			Toast.makeText(TR_Programming_Activity.this, "无法打开该文件，请安装相应的软件！",
					Toast.LENGTH_SHORT).show();
			}
		} else {// 不存在该文件
			Toast.makeText(TR_Programming_Activity.this, "对不起，不存在该文件！", Toast.LENGTH_SHORT)
					.show();
		}
	}
	/**
	 * 
	* @Title: checkEndsWithInStringArray 
	* @Description: 定义用于检查要打开的文件的后缀是否在遍历后缀数组中
	* @param checkItsEnd
	* @param fileEndings
	* @return    
	* @return boolean    
	* @throws
	 */
	private boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}
	/**
	 * 菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.programming_activity, menu);// 菜单
		try{
			menu_note= menu.findItem(R.id.menu_note);
			menu_saveCache= menu.findItem(R.id.menu_saveCache);
			menu_download= menu.findItem(R.id.menu_download);
			menu_watchStart=menu.findItem(R.id.menu_watchStart);
			menu_wantchStop=menu.findItem(R.id.menu_watchStop);	
			menu_lock = menu.findItem(R.id.menu_lock);
			menu_unlock = menu.findItem(R.id.menu_unlock);
		    }catch(Exception e){
			   e.printStackTrace();
		    }
		if (getFragmentManager().findFragmentByTag("NC_1") instanceof Fragments_NCedit1 ||
				getFragmentManager().findFragmentByTag("NC_2") instanceof Fragments_NCedit2 ||
				getFragmentManager().findFragmentByTag("NC_3") instanceof Fragments_NCedit3 ||
				getFragmentManager().findFragmentByTag("NC_4") instanceof Fragments_NCedit4 ||
				getFragmentManager().findFragmentByTag("NC_5") instanceof Fragments_NCedit5 ||
				getFragmentManager().findFragmentByTag("NC_6") instanceof Fragments_NCedit6 ||
				getFragmentManager().findFragmentByTag("NC_7") instanceof Fragments_NCedit7 ||
				getFragmentManager().findFragmentByTag("NC_8") instanceof Fragments_NCedit8 ||
				getFragmentManager().findFragmentByTag("Table_1") instanceof Fragments_Table1 ||
				getFragmentManager().findFragmentByTag("Table_2") instanceof Fragments_Table2 ||
				getFragmentManager().findFragmentByTag("Table_3") instanceof Fragments_Table3 ||
				getFragmentManager().findFragmentByTag("Table_4") instanceof Fragments_Table4 ||
				getFragmentManager().findFragmentByTag("Table_5") instanceof Fragments_Table5 ||
				getFragmentManager().findFragmentByTag("Table_6") instanceof Fragments_Table6 ||
				getFragmentManager().findFragmentByTag("Table_7") instanceof Fragments_Table7 ||
				getFragmentManager().findFragmentByTag("Table_8") instanceof Fragments_Table8) {
			menu_note.setVisible(true);
			menu_saveCache.setVisible(true);
			menu_download.setVisible(true);
			menu_watchStart.setVisible(true);
			menu_unlock.setVisible(true);
		}else{
			menu_note.setVisible(false);
			menu_saveCache.setVisible(true);
			menu_download.setVisible(false);
			menu_watchStart.setVisible(false);
			menu_unlock.setVisible(true);
		}
		menu_wantchStop.setVisible(false);
		menu_lock.setVisible(false);// 初次设置锁定菜单项为隐藏

		this.menu=menu;
		/******************************/
		return true;

	}
	private void getmessage(List<HashMap<String, Object>> message,int flag){
		Iterator<HashMap<String, Object>> its =message.iterator();
		if(flag==4){
			String name1 = "";
			String name2 = "";
			String name3 = "";
			try {
			while (its.hasNext()) {
				HashMap<String, Object> map = its.next();
				name1 = map.get("name1").toString().trim();
				name2 = map.get("name2").toString().trim();
				name3 = map.get("name3").toString().trim();
				int name2ads=0;
				int name3ads=0;
				if(name2.equals("")){
					name2ads=0xffff;
				}else{
					name2ads=TableToBinary.searchAddress(name2,false);
				}
				if(name3.equals("")){
					name3ads=0xffff;
				}else{
					name3ads=TableToBinary.searchAddress(name3,false);
					name3ads=name3ads+16*16;
				}
				
				byte[] name2adsbyte=HexDecoding.int2byteArray2(name2ads);
				byte[] name3adsbyte=HexDecoding.int2byteArray2(name3ads);
				
					String name2adsstring = new String(name2adsbyte,"GBK");
				    String name3adsstring = new String(name3adsbyte,"GBK");
				    
					if(name1.getBytes("gb2312").length==0){name1=name1+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==1){name1=name1+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==2){name1=name1+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==3){name1=name1+"\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==4){name1=name1+"\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==5){name1=name1+"\0\0\0\0\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==6){name1=name1+"\0\0\0\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==7){name1=name1+"\0\0\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==8){name1=name1+"\0\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==9){name1=name1+"\0\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==10){name1=name1+"\0\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==11){name1=name1+"\0\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==12){name1=name1+"\0\0\0\0";}
					else if(name1.getBytes("gb2312").length==13){name1=name1+"\0\0\0";}
					else if(name1.getBytes("gb2312").length==14){name1=name1+"\0\0";}
					else if(name1.getBytes("gb2312").length==15){name1=name1+"\0";}
					ioallString=ioallString+name1+name2adsstring+name3adsstring;
			}} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else if(flag==5){
			try{
			String[] alarmlist=new String[200];
			String symbolString ="";
			String noteString ="";
			while (its.hasNext()) {
				HashMap<String, Object> map = its.next();
				ioallString="";
				symbolString = map.get("symbolNameEditText")
						.toString().trim();
				noteString = map.get("noteEditText")
						.toString().trim();
				if(!symbolString.equals("")){
					int symbolint=Integer.parseInt(symbolString);
					symbolString="动作警报"+symbolint;
				//byte[] symbolbyte=null;
				//byte[] notebyte=null;
				
					//symbolbyte=symbolString.getBytes("gb2312");
					//notebyte=noteString.getBytes("gb2312");
					if(symbolString.getBytes("gb2312").length==9){symbolString=symbolString+"\0\0\0\0\0\0\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==10){symbolString=symbolString+"\0\0\0\0\0\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==11){symbolString=symbolString+"\0\0\0\0\0\0\0\0\0";}
			
				
				int allbyte=0;
				noteString=noteString.replace("\0", "");
				String notebf=noteString;
				//System.out.println("notebf==="+notebf.length());
				int note1flag=0;
				for(int n=0;n<noteString.length()-1;n++){
					char ch=noteString.charAt(n);
					String chstr=ch+"";
					//byte[] chbyte=null;
					
						//chbyte=chstr.getBytes("gb2312");
						allbyte=allbyte+chstr.getBytes("gb2312").length;
					
					
					//System.out.println("chstr="+chstr);
					//System.out.println("allbyte="+allbyte);
					if(allbyte==19||allbyte==39||allbyte==59){
						
						 notebf=notebf.substring(0, n+1+note1flag)+"\0"+notebf.substring(n+1+note1flag, notebf.length());
					 
						allbyte=0;
						note1flag++;
					}
				}
				noteString=notebf;
			/*	try {
					notebyte=noteString.getBytes("gb2312");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//HexDecoding.printHexString("动作警报", notebyte);
				for(int j=0;j<=60;j++){
				   if(noteString.getBytes("gb2312").length==j){
					for(int k=1;k<=60-j;k++){noteString=noteString+"\0";}
				   }
				}
	            
				ioallString=ioallString+symbolString+noteString;
				alarmlist[symbolint-1]=ioallString;
				}
			}
			ioallString="";
			for(int m=0;m<200;m++){
				if(alarmlist[m]==null){
					alarmlist[m]="\0\0\0\0\0\0\0\0\0\0"+"\0\0\0\0\0\0\0\0\0\0"+"\0\0\0\0\0\0\0\0\0\0"+"\0\0\0\0\0\0\0\0\0\0"
							+"\0\0\0\0\0\0\0\0\0\0"+"\0\0\0\0\0\0\0\0\0\0"+"\0\0\0\0\0\0\0\0\0\0"+"\0\0\0\0\0\0\0\0\0\0";
				}
				ioallString=ioallString+alarmlist[m];
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		}else if(flag==3){
			String symbolString ="";
			String signalString ="";
			String[] ioallStrings=new String[210];
			try {
			while (its.hasNext()) {
				HashMap<String, Object> map = its.next();
				if(map.get("setItem").toString().trim().equals("1")){
				symbolString = map.get("symbolNameEditText")
						.toString().trim();
				signalString = map.get("signalNameEditText")
						.toString().trim();
					if(symbolString.getBytes("gb2312").length==0){symbolString=symbolString+"\0\0\0\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==1){symbolString=symbolString+"\0\0\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==2){symbolString=symbolString+"\0\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==3){symbolString=symbolString+"\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==4){symbolString=symbolString+"\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==5){symbolString=symbolString+"\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==6){symbolString=symbolString+"\0\0";}
					else if(symbolString.getBytes("gb2312").length==7){symbolString=symbolString+"\0";}
					
					if(signalString.getBytes("gb2312").length==0){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==1){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==2){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==3){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==4){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==5){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==6){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==7){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==8){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==9){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==10){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==11){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==12){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==13){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==14){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==15){signalString=signalString+"\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==16){signalString=signalString+"\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==17){signalString=signalString+"\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==18){signalString=signalString+"\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==19){signalString=signalString+"\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==20){signalString=signalString+"\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==21){signalString=signalString+"\0\0\0";}
					else if(signalString.getBytes("gb2312").length==22){signalString=signalString+"\0\0";}
					else if(signalString.getBytes("gb2312").length==23){signalString=signalString+"\0";}
					if(symbolString.startsWith("S")||symbolString.startsWith("s")){
						ioallStrings[Integer.parseInt(symbolString.substring(2,5))-1+200]=symbolString+signalString;
					}else if(symbolString.startsWith("F")||symbolString.startsWith("f")){
						ioallStrings[Integer.parseInt(symbolString.substring(2,5))-1+208]=symbolString+signalString;
					}else if(symbolString.startsWith("P")||symbolString.startsWith("p")){
						ioallStrings[Integer.parseInt(symbolString.substring(1,4))-1]=symbolString+signalString;
					}
					
					
			}
			}} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0;i<ioallStrings.length;i++){
				if(ioallStrings[i]!=null){
				   ioallString=ioallString+ioallStrings[i];
				}
			}
		
		}else{
			String symbolString ="";
			String signalString ="";
			try {
			while (its.hasNext()) {
				HashMap<String, Object> map = its.next();
				if((flag==1)||map.get("setItem").toString().trim().equals("1")){
				symbolString = map.get("symbolNameEditText")
						.toString().trim();
				signalString = map.get("signalNameEditText")
						.toString().trim();
				//byte[] symbolbyte=null;
				//byte[] signalbyte=null;
				
					//symbolbyte=symbolString.getBytes("gb2312");
					//signalbyte=signalString.getBytes("gb2312");
					if(symbolString.getBytes("gb2312").length==0){symbolString=symbolString+"\0\0\0\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==1){symbolString=symbolString+"\0\0\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==2){symbolString=symbolString+"\0\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==3){symbolString=symbolString+"\0\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==4){symbolString=symbolString+"\0\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==5){symbolString=symbolString+"\0\0\0";}
					else if(symbolString.getBytes("gb2312").length==6){symbolString=symbolString+"\0\0";}
					else if(symbolString.getBytes("gb2312").length==7){symbolString=symbolString+"\0";}
					
					if(signalString.getBytes("gb2312").length==0){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==1){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==2){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==3){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==4){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==5){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==6){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==7){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==8){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==9){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==10){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==11){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==12){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==13){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==14){signalString=signalString+"\0\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==15){signalString=signalString+"\0\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==16){signalString=signalString+"\0\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==17){signalString=signalString+"\0\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==18){signalString=signalString+"\0\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==19){signalString=signalString+"\0\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==20){signalString=signalString+"\0\0\0\0";}
					else if(signalString.getBytes("gb2312").length==21){signalString=signalString+"\0\0\0";}
					else if(signalString.getBytes("gb2312").length==22){signalString=signalString+"\0\0";}
					else if(signalString.getBytes("gb2312").length==23){signalString=signalString+"\0";}
				
					ioallString=ioallString+symbolString+signalString;
			}
			}} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void addmessage(String date){
		long beforeTime=System.currentTimeMillis();
		ioallString="";
		long beforeTimeqian=System.currentTimeMillis();
        getmessage(ArrayListBound.getDeviceActualInputListData().subList(0, 32),1);
        getmessage(ArrayListBound.getDeviceActualInputListData().subList(32, 64),1);
        getmessage(ArrayListBound.getDeviceActualInputListData().subList(64, 96),1);
        getmessage(ArrayListBound.getDeviceActualInputListData().subList(96, 128),1);
        
        getmessage(ArrayListBound.getDeviceActualOutputListData().subList(0, 32),1);
        getmessage(ArrayListBound.getDeviceActualOutputListData().subList(32, 64),1);
        getmessage(ArrayListBound.getDeviceActualOutputListData().subList(64, 96),1);
        getmessage(ArrayListBound.getDeviceActualOutputListData().subList(96, 128),1);
    	long beforeTimehou=System.currentTimeMillis();
        System.out.println("实际输入、实际输出信息12345="+(beforeTimehou-beforeTimeqian));
        try {
        	iobyteArray=ioallString.getBytes("gb2312");System.out.println("0x080E0000="+iobyteArray.length);
		    sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(iobyteArray,0x080E0000), TR_Programming_Activity.this);
		    sendDataRunnables.add(sendDataRunnable);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e1.printStackTrace();
		}
        long beforeTime1=System.currentTimeMillis();
        System.out.println("实际输入、实际输出信息="+(beforeTime1-beforeTime));
        
        ioallString="";
        getmessage(ArrayListBound.getDeviceMiddleInputListData().subList(0, 28),1);
        try {
			iobyteArray=ioallString.getBytes("gb2312");System.out.println("0x080EF000="+iobyteArray.length);
		    sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(iobyteArray,0x080EF000), TR_Programming_Activity.this);
		    sendDataRunnables.add(sendDataRunnable);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e1.printStackTrace();
		}
        
        ioallString="";
        getmessage(ArrayListBound.getDeviceTimerListData(),2);
        try {
			iobyteArray=ioallString.getBytes("gb2312");System.out.println("0x080E2000="+iobyteArray.length);
		    sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(iobyteArray,0x080E2000), TR_Programming_Activity.this);
		    sendDataRunnables.add(sendDataRunnable);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e1.printStackTrace();
		}
        long beforeTime2=System.currentTimeMillis();
        System.out.println("定时器信息="+(beforeTime2-beforeTime1));
        ioallString="";
        getmessage(ArrayListBound.getDeviceCounterListData(),2);
        try {
			iobyteArray=ioallString.getBytes("gb2312");System.out.println("0x080E2800="+iobyteArray.length);
		    sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(iobyteArray,0x080E2800), TR_Programming_Activity.this);
		    sendDataRunnables.add(sendDataRunnable);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e1.printStackTrace();
		}
        long beforeTime3=System.currentTimeMillis();
        System.out.println("计数器信息="+(beforeTime3-beforeTime2));
        ioallString="";
        getmessage(ArrayListBound.getDevicePositionListData(),3);
        try {
			iobyteArray=ioallString.getBytes("gb2312");System.out.println("0x080E3000="+iobyteArray.length);
		    sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(iobyteArray,0x080E3000), TR_Programming_Activity.this);
		    sendDataRunnables.add(sendDataRunnable);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e1.printStackTrace();
		}
        long beforeTime4=System.currentTimeMillis();
        System.out.println("位置信息="+(beforeTime4-beforeTime3));
        ioallString="";
        getmessage(ArrayListBound.getDeviceOptionalListData(),4);
        try {
			iobyteArray=ioallString.getBytes("gb2312");System.out.println("0x080E5000="+iobyteArray.length);
		    sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(iobyteArray,0x080E5000), TR_Programming_Activity.this);
		    sendDataRunnables.add(sendDataRunnable);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e1.printStackTrace();
		}
        long beforeTime5=System.currentTimeMillis();
        System.out.println("选件操作信息="+(beforeTime5-beforeTime4));
        //String msg=Constans.mouldData.readAlarmOutFile(Constans.trPATH+"警报信息"+File.separator+"servoalarm.trt");
		byte[] byteArray=null;
		try {
			Constans.alarmData.createFolder(Constans.trPATH,Constans.alarmDataFolder).createFile("servoalarmz.trt");
			byteArray=Constans.alarmData.readFileToByte("servoalarmz.trt");
			//byteArray = msg.getBytes("gb2312");System.out.println("0x080E6000="+byteArray.length);
			sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(byteArray, 0x080E6000), TR_Programming_Activity.this);
		    sendDataRunnables.add(sendDataRunnable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e.printStackTrace();
		}
		long beforeTime6=System.currentTimeMillis();
        System.out.println("伺服警报="+(beforeTime6-beforeTime5));
		//msg=Constans.mouldData.readAlarmOutFile(Constans.trPATH+"警报信息"+File.separator+"sysalarm.trt");
		byteArray=null;
		try {
			Constans.alarmData.createFolder(Constans.trPATH,Constans.alarmDataFolder).createFile("sysalarmz.trt");
			byteArray=Constans.alarmData.readFileToByte("sysalarmz.trt");
			//byteArray = msg.getBytes("gb2312");System.out.println("0x080E7000="+byteArray.length);
			System.out.println("byteArraywifi打印数据===="+byteArray);
			sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(byteArray, 0x080E7000), TR_Programming_Activity.this);
			sendDataRunnables.add(sendDataRunnable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e.printStackTrace();
		}
		long beforeTime7=System.currentTimeMillis();
       System.out.println("系统警报="+(beforeTime7-beforeTime6));
		ioallString="";
        getmessage(ArrayListBound.getDeviceAlarmListData(),5);
        try {
			iobyteArray=ioallString.getBytes("gb2312");System.out.println("0x080EB000="+iobyteArray.length);
		    sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(iobyteArray,0x080EB000), TR_Programming_Activity.this);
		    sendDataRunnables.add(sendDataRunnable);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e1.printStackTrace();
		}
		/*msg=Constans.mouldData.readAlarmOutFile(Constans.trPATH+"警报信息"+File.separator+"zsfb.trt");
		byteArray=null;
		try {
			byteArray = msg.getBytes("gb2312");
			sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(byteArray, 0x080EE000), TR_Programming_Activity.this);
			sendDataRunnables.add(sendDataRunnable);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			if(WifiSetting_Info.progressDialog!=null){
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
			e.printStackTrace();
		}*/
        long beforeTime8=System.currentTimeMillis();
        System.out.println("动作警报="+(beforeTime8-beforeTime7));
	 
        byte[] getData=new byte[48];
        Iterator<HashMap<String, Object>> it1 =ArrayListBound.getDeviceTimerListData().iterator();
        while (it1.hasNext()) {
			HashMap<String, Object> map = it1.next();
			int address=Integer.valueOf(map.get("addressText").toString());
			int item=Integer.valueOf(map.get("setItem").toString());
			getData[address/8]=(byte)(getData[address/8]|(item<<(address%8)));
		}
        Iterator<HashMap<String, Object>> it2 =ArrayListBound.getDeviceCounterListData().iterator();
        while (it2.hasNext()) {
			HashMap<String, Object> map = it2.next();
			int address=Integer.valueOf(map.get("addressText").toString());
			int item=Integer.valueOf(map.get("setItem").toString());
			getData[(64+address)/8]=(byte)(getData[(64+address)/8]|(item<<((64+address)%8)));
		}
        Iterator<HashMap<String, Object>> it3 =ArrayListBound.getDevicePositionListData().iterator();
        while (it3.hasNext()) {
        	HashMap<String, Object> map = it3.next();
			String posname=map.get("symbolNameEditText").toString().trim();
			if(posname.contains("SP")){
				int item=Integer.valueOf(map.get("setItem").toString());
				int valueint=Integer.parseInt(posname.substring(2))-1;
				getData[(64+64+212+valueint)/8]=(byte)(getData[(64+64+212+valueint)/8]|(item<<((64+64+212+valueint)%8)));
			}else if(posname.contains("FP")){
				int item=Integer.valueOf(map.get("setItem").toString());
				int valueint=Integer.parseInt(posname.substring(2))-1;
				getData[(64+64+222+valueint)/8]=(byte)(getData[(64+64+222+valueint)/8]|(item<<((64+64+222+valueint)%8)));
			}else if(posname.contains("P")){
				int item=Integer.valueOf(map.get("setItem").toString());
				int valueint=Integer.parseInt(posname.substring(1))-1;
				getData[(64+64+valueint)/8]=(byte)(getData[(64+64+valueint)/8]|(item<<((64+64+valueint)%8)));
			}
        }
        
    	byte[] getData1=new byte[28];
		String strNum = TR_Main_Activity.sharedPreference_openedDir.getString("dir_num","0");
		String strName = TR_Main_Activity.sharedPreference_openedDir.getString("dir_name", "");
		TR_Main_Activity.sharedPreference_openedDir.edit().putString("dir_time", date).commit();
		String strTime = date;
		try {
			if(strName.getBytes("gb2312").length==0){strName=strName+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==1){strName=strName+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==2){strName=strName+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==3){strName=strName+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==4){strName=strName+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==5){strName=strName+"\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==6){strName=strName+"\0\0\0\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==7){strName=strName+"\0\0\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==8){strName=strName+"\0\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==9){strName=strName+"\0\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==10){strName=strName+"\0\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==11){strName=strName+"\0\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==12){strName=strName+"\0\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==13){strName=strName+"\0\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==14){strName=strName+"\0\0\0\0";}
			else if(strName.getBytes("gb2312").length==15){strName=strName+"\0\0\0";}
			else if(strName.getBytes("gb2312").length==16){strName=strName+"\0\0";}
			else if(strName.getBytes("gb2312").length==17){strName=strName+"\0";}
			System.arraycopy(HexDecoding.int2byteArray2(Integer.parseInt(strNum)),0, getData1, 0,2);
			System.arraycopy(strName.getBytes("gb2312"),0, getData1, 2,18);
			String[] timeStrings =strTime.split("\\s+|:|-");
			for(int i=0;i<timeStrings.length;i++){
				if(i==0){
					System.arraycopy(HexDecoding.int2byte(Integer.parseInt(timeStrings[i])-2000),0, getData1, 20+i,1);
				}else{
					System.arraycopy(HexDecoding.int2byte(Integer.parseInt(timeStrings[i])),0, getData1, 20+i,1);
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] getData2=new byte[76];
		System.arraycopy(getData1,0, getData2, 0x00000000, 28);
		System.arraycopy(getData,0, getData2, 0x0000001C, 48);System.out.println("0x20000000="+getData2.length);
        sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(getData2,0x20000000), TR_Programming_Activity.this);
        sendDataRunnables.add(sendDataRunnable);
        long beforeTime9=System.currentTimeMillis();
        System.out.println("模具编号、名称、最后修改时间和模具数据="+(beforeTime9-beforeTime8));
		System.out.println("beforeTime9-beforeTime="+(beforeTime9-beforeTime));
		System.out.println("beforeTime9-WifiSetting_Info.wifiTimeOut="+(beforeTime9-WifiSetting_Info.wifiTimeOut));
	}
	
	private void fromsd(){
		if (WifiSetting_Info.mClient!=null) {
			try{
			sendDataRunnable=sendDataRunnables.get(0);
			sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable,new FinishRunnable(TR_Programming_Activity.this,backMessageTodo1)));
		    runOnUiThread(sendDataRunnable);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				if(WifiSetting_Info.progressDialog!=null){
	    			WifiSetting_Info.progressDialog.dismiss();
	    			WifiSetting_Info.progressDialog=null;
	    		}
				return;
			}
		
		}else{
			if(WifiSetting_Info.progressDialog!=null){
				overflag[5]=1;
				WifiSetting_Info.progressDialog.dismiss();
				WifiSetting_Info.progressDialog=null;
			}
		}
		 
	}
	private Runnable backMessageTodo1=new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 返回的标志位STS1处的判断以及和校验
			sendDataRunnables.remove(0);
			 if(sendDataRunnables.size()>0){
				 fromsd(); 
			 }
			 if(WifiSetting_Info.progressDialog!=null&&sendDataRunnables.size()==0){
				 overflag[0]=1;
				 WifiSetting_Info.progressDialog.dismiss();
				 WifiSetting_Info.progressDialog=null;
				 
			 }
		}
	};
	
	/*****
	 * wifi连接失败
	 */
	private Runnable errorRunnable=new Runnable() {
		@Override
		public void run() {
			WifiSetting_Info.blockFlag=false;
			Toast.makeText(TR_Programming_Activity.this,R.string.T_wificonnfail,Toast.LENGTH_LONG).show();
		}
	};
	
	/***
	 * 连接成功
	 */
	private Runnable successRunnable=new Runnable() {
		@Override
		public void run() {
			if(TR_Programming_Activity.wifi_led!=null){
	        	TR_Programming_Activity.wifi_led.setVisibility(View.VISIBLE);
	        }
			Toast.makeText(TR_Programming_Activity.this,R.string.T_wificonnsuccess,Toast.LENGTH_SHORT).show();
			formatReadMessage=new WifiReadDataFormat(AddressPublic.model_allspeed,2);
			try {
				sendDataRunnable = new SendDataRunnable(DataFeedback, formatReadMessage,
						TR_Programming_Activity.this);
				runOnUiThread(sendDataRunnable);
			} catch (Exception e) {
				Toast.makeText(TR_Programming_Activity.this, e.toString(), Toast.LENGTH_LONG);
			}
		}
		
	};
	private  Runnable stopDataFinishTodo2 = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReadusermode.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadusermode.getFinalData() != null)  
			{
			   System.arraycopy(formatReadusermode.getFinalData(), 0, getData, 0, formatReadusermode.getLength());
			   int zjz=(int)(getData[0]&0x0F);
			   getData=HexDecoding.int2byte((int)(zjz|0x10));
			   try{
			    formatSendMessage=new WifiSendDataFormat(getData, 0x200000AF);
                sendDataRunnable=new SendDataRunnable(formatSendMessage, TR_Programming_Activity.this);
				sendDataFinishRunnable=new FinishRunnable(TR_Programming_Activity.this);
				sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));
				runOnUiThread(sendDataRunnable);
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		    }
		}
	};
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// 响应每个菜单项(通过菜单项的ID)

		case R.id.menu_note:
			int index=0;
			if (getFragmentManager().findFragmentByTag("NC_1") instanceof Fragments_NCedit1){
				NCTranslate.noteList.clear();
				noteflag=1;
				try{
				alength=NCTranslate.beginTranslate(TR_Programming_Activity.this,
						getResources().getStringArray(R.array.IF2));
				new Fragments_NCedit1().showNote();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "NC程序存在错误",
							Toast.LENGTH_SHORT).show();
				}
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("NC_2") instanceof Fragments_NCedit2){
				NCTranslate.noteList.clear();
				noteflag=2;
				try{
				alength=NCTranslate.beginTranslate(TR_Programming_Activity.this,
						getResources().getStringArray(R.array.IF2));
				new Fragments_NCedit2().showNote();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "NC程序存在错误",
							Toast.LENGTH_SHORT).show();
				}
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("NC_3") instanceof Fragments_NCedit3){
				NCTranslate.noteList.clear();
				noteflag=3;
				try{
				alength=NCTranslate.beginTranslate(TR_Programming_Activity.this,
						getResources().getStringArray(R.array.IF2));
				new Fragments_NCedit3().showNote();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "NC程序存在错误",
							Toast.LENGTH_SHORT).show();
				}
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("NC_4") instanceof Fragments_NCedit4){
				NCTranslate.noteList.clear();
				noteflag=4;
				try{
				alength=NCTranslate.beginTranslate(TR_Programming_Activity.this,
						getResources().getStringArray(R.array.IF2));
				new Fragments_NCedit4().showNote();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "NC程序存在错误",
							Toast.LENGTH_SHORT).show();
				}
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("NC_5") instanceof Fragments_NCedit5){
				NCTranslate.noteList.clear();
				noteflag=5;
				try{
				alength=NCTranslate.beginTranslate(TR_Programming_Activity.this,
						getResources().getStringArray(R.array.IF2));
				new Fragments_NCedit5().showNote();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "NC程序存在错误",
							Toast.LENGTH_SHORT).show();
				}
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("NC_6") instanceof Fragments_NCedit6){
				NCTranslate.noteList.clear();
				noteflag=6;
				try{
				alength=NCTranslate.beginTranslate(TR_Programming_Activity.this,
						getResources().getStringArray(R.array.IF2));
				new Fragments_NCedit6().showNote();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "NC程序存在错误",
							Toast.LENGTH_SHORT).show();
				}
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("NC_7") instanceof Fragments_NCedit7){
				NCTranslate.noteList.clear();
				noteflag=7;
				try{
				alength=NCTranslate.beginTranslate(TR_Programming_Activity.this,
						getResources().getStringArray(R.array.IF2));
				new Fragments_NCedit7().showNote();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "NC程序存在错误",
							Toast.LENGTH_SHORT).show();
				}
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("NC_8") instanceof Fragments_NCedit8){
				NCTranslate.noteList.clear();
				noteflag=8;
				try{
				alength=NCTranslate.beginTranslate(TR_Programming_Activity.this,
						getResources().getStringArray(R.array.IF2));
				new Fragments_NCedit8().showNote();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "NC程序存在错误",
							Toast.LENGTH_SHORT).show();
				}
				noteflag=0;
			}
			 
			if (getFragmentManager().findFragmentByTag("Table_1") instanceof Fragments_Table1){
				TableTranslate.tnoteList.clear();
				noteflag=1;
				index=TableTranslate.beginTranslate();
				new Fragments_Table1().showNote();
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("Table_2") instanceof Fragments_Table2){
				TableTranslate.tnoteList.clear();
				noteflag=2;
				index=TableTranslate.beginTranslate();
				new Fragments_Table2().showNote();
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("Table_3") instanceof Fragments_Table3){
				TableTranslate.tnoteList.clear();
				noteflag=3;
				index=TableTranslate.beginTranslate();
				new Fragments_Table3().showNote();
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("Table_4") instanceof Fragments_Table4){
				TableTranslate.tnoteList.clear();
				noteflag=4;
				index=TableTranslate.beginTranslate();
				new Fragments_Table4().showNote();
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("Table_5") instanceof Fragments_Table5){
				TableTranslate.tnoteList.clear();
				noteflag=5;
				index=TableTranslate.beginTranslate();
				new Fragments_Table5().showNote();
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("Table_6") instanceof Fragments_Table6){
				TableTranslate.tnoteList.clear();
				noteflag=6;
				index=TableTranslate.beginTranslate();
				new Fragments_Table6().showNote();
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("Table_7") instanceof Fragments_Table7){
				TableTranslate.tnoteList.clear();
				noteflag=7;
				index=TableTranslate.beginTranslate();
				new Fragments_Table7().showNote();
				noteflag=0;
			}
			if (getFragmentManager().findFragmentByTag("Table_8") instanceof Fragments_Table8){
				TableTranslate.tnoteList.clear();
				noteflag=8;
				index=TableTranslate.beginTranslate();
				new Fragments_Table8().showNote();
				noteflag=0;
			}
			break;
		case R.id.menu_saveCache:// 保存到临时文件夹

			// 开始发送信息文件，首先判断是离线保存还是在线保存
			if(menu_wantchStop.isVisible()){
				Toast.makeText(TR_Programming_Activity.this, "请先关闭监视后，再保存", Toast.LENGTH_SHORT).show();
				break;
			}
			
			new AlertDialog.Builder(TR_Programming_Activity.this)
			.setTitle("提示").setMessage("确定保存到临时文件夹cache")
			.setPositiveButton("确定",
            new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) {
					WifiSetting_Info.progressDialog = ProgressDialog.show(TR_Programming_Activity.this, "保存程序中","请等待", true, false);
		            
		              new Thread()
		              {
		                  public void run()
		                  {
		                     final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(Calendar.getInstance().getTime());// 当前时间
		             TR_Main_Activity.sharedPreference_openedDir.edit().putString("dir_time", date).commit();
					creatFolderSysytem();

					saveAs_DeviceDefine("cache", date);
					saveAs_NcEdit("cache", date);
					saveAs_TableEdit("cache", date);
					
					 WifiSetting_Info.progressDialog.cancel();
		                  }
		              }.start();
		              WifiSetting_Info.progressDialog.setOnDismissListener(new OnDismissListener() {              
				            
							@Override
							public void onDismiss(DialogInterface arg0) {
								// TODO Auto-generated method stub
								Toast.makeText(TR_Programming_Activity.this, "已保存到临时文件夹cache", Toast.LENGTH_SHORT).show();
							}  
				        });
				}
			}).setNegativeButton("取消",null).show();
			
			break;
		case R.id.menu_download:// 下载
			//nc下载
				final String date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(Calendar.getInstance().getTime());// 当前时间

			if (WifiSetting_Info.mClient == null) {
				//Toast.makeText(getApplicationContext(), "当前离线状态，无法下载程序", Toast.LENGTH_SHORT).show();
				if(ServerUtils.isUsingWifi(TR_Programming_Activity.this)){
					new AlertDialog.Builder(TR_Programming_Activity.this)
					.setTitle("提示").setMessage("请连接wifi")
					.setPositiveButton("确定",
		            new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int which) {
							new Thread() {
								public void run() {
									try {
										WifiSetting_Info.mClient = Client.connect();
										new Thread(WifiSetting_Info.mClient).start();
										runOnUiThread(successRunnable);
									} catch (IOException e) {
										e.printStackTrace();
										runOnUiThread(errorRunnable);
									}
								}
							}.start();
						}
					}).setNegativeButton("取消",null).show();
				}else{
					Toast.makeText(TR_Programming_Activity.this, "网络异常，通讯中断！请检查网络！", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			
			//if(TR_Main_Activity.zd_led.getBackground().equals(TR_Main_Activity.drawablezdled)){
			if(Config.AutoState)
			{
				new AlertDialog.Builder(TR_Programming_Activity.this)
				.setTitle("警告")
				.setMessage("机器处于自动状态，如需下载，请切换到手动状态！")
				.setPositiveButton("知道了",null)
				.show();
				break;
		    }
			new AlertDialog.Builder(TR_Programming_Activity.this)
			.setTitle("提示").setMessage("确定要下载吗？")
			.setPositiveButton("确定",new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) {
					if (getFragmentManager().findFragmentByTag("NC_1") instanceof Fragments_NCedit1 ||
							getFragmentManager().findFragmentByTag("NC_2") instanceof Fragments_NCedit2 ||
							getFragmentManager().findFragmentByTag("NC_3") instanceof Fragments_NCedit3 ||
							getFragmentManager().findFragmentByTag("NC_4") instanceof Fragments_NCedit4 ||
							getFragmentManager().findFragmentByTag("NC_5") instanceof Fragments_NCedit5 ||
							getFragmentManager().findFragmentByTag("NC_6") instanceof Fragments_NCedit6 ||
							getFragmentManager().findFragmentByTag("NC_7") instanceof Fragments_NCedit7 ||
							getFragmentManager().findFragmentByTag("NC_8") instanceof Fragments_NCedit8) {
						// 等待提示
						WifiSetting_Info.wifiTimeOut=System.currentTimeMillis();
							WifiSetting_Info.progressDialog = ProgressDialog.show(TR_Programming_Activity.this, "烧写程序中","请等待", true, false);
							
							 new Thread()
				              {
				                  public void run()
				                  {
							try {
								alength=NCTranslate.beginTranslate(TR_Programming_Activity.this,
										getResources().getStringArray(R.array.IF2));// 开始解释
								 if(NCTranslate.dname!=null)
					 		      {
								   if(alength[0]==-1){
									   overflag[1]=1;
									   if(WifiSetting_Info.progressDialog!=null){
											WifiSetting_Info.progressDialog.dismiss();
											WifiSetting_Info.progressDialog=null;
										}
								   }else if(alength[0]==1){
									   overflag[2]=1;
									   if(WifiSetting_Info.progressDialog!=null){
											WifiSetting_Info.progressDialog.dismiss();
											WifiSetting_Info.progressDialog=null;
										}
								   }else if(alength[0]==-2){
										   overflag[6]=1;
										   if(WifiSetting_Info.progressDialog!=null){
												WifiSetting_Info.progressDialog.dismiss();
												WifiSetting_Info.progressDialog=null;
										}
								   }else if(alength[0]==-3){
											   overflag[7]=1;
											   if(WifiSetting_Info.progressDialog!=null){
													WifiSetting_Info.progressDialog.dismiss();
													WifiSetting_Info.progressDialog=null;
										}
								   }
								   return;
					 		     }
								
							} catch (Exception e) {
								// TODO: handle exception
								overflag[3]=1;
								if(WifiSetting_Info.progressDialog!=null){
									WifiSetting_Info.progressDialog.dismiss();
									WifiSetting_Info.progressDialog=null;
								}
								return;
							}
							System.out.println("ab1234");
							// 必须对数据格式类进行初始化，否则会返回一个空指针的错误
							// 加入一个判断，防止nc代码为空引发错误
							if (NCTranslate.getMesHexCode().length != 0
									&& !NCTranslate.getMesHexCode().equals(null)) {
								System.out.println("nc="+NCTranslate.getMesHexCode().length);
					                	  sendDataRunnable = new SendDataRunnable(
										  new WifiSendDataFormat(
												NCTranslate.getMesHexCode(),
												AddressPublic.NCProg_Head),
												TR_Programming_Activity.this);
								          sendDataRunnables.add(sendDataRunnable);
					                	  addmessage(date1);
					  					  fromsd();
								 
							}else {
								overflag[4]=1;
								if(WifiSetting_Info.progressDialog!=null){
									WifiSetting_Info.progressDialog.dismiss();
									WifiSetting_Info.progressDialog=null;
								}
								return;
							}
				                  }
				              }.start();
							WifiSetting_Info.progressDialog.setOnDismissListener(new OnDismissListener() {              
					            
								@Override
								public void onDismiss(DialogInterface arg0) {
									// TODO Auto-generated method stub
									 if(WifiSetting_Info.mClient==null){
										 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
										   .setMessage("网络异常，通讯中断！请检查网络！")
										   .setPositiveButton("确定", null).show();
						                	//Toast.makeText(TR_Programming_Activity.this,"网络异常，传输中断！请检查网络！", Toast.LENGTH_SHORT).show();
										}else if(overflag[0]==1){
											 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
											   .setMessage("数据发送完毕")
											   .setPositiveButton("确定", null).show();
											 overflag[0]=0;
											 formatReadusermode = new WifiReadDataFormat(0x200000AF,1);
												try {
													sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode,TR_Programming_Activity.this);
													sendDataFinishRunnable=new FinishRunnable(TR_Programming_Activity.this,stopDataFinishTodo2);
													sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));
													runOnUiThread(sendDatausermodeRunnable);
												} catch (Exception e) {
													// TODO: handle exception
												}
										}else if(overflag[1]==1){
											 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
											   .setMessage(NCTranslate.ncindex+"的"+NCTranslate.dname+"设备未选择设定")
											   .setPositiveButton("确定", null).show();
											   NCTranslate.dname=null;
											   overflag[1]=0;
										}else if(overflag[2]==1){
											 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
											   .setMessage("没有找到"+NCTranslate.ncindex+"的"+NCTranslate.dname+"设备")
											   .setPositiveButton("确定", null).show();
											   NCTranslate.dname=null;
											   overflag[2]=0;
										}else if(overflag[6]==1){
											 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
											   .setMessage(NCTranslate.ncindex+"的标号"+NCTranslate.dname+"存在错误")
											   .setPositiveButton("确定", null).show();
											 NCTranslate.dname=null;
											 overflag[6]=0;
										}else if(overflag[7]==1){
											 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
											   .setMessage(NCTranslate.ncindex+"的命令"+NCTranslate.dname+"存在错误")
											   .setPositiveButton("确定", null).show();
											 NCTranslate.dname=null;
											 overflag[7]=0;
										}else if(overflag[3]==1){
											 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
											   .setMessage("NC程序存在错误")
											   .setPositiveButton("确定", null).show();
											 overflag[3]=0;
										}else if(overflag[4]==1){
											 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
											   .setMessage("NC程序为空，请填写NC程序")
											   .setPositiveButton("确定", null).show();
											 overflag[4]=0;
										}else if(overflag[5]==1){
											 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
											   .setMessage("程序异常，请重操作！")
											   .setPositiveButton("确定", null).show();
											 overflag[5]=0;
											//Toast.makeText(TR_Programming_Activity.this,"程序异常，请重操作！", Toast.LENGTH_SHORT).show();
										}
								}  
					        });
							//table下载
						} else if (getFragmentManager().findFragmentByTag("Table_1") instanceof Fragments_Table1 ||
								getFragmentManager().findFragmentByTag("Table_2") instanceof Fragments_Table2 ||
								getFragmentManager().findFragmentByTag("Table_3") instanceof Fragments_Table3 ||
								getFragmentManager().findFragmentByTag("Table_4") instanceof Fragments_Table4 ||
								getFragmentManager().findFragmentByTag("Table_5") instanceof Fragments_Table5 ||
								getFragmentManager().findFragmentByTag("Table_6") instanceof Fragments_Table6 ||
								getFragmentManager().findFragmentByTag("Table_7") instanceof Fragments_Table7 ||
								getFragmentManager().findFragmentByTag("Table_8") instanceof Fragments_Table8) {
							// 等待提示
							WifiSetting_Info.wifiTimeOut=System.currentTimeMillis();
							WifiSetting_Info.progressDialog = ProgressDialog.show(TR_Programming_Activity.this, "烧写程序中","请等待", true, false);
							// table的解释下载
							 new Thread()
				              {
				                  public void run()
				                  {
							int index1=TableTranslate.beginTranslate();
							 if(TableTranslate.dname!=null)
				 		      {
								 if(index1==-1){
									 overflag[1]=1;
									 if(WifiSetting_Info.progressDialog!=null){
											WifiSetting_Info.progressDialog.dismiss();
											WifiSetting_Info.progressDialog=null;
										}
								 }else{
									 overflag[2]=1;
									 if(WifiSetting_Info.progressDialog!=null){
											WifiSetting_Info.progressDialog.dismiss();
											WifiSetting_Info.progressDialog=null;
										}
								 }
								 return;
				 		     }
							HexDecoding.printHexString("table生成数据", TableTranslate.getMesHexCode());
			               if (TableTranslate.getMesHexCode().length != 0
									&& !TableTranslate.getMesHexCode().equals(null)) {
			            	   System.out.println("table="+TableTranslate.getMesHexCode().length);
					                	  sendDataRunnable = new SendDataRunnable(
										  new WifiSendDataFormat(
												TableTranslate.getMesHexCode(),
												AddressPublic.SysTable_Head),
												TR_Programming_Activity.this);
								          sendDataRunnables.add(sendDataRunnable);
					                	  addmessage(date1);
					  					  fromsd(); 
					               
								
							}else {
								overflag[3]=1;
								if(WifiSetting_Info.progressDialog!=null){
									WifiSetting_Info.progressDialog.dismiss();
									WifiSetting_Info.progressDialog=null;
								}
								return;
							}
					              }
				              }.start();
							 WifiSetting_Info.progressDialog.setOnDismissListener(new OnDismissListener() {              
						            
									@Override
									public void onDismiss(DialogInterface arg0) {
										// TODO Auto-generated method stub
										 if(WifiSetting_Info.mClient==null){
											 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
											   .setMessage("网络异常，通讯中断！请检查网络！")
											   .setPositiveButton("确定", null).show();
							                	//Toast.makeText(TR_Programming_Activity.this,"网络异常，传输中断！请检查网络！", Toast.LENGTH_SHORT).show();
											}else if(overflag[0]==1){
												 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
												   .setMessage("数据发送完毕")
												   .setPositiveButton("确定", null).show();
												 overflag[0]=0;
												 formatReadusermode = new WifiReadDataFormat(0x200000AF,1);
													try {
														sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode,TR_Programming_Activity.this);
														sendDataFinishRunnable=new FinishRunnable(TR_Programming_Activity.this,stopDataFinishTodo2);
														sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));
														runOnUiThread(sendDatausermodeRunnable);
													} catch (Exception e) {
														// TODO: handle exception
													}
											}else if(overflag[1]==1){
												 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
												   .setMessage(TableTranslate.tableindex+"的"+TableTranslate.dname+"设备未选择设定")
												   .setPositiveButton("确定", null).show();
												   TableTranslate.dname=null;
												   overflag[1]=0;
											}else if(overflag[2]==1){
												 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
												   .setMessage("没有找到"+TableTranslate.tableindex+"的"+TableTranslate.dname+"设备")
												   .setPositiveButton("确定", null).show();
												   TableTranslate.dname=null; 
												   overflag[2]=0;
											}else if(overflag[3]==1){
												 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
												   .setMessage("table程序为空，请填写table程序")
												   .setPositiveButton("确定", null).show();
												 overflag[3]=0;
											}else if(overflag[5]==1){
												 new AlertDialog.Builder(TR_Programming_Activity.this).setTitle("提示")
												   .setMessage("程序异常，请重操作！")
												   .setPositiveButton("确定", null).show();
												 overflag[5]=0;
											}
									}  
						        });
						}
				}
			}).setNegativeButton("取消",null).show();
			break;

		case R.id.menu_watchStart:// 开始监视

			/******************* PLCdata查询 *************************************/
			if (WifiSetting_Info.mClient == null) {
				//Toast.makeText(getApplicationContext(), "当前离线状态，无法监视程序", Toast.LENGTH_SHORT).show();
				if(ServerUtils.isUsingWifi(TR_Programming_Activity.this)){
					new AlertDialog.Builder(TR_Programming_Activity.this)
					.setTitle("提示").setMessage("请连接wifi")
					.setPositiveButton("确定",
		            new DialogInterface.OnClickListener() 
					{
						public void onClick(DialogInterface dialog, int which) {
							new Thread() {
								public void run() {
									try {
										WifiSetting_Info.mClient = Client.connect();
										new Thread(WifiSetting_Info.mClient).start();
										runOnUiThread(successRunnable);
									} catch (IOException e) {
										e.printStackTrace();
										runOnUiThread(errorRunnable);
									}
								}
							}.start();
						}
					}).setNegativeButton("取消",null).show();
				}else{
					Toast.makeText(TR_Programming_Activity.this, "网络异常，通讯中断！请检查网络！", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			menu_watchStart.setVisible(false);
			menu_wantchStop.setVisible(true);
			menu_saveCache.setEnabled(false);
			menu_download.setEnabled(false);

			if (getFragmentManager().findFragmentByTag("Table_1") instanceof Fragments_Table1 ){
				plcquery = new PlcDataQueryRunnable(TR_Programming_Activity.this,1);
				// 不要放在UI线程，减轻UI负担
				int index1=TableTranslate.beginTranslate();
				 if(TableTranslate.dname!=null)
	 		      {
					 if(index1==-1){
				   new AlertDialog.Builder(this).setTitle("提示")
				   .setMessage(TableTranslate.tableindex+"的"+TableTranslate.dname+"设备未选择设定")
				   .setPositiveButton("知道了", null).show();
				   TableTranslate.dname=null;
					 }else{
						 new AlertDialog.Builder(this).setTitle("提示")
						   .setMessage("没有找到"+TableTranslate.tableindex+"的"+TableTranslate.dname+"设备")
						   .setPositiveButton("知道了", null).show();
						   TableTranslate.dname=null; 
					 }
					 menu_watchStart.setVisible(true);
						menu_wantchStop.setVisible(false);
						menu_saveCache.setEnabled(true);
						menu_download.setEnabled(true);
				   break;
	 		     }
				new Thread(plcquery).start();
			}
			if (getFragmentManager().findFragmentByTag("Table_2") instanceof Fragments_Table2 ){
				plcquery = new PlcDataQueryRunnable(TR_Programming_Activity.this,2);
				int index1=TableTranslate.beginTranslate();
				 if(TableTranslate.dname!=null)
	 		      {
					 if(index1==-1){
				   new AlertDialog.Builder(this).setTitle("提示")
				   .setMessage(TableTranslate.tableindex+"的"+TableTranslate.dname+"设备未选择设定")
				   .setPositiveButton("知道了", null).show();
				   TableTranslate.dname=null;
					 }else{
						 new AlertDialog.Builder(this).setTitle("提示")
						   .setMessage("没有找到"+TableTranslate.tableindex+"的"+TableTranslate.dname+"设备")
						   .setPositiveButton("知道了", null).show();
						   TableTranslate.dname=null; 
					 }
					 menu_watchStart.setVisible(true);
						menu_wantchStop.setVisible(false);
						menu_saveCache.setEnabled(true);
						menu_download.setEnabled(true);
				   break;
	 		     }
				new Thread(plcquery).start();
			}
			if (getFragmentManager().findFragmentByTag("Table_3") instanceof Fragments_Table3) {
					plcquery = new PlcDataQueryRunnable(TR_Programming_Activity.this,3);
					int index1=TableTranslate.beginTranslate();
					 if(TableTranslate.dname!=null)
		 		      {
						 if(index1==-1){
					   new AlertDialog.Builder(this).setTitle("提示")
					   .setMessage(TableTranslate.tableindex+"的"+TableTranslate.dname+"设备未选择设定")
					   .setPositiveButton("知道了", null).show();
					   TableTranslate.dname=null;
						 }else{
							 new AlertDialog.Builder(this).setTitle("提示")
							   .setMessage("没有找到"+TableTranslate.tableindex+"的"+TableTranslate.dname+"设备")
							   .setPositiveButton("知道了", null).show();
							   TableTranslate.dname=null; 
						 }
						 menu_watchStart.setVisible(true);
							menu_wantchStop.setVisible(false);
							menu_saveCache.setEnabled(true);
							menu_download.setEnabled(true);
					   break;
		 		     }
					new Thread(plcquery).start();
			}
			if (getFragmentManager().findFragmentByTag("Table_4") instanceof Fragments_Table4) {
					plcquery = new PlcDataQueryRunnable(TR_Programming_Activity.this,4);
					int index1=TableTranslate.beginTranslate();
					 if(TableTranslate.dname!=null)
		 		      {
						 if(index1==-1){
					   new AlertDialog.Builder(this).setTitle("提示")
					   .setMessage(TableTranslate.tableindex+"的"+TableTranslate.dname+"设备未选择设定")
					   .setPositiveButton("知道了", null).show();
					   TableTranslate.dname=null;
						 }else{
							 new AlertDialog.Builder(this).setTitle("提示")
							   .setMessage("没有找到"+TableTranslate.tableindex+"的"+TableTranslate.dname+"设备")
							   .setPositiveButton("知道了", null).show();
							   TableTranslate.dname=null; 
						 }
						 menu_watchStart.setVisible(true);
							menu_wantchStop.setVisible(false);
							menu_saveCache.setEnabled(true);
							menu_download.setEnabled(true);
					   break;
		 		     }
					new Thread(plcquery).start();
			}
			if (getFragmentManager().findFragmentByTag("Table_5") instanceof Fragments_Table5) {
					plcquery = new PlcDataQueryRunnable(TR_Programming_Activity.this,5);
					int index1=TableTranslate.beginTranslate();
					 if(TableTranslate.dname!=null)
		 		      {
						 if(index1==-1){
					   new AlertDialog.Builder(this).setTitle("提示")
					   .setMessage(TableTranslate.tableindex+"的"+TableTranslate.dname+"设备未选择设定")
					   .setPositiveButton("知道了", null).show();
					   TableTranslate.dname=null;
						 }else{
							 new AlertDialog.Builder(this).setTitle("提示")
							   .setMessage("没有找到"+TableTranslate.tableindex+"的"+TableTranslate.dname+"设备")
							   .setPositiveButton("知道了", null).show();
							   TableTranslate.dname=null; 
						 }
						 menu_watchStart.setVisible(true);
							menu_wantchStop.setVisible(false);
							menu_saveCache.setEnabled(true);
							menu_download.setEnabled(true);
					   break;
		 		     }
					new Thread(plcquery).start();
			}
			if (getFragmentManager().findFragmentByTag("Table_6") instanceof Fragments_Table6) {
					plcquery = new PlcDataQueryRunnable(TR_Programming_Activity.this,6);
					int index1=TableTranslate.beginTranslate();
					 if(TableTranslate.dname!=null)
		 		      {
						 if(index1==-1){
					   new AlertDialog.Builder(this).setTitle("提示")
					   .setMessage(TableTranslate.tableindex+"的"+TableTranslate.dname+"设备未选择设定")
					   .setPositiveButton("知道了", null).show();
					   TableTranslate.dname=null;
						 }else{
							 new AlertDialog.Builder(this).setTitle("提示")
							   .setMessage("没有找到"+TableTranslate.tableindex+"的"+TableTranslate.dname+"设备")
							   .setPositiveButton("知道了", null).show();
							   TableTranslate.dname=null; 
						 }
						 menu_watchStart.setVisible(true);
							menu_wantchStop.setVisible(false);
							menu_saveCache.setEnabled(true);
							menu_download.setEnabled(true);
					   break;
		 		     }
					new Thread(plcquery).start();
			}
			if (getFragmentManager().findFragmentByTag("Table_7") instanceof Fragments_Table7) {
					plcquery = new PlcDataQueryRunnable(TR_Programming_Activity.this,7);
					int index1=TableTranslate.beginTranslate();
					 if(TableTranslate.dname!=null)
		 		      {
						 if(index1==-1){
					   new AlertDialog.Builder(this).setTitle("提示")
					   .setMessage(TableTranslate.tableindex+"的"+TableTranslate.dname+"设备未选择设定")
					   .setPositiveButton("知道了", null).show();
					   TableTranslate.dname=null;
						 }else{
							 new AlertDialog.Builder(this).setTitle("提示")
							   .setMessage("没有找到"+TableTranslate.tableindex+"的"+TableTranslate.dname+"设备")
							   .setPositiveButton("知道了", null).show();
							   TableTranslate.dname=null; 
						 }
						 menu_watchStart.setVisible(true);
							menu_wantchStop.setVisible(false);
							menu_saveCache.setEnabled(true);
							menu_download.setEnabled(true);
					   break;
		 		     }
					new Thread(plcquery).start();
			}
			if (getFragmentManager().findFragmentByTag("Table_8") instanceof Fragments_Table8) {
				    plcquery = new PlcDataQueryRunnable(TR_Programming_Activity.this,8);
					int index1=TableTranslate.beginTranslate();
					 if(TableTranslate.dname!=null)
		 		      {
						 if(index1==-1){
					   new AlertDialog.Builder(this).setTitle("提示")
					   .setMessage(TableTranslate.tableindex+"的"+TableTranslate.dname+"设备未选择设定")
					   .setPositiveButton("知道了", null).show();
					   TableTranslate.dname=null;
						 }else{
							 new AlertDialog.Builder(this).setTitle("提示")
							   .setMessage("没有找到"+TableTranslate.tableindex+"的"+TableTranslate.dname+"设备")
							   .setPositiveButton("知道了", null).show();
							   TableTranslate.dname=null; 
						 }
						 menu_watchStart.setVisible(true);
						 menu_wantchStop.setVisible(false);
						 menu_saveCache.setEnabled(true);
							menu_download.setEnabled(true);
					   break;
		 		     }
					new Thread(plcquery).start();
			}
			 if(getFragmentManager().findFragmentByTag("NC_1") instanceof Fragments_NCedit1 ||
						getFragmentManager().findFragmentByTag("NC_2") instanceof Fragments_NCedit2 ||
						getFragmentManager().findFragmentByTag("NC_3") instanceof Fragments_NCedit3 ||
						getFragmentManager().findFragmentByTag("NC_4") instanceof Fragments_NCedit4 ||
						getFragmentManager().findFragmentByTag("NC_5") instanceof Fragments_NCedit5 ||
						getFragmentManager().findFragmentByTag("NC_6") instanceof Fragments_NCedit6 ||
						getFragmentManager().findFragmentByTag("NC_7") instanceof Fragments_NCedit7 ||
						getFragmentManager().findFragmentByTag("NC_8") instanceof Fragments_NCedit8) {
					try{
			    	alength = NCTranslate.beginTranslate(
							TR_Programming_Activity.this, getResources()
									.getStringArray(R.array.IF2));
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getApplicationContext(), "NC程序存在错误",
								Toast.LENGTH_SHORT).show();
					}
					 if(NCTranslate.dname!=null)
		 		      {
					   if(alength[0]==-1){
					   new AlertDialog.Builder(this).setTitle("提示")
					   .setMessage(NCTranslate.ncindex+"的"+NCTranslate.dname+"设备未选择设定")
					   .setPositiveButton("知道了", null).show();
					   NCTranslate.dname=null;
						 }else if(alength[0]==1){
						   new AlertDialog.Builder(this).setTitle("提示")
						   .setMessage("没有找到"+NCTranslate.ncindex+"的"+NCTranslate.dname+"设备")
						   .setPositiveButton("知道了", null).show();
						   NCTranslate.dname=null;
						}else if(alength[0]==-2){
							   new AlertDialog.Builder(this).setTitle("提示")
							   .setMessage(NCTranslate.ncindex+"的标号"+NCTranslate.dname+"存在错误")
							   .setPositiveButton("知道了", null).show();
							   NCTranslate.dname=null;
						}else if(alength[0]==-3){
								   new AlertDialog.Builder(this).setTitle("提示")
								   .setMessage(NCTranslate.ncindex+"的命令"+NCTranslate.dname+"存在错误")
								   .setPositiveButton("知道了", null).show();
								   NCTranslate.dname=null;
						}
					   menu_watchStart.setVisible(true);
						menu_wantchStop.setVisible(false);
						menu_saveCache.setEnabled(true);
						menu_download.setEnabled(true);
					   break;
		 		     }
					watchRunnable = new WatchRunnable(TR_Programming_Activity.this,
							alength);
					Device_ListFragment.firstOpen=true;
					watchflag=true;
					Thread a1 = new Thread(watchRunnable);
					a1.start();
					
				}
		   
			menu_lock.setVisible(false);
			menu_unlock.setVisible(true);// 点击锁定后设置锁定菜单项为隐藏，设置解锁菜单项为显示

			if (alreadyChecked_programmingPassword) {
				alreadyChecked_programmingPassword = false;
				updateAdapter();
			}
			title_lock_programming = "已锁定";
			getActionBar().setSubtitle(title_lock_programming);
			Toast.makeText(getApplicationContext(), "监视开始", Toast.LENGTH_SHORT)
					.show();
			break;

		case R.id.menu_watchStop://停止监视
			menu_watchStart.setVisible(true);
			menu_wantchStop.setVisible(false);
			menu_saveCache.setEnabled(true);
			menu_download.setEnabled(true);

			if (getFragmentManager().findFragmentByTag("Table_1") instanceof Fragments_Table1 ||
					getFragmentManager().findFragmentByTag("Table_2") instanceof Fragments_Table2 ||
					getFragmentManager().findFragmentByTag("Table_3") instanceof Fragments_Table3 ||
					getFragmentManager().findFragmentByTag("Table_4") instanceof Fragments_Table4 ||
					getFragmentManager().findFragmentByTag("Table_5") instanceof Fragments_Table5 ||
					getFragmentManager().findFragmentByTag("Table_6") instanceof Fragments_Table6 ||
					getFragmentManager().findFragmentByTag("Table_7") instanceof Fragments_Table7 ||
					getFragmentManager().findFragmentByTag("Table_8") instanceof Fragments_Table8) {
				try{
				PlcDataQueryRunnable.destroy();
				if (getFragmentManager().findFragmentByTag("Table_1") instanceof Fragments_Table1) {
				     Fragments_Table1.TableEdit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("Table_2") instanceof Fragments_Table2) {
				     Fragments_Table2.TableEdit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("Table_3") instanceof Fragments_Table3) {
				     Fragments_Table3.TableEdit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("Table_4") instanceof Fragments_Table4) {
				     Fragments_Table4.TableEdit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("Table_5") instanceof Fragments_Table5) {
				     Fragments_Table5.TableEdit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("Table_6") instanceof Fragments_Table6) {
				     Fragments_Table6.TableEdit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("Table_7") instanceof Fragments_Table7) {
				     Fragments_Table7.TableEdit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("Table_8") instanceof Fragments_Table8) {
				     Fragments_Table8.TableEdit_Adapter.notifyDataSetChanged();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
				//Fragments_Table.TableEdit_Adapter.notifyDataSetChanged();
			} else if (getFragmentManager().findFragmentByTag("NC_1") instanceof Fragments_NCedit1 ||
					getFragmentManager().findFragmentByTag("NC_2") instanceof Fragments_NCedit2 ||
					getFragmentManager().findFragmentByTag("NC_3") instanceof Fragments_NCedit3 ||
					getFragmentManager().findFragmentByTag("NC_4") instanceof Fragments_NCedit4 ||
					getFragmentManager().findFragmentByTag("NC_5") instanceof Fragments_NCedit5 ||
					getFragmentManager().findFragmentByTag("NC_6") instanceof Fragments_NCedit6 ||
					getFragmentManager().findFragmentByTag("NC_7") instanceof Fragments_NCedit7 ||
					getFragmentManager().findFragmentByTag("NC_8") instanceof Fragments_NCedit8) {
				try{
					if(watchRunnable!=null)	
						watchRunnable.destroy();
				if (getFragmentManager().findFragmentByTag("NC_1") instanceof Fragments_NCedit1) {
					 Fragments_NCedit1.NCedit_Adapter.setSelectItem(-1);
				   Fragments_NCedit1.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_2") instanceof Fragments_NCedit2) {
					 Fragments_NCedit2.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit2.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_3") instanceof Fragments_NCedit3) {
					Fragments_NCedit3.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit3.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_4") instanceof Fragments_NCedit4) {
					Fragments_NCedit4.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit4.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_5") instanceof Fragments_NCedit5) {
					Fragments_NCedit5.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit5.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_6") instanceof Fragments_NCedit6) {
					Fragments_NCedit6.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit6.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_7") instanceof Fragments_NCedit7) {
					Fragments_NCedit7.NCedit_Adapter.setSelectItem(-1);
				 Fragments_NCedit7.NCedit_Adapter.notifyDataSetChanged();
				}
				if (getFragmentManager().findFragmentByTag("NC_8") instanceof Fragments_NCedit8) {
					Fragments_NCedit8.NCedit_Adapter.setSelectItem(-1);
					Fragments_NCedit8.NCedit_Adapter.notifyDataSetChanged();
				}
				 
				}catch(Exception e){
					e.printStackTrace();
				}
				//2013.8.21
			}


			Toast.makeText(getApplicationContext(), "监视停止", Toast.LENGTH_SHORT)
					.show();



			break;
		case R.id.menu_lock:// 锁定面板功能
			menu_lock.setVisible(false);
			menu_unlock.setVisible(true);// 点击锁定后设置锁定菜单项为隐藏，设置解锁菜单项为显示

			if (alreadyChecked_programmingPassword) {
				alreadyChecked_programmingPassword = false;
				updateAdapter();
				Toast.makeText(this, "锁定成功", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "当前已处于锁定状态", Toast.LENGTH_SHORT).show();
			}
			title_lock_programming = "已锁定";
			getActionBar().setSubtitle(title_lock_programming);

			break;
		case R.id.menu_unlock:// 解锁面板功能
			menu_lock.setVisible(true);
			menu_unlock.setVisible(false);// 点击解锁后设置解锁菜单项为隐藏，设置锁定菜单项为显示

			if (alreadyChecked_programmingPassword) {// 已经解锁
				Toast.makeText(this, "当前处于解锁状态", Toast.LENGTH_SHORT).show();
			} else {// 否则先输密码解锁
				password_programming = sharedPreference_password.getString(
						"password_programming", "");
				if (password_programming.equals("")) {// 保存的密码为空，改变标志位
					alreadyChecked_programmingPassword = true;
					new AlertDialog.Builder(this).setTitle("警告")
					.setMessage("当前无密码，处于解锁状态，要锁定请先前往维护界面设置编程密码")
					.setPositiveButton("知道了", null).show();
					updateAdapter();
				} else {// 密码不为空
					// 输密码的输入控制
					final EditText editText = new EditText(this);
					editText.setHint("请输入密码，20位以内");
					editText.setTransformationMethod(PasswordTransformationMethod
							.getInstance());// 设置密码为不可见。
					editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							20) });
					editText.setKeyListener(new NumberKeyListener() {
						@Override
						protected char[] getAcceptedChars() {
							return new char[] { 'a', 'b', 'c', 'd', 'e', 'f',
									'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
									'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
									'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
									'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
									'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
									'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2',
									'3', '4', '5', '6', '7', '8', '9', '0' };
						}

						@Override
						public int getInputType() {
							return android.text.InputType.TYPE_CLASS_TEXT;
						}
					});
					new AlertDialog.Builder(this)
					.setTitle("请输入编程密码以获得权限")
					.setView(editText)
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							String password_input = editText
									.getText().toString()
									.trim();
							if (password_programming
									.equals(password_input)) {// 输入正确就改变标志位，解锁并关闭窗口

								Toast.makeText(
										TR_Programming_Activity.this,
										"解锁成功",
										Toast.LENGTH_SHORT)
										.show();
								alreadyChecked_programmingPassword = true;// 标志位设为真
								updateAdapter();
								try {// 消失
									Field field = dialog
											.getClass()
											.getSuperclass()
											.getDeclaredField(
													"mShowing");
									field.setAccessible(true);
									field.set(dialog, true);// 将mShowing变量设为true，表示对话框未关闭
								} catch (Exception e) {
									e.printStackTrace();
								}
								dialog.dismiss();

							} else {// 不正确，重输，窗口保留
								try {// 不消失
									Field field = dialog
											.getClass()
											.getSuperclass()
											.getDeclaredField(
													"mShowing");
									field.setAccessible(true);
									field.set(dialog, false);// 将mShowing变量设为false，表示对话框已关闭，欺骗android系统
								} catch (Exception e) {
									e.printStackTrace();
								}

								editText.setText("");
								editText.setHint("编程密码输入不正确，请重输");

							}

						}
					})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {

							try {// 消失
								Field field = dialog
										.getClass()
										.getSuperclass()
										.getDeclaredField(
												"mShowing");
								field.setAccessible(true);
								field.set(dialog, true);// 将mShowing变量设为true，表示对话框未关闭
							} catch (Exception e) {
								e.printStackTrace();
							}
							dialog.dismiss();
						}
					}).show();
				}
			}
			title_lock_programming = "未锁定";
			getActionBar().setSubtitle(title_lock_programming);
			break;
			// case R.id.menu_newfile:// 新建文件夹功能
			// filenew();
			// break;
			// case R.id.menu_open:// 读取功能
			// File mouldData = new File(Constans.mouldDataPATH);
			// final String[] readfolders = mouldData.list();// 得到文件夹名
			// new AlertDialog.Builder(this)
			// .setTitle("请选择打开文件夹")
			// .setItems(readfolders,
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// readAllFiles(readfolders[which]);
			// }
			// }).setNegativeButton("取消", null).show();
			// // fileRead();
			// break;
			// case R.id.menu_save:// 保存功能
			// fileSave();
			// break;
			// case R.id.menu_save_as:// 另存为功能
			// final EditText etEditText = new EditText(this);
			// new AlertDialog.Builder(this)
			// .setTitle("请输入另存文件夹名")
			// .setView(etEditText)
			// .setPositiveButton("确定",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// String foldername = etEditText.getText()
			// .toString().trim();
			// if (foldername != null
			// && foldername.length() != 0) {
			// saveAllFilesAs(foldername); // 另存为功能
			// } else {
			// Toast.makeText(
			// TR_Programming_Activity.this,
			// "名称为空，请重新输入名称",
			// Toast.LENGTH_SHORT).show();
			// }
			//
			// }
			// }).setNegativeButton("取消", null).show();
			//
			// break;
			// case R.id.menu_delete:// 删除文件夹功能
			// File delete = new File(Constans.mouldDataPATH);
			// final String[] deletefolders = delete.list();// 得到文件夹名
			// new AlertDialog.Builder(this)
			// .setTitle("请选择删除文件夹")
			// .setItems(deletefolders,
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// Constans.mouldData
			// .deleteFolder(Constans.mouldDataPATH
			// + deletefolders[which]);
			//
			// updateAdapter();
			//
			// Toast.makeText(
			// TR_Programming_Activity.this,
			// deletefolders[which] + "文件夹删除成功",
			// Toast.LENGTH_SHORT).show();
			// }
			// }).setNegativeButton("取消", null).show();
			//
			// break;
			// IAP功能
			/*	case R.id.menu_sendHexFile:

			AlarmQueryRunnable temp = new AlarmQueryRunnable();

			new Thread(temp).start();*/

			/*
			 * WifiSetting_Info.progressDialog = WifiSetting_Info.progressDialog.show(this, "烧写程序中", "请等待", true,
			 * false);
			 * 
			 * try { //文件放置的地址 String
			 * fileName=Constans.mechanicalParameterPATH+"APP_To_Bin.bin";
			 * //ByteArrayOutputStream byteArray=new ByteArrayOutputStream();
			 * DataInputStream dis = new DataInputStream(new
			 * FileInputStream(fileName));
			 * 
			 * byte[] buftemp = new byte[1]; //动态数组，由于不知道程序容量大小，只能浪费资源使用容器
			 * ArrayList<Byte> tempArrayList=new ArrayList<Byte>(); while
			 * ((dis.read(buftemp)!=-1)) { Byte tempByte=new Byte(buftemp[0]);
			 * tempArrayList.add(tempByte); } dis.close();
			 * 
			 * byte[] hexSendData=new byte[tempArrayList.size()];
			 * 
			 * Iterator<Byte> it =tempArrayList.iterator();
			 * 
			 * int offset=0; while (it.hasNext()) { byte[] temp=new byte[1];
			 * temp[0]=(Byte) it.next();
			 * 
			 * System.arraycopy(temp, 0, hexSendData, offset, 1); offset++; }
			 * //打印数据，方便调试 //HexDecoding.printHexString("hex数据",hexSendData);
			 * 
			 * //必须对数据格式类进行初始化，否则会返回一个空指针的错误 formatMessage=new
			 * WifiSendDataFormat(hexSendData, AddressPublic.sysProg_Head);
			 * 
			 * } catch (FileNotFoundException e) { Toast.makeText(this,
			 * "读取文件失败",Toast.LENGTH_SHORT).show(); e.printStackTrace(); } catch
			 * (IOException e) { e.printStackTrace(); }
			 * 
			 * //设定返回数据的响应函数 Client.setOnChatListener(hexFileChatlistener);
			 * 
			 * try { runOnUiThread(hexfileDownload); } catch (Exception e) { //
			 * TODO Auto-generated catch block e.printStackTrace();
			 * Toast.makeText(getApplicationContext(), e.toString(),
			 * Toast.LENGTH_SHORT).show(); //未连接主机出错提示
			 * runOnUiThread(nullErrordialogdissmiss); }
			 */

			//		break;

			/*case R.id.menu_keycode:

			// 发送keycode控制

			// 发送信息
			try {
				// 后设定线程控制listener
				sendDataRunnable = new SendDataRunnable(new WifiSendDataFormat(
						HexDecoding.int2byteArray4(1),
						AddressPublic.keycode_Head),
						TR_Programming_Activity.this);

				finishRunnable = new FinishRunnable(
						TR_Programming_Activity.this, "数据发送完毕");
				// 这种方式只是用于写数据，读数据时暂时不使用这种方式
				sendDataRunnable.setOnlistener(new NormalChatListenner(
						sendDataRunnable, finishRunnable));

				runOnUiThread(sendDataRunnable);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), e.toString(),
						Toast.LENGTH_SHORT).show();
			}

			break;
			 */
		case R.id.menu_activity_main:
			// Intent intent_main=new Intent();
			// intent_main.setClass(TR_Programming_Activity.this,
			// TR_Main_Activity.class);
			if(watchflag==true&&watchRunnable!=null)
				watchRunnable.destroy();
			startActivity(new Intent().setClass(TR_Programming_Activity.this,
					TR_Main_Activity.class));
			Device_ListFragment.fragmentncStatusFragment=null;
			Device_ListFragment.j=1;
			break;
		case R.id.menu_activity_setting:
			if(watchflag==true&&watchRunnable!=null)
			watchRunnable.destroy();
			startActivity(new Intent().setClass(TR_Programming_Activity.this,
					NewPragramActivity.class));
			Device_ListFragment.fragmentncStatusFragment=null;
			Device_ListFragment.j=1;
			break;
		case R.id.menu_activity_parameter_setting:
			if(watchflag==true&&watchRunnable!=null)
			watchRunnable.destroy();
			startActivity(new Intent().setClass(TR_Programming_Activity.this,
					TR_Parameter_Setting_Activity.class));
			Device_ListFragment.fragmentncStatusFragment=null;
			Device_ListFragment.j=1;
			break;
		case R.id.menu_activity_maintainGuide:
			if(watchflag==true&&watchRunnable!=null)
			watchRunnable.destroy();
			startActivity(new Intent().setClass(TR_Programming_Activity.this,
					TR_MaintainGuide_Activity.class));
			Device_ListFragment.fragmentncStatusFragment=null;
			Device_ListFragment.j=1;
			break;
		default:
			// 对没有处理的事件，交给父类来处理
			return super.onOptionsItemSelected(item);
		}
		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
		return true;
	}

	/**
	 * 
	 * @Title: filenew
	 * @Description: 新建模具文件结构，包括设备定义文件、NCEdit文件、TableEdit文件
	 * @return void 返回类型
	 * @throws
	 */
	private void filenew() {
		final EditText etEditText = new EditText(this);
		new AlertDialog.Builder(this).setTitle("请输入新文件夹名").setView(etEditText)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String foldername = etEditText.getText().toString()
						.trim();
				if (foldername != null && foldername.length() != 0) {
					creatFolderSysytem();
					newsaveflag = false;
					foldername_opened = foldername;

					Constans.mouldData
					.createFolder(Constans.mouldDataPATH,
							foldername)
							.createFile(Constans.设备名称数据)
							.createFile(Constans.nc文本数据)
							.createFile(Constans.table文本数据);
					TRFileHandler newFolder = TRFileHandler.create()
							.createFolder(
									Constans.mouldDataPATH + foldername
									+ File.separator,
									Constans.dataToDownloadFolder);

					// 清空显示的所有数据
					new_11before(ArrayListBound
							.getDeviceActualInputListData());
					new_11before(ArrayListBound
							.getDeviceActualOutputListData());
					new_11before(ArrayListBound
							.getDeviceMiddleInputListData());
					new_11before(ArrayListBound
							.getDeviceMiddleOutputListData());
					new_11before(ArrayListBound
							.getDeviceDataInputListData());
					new_11before(ArrayListBound
							.getDeviceDataOutputListData());
					new_11before(ArrayListBound
							.getDeviceRegisterListData());
					new_11before(ArrayListBound
							.getDeviceDataRegisterListData());
					new_11before(ArrayListBound.getDeviceTimerListData());
					new_11before(ArrayListBound
							.getDeviceCounterListData());
					new_11before(ArrayListBound
							.getDevicePositionListData());
					new_after(ArrayListBound.getDeviceAlarmListData());
					new_NCEdit1(ArrayListBound.getNCeditList1Data());
					new_NCEdit2(ArrayListBound.getNCeditList2Data());
					new_NCEdit3(ArrayListBound.getNCeditList3Data());
					new_NCEdit4(ArrayListBound.getNCeditList4Data());
					new_NCEdit5(ArrayListBound.getNCeditList5Data());
					new_NCEdit6(ArrayListBound.getNCeditList6Data());
					new_NCEdit7(ArrayListBound.getNCeditList7Data());
					new_NCEdit8(ArrayListBound.getNCeditList8Data());
					new_TableEdit1(ArrayListBound.getTableList1Data());
					new_TableEdit2(ArrayListBound.getTableList2Data());
					new_TableEdit3(ArrayListBound.getTableList3Data());
					new_TableEdit4(ArrayListBound.getTableList4Data());
					new_TableEdit5(ArrayListBound.getTableList5Data());
					new_TableEdit6(ArrayListBound.getTableList6Data());
					new_TableEdit7(ArrayListBound.getTableList7Data());
					new_TableEdit8(ArrayListBound.getTableList8Data());

					updateAdapter();
					Toast.makeText(TR_Programming_Activity.this,
							"已新建文件夹" + foldername, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(TR_Programming_Activity.this,
							"名称为空，请重新输入名称", Toast.LENGTH_SHORT).show();
				}

			}

		}).setNegativeButton("取消", null).show();
	}

	/**
	 * 
	 * @Title: new_8before
	 * @Description: 新建设备定义前八项
	 * @param ArrayList
	 * @return void 返回类型
	 * @throws
	 */
	private void new_11before(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("symbolNameEditText", "");
			ArrayList.get(i).put("signalNameEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}

	/**
	 * 
	 * @Title: new_4after
	 * @Description: 新建设备定义后四项
	 * @param ArrayList
	 * @return void 返回类型
	 * @throws
	 */
	private void new_after(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("symbolNameEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}

	/**
	 * 
	 * @Title: new_NCEdit
	 * @Description: 新建NCEdit
	 * @param ArrayList
	 * @return void 返回类型
	 * @throws
	 */
	private void new_NCEdit1(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("numSpinner", "");
			ArrayList.get(i).put("orderSpinner", "");
			ArrayList.get(i).put("operatText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_NCEdit2(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("numSpinner", "");
			ArrayList.get(i).put("orderSpinner", "");
			ArrayList.get(i).put("operatText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_NCEdit3(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("numSpinner", "");
			ArrayList.get(i).put("orderSpinner", "");
			ArrayList.get(i).put("operatText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_NCEdit4(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("numSpinner", "");
			ArrayList.get(i).put("orderSpinner", "");
			ArrayList.get(i).put("operatText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_NCEdit5(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("numSpinner", "");
			ArrayList.get(i).put("orderSpinner", "");
			ArrayList.get(i).put("operatText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_NCEdit6(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("numSpinner", "");
			ArrayList.get(i).put("orderSpinner", "");
			ArrayList.get(i).put("operatText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_NCEdit7(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("numSpinner", "");
			ArrayList.get(i).put("orderSpinner", "");
			ArrayList.get(i).put("operatText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_NCEdit8(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("numSpinner", "");
			ArrayList.get(i).put("orderSpinner", "");
			ArrayList.get(i).put("operatText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}

	/**
	 * 
	 * @Title: new_TableEdit
	 * @Description: 新建TableEdit
	 * @param ArrayList
	 * @return void
	 * @throws
	 */
	private void new_TableEdit1(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_TableEdit2(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_TableEdit3(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_TableEdit4(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_TableEdit5(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_TableEdit6(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_TableEdit7(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	private void new_TableEdit8(ArrayList<HashMap<String, Object>> ArrayList) {
		for (int i = 0; i < ArrayList.size(); i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
	}
	/**
	 * @param foldername
	 * @Title: openAllData
	 * @Description: 读取所有文件并显示，之前必须建好文件夹系统 ；同时读取到缓存文件夹
	 * @return void
	 * @throws
	 */
	public void openAllData(String foldername,int flag,Activity targetActivity) {

		errorString = "";
		hasError = false;
		creatFolderSysytem();

		// 每段程序的行数
		int step_DeviceActualInput = 0;
		int step_DeviceActualOutput = 0;
		int step_DeviceMiddleInput = 0;
		int step_DeviceMiddleOutput = 0;
		int step_DeviceDataInput = 0;
		int step_DeviceDataOutput = 0;
		int step_DeviceRegister = 0;
		int step_DeviceDataRegister = 0;
		int step_DeviceTimer = 0;
		int step_DeviceCounter = 0;
		int step_DevicePosition = 0;
		int step_DeviceAlarm = 0;
		int step_DeviceOptional = 0;

		//int step_NCEdit = 0;
		int step_NCEdit1 = 0;
		int step_NCEdit2 = 0;
		int step_NCEdit3 = 0;
		int step_NCEdit4 = 0;
		int step_NCEdit5 = 0;
		int step_NCEdit6 = 0;
		int step_NCEdit7 = 0;
		int step_NCEdit8 = 0;
		//int step_TableEdit = 0;
		int step_TableEdit1 = 0;
		int step_TableEdit2 = 0;
		int step_TableEdit3 = 0;
		int step_TableEdit4 = 0;
		int step_TableEdit5 = 0;
		int step_TableEdit6 = 0;
		int step_TableEdit7 = 0;
		int step_TableEdit8 = 0;

		// 初始化mark_list ，赋值为step都为0时的index
		ArrayList<HashMap<String, Integer>> mark_list = new ArrayList<HashMap<String, Integer>>();
		HashMap<String, Integer> map_mark = new HashMap<String, Integer>();
		map_mark.put("DeviceActualInput", 4);// 确定的位置
		map_mark.put("DeviceActualOutput", 9);// 以下是程序行数为0时的位置
		map_mark.put("DeviceMiddleInput", 14);
		map_mark.put("DeviceMiddleOutput", 19);
		map_mark.put("DeviceDataInput", 24);
		map_mark.put("DeviceDataOutput", 29);
		map_mark.put("DeviceRegister", 34);
		map_mark.put("DeviceDataRegister", 39);
		map_mark.put("DeviceTimer", 44);
		map_mark.put("DeviceCounter", 49);
		map_mark.put("DevicePosition", 54);
		map_mark.put("DeviceAlarm", 59);
		map_mark.put("DeviceOptional", 64);

		//map_mark.put("NCEdit", 4);
		map_mark.put("NCEdit1", 4);// 确定的位置
		map_mark.put("NCEdit2", 9);// 以下是程序行数为0时的位置
		map_mark.put("NCEdit3", 14);
		map_mark.put("NCEdit4", 19);
		map_mark.put("NCEdit5", 24);
		map_mark.put("NCEdit6", 29);
		map_mark.put("NCEdit7", 34);
		map_mark.put("NCEdit8", 39);
		
		//map_mark.put("TableEdit", 4);
		map_mark.put("TableEdit1", 4);// 确定的位置
		map_mark.put("TableEdit2", 9);// 以下是程序行数为0时的位置
		map_mark.put("TableEdit3", 14);
		map_mark.put("TableEdit4", 19);
		map_mark.put("TableEdit5", 24);
		map_mark.put("TableEdit6", 29);
		map_mark.put("TableEdit7", 34);
		map_mark.put("TableEdit8", 39);

		mark_list.add(map_mark);

		// 读取Device大数组
		String[] readlist_Device = Constans.mouldData
				.readOutFile(Constans.mouldDataPATH + foldername
						+ File.separator + Constans.设备名称数据);// 重启之后必须需要完整的路径
		//readlist_Device=new String[]{};
		for (int i = 0; i < readlist_Device.length; i++) {
			readlist_Device[i] = readlist_Device[i].replace("\n", "").trim();// 去除掉字符中的换行，否则字符转int时会出错
			//System.out.println("readlist_Device"+i+"="+readlist_Device[i].replace("\n", "").trim());
		}

		// 读取NCEdit大数组
		String[] readlist_NCEdit = Constans.mouldData
				.readOutFile(Constans.mouldDataPATH + foldername
						+ File.separator + Constans.nc文本数据);// 重启之后必须需要完整的路径
		for (int i = 0; i < readlist_NCEdit.length; i++) {
			readlist_NCEdit[i] = readlist_NCEdit[i].replace("\n", "").trim();// 去除掉字符中的换行，否则字符转int时会出错
		}

		// 读取TableEdit大数组
		String[] readlist_TableEdit = Constans.mouldData
				.readOutFile(Constans.mouldDataPATH + foldername
						+ File.separator + Constans.table文本数据);// 重启之后必须需要完整的路径
		for (int i = 0; i < readlist_TableEdit.length; i++) {
			readlist_TableEdit[i] = readlist_TableEdit[i].replace("\n", "")
					.trim();// 去除掉字符中的换行，否则字符转int时会出错
		}

		if (readlist_Device.length < 59) {
			// errorString = "设备定义文件表头不完整";
			 new AlertDialog.Builder(targetActivity).setTitle("提示")
			   .setMessage("未载入！设备定义文件表头不完整，请检查文件。")
			   .setPositiveButton("知道了", null).show();
		}else if (readlist_NCEdit.length < 39) {
			// errorString = "设备定义文件表头不完整";
			 new AlertDialog.Builder(targetActivity).setTitle("提示")
			   .setMessage("未载入！NC文件表头不完整，请检查文件。")
			   .setPositiveButton("知道了", null).show();
		}else if (readlist_TableEdit.length <39) {
			// errorString = "设备定义文件表头不完整";
			 new AlertDialog.Builder(targetActivity).setTitle("提示")
			   .setMessage("未载入！Table文件表头不完整，请检查文件。")
			   .setPositiveButton("知道了", null).show();
		} else {
			// 得到markname的index和各段的程序行数
			// step_DeviceActualInput = Integer.parseInt(readlist[3].replace(
			// "Step=", ""));

			mark_list.get(0).put("DeviceActualInput", 4);
			for (int i = 0; i < readlist_Device.length; i++) {
				if (readlist_Device[i].equals("[DeviceActualOutput]")) {
					mark_list.get(0).put("DeviceActualOutput", i);
					// step_DeviceActualOutput = Integer.parseInt(readlist[i -
					// 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DeviceMiddleInput]")) {
					mark_list.get(0).put("DeviceMiddleInput", i);
					// step_DeviceMiddleInput = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DeviceMiddleOutput]")) {
					mark_list.get(0).put("DeviceMiddleOutput", i);
					// step_DeviceMiddleOutput = Integer.parseInt(readlist[i -
					// 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DeviceDataInput]")) {
					mark_list.get(0).put("DeviceDataInput", i);
					// step_DeviceDataInput = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DeviceDataOutput]")) {
					mark_list.get(0).put("DeviceDataOutput", i);
					// step_DeviceDataOutput = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DeviceRegister]")) {
					mark_list.get(0).put("DeviceRegister", i);
					// step_DeviceRegister = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DeviceDataRegister]")) {
					mark_list.get(0).put("DeviceDataRegister", i);
					// step_DeviceDataRegister = Integer.parseInt(readlist[i -
					// 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DeviceTimer]")) {
					mark_list.get(0).put("DeviceTimer", i);
					// step_DeviceTimer = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DeviceCounter]")) {
					mark_list.get(0).put("DeviceCounter", i);
					// step_DeviceCounter = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DevicePosition]")) {
					mark_list.get(0).put("DevicePosition", i);
					// step_DevicePosition = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_Device[i].equals("[DeviceAlarm]")) {
					mark_list.get(0).put("DeviceAlarm", i);
					// step_DeviceAlarm = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				}else if (readlist_Device[i].equals("[DeviceOptional]")) {
					mark_list.get(0).put("DeviceOptional", i);
					// step_DeviceAlarm = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				}
			}
			mark_list.get(0).put("NCEdit1", 4);
			for (int i = 0; i < readlist_NCEdit.length; i++) {
				if (readlist_NCEdit[i].equals("[NCEdit1]")) {
					mark_list.get(0).put("NCEdit1", i);
					// step_DeviceActualOutput = Integer.parseInt(readlist[i -
					// 1]
					// .replace("Step=", ""));
				} else if (readlist_NCEdit[i].equals("[NCEdit2]")) {
					mark_list.get(0).put("NCEdit2", i);
					// step_DeviceMiddleInput = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_NCEdit[i].equals("[NCEdit3]")) {
					mark_list.get(0).put("NCEdit3", i);
					// step_DeviceMiddleOutput = Integer.parseInt(readlist[i -
					// 1]
					// .replace("Step=", ""));
				} else if (readlist_NCEdit[i].equals("[NCEdit4]")) {
					mark_list.get(0).put("NCEdit4", i);
					// step_DeviceDataInput = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_NCEdit[i].equals("[NCEdit5]")) {
					mark_list.get(0).put("NCEdit5", i);
					// step_DeviceDataOutput = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_NCEdit[i].equals("[NCEdit6]")) {
					mark_list.get(0).put("NCEdit6", i);
					// step_DeviceRegister = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_NCEdit[i].equals("[NCEdit7]")) {
					mark_list.get(0).put("NCEdit7", i);
					// step_DeviceDataRegister = Integer.parseInt(readlist[i -
					// 1]
					// .replace("Step=", ""));
				} else if (readlist_NCEdit[i].equals("[NCEdit8]")) {
					mark_list.get(0).put("NCEdit8", i);
					// step_DeviceTimer = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} 
			}
			
			mark_list.get(0).put("TableEdit1", 4);
			for (int i = 0; i < readlist_TableEdit.length; i++) {
				if (readlist_TableEdit[i].equals("[TableEdit1]")) {
					mark_list.get(0).put("TableEdit1", i);
					// step_DeviceActualOutput = Integer.parseInt(readlist[i -
					// 1]
					// .replace("Step=", ""));
				} else if (readlist_TableEdit[i].equals("[TableEdit2]")) {
					mark_list.get(0).put("TableEdit2", i);
					// step_DeviceMiddleInput = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_TableEdit[i].equals("[TableEdit3]")) {
					mark_list.get(0).put("TableEdit3", i);
					// step_DeviceMiddleOutput = Integer.parseInt(readlist[i -
					// 1]
					// .replace("Step=", ""));
				} else if (readlist_TableEdit[i].equals("[TableEdit4]")) {
					mark_list.get(0).put("TableEdit4", i);
					// step_DeviceDataInput = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_TableEdit[i].equals("[TableEdit5]")) {
					mark_list.get(0).put("TableEdit5", i);
					// step_DeviceDataOutput = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_TableEdit[i].equals("[TableEdit6]")) {
					mark_list.get(0).put("TableEdit6", i);
					// step_DeviceRegister = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} else if (readlist_TableEdit[i].equals("[TableEdit7]")) {
					mark_list.get(0).put("TableEdit7", i);
					// step_DeviceDataRegister = Integer.parseInt(readlist[i -
					// 1]
					// .replace("Step=", ""));
				} else if (readlist_TableEdit[i].equals("[TableEdit8]")) {
					mark_list.get(0).put("TableEdit8", i);
					// step_DeviceTimer = Integer.parseInt(readlist[i - 1]
					// .replace("Step=", ""));
				} 
			}
			// 这才是真正的步长，可以用于检查错误
			step_DeviceActualInput = (mark_list.get(0)
					.get("DeviceActualOutput").intValue()
					- 4 - mark_list.get(0).get("DeviceActualInput").intValue() - 1) / 4;
			step_DeviceActualOutput = (mark_list.get(0)
					.get("DeviceMiddleInput").intValue()
					- 4 - mark_list.get(0).get("DeviceActualOutput").intValue() - 1) / 4;
			step_DeviceMiddleInput = (mark_list.get(0)
					.get("DeviceMiddleOutput").intValue()
					- 4 - mark_list.get(0).get("DeviceMiddleInput").intValue() - 1) / 4;
			step_DeviceMiddleOutput = (mark_list.get(0).get("DeviceDataInput")
					.intValue()
					- 4 - mark_list.get(0).get("DeviceMiddleOutput").intValue() - 1) / 4;
			step_DeviceDataInput = (mark_list.get(0).get("DeviceDataOutput")
					.intValue()
					- 4 - mark_list.get(0).get("DeviceDataInput").intValue() - 1) / 4;
			step_DeviceDataOutput = (mark_list.get(0).get("DeviceRegister")
					.intValue()
					- 4 - mark_list.get(0).get("DeviceDataOutput").intValue() - 1) / 4;
			step_DeviceRegister = (mark_list.get(0).get("DeviceDataRegister")
					.intValue()
					- 4 - mark_list.get(0).get("DeviceRegister").intValue() - 1) / 4;
			step_DeviceDataRegister = (mark_list.get(0).get("DeviceTimer")
					.intValue()
					- 4 - mark_list.get(0).get("DeviceDataRegister").intValue() - 1) / 4;
			step_DeviceTimer = (mark_list.get(0).get("DeviceCounter")
					.intValue()
					- 4 - mark_list.get(0).get("DeviceTimer").intValue() - 1) / 5;
			step_DeviceCounter = (mark_list.get(0).get("DevicePosition")
					.intValue()
					- 4 - mark_list.get(0).get("DeviceCounter").intValue() - 1) / 5;
			step_DevicePosition = (mark_list.get(0).get("DeviceAlarm")
					.intValue()
					- 4 - mark_list.get(0).get("DevicePosition").intValue() - 1) / 5;
			step_DeviceAlarm = (mark_list.get(0).get("DeviceOptional")
					.intValue()
					- 4 - mark_list.get(0).get("DeviceAlarm").intValue() - 1) / 3;
			step_DeviceOptional = (readlist_Device.length - mark_list.get(0)
					.get("DeviceOptional").intValue()) / 4;

			/*step_NCEdit = (readlist_NCEdit.length - mark_list.get(0)
					.get("NCEdit").intValue()) / 5;DeviceOptional*/
			step_NCEdit1 = (mark_list.get(0)
					.get("NCEdit2").intValue()
					- 4 - mark_list.get(0).get("NCEdit1").intValue() - 1)  / 5;
			step_NCEdit2 = (mark_list.get(0)
					.get("NCEdit3").intValue()
					- 4 - mark_list.get(0).get("NCEdit2").intValue() - 1)  / 5;
			step_NCEdit3 = (mark_list.get(0)
					.get("NCEdit4").intValue()
					- 4 - mark_list.get(0).get("NCEdit3").intValue() - 1)  / 5;
			step_NCEdit4 = (mark_list.get(0)
					.get("NCEdit5").intValue()
					- 4 - mark_list.get(0).get("NCEdit4").intValue() - 1)  / 5;
			step_NCEdit5 = (mark_list.get(0)
					.get("NCEdit6").intValue()
					- 4 - mark_list.get(0).get("NCEdit5").intValue() - 1)  / 5;
			step_NCEdit6 = (mark_list.get(0)
					.get("NCEdit7").intValue()
					- 4 - mark_list.get(0).get("NCEdit6").intValue() - 1)  / 5;
			step_NCEdit7 = (mark_list.get(0)
					.get("NCEdit8").intValue()
					- 4 - mark_list.get(0).get("NCEdit7").intValue() - 1)  / 5;
			step_NCEdit8 = (readlist_NCEdit.length
					- 4 - mark_list.get(0).get("NCEdit8").intValue() - 1)  / 5;

			/*step_TableEdit = (readlist_TableEdit.length - mark_list.get(0)
					.get("TableEdit").intValue()) / 4;*/
			step_TableEdit1 = (mark_list.get(0)
					.get("TableEdit2").intValue()
					- 4 - mark_list.get(0).get("TableEdit1").intValue() - 1)  / 4;
			step_TableEdit2 = (mark_list.get(0)
					.get("TableEdit3").intValue()
					- 4 - mark_list.get(0).get("TableEdit2").intValue() - 1)  / 4;
			step_TableEdit3 = (mark_list.get(0)
					.get("TableEdit4").intValue()
					- 4 - mark_list.get(0).get("TableEdit3").intValue() - 1)  / 4;
			step_TableEdit4 = (mark_list.get(0)
					.get("TableEdit5").intValue()
					- 4 - mark_list.get(0).get("TableEdit4").intValue() - 1)  / 4;
			step_TableEdit5 = (mark_list.get(0)
					.get("TableEdit6").intValue()
					- 4 - mark_list.get(0).get("TableEdit5").intValue() - 1)  / 4;
			step_TableEdit6 = (mark_list.get(0)
					.get("TableEdit7").intValue()
					- 4 - mark_list.get(0).get("TableEdit6").intValue() - 1)  / 4;
			step_TableEdit7 = (mark_list.get(0)
					.get("TableEdit8").intValue()
					- 4 - mark_list.get(0).get("TableEdit7").intValue() - 1)  / 4;
			step_TableEdit8 = (readlist_TableEdit.length
					- 4 - mark_list.get(0).get("TableEdit8").intValue() - 1)  / 4;

			// 先检查错误，没有错误再显示
			hasError = checkError_Device8before(hasError,
					step_DeviceActualInput, readlist_Device, mark_list,
					"DeviceActualInput", "X");
			System.out.println(hasError);
			hasError = checkError_Device8before(hasError,
					step_DeviceActualOutput, readlist_Device, mark_list,
					"DeviceActualOutput", "Y");
			System.out.println(hasError);
			hasError = checkError_Device8before(hasError,
					step_DeviceMiddleInput, readlist_Device, mark_list,
					"DeviceMiddleInput", "MX");
			System.out.println(hasError);
			hasError = checkError_Device8before(hasError,
					step_DeviceMiddleOutput, readlist_Device, mark_list,
					"DeviceMiddleOutput", "MY");
			System.out.println(hasError);
			hasError = checkError_Device8before(hasError, step_DeviceDataInput,
					readlist_Device, mark_list, "DeviceDataInput", "DX");
			System.out.println(hasError);
			hasError = checkError_Device8before(hasError,
					step_DeviceDataOutput, readlist_Device, mark_list,
					"DeviceDataOutput", "DY");
			System.out.println(hasError);
			hasError = checkError_Device8before(hasError, step_DeviceRegister,
					readlist_Device, mark_list, "DeviceRegister", "LM");
			System.out.println(hasError);
			hasError = checkError_Device8before(hasError,
					step_DeviceDataRegister, readlist_Device, mark_list,
					"DeviceDataRegister", "LD");
			System.out.println(hasError);
			hasError = checkError_Device2after(hasError, step_DeviceTimer,
					readlist_Device, mark_list, "DeviceTimer", "T");
			System.out.println(hasError);
			hasError = checkError_Device2after(hasError, step_DeviceCounter,
					readlist_Device, mark_list, "DeviceCounter", "C");
			System.out.println(hasError);
			hasError = checkError_DevicePosition(hasError, step_DevicePosition,
					readlist_Device, mark_list);
			System.out.println(hasError);
			hasError = checkError_DeviceAlarm(hasError, step_DeviceAlarm,
					readlist_Device, mark_list);
			System.out.println(hasError);
			hasError = checkError_DeviceOptional(hasError, step_DeviceOptional,
					readlist_Device, mark_list);
			System.out.println(hasError);
			
			hasError = checkError_NCEdit(hasError, readlist_NCEdit,
					step_NCEdit1, mark_list,"1");
			System.out.println(hasError);
			hasError = checkError_NCEdit(hasError, readlist_NCEdit,
					step_NCEdit2, mark_list,"2");
			System.out.println(hasError);
			hasError = checkError_NCEdit(hasError, readlist_NCEdit,
					step_NCEdit3, mark_list,"3");
			System.out.println(hasError);
			hasError = checkError_NCEdit(hasError, readlist_NCEdit,
					step_NCEdit4, mark_list,"4");
			System.out.println(hasError);
			hasError = checkError_NCEdit(hasError, readlist_NCEdit,
					step_NCEdit5, mark_list,"5");
			System.out.println(hasError);
			hasError = checkError_NCEdit(hasError, readlist_NCEdit,
					step_NCEdit6, mark_list,"6");
			System.out.println(hasError);
			hasError = checkError_NCEdit(hasError, readlist_NCEdit,
					step_NCEdit7, mark_list,"7");
			System.out.println(hasError);
			hasError = checkError_NCEdit(hasError, readlist_NCEdit,
					step_NCEdit8, mark_list,"8");
			System.out.println(hasError);
			
			/*hasError = checkError_TableEdit(hasError, readlist_TableEdit,
					step_TableEdit, mark_list);
			System.out.println(hasError);*/

			hasError = checkError_TableEdit(hasError, readlist_TableEdit,
					step_TableEdit1, mark_list,"1");
			System.out.println(hasError);
			hasError = checkError_TableEdit(hasError, readlist_TableEdit,
					step_TableEdit2, mark_list,"2");
			System.out.println(hasError);
			hasError = checkError_TableEdit(hasError, readlist_TableEdit,
					step_TableEdit3, mark_list,"3");
			System.out.println(hasError);
			hasError = checkError_TableEdit(hasError, readlist_TableEdit,
					step_TableEdit4, mark_list,"4");
			System.out.println(hasError);
			hasError = checkError_TableEdit(hasError, readlist_TableEdit,
					step_TableEdit5, mark_list,"5");
			System.out.println(hasError);
			hasError = checkError_TableEdit(hasError, readlist_TableEdit,
					step_TableEdit6, mark_list,"6");
			System.out.println(hasError);
			hasError = checkError_TableEdit(hasError, readlist_TableEdit,
					step_TableEdit7, mark_list,"7");
			System.out.println(hasError);
			hasError = checkError_TableEdit(hasError, readlist_TableEdit,
					step_TableEdit8, mark_list,"8");
			System.out.println(hasError);
			// 没错误则显示
			if (!hasError) {
				read_8before(ArrayListBound.getDeviceActualInputListData(),
						"DeviceActualInput", readlist_Device,
						step_DeviceActualInput, mark_list,targetActivity);

				read_8before(ArrayListBound.getDeviceActualOutputListData(),
						"DeviceActualOutput", readlist_Device,
						step_DeviceActualOutput, mark_list,targetActivity);

				read_8before(ArrayListBound.getDeviceMiddleInputListData(),
						"DeviceMiddleInput", readlist_Device,
						step_DeviceMiddleInput, mark_list,targetActivity);

				read_8before(ArrayListBound.getDeviceMiddleOutputListData(),
						"DeviceMiddleOutput", readlist_Device,
						step_DeviceMiddleOutput, mark_list,targetActivity);

				read_8before(ArrayListBound.getDeviceDataInputListData(),
						"DeviceDataInput", readlist_Device,
						step_DeviceDataInput, mark_list,targetActivity);

				read_8before(ArrayListBound.getDeviceDataOutputListData(),
						"DeviceDataOutput", readlist_Device,
						step_DeviceDataOutput, mark_list,targetActivity);

				read_8before(ArrayListBound.getDeviceRegisterListData(),
						"DeviceRegister", readlist_Device, step_DeviceRegister,
						mark_list,targetActivity);

				read_8before(ArrayListBound.getDeviceDataRegisterListData(),
						"DeviceDataRegister", readlist_Device,
						step_DeviceDataRegister, mark_list,targetActivity);

				read_4after(ArrayListBound.getDeviceTimerListData(),
						"DeviceTimer", readlist_Device, step_DeviceTimer,
						mark_list,targetActivity);

				read_4after(ArrayListBound.getDeviceCounterListData(),
						"DeviceCounter", readlist_Device, step_DeviceCounter,
						mark_list,targetActivity);

				read_4after(ArrayListBound.getDevicePositionListData(),
						"DevicePosition", readlist_Device, step_DevicePosition,
						mark_list,targetActivity);

				read_4after(ArrayListBound.getDeviceAlarmListData(),
						"DeviceAlarm", readlist_Device, step_DeviceAlarm,
						mark_list,targetActivity);
				
				read_Optional(ArrayListBound.getDeviceOptionalListData(),
						"DeviceOptional", readlist_Device, step_DeviceOptional,
						mark_list,targetActivity);

				//read_NCEdit(readlist_NCEdit, step_NCEdit);
				read_NCEdit(ArrayListBound.getNCeditList1Data(),
						"NCEdit1", readlist_NCEdit, step_NCEdit1,
						mark_list,targetActivity);
				read_NCEdit(ArrayListBound.getNCeditList2Data(),
						"NCEdit2", readlist_NCEdit, step_NCEdit2,
						mark_list,targetActivity);
				read_NCEdit(ArrayListBound.getNCeditList3Data(),
						"NCEdit3", readlist_NCEdit, step_NCEdit3,
						mark_list,targetActivity);
				read_NCEdit(ArrayListBound.getNCeditList4Data(),
						"NCEdit4", readlist_NCEdit, step_NCEdit4,
						mark_list,targetActivity);
				read_NCEdit(ArrayListBound.getNCeditList5Data(),
						"NCEdit5", readlist_NCEdit, step_NCEdit5,
						mark_list,targetActivity);
				read_NCEdit(ArrayListBound.getNCeditList6Data(),
						"NCEdit6", readlist_NCEdit, step_NCEdit6,
						mark_list,targetActivity);
				read_NCEdit(ArrayListBound.getNCeditList7Data(),
						"NCEdit7", readlist_NCEdit, step_NCEdit7,
						mark_list,targetActivity);
				read_NCEdit(ArrayListBound.getNCeditList8Data(),
						"NCEdit8", readlist_NCEdit, step_NCEdit8,
						mark_list,targetActivity);

				//read_TableEdit(readlist_TableEdit, step_TableEdit);
				read_TableEdit(ArrayListBound.getTableList1Data(),
						"TableEdit1", readlist_TableEdit, step_TableEdit1,
						mark_list,targetActivity);
				read_TableEdit(ArrayListBound.getTableList2Data(),
						"TableEdit2", readlist_TableEdit, step_TableEdit2,
						mark_list,targetActivity);
				read_TableEdit(ArrayListBound.getTableList3Data(),
						"TableEdit3", readlist_TableEdit, step_TableEdit3,
						mark_list,targetActivity);
				read_TableEdit(ArrayListBound.getTableList4Data(),
						"TableEdit4", readlist_TableEdit, step_TableEdit4,
						mark_list,targetActivity);
				read_TableEdit(ArrayListBound.getTableList5Data(),
						"TableEdit5", readlist_TableEdit, step_TableEdit5,
						mark_list,targetActivity);
				read_TableEdit(ArrayListBound.getTableList6Data(),
						"TableEdit6", readlist_TableEdit, step_TableEdit6,
						mark_list,targetActivity);
				read_TableEdit(ArrayListBound.getTableList7Data(),
						"TableEdit7", readlist_TableEdit, step_TableEdit7,
						mark_list,targetActivity);
				read_TableEdit(ArrayListBound.getTableList8Data(),
						"TableEdit8", readlist_TableEdit, step_TableEdit8,
						mark_list,targetActivity);

				// byteArray=readMouldData2();//提供传输的接口

				// // 更新显示
				// updateAdapter();
				// // 两个标记
				// foldername_opened = foldername;
				// newsaveflag = false;

				// Toast.makeText(this, "文件读取成功", Toast.LENGTH_SHORT).show();
				if(flag==0){
				new AlertDialog.Builder(targetActivity).setTitle("提示")
				.setMessage("已成功读取上次打开的文件")
				.setPositiveButton("知道了", null).show();}
				//firstOpen = false;

			} else {
				new AlertDialog.Builder(targetActivity).setTitle("提示")
				.setMessage(errorString)
				.setPositiveButton("知道了", null).show();
				//Toast.makeText(targetActivity, "文件存在错误" + errorString, Toast.LENGTH_SHORT).show();
				System.out.println("文件存在错误" + errorString);
			}

		}

	}

	/**
	 * 
	 * @Title: checkError_Device8before
	 * @Description: 检查设备定义中前8项错误
	 * @param hasError
	 *            每检查一项，确定是否存在错误
	 * @param step_Device
	 *            设备定义中各项的程序行数
	 * @param readlist
	 *            直接从文件中读取的大数组
	 * @param mark_list
	 *            存放每项的标志
	 * @param markName
	 *            每项的标志名
	 * @param headString
	 *            符号名的起始字母
	 * @return boolean
	 * @throws
	 */
	private boolean checkError_Device8before(boolean hasError, int step_Device,
			String[] readlist, ArrayList<HashMap<String, Integer>> mark_list,
			String markName, String headString) {
		try {
		if (!hasError) {
			if(step_Device<0){
					errorString += "设备定义"+markName+"的后面几项存在错误，请检查文件！";
					hasError = true;
				}
			for (int i = 0; i < step_Device; i++) {
				try{
				    Integer.valueOf(readlist[4 * i+ mark_list.get(0).get(markName) + 1]);
				}catch(Exception e) {
					// TODO: handle exception
					errorString += "设备定义"+markName+"中第"+i+"行附近存在错误";
					hasError = true;
				    break;
				}
				String stringToCheck = readlist[4 * i+ mark_list.get(0).get(markName) + 2].toString().trim();
				if (!stringToCheck.equals("")) {
					if(markName.equals("DeviceActualInput")||markName.equals("DeviceActualOutput")){
					if ((!stringToCheck.startsWith(headString))
							|| ((stringToCheck.getBytes("gb2312")).length > 8)) {
						try{
						    errorString += "设备定义"+markName+"中序号为"+Integer.valueOf(readlist[4 * i+ mark_list.get(0).get(markName) + 1])+"这一行存在语法错误，符号名错误";
						}catch(Exception e) {
							// TODO: handle exception
							errorString += "设备定义"+markName+"中第"+i+"行附近存在错误";
						}
						hasError = true;
						System.out.println("文件存在错误  " + errorString);
						break;// 有错误就跳出循环
					} else if (((readlist[4 * i + mark_list.get(0).get(markName)+ 3].getBytes("gb2312")).length > 24)
					                     || ((readlist[4 * i + mark_list.get(0).get(markName)+ 4].getBytes("gb2312")).length > 80)) {
						
						try{
						     errorString +="设备定义"+markName+"中序号为"+Integer.valueOf(readlist[4 * i+ mark_list.get(0).get(markName) + 1])+"这一行存在语法错误，信号名或注释长度错误";
						}catch(Exception e) {
							// TODO: handle exception
							errorString += "设备定义"+markName+"中第"+i+"行附近存在错误";
						}
						hasError = true;
						System.out.println("文件存在错误  " + errorString);
						break;
					}
					}else{
						if ((!stringToCheck.startsWith(headString))
								|| ((stringToCheck.getBytes("gb2312")).length > 20)) {
							try{
							    errorString += "设备定义"+markName+"中序号为"+Integer.valueOf(readlist[4 * i+ mark_list.get(0).get(markName) + 1])+"这一行存在语法错误，符号名错误";
							}catch(Exception e) {
								// TODO: handle exception
								errorString += "设备定义"+markName+"中第"+i+"行附近存在错误";
							}
							hasError = true;
							System.out.println("文件存在错误  " + errorString);
							break;// 有错误就跳出循环
						} else if (((readlist[4 * i + mark_list.get(0).get(markName)+ 3].getBytes("gb2312")).length >40)
						                     || ((readlist[4 * i + mark_list.get(0).get(markName)
						                                  + 4].getBytes("gb2312")).length > 80)) {
							
							try{
							     errorString +="设备定义"+markName+"中序号为"+Integer.valueOf(readlist[4 * i+ mark_list.get(0).get(markName) + 1])+"这一行存在语法错误，信号名或注释长度错误";
							}catch(Exception e) {
								// TODO: handle exception
								errorString += "设备定义"+markName+"中第"+i+"行附近存在错误";
							}
							hasError = true;
							System.out.println("文件存在错误  " + errorString);
							break;
						}
					}
				}

			}
		}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasError;
	}

	/**
	 * 
	 * @Title: checkError_Device2after
	 * @Description: 检查设备定义定时器和计数器的错误
	 * @param hasError
	 * @param step_Device
	 * @param readlist
	 * @param mark_list
	 * @param markName
	 * @param headString
	 * @return boolean
	 * @throws
	 */
	private boolean checkError_Device2after(boolean hasError, int step_Device,
			String[] readlist, ArrayList<HashMap<String, Integer>> mark_list,
			String markName, String headString) {
		try{
		if (!hasError) {
			if(step_Device<0){
				errorString += "设备定义"+markName+"的后面几项存在错误，请检查文件！";
				hasError = true;
			}
			for (int i = 0; i < step_Device; i++) {
				 try{
					   Integer.valueOf(readlist[5 * i+ mark_list.get(0).get(markName) + 1]);
					}catch(Exception e) {
						// TODO: handle exception
						errorString += "设备定义"+markName+"中第"+i+"行附近存在错误";
						hasError = true;
					    break;
					}
				String stringToCheck = readlist[5 * i
				                                + mark_list.get(0).get(markName) + 2].toString().trim();
				if (!stringToCheck.equals("")) {
					if ((!stringToCheck.startsWith(headString))
							|| ((stringToCheck.getBytes("gb2312")).length > 8)) {
						try{
						    errorString += "设备定义"+markName+"中序号为"+Integer.valueOf(readlist[5 * i+ mark_list.get(0).get(markName) + 1])+"这一行存在语法错误，符号名错误";
						}catch(Exception e) {
							// TODO: handle exception
							errorString += "设备定义"+markName+"中第"+i+"行附近存在错误";
						}
						hasError = true;
						break;// 有错误就跳出循环
					} else if ((readlist[5 * i + mark_list.get(0).get(markName)+ 3].getBytes("gb2312")).length > 24||
							(readlist[5 * i + mark_list.get(0).get(markName)+ 4].getBytes("gb2312")).length > 80) {
						try{
						    errorString += "设备定义"+markName+"中序号为"+Integer.valueOf(readlist[5 * i+ mark_list.get(0).get(markName) + 1])+"这一行存在语法错误，信号名或注释长度错误";
						}catch(Exception e) {
							// TODO: handle exception
							errorString += "设备定义"+markName+"中第"+i+"行附近存在错误";
						}
						hasError = true;
						break;
					}
				}
			}
		}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasError;
	}

	/**
	 * 
	 * @Title: checkError_DevicePosition
	 * @Description: 单独检查设备定义位置项的错误 ，因为headString有Sp、FP、P三种
	 * @param hasError
	 * @param step_DevicePosition
	 * @param readlist
	 * @param mark_list
	 * @return boolean
	 * @throws
	 */
	private boolean checkError_DevicePosition(boolean hasError,
			int step_DevicePosition, String[] readlist,
			ArrayList<HashMap<String, Integer>> mark_list) {
		try{
		if (!hasError) {
			if(step_DevicePosition<0){
				errorString += "设备定义位置的后面几项存在错误，请检查文件！";
				hasError = true;
			}
			for (int i = 0; i < step_DevicePosition; i++) {
				try{
				    Integer.valueOf(readlist[5 * i + mark_list.get(0).get("DevicePosition")+ 1]);
				}catch(Exception e) {
					// TODO: handle exception
					errorString += "设备定义位置中第"+i+"行附近存在错误";
					hasError = true;
				    break;
				}
				String stringToCheck=readlist[5 * i + mark_list.get(0).get("DevicePosition")+ 2].toString().trim();
				if (!stringToCheck.equals("")) {
				if (!(readlist[5 * i + mark_list.get(0).get("DevicePosition")
				               + 2].startsWith("SP")
				               || readlist[5 * i
				                           + mark_list.get(0).get("DevicePosition") + 2]
				                        		   .startsWith("FP") || readlist[5 * i
				                        		                                 + mark_list.get(0).get("DevicePosition") + 2]
				                        		                                		 .startsWith("P"))
				                        		                                		 || ((readlist[5 * i+ mark_list.get(0).get("DevicePosition") + 2].getBytes("gb2312")).length > 8)) {
					try{
					    errorString += "设备定义位置中序号为"+Integer.valueOf(readlist[5 * i+ mark_list.get(0).get("DevicePosition") + 1])+"这一行存在语法错误，符号名错误";
					}catch(Exception e) {
						// TODO: handle exception
						errorString += "设备定义位置中第"+i+"行附近存在错误";
					}
					hasError = true;
					break;// 有错误就跳出循环
				} else if ((readlist[5 * i+ mark_list.get(0).get("DevicePosition") + 3].getBytes("gb2312")).length > 24||
						(readlist[5 * i+ mark_list.get(0).get("DevicePosition") + 4].getBytes("gb2312")).length > 80) {
					try{
					errorString += "设备定义位置中序号为"+Integer.valueOf(readlist[5 * i+ mark_list.get(0).get("DevicePosition") + 1])+"这一行存在语法错误，信号名或注释长度错误";
					}catch(Exception e) {
						// TODO: handle exception
						errorString += "设备定义位置中第"+i+"行附近存在错误";
					}
					hasError = true;
					break;
				}
				}
			}
		}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasError;
	}

	/**
	 * 
	 * @Title: checkError_DeviceAlarm
	 * @Description: 单独检查设备定义警报项 的错误，因为警报项符号名为纯数字
	 * @param hasError
	 * @param step_DeviceAlarm
	 * @param readlist
	 * @param mark_list
	 * @return boolean
	 * @throws
	 */
	private boolean checkError_DeviceAlarm(boolean hasError,
			int step_DeviceAlarm, String[] readlist,
			ArrayList<HashMap<String, Integer>> mark_list) {
       try{
		if (!hasError) {
			if(step_DeviceAlarm<0){
				errorString += "设备定义警报的后面几项存在错误，请检查文件！";
				hasError = true;
			}
			for (int i = 0; i < step_DeviceAlarm; i++) {
				try{
				    Integer.valueOf(readlist[3 * i + mark_list.get(0).get("DeviceAlarm") + 1]);
				}catch(Exception e) {
					// TODO: handle exception
					errorString += "设备定义警报中第"+i+"行附近存在错误";
					hasError = true;
				    break;
				}
				String stringToCheck=readlist[3 * i + mark_list.get(0).get("DeviceAlarm") + 2].toString().trim();
				if (!stringToCheck.equals("")) {
				if (!(readlist[3 * i + mark_list.get(0).get("DeviceAlarm") + 2]
						.matches("[0-9]+"))// 正则表达式，判断是否全部是数字
						|| (readlist[3 * i + mark_list.get(0).get("DeviceAlarm")
						            + 2].getBytes("gb2312")).length > 8) {
					try{
					    errorString += "设备定义警报中序号为"+Integer.valueOf(readlist[3 * i+ mark_list.get(0).get("DeviceAlarm") + 1])+"这一行存在语法错误，符号名错误";
					}catch(Exception e) {
						// TODO: handle exception
						errorString += "设备定义警报中第"+i+"行附近存在错误";
					}
					hasError = true;
					break;// 有错误就跳出循环
				} else if ((readlist[3 * i + mark_list.get(0).get("DeviceAlarm")+ 3].getBytes("gb2312")).length > 60) {
					try{
					    errorString += "设备定义警报中序号为"+Integer.valueOf(readlist[3 * i+ mark_list.get(0).get("DeviceAlarm") + 1])+"这一行存在语法错误，注释长度错误";
					}catch(Exception e) {
						// TODO: handle exception
						errorString += "设备定义警报中第"+i+"行附近存在错误";
					}
					hasError = true;
					break;
				}
				}
			}
		}
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		return hasError;
	}
	private boolean checkError_DeviceOptional(boolean hasError, int step_Device,
			String[] readlist, ArrayList<HashMap<String, Integer>> mark_list) {
		try{
		if (!hasError) {
			if(step_Device<0){
				errorString += "设备定义选件操作的后面几项存在错误，请检查文件！";
				hasError = true;
			}
			for (int i = 0; i < step_Device; i++) {
				try{
				    Integer.valueOf(readlist[4 * i + mark_list.get(0).get("DeviceOptional") + 1]);
				}catch(Exception e) {
					// TODO: handle exception
					errorString += "设备定义选件操作中第"+i+"行附近存在错误";
					hasError = true;
				    break;
				}
				String name1=readlist[4 * i + mark_list.get(0).get("DeviceOptional") + 2].toString().trim();
				String name2=readlist[4 * i + mark_list.get(0).get("DeviceOptional") + 3].toString().trim();
				String name3=readlist[4 * i + mark_list.get(0).get("DeviceOptional") + 4].toString().trim();
				if ((!name1.equals(""))||(!name2.equals(""))||(!name3.equals(""))) {
					if(((name1.getBytes("gb2312")).length > 16)||((name2.getBytes("gb2312")).length > 8)||((name3.getBytes("gb2312")).length > 8)) {
						try{
						errorString += "设备定义选件操作中序号为"+Integer.valueOf(readlist[4 * i+ mark_list.get(0).get("DeviceOptional") + 1])+"这一行存在长度越界错误";
						}catch(Exception e) {
							// TODO: handle exception
							errorString += "设备定义选件操作中第"+i+"行附近存在错误";
						}
						hasError = true;
						break;// 有错误就跳出循环
					}
				}
			}
		}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hasError;
	}
	/**
	 * 
	 * @Title: checkError_NCEdit
	 * @Description: 检查NCEdit错误
	 * @param hasError
	 * @param readlist_NCEdit
	 * @param step_NCEdit
	 * @param mark_list
	 * @return boolean
	 * @throws
	 */
	private boolean checkError_NCEdit(boolean hasError,
			String[] readlist_NCEdit, int step_NCEdit,
			ArrayList<HashMap<String, Integer>> mark_list,String flag) {

		if (!hasError) {
			if (readlist_NCEdit == null || readlist_NCEdit.length < 5) {
				errorString += "NC程序存在语法错误，表头错误";
				hasError = true;
			} else {
				String index="NCEdit"+flag;
				if(step_NCEdit<0){
					errorString += index+"的后面几项存在错误，请检查文件！";
					hasError = true;
				}
				for (int i = 0; i < step_NCEdit; i++) {
					try{
					    Integer.valueOf(readlist_NCEdit[5 * i + mark_list.get(0).get(index)+ 1]);
					}catch(Exception e) {
						// TODO: handle exception
						errorString += index+"中第"+i+"行附近存在错误";
						hasError = true;
					    break;
					}
				}
			}

		}
		return hasError;
	}

	/**
	 * 
	 * @Title: checkError_TableEdit
	 * @Description: 检查TableEdit错误
	 * @param hasError
	 * @param readlist_TableEdit
	 * @param step_TableEdit
	 * @param mark_list
	 * @return boolean
	 * @throws
	 */
	private boolean checkError_TableEdit(boolean hasError,
			String[] readlist_TableEdit, int step_TableEdit,
			ArrayList<HashMap<String, Integer>> mark_list,String flag) {

		if (!hasError) {
			if (readlist_TableEdit == null || readlist_TableEdit.length < 5) {
				errorString += "Table程序存在语法错误，表头错误";
				hasError = true;
			} else {
				String index="TableEdit"+flag;
				if(step_TableEdit<0){
					errorString += index+"的后面几项存在错误，请检查文件！";
					hasError = true;
				}
				for (int i = 0; i < step_TableEdit; i++) {
					try{
					    Integer.valueOf(readlist_TableEdit[4 * i+ mark_list.get(0).get(index) + 1]);
					}catch(Exception e) {
						// TODO: handle exception
						errorString += index+"中第"+i+"行附近存在错误";
						hasError = true;
					    break;
					}
				}
			}

		}
		return hasError;
	}

	/**
	 * 
	 * @Title: updateAdapter
	 * @Description: 读取文件后，需要根据当前页面来判断相应的adapter来更新显示，否则出错
	 * @return void
	 * @throws
	 */
	private void updateAdapter() {
		// 必须根据当前显示的fragment来调用adapter更新,否则会找不到view可出错
		String tab_current = getActionBar().getSelectedTab().getText()
				.toString().trim();
		if (tab_current.equals("设备定义")) {
			ListView lv = com.tr.programming.Device_ListFragment.listView_device;
			String child_current = lv.getAdapter()
					.getItem(lv.getCheckedItemPosition()).toString().trim();
			if (child_current.equals("实际输入")) {
				Fragments_Device_ActualInput.ActualInput_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("实际输出")) {
				Fragments_Device_ActualOutput.ActualOutput_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("中间输入")) {
				Fragments_Device_MiddleInput.MiddleInput_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("中间输出")) {
				Fragments_Device_MiddleOutput.MiddleOutput_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("数据输入")) {
				Fragments_Device_DataInput.DeviceDataInput_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("数据输出")) {
				com.tr.programming.Fragments_Device_DataOutput.DeviceDataOutput_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("掉电可保持寄存器")) {
				Fragments_Device_Register.DeviceRegister_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("掉电可保持数据寄存器")) {
				Fragments_Device_DataRegister.DeviceDataRegister_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("定时器")) {
				Fragments_Device_Timer.DeviceTimer_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("计数器")) {
				Fragments_Device_Counter.DeviceCounter_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("位置")) {
				Fragments_Device_Position.DevicePosition_Adapter
				.notifyDataSetChanged();
			} else if (child_current.equals("警报")) {
				Fragments_Device_Alarm.DeviceAlarm_Adapter
				.notifyDataSetChanged();
			}else if (child_current.equals("选件操作")) {
				Fragments_Device_Optional.DeviceOptional_Adapter
				.notifyDataSetChanged();
			}
		} else if (tab_current.equals("NC编辑")) {
			//Fragments_NCedit.NCedit_Adapter.notifyDataSetChanged();
			ListView lv = com.tr.programming.Device_ListFragment.listView_nc;
			String child_current = lv.getAdapter()
					.getItem(lv.getCheckedItemPosition()).toString().trim();
			if (child_current.equals("原点")) {
				Fragments_NCedit1.NCedit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("主进程")) {
				Fragments_NCedit2.NCedit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("进程1")) {
				Fragments_NCedit3.NCedit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("进程2")) {
				Fragments_NCedit4.NCedit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("进程3")) {
				Fragments_NCedit5.NCedit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("进程4")) {
				Fragments_NCedit6.NCedit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("进程5")) {
				Fragments_NCedit7.NCedit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("进程6")) {
				Fragments_NCedit8.NCedit_Adapter.notifyDataSetChanged();
			}  
		} else if (tab_current.equals("Table编辑")) {
			ListView lv = com.tr.programming.Device_ListFragment.listView_table;
			String child_current = lv.getAdapter()
					.getItem(lv.getCheckedItemPosition()).toString().trim();
			if (child_current.equals("T编辑1")) {
				Fragments_Table1.TableEdit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("T编辑2")) {
				Fragments_Table2.TableEdit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("T编辑3")) {
				Fragments_Table3.TableEdit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("T编辑4")) {
				Fragments_Table4.TableEdit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("T编辑5")) {
				Fragments_Table5.TableEdit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("T编辑6")) {
				Fragments_Table6.TableEdit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("T编辑7")) {
				Fragments_Table7.TableEdit_Adapter.notifyDataSetChanged();
			} else if (child_current.equals("T编辑8")) {
				Fragments_Table8.TableEdit_Adapter.notifyDataSetChanged();
			}  
		}
		/*
		else {
			Fragments_Table.TableEdit_Adapter.notifyDataSetChanged();
		}
*/
	}

	/**
	 * 
	 * @Title: read_8before
	 * @Description: 读取设备定义前8项
	 * @param ArrayList
	 * @param markname
	 * @param readlist
	 * @param step
	 * @param mark_list
	 * @return void
	 * @throws
	 */
	private void read_8before(ArrayList<HashMap<String, Object>> ArrayList,
			String markname, String[] readlist, int step,
			ArrayList<HashMap<String, Integer>> mark_list,Activity targetActivity) {
   
		int j = ArrayList.size();
		// 重置所有行
		for (int i = 0; i < j; i++) {
			ArrayList.get(i).put("symbolNameEditText", "");
			ArrayList.get(i).put("signalNameEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}

		if (step == 0) {
			// Toast.makeText(targetActivity, markname+"数据为空", Toast.LENGTH_SHORT).show();
		}else if (step <0) {
			 new AlertDialog.Builder(targetActivity).setTitle("提示")
		     .setMessage( "设备定义"+markname+"的后面几项存在错误，请检查文件！")
		     .setPositiveButton("知道了", null).show();
		} else {
			// 判断文件中程序行数与显示的行数的大小关系
			int k = step - j;

			// 暂时不用增加不够的行，，该功能先保留
			// if (k>0) {//增加不够的行
			// for (int i = 0; i <k+1 ; i++) {
			// HashMap<String, Object> mapadd = new HashMap<String, Object>();
			// mapadd.put("addressText", String.format("%04d", j+i));//
			// mapadd.put("symbolNameEditText","");
			// mapadd.put("signalNameEditText","");
			// mapadd.put("noteEditText","");
			// ArrayList.add(j+i,mapadd);
			//
			// }
			// }

			// 暂时不用消去多余的行，只需要把多的行重置，该功能先保留
			// else {
			// for ( int m=j-1; m>step; m--) {//消去多余的行
			// ArrayList.remove(m);
			// /**
			// *
			// 只能设置一个adapter，不然其他的找不到getActivity()会出错;不足就是若在这个界面就可以实时刷新，若在其他界面就只能进行一次切换来刷新当前界面
			// */
			// com.tr.programming.Fragments_Device_ActualInput.ActualInput_Adapter.notifyDataSetChanged();
			//
			// }
			// }

			// 读取显示,显示在各自的位置上
			for (int i = 0; i < step; i++) {
				
				try{
					int pos = Integer.parseInt(readlist[4 * i+ mark_list.get(0).get(markname) + 1]);// 得到实际位置地址值
				
				ArrayList.get(pos).put("symbolNameEditText",
						readlist[4 * i + mark_list.get(0).get(markname) + 2]);
				ArrayList.get(pos).put("signalNameEditText",
						readlist[4 * i + mark_list.get(0).get(markname) + 3]);
				ArrayList.get(pos).put("noteEditText",
						readlist[4 * i + mark_list.get(0).get(markname) + 4]);
				} catch (Exception e) {
			// TODO: handle exception
			      new AlertDialog.Builder(targetActivity).setTitle("提示")
			     .setMessage( "设备定义"+markname+"中第"+i+"行附近存在错误，请检查文件！")
			     .setPositiveButton("知道了", null).show();
			      return;
		        }
			}

			// Toast.makeText(this, markname + "数据已载入",
			// Toast.LENGTH_SHORT).show();

		} 
	}

	/**
	 * 
	 * @Title: read_4after
	 * @Description: 读取设备定义后4项
	 * @param ArrayList
	 * @param markname
	 * @param readlist
	 * @param step
	 * @param mark_list
	 * @return void
	 * @throws
	 */
	private void read_4after(ArrayList<HashMap<String, Object>> ArrayList,
			String markname, String[] readlist, int step,
			ArrayList<HashMap<String, Integer>> mark_list,Activity targetActivity) {
		
		int j = ArrayList.size();
		// 重置所有行
		if (markname.equals("DeviceAlarm")) {
		  for (int i = 0; i < j; i++) {
			ArrayList.get(i).put("symbolNameEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		  }
		}
		if (!markname.equals("DeviceAlarm")) {
			 for (int i = 0; i < j; i++) {
					ArrayList.get(i).put("symbolNameEditText", "");
					ArrayList.get(i).put("signalNameEditText", "");
					ArrayList.get(i).put("noteEditText", "");
				  }	
		}

		if (step == 0) {
			//Toast.makeText(targetActivity, markname + "数据为空", Toast.LENGTH_SHORT).show();
		}else if (step <0) {
			 new AlertDialog.Builder(targetActivity).setTitle("提示")
		     .setMessage( "设备定义"+markname+"的后面几项存在错误，请检查文件！")
		     .setPositiveButton("知道了", null).show();
		}  else {
			// 判断文件中程序行数与显示的行数的大小关系
			int k = step - j;
			// 增加不够的行
			if (k > 0&&markname.equals("DeviceAlarm")) {
				for (int i = 0; i < k + 1; i++) {
					HashMap<String, Object> mapadd = new HashMap<String, Object>();
					mapadd.put("addressText", String.format("%03d", j + i+1));
					mapadd.put("symbolNameEditText", "");
					mapadd.put("noteEditText", "");
					ArrayList.add(j + i, mapadd);

				}
			}

			if (k > 0&&markname.equals("DevicePosition")) {
				for (int i = 0; i < k + 1; i++) {
					HashMap<String, Object> mapadd = new HashMap<String, Object>();
					mapadd.put("addressText", String.format("%04d", j + i+1));
					mapadd.put("symbolNameEditText", "");
					mapadd.put("signalNameEditText", "");
					mapadd.put("noteEditText", "");
					mapadd.put("setItem", "0");
					ArrayList.add(j + i, mapadd);

				}
			}

			if(k > 0&&(markname.equals("DeviceTimer")||markname.equals("DeviceCounter"))){
				for (int i = 0; i < k + 1; i++) {
					HashMap<String, Object> mapadd = new HashMap<String, Object>();
					mapadd.put("addressText", String.format("%04d", j + i));
					mapadd.put("symbolNameEditText", "");
					mapadd.put("signalNameEditText", "");
					mapadd.put("noteEditText", "");
					mapadd.put("setItem", "0");
					ArrayList.add(j + i, mapadd);

				}
			}
			// 暂时不用消去多余的行，只需要把多的行重置，该功能保留
			// else { //消去多余的行
			// for ( int m=j-1; m>step; m--) {
			// ArrayList.remove(m);
			// }
			// }

			// 读取显示,从第一行开始显示
			// step为0的话就不显示
//			for (int i = 0; i < step; i++) {
//				ArrayList.get(i).put("symbolNameEditText",
//						readlist[3 * i + mark_list.get(0).get(markname) + 2]);
//				ArrayList.get(i).put("noteEditText",
//						readlist[3 * i + mark_list.get(0).get(markname) + 3]);
//			}
			if (markname.equals("DeviceAlarm")) {
				for (int i = 0; i < step; i++) {
					try{
					int lineToAdd=Integer.valueOf(readlist[3 * i + mark_list.get(0).get(markname) + 1]);
					// 判断文件中程序行数与显示的行数的大小关系
					int m = lineToAdd- ArrayList.size();
					// 增加不够的行
					if (m > 0) {
						for (int n = 0; n < m + 1; n++) {
							HashMap<String, Object> mapadd = new HashMap<String, Object>();
							mapadd.put("addressText", String.format("%03d", ArrayList.size()+1));// //============位置保存问题
							mapadd.put("symbolNameEditText", "");
							mapadd.put("noteEditText", "");
							ArrayList.add(ArrayList.size() , mapadd);
						}
					}
					ArrayList.get(lineToAdd-1).put("symbolNameEditText",
							readlist[3 * i + mark_list.get(0).get(markname) + 2]);
					ArrayList.get(lineToAdd-1).put("noteEditText",
							readlist[3 * i + mark_list.get(0).get(markname) + 3]);
					} catch (Exception e) {
						// TODO: handle exception
						 new AlertDialog.Builder(targetActivity).setTitle("提示")
						   .setMessage("设备定义"+markname+"中第"+i+"行附近存在错误，请检查文件！")
						   .setPositiveButton("知道了", null).show();
						 return;
					}
					
				}
			}else if (markname.equals("DevicePosition")) {
				for (int i = 0; i < step; i++) {
					try{
					int lineToAdd=Integer.valueOf(readlist[5 * i + mark_list.get(0).get(markname) + 1]);
					// 判断文件中程序行数与显示的行数的大小关系
					int m = lineToAdd- ArrayList.size();
					// 增加不够的行
					if (m > 0) {
						for (int n = 0; n < m + 1; n++) {
							HashMap<String, Object> mapadd = new HashMap<String, Object>();
							mapadd.put("addressText", String.format("%04d", ArrayList.size()+1));
							mapadd.put("symbolNameEditText", "");
							mapadd.put("signalNameEditText", "");
							mapadd.put("noteEditText", "");
							mapadd.put("setItem", "0");
							ArrayList.add(ArrayList.size(), mapadd);
						}
					}
					ArrayList.get(lineToAdd-1).put("symbolNameEditText",
							readlist[5 * i + mark_list.get(0).get(markname) + 2]);
					ArrayList.get(lineToAdd-1).put("signalNameEditText",
							readlist[5 * i + mark_list.get(0).get(markname) + 3]);
					ArrayList.get(lineToAdd-1).put("noteEditText",
							readlist[5 * i + mark_list.get(0).get(markname) + 4]);
					if(readlist[5 * i + mark_list.get(0).get(markname) + 5].equals("")){
						readlist[5 * i + mark_list.get(0).get(markname) + 5]="0";
					}
					ArrayList.get(lineToAdd-1).put("setItem",
							readlist[5 * i + mark_list.get(0).get(markname) + 5]);
					} catch (Exception e) {
						// TODO: handle exception
						 new AlertDialog.Builder(targetActivity).setTitle("提示")
						   .setMessage("设备定义"+markname+"中第"+i+"行附近存在错误，请检查文件！")
						   .setPositiveButton("知道了", null).show();
						 return;
					}
				}
			}else {
				for (int i = 0; i < step; i++) {
					try{
					int lineToAdd=Integer.valueOf(readlist[5 * i + mark_list.get(0).get(markname) + 1]);
					// 判断文件中程序行数与显示的行数的大小关系
					int m = lineToAdd+1- ArrayList.size();
					// 增加不够的行
					if (m > 0) {
						for (int n = 0; n < m + 1; n++) {
							HashMap<String, Object> mapadd = new HashMap<String, Object>();
							mapadd.put("addressText", String.format("%04d", ArrayList.size()));
							mapadd.put("symbolNameEditText", "");
							mapadd.put("signalNameEditText", "");
							mapadd.put("noteEditText", "");
							mapadd.put("setItem", "0");
							ArrayList.add(ArrayList.size(), mapadd);
						}
					}
					ArrayList.get(lineToAdd).put("symbolNameEditText",
							readlist[5 * i + mark_list.get(0).get(markname) + 2]);
					ArrayList.get(lineToAdd).put("signalNameEditText",
							readlist[5 * i + mark_list.get(0).get(markname) + 3]);
					ArrayList.get(lineToAdd).put("noteEditText",
							readlist[5 * i + mark_list.get(0).get(markname) + 4]);
					if(readlist[5 * i + mark_list.get(0).get(markname) + 5].equals("")){
						readlist[5 * i + mark_list.get(0).get(markname) + 5]="0";
					}
					ArrayList.get(lineToAdd).put("setItem",
							readlist[5 * i + mark_list.get(0).get(markname) + 5]);
					} catch (Exception e) {
						// TODO: handle exception
						 new AlertDialog.Builder(targetActivity).setTitle("提示")
						   .setMessage("设备定义"+markname+"中第"+i+"行附近存在错误，请检查文件！")
						   .setPositiveButton("知道了", null).show();
						 return;
					}
				}
			}
			
			// Toast.makeText(this, markname + "数据已载入",
			// Toast.LENGTH_SHORT).show();

		}
	}
	private void read_Optional(ArrayList<HashMap<String, Object>> ArrayList,
			String markname, String[] readlist, int step,
			ArrayList<HashMap<String, Integer>> mark_list,Activity targetActivity) {
		
		int j = ArrayList.size();
		// 重置所有行
		if (markname.equals("DeviceOptional")) {
			 for (int i = 0; i < j; i++) {
					ArrayList.get(i).put("name1", "");
					ArrayList.get(i).put("name2", "");
					ArrayList.get(i).put("name3", "");
				  }	
		}
		if (step== 0) {
			//Toast.makeText(targetActivity, markname + "数据为空", Toast.LENGTH_SHORT).show();
		}else if (step <0) {
			 new AlertDialog.Builder(targetActivity).setTitle("提示")
		     .setMessage( "设备定义"+markname+"的后面几项存在错误，请检查文件！")
		     .setPositiveButton("知道了", null).show();
		}  else {
			if (markname.equals("DeviceOptional")) {
				for (int i = 0; i < step; i++) {
					try{
					int lineToAdd=Integer.valueOf(readlist[4 * i + mark_list.get(0).get(markname) + 1]);
					ArrayList.get(lineToAdd-1).put("name1",
							readlist[4 * i + mark_list.get(0).get(markname) + 2]);
					ArrayList.get(lineToAdd-1).put("name2",
							readlist[4 * i + mark_list.get(0).get(markname) + 3]);
					ArrayList.get(lineToAdd-1).put("name3",
							readlist[4 * i + mark_list.get(0).get(markname) + 4]);
					} catch (Exception e) {
						// TODO: handle exception
						 new AlertDialog.Builder(targetActivity).setTitle("提示")
						   .setMessage("设备定义"+markname+"中第"+i+"行附近存在错误，请检查文件！")
						   .setPositiveButton("知道了", null).show();
						 return;
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Title: read_NCEdit
	 * @Description: 读取NCEdit
	 * @param readlist_NCEdit
	 * @param step_NCEdit
	 * @return void
	 * @throws
	 */
	private void read_NCEdit(ArrayList<HashMap<String, Object>> ArrayList,
			String markname, String[] readlist, int step,
			ArrayList<HashMap<String, Integer>> mark_list,Activity targetActivity) {
       
		// 判断文件中程序行数与显示的行数的大小关系
		int j = ArrayList.size();
		 for (int i = 0; i < j; i++) {
			 ArrayList.get(i).put("numSpinner", "");
				ArrayList.get(i).put("orderSpinner", "");
				ArrayList.get(i).put("operatText", "");
				ArrayList.get(i).put("noteEditText", "");
		 }
		if (step == 0) {
			//Toast.makeText(targetActivity, markname + "数据为空", Toast.LENGTH_SHORT).show();
		}else if (step <0) {
			 new AlertDialog.Builder(targetActivity).setTitle("提示")
		     .setMessage( markname+"的后面几项存在错误，请检查文件！")
		     .setPositiveButton("知道了", null).show();
		}  else {
		int k = step- j;
		if (k > 0) {// 增加不够的行
			for (int i = 0; i < k + 1; i++) {
				HashMap<String, Object> mapadd = new HashMap<String, Object>();
				mapadd.put("addressText", String.format("%04d", j + i));//
				mapadd.put("numSpinner", "");
				mapadd.put("orderSpinner", "");
				mapadd.put("operatText", "");
				mapadd.put("noteEditText", "");
				ArrayList.add(j + i, mapadd);
			}
		}

				for (int i = step; i < j; i++) {
					ArrayList.get(i).put("numSpinner", "");
					ArrayList.get(i).put("orderSpinner", "");
					ArrayList.get(i).put("operatText", "");
					ArrayList.get(i).put("noteEditText", "");
				}
		// 读取显示,从第一行开始显示
		for (int i = 0; i < step; i++) {
			try{
			int pos = Integer.parseInt(readlist[5 * i
			                                    + mark_list.get(0).get(markname) + 1]);// 得到实际位置地址值
			// 判断文件中程序行数与显示的行数的大小关系
			int m = pos+1- ArrayList.size();
			// 增加不够的行
			if (m > 0) {
				for (int n = 0; n < m + 1; n++) {
					HashMap<String, Object> mapadd = new HashMap<String, Object>();
					mapadd.put("addressText", String.format("%04d", ArrayList.size()));//
					mapadd.put("numSpinner", "");
					mapadd.put("orderSpinner", "");
					mapadd.put("operatText", "");
					mapadd.put("noteEditText", "");
					ArrayList.add(ArrayList.size(), mapadd);
				}
			}
			ArrayList.get(pos)
			.put("numSpinner", readlist[5 * i + mark_list.get(0).get(markname) + 2]);
			ArrayList.get(pos)
			.put("orderSpinner", readlist[5 * i  + mark_list.get(0).get(markname) + 3]);
			ArrayList.get(pos)
			.put("operatText", readlist[5 * i  + mark_list.get(0).get(markname) + 4]);
			ArrayList.get(pos)
			.put("noteEditText", readlist[5 * i + mark_list.get(0).get(markname) + 5]);
			} catch (Exception e) {
				// TODO: handle exception
				 new AlertDialog.Builder(targetActivity).setTitle("提示")
				   .setMessage(markname+"中第"+i+"行附近存在错误，请检查文件！")
				   .setPositiveButton("知道了", null).show();
				 return;
			}
			// com.tr.programming.Fragments_NCedit.NCedit_Adapter.notifyDataSetChanged();
		}
		// Toast.makeText(this, "nc文本数据已载入", Toast.LENGTH_SHORT).show();
		}
   	}

	

	/**
	 * 
	 * @Title: read_TableEdit
	 * @Description: 读取TableEdit
	 * @param readlist_TableEdit
	 * @param step_TableEdit
	 * @return void
	 * @throws
	 */
	private void read_TableEdit(ArrayList<HashMap<String, Object>> ArrayList,
			String markname, String[] readlist, int step,
			ArrayList<HashMap<String, Integer>> mark_list,Activity targetActivity) {
      
		// 判断文件中程序行数与显示的行数的大小关系
		int j = ArrayList.size();
		for (int i = 0; i < j; i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
		if (step== 0) {
			//Toast.makeText(targetActivity, markname + "数据为空", Toast.LENGTH_SHORT).show();
		} else if (step <0) {
			 new AlertDialog.Builder(targetActivity).setTitle("提示")
		     .setMessage( markname+"的后面几项存在错误，请检查文件！")
		     .setPositiveButton("知道了", null).show();
		} else {
		int k = step - j;

		if (k > 0) {// 增加不够的行
			for (int i = 0; i < k + 1; i++) {
				HashMap<String, Object> mapadd = new HashMap<String, Object>();
				mapadd.put("numText", String.format("%04d", j + i));//
				mapadd.put("inputEditText", "");
				mapadd.put("outputEditText", "");
				mapadd.put("noteEditText", "");
				ArrayList.add(j + i, mapadd);
				// com.tr.programming.Fragments_Table.TableEdit_Adapter.notifyDataSetChanged();
			}
		}

		// 读取显示,从第一行开始显示
		for (int i = 0; i < step; i++) {
           try{
			int pos = Integer.parseInt(readlist[4 * i+ mark_list.get(0).get(markname) + 1]);// 得到实际位置地址值
			// 判断文件中程序行数与显示的行数的大小关系
						int m = pos+1- ArrayList.size();
						// 增加不够的行
						if (m > 0) {
							for (int n = 0; n < m + 1; n++) {
								HashMap<String, Object> mapadd = new HashMap<String, Object>();
								mapadd.put("numText", String.format("%04d", ArrayList.size()));//
								mapadd.put("inputEditText", "");
								mapadd.put("outputEditText", "");
								mapadd.put("noteEditText", "");
								ArrayList.add(ArrayList.size(), mapadd);
							}
						}
			ArrayList.get(pos)
			.put("inputEditText", readlist[4 * i + mark_list.get(0).get(markname) + 2]);
			ArrayList.get(pos)
			.put("outputEditText", readlist[4 * i  + mark_list.get(0).get(markname) + 3]);
			ArrayList.get(pos)
			.put("noteEditText", readlist[4 * i  + mark_list.get(0).get(markname) + 4]);
         } catch (Exception e) {
	      // TODO: handle exception
	       new AlertDialog.Builder(targetActivity).setTitle("提示")
	      .setMessage(markname+"中第"+i+"行附近存在错误，请检查文件！")
	      .setPositiveButton("知道了", null).show();
	       return;
         }
		
		}
		for (int i = step; i < j; i++) {
			ArrayList.get(i).put("inputEditText", "");
			ArrayList.get(i).put("outputEditText", "");
			ArrayList.get(i).put("noteEditText", "");
		}
		}
	}

	/**
	 * 
	 * @Title: fileSave
	 * @Description: 保存文件结构 ，要判断是新建的文件的第一次保存还是打开的文件的保存
	 * @return void
	 * @throws
	 */
	// private void fileSave() {
	// // 保存pad上编辑的程序
	// creatFolderSysytem();
	// final String date = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());//
	// 当前时间
	//
	// if (newsaveflag) {// 为真则是开机后新编辑的文件
	//
	// final EditText editText = new EditText(this);
	// new AlertDialog.Builder(this)
	// .setTitle("这是新编辑的文件，请输入保存的文件名")
	// .setView(editText)
	// .setPositiveButton("确定",
	// new DialogInterface.OnClickListener() {// 输入名字后确定
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	//
	// final String foldername = editText
	// .getText().toString().trim();
	// if (foldername != null
	// && foldername.length() != 0) {
	// newsaveflag = false;
	// foldername_opened = foldername;
	// File mouldData = new File(
	// Constans.mouldDataPATH);
	// final String[] folders = mouldData
	// .list();// 得到文件夹名
	// boolean alreadExist = false;
	//
	// for (int i = 0; i < folders.length; i++) {// 判断输入的名字是否与已存在的文件有重名
	// if (folders[i].equals(foldername)) {
	// alreadExist = true;
	// }
	// }
	//
	// if (alreadExist) {// 有重名，选择是否覆盖原文件
	// new AlertDialog.Builder(
	// TR_Programming_Activity.this)
	// .setTitle("注意！")
	// .setMessage(
	// "已存在该文件夹，是否要覆盖？")
	// .setPositiveButton(
	// "确定",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(
	// DialogInterface dialog,
	// int which) {// 确定覆盖
	// Constans.mouldData
	// .createFolder(
	// Constans.mouldDataPATH,
	// foldername)
	// .createFile(
	// Constans.设备名称数据);
	// Constans.mouldData
	// .cleanFile(Constans.设备名称数据);
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceActualInputListData(),
	// "[DeviceActualInput]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceActualOutputListData(),
	// "[DeviceActualOutput]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceMiddleInputListData(),
	// "[DeviceMiddleInput]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceMiddleOutputListData(),
	// "[DeviceMiddleOutput]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceDataInputListData(),
	// "[DeviceDataInput]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceDataOutputListData(),
	// "[DeviceDataOutput]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceRegisterListData(),
	// "[DeviceRegister]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceDataRegisterListData(),
	// "[DeviceDataRegister]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_4later(
	// foldername,
	// ArrayListBound
	// .getDeviceTimerListData(),
	// "[DeviceTimer]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_4later(
	// foldername,
	// ArrayListBound
	// .getDeviceCounterListData(),
	// "[DeviceCounter]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_4later(
	// foldername,
	// ArrayListBound
	// .getDevicePositionListData(),
	// "[DevicePosition]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	// saveAs_4later(
	// foldername,
	// ArrayListBound
	// .getDeviceAlarmListData(),
	// "[DeviceAlarm]",
	// date);
	// Constans.mouldData
	// .writeFile(
	// Constans.设备名称数据,
	// "\r\n");// 不同段之间空一行
	//
	// saveAs_NcEdit(
	// foldername,
	// date);
	// saveAs_TableEdit(
	// foldername,
	// date);
	// Toast.makeText(
	// TR_Programming_Activity.this,
	// "已覆盖文件夹"
	// + foldername,
	// Toast.LENGTH_LONG)
	// .show();
	// }
	//
	// })
	// .setNegativeButton(
	// "取消",
	// new DialogInterface.OnClickListener() {// 不覆盖，提示重新输入名字保存
	// @Override
	// public void onClick(
	// DialogInterface dialog,
	// int which) {
	// Toast.makeText(
	// TR_Programming_Activity.this,
	// "请输入不同名字再次保存",
	// Toast.LENGTH_SHORT)
	// .show();
	// }
	// }).show();
	// } else {// 没有重名，可以直接保存
	//
	// Constans.mouldData.createFolder(
	// Constans.mouldDataPATH,
	// foldername).createFile(
	// Constans.设备名称数据);
	// Constans.mouldData
	// .cleanFile(Constans.设备名称数据);
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceActualInputListData(),
	// "[DeviceActualInput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceActualOutputListData(),
	// "[DeviceActualOutput]",
	// date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceMiddleInputListData(),
	// "[DeviceMiddleInput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceMiddleOutputListData(),
	// "[DeviceMiddleOutput]",
	// date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceDataInputListData(),
	// "[DeviceDataInput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceDataOutputListData(),
	// "[DeviceDataOutput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceRegisterListData(),
	// "[DeviceRegister]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(
	// foldername,
	// ArrayListBound
	// .getDeviceDataRegisterListData(),
	// "[DeviceDataRegister]",
	// date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(
	// foldername,
	// ArrayListBound
	// .getDeviceTimerListData(),
	// "[DeviceTimer]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(
	// foldername,
	// ArrayListBound
	// .getDeviceCounterListData(),
	// "[DeviceCounter]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(
	// foldername,
	// ArrayListBound
	// .getDevicePositionListData(),
	// "[DevicePosition]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(
	// foldername,
	// ArrayListBound
	// .getDeviceAlarmListData(),
	// "[DeviceAlarm]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	//
	// saveAs_NcEdit(foldername, date);
	// saveAs_TableEdit(foldername, date);
	// Toast.makeText(
	// TR_Programming_Activity.this,
	// "已保存文件夹" + foldername,
	// Toast.LENGTH_SHORT).show();
	// }
	// } else {
	// Toast.makeText(
	// TR_Programming_Activity.this,
	// "名称为空，请重新输入名称",
	// Toast.LENGTH_SHORT).show();
	// }
	//
	// }
	// }).setNegativeButton("取消", null).show();
	// } else {// 为假则是已打开过或以保存的的文件
	//
	// Constans.mouldData.createFolder(Constans.mouldDataPATH,
	// foldername_opened).createFile(Constans.设备名称数据);
	// Constans.mouldData.cleanFile(Constans.设备名称数据);
	// saveAs_8before(foldername_opened,
	// ArrayListBound.getDeviceActualInputListData(),
	// "[DeviceActualInput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername_opened,
	// ArrayListBound.getDeviceActualOutputListData(),
	// "[DeviceActualOutput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername_opened,
	// ArrayListBound.getDeviceMiddleInputListData(),
	// "[DeviceMiddleInput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername_opened,
	// ArrayListBound.getDeviceMiddleOutputListData(),
	// "[DeviceMiddleOutput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername_opened,
	// ArrayListBound.getDeviceDataInputListData(),
	// "[DeviceDataInput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername_opened,
	// ArrayListBound.getDeviceDataOutputListData(),
	// "[DeviceDataOutput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername_opened,
	// ArrayListBound.getDeviceRegisterListData(),
	// "[DeviceRegister]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername_opened,
	// ArrayListBound.getDeviceDataRegisterListData(),
	// "[DeviceDataRegister]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername_opened,
	// ArrayListBound.getDeviceTimerListData(), "[DeviceTimer]",
	// date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername_opened,
	// ArrayListBound.getDeviceCounterListData(),
	// "[DeviceCounter]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername_opened,
	// ArrayListBound.getDevicePositionListData(),
	// "[DevicePosition]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername_opened,
	// ArrayListBound.getDeviceAlarmListData(), "[DeviceAlarm]",
	// date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	//
	// saveAs_NcEdit(foldername_opened, date);
	// saveAs_TableEdit(foldername_opened, date);
	// Toast.makeText(TR_Programming_Activity.this,
	// "已保存文件夹" + foldername_opened, Toast.LENGTH_LONG).show();
	//
	// }
	// }

	/**
	 * 
	 * @Title: saveAllFilesAs
	 * @Description: 另存文件功能
	 * @param foldername
	 *            另存文件名 ，要判断是否重名，是否覆盖
	 * @return void
	 * @throws
	 */
	// private void saveAllFilesAs(final String foldername) {// 另存为功能
	// // 保存pad上编辑的程序
	// foldername_opened = foldername;
	// newsaveflag = false;
	// creatFolderSysytem();
	// final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	// .format(Calendar.getInstance().getTime());// 当前时间
	// boolean alreadExist = false;
	//
	// File mouldData = new File(Constans.mouldDataPATH);
	// final String[] folders = mouldData.list();// 得到文件夹名
	//
	// for (int i = 0; i < folders.length; i++) {
	// if (folders[i].equals(foldername)) {
	// alreadExist = true;
	// }
	// }
	//
	// if (alreadExist) {
	// new AlertDialog.Builder(this)
	// .setTitle("注意！")
	// .setMessage("已存在该文件夹，是否要覆盖？")
	// .setPositiveButton("确定",
	// new DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog,
	// int which) {
	// Constans.mouldData.createFolder(
	// Constans.mouldDataPATH, foldername)
	// .createFile(Constans.设备名称数据);
	// Constans.mouldData
	// .cleanFile(Constans.设备名称数据);
	// saveAs_8before(foldername, ArrayListBound
	// .getDeviceActualInputListData(),
	// "[DeviceActualInput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername, ArrayListBound
	// .getDeviceActualOutputListData(),
	// "[DeviceActualOutput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername, ArrayListBound
	// .getDeviceMiddleInputListData(),
	// "[DeviceMiddleInput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername, ArrayListBound
	// .getDeviceMiddleOutputListData(),
	// "[DeviceMiddleOutput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername, ArrayListBound
	// .getDeviceDataInputListData(),
	// "[DeviceDataInput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername, ArrayListBound
	// .getDeviceDataOutputListData(),
	// "[DeviceDataOutput]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername, ArrayListBound
	// .getDeviceRegisterListData(),
	// "[DeviceRegister]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername, ArrayListBound
	// .getDeviceDataRegisterListData(),
	// "[DeviceDataRegister]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername, ArrayListBound
	// .getDeviceTimerListData(),
	// "[DeviceTimer]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername, ArrayListBound
	// .getDeviceCounterListData(),
	// "[DeviceCounter]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername, ArrayListBound
	// .getDevicePositionListData(),
	// "[DevicePosition]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername, ArrayListBound
	// .getDeviceAlarmListData(),
	// "[DeviceAlarm]", date);
	// Constans.mouldData.writeFile(
	// Constans.设备名称数据, "\r\n");// 不同段之间空一行
	//
	// saveAs_NcEdit(foldername, date);
	// saveAs_TableEdit(foldername, date);
	// }
	// }).setNegativeButton("取消", null).show();
	// Toast.makeText(this, "已覆盖文件夹" + foldername, Toast.LENGTH_LONG)
	// .show();
	//
	// } else {
	// Constans.mouldData.createFolder(Constans.mouldDataPATH, foldername)
	// .createFile(Constans.设备名称数据);
	// Constans.mouldData.cleanFile(Constans.设备名称数据);
	// saveAs_8before(foldername,
	// ArrayListBound.getDeviceActualInputListData(),
	// "[DeviceActualInput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername,
	// ArrayListBound.getDeviceActualOutputListData(),
	// "[DeviceActualOutput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername,
	// ArrayListBound.getDeviceMiddleInputListData(),
	// "[DeviceMiddleInput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername,
	// ArrayListBound.getDeviceMiddleOutputListData(),
	// "[DeviceMiddleOutput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername,
	// ArrayListBound.getDeviceDataInputListData(),
	// "[DeviceDataInput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername,
	// ArrayListBound.getDeviceDataOutputListData(),
	// "[DeviceDataOutput]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername,
	// ArrayListBound.getDeviceRegisterListData(),
	// "[DeviceRegister]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_8before(foldername,
	// ArrayListBound.getDeviceDataRegisterListData(),
	// "[DeviceDataRegister]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername, ArrayListBound.getDeviceTimerListData(),
	// "[DeviceTimer]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername,
	// ArrayListBound.getDeviceCounterListData(),
	// "[DeviceCounter]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername,
	// ArrayListBound.getDevicePositionListData(),
	// "[DevicePosition]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	// saveAs_4later(foldername, ArrayListBound.getDeviceAlarmListData(),
	// "[DeviceAlarm]", date);
	// Constans.mouldData.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	//
	// saveAs_NcEdit(foldername, date);
	// saveAs_TableEdit(foldername, date);
	// Toast.makeText(this, "已新建文件夹" + foldername, Toast.LENGTH_SHORT)
	// .show();
	// }
	//
	// }

	/**
	 * 
	 * @Title: saveAs_DeviceDefine
	 * @Description: 保存设备定义文件
	 * @param foldername
	 * @param date
	 * @return void
	 * @throws
	 */
	private void saveAs_DeviceDefine(String foldername, String date) {
		Constans.cache.cleanFile(Constans.设备名称数据);

		saveAs_8before(foldername,
				ArrayListBound.getDeviceActualInputListData(),
				"[DeviceActualInput]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_8before(foldername,
				ArrayListBound.getDeviceActualOutputListData(),
				"[DeviceActualOutput]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_8before(foldername,
				ArrayListBound.getDeviceMiddleInputListData(),
				"[DeviceMiddleInput]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_8before(foldername,
				ArrayListBound.getDeviceMiddleOutputListData(),
				"[DeviceMiddleOutput]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_8before(foldername, ArrayListBound.getDeviceDataInputListData(),
				"[DeviceDataInput]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_8before(foldername,
				ArrayListBound.getDeviceDataOutputListData(),
				"[DeviceDataOutput]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_8before(foldername, ArrayListBound.getDeviceRegisterListData(),
				"[DeviceRegister]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_8before(foldername,
				ArrayListBound.getDeviceDataRegisterListData(),
				"[DeviceDataRegister]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_4later(foldername, ArrayListBound.getDeviceTimerListData(),
				"[DeviceTimer]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_4later(foldername, ArrayListBound.getDeviceCounterListData(),
				"[DeviceCounter]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_4later(foldername, ArrayListBound.getDevicePositionListData(),
				"[DevicePosition]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_4later(foldername, ArrayListBound.getDeviceAlarmListData(),
				"[DeviceAlarm]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
		saveAs_Optional(foldername, ArrayListBound.getDeviceOptionalListData(),
				"[DeviceOptional]", date);
		Constans.cache.writeFile(Constans.设备名称数据, "\r\n");// 不同段之间空一行
	}

	/**
	 * 
	 * @Title: saveAs_8before
	 * @Description: 保存设备定义前8项
	 * @param foldername
	 * @param ArrayList
	 * @param markname
	 * @param date
	 *            保存时间
	 * @return void
	 * @throws
	 */
	private void saveAs_8before(String foldername,
			ArrayList<HashMap<String, Object>> ArrayList, String markname,
			String date) {
		int step1 = ArrayList.size();
		for (int i = 0; i < ArrayList.size(); i++) {
			String symbolString = ArrayList.get(i).get("symbolNameEditText")
					.toString().trim();
			String signalString = ArrayList.get(i).get("signalNameEditText")
					.toString().trim();
			String notesString = ArrayList.get(i).get("noteEditText")
					.toString().trim();
			if (symbolString.length() == 0 && signalString.length() == 0
					&& notesString.length() == 0) {
				step1--;
			}
		}
		final int step = step1;

		// 写入文件头
		Constans.cache.writeFile(Constans.设备名称数据, "[Time]" + "/·" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据, date + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据, "[Step]" + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据,
				"Step" + "=" + String.valueOf(step) + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据, markname + "/" + "\r\n");
		// 循环写入每一行代码到文件中
		for (int i = 0; i < ArrayList.size(); i++) {
			String addresssString = ArrayList.get(i).get("addressText")
					.toString().trim();
			String symbolString = ArrayList.get(i).get("symbolNameEditText")
					.toString().trim();
			String signalString = ArrayList.get(i).get("signalNameEditText")
					.toString().trim();
			String notesString = ArrayList.get(i).get("noteEditText")
					.toString().trim();

			if (symbolString.length() == 0 && signalString.length() == 0
					&& notesString.length() == 0) {
				// do nothing
			} else {
				if (symbolString.length() == 0) {
					symbolString = " ";
				}
				if (signalString.length() == 0) {
					signalString = " ";
				}
				if (notesString.length() == 0) {
					notesString = " ";
				}
				Constans.cache.writeFile(Constans.设备名称数据, addresssString + "/"
						+ symbolString + "/" + signalString + "/" + notesString
						+ "/" + "\r\n");
			}

		}

	}

	/**
	 * 
	 * @Title: saveAs_4later
	 * @Description: 保存设备定义后4项
	 * @param foldername
	 * @param ArrayList
	 * @param markname
	 * @param date
	 * @return void
	 * @throws 
	 */
	private void saveAs_4later(String foldername,
			ArrayList<HashMap<String, Object>> ArrayList, String markname,
			String date) {
		int step1 = ArrayList.size();
		String setString="";
		String signalString="";
		if(markname.equals("[DeviceAlarm]")){
		for (int i = 0; i < ArrayList.size(); i++) {
			String symbolString = ArrayList.get(i).get("symbolNameEditText")
					.toString().trim();
			String notesString = ArrayList.get(i).get("noteEditText")
					.toString().trim();
			if (symbolString.length() == 0 && notesString.length() == 0) {
				step1--;
			}
		}
		}
		if(!markname.equals("[DeviceAlarm]")){
			for (int i = 0; i < ArrayList.size(); i++) {
				String symbolString = ArrayList.get(i).get("symbolNameEditText")
						.toString().trim();
				signalString = ArrayList.get(i).get("signalNameEditText")
						.toString().trim();
				String notesString = ArrayList.get(i).get("noteEditText")
						.toString().trim();
				if (symbolString.length() == 0&& signalString.length() == 0 && notesString.length() == 0) {
					step1--;
				}
			}
			}
		final int step = step1;

		// 写入文件头
		Constans.cache.writeFile(Constans.设备名称数据, "[Time]" + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据, date + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据, "[Step]" + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据,
				"Step" + "=" + String.valueOf(step) + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据, markname + "/" + "\r\n");
		// 循环写入每一行代码到文件中
		for (int i = 0; i < ArrayList.size(); i++) {
			String addresssString = ArrayList.get(i).get("addressText")
					.toString().trim();
			String symbolString = ArrayList.get(i).get("symbolNameEditText")
					.toString().trim();
			String notesString = ArrayList.get(i).get("noteEditText")
					.toString().trim();
			if(!markname.equals("[DeviceAlarm]")){
				signalString = ArrayList.get(i).get("signalNameEditText")
					.toString().trim();
				 setString = ArrayList.get(i).get("setItem")
							.toString().trim();
			}
			
			if (symbolString.length() == 0 && notesString.length() == 0&&signalString.length() == 0) {
				// do nothing
			} else {
				if (symbolString.length() == 0) {
					symbolString = " ";
				}
				if (notesString.length() == 0) {
					notesString = " ";
				}
				if(markname.equals("[DeviceAlarm]")){
					Constans.cache.writeFile(Constans.设备名称数据, addresssString + "/"
							+ symbolString + "/" + notesString + "/"+ "\r\n");
				}else{
					Constans.cache.writeFile(Constans.设备名称数据, addresssString + "/"
						+ symbolString + "/" + signalString + "/" + notesString + "/"+ setString + "/" + "\r\n");
				}
				
			}

		}

	}
	private void saveAs_Optional(String foldername,
			ArrayList<HashMap<String, Object>> ArrayList, String markname,
			String date) {
		// 写入文件头
		Constans.cache.writeFile(Constans.设备名称数据, "[Time]" + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据, date + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据, "[Step]" + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据,
				"Step" + "=" + 32 + "/" + "\r\n");
		Constans.cache.writeFile(Constans.设备名称数据, markname + "/" + "\r\n");
		// 循环写入每一行代码到文件中
		for (int i = 0; i < ArrayList.size(); i++) {
			String addresssString = ArrayList.get(i).get("addressText")
					.toString().trim();
			String name1 = ArrayList.get(i).get("name1")
					.toString().trim();
			String name2 = ArrayList.get(i).get("name2")
					.toString().trim();
			String name3 = ArrayList.get(i).get("name3")
					.toString().trim();
			Constans.cache.writeFile(Constans.设备名称数据, addresssString + "/"
					+ name1 + "/" + name2 + "/" + name3 + "/" + "\r\n");
			
		}

	}

	/**
	 * 
	 * @Title: saveAs_NcEdit
	 * @Description: 保存NcEdit
	 * @param foldername
	 * @param date
	 * @return void
	 * @throws
	 */
	private static void saveAs_NcEdit(String foldername, String date) {
		Constans.cache.cleanFile(Constans.nc文本数据);

		saveAs_8nc(foldername,
				ArrayListBound.getNCeditList1Data(),
				"[NCEdit1]", date);
		Constans.cache.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		saveAs_8nc(foldername,
				ArrayListBound.getNCeditList2Data(),
				"[NCEdit2]", date);
		Constans.cache.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		saveAs_8nc(foldername,
				ArrayListBound.getNCeditList3Data(),
				"[NCEdit3]", date);
		Constans.cache.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		saveAs_8nc(foldername,
				ArrayListBound.getNCeditList4Data(),
				"[NCEdit4]", date);
		Constans.cache.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		saveAs_8nc(foldername,
				ArrayListBound.getNCeditList5Data(),
				"[NCEdit5]", date);
		Constans.cache.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		saveAs_8nc(foldername,
				ArrayListBound.getNCeditList6Data(),
				"[NCEdit6]", date);
		Constans.cache.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		saveAs_8nc(foldername,
				ArrayListBound.getNCeditList7Data(),
				"[NCEdit7]", date);
		Constans.cache.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		saveAs_8nc(foldername,
				ArrayListBound.getNCeditList8Data(),
				"[NCEdit8]", date);
		Constans.cache.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		

	}
	private static void saveAs_8nc(String foldername,
			ArrayList<HashMap<String, Object>> ArrayList, String markname,
			String date) {
		int step1 = ArrayList.size();
		for (int i = 0; i < ArrayList.size(); i++) {
			String numString = ArrayList.get(i)
					.get("numSpinner").toString().trim();
			String orderString = ArrayList.get(i)
					.get("orderSpinner").toString().trim();
			String operatString =ArrayList.get(i)
					.get("operatText").toString().trim();
			String notesString = ArrayList.get(i)
					.get("noteEditText").toString().trim();
			if (numString.length() == 0 && orderString.length() == 0
					&& operatString.length() == 0 && notesString.length() == 0) {
				step1--;// 去掉空行行数
			}
		}
		final int step = step1;

		// 写入文件头
		Constans.cache.writeFile(Constans.nc文本数据, "[Time]" + "/" + "\r\n");
		Constans.cache.writeFile(Constans.nc文本数据, date + "/" + "\r\n");
		Constans.cache.writeFile(Constans.nc文本数据, "[Step]" + "/" + "\r\n");
		Constans.cache.writeFile(Constans.nc文本数据,
				"Step" + "=" + String.valueOf(step) + "/" + "\r\n");
		Constans.cache.writeFile(Constans.nc文本数据, markname + "/" + "\r\n");
		// 循环写入每一行代码到文件中
		for (int i = 0; i < ArrayList.size(); i++) {
			String addresssString =ArrayList.get(i)
					.get("addressText").toString().trim();
			String numString = ArrayList.get(i)
					.get("numSpinner").toString().trim();
			String orderString = ArrayList.get(i)
					.get("orderSpinner").toString().trim();
			String operatString = ArrayList.get(i)
					.get("operatText").toString().trim();
			String notesString = ArrayList.get(i)
					.get("noteEditText").toString().trim();

			if (numString.length() == 0 && orderString.length() == 0
					&& notesString.length() == 0) {
				// do nothing
			} else {
				if (numString.length() == 0) {
					numString = " ";
				}
				if (orderString.length() == 0) {
					orderString = " ";
				}
				if (operatString.length() == 0
						|| operatString.split("\\W+").length == 0) {
					operatString = " ";
				}
				if (notesString.length() == 0) {
					notesString = " ";
				}
				Constans.cache.writeFile(Constans.nc文本数据, addresssString + "/"
						+ numString + "/" + orderString + "/" + operatString
						+ "/" + notesString + "/" + "\r\n");
			}

		}

	}

	/**
	 * 
	 * @Title: saveAs_TableEdit
	 * @Description: 保存TableEdit
	 * @param foldername
	 * @param date
	 * @return void
	 * @throws
	 */
	private void saveAs_TableEdit(String foldername, String date) {
		Constans.cache.cleanFile(Constans.table文本数据);

		saveAs_8table(foldername,
				ArrayListBound.getTableList1Data(),
				"[TableEdit1]", date);
		Constans.cache.writeFile(Constans.table文本数据, "\r\n");// 不同段之间空一行
		saveAs_8table(foldername,
				ArrayListBound.getTableList2Data(),
				"[TableEdit2]", date);
		Constans.cache.writeFile(Constans.table文本数据, "\r\n");// 不同段之间空一行
		saveAs_8table(foldername,
				ArrayListBound.getTableList3Data(),
				"[TableEdit3]", date);
		Constans.cache.writeFile(Constans.table文本数据, "\r\n");// 不同段之间空一行
		saveAs_8table(foldername,
				ArrayListBound.getTableList4Data(),
				"[TableEdit4]", date);
		Constans.cache.writeFile(Constans.table文本数据, "\r\n");// 不同段之间空一行
		saveAs_8table(foldername,
				ArrayListBound.getTableList5Data(),
				"[TableEdit5]", date);
		Constans.cache.writeFile(Constans.table文本数据, "\r\n");// 不同段之间空一行
		saveAs_8table(foldername,
				ArrayListBound.getTableList6Data(),
				"[TableEdit6]", date);
		Constans.cache.writeFile(Constans.table文本数据, "\r\n");// 不同段之间空一行
		saveAs_8table(foldername,
				ArrayListBound.getTableList7Data(),
				"[TableEdit7]", date);
		Constans.cache.writeFile(Constans.table文本数据, "\r\n");// 不同段之间空一行
		saveAs_8table(foldername,
				ArrayListBound.getTableList8Data(),
				"[TableEdit8]", date);
		Constans.cache.writeFile(Constans.table文本数据, "\r\n");// 不同段之间空一行
		

	}
	private void saveAs_8table(String foldername,
			ArrayList<HashMap<String, Object>> ArrayList, String markname,
			String date) {
		int step1 = ArrayList.size();
		for (int i = 0; i < ArrayList.size(); i++) {
			String inputString = ArrayList.get(i)
					.get("inputEditText").toString().trim();
			String outputString = ArrayList.get(i)
					.get("outputEditText").toString().trim();
			String notesString = ArrayList.get(i)
					.get("noteEditText").toString().trim();
			if (inputString.length() == 0 && outputString.length() == 0
					&& notesString.length() == 0) {
				step1--;
			}
		}
		final int step = step1;

		//Constans.cache.cleanFile(Constans.table文本数据);
		// 写入文件头
		Constans.cache.writeFile(Constans.table文本数据, "[Time]" + "/" + "\r\n");
		Constans.cache.writeFile(Constans.table文本数据, date + "/" + "\r\n");
		Constans.cache.writeFile(Constans.table文本数据, "[Step]" + "/" + "\r\n");
		Constans.cache.writeFile(Constans.table文本数据,
				"Step" + "=" + String.valueOf(step) + "/" + "\r\n");
		Constans.cache.writeFile(Constans.table文本数据, markname + "/"
				+ "\r\n");
		// 循环写入每一行代码到文件中
		for (int i = 0; i < ArrayList.size(); i++) {
			String numString = ArrayList.get(i)
					.get("numText").toString().trim();
			String inputString =ArrayList.get(i)
					.get("inputEditText").toString().trim();
			String outputString = ArrayList.get(i)
					.get("outputEditText").toString().trim();
			String notesString =ArrayList.get(i)
					.get("noteEditText").toString().trim();

			if (inputString.length() == 0 && outputString.length() == 0
					&& notesString.length() == 0) {
				// do nothing
			} else {
				if (inputString.length() == 0) {
					inputString = " ";// 增加一个空格占位
				}
				if (outputString.length() == 0) {
					outputString = " ";
				}
				if (notesString.length() == 0) {
					notesString = " ";
				}
				Constans.cache.writeFile(Constans.table文本数据, numString + "/"
						+ inputString + "/" + outputString + "/" + notesString
						+ "/" + "\r\n");
			}

		}

	}

	/**
	 * 保存模具数据（xyj同学完成）
	 * 
	 * @param foldername
	 * @param date
	 */
	private void saveMouldData(String foldername, String date) {

		/*
		 * Constans.mouldData.createFolder(Constans.trPATH,Constans.mouldDataFolder
		 * ).createFile(Constans.模具数据);//mould。trt
		 * Constans.mouldData.cleanFile(Constans.模具数据);
		 * 
		 * if (position>=0) {
		 * ArrayList.get(position).put("note_mould_setting",date); }
		 * 
		 * // 循环写入每一行代码到文件中 for (int i = 0; i < ArrayList.size(); i++) { String
		 * numString = ArrayList.get(i).get("num") .toString().trim(); String
		 * num_mould_settingString = ArrayList.get(i).get("num_mould_setting")
		 * .toString().trim(); String name_mould_settingString =
		 * ArrayList.get(i).get("name_mould_setting") .toString().trim(); String
		 * note_mould_settingString = ArrayList.get(i).get("note_mould_setting")
		 * .toString().trim();
		 * 
		 * 
		 * 
		 * if (num_mould_settingString.length() == 0 &&
		 * name_mould_settingString.length() == 0 &&
		 * note_mould_settingString.length() == 0) { // do nothing } else { if
		 * (num_mould_settingString.length() == 0) { num_mould_settingString =
		 * " "; } if (name_mould_settingString.length() == 0) {
		 * name_mould_settingString = " "; } if
		 * (note_mould_settingString.length() == 0) { note_mould_settingString =
		 * " "; } Constans.cache.writeFile(Constans.模具数据, numString + "/" +
		 * num_mould_settingString + "/" + name_mould_settingString + "/" +
		 * note_mould_settingString + "/" + "\r\n"); }
		 * 
		 * }
		 */
	}

	/**
	 * 
	 * @Title: creatFolderSysytem
	 * @Description: 实现保存写文件前，要保证目录存在
	 * @return void
	 * @throws
	 */
	private void creatFolderSysytem() {
		// TODO Auto-generated method stub
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		// 创建文件夹目录
		if (!sdCardExist) {
			Toast.makeText(getApplicationContext(), "请插入外部SD存储卡",
					Toast.LENGTH_SHORT).show();// 如果不存在SD卡，进行提示
		} else {
			/* 分级目录必须分开创建，不能嵌套创建，否则子目录可能不存在 */

			File trFile = new File(Constans.trPATH);// 新建一级目录
			if (!trFile.exists()) {// 判断文件夹目录是否存在
				trFile.mkdir();// 如果不存在则创建
				Toast.makeText(getApplicationContext(),
						"新创建文件夹结构" + Constans.trFolder, Toast.LENGTH_SHORT)
						.show();// 提示新创建
			} else {
				// Toast.makeText(getApplicationContext(),
				// "已存在文件夹" + Constans.trFolder, Toast.LENGTH_SHORT)
				// .show();// 提示已创建
			}

			// 好的
			// Constans.mechanicalParameter.createFolder(Constans.mechanicalParameterPATH).createFile("mechanicalParameter.set");
			// Constans.otherParameter.createFolder(Constans.otherParameterPATH).createFile("otherParameter.trt");
			// Constans. mouldData.createFolder(Constans.mouldDataPATH);
			// Constans.mouldData1.createFolder(Constans.mouldData1PATH).createFile("mouldData.trt");//新建
			// 三级目录
			// Constans.mouldData2.createFolder(Constans.mouldData2PATH);
			// Constans.tableText.createFolder(Constans.tableTextPATH).createFile("Userlog.trt");
			// Constans.tableText.createFile("syslog.trd");
			// Constans.ncText.createFolder(Constans.ncTextPATH).createFile("userprg.trt");
			// Constans.deviceName.createFolder(Constans.deviceNamePATH).createFile("device.trt");
			// Constans.dataToDownload.createFolder(Constans.dataToDownloadPATH);
			// Constans.ncToTable.createFolder(Constans.ncToTablePATH).createFile("userprg.trd");
			// Constans.tableToData.createFolder(Constans.tableToDataPATH).createFile("Userlog.trd");
			// Constans.tableToData.createFile("syslog.trd");
			// Constans.positionSetData.createFolder(Constans.positionSetDataPATH).createFile("prdpara.set");
			// Constans.otherData.createFolder(Constans.otherDataPATH).createFile("otherData.trt");
			// Constans.mouldData3.createFolder(Constans.mouldData3PATH).createFile("mouldData3.trt");
			// Constans.mouldData4.createFolder(Constans.mouldData4PATH).createFile("mouldData4.trt");
			// Constans.otherData1.createFolder(Constans.otherData1PATH).createFile("otherData1.trt");
			// Constans.otherData2.createFolder(Constans.otherData2PATH).createFile("otherData2.trt");

			// 好的
			// Constans.mechanicalParameter.createFolder(Constans.trPATH,
			// Constans.mechanicalParameterFolder).createFile("mechanicalParameter.set");
			// Constans.otherParameter.createFolder(Constans.trPATH,
			// Constans.otherParameterFolder).createFile("otherParameter.trt");
			// Constans.mouldData.createFolder(Constans.trPATH,
			// Constans.mouldDataFolder);
			// Constans.mouldData1.createFolder(Constans.mouldDataPATH,
			// Constans.mouldData1Folder).createFile(Constans.nc文本数据)
			// .createFile(Constans.table文本数据)
			// .createFile(Constans.设备名称数据);
			// Constans.dataToDownload.createFolder(Constans.mouldData1PATH,
			// Constans.dataToDownloadFolder);
			// Constans.mouldData2.createFolder(Constans.mouldDataPATH,
			// Constans.mouldData2Folder).createFile(Constans.nc文本数据)
			// .createFile(Constans.table文本数据)
			// .createFile(Constans.设备名称数据);
			// Constans.dataToDownload.createFolder(Constans.mouldData2PATH,
			// Constans.dataToDownloadFolder);
			Constans.mechanicalParameter.createFolder(Constans.trPATH,
					Constans.mechanicalParameterFolder);
			Constans.otherParameter.createFolder(Constans.trPATH,
					Constans.otherParameterFolder).createFile(
							"otherParameter.trt");
			Constans.mouldData.createFolder(Constans.trPATH,
					Constans.mouldDataFolder);
			Constans.cache.createFolder(Constans.mouldDataPATH, "cache")
			.createFile("device.trt").createFile("userprg.trt")
			.createFile("Userlog.trt");
			Constans.dataToDownload.createFolder(Constans.mouldDataPATH
					+ "cache" + File.separator, Constans.dataToDownloadFolder);

		}
	}


	
	public static void autoSaveAfterCrash()
	{
		final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
		.format(Calendar.getInstance().getTime());// 当前时间
		saveAs_NcEdit("cache", date);
	}	

	/**
	 * 返回键直接回到主界面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(WifiSetting_Info.progressDialog!=null){
			  WifiSetting_Info.progressDialog.setCanceledOnTouchOutside(true);
			}
			Device_ListFragment.fragmentncStatusFragment=null;
			Device_ListFragment.j=1;
			startActivity(new Intent().setClass(this, TR_Main_Activity.class));
			
		}

		return super.onKeyDown(keyCode, event);
	}

}