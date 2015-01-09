package com.tr.newpragram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.DelayCount;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

import com.dbutils.ArrayListBound;
import com.explain.HexDecoding;
import com.tr.R;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Fragments_extras extends Fragment {

	private Button btn_usermode;
	private Button btn_userdata;
	private Button btn_allspeed;
	
	byte[] temp=new byte[4];
	ArrayList<HashMap<String, Object>> list_usermode = ArrayListBound.getUserModeListData();
	ArrayList<HashMap<String, Object>> list_userdata = ArrayListBound.getUserDataListData();
	ArrayList<HashMap<String, Object>> list_allspeed = ArrayListBound.getAllSpeedListData();
	private byte[] getData;
	private ChatListener usermodeDataFeedback;
	protected WifiReadDataFormat formatReadusermode;
	protected SendDataRunnable sendDatausermodeRunnable;
	
	ListView listView_usermode;
	MyAdapter_usermode adapter_usermode;
	
	private ChatListener userdataDataFeedback;
	protected WifiReadDataFormat formatReaduserdata;
	protected SendDataRunnable sendDatauserdataRunnable;
	
	ListView listView_userdata;
	MyAdapter_userdata adapter_userdata;
	
	private ChatListener allspeedDataFeedback;
	protected WifiReadDataFormat formatReadallspeed;
	protected SendDataRunnable sendDataallspeedRunnable;
	
	ListView listView_allspeed;
	MyAdapter_allspeed adapter_allspeed;

	private WifiSendDataFormat formatSendMessage;
	private SendDataRunnable sendDataRunnable;
	private FinishRunnable sendDataFinishRunnable;
	
	private ledRunnable ledrunnable;
	private AlarmQueryRunnable alarmQueryRunnable;
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("destroy");
		alarmQueryRunnable.destroy();
		ledrunnable.destroy();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			list_usermode = ArrayListBound.getUserModeListData();
			list_userdata = ArrayListBound.getUserDataListData();
			list_allspeed = ArrayListBound.getAllSpeedListData();
		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
		Thread a2 = new Thread(alarmQueryRunnable);
		a2.start();
		ledrunnable=new ledRunnable(getActivity());
		Thread a4 = new Thread(ledrunnable);
		a4.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		System.out.println("Fragments_extras   onCreate");
	}
	
	//获取返回的数据后进行的UI操作
	private final Runnable usermodereadDataFinishTodo = new Runnable(){
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
			//重新给list_usermode列表赋值
			ArrayList<HashMap<String, Object>> listmi=ArrayListBound.getDeviceMiddleInputListData();
			for(int i=0;i<formatReadusermode.getLength()*8-4;i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("number", i+1);
			    map.put("name",listmi.get(i).get("signalNameEditText").toString());
			    map.put("state", (int)((getData[i/8]>>(i%8))&0x01));
			   
			    list_usermode.set(i, map);
			}
			    adapter_usermode= new MyAdapter_usermode(getActivity(), list_usermode,
						R.layout.parameter_setting_other_functions_usermode_item,
						new String[] { "number", "name","state"},
						new int[] { R.id.number, R.id.modename,R.id.state});
				listView_usermode.setAdapter(adapter_usermode);
				
		}
		}

	};
	//获取返回的数据后进行的UI操作
	private final Runnable userdatareadDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReaduserdata.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReaduserdata.getFinalData() != null)  
			{
			System.arraycopy(formatReaduserdata.getFinalData(), 0, getData, 0, formatReaduserdata.getLength());
			//重新给list_usermode列表赋值
	
			for(int i=0;i<16;i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
			    map.put("number", i+1);
			    map.put("name",ArrayListBound.getDeviceDataInputListData().get(i).get("signalNameEditText").toString());
			    map.put("state", String.valueOf(HexDecoding.Array4Toint(getData, i*4)));
			    list_userdata.set(i, map);
			}
			adapter_userdata= new MyAdapter_userdata(getActivity(), list_userdata,
					R.layout.parameter_setting_other_functions_userdata_item,
					new String[] { "number", "name","state"},
					new int[] { R.id.number, R.id.dataname,R.id.state});
			listView_userdata.setAdapter(adapter_userdata);
		}
		}

	};
	//获取返回的数据后进行的UI操作
	private final Runnable allspeedreadDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReadallspeed.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadallspeed.getFinalData() != null)  
			{
			System.arraycopy(formatReadallspeed.getFinalData(), 0, getData, 0, formatReadallspeed.getLength());
			//重新给list_usermode列表赋值
	
			    HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("name","速度");
				map1.put("editname",String.valueOf((int)(getData[0]&0xff)));
				list_allspeed.set(0, map1);
				HashMap<String, Object> map2 = new HashMap<String, Object>();
				map2.put("name","加速度");
				map2.put("editname",String.valueOf((int)(getData[1]&0xff)));
				list_allspeed.set(1, map2);
				HashMap<String, Object> map3 = new HashMap<String, Object>();
				map3.put("name","减速度");
				map3.put("editname",String.valueOf((int)(getData[2]&0xff)));
				list_allspeed.set(2, map3);
			    adapter_allspeed= new MyAdapter_allspeed(getActivity(), list_allspeed,
						R.layout.tab_setting_extras_allspeed_item,
						new String[] { "name", "editname"},
						new int[] { R.id.name, R.id.editname});
				listView_allspeed.setAdapter(adapter_allspeed);
				
		}
		}

	};
	@Override
	public void onStart(){
		super.onStart();
		System.out.println("Fragments_extras   onstart");
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		System.out.println("Fragments_extras   onCreateView");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.tab_setting_extras, container,false);
		
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		System.out.println("Fragments_extras   onActivityCreated");
		try{
		super.onActivityCreated(savedInstanceState);

	
				btn_usermode=(Button) getActivity().findViewById(R.id.usermode);
				if(btn_usermode==null){
					return;
				}
				btn_usermode.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (WifiSetting_Info.mClient == null) {
							Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
							return;
						}
						View view_usermode=View.inflate(getActivity(), R.layout.parameter_setting_other_functions_usermode, null);
						AlertDialog dialog_usermode=new AlertDialog.Builder(getActivity())
						.setTitle("用户模式设定").setView(view_usermode)
						.setPositiveButton("设定",
			            new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int which) {
								byte[] temp=new byte[4];
								try {
								for(int i=0;i<4;i++){
									int result=0;
									for(int j=0;j<8;j++){
										if(j+i*8>27){break;}
										if(list_usermode.get(j+i*8).get("state").toString().equals("")){return;}
										result=result|(Integer.valueOf(list_usermode.get(j+i*8).get("state").toString())<<j);//此处的list_usermode已 经过 用户修改
								    }
									System.arraycopy(HexDecoding.int2byte(result), 0, temp, i, 1);
								}
								  formatSendMessage=new WifiSendDataFormat(temp, 0x200000AC);
			                       
			                        	sendDataRunnable=new SendDataRunnable(formatSendMessage, getActivity());

										sendDataFinishRunnable=new FinishRunnable(getActivity(), "用户模式设定信息成功发送给下位机");

										sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

										getActivity().runOnUiThread(sendDataRunnable);
										} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
										//Toast.makeText(getActivity(), "请先连接主机", Toast.LENGTH_LONG).show();
									    }
			                        //Toast.makeText(getActivity(), "用户模式设定成功！",Toast.LENGTH_LONG).show();
							}
						}).setNegativeButton("取消",null).show();
					    listView_usermode = (ListView) dialog_usermode.findViewById(R.id.listView_usermode);
						if(listView_usermode==null){
							return;
						}

						formatReadusermode = new WifiReadDataFormat(0x200000AC,4);
						try {
							
							sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode, getActivity());

							sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",usermodereadDataFinishTodo);

							sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));

							getActivity().runOnUiThread(sendDatausermodeRunnable);
						} catch (Exception e) {
							// TODO: handle exception
							Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
						}
						
						
					}
				});	
				
	
				btn_userdata=(Button) getActivity().findViewById(R.id.userdata);
				if(btn_userdata==null){
					return;
				}
				btn_userdata.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (WifiSetting_Info.mClient == null) {
							Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
							return;
						}
						View view_userdata=View.inflate(getActivity(), R.layout.parameter_setting_other_functions_userdata, null);
						AlertDialog dialog_userdata=new AlertDialog.Builder(getActivity())
						.setTitle("用户数据设定").setView(view_userdata)
						.setPositiveButton("设定",
			            new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int which) {
								byte[] temp=new byte[16*4];
								 try {
								for(int i=0;i<16;i++){
									if(list_userdata.get(i).get("state").toString().equals("")){
										Toast.makeText(getActivity(), "数据为空，请重新输入！", Toast.LENGTH_SHORT).show();
										return;}
								    System.arraycopy(HexDecoding.int2byteArray4(Integer.parseInt(list_userdata.get(i).get("state").toString())), 0, temp, i*4, 4);
								}
								  formatSendMessage=new WifiSendDataFormat(temp, 0x200000B0);
			                       
			                        	sendDataRunnable=new SendDataRunnable(formatSendMessage, getActivity());

										sendDataFinishRunnable=new FinishRunnable(getActivity(), "用户数据设定信息成功发送给下位机");

										sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

										getActivity().runOnUiThread(sendDataRunnable);
										
										} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
										Toast.makeText(getActivity(), "数据有误，请重新输入！", Toast.LENGTH_SHORT).show();
									    }
			                        //Toast.makeText(getActivity(), "用户数据设定成功！",Toast.LENGTH_LONG).show();
							}
						}).setNegativeButton("取消",null).show();
					    listView_userdata = (ListView) dialog_userdata.findViewById(R.id.listView_userdata);
						if(listView_userdata==null){
							return;
						}
						formatReaduserdata = new WifiReadDataFormat(0x200000B0,16*4);
						try {
							sendDatauserdataRunnable=new SendDataRunnable(formatReaduserdata, getActivity());

							sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",userdatareadDataFinishTodo);

							sendDatauserdataRunnable.setOnlistener(new NormalChatListenner(sendDatauserdataRunnable, sendDataFinishRunnable));

							getActivity().runOnUiThread(sendDatauserdataRunnable);
						} catch (Exception e) {
							// TODO: handle exception
							Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
						}
						
						
					}
				});	
		
				btn_allspeed=(Button) getActivity().findViewById(R.id.allspeed);
				if(btn_allspeed==null){
					return;
				}
				btn_allspeed.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (WifiSetting_Info.mClient == null) {
							Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
							return;
						}
						View view_allspeed=View.inflate(getActivity(), R.layout.tab_setting_extras_allspeed, null);
						AlertDialog dialog_allspeed=new AlertDialog.Builder(getActivity())
						.setTitle("全体速度设定").setView(view_allspeed)
						.setPositiveButton("设定",
			            new DialogInterface.OnClickListener() 
						{
							public void onClick(DialogInterface dialog, int which) { 
								try {
								if(((Map)listView_allspeed.getItemAtPosition(0)).get("editname").toString().equals("")){
									Toast.makeText(getActivity(), "速度为空，请重新输入！", Toast.LENGTH_SHORT).show();return;}
								if(((Map)listView_allspeed.getItemAtPosition(1)).get("editname").toString().equals("")){
									Toast.makeText(getActivity(), "加速度为空，请重新输入！", Toast.LENGTH_SHORT).show();return;}
								if(((Map)listView_allspeed.getItemAtPosition(2)).get("editname").toString().equals("")){
									Toast.makeText(getActivity(), "减速度为空，请重新输入！", Toast.LENGTH_SHORT).show();return;}
								System.arraycopy(HexDecoding.int2byte(Integer.parseInt(((Map)listView_allspeed.getItemAtPosition(0)).get("editname").toString())), 0, temp, 0, 1);
								System.arraycopy(HexDecoding.int2byte(Integer.parseInt(((Map)listView_allspeed.getItemAtPosition(1)).get("editname").toString())), 0, temp, 1, 1);
								System.arraycopy(HexDecoding.int2byte(Integer.parseInt(((Map)listView_allspeed.getItemAtPosition(2)).get("editname").toString())), 0, temp, 2, 1);
								System.arraycopy(HexDecoding.int2byte(1), 0, temp, 3, 1);
								  formatSendMessage=new WifiSendDataFormat(temp, 0x400241C0);
			                       
			                        	sendDataRunnable=new SendDataRunnable(formatSendMessage, getActivity());

										sendDataFinishRunnable=new FinishRunnable(getActivity(), "全体速度设定信息成功发送给下位机");

										sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

										getActivity().runOnUiThread(sendDataRunnable);
										} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
										Toast.makeText(getActivity(), "数据有误，请重新输入！", Toast.LENGTH_SHORT).show();
									    }
			                        //Toast.makeText(getActivity(), "全体速度设定成功！",Toast.LENGTH_LONG).show();
							}
						}).setNegativeButton("取消",null).show();
						 listView_allspeed = (ListView) dialog_allspeed.findViewById(R.id.listView_allspeed);
							if(listView_allspeed==null){
								return;
							}
							formatReadallspeed = new WifiReadDataFormat(0x400241C0,4);
							try {
								
								sendDataallspeedRunnable=new SendDataRunnable(formatReadallspeed, getActivity());

								sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",allspeedreadDataFinishTodo);

								sendDataallspeedRunnable.setOnlistener(new NormalChatListenner(sendDataallspeedRunnable, sendDataFinishRunnable));

								getActivity().runOnUiThread(sendDataallspeedRunnable);
							} catch (Exception e) {
								// TODO: handle exception
								Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
							}
							
					}
					
				});	
	}catch(Exception e){
		e.printStackTrace();
	}
	}

 	public class MyAdapter_usermode extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView number;
 			TextView modename;
 			ToggleButton state;
 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		// MyAdapter的构造函数
 		public MyAdapter_usermode(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
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
 				holder.number = (TextView) convertView
 						.findViewById(valueViewID[0]);
 				holder.modename = (TextView) convertView
 						.findViewById(valueViewID[1]);
 				holder.state = (ToggleButton) convertView
 						.findViewById(valueViewID[2]);
 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String number = appInfo.get(keyString[0]).toString();
 				String modename = appInfo.get(keyString[1]).toString();
 				String state = appInfo.get(keyString[2]).toString();
 				holder.number.setText(number);
 				holder.modename.setText(modename);
 				//holder.modename.setOnClickListener(new modenameListener(position));
 				if(state.equals("1")){
 				     holder.state.setText("ON");
 				    holder.state.setBackgroundColor(Color.BLUE);
 				}else{
 					 holder.state.setText("OFF");
 					holder.state.setBackgroundColor(Color.GRAY);
 				}
 				final int mposition=position;
 				holder.state.setOnCheckedChangeListener(new OnCheckedChangeListener() {

 					@Override
 		 			public void onCheckedChanged(CompoundButton buttonView,
 		 					boolean isChecked) {

 		 				if (isChecked) {
 							mAppList.get(mposition).put("state", "1");
 		 				} else {
 		 					mAppList.get(mposition).put("state", "0");
 		 				}
 		 				notifyDataSetChanged();
 		 			}
 		 		});
 			}
 			return convertView;
 		}
