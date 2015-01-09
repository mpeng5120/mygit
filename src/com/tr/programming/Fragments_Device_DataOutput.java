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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dbutils.ArrayListBound;
import com.tr.R;
import com.tr.programming.Fragments_Device_ActualInput.MyAdapter;
import com.tr.programming.Fragments_Device_ActualInput.lockListener;
import com.tr.programming.Fragments_Device_DataInput.MyAdapter.notelistener;
import com.tr.programming.Fragments_Device_DataInput.MyAdapter.signalNameListener;
import com.tr.programming.Fragments_Device_DataInput.MyAdapter.symbolNameListener;


public class Fragments_Device_DataOutput extends Fragment  {

	private ListView mListView;
	DecimalFormat format = new DecimalFormat("####");// 格式转化类,0001的格式，暂时这么做了，以后找到更好的方法可以替换

	ArrayList<HashMap<String, Object>> DataOutputList=ArrayListBound.getDeviceDataOutputListData();
	public static  MyAdapter DeviceDataOutput_Adapter = null;
	//分别存放已有的符号名和信号名，进行语法检查
	private ArrayList<String> DataOutputList_simbolname=new ArrayList<String>();
	private ArrayList<String> DataOutputList_signalname=new ArrayList<String>();
	private boolean check=false;
	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		DataOutputList=ArrayListBound.getDeviceDataOutputListData();
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
        System.out.println("EidtFragment--->onCreateView");
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        return inflater.inflate(R.layout.tab_device_definition_8before, container, false);
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
 		DeviceDataOutput_Adapter = new MyAdapter(getActivity(),
 				DataOutputList,// 数据源
 				R.layout.nc_device_definition_8before_item,// ListItem的XML实现

 				// 动态数组与ImageItem对应的子项
 				new String[] { "addressText", "symbolNameEditText", "signalNameEditText", "noteEditText" },
 				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
 				new int[] { R.id.addressText, R.id.symbolNameEditText,R.id.signalNameEditText, R.id.noteEditText });
 		// 生成适配器的Item和动态数组对应的元素

 		// 添加并且显示
 		mListView.setAdapter(DeviceDataOutput_Adapter);
 		// 添加点击
 		mListView.setOnItemClickListener(new OnItemClickListener() {
 			@Override
 			public void onItemClick(AdapterView<?> arg0, View arg1,
 					final int position, long arg3) {
 				if (TR_Programming_Activity.alreadyChecked_programmingPassword) {
 					DeviceDataOutput_Adapter.setSelectItem(position);
 					DeviceDataOutput_Adapter.notifyDataSetChanged();
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
 				holder.signalNameEditText = (TextView) convertView
 						.findViewById(valueViewID[2]);
 				holder.noteEditText = (TextView) convertView
 						.findViewById(valueViewID[3]);

 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String numText = (String) appInfo.get(keyString[0]);
 				String symbolNameEditText = (String) appInfo.get(keyString[1]);
 				String signalNameEditText = (String) appInfo.get(keyString[2]);
 				String noteEditText = (String) appInfo.get(keyString[3]);

 				holder.addressText.setText(numText);
 				holder.symbolNameEditText.setText(symbolNameEditText);
 				holder.signalNameEditText.setText(signalNameEditText);
 				holder.noteEditText.setText(noteEditText);
 			// 选中红色显示
				if (position == selectItem) {
					// 如果当前的行就是ListView中选中的一行，就更改显示样式
					convertView.setBackgroundColor(Color.RED);
				} else {
					convertView.setBackgroundColor(Color.BLACK);
				}
				if (TR_Programming_Activity.alreadyChecked_programmingPassword) {
					holder.symbolNameEditText.setOnClickListener(new symbolNameListener(
	 						position));// 命令行添加监听器，position为行号
	 				holder.signalNameEditText.setOnClickListener(new signalNameListener(position));// 标号行添加监听器
	 				holder.noteEditText.setOnClickListener(new notelistener(position));// 注释行添加监听器
				}else {
					holder.symbolNameEditText.setOnClickListener(new lockListener(getActivity()));
					holder.signalNameEditText.setOnClickListener(new lockListener(getActivity()));
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

 			@Override
 			public void onClick(final View v) {
                try{
                	setSelectItem(position);
    				notifyDataSetChanged();
 				final EditText etEditText = new EditText(getActivity());
 				etEditText.setHint("名称可以包含字母、数字和下划线，长度不大于20个字符");
 				//限制输入长度为8
 				etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
 				//限制只能输入字符数字和下划线
 				etEditText.setKeyListener(new NumberKeyListener() {
 				    @Override
 				    protected char[] getAcceptedChars() {
 				        return new char[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
 				        					'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
 				        					'1', '2', '3', '4', '5', '6', '7', '8','9', '0' ,'_',};
 				        }
 				    
					@Override
					public int getInputType() {
						// TODO Auto-generated method stub
						return android.text.InputType.TYPE_CLASS_TEXT;
					}
 				    
 				});	
 				final String symbolNameString=(String) mAppList.get(position).get(keyString[1]);
			    etEditText.setText(symbolNameString.replace("DY", ""));
			    etEditText.setSelection(symbolNameString.replace("DY", "").length());//设置光标位置	
 				new AlertDialog.Builder(getActivity())
 						.setTitle("请添加数据输出符号名")
 						.setView(etEditText)
 						.setPositiveButton(R.string.OK,//确定
 								new DialogInterface.OnClickListener() {

 									@Override
 									public void onClick(DialogInterface arg0,int arg1) {
 										String editString=etEditText.getText().toString().trim();
 										if (editString.length()!=0) {
 											editString="DY"+editString;
										}
 										if (!symbolNameString.equals(editString)) {
 											Fragments_Device_ActualInput.checkDevice8List(DataOutputList_simbolname,DataOutputList_signalname, ArrayListBound.getDeviceDataOutputListData());
											
 											if (DataOutputList_simbolname.contains(editString)) {
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
 				etEditText.setHint("名称长度不大于40个字符");
 				//限制输入长度为20
 				etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(40)});
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
											if(signalbyte.length>40){
												editString = "";
												Toast.makeText(getActivity(),
														"信号名长度大于40个字符！请重新输入",
														Toast.LENGTH_SHORT).show();
												return;
											}
										} catch (UnsupportedEncodingException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
 										if (!signalNameString.equals(editString)) {
											Fragments_Device_ActualInput.checkDevice8List(DataOutputList_simbolname,DataOutputList_signalname, ArrayListBound.getDeviceDataOutputListData());
											
											/*if (DataOutputList_signalname.contains(editString)) {
												editString="";
												Toast.makeText(getActivity(), "与已有名称重名！请重新输入", Toast.LENGTH_SHORT).show();
											}*/
 										}

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
 										map.put("signalNameEditText",mAppList.get(position)
												.get(keyString[2]));
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

 	}










  
}