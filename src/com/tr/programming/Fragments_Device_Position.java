package com.tr.programming;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.WatchRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

import com.dbutils.ArrayListBound;
import com.tr.R;
import com.tr.programming.Fragments_Device_ActualInput.lockListener;
import com.tr.programming.Fragments_Device_Alarm.MyAdapter.notelistener;
import com.tr.programming.Fragments_Device_Alarm.MyAdapter.symbolNameListener;
import com.tr.programming.Fragments_Device_Counter.MyAdapter.signalNameListener;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Fragments_Device_Position extends Fragment  {

	private ListView mListView;
	DecimalFormat format = new DecimalFormat("####");// 格式转化类,0001的格式，暂时这么做了，以后找到更好的方法可以替换
	ArrayList<HashMap<String, Object>> ActualPositionList=ArrayListBound.getDevicePositionListData();
	public static  MyAdapter DevicePosition_Adapter = null;
	//存放已有的符号名进行语法检查
	private ArrayList<String> PositionList_simbolname=new ArrayList<String>();
	private boolean check=false;
	
	HashMap<Integer, Integer> PositionList=new HashMap<Integer, Integer>();

	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActualPositionList=ArrayListBound.getDevicePositionListData();
		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
		Thread a2 = new Thread(alarmQueryRunnable);
		a2.start();
		ledrunnable=new ledRunnable(getActivity());
		Thread a4 = new Thread(ledrunnable);
		a4.start();
	}
	@Override
	public void onPause(){
		super.onPause();
		//离开页面的时候监视的状态显示改变
		try{
			alarmQueryRunnable.destroy();
			ledrunnable.destroy();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        System.out.println("EidtFragment--->onCreate");
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
//        System.out.println("EidtFragment--->onCreateView");
    	getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        return inflater.inflate(R.layout.tab_device_definition_3after, container, false);
    }
    
    @Override
 	public void onActivityCreated(Bundle savedInstanceState) {
 		super.onActivityCreated(savedInstanceState);// 调用父类的方法，完成系统的一些操作
try{
 		format.setMinimumIntegerDigits(4);// 显示最少4位数
 		mListView = (ListView) getActivity().findViewById(R.id.list);
 		if(mListView==null){
			return;
		}
 		
 		//使用final类实属无奈，不知道如何处理，还要看书
 		DevicePosition_Adapter = new MyAdapter(getActivity(),
 				ActualPositionList,// 数据源
 				R.layout.nc_device_definition_3after_item,// ListItem的XML实现

 				// 动态数组与ImageItem对应的子项
 				new String[] { "addressText", "symbolNameEditText","signalNameEditText", "noteEditText","setItem" },
 				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
 				new int[] { R.id.addressText, R.id.symbolNameEditText,R.id.signalNameEditText, R.id.noteEditText ,R.id.check});
 		// 生成适配器的Item和动态数组对应的元素

 		// 添加并且显示
 		mListView.setAdapter(DevicePosition_Adapter);
 		// 添加点击
 		mListView.setOnItemClickListener(new OnItemClickListener() {
 			@Override
 			public void onItemClick(AdapterView<?> arg0, View arg1,
 					final int position, long arg3) {
 				if (TR_Programming_Activity.alreadyChecked_programmingPassword) {
 					DevicePosition_Adapter.setSelectItem(position);
 					DevicePosition_Adapter.notifyDataSetChanged();
 					new AlertDialog.Builder(getActivity())
					.setTitle(R.string.programming_lineOperate)//行操作
//					.setItems(new String[] { getResources().getString(R.string.programming_insertLineUp), "删除该行", "下面插入一行" },
					.setItems(getResources().getStringArray(R.array.programming_array_lineOperate),//行操作数组
							new DialogInterface.OnClickListener() { // 添加选择某项后的方法
								@Override
								public void onClick(DialogInterface dialog,
										int which) { //
									switch (which) {
									case 0:// 上面插入一行,自动删除最后一行，保持行数固定
										HashMap<String, Object> map1 = new HashMap<String, Object>();
										map1.put("addressText", String
												.format("%04d", position+1));//
										map1.put("symbolNameEditText", "");
										map1.put("signalNameEditText", "");
										map1.put("noteEditText", "");
										map1.put("setItem", "0");
										ActualPositionList.add(position, map1);
										// 更新地址号
										for (int restposition = position + 1; restposition < ActualPositionList
												.size(); restposition++) {
											ActualPositionList.get(
													restposition).put(
													"addressText",
													String.format("%04d",
															restposition+1));
										}
										/*// 自动删除最后一行
										ActualPositionList
												.remove(ActualPositionList
														.size() - 1);*/
										DevicePosition_Adapter
												.notifyDataSetChanged();
										Toast.makeText(getActivity(),
												"上面已插入一行",
												Toast.LENGTH_SHORT).show();
										break;

									case 1:// 删除该行，最后自动添加一行，保持行数固定
										new AlertDialog.Builder(getActivity())
										.setTitle("删除警告")
										.setMessage("确认删除地址为"+String.format("%04d", position)+"这一行设备？")
										.setPositiveButton("确定删除",
												new OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog,int which) {
												ActualPositionList.remove(position);
												for (int restposition = position; restposition < ActualPositionList
														.size(); restposition++) {
													ActualPositionList.get(
															restposition).put(
															"addressText",
															String.format("%04d",
																	restposition+1));
												}
												/*// 最后自动添加一行
												HashMap<String, Object> mapadd1 = new HashMap<String, Object>();
												mapadd1.put("addressText", String
														.format("%04d",
																ActualPositionList
																		.size()+1));//
												mapadd1.put("symbolNameEditText",
														"");
												mapadd1.put("signalNameEditText",
														"");
												mapadd1.put("noteEditText", "");
												mapadd1.put("setItem", "0");
												ActualPositionList.add(mapadd1);*/

												DevicePosition_Adapter
														.notifyDataSetChanged();
												Toast.makeText(getActivity(),
														"已删除该行", Toast.LENGTH_SHORT)
														.show();
                                              }
										}).setNegativeButton("取消", null).show();
										break;
									case 2:// 下面插入一行，自动删除最后一行，保持行数固定
										int nextposition = (position + 1);
										HashMap<String, Object> map2 = new HashMap<String, Object>();
										map2.put("addressText", String
												.format("%04d",
														nextposition+1));//
										map2.put("symbolNameEditText", "");
										map2.put("signalNameEditText", "");
										map2.put("noteEditText", "");
										map2.put("setItem", "0");
										ActualPositionList.add(nextposition,
												map2);

										for (int restposition = nextposition + 1; restposition < ActualPositionList
												.size(); restposition++) {
											ActualPositionList.get(
													restposition).put(
													"addressText",
													String.format("%04d",
															restposition+1));
										}
										/*// 自动删除最后一行
										ActualPositionList
												.remove(ActualPositionList
														.size() - 1);*/
										DevicePosition_Adapter
												.notifyDataSetChanged();
										Toast.makeText(getActivity(),
												"下面已插入一行",
												Toast.LENGTH_SHORT).show();
										break;
									default:
										break;
									}
								}
							}).setNegativeButton("取消", null).show();
 				}else {
					Toast.makeText(getActivity(), "当前处于锁定状态", Toast.LENGTH_SHORT).show();
				}
 			}
 		});

}catch(Exception e){
	e.printStackTrace();
}
 	}



 	// 由于需要在listview中针对每一行各个控件增加响应函数，因此使用自定义的listview的接口
 	// 内部类
 	public class MyAdapter extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView addressText;
 			TextView symbolNameEditText;
 			TextView signalNameEditText;
 			TextView noteEditText;
 			CheckBox setItem;
 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		private int selectItem = -1;
 		// MyAdapter的构造函数
 		public MyAdapter(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
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
 		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}
 		@Override
 		public View getView(int position, View convertView, ViewGroup parent) {
 			if (convertView != null) {
 				holder = (buttonViewHolder) convertView.getTag();
 			} else {
 				convertView = mInflater.inflate(mLayoutID,null);
 				holder = new buttonViewHolder();
 				holder.addressText = (TextView) convertView
 						.findViewById(valueViewID[0]);
 				holder.symbolNameEditText = (TextView) convertView
 						.findViewById(valueViewID[1]);
 				holder.signalNameEditText=(TextView)convertView.findViewById(valueViewID[2]);
 				holder.noteEditText = (TextView) convertView
 						.findViewById(valueViewID[3]);
                holder.setItem=(CheckBox)convertView.findViewById(valueViewID[4]);
 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String addressText = (String) appInfo.get(keyString[0]);
 				String symbolNameEditText = (String) appInfo.get(keyString[1]);
 				String signalNameEditText=(String)appInfo.get(keyString[2]);
 				String noteEditText = (String) appInfo.get(keyString[3]);
 				String setItem=(String)appInfo.get(keyString[4]);

 				holder.addressText.setText(addressText);
 				holder.symbolNameEditText.setText(symbolNameEditText);
 				holder.signalNameEditText.setText(signalNameEditText);
 				holder.noteEditText.setText(noteEditText);
 				if(setItem.equals("1")){
 					holder.setItem.setChecked(true);
 				}else{
 					holder.setItem.setChecked(false);
 				}
 			// 选中红色显示
				if (position == selectItem) {
					// 如果当前的行就是ListView中选中的一行，就更改显示样式
					convertView.setBackgroundColor(Color.RED);
				} else {
					convertView.setBackgroundColor(Color.BLACK);
				}

 				if (TR_Programming_Activity.alreadyChecked_programmingPassword) {
					holder.symbolNameEditText
							.setOnClickListener(new symbolNameListener(position));
					holder.signalNameEditText.setOnClickListener(new signalNameListener(position));
					holder.noteEditText.setOnClickListener(new notelistener(
							position));
					holder.setItem.setOnClickListener(new setItemListener(position));
				}else {
					holder.symbolNameEditText.setOnClickListener(new lockListener(getActivity()));
					holder.signalNameEditText.setOnClickListener(new lockListener(getActivity()));
					holder.noteEditText.setOnClickListener(new lockListener(getActivity()));
					holder.setItem.setOnClickListener(new setItemlockListener());
				}
 			}
 			return convertView;
 		}
 		class setItemListener implements android.view.View.OnClickListener {
 			private int position;
 			setItemListener(int pos){
 				position = pos;
 			}
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					setSelectItem(position);
					notifyDataSetChanged();
				if(mAppList.get(position)
						.get(keyString[4])
						.toString().equals("1")){
					mAppList.get(position).put("setItem", "0");
						notifyDataSetChanged();
				}else{
					mAppList.get(position).put("setItem", "1");
					notifyDataSetChanged();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			}
 		}
 		class setItemlockListener implements android.view.View.OnClickListener {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					Toast.makeText(getActivity(), "当前处于锁定状态", Toast.LENGTH_SHORT).show();
					notifyDataSetChanged();
			}
 		}
        
 		class symbolNameListener implements android.view.View.OnClickListener {
 			private int position;

 			symbolNameListener(int pos) {
 				position = pos;
 			}

 			
 			
 			
 			@Override
 			public void onClick(final View v) {
 				try{
 					setSelectItem(position);
 					notifyDataSetChanged();
 				//判断自动增加一行
 				if (position==(mAppList.size()-1)) {
 					addOneItem();	
 					notifyDataSetChanged();
 				}
 				
 				View posView=View.inflate(getActivity(), R.layout.device_position_symbolname,null );//在xml属性中设置只能输入数字和位数
 	 			final RadioGroup radioGroupPos=(RadioGroup) posView.findViewById(R.id.radioGroupPos);
 	 			RadioButton radioP=(RadioButton) posView.findViewById(R.id.radioP);
 	 			RadioButton radioSP=(RadioButton) posView.findViewById(R.id.radioSP);
 	 			RadioButton radioFP=(RadioButton) posView.findViewById(R.id.radioFP);
 				final EditText etEditText=(EditText) posView.findViewById(R.id.editText_pos);
 				if(radioGroupPos==null){return;}
 				if(radioP==null){return;}
 				if(radioSP==null){return;}
 				if(radioFP==null){return;}
 				if(etEditText==null){return;}
 				radioGroupPos.setOnCheckedChangeListener(new OnCheckedChangeListener() {								
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId==R.id.radioP) {
							etEditText.setHint("输入范围1~200");
							etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
						}else if(checkedId==R.id.radioSP){
							etEditText.setHint("输入范围1~8");
							etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
							etEditText.setKeyListener(new NumberKeyListener() {
			 				    @Override
			 				    protected char[] getAcceptedChars() {
			 				        return new char[] { '1', '2', '3', '4', '5', '6', '7', '8'};
			 				    }
			 				    @Override
			 				    public int getInputType() {
			 				        return android.text.InputType.TYPE_CLASS_PHONE;//数字键盘
			 				    }
			 				});
						}else{
							etEditText.setHint("输入范围1~2");
							etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
							etEditText.setKeyListener(new NumberKeyListener() {
			 				    @Override
			 				    protected char[] getAcceptedChars() {
			 				        return new char[] { '1', '2'};
			 				    }
			 				    @Override
			 				    public int getInputType() {
			 				        return android.text.InputType.TYPE_CLASS_PHONE;//数字键盘
			 				    }
			 				});
						}
					}
				});
 				final String symbolNameString=(String) mAppList.get(position).get(keyString[1]);
 				if (symbolNameString.contains("SP")) {
					etEditText.setText(symbolNameString.replace("SP", ""));
					etEditText.setSelection(symbolNameString.replace("SP", "").length());//设置光标位置	
					radioSP.setChecked(true);
				}else if (symbolNameString.contains("FP")) {
					etEditText.setText(symbolNameString.replace("FP", ""));
					etEditText.setSelection(symbolNameString.replace("FP", "").length());//设置光标位置	
					radioFP.setChecked(true);
				}else if (symbolNameString.contains("P")) {
					etEditText.setText(symbolNameString.replace("P", ""));
					etEditText.setSelection(symbolNameString.replace("P", "").length());//设置光标位置	
					radioP.setChecked(true);
				} 	
 				
 				new AlertDialog.Builder(getActivity())
 						.setTitle("请添加符号名")
 						.setView(posView)
 						.setPositiveButton(R.string.OK,//确定
 								new DialogInterface.OnClickListener() {
 									@Override
 									public void onClick(DialogInterface arg0,int arg1) {
 										// TODO 自动生成的方法存根
 										String editString=etEditText.getText().toString().trim();
 										String pos="";
 										if (editString.length()!=0) {
 											if (radioGroupPos.getCheckedRadioButtonId()==R.id.radioP) {
 												int data = Integer.parseInt(editString);
												 if(data<1||data>200){
													 Toast.makeText(getActivity(), "输入范围1~200", Toast.LENGTH_SHORT).show(); 
													 return;
												 }
 												pos="P";
 											}else if(radioGroupPos.getCheckedRadioButtonId()==R.id.radioSP) {
 												int data = Integer.parseInt(editString);
												 if(data<1||data>8){
													 Toast.makeText(getActivity(), "输入范围1~8", Toast.LENGTH_SHORT).show(); 
													 return;
												 }
 												pos="SP";
 											}else {
 												int data = Integer.parseInt(editString);
												 if(data<1||data>2){
													 Toast.makeText(getActivity(), "输入范围1~2", Toast.LENGTH_SHORT).show(); 
													 return;
												 }
 												pos="FP";
 											}
 											
											editString=pos+com.tr.programming.Fragments_NCedit1.addZeroForString(editString, 3);
											
 										}
 										if (!symbolNameString.equals(editString)) {
 											Fragments_Device_Timer.checkDevice4List(PositionList_simbolname, ArrayListBound.getDevicePositionListData());
 											
 											if (PositionList_simbolname.contains(editString)) {
 												editString="";
 												Toast.makeText(getActivity(), "与已有名称重名！请重新输入", Toast.LENGTH_SHORT).show();
 												return;
 											}
 										}
 								
											

 										HashMap<String, Object> map = new HashMap<String, Object>();
 										map.put("addressText",
 												mAppList.get(position)
 														.get(keyString[0])
 														.toString());
 										map.put("symbolNameEditText",editString);
 										map.put("signalNameEditText", 
 												mAppList.get(position)
												.get(keyString[2])
												.toString());
 										map.put("noteEditText", 
 												mAppList.get(position)
												.get(keyString[3])
												.toString());
 										map.put("setItem", 
 												mAppList.get(position)
												.get(keyString[4])
												.toString());
 										mAppList.set(position, map);
 										notifyDataSetChanged();
 									}

 								}).setNegativeButton(R.string.CANCEL, null)//取消
 								.show();
 			}catch(Exception e){
 				e.printStackTrace();
 			}
 			}

 		}
 		class signalNameListener implements android.view.View.OnClickListener {

 			private int position;
 			EditText etEditText = new EditText(getActivity());
 			// 构造函数
 			signalNameListener(int pos) {
 				position = pos;
 			}

 			@Override
 			public void onClick(final View v) {
 				try{
 					setSelectItem(position);
 					notifyDataSetChanged();
 				etEditText.setHint("名称长度不大于24个字符");
 				//限制输入长度为20
 				etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(24)});
 				final String signalNameString=(String) mAppList.get(position).get(keyString[2]);
			    etEditText.setText(signalNameString);
			    etEditText.setSelection(signalNameString.length());//设置光标位置	
 				new AlertDialog.Builder(getActivity())
 						.setTitle("请添加信号名")
 						.setView(etEditText)
 						.setPositiveButton(R.string.OK,//确定
 								new DialogInterface.OnClickListener() {

 									@Override
 									public void onClick(DialogInterface arg0,
 											int arg1) {
 										String editString=etEditText.getText().toString().trim();
 										try {
											byte[] signalbyte=editString.getBytes("gb2312");
											if(signalbyte.length>24){
												editString = "";
												Toast.makeText(getActivity(),
														"信号名长度大于24个字符！请重新输入",
														Toast.LENGTH_SHORT).show();
												return;
											}
										} catch (UnsupportedEncodingException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
 									/*	if (!signalNameString.equals(editString)) {
											Fragments_Device_ActualInput.checkDevice8List(DataInputList_simbolname,DataInputList_signalname, ArrayListBound.getDeviceDataInputListData());
											
											if (DataInputList_signalname.contains(editString)) {
												editString="";
												Toast.makeText(getActivity(), "与已有名称重名！请重新输入", Toast.LENGTH_SHORT).show();
											}
 										}
*/
 										HashMap<String, Object> map = new HashMap<String, Object>();
 										map.put("addressText",
 												mAppList.get(position)
 														.get(keyString[0])
 														.toString());//
 										map.put("symbolNameEditText",
 												mAppList.get(position)
 														.get(keyString[1])
														.toString());
 										map.put("signalNameEditText",editString);
 										map.put("noteEditText",
 												mAppList.get(position)
 														.get(keyString[3])
 														.toString());
 										map.put("setItem", 
 												mAppList.get(position)
												.get(keyString[4])
												.toString());
 										mAppList.set(position, map);
 										notifyDataSetChanged();
 									}

 								}).setNegativeButton(R.string.CANCEL, null)//取消
 								.show();
 			}catch(Exception e){
 				e.printStackTrace();
 			}
 			}
 		}
 		class notelistener implements android.view.View.OnClickListener {

 			private int position;

 			// 构造函数
 			notelistener(int pos) {
 				position = pos;
 			}

 			@Override
 			public void onClick(final View v) {
 				try{
 					setSelectItem(position);
 					notifyDataSetChanged();
 				//判断自动增加一行
 				if (position==(mAppList.size()-1)) {
 					addOneItem();	
 					notifyDataSetChanged();
 				}
 				
 				final EditText etEditText = new EditText(getActivity());
 				etEditText.setHint("注释长度不大于80个字符");
 				//限制输入长度为40
 				etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(80)});
 				
 				String noteString=(String) mAppList.get(position).get(keyString[3]);
			    etEditText.setText(noteString);
			    etEditText.setSelection(noteString.length());//设置光标位置	
 				new AlertDialog.Builder(getActivity())
 						.setTitle("请添加注释")
 						.setView(etEditText)
 						.setPositiveButton(R.string.OK,//确定
 								new DialogInterface.OnClickListener() {

 									@Override
 									public void onClick(DialogInterface arg0,
 											int arg1) {
 										String editString=etEditText.getText().toString().trim();
 										try {
											byte[] signalbyte=editString.getBytes("gb2312");
											if(signalbyte.length>80){
												editString = "";
												Toast.makeText(getActivity(),
														"注释长度大于80个字符！请重新输入",
														Toast.LENGTH_SHORT).show();
												return;
											}
										} catch (UnsupportedEncodingException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
 										//排除非法字符"/"
 										if (editString.contains("/")) {
 											editString=editString.replace("/", "\\");
 											Toast.makeText(getActivity(), "/为非法字符，已替换为\\", Toast.LENGTH_SHORT).show();
										}
 										HashMap<String, Object> map = new HashMap<String, Object>();
 										map.put("addressText",mAppList.get(position)
 														.get(keyString[0])
 														.toString());//
 										map.put("symbolNameEditText",mAppList.get(position)
													.get(keyString[1])
													.toString());
 										map.put("signalNameEditText", 
 												mAppList.get(position)
												.get(keyString[2])
												.toString());
 										map.put("noteEditText",editString);
 										map.put("setItem", 
 												mAppList.get(position)
												.get(keyString[4])
												.toString());
 										mAppList.set(position, map);
 										notifyDataSetChanged();
 									}

 								}).setNegativeButton(R.string.CANCEL, null)//取消
 								.show();
 			}catch(Exception e){
 				e.printStackTrace();
 			}
 			}

 		}
 		private void addOneItem() {
 			try{
			HashMap<String, Object> mapadd = new HashMap<String, Object>();
			mapadd.put("addressText",	String.format("%04d", mAppList.size()+1));//
			mapadd.put("symbolNameEditText","");
			mapadd.put("signalNameEditText","");
			mapadd.put("noteEditText","");
			mapadd.put("setItem", "0");
			mAppList.add(mAppList.size(),mapadd);
 		}catch(Exception e){
			e.printStackTrace();
		}
		}

 	}




  
}