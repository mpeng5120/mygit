package com.tr.newpragram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;
import wifiRunnablesAndChatlistener.posccalmQueryRunnable;

import com.dbutils.ArrayListBound;
import com.explain.NCTranslate;
import com.tr.R;
import com.tr.programming.TR_Programming_Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 列表Fragment
 * @author mining
 *
 */
public class FragmentList extends Fragment {
	
	private ArrayList<HashMap<String, Object>> mDataSourceList = new ArrayList<HashMap<String, Object>>();
	private List<FragmentTransaction> mBackStackList = new ArrayList<FragmentTransaction>();
	private ListView listView;
	private ListAdapter myAdapte;
	private Button DelBtn = null;
	private Button AddBtn = null;
	private Button DownLoadBtn = null;
	private Activity myActivity;

	private ledRunnable ledrunnable;
	private AlarmQueryRunnable alarmQueryRunnable;
	private posccalmQueryRunnable  posccalmQueryRunnable;
	
	ArrayList<HashMap<String, Object>> NcEditList = ArrayListBound.getNCeditList3Data();
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("destroy");
//		alarmQueryRunnable.destroy();
		ledrunnable.destroy();
		posccalmQueryRunnable.destroy();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.fragment_list_layout, container, false);

	}
	public  void showNote(){
		NCTranslate.noteList.clear();
		TR_Programming_Activity.noteflag=3;
		int[] test  =NCTranslate.beginTranslate(getActivity(),
				getResources().getStringArray(R.array.IF2));
		try{
			NcEditList= ArrayListBound.getNCeditList3Data();
			//Log.d("mpeng"," ncdedit size is" +NcEditList.get(0).toString());
		for(int i=0;i<NcEditList.size();i++){
			if(NcEditList.get(i).get("operatText").equals("")&&NcEditList.get(i).get("orderSpinner").equals(""))
				continue;
			NcEditList.get(i).put("noteEditText", NCTranslate.noteList.get(i));
			myAdapte.notifyDataSetChanged();
			
		}
		TR_Programming_Activity.noteflag=0;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	public Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Log.d("mpeng","the pos name is "+msg.obj.toString());
				setHighLight(msg.obj.toString());
				break;
			}
		};
	};
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		 DelBtn= (Button)getActivity().findViewById(R.id.deleteBtn);
		 AddBtn = (Button)getActivity().findViewById(R.id.insertBtn);
		 DownLoadBtn = (Button)getActivity().findViewById(R.id.downLoadBtn);
		//添加数据到ListView
//		for(int i=0, count=20; i<count; i++){
//			mDataSourceList.add("列表数据" + i);
//		}
		 mDataSourceList = ArrayListBound.getNCeditList3Data();
		
		//列表页面的ListView
		listView = (ListView) getActivity().findViewById(R.id.fragment_list);
		myAdapte  = new ListAdapter(getActivity(), mDataSourceList);
		listView.setAdapter(myAdapte);
		myAdapte.notifyDataSetChanged();
		listView.setClickable(false);
		
		
	}
	

	public void updateMoveList(ArrayList<String> ppositionList,ArrayList<String> spositionList,ArrayList<String> fpositionList,ArrayList<HashMap<String, Object>> arrayList) {
		try{
		ppositionList.clear();
		spositionList.clear();
		fpositionList.clear();
		for (int i = 0; i < arrayList.size(); i++) {
			String posString=(String) arrayList.get(i).get("symbolNameEditText");
			String signalstr=(String) arrayList.get(i).get("signalNameEditText");
			if (posString.contains("SP")&&arrayList.get(i).get("setItem").toString().equals("1")) {
				spositionList.add(posString+"<—-—"+signalstr+"—-—>");
			}else if (posString.contains("FP")&&arrayList.get(i).get("setItem").toString().equals("1")) {
				fpositionList.add(posString+"<—-—"+signalstr+"—-—>");
			}else if (posString.contains("P")&&arrayList.get(i).get("setItem").toString().equals("1")) {
				ppositionList.add(posString+"<—-—"+signalstr+"—-—>");
			}
		}	

	}catch(Exception e){
		e.printStackTrace();
	}
	}	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
//		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
//		Thread a2 = new Thread(alarmQueryRunnable);
//		a2.start();
		ledrunnable=new ledRunnable(getActivity());
		Thread a4 = new Thread(ledrunnable);
		a4.start();
		posccalmQueryRunnable = new posccalmQueryRunnable(getActivity());
		Thread a3 = new Thread(posccalmQueryRunnable);
		a3.start();
		}catch(Exception e){
			e.printStackTrace();
		}
		showNote();		
	}
	/* (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivity= activity;
		((NewPragramActivity) myActivity).setHandler(mHandler);
		
	}
	/**
	 * 
	 * @param msg
	 */
	private void showTost(String msg){
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}

	
	private void setHighLight(String name)
	{
		myAdapte  = new ListAdapter(getActivity(), mDataSourceList,name);
		listView.setAdapter(myAdapte);
		for(int i =0;i<mDataSourceList.size();i++)
		{
			if(mDataSourceList.get(i).get("noteEditText").toString().contains(name))
			{
				listView.setSelection(i);				
				break;
			}
		}
		myAdapte.notifyDataSetChanged();

	}
	

}
