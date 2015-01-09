package com.tr.programming;



import java.util.HashMap;

import wifiRunnablesAndChatlistener.WatchRunnable;

import com.dbutils.Constans;
import com.tr.R;
import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ActionBar.TabListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Device_ListFragment extends ListFragment implements TabListener{

	private   Fragment fragmentDeviceStatusFragment;//设备定义最当前的状态
	public static  Fragment fragmentncStatusFragment;
	private   Fragment fragmenttableStatusFragment;
	//private  static Fragment fragmentShowing;//保存当前存储的nc编辑和table编辑
	public static ListView listView_device;
	public static ListView listView_nc;
	public static ListView listView_table;
	public static HashMap<String, Fragment> fragmentListItem = new HashMap<String, Fragment>();
	int i=0;
	public static int j=1;
	int k=0;
	public static boolean firstOpen = false;
/*	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try{
		fragmentDeviceStatusFragment=null;
		fragmentncStatusFragment=null;
		fragmenttableStatusFragment=null;
		fragmentShowing=null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
	

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("Device_ListFragment  onActivityCreated");
		
		//fragmentListItem.put("Table_Edit", new Fragments_Table());
		//fragmentListItem.put("NC_Edit", new Fragments_NCedit());
		try{
		fragmentListItem.put("Device_ActualInput", new Fragments_Device_ActualInput());
		fragmentListItem.put("Device_ActualOutput", new Fragments_Device_ActualOutput());
		fragmentListItem.put("Device_MiddleInput", new Fragments_Device_MiddleInput());
		fragmentListItem.put("Device_MiddleOutput", new Fragments_Device_MiddleOutput());
		fragmentListItem.put("Device_DataInput", new Fragments_Device_DataInput());
		fragmentListItem.put("Device_DataOutput", new Fragments_Device_DataOutput());
		fragmentListItem.put("Device_Register", new Fragments_Device_Register());
		fragmentListItem.put("Device_DataRegister", new Fragments_Device_DataRegister());
		fragmentListItem.put("Device_Timer", new Fragments_Device_Timer());
		fragmentListItem.put("Device_Counter", new Fragments_Device_Counter());		
		fragmentListItem.put("Device_Position", new Fragments_Device_Position());
		fragmentListItem.put("Device_Alarm", new Fragments_Device_Alarm());
		fragmentListItem.put("Device_Optional", new Fragments_Device_Optional());
		
		fragmentListItem.put("NC_1", new Fragments_NCedit1());
		fragmentListItem.put("NC_2", new Fragments_NCedit2());
		fragmentListItem.put("NC_3", new Fragments_NCedit3());
		fragmentListItem.put("NC_4", new Fragments_NCedit4());
		fragmentListItem.put("NC_5", new Fragments_NCedit5());
		fragmentListItem.put("NC_6", new Fragments_NCedit6());
		fragmentListItem.put("NC_7", new Fragments_NCedit7());
		fragmentListItem.put("NC_8", new Fragments_NCedit8());

		fragmentListItem.put("Table_1", new Fragments_Table1());
		fragmentListItem.put("Table_2", new Fragments_Table2());
		fragmentListItem.put("Table_3", new Fragments_Table3());
		fragmentListItem.put("Table_4", new Fragments_Table4());
		fragmentListItem.put("Table_5", new Fragments_Table5());
		fragmentListItem.put("Table_6", new Fragments_Table6());
		fragmentListItem.put("Table_7", new Fragments_Table7());
		fragmentListItem.put("Table_8", new Fragments_Table8());

//		getActivity().getActionBar().setDisplayShowTitleEnabled(false);// 不显示app名
//		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


//		getActivity().getActionBar().addTab( getActivity().getActionBar().newTab().setText(getString(R.string.programming_deviceDefine)).setTabListener(this));
		getActivity().getActionBar().addTab( getActivity().getActionBar().newTab().setText(R.string.programming_deviceDefine).setTabListener(this));
		getActivity().getActionBar().addTab( getActivity().getActionBar().newTab().setText(R.string.programming_ncEdit).setTabListener(this));
		getActivity().getActionBar().addTab( getActivity().getActionBar().newTab().setText(R.string.programming_tableEdit).setTabListener(this));
//		getActivity().getActionBar().addTab( getActivity().getActionBar().newTab().setText("设备定义").setTabListener(this));
//		getActivity().getActionBar().addTab( getActivity().getActionBar().newTab().setText("NC编辑").setTabListener(this));
//		getActivity().getActionBar().addTab( getActivity().getActionBar().newTab().setText("Table编辑").setTabListener(this));
		//showdeviceDefine();
	}catch(Exception e){
		e.printStackTrace();
	}
	}

//	/*
//	 * 获取listview的内容
//	 */
//	public void populateTitles() {
//		String[] items = getResources().getStringArray(R.array.programming_array_deviceDefineList);
//		// Convenience method to attach an adapter to ListFragment's ListView
//		setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.listitem_devicetitle, getResources().getStringArray(R.array.programming_array_deviceDefineList)));
//
//	}

	public void showdeviceDefine(){
		try{
		listView_device = getListView();
		listView_device.setCacheColorHint(Color.TRANSPARENT);

		// Highlight the currently selected item
		listView_device.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

//		populateTitles();
		final String[] deviceDefineList = getResources().getStringArray(R.array.programming_array_deviceDefineList);
		setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.listitem_devicetitle, deviceDefineList));//设备定义的数组
		//初始化界面
		listView_device.setItemChecked(0, true);
		if (fragmentDeviceStatusFragment==null) {  
			fragmentDeviceStatusFragment = fragmentListItem.get("Device_ActualInput");  
        }  
		getFragmentManager().beginTransaction().add(R.id.fragment_containerChild, fragmentDeviceStatusFragment).commit();
		listView_device.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int number,long arg3) {
				try{
				switch (Constans.device_defineEnum.getDevice(deviceDefineList[number])) {
				case  实际输入:
					if (i!=0) {	//i防止重复点击出错				
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_ActualInput"), Constans.FRAGMENT_DeviceString[0]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_ActualInput");		
						i=0;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
					}
					break;									
				case  实际输出:
					if (i!=1) {						
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_ActualOutput"), Constans.FRAGMENT_DeviceString[1]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_ActualOutput");
						i=1;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
					}
					break;										
				case  中间输入:
					if (i!=2) {						
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_MiddleInput"), Constans.FRAGMENT_DeviceString[2]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_MiddleInput");
						i=2;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
					}
					break;										
				case  中间输出:
					if (i!=3) {						
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_MiddleOutput"), Constans.FRAGMENT_DeviceString[3]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_MiddleOutput");
						i=3;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;						
				case  数据输入:
					if (i!=4) {						
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_DataInput"), Constans.FRAGMENT_DeviceString[4]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_DataInput");
						i=4;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;						
				case  数据输出:
					if (i!=5) {						
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_DataOutput"), Constans.FRAGMENT_DeviceString[5]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_DataOutput");
						i=5;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;						
				case  掉电可保持寄存器:
					if (i!=6) {						
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_Register"), Constans.FRAGMENT_DeviceString[6]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_Register");
						i=6;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;						
				case  掉电可保持数据寄存器:
					if (i!=7) {						
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_DataRegister"), Constans.FRAGMENT_DeviceString[7]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_DataRegister");
						i=7;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;						
				case  定时器:
					if (i!=8) {						
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_Timer"), Constans.FRAGMENT_DeviceString[8]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_Timer");
						i=8;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;						
				case   计数器:
					if (i!=9) {
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_Counter"), Constans.FRAGMENT_DeviceString[9]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_Counter");
						i=9;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;					
				case  位置:
					if (i!=10) {
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_Position"), Constans.FRAGMENT_DeviceString[10]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_Position");
						i=10;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;
				case  警报:
					if (i!=11) {
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_Alarm"), Constans.FRAGMENT_DeviceString[11]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_Alarm");
						i=11;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;
				case  选件操作:
					if (i!=12) {
						getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Device_Optional"), Constans.FRAGMENT_DeviceString[12]).commit();
						fragmentDeviceStatusFragment=fragmentListItem.get("Device_Optional");
						i=12;
						if(TR_Programming_Activity.menu==null){
							return;
						}
						break;
						}
						break;
				default:
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			}

		});
	}catch(Exception e){
		e.printStackTrace();
	}
	}

	public void showncDefine(){
		try{
		listView_nc = getListView();
		listView_nc.setCacheColorHint(Color.TRANSPARENT);
		listView_nc.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		final String[] ncDefineList = getResources().getStringArray(R.array.programming_array_ncDefineList);
		setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.listitem_nctitle, ncDefineList));//nc的数组
		listView_nc.setItemChecked(1, true);
		
		if (fragmentncStatusFragment==null) {  
			fragmentncStatusFragment = fragmentListItem.get("NC_2");  
        }  
		getFragmentManager().beginTransaction().add(R.id.fragment_containerChild, fragmentncStatusFragment,Constans.FRAGMENT_NCString[j]).commit();    

        
		
		// Enable drag and dropping
		listView_nc.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int number,long arg3) {
				try{
				switch (Constans.nc_defineEnum.getDevice(ncDefineList[number])) {
				case  原点:
					if (j!=0) {	//j防止重复点击出错				
						getFragmentManager().beginTransaction().remove(fragmentncStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("NC_1"), Constans.FRAGMENT_NCString[0]).commit();
						fragmentncStatusFragment=fragmentListItem.get("NC_1");		
						j=0;
						break;
					}
					break;									
				case  主进程:
					if (j!=1) {						
						getFragmentManager().beginTransaction().remove(fragmentncStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("NC_2"), Constans.FRAGMENT_NCString[1]).commit();
						fragmentncStatusFragment=fragmentListItem.get("NC_2");
						j=1;
						break;
					}
					break;										
				case  进程1:
					if (j!=2) {						
						getFragmentManager().beginTransaction().remove(fragmentncStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("NC_3"), Constans.FRAGMENT_NCString[2]).commit();
						fragmentncStatusFragment=fragmentListItem.get("NC_3");
						j=2;
						break;
					}
					break;										
				case  进程2:
					if (j!=3) {						
						getFragmentManager().beginTransaction().remove(fragmentncStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("NC_4"), Constans.FRAGMENT_NCString[3]).commit();
						fragmentncStatusFragment=fragmentListItem.get("NC_4");
						j=3;
						break;
						}
						break;						
				case  进程3:
					if (j!=4) {						
						getFragmentManager().beginTransaction().remove(fragmentncStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("NC_5"), Constans.FRAGMENT_NCString[4]).commit();
						fragmentncStatusFragment=fragmentListItem.get("NC_5");
						j=4;
						break;
						}
						break;						
				case  进程4:
					if (j!=5) {						
						getFragmentManager().beginTransaction().remove(fragmentncStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("NC_6"), Constans.FRAGMENT_NCString[5]).commit();
						fragmentncStatusFragment=fragmentListItem.get("NC_6");
						j=5;
						break;
						}
						break;						
				case  进程5:
					if (j!=6) {						
						getFragmentManager().beginTransaction().remove(fragmentncStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("NC_7"), Constans.FRAGMENT_NCString[6]).commit();
						fragmentncStatusFragment=fragmentListItem.get("NC_7");
						j=6;
						break;
						}
						break;						
				case  进程6:
					if (j!=7) {						
						getFragmentManager().beginTransaction().remove(fragmentncStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("NC_8"), Constans.FRAGMENT_NCString[7]).commit();
						fragmentncStatusFragment=fragmentListItem.get("NC_8");
						j=7;
						break;
						}
						break;						
				default:
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			}

		});
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
	public void showtableDefine(){
		try{
		listView_table = getListView();
		listView_table.setCacheColorHint(Color.TRANSPARENT);
		listView_table.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		final String[] tableDefineList = getResources().getStringArray(R.array.programming_array_tableDefineList);
		setListAdapter(new ArrayAdapter<String>(getActivity(),R.layout.listitem_tabletitle, tableDefineList));//table的数组
		listView_table.setItemChecked(0, true);
		
		if (fragmenttableStatusFragment==null) {  
			fragmenttableStatusFragment = fragmentListItem.get("Table_1");  
        }  
		getFragmentManager().beginTransaction().add(R.id.fragment_containerChild, fragmenttableStatusFragment,Constans.FRAGMENT_TableString[k]).commit();    

        
		
		// Enable drag and dropping
		listView_table.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int number,long arg3) {
				try{
				switch (Constans.table_defineEnum.getDevice(tableDefineList[number])) {
				case  T编辑1:
					if (k!=0) {	//i防止重复点击出错		
						if(firstOpen){
							TR_Programming_Activity.watchRunnable.destroy();
						}
						getFragmentManager().beginTransaction().remove(fragmenttableStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Table_1"), Constans.FRAGMENT_TableString[0]).commit();
						fragmenttableStatusFragment=fragmentListItem.get("Table_1");		
						k=0;
						break;
					}
					break;									
				case  T编辑2:
					if (k!=1) {						
						getFragmentManager().beginTransaction().remove(fragmenttableStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Table_2"), Constans.FRAGMENT_TableString[1]).commit();
						fragmenttableStatusFragment=fragmentListItem.get("Table_2");
						k=1;
						break;
					}
					break;										
				case  T编辑3:
					if (k!=2) {						
						getFragmentManager().beginTransaction().remove(fragmenttableStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Table_3"), Constans.FRAGMENT_TableString[2]).commit();
						fragmenttableStatusFragment=fragmentListItem.get("Table_3");
						k=2;
						break;
					}
					break;										
				case  T编辑4:
					if (k!=3) {						
						getFragmentManager().beginTransaction().remove(fragmenttableStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Table_4"), Constans.FRAGMENT_TableString[3]).commit();
						fragmenttableStatusFragment=fragmentListItem.get("Table_4");
						k=3;
						break;
						}
						break;						
				case  T编辑5:
					if (k!=4) {						
						getFragmentManager().beginTransaction().remove(fragmenttableStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Table_5"), Constans.FRAGMENT_TableString[4]).commit();
						fragmenttableStatusFragment=fragmentListItem.get("Table_5");
						k=4;
						break;
						}
						break;						
				case  T编辑6:
					if (k!=5) {						
						getFragmentManager().beginTransaction().remove(fragmenttableStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Table_6"), Constans.FRAGMENT_TableString[5]).commit();
						fragmenttableStatusFragment=fragmentListItem.get("Table_6");
						k=5;
						break;
						}
						break;						
				case  T编辑7:
					if (k!=6) {						
						getFragmentManager().beginTransaction().remove(fragmenttableStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Table_7"), Constans.FRAGMENT_TableString[6]).commit();
						fragmenttableStatusFragment=fragmentListItem.get("Table_7");
						k=6;
						break;
						}
						break;						
				case  T编辑8:
					if (k!=7) {						
						getFragmentManager().beginTransaction().remove(fragmenttableStatusFragment).commit();
						getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,fragmentListItem.get("Table_8"), Constans.FRAGMENT_TableString[7]).commit();
						fragmenttableStatusFragment=fragmentListItem.get("Table_8");
						k=7;
						break;
						}
						break;						
				default:
					break;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			}

		});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO 自动生成的方法存根
		try{
		if(tab.getText().equals(getString(R.string.programming_deviceDefine))){	
			if(firstOpen){
				TR_Programming_Activity.watchRunnable.destroy();
				System.out.println("programming_deviceDefine watchRunnable.destroy11111111");
			}
			getFragmentManager().beginTransaction().show(getFragmentManager().findFragmentById(R.id.titles_frag)).commit();
			showdeviceDefine();
			for(int m=0;m<13;m++){
			if(i==m){
				listView_device.setItemChecked(i, true);
			 }
			}
		
			
			if(TR_Programming_Activity.menu==null){
				return;
			}
			TR_Programming_Activity.menu.findItem(R.id.menu_note).setVisible(false);
			TR_Programming_Activity.menu.findItem(R.id.menu_download).setVisible(false);
			TR_Programming_Activity.menu.findItem(R.id.menu_watchStart).setVisible(false);
			TR_Programming_Activity.menu.findItem(R.id.menu_watchStop).setVisible(false);
			TR_Programming_Activity.menu.findItem(R.id.menu_saveCache).setEnabled(true);
			System.out.println("TR_Programming_Activity.menu.findItem(R.id.menu_watchStart).setVisible(false);333");
		}else if(tab.getText().equals(getString(R.string.programming_ncEdit))){
			getFragmentManager().beginTransaction().show(getFragmentManager().findFragmentById(R.id.titles_frag)).commit();
			showncDefine();
			for(int m=0;m<8;m++){
				if(j==m){
					listView_nc.setItemChecked(j, true);
				 }
				}
			TR_Programming_Activity.menu.findItem(R.id.menu_note).setVisible(true);
			TR_Programming_Activity.menu.findItem(R.id.menu_download).setVisible(true);
			TR_Programming_Activity.menu.findItem(R.id.menu_watchStart).setVisible(true);
			TR_Programming_Activity.menu.findItem(R.id.menu_watchStop).setVisible(false);
			TR_Programming_Activity.menu.findItem(R.id.menu_saveCache).setEnabled(true);
			TR_Programming_Activity.menu.findItem(R.id.menu_download).setEnabled(true);
		}else if (tab.getText().equals(getString(R.string.programming_tableEdit))) {
			if(firstOpen){
				TR_Programming_Activity.watchRunnable.destroy();
				System.out.println("programming_tableEdit watchRunnable.destroy22222222");
			}
			getFragmentManager().beginTransaction().show(getFragmentManager().findFragmentById(R.id.titles_frag)).commit();
			showtableDefine();
			for(int m=0;m<8;m++){
				if(k==m){
					listView_table.setItemChecked(k, true);
				 }
				}
			
			if(TR_Programming_Activity.menu==null){
				return;
			}
			TR_Programming_Activity.menu.findItem(R.id.menu_note).setVisible(true);
			TR_Programming_Activity.menu.findItem(R.id.menu_download).setVisible(true);
			TR_Programming_Activity.menu.findItem(R.id.menu_watchStart).setVisible(true);
			TR_Programming_Activity.menu.findItem(R.id.menu_watchStop).setVisible(false);
			TR_Programming_Activity.menu.findItem(R.id.menu_saveCache).setEnabled(true);
			TR_Programming_Activity.menu.findItem(R.id.menu_download).setEnabled(true);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO 自动生成的方法存根
		try{
		/*if(fragmentShowing!=null){
			getFragmentManager().beginTransaction().remove(fragmentShowing).commit();
		}*/
		if(fragmentDeviceStatusFragment!=null){
			getFragmentManager().beginTransaction().remove(fragmentDeviceStatusFragment).commit();
		}
		if(fragmentncStatusFragment!=null){
			getFragmentManager().beginTransaction().remove(fragmentncStatusFragment).commit();
		}
		if(fragmenttableStatusFragment!=null){
			getFragmentManager().beginTransaction().remove(fragmenttableStatusFragment).commit();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
	}


}
