package com.tr.parameter_setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiReadMassiveDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.DelayCount;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.explain.HexDecoding;
import com.explain.TableToBinary;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.maintainguide.Fragments_maintain.MyAdapter_Alarm;
import com.tr.maintainguide.Fragments_maintain.MyAdapter_Alarmzb;
import com.tr.maintainguide.Fragments_maintain.MyAdapter_IO;
import com.tr.maintainguide.Fragments_maintain.MyAdapter_Version;
import com.tr.programming.SemicolonTokenizer;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.text.InputFilter;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.KeyEvent;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Fragments_Other_Functions extends Fragment {
	private Button btn_getdata;
	//private Button btn_shortkey;//IO监视设定
	private Button btn_timeset;//时效设定
	private TextView showdata;
	String info = "";
	
	Button btn_da;
	Button btn_hzbm;
	ListView listView_da;
	MyAdapter_DA adapter_da;
	ArrayList<HashMap<String, Object>> list_da = ArrayListBound.getDAListData();
	private SendDataRunnable sendDataDARunnable;
	private WifiReadDataFormat formatReadDA;
	private  ChatListener DADataFeedback ;	
	private byte[] datemp=new byte[4*6];
	
	private Button clearbf;
	private WifiReadDataFormat bfformatReadMessage;
	private  ChatListener bfDataFeedback ;
	
	private EditText layoutAddress;
	private EditText layoutLength;
	private int address;
	private RadioGroup radioGroup_jz;
	private RadioButton rbtn_sl;
	private RadioButton rbtn_s;
/*	RadioGroup radioGroup_jz;
	RadioButton rbtn_sl;
	RadioButton rbtn_s;*/
	
	//ListView listView_shortkey;
	//MyAdapter_shortkey adapter_shortkey;
	//ArrayList<HashMap<String, Object>> list_shortkey = ArrayListBound.getshortkeyListData();
	private Thread mClockThread;
	private Handler handler;
	
	private String year;
	private String month;
	private String date;
	private String openFlag;
	private String type;
	private String para;
	
	private EditText edityear;
	private EditText editmonth;
	private EditText editdate;
	private CheckBox cb_openFlag;
	private Spinner spinner_type;
	private EditText editpara;
	private  static ArrayList<String> typeList = new ArrayList<String>();//存放类型，以适配spinner
	ArrayAdapter<String> typeList_Adapter;
	
	private WifiReadMassiveDataFormat formatReadMessage;
	private SendDataRunnable sendDataRunnable;
	private  ChatListener dataFeedback ;
	private byte[] getData;
	
	private ChatListener ioDataFeedback;
	protected WifiReadDataFormat formatReadio;
	protected SendDataRunnable sendDataioRunnable;
	
	private ChatListener timeDataFeedback;
	protected WifiReadDataFormat formatReadtime;
	protected SendDataRunnable sendDatatimeRunnable;
	//private static ArrayList<String> InputList_symbol_Table = new ArrayList<String>();
	//private static ArrayList<String> ActualInputList_symbol = new ArrayList<String>();// 需要初始化，不然空指针错误
	//private static ArrayList<String> ActualOutputList_symbol = new ArrayList<String>();
	//private static HashMap<Integer,String> deviceOffset1 = new HashMap<Integer,String>();
	//private static HashMap<String, Integer> deviceOffset2 = new HashMap<String, Integer>();
	
	private WifiSendDataFormat formatSendMessage;
	private FinishRunnable sendDataFinishRunnable;
	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("Fragments_Other_Functions onPause");
		alarmQueryRunnable.destroy();
		ledrunnable.destroy();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//list_shortkey = ArrayListBound.getshortkeyListData();
		list_da = ArrayListBound.getDAListData();
		System.out.println("Fragments_Other_Functions onResume");
		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
		Thread a2 = new Thread(alarmQueryRunnable);
		a2.start();
		ledrunnable=new ledRunnable(getActivity());
		Thread a4 = new Thread(ledrunnable);
		a4.start();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Fragments_Other_Functions onCreate");
		
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
					handler.sendMessage(m);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
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
					handler.sendMessage(m);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	//获取返回的数据后进行的UI操作
	private final Runnable readgetDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReadMessage.getLength()];
			//获取返回的数据，从第0位开始拷贝数据
			if( formatReadMessage.getFinalData() != null)  
			{
			System.arraycopy(formatReadMessage.getFinalData(), 0, getData, 0, formatReadMessage.getLength());
		    showdata = (TextView) getActivity().findViewById(R.id.showdata);
						if(showdata==null){
							return;
						}
						if(getData.length<=16){
							info = "";
							for (int i = 0; i < getData.length; i++) {
								if ((i + 1) % 8 == 0) {
									info = info+ getData[i]+ "                  ";
								} else if (((i + 1) % 4 == 0)) {
									info = info+ getData[i]+ "         ";
								} else {
									info = info+ getData[i]+ "   ";
								}

							}	  
							mClockThread=new LooperThread();
							mClockThread.start();
							 }else{
								 Constans.getdata.createFolder("/mnt/sdcard/", "getdata").createFile("test.txt");
							     Constans.getdata.writeFileFromByte("test.txt", getData);
							     System.out.println("数据量大，已成功保存到SD卡");
							     mClockThread=new cLooperThread();
								 mClockThread.start();
						}
				
		}
		}

	};
	
