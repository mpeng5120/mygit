/**
 * 
 */
package com.tr.newpragram;
import java.util.ArrayList;
import java.util.HashMap;

import com.dbutils.ArrayListBound;
import com.tr.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class TableAdapter extends BaseAdapter {

		private class buttonViewHolder {
			TextView numText;
			TextView XText;
			TextView YText;
			TextView HText;
			TextView ZText;
			TextView LText;

		}

		private ArrayList<HashMap<String, Object>> mAppList;
		private LayoutInflater mInflater;
		private Context mContext;
		private String[] keyString;
		private buttonViewHolder holder;
		private int mselectItem = -1;
//		private String[]  str = new String[] {"numText", "xPosition", "yPosition","zPosition","hPosition", "lPosition"};
		private String[]  str = new String[] {"number", "xvalue", "yvalue","hvalue","zvalue", "lvalue"};
		
		ArrayList<HashMap<String, Object>> NcEditList = ArrayListBound.getNCeditList3Data();
		// MyAdapter的构造函数
		public TableAdapter(Context c, ArrayList<HashMap<String, Object>> appList,
				String[] from) {			
			mAppList = appList;
			Log.d("mpeng"," tableadapter create mAppList " +mAppList.size());
			mContext = c;
		
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			keyString = new String[from.length];
			System.arraycopy(from, 0, keyString, 0, from.length);

		}
		public void setSelectItem(int selectItem) {
			mselectItem = selectItem;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("mpeng"," the get view postion "+position);
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(R.layout.position_list_item, null);
				
				holder = new buttonViewHolder();
				holder.numText = (TextView) convertView
						.findViewById(R.id.numText);
			/*	holder.XText = (TextView) convertView
						.findViewById(R.id.XEditText);
				holder.YText = (TextView) convertView
						.findViewById(R.id.YEditText);
				holder.HText = (TextView) convertView
						.findViewById(R.id.HEditText);
				holder.ZText = (TextView) convertView
						.findViewById(R.id.ZEditText);
				holder.LText = (TextView) convertView
						.findViewById(R.id.LEditText);*/
				convertView.setTag(holder);
			}
			Log.d("mpeng"," the applist size is "+mAppList.size()+" pos :"+position);
			HashMap<String, Object> map_item = mAppList.get(position);
			if (map_item != null) {
				String numText =  map_item.get(keyString[0]).toString();
				String XText = map_item.get(keyString[1]).toString();
				String YText = map_item.get(keyString[2]).toString();
				String HText = map_item.get(keyString[3]).toString();
				String ZText = map_item.get(keyString[4]).toString();
				String LText = map_item.get(keyString[5]).toString();
				Log.d("mpeng","the get view "+ numText);
				holder.numText.setText(numText);
				holder.XText.setText(XText);
				holder.YText.setText(YText);
				holder.HText.setText(HText);
				holder.ZText.setText(ZText);
				holder.LText.setText(LText);
//				holder.numText.setOnClickListener)
/*				holder.XText.setOnClickListener(new setValueListener(position,1));
				holder.YText.setOnClickListener(new setValueListener(position,2));
				holder.ZText.setOnClickListener(new setValueListener(position,3));
				holder.HText.setOnClickListener(new setValueListener(position,4));
				holder.LText.setOnClickListener(new setValueListener(position,5));*/

		    	HashMap<String, Object> ListItem = new HashMap<String, Object>();	
//		    	for(int i = 0 ;i<NcEditList.size();i++)
//		    	{
//			    	ListItem = NcEditList.get(position);
//			    	String opText = ListItem.get("operatText").toString();
//			    	if(opText.contains("A1")){
//			    		
//			    	}else if(opText.contains("A2")){
//			    		
//			    	}else if(opText.contains("A3")){
//			    		
//			    	}else if(opText.contains("A4")){
//			    		
//			    	}else if(opText.contains("A5")){
//			    		
//			    	}
//		    	}
				if(XText.equals("")){
					holder.XText.setBackgroundColor(convertView.getResources().getColor(R.color.gray));					
				 	holder.XText.setClickable(false);	
				}else{
					holder.XText.setBackgroundColor(convertView.getResources().getColor(R.color.white));
					holder.XText.setClickable(false);	
				}
			
				if(YText.equals("")){
					holder.YText.setBackgroundColor(convertView.getResources().getColor(R.color.gray));					
				 	holder.YText.setClickable(false);	
				}else{
					holder.YText.setBackgroundColor(convertView.getResources().getColor(R.color.white));
					holder.YText.setClickable(false);	
				}
				
				if(HText.equals("")){
					holder.HText.setBackgroundColor(convertView.getResources().getColor(R.color.gray));					
				 	holder.HText.setClickable(false);	
				}else{
					holder.HText.setBackgroundColor(convertView.getResources().getColor(R.color.white));
					holder.HText.setClickable(false);	
				}
				
				if(ZText.equals("")){
					holder.ZText.setBackgroundColor(convertView.getResources().getColor(R.color.gray));					
				 	holder.ZText.setClickable(false);	
				}else{
					holder.ZText.setBackgroundColor(convertView.getResources().getColor(R.color.white));
					holder.ZText.setClickable(false);	
				}
				
				if(LText.equals("")){
					holder.LText.setBackgroundColor(convertView.getResources().getColor(R.color.gray));					
				 	holder.LText.setClickable(false);	
				}else{
					holder.LText.setBackgroundColor(convertView.getResources().getColor(R.color.white));
					holder.LText.setClickable(false);	
				}
			}
			
			if (position == mselectItem) {
//				holder.noteEditText.setTextColor(Color.RED);
	    	    convertView.setBackgroundColor(Color.RED);
            }else{//如果被选中，设置为红色

//            	holder.noteEditText.setTextColor(Color.BLACK);
            	convertView.setBackgroundColor(Color.BLACK);
			}
			return convertView;
		}
