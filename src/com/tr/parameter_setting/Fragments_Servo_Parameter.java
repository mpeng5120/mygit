package com.tr.parameter_setting;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.DelayCount;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

import com.dataInAddress.AddressPublic;
import com.dataInAddress.Machinedata;
import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.explain.HexDecoding;
import com.explain.ServoDataGenerate;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.programming.TR_Programming_Activity;
import com.wifiexchange.ChatListener;
import com.wifiexchange.Client;
import com.wifiexchange.ServerUtils;
import com.wifiexchange.WifiSetting_Info;

import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Fragments_Servo_Parameter extends Fragment {
	ListView listView_Servo_Parameter;
	MyAdapter Servo_Parameter_Adapter;
	Spinner spinner_servo;
	Button button_saveMain;
	Button button_EEPROM;
	ArrayAdapter<String> axleSpinnerAdapter;
	private static String selectedItem_Servo;
	ArrayList<String> axles = new ArrayList<String>();
	ArrayList<HashMap<String, Object>> parameter_setting_servo_list;
	//= ArrayListBound.getServoParameterListData();
	public static ArrayList<HashMap<String, Object>> read_arraylist_servo = new ArrayList<HashMap<String, Object>>();

	// 发送数据封装类
	private WifiSendDataFormat formatSendMessage;
	private WifiReadDataFormat formatReadMessage;

	// servodata数据生成
	ServoDataGenerate mGenerate;
	Machinedata mccMachinedata;

	// ui改变保存数据
	private int uiChangePosition;
	private byte[] getData;

	private SendDataRunnable servoDataSend;
	private FinishRunnable sendDataFinish;

	private SendDataRunnable servoDataSend1;
	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;
	private  ChatListener DataFeedback ;
	private SendDataRunnable sendDataRunnable;
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("Fragments_Servo_Parameter onPause");
		alarmQueryRunnable.destroy();
		ledrunnable.destroy();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("Fragments_Servo_Parameter onResume");
		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
		Thread a2 = new Thread(alarmQueryRunnable);
		a2.start();
		ledrunnable=new ledRunnable(getActivity());
		Thread a4 = new Thread(ledrunnable);
		a4.start();
	}
	/** 读取伺服参数时的通信线程在写完要读的内容后跳转到读对应地址的函数 */
	private final ChatListener servoReadJump = new ChatListener() {
		@Override
		public void onChat(byte[] message) {
			// 返回的标志位STS1处的判断以及和校验
			formatSendMessage.backMessageCheck(message);

			if (!formatSendMessage.finishStatus()) {
				getActivity().runOnUiThread(servoDataSend);
			} else {
				try {
					Thread.sleep(30);

					servoDataSend1 = new SendDataRunnable(
							servoReadDataFeedback, formatReadMessage,
							getActivity());

					getActivity().runOnUiThread(servoDataSend1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 写完数据之后开始读数据

			}
		}
	};

	/** 读取伺服参数时的通信线程收到信息时的处理函数. */
	private final ChatListener servoReadDataFeedback = new ChatListener() {
		@Override
		public void onChat(byte[] message) {

			// 返回的标志位STS1处的判断以及和校验
			formatReadMessage.backMessageCheck(message);
			if (!formatReadMessage.finishStatus()) {
				getActivity().runOnUiThread(servoDataSend1);
			} else {
				// 发送正确且完成
				// 处理返回的数据
				System.out.println("WifiSetting_Info.blockFlag = "+WifiSetting_Info.blockFlag );
				WifiSetting_Info.blockFlag = true;
				getData = new byte[formatReadMessage.getLength()];
				// 获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(message, 8, getData, 0,
						formatReadMessage.getLength());
				// 对所收集到到的数据进行处理
				getActivity().runOnUiThread(backMessageTodo);

				// WifiSetting_Info.blockFlag=true;
			}
		}
	};

	// 获取返回的数据后进行的UI操作
	private final Runnable backMessageTodo = new Runnable() {
		@Override
		public void run() {

			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("numText",
					Servo_Parameter_Adapter.getmAppList().get(uiChangePosition)
							.get(Servo_Parameter_Adapter.getkeyString()[0])
							.toString());//
			map.put("contentText",
					Servo_Parameter_Adapter.getmAppList().get(uiChangePosition)
							.get(Servo_Parameter_Adapter.getkeyString()[1])
							.toString());

			// 这里根据返回的值更新修改显示
			map.put("currentValueText", HexDecoding.Array4Toint(getData));

			map.put("setValueText",
					Servo_Parameter_Adapter.getmAppList().get(uiChangePosition)
							.get(Servo_Parameter_Adapter.getkeyString()[3])
							.toString());

			map.put("noteText",
					Servo_Parameter_Adapter.getmAppList().get(uiChangePosition)
							.get(Servo_Parameter_Adapter.getkeyString()[4])
							.toString());
			Servo_Parameter_Adapter.getmAppList().set(uiChangePosition, map);
			// 更新界面
			Servo_Parameter_Adapter.notifyDataSetChanged();

			Toast.makeText(getActivity(), "数据已更新", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Fragments_Servo_Parameter onCreate");
		DataFeedback = new ChatListener() {
			@Override
			public void onChat(byte[] message) {

				//返回的标志位STS1处的判断以及和校验
				formatReadMessage.backMessageCheck(message);
				if(!formatReadMessage.finishStatus()){
					getActivity().runOnUiThread(sendDataRunnable);			
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
								TR_Parameter_Setting_Activity.seekbar.setProgress(0);
							}
							if(HexDecoding.Array2Toint(getData)==2){
								Constans.currentSeekbarValue=125;
								TR_Parameter_Setting_Activity.seekbar.setProgress(125);
								}
							if(HexDecoding.Array2Toint(getData)==3){
								Constans.currentSeekbarValue=250;
								TR_Parameter_Setting_Activity.seekbar.setProgress(250);
								}
							if(HexDecoding.Array2Toint(getData)==4){
								Constans.currentSeekbarValue=375;
								TR_Parameter_Setting_Activity.seekbar.setProgress(375);
								}
							if(HexDecoding.Array2Toint(getData)==5){
								Constans.currentSeekbarValue=500;
								TR_Parameter_Setting_Activity.seekbar.setProgress(500);
								}
						}
					}
			
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Fragments_Servo_Parameter onCreateView");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.tab_parameter_setting_servo,
				container, false);
		

	}
	/*****
	 * wifi连接失败
	 */
	private Runnable errorRunnable=new Runnable() {
		@Override
		public void run() {
			WifiSetting_Info.blockFlag=false;
			Toast.makeText(getActivity(),R.string.T_wificonnfail,Toast.LENGTH_LONG).show();
		}
	};
	
	/***
	 * 连接成功
	 */
	private Runnable successRunnable=new Runnable() {
		@Override
		public void run() {
			if(TR_Parameter_Setting_Activity.wifi_led!=null){
	        	TR_Parameter_Setting_Activity.wifi_led.setVisibility(View.VISIBLE);
	        }
			Toast.makeText(getActivity(),R.string.T_wificonnsuccess,Toast.LENGTH_SHORT).show();
			formatReadMessage=new WifiReadDataFormat(AddressPublic.model_allspeed,2);
			try {
				sendDataRunnable = new SendDataRunnable(DataFeedback, formatReadMessage,
						getActivity());
				getActivity().runOnUiThread(sendDataRunnable);
			} catch (Exception e) {
				Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
			}
		}
		
	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("Fragments_Servo_Parameter onActivityCreated");
		
		parameter_setting_servo_list= ArrayListBound.getServoParameterListData();
		spinner_servo = (Spinner) getActivity().findViewById(R.id.spinner_servo);
		if (spinner_servo == null) {
			return;
		}
		button_EEPROM=(Button)getActivity().findViewById(R.id.button_eeprom);
		if(button_EEPROM==null){
			return;
		}
		button_saveMain = (Button) getActivity().findViewById(
				R.id.button_saveMain_servoPara);
		if (button_saveMain == null) {
			return;
		}
		listView_Servo_Parameter = (ListView) getActivity().findViewById(
				R.id.listView_servopara);
		if (listView_Servo_Parameter == null) {
			return;
		}
		System.out.println("Fragments_Servo_Parameter onActivityCreated sm="+Fragments_Mechanical_Parameter.selectedItem);
		/*selectedItem_Servo=Fragments_Mechanical_Parameter.selectedItem;
		if (read_arraylist_servo.isEmpty()) {
			read_arraylist_servo = read_servo_Parameter();
		}*/
		if (read_arraylist_servo.isEmpty()||!selectedItem_Servo.equals(Fragments_Mechanical_Parameter.selectedItem)) {
			selectedItem_Servo=Fragments_Mechanical_Parameter.selectedItem;
			read_arraylist_servo = read_servo_Parameter();
		}
		selectedItem_Servo=Fragments_Mechanical_Parameter.selectedItem;
		
		Servo_Parameter_Adapter = new MyAdapter(
				getActivity(),
				parameter_setting_servo_list,
				R.layout.parameter_setting_servo_item,
				new String[] { "numText", "contentText", "currentValueText",
						"setValueText", "noteText", "readButton", "writeButton" },
				new int[] { R.id.numText, R.id.contentText,
						R.id.currentValueText, R.id.setValueText,
						R.id.noteText, R.id.readButton, R.id.writeButton });
		listView_Servo_Parameter.setAdapter(Servo_Parameter_Adapter);

		if (axles.isEmpty()) {
			// axles.add("请选择轴");
			axles.add("轴1");
			axles.add("轴2");
			axles.add("轴3");
			axles.add("轴4");
			axles.add("轴5");
			axles.add("轴6");
			axles.add("轴7");
			axles.add("轴8");
		} else {

		}

		axleSpinnerAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, axles);
		axleSpinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_servo.setAdapter(axleSpinnerAdapter);
		/**
		 * 选择不同轴显示不同轴信息
		 */
		spinner_servo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (arg2 == 0) {
					show("1轴");
				} else if (arg2 == 1) {
					show("2轴");
				} else if (arg2 == 2) {
					show("3轴");
				} else if (arg2 == 3) {
					show("4轴");
				} else if (arg2 == 4) {
					show("5轴");
				} else if (arg2 == 5) {
					show("6轴");
				} else if (arg2 == 6) {
					show("7轴");
				} else if (arg2 == 7) {
					show("8轴");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		button_EEPROM.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final int[] overflag=new int[]{0,0};
				if(WifiSetting_Info.mClient==null){
						if(ServerUtils.isUsingWifi(getActivity())){
							new AlertDialog.Builder(getActivity())
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
												getActivity().runOnUiThread(successRunnable);
											} catch (IOException e) {
												e.printStackTrace();
												getActivity().runOnUiThread(errorRunnable);
											}
										}
									}.start();
								}
							}).setNegativeButton("取消",null).show();
						}else{
							Toast.makeText(getActivity(), "网络异常，通讯中断！请检查网络！", Toast.LENGTH_SHORT).show();
						}
					
					return;
				}
				WifiSetting_Info.wifiTimeOut=System.currentTimeMillis();
				 WifiSetting_Info.progressDialog = ProgressDialog.show(getActivity(),"写入伺服EEPROM中", "请等待", true, false);
			       new Thread()
		              {
		                  public void run()
		                  {
		                	  if(WifiSetting_Info.mClient!=null){
		                		for(int i=0;i<=72;i++){
						int address=0;	
						byte[] temp;
						// 获取轴数信息
						int axis = Integer.valueOf(spinner_servo.getSelectedItem()
								.toString().substring(1));
						if(i==72){
							temp=HexDecoding.int2byteArray4(1);
							address = address+4;
						}else{
							// 获得设定值，并转化为字节数组
							temp = HexDecoding.int2byteArray4(Integer
									.parseInt(parameter_setting_servo_list.get(i).get("setValueText")
											.toString()));
							address = AddressPublic.machineRAMData_Head
									+ 1024
									* (axis - 1)
									+ 4
									* (Integer.valueOf(parameter_setting_servo_list.get(i).get("numText")
											.toString()) - 1);
						}
						// 获取行数
						// Integer.parseInt(mAppList.get(position).get(keyString[0]).toString());
						// 计算应当发送的地址

						

						formatSendMessage = new WifiSendDataFormat(temp, address);

						try {
							servoDataSend = new SendDataRunnable(formatSendMessage,getActivity(),1);
							if(WifiSetting_Info.mClient==null){
								if(WifiSetting_Info.progressDialog!=null){
		                			WifiSetting_Info.progressDialog.dismiss();
		                			WifiSetting_Info.progressDialog=null;
		                		}
								return;
							}
							sendDataFinish = new FinishRunnable(getActivity());
							servoDataSend.setOnlistener(new NormalChatListenner(
									servoDataSend, sendDataFinish));

							getActivity().runOnUiThread(servoDataSend);
							
							Thread.sleep(150);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							if(WifiSetting_Info.progressDialog!=null){
								overflag[1]=1;
	                			WifiSetting_Info.progressDialog.dismiss();
	                			WifiSetting_Info.progressDialog=null;
	                		}
							//Toast.makeText(getActivity(), "请先连接主机", Toast.LENGTH_LONG).show();
							return;
						}
						} 
		                		}
		                	  
		                		if(WifiSetting_Info.progressDialog!=null){
		                			overflag[0]=1;
		                			WifiSetting_Info.progressDialog.dismiss();
		                			WifiSetting_Info.progressDialog=null;
		                			
		                		}
		                  }
		              }.start(); 
		              WifiSetting_Info.progressDialog.setOnDismissListener(new OnDismissListener() {  
							@Override
							public void onDismiss(DialogInterface arg0) {
								// TODO Auto-generated method stub
								 if(WifiSetting_Info.mClient==null){
									 new AlertDialog.Builder(getActivity()).setTitle("提示")
									   .setMessage("网络异常，通讯中断！请检查网络！")
									   .setPositiveButton("确定", null).show();
					                	//Toast.makeText(getActivity(),"网络异常，传输中断！请检查网络！", Toast.LENGTH_SHORT).show();
									}else if(overflag[0]==1){
										new AlertDialog.Builder(getActivity()).setTitle("提示")
										   .setMessage("数据发送完毕")
										   .setPositiveButton("确定", null).show();
										overflag[0]=0;
									}else if(overflag[1]==1){
										new AlertDialog.Builder(getActivity()).setTitle("提示")
										   .setMessage("程序异常，请重操作！")
										   .setPositiveButton("确定", null).show();
										overflag[1]=0;
										//Toast.makeText(getActivity(),"程序异常，请重操作！", Toast.LENGTH_SHORT).show();
									}
							}  
				        });
		                
			}
			
		});
		button_saveMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			/*	WifiSetting_Info.progressDialog = WifiSetting_Info.progressDialog.show(getActivity(),
						"保存文件更新下位机", "请等待", true, false);*/

				saveToMain_Servo_Paramete();

				// 记住，使用数据格式转换时必须得要新建转换类，否则会数组越界
				ServoDataGenerate mGenerate = new ServoDataGenerate();

				Machinedata mccMachinedata = new Machinedata();

				if (mGenerate.dataGenerate(read_arraylist_servo,
						Fragments_Servo_Parameter.this, mccMachinedata)) {

					// HexDecoding.printHexString("伺服全部数据",
					// mccMachinedata.structServoByteArray());

					formatSendMessage = new WifiSendDataFormat(mccMachinedata
							.structServoByteArray(),
							AddressPublic.machineROMParam_Head);// 调用通讯协议构造函数，初始化
					if(WifiSetting_Info.mClient==null){
						if(ServerUtils.isUsingWifi(getActivity())){
							new AlertDialog.Builder(getActivity())
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
												getActivity().runOnUiThread(successRunnable);
											} catch (IOException e) {
												e.printStackTrace();
												getActivity().runOnUiThread(errorRunnable);
											}
										}
									}.start();
								}
							}).setNegativeButton("取消",null).show();
						}else{
							Toast.makeText(getActivity(), "网络异常，通讯中断！请检查网络！", Toast.LENGTH_SHORT).show();
						}
					}else {
					try {

						servoDataSend = new SendDataRunnable(formatSendMessage,
								getActivity());

						sendDataFinish = new FinishRunnable("伺服参数文件已成功发送到下位机",getActivity());

						servoDataSend.setOnlistener(new NormalChatListenner(
								servoDataSend, sendDataFinish));

						getActivity().runOnUiThread(servoDataSend);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

						Toast.makeText(getActivity().getApplicationContext(),
								e.toString(), Toast.LENGTH_SHORT).show();
					}
					}
				}

			}
		});
	}

	protected void saveToMain_Servo_Paramete() {

		creatFolderSysytem();

		Constans.mechanicalParameter.cleanFile(selectedItem_Servo+File.separator+Constans.伺服参数);
		// 更新要保存的read_arraylist_servo
		if (spinner_servo.getSelectedItemPosition() == 0) {
			for (int i = 0; i < ArrayListBound.getServoParameterListData()
					.size(); i++) {
				// read_arraylist_servo.get(i).put("numText",
				// ArrayListBound.getServoParameterListData().get(i).get("numText"));
				read_arraylist_servo.get(i).put(
						"currentValueText1",
						ArrayListBound.getServoParameterListData().get(i)
								.get("currentValueText"));
				read_arraylist_servo.get(i).put(
						"setValueText1",
						ArrayListBound.getServoParameterListData().get(i)
								.get("setValueText"));
			}
		} else if (spinner_servo.getSelectedItemPosition() == 1) {
			for (int i = 0; i < ArrayListBound.getServoParameterListData()
					.size(); i++) {
				read_arraylist_servo.get(i).put(
						"currentValueText2",
						ArrayListBound.getServoParameterListData().get(i)
								.get("currentValueText"));
				read_arraylist_servo.get(i).put(
						"setValueText2",
						ArrayListBound.getServoParameterListData().get(i)
								.get("setValueText"));
			}
		} else if (spinner_servo.getSelectedItemPosition() == 2) {
			for (int i = 0; i < ArrayListBound.getServoParameterListData()
					.size(); i++) {
				read_arraylist_servo.get(i).put(
						"currentValueText3",
						ArrayListBound.getServoParameterListData().get(i)
								.get("currentValueText"));
				read_arraylist_servo.get(i).put(
						"setValueText3",
						ArrayListBound.getServoParameterListData().get(i)
								.get("setValueText"));
			}
		} else if (spinner_servo.getSelectedItemPosition() == 3) {
			for (int i = 0; i < ArrayListBound.getServoParameterListData()
					.size(); i++) {
				read_arraylist_servo.get(i).put(
						"currentValueText4",
						ArrayListBound.getServoParameterListData().get(i)
								.get("currentValueText"));
				read_arraylist_servo.get(i).put(
						"setValueText4",
						ArrayListBound.getServoParameterListData().get(i)
								.get("setValueText"));
			}
		} else if (spinner_servo.getSelectedItemPosition() == 4) {
			for (int i = 0; i < ArrayListBound.getServoParameterListData()
					.size(); i++) {
				read_arraylist_servo.get(i).put(
						"currentValueText5",
						ArrayListBound.getServoParameterListData().get(i)
								.get("currentValueText"));
				read_arraylist_servo.get(i).put(
						"setValueText5",
						ArrayListBound.getServoParameterListData().get(i)
								.get("setValueText"));
			}
		} else if (spinner_servo.getSelectedItemPosition() == 5) {
			for (int i = 0; i < ArrayListBound.getServoParameterListData()
					.size(); i++) {
				read_arraylist_servo.get(i).put(
						"currentValueText6",
						ArrayListBound.getServoParameterListData().get(i)
								.get("currentValueText"));
				read_arraylist_servo.get(i).put(
						"setValueText6",
						ArrayListBound.getServoParameterListData().get(i)
								.get("setValueText"));
			}
		} else if (spinner_servo.getSelectedItemPosition() == 6) {
			for (int i = 0; i < ArrayListBound.getServoParameterListData()
					.size(); i++) {
				read_arraylist_servo.get(i).put(
						"currentValueText7",
						ArrayListBound.getServoParameterListData().get(i)
								.get("currentValueText"));
				read_arraylist_servo.get(i).put(
						"setValueText7",
						ArrayListBound.getServoParameterListData().get(i)
								.get("setValueText"));
			}
		} else if (spinner_servo.getSelectedItemPosition() == 7) {
			for (int i = 0; i < ArrayListBound.getServoParameterListData()
					.size(); i++) {
				read_arraylist_servo.get(i).put(
						"currentValueText8",
						ArrayListBound.getServoParameterListData().get(i)
								.get("currentValueText"));
				read_arraylist_servo.get(i).put(
						"setValueText8",
						ArrayListBound.getServoParameterListData().get(i)
								.get("setValueText"));
			}
		}

		// 循环写入每一行代码到文件中
		for (int i = 0; i < ArrayListBound.getServoParameterListData().size(); i++) {
			Constans.mechanicalParameter.writeFile(selectedItem_Servo+File.separator+Constans.伺服参数,

			read_arraylist_servo.get(i).get("numText") + "/"
					+ read_arraylist_servo.get(i).get("contentText") + "/"
					+ read_arraylist_servo.get(i).get("noteText") + "/"
					/**
					 * 1轴信息
					 */
					+ read_arraylist_servo.get(i).get("currentValueText1")
					+ "/" + read_arraylist_servo.get(i).get("setValueText1")
					+ "/"
					/**
					 * 2轴信息
					 */
					+ read_arraylist_servo.get(i).get("currentValueText2")
					+ "/" + read_arraylist_servo.get(i).get("setValueText2")
					+ "/"
					/**
					 * 3轴信息
					 */
					+ read_arraylist_servo.get(i).get("currentValueText3")
					+ "/" + read_arraylist_servo.get(i).get("setValueText3")
					+ "/"
					/**
					 * 4轴信息
					 */
					+ read_arraylist_servo.get(i).get("currentValueText4")
					+ "/" + read_arraylist_servo.get(i).get("setValueText4")
					+ "/"
					/**
					 * 5轴信息
					 */
					+ read_arraylist_servo.get(i).get("currentValueText5")
					+ "/" + read_arraylist_servo.get(i).get("setValueText5")
					+ "/"
					/**
					 * 6轴信息
					 */
					+ read_arraylist_servo.get(i).get("currentValueText6")
					+ "/" + read_arraylist_servo.get(i).get("setValueText6")
					+ "/"
					/**
					 * 7轴信息
					 */
					+ read_arraylist_servo.get(i).get("currentValueText7")
					+ "/" + read_arraylist_servo.get(i).get("setValueText7")
					+ "/"
					/**
					 * 8轴信息
					 */
					+ read_arraylist_servo.get(i).get("currentValueText8")
					+ "/" + read_arraylist_servo.get(i).get("setValueText8")
					+ "/"

					+ "\r\n");

		}

		Toast.makeText(getActivity(), "已保存伺服参数文件", Toast.LENGTH_SHORT).show();

	}

	private void creatFolderSysytem() {

		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		// 创建文件夹目录
		if (!sdCardExist) {
			Toast.makeText(getActivity().getApplicationContext(), "请插入外部SD存储卡",
					Toast.LENGTH_SHORT).show();// 如果不存在SD卡，进行提示
		} else {
			/* 分级目录必须分开创建，不能嵌套创建，否则子目录可能不存在 */

			File trFile = new File(Constans.trPATH);// 新建一级目录
			if (!trFile.exists()) {// 判断文件夹目录是否存在
				trFile.mkdir();// 如果不存在则创建
				/*Toast.makeText(getActivity().getApplicationContext(),
						"新创建文件夹" + Constans.trFolder, Toast.LENGTH_SHORT)
						.show();// 提示新创建
*/			} else {
				/*Toast.makeText(getActivity().getApplicationContext(),
						"已存在文件夹" + Constans.trFolder, Toast.LENGTH_SHORT)
						.show();// 提示已创建
*/			}

			Constans.mechanicalParameter.createFolder(Constans.trPATH,
					Constans.mechanicalParameterFolder);

		}
	}

	// protected void unshow() {
	// for (int i = 0; i < ArrayListBound.getServoParameterListData().size();
	// i++) {
	// ArrayListBound.getServoParameterListData().get(i)
	// .put("contentText", "");
	// ArrayListBound.getServoParameterListData().get(i)
	// .put("currentValueText", "");
	// ArrayListBound.getServoParameterListData().get(i)
	// .put("setValueText", "");
	// ArrayListBound.getServoParameterListData().get(i)
	// .put("noteText", "");
	// Servo_Parameter_Adapter.notifyDataSetChanged();
	// }
	//
	// }

	protected void show(String axle) {

		if (axle.equals("1轴")) {
			for (int i = 0; i < read_arraylist_servo.size(); i++) {
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("contentText",
								read_arraylist_servo.get(i).get("contentText"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("currentValueText",
								read_arraylist_servo.get(i).get(
										"currentValueText1"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("setValueText",
								read_arraylist_servo.get(i)
										.get("setValueText1"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("noteText",
								read_arraylist_servo.get(i).get("noteText"));
				Servo_Parameter_Adapter.notifyDataSetChanged();
			}

		} else if (axle.equals("2轴")) {
			for (int i = 0; i < read_arraylist_servo.size(); i++) {
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("contentText",
								read_arraylist_servo.get(i).get("contentText"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("currentValueText",
								read_arraylist_servo.get(i).get(
										"currentValueText2"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("setValueText",
								read_arraylist_servo.get(i)
										.get("setValueText2"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("noteText",
								read_arraylist_servo.get(i).get("noteText"));
				Servo_Parameter_Adapter.notifyDataSetChanged();
			}
		} else if (axle.equals("3轴")) {
			for (int i = 0; i < read_arraylist_servo.size(); i++) {
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("contentText",
								read_arraylist_servo.get(i).get("contentText"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("currentValueText",
								read_arraylist_servo.get(i).get(
										"currentValueText3"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("setValueText",
								read_arraylist_servo.get(i)
										.get("setValueText3"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("noteText",
								read_arraylist_servo.get(i).get("noteText"));
				Servo_Parameter_Adapter.notifyDataSetChanged();
			}
		} else if (axle.equals("4轴")) {
			for (int i = 0; i < read_arraylist_servo.size(); i++) {
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("contentText",
								read_arraylist_servo.get(i).get("contentText"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("currentValueText",
								read_arraylist_servo.get(i).get(
										"currentValueText4"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("setValueText",
								read_arraylist_servo.get(i)
										.get("setValueText4"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("noteText",
								read_arraylist_servo.get(i).get("noteText"));
				Servo_Parameter_Adapter.notifyDataSetChanged();
			}
		} else if (axle.equals("5轴")) {
			for (int i = 0; i < read_arraylist_servo.size(); i++) {
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("contentText",
								read_arraylist_servo.get(i).get("contentText"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("currentValueText",
								read_arraylist_servo.get(i).get(
										"currentValueText5"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("setValueText",
								read_arraylist_servo.get(i)
										.get("setValueText5"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("noteText",
								read_arraylist_servo.get(i).get("noteText"));
				Servo_Parameter_Adapter.notifyDataSetChanged();
			}
		} else if (axle.equals("6轴")) {
			for (int i = 0; i < read_arraylist_servo.size(); i++) {
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("contentText",
								read_arraylist_servo.get(i).get("contentText"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("currentValueText",
								read_arraylist_servo.get(i).get(
										"currentValueText6"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("setValueText",
								read_arraylist_servo.get(i)
										.get("setValueText6"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("noteText",
								read_arraylist_servo.get(i).get("noteText"));
				Servo_Parameter_Adapter.notifyDataSetChanged();
			}
		} else if (axle.equals("7轴")) {
			for (int i = 0; i < read_arraylist_servo.size(); i++) {
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("contentText",
								read_arraylist_servo.get(i).get("contentText"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("currentValueText",
								read_arraylist_servo.get(i).get(
										"currentValueText7"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("setValueText",
								read_arraylist_servo.get(i)
										.get("setValueText7"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("noteText",
								read_arraylist_servo.get(i).get("noteText"));
				Servo_Parameter_Adapter.notifyDataSetChanged();
			}
		} else if (axle.equals("8轴")) {
			for (int i = 0; i < read_arraylist_servo.size(); i++) {
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("contentText",
								read_arraylist_servo.get(i).get("contentText"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("currentValueText",
								read_arraylist_servo.get(i).get(
										"currentValueText8"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("setValueText",
								read_arraylist_servo.get(i)
										.get("setValueText8"));
				ArrayListBound
						.getServoParameterListData()
						.get(i)
						.put("noteText",
								read_arraylist_servo.get(i).get("noteText"));
				Servo_Parameter_Adapter.notifyDataSetChanged();
			}
		}

	}

	private ArrayList<HashMap<String, Object>> read_servo_Parameter() {
		ArrayList<HashMap<String, Object>> read_arraylist = new ArrayList<HashMap<String, Object>>();
		try {
		String[] readlist = Constans.mechanicalParameter
				.readOutFile(Constans.mechanicalParameterPATH +selectedItem_Servo+File.separator+Constans.伺服参数);// 重启之后必须需要完整的路径
		for (int i = 0; i < readlist.length; i++) {
			readlist[i] = readlist[i].trim();
		}
		for (int i = 0; i < readlist.length / 19; i++) {
			HashMap<String, Object> mapadd = new HashMap<String, Object>();
			mapadd.put("numText", readlist[19 * i]);
			mapadd.put("contentText", readlist[19 * i + 1]);
			mapadd.put("noteText", readlist[19 * i + 2]);
			mapadd.put("currentValueText1", readlist[19 * i + 3]);
			mapadd.put("setValueText1", readlist[19 * i + 4]);
			mapadd.put("currentValueText2", readlist[19 * i + 5]);
			mapadd.put("setValueText2", readlist[19 * i + 6]);
			mapadd.put("currentValueText3", readlist[19 * i + 7]);
			mapadd.put("setValueText3", readlist[19 * i + 8]);
			mapadd.put("currentValueText4", readlist[19 * i + 9]);
			mapadd.put("setValueText4", readlist[19 * i + 10]);
			mapadd.put("currentValueText5", readlist[19 * i + 11]);
			mapadd.put("setValueText5", readlist[19 * i + 12]);
			mapadd.put("currentValueText6", readlist[19 * i + 13]);
			mapadd.put("setValueText6", readlist[19 * i + 14]);
			mapadd.put("currentValueText7", readlist[19 * i + 15]);
			mapadd.put("setValueText7", readlist[19 * i + 16]);
			mapadd.put("currentValueText8", readlist[19 * i + 17]);
			mapadd.put("setValueText8", readlist[19 * i + 18]);

			read_arraylist.add(mapadd);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
		return read_arraylist;
	}

	private class MyAdapter extends BaseAdapter {
		private class buttonViewHolder {
			TextView numText;
			TextView contentText;
			TextView currentValueText;
			TextView setValueText;
			TextView noteText;
			Button readButton;
			Button writeButton;
		}

		private ArrayList<HashMap<String, Object>> mAppList;
		private LayoutInflater mInflater;
		private Context mContext;
		private String[] keyString;
		private int[] valueViewID;
		private buttonViewHolder holder;
		private int mLayoutID;

		// MyAdapter的构造函数
		public MyAdapter(Context c, ArrayList<HashMap<String, Object>> appList,
				int layoutID, String[] from, int[] to) {
			mAppList = appList;
			mContext = c;
			mLayoutID = layoutID;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			keyString = new String[from.length];
			valueViewID = new int[to.length];

			System.arraycopy(from, 0, keyString, 0, from.length);
			System.arraycopy(to, 0, valueViewID, 0, to.length);
		}

		public String[] getkeyString() {
			return this.keyString;
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

		public ArrayList<HashMap<String, Object>> getmAppList() {
			return mAppList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(mLayoutID, null);
				;
				holder = new buttonViewHolder();
				holder.numText = (TextView) convertView
						.findViewById(valueViewID[0]);
				holder.contentText = (TextView) convertView
						.findViewById(valueViewID[1]);
				holder.currentValueText = (TextView) convertView
						.findViewById(valueViewID[2]);
				holder.setValueText = (TextView) convertView
						.findViewById(valueViewID[3]);
				holder.noteText = (TextView) convertView
						.findViewById(valueViewID[4]);
				holder.readButton = (Button) convertView
						.findViewById(valueViewID[5]);
				holder.writeButton = (Button) convertView
						.findViewById(valueViewID[6]);
				convertView.setTag(holder);
			}

			HashMap<String, Object> map_item = mAppList.get(position);

			if (map_item != null) {
				String numText = map_item.get(keyString[0]).toString();
				String contentText = map_item.get(keyString[1]).toString();
				String currentValueText = map_item.get(keyString[2]).toString();
				String setValueText = map_item.get(keyString[3]).toString();
				String noteText = map_item.get(keyString[4]).toString();

				holder.numText.setText(numText);
				holder.contentText.setText(contentText);
				holder.currentValueText.setText(currentValueText);
				holder.setValueText.setText(setValueText);
				holder.noteText.setText(noteText);

				// holder.setValueText.setClickable(true);
				// holder.setValueText.setFocusable(true);
				holder.setValueText.setOnClickListener(new setValueListener(
						position));// 添加设定值行监听器

				holder.readButton.setOnClickListener(new readButtonListener(
						position));
				holder.writeButton.setOnClickListener(new writeButtonListener(
						position));
			}
			return convertView;
		}

		// 设定值行监听器
		class setValueListener implements android.view.View.OnClickListener {

			private int position;

			// 构造函数
			setValueListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {

				final EditText etEditText = new EditText(getActivity());
				etEditText.setHint("请输入数字");
				String setValueString =  mAppList.get(position).get(
						keyString[3]).toString();

				etEditText.setText(setValueString);
				etEditText.setSelection(setValueString.length());// 设置光标位置

				// 限制只能输入0~9的数字和点号
				etEditText.setKeyListener(new NumberKeyListener() {
					@Override
					protected char[] getAcceptedChars() {
						return new char[] { '1', '2', '3', '4', '5', '6', '7',
								'8', '9', '0', '.' };
					}

					@Override
					public int getInputType() {
						return android.text.InputType.TYPE_CLASS_NUMBER;// 数字键盘
					}
				});
				new AlertDialog.Builder(getActivity())
						.setTitle("请添加设定值")
						.setView(etEditText)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										String editString = etEditText
												.getText().toString();

										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("numText",
												mAppList.get(position)
														.get(keyString[0])
														.toString());//
										map.put("contentText",
												mAppList.get(position)
														.get(keyString[1])
														.toString());

										map.put("currentValueText", mAppList
												.get(position)
												.get(keyString[2]).toString());
										map.put("setValueText", editString);

										map.put("noteText",
												mAppList.get(position)
														.get(keyString[4])
														.toString());
										mAppList.set(position, map);
										notifyDataSetChanged();
									}

								}).setNegativeButton("取消", null).show();

			}
		}

		class readButtonListener implements android.view.View.OnClickListener {
			private int position;

			// 构造函数
			readButtonListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
				// 写给下位机[Define.MAX_AXIS_NUM][256]中的某一个数
				// 获取轴数信息
				int axis = Integer.parseInt(spinner_servo.getSelectedItem()
						.toString().substring(1));
				// 获取行数
				// Integer.parseInt(mAppList.get(position).get(keyString[0]).toString());
				// 计算应当发送的地址
				// uint32_t servoRdNo;
				// 当为负值的时候表示无效，PPU会根据这个值反复的从SPU读取
				// 将该地址写入servoRdNo
				int address_data_send = (axis - 1)
						* 256
						+ (Integer.parseInt(mAppList.get(position)
								.get(keyString[0]).toString()));

				int address_read = AddressPublic.SysSts_servoPara
						+ (axis - 1)
						* 1024
						+ 4
						* (Integer.parseInt(mAppList.get(position)
								.get(keyString[0]).toString()) - 1);

				Fragments_Servo_Parameter.this.uiChangePosition = position;

				formatSendMessage = new WifiSendDataFormat(
						HexDecoding.int2byteArray4(address_data_send),
						AddressPublic.SysSts_servoRdNo);
				formatReadMessage = new WifiReadDataFormat(address_read, 4);

				try {

					servoDataSend = new SendDataRunnable(servoReadJump,
							formatSendMessage, getActivity());

					getActivity().runOnUiThread(servoDataSend);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Toast.makeText(getActivity(), e.toString(),
							Toast.LENGTH_SHORT).show();
				}

				/*
				 * Toast.makeText( getActivity(), "读取" + "\"" +
				 * mAppList.get(position).get(keyString[1]) .toString() + "\"" +
				 * "当前值", Toast.LENGTH_SHORT).show();
				 */

			}
		}

		class writeButtonListener implements android.view.View.OnClickListener {
			private int position;

			// 构造函数
			writeButtonListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {

				// 获得设定值，并转化为字节数组
				byte[] temp = HexDecoding.int2byteArray4(Integer
						.parseInt(mAppList.get(position).get(keyString[3])
								.toString()));

				// 获取轴数信息
				int axis = Integer.valueOf(spinner_servo.getSelectedItem()
						.toString().substring(1));

				// 获取行数
				// Integer.parseInt(mAppList.get(position).get(keyString[0]).toString());
				// 计算应当发送的地址

				int address = AddressPublic.machineRAMData_Head
						+ 1024
						* (axis - 1)
						+ 4
						* (Integer.valueOf(mAppList.get(position)
								.get(keyString[0]).toString()) - 1);

				formatSendMessage = new WifiSendDataFormat(temp, address);

				try {

					servoDataSend = new SendDataRunnable(formatSendMessage,
							getActivity());

					sendDataFinish = new FinishRunnable(getActivity(), "数据发送完毕");

					servoDataSend.setOnlistener(new NormalChatListenner(
							servoDataSend, sendDataFinish));

					getActivity().runOnUiThread(servoDataSend);

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Toast.makeText(getActivity(), "请先连接主机", Toast.LENGTH_LONG)
							.show();
				}

			}
		}

	}

}
