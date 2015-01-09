package com.tr.newpragram;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import wifiProtocol.WifiReadDataFormat;
import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.DelayCount;
import wifiRunnablesAndChatlistener.DelayoptionQueryRunnble;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;
import wifiRunnablesAndChatlistener.posccalmQueryRunnable;

import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.dbutils.OpenFiles;
import com.explain.HexDecoding;
import com.tr.R;
import com.tr.programming.Config;
import com.wifiexchange.WifiSetting_Info;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Fragments_FreeOption extends Fragment {

	private ListView freeListView_setting;
	public static ArrayList<HashMap<String, Object>> freeList_setting = new ArrayList<HashMap<String, Object>>();// 表格要绑定的list
	public MyAdapter freeList_Adapter;

	private static ListView optionListView_setting;
	private ArrayList<HashMap<String, Object>> optionList_setting = new ArrayList<HashMap<String, Object>>();

	public OptionAdapter option_Adapter;
	
	private static int selectedItem = 0;
	// ------------
	public static DelayCount delayCount;
	public static  DelayCount tempCount;
	private ImageView imageView1;
	
	private byte[] getData;
	protected WifiReadDataFormat formatReadfree;
	protected SendDataRunnable sendDatafreeRunnable;
	private FinishRunnable sendDataFinishRunnable;
	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;
	private posccalmQueryRunnable  posccalmQueryRunnable;
	
	private DelayoptionQueryRunnble dqr ;
	private Thread delayQueryThread ;
	private Handler myHandler;
	
	private RadioGroup ArmSelect;
	private RadioButton ProductArmBtn;
	private RadioButton FeedArmBtn;
	
	private RadioGroup SpeedSelect;
	private RadioButton SpeedLow;
	private RadioButton SpeedMid;
	private RadioButton SpeedHigh;
	private RadioButton SpeedZero;
	private RadioButton SpeedTen;
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getActivity().getIntent().putExtra("zyczsetting", false);
		System.out.println("onPause1");
        if(delayCount!=null)
        	delayCount.destroy();
		
		 if(tempCount!=null)
			 tempCount.destroy();
			
		 System.out.println("关闭delayCount1");
//		 if(alarmQueryRunnable!=null)
//			 alarmQueryRunnable.destroy();
//		 if(ledrunnable!=null)
//			 ledrunnable.destroy();
		 if(posccalmQueryRunnable!=null)
			 posccalmQueryRunnable.destroy();
		 
		Config.SelectSpeedId = -1;
		Config.SelectArmId = - 1;
		DelayoptionQueryRunnble.destroy();
		System.out.println("关闭alarmQueryRunnable1");
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
		System.out.println("Fragments_free   alarmQueryRunnable");
		if (WifiSetting_Info.mClient == null) {
			Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
			return;
		}
//		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
//	    Thread a2 = new Thread(alarmQueryRunnable);
//		a2.start();
//		ledrunnable=new ledRunnable(getActivity());
//		Thread a4 = new Thread(ledrunnable);
//		a4.start();
		
		posccalmQueryRunnable = new posccalmQueryRunnable(getActivity());
		Thread a3 = new Thread(posccalmQueryRunnable);
		a3.start();
		
		formatReadfree = new WifiReadDataFormat(0x1000B234,4);
		try {
			
			sendDatafreeRunnable=new SendDataRunnable(formatReadfree, getActivity());

			sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readDataFinishTodo);

			sendDatafreeRunnable.setOnlistener(new NormalChatListenner(sendDatafreeRunnable, sendDataFinishRunnable));

			getActivity().runOnUiThread(sendDatafreeRunnable);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
		}

		dqr = new DelayoptionQueryRunnble(getActivity(), optionListView_setting);
	    delayQueryThread=new Thread(dqr) ;
	    delayQueryThread.start();
		optionListView_setting.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				myHandler.sendEmptyMessageDelayed(111, 500);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		initialoptionList(optionList_setting);
		
		option_Adapter = new OptionAdapter(getActivity(),
				optionList_setting, R.layout.tab_setting_option_item,
				new String[] {"fgf","button_option1", "isymbol1", "osymbol1"}, new int[] {
						R.id.fgf,R.id.button_option1, R.id.isymbol1, R.id.osymbol1});

		optionListView_setting.setAdapter(option_Adapter);
		
		optionListView_setting.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				try{
					option_Adapter.setSelectItem(position);
					option_Adapter.notifyDataSetChanged();
					
						final String valueString=optionList_setting.get(position).get("button_option1").toString().trim();
						if(!valueString.equals("")){
							new AlertDialog.Builder(getActivity())
							.setTitle("提示").setMessage("确定执行"+valueString+"操作？")
							.setPositiveButton("确定",
				            new DialogInterface.OnClickListener() 
							{
								public void onClick(DialogInterface dialog, int which) {
									int valueint=0;
									for(int i=0;i<ArrayListBound.getDeviceOptionalListData().size();i++){
										String button_option1String = ArrayListBound.getDeviceOptionalListData().get(i).get("name1").toString();
									    if(button_option1String.equals(valueString)){
									    	valueint=Integer.valueOf(ArrayListBound.getDeviceOptionalListData().get(i).get("addressText").toString());
									    }
									}
									 KeyCodeSend.send((valueint-1)+201, getActivity());
								}
							}).setNegativeButton("取消",null).show();
							
						}
			    }catch(Exception e){
				  e.printStackTrace();
			    }

			}

		});
	}catch(Exception e){
		e.printStackTrace();
	}
	}

	//获取返回的数据后进行的UI操作
	private final Runnable readDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			try{
			getData=new byte[formatReadfree.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadfree.getFinalData() != null)  
			{
			System.arraycopy(formatReadfree.getFinalData(), 0, getData, 0, formatReadfree.getLength());
			//重新给list_usermode列表赋值
	
			    initialFreeList(freeList_setting,getData);
				freeList_Adapter = new MyAdapter(getActivity(), freeList_setting,
						R.layout.setting_free_item,
						new String[] { "axle_free_setting", "currentPos_free_setting",
								"originLimit_free_setting", }, new int[] {
								R.id.axle_free_setting, R.id.currentPos_free_setting,
								R.id.originLimit_free_setting });
				freeListView_setting.setAdapter(freeList_Adapter);
				

				delayCount = new DelayCount(500, 100, getActivity(),
						freeListView_setting, "freePoint");
				if (delayCount == null) {
					return;
				}
				delayCount.start();
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		}

	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Fragments_free   onCreate");
		if(getActivity().getIntent().getBooleanExtra("zyczsetting", false)==true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		myHandler = new Handler(){

			/* (non-Javadoc)
			 * @see android.os.Handler#dispatchMessage(android.os.Message)
			 */
			@Override
			public void dispatchMessage(Message msg) {
				// TODO Auto-generated method stub
				super.dispatchMessage(msg);
				if(!delayQueryThread.isAlive())
				{
				dqr = new DelayoptionQueryRunnble(getActivity(), optionListView_setting);
			    delayQueryThread=new Thread(dqr) ;
			    delayQueryThread.start();
				}
				else 
				{
					myHandler.sendEmptyMessageDelayed(111, 500);
				}
			}
			
			
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Fragments_free   onCreateView");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		View view = inflater.inflate(R.layout.tab_setting_free_new, container,
				false);

		freeListView_setting = (ListView) view
				.findViewById(R.id.listView_free_setting);
		if (freeListView_setting == null) {
			return null;
		}
		
		optionListView_setting = (ListView) view
				.findViewById(R.id.listView_option);
		if(optionListView_setting==null){
			return null;
		}
		return view;

	}
	@Override
	public void onStart(){
		super.onStart();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try{
		System.out.println("Fragments_free   onActivityCreated");
        imageView1=(ImageView) getActivity().findViewById(R.id.imageView1);
        
        ArmSelect = (RadioGroup) getActivity().findViewById(R.id.armRdioGroup);
        ProductArmBtn = (RadioButton) getActivity().findViewById(R.id.ProductArm);
        FeedArmBtn = (RadioButton) getActivity().findViewById(R.id.FeedArm);
        
        ArmSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				Config.SelectArmId = checkedId;
				if(checkedId == ProductArmBtn.getId())
					imageView1.setBackgroundResource(R.drawable.posv1);
				else
					imageView1.setBackgroundResource(R.drawable.posv);
			}
		});
        ArmSelect.check(ProductArmBtn.getId()); 
        SpeedSelect = (RadioGroup) getActivity().findViewById(R.id.speedRadioGroup);
        SpeedLow =  (RadioButton) getActivity().findViewById(R.id.speedBtnOne);
        SpeedMid =  (RadioButton) getActivity().findViewById(R.id.speedBtnTwo);
        SpeedHigh =  (RadioButton) getActivity().findViewById(R.id.speedBtnThree);
        SpeedTen =  (RadioButton) getActivity().findViewById(R.id.speedBtnFour);
        SpeedZero =  (RadioButton) getActivity().findViewById(R.id.speedBtnFive);
        
        SpeedSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				Config.SelectSpeedId = checkedId;				
			}
		});
        SpeedSelect.check(SpeedLow.getId());
    	
		/**
		 * 表格监听器，选中项红色表示
		 */
		freeListView_setting.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 选中红色表示
				selectedItem = position;
				switch (position) {
				case 0:
					KeyCodeSend.send(111, getActivity());
					break;
				case 1:
					KeyCodeSend.send(112, getActivity());
					break;
				case 2:
					KeyCodeSend.send(113, getActivity());
					break;
				case 3:
					KeyCodeSend.send(114, getActivity());
					break;
				case 4:
					KeyCodeSend.send(115, getActivity());
					break;
				default:
					break;
				}

				freeList_Adapter.setSelectItem(position);
				freeList_Adapter.notifyDataSetChanged();
			}
		});

		/**
		 * 默认选中，红色表示
		 */
		if (freeList_setting.size() != 0) {
			freeListView_setting.setSelection(selectedItem);
			freeList_Adapter.setSelectItem(selectedItem);
			freeList_Adapter.notifyDataSetChanged();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	/**
	 * 
	* @Title: openThisFile 
	* @Description: 通过调用OpenFiles类返回的Intent，打开相应的文件;文件暂时都默认放在 rawPATH下
	* @param string    
	* @return void    
	* @throws
	 */
	public void openThisFileFromSD(String filename) {

		File currentPath = new File(Constans.rawPATH + filename);
		
		if (currentPath != null && currentPath.isFile()){// 存在该文件
			String fileName = currentPath.toString();
			Intent intent;
			if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingImage))) {
				intent = OpenFiles.getImageFileIntent(currentPath);
				startActivity(intent);
			}else {
				Toast.makeText(getActivity(), "无法打开该文件，请安装相应的软件！",
						Toast.LENGTH_SHORT).show();
			}
		} else {// 不存在该文件
			Toast.makeText(getActivity(), "对不起，不存在该文件！", Toast.LENGTH_SHORT)
					.show();
			;
		}
	}
	/**
	 * 
	* @Title: checkEndsWithInStringArray 
	* @Description: 定义用于检查要打开的文件的后缀是否在遍历后缀数组中
	* @param checkItsEnd
	* @param fileEndings
	* @return    
	* @return boolean    
	* @throws
	 */
	private boolean checkEndsWithInStringArray(String checkItsEnd,
			String[] fileEndings) {
		for (String aEnd : fileEndings) {
			if (checkItsEnd.endsWith(aEnd))
				return true;
		}
		return false;
	}
	private void initialFreeList(ArrayList<HashMap<String, Object>> arrayList,byte[] getData) {

			arrayList.clear();
			HexDecoding.printHexString("读数据========", getData);
		    HashMap<String, Object> map0 = new HashMap<String, Object>();
			map0.put("axle_free_setting", "走行(X)");
			map0.put("currentPos_free_setting", "*****.*");
			map0.put("originLimit_free_setting", (int)((getData[3]>>0)&0x01));
			//freeList_setting.set(0, map0);
			freeList_setting.add(map0);
		    HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("axle_free_setting", "制品前后(Y)");
			map1.put("currentPos_free_setting", "*****.*");
			map1.put("originLimit_free_setting", (int)((getData[3]>>1)&0x01));
			//freeList_setting.set(1, map1);
			freeList_setting.add(map1);
		    HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("axle_free_setting", "料道前后(Ys)");
			map2.put("currentPos_free_setting", "*****.*");
			map2.put("originLimit_free_setting", (int)((getData[3]>>2)&0x01));
			//freeList_setting.set(2, map2);
			freeList_setting.add(map2);
		    HashMap<String, Object> map3 = new HashMap<String, Object>();
			map3.put("axle_free_setting", "制品上下(Z)");
			map3.put("currentPos_free_setting", "*****.*");
			map3.put("originLimit_free_setting", (int)((getData[3]>>3)&0x01));
			//freeList_setting.set(3, map3);
			freeList_setting.add(map3);
		    HashMap<String, Object> map4 = new HashMap<String, Object>();
			map4.put("axle_free_setting", "料道上下(Zs)");
			map4.put("currentPos_free_setting", "*****.*");
			map4.put("originLimit_free_setting", (int)((getData[3]>>4)&0x01));
			//freeList_setting.set(4, map4);
		    freeList_setting.add(map4);

	}

	private void initialoptionList(ArrayList<HashMap<String, Object>> optionList) {

		try{
			optionList.clear();
		for (int i = 0; i <32; i++) {
			String addressString = ArrayListBound.getDeviceOptionalListData().get(i).get("addressText").toString();
			String button_option1String = ArrayListBound.getDeviceOptionalListData().get(i).get("name1").toString().trim();
			String isymbol1String = ArrayListBound.getDeviceOptionalListData().get(i).get("name2").toString();
			String osymbol1String = ArrayListBound.getDeviceOptionalListData().get(i).get("name3").toString();
			//if(!button_option1String.equals("")){
				HashMap<String, Object> map0 = new HashMap<String, Object>();
				map0.put("fgf", addressString);
				map0.put("button_option1", button_option1String);
				map0.put("isymbol1", isymbol1String);
				map0.put("osymbol1", osymbol1String);
				optionList.add(map0);
		//	}
				
		}

		if (optionList.size() == 0) {
			Toast.makeText(getActivity(), "数据为空", Toast.LENGTH_SHORT).show();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	private class MyAdapter extends BaseAdapter {
		private class buttonViewHolder {
			TextView axle_free_setting;
			TextView currentPos_free_setting;
			TextView originLimit_free_setting;
		}

		private ArrayList<HashMap<String, Object>> mAppList;
		private LayoutInflater mInflater;
		private Context mContext;
		private String[] keyString;
		private int[] valueViewID;
		private buttonViewHolder holder;
		private int mLayoutID;
		private int mselectedItem = -1;

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

		// 选中红色表示
		public void setSelectItem(int selectItem) {
			this.mselectedItem = selectItem;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();
			} else {
				convertView = mInflater.inflate(mLayoutID, null);
				holder = new buttonViewHolder();
				holder.axle_free_setting = (TextView) convertView
						.findViewById(valueViewID[0]);
				holder.currentPos_free_setting = (TextView) convertView
						.findViewById(valueViewID[1]);
				holder.originLimit_free_setting = (TextView) convertView
						.findViewById(valueViewID[2]);

				convertView.setTag(holder);
			}

			HashMap<String, Object> appInfo = mAppList.get(position);
			if (appInfo != null) {
				String axle_free_setting =  appInfo.get(keyString[0]).toString();
				String currentPos_free_setting =  appInfo.get(keyString[1]).toString();
                String originLimit_free_setting= appInfo.get(keyString[2]).toString();
				holder.axle_free_setting.setText(axle_free_setting);
				holder.currentPos_free_setting.setText(currentPos_free_setting);
                if(originLimit_free_setting.equals("1")){
                	holder.originLimit_free_setting.setText("ON");
                	holder.originLimit_free_setting.setBackgroundColor(Color.RED);
                }else{
                	holder.originLimit_free_setting.setText("OFF");
                	holder.originLimit_free_setting.setBackgroundColor(Color.GRAY);
                }
			}

			// 选中红色显示
			if (position == mselectedItem) {
				// 如果当前的行就是ListView中选中的一行，就更改显示样式
				convertView.setBackgroundColor(Color.RED);
			} else {
				convertView.setBackgroundColor(Color.BLACK);
			}

			return convertView;

		}
	}
	
	public class OptionAdapter extends BaseAdapter {
		private class buttonViewHolder {
			TextView fgf;
			TextView button_option1;
			TextView isymbol1;
			TextView osymbol1;

		}

		private ArrayList<HashMap<String, Object>> mAppList;
		private Context mContext;
		private LayoutInflater mInflater;
		private String[] keyString;
		private int[] valueViewID;
		private buttonViewHolder holder;
		private int selectItem = -1;
		public OptionAdapter(Context c, ArrayList<HashMap<String, Object>> appList,
				int resource, String[] from, int[] to) {
			// TODO 自动生成的构造函数存根
			mAppList = appList;
			mContext = c;
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

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();			
				
			} else {
				convertView = mInflater.inflate(R.layout.tab_setting_option_item,
						null);
				holder = new buttonViewHolder();
				holder.fgf=(TextView) convertView.findViewById(valueViewID[0]);
				holder.button_option1 = (TextView) convertView.findViewById(valueViewID[1]);
				holder.isymbol1 = (TextView) convertView.findViewById(valueViewID[2]);
				holder.osymbol1 = (TextView) convertView.findViewById(valueViewID[3]);
				
				convertView.setTag(holder);
			}

			HashMap<String, Object> appInfo = mAppList.get(position);

			if (appInfo != null) {
				String fgfText = appInfo.get(keyString[0]).toString().trim();
				String button_option1Text = appInfo.get(keyString[1]).toString().trim();
				String isymbol1Text = appInfo.get(keyString[2]).toString().trim();
				String osymbol1Text = appInfo.get(keyString[3]).toString().trim();
				
				holder.fgf.setText(fgfText);
				holder.button_option1.setText(button_option1Text);
				holder.isymbol1.setText(isymbol1Text);
				holder.osymbol1.setText(osymbol1Text);
				// 选中红色显示
				if (position == selectItem) {
					// 如果当前的行就是ListView中选中的一行，就更改显示样式
					convertView.setBackgroundColor(Color.RED);
				} else {
					convertView.setBackgroundColor(Color.BLACK);
				}
			}

			return convertView;
		}
	}

}