/*
		// 设定值行监听器
		class setValueListener implements android.view.View.OnClickListener {

			private int position;
			private int col;

			// 构造函数
			setValueListener(int pos,int c) {
				position = pos;
				col= c;
			}

			@Override
			public void onClick(final View v) {

				final EditText etEditText = new EditText(mContext);
				String setValueString = mAppList.get(position).get(
						keyString[col]).toString();
				etEditText.setHint("请输入数字");
				etEditText.setText(setValueString);
				etEditText.setSelection(setValueString.length());// 设置光标位置
				// 限制只能输入0~9的数字和点号
				etEditText.setKeyListener(new NumberKeyListener() {
					@Override
					protected char[] getAcceptedChars() {
						return new char[] { '1', '2', '3', '4', '5', '6', '7',
								'8', '9', '0', '.','+','-' };
					}

					@Override
					public int getInputType() {
						return android.text.InputType.TYPE_CLASS_NUMBER;// 数字键盘
					}
				});

				new AlertDialog.Builder(mContext)
				.setTitle("请添加设定值")
				.setView(etEditText)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0,
							int arg1) {
						String editString = etEditText
								.getText().toString();
						double editDouble=0;
						if (!editString.startsWith(".")) {
							if (editString.contains(".")) {
								String[] editStrings = editString.split("\\.");
								if (2 == editStrings.length) {
									editDouble = Double.parseDouble(editString);
									/*if (Math.abs(editDouble) >= 100000) {
										Toast.makeText(getActivity(),"数值超过范围，请重新输入",Toast.LENGTH_SHORT).show();
										editString = "";
									} else {*/
/*										editString = String.format("%1$+5.1f",editDouble);
									//}
								} else {
									Toast.makeText(mContext,"数字格式错误，请重新输入",Toast.LENGTH_SHORT).show();
									editString = "";
								}
							} /*else {
								editDouble = Double.parseDouble(editString);
								if (Math.abs(editDouble) >= 100000) {
									Toast.makeText(getActivity(),"数值超过范围，请重新输入",Toast.LENGTH_SHORT).show();
									editString = "";
								} else {
									//editString = String.format("%1$+5.1f",editDouble);
								}
							}
*/
/*						} else {
							Toast.makeText(mContext,"数字格式错误，请重新输入",Toast.LENGTH_SHORT).show();
							editString = "";
						}
						HashMap<String, Object> map = new HashMap<String, Object>();
						map = (HashMap<String, Object>) mAppList.get(position).clone();
	
						map.put(str[col],editString);	
						mAppList.set(position, map);
						notifyDataSetChanged();
					}

				}).setNegativeButton("取消", null).show();

			}
		}*/
	
}
