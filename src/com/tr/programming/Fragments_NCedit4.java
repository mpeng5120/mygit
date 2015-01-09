package com.tr.programming;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.*;   
import javax.*;


import wifiProtocol.WifiReadDataFormat;
import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.PlcDataQueryRunnable;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.WatchRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.explain.NCTranslate;
import com.explain.TableTranslate;
import com.tr.R;
import com.tr.programming.Fragments_Device_ActualInput.lockListener;
import com.tr.programming.Fragments_NCedit1.MyAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import awesomeWatch.TableWatch;

public class Fragments_NCedit4 extends Fragment {
	
	String[] nc_ordersall=Constans.nc_orders;
	String[] nc_orders=new String[15];
	Constans.NCorders_enum  NCorders_enum;
	//private Activity targetActivity=getActivity();
	public static ListView mListView;
	DecimalFormat format = new DecimalFormat("####");// 格式转化类,0001的格式，暂时这么做了，以后找到更好的方法可以替换
	ArrayList<HashMap<String, Object>> NcEditList = ArrayListBound.getNCeditList4Data();
	
	public static  MyAdapter NCedit_Adapter = null;
	
	public static ArrayList<String> NullList=new ArrayList<String>();
	//move命令spinner绑定list
	public static ArrayList<String> PPositionList=new ArrayList<String>();
	public static ArrayList<String> SPositionList=new ArrayList<String>();
	public static ArrayList<String> FPositionList=new ArrayList<String>();
    
	//IF OUT IFThen中公用的signalname显示的list
	public static ArrayList<String> List_signalname=new ArrayList<String>();
	
	//IF和IFThen都会用到的
	ArrayAdapter<String> ActualInput_Adapter;
	ArrayAdapter<String> MiddleInput_Adapter;
	ArrayAdapter<String> DataInput_Adapter;
	
	
	ArrayAdapter<String> ActualOutput_Adapter;
	ArrayAdapter<String> MiddleOutput_Adapter;
	ArrayAdapter<String> DataOutput_Adapter;
	
	ArrayAdapter<String> Register_Adapter;
	ArrayAdapter<String> DataRegister_Adapter;

	//IFThen和SEQ都会用到的
	ArrayAdapter<String> GOTO_Adapter;
	ArrayAdapter<String> CALL_Adapter;
	
	ArrayAdapter<String> QON_Adapter;
	ArrayAdapter<String> QOFF_Adapter;
	//IF和 IFThen命令spinner中实际输入暂时绑定的list
	public static ArrayList<String> ActualInputList_symbol=new ArrayList<String>();//需要初始化，不然空指针错误
	public static ArrayList<String> ActualInputList_signal=new ArrayList<String>();
	public static ArrayList<String> MiddleInputList_symbol=new ArrayList<String>();
	public static ArrayList<String> MiddleInputList_signal=new ArrayList<String>();
	public static ArrayList<String> DataInputList_symbol=new ArrayList<String>();
	public static ArrayList<String> DataInputList_signal=new ArrayList<String>();
	public static ArrayList<String> RegisterList_symbol=new ArrayList<String>();
	public static ArrayList<String> RegisterList_signal=new ArrayList<String>();
	public static ArrayList<String> DataRegisterList_symbol=new ArrayList<String>();
	public static ArrayList<String> DataRegisterList_signal=new ArrayList<String>();
	
	public static Hashtable<Integer,View> temphHashTable = new Hashtable<Integer,View>();

	String inPutSymbolName_IF="";//输入设备符号和名称字符串
	String inPutAnswer_IF="";//输入结果ON OFF字符串
	String setText_IF="";//set中输入的值
	String orderPreviewString_IF="";//预览结果字符串
	String logic_IF="";//输入逻辑字符串
	ArrayList<String> orderPreviewList_IF=new ArrayList<String>();//存放预览结果字符串，便于撤销按钮撤销输入时得到字符串
	//IFThen
	String inPutSymbolName_IFThen="";//输入设备符号和名称字符串
	String inPutAnswer_IFThen="";//输入结果ON OFF字符串
	String setText_IFThen="";//set中输入的值
	String orderPreviewString_IFThen="";//预览结果字符串
	String logic_IFThen="";//输入逻辑字符串
	ArrayList<String> orderPreviewList_IFThen=new ArrayList<String>();//存放预览结果字符串，便于撤销按钮撤销输入时得到字符串
	String seqString_IFThen="";//跳转字符串


	//OUT命令spinner中实际输出暂时绑定的list
	public static ArrayList<String> ActualOutputList_symbol=new ArrayList<String>();
	public static ArrayList<String> ActualOutputList_signal=new ArrayList<String>();
	public static ArrayList<String> MiddleOutputList_symbol=new ArrayList<String>();
	public static ArrayList<String> MiddleOutputList_signal=new ArrayList<String>();
	public static ArrayList<String> DataOutputList_symbol=new ArrayList<String>();
	public static ArrayList<String> DataOutputList_signal=new ArrayList<String>();
	String outPutSymbolName="";//输出设备符号和名称字符串
	String outPutAnswer="";//输出结果选择字符串
	String orderPreviewString_OUT="";//预览结果字符串
	ArrayList<String> orderPreviewList_OUT=new ArrayList<String>();//存放预览结果字符串，便于撤销输入时得到字符串


		
	//SEQUNTIAL和IF命令中spinner绑定的list
	public static ArrayList<String> GOTOList=new ArrayList<String>();
	public static ArrayList<String> CALLList=new ArrayList<String>();
	public static ArrayList<String> QONList=new ArrayList<String>();
	public static ArrayList<String> QOFFList=new ArrayList<String>();
	//COUNT命令
	public static ArrayList<String> symbolList_Counter=new ArrayList<String>();
	public static ArrayList<String> noteList_Counter=new ArrayList<String>();
	ArrayAdapter<String> COUNT_Adapter;
	//WAIT命令
	public static ArrayList<String> symbolList_WAIT=new ArrayList<String>();
	ArrayAdapter<String> WAIT_Adapter;
	//ALARM命令
	public static ArrayList<String> symbolList_ALARM=new ArrayList<String>();
	public static ArrayList<String> noteList_ALARM=new ArrayList<String>();
	ArrayAdapter<String> ALARM_Adapter;

	//存放已有的标号名进行语法检查
	private ArrayList<String> NumList_name=new ArrayList<String>();

	private AlarmQueryRunnable alarmQueryRunnable;
	private ledRunnable ledrunnable;

