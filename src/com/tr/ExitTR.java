package com.tr;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;


/**
 * 存储每一个activity，并实现完全关闭程序
 * @author shea
 *
 */
public class ExitTR extends Application {
	private static ExitTR instance;
	private List<Activity> activitylist = new LinkedList<Activity>();

	public static ExitTR getInstance() {
		if (null == instance) {
			instance = new ExitTR();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		if (activitylist!=null&&activitylist.size()>0) {
			if (!activitylist.contains(activity)) {
				activitylist.add(activity);
			}
		}else {
			activitylist.add(activity);
		}
		
	}

	public void exit() {
		if (activitylist!=null&&activitylist.size()>0) {
			for (Activity ac : activitylist) {
				ac.finish();
			}
		}
		
		System.exit(0);
	}
}
