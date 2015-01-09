package com.tr.crash;

import com.tr.programming.Config;

import android.app.Application;

public class CrashApplication extends Application {
	private static CrashApplication instance;
	@Override
	public void onCreate() {
		super.onCreate();
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(this);	
//		sendBroadcast(intent)
		instance = this;
	}
	  public static CrashApplication getInstance() {
	        return instance;
	    }

	
	
}