	private MultiAutoCompleteTextView layoutfind; 
	private MultiAutoCompleteTextView layoutreplace;
	public static ArrayList<String> opList_symbol_NC = new ArrayList<String>();//存储设备定义中的所有信号名
	//ArrayList<HashMap<String, Object>> selectNcEditList=new ArrayList<HashMap<String, Object>>();//被选中待粘贴的代码
	public static List<ArrayList<HashMap<String, Object>>> oldNcEditLists=new ArrayList<ArrayList<HashMap<String, Object>>>();//撤销、恢复使用
	public static List<ArrayList<HashMap<String, Object>>> newNcEditLists=new ArrayList<ArrayList<HashMap<String, Object>>>();//撤销、恢复使用
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("destroy");
		alarmQueryRunnable.destroy();
		ledrunnable.destroy();
	}
	public void showNote(){
		try{
			NcEditList= ArrayListBound.getNCeditList4Data();
		for(int i=0;i<NcEditList.size();i++){
			if(NcEditList.get(i).get("operatText").equals("")&&NcEditList.get(i).get("orderSpinner").equals(""))
				continue;
			NcEditList.get(i).put("noteEditText", NCTranslate.noteList.get(i));
			NCedit_Adapter.notifyDataSetChanged();		
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println("Fragments_NCedit4--->onCreate");
		try{
		for(int i=0;i<nc_ordersall.length;i++){
			nc_orders[i]=nc_ordersall[i].split(" ")[0];
		}
	}catch(Exception e){
		e.printStackTrace();
	}
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("Fragments_NCedit4--->onCreateView");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.tab_nc_edit, container, false);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/***************************************************************/
		try{
			NcEditList = ArrayListBound.getNCeditList4Data();
		WatchRunnable.NCbeginWatch(mListView);
		/***************************************************************/
		System.out.println("Fragments_NCedit4  alarmQueryRunnable");
		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
		Thread a2 = new Thread(alarmQueryRunnable);
		a2.start();
		ledrunnable=new ledRunnable(getActivity());
		Thread a4 = new Thread(ledrunnable);
		a4.start();

		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	// 参考Fragment的生命周期
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		System.out.println("Fragments_NCedit4--->onActivityCreated");
		super.onActivityCreated(savedInstanceState);// 调用父类的方法，完成系统的一些操作
        try{
		format.setMinimumIntegerDigits(4);// 显示最少4位数
		updateMoveList(PPositionList,SPositionList,FPositionList,ArrayListBound.getDevicePositionListData());
		updateTIMEList(symbolList_WAIT,ArrayListBound.getDeviceTimerListData());
		updateALARMList(symbolList_ALARM,noteList_ALARM,ArrayListBound.getDeviceAlarmListData());
		updateCOUNTERList(symbolList_Counter,noteList_Counter,ArrayListBound.getDeviceCounterListData());
		
		updateINOUTIFList(ActualInputList_symbol,ActualInputList_signal,ArrayListBound.getDeviceActualInputListData());
		updateINOUTIFList(ActualOutputList_symbol,ActualOutputList_signal,ArrayListBound.getDeviceActualOutputListData());
		updateINOUTIFList(MiddleInputList_symbol,MiddleInputList_signal,ArrayListBound.getDeviceMiddleInputListData());
		updateINOUTIFList(MiddleOutputList_symbol,MiddleOutputList_signal,ArrayListBound.getDeviceMiddleOutputListData());
		updateINOUTIFList(DataInputList_symbol,DataInputList_signal,ArrayListBound.getDeviceDataInputListData());
		updateINOUTIFList(DataOutputList_symbol,DataOutputList_signal,ArrayListBound.getDeviceDataOutputListData());
		updateINOUTIFList(RegisterList_symbol,RegisterList_signal,ArrayListBound.getDeviceRegisterListData());
		updateINOUTIFList(DataRegisterList_symbol,DataRegisterList_signal,ArrayListBound.getDeviceDataRegisterListData());
		
		if (!opList_symbol_NC.isEmpty()) {
			opList_symbol_NC.clear();
		}
		opList_symbol_NC.addAll(ActualInputList_symbol);
		opList_symbol_NC.addAll(ActualOutputList_symbol);
		opList_symbol_NC.addAll(MiddleInputList_symbol);
		opList_symbol_NC.addAll(MiddleOutputList_symbol);
		opList_symbol_NC.addAll(DataInputList_symbol);
		opList_symbol_NC.addAll(DataOutputList_symbol);
		opList_symbol_NC.addAll(RegisterList_symbol);
		opList_symbol_NC.addAll(DataRegisterList_symbol);
		opList_symbol_NC.addAll(symbolList_WAIT);
		opList_symbol_NC.addAll(symbolList_Counter);
		opList_symbol_NC.addAll(PPositionList);
		opList_symbol_NC.addAll(SPositionList);
		opList_symbol_NC.addAll(FPositionList);
		opList_symbol_NC.addAll(symbolList_ALARM);
		mListView = (ListView) getActivity().findViewById(R.id.list);
		if(mListView==null){
			return;
		}
		
		 NCedit_Adapter = new MyAdapter(getActivity(),
				NcEditList,// 数据源
				R.layout.nc_edit_item,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { "addressText", "numSpinner", "orderSpinner",
						"operatText", "noteEditText" },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.addressText, R.id.numSpinner,
						R.id.orderSpinner, R.id.operatText, R.id.noteEditText });		
		
		// 生成适配器的Item和动态数组对应的元素

		// 添加并且显示
		mListView.setAdapter(NCedit_Adapter);
	
		// 添加点击
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
					final int position, long arg3) {
				try{
				if (TR_Programming_Activity.alreadyChecked_programmingPassword) {
						new AlertDialog.Builder(getActivity())
						.setTitle(R.string.programming_batchoperation)//批量操作
						.setItems(getResources().getStringArray(R.array.programming_array_batchoperation),//批量操作数组
								new DialogInterface.OnClickListener() { // 添加选择某项后的方法
									@Override
									public void onClick(DialogInterface dialog,int which) { 
										int selectcount=0;
											int[] selects=new int[NcEditList.size()];
											 for(int i=0;i<NcEditList.size();i++){
												 if(NCedit_Adapter.getListselected().size()-1<i){
													 for(int j=0;j<i-(NCedit_Adapter.getListselected().size()-1);j++){
														 NCedit_Adapter.getListselected().add(false);
													 }
												 }else{
 												     if(NCedit_Adapter.getListselected().get(i)==true){
 													    selects[selectcount]=i;
 													    selectcount++;
 												     }
												 }
											 }
										switch (which) {
										case 0:	//剪切
											ArrayList<HashMap<String, Object>> oldNcEditList=new ArrayList<HashMap<String, Object>>();//撤销、恢复使用
											for(int m=0;m<NcEditList.size();m++){
												HashMap<String, Object> map2 = new HashMap<String, Object>();
												map2.put("addressText",	String.format("%04d", m));
												map2.put("numSpinner",NcEditList.get(m).get("numSpinner"));
												map2.put("orderSpinner",NcEditList.get(m).get("orderSpinner"));
												map2.put("operatText",NcEditList.get(m).get("operatText"));
												map2.put("noteEditText",NcEditList.get(m).get("noteEditText"));
												oldNcEditList.add(m,map2);
											}
											oldNcEditLists.add(oldNcEditList);
											if(oldNcEditLists.size()>30){
												oldNcEditLists.remove(0);
											}
											if(selectcount==0){
												if(Fragments_NCedit1.selectNcEditList==null){return;}
												Fragments_NCedit1.selectNcEditList.clear();
												Fragments_NCedit1.selectNcEditList.add(NcEditList.get(position));
												
												NcEditList.remove(position);
												
													//更新地址号
													for ( int restposition=position ; restposition < NcEditList.size(); restposition++) {
														NcEditList.get(restposition).put("addressText", String.format("%04d", restposition));
													}
												
												NCedit_Adapter.notifyDataSetChanged();
												Toast.makeText(getActivity(),"已剪切当前行", Toast.LENGTH_SHORT).show();
											 }else{
											if(Fragments_NCedit1.selectNcEditList==null){return;}
											Fragments_NCedit1.selectNcEditList.clear();
											for(int i=0;i<selectcount;i++){
												Fragments_NCedit1.selectNcEditList.add(NcEditList.get(selects[i]));
											}
											
											for(int i=0;i<selectcount;i++){
													NcEditList.remove(selects[i]);
													for(int j=i+1;j<selectcount;j++){
														selects[j]=selects[j]-1;
													}
													
														//更新地址号
														for ( int restposition=selects[i] ; restposition < NcEditList.size(); restposition++) {
															NcEditList.get(restposition).put("addressText", String.format("%04d", restposition));
														}
													
											}
											 for(int j=0;j<NcEditList.size();j++){
												 if(NCedit_Adapter.getListselected().get(j)==true){
													NCedit_Adapter.modifyStates(j);//根据点击的位置改变该行的选择状态
												    NCedit_Adapter.getView(j, arg1, mListView);
												 }
												}
												NCedit_Adapter.notifyDataSetChanged();
											Toast.makeText(getActivity(),"已剪切选中行", Toast.LENGTH_SHORT).show();
											 }
											HashMap<String, Object> map = new HashMap<String, Object>();
											map.put("addressText",	String.format("%04d", NcEditList.size()));//
											map.put("numSpinner","");
											map.put("orderSpinner","");
											map.put("operatText","");
											map.put("noteEditText","");
											NcEditList.add(map);
											break;
										case 1://复制
											if(selectcount==0){
												if(Fragments_NCedit1.selectNcEditList==null){return;}
												Fragments_NCedit1.selectNcEditList.clear();
												Fragments_NCedit1.selectNcEditList.add(NcEditList.get(position));
												Toast.makeText(getActivity(),"已复制当前行", Toast.LENGTH_SHORT).show();
											 }else{
											if(Fragments_NCedit1.selectNcEditList==null){return;}
											Fragments_NCedit1.selectNcEditList.clear();
											for(int i=0;i<selectcount;i++){
												Fragments_NCedit1.selectNcEditList.add(NcEditList.get(selects[i]));
											}
											Toast.makeText(getActivity(),"已复制选中行", Toast.LENGTH_SHORT).show();
											 }
											break;
										case 2://粘贴
											 for(int j=0;j<NcEditList.size();j++){
												 if(NCedit_Adapter.getListselected().get(j)==true){
													NCedit_Adapter.modifyStates(j);//根据点击的位置改变该行的选择状态
												    NCedit_Adapter.getView(j, arg1, mListView);
												 }
												}
											if(Fragments_NCedit1.selectNcEditList.size()==0){
												Toast.makeText(getActivity(),"请先复制待粘贴的代码", Toast.LENGTH_SHORT).show();
											}else{
												ArrayList<HashMap<String, Object>> oldNcEditList1=new ArrayList<HashMap<String, Object>>();//撤销、恢复使用
												for(int m=0;m<NcEditList.size();m++){
													HashMap<String, Object> map2 = new HashMap<String, Object>();
													map2.put("addressText",	String.format("%04d", m));
													map2.put("numSpinner",NcEditList.get(m).get("numSpinner"));
													map2.put("orderSpinner",NcEditList.get(m).get("orderSpinner"));
													map2.put("operatText",NcEditList.get(m).get("operatText"));
													map2.put("noteEditText",NcEditList.get(m).get("noteEditText"));
													oldNcEditList1.add(m,map2);		
												}
												oldNcEditLists.add(oldNcEditList1);
												if(oldNcEditLists.size()>30){
													oldNcEditLists.remove(0);
												}
											for(int i=0;i<Fragments_NCedit1.selectNcEditList.size();i++){
											int nextposition=(position+i);
											HashMap<String, Object> map2 = new HashMap<String, Object>();
											map2.put("addressText",	String.format("%04d", nextposition));//
											map2.put("numSpinner",Fragments_NCedit1.selectNcEditList.get(i).get("numSpinner"));
											map2.put("orderSpinner",Fragments_NCedit1.selectNcEditList.get(i).get("orderSpinner"));
											map2.put("operatText",Fragments_NCedit1.selectNcEditList.get(i).get("operatText"));
											map2.put("noteEditText",Fragments_NCedit1.selectNcEditList.get(i).get("noteEditText"));
											NcEditList.add(nextposition,map2);		
											//更新之后的地址号
											for ( int restposition=nextposition+1 ; restposition < NcEditList.size(); restposition++) {
												NcEditList.get(restposition).put("addressText", String.format("%04d", restposition));
											}
											if(NCedit_Adapter.getListselected().size()-1<(nextposition+1)){
												 for(int k=0;k<(nextposition+1)-(NCedit_Adapter.getListselected().size()-1);k++){
													 NCedit_Adapter.getListselected().add(false);
												 }
											 }
											if(NCedit_Adapter.getListselected().get(nextposition)==false){
											  NCedit_Adapter.modifyStates(nextposition);//根据点击的位置改变该行的选择状态
											  NCedit_Adapter.getView(nextposition, arg1, mListView);
											}								
											}
											NCedit_Adapter.notifyDataSetChanged();
											Toast.makeText(getActivity(),"已粘贴选中行", Toast.LENGTH_SHORT).show();
											}
											
											break;
										case 3://删除
											 ArrayList<HashMap<String, Object>> oldNcEditList2=new ArrayList<HashMap<String, Object>>();//撤销、恢复使用
											 for(int m=0;m<NcEditList.size();m++){
												 HashMap<String, Object> map2 = new HashMap<String, Object>();
													map2.put("addressText",	String.format("%04d", m));
													map2.put("numSpinner",NcEditList.get(m).get("numSpinner"));
													map2.put("orderSpinner",NcEditList.get(m).get("orderSpinner"));
													map2.put("operatText",NcEditList.get(m).get("operatText"));
													map2.put("noteEditText",NcEditList.get(m).get("noteEditText"));
													oldNcEditList2.add(m,map2);
												}
											 oldNcEditLists.add(oldNcEditList2);
											 if(oldNcEditLists.size()>30){
													oldNcEditLists.remove(0);
												}
											 if(selectcount==0){
													NcEditList.remove(position);
													
														//更新地址号
														for ( int restposition=position ; restposition < NcEditList.size(); restposition++) {
															NcEditList.get(restposition).put("addressText", String.format("%04d", restposition));
														}
													
													NCedit_Adapter.notifyDataSetChanged();
													Toast.makeText(getActivity(),"已删除当前行", Toast.LENGTH_SHORT).show();
											 }else{
												 for(int i=0;i<selectcount;i++){
														NcEditList.remove(selects[i]);
														for(int j=i+1;j<selectcount;j++){
															selects[j]=selects[j]-1;
														}
														
															//更新地址号
															for ( int restposition=selects[i] ; restposition < NcEditList.size(); restposition++) {
																NcEditList.get(restposition).put("addressText", String.format("%04d", restposition));
															}
														
												}
												 for(int j=0;j<NcEditList.size();j++){
													 if(NCedit_Adapter.getListselected().get(j)==true){
														NCedit_Adapter.modifyStates(j);//根据点击的位置改变该行的选择状态
													    NCedit_Adapter.getView(j, arg1, mListView);
													 }
													}
													NCedit_Adapter.notifyDataSetChanged();
												Toast.makeText(getActivity(),"已删除选中行", Toast.LENGTH_SHORT).show();
												
											 }
											    map = new HashMap<String, Object>();
												map.put("addressText",	String.format("%04d", NcEditList.size()));//
												map.put("numSpinner","");
												map.put("orderSpinner","");
												map.put("operatText","");
												map.put("noteEditText","");
												NcEditList.add(map);
											break;
										case 4://插入
											 ArrayList<HashMap<String, Object>> oldNcEditList3=new ArrayList<HashMap<String, Object>>();//撤销、恢复使用
											 for(int m=0;m<NcEditList.size();m++){
												 HashMap<String, Object> map2 = new HashMap<String, Object>();
													map2.put("addressText",	String.format("%04d", m));
													map2.put("numSpinner",NcEditList.get(m).get("numSpinner"));
													map2.put("orderSpinner",NcEditList.get(m).get("orderSpinner"));
													map2.put("operatText",NcEditList.get(m).get("operatText"));
													map2.put("noteEditText",NcEditList.get(m).get("noteEditText"));
													oldNcEditList3.add(m,map2);
												}
											 oldNcEditLists.add(oldNcEditList3);
											 if(oldNcEditLists.size()>30){
													oldNcEditLists.remove(0);
												}
											if(selectcount==0){
												HashMap<String, Object> map1 = new HashMap<String, Object>();
												map1.put("addressText",	String.format("%04d", position) ); //数字格式化，右对齐，高位补0,0代表前面补0,4代表长度为4，d代表参数为正数型  
												map1.put("numSpinner", "");
												map1.put("orderSpinner","");
												map1.put("operatText","");
												map1.put("noteEditText","");
												NcEditList.add(position,map1);		
												//更新地址号
												for ( int restposition=position+1 ; restposition < NcEditList.size(); restposition++) {
													NcEditList.get(restposition).put("addressText", String.format("%04d", restposition));
												}								
												Toast.makeText(getActivity(),"上面已插入一行", Toast.LENGTH_SHORT).show();	
												
											 }else{
												 for(int i=0;i<selectcount;i++){
													    HashMap<String, Object> map1 = new HashMap<String, Object>();
														map1.put("addressText",	String.format("%04d", position) ); //数字格式化，右对齐，高位补0,0代表前面补0,4代表长度为4，d代表参数为正数型  
														map1.put("numSpinner", "");
														map1.put("orderSpinner","");
														map1.put("operatText","");
														map1.put("noteEditText","");
														NcEditList.add(position,map1);		
														//更新地址号
														for ( int restposition=position+1 ; restposition < NcEditList.size(); restposition++) {
															NcEditList.get(restposition).put("addressText", String.format("%04d", restposition));
														}									
													}
											    Toast.makeText(getActivity(),"已插入空白行", Toast.LENGTH_SHORT).show();
														
											 }
											for(int j=0;j<NcEditList.size();j++){
												 if(NCedit_Adapter.getListselected().size()-1<j){
													 for(int k=0;k<j-(NCedit_Adapter.getListselected().size()-1);k++){
														 NCedit_Adapter.getListselected().add(false);
													 }
												 }else{
													 if(NCedit_Adapter.getListselected().get(j)==true){
															NCedit_Adapter.modifyStates(j);//根据点击的位置改变该行的选择状态
														    NCedit_Adapter.getView(j, arg1, mListView);
														   
														 }
												 }
												
												} 
											NCedit_Adapter.notifyDataSetChanged();
											
											break;
										case 5://查找/替换
											
											final View view_findandreplace = View.inflate(getActivity(),
													R.layout.tab_nc_findandreplace, null);
											final AlertDialog dialog_findandreplace = new AlertDialog.Builder(
													getActivity())
													.setTitle("查找/替换")
													.setView(view_findandreplace)
													.setPositiveButton("替换",
															new DialogInterface.OnClickListener() {
																public void onClick(DialogInterface dialog,int which) {
																	ArrayList<HashMap<String, Object>> oldNcEditList4=new ArrayList<HashMap<String, Object>>();//撤销、恢复使用
																	for(int m=0;m<NcEditList.size();m++){
																		HashMap<String, Object> map2 = new HashMap<String, Object>();
																		map2.put("addressText",	String.format("%04d", m));
																		map2.put("numSpinner",NcEditList.get(m).get("numSpinner"));
																		map2.put("orderSpinner",NcEditList.get(m).get("orderSpinner"));
																		map2.put("operatText",NcEditList.get(m).get("operatText"));
																		map2.put("noteEditText",NcEditList.get(m).get("noteEditText"));
																		oldNcEditList4.add(m,map2);
																	}
																	oldNcEditLists.add(oldNcEditList4);
																	if(oldNcEditLists.size()>30){
																		oldNcEditLists.remove(0);
																	}
																	String layoutfindstr=layoutfind.getText().toString().trim();
																	String layoutreplacestr=layoutreplace.getText().toString().trim();
																	int layoutreplacecount=0;
																	 for(int j=0;j<NcEditList.size();j++){
																		 String operatstr=NcEditList.get(j).get("operatText").toString();
																		 if(find_nc(layoutfindstr,operatstr)&&
																				 NCedit_Adapter.getListselected().get(j)==true){
																			 operatstr=replace_nc(layoutfindstr,operatstr,layoutreplacestr);
																			 NcEditList.get(j).put("operatText", operatstr);
																		    NCedit_Adapter.notifyDataSetChanged();
																		    layoutreplacecount++;
																		 }
																		}
																	 if(layoutreplacecount==0){
																		 oldNcEditLists.remove(oldNcEditLists.size()-1);
																		 Toast.makeText(getActivity(),"无信号名被替换", Toast.LENGTH_SHORT).show(); 
																	 }else{
																		 
																		 Toast.makeText(getActivity(),layoutreplacecount+"信号名已替换", Toast.LENGTH_SHORT).show();
																	 }
																}
															}).setNegativeButton("查找",
															new DialogInterface.OnClickListener() {
																public void onClick(DialogInterface dialog,int which) {
																	String layoutfindstr=layoutfind.getText().toString().trim();
																	for(int j=0;j<NcEditList.size();j++){
																		 if(NCedit_Adapter.getListselected().get(j)==true){
																			NCedit_Adapter.modifyStates(j);//根据点击的位置改变该行的选择状态
																		    NCedit_Adapter.getView(j, arg1, mListView);
																		    NCedit_Adapter.notifyDataSetChanged();
																		 }
																		}
																	if(layoutfindstr.equals("")){
																	}else{
																		int layoutfindcount=0;
																	 for(int j=0;j<NcEditList.size();j++){
																		 String operatString=NcEditList.get(j).get("operatText").toString();
																		 if(find_nc(layoutfindstr,operatString)){
																			NCedit_Adapter.modifyStates(j);//根据点击的位置改变该行的选择状态
																		    NCedit_Adapter.getView(j, arg1, mListView);
																		    NCedit_Adapter.notifyDataSetChanged();
																		    layoutfindcount++;
																		 }
																		}
																	 if(layoutfindcount==0){
																		 Toast.makeText(getActivity(),"无此信号名", Toast.LENGTH_SHORT).show(); 
																	 }else{
																		 Toast.makeText(getActivity(),layoutfindcount+"行代码包含此信号名", Toast.LENGTH_SHORT).show();
																	 }
																	 }
																}
															}).show();
											layoutfind = (MultiAutoCompleteTextView) dialog_findandreplace.findViewById(R.id.editText_find);
											if (layoutfind== null) {
												return;
											}
											layoutfind.setTokenizer(new SemicolonTokenizer(' '));// 设置空格为分隔符
											layoutfind.setCompletionHint("请选择一项");
											layoutfind.setThreshold(1);// 输入一个字符就开始检测，出现下拉选择
											layoutfind.setDropDownHeight(300);
											ArrayAdapter<String> adapter = new ArrayAdapter<String>(
													getActivity(),
													android.R.layout.simple_dropdown_item_1line,
													opList_symbol_NC);
											layoutfind.setAdapter(adapter);
											layoutfind.addTextChangedListener(new TextWatcher() {
										           @Override
										            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
										                    int arg3) {
										            }
										             @Override
										            public void onTextChanged(CharSequence s, int arg1, int arg2,
										                    int arg3) {
										            	
										            }
													@Override
													public void afterTextChanged(Editable s) {
														// TODO Auto-generated method stub
														
														 String str=s.toString();
														 String str1="";
														 String str2="";
														 if(str.contains("<—-—")||str.contains("—-—>")){
														 str1=str.split("<—-—")[0].trim();
														 str2=str.split("—-—>")[1].trim();
														 str=str1+str2;
														 }
														 if(!layoutfind.getText().toString().equals(str)){
															 layoutfind.setText(str);
															 layoutfind.setSelection(str1.length());// 设置光标位置 
														 }
														
													}
												});
											
											layoutreplace = (MultiAutoCompleteTextView) dialog_findandreplace.findViewById(R.id.editText_replace);
											if (layoutreplace== null) {
												return;
											}
											layoutreplace.setTokenizer(new SemicolonTokenizer(' '));// 设置空格为分隔符
											layoutreplace.setCompletionHint("请选择一项");
											layoutreplace.setThreshold(1);// 输入一个字符就开始检测，出现下拉选择
											layoutreplace.setDropDownHeight(300);
											ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
													getActivity(),
													android.R.layout.simple_dropdown_item_1line,
													opList_symbol_NC);
											layoutreplace.setAdapter(adapter1);
											layoutreplace.addTextChangedListener(new TextWatcher() {
										           @Override
										            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
										                    int arg3) {
										            }
										             @Override
										            public void onTextChanged(CharSequence s, int arg1, int arg2,
										                    int arg3) {
										            	
										            }
													@Override
													public void afterTextChanged(Editable s) {
														// TODO Auto-generated method stub
														
														 String str=s.toString();
														 String str1="";
														 String str2="";
														 if(str.contains("<—-—")||str.contains("—-—>")){
														 str1=str.split("<—-—")[0].trim();
														 str2=str.split("—-—>")[1].trim();
														 str=str1+str2;
														 }
														 if(!layoutreplace.getText().toString().equals(str)){
															 layoutreplace.setText(str);
															 layoutreplace.setSelection(str1.length());// 设置光标位置 
														 }
														
													}
												});
											break;
										case 8://全选
											 for(int j=0;j<NcEditList.size();j++){
												 if(NCedit_Adapter.getListselected().get(j)==false){
													NCedit_Adapter.modifyStates(j);//根据点击的位置改变该行的选择状态
												    NCedit_Adapter.getView(j, arg1, mListView);
												    NCedit_Adapter.notifyDataSetChanged();
												 }
												}
											break;
										case 9://全不选
											 for(int j=0;j<NcEditList.size();j++){
												 if(NCedit_Adapter.getListselected().get(j)==true){
													NCedit_Adapter.modifyStates(j);//根据点击的位置改变该行的选择状态
												    NCedit_Adapter.getView(j, arg1, mListView);
												    NCedit_Adapter.notifyDataSetChanged();
												 }
												}
											break;
										case 10://反选
											 for(int j=0;j<NcEditList.size();j++){
													NCedit_Adapter.modifyStates(j);//根据点击的位置改变该行的选择状态
												    NCedit_Adapter.getView(j, arg1, mListView);
												    NCedit_Adapter.notifyDataSetChanged();
												}
											break;
										case 6://撤销
											if(oldNcEditLists.size()<=0){
												Toast.makeText(getActivity(), "没有待撤销的操作", Toast.LENGTH_SHORT).show();
											}else{
												if(newNcEditLists.size()==0){
												ArrayList<HashMap<String, Object>> newNcEditList=new ArrayList<HashMap<String, Object>>();//撤销、恢复使用
												for(int m=0;m<NcEditList.size();m++){
													HashMap<String, Object> map2 = new HashMap<String, Object>();
													map2.put("addressText",	String.format("%04d", m));
													map2.put("numSpinner",NcEditList.get(m).get("numSpinner"));
													map2.put("orderSpinner",NcEditList.get(m).get("orderSpinner"));
													map2.put("operatText",NcEditList.get(m).get("operatText"));
													map2.put("noteEditText",NcEditList.get(m).get("noteEditText"));
													newNcEditList.add(m,map2);
												}
												newNcEditLists.add(newNcEditList);
												}
													ArrayList<HashMap<String, Object>> newNcEditList1=new ArrayList<HashMap<String, Object>>();//撤销、恢复使用
													NcEditList.clear();
													for(int m=0;m<(oldNcEditLists.get(oldNcEditLists.size()-1)).size();m++){
														HashMap<String, Object> map2 = new HashMap<String, Object>();
														map2.put("addressText",	String.format("%04d", m));
														map2.put("numSpinner",(oldNcEditLists.get(oldNcEditLists.size()-1)).get(m).get("numSpinner"));
														map2.put("orderSpinner",(oldNcEditLists.get(oldNcEditLists.size()-1)).get(m).get("orderSpinner"));
														map2.put("operatText",(oldNcEditLists.get(oldNcEditLists.size()-1)).get(m).get("operatText"));
														map2.put("noteEditText",(oldNcEditLists.get(oldNcEditLists.size()-1)).get(m).get("noteEditText"));
														newNcEditList1.add(m,map2);
														NcEditList.add(m,map2);
													}
													newNcEditLists.add(newNcEditList1);
												
												 NCedit_Adapter = new MyAdapter(getActivity(),
														    NcEditList,// 数据源
															R.layout.nc_edit_item,// ListItem的XML实现
															// 动态数组与ImageItem对应的子项
															new String[] { "addressText", "numSpinner", "orderSpinner",
																	"operatText", "noteEditText" },
															// ImageItem的XML文件里面的一个ImageView,两个TextView ID
															new int[] { R.id.addressText, R.id.numSpinner,
																	R.id.orderSpinner, R.id.operatText, R.id.noteEditText });		
												mListView.setAdapter(NCedit_Adapter);
												NCedit_Adapter.notifyDataSetChanged();
												oldNcEditLists.remove(oldNcEditLists.size()-1);
											}
											break;
										case 7://恢复
											if(newNcEditLists.size()<=1){
												Toast.makeText(getActivity(), "没有待恢复的操作", Toast.LENGTH_SHORT).show();
											}else{
											ArrayList<HashMap<String, Object>> oldNcEditList5=new ArrayList<HashMap<String, Object>>();//撤销、恢复使用
											for(int m=0;m<(newNcEditLists.get(newNcEditLists.size()-1)).size();m++){
												HashMap<String, Object> map2 = new HashMap<String, Object>();
												map2.put("addressText",	String.format("%04d", m));
												map2.put("numSpinner",(newNcEditLists.get(newNcEditLists.size()-1)).get(m).get("numSpinner"));
												map2.put("orderSpinner",(newNcEditLists.get(newNcEditLists.size()-1)).get(m).get("orderSpinner"));
												map2.put("operatText",(newNcEditLists.get(newNcEditLists.size()-1)).get(m).get("operatText"));
												map2.put("noteEditText",(newNcEditLists.get(newNcEditLists.size()-1)).get(m).get("noteEditText"));
												oldNcEditList5.add(m,map2);
											}
											oldNcEditLists.add(oldNcEditList5);
											
											newNcEditLists.remove(newNcEditLists.size()-1);
											
											NcEditList.clear();
											for(int m=0;m<(newNcEditLists.get(newNcEditLists.size()-1)).size();m++){
												HashMap<String, Object> map2 = new HashMap<String, Object>();
												map2.put("addressText",	String.format("%04d", m));
												map2.put("numSpinner",(newNcEditLists.get(newNcEditLists.size()-1)).get(m).get("numSpinner"));
												map2.put("orderSpinner",(newNcEditLists.get(newNcEditLists.size()-1)).get(m).get("orderSpinner"));
												map2.put("operatText",(newNcEditLists.get(newNcEditLists.size()-1)).get(m).get("operatText"));
												map2.put("noteEditText",(newNcEditLists.get(newNcEditLists.size()-1)).get(m).get("noteEditText"));
												NcEditList.add(m,map2);
											}
											 NCedit_Adapter = new MyAdapter(getActivity(),
														NcEditList,// 数据源
														R.layout.nc_edit_item,// ListItem的XML实现
														// 动态数组与ImageItem对应的子项
														new String[] { "addressText", "numSpinner", "orderSpinner",
																"operatText", "noteEditText" },
														// ImageItem的XML文件里面的一个ImageView,两个TextView ID
														new int[] { R.id.addressText, R.id.numSpinner,
																R.id.orderSpinner, R.id.operatText, R.id.noteEditText });		
											mListView.setAdapter(NCedit_Adapter);
											NCedit_Adapter.notifyDataSetChanged();
											}
											break;
										default:
											break;
										}
									}
								}).setNegativeButton("取消", null).show();
				
			
				}else {
					try{
						Toast.makeText(getActivity(), "当前处于锁定状态", Toast.LENGTH_SHORT).show();
						}catch(Exception e){
							e.printStackTrace();
						}
			}}catch(Exception e){
				e.printStackTrace();
			}
				return false;
			}
		});
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);//设置为多选
		// 添加点击
				mListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							final int position, long arg3) {
						try{
						if (TR_Programming_Activity.alreadyChecked_programmingPassword) {
								NCedit_Adapter.modifyStates(position);//根据点击的位置改变该行的选择状态
								NCedit_Adapter.getView(position, arg1, mListView);
								NCedit_Adapter.notifyDataSetChanged();
						}else {
						Toast.makeText(getActivity(), "当前处于锁定状态", Toast.LENGTH_SHORT).show();
					    }}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
		// 添加长按点击，看看以后有什么别的功能，可以采用长按
		/*
		 * list.setOnCreateContextMenuListener(new OnCreateContextMenuListener()
		 * {
		 * 
		 * @Override public void onCreateContextMenu(ContextMenu menu, View
		 * v,ContextMenuInfo menuInfo) {
		 * menu.setHeaderTitle("长按菜单-ContextMenu"); menu.add(0, 0, 0,
		 * "弹出长按菜单0"); menu.add(0, 1, 0, "弹出长按菜单1"); } });
		 */
	}catch(Exception e){
		e.printStackTrace();
	}
	}


	private String replace_nc(String rege,String str,String value){
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
	}catch(Exception e){
		e.printStackTrace();
	}
		   return str.trim();
	}
	private boolean find_nc(String rege,String str){
		   String regex = "[^A-Za-z0-9]"+rege+"[^A-Za-z0-9]";
		   str=" "+str+" ";
		   boolean result=false;
		   try{
		   Pattern pat = Pattern.compile(regex);  
		   Matcher matcher = pat.matcher(str); 
		   while (matcher.find()) { 
			   result=true;
		   } 
	}catch(Exception e){
		e.printStackTrace();
	}
		   return result;
	}
	public void updateMoveList(ArrayList<String> ppositionList,ArrayList<String> spositionList,ArrayList<String> fpositionList,ArrayList<HashMap<String, Object>> arrayList) {
		try{
		ppositionList.clear();
		spositionList.clear();
		fpositionList.clear();
		for (int i = 0; i < arrayList.size(); i++) {
			String posString=(String) arrayList.get(i).get("symbolNameEditText");
			String signalstr=(String) arrayList.get(i).get("signalNameEditText");
			if (posString.contains("SP")&&arrayList.get(i).get("setItem").toString().equals("1")) {
				spositionList.add(posString+"<—-—"+signalstr+"—-—>");
			}else if (posString.contains("FP")&&arrayList.get(i).get("setItem").toString().equals("1")) {
				fpositionList.add(posString+"<—-—"+signalstr+"—-—>");
			}else if (posString.contains("P")&&arrayList.get(i).get("setItem").toString().equals("1")) {
				ppositionList.add(posString+"<—-—"+signalstr+"—-—>");
			}
		}	
	}catch(Exception e){
		e.printStackTrace();
	}
	}

	@SuppressWarnings("null")
	public void updateINOUTIFList(ArrayList<String> symbolList, ArrayList<String> signalList,ArrayList<HashMap<String, Object>> arrayList) {
		try{
			symbolList.clear();
			signalList.clear();
			for (int i = 0; i < arrayList.size(); i++) {											
				String symbolstr=arrayList.get(i).get("symbolNameEditText").toString().trim();
				String signalstr=(String)arrayList.get(i).get("signalNameEditText").toString().trim();
				if (!symbolstr.equals("")) {
					symbolList.add(symbolstr);
					signalList.add(signalstr);
				}	
			}	
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
	@SuppressWarnings("null")
	public void updateALARMList(ArrayList<String> symbolList, ArrayList<String> noteList,ArrayList<HashMap<String, Object>> arrayList) {
		// 也可以直接清空再添加
	try{
			symbolList.clear();
			noteList.clear();
			for (int i = 0; i < arrayList.size(); i++) {											
				String symbolstr= arrayList.get(i).get("symbolNameEditText").toString().trim();
				String notestr= arrayList.get(i).get("noteEditText").toString().trim();
				if (!symbolstr.equals("")) {
					symbolList.add(symbolstr);
					noteList.add(notestr);
				}	
			}	
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
	@SuppressWarnings("null")
	public void updateTIMEList(ArrayList<String> symbolList,ArrayList<HashMap<String, Object>> arrayList) {
		// 也可以直接清空再添加
	try{
		symbolList.clear();
	for (int i = 0; i <arrayList.size(); i++) {
		String symbolString_WAIT= arrayList.get(i).get("symbolNameEditText").toString().trim();
		String signalstr=arrayList.get(i).get("signalNameEditText").toString().trim();
		if (!symbolString_WAIT.equals("")&&arrayList.get(i).get("setItem").toString().trim().equals("1")) {
			symbolList.add(symbolString_WAIT+"<—-—"+signalstr+"—-—>");
		}
	}
   }catch(Exception e){
	e.printStackTrace();
   }
   }
	
	@SuppressWarnings("null")
	public void updateCOUNTERList(ArrayList<String> symbolList, ArrayList<String> noteList,ArrayList<HashMap<String, Object>> arrayList) {
		// 也可以直接清空再添加
	try{
		symbolList.clear();
		noteList.clear();
		for (int i = 0; i < arrayList.size(); i++) {
			String symbolString_Counter=arrayList.get(i).get("symbolNameEditText").toString().trim();
			String notrString_Counter=arrayList.get(i).get("signalNameEditText").toString().trim();
			if (symbolString_Counter.contains("C")&&arrayList.get(i).get("setItem").toString().equals("1")) {
				symbolList.add(symbolString_Counter);
				noteList.add(notrString_Counter);
			}
		}	
   }catch(Exception e){
	e.printStackTrace();
   }
   }


	// 由于需要在listview中针对每一行各个控件增加响应函数，因此使用自定义的listview的接口
	// 内部类
	public class MyAdapter extends BaseAdapter {
		private class buttonViewHolder {
			TextView addressText;
			TextView numSpinner;
			TextView orderSpinner;
			TextView operatText;
			TextView noteEditText;

		}

		private ArrayList<HashMap<String, Object>> mAppList;
		private LayoutInflater mInflater;
		private Context mContext;
		private String[] keyString;
		private int[] valueViewID;
		private buttonViewHolder holder;
		private int mLayoutID;
		private int mselectItem = -1;
		private List<Boolean> listselected;//用布尔型的list记录每一行的选中状态

		// MyAdapter的构造函数
		public MyAdapter(Context c, ArrayList<HashMap<String, Object>> appList,int layoutID, String[] from, int[] to) {
			mAppList = appList;
			mContext = c;
			mLayoutID=layoutID;
			mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			keyString = new String[from.length];
			valueViewID = new int[to.length];

			System.arraycopy(from, 0, keyString, 0, from.length);
			System.arraycopy(to, 0, valueViewID, 0, to.length);
			
			
			 this.setListselected(new ArrayList<Boolean>(getCount()));
		        for(int i=0;i<getCount();i++)
		        	getListselected().add(false);//初始为false，长度和listview一样

		}
		public List<Boolean> getListselected() {
	            return listselected;
	    }
	    public void setListselected(List<Boolean> listselected) {
	            this.listselected = listselected;
	    }

		public String[] getkeyString() {
		    return this.keyString;
		}

		@Override
		public int getCount() {
			return mAppList.size();
		}

		@Override
		public Object getItem(int position) {
			return mAppList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		public void setSelectItem(int selectItem) {
			mselectItem = selectItem;
		}
		// 删除某一行
		public void removeItem(int position) {
			mAppList.remove(position);
			this.notifyDataSetChanged();
		}
		 public void modifyStates(int position){ 
			 if(getListselected().size()-1<position){
				 for(int i=0;i<position-(getListselected().size()-1);i++){
				   getListselected().add(false);
				 }
			 }else{
	            if(getListselected().get(position)==false){
	                 getListselected().set(position, true);//如果相应position的记录是未被选中则设置为选中（true）
	                 //notifyDataSetChanged();
	            }else{
	                getListselected().set(position, false);//否则相应position的记录是被选中则设置为未选中（false）
	                 //notifyDataSetChanged();
	                }
			 }
	      }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null) {
				holder = (buttonViewHolder) convertView.getTag();
				
			} else {
//				convertView = mInflater.inflate(R.layout.nc_edit_item,null);
				convertView= mInflater.inflate(mLayoutID,null);
				holder = new buttonViewHolder();
				holder.addressText = (TextView) convertView
						.findViewById(valueViewID[0]);
				holder.numSpinner = (TextView) convertView
						.findViewById(valueViewID[1]);
				holder.orderSpinner = (TextView) convertView
						.findViewById(valueViewID[2]);
				holder.operatText = (TextView) convertView
						.findViewById(valueViewID[3]);
				holder.noteEditText = (TextView) convertView
						.findViewById(valueViewID[4]);

				convertView.setTag(holder);
			}
			temphHashTable.put(position, convertView);

			HashMap<String, Object> appInfo = mAppList.get(position);
			if (appInfo != null) {
				String addressText = (String) appInfo.get(keyString[0]);
				String numSpinner = appInfo.get(keyString[1]).toString();
				String orderSpinner = (String) appInfo.get(keyString[2]);
				String operatText = (String) appInfo.get(keyString[3]);
				String noteEditText = (String) appInfo.get(keyString[4]);

				holder.addressText.setText(addressText);
				holder.numSpinner.setText(numSpinner);
				holder.orderSpinner.setText(orderSpinner);
				holder.operatText.setText(operatText);
				holder.noteEditText.setText(noteEditText);
				// 选中红色显示
				if(getListselected().size()-1<position){
					 for(int i=0;i<position-(getListselected().size()-1);i++){
					   getListselected().add(false);
					 }
				 }else{
					 if(TR_Programming_Activity.menu.findItem(R.id.menu_watchStart).isVisible()){
			    		  setSelectItem(-1);
			    	  }
			      if(getListselected().get(position)==false){//如果未被选中，设置为黑色
			    	    holder.addressText.setTextColor(Color.BLACK);
			    	    holder.numSpinner.setTextColor(Color.BLACK);
			    	    holder.orderSpinner.setTextColor(Color.BLACK);
			    	    holder.operatText.setTextColor(Color.BLACK);
			    	    holder.noteEditText.setTextColor(Color.BLACK);
			    	  convertView.setBackgroundColor(Color.BLACK);
	                }else{//如果被选中，设置为红色
	                	holder.addressText.setTextColor(Color.RED);
	                	holder.numSpinner.setTextColor(Color.RED);
	                	holder.orderSpinner.setTextColor(Color.RED);
	                	holder.operatText.setTextColor(Color.RED);
	                	holder.noteEditText.setTextColor(Color.RED);
	                	convertView.setBackgroundColor(Color.RED);
			    	    }
				 }
				if (TR_Programming_Activity.alreadyChecked_programmingPassword) {
					holder.orderSpinner.setOnClickListener(new orderListener(position));
					holder.numSpinner.setOnClickListener(new numlistener(position));
					holder.noteEditText.setOnClickListener(new notelistener(position));
				}else {
					holder.orderSpinner.setOnClickListener(new lockListener(getActivity()));
					holder.numSpinner.setOnClickListener(new lockListener(getActivity()));
					holder.noteEditText.setOnClickListener(new lockListener(getActivity()));
				}
			}
			// 选中绿色显示
						if (position == mselectItem) {
							// 如果当前的行就是ListView中选中的一行，就更改显示样式
							holder.addressText.setBackgroundColor(Color.GREEN);
				    	    holder.numSpinner.setBackgroundColor(Color.GREEN);
				    	    holder.orderSpinner.setBackgroundColor(Color.GREEN);
				    	    holder.operatText.setBackgroundColor(Color.GREEN);
				    	    holder.noteEditText.setBackgroundColor(Color.GREEN);
						} else {
							holder.addressText.setBackgroundColor(Color.GRAY);
				    	    holder.numSpinner.setBackgroundColor(Color.WHITE);
				    	    holder.orderSpinner.setBackgroundColor(Color.WHITE);
				    	    holder.operatText.setBackgroundColor(Color.GRAY);
				    	    holder.noteEditText.setBackgroundColor(Color.WHITE);
						}
			return convertView;
		}
		class orderListener implements android.view.View.OnClickListener {
			private int position;			
			orderListener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
            try{
				//判断自动增加一行
				if (position==(mAppList.size()-1)) {
					addOneItem();	
					notifyDataSetChanged();
				}
				if(mAppList.get(position).get("orderSpinner").toString().trim().equals("MOVE")){
					moveoperate("MOVE",position);
				}else if(mAppList.get(position).get("orderSpinner").toString().trim().equals("MOVEP")){
					moveoperate("MOVEP",position);
				}else if(mAppList.get(position).get("orderSpinner").toString().trim().equals("OUT")){
					outoperate(position);
				}else if(mAppList.get(position).get("orderSpinner").toString().trim().equals("IF")){
					ifoperate(position);
				}else if(mAppList.get(position).get("orderSpinner").toString().trim().equals("TWAIT")){
					waitoperate(position);
				}else if(mAppList.get(position).get("orderSpinner").toString().trim().equals("IFThen")){
					ifthenoperate(position);
				}else if(mAppList.get(position).get("orderSpinner").toString().trim().equals("ALARM")){
					alarmoperate(position);
				}else if(mAppList.get(position).get("orderSpinner").toString().trim().equals("CINC")||
						mAppList.get(position).get("orderSpinner").toString().trim().equals("CDEC")||
						mAppList.get(position).get("orderSpinner").toString().trim().equals("CCLR")||
						mAppList.get(position).get("orderSpinner").toString().trim().equals("CSET")){
					counteroperate(position);
				}else if(mAppList.get(position).get("orderSpinner").toString().trim().equals("GOTO")||
						mAppList.get(position).get("orderSpinner").toString().trim().equals("CALL")){
					sequentialoperate(position);
				}else if(mAppList.get(position).get("orderSpinner").toString().trim().equals("QON")||
						mAppList.get(position).get("orderSpinner").toString().trim().equals("QOFF")){
					paralleloperate(position);
				}else{
				// 弹出指令选择窗口，修改内容
					selectoperate(position);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			}

		}

		class numlistener implements android.view.View.OnClickListener {

			private int position;

			// 构造函数
			numlistener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
try{
				//判断自动增加一行
				if (position==(mAppList.size()-1)) {
					addOneItem();	
					notifyDataSetChanged();
				}
				
				
//				final DecimalFormat format = new DecimalFormat("###");// 格式转化类,001的格式，暂时这么做了，以后找到更好的方法可以替换
//				format.setMinimumIntegerDigits(3);
				
				View NumView=View.inflate(getActivity(), R.layout.nc_num,null );//在xml属性中设置只能输入数字和位数
				final RadioGroup radioGroupLS=(RadioGroup) NumView.findViewById(R.id.radioGroupLS);
				final EditText etEditText=(EditText) NumView.findViewById(R.id.numEditText);
				
				RadioButton  radioL=(RadioButton) NumView.findViewById(R.id.radioL);
				RadioButton  radioS=(RadioButton) NumView.findViewById(R.id.radioS);
				RadioButton  radioQ=(RadioButton) NumView.findViewById(R.id.radioQ);
				if(radioGroupLS==null){return;}
				if(etEditText==null){return;}
				if(radioL==null){return;}
				if(radioS==null){return;}
				if(radioQ==null){return;}
//				final EditText etEditText = new EditText(getActivity());//提到前面就可以不用final
//				etEditText.setHint("输入范围0~999");
//				//限制输入长度为3
// 				etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
// 				//限制只能输入0~9的数字和L,S
 				/*etEditText.setKeyListener(new NumberKeyListener() {
				    @Override
				    protected char[] getAcceptedChars() {
 				        return new char[] {'1', '2', '3', '4', '5', '6', '7', '8','9', '0' };
 				    }
 				    @Override
 				    public int getInputType() {
				        return android.text.InputType.TYPE_CLASS_PHONE;//数字键盘
 				    }
 				});	*/
				radioGroupLS.setOnCheckedChangeListener(new OnCheckedChangeListener() {								
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId==R.id.radioQ) {
							etEditText.setHint("输入范围31~39");
							etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
						}else{
							etEditText.setHint("输入范围0~999");
							etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
						}
					}
				});
			
				final String numTextString=(String) mAppList.get(position).get(keyString[1]);
				if (numTextString.contains("L")) {
					etEditText.setText(numTextString.replace("L", ""));
					radioL.setChecked(true);
				}else if (numTextString.contains("S")) {
					etEditText.setText(numTextString.replace("S", ""));
					radioS.setChecked(true);
				}else if (numTextString.contains("Q")) {
					etEditText.setText(numTextString.replace("Q", ""));
					radioQ.setChecked(true);
				}
			    etEditText.setSelection(etEditText.getText().length());//设置光标位置	
				new AlertDialog.Builder(getActivity())
						.setTitle("请输入标号")
//						.setView(etEditText)
						.setView(NumView)
						.setPositiveButton(R.string.OK,//确定
								new DialogInterface.OnClickListener() {						
									@Override
									public void onClick(DialogInterface arg0,int arg1) {
										String editString=etEditText.getText().toString().trim();
										String LS="";
										if (editString.length()!=0) {
											if (radioGroupLS.getCheckedRadioButtonId()==R.id.radioL) {
												LS="L";
											}else if (radioGroupLS.getCheckedRadioButtonId()==R.id.radioS){
												LS="S";
											}else{
												LS="Q";
												int data = Integer.parseInt(editString);
												 if(data<=30||data>39){
													 Toast.makeText(getActivity(), "输入范围31~39", Toast.LENGTH_SHORT).show(); 
													 return;
												 }
											}
											editString=LS+addZeroForString(editString, 3);
										}
										if (!numTextString.equals(editString)) {
											checkNumnameList(NumList_name);
											if (NumList_name.contains(editString)){
												editString="";
												Toast.makeText(getActivity(), "与已有名称重名！请重新输入", Toast.LENGTH_SHORT).show();
												return;
											}
										}
											
										
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("addressText",
												mAppList.get(position)
														.get(keyString[0])
														.toString());
										map.put("numSpinner",editString);
										map.put("orderSpinner",
												mAppList.get(position)
														.get(keyString[2])
														.toString());
										map.put("operatText",
												mAppList.get(position)
														.get(keyString[3])
														.toString());
										map.put("noteEditText",
												mAppList.get(position)
														.get(keyString[4])
														.toString());
										mAppList.set(position, map);
										notifyDataSetChanged();
									}

								}).setNegativeButton(R.string.CANCEL, null)//取消
								.show();
			}catch(Exception e){
				e.printStackTrace();
			}
			}

		}

		class notelistener implements android.view.View.OnClickListener {

			private int position;

			// 构造函数
			notelistener(int pos) {
				position = pos;
			}

			@Override
			public void onClick(final View v) {
				//判断自动增加一行
				try{
				if (position==(mAppList.size()-1)) {
					addOneItem();	
					notifyDataSetChanged();
				}
				
				final EditText etEditText = new EditText(getActivity());
				etEditText.setHint("注释长度不大于4000个字符");
 				//限制输入长度为40
 				etEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4000)});
				String noteString=(String) mAppList.get(position).get(keyString[4]);
			    etEditText.setText(noteString);
			    etEditText.setSelection(noteString.length());//设置光标位置	
				new AlertDialog.Builder(getActivity())
						.setTitle("请添加注释")
						.setView(etEditText)
						.setPositiveButton(R.string.OK,//确定
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										String editString=etEditText.getText().toString().trim();
										//排除非法字符"/"
 										if (editString.contains("/")) {
 											editString=editString.replace("/", "\\");
 											Toast.makeText(getActivity(), "/为非法字符，已替换为\\", Toast.LENGTH_SHORT).show();
										}
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("addressText",
												mAppList.get(position)
														.get(keyString[0])
														.toString());//
										map.put("numSpinner",
												mAppList.get(position)
														.get(keyString[1])
														.toString());
										map.put("orderSpinner",
												mAppList.get(position)
														.get(keyString[2])
														.toString());
										map.put("operatText",
												mAppList.get(position)
														.get(keyString[3])
														.toString());
										map.put("noteEditText",editString);
										mAppList.set(position, map);
										notifyDataSetChanged();
									}

								}).setNegativeButton(R.string.CANCEL, null)//取消
								.show();
			}catch(Exception e){
				e.printStackTrace();
			}

			}

		}
		
		

		public  void addOneItem() {
			HashMap<String, Object> mapadd = new HashMap<String, Object>();
			mapadd.put("addressText",	String.format("%04d", mAppList.size()));//
			mapadd.put("numSpinner","");
			mapadd.put("orderSpinner","");
			mapadd.put("operatText","");
			mapadd.put("noteEditText","");
			mAppList.add(mAppList.size(),mapadd);
			
		}

		public void moveoperate(String oper,int position){
			final int qxposition=position;
			View orderView=View.inflate(getActivity(), R.layout.nc_order_move,null );//和move一样
			
			RadioGroup groupMOVE = (RadioGroup) orderView.findViewById(R.id.radioGroupMOVE);
			final RadioButton radioP_MOVE=(RadioButton) orderView.findViewById(R.id.radioP_MOVE);
			final RadioButton radioSP_MOVE=(RadioButton) orderView.findViewById(R.id.radioSP_MOVE);
			final RadioButton radioFP_MOVE=(RadioButton) orderView.findViewById(R.id.radioFP_MOVE);
	    	final Spinner spinnerP_MOVE=(Spinner)orderView.findViewById(R.id.spinnerP_MOVE);
	    	final Spinner spinnerSP_MOVE=(Spinner)orderView.findViewById(R.id.spinnerSP_MOVE);
	    	final Spinner spinnerFP_MOVE=(Spinner)orderView.findViewById(R.id.spinnerFP_MOVE);
	    	CheckBox checkbox1=(CheckBox)orderView.findViewById(R.id.checkBox1);
	    	CheckBox checkbox2=(CheckBox)orderView.findViewById(R.id.checkBox2);
	    	CheckBox checkbox3=(CheckBox)orderView.findViewById(R.id.checkBox4);
	    	CheckBox checkbox4=(CheckBox)orderView.findViewById(R.id.checkBox3);
	    	CheckBox checkbox5=(CheckBox)orderView.findViewById(R.id.checkBox5);
	    	if(groupMOVE==null){return;}
	    	if(radioP_MOVE==null){return;}
	    	if(radioSP_MOVE==null){return;}
	    	if(radioFP_MOVE==null){return;}
	    	if(spinnerP_MOVE==null){return;}
	    	if(spinnerSP_MOVE==null){return;}
	    	if(spinnerFP_MOVE==null){return;}
	    	if(checkbox1==null){return;}
	    	if(checkbox2==null){return;}
	    	if(checkbox3==null){return;}
	    	if(checkbox4==null){return;}
	    	if(checkbox5==null){return;}
	    	CheckBox[] checkboxs={checkbox1,checkbox2,checkbox3,checkbox4,checkbox5};
//	    	final ArrayAdapter<String> NullAdapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,NullList);
	    	final ArrayAdapter<String> PAdapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,PPositionList);	
	    	final ArrayAdapter<String> SPAdapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,SPositionList);
	    	final ArrayAdapter<String> FPAdapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,FPositionList);
	    	PAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	SPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	FPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	
	    	/**
	    	 * 再次点击不用再次输入,自动填入；
	    	 */
	    	String order_MOVE=mAppList.get(position).get("orderSpinner").toString();
	    	String operat_MOVE=mAppList.get(position).get("operatText").toString();
	    	if (order_MOVE.contains("MOVE")||order_MOVE.contains("MOVEP")) {
	    		if (operat_MOVE.contains("SP")) {
					radioSP_MOVE.setChecked(true);
					spinnerSP_MOVE.setAdapter(SPAdapter);
					operat_MOVE = operat_MOVE.trim();
					char[] chs=operat_MOVE.toCharArray();
					char ch=' ';
					for(int i=0;i<chs.length;i++){
						if(chs[i]=='A'){
							ch=chs[i+1];
							int index=(int)ch-49;
							checkboxs[index].setChecked(true);
						}
						if(chs[i]=='S'||chs[i]=='s'){
							operat_MOVE=operat_MOVE.substring(i);
							break;
						}
					}
					for(int m=0;m<SPositionList.size();m++){
						if(SPositionList.get(m).startsWith(operat_MOVE)){
							spinnerSP_MOVE.setSelection(m);
						}
					}
	    			//spinnerSP_MOVE.setSelection(SPositionList.indexOf(operat_MOVE));
				}else if (operat_MOVE.contains("FP")) {
					radioFP_MOVE.setChecked(true);
					spinnerFP_MOVE.setAdapter(FPAdapter);
					operat_MOVE = operat_MOVE.trim();
					char[] chs=operat_MOVE.toCharArray();
					char ch=' ';
					for(int i=0;i<chs.length;i++){
						if(chs[i]=='A'){
							ch=chs[i+1];
							int index=(int)ch-49;
							checkboxs[index].setChecked(true);
						}
						if(chs[i]=='F'||chs[i]=='f'){
							operat_MOVE=operat_MOVE.substring(i);
							break;
						}
					}
					for(int m=0;m<FPositionList.size();m++){
						if(FPositionList.get(m).startsWith(operat_MOVE)){
							spinnerFP_MOVE.setSelection(m);
						}
					}
	    			//spinnerFP_MOVE.setSelection(FPositionList.indexOf(operat_MOVE));
				}else if (operat_MOVE.contains("P")) {
					radioP_MOVE.setChecked(true);
					spinnerP_MOVE.setAdapter(PAdapter);
					operat_MOVE = operat_MOVE.trim();
					char[] chs=operat_MOVE.toCharArray();
					char ch=' ';
					for(int i=0;i<chs.length;i++){
						if(chs[i]=='A'){
							ch=chs[i+1];
							int index=(int)ch-49;
							checkboxs[index].setChecked(true);
						}
						if(chs[i]=='P'||chs[i]=='p'){
							operat_MOVE=operat_MOVE.substring(i);
							break;
						}
					}
					for(int m=0;m<PPositionList.size();m++){
						if(PPositionList.get(m).startsWith(operat_MOVE)){
							spinnerP_MOVE.setSelection(m);
						}
					}
	    			//spinnerP_MOVE.setSelection(PPositionList.indexOf(operat_MOVE));
				}										    													    		
			}else if (radioP_MOVE.isChecked()) {
				spinnerP_MOVE.setAdapter(PAdapter);
	    	}
	    	
	    	/**
	    	 * 空进入
	    	 */
	    	
	    	//默认选中radio0，载入arraylist
	    	/*if (radioP_MOVE.isChecked()) {
				spinnerP_MOVE.setAdapter(PAdapter);
	    	}*/	

	    	//设置单选监听
	    	groupMOVE.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					
					if (checkedId==R.id.radioP_MOVE) {
						System.out.println("选中P");
						spinnerP_MOVE.setAdapter(PAdapter);
						spinnerP_MOVE.setClickable(true);
//						spinnerSP_MOVE.setAdapter(NullAdapter);
//						spinnerFP_MOVE.setAdapter(NullAdapter);		
						spinnerSP_MOVE.setClickable(false);
						spinnerFP_MOVE.setClickable(false);
					}
					else if (checkedId==R.id.radioSP_MOVE) {
						System.out.println("选中SP");
						spinnerSP_MOVE.setAdapter(SPAdapter);
						spinnerSP_MOVE.setClickable(true);
						spinnerP_MOVE.setClickable(false);
						spinnerFP_MOVE.setClickable(false);
					}
					else {
						System.out.println("选中FP");
						spinnerFP_MOVE.setAdapter(FPAdapter);	
						spinnerFP_MOVE.setClickable(true);
						spinnerP_MOVE.setClickable(false);
						spinnerSP_MOVE.setClickable(false);
					}
				}
			});
	    	AlertDialog dialog2=new AlertDialog.Builder(getActivity())
			 .setTitle("轴移动指令("+oper+")")
			 .setView(orderView)
			 .setCancelable(false)//点击框外不消失
			 .setPositiveButton(R.string.OK,//确定 
					 new DetailedOrderListener4 (orderView,position,oper,NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
			 .setNegativeButton("取消", null).setNeutralButton("指令表", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) {
                	 selectoperate(qxposition);
                 }  
             }).show();
			 //.setContentView(R.layout.nc_listrowcontent);
           
			 WindowManager manager = getActivity().getWindowManager(); 
			 Display display = manager.getDefaultDisplay(); 
			 Rect picRect=new Rect();
			 display.getRectSize(picRect); 
			  
			 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
			 params.width = picRect.width()*4/5; 
			 params.height =picRect.height()*4/5 ; 
			 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

			//Toast.makeText(getActivity(),"添加指令" + oper,Toast.LENGTH_SHORT).show();
		}
		public void ifoperate(int position){
			final int qxposition=position;
			View orderView=View.inflate(getActivity(), R.layout.nc_order_if,null );
			//窗口IF命令中spinner绑定的List
					
			final RadioGroup radioGroupIF = (RadioGroup) orderView.findViewById(R.id.radioGroupIF);
			final RadioGroup GroupLogical_IF = (RadioGroup) orderView.findViewById(R.id.GroupLogical_IF);
			final RadioButton radioSET_IF=(RadioButton) orderView .findViewById(R.id.radioSET_IF);
			final RadioButton radioON_IF=(RadioButton) orderView .findViewById(R.id.radioON_IF);
			final RadioButton radioOFF_IF=(RadioButton) orderView .findViewById(R.id.radioOFF_IF);
	    	final Spinner spinner1_IF=(Spinner)orderView.findViewById(R.id.spinner1_IF);
	    	final Spinner spinner2_IF=(Spinner)orderView.findViewById(R.id.spinner2_IF);
	    	final Spinner spinner3_IF=(Spinner)orderView.findViewById(R.id.spinner3_IF);
	    	final EditText editSET_IF=(EditText) orderView.findViewById(R.id.editSET_IF);
	    	final TextView textView_IF=(TextView) orderView.findViewById(R.id.textView_IF);
	    	final TextView orderPreview_IF=(TextView) orderView.findViewById(R.id.orderPreview_IF);
	    	if(radioGroupIF==null){return;}
	    	if(GroupLogical_IF==null){return;}
	    	if(radioSET_IF==null){return;}
	    	if(radioON_IF==null){return;}
	    	if(radioOFF_IF==null){return;}
	    	if(spinner1_IF==null){return;}
	    	if(spinner2_IF==null){return;}
	    	if(spinner3_IF==null){return;}
	    	if(editSET_IF==null){return;}
	    	if(textView_IF==null){return;}
	    	if(orderPreview_IF==null){return;}
	    	radioSET_IF.setTextColor(Color.GRAY);
	    	
	    	ActualInput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,ActualInputList_symbol);
	    	ActualOutput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,ActualOutputList_symbol);
	    	MiddleInput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,MiddleInputList_symbol);
	    	MiddleOutput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,MiddleOutputList_symbol);
	    	DataInput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,DataInputList_symbol);
	    	DataOutput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,DataOutputList_symbol);
	    	Register_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,RegisterList_symbol);
	    	DataRegister_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,DataRegisterList_symbol);

	    	ActualInput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	ActualOutput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	MiddleInput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	      	MiddleOutput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	DataInput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	DataOutput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	Register_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	DataRegister_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	
	    	
	    	inPutAnswer_IF="OFF";//每次进入命令窗口，默认选中OFF，已经在xml中设置
	    	logic_IF=",";//每次进入命令窗口，默认选中AND
	    	orderPreviewString_IF="";//每次进入命令窗口，字符串清空，
	    	orderPreviewList_IF.clear();//每次进入命令窗口，字符串清空，
	    	orderPreviewList_IF.add(orderPreviewString_IF);//第一位不存放实际字符串
	    	
	    	/*orderPreview_IF.setOnClickListener(new android.view.View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});*/
	    	/**
	    	 * 再次点击不用再次输入,自动填入；
	    	 */
	    	String order_IF=mAppList.get(position).get("orderSpinner").toString().trim();
	    	String operat_IF=mAppList.get(position).get("operatText").toString().trim();
	    	if (order_IF.equals("IF")&&operat_IF.length()!=0) {										    	
	    		orderPreview_IF.setText(operat_IF);
	    		//orderPreview_IF.setSelection(operat_IF.length());//设置光标位置
	    		orderPreviewList_IF.clear();
	    		orderPreviewString_IF="";
	    		orderPreviewList_IF.add(orderPreviewString_IF);
	    		
	    		String[] orderPreviewArray1_IF= operat_IF.split(",");//先用逗号分隔
	    		for (int i = 0; i < orderPreviewArray1_IF.length; i++) {
	    			
					if (orderPreviewArray1_IF[i].contains("or")) {
						
						String[] orderPreviewArray2_IF= orderPreviewArray1_IF[i].split("or");//再用or分隔	
						if (i==0) {
							for (int k = 0; k < orderPreviewArray2_IF.length; k++) {
								if (k == 0) {
									orderPreviewString_IF +=orderPreviewArray2_IF[0];
									orderPreviewList_IF.add(orderPreviewString_IF);
								}else {
									orderPreviewString_IF +="or"+ orderPreviewArray2_IF[k];
									orderPreviewList_IF.add(orderPreviewString_IF);
								}
							}
						}else {
							for (int k = 0; k < orderPreviewArray2_IF.length; k++) {
								if (k == 0) {
									orderPreviewString_IF +=","+orderPreviewArray2_IF[0];
									orderPreviewList_IF.add(orderPreviewString_IF);
								}else {
									orderPreviewString_IF +="or"+ orderPreviewArray2_IF[k];
									orderPreviewList_IF.add(orderPreviewString_IF);
								}
							}	
						}
					}else {//将不包含or的存好
							if (i==0) {
								orderPreviewString_IF += orderPreviewArray1_IF[0];
								orderPreviewList_IF.add(orderPreviewString_IF);	
							}else {
								orderPreviewString_IF +=","+ orderPreviewArray1_IF[i];
								orderPreviewList_IF.add(orderPreviewString_IF);
							}	
					}	
				}		
			}
	    	
	    	/**
	    	 * 空进入
	    	 */

	    	//通过输入类型的选择来实时绑定相应的adapter
	    	spinner1_IF.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,View arg1,int pos, long id) {
					if (parent.getItemAtPosition(pos).toString().equals("实际输入")) {
						spinner2_IF.setAdapter(ActualInput_Adapter);
						List_signalname=ActualInputList_signal;
						radioON_IF.setClickable(true);//设置只有数据输入时才能激活radioSET_IN和spinner3_IN
						radioON_IF.setTextColor(Color.BLACK);
						radioOFF_IF.setChecked(true);
						radioOFF_IF.setClickable(true);
						radioOFF_IF.setTextColor(Color.BLACK);
						radioSET_IF.setClickable(false);
						radioSET_IF.setTextColor(Color.GRAY);
						spinner3_IF.setClickable(false);
						textView_IF.setTextColor(Color.GRAY);
					}else if (parent.getItemAtPosition(pos).toString().equals("实际输出")) {			
						spinner2_IF.setAdapter(ActualOutput_Adapter);
						List_signalname=ActualOutputList_signal;
						radioON_IF.setClickable(true);
						radioON_IF.setTextColor(Color.BLACK);
						radioOFF_IF.setChecked(true);
						radioOFF_IF.setClickable(true);
						radioOFF_IF.setTextColor(Color.BLACK);
						radioSET_IF.setClickable(false);
						radioSET_IF.setTextColor(Color.GRAY);
						spinner3_IF.setClickable(false);
						textView_IF.setTextColor(Color.GRAY);
					}else if (parent.getItemAtPosition(pos).toString().equals("中间输入")) {			
						spinner2_IF.setAdapter(MiddleInput_Adapter);
						List_signalname=MiddleInputList_signal;
						radioON_IF.setClickable(true);
						radioON_IF.setTextColor(Color.BLACK);
						radioOFF_IF.setChecked(true);
						radioOFF_IF.setClickable(true);
						radioOFF_IF.setTextColor(Color.BLACK);
						radioSET_IF.setClickable(false);
						radioSET_IF.setTextColor(Color.GRAY);
						spinner3_IF.setClickable(false);
						textView_IF.setTextColor(Color.GRAY);
					}else if (parent.getItemAtPosition(pos).toString().equals("中间输出")) {			
						spinner2_IF.setAdapter(MiddleOutput_Adapter);
						List_signalname=MiddleOutputList_signal;
						radioON_IF.setClickable(true);
						radioON_IF.setTextColor(Color.BLACK);
						radioOFF_IF.setChecked(true);
						radioOFF_IF.setClickable(true);
						radioOFF_IF.setTextColor(Color.BLACK);
						radioSET_IF.setClickable(false);
						radioSET_IF.setTextColor(Color.GRAY);
						spinner3_IF.setClickable(false);
						textView_IF.setTextColor(Color.GRAY);
					}else if (parent.getItemAtPosition(pos).toString().equals("数据输入")) {
						spinner2_IF.setAdapter(DataInput_Adapter);
						List_signalname=DataInputList_signal;
						radioON_IF.setClickable(false);
						radioON_IF.setTextColor(Color.GRAY);
						radioOFF_IF.setClickable(false);
						radioOFF_IF.setTextColor(Color.GRAY);
						radioSET_IF.setChecked(true);
//						radioSET_IF.setClickable(true);
						radioSET_IF.setTextColor(Color.BLACK);
						spinner3_IF.setClickable(true);
						textView_IF.setTextColor(Color.BLACK);
					}else if (parent.getItemAtPosition(pos).toString().equals("数据输出")) {
						spinner2_IF.setAdapter(DataOutput_Adapter);
						List_signalname=DataOutputList_signal;
						radioON_IF.setClickable(true);
						radioON_IF.setTextColor(Color.BLACK);
						radioOFF_IF.setChecked(true);
						radioOFF_IF.setClickable(true);
						radioOFF_IF.setTextColor(Color.BLACK);
						radioSET_IF.setClickable(false);
						radioSET_IF.setTextColor(Color.GRAY);
						spinner3_IF.setClickable(false);
						textView_IF.setTextColor(Color.GRAY);
					}else if (parent.getItemAtPosition(pos).toString().equals("掉电可保持寄存器")) {
						spinner2_IF.setAdapter(Register_Adapter);
						List_signalname=RegisterList_signal;
						radioON_IF.setClickable(true);
						radioON_IF.setTextColor(Color.BLACK);
						radioOFF_IF.setChecked(true);
						radioOFF_IF.setClickable(true);
						radioOFF_IF.setTextColor(Color.BLACK);
						radioSET_IF.setClickable(false);
						radioSET_IF.setTextColor(Color.GRAY);
						spinner3_IF.setClickable(false);
						textView_IF.setTextColor(Color.GRAY);
					}else if (parent.getItemAtPosition(pos).toString().equals("掉电可保持数据寄存器")) {
						spinner2_IF.setAdapter(DataRegister_Adapter);
						List_signalname=DataRegisterList_signal;
						radioON_IF.setClickable(true);
						radioON_IF.setTextColor(Color.BLACK);
						radioOFF_IF.setChecked(true);
						radioOFF_IF.setClickable(true);
						radioOFF_IF.setTextColor(Color.BLACK);
						radioSET_IF.setClickable(false);
						radioSET_IF.setTextColor(Color.GRAY);
						spinner3_IF.setClickable(false);
						textView_IF.setTextColor(Color.GRAY);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
	    	//在下面textview中显示选中项的名称
	    	final TextView signalname_IF=(TextView) orderView.findViewById(R.id.signalname_IF);
	    	if(signalname_IF==null){
	    		return;
	    	}
	    	spinner2_IF.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,View arg1,int pos, long id) {																												
					signalname_IF.setText(List_signalname.get(pos));
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub														
				}
			});
	    	
	    	

	    	editSET_IF.setFocusable(false);//edittext不可编辑
	    	radioGroupIF.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// 改变editSET的编辑状态			
					if (checkedId==R.id.radioSET_IF) {
						System.out.println("选中SET");
						editSET_IF.setFocusableInTouchMode(true);	
					}else {
						System.out.println("edittext不可编辑");
						editSET_IF.setFocusable(false);
					}
					//同时得到结果字符串
					inPutAnswer_IF=((RadioButton)group.findViewById(checkedId)).getText().toString();
					
				}
			});
	    	//得到逻辑字符串
	    	GroupLogical_IF.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (checkedId==R.id.radioAND_IF) {
						logic_IF=",";
					}else {
						logic_IF="or";
					}																																																							
				}
			});
	    	//增加输入和撤销输入功能
	    	Button buttonAdd_IF=(Button) orderView.findViewById(R.id.buttonAdd_IF);
	    	if(buttonAdd_IF==null){
	    		return;
	    	}
	    	Button buttonCancel_IF=(Button) orderView.findViewById(R.id.buttonCancel_IF);
	    	if(buttonCancel_IF==null){
	    		return;
	    	}

	    	
	    	buttonAdd_IF.setOnClickListener(new android.view.View.OnClickListener() {//增加输入													
				@Override	
				public void onClick(View v) {
					if (spinner2_IF.getSelectedItem()!=null) {
						inPutSymbolName_IF=spinner2_IF.getSelectedItem().toString().trim();
					}
					if (radioSET_IF.isChecked()) {
						inPutAnswer_IF=spinner3_IF.getSelectedItem().toString().toLowerCase().trim()+editSET_IF.getText().toString().trim();
					}
					
					if (orderPreviewList_IF.size()==1) {//已经没有命令了
						orderPreviewString_IF=inPutSymbolName_IF+" "+inPutAnswer_IF;
						orderPreviewList_IF.add(orderPreviewString_IF);
						System.out.println("     1");
					}else {
						System.out.println("add "+orderPreviewList_IF.size());
						orderPreviewString_IF+=" "+logic_IF+" "+inPutSymbolName_IF+" "+inPutAnswer_IF;
						orderPreviewList_IF.add(orderPreviewString_IF);
					}														
					//在指令预览中显示已编辑命令
					orderPreview_IF.setText(orderPreviewString_IF);
					
				}	
			});
	    	
	    	buttonCancel_IF.setOnClickListener(new android.view.View.OnClickListener() {//撤销输入	
				@Override
				public void onClick(View v) {
					if (orderPreviewList_IF.size()==1) {
						orderPreview_IF.setText("");
						System.out.println("     1");
					}else {
						System.out.println("cancel "+orderPreviewList_IF.size());
						orderPreviewList_IF.remove(orderPreviewList_IF.size()-1);
						orderPreviewString_IF=orderPreviewList_IF.get(orderPreviewList_IF.size()-1);
						orderPreview_IF.setText(orderPreviewString_IF);
					}
					
				}});
	    	
	    	 AlertDialog dialog2=new AlertDialog.Builder(getActivity())
			 .setTitle("条件指令(IF)")
			 .setView(orderView)
			 .setCancelable(false)//点击框外不消失
			 .setPositiveButton(R.string.OK,//确定 
					 new DetailedOrderListener4 (orderView,position,"IF",NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
			 .setNegativeButton("取消", null).setNeutralButton("指令表", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) {
                	 selectoperate(qxposition);
                 }  
             }).show();
			 //.setContentView(R.layout.nc_listrowcontent);
            
			 WindowManager manager = getActivity().getWindowManager(); 
			 Display display = manager.getDefaultDisplay(); 
			 Rect picRect=new Rect();
			 display.getRectSize(picRect); 
			  
			 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
			 params.width = picRect.width()*4/5; 
			 params.height =picRect.height()*4/5 ; 
			 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

			//Toast.makeText(getActivity(),"添加指令IF",Toast.LENGTH_SHORT).show();
		}

		public void outoperate(int position) {
			final int qxposition=position;
			View orderView=View.inflate(getActivity(), R.layout.nc_order_out,null );

			RadioGroup groupOUT = (RadioGroup) orderView.findViewById(R.id.radioGroupOUT);
	    	final Spinner spinner1_OUT=(Spinner)orderView.findViewById(R.id.spinner1_OUT);
	    	final Spinner spinner2_OUT=(Spinner)orderView.findViewById(R.id.spinner2_OUT);
	    	final RadioButton radioON_OUT=(RadioButton) orderView.findViewById(R.id.radioON_OUT);
	    	final RadioButton radioOFF_OUT=(RadioButton) orderView.findViewById(R.id.radioOFF_OUT);
	    	final RadioButton radioSET_OUT=(RadioButton) orderView.findViewById(R.id.radioSET_OUT);
	    	final EditText editSET_OUT=(EditText) orderView.findViewById(R.id.editSet_OUT);
	    	final TextView orderPreview_OUT=(TextView) orderView.findViewById(R.id.orderPreview_OUT);
	    	if(groupOUT==null){return;}
	    	if(spinner1_OUT==null){return;}
	    	if(spinner2_OUT==null){return;}
	    	if(radioON_OUT==null){return;}
	    	if(radioOFF_OUT==null){return;}
	    	if(radioSET_OUT==null){return;}
	    	if(editSET_OUT==null){return;}
	    	if(orderPreview_OUT==null){return;}
	    	
	    	ActualOutput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,ActualOutputList_symbol);
	    	MiddleOutput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,MiddleOutputList_symbol);
	    	DataOutput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,DataOutputList_symbol);
	    	Register_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,RegisterList_symbol);
	    	DataRegister_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,DataRegisterList_symbol);
	    	
	    	
	    	
	    	ActualOutput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	MiddleOutput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	DataOutput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	Register_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	DataRegister_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	
	    	radioSET_OUT.setTextColor(Color.GRAY);
	    	
	    	outPutAnswer="OFF";//每次进入命令窗口，默认选中OFF，已经在xml中设置
	    	orderPreviewString_OUT="";//每次进入命令窗口，字符串清空，
	    	orderPreviewList_OUT.clear();//每次进入命令窗口，字符串清空，
	    	orderPreviewList_OUT.add(orderPreviewString_OUT);//第一位不存放实际字符串
	    	editSET_OUT.setFocusable(false);

	    	/**
	    	 * 再次点击不用再次输入,自动填入；
	    	 */
	    	String order_OUT=mAppList.get(position).get("orderSpinner").toString().trim();
	    	String operat_OUT=mAppList.get(position).get("operatText").toString().trim();
	    	if (order_OUT.equals("OUT")&&operat_OUT.length()!=0) {	
	    		orderPreview_OUT.setText(operat_OUT);
	    		orderPreviewList_OUT.clear();
	    		orderPreviewString_OUT="";
	    		orderPreviewList_OUT.add(orderPreviewString_OUT);
	    		
	    		String[] orderPreviewArray1_OUT= operat_OUT.split(",");//用逗号分隔
	    		for (int i = 0; i < orderPreviewArray1_OUT.length; i++) {
						if (i==0) {
							orderPreviewString_OUT +=orderPreviewArray1_OUT[0];
							orderPreviewList_OUT.add(orderPreviewString_OUT);
						}else {
							orderPreviewString_OUT +=","+orderPreviewArray1_OUT[i];
							orderPreviewList_OUT.add(orderPreviewString_OUT);
						}
				}		
			}
	    	
	    	/**
	    	 * 空进入
	    	 */
	    	
	    	
	    	spinner1_OUT.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,View arg1,int pos, long id) {
					if (parent.getItemAtPosition(pos).toString().trim().equals("实际输出")) {
						spinner2_OUT.setAdapter(ActualOutput_Adapter);
						List_signalname=ActualOutputList_signal;
						radioON_OUT.setClickable(true);//设置只有数据输入时才能激活radioSET_IN
						radioON_OUT.setTextColor(Color.BLACK);
						radioOFF_OUT.setChecked(true);
						radioOFF_OUT.setClickable(true);
						radioOFF_OUT.setTextColor(Color.BLACK);
						radioSET_OUT.setClickable(false);	
						radioSET_OUT.setTextColor(Color.GRAY);
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("中间输出")) {
						spinner2_OUT.setAdapter(MiddleOutput_Adapter);
						List_signalname=MiddleOutputList_signal;
						radioON_OUT.setClickable(true);
						radioON_OUT.setTextColor(Color.BLACK);
						radioOFF_OUT.setClickable(true);
						radioOFF_OUT.setTextColor(Color.BLACK);
						radioSET_OUT.setClickable(false);
						radioSET_OUT.setTextColor(Color.GRAY);
						radioOFF_OUT.setChecked(true);
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("数据输出")) {
						spinner2_OUT.setAdapter(DataOutput_Adapter);
						List_signalname=DataOutputList_signal;
						radioON_OUT.setClickable(false);
						radioON_OUT.setTextColor(Color.GRAY);
						radioOFF_OUT.setClickable(false);
						radioOFF_OUT.setTextColor(Color.GRAY);
//						radioSET_OUT.setClickable(true);
						radioSET_OUT.setChecked(true);
						radioSET_OUT.setTextColor(Color.BLACK);
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("掉电可保持寄存器")) {
						spinner2_OUT.setAdapter(Register_Adapter);
						List_signalname=RegisterList_signal;
						radioON_OUT.setClickable(true);
						radioON_OUT.setTextColor(Color.BLACK);
						radioOFF_OUT.setClickable(true);
						radioOFF_OUT.setTextColor(Color.BLACK);
						radioSET_OUT.setClickable(false);
						radioSET_OUT.setTextColor(Color.GRAY);
						radioOFF_OUT.setChecked(true);
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("掉电可保持数据寄存器")) {
						spinner2_OUT.setAdapter(DataRegister_Adapter);
						List_signalname=DataRegisterList_signal;
						radioON_OUT.setClickable(true);
						radioON_OUT.setTextColor(Color.BLACK);
						radioOFF_OUT.setClickable(true);
						radioOFF_OUT.setTextColor(Color.BLACK);
						radioSET_OUT.setClickable(false);
						radioSET_OUT.setTextColor(Color.GRAY);
						radioOFF_OUT.setChecked(true);
					}	
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	    	
	    	//在下面textview中显示选中项的名称
	    	final TextView signalname_OUT=(TextView) orderView.findViewById(R.id.signalname_OUT);
	    	if(signalname_OUT==null){return;}
	    	spinner2_OUT.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,View arg1,int pos, long id) {																												
					signalname_OUT.setText(List_signalname.get(pos));
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub														
				}
			});
	    	
	    	

	    	groupOUT.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					
					if (checkedId==R.id.radioSET_OUT) {
						System.out.println("选中SET");
						editSET_OUT.setFocusableInTouchMode(true);
					}else {
						System.out.println("edittext不可编辑");
						editSET_OUT.setFocusable(false);
					}
					//同时得到结果字符串
					outPutAnswer=((RadioButton)group.findViewById(checkedId)).getText().toString().trim();
					
					
				}
			});
			
	    	//增加输入和撤销输入功能
	    	Button buttonAdd_OUT=(Button) orderView.findViewById(R.id.buttonAdd_OUT);
	    	Button buttonCancel_OUT=(Button) orderView.findViewById(R.id.buttonCancel_OUT);
	        if(buttonAdd_OUT==null){return;}
	        if(buttonCancel_OUT==null){return;}
	    	buttonAdd_OUT.setOnClickListener(new android.view.View.OnClickListener() {//增加输入													
				@Override
				public void onClick(View v) {
					if (spinner2_OUT.getSelectedItem()!=null) {
						outPutSymbolName=spinner2_OUT.getSelectedItem().toString().trim();
					}
					if (radioSET_OUT.isChecked()) {
						outPutAnswer="="+editSET_OUT.getText().toString().trim();
					}
					
					if (orderPreviewList_OUT.size()==1) {//已经没有命令了
						orderPreviewString_OUT=outPutSymbolName+" "+outPutAnswer;
						orderPreviewList_OUT.add(orderPreviewString_OUT);
					}else {
						orderPreviewString_OUT+=","+outPutSymbolName+" "+outPutAnswer;
						orderPreviewList_OUT.add(orderPreviewString_OUT);
					}														
					//在指令预览中显示已编辑命令
					orderPreview_OUT.setText(orderPreviewString_OUT);
					
				}
			});
	    	
	    	buttonCancel_OUT.setOnClickListener(new android.view.View.OnClickListener() {//撤销输入	
				@Override												
				public void onClick(View v) {
					if ((orderPreviewList_OUT.size()-1)==0) {
						orderPreview_OUT.setText("");
					}else {
						orderPreviewList_OUT.remove(orderPreviewList_OUT.size()-1);
						orderPreviewString_OUT=orderPreviewList_OUT.get(orderPreviewList_OUT.size()-1);
						orderPreview_OUT.setText(orderPreviewString_OUT);
					}
					
				}});
	    	AlertDialog dialog2=new AlertDialog.Builder(getActivity())
			 .setTitle("信号输出指令(OUT)")
			 .setView(orderView)
			 .setCancelable(false)//点击框外不消失
			 .setPositiveButton(R.string.OK,//确定 
					 new DetailedOrderListener4 (orderView,position,"OUT",NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
			 .setNegativeButton("取消", null).setNeutralButton("指令表", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) {
                	 selectoperate(qxposition);
                 }  
             }).show();
			 //.setContentView(R.layout.nc_listrowcontent);
           
			 WindowManager manager = getActivity().getWindowManager(); 
			 Display display = manager.getDefaultDisplay(); 
			 Rect picRect=new Rect();
			 display.getRectSize(picRect); 
			  
			 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
			 params.width = picRect.width()*4/5; 
			 params.height =picRect.height()*4/5 ; 
			 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

			//Toast.makeText(getActivity(),"添加指令OUT",Toast.LENGTH_SHORT).show();
	   }
	    public void waitoperate(int position){
	    	final int qxposition=position;
	    	View orderView=View.inflate(getActivity(), R.layout.nc_order_wait,null );
			RadioGroup radioGroup_WAIT = (RadioGroup) orderView.findViewById(R.id.radioGroup_WAIT);
			final EditText editText_WAIT=(EditText) orderView.findViewById(R.id.editText_WAIT);
			final Spinner spinner_WAIT=(Spinner) orderView.findViewById(R.id.spinner_WAIT);
			RadioButton radiofixed_WAIT=(RadioButton) orderView.findViewById(R.id.radiofixed_WAIT);
	    	RadioButton radioset_WAIT=(RadioButton) orderView.findViewById(R.id.radioset_WAIT);
	    	RadioButton radiopos_WAIT=(RadioButton) orderView.findViewById(R.id.radiopos_WAIT);
	    	if(radioGroup_WAIT==null){return;}
	    	if(editText_WAIT==null){return;}
	    	if(spinner_WAIT==null){return;}
	    	if(radiofixed_WAIT==null){return;}
	    	if(radioset_WAIT==null){return;}
	    	if(radiopos_WAIT==null){return;}
	    	editText_WAIT.setFocusable(false);
	    	radioset_WAIT.setChecked(true);
		/*	symbolList_WAIT.clear();
			for (int i = 0; i < ArrayListBound.getDeviceTimerListData().size(); i++) {
				String symbolString_WAIT= ArrayListBound.getDeviceTimerListData().get(i).get("symbolNameEditText").toString().trim();
				if (symbolString_WAIT!=""&&ArrayListBound.getDeviceTimerListData().get(i).get("setItem").toString().trim().equals("1")) {
					symbolList_WAIT.add(symbolString_WAIT);
				}
			}	*/
			
			WAIT_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,symbolList_WAIT);
			WAIT_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_WAIT.setAdapter(WAIT_Adapter);
			
			/**
	    	 * 再次点击不用再次输入,自动填入；
	    	 */
	    	String order_WAIT=mAppList.get(position).get("orderSpinner").toString().trim();
	    	String operat_WAIT=mAppList.get(position).get("operatText").toString().trim();
	    	if (order_WAIT.contains("WAIT")) {
	    		if (operat_WAIT.contains("s")) {
	    			radiofixed_WAIT.setChecked(true);
	    			editText_WAIT.setFocusableInTouchMode(true);
	    			editText_WAIT.setText(operat_WAIT.replace("s", ""));
	    			//editText_WAIT.setSelection(operat_WAIT.length());//设置光标位置	
				}else if (operat_WAIT.contains("T")) {
					radioset_WAIT.setChecked(true);
					for(int m=0;m<symbolList_WAIT.size();m++){
						if(symbolList_WAIT.get(m).startsWith(operat_WAIT)){
							spinner_WAIT.setSelection(m);	
						}
					}
					//spinner_WAIT.setSelection(symbolList_WAIT.indexOf(operat_WAIT));	
				}else {
					radiopos_WAIT.setChecked(true);
				}
			}
			/**
			 * 空进入
			 */
			radioGroup_WAIT.setOnCheckedChangeListener(new OnCheckedChangeListener() {	
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (checkedId==R.id.radiofixed_WAIT) {
						editText_WAIT.setFocusableInTouchMode(true);
						spinner_WAIT.setClickable(false);
					}else if (checkedId==R.id.radioset_WAIT) {
						editText_WAIT.setFocusable(false);
						spinner_WAIT.setClickable(true);
					}else {
						editText_WAIT.setFocusable(false);
						spinner_WAIT.setClickable(false);
					} 										
				}
			});	
			AlertDialog dialog2=new AlertDialog.Builder(getActivity())
			 .setTitle("等待指令(WAIT)")
			 .setView(orderView)
			 .setCancelable(false)//点击框外不消失
			 .setPositiveButton(R.string.OK,//确定 
					 new DetailedOrderListener4 (orderView,position,"WAIT",NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
			 .setNegativeButton("取消", null).setNeutralButton("指令表", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) {
                	 selectoperate(qxposition);
                 }  
             }).show();
			 //.setContentView(R.layout.nc_listrowcontent);
          
			 WindowManager manager = getActivity().getWindowManager(); 
			 Display display = manager.getDefaultDisplay(); 
			 Rect picRect=new Rect();
			 display.getRectSize(picRect); 
			  
			 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
			 params.width = picRect.width()*4/5; 
			 params.height =picRect.height()*4/5 ; 
			 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

			//Toast.makeText(getActivity(),"添加指令WAIT",Toast.LENGTH_SHORT).show();
	    }
	    public void ifthenoperate(int position){
	    	final int qxposition=position;
	    	View orderView=View.inflate(getActivity(), R.layout.nc_order_ifthen,null );
			final Spinner spinner1_IFThen=(Spinner) orderView.findViewById(R.id.spinner1_IFThen);
			final Spinner spinner2_IFThen=(Spinner) orderView.findViewById(R.id.spinner2_IFThen);
			final Spinner spinner3_IFThen=(Spinner) orderView.findViewById(R.id.spinner3_IFThen);
			if(spinner1_IFThen==null){return;}
			if(spinner2_IFThen==null){return;}
			if(spinner3_IFThen==null){return;}
			
			inPutAnswer_IFThen="OFF";//每次进入命令窗口，默认选中OFF，已经在xml中设置
			
			ActualInput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,ActualInputList_symbol);
	    	ActualOutput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,ActualOutputList_symbol);
	    	MiddleInput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,MiddleInputList_symbol);
	    	MiddleOutput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,MiddleOutputList_symbol);
	    	DataInput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,DataInputList_symbol);
	    	DataOutput_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,DataOutputList_symbol);
	    	Register_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,RegisterList_symbol);
	    	DataRegister_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,DataRegisterList_symbol);

	    	ActualInput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	ActualOutput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	MiddleInput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	      	MiddleOutput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	DataInput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	DataOutput_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	Register_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	DataRegister_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    	
			final RadioButton radioON_IFThen=(RadioButton) orderView .findViewById(R.id.radioON_IFThen);
			final RadioButton radioOFF_IFThen=(RadioButton) orderView .findViewById(R.id.radioOFF_IFThen);	
			final RadioButton radioSET_IFThen=(RadioButton) orderView .findViewById(R.id.radioSET_IFThen);	
	    	final EditText editSET_IFThen=(EditText) orderView.findViewById(R.id.editSET_IFThen);
