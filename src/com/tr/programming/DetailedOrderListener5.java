package com.tr.programming;

import java.util.ArrayList;
import java.util.HashMap;
import com.dbutils.Constans;
import com.tr.R;
import com.tr.programming.Fragments_NCedit5.MyAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedOrderListener5 implements OnClickListener {
    private View view;
    private int position;
    private String order_now;
    private ArrayList<HashMap<String, Object>> appList;
    private MyAdapter myAdaptertest;
    private String mnoteString;

    //MOVE,MOVEP界面所需参数
    private String P="";
    private String SP="";
    private String FP="";
    private String PO="";
    private String axisString = "";
    
    //IF界面所需参数
    private String inPutSymbolName="";
    private String inPutAnswer="";
    private String morderPreviewString="";
    //OUT界面所需参数
    private String outPutSymbolName="";
    private String outPutAnswer="";
    
    //SEQUENTIAL界面所需参数
    private String order_now_SEQ="";
    private String operate_SEQ="";
    private String order_now_PAR="";
    private String operate_PAR="";
    

    


    

    
    public DetailedOrderListener5(View orderview, int pos, String order_array,ArrayList<HashMap<String, Object>> NcEditList,MyAdapter myAdapter) {
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


	//move命令界面的操作
    public void ncOrderMOVE(View view) {
	try{
	// 递归查询页面上所有的控件，判断类型，获取用户填入的信息
/*    	System.out.println("((ViewGroup) view).getChildCount()="+((ViewGroup) view).getChildCount());
	for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
		System.out.println("axisString"+i+"="+axisString);
	    View v = ((ViewGroup) view).getChildAt(i);
	    if (v instanceof CheckBox) {
		if (((CheckBox) v).isChecked()) {
		    String name = ((CheckBox) v).getText().toString();
		    if(name.equals("走行"))
		    {
			if(axisString.equals("")){
			    axisString+="A1";
			}
			else {
			    axisString+=",A1";
			}			
		    }
		    else if (name.equals("制品前后")) {
			if(axisString.equals("")){
			    axisString+="A2";
			}
			else {
			    axisString+=",A2";
			}
		    }
		    else if (name.equals("料道前后")) {
			if(axisString.equals("")){
			    axisString+="A3";
			}
			else {
			    axisString+=",A3";
			}
		    }
		    else if (name.equals("制品上下")) {
			if(axisString.equals("")){
			    axisString+="A4";			}
			else {
			    axisString+=",A4";
			}
		    }
		    else if (name.equals("料道上下")) {
			if(axisString.equals("")){
			    axisString+="A5";
			}
			else {
			    axisString+=",A5";
			}
	      }		    	    
		 }
	    }
	    else if (v instanceof TextView||v instanceof Space) {
		//我是占位
	    }
	    //标准点位置
	    else if(v.getId()==R.id.spinnerP_MOVE){
	    	System.out.println("spinnerP_MOVE");
	    	if (((Spinner) v).getSelectedItem()==null) {
	    		P="";			
			}else {
				P = ((Spinner) v).getSelectedItem().toString();
				PO=P;
			}
		    
		     
	    }
	    //标准装箱位置
	    else if(v.getId()==R.id.spinnerSP_MOVE){
	    	System.out.println("spinnerSP_MOVE");
	    	if (((Spinner) v).getSelectedItem()==null) {
	    		SP="";			
			}else {
				SP = ((Spinner) v).getSelectedItem().toString();
				PO=SP;
			}
	    }
	    //自由装箱位置
	    else if(v.getId()==R.id.spinnerFP_MOVE){
	    	System.out.println("spinnerFP");
	    	if (((Spinner) v).getSelectedItem()==null) {
	    		FP="";			
			}else {
				FP = ((Spinner) v).getSelectedItem().toString();
				PO=FP;
			}
	    }
	   else {
		ncOrderMOVE(v);
	    }
	}*/
    	CheckBox checkbox1=(CheckBox)view.findViewById(R.id.checkBox1);
    	CheckBox checkbox2=(CheckBox)view.findViewById(R.id.checkBox2);
    	CheckBox checkbox3=(CheckBox)view.findViewById(R.id.checkBox3);
    	CheckBox checkbox4=(CheckBox)view.findViewById(R.id.checkBox4);
    	CheckBox checkbox5=(CheckBox)view.findViewById(R.id.checkBox5);
    	if(checkbox1==null){return;}
    	if(checkbox2==null){return;}
    	if(checkbox3==null){return;}
    	if(checkbox4==null){return;}
    	if(checkbox5==null){return;}
    	if (checkbox1.isChecked()) {
		   if(axisString.equals("")){
			    axisString+="A1";
			}
			else {
			    axisString+=" A1";
			}			
		  }
    	if (checkbox2.isChecked()) {
 		   if(axisString.equals("")){
 			    axisString+="A2";
 			}
 			else {
 			    axisString+=" A2";
 			}			
 		  }
    	if (checkbox4.isChecked()) {
  		   if(axisString.equals("")){
  			    axisString+="A3";
  			}
  			else {
  			    axisString+=" A3";
  			}			
  		  }
    	if (checkbox3.isChecked()) {
 		   if(axisString.equals("")){
 			    axisString+="A4";
 			}
 			else {
 			    axisString+=" A4";
 			}			
 		  }
    	
    	if (checkbox5.isChecked()) {
 		   if(axisString.equals("")){
 			    axisString+="A5";
 			}
 			else {
 			    axisString+=" A5";
 			}			
 		  }
    	if(!axisString.equals("")){
    		axisString=axisString+" ";
    	}
    	RadioButton radioP_MOVE=(RadioButton) view.findViewById(R.id.radioP_MOVE);
		RadioButton radioSP_MOVE=(RadioButton) view.findViewById(R.id.radioSP_MOVE);
 		RadioButton radioFP_MOVE=(RadioButton) view.findViewById(R.id.radioFP_MOVE);
    	Spinner spinnerP_MOVE=(Spinner)view.findViewById(R.id.spinnerP_MOVE);
    	Spinner spinnerSP_MOVE=(Spinner)view.findViewById(R.id.spinnerSP_MOVE);
    	Spinner spinnerFP_MOVE=(Spinner)view.findViewById(R.id.spinnerFP_MOVE);
    	if(radioP_MOVE==null){return;}
    	if(radioSP_MOVE==null){return;}
    	if(radioFP_MOVE==null){return;}
    	if(spinnerP_MOVE==null){return;}
    	if(spinnerSP_MOVE==null){return;}
    	if(spinnerFP_MOVE==null){return;}
    	if (radioP_MOVE.isChecked()) {
    		if (spinnerP_MOVE.getSelectedItem()!=null) {
    			PO=spinnerP_MOVE.getSelectedItem().toString();
			}
		}else if (radioSP_MOVE.isChecked()) {
			if (spinnerSP_MOVE.getSelectedItem()!=null) {
				PO=spinnerSP_MOVE.getSelectedItem().toString();
			}	
		}else if(radioFP_MOVE.isChecked()){
			if (spinnerFP_MOVE.getSelectedItem()!=null) {
				PO=spinnerFP_MOVE.getSelectedItem().toString();
			}
		}
    }catch(Exception e){
		e.printStackTrace();
	}
    }
    
    
    public void ncOrderIF(View view) {
try{
//    	for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//    	    View v = ((ViewGroup) view).getChildAt(i);
//    	    if (v instanceof Spinner) {
//    	    	if (v.getId()==R.id.spinner1_IN) {   	    			
//    				String In1 = ((Spinner) v).getSelectedItem().toString();		
//				}else if (v.getId()==R.id.spinner2_IN) {
//					if (((Spinner) v).getSelectedItem()==null) {
//						inPutSymbolName="";	
//					}else {
//						inPutSymbolName= ((Spinner) v).getSelectedItem().toString();
//					}
//					
//				}else {
//					String In3 = ((Spinner) v).getSelectedItem().toString();
//				}  	
//    	    }else if (v instanceof RadioButton) {
//    	    	if (((RadioButton) v).isChecked()) {
//    	    		String name=((RadioButton) v).getText().toString();
//    	    		if (name.equals("ON")) {
//    	    			inPutAnswer="ON";
//					}else if (name.equals("OFF")) {
//						inPutAnswer="OFF";
//					}else if (name.equals("SET")) {
//						
//					}
//					
//				}
//				
//			}else if (v instanceof TextView||v instanceof Space) {
//				//排除TextView和space
//		    }else {
//				ncOrderIN(v);
//			}
//    	
//    	}
    	TextView orderPreview_IF=(TextView) view.findViewById(R.id.orderPreview_IF);
    	if(orderPreview_IF==null){return;}
    	morderPreviewString=orderPreview_IF.getText().toString();
    }catch(Exception e){
		e.printStackTrace();
	}
    }

    public void ncOrderOUT(View view) {
    	try{
//    	//试试不用遍历来实现
//    	Spinner spinner2_OUT=(Spinner)view.findViewById(R.id.spinner2_OUT);
//    	if ( spinner2_OUT.getSelectedItem()==null) {
//    		outPutSymbolName="";	
//		}else {
//			outPutSymbolName= spinner2_OUT.getSelectedItem().toString();
//		}
//    	RadioButton radioON_OUT=(RadioButton) view.findViewById(R.id.radioON_OUT);
//    	RadioButton radioOFF_OUT=(RadioButton) view.findViewById(R.id.radioOFF_OUT);
//    	RadioButton radioSET_OUT=(RadioButton) view.findViewById(R.id.radioSET_OUT);
//    	if (radioON_OUT.isChecked()) {
//    		outPutAnswer="ON";	
//		}else if (radioOFF_OUT.isChecked()) {
//			outPutAnswer="OFF";
//		}else if (radioSET_OUT.isChecked()) {
//			outPutAnswer="SET";
//		}

    	TextView orderPreview_OUT=(TextView) view.findViewById(R.id.orderPreview_OUT);
    	if(orderPreview_OUT==null){return;}
    	morderPreviewString=orderPreview_OUT.getText().toString();
    }catch(Exception e){
		e.printStackTrace();
	}
    	
    }

    public void ncOrderPARALLEL(View view) {
    	try{
    	RadioButton radioQON=(RadioButton) view.findViewById(R.id.radioQON);
		RadioButton radioQOFF=(RadioButton) view.findViewById(R.id.radioQOFF);
    	Spinner spinnerQON=(Spinner) view.findViewById(R.id.spinner_QON);
    	Spinner spinnerQOFF=(Spinner) view.findViewById(R.id.spinner_QOFF);
    	if(radioQON==null){return;}
    	if(radioQOFF==null){return;}
    	if(spinnerQON==null){return;}
    	if(spinnerQOFF==null){return;}
    	if (radioQON.isChecked()) {
    		if (spinnerQON.getSelectedItem()!=null) {
    			order_now_PAR="QON";
        		operate_PAR=spinnerQON.getSelectedItem().toString();
			}   		
		}else if(radioQOFF.isChecked()){
			if (spinnerQOFF.getSelectedItem()!=null) {
				order_now_PAR="QOFF";
				operate_PAR=spinnerQOFF.getSelectedItem().toString();
			}		
		}	
    	}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    public void ncOrderSEQUENTIAL(View view) {
    	try{
    	RadioButton radioGOTO=(RadioButton) view.findViewById(R.id.radioGOTO);
		RadioButton radioCALL=(RadioButton) view.findViewById(R.id.radioCALL);
    	Spinner spinnerGOTO=(Spinner) view.findViewById(R.id.spinner_GOTO);
    	Spinner spinnerCALL=(Spinner) view.findViewById(R.id.spinner_CALL);
    	if(radioGOTO==null){return;}
    	if(radioCALL==null){return;}
    	if(spinnerGOTO==null){return;}
    	if(spinnerCALL==null){return;}
    	if (radioGOTO.isChecked()) {
    		if (spinnerGOTO.getSelectedItem()!=null) {
    			order_now_SEQ="GOTO";
        		operate_SEQ=spinnerGOTO.getSelectedItem().toString();
			}   		
		}else if(radioCALL.isChecked()){
			if (spinnerCALL.getSelectedItem()!=null) {
				order_now_SEQ="CALL";
				operate_SEQ=spinnerCALL.getSelectedItem().toString();
			}		
		}
    	}catch(Exception e){
			e.printStackTrace();
		}
    }
    public void ncOrderIFThen(View view) {
    	try{
//    	Spinner spinnerGOTO=(Spinner) view.findViewById(R.id.spinner_GOTO);
//    	Spinner spinnerCALL=(Spinner) view.findViewById(R.id.spinner_CALL);
//    	if (spinnerGOTO.isClickable()) {
//    		if (spinnerGOTO.getSelectedItem()!=null) {
//    			order_now_SEQ="GOTO";
//        		operate_SEQ=spinnerGOTO.getSelectedItem().toString();
//			}   		
//		}else if(spinnerCALL.isClickable()){
//			if (spinnerCALL.getSelectedItem()!=null) {
//				order_now_SEQ="CALL";
//				operate_SEQ=spinnerCALL.getSelectedItem().toString();
//			}		
//		}	
    	
    	TextView orderPreview_IFThen=(TextView) view.findViewById(R.id.orderPreview_IFThen);
    	if(orderPreview_IFThen==null){return;}
//    	Spinner spinnerGOTO_IF=(Spinner) view.findViewById(R.id.spinnerGOTO_IF);
//		Spinner spinnerCALL_IF=(Spinner) view.findViewById(R.id.spinnerCALL_IF);

    	morderPreviewString=orderPreview_IFThen.getText().toString();
//    	if (morderPreviewString.contains("GOTO")||morderPreviewString.contains("CALL")) {	
//		}
//    	else {
//			new AlertDialog.Builder(view.getContext()).setTitle("警告").setMessage("还未添加跳转语句").setPositiveButton("确定", null).show();
//		}
    	}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    public void ncOrderCOUNT(View view) {
try{
    	Spinner spinner_COUNT=(Spinner) view.findViewById(R.id.spinner_COUNT);
    	RadioButton radioINC_COUNT=(RadioButton) view.findViewById(R.id.radioINC_COUNT);
    	RadioButton radioDEC_COUNT=(RadioButton) view.findViewById(R.id.radioDEC_COUNT);
    	RadioButton radioCLR_COUNT=(RadioButton) view.findViewById(R.id.radioCLR_COUNT);
    	EditText editText=(EditText) view.findViewById(R.id.editText_COUNT);
    	if(spinner_COUNT==null){return;}
    	if(radioINC_COUNT==null){return;}
    	if(radioDEC_COUNT==null){return;}
    	if(radioCLR_COUNT==null){return;}
    	if(editText==null){return;}
    	if (spinner_COUNT.getSelectedItem()!=null) {
    		if (radioINC_COUNT.isChecked()) {
        		morderPreviewString=spinner_COUNT.getSelectedItem().toString();
        		order_now="CINC";
    		}else if (radioDEC_COUNT.isChecked()) {
    			morderPreviewString=spinner_COUNT.getSelectedItem().toString();
    			order_now="CDEC";
    		}else if (radioCLR_COUNT.isChecked()) {
    			morderPreviewString=spinner_COUNT.getSelectedItem().toString();
    			order_now="CCLR";
    		}else {
    			morderPreviewString=spinner_COUNT.getSelectedItem().toString()+"="+editText.getText().toString();
    			order_now="CSET";
    		}
		}	
    }catch(Exception e){
		e.printStackTrace();
	}
    }
    
    public void ncOrderWAIT(View view) {
try{
    	Spinner spinner_WAIT=(Spinner) view.findViewById(R.id.spinner_WAIT);
    	EditText editText_WAIT=(EditText) view.findViewById(R.id.editText_WAIT);
    	RadioButton radiofixed_WAIT=(RadioButton) view.findViewById(R.id.radiofixed_WAIT);
    	RadioButton radioset_WAIT=(RadioButton) view.findViewById(R.id.radioset_WAIT);
    	RadioButton radiopos_WAIT=(RadioButton) view.findViewById(R.id.radiopos_WAIT);
    	   if(spinner_WAIT==null){return;}
           if(editText_WAIT==null){return;}
           if(radiofixed_WAIT==null){return;}
           if(radioset_WAIT==null){return;}
           if(radiopos_WAIT==null){return;}
		if (radiofixed_WAIT.isChecked()) {
    		morderPreviewString=editText_WAIT.getText().toString()+"s";
    		order_now="TWAIT";
		}else if (radioset_WAIT.isChecked()) {
			if (spinner_WAIT.getSelectedItem()!=null) {
				morderPreviewString=spinner_WAIT.getSelectedItem().toString();
			}
			order_now="TWAIT";
		}else {
			morderPreviewString="";
			order_now="PWAIT";
		}
    }catch(Exception e){
		e.printStackTrace();
	}
    }
    
    
    public void ncOrderALARM(View view) {
    	try{
    	Spinner spinner_ALARM=(Spinner) view.findViewById(R.id.spinner_ALARM);
    	//TextView content_ALARM=(TextView) view.findViewById(R.id.content_ALARM);
    	if(spinner_ALARM==null){return;}
    	///if(content_ALARM==null){return;}
    	order_now="ALARM";
    	if (spinner_ALARM!=null) {
    		morderPreviewString=spinner_ALARM.getSelectedItem().toString();
    		//mnoteString=content_ALARM.getText().toString();
		} 	
    	}catch(Exception e){
			e.printStackTrace();
		}
    }
    
    @Override
    public void onClick(DialogInterface dialog, int which) {
	// TODO 自动生成的方法存根
    	try{
	switch (Constans.NCorders_enum.getOrder(order_now)) {
	case OUTP:
		break;
	case MOVE:
	case MOVEP:
		ncOrderMOVE(view);
        
		//修改容器内容
		HashMap<String, Object> mapMOVE = new HashMap<String, Object>();
		mapMOVE.put("addressText",
			appList.get(position)
			.get(myAdaptertest.getkeyString()[0])
			.toString());
		mapMOVE.put("numSpinner",
			appList.get(position)
			.get(myAdaptertest.getkeyString()[1])
			.toString());		
		mapMOVE.put("orderSpinner",order_now);
//		mapMOVE.put("operatText",axisString+","+P+","+SP+","+FP);	
		mapMOVE.put("operatText",(axisString+PO).split("<—-—")[0]);	
		mapMOVE.put("noteEditText",
			appList.get(position)
			.get(myAdaptertest.getkeyString()[4])
			.toString());
		appList.set(position, mapMOVE);
		//更新显示
		myAdaptertest.notifyDataSetChanged();
	    break;
	    
		case IF:
			ncOrderIF(view);
			HashMap<String, Object> mapIF = new HashMap<String, Object>();
			if(morderPreviewString.equals("")){
				mapIF.put("addressText",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[0])
						.toString());
					mapIF.put("numSpinner",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[1])
						.toString());		
					mapIF.put("orderSpinner","");
					mapIF.put("operatText","");
					mapIF.put("noteEditText","");
			}else{
			mapIF.put("addressText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[0])
				.toString());
			mapIF.put("numSpinner",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[1])
				.toString());		
			mapIF.put("orderSpinner",order_now);
			mapIF.put("operatText",morderPreviewString);
			mapIF.put("noteEditText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[4])
				.toString());
			}
			appList.set(position, mapIF);
			//更新显示
			myAdaptertest.notifyDataSetChanged();
		    break;
		case OUT:
			ncOrderOUT(view);
			HashMap<String, Object> mapOUT = new HashMap<String, Object>();
			if(morderPreviewString.equals("")){
				mapOUT.put("addressText",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[0])
						.toString());
					mapOUT.put("numSpinner",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[1])
						.toString());		
					mapOUT.put("orderSpinner","");
					mapOUT.put("operatText","");
					mapOUT.put("noteEditText","");
			}else{
			mapOUT.put("addressText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[0])
				.toString());
			mapOUT.put("numSpinner",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[1])
				.toString());		
			mapOUT.put("orderSpinner",order_now);
			mapOUT.put("operatText",morderPreviewString);
			mapOUT.put("noteEditText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[4])
				.toString());
			}
			appList.set(position, mapOUT);
			//更新显示
			myAdaptertest.notifyDataSetChanged();
		    break;    
		case PARALLEL:
			ncOrderPARALLEL(view);
			HashMap<String, Object> mapPARALLEL = new HashMap<String, Object>();
			mapPARALLEL.put("addressText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[0])
				.toString());
			mapPARALLEL.put("numSpinner",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[1])
				.toString());		
			mapPARALLEL.put("orderSpinner",order_now_PAR);
			mapPARALLEL.put("operatText",operate_PAR);
			mapPARALLEL.put("noteEditText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[4])
				.toString());
			appList.set(position, mapPARALLEL);
			//更新显示
			myAdaptertest.notifyDataSetChanged();
		    break;
		case SEQUENTIAL:
			ncOrderSEQUENTIAL(view);
			HashMap<String, Object> mapSEQUENTIAL = new HashMap<String, Object>();
			mapSEQUENTIAL.put("addressText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[0])
				.toString());
			mapSEQUENTIAL.put("numSpinner",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[1])
				.toString());		
			mapSEQUENTIAL.put("orderSpinner",order_now_SEQ);
			mapSEQUENTIAL.put("operatText",operate_SEQ);
			mapSEQUENTIAL.put("noteEditText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[4])
				.toString());
			appList.set(position, mapSEQUENTIAL);
			//更新显示
			myAdaptertest.notifyDataSetChanged();
		    break;
		case IFThen:
			ncOrderIFThen(view);
			HashMap<String, Object> mapIFThen = new HashMap<String, Object>();
			if(morderPreviewString.equals("")){
				mapIFThen.put("addressText",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[0])
						.toString());
					mapIFThen.put("numSpinner",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[1])
						.toString());		
					mapIFThen.put("orderSpinner","");
					mapIFThen.put("operatText","");
					mapIFThen.put("noteEditText","");
			}else{
			mapIFThen.put("addressText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[0])
				.toString());
			mapIFThen.put("numSpinner",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[1])
				.toString());		
			mapIFThen.put("orderSpinner",order_now);
			mapIFThen.put("operatText",morderPreviewString);
			mapIFThen.put("noteEditText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[4])
				.toString());
			}
			appList.set(position, mapIFThen);
			//更新显示
			myAdaptertest.notifyDataSetChanged();
			break;
			
		case COUNTER:
			ncOrderCOUNT(view);
			HashMap<String, Object> mapCOUNT = new HashMap<String, Object>();
			if(morderPreviewString.equals("")){
				mapCOUNT.put("addressText",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[0])
						.toString());
					mapCOUNT.put("numSpinner",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[1])
						.toString());		
					mapCOUNT.put("orderSpinner","");
					mapCOUNT.put("operatText","");
					mapCOUNT.put("noteEditText","");
			}else{
			mapCOUNT.put("addressText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[0])
				.toString());
			mapCOUNT.put("numSpinner",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[1])
				.toString());		
			mapCOUNT.put("orderSpinner",order_now);
			mapCOUNT.put("operatText",morderPreviewString);
			mapCOUNT.put("noteEditText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[4])
				.toString());
			}
			appList.set(position, mapCOUNT);
			//更新显示
			myAdaptertest.notifyDataSetChanged();

			break;
		case WAIT:
			ncOrderWAIT(view);
			HashMap<String, Object> mapWAIT = new HashMap<String, Object>();
			if(morderPreviewString.equals("")){
				mapWAIT.put("addressText",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[0])
						.toString());
					mapWAIT.put("numSpinner",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[1])
						.toString());		
					mapWAIT.put("orderSpinner","");
					mapWAIT.put("operatText","");
					mapWAIT.put("noteEditText","");
			}else{
			mapWAIT.put("addressText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[0])
				.toString());
			mapWAIT.put("numSpinner",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[1])
				.toString());		
			mapWAIT.put("orderSpinner",order_now);
			mapWAIT.put("operatText",morderPreviewString.split("<—-—")[0]);
			mapWAIT.put("noteEditText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[4])
				.toString());
			}
			appList.set(position, mapWAIT);
			//更新显示
			myAdaptertest.notifyDataSetChanged();
			break;	
		case ALARM:
			ncOrderALARM(view);
			HashMap<String, Object> mapALARM = new HashMap<String, Object>();
			if(morderPreviewString.equals("")){
				mapALARM.put("addressText",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[0])
						.toString());
					mapALARM.put("numSpinner",
						appList.get(position)
						.get(myAdaptertest.getkeyString()[1])
						.toString());		
					mapALARM.put("orderSpinner","");
					mapALARM.put("operatText","");
					mapALARM.put("noteEditText","");
			}else{
			mapALARM.put("addressText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[0])
				.toString());
			mapALARM.put("numSpinner",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[1])
				.toString());		
			mapALARM.put("orderSpinner",order_now);
			mapALARM.put("operatText",morderPreviewString);
			mapALARM.put("noteEditText",appList.get(position)
					.get(myAdaptertest.getkeyString()[4])
					.toString());
			}
			appList.set(position, mapALARM);
			//更新显示
			myAdaptertest.notifyDataSetChanged();
			break;
		
	case RET:
		HashMap<String, Object> mapRET = new HashMap<String, Object>();
		mapRET.put("addressText",
			appList.get(position)
			.get(myAdaptertest.getkeyString()[0])
			.toString());
		mapRET.put("numSpinner",
			appList.get(position)
			.get(myAdaptertest.getkeyString()[1])
			.toString());		
		mapRET.put("orderSpinner",order_now);
		mapRET.put("operatText",appList.get(position)
				.get(myAdaptertest.getkeyString()[3])
				.toString());
		mapRET.put("noteEditText",
			appList.get(position)
			.get(myAdaptertest.getkeyString()[4])
			.toString());
		appList.set(position, mapRET);
		//更新显示
		myAdaptertest.notifyDataSetChanged();
		
		break;
	case END:
		HashMap<String, Object> mapEND = new HashMap<String, Object>();
			mapEND.put("addressText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[0])
				.toString());
			mapEND.put("numSpinner",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[1])
				.toString());		
			mapEND.put("orderSpinner",order_now);
			mapEND.put("operatText",appList.get(position)
					.get(myAdaptertest.getkeyString()[3])
					.toString());
			mapEND.put("noteEditText",
				appList.get(position)
				.get(myAdaptertest.getkeyString()[4])
				.toString());
			appList.set(position, mapEND);
			//更新显示
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
