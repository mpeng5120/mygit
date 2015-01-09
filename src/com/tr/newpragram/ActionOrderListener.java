package com.tr.newpragram;

import java.util.ArrayList;
import java.util.HashMap;

import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.explain.TableToBinary;
import com.tr.R;
import com.tr.programming.Config;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ActionOrderListener implements OnClickListener {
    private View view;
    private int position;
    private String order_now;
    private ArrayList<HashMap<String, Object>> appList;
    private com.tr.newpragram.Fragments_Action.MyAdapter myAdaptertest;
    private String PO="";
    private String axisString = "";
    
    private String axlenote="";
    
    private String morderPreviewString="";
    public static ArrayList<HashMap<String, Object>> posList = new ArrayList<HashMap<String, Object>>();// 表格要绑定的list
    
    

    


    public ActionOrderListener(View orderview, int pos, String order_array,ArrayList<HashMap<String, Object>> NcEditList) {
    	// TODO 自动生成的构造函数存根
    	view = orderview;
    	position = pos;
    	order_now = order_array;
    	appList=NcEditList;
        }

    
    public ActionOrderListener(View orderview, int pos, String order_array,ArrayList<HashMap<String, Object>> NcEditList,com.tr.newpragram.Fragments_Action.MyAdapter myAdapter) {
	// TODO 自动生成的构造函数存根
	view = orderview;
	position = pos;
	order_now = order_array;
	appList=NcEditList;
	myAdaptertest=myAdapter;	
    }



/*    public DetailedOrderListener(View orderview, int pos,
			String order_array, ArrayList<HashMap<String, Object>> NcEditList,
			com.tr.programming.Fragments_NCedit1.MyAdapter myAdapter) {
		// TODO Auto-generated constructor stub
    	view = orderview;
    	position = pos;
    	order_now = order_array;
    	appList=NcEditList;
    	myAdaptertest=myAdapter;
	}
*/
    // 按SPINNER的选中的ITEM去辨别保存在哪个LIST中
    public void  switchItemId(int id)
    {
 	  String STR; 
 	 Spinner  TV =  (Spinner) view.findViewById(R.id.CerrectInfo);
 	  ToggleButton armCheck =  (ToggleButton) view.findViewById(R.id.sfp_switch);
 	  
 		  
 	   EditText speed=(EditText)view.findViewById(R.id.speed);
 	  EditText aspeed=(EditText)view.findViewById(R.id.aspeed);
	  EditText dspeed=(EditText)view.findViewById(R.id.dspeed);
 	  
 	  EditText  xEdTxt =  (EditText) view.findViewById(R.id.xEditText);
	  EditText  yEdTxt =  (EditText) view.findViewById(R.id.yEditText);
	  EditText  zEdTxt =  (EditText) view.findViewById(R.id.zEditText);
	  EditText  hEdTxt =  (EditText) view.findViewById(R.id.hEditText);
	  EditText  lEdTxt =  (EditText) view.findViewById(R.id.lEditText);
	  
	  CheckBox checkboxx=(CheckBox)view.findViewById(R.id.checkBox1);
	  CheckBox checkboxy=(CheckBox)view.findViewById(R.id.checkBox2);
	  CheckBox checkboxh=(CheckBox)view.findViewById(R.id.checkBox4);
	  CheckBox checkboxz=(CheckBox)view.findViewById(R.id.checkBox3);
	  CheckBox checkboxl=(CheckBox)view.findViewById(R.id.checkBox5);
	  
	  String xValue = xEdTxt.getText().toString();
	  String yValue = yEdTxt.getText().toString();
	  String zValue = zEdTxt.getText().toString();
	  String hValue = hEdTxt.getText().toString();
	  String lValue = lEdTxt.getText().toString();
 	 
 	   switch(id)
 	   {
 	   case 0:
 			  xEdTxt.getEditableText();
 		   // 标准位置		
 			  
 			  if(Config.list_pname.isEmpty())
 			  { 
 				 if(TV.getSelectedItem()==null)
 					 return;
				  Config.list_pname.add(TV.getSelectedItem().toString().trim());
					 
 			  }else{
 				  if(!Config.list_pname.contains(TV.getSelectedItem().toString().trim())){
 					 Config.list_pname.add(TV.getSelectedItem().toString().trim());
 				  }
 			  }
 		   break;
 	   case 1:
 		   Log.d("mpeng","config spname"+Config.list_spname.toString());
 		  if(Config.list_spname.isEmpty())
			  { 
				  Config.list_spname.add(TV.getSelectedItem().toString().trim());
			  }else{
				  if(!Config.list_spname.contains(TV.getSelectedItem().toString().trim())){
					 Config.list_spname.add(TV.getSelectedItem().toString().trim());
					 Log.d("mpeng","config 2222 "+Config.list_spname.toString());
				  }
			  }
 		  break;
 	   case 2:
 		  Log.d("mpeng","config list_fpname "+Config.list_fpname.toString());
 		  if(Config.list_fpname.isEmpty())
			  { 
				  Config.list_fpname.add(TV.getSelectedItem().toString().trim());
			  }else{
				  if(!Config.list_fpname.contains(TV.getSelectedItem().toString().trim())){
					 Config.list_fpname.add(TV.getSelectedItem().toString().trim());
				  }
			  }
 		  break;

 	   }
 	   STR  =   TV.getSelectedItem().toString();
 	
    }

	//move命令界面的操作
    public void ncOrderMOVE(View view) {
	try{
		Log.d("mpeng","ncOrderMOVE!!");
    	CheckBox checkbox1=(CheckBox)view.findViewById(R.id.checkBox1);
    	CheckBox checkbox2=(CheckBox)view.findViewById(R.id.checkBox2);
    	CheckBox checkbox3=(CheckBox)view.findViewById(R.id.checkBox3);
    	CheckBox checkbox4=(CheckBox)view.findViewById(R.id.checkBox4);
    	CheckBox checkbox5=(CheckBox)view.findViewById(R.id.checkBox5);
    	Spinner posSpinner = (Spinner)view.findViewById(R.id.SelectSpinner);
    	Log.d("mpeng","  item id :"+posSpinner.getSelectedItemId());
    	switchItemId((int) posSpinner.getSelectedItemId());
    	if(checkbox1==null){return;}
    	if(checkbox2==null){return;}
    	if(checkbox3==null){return;}
    	if(checkbox4==null){return;}
    	if(checkbox5==null){return;}
    	if (checkbox1.isChecked()) {
    		 if(axisString.equals("")){
 			    axisString+="A1";
 			    axlenote=axlenote+"X、";
 			}
 			else {
 			    axisString+=" A1";
 			    axlenote=axlenote+" X、";
 			}			
 		  }
     	if (checkbox2.isChecked()) {
  		   if(axisString.equals("")){
  			    axisString+="A2";
  			   axlenote=axlenote+"Y、";
  			}
  			else {
  			    axisString+=" A2";
  			   axlenote=axlenote+" Y、";
  			}			
  		  }
     	if (checkbox4.isChecked()) {
   		   if(axisString.equals("")){
   			    axisString+="A3";
   			  axlenote=axlenote+"H、";
   			}
   			else {
   			    axisString+=" A3";
   			  axlenote=axlenote+" H、";
   			}			
   		  }
     	if (checkbox3.isChecked()) {
  		   if(axisString.equals("")){
  			    axisString+="A4";
  			   axlenote=axlenote+"Z、";
  			}
  			else {
  			    axisString+=" A4";
  			   axlenote=axlenote+" Z、";
  			}			
  		  }
     	
     	if (checkbox5.isChecked()) {
  		   if(axisString.equals("")){
  			    axisString+="A5";
  			   axlenote=axlenote+"L、";
  			}
  			else {
  			    axisString+=" A5";
  			   axlenote=axlenote+" L、";
  			}			
  		  }
    	if(!axisString.equals("")){
    		axisString=axisString+" ";
    	}
    	if(axlenote.equals("")){
    		axlenote=axlenote+"  ";
    	}
    	Spinner  TV =  (Spinner) view.findViewById(R.id.CerrectInfo);
    	if(TV==null||TV.getSelectedItem()==null){return;}
		PO=TV.getSelectedItem().toString();
    }catch(Exception e){
		e.printStackTrace();
	}
    }
    
    
    public void ncOrderIF(View view) {
    	try{
    		CheckBox zscheckbox1=(CheckBox)view.findViewById(R.id.checkBox1);
        	CheckBox zscheckbox2=(CheckBox)view.findViewById(R.id.checkBox2);
        	if(zscheckbox1==null){return;}
        	if(zscheckbox2==null){return;}
        	ArrayList<HashMap<String, Object>> inputList = ArrayListBound.getDeviceActualInputListData();
    		String symbolname="";
        	if(zscheckbox1.isChecked()){
        		symbolname=inputList.get(16).get("symbolNameEditText").toString().trim();
        		axlenote="如果"+zscheckbox1.getText().toString();
            	morderPreviewString=symbolname+" ON";
        	}
        	if(zscheckbox2.isChecked()){
        		symbolname=inputList.get(19).get("symbolNameEditText").toString().trim();
        		axlenote="如果"+zscheckbox2.getText().toString();
        		morderPreviewString=symbolname+" ON";
        	}
    }catch(Exception e){
		e.printStackTrace();
	}
    }

    public void ncOrderOUT(View view) {
        try{
        	CheckBox zscheckbox1=(CheckBox)view.findViewById(R.id.checkBox1);
        	CheckBox zscheckbox2=(CheckBox)view.findViewById(R.id.checkBox2);
        	if(zscheckbox1==null){return;}
        	if(zscheckbox2==null){return;}
    		ArrayList<HashMap<String, Object>> outputList = ArrayListBound.getDeviceActualOutputListData();
    		String symbolname32=outputList.get(32).get("symbolNameEditText").toString().trim();
    		String symbolname33=outputList.get(33).get("symbolNameEditText").toString().trim();
        	if(zscheckbox1.isChecked()){
        		axlenote="输出"+zscheckbox1.getText().toString();
            	morderPreviewString=symbolname32+" ON,"+symbolname33+" OFF";
        	}
        	if(zscheckbox2.isChecked()){
        		axlenote="输出"+zscheckbox2.getText().toString();
        		morderPreviewString=symbolname32+" OFF,"+symbolname33+" ON";
        	}
        	
        }catch(Exception e){
    		e.printStackTrace();
    	}
        	
        }
    
    public void ncOrderOUTP(View view) {
        try{
        	CheckBox zjcheckbox1=(CheckBox)view.findViewById(R.id.checkBox1);
        	CheckBox zjcheckbox2=(CheckBox)view.findViewById(R.id.checkBox2);
        	CheckBox zjcheckbox3=(CheckBox)view.findViewById(R.id.checkBox3);
        	CheckBox zjcheckbox4=(CheckBox)view.findViewById(R.id.checkBox4);
        	CheckBox zjcheckbox5=(CheckBox)view.findViewById(R.id.checkBox5);
        	RadioButton radioON_zj1=(RadioButton) view.findViewById(R.id.radio_on1);
        	RadioButton radioOFF_zj1=(RadioButton) view.findViewById(R.id.radio_off1);
        	RadioButton radioON_zj2=(RadioButton) view.findViewById(R.id.radio_on2);
        	RadioButton radioOFF_zj2=(RadioButton) view.findViewById(R.id.radio_off2);
        	RadioButton radioON_zj3=(RadioButton) view.findViewById(R.id.radio_on3);
        	RadioButton radioOFF_zj3=(RadioButton) view.findViewById(R.id.radio_off3);
        	RadioButton radioON_zj4=(RadioButton) view.findViewById(R.id.radio_on4);
        	RadioButton radioOFF_zj4=(RadioButton) view.findViewById(R.id.radio_off4);
        	RadioButton radioON_zj5=(RadioButton) view.findViewById(R.id.radio_on5);
        	RadioButton radioOFF_zj5=(RadioButton) view.findViewById(R.id.radio_off5);
        	if(zjcheckbox1==null){return;}
        	if(zjcheckbox2==null){return;}
        	if(zjcheckbox3==null){return;}
        	if(zjcheckbox4==null){return;}
        	if(zjcheckbox5==null){return;}
    		ArrayList<HashMap<String, Object>> outputList = ArrayListBound.getDeviceActualOutputListData();
    		String symbolname="";
        	if(zjcheckbox1.isChecked()){
        		symbolname=outputList.get(34).get("symbolNameEditText").toString().trim();
        		if(radioON_zj1.isChecked()){
        			axlenote+=zjcheckbox1.getText()+" ON,";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+=zjcheckbox1.getText()+" OFF,";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox2.isChecked()){
        		symbolname=outputList.get(35).get("symbolNameEditText").toString().trim();
        		if(radioON_zj2.isChecked()){
        			axlenote+=zjcheckbox2.getText()+" ON,";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+=zjcheckbox2.getText()+" OFF,";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox3.isChecked()){
        		symbolname=outputList.get(38).get("symbolNameEditText").toString().trim();
        		if(radioON_zj3.isChecked()){
        			axlenote+=zjcheckbox3.getText()+" ON,";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+=zjcheckbox3.getText()+" OFF,";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox4.isChecked()){
        		symbolname=outputList.get(36).get("symbolNameEditText").toString().trim();
        		if(radioON_zj4.isChecked()){
        			axlenote+=zjcheckbox4.getText()+" ON,";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+=zjcheckbox4.getText()+" OFF,";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox5.isChecked()){
        		symbolname=outputList.get(37).get("symbolNameEditText").toString().trim();
        		if(radioON_zj5.isChecked()){
        			axlenote+=zjcheckbox5.getText()+" ON,";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+=zjcheckbox5.getText()+" OFF,";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        }catch(Exception e){
    		e.printStackTrace();
    	}
        	
        }

    public void ncOrderPARALLEL(View view) {
    try{

    	CheckBox zjcheckbox1=(CheckBox)view.findViewById(R.id.checkBox1);
    	CheckBox zjcheckbox2=(CheckBox)view.findViewById(R.id.checkBox2);
    	CheckBox zjcheckbox3=(CheckBox)view.findViewById(R.id.checkBox3);
    	CheckBox zjcheckbox4=(CheckBox)view.findViewById(R.id.checkBox4);
    	RadioButton radioON_zj1=(RadioButton) view.findViewById(R.id.radio_on1);
    	RadioButton radioOFF_zj1=(RadioButton) view.findViewById(R.id.radio_off1);
    	RadioButton radioON_zj2=(RadioButton) view.findViewById(R.id.radio_on2);
    	RadioButton radioOFF_zj2=(RadioButton) view.findViewById(R.id.radio_off2);
    	RadioButton radioON_zj3=(RadioButton) view.findViewById(R.id.radio_on3);
    	RadioButton radioOFF_zj3=(RadioButton) view.findViewById(R.id.radio_off3);
    	RadioButton radioON_zj4=(RadioButton) view.findViewById(R.id.radio_on4);
    	RadioButton radioOFF_zj4=(RadioButton) view.findViewById(R.id.radio_off4);
    	if(zjcheckbox1==null){return;}
    	if(zjcheckbox2==null){return;}
    	if(zjcheckbox3==null){return;}
    	if(zjcheckbox4==null){return;}
		ArrayList<HashMap<String, Object>> outputList = ArrayListBound.getDeviceActualOutputListData();
		String symbolname="";
    	if(zjcheckbox1.isChecked()){
    		symbolname=outputList.get(16).get("symbolNameEditText").toString().trim();
    		if(radioON_zj1.isChecked()){
    			axlenote+=zjcheckbox1.getText()+" ON,";
            	morderPreviewString+=symbolname+" ON,";
    		}else{
    			axlenote+=zjcheckbox1.getText()+" OFF,";
            	morderPreviewString+=symbolname+" OFF,";
    		}
    	}
    	if(zjcheckbox2.isChecked()){
    		symbolname=outputList.get(17).get("symbolNameEditText").toString().trim();
    		if(radioON_zj2.isChecked()){
    			axlenote+=zjcheckbox2.getText()+" ON,";
            	morderPreviewString+=symbolname+" ON,";
    		}else{
    			axlenote+=zjcheckbox2.getText()+" OFF,";
            	morderPreviewString+=symbolname+" OFF,";
    		}
    	}
    	if(zjcheckbox3.isChecked()){
    		symbolname=outputList.get(18).get("symbolNameEditText").toString().trim();
    		if(radioON_zj3.isChecked()){
    			axlenote+=zjcheckbox3.getText()+" ON,";
            	morderPreviewString+=symbolname+" ON,";
    		}else{
    			axlenote+=zjcheckbox3.getText()+" OFF,";
            	morderPreviewString+=symbolname+" OFF,";
    		}
    	}
    	if(zjcheckbox4.isChecked()){
    		symbolname=outputList.get(37).get("symbolNameEditText").toString().trim();
    		if(radioON_zj4.isChecked()){
    			axlenote+=zjcheckbox4.getText()+" ON,";
            	morderPreviewString+=symbolname+" ON,";
    		}else{
    			axlenote+=zjcheckbox4.getText()+" OFF,";
            	morderPreviewString+=symbolname+" OFF,";
    		}
    	}
    }catch(Exception e){
		e.printStackTrace();
	}
    }
    
    public void ncOrderSEQUENTIAL(View view) {
    	try{
    		CheckBox zjcheckbox1=(CheckBox)view.findViewById(R.id.checkBox1);
        	CheckBox zjcheckbox2=(CheckBox)view.findViewById(R.id.checkBox2);
        	CheckBox zjcheckbox3=(CheckBox)view.findViewById(R.id.checkBox3);
        	CheckBox zjcheckbox4=(CheckBox)view.findViewById(R.id.checkBox4);
        	CheckBox zjcheckbox5=(CheckBox)view.findViewById(R.id.checkBox5);
        	CheckBox zjcheckbox6=(CheckBox)view.findViewById(R.id.checkBox6);
        	CheckBox zjcheckbox7=(CheckBox)view.findViewById(R.id.checkBox7);
        	CheckBox zjcheckbox8=(CheckBox)view.findViewById(R.id.checkBox8);
        	CheckBox zjcheckbox9=(CheckBox)view.findViewById(R.id.checkBox9);
        	CheckBox zjcheckbox10=(CheckBox)view.findViewById(R.id.checkBox10);
        	RadioButton radioON_zj1=(RadioButton) view.findViewById(R.id.radio_on1);
        	RadioButton radioOFF_zj1=(RadioButton) view.findViewById(R.id.radio_off1);
        	RadioButton radioON_zj2=(RadioButton) view.findViewById(R.id.radio_on2);
        	RadioButton radioOFF_zj2=(RadioButton) view.findViewById(R.id.radio_off2);
        	RadioButton radioON_zj3=(RadioButton) view.findViewById(R.id.radio_on3);
        	RadioButton radioOFF_zj3=(RadioButton) view.findViewById(R.id.radio_off3);
        	RadioButton radioON_zj4=(RadioButton) view.findViewById(R.id.radio_on4);
        	RadioButton radioOFF_zj4=(RadioButton) view.findViewById(R.id.radio_off4);
        	RadioButton radioON_zj5=(RadioButton) view.findViewById(R.id.radio_on5);
        	RadioButton radioOFF_zj5=(RadioButton) view.findViewById(R.id.radio_off5);
        	RadioButton radioON_zj6=(RadioButton) view.findViewById(R.id.radio_on6);
        	RadioButton radioOFF_zj6=(RadioButton) view.findViewById(R.id.radio_off6);
        	RadioButton radioON_zj7=(RadioButton) view.findViewById(R.id.radio_on7);
        	RadioButton radioOFF_zj7=(RadioButton) view.findViewById(R.id.radio_off7);
        	RadioButton radioON_zj8=(RadioButton) view.findViewById(R.id.radio_on8);
        	RadioButton radioOFF_zj8=(RadioButton) view.findViewById(R.id.radio_off8);
        	RadioButton radioON_zj9=(RadioButton) view.findViewById(R.id.radio_on9);
        	RadioButton radioOFF_zj9=(RadioButton) view.findViewById(R.id.radio_off9);
        	RadioButton radioON_zj10=(RadioButton) view.findViewById(R.id.radio_on10);
        	RadioButton radioOFF_zj10=(RadioButton) view.findViewById(R.id.radio_off10);
        	if(zjcheckbox1==null){return;}
        	if(zjcheckbox2==null){return;}
        	if(zjcheckbox3==null){return;}
        	if(zjcheckbox4==null){return;}
        	if(zjcheckbox5==null){return;}
        	if(zjcheckbox6==null){return;}
        	if(zjcheckbox7==null){return;}
        	if(zjcheckbox8==null){return;}
        	if(zjcheckbox9==null){return;}
        	if(zjcheckbox10==null){return;}
        	ArrayList<HashMap<String, Object>> inputList = ArrayListBound.getDeviceMiddleOutputListData();
    		String symbolname="";
        	if(zjcheckbox1.isChecked()){
        		symbolname=inputList.get(96).get("symbolNameEditText").toString().trim();
        		if(radioON_zj1.isChecked()){
        			axlenote+="开始检测"+zjcheckbox1.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox1.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox2.isChecked()){
        		symbolname=inputList.get(97).get("symbolNameEditText").toString().trim();
        		if(radioON_zj2.isChecked()){
        			axlenote+="开始检测"+zjcheckbox2.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox2.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox3.isChecked()){
        		symbolname=inputList.get(98).get("symbolNameEditText").toString().trim();
        		if(radioON_zj3.isChecked()){
        			axlenote+="开始检测"+zjcheckbox3.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox3.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox4.isChecked()){
        		symbolname=inputList.get(99).get("symbolNameEditText").toString().trim();
        		if(radioON_zj4.isChecked()){
        			axlenote+="开始检测"+zjcheckbox4.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox4.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox5.isChecked()){
        		symbolname=inputList.get(100).get("symbolNameEditText").toString().trim();
        		if(radioON_zj5.isChecked()){
        			axlenote+="开始检测"+zjcheckbox5.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox5.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox6.isChecked()){
        		symbolname=inputList.get(101).get("symbolNameEditText").toString().trim();
        		if(radioON_zj6.isChecked()){
        			axlenote+="开始检测"+zjcheckbox6.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox6.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox7.isChecked()){
        		symbolname=inputList.get(102).get("symbolNameEditText").toString().trim();
        		if(radioON_zj7.isChecked()){
        			axlenote+="开始检测"+zjcheckbox7.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox7.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox8.isChecked()){
        		symbolname=inputList.get(103).get("symbolNameEditText").toString().trim();
        		if(radioON_zj8.isChecked()){
        			axlenote+="开始检测"+zjcheckbox8.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox8.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox9.isChecked()){
        		symbolname=inputList.get(104).get("symbolNameEditText").toString().trim();
        		if(radioON_zj9.isChecked()){
        			axlenote+="开始检测"+zjcheckbox9.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox9.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
        	if(zjcheckbox10.isChecked()){
        		symbolname=inputList.get(105).get("symbolNameEditText").toString().trim();
        		if(radioON_zj10.isChecked()){
        			axlenote+="开始检测"+zjcheckbox10.getText()+",";
                	morderPreviewString+=symbolname+" ON,";
        		}else{
        			axlenote+="停止检测"+zjcheckbox10.getText()+",";
                	morderPreviewString+=symbolname+" OFF,";
        		}
        	}
    	}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    public void ncOrderWAIT(View view) {
try{
    	EditText editText_WAIT=(EditText) view.findViewById(R.id.editText_WAIT);
        if(editText_WAIT==null){return;}
    	Spinner  TV =  (Spinner) view.findViewById(R.id.CerrectInfo);
    	if(TV==null||(TV.getSelectedItem()==null)){return;}
		PO=TV.getSelectedItem().toString().trim();
		
		 if(Config.list_timername.isEmpty())
		 {
			   Config.list_timername.add(PO);
		 }else{
			 if(!Config.list_timername.contains(PO)){
				 Config.list_timername.add(PO);
			 }
		 }
    }catch(Exception e){
		e.printStackTrace();
	}
    }
    
    @Override
    public void onClick(DialogInterface dialog, int which) {
	// TODO 自动生成的方法存根
    	try{
    		Log.d("mpeng","Constans.NCorders_enum.getOrder(order_now) ");
    		Fragments_Action.changeModifyStatus(true);
	switch (Constans.NCorders_enum.getOrder(order_now)) {	
	case OUTP:
		ncOrderOUTP(view);
		HashMap<String, Object> mapOUTP = new HashMap<String, Object>();
		mapOUTP = (HashMap<String, Object>) appList.get(position).clone();
		mapOUTP.put("orderSpinner","OUT");	
		mapOUTP.put("operatText",morderPreviewString.subSequence(0, morderPreviewString.length()-1));	
		mapOUTP.put("noteEditText","输出"+axlenote.subSequence(0, axlenote.length()-1));
		appList.set(position, mapOUTP);
		myAdaptertest.notifyDataSetChanged();
		break;
	case MOVE:
	case MOVEP:
		ncOrderMOVE(view);
		
//		//修改容器内容
		HashMap<String, Object> mapMOVE = new HashMap<String, Object>();
		Log.d("mpeng"," the app list is "+position);
		mapMOVE = (HashMap<String, Object>) appList.get(position).clone();
				
			mapMOVE.put("orderSpinner",order_now);
			mapMOVE.put("operatText",axisString+PO);	
		mapMOVE.put("noteEditText",axlenote.substring(0, axlenote.length()-1)+"移动到"+PO);
		appList.set(position, mapMOVE);
		myAdaptertest.notifyDataSetChanged();
		Log.d("mpeng","the nc list "+ArrayListBound.getNCeditList3Data().toString());
	    break;
	    
	case IF:
		ncOrderIF(view);
		HashMap<String, Object> mapIF = new HashMap<String, Object>();
		mapIF = (HashMap<String, Object>) appList.get(position).clone();
		mapIF.put("orderSpinner","IF");	
		mapIF.put("operatText",morderPreviewString);	
		mapIF.put("noteEditText",axlenote);
		appList.set(position, mapIF);
		//更新显示
		myAdaptertest.notifyDataSetChanged();
	    break;
	case OUT:
		ncOrderOUT(view);
		HashMap<String, Object> mapOUT = new HashMap<String, Object>();
		mapOUT = (HashMap<String, Object>) appList.get(position).clone();
		mapOUT.put("orderSpinner","OUT");	
		mapOUT.put("operatText",morderPreviewString);	
		mapOUT.put("noteEditText",axlenote);
		appList.set(position, mapOUT);
		myAdaptertest.notifyDataSetChanged();
	    break;    
	case PARALLEL:
		ncOrderPARALLEL(view);
		HashMap<String, Object> mapPARALLEL = new HashMap<String, Object>();
		mapPARALLEL = (HashMap<String, Object>) appList.get(position).clone();
		mapPARALLEL.put("orderSpinner","OUT");	
		mapPARALLEL.put("operatText",morderPreviewString.subSequence(0, morderPreviewString.length()-1));	
		mapPARALLEL.put("noteEditText","输出"+axlenote.subSequence(0, axlenote.length()-1));
		appList.set(position, mapPARALLEL);
		myAdaptertest.notifyDataSetChanged();
	    break;
	case SEQUENTIAL:
		ncOrderSEQUENTIAL(view);
		HashMap<String, Object> mapSEQUENTIAL = new HashMap<String, Object>();
		mapSEQUENTIAL = (HashMap<String, Object>) appList.get(position).clone();
		mapSEQUENTIAL.put("orderSpinner","OUT");	
		mapSEQUENTIAL.put("operatText",morderPreviewString.subSequence(0, morderPreviewString.length()-1));	
		mapSEQUENTIAL.put("noteEditText",axlenote.subSequence(0, axlenote.length()-1));
		appList.set(position, mapSEQUENTIAL);
		myAdaptertest.notifyDataSetChanged();
	    break;
	case WAIT:
		ncOrderWAIT(view);
		HashMap<String, Object> mapWAIT = new HashMap<String, Object>();
		mapWAIT = (HashMap<String, Object>) appList.get(position).clone();
		mapWAIT.put("orderSpinner","TWAIT");	
		mapWAIT.put("operatText",PO);	
		mapWAIT.put("noteEditText","等待"+PO);
		appList.set(position, mapWAIT);
		myAdaptertest.notifyDataSetChanged();
		break;	
	default:
		break;
	}
    }catch(Exception e){
		e.printStackTrace();
	}
    	
    }
    
}
