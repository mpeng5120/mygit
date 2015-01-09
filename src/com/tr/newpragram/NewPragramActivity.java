package com.tr.newpragram;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.PositionQueryRunnable;
import wifiRunnablesAndChatlistener.SendDataRunnable;

import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.explain.HexDecoding;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.maintainguide.TR_MaintainGuide_Activity;
import com.tr.parameter_setting.TR_Parameter_Setting_Activity;
import com.tr.programming.Config;
import com.tr.programming.TR_Programming_Activity;
import com.wifiexchange.WifiSetting_Info;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.FragmentActivity;


import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
public class NewPragramActivity extends FragmentActivity implements TabListener {
	private UpdateFragmentReceiver FR = null;
	private final String TAG ="MainActivity";
	private Bundle mBundle;	
	private LinearLayout FragmentContainer;
	private LinearLayout FragmentDatil;
	private LinearLayout FragmentOther;
	private android.app.Fragment listFragment ;			
	private android.app.Fragment detailFragment ;
	private android.app.Fragment FragmentMoudle;	
	private android.app.Fragment FragmentExtra;	
	private android.app.Fragment FragmentFreeOption;	
	private android.app.Fragment FragmentAciton;	

	
	private Fragment detailSettingFragment;	
	
	

	private    FragmentTransaction FgTransaction;
	private Handler myHandler;
	private String myName;
	
	public static PositionQueryRunnable positionQueryRunnable;
	private Button ydBtn;
	private Button zdBtn;
	private Button dbjBtn;
	private Button dxhBtn;
	private TextView myMoudleInfo;
	private ArrayList<HashMap<String, Object>> list_mould_setting = ArrayListBound.getMouldDataListData();
	private static boolean clicked_btn_manual = true;
	private static boolean clicked_btn_origin = false;
	private static boolean clicked_btn_step = false;
	private static boolean clicked_btn_automatic = false;
	private static final int manual_ID = 10;
	
	private ArrayList<String> list_mouldname=new ArrayList<String>();
	private ArrayList<String> list_mouldnum=new ArrayList<String>();
	private int saveposition;
	private String saveStrname;
	private int listselectposition=0;
	public static ArrayList<HashMap<String, Object>> nctestlist = null;
	private AlertDialog nocationDialog;
	private  ActionBar actionBar;
	private AlertDialog  KeyMsgDialog;
	
	private WifiReadDataFormat formatReadusermode;
	private SendDataRunnable sendDatausermodeRunnable;
	private FinishRunnable sendDataFinishRunnable;
	private WifiSendDataFormat formatSendMessage;
	private SendDataRunnable sendDataRunnable;
	private byte[] getData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_main);
//				initData();
				FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);
				FragmentDatil = (LinearLayout) findViewById(R.id.detail_container);
				FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
				listFragment = new FragmentList();		
				detailFragment = new FragmentDetail();
//				otherFragment = new Fragments_Action();
				
//				FragmentManager fragmentManager;
//				FragmentTransaction fragmentTransaction;
//				fragmentManager = getSupportFragmentManager();
//				fragmentTransaction = fragmentManager.beginTransaction();
	ydBtn=(Button)findViewById(R.id.button1);
				zdBtn=(Button)findViewById(R.id.button3);
				dbjBtn=(Button)findViewById(R.id.button4);
				dxhBtn=(Button)findViewById(R.id.button5);
				ydBtn.setOnClickListener(mode_origin_listener);
				zdBtn.setOnClickListener(mode_automatic_listener);
				dbjBtn.setOnClickListener(mode_step_listener);
//				dxhBtn.setOnClickListener(mode_origin_listener);
				myMoudleInfo = (TextView) findViewById(R.id.ModleInfo);
				myMoudleInfo.setText(TR_Main_Activity.title_mouldData);
				initView();
