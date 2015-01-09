package com.tr.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiReadMassiveDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.DelayTimerQueryRunnble;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.WatchRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;
import wifiRunnablesAndChatlistener.posccalmQueryRunnable;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.method.NumberKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.dataInAddress.AddressPublic;
import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.dbutils.OpenFiles;
import com.explain.HexDecoding;
import com.explain.NCTranslate;
import com.tr.ExitTR;
import com.tr.R;
import com.tr.maintainguide.TR_MaintainGuide_Activity;
import com.tr.newpragram.ListAdapter;
import com.tr.newpragram.NewPragramActivity;
import com.tr.parameter_setting.TR_Parameter_Setting_Activity;
import com.tr.programming.Config;
import com.tr.programming.TR_Programming_Activity;
import com.wifiexchange.ChatListener;
import com.wifiexchange.Client;
import com.wifiexchange.WifiSetting_Info;
/**
 * 
 * @ClassName: TR_Main_Activity
 * @Description: 主界面
 * @author 李婷婷
 * @date 2013-4-11 上午9:33:13
 * 
 */
@SuppressLint("ResourceAsColor")
public class TR_Main_Activity extends Activity {

	
	private Handler mHandler;
	private Thread cClockThread;//wifi链接成功
	private Thread mClockThread;//wifi链接失败
	private ImageButton btn_stop = null;
	private ImageButton btn_f1 = null;
	
	
	private ImageButton btn_jxwz=null;
	private ImageButton btn_timer=null;
	private ImageButton btn_counter=null;
	private ImageButton btn_zycz=null;
	private ImageButton btn_mjsjcz=null;
	private ImageButton btn_ydcz=null;
	private ImageButton btn_bjcz=null;
	private ImageButton btn_zdcz=null;
	
	private ImageButton btn_iojs=null;
	private ImageButton btn_wifilj=null;
	private ImageButton btn_xjcz=null;
	private ImageButton btn_czsc=null;
	private ImageButton btn_jbll=null;
	//private ImageButton btn_yyqh=null;
	private ImageButton btn_mmsd=null;
	//private ImageButton btn_szxtsj=null;
	//private ImageButton btn_dqbc=null;
	private ImageButton btn_fw=null;
	private ImageButton btn_bbxs=null;
	
	private TextView mjbhtext;//显示下位机当前模具信息
	private ImageView mjszImage;
	
	private Button StartWatchBtn;
	private Button StopWatchBtn;
	public static ListView WatchListView;
	private ArrayList<HashMap<String, Object>> ProgramList;
	public static ListAdapter myProgramListAdapter = null;
	int[]  alength ;
	private  WatchRunnable watchRunnable;	
	public static VerticalScrollTextView mSampleView;
	//public static TextView dqjbxxtext;//显示当前警报信息
	private  Button alarm_led;
	public static  Button wifi_led;
	//public static String[] dqjbxxstr=new String[]{};
	public static List lst=new ArrayList<Sentence>();
	private Button moving_led;
	private Button zd_led;
	private Button[] btns=new Button[27];//存放快捷按钮
	//private Button btn_manual = null;
	private static boolean clicked_btn_manual = true;
	//private Button btn_origin = null;
	private static boolean clicked_btn_origin = false;
	//private Button btn_step;
	private static boolean clicked_btn_step = false;
	//private Button btn_automatic = null;
	private static boolean clicked_btn_automatic = false;
	//private ToggleButton btn_start_stop = null;
	//private static boolean checked_btn_start_stop = false;
	//private static boolean checked_origin_start_stop = false;
	//private static boolean checked_step_start_stop = false;
	//private static boolean checked_automatic_start_stop = false;
	private VerticalSeekBar seekbar;
	private static final int manual_ID = 10;
	private int lastProgress = 0;// TODO 预留参数，从文件读取档位设置；
	private long mExitTime;
	//private Button btn_China = null;
	//private Button btn_America = null;
	//private Button btn_HK = null;
	private SharedPreferences preferences;//程序运行次数
	private SharedPreferences sharedPreference_language;
	private SharedPreferences sharedPreference_password;
	public static Boolean alreadyChecked_operatePassword = false;// 是否已经输入密码标志位，确认后才能获得权限
	private String password_operate;

	private String langString;
	private String regionString;

	private ActionBar actionBar;

	public static String title_mouldData = "";

	private static boolean firstOpen = true;
	public static SharedPreferences sharedPreference_openedDir;

	private posccalmQueryRunnable posccalmqueryrunnable;
	//private PositionQueryRunnable positionQueryRunnable;
	//private AlarmQueryRunnable alarmQueryRunnable;
	//private PositionQueryRunnable cntCycQueryRunnable;
	private ledRunnable ledrunnable;
	//private WifiConnectRunnable wifiConnectRunnable;
	private WifiReadDataFormat formatReadMessage;
	private SendDataRunnable sendDataRunnable;
	private byte[] getData;
	private  ChatListener DataFeedback ;
	private  ChatListener DataFeedback1 ;
	private  ChatListener mainDataFeedback ;
	public static String wifiNum="";
	public static String wifiName="";
	public static String wifiTime="";
	//public static String wifiAll="";
	
	private SendDataRunnable sendDataAlmRunnable;
	private WifiReadMassiveDataFormat formatReadAlmmsg;
	private WifiReadDataFormat formatReadAlm;
	private  ChatListener AlmDataFeedback ;	
	private ChatListener AlmCPFeedback;
	private int Almallnum;// 当前伺服总数
	private int AlmCP;// 当前伺服参数编号
	
	private WifiReadDataFormat formatReadusermode;
	private SendDataRunnable sendDatausermodeRunnable;
	private SendDataRunnable sendDataioRunnable;
	private WifiReadDataFormat formatReadio;
	private  ChatListener ioDataFeedback ;
	private FinishRunnable sendDataFinishRunnable;
	private ListView listView_io;
	private MyAdapter_IO adapter_io;
	private AlertDialog dialog_io;
	private ListView listView_version;
	private ListView listView_alarm;
	
	private MyAdapter_Version adapter_version;
	private MyAdapter_Alarm adapter_alarm;
	
	private ArrayList<HashMap<String, Object>> list_version;
	private ArrayList<HashMap<String, Object>> list_alarm;
	private ArrayList<HashMap<String, Object>> list_io;
	private ArrayList<HashMap<String, Object>> list_alarmzb;
	
	private RadioGroup radioGroup_password;
	private Button btn_create_password;
	private Button btn_change_password;
	private Button btn_delete_password;
	private String password_setting;
	private String password_programming;
	private String password;
	private String title_create;
	private String title_delete;
	private String title_change;
	private String passwordToDelete;
	private byte[] temp=new byte[4*6];
	private WifiSendDataFormat formatSendMessage;
	private String password_old;
	private String passwordToChange;	
	private UpdateLedReceiver BR = null;
	private AlertDialog KeyMsgDialog = null;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("mpeng","main on pause");
		//positionQueryRunnable.destroy();
		//cntCycQueryRunnable.destroy();
		//alarmQueryRunnable.destroy();
		if(dialog_io!=null){
			dialog_io.dismiss();
		}
		
