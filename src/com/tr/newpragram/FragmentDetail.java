package com.tr.newpragram;

import java.util.ArrayList;
import java.util.HashMap;
import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.PositionQueryRunnable;
import wifiRunnablesAndChatlistener.SendDataRunnable;

import com.dataInAddress.AddressPublic;
import com.dataInAddress.Define;
import com.dbutils.ArrayListBound;
import com.explain.HexDecoding;
import com.explain.TableToBinary;
import com.tr.R;
import com.tr.programming.Config;
import com.wifiexchange.WifiSetting_Info;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 详情Fragment
 * @author mining
 *
 */

public class FragmentDetail extends Fragment {
	private final String TAG = "FragmentDetail";
	private RadioGroup genderGroup = null ;
	private RadioButton PositonBtn = null;
	private RadioButton TimerBtn = null;
	private RadioButton CounterBtn = null;	
	private Button DetailBtn = null;
	private ListView DetailList = null;
	private int pos = -1;
	private LinearLayout Positionlayout ;
	private LinearLayout Counterlayout ;
	private LinearLayout Timerlayout ;
	private WifiReadDataFormat formatReadMessage;	
	//private TableAdapter PostionAdapter ;
	private CounterAdapter myCounteerAdapter;
	private TimerAdapter myTimerAdapter;

	public static ArrayList<HashMap<String, Object>> P_posList_setting = new ArrayList<HashMap<String, Object>>();// 表格要绑定的list
	public static MyAdapter P_posList_Adapter = null;
	private View tempV;
	private int tempposition;
	private String tempeditString;
	private static int selectedItem = -1;
	private static int selectedID = -1;
	private static String selectedKey = "foot_p_setting";
	private ArrayList<String> allpositionList_setting = new ArrayList<String>();//存放位置
	//下面是通讯相关变量，勿删除
	private WifiSendDataFormat formatSendMessage;
	private SendDataRunnable sendDataRunnable;
	private FinishRunnable sendDataFinishRunnable;	

