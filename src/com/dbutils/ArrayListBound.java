package com.dbutils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import android.text.Html;
import android.view.View;
import android.widget.BaseAdapter;

import com.explain.HexDecoding;
import com.tr.R;
import com.tr.R.color;
import com.tr.main.TR_Main_Activity;
import com.tr.parameter_setting.Fragments_Mechanical_Parameter;
import com.tr.parameter_setting.Fragments_Other_Functions;
import com.tr.parameter_setting.Fragments_Servo_Parameter;
import com.tr.programming.Fragments_Device_ActualInput.MyAdapter;
import com.tr.programming.TR_Programming_Activity;

/*
 *	read txt files and initialize listview bounddata 
 *if buliding new files,have some default data in listview bound data
 *Attention:can not use without in 
 */
public class ArrayListBound {
	
	// 格式转化类,0001的格式，暂时这么做了，以后找到更好的方法可以替换
	static DecimalFormat format = new DecimalFormat("####");
	
	
	//设备定义中各选项卡绑定数据
	private static ArrayList<HashMap<String, Object>> device_actualInput_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> device_actualOutput_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> device_middleInput_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> device_middleOutput_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> device_dataInput_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> device_dataOutput_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> device_register_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> device_dataRegister_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> device_timer_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> device_counter_list = new ArrayList<HashMap<String, Object>>();	
	private static ArrayList<HashMap<String, Object>> device_position_list = new ArrayList<HashMap<String, Object>>();		
	private static ArrayList<HashMap<String, Object>> device_alarm_list = new ArrayList<HashMap<String, Object>>();	
	private static ArrayList<HashMap<String, Object>> device_optional_list = new ArrayList<HashMap<String, Object>>();	
	//NC编辑绑定数据
	//private static ArrayList<HashMap<String, Object>> nc_edit_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> nc_edit_list1 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> nc_edit_list2 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> nc_edit_list3 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> nc_edit_list4 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> nc_edit_list5 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> nc_edit_list6 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> nc_edit_list7 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> nc_edit_list8 = new ArrayList<HashMap<String, Object>>();

	//table编辑绑定数据
	//private static ArrayList<HashMap<String, Object>> table_edit_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> table_edit_list1 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> table_edit_list2 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> table_edit_list3 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> table_edit_list4 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> table_edit_list5= new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> table_edit_list6 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> table_edit_list7 = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Object>> table_edit_list8 = new ArrayList<HashMap<String, Object>>();
	
	//mechanical_parameter_setting编辑绑定数据
	private static ArrayList<HashMap<String, Object>> parameter_setting_mechanical_list = new ArrayList<HashMap<String, Object>>();
	private static ArrayList<HashMap<String, Boolean>> mechanicalPara_tabChange_list = new ArrayList<HashMap<String, Boolean>>();
	
	private static ArrayList<HashMap<String, Object>>  parameter_setting_servol_list = new ArrayList<HashMap<String, Object>>();
	
	private static ArrayList<HashMap<String, Object>>  setting_mould_list = new ArrayList<HashMap<String, Object>>();
	
	
	
	public static ArrayList<HashMap<String, Object>> P_posList_setting=new ArrayList<HashMap<String, Object>>();
	
	/**
	 * 账户信息需要从本地读取，暂时先用这里的
	 */
	public static ArrayList<HashMap<String, Object>> maintain_accountList=new ArrayList<HashMap<String, Object>>();
	
	public static ArrayList<HashMap<String, Object>> list_alarmzb=new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> list_version=new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> list_alarm=new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> list_da=new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> list_io=new ArrayList<HashMap<String, Object>>();
	//public static ArrayList<HashMap<String, Object>> list_shortkey=new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> list_usermode=new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> list_userdata=new ArrayList<HashMap<String, Object>>();
	public static ArrayList<HashMap<String, Object>> list_allspeed=new ArrayList<HashMap<String, Object>>();
	
	//new file mode:without reading txt files
	public ArrayListBound() {
		// TODO Auto-generated constructor stub
		format.setMinimumIntegerDigits(4);// 显示最少4位数
		//nc edit data initialize
	}
		

	//reading previous files mode:
	public ArrayListBound(String files) {
		// TODO Auto-generated constructor stub
		
		
		
	}
	public  static ArrayList<HashMap<String, Object>> getDeviceActualInputListData() {
		if (device_actualInput_list.isEmpty()) {
			for (int i = 0; i < 128; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				device_actualInput_list.add(map);
			}
			return device_actualInput_list;
		}else {
			return device_actualInput_list;
		}
	}

