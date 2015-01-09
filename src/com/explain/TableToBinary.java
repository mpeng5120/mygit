package com.explain;

import java.util.HashMap;
import java.util.Iterator;
import com.dataInAddress.AddressPublic;
import com.dataInAddress.Define;
import com.dataInAddress.PlcData;
import com.dbutils.ArrayListBound;
import com.dbutils.Constans;

public class TableToBinary {
	// table 指令函数区

	// 一条table命令包含12个byte，总共三个参数（或者命令），各占4byte，
	// 每个参数（或者命令）的byte数组长度为4
	public final static int byteBlockLength = 4;
	private static int ERROR_DEVICE_NOT_FOUND = 999;// 表示未找到
	private static int flog;
	public static String noteText="未定义";
	/**
	 * 遍历查找设备定义中某一项的序号，必须强制定义所有设备名称的头字母 根据头字母对应不同的arraylist进行地址查询
	 * 实际输入device_actualInput_list：X； 实际输出device_actualOutput_list：Y ；
	 * 中间输入device_middleInput_list：MX ；中间输出device_middleOutput_list：MY ；
	 * 数据输入device_dataInput_list：DX ；数据输出device_dataOutput_list：DY；
	 * 掉电可保持寄存器device_register_list：LM ；掉电可保持数据寄存器device_dataRegister_list：LD ；
	 * 定时器device_timer_list：T ； 计数器device_counter_list：C； 位置输入：P 标准装箱位置输入：SP
	 * 自由装箱位置输入：FP 查找不到返回ERROR_DEVICE_NOT_FOUND错误(暂时没用)
	 * 
	 * @param devicename
	 *            查找内容,内容为设备名
	 * @param padOrStm32
	 *            true返回的是对应下位机的地址，false返回的是数组中的编号（主要是对于点的信息）
	 * @return ！！！！！！！！！！！！！！！！！返回的是该设备名对应下位机的地址！！！！！！！！！
	 * 
	 */
	/*public static void search(String aoperation,String devicename) {
		alloperation=aoperation;
		searchAddress(devicename,false);
		//return noteText;
	}*/
	public static int searchAddress(String devicename,
			Boolean padFalse_Stm32True) {

		int result = ERROR_DEVICE_NOT_FOUND;
		Iterator<HashMap<String, Object>> it;
		// 首先根据首字母进行判断
		devicename = devicename.trim();
		if(devicename.length()==0){
			return 0xffff;
		}
		switch (devicename.charAt(0)) {
		/*********************** 下面为数据设备，计算序号以及对应下位机的地址 **************/
		case 'X':// 对应IN[]
		case 'x':
			it = ArrayListBound.getDeviceActualInputListData().iterator();
			int deviceNum = ArrayListBound.getDeviceActualInputListData().size();
			int index = 0;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().trim().equals("")) {
					index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString().trim()
						.equalsIgnoreCase(devicename)) {
					if(padFalse_Stm32True==null){
						noteText=map.get("signalNameEditText").toString().trim();
						if(noteText.equals("")){
							noteText="未定义";
						}
						return 0;
					}
					if (padFalse_Stm32True) {
						// 下位机地址
						result = PlcData.getINoffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));
					} else {
						// 序号
						result = Integer.parseInt(map.get("addressText")
								.toString());
					}

