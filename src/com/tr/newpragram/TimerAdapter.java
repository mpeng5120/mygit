/**
 * 
 */
package com.tr.newpragram;
import java.util.ArrayList;
import java.util.HashMap;

import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.SendDataRunnable;

import com.dataInAddress.AddressPublic;
import com.explain.HexDecoding;
import com.explain.TableToBinary;
import com.tr.R;

import android.app.Activity;
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
public class TimerAdapter extends BaseAdapter {

		private class buttonViewHolder {
			TextView numText;
			TextView timerText;
		}

		private ArrayList<HashMap<String, Object>> mAppList;
		private LayoutInflater mInflater;
		private Activity mContext;
		private String[] keyString;
		private buttonViewHolder holder;	
		private int mselectItem = -1;
		private double timeData;
		private int tempposition;
		// MyAdapter的构造函数
		public TimerAdapter(Activity c, ArrayList<HashMap<String, Object>> appList,
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

		@Override
		public int getCount() {
			return mAppList.size();
		}
		public void setSelectItem(int selectItem) {
			mselectItem = selectItem;
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
				convertView = mInflater.inflate(R.layout.timer_list_item, null);
				
				holder = new buttonViewHolder();
				holder.numText = (TextView) convertView
						.findViewById(R.id.numText);
				holder.timerText = (TextView) convertView
						.findViewById(R.id.vouleEditText);
				convertView.setTag(holder);
			}

			HashMap<String, Object> map_item = mAppList.get(position);
			if (map_item != null) {
				String numText =  map_item.get(keyString[0]).toString();
				String timerText = map_item.get(keyString[1]).toString();

				holder.numText.setText(numText);
				holder.timerText.setText(timerText);
				holder.timerText.setClickable(true);
				holder.timerText.setOnClickListener(new setValueListener(position));// 添加设定值行监听器
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
				etEditText.setHint("支持格式为##.##的数值，整数最多2位，小数最多2位");
				etEditText.setText(setValueString);
				etEditText.setSelection(setValueString.length());// 设置光标位置
				// 限制只能输入0~9的数字和点号
				etEditText.setKeyListener(new NumberKeyListener() {
					@Override
					protected char[] getAcceptedChars() {
						return new char[] { '1', '2', '3', '4', '5', '6', '7',
								'8', '9', '0', '.'};
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
								.getText().toString().trim();
						if (editString.equals("")) {
							Toast.makeText(mContext, "数据为空，请重新输入", Toast.LENGTH_SHORT).show();
						    return;
						}
						
							if (editString.contains(".")) {//可能是小数
								String[] editStrings=editString.split("\\.");
								if (2==editStrings.length) {//是正确的小数

									double editDouble=Double.parseDouble(editString);
									if (Math.abs(editDouble)>99.99) {
										Toast.makeText(mContext, "数据超过范围，请重新输入", Toast.LENGTH_SHORT).show();
										editString = "";
										return;
									}else {
										editString=String.format("%1$2.2f", editDouble);
									}

								}else {
									Toast.makeText(mContext, "数据格式错误，请重新输入", Toast.LENGTH_SHORT).show();
									editString = "";
									return;
								}
								
							}else {//只有整数
								double editDouble=Double.parseDouble(editString);
								if (Math.abs(editDouble)>99.99) {
									Toast.makeText(mContext, "数据超过范围，请重新输入", Toast.LENGTH_SHORT).show();
									editString = "";
									return;
								}else{
									editString=String.format("%1$2.2f", editDouble);
								}
							}

						
						try {
							tempposition=position;
							timeData=Double.valueOf(editString);
							//这里查询的定时器变量为在listview中的个数
							WifiSendDataFormat sendDataFormat=new WifiSendDataFormat(
									HexDecoding.int2byteArray4((int)(timeData*100)),
									AddressPublic.model_Timer_Head+4*TableToBinary.searchAddress(mAppList.get(position)
											.get(keyString[0]).toString(), false));
							SendDataRunnable sendDataRunnable=new SendDataRunnable(sendDataFormat,mContext);
							FinishRunnable finishRunnable=new FinishRunnable(mContext, "发送完成",backMessageTodoRunnable);
							sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, finishRunnable));
							
							mContext.runOnUiThread(sendDataRunnable);
							
						} catch (Exception e) {
							// TODO: handle exception
							Toast.makeText(mContext, "请输入正确的数据", Toast.LENGTH_SHORT).show();
						}
						
	
					
					}

				}).setNegativeButton("取消", null).show();

			}
			
		}

		private Runnable backMessageTodoRunnable=new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try{
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(keyString[0],
						mAppList.get(tempposition)
								.get(keyString[0])
								.toString());//
				map.put(keyString[1], timeData);
				mAppList.set(tempposition, map);
				notifyDataSetChanged();
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
				
			}
		};
}