	public  static ArrayList<HashMap<String, Object>> getDeviceActualOutputListData() {
		if (device_actualOutput_list.isEmpty()) {
			for (int i = 0; i < 128; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				device_actualOutput_list.add(map);
			}
			return device_actualOutput_list;
		}else {
			return device_actualOutput_list;
		}
	}
	
	public  static ArrayList<HashMap<String, Object>> getDeviceMiddleInputListData() {
		if (device_middleInput_list.isEmpty()) {
			for (int i = 0; i < 256; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				device_middleInput_list.add(map);
			}
			return device_middleInput_list;
		}else {
			return device_middleInput_list;
		}
	}
	public  static ArrayList<HashMap<String, Object>> getDeviceMiddleOutputListData() {
		if (device_middleOutput_list.isEmpty()) {
			for (int i = 0; i < 256; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				device_middleOutput_list.add(map);
			}
			return device_middleOutput_list;
		}else {
			return device_middleOutput_list;
		}
	}
	public  static ArrayList<HashMap<String, Object>> getDeviceDataInputListData() {
		if (device_dataInput_list.isEmpty()) {
			for (int i = 0; i < 128; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				device_dataInput_list.add(map);
			}
			return device_dataInput_list;
		}else {
			return device_dataInput_list;
		}
	}
	public  static ArrayList<HashMap<String, Object>> getDeviceDataOutputListData() {
		if (device_dataOutput_list.isEmpty()) {
			for (int i = 0; i < 128; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText",String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				device_dataOutput_list.add(map);
			}
			return device_dataOutput_list;
		}else {
			return device_dataOutput_list;
		}
	}
	public  static ArrayList<HashMap<String, Object>> getDeviceRegisterListData() {
		if (device_register_list.isEmpty()) {
			for (int i = 0; i < 32; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				device_register_list.add(map);
			}
			return device_register_list;
		}else {
			return device_register_list;
		}
	}
	public  static ArrayList<HashMap<String, Object>> getDeviceDataRegisterListData() {
		if (device_dataRegister_list.isEmpty()) {
			for (int i = 0; i < 32; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				device_dataRegister_list.add(map);
			}
			return device_dataRegister_list;
		}else {
			return device_dataRegister_list;
		}
	}
	public  static ArrayList<HashMap<String, Object>> getDeviceTimerListData() {
		if (device_timer_list.isEmpty()) {
			for (int i = 0; i < 64; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				map.put("setItem", "0");
 				device_timer_list.add(map);
			}
			return device_timer_list;
		}else {
			return device_timer_list;
		}
	}
	public  static ArrayList<HashMap<String, Object>> getDeviceCounterListData() {
		if (device_counter_list.isEmpty()) {
			for (int i = 0; i < 64; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				map.put("setItem", "0");
 				device_counter_list.add(map);
			}
			return device_counter_list;
		}else {
			return device_counter_list;
		}
	}
	
	
	
	public  static ArrayList<HashMap<String, Object>> getDevicePositionListData() {
		if (device_position_list.isEmpty()) {
			for (int i = 1; i < 20; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("signalNameEditText", "");
 				map.put("noteEditText", "");
 				map.put("setItem", "0");
				device_position_list.add(map);
			}
			return device_position_list;
		}else {
			return device_position_list;
		}
	}
	
	public  static ArrayList<HashMap<String, Object>> getDeviceAlarmListData() {
		if (device_alarm_list.isEmpty()) {
			for (int i = 1; i < 2; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%03d", i));//
 				map.put("symbolNameEditText", "");
 				map.put("noteEditText", "");
 				device_alarm_list.add(map);
			}
			return device_alarm_list;
		}else {
			return device_alarm_list;
		}
	}
	public  static ArrayList<HashMap<String, Object>> getDeviceOptionalListData() {
		if (device_optional_list.isEmpty()) {
			for (int i =1; i <=32; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%02d", i));//
				map.put("name1", "");//
 				map.put("name2", "");
 				map.put("name3", "");
 				device_optional_list.add(map);
			}
			return device_optional_list;
		}else {
			return device_optional_list;
		}
	}
	
