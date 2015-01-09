package com.tr.maintainguide;

import java.io.File;

import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dbutils.Constans;
import com.dbutils.OpenFiles;
import com.tr.R;
import com.tr.main.TR_Main_Activity;

public class Fragments_guide extends Fragment{
	
	private ImageButton ibtn1;
	private ImageButton ibtn2;
	private ImageButton ibtn3;
	private ImageButton ibtn4;
	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("destroy");
		alarmQueryRunnable.destroy();
		ledrunnable.destroy();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println(" Fragments_guide alarmQueryRunnable22222");
		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
		Thread a2 = new Thread(alarmQueryRunnable);
		a2.start();
		ledrunnable=new ledRunnable(getActivity());
		Thread a4 = new Thread(ledrunnable);
		a4.start();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Fragments_guide   onCreate ");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Fragments_guide   onCreateView ");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.tab_maintainguide_guide, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("Fragments_guide   onActivityCreated ");
		
		ibtn1=(ImageButton) getActivity().findViewById(R.id.guide1);
		if(ibtn1==null){
			return;
		}
		ibtn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openThisFileFromSD("visio.pdf");
			}
		});
		ibtn2=(ImageButton) getActivity().findViewById(R.id.guide2);
		if(ibtn2==null){
			return;
		}
		ibtn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openThisFileFromSD("js_png.png");
			}
		});
		ibtn3=(ImageButton) getActivity().findViewById(R.id.guide3);
		if(ibtn3==null){
			return;
		}
		ibtn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openThisFileFromSD("traapp.pdf");
				
			}
		});
		
		ibtn4=(ImageButton) getActivity().findViewById(R.id.guide4);
		if(ibtn4==null){
			return;
		}
		ibtn4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				openThisFileFromSD("ppp_png.png");
			}
		});
		
		
		
		
		
		
		
		
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
					.getStringArray(R.array.fileEndingImage))) {
				intent = OpenFiles.getImageFileIntent(currentPath);
				startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingWebText))) {
				intent = OpenFiles.getHtmlFileIntent(currentPath);
				startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingPackage))) {
				intent = OpenFiles.getApkFileIntent(currentPath);
				startActivity(intent);

			} else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingAudio))) {
				intent = OpenFiles.getAudioFileIntent(currentPath);
				startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingVideo))) {
				intent = OpenFiles.getVideoFileIntent(currentPath);
				startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingText))) {
				intent = OpenFiles.getTextFileIntent(currentPath);
				startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingPdf))) {
				intent = OpenFiles.getPdfFileIntent(currentPath);
				startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingWord))) {
				intent = OpenFiles.getWordFileIntent(currentPath);
				startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingExcel))) {
				intent = OpenFiles.getExcelFileIntent(currentPath);
				startActivity(intent);
			} else if (checkEndsWithInStringArray(fileName, getResources()
					.getStringArray(R.array.fileEndingPPT))) {
				intent = OpenFiles.getPPTFileIntent(currentPath);
				startActivity(intent);
			} else {
				Toast.makeText(getActivity(), "无法打开该文件，请安装相应的软件！",
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
				// TODO: handle exception
			Toast.makeText(getActivity(), "无法打开该文件，请安装相应的软件！",
					Toast.LENGTH_SHORT).show();
			}
		} else {// 不存在该文件
			Toast.makeText(getActivity(), "对不起，不存在该文件！", Toast.LENGTH_SHORT)
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

}
