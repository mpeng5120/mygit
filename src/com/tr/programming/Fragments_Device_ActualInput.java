package com.tr.programming;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.PlcDataQueryRunnable;
import wifiRunnablesAndChatlistener.WatchRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

import com.dbutils.ArrayListBound;
import com.tr.R;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import awesomeWatch.TableWatch;

public class Fragments_Device_ActualInput extends Fragment {

	private ListView mListView;
	DecimalFormat format = new DecimalFormat("####");// 格式转化类,0001的格式，暂时这么做了，以后找到更好的方法可以替换
	ArrayList<HashMap<String, Object>> ActualInputList = ArrayListBound
			.getDeviceActualInputListData();
	public static MyAdapter ActualInput_Adapter = null;
	//分别存放已有的实际输入符号名和信号名，进行语法检查
	private ArrayList<String> ActualInputList_simbolname=new ArrayList<String>();
	private ArrayList<String> ActualInputList_signalname=new ArrayList<String>();
	private boolean check=false;
	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ActualInputList = ArrayListBound.getDeviceActualInputListData();
		System.out.println("Fragments_Device_ActualInput  alarmQueryRunnable");
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
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("Fragments_Device_ActualInput--->onCreate");
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Fragments_Device_ActualInput--->onCreateView");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.tab_device_definition_8before,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);// 调用父类的方法，完成系统的一些操作
		System.out.println("Fragments_Device_ActualInput--->onActivityCreated");
try{
		format.setMinimumIntegerDigits(4);// 显示最少4位数
		mListView = (ListView) getActivity().findViewById(R.id.list);
		if(mListView==null){
			return;
		}
		ActualInput_Adapter = new MyAdapter(getActivity(), ActualInputList,// 数据源
				R.layout.nc_device_definition_8before_item,// ListItem的XML实现
				new String[] { "addressText", "symbolNameEditText",
						"signalNameEditText", "noteEditText" }, new int[] {
						R.id.addressText, R.id.symbolNameEditText,
						R.id.signalNameEditText, R.id.noteEditText });

		mListView.setAdapter(ActualInput_Adapter);
		// 添加点击
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				if (TR_Programming_Activity.alreadyChecked_programmingPassword) {

					// 选中红色显示
					ActualInput_Adapter.setSelectItem(position);
					ActualInput_Adapter.notifyDataSetChanged();

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
				convertView = mInflater.inflate(mLayoutID, null);
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


				holder.symbolNameEditText.setBackgroundColor(Color.WHITE);
				holder.signalNameEditText.setBackgroundColor(Color.WHITE);
				holder.noteEditText.setBackgroundColor(Color.WHITE);
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
					holder.signalNameEditText
							.setOnClickListener(new signalNameListener(position));
					holder.noteEditText.setOnClickListener(new notelistener(
							position));
				}else {
					holder.symbolNameEditText.setOnClickListener(new lockListener(getActivity()));
					holder.signalNameEditText.setOnClickListener(new lockListener(getActivity()));
					holder.noteEditText.setOnClickListener(new lockListener(getActivity()));
				}
				
			}
			return convertView;
		}

		/**
		 * 符号名，内部类symbolNameListener
		 * 
		 * @author 李婷婷
		 * 
		 */
		class symbolNameListener implements android.view.View.OnClickListener {
			private int position;

			symbolNameListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
				try{
				final EditText etEditText = new EditText(getActivity());
				etEditText.setHint("名称可以包含字母、数字和下划线，长度不大于8个字符");
				// 限制输入长度为8
				etEditText
						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(7) });
				// //给edittext添加监听器
				// etEditText.addTextChangedListener(new
				// editTextListener(etEditText,"X",8));
				// 限制只能输入字符数字和下划线
				etEditText.setKeyListener(new NumberKeyListener() {
					@Override
					protected char[] getAcceptedChars() {
						return new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
								'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
								'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
								'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
								'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
								'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
								'1', '2', '3', '4', '5', '6', '7', '8', '9',
								'0', '_', };
					}

					@Override
					public int getInputType() {
						// TODO Auto-generated method stub
						return android.text.InputType.TYPE_CLASS_TEXT;
					}

				});

				final String symbolNameString = (String) mAppList.get(position).get(
						keyString[1]);
				etEditText.setText(symbolNameString.replace("X", ""));
				etEditText.setSelection(symbolNameString.replace("X", "")
						.length());// 设置光标位置
				new AlertDialog.Builder(getActivity())
						.setTitle("请添加实际输入(X)符号名")
						.setView(etEditText)
						.setPositiveButton(R.string.OK,//确定
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										
										String editString = etEditText
												.getText().toString().trim();
										if (editString.length() != 0) {
											editString = "X" + editString;
										}
										
										if (!symbolNameString.equals(editString)) {
											checkDevice8List(ActualInputList_simbolname,ActualInputList_signalname, ArrayListBound.getDeviceActualInputListData());
											if (ActualInputList_simbolname.contains(editString)) {
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
										map.put("symbolNameEditText",
												editString);
										map.put("signalNameEditText", mAppList
												.get(position)
												.get(keyString[2]).toString());
										map.put("noteEditText",
												mAppList.get(position)
														.get(keyString[3])
														.toString());
										mAppList.set(position, map);
										notifyDataSetChanged();
									}

								}).setNegativeButton(R.string.CANCEL, null)//取消
								.show();

				setSelectItem(position);
				notifyDataSetChanged();
			}catch(Exception e){
				e.printStackTrace();
			}
			}

		}

		// //edittext的监听器
		// class editTextListener implements TextWatcher {
		// private CharSequence temp;
		// private int editStart ;
		// private int editEnd ;
		//
		// private EditText mEditText = null;
		// private CharSequence mStrToStart;
		// private int mStrMaxLength = 0;
		//
		//
		// public editTextListener(EditText editText,CharSequence strToStart,int
		// strMaxLength) {
		// mEditText=editText;
		// mStrToStart=strToStart;
		// mStrMaxLength=strMaxLength;
		// }
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		// temp = s;
		//
		//
		//
		// }
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int
		// count,int after) {
		// mEditText.setText(s);
		// }
		// @Override
		// public void afterTextChanged(Editable s) {
		// editStart = mEditText.getSelectionStart();
		// editEnd = mEditText.getSelectionEnd();
		// if (temp.length() > 10) {
		// Toast.makeText(getActivity(), "你输入的字数已经超过了限制！",
		// Toast.LENGTH_SHORT).show();
		// s.delete(editStart-1, editEnd);
		// int tempSelection = editStart;
		// mEditText.setText(s);
		// mEditText.setSelection(tempSelection);//设置光标位置
		// }
		// }
		// }
		//

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
				etEditText.setHint("名称长度不大于24个字符");
				// 限制输入长度为20
				etEditText
						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
								24) });

				final String signalNameString = (String) mAppList.get(position).get(
						keyString[2]);
				etEditText.setText(signalNameString);
				etEditText.setSelection(signalNameString.length());// 设置光标位置
				new AlertDialog.Builder(getActivity())
						.setTitle("请添加实际输入信号名")
						.setView(etEditText)
						.setPositiveButton(R.string.OK,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										String editString = etEditText
												.getText().toString().trim();
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
										if (!signalNameString.equals(editString)) {
											checkDevice8List(
													ActualInputList_simbolname,
													ActualInputList_signalname,
													ArrayListBound
															.getDeviceActualInputListData());

										/*	if (ActualInputList_signalname
													.contains(editString)) {
												editString = "";
												Toast.makeText(getActivity(),
														"与已有名称重名！请重新输入",
														Toast.LENGTH_SHORT).show();
											}*/
										}
										
										
										
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("addressText",
												mAppList.get(position)
														.get(keyString[0])
														.toString());//
										map.put("symbolNameEditText", mAppList
												.get(position)
												.get(keyString[1]).toString());
										map.put("signalNameEditText",
												editString);
										map.put("noteEditText",
												mAppList.get(position)
														.get(keyString[3])
														.toString());
										mAppList.set(position, map);
										notifyDataSetChanged();
									}

								}).setNegativeButton(R.string.CANCEL, null).show();

				setSelectItem(position);
				notifyDataSetChanged();
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
				final EditText etEditText = new EditText(getActivity());

				etEditText.setHint("注释长度不大于80个字符");
				// 限制输入长度为40
				etEditText
						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
								80) });

				String noteString = (String) mAppList.get(position).get(
						keyString[3]);
				etEditText.setText(noteString);
				etEditText.setSelection(noteString.length());// 设置光标位置

				// /**
				// * 检查输入长度和字符，过滤掉符号
				// */
				// etEditText.addTextChangedListener(new TextWatcher() {
				//
				// int maxLenth=40;//长度最大为40个字符
				// CharSequence temp;
				//
				// @Override
				// public void onTextChanged(CharSequence s, int start, int
				// before, int count) {
				//
				// String editable = etEditText.getText().toString();
				// String str = stringFilter(editable); //过滤特殊字符
				// // if (!editable.equals(str)) {
				// // etEditText.setText(str);
				// // }
				// temp = s;
				// }
				//
				// @Override
				// public void beforeTextChanged(CharSequence s, int start, int
				// count,
				// int after) {
				// }
				//
				// @Override
				// public void afterTextChanged(Editable s) {
				//
				// int editStart = etEditText.getSelectionStart();
				// int editEnd = etEditText.getSelectionEnd();
				// if (temp.length() > maxLenth) {
				// Toast.makeText(getActivity(), "你输入的字数已经超过了40限制！",
				// Toast.LENGTH_SHORT).show();
				// s.delete(editStart-1, editEnd);
				// etEditText.setText(s);
				// etEditText.setSelection(s.length());//设置光标位置
				// }
				//
				// }
				// });

				new AlertDialog.Builder(getActivity())
						.setTitle("请添加实际输入注释")
						.setView(etEditText)
						.setPositiveButton(R.string.OK,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {

										String editString = etEditText
												.getText().toString().trim();
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
										map.put("addressText",
												mAppList.get(position)
														.get(keyString[0])
														.toString());//
										map.put("symbolNameEditText", mAppList
												.get(position)
												.get(keyString[1]).toString());
										map.put("signalNameEditText", mAppList
												.get(position)
												.get(keyString[2]));
										map.put("noteEditText", editString);
										mAppList.set(position, map);
										notifyDataSetChanged();
									}

								}).setNegativeButton(R.string.CANCEL, null).show();

				setSelectItem(position);
				notifyDataSetChanged();
			}catch(Exception e){
				e.printStackTrace();
			}
			}

		}

	}
	
	/**
	 * 将已有的实际输入符号名和信号名存入list
	 * @param actualInputList_simbolname2
	 * @param actualInputList_signalname2
	 * @param arrayList 
	 */
	public static void checkDevice8List(
			ArrayList<String> list_simbolname,
			ArrayList<String> list_signalname, ArrayList<HashMap<String, Object>> arrayList) {

		// 直接清空再添加
	try{
		list_simbolname.clear();
		list_signalname.clear();
			for (int i = 0; i < arrayList.size(); i++) {											
				String symbolstr=arrayList.get(i).get("symbolNameEditText").toString().trim();
				String signalstr=arrayList.get(i).get("signalNameEditText").toString().trim();
				if (!symbolstr.equals("")) {
					list_simbolname.add(symbolstr);
				}	
				if (!signalstr.equals("")) {
					list_signalname.add(signalstr);
				}
			}			
	}catch(Exception e){
		e.printStackTrace();
	}		
	}


//	/**
//	 * 过滤字符
//	 * 
//	 * @param str
//	 * @return
//	 * @throws PatternSyntaxException
//	 */
//	public static String stringFilter(String str) throws PatternSyntaxException {
//		String regEx = "[/\\:*?<>|\"\n\t]";
//		Pattern p = Pattern.compile(regEx);
//		Matcher m = p.matcher(str);
//		return m.replaceAll("");
//	}
	

	/**
	 * 
	* @ClassName: lockListener 
	* @Description: 提示已锁定 
	* @author 李婷婷
	* @date 2013-4-22 下午8:10:08 
	*
	 */

	public static class lockListener implements android.view.View.OnClickListener{
		Activity mActivity ; 
		// 构造函数
		lockListener( Activity activity) {
			mActivity = activity;
		}
		@Override
		public void onClick(View v) {
			try{
			Toast.makeText(mActivity, "当前处于锁定状态", Toast.LENGTH_SHORT).show();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	} 

}