	public static ArrayList<HashMap<String, Object>> getNCeditList1Data() {
		if (nc_edit_list1.isEmpty()) {
			for (int i = 0; i < 20; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				
				map.put("addressText", String.format("%04d", i));
				map.put("numSpinner", "");
				map.put("orderSpinner", "");
				map.put("operatText", "");
				map.put("noteEditText", "");
				map.put("checkbox", "");
				nc_edit_list1.add(map);
			}
			return nc_edit_list1;
		}else {
			return nc_edit_list1;
		}
	}
	public static ArrayList<HashMap<String, Object>> getNCeditList2Data() {
		if (nc_edit_list2.isEmpty()) {
			for (int i = 0; i < 20; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
				map.put("numSpinner", "");
				map.put("orderSpinner", "");
				map.put("operatText", "");
				map.put("noteEditText", "");
				nc_edit_list2.add(map);
			}
			return nc_edit_list2;
		}else {
			return nc_edit_list2;
		}
	}
	public static ArrayList<HashMap<String, Object>> getNCeditList3Data() {
		if (nc_edit_list3.isEmpty()) {
			for (int i = 0; i < 20; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
				map.put("numSpinner", "");
				map.put("orderSpinner", "");
				map.put("operatText", "");
				map.put("noteEditText", "");
				nc_edit_list3.add(map);
			}
			return nc_edit_list3;
		}else {
			return nc_edit_list3;
		}
	}
	public static ArrayList<HashMap<String, Object>> getNCeditList4Data() {
		if (nc_edit_list4.isEmpty()) {
			for (int i = 0; i < 20; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
				map.put("numSpinner", "");
				map.put("orderSpinner", "");
				map.put("operatText", "");
				map.put("noteEditText", "");
				nc_edit_list4.add(map);
			}
			return nc_edit_list4;
		}else {
			return nc_edit_list4;
		}
	}
	public static ArrayList<HashMap<String, Object>> getNCeditList5Data() {
		if (nc_edit_list5.isEmpty()) {
			for (int i = 0; i < 20; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
				map.put("numSpinner", "");
				map.put("orderSpinner", "");
				map.put("operatText", "");
				map.put("noteEditText", "");
				nc_edit_list5.add(map);
			}
			return nc_edit_list5;
		}else {
			return nc_edit_list5;
		}
	}
	public static ArrayList<HashMap<String, Object>> getNCeditList6Data() {
		if (nc_edit_list6.isEmpty()) {
			for (int i = 0; i < 20; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
				map.put("numSpinner", "");
				map.put("orderSpinner", "");
				map.put("operatText", "");
				map.put("noteEditText", "");
				nc_edit_list6.add(map);
			}
			return nc_edit_list6;
		}else {
			return nc_edit_list6;
		}
	}
	public static ArrayList<HashMap<String, Object>> getNCeditList7Data() {
		if (nc_edit_list7.isEmpty()) {
			for (int i = 0; i < 20; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
				map.put("numSpinner", "");
				map.put("orderSpinner", "");
				map.put("operatText", "");
				map.put("noteEditText", "");
				nc_edit_list7.add(map);
			}
			return nc_edit_list7;
		}else {
			return nc_edit_list7;
		}
	}
	public static ArrayList<HashMap<String, Object>> getNCeditList8Data() {
		if (nc_edit_list8.isEmpty()) {
			for (int i = 0; i < 20; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("addressText", String.format("%04d", i));//
				map.put("numSpinner", "");
				map.put("orderSpinner", "");
				map.put("operatText", "");
				map.put("noteEditText", "");
				nc_edit_list8.add(map);
			}
			return nc_edit_list8;
		}else {
			return nc_edit_list8;
		}
	}
	/*public static ArrayList<HashMap<String, Object>> getTableListData() {
		if (table_edit_list.isEmpty()) {
 			for (int i = 0; i < 20; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", format.format(i));//
 				map.put("inputEditText", "");
 				map.put("outputEditText", "");
 				map.put("noteEditText", "");
 				table_edit_list.add(map);
			}
			return table_edit_list;
		}else {
			return table_edit_list;
		}
	}*/
	public static ArrayList<HashMap<String, Object>> getTableList1Data() {
		if (table_edit_list1.isEmpty()) {
 			for (int i = 0; i < 20; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", String.format("%04d", i));//
 				map.put("inputEditText", "");
 				map.put("outputEditText", "");
 				map.put("noteEditText", "");
 				table_edit_list1.add(map);
			}
			return table_edit_list1;
		}else {
			return table_edit_list1;
		}
	}
	public static ArrayList<HashMap<String, Object>> getTableList2Data() {
		if (table_edit_list2.isEmpty()) {
 			for (int i = 0; i < 20; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", String.format("%04d", i));//
 				map.put("inputEditText", "");
 				map.put("outputEditText", "");
 				map.put("noteEditText", "");
 				table_edit_list2.add(map);
			}
			return table_edit_list2;
		}else {
			return table_edit_list2;
		}
	}
	public static ArrayList<HashMap<String, Object>> getTableList3Data() {
		if (table_edit_list3.isEmpty()) {
 			for (int i = 0; i < 20; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", String.format("%04d", i));//
 				map.put("inputEditText", "");
 				map.put("outputEditText", "");
 				map.put("noteEditText", "");
 				table_edit_list3.add(map);
			}
			return table_edit_list3;
		}else {
			return table_edit_list3;
		}
	}
	public static ArrayList<HashMap<String, Object>> getTableList4Data() {
		if (table_edit_list4.isEmpty()) {
 			for (int i = 0; i < 20; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", String.format("%04d", i));//
 				map.put("inputEditText", "");
 				map.put("outputEditText", "");
 				map.put("noteEditText", "");
 				table_edit_list4.add(map);
			}
			return table_edit_list4;
		}else {
			return table_edit_list4;
		}
	}
	public static ArrayList<HashMap<String, Object>> getTableList5Data() {
		if (table_edit_list5.isEmpty()) {
 			for (int i = 0; i < 20; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", String.format("%04d", i));//
 				map.put("inputEditText", "");
 				map.put("outputEditText", "");
 				map.put("noteEditText", "");
 				table_edit_list5.add(map);
			}
			return table_edit_list5;
		}else {
			return table_edit_list5;
		}
	}
	public static ArrayList<HashMap<String, Object>> getTableList6Data() {
		if (table_edit_list6.isEmpty()) {
 			for (int i = 0; i < 20; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", String.format("%04d", i));//
 				map.put("inputEditText", "");
 				map.put("outputEditText", "");
 				map.put("noteEditText", "");
 				table_edit_list6.add(map);
			}
			return table_edit_list6;
		}else {
			return table_edit_list6;
		}
	}
	public static ArrayList<HashMap<String, Object>> getTableList7Data() {
		if (table_edit_list7.isEmpty()) {
 			for (int i = 0; i < 20; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", String.format("%04d", i));//
 				map.put("inputEditText", "");
 				map.put("outputEditText", "");
 				map.put("noteEditText", "");
 				table_edit_list7.add(map);
			}
			return table_edit_list7;
		}else {
			return table_edit_list7;
		}
	}
	public static ArrayList<HashMap<String, Object>> getTableList8Data() {
		if (table_edit_list8.isEmpty()) {
 			for (int i = 0; i < 20; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText",String.format("%04d", i));//
 				map.put("inputEditText", "");
 				map.put("outputEditText", "");
 				map.put("noteEditText", "");
 				table_edit_list8.add(map);
			}
			return table_edit_list8;
		}else {
			return table_edit_list8;
		}
	}
	
