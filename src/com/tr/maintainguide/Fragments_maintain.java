package com.tr.maintainguide;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.tr.ExitTR;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.main.TR_Main_Activity.MyAdapter_Alarm;
import com.tr.programming.TR_Programming_Activity;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiReadMassiveDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.CntCycQueryRunnable;
import wifiRunnablesAndChatlistener.DelayCount;
import wifiRunnablesAndChatlistener.DelayTimerQueryRunnble;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;
import wifiRunnablesAndChatlistener.maintimeRunnable;

import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;
 
import com.explain.HexDecoding;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class Fragments_maintain extends Fragment{
	
	private Button btn_version;
	private Button btn_alarm;
	private Button btn_passwordSet;
	private Button btn_io;
	private Button btn_alarmzb;
	private Button btn_zqcx;
	private Button btn_maintimeset;
	
	private ListView listView_version;
	private ListView listView_alarm;
	private ListView listView_io;
	private ListView listView_alarmzb;
	
	private MyAdapter_Version adapter_version;
	private MyAdapter_Alarm adapter_alarm;
	private MyAdapter_IO adapter_io;
	private MyAdapter_Alarmzb adapter_alarmzb;
	
	private AlertDialog dialog_io;
	private AlertDialog dialog_timeset;
	ArrayList<HashMap<String, Object>> list_version ;
	ArrayList<HashMap<String, Object>> list_alarm ;
	ArrayList<HashMap<String, Object>> list_io ;
	ArrayList<HashMap<String, Object>> list_alarmzb;
	
	private int thisyear;
	private int thismonth;
	private int thisdate;
	private int thisweek;
	private int thishour;
	private int thismin;
	private int thissec;
	
	private TextView edityear;
	private TextView editmonth;
	private TextView editdate;
	private TextView textweek;
	private TextView edithour;
	private TextView editmin;
	private TextView editsec;
	private NumberPicker secPicker;
	protected WifiReadDataFormat formatReadtime;
	protected SendDataRunnable sendDatatimeRunnable;
	
	private SharedPreferences sharedPreference_password;
	RadioGroup radioGroup_password;
	Button btn_create_password;
	Button btn_change_password;
	Button btn_delete_password;
	private String password_operate;
	private String password_setting;
	private String password_programming;
	String password;
	String title_create;
	String title_delete;
	String title_change;
	
	String passwordToDelete;
    private SendDataRunnable sendDataRunnable;
	private FinishRunnable sendDataFinishRunnable;
	private WifiReadDataFormat formatReadMessage;
	private byte[] getData;
	private  ChatListener DataFeedback ;

	private SendDataRunnable sendDataAlmRunnable;
//	private FinishRunnable sendDataAlmFinishRunnable;
	private WifiReadMassiveDataFormat formatReadAlmmsg;
	private WifiReadDataFormat formatReadAlm;
	//private byte[] getData;
	private  ChatListener AlmDataFeedback ;	
	private ChatListener AlmCPFeedback;
	private int Almallnum;// 当前伺服总数
	private int AlmCP;// 当前伺服参数编号
	
	private SendDataRunnable sendDataioRunnable;
	private WifiReadDataFormat formatReadio;
	private  ChatListener ioDataFeedback ;
		
	
	private WifiSendDataFormat formatSendMessage;
	String password_old;
	String passwordToChange;
	
	private ledRunnable ledrunnable;
	private AlarmQueryRunnable alarmQueryRunnable;
	private CntCycQueryRunnable cntCycQueryRunnable;
	private maintimeRunnable maintimerunnable;
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		list_alarmzb= ArrayListBound.getAlarmzbListData();
		new Thread() {
			public void run() {
				
				list_version = ArrayListBound.getVersionListData();
				list_alarm = ArrayListBound.getAlarmListData();
				list_io = ArrayListBound.getIOListData();
			    
			}

		}.start();
		System.out.println(" Fragments_maintain alarmQueryRunnable11111");
		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
		Thread a2 = new Thread(alarmQueryRunnable);
		a2.start();
		ledrunnable=new ledRunnable(getActivity());
		Thread a4 = new Thread(ledrunnable);
		a4.start();
	}
	
