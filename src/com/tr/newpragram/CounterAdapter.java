/**
 * 
 */
package com.tr.newpragram;
import java.util.ArrayList;
import java.util.HashMap;

import com.tr.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class CounterAdapter extends BaseAdapter {

		private class buttonViewHolder {
			TextView numText;
			TextView counterText;


		}

		private ArrayList<HashMap<String, Object>> mAppList;
		private LayoutInflater mInflater;
		private Context mContext;
		private String[] keyString;
		private buttonViewHolder holder;	
		private int mselectItem = -1;
		// MyAdapter的构造函数
		public CounterAdapter(Context c, ArrayList<HashMap<String, Object>> appList,
				String[] from) {
			mAppList = appList;
			mContext = c;
		
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			keyString = new String[from.length];
			System.arraycopy(from, 0, keyString, 0, from.length);

		}

		public String[] getkeyString() {
			return this.keyString;
		}
		public void setSelectItem(int selectItem) {
			mselectItem = selectItem;
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
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(R.layout.counter_list_item, null);
				
				holder = new buttonViewHolder();
				holder.numText = (TextView) convertView
						.findViewById(R.id.numText);
				holder.counterText = (TextView) convertView
						.findViewById(R.id.vouleEditText);

				convertView.setTag(holder);
			}

			HashMap<String, Object> map_item = mAppList.get(position);
			if (map_item != null) {
				String numText =  map_item.get(keyString[0]).toString();
				String counterText = map_item.get(keyString[1]).toString();
				holder.numText.setText(numText);
				holder.counterText.setText(counterText);
				holder.counterText.setClickable(true);
				holder.counterText.setOnClickListener(new setValueListener(position));// 添加设定值行监听器
			}
			if (position == mselectItem) {
	    	    convertView.setBackgroundColor(Color.RED);
            }else{//如果被选中，设置为红色
            	convertView.setBackgroundColor(Color.BLACK);
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

				final EditText etEditText = new EditText(mContext);
				String setValueString = mAppList.get(position).get(
						keyString[1]).toString();
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
										editString = String.format("%1$+5.1f",editDouble);
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
						} else {
							Toast.makeText(mContext,"数字格式错误，请重新输入",Toast.LENGTH_SHORT).show();
							editString = "";
						}
						HashMap<String, Object> map = new HashMap<String, Object>();
						map = mAppList.get(position);
						map.put("value",editString);						
						mAppList.set(position, map);
						notifyDataSetChanged();
					}

				}).setNegativeButton("取消", null).show();

			}
		}
	
}
