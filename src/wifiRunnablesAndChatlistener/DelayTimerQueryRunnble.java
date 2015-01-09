package wifiRunnablesAndChatlistener;

import wifiProtocol.WifiReadDataFormat;

import android.R.integer;
import android.app.Activity;
import android.graphics.Color;
import android.widget.ListView;
import android.widget.TextView;

import com.dataInAddress.AddressPublic;
import com.dataInAddress.Define;
import com.explain.HexDecoding;
import com.explain.TableToBinary;
import com.tr.R;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

public class DelayTimerQueryRunnble implements Runnable {

	private ListView targetListView;
	private WifiReadDataFormat timeQueryDataFormat;
	private Activity targetActivity;
	private SendDataRunnable timeQueryRunnable;

	private static boolean destroyFlag;
	private static int selfexistFlag=0;

	private static int flag=0;
	private byte[] getData;
	private int[] idevicelist;
	private int[] odevicelist;
	private TextView[] ideviceTextViews;
	private TextView[] odeviceTextViews;
	/**
	 * 构造函数
	 */
	public DelayTimerQueryRunnble(Activity targetActivity,ListView targetListView) {
		try{
		// TODO Auto-generated constructor stub
		//System.out.println("构造IO监视");
		destroyFlag=true;
		selfexistFlag++;
		System.out.println("构造IO监视selfexistFlag="+selfexistFlag+"   "+destroyFlag+"   "+flag);
		this.targetActivity=targetActivity;
		this.targetListView=targetListView;

		if(targetActivity == null){
			System.out.println("targetActivity == null ");
			return;
		}
		if(targetListView == null){
			System.out.println("targetListView == null ");
			return;
		}
		timeQueryDataFormat=new WifiReadDataFormat(0x10007000, 32);
		idevicelist=new int[targetListView.getLastVisiblePosition()-targetListView.getFirstVisiblePosition()+1];
		odevicelist=new int[targetListView.getLastVisiblePosition()-targetListView.getFirstVisiblePosition()+1];
		ideviceTextViews=new TextView[idevicelist.length];
		odeviceTextViews=new TextView[odevicelist.length];
		
		for (int j = 0; j < idevicelist.length; j++) {
			    idevicelist[j]=Integer.parseInt(((TextView)(targetListView.getChildAt(j).findViewById(R.id.io_address))).getText().toString().trim());
			    odevicelist[j]=Integer.parseInt(((TextView)(targetListView.getChildAt(j).findViewById(R.id.io_address))).getText().toString().trim());;
				ideviceTextViews[j]=((TextView)(targetListView.getChildAt(j).findViewById(R.id.i_state)));
				odeviceTextViews[j]=((TextView)(targetListView.getChildAt(j).findViewById(R.id.o_state)));
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

	private ChatListener readDataFeedback=new ChatListener() {
		@Override
		public void onChat(byte[] message) {
			// TODO Auto-generated method stub
			timeQueryDataFormat.backMessageCheck(message);
			if(!timeQueryDataFormat.finishStatus()){
				try {
					Thread.sleep(20);
					WifiSetting_Info.mClient.sendMessage(timeQueryDataFormat.sendDataFormat(), readDataFeedback,targetActivity);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				//发送正确且完成
				//处理返回的数据	
				getData=new byte[timeQueryDataFormat.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(message, 8, getData, 0, timeQueryDataFormat.getLength());
				//对所收集到到的数据进行处理
			
				Runnable pointShowRunnable=new Runnable() {
					@Override
					public void run() {
						try{
						// TODO Auto-generated method stub
						for (int i = 0; i < idevicelist.length; i++) {
							
								byte[] itemp=new byte[1];
								System.arraycopy(getData, (idevicelist[i])/8, itemp, 0, 1);
							    if(((int)((itemp[0]>>(idevicelist[i]%8))&0x01))==1){
							    	ideviceTextViews[i].setText("ON");
									ideviceTextViews[i].setBackgroundColor(Color.RED);
							    }else{
							    	//System.out.println("输入设备OFF");
							    	ideviceTextViews[i].setText("OFF");
									ideviceTextViews[i].setBackgroundColor(Color.GRAY);
							    }
						
								byte[] otemp=new byte[1];
							    System.arraycopy(getData, (odevicelist[i])/8+16, otemp, 0, 1);
							    if(((int)((otemp[0]>>(odevicelist[i]%8))&0x01))==1){
							    	odeviceTextViews[i].setText("ON");
									odeviceTextViews[i].setBackgroundColor(Color.RED);
							    }else{
							    	//System.out.println("输出设备OFF");
							    	odeviceTextViews[i].setText("OFF");
									odeviceTextViews[i].setBackgroundColor(Color.GRAY);
							    }
							
							
						}} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				 if( targetActivity != null )
					targetActivity.runOnUiThread(pointShowRunnable);
			}
		}
	};
	/**
	 * 线程关闭函数
	 */
	public static void destroy() {
		
		destroyFlag=false;
		flag=0;
		selfexistFlag=0;
		System.out.println("关闭IO监视");
	}
	
	@Override
	public void run() {
		try {
		while (selfexistFlag==1&&WifiSetting_Info.mClient!=null) {
			while(destroyFlag){
				flag=1;
				Thread.sleep(150);
				WifiSetting_Info.mClient.sendMessage(timeQueryDataFormat.sendDataFormat(),readDataFeedback,targetActivity);
             }
		}
		if(selfexistFlag!=1&&flag!=1){
			while(destroyFlag){
				flag=1;
				Thread.sleep(150);
				WifiSetting_Info.mClient.sendMessage(timeQueryDataFormat.sendDataFormat(),readDataFeedback,targetActivity);
             }
		}} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
