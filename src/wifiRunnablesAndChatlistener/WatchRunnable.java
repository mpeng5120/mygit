package wifiRunnablesAndChatlistener;



import java.util.HashMap;

import wifiProtocol.WifiReadDataFormat;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.widget.ListView;

import com.dbutils.ArrayListBound;
import com.dbutils.Constans;
import com.explain.HexDecoding;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.programming.Device_ListFragment;
import com.tr.programming.Fragments_Device_ActualInput;
import com.tr.programming.Fragments_NCedit1;
import com.tr.programming.Fragments_NCedit2;
import com.tr.programming.Fragments_NCedit3;
import com.tr.programming.Fragments_NCedit4;
import com.tr.programming.Fragments_NCedit5;
import com.tr.programming.Fragments_NCedit6;
import com.tr.programming.Fragments_NCedit7;
import com.tr.programming.Fragments_NCedit8;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

/**
  当前nc程序执行的监视线程
  
  @author 李婷婷
  
 **/
public class WatchRunnable implements Runnable {

	// 暂时只要前五轴信息
	private WifiReadDataFormat formatReadMessage = new WifiReadDataFormat(
			0x1000B238, 4);
	private byte[] getData;
	private   boolean destroyPositionQueryFlag=false;
	private   boolean selfDestroy_flag=true;
	private   boolean chatlistener_flag = true;

	private Activity targetActivity;
	private int ncwatchindex;
	private int ncerrorFlag=0;
	private int[] alength;
	//public static ListView listView_nc;
	//private Fragment fragmentncStatusFragment;
	//public static HashMap<String, Fragment> fragmentListItem = new HashMap<String, Fragment>();
	private int and1;
	private int and2;
	private int and3;
	private int and4;
	private int and5;
	private int and6;
	private int and7;
	private int and8;
	private boolean IsFromMain= false;

	public WatchRunnable(Activity targetActivity, int[] alength) {
		
		this.targetActivity = targetActivity;
		this.alength = alength;
		and1=ArrayListBound.getNCeditList1Data().size();
		and2=and1+ArrayListBound.getNCeditList2Data().size();
		and3=and2+ArrayListBound.getNCeditList3Data().size();
		and4=and3+ArrayListBound.getNCeditList4Data().size();
		and5=and4+ArrayListBound.getNCeditList5Data().size();
		and6=and5+ArrayListBound.getNCeditList6Data().size();
		and7=and6+ArrayListBound.getNCeditList7Data().size();
		and8=and7+ArrayListBound.getNCeditList8Data().size();
	}
	

	public WatchRunnable(Activity targetActivity, int[] alength,boolean formMain) {
		
		this.targetActivity = targetActivity;
		this.alength = alength;
		and1=ArrayListBound.getNCeditList1Data().size();
		and2=and1+ArrayListBound.getNCeditList2Data().size();
		and3=and2+ArrayListBound.getNCeditList3Data().size();
		and4=and3+ArrayListBound.getNCeditList4Data().size();
		and5=and4+ArrayListBound.getNCeditList5Data().size();
		and6=and5+ArrayListBound.getNCeditList6Data().size();
		and7=and6+ArrayListBound.getNCeditList7Data().size();
		and8=and7+ArrayListBound.getNCeditList8Data().size();
		this.IsFromMain = formMain;
	}

	// 销毁该线程
	public  void destroy() {
		System.out.println("NC监视查询线程关闭");
		destroyPositionQueryFlag=true;
		selfDestroy_flag=false;
		chatlistener_flag = false;
	}

	public byte[] getData() {
		return getData;
	}

	public static void NCbeginWatch(ListView listView) {
	}