//		//列表页面需要显示的Fragment
//				com.tr.newpragram.FragmentList listFragment = new FragmentList();				
//				FragmentManager fragmentManager1 = getSupportFragmentManager();
//				FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
//				fragmentTransaction1.add(R.id.list_container, listFragment);				
//				fragmentTransaction1.commit();
//				
//				//实例化详情Fragment
//				Fragment detailFragment = new FragmentDetail();				
//				//从列表页面传递需要的参数到详情页面				
//				final FragmentManager fragmentManager11 = getSupportFragmentManager();
//				final FragmentTransaction fragmentTransaction11 = fragmentManager11.beginTransaction();				
//				fragmentTransaction11.replace(R.id.detail_container, detailFragment);		
//				fragmentTransaction11.commit();
		//注册广播监听
		FR = new UpdateFragmentReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("gotoDetailSetting");
        filter.addAction("goPositionPreview");
        filter.addAction("highlight");
        filter.addAction("KeyMsg");
        registerReceiver(FR, filter);
	}
	
	
	
	/**
	 * 初始化组件
	 */

	

	//获取返回的数据后进行的UI操作
	private final Runnable originDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReadusermode.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadusermode.getFinalData() != null)  
			{
			   System.arraycopy(formatReadusermode.getFinalData(), 0, getData, 0, formatReadusermode.getLength());
			   int zjz=(int)(getData[0]&0x0F);
			   getData=HexDecoding.int2byte((int)(zjz|0x40));
			   try{
			    formatSendMessage=new WifiSendDataFormat(getData, 0x200000AF);
                sendDataRunnable=new SendDataRunnable(formatSendMessage, NewPragramActivity.this);
				sendDataFinishRunnable=new FinishRunnable(NewPragramActivity.this);
				sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));
				runOnUiThread(sendDataRunnable);
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		    }
		}
	};
	
	private final Runnable stepDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReadusermode.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadusermode.getFinalData() != null)  
			{
			   System.arraycopy(formatReadusermode.getFinalData(), 0, getData, 0, formatReadusermode.getLength());
			   int zjz=(int)(getData[0]&0x0F);
			   getData=HexDecoding.int2byte((int)(zjz|0x80));
			   try{
			    formatSendMessage=new WifiSendDataFormat(getData, 0x200000AF);
                sendDataRunnable=new SendDataRunnable(formatSendMessage, NewPragramActivity.this);
				sendDataFinishRunnable=new FinishRunnable(NewPragramActivity.this);
				sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));
				runOnUiThread(sendDataRunnable);
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		    }
		}
	};
	
	private final Runnable automaticDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReadusermode.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadusermode.getFinalData() != null)  
			{
			   System.arraycopy(formatReadusermode.getFinalData(), 0, getData, 0, formatReadusermode.getLength());
			   int zjz=(int)(getData[0]&0x0F);
			   getData=HexDecoding.int2byte((int)(zjz|0x20));
			   try{
			    formatSendMessage=new WifiSendDataFormat(getData, 0x200000AF);
                sendDataRunnable=new SendDataRunnable(formatSendMessage, NewPragramActivity.this);
				sendDataFinishRunnable=new FinishRunnable(NewPragramActivity.this);
				sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));
				runOnUiThread(sendDataRunnable);
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		    }
		}
	};
/**
 * 原点模式Listener
 */
public OnClickListener mode_origin_listener = new OnClickListener() {

	@Override
	public void onClick(View v) {
		if (WifiSetting_Info.mClient == null) {
			Toast.makeText(NewPragramActivity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!clicked_btn_origin) {
			clicked_btn_manual = false;
			clicked_btn_origin = true;
			clicked_btn_step = false;
			clicked_btn_automatic = false;
			// 删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.cancel(manual_ID);
		}
		
		formatReadusermode = new WifiReadDataFormat(0x200000AF,1);
		try {
			sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode,NewPragramActivity.this);
			sendDataFinishRunnable=new FinishRunnable(NewPragramActivity.this,originDataFinishTodo);
			sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));
			runOnUiThread(sendDatausermodeRunnable);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		//if(checked_origin_start_stop==false){
		new AlertDialog.Builder(NewPragramActivity.this)
		.setTitle(R.string.T_titlenotice).setMessage(R.string.T_executeydcz)
		.setPositiveButton(R.string.T_titleexecute,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				//checked_origin_start_stop = true;
				KeyCodeSend.send(5, NewPragramActivity.this);
			}

		}).setNegativeButton(R.string.T_titlecancel,null).show();
		/*}else{
			new AlertDialog.Builder(TR_Main_Activity.this)
			.setTitle("操作提示").setMessage("是否停止原点操作？")
			.setPositiveButton("停止",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					// TODO Auto-generated method stub
					checked_origin_start_stop = false;
					KeyCodeSend.send(5, TR_Main_Activity.this);
				}

			}).setNegativeButton("取消",null).show();
		}*/
			}
};

/**
 * 步进模式Listener
 */
public OnClickListener mode_step_listener = new OnClickListener() {

	@Override
	public void onClick(View v) {
		if (WifiSetting_Info.mClient == null) {
			Toast.makeText(NewPragramActivity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!clicked_btn_step) {
			clicked_btn_manual = false;
			clicked_btn_origin = false;
			clicked_btn_step = true;
			clicked_btn_automatic = false;
			// 删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.cancel(manual_ID);
		}
		formatReadusermode = new WifiReadDataFormat(0x200000AF,1);
		try {
			sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode,NewPragramActivity.this);
			sendDataFinishRunnable=new FinishRunnable(NewPragramActivity.this,stepDataFinishTodo);
			sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));
			runOnUiThread(sendDatausermodeRunnable);
		} catch (Exception e) {
			// TODO: handle exception
		}
		new AlertDialog.Builder(NewPragramActivity.this)
		.setTitle(R.string.T_titlenotice).setMessage(R.string.T_executebjcz)
		.setPositiveButton(R.string.T_titleonestepexecute,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				KeyCodeSend.send(5, NewPragramActivity.this);
			}

		}).setNegativeButton(R.string.T_titlecancel,null).show();

				}
};