/*	//获取返回的数据后进行的UI操作
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
			for(int i=0;i<formatReadio.getLength()/6;i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("number", i+1);
			    for(int j=1;j<=3;j++){
			    	String getname=deviceOffset1.get(HexDecoding.Array2Toint(getData, 6*i+2*(j-1)));
			    	if(getname==null){
			    		map.put("name"+j,"");
			    	}else{
			    	    map.put("name"+j,getname);
			    	}
			    }
			   // list_shortkey.set(i, map);
			    list_shortkey.add(map);
			}

				adapter_shortkey= new MyAdapter_shortkey(getActivity(), list_shortkey,
						R.layout.parameter_setting_other_functions_shortkey_item,
						new String[] { "number", "name1", "name2", "name3"},
						new int[] { R.id.number, R.id.name1, R.id.name2, R.id.name3});
				listView_shortkey.setAdapter(adapter_shortkey);
		}
		}

	};*/
	//获取返回的数据后进行的UI操作
	private final Runnable readtimeDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReadtime.getLength()];
			//获取返回的数据，从第0位开始拷贝数据
			if( formatReadtime.getFinalData() != null)  
			{
			System.arraycopy(formatReadtime.getFinalData(), 0, getData, 0, formatReadtime.getLength());
			//重新给列表赋值
			year=String.valueOf(HexDecoding.Array2Toint(getData, 0));
			month=String.valueOf((int)(getData[2]&0xff));
			date=String.valueOf((int)(getData[3]&0xff));
			openFlag=String.valueOf((int)(getData[4]&0xff));
			type=String.valueOf((int)(getData[5]&0xff));
			para=String.valueOf(HexDecoding.Array2Toint(getData, 6));
			
			Runnable uiShowRunnable=new Runnable() {
				public void run() {
			typeList.clear();
			typeList.add("1");typeList.add("2");typeList.add("3");typeList.add("4");typeList.add("5");
			typeList_Adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,typeList);
			typeList_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_type.setAdapter(typeList_Adapter);
			
			edityear.setText(year);
			if(year!=null){
			edityear.setSelection(year.length());// 设置光标位置
			}
			editmonth.setText(month);
			if(month!=null){
			editmonth.setSelection(month.length());// 设置光标位置
			}
			editdate.setText(date);
			if(date!=null){
			editdate.setSelection(date.length());// 设置光标位置
			}
			if(openFlag==null)
			{openFlag="1";}
			if(openFlag.equals("1")){
				cb_openFlag.setChecked(true);
			}else{
				cb_openFlag.setChecked(false);
			}
			if(type==null||Integer.valueOf(type)<1||Integer.valueOf(type)>5)
			{type="1";}
			spinner_type.setSelection(Integer.valueOf(type)-1); 
			editpara.setText(para);
			if(para!=null){
			editpara.setSelection(para.length());// 设置光标位置
			}
				}
			};
			getActivity().runOnUiThread(uiShowRunnable);
				
		}
		}

	};
	//获取返回的数据后进行的UI操作

	private final Runnable readDADataFinishTodo = new Runnable(){
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//对于返回的36字节
		//发送正确且完成
		//处理返回的数据	
		getData=new byte[formatReadDA.getLength()];
		//获取返回的数据，从第八位开始拷贝数据
		if( formatReadDA.getFinalData() != null)  
		{
		System.arraycopy(formatReadDA.getFinalData(), 0, getData, 0, formatReadDA.getLength());
		//重新给list_usermode列表赋值

		int da1address = HexDecoding.Array4Toint(getData, 0);
		int da1database = HexDecoding.Array4Toint(getData, 4);
		int da1bs = HexDecoding.Array4Toint(getData, 8);
		int da2address = HexDecoding.Array4Toint(getData, 12);
		int da2database = HexDecoding.Array4Toint(getData, 16);
		int da2bs = HexDecoding.Array4Toint(getData, 20);
		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("da", "DA通道1");
		map0.put("da_text","");
		list_da.set(0, map0);
		
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("da", "数据地址（0x）：");
		map1.put("da_text",String.valueOf(da1address));
		list_da.set(1, map1);
		
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("da", "数据基准值：");
		map2.put("da_text",String.valueOf(da1database));
		list_da.set(2, map2);
		
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("da", "倍数：");
		map3.put("da_text",String.valueOf(da1bs));
		list_da.set(3, map3);
		
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("da", "DA通道2");
		map4.put("da_text","");
		list_da.set(4, map4);
		
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("da", "数据地址（0x）：");
		map5.put("da_text",String.valueOf(da2address));
		list_da.set(5, map5);
		
		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("da", "数据基准值：");
		map6.put("da_text",String.valueOf(da2database));
		list_da.set(6, map6);
		
		HashMap<String, Object> map7 = new HashMap<String, Object>();
		map7.put("da", "倍数：");
		map7.put("da_text",String.valueOf(da2bs));
		list_da.set(7, map7);
		    adapter_da= new MyAdapter_DA(getActivity(), list_da,
					R.layout.maintainguide_maintain_da_item,
					new String[] { "da", "da_text"},
					new int[] { R.id.name, R.id.editname});
			listView_da.setAdapter(adapter_da);
	}
	}

};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Fragments_Other_Functions onCreateView");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.tab_parameter_setting_other_functions,
				container, false);

	}
	@Override
	public void onStart(){
		System.out.println("Fragments_Other_Functions onStart");
		super.onStart();
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		System.out.println("Fragments_Other_Functions onActivityCreated");
		handler=new Handler()
		{
			public void handleMessage(Message msg)
			{
				if(msg.what==0){
				  showdata.setText(info.toString());	
				}else{
					Toast.makeText(getActivity(), "数据量大，已成功保存到SD卡",Toast.LENGTH_LONG).show();
				}
				super.handleMessage(msg);
			}
		};
		//提取数据
		btn_getdata = (Button) getActivity().findViewById(R.id.getdata);
		if (btn_getdata == null) {
			return;
		}
		btn_getdata.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				
				final View view_getdata = View.inflate(getActivity(),
						R.layout.parameter_setting_other_functions, null);
				final AlertDialog dialog_password = new AlertDialog.Builder(
						getActivity())
						.setTitle("提取数据")
						.setView(view_getdata)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int which) {
										if(layoutAddress.getText().toString().equals("")){
											Toast.makeText(getActivity(), "地址为空，请重新输入", Toast.LENGTH_SHORT).show();
											return;
										}
										if(layoutLength.getText().toString().equals("")){
											Toast.makeText(getActivity(), "长度为空，请重新输入", Toast.LENGTH_SHORT).show();
											return;
										}
										try {
										if (rbtn_sl.isChecked()) {
											if(layoutAddress.getText().toString().length()!=8){
												Toast.makeText(getActivity(), "地址的长度有误，请重新输入", Toast.LENGTH_SHORT).show();
												return;
											}
											int j = 0, k = 0;
											String valuestr="";
											int data = 0;
											for (int i = layoutAddress.getText().toString().length()-1; i >=0; i--) {
												if(k<8){
													valuestr=layoutAddress.getText().toString().substring(i,i + 1);
													if(valuestr.equalsIgnoreCase("A")){
														j = 10;
													}else if(valuestr.equalsIgnoreCase("B")){
														j = 11;
													}else if(valuestr.equalsIgnoreCase("C")){
														j = 12;
													}else if(valuestr.equalsIgnoreCase("D")){
														j = 13;
													}else if(valuestr.equalsIgnoreCase("E")){
														j = 14;
													}else if(valuestr.equalsIgnoreCase("F")){
														j = 15;
													}else{
														j = Integer.valueOf(valuestr);
													}
												    data = data+ (int) (j * Math.pow(16, k));
												}
												k++;
											}
											address = data;
										} else {
											address = Integer.parseInt(layoutAddress.getText().toString());
										}
										System.out.println("address="+ address);
										System.out.println("length="+layoutLength.getText().toString());
										formatReadMessage = new WifiReadMassiveDataFormat(address, Integer.parseInt(layoutLength.getText().toString()));
										
											sendDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());

											sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readgetDataFinishTodo);

											sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

											getActivity().runOnUiThread(sendDataRunnable);
											} catch (Exception e) {
											// TODO: handle exception
												Toast.makeText(getActivity(), "数据有误，请重新输入", Toast.LENGTH_SHORT).show();
										}
									}
								}).setNegativeButton("取消",null).show();
				radioGroup_jz=(RadioGroup) dialog_password.findViewById(R.id.radioGroup_jz);
				rbtn_sl=(RadioButton) dialog_password.findViewById(R.id.radio_sl);
				rbtn_s=(RadioButton) dialog_password.findViewById(R.id.radio_s);
				if(radioGroup_jz==null){return;}
				if(rbtn_sl==null){return;}
				if(rbtn_s==null){return;}
				
				layoutAddress = (EditText) dialog_password.findViewById(R.id.editText_address);
				if (layoutAddress == null) {
					return;
				}
				layoutLength = (EditText) dialog_password.findViewById(R.id.editText_length);
				if (layoutLength == null) {
					return;
				}
				layoutAddress.setHint("输入8位十六进制的数据");
				layoutLength.setHint("输入十进制的数据");
				radioGroup_jz.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if(checkedId==R.id.radio_sl){
							layoutAddress.setHint("输入8位十六进制的数据");
							layoutAddress.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
							layoutAddress.setKeyListener(new NumberKeyListener() {
								@Override
								protected char[] getAcceptedChars() {
									return new char[] { '1', '2', '3', '4', '5', '6', '7',
											'8', '9', '0', 'A','B','C','D','E','F',
											'a','b','c','d','e','f'};
								}

								@Override
								public int getInputType() {
									return android.text.InputType.TYPE_CLASS_NUMBER;// 数字键盘
								}
							});
						}else{
							layoutAddress.setHint("输入十进制的数据");
							layoutAddress.setKeyListener(new NumberKeyListener() {
								@Override
								protected char[] getAcceptedChars() {
									return new char[] { '1', '2', '3', '4', '5', '6', '7',
											'8', '9', '0'};
								}

								@Override
								public int getInputType() {
									return android.text.InputType.TYPE_CLASS_NUMBER;// 数字键盘
								}
							});
						}
					}
				});
				return;
			

		}
		});
		
		//时效设定
		btn_timeset = (Button) getActivity().findViewById(R.id.timeset);
		if (btn_timeset == null) {
			return;
		}
		btn_timeset.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				View view_timeset=View.inflate(getActivity(), R.layout.parameter_setting_other_functions_timeset, null);
				AlertDialog dialog_timeset=new AlertDialog.Builder(getActivity())
				.setTitle("时效设定").setView(view_timeset)
				.setPositiveButton("设定",
	            new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) {
						byte[] temp=new byte[8];
						try {
						year=edityear.getText().toString();
						month=editmonth.getText().toString();
						date=editdate.getText().toString();
						if(cb_openFlag.isChecked()){
							openFlag="1";
						}else{
							openFlag="0";
						}
						type=spinner_type.getSelectedItem().toString();
						para=editpara.getText().toString();
						if(year==null||year.equals("")){Toast.makeText(getActivity(), "年为空，请重新输入", Toast.LENGTH_SHORT).show();return;}
						if(month==null||month.equals("")){Toast.makeText(getActivity(), "月为空，请重新输入", Toast.LENGTH_SHORT).show();return;}
						if(date==null||date.equals("")){Toast.makeText(getActivity(), "日为空，请重新输入", Toast.LENGTH_SHORT).show();return;}
						if(para==null||para.equals("")){Toast.makeText(getActivity(), "解除参数为空，请重新输入", Toast.LENGTH_SHORT).show();return;}
						System.arraycopy(HexDecoding.int2byteArray2(Integer.valueOf(year)), 0, temp,0, 2);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(month)), 0, temp,2, 1);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(date)), 0, temp,3, 1);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(openFlag)), 0, temp,4, 1);
						System.arraycopy(HexDecoding.int2byte(Integer.valueOf(type)), 0, temp,5, 1);
						System.arraycopy(HexDecoding.int2byteArray2(Integer.valueOf(para)), 0, temp,6, 2);
						  formatSendMessage=new WifiSendDataFormat(temp, 0x400241F8);
	                        
	                        	sendDataRunnable=new SendDataRunnable(formatSendMessage, getActivity());

								sendDataFinishRunnable=new FinishRunnable(getActivity(), "时效设定信息成功发送给下位机");

								sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

								getActivity().runOnUiThread(sendDataRunnable);
								} catch (Exception e) {
								// TODO: handle exception
								  e.printStackTrace();
								  Toast.makeText(getActivity(), "数据有误，请重新输入", Toast.LENGTH_SHORT).show();
							    }
				    }
				}).setNegativeButton("取消",null).show();
				        edityear=(EditText)dialog_timeset.findViewById(R.id.editText_year);
						editmonth=(EditText)dialog_timeset.findViewById(R.id.editText_month);
						editdate=(EditText)dialog_timeset.findViewById(R.id.editText_date);
						cb_openFlag=(CheckBox)dialog_timeset.findViewById(R.id.radio_timeset);
						spinner_type=(Spinner)dialog_timeset.findViewById(R.id.spinner_type);
						editpara=(EditText)dialog_timeset.findViewById(R.id.editText_para);
						if(edityear==null){return;}
						if(editmonth==null){return;}
						if(editdate==null){return;}
						if(cb_openFlag==null){return;}
						if(spinner_type==null){return;}
						if(editpara==null){return;}
						if (WifiSetting_Info.mClient == null) {
							Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
							return;
						}
						formatReadtime = new WifiReadDataFormat(0x400241F8, 8);
						try {
							sendDatatimeRunnable=new SendDataRunnable(formatReadtime, getActivity());

							sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readtimeDataFinishTodo);

							sendDatatimeRunnable.setOnlistener(new NormalChatListenner(sendDatatimeRunnable, sendDataFinishRunnable));

							getActivity().runOnUiThread(sendDatatimeRunnable);

						} catch (Exception e) {
							// TODO: handle exception
							Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
						}
						
						
			}
		});
		
		btn_hzbm=(Button) getActivity().findViewById(R.id.drzk);
		if(btn_hzbm==null){
			return;
		}
		btn_hzbm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog dialog_da=new AlertDialog.Builder(getActivity())
				.setTitle("提示").setMessage("确定导入字库")
				.setPositiveButton("确定",
	            new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) {
						if (WifiSetting_Info.mClient!=null) {
			                   try{
			                	Constans.mouldData.createFolder(Constans.trPATH, "字库").createFile("hzdot.bin");
							    byte[] byteArray=Constans.mouldData.readFileToByte("hzdot.bin");
							    WifiSetting_Info.wifiTimeOut=System.currentTimeMillis();
								WifiSetting_Info.progressDialog= ProgressDialog.show(getActivity(),"导入字库中", "请等待", true, false);
								formatSendMessage=new WifiSendDataFormat(byteArray, 0x080A0000);
							    sendDataRunnable=new SendDataRunnable(formatSendMessage, getActivity());
								sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable,new FinishRunnable(getActivity(),"字库导入成功",WifiSetting_Info.progressDialog)));
								getActivity().runOnUiThread(sendDataRunnable);
			           		    }catch (Exception e) {
			        			// TODO: handle exception
			        			e.printStackTrace();
			        			if(WifiSetting_Info.progressDialog!=null){
			            			WifiSetting_Info.progressDialog.dismiss();
			            			new AlertDialog.Builder(getActivity()).setTitle("提示")
									   .setMessage("程序异常，请重操作！")
									   .setPositiveButton("确定", null).show();
			            		}
			        		    }
			                   WifiSetting_Info.progressDialog.setOnDismissListener(new OnDismissListener() { 
									@Override
									public void onDismiss(DialogInterface arg0) {
										// TODO Auto-generated method stub
										 if(WifiSetting_Info.mClient==null){
											 new AlertDialog.Builder(getActivity()).setTitle("提示")
											   .setMessage("网络异常，通讯中断！请检查网络！")
											   .setPositiveButton("确定", null).show();
											}
										 WifiSetting_Info.progressDialog=null;
									}  
						        });
							}else{
									Toast.makeText(getActivity(), "请先连接主机", Toast.LENGTH_SHORT).show();
								}
					}
				}).setNegativeButton("取消",null).show();
				
			}
		});	
		btn_da=(Button) getActivity().findViewById(R.id.dajs);
		if(btn_da==null){
			return;
		}
		btn_da.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				View view_da=View.inflate(getActivity(), R.layout.maintainguide_maintain_da, null);
				AlertDialog dialog_da=new AlertDialog.Builder(getActivity())
				.setTitle("DA设定窗口").setView(view_da)
				.setPositiveButton("设定",
	            new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) {
						  try {
							for(int i=1;i<=7;i++){
							if(i==4) continue;
							if(((Map)listView_da.getItemAtPosition(i)).get("da_text").toString().equals("")){
								Toast.makeText(getActivity(), "数据为空，请重新输入", Toast.LENGTH_SHORT).show();
								return;
							}
						}
						System.arraycopy(HexDecoding.int2byteArray4(Integer.parseInt(((Map)listView_da.getItemAtPosition(1)).get("da_text").toString())), 0, datemp, 0, 4);
						System.arraycopy(HexDecoding.int2byteArray4(Integer.parseInt(((Map)listView_da.getItemAtPosition(2)).get("da_text").toString())), 0, datemp, 4, 4);
						System.arraycopy(HexDecoding.int2byteArray4(Integer.parseInt(((Map)listView_da.getItemAtPosition(3)).get("da_text").toString())), 0, datemp, 8, 4);
						System.arraycopy(HexDecoding.int2byteArray4(Integer.parseInt(((Map)listView_da.getItemAtPosition(5)).get("da_text").toString())), 0, datemp, 12, 4);
						System.arraycopy(HexDecoding.int2byteArray4(Integer.parseInt(((Map)listView_da.getItemAtPosition(6)).get("da_text").toString())), 0, datemp, 16, 4);
						System.arraycopy(HexDecoding.int2byteArray4(Integer.parseInt(((Map)listView_da.getItemAtPosition(7)).get("da_text").toString())), 0, datemp, 20, 4);
					  formatSendMessage=new WifiSendDataFormat(datemp, 0x40024A00);
                      
                        	sendDataRunnable=new SendDataRunnable(formatSendMessage, getActivity());

							sendDataFinishRunnable=new FinishRunnable(getActivity(), "DA监视信息成功发送给下位机");

							sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

							getActivity().runOnUiThread(sendDataRunnable);
							
							} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							Toast.makeText(getActivity(), "数据有误，请重新输入", Toast.LENGTH_SHORT).show();
						    }
					}
				}).setNegativeButton("取消",null).show();
			    listView_da = (ListView) dialog_da.findViewById(R.id.listView_da);
				if(listView_da==null){
					return;
				}
				if (WifiSetting_Info.mClient == null) {
					Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				formatReadDA = new WifiReadDataFormat(0x40024A00, 4*6);
				try {
					sendDataDARunnable=new SendDataRunnable(formatReadDA, getActivity());

					sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readDADataFinishTodo);

					sendDataDARunnable.setOnlistener(new NormalChatListenner(sendDataDARunnable, sendDataFinishRunnable));

					getActivity().runOnUiThread(sendDataDARunnable);
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
				}
				
				
			}
		});	
		//备份清除
		bfDataFeedback = new ChatListener() {
			@Override
			public void onChat(byte[] message) {

				//返回的标志位STS1处的判断以及和校验
				bfformatReadMessage.backMessageCheck(message);
				if(!bfformatReadMessage.finishStatus()){
					getActivity().runOnUiThread(sendDataRunnable);			
				}else {
					//发送正确且完成
					//处理返回的数据	
					getData=new byte[bfformatReadMessage.getLength()];
					//获取返回的数据，从第八位开始拷贝数据
					System.arraycopy(message, 8, getData, 0, bfformatReadMessage.getLength());
					// TODO Auto-generated method stub
					WifiSetting_Info.blockFlag=true;
						}
					}
			
		};
		clearbf=(Button) getActivity().findViewById(R.id.bfqc);
		if(clearbf==null){
			return;
		}
		clearbf.setOnClickListener(new OnClickListener()  {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity())
				.setTitle("备份清除").setMessage("是否确定清除备份？")
				.setPositiveButton("确定",
	            new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int which) {

						bfformatReadMessage=new WifiReadDataFormat(0x40024200,8);
						try {
							if (WifiSetting_Info.mClient == null) {
								Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
								return;
							}
							sendDataRunnable = new SendDataRunnable(bfDataFeedback,bfformatReadMessage,
									getActivity());
							getActivity().runOnUiThread(sendDataRunnable);
							
						} catch (Exception e) {
							Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
							
						}
						final EditText etEditText = new EditText(getActivity());
						// 限制只能输入0~9的数字和点号
						etEditText.setKeyListener(new NumberKeyListener() {
							@Override
							protected char[] getAcceptedChars() {
								return new char[] { '1', '2', '3', '4', '5', '6', '7',
										'8', '9', '0'};
							}

							@Override
							public int getInputType() {
								return android.text.InputType.TYPE_CLASS_NUMBER;// 数字键盘
							}
						});
						etEditText.setHint("请输入数字");
						new AlertDialog.Builder(getActivity())
						.setTitle("请输入密码")
						.setView(etEditText)
						.setPositiveButton(R.string.OK,//确定
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										
										String editString = etEditText
												.getText().toString().trim();
										int password=0;
										try{
									      for(int i=7;i>=0;i--){
									    	  int value=(int)(getData[i]&0xff);
												//password=password+value<<(8*(7-i));	
												password=password+value<<(8*i);	
											 }
										  //if(Integer.parseInt(editString)==password){
										  if(editString.trim().equals("2012")){
									    	  KeyCodeSend.send(9999, getActivity());
									      }else{
									    	  Toast.makeText(getActivity(), "密码错误", Toast.LENGTH_SHORT).show();
									      }
									     
									      System.out.println("password======"+password);
										}catch(Exception e)
											{
												e.printStackTrace();
												Toast.makeText(getActivity(), "数据有误，请重新输入", Toast.LENGTH_SHORT).show();
											}
										
									}

								}).show();
					}
				}).setNegativeButton("取消",null).show();
				
			}
		});
		return;
	}
	

 	public class MyAdapter_DA extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView da;
 			TextView da_text;
 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		// MyAdapter的构造函数
 		public MyAdapter_DA(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
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
 				holder.da = (TextView) convertView
 						.findViewById(valueViewID[0]);
 				holder.da_text = (TextView) convertView
 						.findViewById(valueViewID[1]);

 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String da = appInfo.get(keyString[0]).toString();
 				String da_text = appInfo.get(keyString[1]).toString();

 				holder.da.setText(da);
 				holder.da_text.setText(da_text);
 				if(position==1||position==2||position==3||position==5||position==6||position==7)
 				  holder.da_text.setOnClickListener(new datextListener(position));
 			}
 			return convertView;
 		}
 		class datextListener implements android.view.View.OnClickListener {
			private int position;

			datextListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
				final EditText etEditText = new EditText(getActivity());
				etEditText.setKeyListener(new NumberKeyListener() {
 				    @Override
 				    protected char[] getAcceptedChars() {
 				        return new char[] { '1', '2', '3', '4', '5', '6', '7', '8','9', '0' };
 				    }
 				    @Override
 				    public int getInputType() {
 				        return android.text.InputType.TYPE_CLASS_PHONE;//数字键盘
 				    }
 				});
				String daString=mAppList.get(position).get(
						keyString[1]).toString();
				etEditText.setText(daString);
				etEditText.setSelection(daString.length());// 设置光标位置
				new AlertDialog.Builder(getActivity())
						.setTitle("请添加值")
						.setView(etEditText)
						.setPositiveButton(R.string.OK,//确定
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										
										String editString = etEditText
												.getText().toString().trim();
										if (editString.equals("")) {
											Toast.makeText(getActivity(), "数据为空，请重新输入", Toast.LENGTH_SHORT).show();
										    return;
										}
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("da",
												mAppList.get(position)
														.get(keyString[0])
														.toString());
										map.put("da_text",
												editString);
										mAppList.set(position, map);
										notifyDataSetChanged();
									}

								}).setNegativeButton(R.string.CANCEL, null)//取消
								.show();

				notifyDataSetChanged();
			}

		}
 	}
}