/* 		class modenameListener implements android.view.View.OnClickListener {
			private int position;

			modenameListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
				final EditText etEditText = new EditText(getActivity());
				String daString=mAppList.get(position).get(
						keyString[1]).toString();
				etEditText.setText(daString);
				etEditText.setSelection(daString.length());// 设置光标位置
				new AlertDialog.Builder(getActivity())
						.setTitle("请添加名称")
						.setView(etEditText)
						.setPositiveButton(R.string.OK,//确定
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										
										String editString = etEditText
												.getText().toString().trim();
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("number",
												mAppList.get(position)
														.get(keyString[0])
														.toString());
										map.put("name",editString);
										map.put("state",
												mAppList.get(position)
												.get(keyString[2])
												.toString());
										mAppList.set(position, map);
										notifyDataSetChanged();
									}

								}).setNegativeButton(R.string.CANCEL, null)//取消
								.show();

				notifyDataSetChanged();
			}

		}*/
 	}
 	public class MyAdapter_userdata extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView number;
 			TextView modename;
 			TextView state;
 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		// MyAdapter的构造函数
 		public MyAdapter_userdata(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
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
 				holder.number = (TextView) convertView
 						.findViewById(valueViewID[0]);
 				holder.modename = (TextView) convertView
 						.findViewById(valueViewID[1]);
 				holder.state = (TextView) convertView
 						.findViewById(valueViewID[2]);
 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String number = appInfo.get(keyString[0]).toString();
 				String modename = appInfo.get(keyString[1]).toString();
 				String state = appInfo.get(keyString[2]).toString();
 				holder.number.setText(number);
 				holder.modename.setText(modename);
 			    holder.state.setText(state);
 			    //holder.modename.setOnClickListener(new modenameListener(position));
 			    holder.state.setOnClickListener(new dataListener(position));
 			}
 			return convertView;
 		}