	private byte[] getData;
	ArrayList<Integer> toaddress=new ArrayList<Integer>();
	ArrayList<Integer> tolength=new ArrayList<Integer>();
	int posindex=0;
	private ArrayList<HashMap<String, Object>> TimerList_setting = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> PresetCounterList_setting = new ArrayList<HashMap<String, Object>>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.fragment_detail_layout, container, false);
		
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
		P_posList_setting.clear();
		toaddress.clear();
		tolength.clear();
		posindex=0;
		allpositionList_setting.clear();
		allpositionList_setting.addAll(Config.list_pname);
		allpositionList_setting.addAll(Config.list_spname);
		allpositionList_setting.addAll(Config.list_fpname);
		for(int i=0;i<allpositionList_setting.size();i++){
			System.out.println(i+"         "+allpositionList_setting.get(i));
		}
		for(int i=0;i<allpositionList_setting.size();i++){
			if(allpositionList_setting.get(i).contains("SP")){
				int send_SPaddress=AddressPublic.model_SP_point_Head
						+(AddressPublic.model_FP_point_Head-AddressPublic.model_SP_point_Head)/Define.MAX_STDPACK_NUM
						*(Integer.parseInt((allpositionList_setting.get(i).toString().split("\\s++")[0]).trim().substring(2))-1);				
				toaddress.add(send_SPaddress);
				tolength.add(20);
			}else if(allpositionList_setting.get(i).contains("FP")){
				String devicename=allpositionList_setting.get(i).toString().split("\\s++")[0];
				devicename=devicename.trim();
				int send_FPaddress=0;
				if(devicename.equalsIgnoreCase("FP001")){
					send_FPaddress = 0x20003AB0;
				}else if(devicename.equalsIgnoreCase("FP002")){
					send_FPaddress = 0x20003AC6;
				}
				toaddress.add(send_FPaddress);
				tolength.add(16);
			}else if(allpositionList_setting.get(i).contains("P")){
				int send_Paddress=AddressPublic.model_P_point_Head
						+(AddressPublic.model_SP_point_Head-AddressPublic.model_P_point_Head)/Define.MAX_STDPOINT_NUM
						*(Integer.parseInt((allpositionList_setting.get(i).toString().split("\\s++")[0]).trim().substring(1))-1);
				toaddress.add(send_Paddress);
				tolength.add(20);
			}else{
				
			}
		}
		System.out.println("toaddress.size()==="+toaddress.size()+"    tolength.size()==="+tolength.size());
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try{
		Positionlayout = (LinearLayout) getActivity().findViewById(R.id.positiontitle);		
		Counterlayout = (LinearLayout) getActivity().findViewById(R.id.countertitle);		
		Timerlayout = (LinearLayout) getActivity().findViewById(R.id.timertitle);		
		Positionlayout.setVisibility(View.GONE) ;
		Counterlayout.setVisibility(View.GONE) ;
		Timerlayout.setVisibility(View.GONE) ;
		
		getActivity().findViewById(R.id.detail_container);		
		DetailList = (ListView) getActivity().findViewById(R.id.fragment_detail);
		//获取列表传来的参数
		
		//Radio Group
		genderGroup = (RadioGroup)getActivity().findViewById(R.id.genderGroup);
		
		PositonBtn = (RadioButton) getActivity().findViewById(R.id.positionbutton);
		TimerBtn = (RadioButton) getActivity().findViewById(R.id.timerbutton);
		CounterBtn = (RadioButton) getActivity().findViewById(R.id.counterbutton);
	    DetailBtn = (Button)getActivity().findViewById(R.id.detailbutton);
	    DetailBtn.setClickable(false);
	    DetailBtn.setOnClickListener(DetailBtnListener);  

		genderGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId)
				{
					case R.id.positionbutton:
						Log.d(TAG," positionbutton");
						break;
					case R.id.timerbutton:
						Log.d(TAG," timerbutton ");
						break;
					case R.id.counterbutton:
						Log.d(TAG,"counterbutton ");
						break;
					default:
						break;
				}
				updateListView();
			}
		});

		PositonBtn.setChecked(true);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	private void backMessageToDo1(View v, String editString,int position,String keyString[],ArrayList<HashMap<String, Object>> mAppList) {
	       try{
			HashMap<String, Object> map = new HashMap<String, Object>();

			switch (v.getId()) {
			case R.id.foot_p_setting:
				map.put("name_p_setting",
						mAppList.get(position).get(keyString[0]).toString());
				map.put("foot_p_setting", editString);
				map.put("productBA_p_setting",
						mAppList.get(position).get(keyString[2]).toString());
				map.put("productUD_p_setting",
						mAppList.get(position).get(keyString[3]).toString());
				map.put("feedertroughBA_p_setting", mAppList.get(position)
						.get(keyString[4]).toString());
				map.put("feedertroughUD_p_setting", mAppList.get(position)
						.get(keyString[5]).toString());

				mAppList.set(position, map);
				break;
			case R.id.productBA_p_setting:
				map.put("name_p_setting",
						mAppList.get(position).get(keyString[0]).toString());
				map.put("foot_p_setting",
						mAppList.get(position).get(keyString[1]).toString());
				map.put("productBA_p_setting", editString);
				map.put("productUD_p_setting",
						mAppList.get(position).get(keyString[3]).toString());
				map.put("feedertroughBA_p_setting", mAppList.get(position)
						.get(keyString[4]).toString());
				map.put("feedertroughUD_p_setting", mAppList.get(position)
						.get(keyString[5]).toString());

				mAppList.set(position, map);
				break;
			case R.id.productUD_p_setting:
				map.put("name_p_setting",
						mAppList.get(position).get(keyString[0]).toString());
				map.put("foot_p_setting",
						mAppList.get(position).get(keyString[1]).toString());
				map.put("productBA_p_setting",
						mAppList.get(position).get(keyString[2]).toString());
				map.put("productUD_p_setting", editString);
				map.put("feedertroughBA_p_setting", mAppList.get(position)
						.get(keyString[4]).toString());
				map.put("feedertroughUD_p_setting", mAppList.get(position)
						.get(keyString[5]).toString());

				mAppList.set(position, map);
				break;
			case R.id.feedertroughBA_p_setting:
				map.put("name_p_setting",
						mAppList.get(position).get(keyString[0]).toString());
				map.put("foot_p_setting",
						mAppList.get(position).get(keyString[1]).toString());
				map.put("productBA_p_setting",
						mAppList.get(position).get(keyString[2]).toString());
				map.put("productUD_p_setting",
						mAppList.get(position).get(keyString[3]).toString());
				map.put("feedertroughBA_p_setting", editString);
				map.put("feedertroughUD_p_setting", mAppList.get(position)
						.get(keyString[5]).toString());

				mAppList.set(position, map);
				break;
			case R.id.feedertroughUD_p_setting:
				map.put("name_p_setting",
						mAppList.get(position).get(keyString[0]).toString());
				map.put("foot_p_setting",
						mAppList.get(position).get(keyString[1]).toString());
				map.put("productBA_p_setting",
						mAppList.get(position).get(keyString[2]).toString());
				map.put("productUD_p_setting",
						mAppList.get(position).get(keyString[3]).toString());
				map.put("feedertroughBA_p_setting", mAppList.get(position)
						.get(keyString[4]).toString());
				map.put("feedertroughUD_p_setting", editString);

				mAppList.set(position, map);
				break;

			default:
				break;
			}
			P_posList_Adapter.notifyDataSetChanged();
		}catch(Exception e){
			e.printStackTrace();
		}
		}

		private final Runnable backMessageToDo1 = new Runnable(){
			@Override
			public void run() {	
				backMessageToDo1(tempV,tempeditString,tempposition,P_posList_Adapter.keyString,P_posList_Adapter.mAppList);
				//Toast.makeText(getActivity(), "数据发送完毕", Toast.LENGTH_LONG).show();
			}
		};

		private void tosd(){
		try{
			formatReadMessage=new WifiReadDataFormat(toaddress.get(0),tolength.get(0));

				sendDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());

				sendDataFinishRunnable=new FinishRunnable(getActivity(),backMessageTodo);

				sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

				getActivity().runOnUiThread(sendDataRunnable);
		}catch(Exception e){
			if(WifiSetting_Info.mClient!=null){
			  //P_posList_setting.remove(posindex-2);
			  //P_posList_Adapter.notifyDataSetChanged();
			}
			e.printStackTrace();
		}
	}
	private Runnable backMessageTodo=new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 返回的标志位STS1处的判断以及和校验
			 try{
					getData=new byte[formatReadMessage.getLength()];
					//获取返回的数据，从第八位开始拷贝数据
					if( formatReadMessage.getFinalData() != null) 
					{
						System.arraycopy(formatReadMessage.getFinalData(), 0, getData, 0, formatReadMessage.getLength());
						
						System.out.println("posindex======"+posindex);
						if(allpositionList_setting.get(posindex).contains("SP")){
							HashMap<String, Object> map1 = new HashMap<String, Object>();
							map1.put("name_p_setting",allpositionList_setting.get(posindex));
							int foot_p=HexDecoding.Array4Toint(getData, 0);
							int productBA_p=HexDecoding.Array4Toint(getData, 4);
							int productUD_p=HexDecoding.Array4Toint(getData, 8);
							int feedertroughBA_p=HexDecoding.Array4Toint(getData, 12);
							int feedertroughUD_p=HexDecoding.Array4Toint(getData, 16);
							
							map1.put("foot_p_setting", String.format("%1$5.1f",Double.valueOf(foot_p)/100));
							map1.put("productBA_p_setting", String.format("%1$5.1f",Double.valueOf(productBA_p)/100));
							map1.put("productUD_p_setting", String.format("%1$5.1f",Double.valueOf(productUD_p)/100));
							map1.put("feedertroughBA_p_setting", String.format("%1$5.1f",Double.valueOf(feedertroughBA_p)/100));
							map1.put("feedertroughUD_p_setting", String.format("%1$5.1f",Double.valueOf(feedertroughUD_p)/100));
							P_posList_setting.add(map1);
						}else if(allpositionList_setting.get(posindex).contains("FP")){
							HashMap<String, Object> map1 = new HashMap<String, Object>();
							map1.put("name_p_setting",allpositionList_setting.get(posindex));
							map1.put("foot_p_setting", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(getData,0))/100)));
							map1.put("productBA_p_setting",String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(getData,2))/100)));
							map1.put("productUD_p_setting", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(getData,4))/100)));
							map1.put("feedertroughBA_p_setting",String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(getData,6))/100)));
							map1.put("feedertroughUD_p_setting", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(getData,8))/100)));
							P_posList_setting.add( map1);
						}else if(allpositionList_setting.get(posindex).contains("P")){
							HashMap<String, Object> map1 = new HashMap<String, Object>();
							map1.put("name_p_setting", allpositionList_setting.get(posindex));
							int foot_p=HexDecoding.Array4Toint(getData, 0);
							int productBA_p=HexDecoding.Array4Toint(getData, 4);
							int productUD_p=HexDecoding.Array4Toint(getData, 8);
							int feedertroughBA_p=HexDecoding.Array4Toint(getData, 12);
							int feedertroughUD_p=HexDecoding.Array4Toint(getData, 16);
							
							map1.put("foot_p_setting", String.format("%1$5.1f",Double.valueOf(foot_p)/100));
							map1.put("productBA_p_setting", String.format("%1$5.1f",Double.valueOf(productBA_p)/100));
							map1.put("productUD_p_setting", String.format("%1$5.1f",Double.valueOf(productUD_p)/100));
							map1.put("feedertroughBA_p_setting", String.format("%1$5.1f",Double.valueOf(feedertroughBA_p)/100));
							map1.put("feedertroughUD_p_setting", String.format("%1$5.1f",Double.valueOf(feedertroughUD_p)/100));
							P_posList_setting.add(map1);
						}
						P_posList_Adapter.notifyDataSetChanged();
						++posindex;
					
					}
			 toaddress.remove(0);
			 tolength.remove(0);
			 if(toaddress.size()>0){
				 tosd(); 
			 }else{
				 Toast.makeText(getActivity(), "数据读取完毕", Toast.LENGTH_SHORT).show();
			 }
			
			 }catch(Exception e){
					e.printStackTrace();
				}
		}
	};
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}

	private void initialTimerList(byte[] getData) {
		try{
			TimerList_setting.clear();
		 ArrayList<HashMap<String, Object>> timerlists=ArrayListBound.getDeviceTimerListData();
		 byte[] temp=new byte[4];
			for (int i = 0; i < timerlists.size(); i++) {
				String timerNameString =timerlists.get(i).get("symbolNameEditText").toString().trim();
			    System.arraycopy(getData, i*4, temp, 0, 4);
				if(!Config.list_timername.contains(timerNameString)){					
					continue;
				}
				if (!timerNameString.equals("")) 
				{
					timerlists.get(i).put("setItem","1");	
					HashMap<String, Object> map0 = new HashMap<String, Object>();
					map0.put("numText", timerNameString);
					map0.put("value", String.valueOf(Double.valueOf(String.valueOf(HexDecoding.Array4Toint(temp)))/100));
										
					TimerList_setting.add(map0);
				}
			}
			if (TimerList_setting.size() == 0) {
				Toast.makeText(getActivity(), "没有定时器，数据为空", Toast.LENGTH_SHORT)
						.show();
			}
	}catch(Exception e){
		e.printStackTrace();
	}

	}
	//获取返回的数据后进行的UI操作
		private final Runnable readDataFinishTodo = new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//对于返回的36字节

				getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				if( formatReadMessage.getFinalData() != null)  
				{
					System.arraycopy(formatReadMessage.getFinalData(), 0, getData, 0, formatReadMessage.getLength());
		
					if( getActivity() != null)  
					{
								//initialTimerList(TimerList_setting, timernoteList_setting,getData);// 可以考虑放到最初进来的spinnerfragment中去，这样就不用每次刷新
								initialTimerList(getData);
								
								myTimerAdapter.notifyDataSetChanged();
							
						
					}
				}
				


			}

		};
