package com.tr.maintainguide;

import java.io.File;

import wifiRunnablesAndChatlistener.KeyCodeSend;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dbutils.Constans;
import com.dbutils.OpenFiles;
import com.tr.ExitTR;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.main.VerticalSeekBar;
import com.tr.main.VerticalSeekBarListener;
import com.tr.newpragram.NewPragramActivity;
import com.tr.parameter_setting.TR_Parameter_Setting_Activity;
import com.tr.programming.TR_Programming_Activity;
import com.wifiexchange.WifiSetting_Info;

public class TR_MaintainGuide_Activity extends Activity {

	public static boolean alreadyLogin=false;
	
	public static Button wifi_led;
	//private AlarmQueryRunnable alarmQueryRunnable;
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("destroy");
		//alarmQueryRunnable.destroy();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(WifiSetting_Info.mClient!=null){
			wifi_led.setVisibility(View.VISIBLE);
        }
		System.out.println("TR_Setting_Activity   onResume");
		//alarmQueryRunnable = new AlarmQueryRunnable(TR_MaintainGuide_Activity.this);
		//Thread a2 = new Thread(alarmQueryRunnable);
		//a2.start();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		ExitTR.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tr_maintain);
	
		
		ActionBar bar = getActionBar();
		bar.setDisplayShowTitleEnabled(false);// 不显示app名
		bar.setDisplayHomeAsUpEnabled(false);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


		bar.addTab(bar.newTab().setText("维护监视类").setTabListener(new MyTabListener(new Fragments_maintain())));
		bar.addTab(bar.newTab().setText("指南").setTabListener(new MyTabListener(new Fragments_guide())));

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
				openThisFileFromSD("maintainguideactivity.pdf");
				KeyCodeSend.send(6, TR_MaintainGuide_Activity.this);
			}
		});
		
		
		
		//seekbar初始化
		VerticalSeekBar seekbar = (VerticalSeekBar) findViewById(R.id.seekbar);
		if(seekbar==null){return;}
        seekbar.setMax(500);//最大档位
        seekbar.initProgress();//默认档位
        seekbar.setOnSeekBarChangeListener(new VerticalSeekBarListener(TR_MaintainGuide_Activity.this));
        
        getActionBar().setDisplayShowTitleEnabled(true);
	}
	class ButtonListener implements OnClickListener, OnTouchListener{  
		   public boolean onTouch(View v, MotionEvent event) {  
		            if(v.getId() == R.id.stop){    
		                if(event.getAction() == MotionEvent.ACTION_DOWN){ 
		                	KeyCodeSend.send(999, TR_MaintainGuide_Activity.this);
		                }  
		            }  
		            return false;  
		        }

		@Override
		public void onClick(View v) {
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
			try{
			if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingPdf))) {
				intent = OpenFiles.getPdfFileIntent(currentPath);
				startActivity(intent);
			} else {
				Toast.makeText(TR_MaintainGuide_Activity.this, "无法打开该文件，请安装相应的软件！",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
				// TODO: handle exception
			Toast.makeText(TR_MaintainGuide_Activity.this, "无法打开该文件，请安装相应的软件！",
					Toast.LENGTH_SHORT).show();
			}
		} else {// 不存在该文件
			Toast.makeText(TR_MaintainGuide_Activity.this, "对不起，不存在该文件！", Toast.LENGTH_SHORT)
					.show();
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
	class MyTabListener implements TabListener{
		private Fragment fragment;

		public MyTabListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO 自动生成的方法存根
			
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO 自动生成的方法存根
			ft.add(R.id.fragment_container_setting,fragment, null);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO 自动生成的方法存根
			ft.remove(fragment);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.maintain_guide_activity, menu);// 菜单
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_activity_main:
			startActivity(new Intent().setClass(TR_MaintainGuide_Activity.this, TR_Main_Activity.class));
			break;
		case R.id.menu_activity_programming:
			startActivity(new Intent().setClass(TR_MaintainGuide_Activity.this, TR_Programming_Activity.class));
			break;
		case R.id.menu_activity_setting:
			startActivity(new Intent().setClass(TR_MaintainGuide_Activity.this,NewPragramActivity.class));
			break;
		case R.id.menu_activity_parameter_setting:
			startActivity(new Intent().setClass(TR_MaintainGuide_Activity.this, TR_Parameter_Setting_Activity.class));
			break;
		default:
			// 对没有处理的事件，交给父类来处理
			return super.onOptionsItemSelected(item);
		}
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
	
	

}
