package wifiRunnablesAndChatlistener;

import wifiProtocol.WifiReadDataFormat;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import com.explain.TableToBinary;
import com.tr.R;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

/**
 * 复位状态查询
 * @author 李婷婷
 *
 */
public class DelayoptionQueryRunnble implements Runnable {
	
	//整合好的plc地址加上偏移的地址（计数器）
	/*private WifiReadDataFormat formatReadMessage=new WifiReadDataFormat(
			AddressPublic.finalPlcData_Head+PlcData.getMCNToffsetAddress(0),
			4*Define.MAX_COUNTER_NUM );*/
	private WifiReadDataFormat formatReadMessage=new WifiReadDataFormat(0x10007000, 32);
	private byte[] getData;
	
	private static boolean selfDestroy_flag;
	private static boolean destroyPositionQueryFlag;
	
	
	private Activity targetActivity;
	private ListView listView;
	
	private int[] idevicelist1;
	private int[] odevicelist1;
	private TextView[] ideviceTextViews1;
	private TextView[] odeviceTextViews1;
	
	private Runnable pointShowRunnable;	

	/**
	 * 构造函数
	 * @param targetActivity
	 * @param viewbeingChangedArrayList
	 */
	public DelayoptionQueryRunnble(Activity targetActivity, ListView tempListView) {
		try{
		//System.out.println("计数器查询线程");
		selfDestroy_flag=true;		
		destroyPositionQueryFlag = true;		
		this.targetActivity=targetActivity;		
		listView=tempListView;
		
		idevicelist1=new int[listView.getLastVisiblePosition()-listView.getFirstVisiblePosition()+1];
		odevicelist1=new int[listView.getLastVisiblePosition()-listView.getFirstVisiblePosition()+1];
		ideviceTextViews1=new TextView[idevicelist1.length];
		odeviceTextViews1=new TextView[odevicelist1.length];
		
		for (int j = 0; j < idevicelist1.length; j++) {
			
			if((((TextView)(listView.getChildAt(j).findViewById(R.id.isymbol1))).getText().toString().trim()).equals("")){
				idevicelist1[j]=0xffff;
			}else{
				idevicelist1[j]=TableToBinary.searchAddress(((TextView)(listView.getChildAt(j).findViewById(R.id.isymbol1))).getText().toString().trim(),false);
			}
			if((((TextView)(listView.getChildAt(j).findViewById(R.id.osymbol1))).getText().toString().trim()).equals("")){
				odevicelist1[j]=0xffff;
			}else{
				odevicelist1[j]=TableToBinary.searchAddress(((TextView)(listView.getChildAt(j).findViewById(R.id.osymbol1))).getText().toString().trim(),false);
			}
			ideviceTextViews1[j]=((TextView)(listView.getChildAt(j).findViewById(R.id.isymbol1)));
			odeviceTextViews1[j]=((TextView)(listView.getChildAt(j).findViewById(R.id.osymbol1)));	
		}
		pointShowRunnable=new Runnable() {
			@Override
			public void run() {
				try{
					// TODO Auto-generated method stub
					for (int i = 0; i < idevicelist1.length; i++) {
						if(idevicelist1[i]==0xffff){
							ideviceTextViews1[i].setBackgroundColor(Color.GRAY);
							//break;
						}else{
							byte[] itemp1=new byte[1];
							System.arraycopy(getData, (idevicelist1[i])/8, itemp1, 0, 1);
						    if(((int)((itemp1[0]>>(idevicelist1[i]%8))&0x01))==1){
								ideviceTextViews1[i].setBackgroundColor(Color.RED);
						    }else{
								ideviceTextViews1[i].setBackgroundColor(Color.GRAY);
						    }
						}
						if(odevicelist1[i]==0xffff){
							odeviceTextViews1[i].setBackgroundColor(Color.GRAY);
							//break;
						}else{
							byte[] otemp1=new byte[1];
						    System.arraycopy(getData, (odevicelist1[i])/8+16, otemp1, 0, 1);
						    if(((int)((otemp1[0]>>(odevicelist1[i]%8))&0x01))==1){
								odeviceTextViews1[i].setBackgroundColor(Color.RED);
						    }else{
								odeviceTextViews1[i].setBackgroundColor(Color.GRAY);
						    }
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	
	
	
	
	
	/** 读取伺服参数时的通信线程收到信息时的处理函数. */
	private final ChatListener ReadDataFeedback = new ChatListener() {
		@Override
		public void onChat(byte[] message) {

			//返回的标志位STS1处的判断以及和校验
			formatReadMessage.backMessageCheck(message);
			
			if(!formatReadMessage.finishStatus()){

				WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
			
			}else {
				//发送正确且完成
				//处理返回的数据	
				getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(formatReadMessage.getFinalData(),0, getData, 0, formatReadMessage.getLength());
				//对所收集到到的数据进行处理
				if(pointShowRunnable!=null&&targetActivity!=null)
					targetActivity.runOnUiThread(pointShowRunnable);

			}
		}
	};
	
	//销毁该线程
	public static void destroy() {
			System.out.println("计数器线程关闭");		
			selfDestroy_flag=false;
			destroyPositionQueryFlag = false;
	}
	
	@Override
	public void run() {
		try {
			while (selfDestroy_flag) {
				if(WifiSetting_Info.LOCK == null)
					WifiSetting_Info.LOCK = new Object();
				synchronized(WifiSetting_Info.LOCK)
				{
					WifiSetting_Info.LOCK.notify();
					try {
						if (WifiSetting_Info.blockFlag&&selfDestroy_flag&&WifiSetting_Info.mClient!=null) {
							Thread.sleep(170);
							Log.d("mpeng","run~~~~~~delay~~~~~~~~~");
							WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
						}
					} catch (Exception e) {
						e.printStackTrace();
						destroy();
					}						
						 WifiSetting_Info.LOCK.wait();					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			destroy();
		}
	}

	
	
	
}
