
package com.tr.programming;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dbutils.ArrayListBound;
import com.explain.TableToBinary;
import com.tr.R;
import com.tr.programming.Fragments_Device_ActualInput.lockListener;


public class Fragments_Device_Optional extends Fragment  {

	private ListView mListView;
	DecimalFormat format = new DecimalFormat("####");// 格式转化类,0001的格式，暂时这么做了，以后找到更好的方法可以替换
	ArrayList<HashMap<String, Object>> OptionalList=ArrayListBound.getDeviceOptionalListData();
	public static  MyAdapter DeviceOptional_Adapter = null;
	//存放已有的符号名进行语法检查
	private ArrayList<String> OptionalList_name=new ArrayList<String>();
	private boolean check=false;
	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;
	private static ArrayList<String> ActualInputList_symbol = new ArrayList<String>();// 需要初始化，不然空指针错误
	private static ArrayList<String> ActualOutputList_symbol = new ArrayList<String>();
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		OptionalList=ArrayListBound.getDeviceOptionalListData();
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
        return inflater.inflate(R.layout.tab_device_definition_optional, container, false);
    }
    
    @Override
 	public void onActivityCreated(Bundle savedInstanceState) {
 		super.onActivityCreated(savedInstanceState);// 调用父类的方法，完成系统的一些操作
try{
	initialList_Table(ActualInputList_symbol,
			ArrayListBound.getDeviceActualInputListData());
	initialList_Table(ActualOutputList_symbol,
			ArrayListBound.getDeviceActualOutputListData());
 		format.setMinimumIntegerDigits(4);// 显示最少4位数

 		mListView = (ListView) getActivity().findViewById(R.id.list);
 		if(mListView==null){
			return;
		}
 		//使用final类实属无奈，不知道如何处理，还要看书
 		DeviceOptional_Adapter = new MyAdapter(getActivity(),
 				OptionalList,// 数据源
 				R.layout.nc_device_definition_optional_item,// ListItem的XML实现

 				// 动态数组与ImageItem对应的子项
 				new String[] { "addressText", "name1", "name2", "name3" },
 				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
 				new int[] { R.id.addressText, R.id.name1, R.id.name2, R.id.name3});
 		// 生成适配器的Item和动态数组对应的元素

 		// 添加并且显示
 		mListView.setAdapter(DeviceOptional_Adapter);
 	// 添加点击
 			mListView.setOnItemClickListener(new OnItemClickListener() {
 				@Override
 				public void onItemClick(AdapterView<?> arg0, View arg1,
 						final int position, long arg3) {
 					if (TR_Programming_Activity.alreadyChecked_programmingPassword) {

 						// 选中红色显示
 						DeviceOptional_Adapter.setSelectItem(position);
 						DeviceOptional_Adapter.notifyDataSetChanged();

 					}else {
 						Toast.makeText(getActivity(), "当前处于锁定状态", Toast.LENGTH_SHORT).show();
 					}

 				}

 			});
}catch(Exception e){
	e.printStackTrace();
}
 	}

	private void initialList_Table(ArrayList<String> symbolList,
			ArrayList<HashMap<String, Object>> arrayList) {
		try{
		symbolList.clear();
		for (int i = 0; i < arrayList.size(); i++) {
			String symbolstr = (String) arrayList.get(i).get(
					"symbolNameEditText");
			if (!symbolstr.equals("")) {
				symbolList.add(symbolstr);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

 	// 由于需要在listview中针对每一行各个控件增加响应函数，因此使用自定义的listview的接口
 	// 内部类
 	public class MyAdapter extends BaseAdapter {
 		private class buttonViewHolder {
 			TextView addressText;
 			TextView name1;
 			TextView name2;
 			TextView name3;
 		}

 		private ArrayList<HashMap<String, Object>> mAppList;
 		private LayoutInflater mInflater;
 		private Context mContext;
 		private String[] keyString;
 		private int[] valueViewID;
 		private buttonViewHolder holder;
 		private int mLayoutID;
 		private int mselectItem=-1;
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

 		private void setSelectItem(int selectItem) {
			mselectItem = selectItem;
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
 				holder.name1= (TextView) convertView
 						.findViewById(valueViewID[1]);
 				holder.name2= (TextView) convertView
 						.findViewById(valueViewID[2]);
 				holder.name3= (TextView) convertView
 						.findViewById(valueViewID[3]);
 				convertView.setTag(holder);
 			}

 			HashMap<String, Object> appInfo = mAppList.get(position);
 			if (appInfo != null) {
 				String addressText = (String) appInfo.get(keyString[0]);
 				String name1 = (String) appInfo.get(keyString[1]);
 				String name2 = (String) appInfo.get(keyString[2]);
 				String name3 = (String) appInfo.get(keyString[3]);
 				holder.addressText.setText(addressText);
 				holder.name1.setText(name1);
 				holder.name2.setText(name2);
 				holder.name3.setText(name3);

 				if (TR_Programming_Activity.alreadyChecked_programmingPassword) {
					holder.name1.setOnClickListener(new nameListener(position));
					holder.name2.setOnClickListener(new inameListener(position));
					holder.name3.setOnClickListener(new onameListener(position));
				}else {
					holder.name1.setOnClickListener(new lockListener(getActivity()));
					holder.name2.setOnClickListener(new lockListener(getActivity()));
					holder.name3.setOnClickListener(new lockListener(getActivity()));
				}
 			}
 			// 选中红色显示
			if (position == mselectItem) {
				// 如果当前的行就是ListView中选中的一行，就更改显示样式
				convertView.setBackgroundColor(Color.RED);
			} else {
				convertView.setBackgroundColor(Color.BLACK);
			}
 			return convertView;
 		}

 		class nameListener implements android.view.View.OnClickListener {
 			private int position;
 			String nameString="";
 			nameListener(int pos) {
 				position = pos;
 			}

 			@Override
 			public void onClick(final View v) {
 			// 选中红色显示
 				try{
				setSelectItem(position);
				notifyDataSetChanged();
				
 				final EditText etEditText = new EditText(getActivity());
 				nameString="";
					etEditText.setHint("keycode="+(position+201));
					nameString =  mAppList.get(position).get(
							keyString[1]).toString().trim();
			    etEditText.setText(nameString);
			    etEditText.setSelection(nameString.length());//设置光标位置	
 				
 				new AlertDialog.Builder(getActivity())
 						.setTitle("请添加名称")
 						.setView(etEditText)
 						.setPositiveButton(R.string.OK,//确定
 								new DialogInterface.OnClickListener() {
 									@Override
 									public void onClick(DialogInterface arg0,int arg1) {
 										String editString=etEditText.getText().toString().trim();
 											try {
 												byte[] signalbyte=editString.getBytes("gb2312");
 												if(signalbyte.length>16){
 													editString = "";
 													Toast.makeText(getActivity(),
 															"信号名长度大于16个字符！请重新输入",
 															Toast.LENGTH_SHORT).show();
 													return;
 												}
 											} catch (UnsupportedEncodingException e) {
 												// TODO Auto-generated catch block
 												e.printStackTrace();
 											}
 	 										if (!nameString.equals(editString)) {
 	 											checkOptionalList(OptionalList_name, ArrayListBound.getDeviceOptionalListData());
 	 											
 	 											if (OptionalList_name.contains(editString)) {
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
 											map.put("name1",editString);
 	 										map.put("name2", 
 	 												mAppList.get(position)
 													.get(keyString[2])
 													.toString());
 	 										map.put("name3", 
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
 		class inameListener implements android.view.View.OnClickListener {
 			private int position;
 			String nameString="";
 			inameListener(int pos) {
 				position = pos;
 			}

 			@Override
 			public void onClick(final View v) {
 			// 选中红色显示
 				try{
				setSelectItem(position);
				notifyDataSetChanged();
				
				final MultiAutoCompleteTextView etEditText = new MultiAutoCompleteTextView(
						getActivity());// 多值自动完成输入框
				etEditText.setHint("请输入设备定义中的输入有关项");
				// etEditText.setTokenizer(new
				// MultiAutoCompleteTextView.CommaTokenizer());//设置逗号为分隔符
				etEditText.setTokenizer(new SemicolonTokenizer(' '));// 设置空格为分隔符
				etEditText.setCompletionHint("请选择一项");
				etEditText.setThreshold(1);// 输入一个字符就开始检测，出现下拉选择
				etEditText.setDropDownHeight(260);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(),
						android.R.layout.simple_dropdown_item_1line,
						ActualInputList_symbol);
				etEditText.setAdapter(adapter);
					nameString =  mAppList.get(position).get(
							keyString[2]).toString().trim();
			    etEditText.setText(nameString);
			    etEditText.setSelection(nameString.length());//设置光标位置
 				new AlertDialog.Builder(getActivity())
 						.setTitle("请添加名称")
 						.setView(etEditText)
 						.setPositiveButton(R.string.OK,//确定
 								new DialogInterface.OnClickListener() {
 									@Override
 									public void onClick(DialogInterface arg0,int arg1) {
 										String editString=etEditText.getText().toString().trim();
 											try {
 												byte[] signalbyte=editString.getBytes("gb2312");
 												if(signalbyte.length>8){
 													editString = "";
 													Toast.makeText(getActivity(),
 															"符号名长度大于8个字符！请重新输入",
 															Toast.LENGTH_SHORT).show();
 													return;
 												}
 											} catch (UnsupportedEncodingException e) {
 												// TODO Auto-generated catch block
 												e.printStackTrace();
 											}
 										HashMap<String, Object> map = new HashMap<String, Object>();
 										map.put("addressText",
 												mAppList.get(position)
 														.get(keyString[0])
 														.toString());
 											map.put("name2",editString);
 	 										map.put("name1", 
 	 												mAppList.get(position)
 													.get(keyString[1])
 													.toString());
 	 										map.put("name3", 
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
 		class onameListener implements android.view.View.OnClickListener {
 			private int position;
 			String nameString="";
 			onameListener(int pos) {
 				position = pos;
 			}

 			@Override
 			public void onClick(final View v) {
 			// 选中红色显示
 				try{
				setSelectItem(position);
				notifyDataSetChanged();
				
				final MultiAutoCompleteTextView etEditText = new MultiAutoCompleteTextView(
						getActivity());// 多值自动完成输入框
				etEditText.setHint("请输入设备定义中的输出有关项");
				// etEditText.setTokenizer(new
				// MultiAutoCompleteTextView.CommaTokenizer());//设置逗号为分隔符
				etEditText.setTokenizer(new SemicolonTokenizer(' '));// 设置空格为分隔符
				etEditText.setCompletionHint("请选择一项");
				etEditText.setThreshold(1);// 输入一个字符就开始检测，出现下拉选择
				etEditText.setDropDownHeight(260);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(),
						android.R.layout.simple_dropdown_item_1line,
						ActualOutputList_symbol);
				etEditText.setAdapter(adapter);
					nameString =  mAppList.get(position).get(
							keyString[3]).toString().trim();
			    etEditText.setText(nameString);
			    etEditText.setSelection(nameString.length());//设置光标位置	
 				
 				new AlertDialog.Builder(getActivity())
 						.setTitle("请添加名称")
 						.setView(etEditText)
 						.setPositiveButton(R.string.OK,//确定
 								new DialogInterface.OnClickListener() {
 									@Override
 									public void onClick(DialogInterface arg0,int arg1) {
 										String editString=etEditText.getText().toString().trim();
 											try {
 												byte[] signalbyte=editString.getBytes("gb2312");
 												if(signalbyte.length>8){
 													editString = "";
 													Toast.makeText(getActivity(),
 															"符号名长度大于8个字符！请重新输入",
 															Toast.LENGTH_SHORT).show();
 													return;
 												}
 											} catch (UnsupportedEncodingException e) {
 												// TODO Auto-generated catch block
 												e.printStackTrace();
 											}
 										HashMap<String, Object> map = new HashMap<String, Object>();
 										map.put("addressText",
 												mAppList.get(position)
 														.get(keyString[0])
 														.toString());
 											map.put("name3",editString);
 	 										map.put("name1", 
 	 												mAppList.get(position)
 													.get(keyString[1])
 													.toString());
 	 										map.put("name2", 
 	 												mAppList.get(position)
 													.get(keyString[2])
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


 	}
	public static void checkOptionalList(
			ArrayList<String> list_name,
			ArrayList<HashMap<String, Object>> arrayList) {
		// 直接清空再添加
		try{
		list_name.clear();
		for (int i = 0; i < arrayList.size(); i++) {											
			String mouldstr=arrayList.get(i).get("name1").toString().trim();
			if (!mouldstr.equals("")) {
				list_name.add(mouldstr);
			}
			mouldstr=arrayList.get(i).get("name2").toString().trim();
			if (!mouldstr.equals("")) {
				list_name.add(mouldstr);
			}
			mouldstr=arrayList.get(i).get("name3").toString().trim();
			if (!mouldstr.equals("")) {
				list_name.add(mouldstr);
			}
		}			
	}catch(Exception e){
		e.printStackTrace();
	}
	}
}