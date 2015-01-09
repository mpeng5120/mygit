package com.explain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.tr.programming.TR_Programming_Activity;

/**
 * 用来解释系统table和用户table的类
 * 
 * @author 李婷婷
 * 
 */
public class TableTranslate {
	//public static HashMap<Object, Object> info=null; 
    public static String  dname=null;
    public static String  tableindex;
    public static ArrayList<String> tnoteList = new ArrayList<String>();
	// 容器中每一条记录为长度12的byte数组
	private static ArrayList<byte[]> binary_code = new ArrayList<byte[]>(){

		@Override
		public boolean add(byte[] object) {
			// TODO Auto-generated method stub
			HexDecoding.printHexString("添加", object);
			return super.add(object);
		}
		
	};

	/**
	 * 生成最终的byte数组，发送给下位机
	 * 
	 * @return 数据的byte数组
	 */
	public static byte[] getMesHexCode() {
		Iterator<byte[]> it = binary_code.iterator();
		// 根据生成的容器的大小，确定发送的byte数组
		byte[] message = new byte[binary_code.size() * 12];
		// 计数器
		int counter = 0;

		while (it.hasNext()) {

			System.arraycopy(it.next(), 0, message, counter, 12);
			counter += 12;

		}
		return message;
	}

	public static int beginTranslate() {
		int  index=0;
		 int and1=ArrayListBound.getTableList1Data().size();
		 int and2=and1+ArrayListBound.getTableList2Data().size();
		 int and3=and2+ArrayListBound.getTableList3Data().size();
		 int and4=and3+ArrayListBound.getTableList4Data().size();
		 int and5=and4+ArrayListBound.getTableList5Data().size();
		 int and6=and5+ArrayListBound.getTableList6Data().size();
		 int and7=and6+ArrayListBound.getTableList7Data().size();
		if (!binary_code.isEmpty()) {
			binary_code.clear();
		}
		if(TR_Programming_Activity.noteflag==1||TR_Programming_Activity.noteflag==0){
		Iterator<HashMap<String, Object>> it1 = ArrayListBound.getTableList1Data().iterator();
        while (it1.hasNext()) {
			index++;
			HashMap<String, Object> map = it1.next();
			String inputString = map.get("inputEditText").toString();// IF命令中所有的条件
			String outputString = map.get("outputEditText").toString();// OUT命令中所有的条件
			if(inputString.trim().equals("")||outputString.trim().equals("")){
				tnoteList.add("");
				continue;
			}
			int flag=codeExplain(inputString,outputString);
			if(flag==-1){
				tableindex="页面1中序号为"+String.format("%04d", (index-1));
				return -1;
			}
			if(flag==1){
				tableindex="页面1中序号为"+String.format("%04d", (index-1));
				return index;
			}
        }
		}
		if(TR_Programming_Activity.noteflag==2||TR_Programming_Activity.noteflag==0){
    	Iterator<HashMap<String, Object>> it2 = ArrayListBound.getTableList2Data().iterator();
        while (it2.hasNext()) {
			index++;
			HashMap<String, Object> map = it2.next();
			String inputString = map.get("inputEditText").toString();// IF命令中所有的条件
			String outputString = map.get("outputEditText").toString();// OUT命令中所有的条件
			if(inputString.trim().equals("")||outputString.trim().equals("")){
				tnoteList.add("");
				continue;
			}
			int flag=codeExplain(inputString,outputString);
			if(flag==-1){
				tableindex="页面2中序号为"+String.format("%04d", (index-and1-1));
				return -1;
			}
			if(flag==1){
				tableindex="页面2中序号为"+String.format("%04d", (index-and1-1));
				return index;
			}
        }
		}
		if(TR_Programming_Activity.noteflag==3||TR_Programming_Activity.noteflag==0){
    	Iterator<HashMap<String, Object>> it3 = ArrayListBound.getTableList3Data().iterator();
        while (it3.hasNext()) {
			index++;
			HashMap<String, Object> map = it3.next();
			String inputString = map.get("inputEditText").toString();// IF命令中所有的条件
			String outputString = map.get("outputEditText").toString();// OUT命令中所有的条件
			if(inputString.trim().equals("")||outputString.trim().equals("")){
				tnoteList.add("");
				continue;
			}
			int flag=codeExplain(inputString,outputString);
			if(flag==-1){
				tableindex="页面3中序号为"+String.format("%04d", (index-and2-1));
				return -1;
			}
			if(flag==1){
				tableindex="页面3中序号为"+String.format("%04d", (index-and2-1));
				return index;
			}
        }
		}
		if(TR_Programming_Activity.noteflag==4||TR_Programming_Activity.noteflag==0){
    	Iterator<HashMap<String, Object>> it4 = ArrayListBound.getTableList4Data().iterator();
        while (it4.hasNext()) {
			index++;
			HashMap<String, Object> map = it4.next();
			String inputString = map.get("inputEditText").toString();// IF命令中所有的条件
			String outputString = map.get("outputEditText").toString();// OUT命令中所有的条件
			if(inputString.trim().equals("")||outputString.trim().equals("")){
				tnoteList.add("");
				continue;
			}
			int flag=codeExplain(inputString,outputString);
			if(flag==-1){
				tableindex="页面4中序号为"+String.format("%04d", (index-and3-1));
				return -1;
			}
			if(flag==1){
				tableindex="页面4中序号为"+String.format("%04d", (index-and3-1));
				return index;
			}
        }
		}
		if(TR_Programming_Activity.noteflag==5||TR_Programming_Activity.noteflag==0){
    	Iterator<HashMap<String, Object>> it5 = ArrayListBound.getTableList5Data().iterator();
        while (it5.hasNext()) {
			index++;
			HashMap<String, Object> map = it5.next();
			String inputString = map.get("inputEditText").toString();// IF命令中所有的条件
			String outputString = map.get("outputEditText").toString();// OUT命令中所有的条件
			if(inputString.trim().equals("")||outputString.trim().equals("")){
				tnoteList.add("");
				continue;
			}
			int flag=codeExplain(inputString,outputString);
			if(flag==-1){
				tableindex="页面5中序号为"+String.format("%04d", (index-and4-1));
				return -1;
			}
			if(flag==1){
				tableindex="页面5中序号为"+String.format("%04d", (index-and4-1));
				return index;
			}
        }
		}
		if(TR_Programming_Activity.noteflag==6||TR_Programming_Activity.noteflag==0){
    	Iterator<HashMap<String, Object>> it6 = ArrayListBound.getTableList6Data().iterator();
        while (it6.hasNext()) {
			index++;
			HashMap<String, Object> map = it6.next();
			String inputString = map.get("inputEditText").toString();// IF命令中所有的条件
			String outputString = map.get("outputEditText").toString();// OUT命令中所有的条件
			if(inputString.trim().equals("")||outputString.trim().equals("")){
				tnoteList.add("");
				continue;
			}
			int flag=codeExplain(inputString,outputString);
			if(flag==-1){
				tableindex="页面6中序号为"+String.format("%04d", (index-and5-1));
				return -1;
			}
			if(flag==1){
				tableindex="页面6中序号为"+String.format("%04d", (index-and5-1));
				return index;
			}
        }
		}
		if(TR_Programming_Activity.noteflag==7||TR_Programming_Activity.noteflag==0){
    	Iterator<HashMap<String, Object>> it7 = ArrayListBound.getTableList7Data().iterator();
        while (it7.hasNext()) {
			index++;
			HashMap<String, Object> map = it7.next();
			String inputString = map.get("inputEditText").toString();// IF命令中所有的条件
			String outputString = map.get("outputEditText").toString();// OUT命令中所有的条件
			if(inputString.trim().equals("")||outputString.trim().equals("")){
				tnoteList.add("");
				continue;
			}
			int flag=codeExplain(inputString,outputString);
			if(flag==-1){
				tableindex="页面7中序号为"+String.format("%04d", (index-and6-1));
				return -1;
			}
			if(flag==1){
				tableindex="页面7中序号为"+String.format("%04d", (index-and6-1));
				return index;
			}
        }
		}
		if(TR_Programming_Activity.noteflag==8||TR_Programming_Activity.noteflag==0){
    	Iterator<HashMap<String, Object>> it8 = ArrayListBound.getTableList8Data().iterator();
        while (it8.hasNext()) {
			index++;
			HashMap<String, Object> map = it8.next();
			String inputString = map.get("inputEditText").toString();// IF命令中所有的条件
			String outputString = map.get("outputEditText").toString();// OUT命令中所有的条件
			if(inputString.trim().equals("")||outputString.trim().equals("")){
				tnoteList.add("");
				continue;
			}
			int flag=codeExplain(inputString,outputString);
			if(flag==-1){
				tableindex="页面8中序号为"+String.format("%04d", (index-and7-1));
				return -1;
			}
			if(flag==1){
				tableindex="页面8中序号为"+String.format("%04d", (index-and7-1));
				return index;
			}
        }
		}
		return index;
	}