	//** 读取伺服参数时的通信线程收到信息时的处理函数. *//*
	private final ChatListener ReadDataFeedback = new ChatListener() {
		@Override
		public void onChat(byte[] message) {
			if (!chatlistener_flag)
				return;
			formatReadMessage.backMessageCheck(message);
			if (!formatReadMessage.finishStatus()) {
				try {
					Thread.sleep(20);
					WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(),ReadDataFeedback, targetActivity);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block 
					e.printStackTrace();
				}
			} else {
				getData = new byte[formatReadMessage.getLength()];
				System.arraycopy(message, 8, getData, 0,
						formatReadMessage.getLength());
				System.currentTimeMillis();
				
				Runnable pointShowRunnable = new Runnable() {
					@Override
					public void run() {
						try{
						int data = HexDecoding.Array4Toint(getData) - 0x08040000;

						for(int i = 0; i <alength.length; i++) {

							if(data > alength[i]&&i==(alength.length-1)&&ncerrorFlag==0){
								ncerrorFlag=1;
								new AlertDialog.Builder(targetActivity).setTitle("提示")
								   .setMessage("NC程序执行有误！")
								   .setPositiveButton("知道了", null).show();
							}
							if (data > alength[i]) {
								continue;
							} else {
									ncwatchindex = i;
								if(ncwatchindex<and1) {
									System.out.println("NC程序执行到页面1序号为" + (ncwatchindex)+ "这一行！");
									if(!IsFromMain){
										if(Device_ListFragment.j==0) //第一次进
										{										
											Fragments_NCedit1.mListView.requestFocusFromTouch();//模拟手触功能
											Fragments_NCedit1.mListView.setSelection(ncwatchindex);
											Fragments_NCedit1.NCedit_Adapter.setSelectItem(ncwatchindex);
											Fragments_NCedit1.NCedit_Adapter.notifyDataSetChanged();
										}
										if(Device_ListFragment.j!=0&&selfDestroy_flag){			
										
										targetActivity.getFragmentManager().beginTransaction().remove(Device_ListFragment.fragmentncStatusFragment).commit();
										targetActivity.getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,Device_ListFragment.fragmentListItem.get("NC_1"), Constans.FRAGMENT_NCString[0]).commit();
										Device_ListFragment.fragmentncStatusFragment=Device_ListFragment.fragmentListItem.get("NC_1");
										Device_ListFragment.listView_nc.setItemChecked(0, true);
										Device_ListFragment.j=0;
										}
										
										
									}
								}else if(ncwatchindex<and2){
									System.out.println("NC程序执行到页面2序号为" + (ncwatchindex-and1)+ "这一行！");
									if(Device_ListFragment.j!=1&&selfDestroy_flag){
									targetActivity.getFragmentManager().beginTransaction().remove(Device_ListFragment.fragmentncStatusFragment).commit();
									targetActivity.getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,Device_ListFragment.fragmentListItem.get("NC_2"), Constans.FRAGMENT_NCString[1]).commit();
									Device_ListFragment.fragmentncStatusFragment=Device_ListFragment.fragmentListItem.get("NC_2");
									Device_ListFragment.listView_nc.setItemChecked(1, true);
									Device_ListFragment.j=1;
									}
									Fragments_NCedit2.mListView.requestFocusFromTouch();//模拟手触功能
									Fragments_NCedit2.mListView.setSelection(ncwatchindex-and1);
									Fragments_NCedit2.NCedit_Adapter.setSelectItem(ncwatchindex-and1);
									Fragments_NCedit2.NCedit_Adapter.notifyDataSetChanged();
								}else if(ncwatchindex<and3){
									if(IsFromMain)
									{										
											TR_Main_Activity.WatchListView .requestFocusFromTouch();//模拟手触功能
											TR_Main_Activity.WatchListView.setSelection(ncwatchindex-and2);
											TR_Main_Activity.myProgramListAdapter.setSelectItem(ncwatchindex-and2);
											TR_Main_Activity.myProgramListAdapter.notifyDataSetChanged();
									}else
									{
										if(Device_ListFragment.j!=2&&selfDestroy_flag){
										targetActivity.getFragmentManager().beginTransaction().remove(Device_ListFragment.fragmentncStatusFragment).commit();
										targetActivity.getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,Device_ListFragment.fragmentListItem.get("NC_3"), Constans.FRAGMENT_NCString[2]).commit();
										Device_ListFragment.fragmentncStatusFragment=Device_ListFragment.fragmentListItem.get("NC_3");
										Device_ListFragment.listView_nc.setItemChecked(2, true);
										Device_ListFragment.j=2;
										}
										Fragments_NCedit3.mListView.requestFocusFromTouch();//模拟手触功能
										Fragments_NCedit3.mListView.setSelection(ncwatchindex-and2);
										Fragments_NCedit3.NCedit_Adapter.setSelectItem(ncwatchindex-and2);
										Fragments_NCedit3.NCedit_Adapter.notifyDataSetChanged();
									}
								}else if(ncwatchindex<and4){
									if(Device_ListFragment.j!=3&&selfDestroy_flag){
									targetActivity.getFragmentManager().beginTransaction().remove(Device_ListFragment.fragmentncStatusFragment).commit();
									targetActivity.getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,Device_ListFragment.fragmentListItem.get("NC_4"), Constans.FRAGMENT_NCString[3]).commit();
									Device_ListFragment.fragmentncStatusFragment=Device_ListFragment.fragmentListItem.get("NC_4");
									Device_ListFragment.listView_nc.setItemChecked(3, true);
									Device_ListFragment.j=3;
								    }
								    Fragments_NCedit4.mListView.requestFocusFromTouch();//模拟手触功能
								    Fragments_NCedit4.mListView.setSelection(ncwatchindex-and3);
								    Fragments_NCedit4.NCedit_Adapter.setSelectItem(ncwatchindex-and3);
								    Fragments_NCedit4.NCedit_Adapter.notifyDataSetChanged();
								}else if(ncwatchindex<and5){
									if(Device_ListFragment.j!=4&&selfDestroy_flag){
									targetActivity.getFragmentManager().beginTransaction().remove(Device_ListFragment.fragmentncStatusFragment).commit();
									targetActivity.getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,Device_ListFragment.fragmentListItem.get("NC_5"), Constans.FRAGMENT_NCString[4]).commit();
									Device_ListFragment.fragmentncStatusFragment=Device_ListFragment.fragmentListItem.get("NC_5");
									Device_ListFragment.listView_nc.setItemChecked(4, true);
									Device_ListFragment.j=4;
								    }
								    Fragments_NCedit5.mListView.requestFocusFromTouch();//模拟手触功能
								    Fragments_NCedit5.mListView.setSelection(ncwatchindex-and4);
								    Fragments_NCedit5.NCedit_Adapter.setSelectItem(ncwatchindex-and4);
								    Fragments_NCedit5.NCedit_Adapter.notifyDataSetChanged();
								}else if(ncwatchindex<and6){
									if(Device_ListFragment.j!=5&&selfDestroy_flag){
									targetActivity.getFragmentManager().beginTransaction().remove(Device_ListFragment.fragmentncStatusFragment).commit();
									targetActivity.getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,Device_ListFragment.fragmentListItem.get("NC_6"), Constans.FRAGMENT_NCString[5]).commit();
									Device_ListFragment.fragmentncStatusFragment=Device_ListFragment.fragmentListItem.get("NC_6");
									Device_ListFragment.listView_nc.setItemChecked(5, true);
									Device_ListFragment.j=5;
								    }
								    Fragments_NCedit6.mListView.requestFocusFromTouch();//模拟手触功能
								    Fragments_NCedit6.mListView.setSelection(ncwatchindex-and5);
								    Fragments_NCedit6.NCedit_Adapter.setSelectItem(ncwatchindex-and5);
								    Fragments_NCedit6.NCedit_Adapter.notifyDataSetChanged();
								}else if(ncwatchindex<and7){
									if(Device_ListFragment.j!=6&&selfDestroy_flag){
									targetActivity.getFragmentManager().beginTransaction().remove(Device_ListFragment.fragmentncStatusFragment).commit();
									targetActivity.getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,Device_ListFragment.fragmentListItem.get("NC_7"), Constans.FRAGMENT_NCString[6]).commit();
									Device_ListFragment.fragmentncStatusFragment=Device_ListFragment.fragmentListItem.get("NC_7");
									Device_ListFragment.listView_nc.setItemChecked(6, true);
									Device_ListFragment.j=6;
								    }
								    Fragments_NCedit7.mListView.requestFocusFromTouch();//模拟手触功能
								    Fragments_NCedit7.mListView.setSelection(ncwatchindex-and6);
								    Fragments_NCedit7.NCedit_Adapter.setSelectItem(ncwatchindex-and6);
								    Fragments_NCedit7.NCedit_Adapter.notifyDataSetChanged();
								}else if(ncwatchindex<and8){
									if(Device_ListFragment.j!=7&&selfDestroy_flag){
									targetActivity.getFragmentManager().beginTransaction().remove(Device_ListFragment.fragmentncStatusFragment).commit();
									targetActivity.getFragmentManager().beginTransaction().add(R.id.fragment_containerChild,Device_ListFragment.fragmentListItem.get("NC_8"), Constans.FRAGMENT_NCString[7]).commit();
									Device_ListFragment.fragmentncStatusFragment=Device_ListFragment.fragmentListItem.get("NC_8");
									Device_ListFragment.listView_nc.setItemChecked(7, true);
									Device_ListFragment.j=7;
								    }
								    Fragments_NCedit8.mListView.requestFocusFromTouch();//模拟手触功能
								    Fragments_NCedit8.mListView.setSelection(ncwatchindex-and7);
								    Fragments_NCedit8.NCedit_Adapter.setSelectItem(ncwatchindex-and7);
								    Fragments_NCedit8.NCedit_Adapter.notifyDataSetChanged();
								}
								break;
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block 
						e.printStackTrace();
					}
					}
					
				};
				
				if (targetActivity == null) {
					return;
				}
				targetActivity.runOnUiThread(pointShowRunnable);
			}
		}
	};
	@Override
	public void run() {
	try {
	while (!destroyPositionQueryFlag) {
		if(WifiSetting_Info.mClient!=null)
			while (WifiSetting_Info.blockFlag&&WifiSetting_Info.mClient!=null&&selfDestroy_flag) {
				System.currentTimeMillis();
				Thread.sleep(200);
				WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
			}
		}} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
