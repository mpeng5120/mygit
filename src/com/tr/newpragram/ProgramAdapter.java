/**
 * 
 */
package com.tr.newpragram;
import java.util.List;

import com.tr.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
public class ProgramAdapter extends BaseAdapter {

		private class buttonViewHolder {
			TextView numText;
		}

		private List<String> mAppList;
		private LayoutInflater mInflater;
		private Context mContext;
		private buttonViewHolder holder;
		private int selectItem ;
		// MyAdapter的构造函数
		public ProgramAdapter(Context c,  List<String>  appList) {
			mAppList = appList;
			mContext = c;
			mInflater = LayoutInflater.from(mContext);
		}
	
		@Override
		public int getCount() {
			return mAppList.size();
		}

		@Override
		public Object getItem(int position) {
			return mAppList.get(position);
		}

		public void setSelection(int pos)
		{
			Log.d("mpeng","the pos is"+pos);
			this.selectItem = pos;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(R.layout.list_item, null);
				
				holder = new buttonViewHolder();
				holder.numText = (TextView) convertView
						.findViewById(R.id.Tex1);
				convertView.setTag(holder);
			}
		
			if (!mAppList.isEmpty()) {
				String numText =  mAppList.get(position);
				holder.numText.setText(numText);
			}
		
			if(position == selectItem)
			{
				Log.d("mpeng","the select item is "+selectItem);
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
			}
			else{
				convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
			}

			return convertView;
		}


	
}
