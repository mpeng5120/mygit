/**
 * 
 */
package com.tr.newpragram;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tr.R;
import com.tr.R.color;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.NumberKeyListener;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
public class ListAdapter extends BaseAdapter {

		private class buttonViewHolder {
			TextView numText;
			TextView praText;
			}

		private ArrayList<HashMap<String, Object>> mAppList;
		private LayoutInflater mInflater;
		private Context mContext;
		private String[] keyString;
		private buttonViewHolder holder;	
		private String CurSelName;
		private int mselectItem = -1;
		// MyAdapter的构造函数
		public ListAdapter(Context c, ArrayList<HashMap<String, Object>> appList
				) {
			mAppList = appList;
			mContext = c;		
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}
		public ListAdapter(Context c, ArrayList<HashMap<String, Object>> appList,String CurName
				) {
			mAppList = appList;
			mContext = c;		
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			CurSelName = CurName;

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
	   	private boolean find_name(String rege,String str){
			   String regex = "[^A-Za-z0-9]"+rege+"[^A-Za-z0-9]";
			   str=" "+str+" ";
			   boolean result=false;
			   try{
			   Pattern pat = Pattern.compile(regex);  
			   Matcher matcher = pat.matcher(str); 
			   while (matcher.find()) { 
				   result=true;
			   } 
		      }catch(Exception e){
			   e.printStackTrace();
		      }
			   return result;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {			
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(R.layout.pragram_list_item, null);
				
				holder = new buttonViewHolder();
				holder.numText = (TextView) convertView
						.findViewById(R.id.LineText);
				holder.praText = (TextView) convertView
						.findViewById(R.id.praEditText);
				convertView.setTag(holder);
			}

			HashMap<String, Object> map_item = mAppList.get(position);
			if (map_item != null) {
				String numText =  map_item.get("addressText").toString();
				String pragram =  map_item.get("noteEditText").toString();
				holder.numText.setText(String.valueOf(position));
				holder.praText.setText(pragram);				
				if(CurSelName!=null){
						
					int index = pragram.indexOf(CurSelName);
					if(find_name(CurSelName,pragram))
					{		
							SpannableStringBuilder style=new SpannableStringBuilder(pragram); 
							style.setSpan(new BackgroundColorSpan(Color.RED),index,CurSelName.length()+index,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置textview的背景颜色
					        style.setSpan(new ForegroundColorSpan(Color.YELLOW),index,CurSelName.length()+index,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); //设置指定位置文字的颜色 
					        holder.praText.setText(style);
							
					}
				}
				
				
//				if (position == mselectItem) {	
//					
//					holder.numText.setBackgroundColor(Color.GREEN);
//		    	    holder.praText.setBackgroundColor(Color.GREEN);
//					
//	            }else{//如果被选中，设置为红色	          
//					holder.numText.setBackgroundColor(Color.GRAY);
//		    	    holder.praText.setBackgroundColor(Color.WHITE);
//				}
// 添加设定值行监听器
			}
			
			
			return convertView;
		}

		// 设定值行监听器

	
}