/**
 * 自动模式Listener
 */
public OnClickListener mode_automatic_listener = new OnClickListener() {

	@Override
	public void onClick(View v) {
		if (WifiSetting_Info.mClient == null) {
			Toast.makeText(NewPragramActivity.this,"请先连接主机", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!clicked_btn_automatic) {
			clicked_btn_manual = false;
			clicked_btn_origin = false;
			clicked_btn_step = false;
			clicked_btn_automatic = true;
			// 删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			notificationManager.cancel(manual_ID);

		}
		formatReadusermode = new WifiReadDataFormat(0x200000AF,1);
		try {
			sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode,NewPragramActivity.this);
			sendDataFinishRunnable=new FinishRunnable(NewPragramActivity.this,automaticDataFinishTodo);
			sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));
			runOnUiThread(sendDatausermodeRunnable);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//if(checked_automatic_start_stop==false){
		new AlertDialog.Builder(NewPragramActivity.this)
		.setTitle(R.string.T_titlenotice).setMessage(R.string.T_executezdcz)
		.setPositiveButton(R.string.T_titleexecute,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				//checked_automatic_start_stop = true;
				KeyCodeSend.send(5, NewPragramActivity.this);
			}

		}).setNegativeButton(R.string.T_titlecancel,null).show();
	/*	}else{
			new AlertDialog.Builder(TR_Main_Activity.this)
			.setTitle("操作提示").setMessage("是否停止自动操作？")
			.setPositiveButton("停止",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					// TODO Auto-generated method stub
					checked_automatic_start_stop = false;
					KeyCodeSend.send(5, TR_Main_Activity.this);
				}

			}).setNegativeButton("取消",null).show();
		}*/
				}
};




	
	private void initView(){
		// 提示getActionBar方法一定在setContentView后面

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.addTab(actionBar.newTab().setText(R.string.moudle_data).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.move_teach).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.action_modify).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.hand_operation).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.other_setting).setTabListener(this));
		
	
		
	}
	
	
	