/* 		class modenameListener implements android.view.View.OnClickListener {
			private int position;

			modenameListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
				final EditText etEditText = new EditText(getActivity());
				String daString=mAppList.get(position).get(
						keyString[1]).toString();
				etEditText.setText(daString);
				etEditText.setSelection(daString.length());// 设置光标位置
				new AlertDialog.Builder(getActivity())
						.setTitle("请添加名称")
						.setView(etEditText)
						.setPositiveButton(R.string.OK,//确定
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										
										String editString = etEditText
												.getText().toString().trim();
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("number",
												mAppList.get(position)
														.get(keyString[0])
														.toString());
										map.put("name",editString);
										map.put("state",
												mAppList.get(position)
												.get(keyString[2])
												.toString());
										mAppList.set(position, map);
										notifyDataSetChanged();
									}

								}).setNegativeButton(R.string.CANCEL, null)//取消
								.show();

				notifyDataSetChanged();
			}

		}*/
 		class dataListener implements android.view.View.OnClickListener {
			private int position;

			dataListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
				final EditText etEditText = new EditText(getActivity());
				String daString=mAppList.get(position).get(
						keyString[2]).toString();
				etEditText.setText(daString);
				etEditText.setSelection(daString.length());// 设置光标位置
				etEditText.setHint("请输入数值");
				//etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
				etEditText.setKeyListener(new NumberKeyListener() {
					@Override
					protected char[] getAcceptedChars() {
						return new char[] { '0','1','2','3','4','5','6','7','8','9','.'};
					}

					@Override
					public int getInputType() {
						return android.text.InputType.TYPE_CLASS_NUMBER;// 数字键盘
					}
				});
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
										map.put("number",
												mAppList.get(position)
														.get(keyString[0])
														.toString());
										map.put("name",
												mAppList.get(position)
														.get(keyString[1])
														.toString());
										map.put("state",
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
 	public class MyAdapter_allspeed extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView name;
 			TextView editname;
 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		// MyAdapter的构造函数
 		public MyAdapter_allspeed(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
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
 				holder.name = (TextView) convertView
 						.findViewById(valueViewID[0]);
 				holder.editname = (TextView) convertView
 						.findViewById(valueViewID[1]);
 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String name = appInfo.get(keyString[0]).toString();
 				String editname = appInfo.get(keyString[1]).toString();
 				holder.name.setText(name);
 				holder.editname.setText(editname);
 			    holder.editname.setOnClickListener(new dataListener(position));
 			}
 			return convertView;
 		}
 		class dataListener implements android.view.View.OnClickListener {
			private int position;

			dataListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
				final EditText etEditText = new EditText(getActivity());
				String speedString=mAppList.get(position).get(
						keyString[1]).toString();
				etEditText.setText(speedString);
				etEditText.setSelection(speedString.length());// 设置光标位置
				switch (position) {
				case 0://速度，限定1~100
					etEditText.setHint("数值范围1~100");
					etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
					etEditText.setKeyListener(new NumberKeyListener() {
						@Override
						protected char[] getAcceptedChars() {
							return new char[] {'1', '2', '3', '4', '5', '6', '7',
									'8', '9', '0'};
						}

						@Override
						public int getInputType() {
							return android.text.InputType.TYPE_CLASS_NUMBER;// 数字键盘
						}
					});
					break;
				case 1:// 加速度，限定1~5
				case 2:// 减速度，限定1~5
					etEditText.setHint("数值范围1~5");
					etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
					etEditText.setKeyListener(new NumberKeyListener() {
						@Override
						protected char[] getAcceptedChars() {
							return new char[] { '1', '2', '3', '4', '5' };
						}

						@Override
						public int getInputType() {
							return android.text.InputType.TYPE_CLASS_NUMBER;// 数字键盘
						}
					});
					break;
				}
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
										int editInt;
										if (!editString.equals("")) {
											switch (position) {
											case 0://速度，限定1~100
												editInt=Integer.parseInt(editString);
												if (editInt<1||editInt > 100) {
													Toast.makeText(getActivity(),"速度超过范围1~100，请重新输入",Toast.LENGTH_SHORT).show();
													editString = "";
													return;
												}
												break;
											case 1:// 加速度，限定1~5
												editInt=Integer.parseInt(editString);
												if (editInt <1|| editInt > 5) {
													Toast.makeText(getActivity(),"加速度超过范围1~5，请重新输入",Toast.LENGTH_SHORT).show();
													editString = "";
													return;
												}
												break;
											case 2:// 减速度，限定1~5
												editInt=Integer.parseInt(editString);
												if (editInt<1||editInt>5) {
													Toast.makeText(getActivity(),"减速度超过范围1~5，请重新输入",Toast.LENGTH_SHORT).show();
													editString = "";
													return;
												}
												break;
											}
											
										}
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("name",
												mAppList.get(position)
														.get(keyString[0])
														.toString());
										map.put("editname",
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
