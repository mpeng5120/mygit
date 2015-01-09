
package com.tr.parameter_setting;

import java.util.HashMap;

import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;

import com.dbutils.ArrayListBound;
import com.tr.ExitTR;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.main.VerticalSeekBar;
import com.tr.main.VerticalSeekBarListener;
import com.tr.maintainguide.TR_MaintainGuide_Activity;
import com.tr.newpragram.NewPragramActivity;
import com.tr.programming.TR_Programming_Activity;
import com.wifiexchange.WifiSetting_Info;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * 
* @ClassName: TR_Parameter_Setting_Activity 
* @Description: 参数设定类界面
* @author 李婷婷
* @date 2013-4-11 上午9:05:26 
*
 */
public class TR_Parameter_Setting_Activity extends Activity implements TabListener{

	HashMap<String, Fragment> fragmentList_parameter_setting=new HashMap<String, Fragment>();
	public Fragment fragmentShowing_parameter_setting;//当前显示的fragmentList_parameter_setting
	public static VerticalSeekBar  seekbar;
	public static Button wifi_led;
	//private AlarmQueryRunnable alarmQueryRunnable;
//	public static boolean machanicalParameter=true;//解决切换tab时自动再次判断checkbox的bug；
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("TR_Parameter_Setting_Activity onPause");
		//alarmQueryRunnable.destroy();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(WifiSetting_Info.mClient!=null){
			wifi_led.setVisibility(View.VISIBLE);
        }
		System.out.println("TR_Parameter_Setting_Activity onResume");
	/*	alarmQueryRunnable = new AlarmQueryRunnable(TR_Parameter_Setting_Activity.this);
		Thread a2 = new Thread(alarmQueryRunnable);
		a2.start();*/
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		super.onCreate(savedInstanceState);	
		System.out.println("TR_Parameter_Setting_Activity onCreate");
		ExitTR.getInstance().addActivity(this);
		setContentView(R.layout.tr_parameter_setting);
		init_TR_Parameter_Setting();
	}
	
	/**
	 * 
	* @Title: init_TR_Parameter_Setting 
	* @Description: 初始化
	* @param    
	* @return void    
	* @throws
	 */
	private void init_TR_Parameter_Setting() {
		wifi_led=(Button)findViewById(R.id.wifi_led);
		if(wifi_led==null){return;}
		
		ImageButton btn_stop = (ImageButton) findViewById(R.id.stop);
		if(btn_stop==null){return;}
		ButtonListener b = new ButtonListener();           
		btn_stop.setOnClickListener(b);  
		btn_stop.setOnTouchListener(b); 
		
		ImageButton btn_f1 = (ImageButton) findViewById(R.id.f1);
		if(btn_f1==null){return;}
		btn_f1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				KeyCodeSend.send(6, TR_Parameter_Setting_Activity.this);
			}
		});
        seekbar = (VerticalSeekBar) findViewById(R.id.seekbar);
        if(seekbar==null){return;}
        seekbar.setMax(500);//最大档位
        seekbar.initProgress();//默认档位
        seekbar.setOnSeekBarChangeListener(new VerticalSeekBarListener(TR_Parameter_Setting_Activity.this));
		
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		getActionBar().addTab( getActionBar().newTab().setText(R.string.parameterSetting_mechanicalPara).setTabListener(this));
		getActionBar().addTab( getActionBar().newTab().setText(R.string.parameterSetting_servoPara).setTabListener(this));
		getActionBar().addTab( getActionBar().newTab().setText(R.string.parameterSetting_downloadManagement).setTabListener(this));
		getActionBar().addTab( getActionBar().newTab().setText(R.string.parameterSetting_otherFunction).setTabListener(this));
		
		fragmentList_parameter_setting.put("Mechanical_Parameter", new Fragments_Mechanical_Parameter());//暂时先用2
		fragmentList_parameter_setting.put("Servo_Parameter", new Fragments_Servo_Parameter());
		fragmentList_parameter_setting.put("Download_Management", new Fragments_download_management());
		fragmentList_parameter_setting.put("Other_Functions", new Fragments_Other_Functions());
		
		if (null == fragmentShowing_parameter_setting) {  
			fragmentShowing_parameter_setting = fragmentList_parameter_setting.get("Mechanical_Parameter");  
        }  
		getFragmentManager().beginTransaction().add(R.id.fragment_parameter_setting, fragmentShowing_parameter_setting).commit();   
		
		
		
	}
	class ButtonListener implements OnClickListener, OnTouchListener{  
		   public boolean onTouch(View v, MotionEvent event) {  
		            if(v.getId() == R.id.stop){    
		                if(event.getAction() == MotionEvent.ACTION_DOWN){ 
		                	KeyCodeSend.send(999, TR_Parameter_Setting_Activity.this);
		                }  
		            }  
		            return false;  
		        }

		@Override
		public void onClick(View v) {
		}  
		          
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if(tab.getText().equals(getString(R.string.parameterSetting_mechanicalPara))){
			if(fragmentShowing_parameter_setting!=null){
				getFragmentManager().beginTransaction().add(R.id.fragment_parameter_setting, fragmentList_parameter_setting.get("Mechanical_Parameter"), null).commit();
				fragmentShowing_parameter_setting=fragmentList_parameter_setting.get("Mechanical_Parameter");
			}	

		}else if(tab.getText().equals(getString(R.string.parameterSetting_servoPara))){
			getFragmentManager().beginTransaction().add(R.id.fragment_parameter_setting, fragmentList_parameter_setting.get("Servo_Parameter"), null).commit();
			fragmentShowing_parameter_setting=fragmentList_parameter_setting.get("Servo_Parameter");
			ArrayListBound.setTabChange_false();
		}else if (tab.getText().equals(getString(R.string.parameterSetting_downloadManagement))) {
			getFragmentManager().beginTransaction().add(R.id.fragment_parameter_setting, fragmentList_parameter_setting.get("Download_Management"), null).commit();
			fragmentShowing_parameter_setting=fragmentList_parameter_setting.get("Download_Management");
			ArrayListBound.setTabChange_false();
		}else if (tab.getText().equals(getString(R.string.parameterSetting_otherFunction))) {
			getFragmentManager().beginTransaction().add(R.id.fragment_parameter_setting, fragmentList_parameter_setting.get("Other_Functions"), null).commit();
			fragmentShowing_parameter_setting=fragmentList_parameter_setting.get("Other_Functions");
			ArrayListBound.setTabChange_false();
		}
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if(fragmentShowing_parameter_setting!=null){
			getFragmentManager().beginTransaction().remove(fragmentShowing_parameter_setting).commit();
		}		
	}

	/**
	 * 菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.parameter_setting_activity, menu);//菜单
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		//响应每个菜单项(通过菜单项的ID)
		case R.id.menu_activity_main:
//			Intent intent_main=new Intent();
//			intent_main.setClass(TR_Programming_Activity.this, TR_Main_Activity.class);
			startActivity(new Intent().setClass(TR_Parameter_Setting_Activity.this, TR_Main_Activity.class));
			break;
		case R.id.menu_activity_programming:
			startActivity(new Intent().setClass(TR_Parameter_Setting_Activity.this, TR_Programming_Activity.class));
			break;
		case R.id.menu_activity_setting:
			startActivity(new Intent().setClass(TR_Parameter_Setting_Activity.this, NewPragramActivity.class));
			break;
		case R.id.menu_activity_maintainGuide:
			startActivity(new Intent().setClass(TR_Parameter_Setting_Activity.this,TR_MaintainGuide_Activity.class));
			break;
//		case R.id.menu_Exit:
//			
//			break;
			
		}
		return true;
	}

	
	/**
	 * 返回键直接回到主界面
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0) {
			startActivity(new Intent().setClass(this, TR_Main_Activity.class));
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	


}