/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent it = getIntent(); 
		Runnable GotoFreeOption = new Runnable() {
			public void run() {
				actionBar.setSelectedNavigationItem(3);
			}
		} ;
		if(it.getFlags()==3)
		{
			Handler myh = new Handler();
			myh.postDelayed(GotoFreeOption, 20);
			
		}
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

    /* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("mpeng","onDestroy new pragram activity");
		unregisterReceiver(FR);
		if(nocationDialog!=null)
			nocationDialog.dismiss();
		super.onDestroy();
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		Log.d(TAG,"on destroy ");
//		unregisterReceiver(FR);
//		super.onDestroy();
//	}
////
	 private class UpdateFragmentReceiver extends BroadcastReceiver
	    {
	        @Override
	        public void onReceive(Context context, Intent intent)
	        {
	        
	            String action = intent.getAction();
	        	if(action.equals("KeyMsg"))
	        	{	        		
//	        		detailSettingFragment!=null||
		        	if(actionBar.getSelectedNavigationIndex()==1&&detailSettingFragment!=null&&detailSettingFragment.isAdded())
		        	{
		        		Log.e("mpeng","get action !!!!!!!!!");
		        		 byte[] keycode = new byte[2];
		            	 keycode = intent.getExtras().getByteArray("keycode");
		            	 KeyMsgHandle(keycode,context);
		            	 //
		        	}
		        	else if(actionBar.getSelectedNavigationIndex()==3)
		        	{
		        		Log.e("mpeng","get action !!!!!!!!!");
		        		 byte[] keycode = new byte[2];
		            	 keycode = intent.getExtras().getByteArray("keycode");
		            	 KeyMsgHandle(keycode,context);
		            	 //
		        	}
		        	else 
		        	{
		        		 byte[] keycode = new byte[2];
		            	 keycode = intent.getExtras().getByteArray("keycode");
		        		 int Key_Value = keycode[0] & 0xFF;//255
		        		 if(Key_Value==153)
		        			 return;
		        	    if(KeyMsgDialog != null)
			            	KeyMsgDialog.dismiss();
			             KeyMsgDialog = new AlertDialog.Builder(context).setTitle("提示")
			            .setMessage("是否要进行自由操作！")
			            .setNegativeButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								if(actionBar.getSelectedNavigationIndex()!=1)
								{
									actionBar.setSelectedNavigationItem(selTab.getPosition());
								}
							}
						})
			            .setPositiveButton("取消", null)
			            .show();
		        	}
	        	}else
	        	{
	        		
	            Bundle text = intent.getExtras();
	            int pos =  intent.getIntExtra("Clickposition", -1);
	        	Log.d("mpeng"," the pos is  333  "+text);
				mBundle = intent.getExtras();
				myName = intent.getStringExtra("name");
	            SwitchDetailFg(action,pos);
	        	}
	           
	        }
	    }

	 /* (non-Javadoc)
	 * @see android.app.Activity#recreate()
	 */

	private void SwitchDetailFg(String str,int pos)
	{
		
		if(str.equals("goPositionPreview"))
		{
			final   FragmentManager fragmentManager1 = this.getFragmentManager();
			final   FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();		
			Log.d(TAG,"SwitchDetailFg goPositionPreview");
			fragmentTransaction1.remove(detailSettingFragment);
			fragmentTransaction1.add(R.id.detail_container, detailFragment, null);
			fragmentTransaction1.commitAllowingStateLoss();	
		}
		else if(str.equals("gotoDetailSetting"))
		{
//			//从列表页面传递需要的参数到详情页面
			final   FragmentManager fragmentManager1 = this.getFragmentManager();
			final   FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();	
			detailSettingFragment = new FragmentOne();
			detailSettingFragment.setArguments(mBundle);
			fragmentTransaction1.remove(detailFragment);
			fragmentTransaction1.add(R.id.detail_container, detailSettingFragment, null);
			fragmentTransaction1.commitAllowingStateLoss();			
			Log.d(TAG,"SwitchDetailFg gotoDetailSetting:"+FgTransaction);		


		}else if(str.equals("highlight"))
		{
			Message msg = new Message();
			msg.obj = myName;
			msg.what = 1;
			if(myHandler!=null)
				myHandler.handleMessage(msg);
		}
		
	}
	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabReselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		if(unseltab==2&&Fragments_Action.getModifyStatus())
		{
		   
			if(FragmentAciton  !=null)   
        	{
        		Log.d("mpeng"," tabunselect 1111 :"+tab.getPosition());
        		
        		ft.remove(FragmentAciton);
        	}
		
			switch(tab.getPosition())
	        {
	        case 0:  
	        	if(FragmentContainer == null)
	        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);	
	   
	        	FragmentContainer.setVisibility(View.GONE);
	        	if(FragmentContainer == null)
	        		FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
	        	FragmentOther.setVisibility(View.VISIBLE);	
	
				if(FragmentMoudle!=null)
					ft.remove(FragmentMoudle);	
	
				FragmentMoudle = new Fragments_mouldData();
				ft.add(R.id.fragment_container1, FragmentMoudle, null);
	
	
	
	       	 break;
	        case 1:    
		
	        	if(FragmentContainer == null)
	        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);	
	   
	        	if(FragmentOther == null)
	        			FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
	        	
				FragmentOther.setVisibility(View.GONE);	
				FragmentContainer.setVisibility(View.VISIBLE);
	        	ft.add(R.id.list_container, listFragment, null);
	        	ft.add(R.id.detail_container, detailFragment, null);
	  
	
				
	       	 break;
	        case 2:
	        	
	        	if(FragmentContainer == null)
	        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);
	   
	        	FragmentContainer.setVisibility(View.GONE);
	        	if(FragmentContainer == null)
	        		FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
	        	FragmentOther.setVisibility(View.VISIBLE);	
	
				if(FragmentAciton!=null)
					ft.remove(FragmentAciton);	
	
				FragmentAciton = new Fragments_Action();
				ft.add(R.id.fragment_container1, FragmentAciton, null);
	
				fromDialog = false;
	       	 break;
	        case 3:
	    		Log.d("mpeng","the get position is "+tab.getPosition());	
	        	if(FragmentContainer == null)
	        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);	
	   
	        	FragmentContainer.setVisibility(View.GONE);
	        	if(FragmentContainer == null)
	        		FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
	        	FragmentOther.setVisibility(View.VISIBLE);	
	
				if(FragmentFreeOption!=null)
					ft.remove(FragmentFreeOption);	
	
				FragmentFreeOption = new Fragments_FreeOption();
				ft.add(R.id.fragment_container1, FragmentFreeOption, null);
	
	       	 break;
	        case 4:
	        	if(FragmentContainer == null)
	        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);	
	   
	        	FragmentContainer.setVisibility(View.GONE);
	        	if(FragmentContainer == null)
	        		FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
	        	FragmentOther.setVisibility(View.VISIBLE);	
	
				if(FragmentExtra!=null)
					ft.remove(FragmentExtra);	
	
				FragmentExtra = new Fragments_extras();
				ft.add(R.id.fragment_container1, FragmentExtra, null);
		
	
	       	break;
	        }
	 }
	}
	private int unseltab = -1;
	private boolean  fromDialog = false;
	private Tab selTab = null;	
	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabSelected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub		
		selTab = tab;
		if(unseltab == 2&&!fromDialog&&Fragments_Action.getModifyStatus())
		{		
			showDialog();
			return;
		}
			

		switch(tab.getPosition())
        {
        case 0:  
        	if(FragmentContainer == null)
        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);	
   
        	FragmentContainer.setVisibility(View.GONE);
        	if(FragmentContainer == null)
        		FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
        	FragmentOther.setVisibility(View.VISIBLE);	

			if(FragmentMoudle!=null)
				ft.remove(FragmentMoudle);	

			FragmentMoudle = new Fragments_mouldData();
			ft.add(R.id.fragment_container1, FragmentMoudle, null);



       	 break;
        case 1:    
	
        	if(FragmentContainer == null)
        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);	
   
        	if(FragmentOther == null)
        			FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
        	
			FragmentOther.setVisibility(View.GONE);	
			FragmentContainer.setVisibility(View.VISIBLE);
        	ft.add(R.id.list_container, listFragment, null);
        	ft.add(R.id.detail_container, detailFragment, null);
  

			
       	 break;
        case 2:
        	
        	if(FragmentContainer == null)
        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);
   
        	FragmentContainer.setVisibility(View.GONE);
        	if(FragmentContainer == null)
        		FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
        	FragmentOther.setVisibility(View.VISIBLE);	

			if(FragmentAciton!=null)
				ft.remove(FragmentAciton);	

			FragmentAciton = new Fragments_Action();
			ft.add(R.id.fragment_container1, FragmentAciton, null);

			fromDialog = false;
       	 break;
        case 3:
    		Log.d("mpeng","the get position is "+tab.getPosition());	
        	if(FragmentContainer == null)
        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);	
   
        	FragmentContainer.setVisibility(View.GONE);
        	if(FragmentContainer == null)
        		FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
        	FragmentOther.setVisibility(View.VISIBLE);	

			if(FragmentFreeOption!=null)
				ft.remove(FragmentFreeOption);	

			FragmentFreeOption = new Fragments_FreeOption();
			ft.add(R.id.fragment_container1, FragmentFreeOption, null);

       	 break;
        case 4:
        	if(FragmentContainer == null)
        		FragmentContainer = (LinearLayout) findViewById(R.id.tab2_layout);	
   
        	FragmentContainer.setVisibility(View.GONE);
        	if(FragmentContainer == null)
        		FragmentOther = (LinearLayout) findViewById(R.id.fragment_container1);
        	FragmentOther.setVisibility(View.VISIBLE);	

			if(FragmentExtra!=null)
				ft.remove(FragmentExtra);	

			FragmentExtra = new Fragments_extras();
			ft.add(R.id.fragment_container1, FragmentExtra, null);
	

       	break;
        }
	}
	/* (non-Javadoc)
	 * @see android.app.ActionBar.TabListener#onTabUnselected(android.app.ActionBar.Tab, android.app.FragmentTransaction)
	 */
	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub	
		Log.d("mpeng"," tabunselect :"+tab.getPosition());
		unseltab = tab.getPosition();
		if((unseltab == 2)&&!fromDialog&&Fragments_Action.getModifyStatus())
			return;
		switch(tab.getPosition())
        {
        case 0:   
        	if( FragmentMoudle  !=null)   
        	{
        		ft.remove(FragmentMoudle);
        		ft.detach(FragmentMoudle);
        	}
        	

       	 break;
        case 1:   
        	ft.remove(listFragment);
        	ft.remove(detailFragment);
        	if(detailSettingFragment!=null)
        		ft.remove(detailSettingFragment);
			
       	 break;
        case 2:
        	if( FragmentAciton  !=null)   
        	{
        		ft.remove(FragmentAciton);

        	}
       	 break;
        case 3:
        	if( FragmentFreeOption  !=null)   
        	{
        		ft.remove(FragmentFreeOption);

        	}
       	 break;
        case 4:
        	if( FragmentExtra  !=null)   
        	{
        		ft.remove(FragmentExtra);        	
        	}
       	break;
        }
	}

	public void setHandler(Handler hd)
	{
		myHandler = hd;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.setting_activity, menu);// 菜单
	}catch(Exception e){
		e.printStackTrace();
	}
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d("mpeng","the seltab.getpositon "+selTab.getPosition());
		if(selTab.getPosition() == 2&&!fromDialog&&Fragments_Action.getModifyStatus()){
			showDialog();		
			return false ;
		}
		switch (item.getItemId()) {
		case R.id.menu_activity_main:
//			Intent intent_main=new Intent();
//			intent_main.setClass(TR_Programming_Activity.this, TR_Main_Activity.class);
			startActivity(new Intent().setClass(NewPragramActivity.this, TR_Main_Activity.class));
			NewPragramActivity.this.finish();
			break;
		case R.id.menu_activity_programming:
			startActivity(new Intent().setClass(NewPragramActivity.this, TR_Programming_Activity.class));
			NewPragramActivity.this.finish();
			break;
		case R.id.menu_activity_parameter_setting:
			startActivity(new Intent().setClass(NewPragramActivity.this, TR_Parameter_Setting_Activity.class));
			break;
		case R.id.menu_activity_maintainGuide:
			startActivity(new Intent().setClass(NewPragramActivity.this,TR_MaintainGuide_Activity.class));
			break;
		default:
			// 对没有处理的事件，交给父类来处理
			return super.onOptionsItemSelected(item);
		}
		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
		return true;
	}
	
	
	/**
	 * 返回键直接回到主界面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
			//DelayTimerQueryRunnble.destroy();
			startActivity(new Intent().setClass(this, TR_Main_Activity.class));
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void backupFile(){
		if(nocationDialog!=null)
		nocationDialog.dismiss();
		final View view_newcreate=View.inflate(this, R.layout.tab_setting_mould_newcreate, null);
		new AlertDialog.Builder(this)
		.setTitle("另存为操作").setView(view_newcreate)
		.setPositiveButton("确定",
        new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) {
				//1 新建文件夹
				EditText editmouldnum=(EditText)view_newcreate.findViewById(R.id.editText_mouldnum);
				EditText editmouldname=(EditText)view_newcreate.findViewById(R.id.editText_mouldname);
				String editmouldnumstr=editmouldnum.getText().toString();
				String editmouldnamestr=editmouldname.getText().toString();
				if(editmouldnumstr.equals("")){
					Toast.makeText(NewPragramActivity.this, "模具数据编号为空，请重新输入！", Toast.LENGTH_SHORT).show();
					fromDialog = false;
					return;
				}
				Fragments_mouldData.checkMouldList(list_mouldnum, list_mould_setting,"num_mould_setting");
				if (list_mouldnum.contains(String.format("%1$04d",Integer.parseInt(editmouldnumstr)))) {
					Toast.makeText(NewPragramActivity.this, "模具数据编号重复，请重新输入",Toast.LENGTH_SHORT).show();
					fromDialog = false;
					return;
				}
				if(editmouldnamestr.equals("")){
					Toast.makeText(NewPragramActivity.this, "模具数据名称为空，请重新输入！", Toast.LENGTH_SHORT).show();
					fromDialog = false;
					return;
				}
				Fragments_mouldData.checkMouldList(list_mouldname, list_mould_setting,"name_mould_setting");
				if (list_mouldname.contains(editmouldnamestr.toLowerCase())) {
					Toast.makeText(NewPragramActivity.this, "模具数据名称重复，请重新输入",Toast.LENGTH_SHORT).show();
					fromDialog = false;
					return;
				}
				if (editmouldnamestr.length()>18) {
					Toast.makeText(NewPragramActivity.this, "模具数据名称长度超出范围，请重新输入",Toast.LENGTH_SHORT).show();
					fromDialog = false;
					return;
				}
				Constans.mouldData.createFolder(Constans.mouldDataPATH, editmouldnamestr.toString());
				
				//2，把RAW里面的模板复制进文件夹

				copyFile(Constans.mouldDataPATH+editmouldnamestr.toString(),"device");						
				copyFile(Constans.mouldDataPATH+editmouldnamestr.toString(),"userprg");						
				copyFile(Constans.mouldDataPATH+editmouldnamestr.toString(),"Userlog");
				
				HashMap<String, Object> mapadd = new HashMap<String, Object>();
				mapadd.put("num",	String.format("%1$04d", list_mould_setting.size()+1));//
				mapadd.put("num_mould_setting","");
				mapadd.put("name_mould_setting","");
				mapadd.put("note_mould_setting","");
				list_mould_setting.add(mapadd);
				
				int intAll=Fragments_mouldData.checkAllNum(list_mould_setting);
				final String date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());// 当前时间
				list_mould_setting.get(intAll).put("num_mould_setting",String.format("%1$04d",Integer.parseInt(editmouldnumstr)));
				list_mould_setting.get(intAll).put("name_mould_setting", editmouldnamestr);
				list_mould_setting.get(intAll).put("note_mould_setting", date2);
				
				Fragments_mouldData.saveMouldDataList(list_mould_setting,date2,intAll);
				listselectposition=intAll;
				WifiSetting_Info.wifiTimeOut=System.currentTimeMillis();
				WifiSetting_Info.progressDialog = ProgressDialog.show(NewPragramActivity.this, "另存程序中", "请等待", true, false); 
				
				TR_Main_Activity.sharedPreference_openedDir.edit().putString("dir_num", editmouldnumstr.toString().trim()).commit();
				TR_Main_Activity.sharedPreference_openedDir.edit().putString("dir_name", editmouldnamestr.toString().trim()).commit();
				TR_Main_Activity.sharedPreference_openedDir.edit().putString("dir_time", date2).commit();
				

				saveStrname=editmouldnamestr.toString().trim();
				saveposition=listselectposition;
				
				 new Thread()
	              {
	                  public void run()
	                  {
	                	//3，把NC保存进模板里面 关掉PROGRESSDIALOG
	                		final String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());// 当前时间
	                	    saveAs_NcEdit(saveStrname, date);
	                	  	Fragments_mouldData.saveAs_DeviceDefine(saveStrname, date);
	                	  	Fragments_mouldData.saveAs_TableEdit(saveStrname, date);
	                		if(WifiSetting_Info.progressDialog !=null)
	                		{
	                			WifiSetting_Info.progressDialog.dismiss();
	                			WifiSetting_Info.progressDialog = null;
	                		}
	                  }
	              }.start();
				
			}
		}).setNegativeButton("取消",null).show();
	
	}
	
	private void  copyFile(String path,String name)
	{
		 try{
			String FileName =  path + "/" + name+".trt";
             File dir = new File(path);            
             if (!dir.exists())
                 dir.mkdir(); 
	             if (!(new File(FileName)).exists())
	             {
	            	 InputStream is = null;
	            	 if(name.equals("device"))
	            		     is = getResources().openRawResource(R.raw.device);
	            	 else if(name.equals("userprg"))
	            		 	 is = getResources().openRawResource(R.raw.userprg);
	            	 else if(name.equals("Userlog"))
	            			 is = getResources().openRawResource(R.raw.userlog);
	            	 
	            	 if(is == null)
	            		 return;
	            			 
	                 FileOutputStream fos = new FileOutputStream(FileName);
	                 byte[] buffer = new byte[8192];
	                 int count = 0;
	                 while ((count = is.read(buffer)) > 0)
	                 {
	                     fos.write(buffer, 0, count);
	                 }
	                 fos.close();
	                 is.close();
	             }
             }
		 catch (Exception e)
		{ 
			 new AlertDialog.Builder(NewPragramActivity.this)
			 	  .setTitle("错误报告")
			      .setMessage("无法复制！")
			      .setPositiveButton("确定",
			                     new DialogInterface.OnClickListener(){
			                             public void onClick(DialogInterface dialoginterface, int i){
			                                                                        }
			                      })
			      .show();
		}
	}
	
	private   void saveAs_NcEdit(String foldername, String date) {

		Constans.mouldData.createFolder(Constans.mouldDataPATH, foldername).createFile(Constans.nc文本数据);
		//		Constans.dataToDownload.createFolder(Constans.mouldDataPATH+ foldername + File.separator, Constans.dataToDownloadFolder);
		Constans.mouldData.cleanFile(Constans.nc文本数据);
		Fragments_mouldData.saveAs_8nc(foldername,
				ArrayListBound.getNCeditList1Data(),
				"[NCEdit1]", date);
		Constans.mouldData.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		Fragments_mouldData.saveAs_8nc(foldername,
				ArrayListBound.getNCeditList2Data(),
				"[NCEdit2]", date);
		Constans.mouldData.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		Fragments_mouldData.saveAs_8nc(foldername,
				nctestlist,
				"[NCEdit3]", date);
		Constans.mouldData.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		Fragments_mouldData.saveAs_8nc(foldername,
				ArrayListBound.getNCeditList4Data(),
				"[NCEdit4]", date);
		Constans.mouldData.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		Fragments_mouldData.saveAs_8nc(foldername,
				ArrayListBound.getNCeditList5Data(),
				"[NCEdit5]", date);
		Constans.mouldData.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		Fragments_mouldData.saveAs_8nc(foldername,
				ArrayListBound.getNCeditList6Data(),
				"[NCEdit6]", date);
		Constans.mouldData.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		Fragments_mouldData.saveAs_8nc(foldername,
				ArrayListBound.getNCeditList7Data(),
				"[NCEdit7]", date);
		Constans.mouldData.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		Fragments_mouldData.saveAs_8nc(foldername,
				ArrayListBound.getNCeditList8Data(),
				"[NCEdit8]", date);
		Constans.mouldData.writeFile(Constans.nc文本数据, "\r\n");// 不同段之间空一行
		
	}	

	private void showDialog()
	{
		fromDialog = true;
		nocationDialog = new AlertDialog.Builder(NewPragramActivity.this).setTitle(R.string.T_titlenotice)
		   .setMessage("当前程序未保存，是否备份?")
		   .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				backupFile();	
				actionBar.setSelectedNavigationItem(selTab.getPosition());
				
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				fromDialog = true;
				actionBar.setSelectedNavigationItem(selTab.getPosition());
			}
		}).show();
		nocationDialog.setCancelable(false);
	}
//	public static File getFileFromBytes(byte[] b, String outputFile) { 
//        File ret = Constans.mouldData.createFolder(Constans.mouldDataPATH, "8864").createFile("nc"); 
//        BufferedOutputStream stream = null; 
//        
//        try {
//               ret = new File(outputFile); 
//               FileOutputStream fstream = new FileOutputStream(ret); 
//               stream = new BufferedOutputStream(fstream); 
//               stream.write(b);
//        } catch (Exception e) { 
////            log.error("helper:get file from byte process error!"); 
//            e.printStackTrace(); 
//        } finally { 
//            if (stream != null) { 
//                try { 
//                    stream.close(); 
//                } catch (IOException e) { 
//                    e.printStackTrace(); 
//                } 
//            } 
//        } 
//        return ret; 
//    }
//	
	  private void KeyMsgHandle(byte[]  kc ,Context context)
	  {
	 		 int Key_Value = kc[0] & 0xFF;//255
	 		 int Key_Function = kc[1] & 0xFF;//255	 		
	 		 switch(Key_Value)
	 		 {
	 		 	 case 11:  //HOME		 	
	 		 	 case 14:		 	
			 		KeyCodeSend.send(999, NewPragramActivity.this);
				 break;
	 		 	 case 12:	//Z-  L-
	 		 		 if(Key_Function==0)
	 		 		 {
	 		 			
	 		 			 if(Config.SelectArmId == R.id.ProductArm){
	 		 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					 KeyCodeSend.send(927, NewPragramActivity.this);	//0.1		 					
	 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(928, NewPragramActivity.this);	//1.0
	 		 				 }
	 		 				 
	 		 				 
	 		 			 }else{
	 		 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					 KeyCodeSend.send(947, NewPragramActivity.this);	//0.1		 					
	 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(948, NewPragramActivity.this);	//1.0
	 		 				 }
	 		 				
	 		 				 
	 		 			 }
	 		 		 }
	 		 		 break;
	 		 	case 24:	//Z+  L+
	 		 		 if(Key_Function==0)
	 		 		 {
	 		 			 if(Config.SelectArmId == R.id.ProductArm){
	 		 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					 KeyCodeSend.send(925, NewPragramActivity.this);	//0.1		 					
	 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(926, NewPragramActivity.this);	//1.0
	 		 				 }
	 		 				
	 		 				 
	 		 			 }else{
	 		 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					 KeyCodeSend.send(945, NewPragramActivity.this);	//0.1		 					
	 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(946, NewPragramActivity.this);	//1.0
	 		 				 }
	 		 				
	 		 				 
	 		 			 }
	 		 		 }
	 		 		 break;
	 		 	 case 34: //Y h -
	 		 		 if(Key_Function==0)
	 		 		 {
	 		 			 if(Config.SelectArmId == R.id.ProductArm){
	 		 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					
	 		 					 KeyCodeSend.send(917, NewPragramActivity.this);	//0.1		 					
	 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(918, NewPragramActivity.this);	//1.0
	 		 				 }
	 		 				 
	 		 				 
	 		 			 }else{ //h -
	 		 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					 KeyCodeSend.send(937, NewPragramActivity.this);	//0.1		 					
	 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(938, NewPragramActivity.this);	//1.0
	 		 				 }
	 		 				
	 		 				 
	 		 			 }
	 		 		 }
	 		 		 break;
	 		 	 case 13: //Y+   h
	 		 		 if(Key_Function==0)
	 		 		 {
	 		 			 if(Config.SelectArmId == R.id.ProductArm){   //Y+	 		 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					 KeyCodeSend.send(915, NewPragramActivity.this);	//0.1		 					
	 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(916, NewPragramActivity.this);	//1.0
	 		 				 }	 		 				 
	 		 			 }else{                                 //H+	 		 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					 KeyCodeSend.send(935, NewPragramActivity.this);	//0.1 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(936, NewPragramActivity.this);	//1.0 		 				 
	 		 			     }
	 		 			 }
	 		 		 }
	 		 		 break;
	 		 	 
	 		 	 case 33:	
	 		 		 if(Key_Function==0)
	 		 		 {
	 		 		
	 		 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					 KeyCodeSend.send(907, NewPragramActivity.this);	//0.1		 					
	 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(908, NewPragramActivity.this);	//1.0
	 		 				 }

	 		 				 
	 		 			
	 		 		 }
	 		 		 break;
	 		 	 case 23:		
	 		 		 if(Key_Function==0)
	 		 		 {	 				 
	 		 				 if( Config.SelectSpeedId==R.id.speedBtnFour)
	 		 				 {
	 		 					 KeyCodeSend.send(905, NewPragramActivity.this);	//0.1
	 		 					 
	 		 					 
	 		 				 }else if( Config.SelectSpeedId==R.id.speedBtnFive)
	 		 				 {
	 		 					 KeyCodeSend.send(906, NewPragramActivity.this);	//1.0
	 		 				 }				 
	 		 			
	 		 		 }
	 		 		 break; 		 
	 		 		 
	 			 case 32:
			 		 KeyCodeSend.send(26, NewPragramActivity.this);		 		 
			 		 break;
			 	 case 22:
			 		KeyCodeSend.send(25, NewPragramActivity.this);
			 		 break;
			 	 case 31:
			 		KeyCodeSend.send(24, NewPragramActivity.this);
			 		 break;
			 	 case 21:
			 		KeyCodeSend.send(23, NewPragramActivity.this);
			 		 break;	
	 		 }
	 		
	 	}
}
