package wifiRunnablesAndChatlistener;

import java.io.IOException;
import wifiProtocol.WifiReadDataFormat;
import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;

import com.dataInAddress.AddressPublic;
import com.dataInAddress.Define;
import com.dataInAddress.PlcData;
import com.explain.HexDecoding;
import com.explain.TableToBinary;
import com.tr.R;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

/**
 * 复位状态查询
 * @author 李婷婷
 *
 */
public class CounterQueryRunnable implements Runnable {
	
	//整合好的plc地址加上偏移的地址（计数器）
	/*private WifiReadDataFormat formatReadMessage=new WifiReadDataFormat(
			AddressPublic.finalPlcData_Head+PlcData.getMCNToffsetAddress(0),
			4*Define.MAX_COUNTER_NUM );*/
	private WifiReadDataFormat formatReadMessage=new WifiReadDataFormat(0x40024000,4*Define.MAX_COUNTER_NUM );
	private byte[] getData;
	
	private static boolean selfDestroy_flag;
	
	
	private Activity targetActivity;
	private ListView listView;
	
	private int[] devicelist;
	private TextView[] deviceTextViews;
	private Runnable pointShowRunnable;
	
	//确保一次只能存在一个
	public static int existFlag=0;
	private static int flag=0;
	/**
	 * 构造函数
	 * @param targetActivity
	 * @param viewbeingChangedArrayList
	 */
	public CounterQueryRunnable(Activity targetActivity, ListView tempListView) {
		try{
		//System.out.println("计数器查询线程");
		existFlag++;
		System.out.println("计数器existFlag="+existFlag);
		selfDestroy_flag=true;
		
		this.targetActivity=targetActivity;
		
		listView=tempListView;
		
		
		devicelist=new int[listView.getLastVisiblePosition()-listView.getFirstVisiblePosition()+1];
		deviceTextViews=new TextView[devicelist.length];
		
		
		for (int j = 0; j < devicelist.length; j++) {
			devicelist[j]=TableToBinary.searchAddress(
						((TextView)(listView.getChildAt(j).findViewById(R.id.symbolText))).getText().toString().trim(),false);
		    //System.out.println("找到的计数器在平板的序号");
		}
		for (int j = 0; j < deviceTextViews.length; j++) {
			deviceTextViews[j]=((TextView)(listView.getChildAt(j).findViewById(R.id.currentValueText)));
		}
		pointShowRunnable=new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (int i = 0; i < devicelist.length; i++) {
					byte[] temp=new byte[4];
					System.arraycopy(getData, devicelist[i]*4, temp, 0, 4);
					
					deviceTextViews[i].setText(String.valueOf(HexDecoding.Array4Toint(temp)));

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
				
				targetActivity.runOnUiThread(pointShowRunnable);

			}
		}
	};
	
	//销毁该线程
	public static void destroy() {

			System.out.println("计数器线程关闭");
			
			existFlag=0;
			flag=0;
			selfDestroy_flag=false;
	}
	
	@Override
	public void run() {
		try {
		while (existFlag==1&&WifiSetting_Info.mClient!=null) {
			while (WifiSetting_Info.blockFlag&&selfDestroy_flag) {
				flag=1;
				Thread.sleep(170);
				WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
			}
		}
		if(existFlag!=1&&flag!=1){
			while(WifiSetting_Info.blockFlag&&selfDestroy_flag){
				flag=1;
				Thread.sleep(170);
				WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
            }
		}} catch (Exception e) {
			e.printStackTrace();
			destroy();
		}
	}

	
	
	
}
