package com.tr;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dbutils.Constans;
import com.tr.main.TR_Main_Activity;
import com.tr.programming.Config;
import com.wifiexchange.Client;
import com.wifiexchange.WifiSetting_Info;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class TR_Welcome_Activity extends Activity{
	  // private ImageView  imageView = null;    
	private BootCastReceiver BCR = null;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		super.onCreate(savedInstanceState);
		//无title     
		  requestWindowFeature(Window.FEATURE_NO_TITLE);    

		//全屏     
		  getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);  

		//设置布局文件，在布局文件里设置个图片
		setContentView(R.layout.tr_welcome);
		BCR = new BootCastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("bootComplete");
        registerReceiver(BCR, filter);
        boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if(sdCardExist)		
		{
			try {
				WifiSetting_Info.mClient = Client.connect();
			} catch (IOException e) {
				// TODO Auto-generated catch block				
				e.printStackTrace();
			}
			new Thread(WifiSetting_Info.mClient).start();			
			Handler x=new Handler();
			x.postDelayed(new welcomehandler(), 2000);
		}		
		/*setContentView(R.layout.tr_welcome1);   
        imageView = (ImageView)findViewById(R.id.welcome_image_view);   
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);   
        alphaAnimation.setFillEnabled(true); //启动Fill保持   
        alphaAnimation.setFillAfter(true);  //设置动画的最后一帧是保持在View上面   
        imageView.setAnimation(alphaAnimation);   
        alphaAnimation.setAnimationListener(this);  //为动画设置监听 
*/	
		/*setContentView(R.layout.tr_welcome1); 
		imageView =(ImageView)findViewById(R.id.welcome_image_view);//放置的ImageView控件
		//设置动画背景   
		imageView.setBackgroundResource(R.anim.welcome_alpha);//其中R.anim.animation_list就是上一步准备的动画描述文件的资源名   
		//获得动画对象   
		AnimationDrawable _animaition = (AnimationDrawable)imageView.getBackground();  
		//最后，就可以启动动画了，代码如下：  
		//是否仅仅启动一次？   
		_animaition.setOneShot(false);  
		if(_animaition.isRunning())//是否正在运行？   
		{  
		_animaition.stop();//停止   
		}  	
		_animaition.start();//启动
	
		Handler x=new Handler();
		x.postDelayed(new welcomehandler(), 6000);
		}*/
		SharedPreferences preferences = getSharedPreferences("count",MODE_WORLD_READABLE); 
		int count = preferences.getInt("count", 0); 

		if (count == 0) { 
			/**
			    * 添加快捷方式到桌面 要点：  
			    * 1.给Intent指定action="com.android.launcher.INSTALL_SHORTCUT"
			    * 2.给定义为Intent.EXTRA_SHORTCUT_INENT的Intent设置与安装时一致的action(必须要有)  
			    * 3.添加权限:com.android.launcher.permission.INSTALL_SHORTCUT
			    */
			       Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			       // 不允许重建
			      shortcut.putExtra("duplicate", false);
			       // 设置名字
			      shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,this.getString(R.string.tr_activity_main));
			       // 设置图标
			      shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(this,
			                       R.drawable.tra));
			       // 设置意图和快捷方式关联程序
			      shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,new Intent(this, this.getClass()).setAction(Intent.ACTION_MAIN));
			       // 发送广播
			      sendBroadcast(shortcut); 
        
		}
    }
    
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(BCR);
		super.onDestroy();
	}

	class welcomehandler implements Runnable{
		public void run(){
			startActivity(new Intent(getApplication(),TR_Main_Activity.class));
			TR_Welcome_Activity.this.finish();
		}
	}
	
	 //利用广播+全局状态位来控制  LED的显示
    private class BootCastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();          
            if(action.equals("bootComplete"))
            {
//            	收到开机完成的广播后再去初始这个
            	Log.d("mpeng","start thread");
            	Config.firstCreate  =false;	
				try {
					WifiSetting_Info.mClient = Client.connect();
				} catch (IOException e) {
					// TODO Auto-generated catch block				
						e.printStackTrace();
					}
					new Thread(WifiSetting_Info.mClient).start();			
					Handler x=new Handler();
					x.postDelayed(new welcomehandler(), 1);
				boolean sdCardExist = Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED);
				Log.d("mpeng","xxxxxxx sdcardexit is"+sdCardExist);

            }
            
        }
    }

}