	public static ArrayList<HashMap<String, Object>> getMechanicalParameterListData() {
		if (parameter_setting_mechanical_list.isEmpty()) {
 			for (int i = 1; i < 233; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", String.format("%04d", i));//
 				map.put("contentText", "");
 				map.put("setValueText", "");
 				map.put("noteText", "");
 				parameter_setting_mechanical_list.add(map);
			}
			return parameter_setting_mechanical_list;
		}else {
			return parameter_setting_mechanical_list;
		}
	}
	
	public static ArrayList<HashMap<String, Boolean>> getMechanicalParameterTabChangeListData() {
		if (mechanicalPara_tabChange_list.isEmpty()) {
 				HashMap<String, Boolean> map = new HashMap<String, Boolean>();
 				map.put("轴1", true);//
 				map.put("轴2", true);
 				map.put("轴3", true);
 				map.put("轴4", true);
 				map.put("轴5", true);
 				map.put("轴6", true);
 				map.put("轴7", true);
 				map.put("轴8", true);
 				mechanicalPara_tabChange_list.add(map);
			return mechanicalPara_tabChange_list;
		}else {
			return mechanicalPara_tabChange_list;
		}
	}
	public static void setTabChange_false() {
		
		getMechanicalParameterTabChangeListData().get(0).put("轴1", false);
		getMechanicalParameterTabChangeListData().get(0).put("轴2", false);
		getMechanicalParameterTabChangeListData().get(0).put("轴3", false);
		getMechanicalParameterTabChangeListData().get(0).put("轴4", false);
		getMechanicalParameterTabChangeListData().get(0).put("轴5", false);
		getMechanicalParameterTabChangeListData().get(0).put("轴6", false);
		getMechanicalParameterTabChangeListData().get(0).put("轴7", false);
		getMechanicalParameterTabChangeListData().get(0).put("轴8", false);
		
	}
	