	private static String replace_table(String rege,String str,String value){
		try{
		   String regex = "[^A-Za-z0-9]"+rege+"[^A-Za-z0-9]";
		   str=" "+str+" ";
		   Pattern pat = Pattern.compile(regex);  
		   Matcher matcher = pat.matcher(str); 
		   int count=0;
		   while (matcher.find()) { 
			  int length=value.length()-rege.length();
			  String qtemp = str.substring(0,matcher.start()+count*length);
			  String htemp = str.substring(matcher.end()+count*length,str.length());
		     String temp = str.substring(matcher.start()+count*length,matcher.end()+count*length);
		     String rptemp=temp.charAt(0)+value+temp.charAt(temp.length()-1);
		     str = qtemp+rptemp+htemp;
		     count++;
		   } 
	} catch (Exception e) {
		e.printStackTrace();
	}
		   return str.trim();
	}
	private static int codeExplain(String inputString, String outputString) {
	    /*********************************************************/
		//每行之间增加一条through语句
		binary_code.add(TableToBinary.getTHROUGHBytes(false));
		
		/*********************************************************/
		// +++++++++++++++++++++++++++++++++++++++++++++++
		// 下面处理IF条件
		// +++++++++++++++++++++++++++++++++++++++++++++++
		/********************************************************/
		String inoperation=inputString;
		String innote="";
		String outoperation=outputString;
		String outnote="";
		String finalResult = "";
		String[] ifStrings = inputString.split(",|，");
		try {
			for (int k = 0; k < ifStrings.length; k++) {
				String temp[] = ifStrings[k].split("\\|");
				for (int j = 0; j < temp.length; j++) {
					if (j == 0) {
						finalResult = finalResult + temp[j].trim();
					} else {
						finalResult = finalResult + "/" + "|" + "/"
								+ temp[j].trim();
					}
				}
				if (k != ifStrings.length - 1)
					finalResult = finalResult + "/" + "," + "/";
			}
			ifStrings = finalResult.split("/");
			
			/********************************************************/

			for (int j = 0; j < ifStrings.length; j++) {
				
				//System.out.println("每次输入个体"+ifStrings[j]);
				
				if (ifStrings[j].contains("|")) {
					binary_code.add(TableToBinary.getORBytes());
					
				} else if (ifStrings[j].contains(",")||ifStrings[j].contains(",")) {
					binary_code.add(TableToBinary.getANDBytes());
					
				} else if (ifStrings[j].contains("-")) {
					if(TR_Programming_Activity.noteflag!=0&&(!inoperation.equals(""))){
						TableToBinary.searchAddress(ifStrings[j].split("-")[0].trim(), null);
						String noteText=TableToBinary.noteText;
						if(innote.equals("")){
							innote=replace_table(ifStrings[j].split("-")[0].trim(),inoperation, noteText);
						}else{
							innote=replace_table(ifStrings[j].split("-")[0].trim(),innote, noteText);
						}
						
					}else if(TR_Programming_Activity.noteflag==0){
					int[] temp = TableToBinary
							.deviceSearch(ifStrings[j].split("-")[0].trim(),1);
					if(temp[0]==-1&&temp[1]==-1){
						dname=ifStrings[j].split("-")[0].trim();
						return -1;
					}
					if(temp[0]==0xffff&&temp[1]==0xffff){
						dname=ifStrings[j].split("-")[0].trim();
						return 1;
					}
					binary_code.add(TableToBinary.getB_1Bytes(temp[0],
							temp[1] % 32));
					}
				} else {
					//这里加上>或者<的判定，防止>=或<=进入=
					if (ifStrings[j].contains("=")&&(!ifStrings[j].contains(">"))&&(!ifStrings[j].contains("<"))) {
						//System.out.println("进入到!!!!!!!!!!!!!!!!=");
						if(TR_Programming_Activity.noteflag!=0&&(!inoperation.equals(""))){
							TableToBinary.searchAddress(ifStrings[j].split("=")[0].trim(), null);
							String noteText=TableToBinary.noteText;
							if(innote.equals("")){
								innote=replace_table(ifStrings[j].split("=")[0].trim(),inoperation, noteText);
							}else{
								innote=replace_table(ifStrings[j].split("=")[0].trim(),innote, noteText);
							}
							try {
								Integer.valueOf(ifStrings[j].split("=")[1].trim());
							} catch (NumberFormatException e) {
								// TODO: handle exception
								TableToBinary.searchAddress(ifStrings[j].split("=")[1].trim(), null);
								String noteText1=TableToBinary.noteText;
								if(innote.equals("")){
									innote=replace_table(ifStrings[j].split("=")[1].trim(),inoperation, noteText1);
								}else{
									innote=replace_table(ifStrings[j].split("=")[1].trim(),innote, noteText1);
								}
							}
						}else if(TR_Programming_Activity.noteflag==0){
						int[] temp = TableToBinary
								.deviceSearch(ifStrings[j].split("=")[0].trim(),1);
						if(temp[0]==-1&&temp[1]==-1){
							dname=ifStrings[j].split("=")[0].trim();
							return -1;
						}
						if(temp[0]==0xffff&&temp[1]==0xffff){
							dname=ifStrings[j].split("=")[0].trim();
							return 1;
						}
						int temp1=cpExplain("=", ifStrings[j], Constans.Code_CPEQ,
								Constans.Code_CPEQ_IMM);
						if(temp1==-1){
							return -1;
						}
						if(temp1==1){
							return 1;
						}}
					} else if (ifStrings[j].contains("#")) {
						if(TR_Programming_Activity.noteflag!=0&&(!inoperation.equals(""))){
							TableToBinary.searchAddress(ifStrings[j].split("#")[0].trim(), null);
							String noteText=TableToBinary.noteText;
							if(innote.equals("")){
								innote=replace_table(ifStrings[j].split("#")[0].trim(),inoperation, noteText);
							}else{
								innote=replace_table(ifStrings[j].split("#")[0].trim(),innote, noteText);
							}
							try {
								Integer.valueOf(ifStrings[j].split("#")[1].trim());
							} catch (NumberFormatException e) {
								// TODO: handle exception
								TableToBinary.searchAddress(ifStrings[j].split("#")[1].trim(), null);
								String noteText1=TableToBinary.noteText;
								if(innote.equals("")){
									innote=replace_table(ifStrings[j].split("#")[1].trim(),inoperation, noteText1);
								}else{
									innote=replace_table(ifStrings[j].split("#")[1].trim(),innote, noteText1);
								}
							}
						}else if(TR_Programming_Activity.noteflag==0){
						int[] temp = TableToBinary
								.deviceSearch(ifStrings[j].split("#")[0].trim(),1);
						if(temp[0]==-1&&temp[1]==-1){
							dname=ifStrings[j].split("#")[0].trim();
							return -1;
						}
						if(temp[0]==0xffff&&temp[1]==0xffff){
							dname=ifStrings[j].split("#")[0].trim();
							return 1;
						}
						int temp1=cpExplain("#", ifStrings[j], Constans.Code_CPNE,
								Constans.Code_CPNE_IMM);
						if(temp1==-1){
							return -1;
						}
						if(temp1==1){
							return 1;
						}}
					} else if (ifStrings[j].contains(">=")) {
						//System.out.println("进入到!!!!!!!!!!!!!!!!>=");
						if(TR_Programming_Activity.noteflag!=0&&(!inoperation.equals(""))){
							TableToBinary.searchAddress(ifStrings[j].split(">=")[0].trim(), null);
							String noteText=TableToBinary.noteText;
							if(innote.equals("")){
								innote=replace_table(ifStrings[j].split(">=")[0].trim(),inoperation, noteText);
							}else{
								innote=replace_table(ifStrings[j].split(">=")[0].trim(),innote, noteText);
							}
							try {
								Integer.valueOf(ifStrings[j].split(">=")[1].trim());
							} catch (NumberFormatException e) {
								// TODO: handle exception
								TableToBinary.searchAddress(ifStrings[j].split(">=")[1].trim(), null);
								String noteText1=TableToBinary.noteText;
								if(innote.equals("")){
									innote=replace_table(ifStrings[j].split(">=")[1].trim(),inoperation, noteText1);
								}else{
									innote=replace_table(ifStrings[j].split(">=")[1].trim(),innote, noteText1);
								}
							}
						}else if(TR_Programming_Activity.noteflag==0){
						int[] temp = TableToBinary
								.deviceSearch(ifStrings[j].split(">=")[0].trim(),1);
						if(temp[0]==-1&&temp[1]==-1){
							dname=ifStrings[j].split(">=")[0].trim();
							return -1;
						}
						if(temp[0]==0xffff&&temp[1]==0xffff){
							dname=ifStrings[j].split(">=")[0].trim();
							return 1;
						}
						int temp1=cpExplain(">=", ifStrings[j], Constans.Code_CPGE,
								Constans.Code_CPGE_IMM);
						if(temp1==-1){
							return -1;
						}
						if(temp1==1){
							return 1;
						}
						}
					} else if (ifStrings[j].contains("<=")) {
						//System.out.println("进入到!!!!!!!!!!!!!!!!<=");
						if(TR_Programming_Activity.noteflag!=0&&(!inoperation.equals(""))){
							TableToBinary.searchAddress(ifStrings[j].split("<=")[0].trim(), null);
							String noteText=TableToBinary.noteText;
							if(innote.equals("")){
								innote=replace_table(ifStrings[j].split("<=")[0].trim(),inoperation, noteText);
							}else{
								innote=replace_table(ifStrings[j].split("<=")[0].trim(),innote, noteText);
							}
							try {
								Integer.valueOf(ifStrings[j].split("<=")[1].trim());
							} catch (NumberFormatException e) {
								// TODO: handle exception
								TableToBinary.searchAddress(ifStrings[j].split("<=")[1].trim(), null);
								String noteText1=TableToBinary.noteText;
								if(innote.equals("")){
									innote=replace_table(ifStrings[j].split("<=")[1].trim(),inoperation, noteText1);
								}else{
									innote=replace_table(ifStrings[j].split("<=")[1].trim(),innote, noteText1);
								}
							}
						}else if(TR_Programming_Activity.noteflag==0){
						int[] temp = TableToBinary
								.deviceSearch(ifStrings[j].split("<=")[0].trim(),1);
						if(temp[0]==-1&&temp[1]==-1){
							dname=ifStrings[j].split("<=")[0].trim();
							return -1;
						}
						if(temp[0]==0xffff&&temp[1]==0xffff){
							dname=ifStrings[j].split("<=")[0].trim();
							return 1;
						}
						int temp1=cpExplain("<=", ifStrings[j], Constans.Code_CPLE,
								Constans.Code_CPLE_IMM);
						if(temp1==-1){
							return -1;
						}
						if(temp1==1){
							return 1;
						}}
					} else if (ifStrings[j].contains(">")) {
						//System.out.println("进入到!!!!!!!!!!!!!!!!>");
						if(TR_Programming_Activity.noteflag!=0&&(!inoperation.equals(""))){
							TableToBinary.searchAddress(ifStrings[j].split(">")[0].trim(), null);
							String noteText=TableToBinary.noteText;
							if(innote.equals("")){
								innote=replace_table(ifStrings[j].split(">")[0].trim(),inoperation, noteText);
							}else{
								innote=replace_table(ifStrings[j].split(">")[0].trim(),innote, noteText);
							}
							try {
								Integer.valueOf(ifStrings[j].split(">")[1].trim());
							} catch (NumberFormatException e) {
								// TODO: handle exception
								TableToBinary.searchAddress(ifStrings[j].split(">")[1].trim(), null);
								String noteText1=TableToBinary.noteText;
								if(innote.equals("")){
									innote=replace_table(ifStrings[j].split(">")[1].trim(),inoperation, noteText1);
								}else{
									innote=replace_table(ifStrings[j].split(">")[1].trim(),innote, noteText1);
								}
							}
						}else if(TR_Programming_Activity.noteflag==0){
						int[] temp = TableToBinary
								.deviceSearch(ifStrings[j].split(">")[0].trim(),1);
						if(temp[0]==-1&&temp[1]==-1){
							dname=ifStrings[j].split(">")[0].trim();
							return -1;
						}
						if(temp[0]==0xffff&&temp[1]==0xffff){
							dname=ifStrings[j].split(">")[0].trim();
							return 1;
						}
						int temp1=cpExplain(">", ifStrings[j], Constans.Code_CPGT,
								Constans.Code_CPGT_IMM);
						if(temp1==-1){
							return -1;
						}
						if(temp1==1){
							return 1;
						}}
					} else if (ifStrings[j].contains("<")) {
						//System.out.println("进入到!!!!!!!!!!!!!!!!<");
						if(TR_Programming_Activity.noteflag!=0&&(!inoperation.equals(""))){
							TableToBinary.searchAddress(ifStrings[j].split("<")[0].trim(), null);
							String noteText=TableToBinary.noteText;
							if(innote.equals("")){
								innote=replace_table(ifStrings[j].split("<")[0].trim(),inoperation, noteText);
							}else{
								innote=replace_table(ifStrings[j].split("<")[0].trim(),innote, noteText);
							}
							try {
								Integer.valueOf(ifStrings[j].split("<")[1].trim());
							} catch (NumberFormatException e) {
								// TODO: handle exception
								TableToBinary.searchAddress(ifStrings[j].split("<")[1].trim(), null);
								String noteText1=TableToBinary.noteText;
								if(innote.equals("")){
									innote=replace_table(ifStrings[j].split("<")[1].trim(),inoperation, noteText1);
								}else{
									innote=replace_table(ifStrings[j].split("<")[1].trim(),innote, noteText1);
								}
							}
						}else if(TR_Programming_Activity.noteflag==0){
						int[] temp = TableToBinary
								.deviceSearch(ifStrings[j].split("<")[0].trim(),1);
						if(temp[0]==-1&&temp[1]==-1){
							dname=ifStrings[j].split("<")[0].trim();
							return -1;
						}
						if(temp[0]==0xffff&&temp[1]==0xffff){
							dname=ifStrings[j].split("<")[0].trim();
							return 1;
						}
						int temp1=cpExplain("<", ifStrings[j], Constans.Code_CPLT,
								Constans.Code_CPLT_IMM);
						if(temp1==-1){
							return -1;
						}
						if(temp1==1){
							return 1;
						}}
					} else {

						// ON的情况下是，只有设备名
						if(TR_Programming_Activity.noteflag!=0&&(!inoperation.equals(""))){
							TableToBinary.searchAddress(ifStrings[j].trim(), null);
							String noteText=TableToBinary.noteText;
							if(innote.equals("")){
								innote=replace_table(ifStrings[j].trim(),inoperation, noteText);
							}else{
								innote=replace_table(ifStrings[j].trim(),innote, noteText);
							}
							
						}else if(TR_Programming_Activity.noteflag==0){
						int[] temp = TableToBinary
								.deviceSearch(ifStrings[j].trim(),1);
						if(temp[0]==-1&&temp[1]==-1){
							dname=ifStrings[j].trim();
							return -1;
						}
						if(temp[0]==0xffff&&temp[1]==0xffff){
							dname=ifStrings[j].trim();
							return 1;
						}
						binary_code.add(TableToBinary.getA_1Bytes(temp[0],
								temp[1] % 32));
					}
					}

				}

			}
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			// 下面处理OUT条件
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

			String[] outDevices = outputString.split(",|，");// OUT语句中只有","相隔，无逻辑关系
			for (int i = 0; i < outDevices.length; i++) {
				
				//System.out.println("每次输出"+outDevices[i]);
				
				// 每个OUT命令中，可能存在=，无，-，分别代表MOVE指令，OUTON，OUTOFF指令
				if (outDevices[i].contains("=")) {
					// MOVE指令
					try {
						int tempint = Integer.valueOf(outDevices[i].split("=")[1].trim());
						if(TR_Programming_Activity.noteflag!=0&&(!outoperation.equals(""))){
							TableToBinary.searchAddress(outDevices[i].split("=")[0].trim(), null);
							String noteText=TableToBinary.noteText;
							if(outnote.equals("")){
								outnote=replace_table(outDevices[i].split("=")[0].trim(),outoperation, noteText);
							}else{
								outnote=replace_table(outDevices[i].split("=")[0].trim(),outnote, noteText);
							}
							
						}else if(TR_Programming_Activity.noteflag==0){
						int temp=TableToBinary.searchAddress(outDevices[i].split("=")[0].trim(), true);
						binary_code.add(TableToBinary.getMOVE_IMMBytes(tempint,
								TableToBinary.searchAddress(outDevices[i].split("=")[0].trim(), true)));
						if(temp==-1){
							dname=outDevices[i].split("=")[0].trim();
							return -1;
						}
					if(temp==0xffff){
							dname=outDevices[i].split("=")[0].trim();
							return 1;
						}}
					} catch (NumberFormatException e) {
						// TODO: handle exception
						if(TR_Programming_Activity.noteflag!=0&&(!outoperation.equals(""))){
							TableToBinary.searchAddress(outDevices[i].split("=")[0].trim(), null);
							String noteText=TableToBinary.noteText;
							if(outnote.equals("")){
								outnote=replace_table(outDevices[i].split("=")[0].trim(),outoperation, noteText);
							}else{
								outnote=replace_table(outDevices[i].split("=")[0].trim(),outnote, noteText);
							}
							TableToBinary.searchAddress(outDevices[i].split("=")[1].trim(), null);
							String noteText1=TableToBinary.noteText;
							if(outnote.equals("")){
								outnote=replace_table(outDevices[i].split("=")[1].trim(),outoperation, noteText1);
							}else{
								outnote=replace_table(outDevices[i].split("=")[1].trim(),outnote, noteText1);
							}
							
						}else if(TR_Programming_Activity.noteflag==0){
						int temp0=TableToBinary.searchAddress(outDevices[i].split("=")[0].trim(), true);
						int temp1=TableToBinary.searchAddress(outDevices[i].split("=")[1].trim(), true);
						binary_code.add(TableToBinary.getMOVE_Bytes(
								TableToBinary.searchAddress(outDevices[i].split("=")[1].trim(),true), 
								TableToBinary.searchAddress(outDevices[i].split("=")[0].trim(),true)));
						  if(temp0==-1){
								dname=outDevices[i].split("=")[0].trim();
								return -1;
							} 
						  if(temp1==-1){
								dname=outDevices[i].split("=")[1].trim();
								return -1;
							} 
					  if(temp0==0xffff){
							dname=outDevices[i].split("=")[0].trim();
							return 1;
						} 
					  if(temp1==0xffff){
							dname=outDevices[i].split("=")[1].trim();
							return 1;
						} 
					}
				}

				} else if (outDevices[i].contains("-")) {
					// OUTOFF指令
					if(TR_Programming_Activity.noteflag!=0&&(!outoperation.equals(""))){
						TableToBinary.searchAddress(outDevices[i].split("-")[0].trim(), null);
						String noteText=TableToBinary.noteText;
						if(outnote.equals("")){
							outnote=replace_table(outDevices[i].split("-")[0].trim(),outoperation, noteText);
						}else{
							outnote=replace_table(outDevices[i].split("-")[0].trim(),outnote, noteText);
						}
						
					}else if(TR_Programming_Activity.noteflag==0){
					int[] temp = TableToBinary
							.deviceSearch(outDevices[i].split("-")[0].trim(),0);
					binary_code.add(TableToBinary.getOUT_OFFBytes(temp[0],
							temp[1] % 32));
					 if(temp[0]==-1&&temp[1]==-1){
							dname=outDevices[i].split("-")[0].trim();
							return -1;
						}
				 if(temp[0]==0xffff&&temp[1]==0xffff){
						dname=outDevices[i].split("-")[0].trim();
						return 1;
					} }
				} else {
					// OUTON指令
					if(TR_Programming_Activity.noteflag!=0&&(!outoperation.equals(""))){
						TableToBinary.searchAddress(outDevices[i].trim(), null);
						String noteText=TableToBinary.noteText;
						if(outnote.equals("")){
							outnote=replace_table(outDevices[i].trim(),outoperation, noteText);
						}else{
							outnote=replace_table(outDevices[i].trim(),outnote, noteText);
						}
						
					}else if(TR_Programming_Activity.noteflag==0){
					int[] temp = TableToBinary
							.deviceSearch(outDevices[i].trim(),0);
					binary_code.add(TableToBinary.getOUT_ONBytes(temp[0],
							temp[1] % 32));
					 if(temp[0]==-1&&temp[1]==-1){
							dname=outDevices[i].trim();
							return -1;
						}
				 if(temp[0]==0xffff&&temp[1]==0xffff){
						dname=outDevices[i].trim();
						return 1;
					} 
				}
				}
			}
			String allnote=innote+"\\"+outnote;
			tnoteList.add(allnote);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("youcuowu");
		}
		return 0;
	}
	/**
	 * 解释不同赋值CP的一个中间函数，主要是为了代码美观  getINC
	 * 
	 * @param expressString
	 *            >,<,>=,<=,=,!=
	 * @param formula
	 *            CP的计算式
	 * @param codeOffset
	 *            偏移地址指令
	 * @param codeIMM
	 *            立即数指令
	 */
	private static int cpExplain(String expressString, String formula,
			int codeOffset, int codeIMM) {

		// 临时数组引用地址变量，阅读方便，无实际意义
		int tempint = TableToBinary.searchAddress(
				formula.split(expressString)[0].trim(), true);
		if(tempint==-1){
			dname=formula.split(expressString)[0].trim();
			return -1;
		}
		if(tempint==0xffff){
			dname=formula.split(expressString)[0].trim();
			return 1;
		}
		try {
			int immdata=Integer.valueOf(formula.split(expressString)[1].trim());
			binary_code.add(TableToBinary.getCP_Bytes(codeIMM, tempint, immdata));
			
		} catch (NumberFormatException e) {
			// TODO: handle exception
			int tempint1 = TableToBinary.searchAddress(
					formula.split(expressString)[1].trim(), true);
			if(tempint1==-1){
				dname=formula.split(expressString)[1].trim();
				return -1;
			}
			if(tempint1==0xffff){
				dname=formula.split(expressString)[1].trim();
				return 1;
			}
			binary_code.add(TableToBinary.getCP_Bytes(
					codeOffset,
					tempint,
					TableToBinary.searchAddress(
							formula.split(expressString)[1].trim(), true)));
		}
		return 0;
	}
}