		if(watchRunnable!=null){
			watchRunnable.destroy();
			StartWatchBtn.setEnabled(true);
			StopWatchBtn.setEnabled(false);
		}
		posccalmqueryrunnable.destroy();
		ledrunnable.destroy();
		//更新View
		mSampleView.destroyupdateUI();

		
		unregisterReceiver(BR);
		 if(KeyMsgDialog != null)
         	KeyMsgDialog.dismiss();
	}


	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(WifiSetting_Info.mClient!=null){
        	wifi_led.setVisibility(View.VISIBLE);
        }
		if(WifiSetting_Info.alarmFlag==0){
			alarm_led.setEnabled(false);
		}
		 list_alarmzb= ArrayListBound.getAlarmzbListData();
		new Thread() {
			public void run() {
				list_version = ArrayListBound.getVersionListData();
				list_alarm = ArrayListBound.getAlarmListData();
				list_io = ArrayListBound.getIOListData();
			   
			}

		}.start();
		System.out.println("main onResume");
		
		posccalmqueryrunnable=new posccalmQueryRunnable(TR_Main_Activity.this, findViewById(R.id.pos_foot),
				findViewById(R.id.pos_pro_qh), findViewById(R.id.pos_pro_sx),
				findViewById(R.id.pos_mat_qh), findViewById(R.id.pos_mat_sx),moving_led
                ,findViewById(R.id.counter_out_num),findViewById(R.id.counter_rejects_num));
		Thread a1 = new Thread(posccalmqueryrunnable);
		a1.start();
			
			ledrunnable=new ledRunnable(TR_Main_Activity.this);
			Thread a4 = new Thread(ledrunnable);
			a4.start();
		
		
		/*if(!dqjbxxstr.equals("")){
		dqjbxxtext.setText(dqjbxxstr);
		//alarm_led.setBackgroundDrawable(drawablealarmled);
	}*/
	
	/*for(int i=0;i<lst.size();i++){
			Sentence sen=new Sentence(i,dqjbxxstr[i]);
			lst.add(i, sen);
	}*/	
			
		if(lst.size()==0){
			Sentence sen=new Sentence(0,"无警报信息");
			lst.add(0, sen);
		}
		//给View传递数据
		mSampleView.setList(lst);
		//更新View
	    mSampleView.updateUI();
	    
	    
	    initProgramListView();
	}
	public  void showNote(){
		NCTranslate.noteList.clear();
		TR_Programming_Activity.noteflag=3;
		int[] test  =NCTranslate.beginTranslate(TR_Main_Activity.this,
				getResources().getStringArray(R.array.IF2));
		try{			
			//Log.d("mpeng"," ncdedit size is" +ProgramList.get(0).toString());
		for(int i=0;i<ProgramList.size();i++){
			if(ProgramList.get(i).get("operatText").equals("")&&ProgramList.get(i).get("orderSpinner").equals(""))
				continue;
			ProgramList.get(i).put("noteEditText", NCTranslate.noteList.get(i));
			
		}

		TR_Programming_Activity.noteflag=0;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	private void initProgramListView()
	{

		 showNote();
		 myProgramListAdapter = new ListAdapter(this, ProgramList) ;
		 WatchListView.setAdapter(myProgramListAdapter);
		 myProgramListAdapter.notifyDataSetChanged();
		
	}
	
	
	class DelayCountTimeQuery extends CountDownTimer {
		public DelayCountTimeQuery(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			System.out.println("定时时间到");
			 DelayTimerQueryRunnble tempRunnble=new DelayTimerQueryRunnble(TR_Main_Activity.this, listView_io);
			 new Thread(tempRunnble).start();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			
		}  
		
	}
	public void getAlmdata(){
		System.out.println(".="+Almallnum+"       AlmCP======="+AlmCP);
		formatReadAlmmsg = new WifiReadMassiveDataFormat(0x40024304,12*128);
		try {
			sendDataAlmRunnable=new SendDataRunnable(formatReadAlmmsg, TR_Main_Activity.this);

			sendDataFinishRunnable=new FinishRunnable(TR_Main_Activity.this, "数据读取完毕",readAlmDataFinishTodo);

			sendDataAlmRunnable.setOnlistener(new NormalChatListenner(sendDataAlmRunnable, sendDataFinishRunnable));

			runOnUiThread(sendDataAlmRunnable);
	
			} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(TR_Main_Activity.this, e.toString(), Toast.LENGTH_LONG);
		}
	}
	//获取返回的数据后进行的UI操作
	private final Runnable readAlmDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据
			try{
			getData=new byte[formatReadAlmmsg.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadAlmmsg.getFinalData() != null)  
			{
			System.arraycopy(formatReadAlmmsg.getFinalData(), 0, getData, 0, formatReadAlmmsg.getLength());
			int i = AlmCP;
			// 先显示出来再说 
			for (int j = 0; j < AlmCP; j++) { // 怎么动态去读
				String str;
				i = i - 1;
				int alm = (int)getData[12*i];
				switch( alm )
				{
				case 0:
					str = " 无 ";
					break;
				case 1:		// 系统警报
					str = " 系统警报";
					break;
				case 2:		// 伺服警报
					str = " 伺服警报";
					break;
				case 3:		// 	动作警报
					str = " 动作警报";
					break;
				default:
					str = "其他警报";
					break;
				}
				if( alm == 0)
					continue;

				alm = HexDecoding.Array2Toint(getData, 12*i+2);
				if(str.equals(" 伺服警报")){
					int seralm=alm/256;
					switch( seralm )
					{
					case 0:
						str = " 伺服一警报";
						break;
					case 1:		
						str = " 伺服二警报";
						break;
					case 2:		
						str = " 伺服三警报";
						break;
					case 3:		
						str = " 伺服四警报";
						break;
					case 4:
						str = " 伺服五警报";
						break;
					case 5:		
						str = " 伺服六警报";
						break;
					case 6:		
						str = " 伺服七警报";
						break;
					case 7:		
						str = " 伺服八警报";
						break;
					default:
						break;
					}
					str += alm%256;
				}else{
					str += String.valueOf(alm);	
				}
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("num_alarm","警报"+ (j+1) );
 				map.put("time_alarm", str);
 				
 				str = "20";
 				alm = getData[12*i+4];
 				str += String.valueOf(alm);
 				alm = getData[12*i+5];
 				str += "-";
 				str += String.valueOf(alm);
 				str += "-";
 				alm = getData[12*i+6];
 				str += String.valueOf(alm);
 				
 				str += "  ";
 				alm = getData[12*i+8];
 				str += String.valueOf(alm);
 				
 				str += ":";
 				alm = getData[12*i+9];
 				str += String.valueOf(alm);
 				
 				str += ":";
 				alm = getData[12*i+10];
 				str += String.valueOf(alm);
 				
 				
 				map.put("content_alarm", str);
 				list_alarm.add(map);
			}
			int k = Almallnum-AlmCP;
			for (int j = 0; j < k; j++) { // 怎么动态去读
				String str;
				int alm = (int)getData[12*(128-1-j)];
				switch( alm )
				{
				case 0:
					str = " 无 ";
					break;
				case 1:		// 系统警报
					str = " 系统警报";
					break;
				case 2:		// 伺服警报
					str = " 伺服警报";
					break;
				case 3:		// 	动作警报
					str = " 动作警报";
					break;
				default:
					str = "其他警报";
					break;
				}
				if( alm == 0)
					continue;

				alm = HexDecoding.Array2Toint(getData, 12*(128-1-j)+2);
				if(str.equals(" 伺服警报")){
					int seralm=alm/256;
					switch( seralm )
					{
					case 0:
						str = " 伺服一警报";
						break;
					case 1:		
						str = " 伺服二警报";
						break;
					case 2:		
						str = " 伺服三警报";
						break;
					case 3:		
						str = " 伺服四警报";
						break;
					case 4:
						str = " 伺服五警报";
						break;
					case 5:		
						str = " 伺服六警报";
						break;
					case 6:		
						str = " 伺服七警报";
						break;
					case 7:		
						str = " 伺服八警报";
						break;
					default:
						break;
					}
					str += alm%256;
				}else{
					str += String.valueOf(alm);	
				}
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("num_alarm","警报"+ (j+1+AlmCP) );
 				map.put("time_alarm", str);
 				
 				str = "20";
 				alm = getData[12*(128-1-j)+4];
 				str += String.valueOf(alm);
 				alm = getData[12*(128-1-j)+5];
 				str += "-";
 				str += String.valueOf(alm);
 				str += "-";
 				alm = getData[12*(128-1-j)+6];
 				str += String.valueOf(alm);
 				
 				str += "  ";
 				alm = getData[12*(128-1-j)+8];
 				str += String.valueOf(alm);
 				
 				str += ":";
 				alm = getData[12*(128-1-j)+9];
 				str += String.valueOf(alm);
 				
 				str += ":";
 				alm = getData[12*(128-1-j)+10];
 				str += String.valueOf(alm);
 				
 				
 				map.put("content_alarm", str);
 				list_alarm.add(map);
			}
			    adapter_alarm=new MyAdapter_Alarm(TR_Main_Activity.this, list_alarm, R.layout.maintain_guide_alarm_item, 
						 new String[] { "num_alarm", "time_alarm", "content_alarm"},
						 new int[] { R.id.num_alarm, R.id.time_alarm,R.id.content_alarm});
				 listView_alarm.setAdapter(adapter_alarm);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		}

	};
	//获取返回的数据后进行的UI操作
		private final Runnable readversionDataFinishTodo = new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//对于返回的36字节
				//发送正确且完成
				//处理返回的数据	
				getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				if( formatReadMessage.getFinalData() != null)  
				{
				System.arraycopy(formatReadMessage.getFinalData(), 0, getData, 0, formatReadMessage.getLength());
				//重新给list_usermode列表赋值
		
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("boot", "《数据版本》");
				map.put("boot_version", "");
				map.put("data", "");
				map.put("data_version", "");
				list_version.set(0,map);
				
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("boot", "系统Table版本");
				map1.put("boot_version", "");
				map1.put("data", "机械参数版本");
				map1.put("data_version", "");	
				list_version.set(1, map1);
			
				HashMap<String, Object> map2 = new HashMap<String, Object>();
				map2.put("boot", "");
				map2.put("boot_version", "");
				map2.put("data", "");
				map2.put("data_version", "");
				list_version.set(2, map2);
				
				HashMap<String, Object> map3 = new HashMap<String, Object>();
				map3.put("boot", "《boot版本》");
				map3.put("boot_version", "");
				map3.put("data", "");
				map3.put("data_version", "");
				list_version.set(3, map3);
				
				HashMap<String, Object> map4 = new HashMap<String, Object>();
				map4.put("boot", "CPU");
				int ver = HexDecoding.Array4Toint(getData, 0);
				String str = String.valueOf(ver);
				map4.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 8);
				str = String.valueOf(ver);
				map4.put("data", "PPU");
				map4.put("data_version", str);
				list_version.set(4, map4);
				
				
				HashMap<String, Object> map5 = new HashMap<String, Object>();
				map5.put("boot", "");
				map5.put("boot_version", "");
				map5.put("data", "");
				map5.put("data_version", "");
				list_version.set(5, map5);
				
				HashMap<String, Object> map6 = new HashMap<String, Object>();
				map6.put("boot", "《系统程序版本》");
				map6.put("boot_version", "");
				map6.put("data", "");
				map6.put("data_version", "");
				list_version.set(6, map6);
				
				HashMap<String, Object> map7 = new HashMap<String, Object>();
				map7.put("boot", "CPU");
				ver = HexDecoding.Array4Toint(getData, 4);
				str = String.valueOf(ver);
				map7.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 12);
				str = String.valueOf(ver);
				map7.put("data", "PPU");
				map7.put("data_version", str);
				list_version.set(7, map7);
				
				HashMap<String, Object> map8 = new HashMap<String, Object>();
				map8.put("boot", "平板程序版本");
				map8.put("boot_version", "14100010(内部使用)");
				ver = HexDecoding.Array4Toint(getData, 112);
				str = String.valueOf(ver);
				map8.put("data", "操作盒程序版本");
				map8.put("data_version", str);
				list_version.set(8,map8);
				
				HashMap<String, Object> map9 = new HashMap<String, Object>();
				ver = HexDecoding.Array4Toint(getData, 116);
				str = String.valueOf(ver);
				map9.put("boot", "互锁基板程序版本");
				map9.put("boot_version", str);
				map9.put("data", "");
				map9.put("data_version", "");
				list_version.set(9,map9);
				
				HashMap<String, Object> map10 = new HashMap<String, Object>();
				map10.put("boot", "");
				map10.put("boot_version", "");
				map10.put("data", "");
				map10.put("data_version", "");
				list_version.set(10, map10);
				
				HashMap<String, Object> map11 = new HashMap<String, Object>();
				map11.put("boot", "《IO系统版本》");
				map11.put("boot_version", "");
				map11.put("data", "");
				map11.put("data_version", "");
				list_version.set(11, map11);
				
				HashMap<String, Object> map12 = new HashMap<String, Object>();
				map12.put("boot", "IO1版本");
				ver = HexDecoding.Array4Toint(getData, 16);
				str = String.valueOf(ver);
				map12.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 20);
				str = String.valueOf(ver);
				map12.put("data", "IO2版本");
				map12.put("data_version", str);
				list_version.set(12, map12);
				
				HashMap<String, Object> map13 = new HashMap<String, Object>();
				map13.put("boot", "IO3版本");
				ver = HexDecoding.Array4Toint(getData, 24);
				str = String.valueOf(ver);
				map13.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 28);
				str = String.valueOf(ver);
				map13.put("data", "IO4版本");
				map13.put("data_version", str);
				list_version.set(13, map13);
				
				HashMap<String, Object> map14 = new HashMap<String, Object>();
				map14.put("boot", "IO5版本");
				ver = HexDecoding.Array4Toint(getData, 32);
				str = String.valueOf(ver);
				map14.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 36);
				str = String.valueOf(ver);
				map14.put("data", "IO6版本");
				map14.put("data_version", str);
				list_version.set(14, map14);
				
				HashMap<String, Object> map15 = new HashMap<String, Object>();
				map15.put("boot", "IO7版本");
				ver = HexDecoding.Array4Toint(getData, 40);
				str = String.valueOf(ver);
				map15.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 44);
				str = String.valueOf(ver);
				map15.put("data", "IO8版本");
				map15.put("data_version", str);
				list_version.set(15, map15);
				
				HashMap<String, Object> map16 = new HashMap<String, Object>();
				map16.put("boot", "");
				map16.put("boot_version", "");
				map16.put("data", "");
				map16.put("data_version", "");
				list_version.set(16,map16);
				
				HashMap<String, Object> map17 = new HashMap<String, Object>();
				map17.put("boot", "《SPU系统版本》");
				map17.put("boot_version", "");
				map17.put("data", "");
				map17.put("data_version", "");
				list_version.set(17,map17);
				
				HashMap<String, Object> map18 = new HashMap<String, Object>();
				map18.put("boot", "SPU1版本");
				ver = HexDecoding.Array4Toint(getData, 48);
				str = String.valueOf(ver);
				map18.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 52);
				str = String.valueOf(ver);
				map18.put("data", "SPU2版本");
				map18.put("data_version", str);
				list_version.set(18, map18);
				
				HashMap<String, Object> map19 = new HashMap<String, Object>();
				map19.put("boot", "SPU3版本");
				ver = HexDecoding.Array4Toint(getData, 56);
				str = String.valueOf(ver);
				map19.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 60);
				str = String.valueOf(ver);
				map19.put("data", "SPU4版本");
				map19.put("data_version", str);
				list_version.set(19, map19);
				
				HashMap<String, Object> map20 = new HashMap<String, Object>();
				map20.put("boot", "SPU5版本");
				ver = HexDecoding.Array4Toint(getData, 64);
				str = String.valueOf(ver);
				map20.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 68);
				str = String.valueOf(ver);
				map20.put("data", "SPU6版本");
				map20.put("data_version", str);
				list_version.set(20, map20);
				
				HashMap<String, Object> map21 = new HashMap<String, Object>();
				map21.put("boot", "SPU7版本");
				ver = HexDecoding.Array4Toint(getData, 72);
				str = String.valueOf(ver);
				map21.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 76);
				str = String.valueOf(ver);
				map21.put("data", "SPU8版本");
				map21.put("data_version", str);
				list_version.set(21, map21);
				
				HashMap<String, Object> map22 = new HashMap<String, Object>();
				map22.put("boot", "");
				map22.put("boot_version", "");
				map22.put("data", "");
				map22.put("data_version", "");
				list_version.set(22, map22);
				
				HashMap<String, Object> map23 = new HashMap<String, Object>();
				map23.put("boot", "《伺服版本》");
				map23.put("boot_version", "");
				map23.put("data", "");
				map23.put("data_version", "");
				list_version.set(23, map23);
				
				HashMap<String, Object> map24 = new HashMap<String, Object>();
				map24.put("boot", "伺服1版本");
				ver = HexDecoding.Array4Toint(getData, 80);
				str = String.valueOf(ver);
				map24.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 84);
				str = String.valueOf(ver);
				map24.put("data", "伺服2版本");
				map24.put("data_version", str);
				list_version.set(24, map24);
				
				HashMap<String, Object> map25 = new HashMap<String, Object>();
				map25.put("boot", "伺服3版本");
				ver = HexDecoding.Array4Toint(getData, 88);
				str = String.valueOf(ver);
				map25.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 92);
				str = String.valueOf(ver);
				map25.put("data", "伺服4版本");
				map25.put("data_version", str);
				list_version.set(25, map25);
				
				HashMap<String, Object> map26 = new HashMap<String, Object>();
				map26.put("boot", "伺服5版本");
				ver = HexDecoding.Array4Toint(getData, 96);
				str = String.valueOf(ver);
				map26.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 100);
				str = String.valueOf(ver);
				map26.put("data", "伺服6版本");
				map26.put("data_version", str);
				list_version.set(26, map26);
				
				HashMap<String, Object> map27 = new HashMap<String, Object>();
				map27.put("boot", "伺服7版本");
				ver = HexDecoding.Array4Toint(getData, 104);
				str = String.valueOf(ver);
				map27.put("boot_version", str);
				ver = HexDecoding.Array4Toint(getData, 108);
				str = String.valueOf(ver);
				map27.put("data", "伺服8版本");
				map27.put("data_version", str);
				list_version.set(27, map27);
				adapter_version.notifyDataSetChanged();
					
			}
			}

		};
	//获取返回的数据后进行的UI操作
	private final Runnable readioDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			try{
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReadio.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadio.getFinalData() != null)  
			{
			System.arraycopy(formatReadio.getFinalData(), 0, getData, 0, formatReadio.getLength());
			ArrayList<HashMap<String, Object>> InputList = ArrayListBound.getDeviceActualInputListData();
			ArrayList<HashMap<String, Object>> OutputListData = ArrayListBound.getDeviceActualOutputListData();
			//重新给list_usermode列表赋值
			for (int i = 0; i < 128; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("io_address",i);
 				map.put("i_symbolName", InputList.get(i).get("symbolNameEditText"));
 				map.put("i_signalName", InputList.get(i).get("signalNameEditText"));
 				map.put("o_symbolName", OutputListData.get(i).get("symbolNameEditText"));
 				map.put("o_signalName", OutputListData.get(i).get("signalNameEditText")); 
 				 for(int j=0;j<8;j++){
			    	 if(i%8==j){
			    		 map.put("i_state", (int)((getData[i/8]>>j)&0x01));
			    		 map.put("o_state", (int)((getData[i/8+16]>>j)&0x01));
			    	 }	
			    }
 				if(map.get("i_symbolName").equals("")&&map.get("o_symbolName").equals("")){
 				}else{
 				list_io.add(map);
 				}
			}
				 adapter_io=new MyAdapter_IO(TR_Main_Activity.this, list_io, R.layout.maintainguide_maintain_io_item, 
						 new String[] { "io_address", "i_symbolName", "i_signalName","i_state", "o_symbolName", "o_signalName","o_state"},
						 new int[] { R.id.io_address, R.id.i_symbolName,R.id.i_signalName,R.id.i_state, R.id.o_symbolName,R.id.o_signalName,R.id.o_state});
				 listView_io.setAdapter(adapter_io);
				 DelayCountTimeQuery tempCount=new DelayCountTimeQuery(500, 100);
				  tempCount.start();
				 listView_io.setOnScrollListener(new AbsListView.OnScrollListener() {
						
						@Override
						public void onScrollStateChanged(AbsListView view, int scrollState) {
							// TODO Auto-generated method stub
							if(scrollState==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
								System.out.print("手触滑动");
							}
                            if(scrollState==OnScrollListener.SCROLL_STATE_FLING){
                            	System.out.print("惯性滑动");
							}
                            if(scrollState==OnScrollListener.SCROLL_STATE_IDLE){
                            	System.out.print("滑动停止");
                            }
							   DelayTimerQueryRunnble.destroy();
							   DelayCountTimeQuery tempCount=new DelayCountTimeQuery(500, 100);
								  tempCount.start();
						}
						
						@Override
						public void onScroll(AbsListView view, int firstVisibleItem,
								int visibleItemCount, int totalItemCount) {
							// TODO Auto-generated method stub
							//延时开始查询，待listview更新完成后开始查询
							  
						}
					});
		}} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	};
	

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("main onCreate");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		System.out.println("TR_Main_Activity onCreate");
//		ExitTR.getInstance().addActivity(this);
		ProgramList= ArrayListBound.getNCeditList3Data();
		sharedPreference_password = getSharedPreferences("password",
				Context.MODE_PRIVATE);
		/**
		 ******************************** 
		 * 判断上次使用时设置的语言，自动设置
		 ******************************** 
		 */
		sharedPreference_language = getSharedPreferences("language",
				Context.MODE_PRIVATE);// language为xml文件名
		// 得到文件中的name标签值，第二个参数表示如果没有这个标签的话，返回的默认值
		langString = sharedPreference_language.getString("language", "zh");// 这个language为语言代号，与上一个不同
		regionString = sharedPreference_language.getString("region", "CN");// 存放地区

		Locale locale = new Locale(langString, regionString);// 开机显示上次设置语言
		if (Locale.getDefault() != locale) {// 与默认语言不同时才切换语言
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			getResources().updateConfiguration(config, null);
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		setContentView(R.layout.tr_main);

		
		preferences = getSharedPreferences("count",MODE_WORLD_READABLE); 
		int count = preferences.getInt("count", 0); 

		//判断程序与第几次运行，如果是第一次运行则重装文件系统（警报信息、机械参数、字库） 
		if (count == 0) { 
			creatFolderSysytem();// 创建文件夹系统，要完善
			copyFromRawToSD();// 将预先放好的资源拷贝到sd，以备后续使用
			
		} 
		Editor editor = preferences.edit(); 
		//存入数据 
		editor.putInt("count", ++count); 
		//提交修改 
		editor.commit();
		
		
		mHandler=new Handler()
		{
			public void handleMessage(Message msg)
			{
				if(msg.what==1)
				{
					wifi_led.setVisibility(View.VISIBLE);
					new AlertDialog.Builder(TR_Main_Activity.this).setTitle(R.string.T_titlenotice)
					   .setMessage(R.string.T_wificonnsuccess)
					   .setPositiveButton(R.string.T_titlesure, null).show();
					
				}else if(msg.what == 22)
				{
					initData();
					Log.d("TrMain","init data !!!");
				}
				else{
					new AlertDialog.Builder(TR_Main_Activity.this).setTitle(R.string.T_titlenotice)
					   .setMessage(R.string.T_wificonnfailmain)
					   .setPositiveButton(R.string.T_titlesure, null).show();
				}
				super.handleMessage(msg);

			}
		};
		BR = new UpdateLedReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("updateBtnBg");
        filter.addAction("KeyMsg");
        registerReceiver(BR, filter);
		
		if (firstOpen){// 开机第一次进入		
			firstOpen = false;
			/*if (WifiSetting_Info.mClient == null) {  //第一次进入时，wifi没有发生变化，BroadcastReceiver不会接收到广播，先自动连接
				new Thread(tempRunnable).start();
			}*/
			Message msg = new Message() ;
			msg.what = 22;
			mHandler.sendMessage(msg);
			 new TR_Programming_Activity().openAllData("cache",1,TR_Main_Activity.this);//不用先登录编程类界面，然后在登录设定类界面时，位置和速度界面中的内容才能显示
			sharedPreference_openedDir = getSharedPreferences("openedDir",
					Context.MODE_PRIVATE);
			/*String name = sharedPreference_openedDir.getString("dir_name", "");
			if (name.equals("")) {// 模具数据名称为空，说明之前没有打开数据
				title_mouldData = "当前未载入任何模具数据";
			} else {
				String strNum = sharedPreference_openedDir.getString("dir_num","");
				String strName = sharedPreference_openedDir.getString("dir_name", "");
				String strTime = sharedPreference_openedDir.getString("dir_time", "");
				title_mouldData = "模具编号：" + strNum + " | 名称：" + strName+ " | 最后修改时间：" + strTime;
			}*/

//			// 创建一个NotificationManager的引用
//			NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//			Notification notification = new Notification(R.drawable.manual,
//					"a new manual notification!", System.currentTimeMillis());
//			// 定义Notification的各种属性
//			notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
//			notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
//			/* 设置notification发生时同时发出默认声音 */
//			notification.defaults = Notification.DEFAULT_SOUND;
//			// 点亮屏幕
//			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
//			notification.defaults = Notification.DEFAULT_LIGHTS;
//			notification.ledARGB = Color.BLUE;
//			notification.ledOnMS = 5000;
//
//			CharSequence contentTitle = "manual mode（手动模式）";// 通知栏标题
//			CharSequence contentText = "手动模式已开启";// 通知栏内容
//			/* 创建新的Intent，作为单击Notification留言条时，会运行的Activity */
//			Intent notificationIntent = new Intent(TR_Main_Activity.this,
//					TR_Programming_Activity.class); // 点击该通知后要跳转的Activity，试验用
//
//			// notificationIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
//
//			/* 创建PendingIntent作为设置递延运行的Activity */
//			PendingIntent contentItent = PendingIntent.getActivity(
//					TR_Main_Activity.this, 0, notificationIntent, 0);
//
//			notification.setLatestEventInfo(TR_Main_Activity.this,
//					contentTitle, contentText, contentItent);
//
//			/*
//			 * 把Notification传递给NotificationManager，
//			 * 由于每次更改在线状态时，发出的Notification的id皆为0，
//			 * 因此当新的Notification被发出时，会替换掉旧的Notification，造成不断变换状态栏上的ICON的效果，
//			 * 若发出的Notification的id不同，状态栏上就会显示不同的ICON
//			 */
//			mNotificationManager.notify(manual_ID, notification);

				
		}
		
		init();// 初始化

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
					//WifiSetting_Info.blockFlag = true;
					getData=new byte[formatReadMessage.getLength()];
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
							formatReadMessage=new WifiReadDataFormat(AddressPublic.model_Head,28);
							try {
								sendDataRunnable = new SendDataRunnable(DataFeedback1, formatReadMessage,
										TR_Main_Activity.this);
								runOnUiThread(sendDataRunnable);
							} catch (Exception e) {
								Toast.makeText(TR_Main_Activity.this, e.toString(), Toast.LENGTH_LONG);
							}
						}
					}
			
		};
		
		DataFeedback1 = new ChatListener() {
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
					byte[] numbyte=new byte[2];
					byte[] namebyte=new byte[18];
					byte[] timebyte=new byte[8];
					//获取返回的数据，从第八位开始拷贝数据
					System.arraycopy(message, 8, numbyte, 0,2);
					System.arraycopy(message, 10, namebyte, 0,18);
					System.arraycopy(message, 28, timebyte, 0,8);
					wifiNum = String.format("%04d",HexDecoding.Array2Toint(numbyte));
				    try {
						wifiName=new String(namebyte,"GBK");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String year=String.valueOf((int)(timebyte[0]&0xff));
					String month=String.valueOf((int)(timebyte[1]&0xff));
					String day=String.valueOf((int)(timebyte[2]&0xff));
					String hour=String.valueOf((int)(timebyte[3]&0xff));
					String miun=String.valueOf((int)(timebyte[4]&0xff));
					String mill=String.valueOf((int)(timebyte[5]&0xff));
					if(month.length()==1){month="0"+month;}
					if(day.length()==1){day="0"+day;}
					if(hour.length()==1){hour="0"+hour;}
					if(miun.length()==1){miun="0"+miun;}
					if(mill.length()==1){mill="0"+mill;}
					wifiTime ="20"+year+"-"+month+"-"+day+" "+hour+":"+miun+":"+mill;
					WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);//获取当前正在连接的wifi名  
				    WifiInfo wifiInfo = wifiManager.getConnectionInfo(); 
					if (wifiName.equals("")) {// 模具数据名称为空，说明机械手中未运行程序
						//title_mouldData = "当前未载入任何模具数据"+"  ("+wifiInfo.getSSID()+")";
						title_mouldData = "main基板当前未载入任何模具数据";
					} else {
						title_mouldData = "模具编号：" + wifiNum + " | 名称：" + wifiName;
								//+ " | 最后修改时间：" + wifiTime+"  ("+wifiInfo.getSSID()+")";
					}
                    Runnable barShowRunnable=new Runnable() {
    					public void run() {
    						/*actionBar = getActionBar();
					actionBar.setDisplayShowTitleEnabled(true);
					actionBar.setSubtitle(title_mouldData);*/
    					mjbhtext.setText(title_mouldData);
    					//Bitmap bm = BitmapFactory.decodeFile(Constans.mouldDataPATH + wifiName.trim()+".png");
//    					Bitmap bm = getLoacalBitmap(Constans.mouldDataPATH + wifiName.trim()+ File.separator+ wifiName.trim()+".png"); 
//    					mjszImage.setImageBitmap(bm);
					if (WifiSetting_Info.mClient != null) {
						String strNum = TR_Main_Activity.sharedPreference_openedDir.getString("dir_num","");
						String strName = TR_Main_Activity.sharedPreference_openedDir.getString("dir_name", "").trim();
						String strTime =TR_Main_Activity.sharedPreference_openedDir.getString("dir_time", "").trim();
						System.out.println("strName="+strName+"        "+wifiName.trim());
						System.out.println("strTime="+strTime+"        "+wifiTime.trim());
						if(strName.equals(wifiName.trim())&&strTime.equals(wifiTime.trim())){
							/*new AlertDialog.Builder(TR_Main_Activity.this).setTitle("提示")
							   .setMessage("下位机当前的模具数组与平板当前的模具数组一致，可正常调试！")
							   .setPositiveButton("确定", null).show();*/
						}else{
							new AlertDialog.Builder(TR_Main_Activity.this).setTitle(R.string.T_titlenotice)
							   .setMessage(R.string.T_mouldsnosame)
							   .setPositiveButton(R.string.T_titlesure, null).show();
						}
						}
    					}
    				};
    				runOnUiThread(barShowRunnable);
					
				  }
				}
			
		};
		if (WifiSetting_Info.mClient != null) {
			formatReadMessage=new WifiReadDataFormat(AddressPublic.model_Head,28);
			try {
				sendDataRunnable = new SendDataRunnable(DataFeedback1, formatReadMessage,
						TR_Main_Activity.this);
				runOnUiThread(sendDataRunnable);
			} catch (Exception e) {
				Toast.makeText(TR_Main_Activity.this, e.toString(), Toast.LENGTH_LONG);
			}
		}
		mainDataFeedback = new ChatListener() {
			@Override
			public void onChat(byte[] message) {

				//返回的标志位STS1处的判断以及和校验
				formatReadMessage.backMessageCheck(message);
				if(!formatReadMessage.finishStatus()){
					runOnUiThread(sendDataRunnable);			
				}else {
					//发送正确且完成
					//处理返回的数据	
					getData=new byte[formatReadMessage.getLength()];
					//获取返回的数据，从第八位开始拷贝数据
					System.arraycopy(message, 8, getData, 0, formatReadMessage.getLength());
					// TODO Auto-generated method stub
					try{
					        int bit31=(int)(getData[3]&0x80);
					        int bit30=(int)(getData[3]&0x40); 
					        System.out.println("bit31="+bit31+"        "+"bit30="+bit30);
					        if(true){
					        //if(bit31==0x80&&bit30==0x40){	
					        	if(WifiSetting_Info.alarmFlag==0){
					    			alarm_led.setEnabled(false);
					    		}
					        	cClockThread=new cLooperThread();//因为在onChat内部不能写“窗体”功能函数，所以使用Handler机制
					    		cClockThread.start();

								formatReadMessage=new WifiReadDataFormat(AddressPublic.model_allspeed,2);
								try {
									sendDataRunnable = new SendDataRunnable(DataFeedback, formatReadMessage,
											TR_Main_Activity.this);
									runOnUiThread(sendDataRunnable);
								} catch (Exception e) {
									Toast.makeText(TR_Main_Activity.this, e.toString(), Toast.LENGTH_LONG);
								}
								
					        }else{
					        	//posccalmqueryrunnable.destroy();
					        	//positionQueryRunnable.destroy();
								//cntCycQueryRunnable.destroy();
								//alarmQueryRunnable.destroy();
								WifiSetting_Info.mClient.disconnect();
								mClockThread=new LooperThread();
								mClockThread.start();
					        }
					    }catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
			
		};
	
		
		
	}
	class cLooperThread extends Thread
	{
		public void run()
		{
			super.run();
			try
			{
					Message m=new Message();
					m.what=1;
					TR_Main_Activity.this.mHandler.sendMessage(m);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	class LooperThread extends Thread
	{
		public void run()
		{
			super.run();
			try
			{
					Message m=new Message();
					m.what=0;
					TR_Main_Activity.this.mHandler.sendMessage(m);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	

	/**
	 * 
	 * @Title: copyFromRawToSD
	 * @Description: 将程序中的raw文件夹下的文件都拷贝到SD中的raw文件夹中
	 * @return void
	 * @throws
	 */
	private void copyFromRawToSD() {
		String rawPath = Constans.trPATH + "raw" + "/";
		File rawDir = new File(rawPath);
		//int mechanicalFlag=0;
		if (!rawDir.exists()) {
			rawDir.mkdirs();
		}
		/*String mechanicalParameterPATH = Constans.mechanicalParameterPATH;
		rawDir = new File(mechanicalParameterPATH);
		if (!rawDir.exists()) {
			rawDir.mkdirs();
			mechanicalFlag=1;
		}*/
		String zkDataPATH = Constans.zkDataPATH;
		rawDir = new File(zkDataPATH);
		if (!rawDir.exists()) {
			rawDir.mkdirs();
		}
		String alarmDataPATH = Constans.alarmDataPATH;
		rawDir = new File(alarmDataPATH);
		if (!rawDir.exists()) {
			rawDir.mkdirs();
		}
		Field[] raw = R.raw.class.getFields();// 利用反射得到所有文件，不用知道具体的名称
		for (Field r : raw) {
			try {
				System.out.println("R.raw." + r.getName());
				int id = getResources().getIdentifier(r.getName(), "raw",
						getPackageName());
				if (r.getName().endsWith("_ogg")) {
					String path = rawPath + r.getName() + ".ogg";
					BufferedOutputStream outBuff = new BufferedOutputStream(
							(new FileOutputStream(new File(path))));
					BufferedInputStream inBuff = new BufferedInputStream(
							getResources().openRawResource(id));
					byte[] buff = new byte[20 * 1024];
					int len;
					while ((len = inBuff.read(buff)) > 0) {
						outBuff.write(buff, 0, len);
					}
					outBuff.flush();
					outBuff.close();
					inBuff.close();
				} else if (r.getName().endsWith("_png")) {
					String path = rawPath + r.getName() + ".png";
					BufferedOutputStream outBuff = new BufferedOutputStream(
							(new FileOutputStream(new File(path))));
					BufferedInputStream inBuff = new BufferedInputStream(
							getResources().openRawResource(id));
					byte[] buff = new byte[20 * 1024];
					int len;
					while ((len = inBuff.read(buff)) > 0) {
						outBuff.write(buff, 0, len);
					}
					outBuff.flush();
					outBuff.close();
					inBuff.close();
				}else if (r.getName().contains("activity")) {
					String path = rawPath + r.getName() + ".pdf";
					BufferedOutputStream outBuff = new BufferedOutputStream(
							(new FileOutputStream(new File(path))));
					BufferedInputStream inBuff = new BufferedInputStream(
							getResources().openRawResource(id));
					byte[] buff = new byte[20 * 1024];
					int len;
					while ((len = inBuff.read(buff)) > 0) {
						outBuff.write(buff, 0, len);
					}
					outBuff.flush();
					outBuff.close();
					inBuff.close();
				} else if (r.getName().contains("hzdot")) {
					String path = zkDataPATH + r.getName() + ".bin";
					BufferedOutputStream outBuff = new BufferedOutputStream(
							(new FileOutputStream(new File(path))));
					BufferedInputStream inBuff = new BufferedInputStream(
							getResources().openRawResource(R.raw.hzdot));
					byte[] buff = new byte[20 * 1024];
					int len;
					while ((len = inBuff.read(buff)) > 0) {
						outBuff.write(buff, 0, len);
					}
					outBuff.flush();
					outBuff.close();
					inBuff.close();
				}else if (r.getName().contains("alarm")) {
					String path = alarmDataPATH + r.getName() + ".trt";
					BufferedOutputStream outBuff = new BufferedOutputStream(
							(new FileOutputStream(new File(path))));
					BufferedInputStream inBuff = new BufferedInputStream(
							getResources().openRawResource(id));
					byte[] buff = new byte[20 * 1024];
					int len;
					while ((len = inBuff.read(buff)) > 0) {
						outBuff.write(buff, 0, len);
					}
					outBuff.flush();
					outBuff.close();
					inBuff.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @Title: checkBtn
	 * @Description: 
	 *               由于定制了返回键的功能，返回主界面时是新开启的，对于手动等按钮进行判断，使每次回来时都能对应正确的按钮；直接返回时就不用
	 * @return void
	 * @throws
	 */
/*	private void checkBtn() {

		if (clicked_btn_manual) {
			btn_manual.setBackgroundColor(Color.GREEN);
			btn_origin.setBackgroundColor(R.color.lightgray);
			btn_step.setBackgroundColor(R.color.lightgray);
			btn_automatic.setBackgroundColor(R.color.lightgray);
		} else if (clicked_btn_origin) {
			btn_origin.setBackgroundColor(Color.GREEN);
			btn_manual.setBackgroundColor(R.color.lightgray);
			btn_step.setBackgroundColor(R.color.lightgray);
			btn_automatic.setBackgroundColor(R.color.lightgray);
		} else if (clicked_btn_step) {
			btn_step.setBackgroundColor(Color.GREEN);
			btn_manual.setBackgroundColor(R.color.lightgray);
			btn_origin.setBackgroundColor(R.color.lightgray);
			btn_automatic.setBackgroundColor(R.color.lightgray);
		} else if (clicked_btn_automatic) {
			btn_automatic.setBackgroundColor(Color.GREEN);
			btn_manual.setBackgroundColor(R.color.lightgray);
			btn_origin.setBackgroundColor(R.color.lightgray);
			btn_step.setBackgroundColor(R.color.lightgray);
		}
		if (checked_btn_start_stop) {
			btn_start_stop.setBackgroundColor(Color.GREEN);
			image_autoStart_main.setBackgroundColor(Color.RED);
			image_autoStart_main.setEnabled(true);
		} else {
			btn_start_stop.setBackgroundColor(R.color.lightgray);
			image_autoWait_main.setBackgroundColor(Color.RED);
			image_autoWait_main.setEnabled(true);
		}

	}*/
	
	/***
	 * socket连接
	 */
	private Runnable tempRunnable=new Runnable() {
		@Override
		public void run() {
			try {
				WifiSetting_Info.mClient = Client.connect();
				new Thread(WifiSetting_Info.mClient).start();
				runOnUiThread(finishRunnable);
			} catch (IOException e) {
				e.printStackTrace();
				runOnUiThread(errorRunnable);
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
			Toast.makeText(TR_Main_Activity.this,R.string.T_wificonnfail,Toast.LENGTH_LONG).show();
		}
	};
	
	/***
	 * 连接成功
	 */
	private Runnable finishRunnable=new Runnable() {
		@Override
		public void run() {
			Toast.makeText(TR_Main_Activity.this,R.string.T_wificonnsuccess,Toast.LENGTH_SHORT).show();
		}
		
	};
	
	

	/**
	 * 
	 * @Title: init
	 * @Description: 初始化
	 * @return void
	 * @throws
	 */
	public void init() {

		System.out.println("TR_Main_Activity init");
		//切换语言
/*		btn_China = (Button) findViewById(R.id.language_China);
		if(btn_China==null){return;}
		btn_America = (Button) findViewById(R.id.language_America);
		if(btn_America==null){return;}
		btn_HK = (Button) findViewById(R.id.language_HongKong);
		if(btn_HK==null){return;}

		btn_China.setOnClickListener(languageClickListener);
		btn_America.setOnClickListener(languageClickListener);
		btn_HK.setOnClickListener(languageClickListener);

		//手动、原点、步进、自动、开始/停止
		btn_manual = (Button) findViewById(R.id.btn_mode_manual);
		if(btn_manual==null){return;}
		btn_manual.setOnClickListener(mode_manual_listener);

		btn_origin = (Button) findViewById(R.id.btn_mode_origin);
		if(btn_origin==null){return;}
		btn_origin.setOnClickListener(mode_origin_listener);

		btn_step = (Button) findViewById(R.id.btn_mode_step);
		if(btn_step==null){return;}
		btn_step.setOnClickListener(mode_step_listener);
		
		btn_automatic = (Button) findViewById(R.id.btn_mode_automatic);
		if(btn_automatic==null){return;}
		btn_automatic.setOnClickListener(mode_automatic_listener);
		
		btn_start_stop = (ToggleButton) findViewById(R.id.btn_mode_start_stop);
		if(btn_start_stop==null){return;}
		btn_start_stop.setOnCheckedChangeListener(mode_start_stop_listener);*/
		mSampleView = (VerticalScrollTextView) findViewById(R.id.sampleView1);
		if(mSampleView==null){
			return;
		}
		mjbhtext= (TextView) findViewById(R.id.mjbh);
		if(mjbhtext==null){return;}
		
//		mjszImage= (ImageView) findViewById(R.id.left111_btnzone);
//		if(mjszImage==null){return;}
		
		btn_stop = (ImageButton) findViewById(R.id.stop);
		if(btn_stop==null){return;}
		ButtonListener b = new ButtonListener();           
		btn_stop.setOnClickListener(b);  
		btn_stop.setOnTouchListener(b);  
		
		btn_f1 = (ImageButton) findViewById(R.id.f1);
		if(btn_f1==null){return;}
		btn_f1.setOnClickListener(f1_listener);
		
		btn_iojs = (ImageButton) findViewById(R.id.iojs);
		if(btn_iojs==null){return;}
		btn_iojs.setOnClickListener(iojs_listener);
		
		btn_wifilj = (ImageButton) findViewById(R.id.wifilj);
		if(btn_wifilj==null){return;}
		btn_wifilj.setOnClickListener(wifilj_listener);
		
		AlmCPFeedback = new ChatListener() {
			@Override
			public void onChat(byte[] message) {

				//返回的标志位STS1处的判断以及和校验
				formatReadAlm.backMessageCheck(message);
				if(!formatReadAlm.finishStatus()){
					runOnUiThread(sendDataAlmRunnable);			
				}else {
					//发送正确且完成
					//处理返回的数据	
					getData=new byte[formatReadAlm.getLength()];
					//获取返回的数据，从第八位开始拷贝数据
					System.arraycopy(message, 8, getData, 0, formatReadAlm.getLength());
					Almallnum= HexDecoding.Array4Toint(getData, 0);
					// 当前伺服参数编号
					AlmCP= HexDecoding.Array4Toint(getData, 4);
					}
				getAlmdata();
			}
		};
		btn_jbll = (ImageButton) findViewById(R.id.jbll);
		if(btn_jbll==null){return;}
		btn_jbll.setOnClickListener(jbll_listener);
		
		btn_mmsd = (ImageButton) findViewById(R.id.mmsd);
		if(btn_mmsd==null){return;}
		btn_mmsd.setOnClickListener(mmsd_listener);
		
		btn_fw = (ImageButton) findViewById(R.id.fw);
		if(btn_fw==null){return;}
		btn_fw.setOnClickListener(fw_listener);
		
		btn_bbxs = (ImageButton) findViewById(R.id.bbxs);
		if(btn_bbxs==null){return;}
		btn_bbxs.setOnClickListener(bbxs_listener);
		
		//dqjbxxtext=(TextView)findViewById(R.id.dqjbxxtext);
		//if(dqjbxxtext==null){return;}
		
		/*btn_yyqh=(ImageButton)findViewById(R.id.yyqh);
		if(btn_yyqh==null){return;}
		btn_yyqh.setOnClickListener(languageClickListener);*/
		
		btn_jxwz = (ImageButton) findViewById(R.id.jxwz);
		if(btn_jxwz==null){return;}
		btn_jxwz.setOnClickListener(jxwz_listener);
		
		btn_mjsjcz = (ImageButton) findViewById(R.id.mjsjcz);
		if(btn_mjsjcz==null){return;}
		btn_mjsjcz.setOnClickListener(mjsjcz_listener);
		
		btn_zycz = (ImageButton) findViewById(R.id.zycz);
		if(btn_zycz==null){return;}
		btn_zycz.setOnClickListener(zycz_listener);
		
		btn_timer = (ImageButton) findViewById(R.id.timer);
		if(btn_timer==null){return;}
		btn_timer.setOnClickListener(timer_listener);
		
		btn_counter = (ImageButton) findViewById(R.id.counter);
		if(btn_counter==null){return;}
		btn_counter.setOnClickListener(counter_listener);
		
		btn_ydcz= (ImageButton) findViewById(R.id.ydcz);
		if(btn_ydcz==null){return;}
		btn_ydcz.setOnClickListener(mode_origin_listener);

		btn_bjcz = (ImageButton) findViewById(R.id.bjcz);
		if(btn_bjcz==null){return;}
		btn_bjcz.setOnClickListener(mode_step_listener);
		
		btn_zdcz = (ImageButton) findViewById(R.id.zdcz);
		if(btn_zdcz==null){return;}
		btn_zdcz.setOnClickListener(mode_automatic_listener);
		
		alarm_led=(Button)findViewById(R.id.alarm_led);
		if(alarm_led==null){return;}
		
		moving_led=(Button)findViewById(R.id.moving_led);
		if(moving_led==null){return;}
		
		zd_led=(Button)findViewById(R.id.zd_led);
		if(zd_led==null){return;}
		
		wifi_led=(Button)findViewById(R.id.wifi_led);
		if(wifi_led==null){return;}
		
		if(clicked_btn_automatic==true){
			//zd_led.setVisibility(View.VISIBLE);
		}
		
		btn_xjcz = (ImageButton) findViewById(R.id.xjcz);
		if(btn_xjcz==null){return;}
		btn_xjcz.setOnClickListener(xjcz_listener);
		
		btn_czsc = (ImageButton) findViewById(R.id.czsc);
		if(btn_czsc==null){return;}
		btn_czsc.setOnClickListener(czsc_listener);
		//档位
		seekbar = (VerticalSeekBar) findViewById(R.id.seekbar);
		if(seekbar==null){return;}
		seekbar.setMax(500);// 最大档位
		seekbar.initProgress();// 默认档位
		seekbar.setOnSeekBarChangeListener(new VerticalSeekBarListener(TR_Main_Activity.this));
		
		StartWatchBtn = (Button) findViewById(R.id.watchStart);
		StopWatchBtn = (Button) findViewById(R.id.watchStop);
		WatchListView = (ListView) findViewById(R.id.watchList);
		StartWatchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				
				try{
			    	alength = NCTranslate.beginTranslate(
			    			TR_Main_Activity.this, getResources()
									.getStringArray(R.array.IF2));
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getApplicationContext(), "NC程序存在错误",
								Toast.LENGTH_SHORT).show();
						return;
					}
					 if(NCTranslate.dname!=null)
		 		      {
					   if(alength[0]==-1){
					   new AlertDialog.Builder(TR_Main_Activity.this).setTitle("提示")
					   .setMessage(NCTranslate.ncindex+"的"+NCTranslate.dname+"设备未选择设定")
					   .setPositiveButton("知道了", null).show();
					   NCTranslate.dname=null;
						 }else if(alength[0]==1){
						   new AlertDialog.Builder(TR_Main_Activity.this).setTitle("提示")
						   .setMessage("没有找到"+NCTranslate.ncindex+"的"+NCTranslate.dname+"设备")
						   .setPositiveButton("知道了", null).show();
						   NCTranslate.dname=null;
						}else if(alength[0]==-2){
							   new AlertDialog.Builder(TR_Main_Activity.this).setTitle("提示")
							   .setMessage(NCTranslate.ncindex+"的标号"+NCTranslate.dname+"存在错误")
							   .setPositiveButton("知道了", null).show();
							   NCTranslate.dname=null;
						}else if(alength[0]==-3){
								   new AlertDialog.Builder(TR_Main_Activity.this).setTitle("提示")
								   .setMessage(NCTranslate.ncindex+"的命令"+NCTranslate.dname+"存在错误")
								   .setPositiveButton("知道了", null).show();
								   NCTranslate.dname=null;
						}

			            return;
		 		     }
					watchRunnable = new WatchRunnable(TR_Main_Activity.this,
							alength,true);
//					Device_ListFragment.firstOpen=true;
//				//	watchflag=true;
					Thread a1 = new Thread(watchRunnable);
					a1.start();
				StartWatchBtn.setEnabled(false);
				StopWatchBtn.setEnabled(true);
			}
		});
		StopWatchBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				watchRunnable.destroy();
				StartWatchBtn.setEnabled(true);
				StopWatchBtn.setEnabled(false);
			}
		});
	
	
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("mpeng","onDestroy main activity");
		super.onDestroy();
	
	}
	/**
	 * 语言切换监听器
	 */
	OnClickListener languageClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(sharedPreference_language.getString("region","CN").equals("HK")) {
				langString = sharedPreference_language.getString("language","zh");
				regionString = sharedPreference_language.getString("region","CN");
				if (langString.equals("zh") && regionString.equals("CN")) {
					Toast.makeText(TR_Main_Activity.this, R.string.T_languageCN,
							Toast.LENGTH_SHORT).show();
				} else {
					sharedPreference_language.edit().putString("language", "zh").commit();
					sharedPreference_language.edit().putString("region", "CN").commit();
					setLanguage("zh", "CN");// 一定对应zh-rCN
					Toast.makeText(TR_Main_Activity.this, R.string.T_changeCN,Toast.LENGTH_SHORT).show();
				}
			}else if(sharedPreference_language.getString("region","CN").equals("CN")) {
				langString = sharedPreference_language.getString("language","zh");
				regionString = sharedPreference_language.getString("region","CN");
				if (langString.equals("en") && regionString.equals("US")) {
					Toast.makeText(TR_Main_Activity.this, R.string.T_languageUS,
							Toast.LENGTH_SHORT).show();
				} else {
					sharedPreference_language.edit().putString("language", "en").commit();
					sharedPreference_language.edit().putString("region", "US").commit();
					setLanguage("en", "US");
					Toast.makeText(TR_Main_Activity.this,R.string.T_changeUS,Toast.LENGTH_SHORT).show();
				}
			}else if(sharedPreference_language.getString("region","CN").equals("US")) {
				langString = sharedPreference_language.getString("language","zh");
				regionString = sharedPreference_language.getString("region","CN");
				if (langString.equals("zh") && regionString.equals("HK")) {
					Toast.makeText(TR_Main_Activity.this, R.string.T_languageHK,Toast.LENGTH_SHORT).show();
				} else {
					sharedPreference_language.edit().putString("language", "zh").commit();
					sharedPreference_language.edit().putString("region", "HK").commit();
					setLanguage("zh", "HK");
					Toast.makeText(TR_Main_Activity.this,R.string.T_changeHK,Toast.LENGTH_SHORT).show();
				}
			}else{
				sharedPreference_language.edit().putString("language", "zh").commit();
				sharedPreference_language.edit().putString("language", "CN").commit();
				Configuration config = getResources().getConfiguration();// 获得设置对象
				config.locale = Locale.getDefault();
				getResources().updateConfiguration(config, null);
				TR_Main_Activity.this.finish();
				TR_Main_Activity.this.startActivity(TR_Main_Activity.this.getIntent());
			}
			
		}
	};

	/**
	 * 语言本地切换，支持中文简体，繁体，英语、日语、韩语
	 * 
	 * @Title: setLanguage
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param langString
	 * @param country
	 * @return void
	 * @throws
	 */
	private void setLanguage(String langString, String regionString) {

		Locale locale = new Locale(langString, regionString);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getResources().updateConfiguration(config, null);

		TR_Main_Activity.this.finish();
		TR_Main_Activity.this.startActivity(TR_Main_Activity.this.getIntent());

		// Intent intent = new Intent();
		// intent.setClass(TR_Main_Activity.this,TR_Main_Activity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// TR_Main_Activity.this.startActivity(intent);

		String locale11 = getResources().getConfiguration().locale
				.getDisplayName();
		System.out.println(locale11);

	}

	/**
	 * 使能Listener
	 */
	public OnClickListener enable_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {

		}
	};

	/**
	 * 停止Listener
	 */
	class ButtonListener implements OnClickListener, OnTouchListener{  
		   public boolean onTouch(View v, MotionEvent event) {  
		            if(v.getId() == R.id.stop){    
		                if(event.getAction() == MotionEvent.ACTION_DOWN){ 
		                	KeyCodeSend.send(999, TR_Main_Activity.this);
		                }  
		            }  
		            return false;  
		        }

		@Override
		public void onClick(View v) {
		}  
		          
	}

	/**
	 * F1响应函数
	 */
	public OnClickListener f1_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			openThisFileFromSD("mainactivity.pdf");
			KeyCodeSend.send(6, TR_Main_Activity.this);
		}
	};

	/**
	 * 服务响应函数
	 */
	public OnClickListener fw_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			openThisFileFromSD("fw_png.png");
		}
	};
	
	/**
	 * 警报履历响应函数
	 */
	
	
	public OnClickListener jbll_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (WifiSetting_Info.mClient == null) {
				Toast.makeText(TR_Main_Activity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
				return;
			}
			list_alarm.clear();
			View view_alarm=View.inflate(TR_Main_Activity.this, R.layout.maintain_guide_alarm, null);
			AlertDialog dialog_alarm=new AlertDialog.Builder(TR_Main_Activity.this).setTitle(R.string.T_alarmresume).setView(view_alarm).setNegativeButton("取消",null).show();
			 listView_alarm=(ListView) dialog_alarm.findViewById(R.id.listView_alarm);
				if(listView_alarm==null){
					return;
				}
				if (WifiSetting_Info.mClient != null) {
				formatReadAlm = new WifiReadDataFormat(0x400242FC,8);
				try {

					sendDataAlmRunnable = new SendDataRunnable(AlmCPFeedback, formatReadAlm,TR_Main_Activity.this);
					runOnUiThread(sendDataAlmRunnable);
				
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(TR_Main_Activity.this, e.toString(), Toast.LENGTH_LONG);
				}} else {
					Toast.makeText(TR_Main_Activity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
				}
		}
	};
	/**
	 * 密码设定响应函数
	 */
	public OnClickListener mmsd_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			View view_password=View.inflate(TR_Main_Activity.this, R.layout.maintain_guide_password, null);
			AlertDialog dialog_password = new AlertDialog.Builder(TR_Main_Activity.this)
					.setTitle(R.string.T_passwordset)
					.setView(view_password)
					.setCancelable(false)
					.setNegativeButton(R.string.T_titlecancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									dialog.dismiss();
								}
							}).show();
			
			radioGroup_password=(RadioGroup) dialog_password.findViewById(R.id.radioGroup_password);
			if(radioGroup_password==null){
				return;
			}
			btn_create_password=(Button) dialog_password.findViewById(R.id.btn_create_password);
			if(btn_create_password==null){
				return;
			}
			btn_change_password=(Button) dialog_password.findViewById(R.id.btn_change_password);
			if(btn_change_password==null){
				return;
			}
			btn_delete_password=(Button) dialog_password.findViewById(R.id.btn_delete_password);
			if(btn_delete_password==null){
				return;
			}
			//进入窗口就更新得到密码
			password_operate = sharedPreference_password.getString("password_operate", "");
			password_setting = sharedPreference_password.getString("password_setting", "");
			password_programming = sharedPreference_password.getString("password_programming", "");
			
			//初次进入，得到已设置的密码，判断是否存在
			if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_operate_password) {
				password = password_operate;
			}else if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_setting_password) {
				password = password_setting;
			}else if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_programming_password) {
				password = password_programming;
			}
				
			if (password.equals("")) {//说明还没有创建密码，可以进入创建密码界面
				btn_create_password.setEnabled(true);
				btn_change_password.setEnabled(false);
				btn_delete_password.setEnabled(false);
			}else {//已经创建过密码，灰化创建密码按钮
				btn_create_password.setEnabled(false);
				btn_change_password.setEnabled(true);
				btn_delete_password.setEnabled(true);
			}
			
			radioGroup_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					if (checkedId==R.id.radio_operate_password) {
						if (password_operate.equals("")) {
							btn_create_password.setEnabled(true);
							btn_change_password.setEnabled(false);
							btn_delete_password.setEnabled(false);
						}else {
							btn_create_password.setEnabled(false);
							btn_change_password.setEnabled(true);
							btn_delete_password.setEnabled(true);
						}
					}else if (checkedId==R.id.radio_setting_password) {
						if (password_setting.equals("")) {
							btn_create_password.setEnabled(true);
							btn_change_password.setEnabled(false);
							btn_delete_password.setEnabled(false);
						}else {
							btn_create_password.setEnabled(false);
							btn_change_password.setEnabled(true);
							btn_delete_password.setEnabled(true);
						}
					}else if (checkedId==R.id.radio_programming_password) {
						if (password_programming.equals("")) {
							btn_create_password.setEnabled(true);
							btn_change_password.setEnabled(false);
							btn_delete_password.setEnabled(false);
						}else {
							btn_create_password.setEnabled(false);
							btn_change_password.setEnabled(true);
							btn_delete_password.setEnabled(true);
						}
					}
					
					
				}
			});
															
			/**
			 * ******************************
			 *  1 .密码创建
			 * ******************************
			 */
			btn_create_password.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					password_create="";
					//判断标题
					title_create="";
					if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_operate_password) {
						title_create="操作密码创建";
					}else if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_setting_password) {
						title_create="设定密码创建";
					}else if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_programming_password) {
						title_create="编程密码创建";
					}
					

					//第一次输密码的输入控制
					final EditText editText =new EditText(TR_Main_Activity.this);
					editText.setHint(R.string.T_passwordputin);
					editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
					editText.setKeyListener(new NumberKeyListener() {
	 				    @Override
	 				    protected char[] getAcceptedChars() {
	 				        return new char[] { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			        					'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			        					'1', '2', '3', '4', '5', '6', '7', '8','9', '0'};
	 				    }
	 				    @Override
	 				    public int getInputType() {
	 				        return android.text.InputType.TYPE_CLASS_TEXT;
	 				    }
	 				});	
				
					//第一次输密码
					AlertDialog dialog_create = new AlertDialog.Builder(TR_Main_Activity.this)
							.setTitle(title_create)
							.setView(editText)
							.setCancelable(false)
							.setPositiveButton(R.string.T_titlesure,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {
											final String password_create=editText.getText().toString().trim();
											
											if (password_create.equals("")) {//密码为空，不符合要求，要重新输入
												try {//不消失
													Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
													field.setAccessible(true);
													field.set(dialog,false);//将mShowing变量设为false，表示对话框已关闭，欺骗android系统
												} catch (Exception e) {
													e.printStackTrace();
												}
												
												editText.setText("");
												editText.setHint(R.string.T_passwordputinerror);
												
											}else {//密码不为空，继续第二次输入,且让第一次的dialog消失
												
												//第二次输密码的输入控制
												final EditText editText2 =new EditText(TR_Main_Activity.this);
												editText2.setHint(R.string.T_passwordputin);
												editText2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
												editText2.setKeyListener(new NumberKeyListener() {
													    @Override
													    protected char[] getAcceptedChars() {
													        return new char[] { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
										        					'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
										        					'1', '2', '3', '4', '5', '6', '7', '8','9', '0'};
													    }
													    @Override
													    public int getInputType() {
													        return android.text.InputType.TYPE_CLASS_TEXT;
													    }
													});	
												
												//第二次输密码
												AlertDialog dialog_create_again = new AlertDialog.Builder(TR_Main_Activity.this)
												 		.setTitle("请再次输入"+title_create.substring(0, 4))
														.setView(editText2)
														.setCancelable(false)
												 		.setPositiveButton(R.string.T_titlesure,
																new DialogInterface.OnClickListener() {
																	@Override
																	public void onClick(DialogInterface dialog,int which) {
																		if (editText2.getText().toString().trim().equals(password_create)) {
																			
																			//两次密码相同，放到相应的sharedPreference保存
																			if (title_create.contains("操作")) {
																				sharedPreference_password.edit().putString("password_operate", password_create).commit();
																				password_operate=password_create;//更新密码，方便前面radiogroup的改变判断
																			}else if (title_create.contains("设定")) {
																				sharedPreference_password.edit().putString("password_setting", password_create).commit();
																				password_setting=password_create;
																			}else if (title_create.contains("编程")) {
																				sharedPreference_password.edit().putString("password_programming", password_create).commit();
																				password_programming=password_create;
																			}
																			
																			Toast.makeText(TR_Main_Activity.this, title_create.subSequence(0, 2).toString()+R.string.T_passwordcreate, Toast.LENGTH_SHORT).show();
																			try {//消失
															                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing" );  
															                    field.setAccessible( true );  
															                    field.set(dialog, true );//将mShowing变量设为true，表示对话框未关闭     
															                }catch (Exception e) {
															                	e.printStackTrace(); 
																			}
															                dialog.dismiss();
															                //由于密码创建成功，使修改和删除使能，使创建灰化
															                btn_create_password.setEnabled(false);
																			btn_change_password.setEnabled(true);
																			btn_delete_password.setEnabled(true);
																			
																			
																		} else {//两次密码不同，点击确定不消失。提示重新输入
																			
																			try {//不消失
																				Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
																				field.setAccessible(true);
																				field.set(dialog,false);//将mShowing变量设为false，表示对话框已关闭，欺骗android系统
																			} catch (Exception e) {
																				e.printStackTrace();
																			}
																			
																			editText2.setText("");
																			editText2.setHint(R.string.T_passwordtwoputinnosame);
																		}
																	}
																})
														.setNegativeButton(R.string.T_titlecancel,
																new DialogInterface.OnClickListener() {
																	@Override
																	public void onClick(DialogInterface dialog,int which) {

																		try { // 点击取消 则消失
																			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
																			field.setAccessible(true);
																			field.set(dialog,true);// 将mShowing变量设为true，表示对话框未关闭
																		} catch (Exception e) {
																			e.printStackTrace();
																		}

																		dialog.dismiss();
																	}
																}).show();
												
												try { //进入第二个对话框时让第一个消失
													Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
													field.setAccessible(true);
													field.set(dialog,true);// 将mShowing变量设为true，表示对话框未关闭
												} catch (Exception e) {
													e.printStackTrace();
												}

												dialog.dismiss();
												
												
											}
											
										}
									})
							.setNegativeButton(R.string.T_titlecancel,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {
											try { //点击取消 则消失
							                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing" );  
							                    field.setAccessible( true );  
							                    field.set(dialog, true );//将mShowing变量设为true，表示对话框未关闭    
							                }catch (Exception e) {
							                	e.printStackTrace(); 
											}
											dialog.dismiss();
										}
									}).show();
					
					
					
				}
			});//btn_create_password
			/**
			 * ******************************
			 *  2 .密码删除  
			 *  设定了一个万能密码tr2013，不记得密码时可以使用
			 * ******************************
			 */
			btn_delete_password.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					//判断标题
					title_delete="";
					passwordToDelete="";
					if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_operate_password) {
						title_delete="操作密码删除";
						passwordToDelete=password_operate;
					}else if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_setting_password) {
						title_delete="设定密码删除";
						passwordToDelete=password_setting;
					}else if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_programming_password) {
						title_delete="编程密码删除";
						passwordToDelete=password_programming;
					}
					
					//删除密码的输入控制
					final EditText editText_delete =new EditText(TR_Main_Activity.this);
					editText_delete.setHint(R.string.T_passwordputinold);
					editText_delete.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
					editText_delete.setKeyListener(new NumberKeyListener() {
	 				    @Override
	 				    protected char[] getAcceptedChars() {
	 				        return new char[] { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			        					'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			        					'1', '2', '3', '4', '5', '6', '7', '8','9', '0'};
	 				    }
	 				    @Override
	 				    public int getInputType() {
	 				        return android.text.InputType.TYPE_CLASS_TEXT;
	 				    }
	 				});	
					
					//输入原密码对话框
					AlertDialog dialog_delete = new AlertDialog.Builder(TR_Main_Activity.this)
							.setTitle(title_delete)
							.setView(editText_delete)
							.setCancelable(false)
							.setPositiveButton(R.string.T_titlesure,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {
											String passwordHere = editText_delete.getText().toString().trim();
											
											if (passwordHere.equals(passwordToDelete)||passwordHere.equals("tr2013")) {//如果原密码输入正确或者输入了万能密码，则弹出删除提示
												//弹出确定删除对话框
												new AlertDialog.Builder(TR_Main_Activity.this)
														.setTitle(R.string.T_titlewarn)
														.setMessage(R.string.T_passworddeleteYN)
														.setPositiveButton(R.string.T_titlesure,
																new DialogInterface.OnClickListener() {
																	@Override
																	public void onClick(DialogInterface dialog,int which) {
																		
																		if (title_delete.contains("操作")) {
																			sharedPreference_password.edit().putString("password_operate", "").commit();
																			password_operate="";//更新密码
																		}else if (title_delete.contains("设定")) {
																			sharedPreference_password.edit().putString("password_setting", "").commit();
																			password_setting="";
																		}else if (title_delete.contains("编程")) {
																			sharedPreference_password.edit().putString("password_programming", "").commit();
																			password_programming="";
																		}
																		
														                //由于密码删除成功，使修改和删除灰化，使创建使能
														                btn_create_password.setEnabled(true);
																		btn_change_password.setEnabled(false);
																		btn_delete_password.setEnabled(false);
																		
																		Toast.makeText(TR_Main_Activity.this, title_delete.subSequence(0, 2)+""+R.string.T_passworddelete, Toast.LENGTH_SHORT).show();
																	}
																})
														.setNegativeButton(R.string.T_titlecancel, null)
														.show();
												
												try {//消失
								                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing" );  
								                    field.setAccessible( true );  
								                    field.set(dialog, true );//将mShowing变量设为true，表示对话框未关闭     
								                }catch (Exception e) {
								                	e.printStackTrace(); 
												}
								                dialog.dismiss();
												
											}else {//如果原密码输入不正确，则重新输入
												
												try {//不消失
													Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
													field.setAccessible(true);
													field.set(dialog,false);//将mShowing变量设为false，表示对话框已关闭，欺骗android系统
												} catch (Exception e) {
													e.printStackTrace();
												}
												editText_delete.setText("");
												editText_delete.setHint(R.string.T_passwordputinolderror);
												
											}
											
										}
									})
							.setNegativeButton(R.string.T_titlecancel,new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {
											try {//消失
							                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing" );  
							                    field.setAccessible( true );  
							                    field.set(dialog, true );//将mShowing变量设为true，表示对话框未关闭     
							                }catch (Exception e) {
							                	e.printStackTrace(); 
											}
							                dialog.dismiss();
										}
									}).show();
					
					
					
					
					
					
				}
			});
			/**
			 * ******************************
			 *  3 .密码修改
			 * ******************************
			 */
			btn_change_password.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					//判断标题
					title_change="";
					password_old="";
					if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_operate_password) {
						title_change="操作密码修改";
						password_old=password_operate;
					}else if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_setting_password) {
						title_change="设定密码修改";
						password_old=password_setting;
					}else if (radioGroup_password.getCheckedRadioButtonId()==R.id.radio_programming_password) {
						title_change="编程密码修改";
						password_old=password_programming;
					}
					
					//修改密码的输入原密码输入控制
					final EditText editText_change =new EditText(TR_Main_Activity.this);
					editText_change.setHint(R.string.T_passwordputinold);
					editText_change.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
					editText_change.setKeyListener(new NumberKeyListener() {
	 				    @Override
	 				    protected char[] getAcceptedChars() {
	 				        return new char[] { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			        					'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			        					'1', '2', '3', '4', '5', '6', '7', '8','9', '0'};
	 				    }
	 				    @Override
	 				    public int getInputType() {
	 				        return android.text.InputType.TYPE_CLASS_TEXT;
	 				    }
	 				});	
					//密码修改，先输入原密码
					AlertDialog dialog_change = new AlertDialog.Builder(TR_Main_Activity.this)
							.setTitle(title_change)
							.setView(editText_change)
							.setCancelable(false)
							.setPositiveButton(R.string.T_titlesure,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {
											if (editText_change.getText().toString().trim().equals(password_old)) {//如果原密码输入正确，则弹出修改对话框，退出原密码对话框
												
												//修改密码的输入新密码输入控制
												final EditText editText_change_again =new EditText(TR_Main_Activity.this);
												editText_change_again.setHint(R.string.T_passwordputinnew);
												editText_change_again.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
												editText_change_again.setKeyListener(new NumberKeyListener() {
								 				    @Override
								 				    protected char[] getAcceptedChars() {
								 				        return new char[] { 'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
										        					'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
										        					'1', '2', '3', '4', '5', '6', '7', '8','9', '0'};
								 				    }
								 				    @Override
								 				    public int getInputType() {
								 				        return android.text.InputType.TYPE_CLASS_TEXT;
								 				    }
								 				});	
												
												//密码修改，输入新密码
												AlertDialog dialog_change_again = new AlertDialog.Builder(TR_Main_Activity.this)
														.setTitle(title_change)
														.setView(editText_change_again)
														.setCancelable(false)
														.setPositiveButton(R.string.T_titlesure,
																new DialogInterface.OnClickListener() {
																	@Override
																	public void onClick(DialogInterface dialog,int which) {
																		//得到新密码
																		passwordToChange=editText_change_again.getText().toString().trim();
																		//判断新密码是否为空
																		if (passwordToChange.equals("")) {//为空不符合，对话框不消失，给出提示
																			
																			try {//不消失
																				Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
																				field.setAccessible(true);
																				field.set(dialog,false);//将mShowing变量设为false，表示对话框已关闭，欺骗android系统
																			} catch (Exception e) {
																				e.printStackTrace();
																			}
																			
																			editText_change_again.setText("");
																			editText_change_again.setHint(R.string.T_passwordputinnewerror);
																			
																		}else {
																			//保存新密码
																			if (title_change.contains("操作")) {
																				sharedPreference_password.edit().putString("password_operate", passwordToChange).commit();
																				password_operate=passwordToChange;//更新密码，方便前面radiogroup的改变判断
																			}else if (title_change.contains("设定")) {
																				sharedPreference_password.edit().putString("password_setting", passwordToChange).commit();
																				password_setting=passwordToChange;
																			}else if (title_change.contains("编程")) {
																				sharedPreference_password.edit().putString("password_programming", passwordToChange).commit();
																				password_programming=passwordToChange;
																			}
																			
																			Toast.makeText(TR_Main_Activity.this, title_change.subSequence(0, 2)+"密码修改成功", Toast.LENGTH_SHORT).show();
																			try {//消失
															                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing" );  
															                    field.setAccessible( true );  
															                    field.set(dialog, true );//将mShowing变量设为true，表示对话框未关闭     
															                }catch (Exception e) {
															                	e.printStackTrace(); 
																			}
															                dialog.dismiss();
															                //由于密码修改成功，使修改和删除使能，使创建灰化
															                btn_create_password.setEnabled(false);
																			btn_change_password.setEnabled(true);
																			btn_delete_password.setEnabled(true);
																		}

																	}
																})
														.setNegativeButton(R.string.T_titlecancel,
																new DialogInterface.OnClickListener() {
																	@Override
																	public void onClick(DialogInterface dialog,int which) {
																		
																		try {//消失
														                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing" );  
														                    field.setAccessible( true );  
														                    field.set(dialog, true );//将mShowing变量设为true，表示对话框未关闭     
														                }catch (Exception e) {
														                	e.printStackTrace(); 
																		}
														                dialog.dismiss();
																	}
																}).show();
												
												//点击确定且原密码正确后，输入原密码界面消失
												try {//消失
								                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing" );  
								                    field.setAccessible( true );  
								                    field.set(dialog, true );//将mShowing变量设为true，表示对话框未关闭     
								                }catch (Exception e) {
								                	e.printStackTrace(); 
												}
								                dialog.dismiss();
											
											
											}else {//原密码输入不正确，不退出对话框，重新输入
												
												try {//不消失
													Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
													field.setAccessible(true);
													field.set(dialog,false);//将mShowing变量设为false，表示对话框已关闭，欺骗android系统
												} catch (Exception e) {
													e.printStackTrace();
												}
												
												editText_change.setText("");
												editText_change.setHint(R.string.T_passwordputinolderror);
											}

										}
									})
							.setNegativeButton(R.string.T_titlecancel,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,int which) {

											dialog.dismiss();
											
										}
									}).show();
				}
			});
		}
	};
	/**
	 * 版本显示响应函数
	 */
	public OnClickListener bbxs_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			View view_version = View.inflate(TR_Main_Activity.this,R.layout.maintain_guide_version_dispaly, null);
			
			// 先发送数据，然后等待20ms，等版本数据都到了，再导入到显示的对话框。
			//========================	

				//int readAddr = 0x1000B180;  //!!!!!!注意该地址的变化
				
				//================
					AlertDialog dialog_version = new AlertDialog.Builder(TR_Main_Activity.this).setTitle(R.string.T_versionshow).setView(view_version)
							.setNegativeButton("取消",null).show();
			listView_version = (ListView) dialog_version
					.findViewById(R.id.listView_version);
			if(listView_version==null){
				return;
			}
			
			adapter_version = new MyAdapter_Version(TR_Main_Activity.this, list_version,
					R.layout.maintain_guide_version_item,
					new String[] { "boot", "boot_version", "data","data_version" },
					new int[] { R.id.boot, R.id.boot_version, R.id.data,R.id.data_version });
			listView_version.setAdapter(adapter_version);
			if (WifiSetting_Info.mClient == null) {
				Toast.makeText(TR_Main_Activity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
				return;
			}
			formatReadMessage = new WifiReadDataFormat(0x1000B110, 30*4);
			try {
				sendDataRunnable=new SendDataRunnable(formatReadMessage,TR_Main_Activity.this);

				sendDataFinishRunnable=new FinishRunnable(TR_Main_Activity.this, "数据读取完毕",readversionDataFinishTodo);

				sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

				runOnUiThread(sendDataRunnable);

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(TR_Main_Activity.this, e.toString(), Toast.LENGTH_LONG);
			}
		}
	};
	/**
	 * io监视响应函数
	 */
	public OnClickListener iojs_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (WifiSetting_Info.mClient == null) {
				Toast.makeText(TR_Main_Activity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
				return;
			}
			list_io.clear();
			View view_io=View.inflate(TR_Main_Activity.this, R.layout.maintainguide_maintain_io, null);
			dialog_io=new AlertDialog.Builder(TR_Main_Activity.this).setTitle(R.string.T_IOwatch).setView(view_io).setNegativeButton("取消",new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int which) {
					 DelayTimerQueryRunnble.destroy();
				}
			}).show();
			//监听菜单的关闭事件  
			dialog_io.setOnDismissListener(new OnDismissListener() {              
	            
				@Override
				public void onDismiss(DialogInterface arg0) {
					// TODO Auto-generated method stub
					DelayTimerQueryRunnble.destroy();
				}  
	        });  

			 listView_io=(ListView) dialog_io.findViewById(R.id.listView_io);
				if(listView_io==null){
					return;
				}
				if (WifiSetting_Info.mClient != null) {
				formatReadio= new WifiReadDataFormat(0x10007000,32);

				try {
					sendDataioRunnable=new SendDataRunnable(formatReadio, TR_Main_Activity.this);

					sendDataFinishRunnable=new FinishRunnable(TR_Main_Activity.this, "数据读取完毕",readioDataFinishTodo);

					sendDataioRunnable.setOnlistener(new NormalChatListenner(sendDataioRunnable, sendDataFinishRunnable));

					runOnUiThread(sendDataioRunnable);
			
					} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(TR_Main_Activity.this, e.toString(), Toast.LENGTH_LONG);
				}}
		}
	};
	/**
	 * wifi conn
	 */
	public OnClickListener wifilj_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (WifiSetting_Info.mClient != null) {		
				Toast.makeText(getApplicationContext(), R.string.T_wificonnOK,
						Toast.LENGTH_SHORT).show();
				wifi_led.setVisibility(View.VISIBLE);
			} else {				
				new Thread(tempRunnable).start();
			}
				new AlertDialog.Builder(TR_Main_Activity.this)
				.setTitle(R.string.T_titlenotice).setMessage(R.string.T_wificonnmain)
				.setPositiveButton(R.string.T_titlesure,
	            new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) {						
						formatReadMessage=new WifiReadDataFormat(0x1000B234,4);
						try {
							sendDataRunnable = new SendDataRunnable(mainDataFeedback,formatReadMessage,
									TR_Main_Activity.this);
							runOnUiThread(sendDataRunnable);
						} catch (Exception e) {
							Toast.makeText(TR_Main_Activity.this, e.toString(), Toast.LENGTH_LONG);
						}
					}
				}).setNegativeButton(R.string.T_titlecancel,new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) {
						
						try {
							Toast.makeText(TR_Main_Activity.this, R.string.T_wificonnNO, Toast.LENGTH_SHORT).show();
							//posccalmqueryrunnable.destroy();
							//positionQueryRunnable.destroy();
							//cntCycQueryRunnable.destroy();
							//alarmQueryRunnable.destroy();
							WifiSetting_Info.mClient.disconnect();
						} catch (Exception e) {
							Toast.makeText(TR_Main_Activity.this, e.toString(), Toast.LENGTH_LONG);
						}
					}
				}).show();
		}
	};
	
	/**
	 * 选件操作
	 */
	public OnClickListener xjcz_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent_toMould=new Intent(TR_Main_Activity.this, NewPragramActivity.class);
			intent_toMould.putExtra("xjczsetting", true);
			startActivity(intent_toMould);
		}
	};
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
				if(checkEndsWithInStringArray(fileName, getResources()
						.getStringArray(R.array.fileEndingImage))) {
					intent = OpenFiles.getImageFileIntent(currentPath);
					startActivity(intent);
				}else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingPdf))) {
				intent = OpenFiles.getPdfFileIntent(currentPath);
				startActivity(intent);
			    } else {
				Toast.makeText(TR_Main_Activity.this, "无法打开该文件，请安装相应的软件！",
						Toast.LENGTH_SHORT).show();
			    }
		} catch (Exception e) {
				// TODO: handle exception
			Toast.makeText(TR_Main_Activity.this, "无法打开该文件，请安装相应的软件！",
					Toast.LENGTH_SHORT).show();
			}
		} else {// 不存在该文件
			Toast.makeText(TR_Main_Activity.this, "对不起，不存在该文件！", Toast.LENGTH_SHORT)
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
	public OnClickListener czsc_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			openThisFileFromSD("traappactivity.pdf");
		}
	};
	/**
	 * 跳转到设定类界面
	 */
	public OnClickListener mjsjcz_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent_toMould=new Intent(TR_Main_Activity.this, NewPragramActivity.class);
			KeyCodeSend.send(21, TR_Main_Activity.this);
			startActivity(intent_toMould);
		}
	};
	/**
	 * 跳转到设定类界面中的位置与速度
	 */
	public OnClickListener jxwz_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent_toMould=new Intent(TR_Main_Activity.this, NewPragramActivity.class);
			intent_toMould.putExtra("jxwzsetting", true);
			startActivity(intent_toMould);
		}
	};
	
	/**
	 * 跳转到设定类界面中的自由操作
	 */
	public OnClickListener zycz_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent_toMould=new Intent(TR_Main_Activity.this, NewPragramActivity.class);
			intent_toMould.putExtra("zyczsetting", true);
			startActivity(intent_toMould);
		}
	};
	
	/**
	 * 跳转到设定类界面中的定时器
	 */
	public OnClickListener timer_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent_toMould=new Intent(TR_Main_Activity.this, NewPragramActivity.class);
			intent_toMould.putExtra("timersetting", true);
			startActivity(intent_toMould);
		}
	};
	
	/**
	 * 跳转到设定类界面中的计数器
	 */
	public OnClickListener counter_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent_toMould=new Intent(TR_Main_Activity.this, NewPragramActivity.class);
			intent_toMould.putExtra("countersetting", true);
			startActivity(intent_toMould);
		}
	};
	/**
	 * 手动模式Listener
	 */
	/*public OnClickListener mode_manual_listener = new OnClickListener() {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			if (!clicked_btn_manual) {

				// 创建一个NotificationManager的引用
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.manual,
						"a new manual notification!",
						System.currentTimeMillis());
				// 定义Notification的各种属性
				notification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
				notification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
				 //设置notification发生时同时发出默认声音 
				notification.defaults = Notification.DEFAULT_SOUND;
				// 点亮屏幕
				notification.flags |= Notification.FLAG_SHOW_LIGHTS;
				notification.defaults = Notification.DEFAULT_LIGHTS;
				notification.ledARGB = Color.BLUE;
				notification.ledOnMS = 5000;

				CharSequence contentTitle = "manual mode（手动模式）";// 通知栏标题
				CharSequence contentText = "手动模式已开启";// 通知栏内容
				// 创建新的Intent，作为单击Notification留言条时，会运行的Activity 
				Intent notificationIntent = new Intent(TR_Main_Activity.this,
						TR_Programming_Activity.class); // 点击该通知后要跳转的Activity，试验用

				// notificationIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);

				 //创建PendingIntent作为设置递延运行的Activity 
				PendingIntent contentItent = PendingIntent.getActivity(
						TR_Main_Activity.this, 0, notificationIntent, 0);

				notification.setLatestEventInfo(TR_Main_Activity.this,
						contentTitle, contentText, contentItent);

				
				// * 把Notification传递给NotificationManager，
				// * 由于每次更改在线状态时，发出的Notification的id皆为0，
				 //* 因此当新的Notification被发出时，会替换掉旧的Notification，造成不断变换状态栏上的ICON的效果，
				// * 若发出的Notification的id不同，状态栏上就会显示不同的ICON
				 
				mNotificationManager.notify(manual_ID, notification);

				v.setBackgroundColor(Color.GREEN);
				btn_origin.setBackgroundColor(R.color.lightgray);
				btn_step.setBackgroundColor(R.color.lightgray);
				btn_automatic.setBackgroundColor(R.color.lightgray);
				clicked_btn_manual = true;
				clicked_btn_origin = false;
				clicked_btn_step = false;
				clicked_btn_automatic = false;
				image_autoStart_main.setBackgroundColor(Color.WHITE);
				image_autoWait_main.setBackgroundColor(Color.RED);

				image_autoStart_main.setEnabled(false);
				image_autoWait_main.setEnabled(true);
				// 发送keycode=1
				KeyCodeSend.send(1, TR_Main_Activity.this);
			}
			else
			{
				KeyCodeSend.send(1, TR_Main_Activity.this);
			}
		}

	};*/

	//获取返回的数据后进行的UI操作
			private final Runnable originDataFinishTodo = new Runnable(){
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
					   getData=HexDecoding.int2byte((int)(zjz|0x40));
					   try{
					    formatSendMessage=new WifiSendDataFormat(getData, 0x200000AF);
	                    sendDataRunnable=new SendDataRunnable(formatSendMessage, TR_Main_Activity.this);
						sendDataFinishRunnable=new FinishRunnable(TR_Main_Activity.this);
						sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));
						runOnUiThread(sendDataRunnable);
					   } catch (Exception e) {
						   e.printStackTrace();
					   }
				    }
				}
			};
			
			private final Runnable stepDataFinishTodo = new Runnable(){
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
					   getData=HexDecoding.int2byte((int)(zjz|0x80));
					   try{
					    formatSendMessage=new WifiSendDataFormat(getData, 0x200000AF);
	                    sendDataRunnable=new SendDataRunnable(formatSendMessage, TR_Main_Activity.this);
						sendDataFinishRunnable=new FinishRunnable(TR_Main_Activity.this);
						sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));
						runOnUiThread(sendDataRunnable);
					   } catch (Exception e) {
						   e.printStackTrace();
					   }
				    }
				}
			};
			
			private final Runnable automaticDataFinishTodo = new Runnable(){
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
					   getData=HexDecoding.int2byte((int)(zjz|0x20));
					   try{
					    formatSendMessage=new WifiSendDataFormat(getData, 0x200000AF);
	                    sendDataRunnable=new SendDataRunnable(formatSendMessage, TR_Main_Activity.this);
						sendDataFinishRunnable=new FinishRunnable(TR_Main_Activity.this);
						sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));
						runOnUiThread(sendDataRunnable);
					   } catch (Exception e) {
						   e.printStackTrace();
					   }
				    }
				}
			};
		/**
		 * 原点模式Listener
		 */
		public OnClickListener mode_origin_listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (WifiSetting_Info.mClient == null) {
					Toast.makeText(TR_Main_Activity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!clicked_btn_origin) {
					clicked_btn_manual = false;
					clicked_btn_origin = true;
					clicked_btn_step = false;
					clicked_btn_automatic = false;
					// 删除之前我们定义的通知
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notificationManager.cancel(manual_ID);
				}
				
				formatReadusermode = new WifiReadDataFormat(0x200000AF,1);
				try {
					sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode,TR_Main_Activity.this);
					sendDataFinishRunnable=new FinishRunnable(TR_Main_Activity.this,originDataFinishTodo);
					sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));
					runOnUiThread(sendDatausermodeRunnable);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				//if(checked_origin_start_stop==false){
				new AlertDialog.Builder(TR_Main_Activity.this)
				.setTitle(R.string.T_titlenotice).setMessage(R.string.T_executeydcz)
				.setPositiveButton(R.string.T_titleexecute,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub
						//checked_origin_start_stop = true;
						KeyCodeSend.send(5, TR_Main_Activity.this);
					}

				}).setNegativeButton(R.string.T_titlecancel,null).show();
				/*}else{
					new AlertDialog.Builder(TR_Main_Activity.this)
					.setTitle("操作提示").setMessage("是否停止原点操作？")
					.setPositiveButton("停止",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							checked_origin_start_stop = false;
							KeyCodeSend.send(5, TR_Main_Activity.this);
						}

					}).setNegativeButton("取消",null).show();
				}*/
					}
		};

		/**
		 * 步进模式Listener
		 */
		public OnClickListener mode_step_listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (WifiSetting_Info.mClient == null) {
					Toast.makeText(TR_Main_Activity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!clicked_btn_step) {
					clicked_btn_manual = false;
					clicked_btn_origin = false;
					clicked_btn_step = true;
					clicked_btn_automatic = false;
					// 删除之前我们定义的通知
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notificationManager.cancel(manual_ID);
				}
				formatReadusermode = new WifiReadDataFormat(0x200000AF,1);
				try {
					sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode,TR_Main_Activity.this);
					sendDataFinishRunnable=new FinishRunnable(TR_Main_Activity.this,stepDataFinishTodo);
					sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));
					runOnUiThread(sendDatausermodeRunnable);
				} catch (Exception e) {
					// TODO: handle exception
				}
				new AlertDialog.Builder(TR_Main_Activity.this)
				.setTitle(R.string.T_titlenotice).setMessage(R.string.T_executebjcz)
				.setPositiveButton(R.string.T_titleonestepexecute,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub
						KeyCodeSend.send(5, TR_Main_Activity.this);
					}

				}).setNegativeButton(R.string.T_titlecancel,null).show();
		
						}
		};

		/**
		 * 自动模式Listener
		 */
		public OnClickListener mode_automatic_listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (WifiSetting_Info.mClient == null) {
					Toast.makeText(TR_Main_Activity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!clicked_btn_automatic) {
					clicked_btn_manual = false;
					clicked_btn_origin = false;
					clicked_btn_step = false;
					clicked_btn_automatic = true;
					// 删除之前我们定义的通知
					NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					notificationManager.cancel(manual_ID);

				}
				formatReadusermode = new WifiReadDataFormat(0x200000AF,1);
				try {
					sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode,TR_Main_Activity.this);
					sendDataFinishRunnable=new FinishRunnable(TR_Main_Activity.this,automaticDataFinishTodo);
					sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));
					runOnUiThread(sendDatausermodeRunnable);
				} catch (Exception e) {
					// TODO: handle exception
				}
				//if(checked_automatic_start_stop==false){
				new AlertDialog.Builder(TR_Main_Activity.this)
				.setTitle(R.string.T_titlenotice).setMessage(R.string.T_executezdcz)
				.setPositiveButton(R.string.T_titleexecute,
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub
						//checked_automatic_start_stop = true;
						KeyCodeSend.send(5, TR_Main_Activity.this);
					}

				}).setNegativeButton(R.string.T_titlecancel,null).show();
			/*	}else{
					new AlertDialog.Builder(TR_Main_Activity.this)
					.setTitle("操作提示").setMessage("是否停止自动操作？")
					.setPositiveButton("停止",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							// TODO Auto-generated method stub
							checked_automatic_start_stop = false;
							KeyCodeSend.send(5, TR_Main_Activity.this);
						}

					}).setNegativeButton("取消",null).show();
				}*/
						}
		};

	/**
	 * 开始停止模式Listener
	 */