private void updateListView()
{
	if(PositonBtn.isChecked()){	
		P_posList_Adapter = new MyAdapter(getActivity(), P_posList_setting,
				R.layout.position_list_item, new String[] { "name_p_setting",
			"foot_p_setting", "productBA_p_setting",
			"productUD_p_setting", "feedertroughBA_p_setting",
		"feedertroughUD_p_setting" }, new int[] {
			R.id.name_p_setting, R.id.foot_p_setting,
			R.id.productBA_p_setting, R.id.productUD_p_setting,
			R.id.feedertroughBA_p_setting,
			R.id.feedertroughUD_p_setting });
		DetailList.setAdapter(P_posList_Adapter);
		P_posList_Adapter.notifyDataSetChanged();
		
		
		DetailBtn.setVisibility(View.VISIBLE);
		Positionlayout.setVisibility(View.VISIBLE) ;
		Counterlayout.setVisibility(View.GONE) ;
		Timerlayout.setVisibility(View.GONE) ;
		DetailList.setClickable(true);
		DetailList.setItemsCanFocus(true);
		DetailList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				  pos = arg2;
				  P_posList_Adapter.setSelectItem(pos);
				  P_posList_Adapter.notifyDataSetChanged();
				  BroadToHighLight(allpositionList_setting.get(pos).toString());	
				  
			}
		});
		
		if (WifiSetting_Info.mClient == null) {
			Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
			return;
		}
		tosd();
	
	}else if(TimerBtn.isChecked()){
		Log.d(TAG,"updateListView TimerBtn");
		 myTimerAdapter = new TimerAdapter(getActivity(), TimerList_setting, 
				new String []{"numText", "value"});
		DetailList.setAdapter(myTimerAdapter);
		Positionlayout.setVisibility(View.GONE) ;
		Counterlayout.setVisibility(View.GONE) ;
		Timerlayout.setVisibility(View.VISIBLE) ;
		DetailBtn.setVisibility(View.GONE);
		DetailList.setClickable(true);
		DetailList.setItemsCanFocus(true);
		DetailList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				  pos = arg2;
				  myTimerAdapter.setSelectItem(pos);
				  myTimerAdapter.notifyDataSetChanged();
				  BroadToHighLight(TimerList_setting.get(pos).get("numText").toString());			
			}
		});
		
			if (Config.list_timername.size() == 0) {
			Toast.makeText(getActivity(), "没有定时器，数据为空", Toast.LENGTH_SHORT).show();
		}
			
			if (WifiSetting_Info.mClient == null) {
				Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
				return;
			}
	    	//与下位机通信，获取初始化listview的数据，一次读取36个字节，36个字节用于更新listview
			formatReadMessage=new WifiReadDataFormat(AddressPublic.model_Timer_Head, Define.MAX_TIMER_NUM*4);

			try {

				sendDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());

				sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readDataFinishTodo);

				sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

				getActivity().runOnUiThread(sendDataRunnable);

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
			}
	}else if(CounterBtn.isChecked()){
		Log.d(TAG,"updateListView CounterBtn");
		myCounteerAdapter = new CounterAdapter(getActivity(),PresetCounterList_setting, 
				new String []{"numText", "value"});
		DetailList.setAdapter(myCounteerAdapter);
		Positionlayout.setVisibility(View.GONE) ;
		Counterlayout.setVisibility(View.VISIBLE) ;
		Timerlayout.setVisibility(View.GONE) ;
		DetailBtn.setVisibility(View.GONE);
		DetailList.setClickable(true);
		DetailList.setItemsCanFocus(true);
		DetailList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				  pos = arg2;
				  myCounteerAdapter.setSelectItem(pos);
				  myCounteerAdapter.notifyDataSetChanged();
				  BroadToHighLight(Config.list_countername.get(pos).toString());

			}
		});
			if (Config.list_countername.size() == 0) {
			Toast.makeText(getActivity(), "没有计数器，数据为空", Toast.LENGTH_SHORT).show();
		}
	}
		
}

