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
		//��title     
		  requestWindowFeature(Window.FEATURE_NO_TITLE);    

		//ȫ��     
		  getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);  

		//���ò����ļ����ڲ����ļ������ø�ͼƬ
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
        alphaAnimation.setFillEnabled(true); //����Fill����   
        alphaAnimation.setFillAfter(true);  //���ö��������һ֡�Ǳ�����View����   
        imageView.setAnimation(alphaAnimation);   
        alphaAnimation.setAnimationListener(this);  //Ϊ�������ü��� 
*/	
		/*setContentView(R.layout.tr_welcome1); 
		imageView =(ImageView)findViewById(R.id.welcome_image_view);//���õ�ImageView�ؼ�
		//���ö�������   
		imageView.setBackgroundResource(R.anim.welcome_alpha);//����R.anim.animation_list������һ��׼���Ķ��������ļ�����Դ��   
		//��ö�������   
		AnimationDrawable _animaition = (AnimationDrawable)imageView.getBackground();  
		//��󣬾Ϳ������������ˣ��������£�  
		//�Ƿ��������һ�Σ�   
		_animaition.setOneShot(false);  
		if(_animaition.isRunning())//�Ƿ��������У�   
		{  
		_animaition.stop();//ֹͣ   
		}  	
		_animaition.start();//����
	
		Handler x=new Handler();
		x.postDelayed(new welcomehandler(), 6000);
		}*/
		SharedPreferences preferences = getSharedPreferences("count",MODE_WORLD_READABLE); 
		int count = preferences.getInt("count", 0); 

		if (count == 0) { 
			/**
			    * ��ӿ�ݷ�ʽ������ Ҫ�㣺  
			    * 1.��Intentָ��action="com.android.launcher.INSTALL_SHORTCUT"
			    * 2.������ΪIntent.EXTRA_SHORTCUT_INENT��Intent�����밲װʱһ�µ�action(����Ҫ��)  
			    * 3.���Ȩ��:com.android.launcher.permission.INSTALL_SHORTCUT
			    */
			       Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
			       // �������ؽ�
			      shortcut.putExtra("duplicate", false);
			       // ��������
			      shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,this.getString(R.string.tr_activity_main));
			       // ����ͼ��
			      shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(this,
			                       R.drawable.tra));
			       // ������ͼ�Ϳ�ݷ�ʽ��������
			      shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,new Intent(this, this.getClass()).setAction(Intent.ACTION_MAIN));
			       // ���͹㲥
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
	
	 //���ù㲥+ȫ��״̬λ������  LED����ʾ
    private class BootCastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();          
            if(action.equals("bootComplete"))
            {
//            	�յ�������ɵĹ㲥����ȥ��ʼ���
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
