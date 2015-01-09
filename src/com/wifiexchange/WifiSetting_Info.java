package com.wifiexchange;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tr.R;


public class WifiSetting_Info {
	//客户端信息
	public static Client mClient;
	public static ProgressDialog progressDialog;
	public static long wifiTimeOut;
	public static  String almMSG="";//上次警报信息 
	public static  String[] almMSGs=new String[]{"","","","","","","","","",""};//上次警报信息 
	public static int alarmFlag=0;//0：无警报；1：有警报
	//请求读操作最后得到的返回的数据
	public static byte[] getBackData;
	
	public static boolean blockFlag=false;
	public static int[] ydfgFlag=new int[]{0,0,0,0,0,0,0,0};//原点复归标志位
			
	public static boolean destroyPositionQueryFlag=true;
	
	//wifi自动连接相关变量方法
	//找寻对应名字，建立wifi连接，并且建立socket连接
	public static String wifiname="WIFI_ZJX";
	public static String wifipassword="teat";

	public static String connnectStatus="未连接";
	
	public static Object LOCK ;

	

}