	public static ArrayList<HashMap<String, Object>> getServoParameterListData() {
		if (parameter_setting_servol_list.isEmpty()) {
			try{
			String selectedItem=Fragments_Mechanical_Parameter.selectedItem;
			System.out.println("ArrayListBound selectedItem="+selectedItem);
			int sevoparas= Constans.mechanicalParameter.readOutFile(Constans.mechanicalParameterPATH +selectedItem+"/"+ Constans.伺服参数).length/19+1;
			
			for (int i = 1; i <sevoparas; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("numText", String.format("%04d", i));//
 				map.put("contentText", "");
 				map.put("currentValueText", "");
 				map.put("setValueText", "");
				map.put("noteText", "");
 				parameter_setting_servol_list.add(map);
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return parameter_setting_servol_list;
		}else {
			return parameter_setting_servol_list;
		}
	}
	
	public static ArrayList<HashMap<String, Object>> getAccountListData() {
		if (maintain_accountList.isEmpty()) {

			HashMap<String, Object> map_account=new HashMap<String, Object>();
			map_account.put("1", "123");
			map_account.put("2", "123");
			map_account.put("3", "123");
			map_account.put("4", "123");
			maintain_accountList.add(map_account);
	
			return maintain_accountList;
		}else {
			return maintain_accountList;
		}
	}
	
	
	
	public static ArrayList<HashMap<String, Object>> getMouldDataListData() {
		if (setting_mould_list.isEmpty()) {
			for (int i = 1; i < 2; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("num", String.format("%04d", i));
 				map.put("num_mould_setting", "");
 				map.put("name_mould_setting", "");
 				map.put("note_mould_setting", "");				
				setting_mould_list.add(map);
			}
			return setting_mould_list;
		}else {
			return setting_mould_list;
		}
	}
	
	public static ArrayList<HashMap<String, Object>> getAlarmzbListData() {
		if (list_alarmzb.isEmpty()) {
			try{
			String servoalarmmsg=Constans.mouldData.readAlarmOutFile1(Constans.trPATH+"警报信息"+File.separator+"servoalarm.trt");
			String sysalarmmsg=Constans.mouldData.readAlarmOutFile1(Constans.trPATH+"警报信息"+File.separator+"sysalarm.trt");
			//String allalarmmsg=servoalarmmsg+sysalarmmsg;
			//String[] allalarmmsgs=allalarmmsg.toString().split("///");
			String[] servoalarmmsgs=servoalarmmsg.toString().split("///");
			String[] sysalarmmsgs=sysalarmmsg.toString().split("///");
			
			for (int i = 0; i <servoalarmmsgs.length; i=i+4) {
				if((i+3)==servoalarmmsgs.length){
					HashMap<String, Object> map = new HashMap<String, Object>();
	 				map.put("alarmzbnum", servoalarmmsgs[i]==null?"":servoalarmmsgs[i]);
	 				map.put("alarmzbName",servoalarmmsgs[i+1]==null?"":servoalarmmsgs[i+1]);
	 				map.put("alarmzbmsg", (servoalarmmsgs[i+2]==null?"":servoalarmmsgs[i+2])+"");				
	 				list_alarmzb.add(map);
					break;
				}else if((i+2)==servoalarmmsgs.length){
					HashMap<String, Object> map = new HashMap<String, Object>();
	 				map.put("alarmzbnum", servoalarmmsgs[i]==null?"":servoalarmmsgs[i]);
	 				map.put("alarmzbName",servoalarmmsgs[i+1]==null?"":servoalarmmsgs[i+1]);
	 				map.put("alarmzbmsg", "");				
	 				list_alarmzb.add(map);
					break;
				}else if((i+1)==servoalarmmsgs.length){
					HashMap<String, Object> map = new HashMap<String, Object>();
	 				map.put("alarmzbnum", servoalarmmsgs[i]==null?"":servoalarmmsgs[i]);
	 				map.put("alarmzbName","");
	 				map.put("alarmzbmsg", "");				
	 				list_alarmzb.add(map);
					break;
				}else{
					HashMap<String, Object> map = new HashMap<String, Object>();
	 				map.put("alarmzbnum", servoalarmmsgs[i]==null?"":servoalarmmsgs[i]);
	 				map.put("alarmzbName",servoalarmmsgs[i+1]==null?"":servoalarmmsgs[i+1]);
	 				map.put("alarmzbmsg", (servoalarmmsgs[i+2]==null?"":servoalarmmsgs[i+2])+(servoalarmmsgs[i+3]==null?"":servoalarmmsgs[i+3]));				
	 				list_alarmzb.add(map);
				}
 				
			}
			for (int i = 0; i <sysalarmmsgs.length; i=i+4) {
				if((i+3)==sysalarmmsgs.length){
					HashMap<String, Object> map = new HashMap<String, Object>();
	 				map.put("alarmzbnum", sysalarmmsgs[i]==null?"":sysalarmmsgs[i]);
	 				map.put("alarmzbName",sysalarmmsgs[i+1]==null?"":sysalarmmsgs[i+1]);
	 				map.put("alarmzbmsg", (sysalarmmsgs[i+2]==null?"":sysalarmmsgs[i+2])+"");				
	 				list_alarmzb.add(map);
					break;
				}else if((i+2)==sysalarmmsgs.length){
					HashMap<String, Object> map = new HashMap<String, Object>();
	 				map.put("alarmzbnum", sysalarmmsgs[i]==null?"":sysalarmmsgs[i]);
	 				map.put("alarmzbName",sysalarmmsgs[i+1]==null?"":sysalarmmsgs[i+1]);
	 				map.put("alarmzbmsg", "");				
	 				list_alarmzb.add(map);
					break;
				}else if((i+1)==sysalarmmsgs.length){
					HashMap<String, Object> map = new HashMap<String, Object>();
	 				map.put("alarmzbnum", sysalarmmsgs[i]==null?"":sysalarmmsgs[i]);
	 				map.put("alarmzbName","");
	 				map.put("alarmzbmsg", "");				
	 				list_alarmzb.add(map);
					break;
				}else{
					HashMap<String, Object> map = new HashMap<String, Object>();
	 				map.put("alarmzbnum", sysalarmmsgs[i]==null?"":sysalarmmsgs[i]);
	 				map.put("alarmzbName",sysalarmmsgs[i+1]==null?"":sysalarmmsgs[i+1]);
	 				map.put("alarmzbmsg", (sysalarmmsgs[i+2]==null?"":sysalarmmsgs[i+2])+(sysalarmmsgs[i+3]==null?"":sysalarmmsgs[i+3]));				
	 				list_alarmzb.add(map);
				}
			}} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			return list_alarmzb;
		}else {
			return list_alarmzb;
		}
	}
	
	
	public static ArrayList<HashMap<String, Object>> getVersionListData() {
		if (list_version.isEmpty()) {

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("boot", "《数据版本》");
			map.put("boot_version", "");
			map.put("data", "");
			map.put("data_version", "");
			list_version.add(map);
			
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("boot", "系统Table版本");
			map1.put("boot_version", "");
			map1.put("data", "机械参数版本");
			map1.put("data_version", "");	
			list_version.add(map1);
		
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("boot", "");
			map2.put("boot_version", "");
			map2.put("data", "");
			map2.put("data_version", "");
			list_version.add(map2);
			
			HashMap<String, Object> map3 = new HashMap<String, Object>();
			map3.put("boot", "《boot版本》");
			map3.put("boot_version", "");
			map3.put("data", "");
			map3.put("data_version", "");
			list_version.add(map3);
			
			HashMap<String, Object> map4 = new HashMap<String, Object>();
			map4.put("boot", "CPU");
			map4.put("boot_version", "");
			map4.put("data", "PPU");
			map4.put("data_version", "");
			list_version.add(map4);
			
			
			HashMap<String, Object> map5 = new HashMap<String, Object>();
			map5.put("boot", "");
			map5.put("boot_version", "");
			map5.put("data", "");
			map5.put("data_version", "");
			list_version.add(map5);
			
			HashMap<String, Object> map6 = new HashMap<String, Object>();
			map6.put("boot", "《系统程序版本》");
			map6.put("boot_version", "");
			map6.put("data", "");
			map6.put("data_version", "");
			list_version.add(map6);
			
			HashMap<String, Object> map7 = new HashMap<String, Object>();
			map7.put("boot", "CPU");
			map7.put("boot_version", "");
			map7.put("data", "PPU");
			map7.put("data_version", "");
			list_version.add(map7);
			
			HashMap<String, Object> map8 = new HashMap<String, Object>();
			map8.put("boot", "平板程序版本");
			map8.put("boot_version", "14100010(内部使用)");
			map8.put("data", "操作盒程序版本");
			map8.put("data_version", "");
			list_version.add(map8);
			
			HashMap<String, Object> map9 = new HashMap<String, Object>();
			map9.put("boot", "互锁基板程序版本");
			map9.put("boot_version", "");
			map9.put("data", "");
			map9.put("data_version", "");
			list_version.add(map9);
			
			HashMap<String, Object> map10 = new HashMap<String, Object>();
			map10.put("boot", "");
			map10.put("boot_version", "");
			map10.put("data", "");
			map10.put("data_version", "");
			list_version.add(map10);
			
			HashMap<String, Object> map11 = new HashMap<String, Object>();
			map11.put("boot", "《IO系统版本》");
			map11.put("boot_version", "");
			map11.put("data", "");
			map11.put("data_version", "");
			list_version.add(map11);
			
			HashMap<String, Object> map12 = new HashMap<String, Object>();
			map12.put("boot", "IO1版本");
			map12.put("boot_version", "");
			map12.put("data", "IO2版本");
			map12.put("data_version", "");
			list_version.add(map12);
			
			HashMap<String, Object> map13 = new HashMap<String, Object>();
			map13.put("boot", "IO3版本");
			map13.put("boot_version", "");
			map13.put("data", "IO4版本");
			map13.put("data_version", "");
			list_version.add(map13);
			
			HashMap<String, Object> map14 = new HashMap<String, Object>();
			map14.put("boot", "IO5版本");
			map14.put("boot_version", "");
			map14.put("data", "IO6版本");
			map14.put("data_version", "");
			list_version.add(map14);
			
			HashMap<String, Object> map15 = new HashMap<String, Object>();
			map15.put("boot", "IO7版本");
			map15.put("boot_version", "");
			map15.put("data", "IO8版本");
			map15.put("data_version", "");
			list_version.add(map15);
			
			HashMap<String, Object> map16 = new HashMap<String, Object>();
			map16.put("boot", "");
			map16.put("boot_version", "");
			map16.put("data", "");
			map16.put("data_version", "");
			list_version.add(map16);
			
			HashMap<String, Object> map17 = new HashMap<String, Object>();
			map17.put("boot", "《SPU系统版本》");
			map17.put("boot_version", "");
			map17.put("data", "");
			map17.put("data_version", "");
			list_version.add(map17);
			
			HashMap<String, Object> map18 = new HashMap<String, Object>();
			map18.put("boot", "SPU1版本");
			map18.put("boot_version", "");
			map18.put("data", "SPU2版本");
			map18.put("data_version", "");
			list_version.add(map18);
			
			HashMap<String, Object> map19 = new HashMap<String, Object>();
			map19.put("boot", "SPU3版本");
			map19.put("boot_version", "");
			map19.put("data", "SPU4版本");
			map19.put("data_version", "");
			list_version.add(map19);
			
			HashMap<String, Object> map20 = new HashMap<String, Object>();
			map20.put("boot", "SPU5版本");
			map20.put("boot_version", "");
			map20.put("data", "SPU6版本");
			map20.put("data_version", "");
			list_version.add(map20);
			
			HashMap<String, Object> map21 = new HashMap<String, Object>();
			map21.put("boot", "SPU7版本");
			map21.put("boot_version", "");
			map21.put("data", "SPU8版本");
			map21.put("data_version", "");
			list_version.add(map21);
			
			HashMap<String, Object> map22 = new HashMap<String, Object>();
			map22.put("boot", "");
			map22.put("boot_version", "");
			map22.put("data", "");
			map22.put("data_version", "");
			list_version.add(map22);
			
			HashMap<String, Object> map23 = new HashMap<String, Object>();
			map23.put("boot", "《伺服版本》");
			map23.put("boot_version", "");
			map23.put("data", "");
			map23.put("data_version", "");
			list_version.add(map23);
			
			HashMap<String, Object> map24 = new HashMap<String, Object>();
			map24.put("boot", "伺服1版本");
			map24.put("boot_version", "");
			map24.put("data", "伺服2版本");
			map24.put("data_version", "");
			list_version.add(map24);
			
			HashMap<String, Object> map25 = new HashMap<String, Object>();
			map25.put("boot", "伺服3版本");
			map25.put("boot_version", "");
			map25.put("data", "伺服4版本");
			map25.put("data_version", "");
			list_version.add(map25);
			
			HashMap<String, Object> map26 = new HashMap<String, Object>();
			map26.put("boot", "伺服5版本");
			map26.put("boot_version", "");
			map26.put("data", "伺服6版本");
			map26.put("data_version", "");
			list_version.add(map26);
			
			HashMap<String, Object> map27 = new HashMap<String, Object>();
			map27.put("boot", "伺服7版本");
			map27.put("boot_version", "");
			map27.put("data", "伺服8版本");
			map27.put("data_version", "");
			list_version.add(map27);

			return list_version;
		} else {
			return list_version;
		}
	}
	
	public static ArrayList<HashMap<String, Object>> getAlarmListData() {
		if (list_alarm.isEmpty()) {
			for (int i = 1; i < 1; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("num_alarm","警报"+ i);
 				map.put("time_alarm", "");
 				map.put("content_alarm", "");			
 				list_alarm.add(map);
			}
			return list_alarm;
		}else {
			return list_alarm;
		}
	}
	public static ArrayList<HashMap<String, Object>> getDAListData() {
		if (list_da.isEmpty()) {
			HashMap<String, Object> map0 = new HashMap<String, Object>();
			map0.put("da", "DA通道1");
			map0.put("da_text", "");
			list_da.add(map0);
			
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("da", "数据地址（0x）：");
			map1.put("da_text", "");
			list_da.add(map1);
			
			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("da", "数据基准值：");
			map2.put("da_text", "");
			list_da.add(map2);
			
			HashMap<String, Object> map3 = new HashMap<String, Object>();
			map3.put("da", "倍数：");
			map3.put("da_text", "");
			list_da.add(map3);
			
			HashMap<String, Object> map4 = new HashMap<String, Object>();
			map4.put("da", "DA通道2");
			map4.put("da_text", "");
			list_da.add(map4);
			
			HashMap<String, Object> map5 = new HashMap<String, Object>();
			map5.put("da", "数据地址（0x）：");
			map5.put("da_text", "");
			list_da.add(map5);
			
			HashMap<String, Object> map6 = new HashMap<String, Object>();
			map6.put("da", "数据基准值：");
			map6.put("da_text", "");
			list_da.add(map6);
			
			HashMap<String, Object> map7 = new HashMap<String, Object>();
			map7.put("da", "倍数：");
			map7.put("da_text", "");
			list_da.add(map7);
			return list_da;
		}else {
			return list_da;
		}
	}
	
	public static ArrayList<HashMap<String, Object>> getIOListData() {
		if (list_io.isEmpty()) {
			for (int i = 0; i < 0; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("io_address",i);
 				map.put("i_symbolName", ArrayListBound.getDeviceActualInputListData().get(i).get("symbolNameEditText"));
 				map.put("i_signalName", ArrayListBound.getDeviceActualInputListData().get(i).get("signalNameEditText"));	
 				map.put("i_state", "");
 				map.put("o_symbolName", ArrayListBound.getDeviceActualOutputListData().get(i).get("symbolNameEditText"));
 				map.put("o_signalName", ArrayListBound.getDeviceActualOutputListData().get(i).get("signalNameEditText"));	
 				map.put("o_state", ""); 
 				list_io.add(map);
			}
			return list_io;
		}else {
			return list_io;
		}
	}/*
	public static ArrayList<HashMap<String, Object>> getshortkeyListData() {
		if (list_shortkey.isEmpty()) {
			for (int i = 1; i <1; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("number",i);
 				map.put("name1", "");
 				map.put("name2", "");	
 				map.put("name3", "");
 				list_shortkey.add(map);
			}
			return list_shortkey;
		}else {
			return list_shortkey;
		}
	}*/
	public static ArrayList<HashMap<String, Object>> getUserModeListData() {
		if (list_usermode.isEmpty()) {
			for (int i = 1; i <=28; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("number",i);
 				map.put("name", ArrayListBound.getDeviceMiddleInputListData().get(i-1).get("signalNameEditText").toString());
 				map.put("state", "");
 				list_usermode.add(map);
			}
			return list_usermode;
		}else {
			return list_usermode;
		}
	}
	public static ArrayList<HashMap<String, Object>> getUserDataListData() {
		if (list_userdata.isEmpty()) {
			for (int i = 1; i <= 16; i++) {
 				HashMap<String, Object> map = new HashMap<String, Object>();
 				map.put("number",i);
 				map.put("name", ArrayListBound.getDeviceDataInputListData().get(i-1).get("signalNameEditText").toString());
 				map.put("state", "");
 				list_userdata.add(map);
			}
			return list_userdata;
		}else {
			return list_userdata;
		}
	}
	public static ArrayList<HashMap<String, Object>> getAllSpeedListData() {
		if (list_allspeed.isEmpty()) {
 				HashMap<String, Object> map1 = new HashMap<String, Object>();
 				map1.put("name","速度");
 				map1.put("editname","");
 				list_allspeed.add(map1);
 				HashMap<String, Object> map2 = new HashMap<String, Object>();
 				map2.put("name","加速度");
 				map2.put("editname","");
 				list_allspeed.add(map2);
 				HashMap<String, Object> map3 = new HashMap<String, Object>();
 				map3.put("name","减速度");
 				map3.put("editname","");
 				list_allspeed.add(map3);
			
			return list_allspeed;
		}else {
			return list_allspeed;
		}
	}
}
