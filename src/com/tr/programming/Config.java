/**
 * 
 */
package com.tr.programming;

import java.util.ArrayList;
import java.util.HashMap;

import com.tr.R;

/**
 * @author Administrator
 *
 */
public class Config {
	
	//此LIST在教行界面进行初始化，
	//位置
	public static ArrayList<String> list_pname=new ArrayList<String>();
	public static ArrayList<String> list_spname=new ArrayList<String>();
	public static ArrayList<String> list_fpname=new ArrayList<String>();
	public static ArrayList<String> list_timername=new ArrayList<String>();
	public static ArrayList<String> list_countername=new ArrayList<String>();
	public static boolean AutoState = false;
	public static boolean AlarmState = false;
	public static boolean MoveState = false;
	public static boolean firstCreate =true;
	public static byte[] pspfpaxle=new byte[200];
	public static int SelectArmId = -1;
	public static int SelectSpeedId = -1;	
}