//	    	final RadioButton radioAND_IFThen=(RadioButton) orderView .findViewById(R.id.radioAND_IFThen);	
//	    	final RadioButton radioOR_IFThen=(RadioButton) orderView .findViewById(R.id.radioOR_IFThen);	
	    	final RadioButton radioGOTO_IFThen=(RadioButton) orderView .findViewById(R.id.radioGOTO_IFThen);	
//	    	final RadioButton radioCALL_IFThen=(RadioButton) orderView .findViewById(R.id.radioCALL_IFThen);
	    	final TextView orderPreview_IFThen=(TextView) orderView.findViewById(R.id.orderPreview_IFThen);
	    	if(radioON_IFThen==null){return;}
	    	if(radioOFF_IFThen==null){return;}
	    	if(radioSET_IFThen==null){return;}
	    	if(editSET_IFThen==null){return;}
	    	if(radioGOTO_IFThen==null){return;}
	    	if(orderPreview_IFThen==null){return;}
	    	spinner2_IFThen.setAdapter(ActualInput_Adapter);
	    	editSET_IFThen.setFocusable(false);
	    	radioSET_IFThen.setTextColor(Color.GRAY);
	    	final RadioGroup radioGroupLogical_IFThen=(RadioGroup) orderView.findViewById(R.id.radioGroupLogical_IFThen);
	    	if(radioGroupLogical_IFThen==null){return;}
	    	logic_IFThen=",";//每次进入命令窗口，默认选中AND
	    	
	    	orderPreviewString_IFThen="";//每次进入命令窗口，字符串清空，
	    	orderPreviewList_IFThen.clear();//每次进入命令窗口，字符串清空，
	    	orderPreviewList_IFThen.add(orderPreviewString_IFThen);//第一位不存放实际字符串
	    	
	    	/**
	    	 * 再次点击不用再次输入,自动填入；
	    	 */
	    	String order_IFThen=mAppList.get(position).get("orderSpinner").toString().trim();
	    	String operat_IFThen=mAppList.get(position).get("operatText").toString().trim();
	    	if (order_IFThen.equals("IFThen")&&operat_IFThen.length()!=0) {
	    		orderPreview_IFThen.setText(operat_IFThen);
	    		
	    		boolean GOTO=true;;//判断是goto还是call
	    		
	    		if (operat_IFThen.contains("CALL")) {
	    			GOTO=false;
				}else if(operat_IFThen.contains("GOTO")) {
					GOTO=true;
				}else {
				}

	    		orderPreviewList_IFThen.clear();
	    		orderPreviewString_IFThen="";
	    		orderPreviewList_IFThen.add(orderPreviewString_IFThen);
	    		
	    		String[] orderPreviewArray1_IFThen= operat_IFThen.split(",");//先用逗号分隔
	    		for (int i = 0; i < orderPreviewArray1_IFThen.length; i++) {
	    			if (orderPreviewArray1_IFThen[i].contains("or")) {
	    				
	    				String[] orderPreviewArray2_IFThen= orderPreviewArray1_IFThen[i].split("or");//再用or分隔	
						if (i==0) {
							for (int k = 0; k < orderPreviewArray2_IFThen.length; k++) {
								if (k == 0) {
									orderPreviewString_IFThen +=orderPreviewArray2_IFThen[0];
									orderPreviewList_IFThen.add(orderPreviewString_IFThen);
								}else {
									orderPreviewString_IFThen +="or"+ orderPreviewArray2_IFThen[k];
									orderPreviewList_IFThen.add(orderPreviewString_IFThen);
								}
							}
						}else {
							for (int k = 0; k < orderPreviewArray2_IFThen.length; k++) {
								if (k == 0) {
									orderPreviewString_IFThen +=","+orderPreviewArray2_IFThen[0];
									orderPreviewList_IFThen.add(orderPreviewString_IFThen);
								}else {
									orderPreviewString_IFThen +="or"+ orderPreviewArray2_IFThen[k];
									orderPreviewList_IFThen.add(orderPreviewString_IFThen);
								}
							}	
						}	
	    			}else {//将不包含or的存好,还要判断最后一个跳转的
	    				if (i==0) {
	    					orderPreviewString_IFThen += orderPreviewArray1_IFThen[0];
	    					orderPreviewList_IFThen.add(orderPreviewString_IFThen);	
						}else if (i==orderPreviewArray1_IFThen.length-1) {
							if (GOTO) {
								orderPreviewString_IFThen += ","+orderPreviewArray1_IFThen[i];
		    					orderPreviewList_IFThen.add(orderPreviewString_IFThen);	
							}else {
								orderPreviewString_IFThen += ","+orderPreviewArray1_IFThen[i];
		    					orderPreviewList_IFThen.add(orderPreviewString_IFThen);
							}
						}
	    				else {
							orderPreviewString_IFThen +=","+ orderPreviewArray1_IFThen[i];
							orderPreviewList_IFThen.add(orderPreviewString_IFThen);
						}	
	    				
	    				
	    				
	    				
					}
	    		}		
			}
	    	
	    	/**
	    	 * 空进入
	    	 */

	    	
	    	
	    	spinner1_IFThen.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,View arg1,int pos, long id) {
					if (parent.getItemAtPosition(pos).toString().trim().equals("实际输入")) {
						spinner2_IFThen.setAdapter(ActualInput_Adapter);
						List_signalname=ActualInputList_signal;
						radioON_IFThen.setClickable(true);//设置只有数据输入时才能激活radioSET_IN和spinner3_IN
						radioON_IFThen.setTextColor(Color.BLACK);
						radioOFF_IFThen.setChecked(true);//默认选中OFF
						radioOFF_IFThen.setClickable(true);
						radioOFF_IFThen.setTextColor(Color.BLACK);
						radioSET_IFThen.setClickable(false);
						radioSET_IFThen.setTextColor(Color.GRAY);
						spinner3_IFThen.setClickable(false);
						editSET_IFThen.setFocusable(false);						
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("实际输出")) {
						spinner2_IFThen.setAdapter(ActualOutput_Adapter);
						List_signalname=ActualOutputList_signal;
						radioON_IFThen.setClickable(true);
						radioON_IFThen.setTextColor(Color.BLACK);
						radioOFF_IFThen.setChecked(true);
						radioOFF_IFThen.setClickable(true);
						radioOFF_IFThen.setTextColor(Color.BLACK);
						radioSET_IFThen.setClickable(false);
						radioSET_IFThen.setTextColor(Color.GRAY);
						spinner3_IFThen.setClickable(false);
						editSET_IFThen.setFocusable(false);
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("中间输入")) {
						spinner2_IFThen.setAdapter(MiddleInput_Adapter);
						List_signalname=MiddleInputList_signal;
						radioON_IFThen.setClickable(true);
						radioON_IFThen.setTextColor(Color.BLACK);
						radioOFF_IFThen.setChecked(true);
						radioOFF_IFThen.setClickable(true);
						radioOFF_IFThen.setTextColor(Color.BLACK);
						radioSET_IFThen.setClickable(false);
						radioSET_IFThen.setTextColor(Color.GRAY);
						spinner3_IFThen.setClickable(false);
						editSET_IFThen.setFocusable(false);
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("中间输出")) {
						spinner2_IFThen.setAdapter(MiddleOutput_Adapter);
						List_signalname=MiddleOutputList_signal;
						radioON_IFThen.setClickable(true);
						radioON_IFThen.setTextColor(Color.BLACK);
						radioOFF_IFThen.setChecked(true);
						radioOFF_IFThen.setClickable(true);
						radioOFF_IFThen.setTextColor(Color.BLACK);
						radioSET_IFThen.setClickable(false);
						radioSET_IFThen.setTextColor(Color.GRAY);
						spinner3_IFThen.setClickable(false);
						editSET_IFThen.setFocusable(false);
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("数据输入")) {
						spinner2_IFThen.setAdapter(DataInput_Adapter);
						List_signalname=DataInputList_signal;
						radioON_IFThen.setClickable(false);
						radioON_IFThen.setTextColor(Color.GRAY);
						radioOFF_IFThen.setClickable(false);
						radioOFF_IFThen.setTextColor(Color.GRAY);
						radioSET_IFThen.setChecked(true);
						radioSET_IFThen.setTextColor(Color.BLACK);
						spinner3_IFThen.setClickable(true);
						editSET_IFThen.setFocusableInTouchMode(true);	
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("数据输出")) {
						spinner2_IFThen.setAdapter(DataOutput_Adapter);
						List_signalname=DataOutputList_signal;
						radioON_IFThen.setClickable(true);
						radioON_IFThen.setTextColor(Color.BLACK);
						radioOFF_IFThen.setChecked(true);
						radioOFF_IFThen.setClickable(true);
						radioOFF_IFThen.setTextColor(Color.BLACK);
						radioSET_IFThen.setClickable(false);
						radioSET_IFThen.setTextColor(Color.GRAY);
						spinner3_IFThen.setClickable(false);
						editSET_IFThen.setFocusable(false);	
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("掉电可保持寄存器")) {
						spinner2_IFThen.setAdapter(Register_Adapter);
						List_signalname=RegisterList_signal;
						radioON_IFThen.setClickable(true);
						radioON_IFThen.setTextColor(Color.BLACK);
						radioOFF_IFThen.setChecked(true);
						radioOFF_IFThen.setClickable(true);
						radioOFF_IFThen.setTextColor(Color.BLACK);
						radioSET_IFThen.setClickable(false);
						radioSET_IFThen.setTextColor(Color.GRAY);
						spinner3_IFThen.setClickable(false);
						editSET_IFThen.setFocusable(false);	
					}else if (parent.getItemAtPosition(pos).toString().trim().equals("掉电可保持数据寄存器")) {
						spinner2_IFThen.setAdapter(DataRegister_Adapter);
						List_signalname=DataRegisterList_signal;
						radioON_IFThen.setClickable(true);
						radioON_IFThen.setTextColor(Color.BLACK);
						radioOFF_IFThen.setChecked(true);
						radioOFF_IFThen.setClickable(true);
						radioOFF_IFThen.setTextColor(Color.BLACK);
						radioSET_IFThen.setClickable(false);
						radioSET_IFThen.setTextColor(Color.GRAY);
						spinner3_IFThen.setClickable(false);
						editSET_IFThen.setFocusable(false);	
					}																	
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	    	
	    	//在下面textview中显示符号名和注释中的信号名，同时得到选中项字符串
	    	final TextView signalname_IFThen=(TextView) orderView.findViewById(R.id.signalname_IFThen);
	    	if(signalname_IFThen==null){return;}
	    	spinner2_IFThen.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,View arg1,int pos, long id) {																												
					signalname_IFThen.setText(List_signalname.get(pos));
					if (parent.getSelectedItem()!=null) {
						inPutSymbolName_IFThen=parent.getSelectedItem().toString().trim();
					}
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub														
				}
			});
	    	//得到ON OFF字符，SET关系式
	    	RadioGroup radioGroup1_IFThen=(RadioGroup) orderView.findViewById(R.id.radioGroup1_IFThen);
	    	if(radioGroup1_IFThen==null){return;}
	    	radioGroup1_IFThen.setOnCheckedChangeListener(new OnCheckedChangeListener() {	
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (checkedId==R.id.radioON_IFThen) {
						inPutAnswer_IFThen="ON";	
					}else if (checkedId==R.id.radioOFF_IFThen) {
						inPutAnswer_IFThen="OFF";	
					}//SET关系式在点击增加按钮时判定												
				}
			});
	    	//得到逻辑字符串
			
			radioGroupLogical_IFThen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {																					
						if (checkedId==R.id.radioAND_IFThen) {//AND用逗号代替
							logic_IFThen=",";
						}else {
							logic_IFThen="or";
						}
				}
			});
	    	//跳转选择
			final RadioGroup radioGroupSEQ_IFThen=(RadioGroup) orderView.findViewById(R.id.radioGroupSEQ_IFThen);
	    	final Spinner spinnerGOTO_IFThen=(Spinner) orderView.findViewById(R.id.spinnerGOTO_IFThen);
			final Spinner spinnerCALL_IFThen=(Spinner) orderView.findViewById(R.id.spinnerCALL_IFThen);
			if(radioGroupSEQ_IFThen==null){return;}
			if(spinnerGOTO_IFThen==null){return;}
			if(spinnerCALL_IFThen==null){return;}
			Fragments_NCedit1.updateSEQList(GOTOList,CALLList);
	    	GOTO_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,GOTOList);
			CALL_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,CALLList);
			GOTO_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			CALL_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			if (radioGOTO_IFThen.isChecked()) {
				spinnerGOTO_IFThen.setAdapter(GOTO_Adapter);
				spinnerCALL_IFThen.setClickable(false);
			}
			radioGroupSEQ_IFThen.setOnCheckedChangeListener(new OnCheckedChangeListener() {													
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (checkedId==R.id.radioGOTO_IFThen) {
						spinnerGOTO_IFThen.setAdapter(GOTO_Adapter);
						spinnerGOTO_IFThen.setClickable(true);
						spinnerCALL_IFThen.setClickable(false);
					}else if (checkedId==R.id.radioCALL_IFThen) {
						spinnerCALL_IFThen.setAdapter(CALL_Adapter);
						spinnerCALL_IFThen.setClickable(true);
						spinnerGOTO_IFThen.setClickable(false);
					}	
				}
			});	
			
	    	//增加输入和撤销输入功能
			final Button buttonADD_IFThen=(Button) orderView.findViewById(R.id.buttonADD_IFThen);
			final Button buttonCANCEL_IFThen=(Button) orderView.findViewById(R.id.buttonCANCEL_IFThen);
			if(buttonADD_IFThen==null){return;}
			if(buttonCANCEL_IFThen==null){return;}
	    	buttonADD_IFThen.setOnClickListener(new android.view.View.OnClickListener() {													
				@Override
				public void onClick(View v) {
					if (orderPreviewString_IFThen.contains("GOTO")||orderPreviewString_IFThen.contains("CALL")) {//限定增加跳转后不能再添加命令
					}
					else {
						if (radioSET_IFThen.isChecked()) {
							inPutAnswer_IFThen=spinner3_IFThen.getSelectedItem().toString().toLowerCase().trim()+" "+editSET_IFThen.getText().toString().trim();
						}
						
						if (orderPreviewList_IFThen.size()==1) {//已经没有命令了
							orderPreviewString_IFThen=inPutSymbolName_IFThen+" "+inPutAnswer_IFThen;
							orderPreviewList_IFThen.add(orderPreviewString_IFThen);
						}else {
							orderPreviewString_IFThen+=" "+logic_IFThen+" "+inPutSymbolName_IFThen+" "+inPutAnswer_IFThen;
							orderPreviewList_IFThen.add(orderPreviewString_IFThen);
						}														
						//在指令预览中显示已编辑命令
						orderPreview_IFThen.setText(orderPreviewString_IFThen);		
					}
				}	

			});
			buttonCANCEL_IFThen.setOnClickListener(new android.view.View.OnClickListener() {//撤销输入	
				@Override
				public void onClick(View v) {
					if ((orderPreviewList_IFThen.size()-1)==0) {
						orderPreview_IFThen.setText("");
					}else {
						orderPreviewList_IFThen.remove(orderPreviewList_IFThen.size()-1);
						orderPreviewString_IFThen=orderPreviewList_IFThen.get(orderPreviewList_IFThen.size()-1);
						orderPreview_IFThen.setText(orderPreviewString_IFThen);
					}
					
				}});
			//增加跳转指令预览
			final Button buttonSEQ_IFThen=(Button)orderView.findViewById(R.id.buttonSEQ_IFThen);	
			if(buttonSEQ_IFThen==null){return;}

			buttonSEQ_IFThen.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (spinnerGOTO_IFThen.isClickable()) {
						if (spinnerGOTO_IFThen.getSelectedItem()!=null) {
							seqString_IFThen=", GOTO"+" "+spinnerGOTO_IFThen.getSelectedItem().toString().trim();
						}	
					}else if (spinnerCALL_IFThen.isClickable()) {
						if (spinnerCALL_IFThen.getSelectedItem()!=null) {
							seqString_IFThen=", CALL"+" "+spinnerCALL_IFThen.getSelectedItem().toString().trim();
						}
						
					}		
					if (orderPreviewString_IFThen.contains("GOTO")||orderPreviewString_IFThen.contains("CALL")) {//限定只能有一个跳转，否则覆盖
						orderPreviewList_IFThen.remove(orderPreviewList_IFThen.size()-1);
						orderPreviewString_IFThen=orderPreviewList_IFThen.get(orderPreviewList_IFThen.size()-1);
						orderPreviewString_IFThen+=" "+seqString_IFThen;
						orderPreviewList_IFThen.add(orderPreviewString_IFThen);
						orderPreview_IFThen.setText(orderPreviewString_IFThen);
					}else {
						orderPreviewString_IFThen+=" "+seqString_IFThen;
						orderPreviewList_IFThen.add(orderPreviewString_IFThen);
						orderPreview_IFThen.setText(orderPreviewString_IFThen);
					
					}
					
				}
			});
	    	AlertDialog dialog2=new AlertDialog.Builder(getActivity())
			 .setTitle("如果就跳转指令(IFThen)")
			 .setView(orderView)
			 .setCancelable(false)//点击框外不消失
			 .setPositiveButton(R.string.OK,//确定 
					 new DetailedOrderListener4 (orderView,position,"IFThen",NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
			 .setNegativeButton("取消", null).setNeutralButton("指令表", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) {
                	 selectoperate(qxposition);
                 }  
             }).show();
			 //.setContentView(R.layout.nc_listrowcontent);
         
			 WindowManager manager = getActivity().getWindowManager(); 
			 Display display = manager.getDefaultDisplay(); 
			 Rect picRect=new Rect();
			 display.getRectSize(picRect); 
			  
			 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
			 params.width = picRect.width()*4/5; 
			 params.height =picRect.height()*4/5 ; 
			 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

			//Toast.makeText(getActivity(),"添加指令IFThen",Toast.LENGTH_SHORT).show();
	    }
	    public void alarmoperate(int position){
	    	final int qxposition=position;
	    	View orderView=View.inflate(getActivity(), R.layout.nc_order_alarm,null );
			Spinner spinner_ALARM=(Spinner) orderView.findViewById(R.id.spinner_ALARM); 
			final TextView title_ALARM=(TextView) orderView.findViewById(R.id.title_ALARM);
			final TextView content_ALARM=(TextView) orderView.findViewById(R.id.content_ALARM);
			if(spinner_ALARM==null){return;}
			if(title_ALARM==null){return;}
			if(content_ALARM==null){return;}
			
			ALARM_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,symbolList_ALARM);
			ALARM_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_ALARM.setAdapter(ALARM_Adapter);
			
			/**
	    	 * 再次点击不用再次输入,自动填入；
	    	 */
	    	String order_ALARM=mAppList.get(position).get("orderSpinner").toString().trim();
	    	String operat_ALARM=mAppList.get(position).get("operatText").toString().trim();
	    	String note_ALARM=mAppList.get(position).get("noteEditText").toString().trim();
	    	if (order_ALARM.contains("ALARM")) {
				spinner_ALARM.setAdapter(ALARM_Adapter);
				spinner_ALARM.setSelection(symbolList_ALARM.indexOf(operat_ALARM));
				content_ALARM.setText(note_ALARM);
			}
			/**
			 * 空进入
			 */
			spinner_ALARM.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,View arg1,int pos, long arg3) {
					String alarmString=parent.getSelectedItem().toString().trim();
					title_ALARM.setText("警报"+alarmString+":");
					if (noteList_ALARM!=null) {
						content_ALARM.setText(noteList_ALARM.get(pos).toString().trim());
					}		
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {																											
				}
			});
	    	AlertDialog dialog2=new AlertDialog.Builder(getActivity())
			 .setTitle("警报指令(ALARM)")
			 .setView(orderView)
			 .setCancelable(false)//点击框外不消失
			 .setPositiveButton(R.string.OK,//确定 
					 new DetailedOrderListener4 (orderView,position,"ALARM",NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
			 .setNegativeButton("取消", null).setNeutralButton("指令表", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) {
                	 selectoperate(qxposition);
                 }  
             }).show();
			 //.setContentView(R.layout.nc_listrowcontent);
         
			 WindowManager manager = getActivity().getWindowManager(); 
			 Display display = manager.getDefaultDisplay(); 
			 Rect picRect=new Rect();
			 display.getRectSize(picRect); 
			  
			 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
			 params.width = picRect.width()*4/5; 
			 params.height =picRect.height()*4/5 ; 
			 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

			//Toast.makeText(getActivity(),"添加指令ALARM",Toast.LENGTH_SHORT).show();
	    }
	    public void counteroperate(int position){
	    	final int qxposition=position;
	    	View orderView=View.inflate(getActivity(), R.layout.nc_order_counter,null );												
			Spinner spinner_COUNT=(Spinner) orderView.findViewById(R.id.spinner_COUNT);
			RadioButton radioINC_COUNT=(RadioButton) orderView.findViewById(R.id.radioINC_COUNT);
			RadioButton radioDEC_COUNT=(RadioButton) orderView.findViewById(R.id.radioDEC_COUNT);
			RadioButton radioCLR_COUNT=(RadioButton) orderView.findViewById(R.id.radioCLR_COUNT);
			RadioButton radioSET_COUNT=(RadioButton) orderView.findViewById(R.id.radioSET_COUNT);
			final TextView noteText_COUNT=(TextView) orderView.findViewById(R.id.noteText_COUNT);
			final EditText editText_COUNT=(EditText) orderView.findViewById(R.id.editText_COUNT);
			if(spinner_COUNT==null){return;}
			if(radioINC_COUNT==null){return;}
			if(radioDEC_COUNT==null){return;}
			if(radioCLR_COUNT==null){return;}
			if(radioSET_COUNT==null){return;}
			if(noteText_COUNT==null){return;}
			if(editText_COUNT==null){return;}
			editText_COUNT.setFocusable(false);
			//更新CounterList，若要重用可以精简成函数块
		/*	symbolList_Counter.clear();
			noteList_Counter.clear();
			for (int i = 0; i < ArrayListBound.getDeviceCounterListData().size(); i++) {
				String symbolString_Counter=(String) ArrayListBound.getDeviceCounterListData().get(i).get("symbolNameEditText");
				String notrString_Counter=(String) ArrayListBound.getDeviceCounterListData().get(i).get("noteEditText");
				if (symbolString_Counter.contains("C")&&ArrayListBound.getDeviceCounterListData().get(i).get("setItem").toString().equals("1")) {
					System.out.println("symbolString_Counter="+symbolString_Counter);
					symbolList_Counter.add(symbolString_Counter);
					noteList_Counter.add(notrString_Counter);
				}
			}*/	
			
			COUNT_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,symbolList_Counter);
			COUNT_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			//spinner_COUNT.setAdapter(COUNT_Adapter);
			
			
			/**
	    	 * 再次点击不用再次输入,自动填入；
	    	 */
	    	String order_COUNT=mAppList.get(position).get("orderSpinner").toString().trim();
	    	String operat_COUNT=mAppList.get(position).get("operatText").toString().trim();
	    
    		if (order_COUNT.contains("INC")) {
    			radioINC_COUNT.setChecked(true);
    			spinner_COUNT.setAdapter(COUNT_Adapter);
    			spinner_COUNT.setSelection(symbolList_Counter.indexOf(operat_COUNT));
    			if(symbolList_Counter.indexOf(operat_COUNT)>=0)
    			noteText_COUNT.setText(noteList_Counter.get(symbolList_Counter.indexOf(operat_COUNT)).toString().trim());	
			}else if (order_COUNT.contains("DEC")) {
				radioDEC_COUNT.setChecked(true);
				spinner_COUNT.setAdapter(COUNT_Adapter);
				spinner_COUNT.setSelection(symbolList_Counter.indexOf(operat_COUNT));
				if(symbolList_Counter.indexOf(operat_COUNT)>=0)
				noteText_COUNT.setText(noteList_Counter.get(symbolList_Counter.indexOf(operat_COUNT)).toString().trim());	
			}else if(order_COUNT.contains("CLR")){
				radioCLR_COUNT.setChecked(true);
				spinner_COUNT.setAdapter(COUNT_Adapter);
				spinner_COUNT.setSelection(symbolList_Counter.indexOf(operat_COUNT));
				if(symbolList_Counter.indexOf(operat_COUNT)>=0)
				noteText_COUNT.setText(noteList_Counter.get(symbolList_Counter.indexOf(operat_COUNT)).toString().trim());	
			}else if(order_COUNT.contains("SET")){
				radioSET_COUNT.setChecked(true);
				spinner_COUNT.setAdapter(COUNT_Adapter);
				spinner_COUNT.setSelection(symbolList_Counter.indexOf(operat_COUNT.split("=")[0]));
				if(symbolList_Counter.indexOf(operat_COUNT.split("=")[0])>=0)
				noteText_COUNT.setText(noteList_Counter.get(symbolList_Counter.indexOf(operat_COUNT.split("=")[0])).toString().trim());	
    			editText_COUNT.setFocusableInTouchMode(true);
    			editText_COUNT.setText(operat_COUNT.split("=")[1]);
			}else if (radioINC_COUNT.isChecked()) {
				spinner_COUNT.setAdapter(COUNT_Adapter);
	    	}
			
			/**
			 * 空进入
			 */
			spinner_COUNT.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,View arg1,int pos, long arg3) {
					noteText_COUNT.setText(noteList_Counter.get(pos).toString().trim());		
				}
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});		
			RadioGroup radioGroup_COUNT=(RadioGroup) orderView.findViewById(R.id.radioGroup_COUNT);
			if(radioGroup_COUNT==null){return;}
			radioGroup_COUNT.setOnCheckedChangeListener(new OnCheckedChangeListener() {								
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (checkedId==R.id.radioSET_COUNT) {
						editText_COUNT.setFocusableInTouchMode(true);
					}else{
						editText_COUNT.setFocusable(false);
					}
				}
			});
	    	AlertDialog dialog2=new AlertDialog.Builder(getActivity())
			 .setTitle("计数器指令(COUNTER)")
			 .setView(orderView)
			 .setCancelable(false)//点击框外不消失
			 .setPositiveButton(R.string.OK,//确定 
					 new DetailedOrderListener4 (orderView,position,"COUNTER",NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
			 .setNegativeButton("取消", null).setNeutralButton("指令表", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) {
                	 selectoperate(qxposition);
                 }  
             }).show();
			 //.setContentView(R.layout.nc_listrowcontent);
         
			 WindowManager manager = getActivity().getWindowManager(); 
			 Display display = manager.getDefaultDisplay(); 
			 Rect picRect=new Rect();
			 display.getRectSize(picRect); 
			  
			 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
			 params.width = picRect.width()*4/5; 
			 params.height =picRect.height()*4/5 ; 
			 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

			//Toast.makeText(getActivity(),"添加指令COUNTER",Toast.LENGTH_SHORT).show();
	    }
	    public void sequentialoperate(int position){
	    	final int qxposition=position;
	    	View orderView=View.inflate(getActivity(), R.layout.nc_order_sequential,null );
			Fragments_NCedit1.updateSEQList(GOTOList,CALLList);
			RadioGroup radioGroup_SEQ=(RadioGroup) orderView.findViewById(R.id.radioGroup_SEQ);
			RadioButton radioGOTO=(RadioButton) orderView.findViewById(R.id.radioGOTO);
			RadioButton radioCALL=(RadioButton) orderView.findViewById(R.id.radioCALL);
			final Spinner spinner_GOTO=(Spinner) orderView.findViewById(R.id.spinner_GOTO);
			final Spinner spinner_CALL=(Spinner) orderView.findViewById(R.id.spinner_CALL);
			if(radioGroup_SEQ==null){return;}
			if(radioGOTO==null){return;}
			if(radioCALL==null){return;}
			if(spinner_GOTO==null){return;}
			if(spinner_CALL==null){return;}
			GOTO_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,GOTOList);
			CALL_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,CALLList);
			GOTO_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			CALL_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			/**
	    	 * 再次点击不用再次输入,自动填入；
	    	 */
	    	String order_SEQUENTIAL=mAppList.get(position).get("orderSpinner").toString().trim();
	    	String operat_SEQUENTIAL=mAppList.get(position).get("operatText").toString().trim();
	    	
    		if (order_SEQUENTIAL.contains("GOTO")) {
    			radioGOTO.setChecked(true);
    			spinner_GOTO.setClickable(true);
    			spinner_GOTO.setAdapter(GOTO_Adapter);
    			spinner_GOTO.setSelection(GOTOList.indexOf(operat_SEQUENTIAL));
    			spinner_CALL.setClickable(false);
			}else if (order_SEQUENTIAL.contains("CALL")) {
				radioCALL.setChecked(true);
				spinner_CALL.setClickable(true);
				spinner_CALL.setAdapter(CALL_Adapter);
				spinner_CALL.setSelection(CALLList.indexOf(operat_SEQUENTIAL));	
				spinner_GOTO.setClickable(false);
			}else if (radioGOTO.isChecked()) {
				spinner_GOTO.setAdapter(GOTO_Adapter);
			}
			
			/**
			 * 空进入
			 */
    		
			radioGroup_SEQ.setOnCheckedChangeListener(new OnCheckedChangeListener() {													
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (checkedId==R.id.radioGOTO) {
						spinner_GOTO.setAdapter(GOTO_Adapter);
						spinner_GOTO.setClickable(true);
						spinner_CALL.setClickable(false);
					}else if (checkedId==R.id.radioCALL) {
						spinner_CALL.setAdapter(CALL_Adapter);
						spinner_CALL.setClickable(true);
						spinner_GOTO.setClickable(false);
					}	
				}
			});
	    	AlertDialog dialog2=new AlertDialog.Builder(getActivity())
			 .setTitle("程序控制指令(SEQUENTIAL)")
			 .setView(orderView)
			 .setCancelable(false)//点击框外不消失
			 .setPositiveButton(R.string.OK,//确定 
					 new DetailedOrderListener4 (orderView,position,"SEQUENTIAL",NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
			 .setNegativeButton("取消", null).setNeutralButton("指令表", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) {
                	 selectoperate(qxposition);
                 }  
             }).show();
			 //.setContentView(R.layout.nc_listrowcontent);
         
			 WindowManager manager = getActivity().getWindowManager(); 
			 Display display = manager.getDefaultDisplay(); 
			 Rect picRect=new Rect();
			 display.getRectSize(picRect); 
			  
			 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
			 params.width = picRect.width()*4/5; 
			 params.height =picRect.height()*4/5 ; 
			 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

			//Toast.makeText(getActivity(),"添加指令SEQUENTIAL",Toast.LENGTH_SHORT).show();
	    }
	    public void paralleloperate(int position){
	    	final int qxposition=position;
	    	View orderView=View.inflate(getActivity(), R.layout.nc_order_parallel1,null );
			Fragments_NCedit1.updatePARList(QONList,QOFFList);
			RadioGroup radioGroup_PAR=(RadioGroup) orderView.findViewById(R.id.radioGroup_PAR);
			RadioButton radioQON=(RadioButton) orderView.findViewById(R.id.radioQON);
			RadioButton radioQOFF=(RadioButton) orderView.findViewById(R.id.radioQOFF);
			final Spinner spinner_QON=(Spinner) orderView.findViewById(R.id.spinner_QON);
			final Spinner spinner_QOFF=(Spinner) orderView.findViewById(R.id.spinner_QOFF);
			if(radioGroup_PAR==null){return;}
			if(radioQON==null){return;}
			if(radioQOFF==null){return;}
			if(spinner_QON==null){return;}
			if(spinner_QOFF==null){return;}
			QON_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,QONList);
			QOFF_Adapter= new ArrayAdapter<String>(orderView.getContext(),android.R.layout.simple_spinner_item,QOFFList);
			QON_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			QOFF_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			
			/**
	    	 * 再次点击不用再次输入,自动填入；
	    	 */
	    	String order_PARALLEL=mAppList.get(position).get("orderSpinner").toString().trim();
	    	String operat_PARALLEL=mAppList.get(position).get("operatText").toString().trim();
	    	
    		if (order_PARALLEL.contains("QON")) {
    			radioQON.setChecked(true);
    			spinner_QON.setClickable(true);
    			spinner_QON.setAdapter(QON_Adapter);
    			spinner_QON.setSelection(QONList.indexOf(operat_PARALLEL));
    			spinner_QOFF.setClickable(false);
			}else if (order_PARALLEL.contains("QOFF")) {
				radioQOFF.setChecked(true);
				spinner_QOFF.setClickable(true);
				spinner_QOFF.setAdapter(QOFF_Adapter);
				spinner_QOFF.setSelection(QOFFList.indexOf(operat_PARALLEL));	
				spinner_QON.setClickable(false);
			}else if (radioQON.isChecked()) {
				spinner_QON.setAdapter(QON_Adapter);
			}
			
			/**
			 * 空进入
			 */
    		/*if (radioQON.isChecked()) {
				spinner_QON.setAdapter(QON_Adapter);
			}*/
			radioGroup_PAR.setOnCheckedChangeListener(new OnCheckedChangeListener() {													
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if (checkedId==R.id.radioQON) {
						spinner_QON.setAdapter(QON_Adapter);
						spinner_QON.setClickable(true);
						spinner_QOFF.setClickable(false);
					}else if (checkedId==R.id.radioQOFF) {
						spinner_QOFF.setAdapter(QOFF_Adapter);
						spinner_QOFF.setClickable(true);
						spinner_QON.setClickable(false);
					}	
				}
			});	
	    	AlertDialog dialog2=new AlertDialog.Builder(getActivity())
			 .setTitle("启动进程指令(PARALLEL)")
			 .setView(orderView)
			 .setCancelable(false)//点击框外不消失
			 .setPositiveButton(R.string.OK,//确定 
					 new DetailedOrderListener4 (orderView,position,"PARALLEL",NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
			 .setNegativeButton("取消", null).setNeutralButton("指令表", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) {
                	 selectoperate(qxposition);
                 }  
             }).show();
			 //.setContentView(R.layout.nc_listrowcontent);
         
			 WindowManager manager = getActivity().getWindowManager(); 
			 Display display = manager.getDefaultDisplay(); 
			 Rect picRect=new Rect();
			 display.getRectSize(picRect); 
			  
			 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
			 params.width = picRect.width()*4/5; 
			 params.height =picRect.height()*4/5 ; 
			 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

			//Toast.makeText(getActivity(),"添加指令PARALLEL",Toast.LENGTH_SHORT).show();
	    }
	    public void selectoperate(int position1){
	    	final int position=position1;
	    	new AlertDialog.Builder(getActivity())
			.setTitle("指令表")
			.setItems(nc_ordersall,new DialogInterface.OnClickListener() {
						// 添加选择某项后的方法
						@Override
						public void onClick(DialogInterface dialog,int which) {

							 View orderView=null;
							 String endretpausetitle="";
							 //保留的暂时还没有用到底的命令
//							 View view13=View.inflate(getActivity(), R.layout.nc_order_ret,null );
//							 View view14=View.inflate(getActivity(), R.layout.nc_order_pause,null );
//							 View view18=View.inflate(getActivity(), R.layout.nc_order_end,null );
							 switch (Constans.NCorders_enum.getOrder(nc_orders[which])) {
								case OUTP:
									orderView=View.inflate(getActivity(), R.layout.nc_order_outp,null );
									 AlertDialog dialog2=new AlertDialog.Builder(getActivity())
									 .setTitle("脉冲输出指令("+nc_orders[which]+")")
									 .setView(orderView)
									 .setCancelable(false)//点击框外不消失
									 .setPositiveButton(R.string.OK,//确定 
											 new DetailedOrderListener4 (orderView,position,nc_orders[which],NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
									 .setNegativeButton("取消", null)
									 .show();
									 //.setContentView(R.layout.nc_listrowcontent);
					                
									 WindowManager manager = getActivity().getWindowManager(); 
									 Display display = manager.getDefaultDisplay(); 
									 Rect picRect=new Rect();
									 display.getRectSize(picRect); 
									  
									 WindowManager.LayoutParams params = dialog2.getWindow().getAttributes(); 
									 params.width = picRect.width()*4/5; 
									 params.height =picRect.height()*4/5 ; 
									 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

									//Toast.makeText(getActivity(),"添加指令" + nc_orders[which],Toast.LENGTH_SHORT).show();
									break;
									
								case MOVE:
									moveoperate("MOVE",position);
									break;
								case MOVEP:
									moveoperate("MOVEP",position);
									break;

									
								case ACC:
									orderView=View.inflate(getActivity(), R.layout.nc_order_accel,null );
									 dialog2=new AlertDialog.Builder(getActivity())
									 .setTitle("加减速指令("+nc_orders[which]+")")
									 .setView(orderView)
									 .setCancelable(false)//点击框外不消失
									 .setPositiveButton(R.string.OK,//确定 
											 new DetailedOrderListener4 (orderView,position,nc_orders[which],NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
									 .setNegativeButton("取消", null)
									 .show();
									 //.setContentView(R.layout.nc_listrowcontent);
					                
									 manager = getActivity().getWindowManager(); 
									 display = manager.getDefaultDisplay(); 
									 picRect=new Rect();
									 display.getRectSize(picRect); 
									  
									 params = dialog2.getWindow().getAttributes(); 
									 params.width = picRect.width()*4/5; 
									 params.height =picRect.height()*4/5 ; 
									 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

									//Toast.makeText(getActivity(),"添加指令" + nc_orders[which],Toast.LENGTH_SHORT).show();
									break;
									

								case IF:
									ifoperate(position);
									break;
										
								case OUT:
									outoperate(position);
									break;
									
								case WAIT:
									waitoperate(position);																
									break;
									
								case IFThen:
									ifthenoperate(position);
									break;
									
								case ALARM:
									alarmoperate(position);
									break;
									
								case COUNTER:
									counteroperate(position);
									break;
									
								case SEQUENTIAL:
									sequentialoperate(position);													
									break;
									
								case PARALLEL:
									paralleloperate(position);												
									break;
									
								case END:
									if(endretpausetitle.equals("")){
										endretpausetitle="结束指令("+nc_orders[which]+")";
									}
									
								case RET:
                                    if(endretpausetitle.equals("")){
										endretpausetitle="返回指令("+nc_orders[which]+")";
									}
									
								case PAUSE:
                                    if(endretpausetitle.equals("")){
										endretpausetitle="暂停指令("+nc_orders[which]+")";
									}
									ArrayListBound.getNCeditList4Data().get(position).put("operatText", "");
									 dialog2=new AlertDialog.Builder(getActivity())
									 .setTitle(nc_orders[which]+"指令")
									 .setView(orderView)
									 .setCancelable(false)//点击框外不消失
									 .setPositiveButton(R.string.OK,//确定 
											 new DetailedOrderListener4 (orderView,position,nc_orders[which],NcEditList,MyAdapter.this))//使用单独创建的类控制详细命令界面的响应!!!											
									 .setNegativeButton("取消", null)
									 .show();
									 //.setContentView(R.layout.nc_listrowcontent);
					                
									 manager = getActivity().getWindowManager(); 
									 display = manager.getDefaultDisplay(); 
									 picRect=new Rect();
									 display.getRectSize(picRect); 
									  
									 params = dialog2.getWindow().getAttributes(); 
									 params.width = picRect.width()*4/5; 
									 params.height =picRect.height()*4/5 ; 
									 dialog2.getWindow().setAttributes(params);//调整弹出窗口的大小自适应屏幕

									//Toast.makeText(getActivity(),"添加指令" + nc_orders[which],Toast.LENGTH_SHORT).show();
									break;
								
									
								default:
									break;
								}
						}	
					}).setNegativeButton(R.string.CANCEL, null)//取消
					.show();
	    }
	}
	/**
	 * 替代format出错的函数，给标号自动补位添0
	 * @param str
	 * @param strLength
	 * @return
	 */
	public static String addZeroForString(String str, int strLength) {
	    int strLen = str.length();
	    if (strLen < strLength) {
	        while (strLen < strLength) {
	            StringBuffer sb = new StringBuffer();
	            sb.append("0").append(str);// 左补0
	            // sb.append(str).append("0");//右补0
	            str = sb.toString();
	            strLen = str.length();
	        }
	    }

	    return str;
	}
 	/**
	 * 将已有的标号名存入list
	 * @param list_numname
	 * @param arrayList 
	 */
	public static void checkNumnameList(
			ArrayList<String> list_numname) {
		// 直接清空再添加
		try{
		list_numname.clear();
			for (int i = 0; i < ArrayListBound.getNCeditList1Data().size(); i++) {											
				String numstr=(String)ArrayListBound.getNCeditList1Data().get(i).get("numSpinner");
				if (!numstr.equals("")) {
					list_numname.add(numstr);
				}	
			}
			for (int i = 0; i < ArrayListBound.getNCeditList2Data().size(); i++) {											
				String numstr=(String)ArrayListBound.getNCeditList2Data().get(i).get("numSpinner");
				if (!numstr.equals("")) {
					list_numname.add(numstr);
				}	
			}
			for (int i = 0; i < ArrayListBound.getNCeditList3Data().size(); i++) {											
				String numstr=(String)ArrayListBound.getNCeditList3Data().get(i).get("numSpinner");
				if (!numstr.equals("")) {
					list_numname.add(numstr);
				}	
			}
			for (int i = 0; i < ArrayListBound.getNCeditList4Data().size(); i++) {											
				String numstr=(String)ArrayListBound.getNCeditList4Data().get(i).get("numSpinner");
				if (!numstr.equals("")) {
					list_numname.add(numstr);
				}	
			}
			for (int i = 0; i < ArrayListBound.getNCeditList5Data().size(); i++) {											
				String numstr=(String)ArrayListBound.getNCeditList5Data().get(i).get("numSpinner");
				if (!numstr.equals("")) {
					list_numname.add(numstr);
				}	
			}
			for (int i = 0; i < ArrayListBound.getNCeditList6Data().size(); i++) {											
				String numstr=(String)ArrayListBound.getNCeditList6Data().get(i).get("numSpinner");
				if (!numstr.equals("")) {
					list_numname.add(numstr);
				}	
			}
			for (int i = 0; i < ArrayListBound.getNCeditList7Data().size(); i++) {											
				String numstr=(String)ArrayListBound.getNCeditList7Data().get(i).get("numSpinner");
				if (!numstr.equals("")) {
					list_numname.add(numstr);
				}	
			}
			for (int i = 0; i < ArrayListBound.getNCeditList8Data().size(); i++) {											
				String numstr=(String)ArrayListBound.getNCeditList8Data().get(i).get("numSpinner");
				if (!numstr.equals("")) {
					list_numname.add(numstr);
				}	
			}
	}catch(Exception e){
		e.printStackTrace();
	}		
	}
	
	
	
	
	
	
	
}