					break;
					// 返回地址
				}
				index++;
			}
			if (index >= deviceNum) {
				result = 0xffff;
				return result;
			}
			break;
		case 'Y':
		case 'y':
			it = ArrayListBound.getDeviceActualOutputListData().iterator();
			deviceNum = ArrayListBound.getDeviceActualOutputListData().size();
			index = 0;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().trim().equals("")) {
					index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {
					if(padFalse_Stm32True==null){
						noteText=map.get("signalNameEditText").toString().trim();
						if(noteText.equals("")){
							noteText="未定义";
						}
						return 0;
					}
					if (padFalse_Stm32True) {
						// 下位机地址
						result = PlcData.getOUToffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));
					} else {
						// 序号
						result = Integer.parseInt(map.get("addressText")
								.toString());
					}
					break;
					// 返回地址
				}
				index++;
			}
			if (index >= deviceNum) {
				result = 0xffff;
				return result;
			}
			break;
		case 'M':// MRI[]
		case 'm':
			if (devicename.charAt(1) == 'X' || devicename.charAt(1) == 'x') {
				it = ArrayListBound.getDeviceMiddleInputListData().iterator();
				deviceNum = ArrayListBound.getDeviceMiddleInputListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().trim()
							.equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString()
							.equalsIgnoreCase(devicename)) {
						if(padFalse_Stm32True==null){
							noteText=map.get("signalNameEditText").toString().trim();
							if(noteText.equals("")){
								noteText="未定义";
							}
							return 0;
						}
						if (padFalse_Stm32True) {
							result = PlcData
									.getMRIoffsetAddress(Integer.parseInt(map
											.get("addressText").toString()));
						} else {
							result = Integer.parseInt(map.get("addressText")
									.toString());
						}

						break;
						// 返回地址
					}
					index++;
				}
				if (index >= deviceNum) {
					result = 0xffff;
					return result;
				}
			} else if (devicename.charAt(1) == 'Y'
					|| devicename.charAt(1) == 'y') {
				it = ArrayListBound.getDeviceMiddleOutputListData().iterator();
				deviceNum = ArrayListBound.getDeviceMiddleOutputListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().trim()
							.equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString()
							.equalsIgnoreCase(devicename)) {
						if(padFalse_Stm32True==null){
							noteText=map.get("signalNameEditText").toString().trim();
							if(noteText.equals("")){
								noteText="未定义";
							}
							return 0;
						}
						if (padFalse_Stm32True) {
							result = PlcData
									.getMROoffsetAddress(Integer.parseInt(map
											.get("addressText").toString()));
						} else {
							result = Integer.parseInt(map.get("addressText")
									.toString());
						}

						break;
						// 返回地址
					}
					index++;
				}
				if (index >= deviceNum) {
					result = 0xffff;
					return result;
				}
			} else {
				result = 0xffff;
				return result;
			}
			break;
		case 'D':
		case 'd':
			if (devicename.charAt(1) == 'X' || devicename.charAt(1) == 'x') {
				it = ArrayListBound.getDeviceDataInputListData().iterator();
				deviceNum = ArrayListBound.getDeviceDataInputListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().trim().equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString().trim()
							.equalsIgnoreCase(devicename)) {
						if(padFalse_Stm32True==null){
							noteText=map.get("signalNameEditText").toString().trim();
							if(noteText.equals("")){
								noteText="未定义";
							}
							return 0;
						}
						if (padFalse_Stm32True) {
							result = PlcData
									.getSDIoffsetAddress(Integer.parseInt(map
											.get("addressText").toString()));
						} else {
							result = Integer.parseInt(map.get("addressText")
									.toString());
						}

						break;
						// 返回地址
					}
					index++;
				}
				if (index >= deviceNum) {
					result = 0xffff;
					return result;
				}
			} else if (devicename.charAt(1) == 'Y'
					|| devicename.charAt(1) == 'y') {
				it = ArrayListBound.getDeviceDataOutputListData().iterator();
				deviceNum = ArrayListBound.getDeviceDataOutputListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().trim().equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString().trim()
							.equalsIgnoreCase(devicename)) {
						if(padFalse_Stm32True==null){
							noteText=map.get("signalNameEditText").toString().trim();
							if(noteText.equals("")){
								noteText="未定义";
							}
							return 0;
						}
						if (padFalse_Stm32True) {
							result = PlcData
									.getSDOoffsetAddress(Integer.parseInt(map
											.get("addressText").toString()));
						} else {
							result = Integer.parseInt(map.get("addressText")
									.toString());
						}

						break;
						// 返回地址
					}
					index++;
				}
				if (index >= deviceNum) {
					result = 0xffff;
					return result;
				}
			} else {
				result = 0xffff;
				return result;
			}
			break;
		case 'L':
		case 'l':
			if (devicename.charAt(1) == 'M' || devicename.charAt(1) == 'm') {
				it = ArrayListBound.getDeviceRegisterListData().iterator();
				deviceNum = ArrayListBound.getDeviceRegisterListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().trim()
							.equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString()
							.equalsIgnoreCase(devicename)) {
						if(padFalse_Stm32True==null){
							noteText=map.get("signalNameEditText").toString().trim();
							if(noteText.equals("")){
								noteText="未定义";
							}
							return 0;
						}
						if (padFalse_Stm32True) {
							result = PlcData
									.getMLRoffsetAddress(Integer.parseInt(map
											.get("addressText").toString()));
						} else {
							result = Integer.parseInt(map.get("addressText")
									.toString());
						}

						break;
						// 返回地址
					}
					index++;
				}
				if (index >= deviceNum) {
					result = 0xffff;
					return result;
				}
			} else if (devicename.charAt(1) == 'D'
					|| devicename.charAt(1) == 'd') {
				it = ArrayListBound.getDeviceDataRegisterListData().iterator();
				deviceNum = ArrayListBound.getDeviceDataRegisterListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().trim()
							.equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString()
							.equalsIgnoreCase(devicename)) {
						if(padFalse_Stm32True==null){
							noteText=map.get("signalNameEditText").toString().trim();
							if(noteText.equals("")){
								noteText="未定义";
							}
							return 0;
						}
						if (padFalse_Stm32True) {
							result = PlcData
									.getMLDoffsetAddress(Integer.parseInt(map
											.get("addressText").toString()));
						} else {
							result = Integer.parseInt(map.get("addressText")
									.toString());
						}
						break;
						// 返回地址
					}
					index++;
				}
				if (index >= deviceNum) {
					result = 0xffff;
					return result;
				}
			} else {
				result = 0xffff;
				return result;
			}
			break;
		/************************** 下面为定时器的地址或者在数组中的位置(暂时只需要知道定时器的序号) *************************************/
		case 'T':
		case 't':
			it = ArrayListBound.getDeviceTimerListData().iterator();
			deviceNum = ArrayListBound.getDeviceTimerListData().size();
			index = 0;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {
					if(padFalse_Stm32True==null){
						noteText=map.get("symbolNameEditText").toString().trim();
						if(noteText.equals("")){
							noteText="未定义";
						}
						return 0;
					}
					/*if(!map.get("setItem").toString().trim().equals("1")){
						return -1;
					}*/
					if (padFalse_Stm32True) {
						result = PlcData.getMLDoffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));
						// Integer
						// .valueOf(map.get("addressText").toString());
					} else {
						result = Integer.valueOf(map.get("addressText")
								.toString());
					}
					
					break;
					// 返回地址
				}
				index++;
			}
			if (index >= deviceNum) {
				result = 0xffff;
				return result;
			}
			break;
		/*********************** 下面为计数器的地址或者在数组中的位置(暂时只需要知道计数器的序号) **************/
		case 'C':
		case 'c':
			// 由于在model.h和PLCdata.h都有对应的地址，因此在这里只获取平板偏移地址，用的场合在进行转换
			it = ArrayListBound.getDeviceCounterListData().iterator();
			deviceNum = ArrayListBound.getDeviceCounterListData().size();
			index = 0;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {
					if(padFalse_Stm32True==null){
						noteText=map.get("symbolNameEditText").toString().trim();
						if(noteText.equals("")){
							noteText="未定义";
						}
						return 0;
					}
					/*if(!map.get("setItem").toString().trim().equals("1")){
						return -1;
					}*/
					if (padFalse_Stm32True) {
						result = PlcData.getMCNToffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));
					} else {
						result = Integer.valueOf(map.get("addressText")
								.toString());
					}
					break;
					// 返回地址
				}
				index++;
			}
			if (index >= deviceNum) {
				result = 0xffff;
				return result;
			}
			break;
		/************************** 下面为点的地址或者在数组中的位置 *****************************************/
		case 'P':
		case 'p':
			it = ArrayListBound.getDevicePositionListData().iterator();
			deviceNum = ArrayListBound.getDevicePositionListData().size();
			index = 0;
			int countP_point = 1;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				/*
				 * if (map.get("symbolNameEditText").toString()
				 * .contains(devicename
				 * )&&map.get("symbolNameEditText").toString(
				 * ).length()==devicename.length()) {
				 */
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {
					if(padFalse_Stm32True==null){
						noteText=map.get("symbolNameEditText").toString().trim();
						if(noteText.equals("")){
							noteText="未定义";
						}
						return 0;
					}
					/*if(!map.get("setItem").toString().trim().equals("1")){
						return -1;
					}*/
					if (padFalse_Stm32True) {
						// 这个result对应此设备名在下位机实际的存储地址

						result = AddressPublic.model_P_point_Head
								+ (AddressPublic.model_SP_point_Head - AddressPublic.model_P_point_Head)
								/ Define.MAX_STDPOINT_NUM * (countP_point - 1);
					} else {
						// 这个result对应此设备名在平板上显示的编号
						/*
						 * result = Integer
						 * .parseInt(map.get("addressText").toString());
						 */
						//result = countP_point;
						result =Integer.parseInt(devicename.substring(1));
					}
					break;
				} else if (map.get("symbolNameEditText").toString().charAt(0) == 'P'
						|| map.get("symbolNameEditText").toString().charAt(0) == 'p') {
					countP_point++;
				}

				index++;
			}
			if (index >= deviceNum) {
				result = 0xffff;
				return result;
			}
			break;
		case 'S':
		case 's':
			it = ArrayListBound.getDevicePositionListData().iterator();
			deviceNum = ArrayListBound.getDevicePositionListData().size();
			index = 0;
			countP_point = 1;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {
					if(padFalse_Stm32True==null){
						
						noteText=map.get("symbolNameEditText").toString().trim();
						if(noteText.equals("")){
							noteText="未定义";
						}
						return 0;
					}
					/*if(!map.get("setItem").toString().trim().equals("1")){
						return -1;
					}*/
					if (padFalse_Stm32True) {
						// 这个result对应此设备名在下位机实际的存储地址
						result = AddressPublic.model_SP_point_Head
								+ (AddressPublic.model_FP_point_Head - AddressPublic.model_SP_point_Head)
								/ Define.MAX_STDPACK_NUM * (countP_point - 1);
					} else {
						// 这个result对应此设备名在平板上显示的编号
						//result = countP_point;
						result =Integer.parseInt(devicename.substring(2));
					}
					break;
				} else if (map.get("symbolNameEditText").toString().charAt(0) == 'S'
						|| map.get("symbolNameEditText").toString().charAt(0) == 's') {
					countP_point++;
				}

				index++;
			}
			if (index >= deviceNum) {
				result = 0xffff;
				return result;

			}
			break;
		case 'F':
		case 'f':
			it = ArrayListBound.getDevicePositionListData().iterator();
			deviceNum = ArrayListBound.getDevicePositionListData().size();
			index = 0;
			int countP1_point =0x20003AB0;
			int countP2_point =0x20003AC6;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {
					if(padFalse_Stm32True==null){
						noteText=map.get("symbolNameEditText").toString().trim();
						if(noteText.equals("")){
							noteText="未定义";
						}
						return 0;
					}
					/*if(!map.get("setItem").toString().trim().equals("1")){
						return -1;
					}*/
					if (padFalse_Stm32True) {
						if(devicename.equalsIgnoreCase("FP001")){
							   result = countP1_point;
							}
							if(devicename.equalsIgnoreCase("FP002")){
						       result = countP2_point;
							}
					} else {
						// 这个result对应此设备名在平板上显示的编号
						result = 1;
						
					} 
					break;
				}
				index++;
			}
			if (index >= deviceNum) {
				result = 0xffff;
				return result;
			}
			break;
		default:
			
			it = ArrayListBound.getDeviceAlarmListData().iterator();
			deviceNum = ArrayListBound.getDeviceAlarmListData().size();
			index = 0;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {
					if(padFalse_Stm32True==null){
						noteText=map.get("noteEditText").toString().trim();
						if(noteText.equals("")){
							noteText="未定义";
						}
						return 0;
					}
					if (padFalse_Stm32True) {
						result = 1;
					}
					break;
				}
				index++;
			}
			if (index >= deviceNum) {
				result = 0xffff;
				return result;
			}
		}

		/*if (padFalse_Stm32True) {
			System.out.println("plcdata偏移" + result);
		} else {
			System.out.println("平板偏移" + result);
		}*/
		return result;
	}

	/**
	 * 
	 * @param devicename
	 * @return int[0]对应下位机地址，int[1]对应序号
	 */


	public static int[] deviceSearch(String devicename, int flog) {
		TableToBinary.flog = flog;
		return deviceSearchAddress(devicename);
	}

	public static int[] deviceSearchAddress(String devicename) {

		//System.out.println("devicename="+devicename);
		int result[] = { ERROR_DEVICE_NOT_FOUND, ERROR_DEVICE_NOT_FOUND };
		Iterator<HashMap<String, Object>> it;
		devicename = devicename.trim();
		// 首先根据首字母进行判断
		switch (devicename.trim().charAt(0)) {
		/*********************** 下面为数据设备，计算序号以及对应下位机的地址 **************/
		case 'X':// 对应IN[]
		case 'x':
			it = ArrayListBound.getDeviceActualInputListData().iterator();
			int deviceNum = ArrayListBound.getDeviceActualInputListData().size();
			int index = 0;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {

					// 下位机地址
					result[0] = PlcData.getINoffsetAddress(Integer.parseInt(map
							.get("addressText").toString()));

					// 序号
					result[1] = Integer.parseInt(map.get("addressText")
							.toString());

					break;
					// 返回地址
				}
				index++;

			}
			if (index >= deviceNum) {

				// dname =" "+dname+devicename+" ";
				result[0] = 0xffff;
				result[1] = 0xffff;
				return result;

			}
			break;
		case 'Y':
		case 'y':
			it = ArrayListBound.getDeviceActualOutputListData().iterator();
			deviceNum = ArrayListBound.getDeviceActualOutputListData().size();
			index = 0;
			//System.out.println("deviceNum="+deviceNum);
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {

					// 下位机地址
					result[0] = PlcData.getOUToffsetAddress(Integer
							.parseInt(map.get("addressText").toString()));

					// 序号
					result[1] = Integer.parseInt(map.get("addressText")
							.toString());

					break;
					// 返回地址
				}
				index++;

			}
			//System.out.println("index"+index);
			if (index >= deviceNum) {

				// dname =" "+dname+devicename+" ";
				result[0] = 0xffff;
				result[1] = 0xffff;
				return result;

			}
			break;
		case 'M':// MRI[]
		case 'm':
			if (devicename.charAt(1) == 'X' || devicename.charAt(1) == 'x') {
				it = ArrayListBound.getDeviceMiddleInputListData().iterator();
				deviceNum = ArrayListBound.getDeviceMiddleInputListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString().trim()
							.equalsIgnoreCase(devicename)) {

						result[0] = PlcData.getMRIoffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));

						result[1] = Integer.parseInt(map.get("addressText")
								.toString());
						break;
						// 返回地址
					}
					index++;

				}
				if (index >= deviceNum) {

					// dname =" "+dname+devicename+" ";
					result[0] = 0xffff;
					result[1] = 0xffff;
					return result;

				}

			} else if (devicename.charAt(1) == 'Y'
					|| devicename.charAt(1) == 'y') {
				it = ArrayListBound.getDeviceMiddleOutputListData().iterator();
				deviceNum = ArrayListBound.getDeviceMiddleOutputListData().size();
				index = 0;
				//System.out.println("进入 m+y");
				//System.out.println("deviceNum="+deviceNum);
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString().trim()
							.equalsIgnoreCase(devicename)) {

						result[0] = PlcData.getMROoffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));

						result[1] = Integer.parseInt(map.get("addressText")
								.toString());

						break;
						// 返回地址
					}
					index++;

				}
				//System.out.println("index="+index);
				if (index >= deviceNum) {

					// dname =" "+dname+devicename+" ";
					result[0] = 0xffff;
					result[1] = 0xffff;
					return result;

				}
			} else {
				result[0] = 0xffff;
				result[1] = 0xffff;
				return result;
			}
			break;
		case 'D':
		case 'd':
			if (devicename.charAt(1) == 'X' || devicename.charAt(1) == 'x') {
				it = ArrayListBound.getDeviceDataInputListData().iterator();
				deviceNum = ArrayListBound.getDeviceDataInputListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().trim()
							.equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString().trim()
							.equalsIgnoreCase(devicename)) {

						result[0] = PlcData.getSDIoffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));

						result[1] = Integer.parseInt(map.get("addressText")
								.toString());
						break;
						// 返回地址
					}
					index++;

				}
				if (index >= deviceNum) {

					// dname =" "+dname+devicename+" ";
					result[0] = 0xffff;
					result[1] = 0xffff;
					return result;

				}
			} else if (devicename.charAt(1) == 'Y'
					|| devicename.charAt(1) == 'y') {
				it = ArrayListBound.getDeviceDataOutputListData().iterator();
				deviceNum = ArrayListBound.getDeviceDataOutputListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().trim()
							.equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString().trim()
							.equalsIgnoreCase(devicename)) {

						result[0] = PlcData.getSDOoffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));

						result[1] = Integer.parseInt(map.get("addressText")
								.toString());

						break;
						// 返回地址
					}
					index++;

				}
				if (index >= deviceNum) {

					// dname =" "+dname+devicename+" ";
					result[0] = 0xffff;
					result[1] = 0xffff;
					return result;

				}

			} else {
				result[0] = 0xffff;
				result[1] = 0xffff;
				return result;
			}
			break;
		case 'L':
		case 'l':
			if (devicename.charAt(1) == 'M' || devicename.charAt(1) == 'm') {
				it = ArrayListBound.getDeviceRegisterListData().iterator();
				deviceNum = ArrayListBound.getDeviceRegisterListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString()
							.equalsIgnoreCase(devicename)) {

						result[0] = PlcData.getMLRoffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));

						result[1] = Integer.parseInt(map.get("addressText")
								.toString());

						break;
						// 返回地址
					}
					index++;

				}
				if (index >= deviceNum) {

					// dname =" "+dname+devicename+" ";
					result[0] = 0xffff;
					result[1] = 0xffff;
					return result;
				}
			} else if (devicename.charAt(1) == 'D'
					|| devicename.charAt(1) == 'd') {
				it = ArrayListBound.getDeviceDataRegisterListData().iterator();
				deviceNum = ArrayListBound.getDeviceDataRegisterListData().size();
				index = 0;
				while (it.hasNext()) {
					HashMap<String, Object> map = it.next();
					if (map.get("symbolNameEditText").toString().equals("")) {
						 index++;
						continue;
					}
					if (map.get("symbolNameEditText").toString()
							.equalsIgnoreCase(devicename)) {

						result[0] = PlcData.getMLDoffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));

						result[1] = Integer.parseInt(map.get("addressText")
								.toString());

						break;
						// 返回地址
					}
					index++;

				}
				if (index >= deviceNum) {

					// dname =" "+dname+devicename+" ";
					result[0] = 0xffff;
					result[1] = 0xffff;
					return result;

				}
			} else {
				result[0] = 0xffff;
				result[1] = 0xffff;
				return result;
			}
			break;
		/************************** 下面为定时器的地址或者在数组中的位置(暂时只需要知道定时器的序号) *************************************/
		case 'T':
		case 't':
			it = ArrayListBound.getDeviceTimerListData().iterator();
			deviceNum = ArrayListBound.getDeviceTimerListData().size();
			index = 0;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {
					/*if(!map.get("setItem").toString().trim().equals("1")){
						result[0] = -1;
						result[1] = -1;
						return result;
					}*/
					if (flog == 1) {
						result[0] = PlcData.getMTMUpoffsetAddress(Integer
								.parseInt(map.get("addressText").toString()));
					}
					
					// Integer.parseInt(map.get("addressText")
					// .toString());
					if (flog == 0) {
					result[0] = PlcData.getMTMExoffsetAddress(Integer
							.parseInt(map.get("addressText").toString()));
					}
					result[1] = Integer.parseInt(map.get("addressText")
							.toString());

					break;
					// 返回地址
				}
				index++;

			}
			if (index >= deviceNum) {

				// dname =" "+dname+devicename+" ";
				result[0] = 0xffff;
				result[1] = 0xffff;
				return result;

			}
			break;
		/*********************** 下面为计数器的地址或者在数组中的位置(暂时只需要知道计数器的序号) **************/
		case 'C':
		case 'c':
			it = ArrayListBound.getDeviceCounterListData().iterator();
			deviceNum = ArrayListBound.getDeviceCounterListData().size();
			index = 0;
			while (it.hasNext()) {
				HashMap<String, Object> map = it.next();
				if (map.get("symbolNameEditText").toString().equals("")) {
					 index++;
					continue;
				}
				if (map.get("symbolNameEditText").toString()
						.equalsIgnoreCase(devicename)) {
					/*if(!map.get("setItem").toString().trim().equals("1")){
						result[0] = -1;
						result[1] = -1;
						return result;
					}*/
					if(flog==1){
					result[0] = PlcData.getMCNTUpoffsetAddress(Integer
							.parseInt(map.get("addressText").toString()));
					// Integer
					}
					// .parseInt(map.get("addressText").toString());
					if(flog==0){
					result[0] = PlcData.getMCNToffsetAddress(Integer
							.parseInt(map.get("addressText").toString()));
					}
					result[1] = Integer.parseInt(map.get("addressText")
							.toString());
					break;
					// 返回地址

				}
				index++;

			}
			if (index >= deviceNum) {

				// dname =" "+dname+devicename+" ";
				result[0] = 0xffff;
				result[1] = 0xffff;
				return result;

			}
			break;

		default:
			result[0] = 0xffff;
			result[1] = 0xffff;
			return result;
		}
		//System.out.println("plcdata偏移地址" + result[0] + "平板内偏移" + result[1]);
		return result;
	}

	/**
	 * through 换行函数
	 * 
	 * @param headOrEnd
	 *            起始结尾标志，true为起始换行
	 * @return return 返回长度为4的字节数组
	 */
	public static byte[] getTHROUGHBytes(boolean headOrEnd) {
		byte[] codeTemp = new byte[12];

		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_THROUGH), 0,
				codeTemp, 0, byteBlockLength);

		if (headOrEnd) {
			System.arraycopy(HexDecoding.int2byteArray4(1), 0, codeTemp, 4,
					byteBlockLength);// 开始为1
		} else {
			System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 4,
					byteBlockLength);// 结束为0
		}
		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;

	}

	public static byte[] getTHROUGHBytes(boolean headOrEnd, Boolean temp) {
		byte[] codeTemp = new byte[12];

		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_THROUGH), 0,
				codeTemp, 0, byteBlockLength);

		if (headOrEnd) {
			System.arraycopy(HexDecoding.int2byteArray4(1), 0, codeTemp, 4,
					byteBlockLength);// 开始为1
		} else {
			System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 4,
					byteBlockLength);// 结束为0
		}
		if (temp) {
			System.arraycopy(HexDecoding.int2byteArray4(1), 0, codeTemp, 8,
					byteBlockLength);
		} else {
			System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
					byteBlockLength);
		}

		return codeTemp;

	}

	/**
	 * AND与指令
	 * 
	 * @return return 返回长度为4的字节数组
	 */
	public static byte[] getANDBytes() {
		byte[] codeTemp = new byte[12];

		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_AND), 0,
				codeTemp, 0, byteBlockLength);

		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 4,
				byteBlockLength);

		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;

	}

	/**
	 * OR或table指令
	 * 
	 * @return return 返回长度为4的字节数组
	 */
	public static byte[] getORBytes() {
		byte[] codeTemp = new byte[12];

		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_OR), 0,
				codeTemp, 0, byteBlockLength);

		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 4,
				byteBlockLength);

		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;

	}

	/**
	 * label行号table指令
	 * 
	 * @return return 返回长度为4的字节数组
	 */
	public static byte[] getLABELbytes(int num) {
		byte[] codeTemp = new byte[12];

		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_ORDER_LABEL),
				0, codeTemp, 0, byteBlockLength);

		System.arraycopy(
				HexDecoding.int2byteArray4(num + Constans.Label_Offset), 0,
				codeTemp, 4, byteBlockLength);

		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;

	}

	/**
	 * Subroutine函数号table指令
	 * 
	 * @return return 返回长度为4的字节数组
	 */
	public static byte[] getSUBRoutinebytes(int num) {
		byte[] codeTemp = new byte[12];

		System.arraycopy(
				HexDecoding.int2byteArray4(Constans.Code_ORDER_SUBRoutine), 0,
				codeTemp, 0, byteBlockLength);

		System.arraycopy(
				HexDecoding.int2byteArray4(num + Constans.Subroutine_Offset),
				0, codeTemp, 4, byteBlockLength);

		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;

	}
	public static byte[] getQbytes(int num) {
		byte[] codeTemp = new byte[12];

		System.arraycopy(
				HexDecoding.int2byteArray4(Constans.Code_ORDER_Q), 0,
				codeTemp, 0, byteBlockLength);

		System.arraycopy(
				HexDecoding.int2byteArray4(num),
				0, codeTemp, 4, byteBlockLength);

		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;

	}
	/**
	 * 赋值move函数(立即数)
	 * 
	 * @param immData
	 *            立即数数据
	 * @param offset
	 *            赋值进去的偏移地址
	 * @return 返回长度为4的字节数组
	 */
	public static byte[] getMOVE_IMMBytes(int immData, int offset,char ch,int allA) {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_MOVE_IMM), 0,
				codeTemp, 0, byteBlockLength);

		// TST位置偏移
		System.arraycopy(HexDecoding.int2byteArray4(offset), 0, codeTemp, 4,
				byteBlockLength);

		// 运动点位置
		if(ch=='P'||ch=='p'){
		   System.arraycopy(HexDecoding.int2byteArray4(immData+allA), 0, codeTemp, 8,
				byteBlockLength);
		    }
		if(ch=='S'||ch=='s'){
			if( immData != 0)
				   System.arraycopy(HexDecoding.int2byteArray4(immData+211+allA), 0, codeTemp, 8,
						byteBlockLength);
				else
					System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
							byteBlockLength);
			}
		if(ch=='F'||ch=='f'){
			if( immData != 0)
				   System.arraycopy(HexDecoding.int2byteArray4(immData+221+allA), 0, codeTemp, 8,
						byteBlockLength);
				else
					System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
							byteBlockLength);
	}
		return codeTemp;
	}
	public static byte[] getMOVE_IMMBytes(int immData, int offset) {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_MOVE_IMM), 0,
				codeTemp, 0, byteBlockLength);

		// TST位置偏移
		System.arraycopy(HexDecoding.int2byteArray4(offset), 0, codeTemp, 4,
				byteBlockLength);

		// 运动点位置
		System.arraycopy(HexDecoding.int2byteArray4(immData), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;
	}

	/**
	 * 赋值，非立即数，偏移地址内的数据 之间相互赋值
	 * 
	 * @param dataOffset
	 *            需要赋值进去的数据所在的地址
	 * @param offset
	 *            赋值进去的偏移地址
	 * @return 返回长度为4的字节数组
	 */
	public static byte[] getMOVE_Bytes(int dataOffset, int offset) {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_MOVE), 0,
				codeTemp, 0, byteBlockLength);

		// TST位置偏移
		System.arraycopy(HexDecoding.int2byteArray4(offset), 0, codeTemp, 4,
				byteBlockLength);

		// 运动点位置
		System.arraycopy(HexDecoding.int2byteArray4(dataOffset), 0, codeTemp,
				8, byteBlockLength);

		return codeTemp;
	}

	/**
	 * CP(=,!=,<,>,<=,>=)(IMM)table指令，立即数
	 * 
	 * @param comparedata
	 *            立即数
	 * @param offset
	 *            偏移位置
	 * @param codeNo
	 *            指令代码，(=,!=,<,>,<=,>=)6种指令
	 * @return 返回长度为4的字节数组
	 */
	public static byte[] getCP_Bytes(int codeNo, int offset, int comparedata,char ch,int allA) {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(codeNo), 0, codeTemp, 0,
				byteBlockLength);

		// TEN位置偏移,该位置一定是偏移地址
		System.arraycopy(HexDecoding.int2byteArray4(offset), 0, codeTemp, 4,
				byteBlockLength);

		// data的数据可能是偏移地址，也有可能是立即数
		if(ch=='P'||ch=='p'){
		System.arraycopy(HexDecoding.int2byteArray4(comparedata+allA), 0, codeTemp,
				8, byteBlockLength);
		}
		if(ch=='S'||ch=='s'){
			System.arraycopy(HexDecoding.int2byteArray4(comparedata+211+allA), 0, codeTemp,
					8, byteBlockLength);
			}
		if(ch=='F'||ch=='f'){
			System.arraycopy(HexDecoding.int2byteArray4(comparedata+221+allA), 0, codeTemp,
					8, byteBlockLength);
			}

		return codeTemp;
	}
	public static byte[] getCP_Bytes(int codeNo, int offset, int comparedata) {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(codeNo), 0, codeTemp, 0,
				byteBlockLength);

		// TEN位置偏移,该位置一定是偏移地址
		System.arraycopy(HexDecoding.int2byteArray4(offset), 0, codeTemp, 4,
				byteBlockLength);

		// data的数据可能是偏移地址，也有可能是立即数
		System.arraycopy(HexDecoding.int2byteArray4(comparedata), 0, codeTemp,
				8, byteBlockLength);

		return codeTemp;
	}
	/**
	 * 对应IN指令（if）中的”如果真 ON“ table指令
	 * 
	 * @param address
	 *            设备定义名在listview中的address
	 * @return 返回长度为4的字节数组
	 */
	public static byte[] getA_1Bytes(int address, int bitmask) {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_A_1), 0,
				codeTemp, 0, byteBlockLength);

		// TEN位置偏移
		System.arraycopy(HexDecoding.int2byteArray4(address), 0, codeTemp, 4,
				byteBlockLength);

		//
		System.arraycopy(HexDecoding.int2byteArray4(bitmask), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;
	}

	/**
	 * 对应IN指令（if）中的”如果假 OFF“ table指令
	 * 
	 * @param address
	 * @return 返回长度为4的字节数组
	 */
	public static byte[] getB_1Bytes(int address, int bitmask) {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_B_1), 0,
				codeTemp, 0, byteBlockLength);

		// TEN位置偏移
		System.arraycopy(HexDecoding.int2byteArray4(address), 0, codeTemp, 4,
				byteBlockLength);

		// 运动点位置
		System.arraycopy(HexDecoding.int2byteArray4(bitmask), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;
	}

	/**
	 * 对应OUT指令（结果输出）中的 输出ON table指令
	 * 
	 * @param offset
	 *            设备定义的偏移地址
	 * @return 返回长度为4的字节数组
	 */
	public static byte[] getOUT_ONBytes(int offset, int bitMask) {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_OUT_ON), 0,
				codeTemp, 0, byteBlockLength);

		// TEN位置偏移PlcData.getOUToffsetAddress(offset)
		System.arraycopy(HexDecoding.int2byteArray4(offset), 0, codeTemp, 4,
				byteBlockLength);

		// 运动点位置
		System.arraycopy(HexDecoding.int2byteArray4(bitMask), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;
	}

	/**
	 * 对应OUT指令（结果输出）中的 输出OFF table指令
	 * 
	 * @param offset
	 *            设备定义的偏移地址
	 * @return 返回长度为4的字节数组
	 */
	public static byte[] getOUT_OFFBytes(int offset, int bitMask) {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_OUT_OFF), 0,
				codeTemp, 0, byteBlockLength);

		// TEN位置偏移PlcData.getOUToffsetAddress(offset)
		System.arraycopy(HexDecoding.int2byteArray4(offset), 0, codeTemp, 4,
				byteBlockLength);

		// 运动点位置 offset % 32
		System.arraycopy(HexDecoding.int2byteArray4(bitMask), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;
	}

	/**
	 * seq_end table 指令
	 * 
	 * @return 返回长度为4的字节数组
	 */
	public static byte[] getSEQ_ENDBytes() {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_SEQ_END), 0,
				codeTemp, 0, byteBlockLength);

		// TEN位置偏移
		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 4,
				byteBlockLength);

		// 运动点位置
		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;
	}

	/**
	 * 程序内跳转，无返回的table指令
	 * 
	 * @return 返回长度为四的字节数组
	 */
	public static byte[] getJUMP(String operation) {
		
		byte[] codeTemp = new byte[12];
		try{
		//System.out.println("JUMP");
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_JUMP), 0,
				codeTemp, 0, byteBlockLength);

		// 跳转地址,读取Lxxx中xxx数字+65536
		System.arraycopy(
				HexDecoding.int2byteArray4(Integer.parseInt(operation
						.substring(1)) + Constans.Label_Offset), 0, codeTemp,
				4, byteBlockLength);

		// jump指数第二参数固定为1
		System.arraycopy(HexDecoding.int2byteArray4(1), 0, codeTemp, 8,
				byteBlockLength);
	} catch (Exception e) {
		e.printStackTrace();
	}
		return codeTemp;
	}

	/**
	 * 程序内条件跳转，无返回的table指令
	 * 
	 * @return 返回长度为四的字节数组
	 */
	public static byte[] getJUMPON(String operation) {
		byte[] codeTemp = new byte[12];
		try{
		//System.out.println("JUMPON" + operation);
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_JUMP_ON), 0,
				codeTemp, 0, byteBlockLength);

		// 跳转地址,读取Lxxx中xxx数字+65536
		System.arraycopy(
				HexDecoding.int2byteArray4(Integer.parseInt(operation
						.substring(1)) + Constans.Label_Offset), 0, codeTemp,
				4, byteBlockLength);

		// jumpon指数第二参数固定为1
		System.arraycopy(HexDecoding.int2byteArray4(1), 0, codeTemp, 8,
				byteBlockLength);
	} catch (Exception e) {
		e.printStackTrace();
	}
		return codeTemp;
	}

	/**
	 * 程序内跳转，有返回的table指令，必须检查ret的存在与否
	 * 
	 * @return 返回长度为四的字节数组
	 */
	public static byte[] getCALL(String operation) {
		//System.out.println("call");
		byte[] codeTemp = new byte[12];
		try{
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_CALL), 0,
				codeTemp, 0, byteBlockLength);

		// 跳转地址,读取Sxxx中xxx数字+256
		System.arraycopy(
				HexDecoding.int2byteArray4(Integer.parseInt(operation
						.substring(1)) + Constans.Subroutine_Offset), 0,
				codeTemp, 4, byteBlockLength);

		// call指数最后一位固定为0
		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);
	} catch (Exception e) {
		e.printStackTrace();
	}
		return codeTemp;
	}

	/**
	 * 程序内条件跳转，有返回的table指令，必须检查ret的存在与否
	 * 
	 * @return 返回长度为四的字节数组
	 */
	public static byte[] getCALLON(String operation) {
		System.out.println("callon");
		byte[] codeTemp = new byte[12];
		try{
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_CALL_ON), 0,
				codeTemp, 0, byteBlockLength);

		// 跳转地址,读取Sxxx中xxx数字+256
		System.arraycopy(
				HexDecoding.int2byteArray4(Integer.parseInt(operation
						.substring(1)) + Constans.Subroutine_Offset), 0,
				codeTemp, 4, byteBlockLength);

		// callon指数最后一位固定为0
		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);
	} catch (Exception e) {
		e.printStackTrace();
	}
		return codeTemp;
	}
	public static byte[] getQON(String operation) {
		System.out.println("QON");
		byte[] codeTemp = new byte[12];
		try{
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_QON), 0,
				codeTemp, 0, byteBlockLength);

		// 跳转地址,读取Sxxx中xxx数字
		System.arraycopy(
				HexDecoding.int2byteArray4(Integer.parseInt(operation
						.substring(1))), 0,
				codeTemp, 4, byteBlockLength);

		// qon指数最后一位固定为0
		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);
	} catch (Exception e) {
		e.printStackTrace();
	}
		return codeTemp;
	}
	public static byte[] getQOFF(String operation) {
		System.out.println("QOFF");
		byte[] codeTemp = new byte[12];
		try{
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_QOFF), 0,
				codeTemp, 0, byteBlockLength);

		// 跳转地址,读取Sxxx中xxx数字
		System.arraycopy(
				HexDecoding.int2byteArray4(Integer.parseInt(operation
						.substring(1))), 0,
				codeTemp, 4, byteBlockLength);

		// qoff指数最后一位固定为0
		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);
	} catch (Exception e) {
		e.printStackTrace();
	}
		return codeTemp;
	}
	/**
	 * 计数器加一
	 * 
	 * @param operation
	 * @return
	 */
	public static byte[] getINC(String operation) {

		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_INC), 0,
				codeTemp, 0, byteBlockLength);

		// Cxxx的偏移地址(对应plcdata中)
		System.arraycopy(HexDecoding.int2byteArray4(PlcData
				.getMCNToffsetAddress(TableToBinary.searchAddress(operation,
						false))), 0, codeTemp, 4, byteBlockLength);

		// callon指数最后一位固定为1
		System.arraycopy(HexDecoding.int2byteArray4(1), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;
	}

	/**
	 * 计数器减一
	 * 
	 * @param operation
	 * @return
	 */
	public static byte[] getDEC(String operation) {

		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_DEC), 0,
				codeTemp, 0, byteBlockLength);

		// 跳转地址,读取Sxxx中xxx数字+256
		System.arraycopy(HexDecoding.int2byteArray4(PlcData
				.getMCNToffsetAddress(TableToBinary.searchAddress(operation,
						false))), 0, codeTemp, 4, byteBlockLength);

		// callon指数最后一位固定为1
		System.arraycopy(HexDecoding.int2byteArray4(1), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;
	}

	/**
	 * 程序内函数返回，ret与call对应
	 * 
	 * @return 返回长度为四的字节数组
	 */
	public static byte[] getRet() {
		byte[] codeTemp = new byte[12];
		// 命令代码
		System.arraycopy(HexDecoding.int2byteArray4(Constans.Code_SEQ_RET), 0,
				codeTemp, 0, byteBlockLength);

		// 固定为0
		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 4,
				byteBlockLength);

		// 固定为0
		System.arraycopy(HexDecoding.int2byteArray4(0), 0, codeTemp, 8,
				byteBlockLength);

		return codeTemp;
	}
}
