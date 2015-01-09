package com.tr.programming;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.WatchRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.dbutils.ArrayListBound;
import com.tr.R;
import com.tr.programming.Fragments_Device_ActualInput.lockListener;
import com.tr.programming.Fragments_Device_ActualInput.MyAdapter.notelistener;
import com.tr.programming.Fragments_Device_ActualInput.MyAdapter.signalNameListener;
import com.tr.programming.Fragments_Device_ActualInput.MyAdapter.symbolNameListener;


public class Fragments_Device_Alarm extends Fragment  {

	private ListView mListView;
	DecimalFormat format = new DecimalFormat("####");// 格式转化类,0001的格式，暂时这么做了，以后找到更好的方法可以替换
	ArrayList<HashMap<String, Object>> AlarmList=ArrayListBound.getDeviceAlarmListData();
	public static  MyAdapter DeviceAlarm_Adapter = null;
	//存放已有的符号名进行语法检查
	private ArrayList<String> AlarmList_simbolname=new ArrayList<String>();
	private boolean check=false;
	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AlarmList=ArrayListBound.getDeviceAlarmListData();
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
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
       
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    	getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        return inflater.inflate(R.layout.tab_device_definition_alarm, container, false);
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
 		DeviceAlarm_Adapter = new MyAdapter(getActivity(),
 				AlarmList,// 数据源
 				R.layout.nc_device_definition_alarm_item,// ListItem的XML实现

 				// 动态数组与ImageItem对应的子项
 				new String[] { "addressText", "symbolNameEditText", "noteEditText" },
 				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
 				new int[] { R.id.addressText, R.id.symbolNameEditText, R.id.noteEditText });
 		// 生成适配器的Item和动态数组对应的元素

 		// 添加并且显示
 		mListView.setAdapter(DeviceAlarm_Adapter);
 		// 添加点击
 		mListView.setOnItemClickListener(new OnItemClickListener() {
 			@Override
 			public void onItemClick(AdapterView<?> arg0, View arg1,
 					final int position, long arg3) {
 				if (TR_Programming_Activity.alreadyChecked_programmingPassword) {
 				// 选中红色显示
 					DeviceAlarm_Adapter.setSelectItem(position);
 					DeviceAlarm_Adapter.notifyDataSetChanged();
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
 			TextView noteEditText;

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
 				holder.noteEditText = (TextView) convertView
 						.findViewById(valueViewID[2]);

 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String addressText = (String) appInfo.get(keyString[0]);
 				String symbolNameEditText = (String) appInfo.get(keyString[1]);
 				String noteEditText = (String) appInfo.get(keyString[2]);

 				holder.addressText.setText(addressText);
 				holder.symbolNameEditText.setText(symbolNameEditText);
 				holder.noteEditText.setText(noteEditText);
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
					holder.noteEditText.setOnClickListener(new notelistener(
							position));
				}else {
					holder.symbolNameEditText.setOnClickListener(new lockListener(getActivity()));
					holder.noteEditText.setOnClickListener(new lockListener(getActivity()));
				}
 			}
 			return convertView;
 		}

 		class symbolNameListener implements android.view.View.OnClickListener {
 			private int position;

 			symbolNameListener(int pos) {
 				position = pos;
 			}

 			View posView=View.inflate(getActivity(), R.layout.device_position_symbolname,null );//在xml属性中设置只能输入数字和位数
 			final RadioGroup radioGroupPos=(RadioGroup) posView.findViewById(R.id.radioGroupPos);
			final EditText etEditText=(EditText) posView.findViewById(R.id.editText_pos);
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
 				etEditText.setHint("输入范围1~200");
 				final String symbolNameString=(String) mAppList.get(position).get(keyString[1]);
			    etEditText.setText(symbolNameString);
			    etEditText.setSelection(symbolNameString.length());//设置光标位置		
 				//限制输入长度为3
 				etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
 				//限制只能输入0~9的数字
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
 				
 				new AlertDialog.Builder(getActivity())
 						.setTitle("请添加符号名")
 						.setView(etEditText)
 						.setPositiveButton(R.string.OK,//确定
 								new DialogInterface.OnClickListener() {
 									@Override
 									public void onClick(DialogInterface arg0,int arg1) {
 										try{
 										String editString=etEditText.getText().toString().trim();
 										if(editString.equals("")){
 											editString="";
 										}else{
 										int data = Integer.parseInt(editString);
										 if(data<1||data>200){
											 Toast.makeText(getActivity(), "输入范围1~200", Toast.LENGTH_SHORT).show(); 
											 return;
										 }
 										if (editString.length()!=0) {
 											editString=com.tr.programming.Fragments_NCedit1.addZeroForString(etEditText.getText().toString(), 3);
										}
 										if (!symbolNameString.equals(editString)) {
 											Fragments_Device_Timer.checkDevice4List(AlarmList_simbolname, ArrayListBound.getDeviceAlarmListData());
 											
 											if (AlarmList_simbolname.contains(editString)) {
 												editString="";
 												Toast.makeText(getActivity(), "与已有名称重名！请重新输入", Toast.LENGTH_SHORT).show();
 												return;
 											}
 										}
 										}

 										HashMap<String, Object> map = new HashMap<String, Object>();
 										map.put("addressText",
 												mAppList.get(position)
 														.get(keyString[0])
 														.toString());
 										map.put("symbolNameEditText",editString);
 										map.put("noteEditText", 
 												mAppList.get(position)
												.get(keyString[2])
												.toString());
 										mAppList.set(position, map);
 										notifyDataSetChanged();
 									}catch(Exception e){
 										e.printStackTrace();
 									}
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
 				etEditText.setHint("注释长度不大于60个字符");
 				//限制输入长度为40
 				etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
 				String noteString=(String) mAppList.get(position).get(keyString[2]);
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
 										int allbyte=0;
 										String notebf=editString;
 										int note1flag=0;
 										try{
 										for(int n=0;n<editString.length()-1;n++){
 											char ch=editString.charAt(n);
 											String chstr=ch+"";
 											byte[] chbyte=null;
 										    chbyte=chstr.getBytes("gb2312");
 											allbyte=allbyte+chbyte.length;
 											if(allbyte==19||allbyte==39||allbyte==59){
 												notebf=notebf.substring(0, n+1+note1flag)+"\0"+notebf.substring(n+1+note1flag, notebf.length());
 											    allbyte=0;
 												note1flag++;
 											}
 										}
 										} catch (Exception e) {
											e.printStackTrace();
										}
 										editString=notebf;
 										try {
 											byte[] notebyte=editString.getBytes("gb2312");
 											if(notebyte.length>60){
												editString = "";
												Toast.makeText(getActivity(),
														"注释长度大于60个字符！请重新输入",
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
 										map.put("noteEditText",editString);
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
			mapadd.put("addressText",	String.format("%03d", mAppList.size()+1));//
			mapadd.put("symbolNameEditText","");
			mapadd.put("noteEditText","");
			mAppList.add(mAppList.size(),mapadd);
 		}catch(Exception e){
			e.printStackTrace();
		}
		}

 	}










  
}