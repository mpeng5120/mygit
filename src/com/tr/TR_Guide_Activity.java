package com.tr;

import com.tr.R;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.VideoView;

public class TR_Guide_Activity extends Activity {
	
	public ImageButton guide1=null;
	public ImageButton guide3=null;
	public VideoView videoView=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tr_guide);
		init();	
	}

	private void init() {
		// TODO 自动生成的方法存根
		final ScrollView guideLayout=(ScrollView) findViewById(R.id.ScrollView);
		
		guide1=(ImageButton) findViewById(R.id.guide1);
		guide1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
//				ScrollView guideLayout=(ScrollView) findViewById(R.id.ScrollView);

				View guideView1=getLayoutInflater().inflate(R.layout.tr_guide1, null);
				guideLayout.removeAllViews();
				guideLayout.addView(guideView1);
				
			}
		});
		
		
		guide3=(ImageButton) findViewById(R.id.guide3);
		guide3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				View guideView3=getLayoutInflater().inflate(R.layout.tr_guide3, null);
				guideLayout.removeAllViews();
				guideLayout.addView(guideView3);
				
				videoView=(VideoView) findViewById(R.id.videoView1);
				
				VideoView videoView=new VideoView(TR_Guide_Activity.this); 
				
				MediaController mediaController=new MediaController(TR_Guide_Activity.this);
				mediaController.setAnchorView(videoView);
				videoView.setMediaController(mediaController);
				
				//两种播放都可以
//				videoView.setVideoPath("file:///sdcard/a.mp4");
//				String uriString="/mnt/sdcard/a.mp4";
//				videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath()+"/a.mp4");
				videoView.setVideoPath(Environment.getExternalStorageDirectory()+"/a.mp4");
//				videoView.setVideoURI(uriString);
				videoView.requestFocus();
				videoView.start();
				
				
			}
		});
		
		
	}
	

}