/*	public OnCheckedChangeListener mode_start_stop_listener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				checked_btn_start_stop = true;
				buttonView.setBackgroundColor(Color.GREEN);

				// 发送keycode=5
				KeyCodeSend.send(5, TR_Main_Activity.this);
			} else {
				checked_btn_start_stop = false;
				buttonView.setBackgroundColor(R.color.lightgray);

				// keycode未定义
				// 发送keycode=5
				KeyCodeSend.send(5, TR_Main_Activity.this);
			}

		}
	};*/

	/**
	 * 竖直档位Listener
	 */
	/*
	 * public OnSeekBarChangeListener verticalBar_listener = new
	 * OnSeekBarChangeListener() {
	 * 
	 * @Override public void onStopTrackingTouch(SeekBar seekBar) {
	 * //档位调节停止时，发送信息给下位机 WifiSendDataFormat sendDataFormat=new
	 * WifiSendDataFormat( HexDecoding.int2byteArray4(seekBar.getProgress()),
	 * AddressPublic.model_allspeed);
	 * 
	 * SendDataRunnable sendDataRunnable=new SendDataRunnable(sendDataFormat,
	 * TR_Main_Activity.this); FinishRunnable finishRunnable=new
	 * FinishRunnable(TR_Main_Activity.this, "档位信息成功发送给下位机");
	 * NormalChatListenner listenner=new NormalChatListenner(sendDataRunnable,
	 * finishRunnable); runOnUiThread(sendDataRunnable);
	 * 
	 * }
	 * 
	 * @Override public void onStartTrackingTouch(SeekBar seekBar) {
	 * 
	 * }
	 * 
	 * @Override public void onProgressChanged(SeekBar seekBar, int progress,
	 * boolean fromUser) { // 档位设置，为了美观使刻度均匀分布，但实际传数据时需要转换数据！ //需要设置最大值为4；然后转换
	 * if (progress < 12) { seekBar.setProgress(0);
	 * System.out.println("progress10"); } else if (progress >= 12 && progress <
	 * 37) { seekBar.setProgress(25); System.out.println("progress20"); } else
	 * if (progress >= 37 && progress < 62) { seekBar.setProgress(50);
	 * System.out.println("progress50"); } else if (progress >= 62 && progress <
	 * 87) { seekBar.setProgress(75); System.out.println("progress80"); } else {
	 * seekBar.setProgress(100); System.out.println("progress100"); }
	 * 
	 * // currentPrograss=(progress+1)*10; } };
	 */
	// public void setLanguage(String lang) {
	// Locale locale = new Locale(lang);
	// Resources res = getResources();
	// Configuration config = res.getConfiguration();
	// config.locale = locale;
	// DisplayMetrics dm = res.getDisplayMetrics();
	// res.updateConfiguration(config, dm);
	// }
 	public class MyAdapter_IO extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView io_address;
 			TextView i_symbolName;
 			TextView i_signalName;
 			TextView i_state;
 			TextView o_symbolName;
 			TextView o_signalName;
 			TextView o_state;
 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		// MyAdapter的构造函数
 		public MyAdapter_IO(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
 			mAppList = appList;
 			mContext = c;
 			mLayoutID=layoutID;
 			mInflater = (LayoutInflater) mContext
 					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 			keyString = new String[from.length];
 			valueViewID = new int[to.length];

 			System.arraycopy(from, 0, keyString, 0, from.length);
 			System.arraycopy(to, 0, valueViewID, 0, to.length);
 		}

 		@Override
 		public int getCount() {
 			return mAppList.size();
 		}

 		@Override
 		public Object getItem(int position) {
 			return mAppList.get(position);
 		}

 		@Override
 		public long getItemId(int position) {
 			return position;
 		}

 		// 删除某一行
 		public void removeItem(int position) {
 			mAppList.remove(position);
 			this.notifyDataSetChanged();
 		}

 		@Override
 		public View getView(int position, View convertView, ViewGroup parent) {
 			if (convertView != null) {
 				holder = (buttonViewHolder) convertView.getTag();
 			} else {
 				convertView = mInflater.inflate(mLayoutID,null);
 				holder = new buttonViewHolder();
 				holder.io_address = (TextView) convertView
 						.findViewById(valueViewID[0]);
 				holder.i_symbolName = (TextView) convertView
 						.findViewById(valueViewID[1]);
 				holder.i_signalName = (TextView) convertView
 						.findViewById(valueViewID[2]);
 				holder.i_state = (TextView) convertView
 						.findViewById(valueViewID[3]);
 				holder.o_symbolName = (TextView) convertView
 						.findViewById(valueViewID[4]);
 				holder.o_signalName = (TextView) convertView
 						.findViewById(valueViewID[5]);
 				holder.o_state = (TextView) convertView
 						.findViewById(valueViewID[6]);

 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String io_address = appInfo.get(keyString[0]).toString();
 				String i_symbolName =  appInfo.get(keyString[1]).toString();
 				String i_signalName =  appInfo.get(keyString[2]).toString();
 				String i_state = appInfo.get(keyString[3]).toString();
 				String o_symbolName =  appInfo.get(keyString[4]).toString();
 				String o_signalName =  appInfo.get(keyString[5]).toString();
 				String o_state = appInfo.get(keyString[6]).toString();
 			
 				holder.io_address.setText(io_address);
 				holder.i_symbolName.setText(i_symbolName);
 				holder.i_signalName.setText(i_signalName);
 				
 				holder.o_symbolName.setText(o_symbolName);
 				holder.o_signalName.setText(o_signalName);
 				
 				if(i_state.equals("1")){
 					holder.i_state.setText("ON");
 					holder.i_state.setBackgroundColor(Color.RED);
 				}else{
 					holder.i_state.setText("OFF");
 					holder.i_state.setBackgroundColor(Color.GRAY);
 				}
 				if(o_state.equals("1")){
 					holder.o_state.setText("ON");
 					holder.o_state.setBackgroundColor(Color.RED);
 				}else{
 					holder.o_state.setText("OFF");
 					holder.o_state.setBackgroundColor(Color.GRAY);
 				}
 			}
 			return convertView;
 		}

 	}
 	
 	public class MyAdapter_Version extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView bootText;
 			TextView bootVersionText;
 			TextView dataText;
 			TextView dataVersionText;

 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		// MyAdapter的构造函数
 		public MyAdapter_Version(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
 			mAppList = appList;
 			mContext = c;
 			mLayoutID=layoutID;
 			mInflater = (LayoutInflater) mContext
 					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 			keyString = new String[from.length];
 			valueViewID = new int[to.length];

 			System.arraycopy(from, 0, keyString, 0, from.length);
 			System.arraycopy(to, 0, valueViewID, 0, to.length);
 		}

 		@Override
 		public int getCount() {
 			return mAppList.size();
 		}

 		@Override
 		public Object getItem(int position) {
 			return mAppList.get(position);
 		}

 		@Override
 		public long getItemId(int position) {
 			return position;
 		}

 		// 删除某一行
 		public void removeItem(int position) {
 			mAppList.remove(position);
 			this.notifyDataSetChanged();
 		}

 		@Override
 		public View getView(int position, View convertView, ViewGroup parent) {
 			if (convertView != null) {
 				holder = (buttonViewHolder) convertView.getTag();
 			} else {
 				convertView = mInflater.inflate(mLayoutID,null);
 				holder = new buttonViewHolder();
 				holder.bootText = (TextView) convertView
 						.findViewById(valueViewID[0]);
 				holder.bootVersionText = (TextView) convertView
 						.findViewById(valueViewID[1]);
 				holder.dataText = (TextView) convertView
 						.findViewById(valueViewID[2]);
 				holder.dataVersionText = (TextView) convertView
 						.findViewById(valueViewID[3]);

 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String bootText =  appInfo.get(keyString[0]).toString();
 				String bootVersionText = appInfo.get(keyString[1]).toString();
 				String dataText =  appInfo.get(keyString[2]).toString();
 				String dataVersionText =  appInfo.get(keyString[3]).toString();

 				holder.bootText.setText(bootText);
 				holder.bootVersionText.setText(bootVersionText);
 				holder.dataText.setText(dataText);
 				holder.dataVersionText.setText(dataVersionText);
 				
 			}
 			return convertView;
 		}

 	}

 	public class MyAdapter_Alarm extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView num_alarm;
 			TextView time_alarm;
 			TextView content_alarm;

 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		// MyAdapter的构造函数
 		public MyAdapter_Alarm(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
 			mAppList = appList;
 			mContext = c;
 			mLayoutID=layoutID;
 			mInflater = (LayoutInflater) mContext
 					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 			keyString = new String[from.length];
 			valueViewID = new int[to.length];

 			System.arraycopy(from, 0, keyString, 0, from.length);
 			System.arraycopy(to, 0, valueViewID, 0, to.length);
 		}

 		@Override
 		public int getCount() {
 			return mAppList.size();
 		}

 		@Override
 		public Object getItem(int position) {
 			return mAppList.get(position);
 		}

 		@Override
 		public long getItemId(int position) {
 			return position;
 		}

 		// 删除某一行
 		public void removeItem(int position) {
 			mAppList.remove(position);
 			this.notifyDataSetChanged();
 		}

 		@Override
 		public View getView(int position, View convertView, ViewGroup parent) {
 			if (convertView != null) {
 				holder = (buttonViewHolder) convertView.getTag();
 			} else {
 				convertView = mInflater.inflate(mLayoutID,null);
 				holder = new buttonViewHolder();
 				holder.num_alarm = (TextView) convertView
 						.findViewById(valueViewID[0]);
 				holder.time_alarm = (TextView) convertView
 						.findViewById(valueViewID[1]);
 				holder.content_alarm = (TextView) convertView
 						.findViewById(valueViewID[2]);

 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String num_alarm = appInfo.get(keyString[0]).toString();
 				String time_alarm = appInfo.get(keyString[1]).toString();
 				String content_alarm = appInfo.get(keyString[2]).toString();

 				holder.num_alarm.setText(num_alarm);
 				holder.time_alarm.setText(time_alarm);
 				holder.content_alarm.setText(content_alarm);

 				holder.num_alarm.setOnClickListener(new AlarmListener(position));
				holder.time_alarm.setOnClickListener(new AlarmListener(position));
				holder.content_alarm.setOnClickListener(new AlarmListener(position));
 			}
 			return convertView;
 		}
	class AlarmListener implements OnClickListener {
			
			private int position;

			// 构造函数
			AlarmListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
				try{
	 				String setValueString=mAppList.get(position).get(keyString[1]).toString().trim();
	 				String showmsg="无详细警报信息";
	 				ArrayList<HashMap<String, Object>> AlarmList = ArrayListBound.getDeviceAlarmListData();
	 				if(setValueString.contains("动作")){
	 					for(int i=0;i<AlarmList.size();i++){
							if(AlarmList.get(i).get("symbolNameEditText").toString().trim().equals("")){
								continue;
							}
							if(String.valueOf(Integer.parseInt(AlarmList.get(i).get("symbolNameEditText").toString().trim())).equals(setValueString.substring(4,setValueString.length()))){
							  String notemsg=AlarmList.get(i).get("noteEditText").toString().trim();
	 	 					
	 	 					  showmsg=setValueString+"\n"+notemsg;
	 	 					  System.out.println("showmsg=="+showmsg);
	 	 					  break;
	 	 					}
	 	 				}
	 				}else{
	 					if(setValueString.contains("伺服")){
	 						setValueString=setValueString.substring(0, 2)+setValueString.substring(3, setValueString.length());
	 					}
	 					for(int i=0;i<list_alarmzb.size();i++){
	 	 					System.out.println(list_alarmzb.get(i).get("alarmzbnum").toString().trim());
	 	 					System.out.println(setValueString);
	 	 					if(list_alarmzb.get(i).get("alarmzbnum").toString().trim().equals(setValueString)){
	 	 						showmsg=setValueString+"\n"+list_alarmzb.get(i).get("alarmzbName").toString().trim()
	 	 								+"\n"+list_alarmzb.get(i).get("alarmzbmsg").toString().trim();
	 	 						System.out.println("showmsg=="+showmsg);
	 	 						break;
	 	 					}
	 	 				}	
	 				}
	 				
	 				new AlertDialog.Builder(TR_Main_Activity.this).setTitle("提示")
					   .setMessage(showmsg)
					   .setPositiveButton("知道了", null).show();
				}catch(Exception e){
					e.printStackTrace();
				}
			}

		}
 	}
	/**
	 * 菜单跳转
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity, menu);// 菜单
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_activity_programming:

			if (alreadyChecked_operatePassword) {// 已经输入过密码，直接跳转
				startActivity(new Intent().setClass(TR_Main_Activity.this,
						TR_Programming_Activity.class));
			} else {// 否则先输密码才能跳转
				password_operate = sharedPreference_password.getString(
						"password_operate", "");
				System.out.println(password_operate + "密码");
				if (password_operate.equals("")) {// 保存的密码为空，直接跳转
					startActivity(new Intent().setClass(TR_Main_Activity.this,
							TR_Programming_Activity.class));
				} else {// 密码不为空
					// 输密码的输入控制
					final EditText editText = new EditText(this);
					editText.setHint(R.string.T_passwordputin);
					editText.setTransformationMethod(PasswordTransformationMethod
							.getInstance());// 设置密码为不可见。
					// editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					// //设置密码为可见。
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
							// return
							// android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
						}
					});
					new AlertDialog.Builder(this)
					.setTitle(R.string.T_passwordputinforpower)
					.setView(editText)
					.setCancelable(false)
					.setPositiveButton(R.string.T_titlesure,
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							String password_input = editText
									.getText().toString()
									.trim();
							if (password_operate
									.equals(password_input)) {// 输入正确就跳转，并关闭窗口

								alreadyChecked_operatePassword = true;// 跳转标志位设为真
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

								startActivity(new Intent()
								.setClass(
										TR_Main_Activity.this,
										TR_Programming_Activity.class));
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
								editText.setHint(R.string.T_passwordputinoperateerror);

							}

						}
					})
					.setNegativeButton(R.string.T_titlecancel,
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

			break;
		case R.id.menu_activity_setting:

			if (alreadyChecked_operatePassword) {// 已经输入过密码，直接跳转
				startActivity(new Intent().setClass(TR_Main_Activity.this,
						NewPragramActivity.class));
			} else {// 否则先输密码才能跳转
				password_operate = sharedPreference_password.getString(
						"password_operate", "");
				if (password_operate.equals("")) {// 保存的密码为空，直接跳转
					startActivity(new Intent().setClass(TR_Main_Activity.this,
							NewPragramActivity.class));
				} else {// 密码不为空
					// 输密码的输入控制
					final EditText editText = new EditText(this);
					editText.setHint(R.string.T_passwordputin);
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
					.setTitle(R.string.T_passwordputinforpower)
					.setView(editText)
					.setCancelable(false)
					.setPositiveButton(R.string.T_titlesure,
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							String password_input = editText
									.getText().toString()
									.trim();
							if (password_operate
									.equals(password_input)) {// 输入正确就跳转，并关闭窗口

								alreadyChecked_operatePassword = true;// 跳转标志位设为真
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

								startActivity(new Intent()
								.setClass(
										TR_Main_Activity.this,
										NewPragramActivity.class));
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
								editText.setHint(R.string.T_passwordputinoperateerror);

							}

						}
					})
					.setNegativeButton(R.string.T_titlecancel,
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
			break;
		case R.id.menu_activity_parameter_setting:
			if (alreadyChecked_operatePassword) {// 已经输入过密码，直接跳转
				startActivity(new Intent().setClass(TR_Main_Activity.this,
						TR_Parameter_Setting_Activity.class));
			} else {// 否则先输密码才能跳转
				password_operate = sharedPreference_password.getString(
						"password_operate", "");
				System.out.println(password_operate + "密码");
				if (password_operate.equals("")) {// 保存的密码为空，直接跳转
					startActivity(new Intent().setClass(TR_Main_Activity.this,
							TR_Parameter_Setting_Activity.class));
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
					.setTitle("请输入操作密码以获得权限")
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
							if (password_operate
									.equals(password_input)) {// 输入正确就跳转，并关闭窗口

								alreadyChecked_operatePassword = true;// 跳转标志位设为真
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

								startActivity(new Intent()
								.setClass(
										TR_Main_Activity.this,
										TR_Parameter_Setting_Activity.class));
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
								editText.setHint("操作密码输入不正确，请重输入");

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
			break;
		case R.id.menu_activity_maintainGuide:
			if (alreadyChecked_operatePassword) {// 已经输入过密码，直接跳转
				startActivity(new Intent().setClass(TR_Main_Activity.this,
						TR_MaintainGuide_Activity.class));
			} else {// 否则先输密码才能跳转
				password_operate = sharedPreference_password.getString(
						"password_operate", "");
				System.out.println(password_operate + "密码");
				if (password_operate.equals("")) {// 保存的密码为空，直接跳转
					startActivity(new Intent().setClass(TR_Main_Activity.this,
							TR_MaintainGuide_Activity.class));
				} else {// 密码不为空
					// 输密码的输入控制
					final EditText editText = new EditText(this);
					editText.setHint(R.string.T_passwordputin);
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
					.setTitle(R.string.T_passwordputinforpower)
					.setView(editText)
					.setCancelable(false)
					.setPositiveButton(R.string.T_titlesure,
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							String password_input = editText
									.getText().toString()
									.trim();
							if (password_operate
									.equals(password_input)) {// 输入正确就跳转，并关闭窗口

								alreadyChecked_operatePassword = true;// 跳转标志位设为真
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

								startActivity(new Intent()
								.setClass(
										TR_Main_Activity.this,
										TR_MaintainGuide_Activity.class));
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
								editText.setHint(R.string.T_passwordputinoperateerror);

							}

						}
					})
					.setNegativeButton(R.string.T_titlecancel,
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
			break;
		case R.id.menu_Exit:
			// 删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.cancel(manual_ID);
		//	ExitTR.getInstance().exit();
			break;

		default:
			// 对没有处理的事件，交给父类来处理
			return super.onOptionsItemSelected(item);
		}
		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
		return true;
	}

	/**
	 * 在主界面中按两次退出才退出程序，防止误操作
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, R.string.T_oneagainexittr, Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				// 删除之前我们定义的通知
				NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				notificationManager.cancel(manual_ID);

//				ExitTR.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void creatFolderSysytem() {
		// TODO Auto-generated method stub
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		// 创建文件夹目录
		if (!sdCardExist) {
			Toast.makeText(getApplicationContext(), R.string.T_putinsdcard,
					Toast.LENGTH_SHORT).show();// 如果不存在SD卡，进行提示
		} else {
			/* 分级目录必须分开创建，不能嵌套创建，否则子目录可能不存在 */

			File trFile = new File(Constans.trPATH);// 新建一级目录
			if (!trFile.exists()) {// 判断文件夹目录是否存在
				trFile.mkdir();// 如果不存在则创建
				//Toast.makeText(getApplicationContext(),"新创建文件夹结构" + Constans.trFolder, Toast.LENGTH_SHORT).show();
				Toast.makeText(getApplicationContext(),R.string.T_newcreatefiles, Toast.LENGTH_SHORT).show();// 提示新创建
			} else {
				// Toast.makeText(getApplicationContext(),
				// "已存在文件夹" + Constans.trFolder, Toast.LENGTH_SHORT)
				// .show();// 提示已创建
			}

			Constans.mechanicalParameter.createFolder(Constans.trPATH,
					Constans.mechanicalParameterFolder);
			Constans.otherParameter.createFolder(Constans.trPATH,
					Constans.otherParameterFolder).createFile(
							"otherParameter.trt");
			Constans.mouldData
			.createFolder(Constans.trPATH, Constans.mouldDataFolder)
			.createFile("mouldList.trt").createFile("mould.trt");
			Constans.raw.createFolder(Constans.trPATH, "raw");

		}
	}
	

    private void initData()
	{
    	Config.list_pname.clear();
    	Config.list_fpname.clear();
    	Config.list_spname.clear();
    	Config.list_timername.clear();
    	Config.list_countername.clear();
		try {
			for(int i=0;i<ProgramList.size();i++){
				String order_MOVE=ProgramList.get(i).get("orderSpinner").toString();
				String operatstr=ProgramList.get(i).get("operatText").toString();
				if((order_MOVE.contains("MOVE")||order_MOVE.contains("MOVEP"))&&operatstr.contains("SP")){
					if(!Config.list_spname.contains(operatstr.substring(operatstr.indexOf("S"), operatstr.length()))){
						Config.list_spname.add(operatstr.substring(operatstr.indexOf("S"), operatstr.length()));

					}
				}else if((order_MOVE.contains("MOVE")||order_MOVE.contains("MOVEP"))&&operatstr.contains("FP")){
					if(!Config.list_fpname.contains(operatstr.substring(operatstr.indexOf("F"), operatstr.length()))){
						Config.list_fpname.add(operatstr.substring(operatstr.indexOf("F"), operatstr.length()));

					}
				}else if((order_MOVE.contains("MOVE")||order_MOVE.contains("MOVEP"))&&operatstr.contains("P")){
					if(!Config.list_pname.contains(operatstr.substring(operatstr.indexOf("P"), operatstr.length()))){
						Config.list_pname.add(operatstr.substring(operatstr.indexOf("P"), operatstr.length()));
					}
				}else if(order_MOVE.contains("TWAIT")){
					if(!Config.list_timername.contains(operatstr)){
						Config.list_timername.add(operatstr);
					}
				}else if(order_MOVE.contains("CINC")||order_MOVE.contains("CDEC")||order_MOVE.contains("CCLR")||order_MOVE.contains("CSET")){
					operatstr=operatstr.split("=")[0];
					if(!Config.list_countername.contains(operatstr)){
						Config.list_countername.add(operatstr);
						}
						}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
    //利用广播+全局状态位来控制  LED的显示
    private class UpdateLedReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            Log.d("mpeng","the UpdateLedReceiver");
            if(action.equals("KeyMsg"))
            {
            	Log.d("mpeng","UpdateLedReceiver 1111111");
            	 byte[] keycode = new byte[2];
            	 keycode = intent.getExtras().getByteArray("keycode");
            	 KeyMsgHandle(keycode,context);
//            	 int n = keycode & 0xFF;//255
//            	if(KeyMsgDialog != null)
//            		KeyMsgDialog.dismiss();
//            	 KeyMsgDialog = new AlertDialog.Builder(context).setTitle("收到按键")
//            .setMessage("键值为："+ n).setNegativeButton("ok", null)
//            .show();
            
            }else if(action.equals("updateBtnBg"))
            {	
            String BtnName = intent.getExtras().getString("button");
            if(BtnName.equals("move")){
            	if(Config.MoveState)
            		moving_led.setEnabled(true);
            	else
            		moving_led.setEnabled(false);
            	
            }else if(BtnName.equals("auto")){
            	if(Config.AutoState)
            		zd_led.setEnabled(true);
            	else
            		zd_led.setEnabled(false);
            	
            }else if(BtnName.equals("alarm")){
            	Log.d("mpeng","alarm 1111!!");
            	if(Config.AlarmState)
            	{
            		Log.d("mpeng","alarm true!!");
            		alarm_led.setEnabled(true);
            	}
            	else
            		alarm_led.setEnabled(false);
            }
            else if(BtnName.equals("startThread"))
            {
//            	收到开机完成的广播后再去初始这个
            	Log.d("mpeng","start thread");
            	Message msg =new Message();
            	msg.what = 33;
            	mHandler.sendMessage(msg);
            	boolean sdCardExist = Environment.getExternalStorageState().equals(
        				android.os.Environment.MEDIA_MOUNTED);
            	Log.d("mpeng","xxxxxxx sdcardexit is"+sdCardExist);

            }
           }
            
        }
    }
	private void KeyMsgHandle(byte[]  kc ,Context context)
	{
		 int Key_Value = kc[0] & 0xFF;//255
		 int Key_Function = kc[1] & 0xFF;//255
//		 if(KeyMsgDialog != null)
//         	KeyMsgDialog.dismiss();
//          KeyMsgDialog = new AlertDialog.Builder(context).setTitle("提示")
//         .setMessage("keyvalue:"+ Key_Value+"keyfunction :"+Key_Function)
//         .setPositiveButton("OK", null).show();
		
		 switch(Key_Value)
		 {
		 	 case 11:  //HOME		 	
		 	 case 14:		 	
		 	 case 12:			 		
		 	 case 34:		 		
		 	 case 13:		 	
		 	 case 24:		 	
		 	 case 33:		 	
		 	 case 23:		 	
		 	 //case 153:
		 		 if(Key_Function==0)
		 		 {
		 			 Log.e("mpeng","SHOW key msg dialog");
			        if(KeyMsgDialog != null)
		            	KeyMsgDialog.dismiss();
		             KeyMsgDialog = new AlertDialog.Builder(context).setTitle("提示")
		            .setMessage("是否要进行自由操作！"+ Key_Value)
		            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent it = new Intent();
							it.setClass(TR_Main_Activity.this,NewPragramActivity.class);
							it.setFlags(3);
							startActivity(it);
						}
					})
		            .setPositiveButton("取消", null)
		            .show();
		             KeyMsgDialog.setCanceledOnTouchOutside(false);
		 		 }
		 		 break;
		 		 
		 		 
		 	 case 32:
		 		 KeyCodeSend.send(26, TR_Main_Activity.this);		 		 
		 		 break;
		 	 case 22:
		 		KeyCodeSend.send(25, TR_Main_Activity.this);
		 		 break;
		 	 case 31:
		 		KeyCodeSend.send(24, TR_Main_Activity.this);
		 		 break;
		 	 case 21:
		 		KeyCodeSend.send(23, TR_Main_Activity.this);
		 		 break;	 
		 }
		
	}
}