OnClickListener DetailBtnListener = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent IT = new Intent();
	    Bundle bundle = new Bundle();
	    Log.d("mpeng","the pos is "+pos);
	    //bundle.putInt("pos",pos);    // 当前点击的位置
	    if(pos<0){
	    	Toast.makeText(getActivity(), "请选择位置", Toast.LENGTH_SHORT).show();
	    	return;
	    }
	    String name = allpositionList_setting.get(pos).toString().trim();
	    bundle.putString("PosType", name);//当前是P SP.FP
	    /*if(name.equals("P"))
	    	bundle.putString("PosType", "PLIST");//当前是P SP.FP
	    else if(name.equals("S"))
	    	bundle.putString("PosType", "SPLIST");//当前是P SP.FP
	    else if(name.equals("F"))
	    	bundle.putString("PosType", "FPLIST");//当前是P SP.FP
*/	    
	    IT.putExtras(bundle);
	    IT.setAction("gotoDetailSetting");
		getActivity().sendBroadcast(IT);
	}
};
	
private void BroadToHighLight(String str)
{
	Intent IT = new Intent();
    IT.setAction("highlight");
    IT.putExtra("name", str);
	getActivity().sendBroadcast(IT);

}

private void initData()
{
	Config.list_pname.clear();
	Config.list_fpname.clear();
	Config.list_spname.clear();
	Config.list_timername.clear();
	Config.list_countername.clear();
	Config.pspfpaxle=new byte[200];
	char[] chs={};
	char ch=' ';
	int allA=0;
	try {
		ArrayList<HashMap<String, Object>> List = ArrayListBound.getNCeditList3Data();
		for(int i=0;i<List.size();i++){
			String order_MOVE=List.get(i).get("orderSpinner").toString();
			String operatstr=List.get(i).get("operatText").toString();
			if((order_MOVE.contains("MOVE")||order_MOVE.contains("MOVEP"))&&operatstr.contains("SP")){
				if(!Config.list_spname.contains(operatstr.substring(operatstr.indexOf("S"), operatstr.length()))){
					Config.list_spname.add(operatstr.substring(operatstr.indexOf("S"), operatstr.length()));

				}
			}else if((order_MOVE.contains("MOVE")||order_MOVE.contains("MOVEP"))&&operatstr.contains("FP")){
				if(!Config.list_fpname.contains(operatstr.substring(operatstr.indexOf("F"), operatstr.length()))){
					Config.list_fpname.add(operatstr.substring(operatstr.indexOf("F"), operatstr.length()));

				}
			}else if((order_MOVE.contains("MOVE")||order_MOVE.contains("MOVEP"))&&operatstr.contains("P")){
				if(!Config.list_pname.contains(operatstr.substring(operatstr.indexOf("P"), operatstr.length()))){
					Config.list_pname.add(operatstr.substring(operatstr.indexOf("P"), operatstr.length()));
				}
				
				chs=operatstr.toCharArray();
				 ch=' ';
				 allA=0;
					for(int i1=0;i1<chs.length;i1++){
						if(chs[i1]=='A'){
							ch=chs[i1+1];
							int index=(int)ch-49;
							if((Config.pspfpaxle[Integer.parseInt(operatstr.substring(operatstr.indexOf("P")+1))-1]&(int)(Math.pow(2, index)))==(int)(Math.pow(2, index))){
								continue;
							}
							if(index>=0&&index<=4){
								allA=allA+(int) (Math.pow(2, index));
							}
						}else if(chs[i1]=='P'||chs[i1]=='p'){
							Config.pspfpaxle[Integer.parseInt(operatstr.substring(i1+1))-1]+=HexDecoding.int2byte(allA)[0];
							break;
						}
					}
				
			}else if(order_MOVE.contains("TWAIT")){
				if(!Config.list_timername.contains(operatstr)){
					Config.list_timername.add(operatstr);
				}
			}else if(order_MOVE.contains("CINC")||order_MOVE.contains("CDEC")||order_MOVE.contains("CCLR")||order_MOVE.contains("CSET")){
				operatstr=operatstr.split("=")[0];
				if(!Config.list_countername.contains(operatstr)){
					Config.list_countername.add(operatstr);
					}
					}
			
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
}	
public class MyAdapter extends BaseAdapter {
	private class buttonViewHolder {
		TextView name_p_setting;
		TextView foot_p_setting;
		TextView productBA_p_setting;
		TextView productUD_p_setting;
		TextView feedertroughBA_p_setting;
		TextView feedertroughUD_p_setting;

	}

	private ArrayList<HashMap<String, Object>> mAppList;
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] keyString;
	private int[] valueViewID;
	private buttonViewHolder holder;
	private int mLayoutID;
	private int mselectedItem = -1;
	private int id = -1;
	private boolean isFromClick = false;

	// MyAdapter的构造函数
	public MyAdapter(Context c, ArrayList<HashMap<String, Object>> appList,
			int layoutID, String[] from, int[] to) {
		mAppList = appList;
		mContext = c;
		mLayoutID = layoutID;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		keyString = new String[from.length];
		valueViewID = new int[to.length];

		System.arraycopy(from, 0, keyString, 0, from.length);
		System.arraycopy(to, 0, valueViewID, 0, to.length);
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
		mselectedItem = selectItem;

	}
	// 选中蓝色表示
	public void setSelectItemBlue(int selectedItem, int id) {
		this.mselectedItem = selectedItem;
		this.id = id;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		if (convertView != null) {
			holder = (buttonViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(mLayoutID, null);
			holder = new buttonViewHolder();
			holder.name_p_setting = (TextView) convertView
					.findViewById(valueViewID[0]);
			holder.foot_p_setting = (TextView) convertView
					.findViewById(valueViewID[1]);
			holder.productBA_p_setting = (TextView) convertView
					.findViewById(valueViewID[2]);
			holder.productUD_p_setting = (TextView) convertView
					.findViewById(valueViewID[3]);
			holder.feedertroughBA_p_setting = (TextView) convertView
					.findViewById(valueViewID[4]);
			holder.feedertroughUD_p_setting = (TextView) convertView
					.findViewById(valueViewID[5]);

			convertView.setTag(holder);
		}

		HashMap<String, Object> appInfo = mAppList.get(position);
		if (appInfo != null) {
			String name_p_setting =  appInfo.get(keyString[0]).toString();
			String foot_p_setting = appInfo.get(keyString[1]).toString();
			String productBA_p_setting = appInfo.get(keyString[2]).toString();
			String productUD_p_setting = appInfo.get(keyString[3]).toString();
			String feedertroughBA_p_setting =appInfo.get(keyString[4]).toString();
			String feedertroughUD_p_setting =  appInfo.get(keyString[5]).toString();

			holder.name_p_setting.setText(name_p_setting);
			holder.foot_p_setting.setText(foot_p_setting);
			holder.productBA_p_setting.setText(productBA_p_setting);
			holder.productUD_p_setting.setText(productUD_p_setting);
			holder.feedertroughBA_p_setting.setText(feedertroughBA_p_setting);
			holder.feedertroughUD_p_setting.setText(feedertroughUD_p_setting);
             /**
			 * 短按响应
			 */
			if(name_p_setting.startsWith("P")||name_p_setting.startsWith("p")){
				byte pspfpaxleFlagbyte=Config.pspfpaxle[Integer.parseInt(name_p_setting.substring(1,4))-1];
				if((pspfpaxleFlagbyte&0x01)==1){
					
					holder.foot_p_setting.setOnClickListener(new PListener(position));	
					if(isFromClick&&selectedItem== position&&holder.foot_p_setting.getId() ==selectedID )
						holder.foot_p_setting.setBackgroundColor(Color.BLUE);
					else
					holder.foot_p_setting.setBackgroundColor(Color.WHITE);		
					
				}else{
					holder.foot_p_setting.setOnClickListener(new GrayViewClickListener(position));	
					holder.foot_p_setting.setBackgroundColor(Color.GRAY);
				}
				
				if((pspfpaxleFlagbyte&0x02)==2){
					holder.productBA_p_setting.setOnClickListener(new PListener(position));
					if(isFromClick&&selectedItem== position&&holder.productBA_p_setting.getId() ==selectedID )
						holder.productBA_p_setting.setBackgroundColor(Color.BLUE);
					else
					holder.productBA_p_setting.setBackgroundColor(Color.WHITE);
				}else{
					holder.productBA_p_setting.setOnClickListener(new GrayViewClickListener(position));	
					holder.productBA_p_setting.setBackgroundColor(Color.GRAY);
				}
				
				if((pspfpaxleFlagbyte&0x04)==4){
					holder.productUD_p_setting.setOnClickListener(new PListener(position));
				    if(isFromClick&&selectedItem== position&&holder.productUD_p_setting.getId() ==selectedID )
						holder.productUD_p_setting.setBackgroundColor(Color.BLUE);
					else
						holder.productUD_p_setting.setBackgroundColor(Color.WHITE);
				}else{
					holder.productUD_p_setting.setOnClickListener(new GrayViewClickListener(position));	
					holder.productUD_p_setting.setBackgroundColor(Color.GRAY);
				}
			
				if((pspfpaxleFlagbyte&0x08)==8){
					holder.feedertroughBA_p_setting.setOnClickListener(new PListener(position));
					if(isFromClick&&selectedItem== position&&holder.feedertroughBA_p_setting.getId() ==selectedID )
						holder.feedertroughBA_p_setting.setBackgroundColor(Color.BLUE);
					else
					    holder.feedertroughBA_p_setting.setBackgroundColor(Color.WHITE);
				}else{
					holder.feedertroughBA_p_setting.setOnClickListener(new GrayViewClickListener(position));
					holder.feedertroughBA_p_setting.setBackgroundColor(Color.GRAY);
				}
			
				if((pspfpaxleFlagbyte&0x10)==16){
					holder.feedertroughUD_p_setting.setOnClickListener(new PListener(position));
					if(isFromClick&&selectedItem== position&&holder.feedertroughUD_p_setting.getId() ==selectedID )
						holder.feedertroughUD_p_setting.setBackgroundColor(Color.BLUE);
					else
						holder.feedertroughUD_p_setting.setBackgroundColor(Color.WHITE);
					
				}else{
					holder.feedertroughUD_p_setting.setOnClickListener(new GrayViewClickListener(position));
					holder.feedertroughUD_p_setting.setBackgroundColor(Color.GRAY);
				}
		
			holder.name_p_setting.setBackgroundColor(Color.GRAY);
		}else{
				holder.foot_p_setting.setOnClickListener(new PListener(position));
				holder.productBA_p_setting.setOnClickListener(new PListener(position));
				holder.productUD_p_setting.setOnClickListener(new PListener(position));
				holder.feedertroughBA_p_setting.setOnClickListener(new PListener(position));
				holder.feedertroughUD_p_setting.setOnClickListener(new PListener(position));

			
			/**
			 * 设置背景色，且让选中项蓝色显示,包括多选功能
			 */
			holder.name_p_setting.setBackgroundColor(Color.GRAY);
				if (mselectedItem==position&&selectedID != -1) {
					convertView.findViewById(selectedID).setBackgroundColor(
							Color.BLUE);
					for (int i = 1; i < valueViewID.length; i++) {
						if (selectedID != valueViewID[i]) {
							convertView.findViewById(valueViewID[i])
							.setBackgroundColor(Color.WHITE);
						}
					}					
				} else {
					holder.foot_p_setting.setBackgroundColor(Color.WHITE);
					holder.productBA_p_setting
					.setBackgroundColor(Color.WHITE);
					holder.productUD_p_setting
					.setBackgroundColor(Color.WHITE);
					holder.feedertroughBA_p_setting
					.setBackgroundColor(Color.WHITE);
					holder.feedertroughUD_p_setting
					.setBackgroundColor(Color.WHITE);
					convertView.setBackgroundColor(Color.BLACK);
				}

			}
	
			if(mselectedItem == position)
			{
				
				convertView.setBackgroundColor(Color.RED);
			}
			else
			{
				
				convertView.setBackgroundColor(Color.BLACK);
			}

		}
		return convertView;
	}


	/**
	 * 短按响应类，实现改变背景色，改变seekbar数值功能
	 * 
	 * @author shea
	 * 
	 */

	class GrayViewClickListener implements OnClickListener{
			private int position;
			GrayViewClickListener(int pos){
				position = pos;
			}

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BroadToHighLight(allpositionList_setting.get(position).toString());
			setSelectItem(position);
			notifyDataSetChanged();
			
			
		}
		
	}
	
	class PListener implements OnClickListener {
		private  int position;

		PListener(int pos) {
			position = pos;
		}

		@Override
			public void onClick(final View v) {
			final EditText etEditText = new EditText(getActivity());
			isFromClick = true;		
			tempV=v;
			tempposition=position;
			setSelectItemBlue(position, v.getId());
			selectedItem = position;
			selectedID = v.getId();
			pos = position;
			BroadToHighLight(allpositionList_setting.get(position).toString());	
			notifyDataSetChanged();
			
			
			
			// 限制只能输入0~9的数字和点号
			//设定位置，限制输入正负浮点数
				etEditText.setHint("支持格式为#####.#的正负数，整数最多5位，小数最多1位");
				etEditText.setKeyListener(new NumberKeyListener() {
					@Override
					protected char[] getAcceptedChars() {
						return new char[] { '1', '2', '3', '4', '5', '6', '7',
								'8', '9', '0', '.' ,'+','-'};
					}

					@Override
					public int getInputType() {
						return android.text.InputType.TYPE_CLASS_NUMBER;// 数字键盘
					}
				});
			/**
			 * 判断得到map中需要使用的key值
			 */
			String foot_String = "";
			switch (v.getId()) {
			case R.id.foot_p_setting:
				selectedKey = "foot_p_setting";
				//发送keycode
				KeyCodeSend.send(111, getActivity());
				foot_String =  mAppList.get(position).get(
						keyString[1]).toString();
				break;
			case R.id.productBA_p_setting:
				selectedKey = "productBA_p_setting";
				KeyCodeSend.send(112, getActivity());
				foot_String =  mAppList.get(position).get(
						keyString[2]).toString();
				break;
			case R.id.productUD_p_setting:
				selectedKey = "productUD_p_setting";
				KeyCodeSend.send(113, getActivity());
				foot_String =  mAppList.get(position).get(
						keyString[3]).toString();
				break;
			case R.id.feedertroughBA_p_setting:
				selectedKey = "feedertroughBA_p_setting";
				KeyCodeSend.send(114, getActivity());
				foot_String =  mAppList.get(position).get(
						keyString[4]).toString();
				break;
			case R.id.feedertroughUD_p_setting:
				selectedKey = "feedertroughUD_p_setting";
				KeyCodeSend.send(115, getActivity());
				foot_String =  mAppList.get(position).get(
						keyString[5]).toString();
				break;

			default:
				break;
			}
			// 初始化滑动条，调整设定值
			TextView t = (TextView) v;
			String valueString = t.getText().toString();// 设定位置
			

			etEditText.setText(foot_String);
			etEditText.setSelection(foot_String.length());// 设置光标位置
			new AlertDialog.Builder(getActivity())
			.setTitle("请添加设定值")
			.setView(etEditText)
			.setPositiveButton(R.string.OK,
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0,int arg1) {
					String editString = etEditText.getText().toString().trim();
					try{
					    Double.parseDouble(editString);
					}catch(Exception e){
						return;
					}
					double editDouble=0;

					if (!editString.equals("") ) {//不为空且不以点号开始，继续检查
                            //设定位置
							if (!editString.startsWith(".")) {
								if (editString.contains(".")) {
									String[] editStrings = editString.split("\\.");
									if (2 == editStrings.length) {
										editDouble = Double.parseDouble(editString);
										if (Math.abs(editDouble) >99999.9) {
											Toast.makeText(getActivity(),"数据超过范围，请重新输入",Toast.LENGTH_SHORT).show();
											editString = "";
											return;
										} else {
											editString = String.format("%1$5.1f",editDouble);
										}
									} else {
										Toast.makeText(getActivity(),"数据格式错误，请重新输入",Toast.LENGTH_SHORT).show();
										editString = "";
										return;
									}
								} else {
									editDouble = Double.parseDouble(editString);
									if (Math.abs(editDouble) >99999.9) {
										Toast.makeText(getActivity(),"数据超过范围，请重新输入",Toast.LENGTH_SHORT).show();
										editString = "";
										return;
									} else {
										editString = String.format("%1$5.1f",editDouble);
									}
								}

							} else {
								Toast.makeText(getActivity(),"数据格式错误，请重新输入",Toast.LENGTH_SHORT).show();
								editString = "";
								return;
							}


					} else {//数字为空
						Toast.makeText(getActivity(),"数据为空，请重新输入",Toast.LENGTH_SHORT).show();
						editString = "";
						return;
					}
					String valueString=editString;
					
					// 每次填写完后需要将数据发送给下位机，后才能更新UI
					//根据点的信息确定点在数组中的位置，确定发送地址
					String name_String =  mAppList.get(position).get(keyString[0]).toString();
					int send_address=0;
					//获取设定速度，加速度，减速度信息,填写数据
					byte[] temp=new byte[20];
					if(name_String.contains("SP")){
						send_address=AddressPublic.model_SP_point_Head
								+(AddressPublic.model_FP_point_Head-AddressPublic.model_SP_point_Head)/Define.MAX_STDPACK_NUM
								*(Integer.parseInt((name_String.split("\\s++")[0]).trim().substring(2))-1);
						//获取设定速度，加速度，减速度信息,填写数据
						 temp=new byte[20];
						//把界面上的值读一遍，填入8个字节的字节数组
						try {						
							//防止
							if (!mAppList.get(position).get("foot_p_setting").toString().equals("*****.*")) {
								System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("foot_p_setting").toString())*100)), 0, temp, 0, 4);	
							}
							if (!mAppList.get(position).get("productBA_p_setting").toString().equals("*****.*")) {
								System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("productBA_p_setting").toString())*100)), 0, temp, 4, 4);

							}
							if (!mAppList.get(position).get("productUD_p_setting").toString().equals("*****.*")) {
								System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("productUD_p_setting").toString())*100)), 0, temp, 8, 4);

							}
							if (!mAppList.get(position).get("feedertroughBA_p_setting").toString().equals("*****.*")) {
								System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("feedertroughBA_p_setting").toString())*100)), 0, temp, 12, 4);

							}
							if(!mAppList.get(position).get("feedertroughUD_p_setting").toString().equals("*****.*")){
								System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("feedertroughUD_p_setting").toString())*100)), 0, temp, 16, 4);

							}
							//根据设定值修改内容
							settingValueCopy(v, editString, position, temp);
						  } catch (Exception e) {
							// TODO: handle exception
						  }
					}else if(name_String.contains("FP")){
						send_address=0;
						String devicename=name_String.split("\\s++")[0];
						devicename=devicename.trim();
						if(devicename.equalsIgnoreCase("FP001")){
							send_address = 0x20003AB0;
						}else if(devicename.equalsIgnoreCase("FP002")){
							send_address = 0x20003AC6;
						}
						//获取设定速度，加速度，减速度信息,填写数据
						 temp=new byte[16];
						//把界面上的值读一遍，填入8个字节的字节数组
						try {						
							//防止
							if (!mAppList.get(position).get("foot_p_setting").toString().equals("")) {
								System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(mAppList.get(position).get("foot_p_setting").toString())*100)), 0, temp, 0,2);	
							}
							if (!mAppList.get(position).get("productBA_p_setting").toString().equals("")) {
								System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(mAppList.get(position).get("productBA_p_setting").toString())*100)), 0, temp, 2,2);

							}
							if (!mAppList.get(position).get("productUD_p_setting").toString().equals("")) {
								System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(mAppList.get(position).get("productUD_p_setting").toString())*100)), 0, temp, 4,2);

							}
							if (!mAppList.get(position).get("feedertroughBA_p_setting").toString().equals("")) {
								System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(mAppList.get(position).get("feedertroughBA_p_setting").toString())*100)), 0, temp, 6, 2);

							}
							if(!mAppList.get(position).get("feedertroughUD_p_setting").toString().equals("")){
								System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(mAppList.get(position).get("feedertroughUD_p_setting").toString())*100)), 0, temp, 8, 2);

							}
							//根据设定值修改内容
							settingValueCopy1(v, editString, position, temp);
						  } catch (Exception e) {
							// TODO: handle exception
						  }
					}else if(name_String.contains("P")){
						send_address=AddressPublic.model_P_point_Head
								+(AddressPublic.model_SP_point_Head-AddressPublic.model_P_point_Head)/Define.MAX_STDPOINT_NUM
								*(Integer.parseInt((name_String.split("\\s++")[0]).trim().substring(1))-1);
					//获取设定速度，加速度，减速度信息,填写数据
					 temp=new byte[20];
					//把界面上的值读一遍，填入8个字节的字节数组
					try {						
						//防止
						if (!mAppList.get(position).get("foot_p_setting").toString().equals("*****.*")) {
							System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("foot_p_setting").toString())*100)), 0, temp, 0, 4);	
						}
						if (!mAppList.get(position).get("productBA_p_setting").toString().equals("*****.*")) {
							System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("productBA_p_setting").toString())*100)), 0, temp, 4, 4);

						}
						if (!mAppList.get(position).get("productUD_p_setting").toString().equals("*****.*")) {
							System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("productUD_p_setting").toString())*100)), 0, temp, 8, 4);

						}
						if (!mAppList.get(position).get("feedertroughBA_p_setting").toString().equals("*****.*")) {
							System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("feedertroughBA_p_setting").toString())*100)), 0, temp, 12, 4);

						}
						if(!mAppList.get(position).get("feedertroughUD_p_setting").toString().equals("*****.*")){
							System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(mAppList.get(position).get("feedertroughUD_p_setting").toString())*100)), 0, temp, 16, 4);

						}
						//根据设定值修改内容
						settingValueCopy(v, editString, position, temp);
					  } catch (Exception e) {
						// TODO: handle exception
					  }
					}
					
					formatSendMessage=new WifiSendDataFormat(temp, send_address);

					tempV=v;
					tempeditString=editString;
					tempposition=PListener.this.position;		

					try {


						sendDataRunnable=new SendDataRunnable(formatSendMessage, getActivity());

						sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据发送完毕",backMessageToDo1);

						sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

						getActivity().runOnUiThread(sendDataRunnable);	
						


					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						//Toast.makeText(getActivity(), "请先连接主机", Toast.LENGTH_LONG).show();
					}
				}

			}).setNegativeButton(R.string.CANCEL, null).show();

			return;
		}

		private void settingValueCopy(View v, String editString,int position,byte[] temp) {
         try{
			HashMap<String, Object> map = new HashMap<String, Object>();

			//防止输入文本为空字段（空字段为0）
			if(editString==""){
				editString="0";
			}

			switch (v.getId()) {
			case R.id.foot_p_setting:
					System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 0, 4);
				break;
			case R.id.productBA_p_setting:
				System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 4, 4);
				break;
			case R.id.productUD_p_setting:
					System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 8,4);
				break;
			case R.id.feedertroughBA_p_setting:
				System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 12,4);
				break;
			case R.id.feedertroughUD_p_setting:
				System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 16,4);
				break;

			default:
				break;
			}

			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		private void settingValueCopy1(View v, String editString,int position,byte[] temp) {
         try{
			HashMap<String, Object> map = new HashMap<String, Object>();

			//防止输入文本为空字段（空字段为0）
			if(editString==""){
				editString="0";
			}

			switch (v.getId()) {
			case R.id.foot_p_setting:
					System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(editString)*100)), 0, temp,0, 2);
				break;
			case R.id.productBA_p_setting:
				System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(editString)*100)), 0, temp,2, 2);
				break;
			case R.id.productUD_p_setting:
					System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(editString)*100)), 0, temp,4,2);
				break;
			case R.id.feedertroughBA_p_setting:
				System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(editString)*100)), 0, temp,6,2);
				break;
			case R.id.feedertroughUD_p_setting:
				System.arraycopy(HexDecoding.int2byteArray2((int)(Float.parseFloat(editString)*100)), 0, temp,8,2);
				break;

			default:
				break;
			}

			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

}

}   