//	String password_create;
//	AlertDialog dialog_create_again;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Fragments_maintain   onCreate ");
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//DelayTimerQueryRunnble.destroy();
		if(dialog_timeset!=null){
			dialog_timeset.dismiss();
		}
		if(dialog_io!=null){
			dialog_io.dismiss();
		}
		
		alarmQueryRunnable.destroy();
		ledrunnable.destroy();
	}

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Fragments_maintain   onCreateView ");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.tab_maintainguide_maintain, container,false);
	}

	public void getAlmdata(){
		System.out.println("Almallnum="+Almallnum+"       AlmCP======="+AlmCP);
		formatReadAlmmsg = new WifiReadMassiveDataFormat(0x40024304,12*128);
		try {
			sendDataAlmRunnable=new SendDataRunnable(formatReadAlmmsg, getActivity());

			sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readAlmDataFinishTodo);

			sendDataAlmRunnable.setOnlistener(new NormalChatListenner(sendDataAlmRunnable, sendDataFinishRunnable));

			getActivity().runOnUiThread(sendDataAlmRunnable);
	
			} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
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
			    adapter_alarm=new MyAdapter_Alarm(getActivity(), list_alarm, R.layout.maintain_guide_alarm_item, 
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
				// TODO Auto-generated method stub
				//对于返回的36字节
				//发送正确且完成
				//处理返回的数据	
				getData=new byte[formatReadio.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				if( formatReadio.getFinalData() != null)  
				{
				System.arraycopy(formatReadio.getFinalData(), 0, getData, 0, formatReadio.getLength());
				//重新给list_usermode列表赋值
				for (int i = 0; i < 128; i++) {
	 				HashMap<String, Object> map = new HashMap<String, Object>();
	 				map.put("io_address",i);
	 				map.put("i_symbolName", ArrayListBound.getDeviceActualInputListData().get(i).get("symbolNameEditText"));
	 				map.put("i_signalName", ArrayListBound.getDeviceActualInputListData().get(i).get("signalNameEditText"));
	 				map.put("o_symbolName", ArrayListBound.getDeviceActualOutputListData().get(i).get("symbolNameEditText"));
	 				map.put("o_signalName", ArrayListBound.getDeviceActualOutputListData().get(i).get("signalNameEditText")); 
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
					 adapter_io=new MyAdapter_IO(getActivity(), list_io, R.layout.maintainguide_maintain_io_item, 
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
			}
			}

		};

	@Override
	public void onStart(){
		super.onStart();
		 
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("Fragments_maintain   onActivityCreated ");
		
		sharedPreference_password = getActivity().getSharedPreferences("password",Context.MODE_PRIVATE);// password为xml文件名
		//临时清除存储的值
//		sharedPreference_password.edit().putString("password_operate", "").commit();
//		sharedPreference_password.edit().putString("password_setting", "").commit();
//		sharedPreference_password.edit().putString("password_programing", "").commit();
		// 得到文件中的name标签值，第二个参数表示如果没有这个标签的话，返回的默认值
//		password_operate = sharedPreference_password.getString("password_operate", "");// 这个language为语言代号，与上一个不同
//		password_setting = sharedPreference_password.getString("password_setting", "");
//		password_programming = sharedPreference_password.getString("password_programming", "");
		btn_maintimeset=(Button) getActivity().findViewById(R.id.btn_maintimeSet);
		if(btn_maintimeset==null){
			return;
		}
		btn_maintimeset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View view_timeset=View.inflate(getActivity(), R.layout.maintainguide_maintain_maintimeset, null);
				dialog_timeset=new AlertDialog.Builder(getActivity())
				.setTitle("MAIN基板时间设定").setView(view_timeset)
				.setPositiveButton("设定",
	            new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) {
						byte[] temp=new byte[8];
						try {
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(thisyear)-2000), 0, temp,0, 1);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(thismonth)), 0, temp,1, 1);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(thisdate)), 0, temp,2, 1);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(thisweek)), 0, temp,3, 1);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(thishour)), 0, temp,4, 1);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(thismin)), 0, temp,5, 1);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(thissec)), 0, temp,6, 1);
						  formatSendMessage=new WifiSendDataFormat(temp,0x1000B100);
	                        
	                        	sendDataRunnable=new SendDataRunnable(formatSendMessage, getActivity());

								sendDataFinishRunnable=new FinishRunnable(getActivity(), "MAIN基板时间设定信息成功发送给下位机");

								sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

								getActivity().runOnUiThread(sendDataRunnable);
								} catch (Exception e) {
								// TODO: handle exception
								  e.printStackTrace();
								  Toast.makeText(getActivity(), "数据有误，请重新输入！", Toast.LENGTH_SHORT).show();
							    }
						 try {
			                    Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			                    field.setAccessible(true);
			                    field.set(dialog, false);
			                } catch (Exception e) {
			                    e.printStackTrace();
			                }

				    }
				}).setNegativeButton("取消",new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) {
						try {
							 Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
							 field.setAccessible(true);
							 field.set(dialog, true);
							 } catch (Exception e) {
							 e.printStackTrace();
							 } 
					}
				}).show();
				        edityear=(TextView)dialog_timeset.findViewById(R.id.editText_year);
						editmonth=(TextView)dialog_timeset.findViewById(R.id.editText_month);
						editdate=(TextView)dialog_timeset.findViewById(R.id.editText_date);
						textweek=(TextView)dialog_timeset.findViewById(R.id.week);
						edithour=(TextView)dialog_timeset.findViewById(R.id.editText_hour);
						editmin=(TextView)dialog_timeset.findViewById(R.id.editText_min);
						editsec=(TextView)dialog_timeset.findViewById(R.id.editText_sec);
						secPicker=(NumberPicker)dialog_timeset.findViewById(R.id.date_picker);
						if(secPicker==null){return;}
						secPicker.setMinValue(0);
						secPicker.setMaxValue(59);
					    
						if(edityear==null){return;}
						if(editmonth==null){return;}
						if(editdate==null){return;}
						if(textweek==null){return;}
						if(edithour==null){return;}
						if(editmin==null){return;}
						if(editsec==null){return;}
						
						DatePicker datePicker=(DatePicker)dialog_timeset.findViewById(R.id.datePicker);
				        TimePicker timePicker=(TimePicker)dialog_timeset.findViewById(R.id.timePicker);
				        if(datePicker==null){return;}
						if(timePicker==null){return;}
				        final Calendar calendar=Calendar.getInstance();
				        int cyear=calendar.get(Calendar.YEAR);
				        int monthOfYear=calendar.get(Calendar.MONTH);
				        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
				        thisyear=cyear;
		            	thismonth=monthOfYear+1;
		            	thisdate=dayOfMonth;
		                calendar.set(cyear,monthOfYear,dayOfMonth);
		                thisweek = calendar.get(Calendar.DAY_OF_WEEK);
		                thishour=calendar.get(Calendar.HOUR_OF_DAY);
		            	thismin=calendar.get(Calendar.MINUTE);
		            	thissec=calendar.get(Calendar.SECOND);
				        datePicker.init(cyear, monthOfYear, dayOfMonth, new OnDateChangedListener(){

				            public void onDateChanged(DatePicker view, int year,
				                    int monthOfYear, int dayOfMonth) {
				            	thisyear=year;
				            	thismonth=monthOfYear+1;
				            	thisdate=dayOfMonth;
				                calendar.set(year,monthOfYear,dayOfMonth);
				                thisweek = calendar.get(Calendar.DAY_OF_WEEK);
				            }
				            
				        });
				        timePicker.setIs24HourView(true);// 设置是否为24小时制    
				        timePicker.setOnTimeChangedListener(new OnTimeChangedListener(){
				            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				            	thishour=hourOfDay;
				            	thismin=minute;
				            }
				            
				        });
				        secPicker.setValue(thissec);
				        secPicker.setOnValueChangedListener(new OnValueChangeListener(){

				            @Override
				            public void onValueChange(NumberPicker picker, int oldVal,int newVal) {
				                // TODO Auto-generated method stub
				            	thissec=newVal;
				            }
				            
				        });

				      //监听菜单的关闭事件  
				        dialog_timeset.setOnDismissListener(new OnDismissListener() {              
				            
							@Override
							public void onDismiss(DialogInterface arg0) {
								// TODO Auto-generated method stub
								maintimerunnable.destroy();
							}  
				        }); 
				        maintimerunnable = new maintimeRunnable(
								getActivity(), edityear,editmonth,editdate,textweek,edithour,editmin,editsec);
						
						Thread a3 = new Thread(maintimerunnable);
						a3.start();
						
						
			}
		});	
		
		
		btn_alarmzb=(Button) getActivity().findViewById(R.id.button3);
		if(btn_alarmzb==null){
			return;
		}
		btn_alarmzb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				View view_alarmzb=View.inflate(getActivity(), R.layout.maintainguide_maintain_alarmzb, null);
				AlertDialog dialog_alarmzb=new AlertDialog.Builder(getActivity())
				.setTitle("警报总表").setView(view_alarmzb).setNegativeButton("取消",null).show();
				listView_alarmzb = (ListView) dialog_alarmzb
						.findViewById(R.id.listView_alarmzb);
				if(listView_alarmzb==null){
					return;
				}
				//ArrayList<HashMap<String, Object>> list_alarmzb= ArrayListBound.getAlarmzbListData();
				adapter_alarmzb = new MyAdapter_Alarmzb(getActivity(), list_alarmzb,
						R.layout.maintainguide_maintain_alarmzb_item,
						new String[] { "alarmzbnum", "alarmzbName", "alarmzbmsg" },
						new int[] { R.id.alarmzbnum, R.id.alarmzbName, R.id.alarmzbmsg});
				listView_alarmzb.setAdapter(adapter_alarmzb);
			}
		});	
		
		
			
		btn_version=(Button) getActivity().findViewById(R.id.btn_version);
		if(btn_version==null){
			return;
		}
		btn_version.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				View view_version = View.inflate(getActivity(),R.layout.maintain_guide_version_dispaly, null);
				
				// 先发送数据，然后等待20ms，等版本数据都到了，再导入到显示的对话框。
				//========================	

					//int readAddr = 0x1000B180;  //!!!!!!注意该地址的变化
					
					//================
						AlertDialog dialog_version = new AlertDialog.Builder(
						getActivity()).setTitle("版本显示").setView(view_version)
						.setNegativeButton("取消",null).show();
				listView_version = (ListView) dialog_version
						.findViewById(R.id.listView_version);
				if(listView_version==null){
					return;
				}
				
				adapter_version = new MyAdapter_Version(getActivity(), list_version,
						R.layout.maintain_guide_version_item,
						new String[] { "boot", "boot_version", "data","data_version" },
						new int[] { R.id.boot, R.id.boot_version, R.id.data,R.id.data_version });
				listView_version.setAdapter(adapter_version);
				if (WifiSetting_Info.mClient == null) {
					Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				formatReadMessage = new WifiReadDataFormat(0x1000B110,30*4);
				try {
					sendDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());

					sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readversionDataFinishTodo);

					sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

					getActivity().runOnUiThread(sendDataRunnable);

				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
				}


			}
		});
		/**
		 * ******************************
		 * 警报履历小窗口
		 * ******************************
		 */
		/** 读取伺服参数编号时的通信线程收到信息时的处理函数. */
		AlmCPFeedback = new ChatListener() {
			@Override
			public void onChat(byte[] message) {

				//返回的标志位STS1处的判断以及和校验
				formatReadAlm.backMessageCheck(message);
				if(!formatReadAlm.finishStatus()){
					getActivity().runOnUiThread(sendDataAlmRunnable);			
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
		
		btn_alarm=(Button) getActivity().findViewById(R.id.btn_alarm);
		if(btn_alarm==null){
			return;
		}
		btn_alarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (WifiSetting_Info.mClient == null) {
					Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				list_alarm.clear();
				View view_alarm=View.inflate(getActivity(), R.layout.maintain_guide_alarm, null);
				AlertDialog dialog_alarm=new AlertDialog.Builder(getActivity()).setTitle("警报履历").setView(view_alarm).setNegativeButton("取消",null).show();
						
				 listView_alarm=(ListView) dialog_alarm.findViewById(R.id.listView_alarm);
					if(listView_alarm==null){
						return;
					}
					formatReadAlm = new WifiReadDataFormat(0x400242FC,8);
					try {

						sendDataAlmRunnable = new SendDataRunnable(AlmCPFeedback, formatReadAlm,getActivity());
						getActivity().runOnUiThread(sendDataAlmRunnable);
					
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
					}
				 
				 
			}
		});
		
		btn_zqcx=(Button) getActivity().findViewById(R.id.button2);
		if(btn_zqcx==null){
			return;
		}
		btn_zqcx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				View view_io=View.inflate(getActivity(), R.layout.maintain_guide_periodicsurveillance, null);
				dialog_io=new AlertDialog.Builder(getActivity()).setTitle("周期监视（s）").setView(view_io).setNegativeButton("取消",new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) {
						cntCycQueryRunnable.destroy();
					}
				}).show();
				//监听菜单的关闭事件  
				dialog_io.setOnDismissListener(new OnDismissListener() {              
		            
					@Override
					public void onDismiss(DialogInterface arg0) {
						// TODO Auto-generated method stub
						cntCycQueryRunnable.destroy();
					}  
		        }); 
				cntCycQueryRunnable = new CntCycQueryRunnable(
						getActivity(), dialog_io.findViewById(R.id.periodic_modeling),
						dialog_io.findViewById(R.id.periodic_out), dialog_io.findViewById(R.id.periodic_full),1);
				
				Thread a3 = new Thread(cntCycQueryRunnable);
				a3.start();
			}
		});
		
		btn_io=(Button) getActivity().findViewById(R.id.button5);
		if(btn_io==null){
			return;
		}
		btn_io.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (WifiSetting_Info.mClient == null) {
					Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				list_io.clear();
				View view_io=View.inflate(getActivity(), R.layout.maintainguide_maintain_io, null);
				dialog_io=new AlertDialog.Builder(getActivity()).setTitle("IO监视").setView(view_io).setNegativeButton("取消",new DialogInterface.OnClickListener() 
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
					
					formatReadio= new WifiReadDataFormat(0x10007000,32);

					try {
						sendDataioRunnable=new SendDataRunnable(formatReadio, getActivity());

						sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readioDataFinishTodo);

						sendDataioRunnable.setOnlistener(new NormalChatListenner(sendDataioRunnable, sendDataFinishRunnable));

						getActivity().runOnUiThread(sendDataioRunnable);
				
						} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
					}
			
				 

			}
		});
		/**
		 * ******************************
		 * 密码设定小窗口
		 * ******************************
		 */
		btn_passwordSet=(Button) getActivity().findViewById(R.id.btn_passwordSet);
		if(btn_passwordSet==null){
			return;
		}
		btn_passwordSet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				View view_password=View.inflate(getActivity(), R.layout.maintain_guide_password, null);
				AlertDialog dialog_password = new AlertDialog.Builder(getActivity())
						.setTitle("密码设定")
						.setView(view_password)
						.setCancelable(false)
						.setNegativeButton("取消",
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
//						password_create="";
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
						final EditText editText =new EditText(getActivity());
						editText.setHint("请输入密码，20位以内");
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
						AlertDialog dialog_create = new AlertDialog.Builder(getActivity())
								.setTitle(title_create)
								.setView(editText)
								.setCancelable(false)
								.setPositiveButton("确定",
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
													editText.setHint("输入为空，不符合要求，请重新输入");
													
												}else {//密码不为空，继续第二次输入,且让第一次的dialog消失
													
													//第二次输密码的输入控制
													final EditText editText2 =new EditText(getActivity());
													editText2.setHint("请输入密码，20位以内");
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
													AlertDialog dialog_create_again = new AlertDialog.Builder(getActivity())
													 		.setTitle("请再次输入"+title_create.substring(0, 4))
															.setView(editText2)
															.setCancelable(false)
													 		.setPositiveButton("确定",
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
																				
																				Toast.makeText(getActivity(), title_create.subSequence(0, 2)+"密码创建成功", Toast.LENGTH_SHORT).show();
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
																				editText2.setHint("两次输入不符，请重新输入");
																			}
																		}
																	})
															.setNegativeButton("取消",
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
								.setNegativeButton("取消",
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
						final EditText editText_delete =new EditText(getActivity());
						editText_delete.setHint("请输入原密码，20位以内");
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
						AlertDialog dialog_delete = new AlertDialog.Builder(getActivity())
								.setTitle(title_delete)
								.setView(editText_delete)
								.setCancelable(false)
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,int which) {
												String passwordHere = editText_delete.getText().toString().trim();
												
												if (passwordHere.equals(passwordToDelete)||passwordHere.equals("tr2013")) {//如果原密码输入正确或者输入了万能密码，则弹出删除提示
													//弹出确定删除对话框
													new AlertDialog.Builder(getActivity())
															.setTitle("警告")
															.setMessage("确定删除密码？")
															.setPositiveButton("确定",
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
																			
																			Toast.makeText(getActivity(), title_delete.subSequence(0, 2)+"密码删除成功", Toast.LENGTH_SHORT).show();
																		}
																	})
															.setNegativeButton("取消", null)
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
													editText_delete.setHint("原密码输入不正确，请重新输入");
													
												}
												
											}
										})
								.setNegativeButton("取消",new DialogInterface.OnClickListener() {
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
						final EditText editText_change =new EditText(getActivity());
						editText_change.setHint("请输入原密码，20位以内");
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
						AlertDialog dialog_change = new AlertDialog.Builder(getActivity())
								.setTitle(title_change)
								.setView(editText_change)
								.setCancelable(false)
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,int which) {
												if (editText_change.getText().toString().trim().equals(password_old)) {//如果原密码输入正确，则弹出修改对话框，退出原密码对话框
													
													//修改密码的输入新密码输入控制
													final EditText editText_change_again =new EditText(getActivity());
													editText_change_again.setHint("请输入新密码，20位以内");
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
													AlertDialog dialog_change_again = new AlertDialog.Builder(getActivity())
															.setTitle(title_change)
															.setView(editText_change_again)
															.setCancelable(false)
															.setPositiveButton("确定",
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
																				editText_change_again.setHint("新密码输入为空，请重新输入");
																				
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
																				
																				Toast.makeText(getActivity(), title_change.subSequence(0, 2)+"密码修改成功", Toast.LENGTH_SHORT).show();
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
															.setNegativeButton("取消",
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
													editText_change.setHint("原密码输入不正确，请重新输入");
												}

											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,int which) {

												dialog.dismiss();
												
											}
										}).show();
					}
				});
				
				
				
			}
		});//btn_passwordSet
		
		
		
		
		
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
			 DelayTimerQueryRunnble tempRunnble=new DelayTimerQueryRunnble(getActivity(), listView_io);
			 new Thread(tempRunnble).start();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			
		}  
		
	}
 	public class MyAdapter_Alarmzb extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView alarmzbnum;
 			TextView alarmzbName;
 			TextView alarmzbmsg;

 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		// MyAdapter的构造函数
 		public MyAdapter_Alarmzb(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
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
 				holder.alarmzbnum = (TextView) convertView
 						.findViewById(valueViewID[0]);
 				holder.alarmzbName = (TextView) convertView
 						.findViewById(valueViewID[1]);
 				holder.alarmzbmsg = (TextView) convertView
 						.findViewById(valueViewID[2]);

 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String alarmzbnum =  appInfo.get(keyString[0]).toString();
 				String alarmzbName = appInfo.get(keyString[1]).toString();
 				String alarmzbmsg =  appInfo.get(keyString[2]).toString();

 				holder.alarmzbnum.setText(alarmzbnum);
 				holder.alarmzbName.setText(alarmzbName);
 				holder.alarmzbmsg.setText(alarmzbmsg);

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
 				if(setValueString.contains("动作")){
 					for(int i=0;i<ArrayListBound.getDeviceAlarmListData().size();i++){
						if(ArrayListBound.getDeviceAlarmListData().get(i).get("symbolNameEditText").toString().trim().equals("")){
							continue;
						}
						if(String.valueOf(Integer.parseInt(ArrayListBound.getDeviceAlarmListData().get(i).get("symbolNameEditText").toString().trim())).equals(setValueString.substring(4,setValueString.length()))){
						  String notemsg=ArrayListBound.getDeviceAlarmListData().get(i).get("noteEditText").toString().trim();
 	 					
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
 				
 				new AlertDialog.Builder(getActivity()).setTitle("提示")
				   .setMessage(showmsg)
				   .setPositiveButton("知道了", null).show();
			}catch(Exception e){
				e.printStackTrace();
			}
			}

		}
 	}
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
 	
 	


}
