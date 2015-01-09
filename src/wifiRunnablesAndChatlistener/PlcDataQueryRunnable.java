package wifiRunnablesAndChatlistener;

import android.app.Activity;
import android.widget.Toast;
import awesomeWatch.TableWatch;

import com.dataInAddress.AddressPublic;
import com.dataInAddress.PlcData;
import com.explain.HexDecoding;
import com.tr.programming.Fragments_Table1;
import com.tr.programming.Fragments_Table2;
import com.tr.programming.Fragments_Table3;
import com.tr.programming.Fragments_Table4;
import com.tr.programming.Fragments_Table5;
import com.tr.programming.Fragments_Table6;
import com.tr.programming.Fragments_Table7;
import com.tr.programming.Fragments_Table8;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

import wifiProtocol.WifiReadMassiveDataFormat;

/**
 * plcData查询，用于table监视
 * 
 * @author 李婷婷
 * 
 */
public class PlcDataQueryRunnable implements Runnable {

	private WifiReadMassiveDataFormat formatReadMessage = new WifiReadMassiveDataFormat(
			AddressPublic.finalPlcData_Head, PlcData.End - PlcData.IN_Head);

	private Activity targetActivity;
	private byte[] getData;

	private long beforeTime;
	private long afterTime;

	private static boolean destroyflag = true;
	private boolean alreadygetMsg = true;
	private TableWatch tempTableWatch ;
	/**
	 * 构造函数
	 * 
	 * @param tarActivity
	 */
	public PlcDataQueryRunnable(Activity tarActivity,int flag) {
		targetActivity = tarActivity;
		tempTableWatch = new TableWatch(targetActivity,flag);
		destroyflag = true;
	}

	public static void destroy() {
		destroyflag = false;
		WifiSetting_Info.blockFlag = true;
	}

	private final ChatListener ReadDataFeedback = new ChatListener() {
		@Override
		public void onChat(byte[] message) {
			if (!destroyflag)
				return;
			// 返回的标志位STS1处的判断以及和校验
			formatReadMessage.backMessageCheck(message);
			
		//	HexDecoding.printHexString("每次返回的PLCdata数据", message);
			
			if (!formatReadMessage.finishStatus()) {
				try {
					WifiSetting_Info.mClient.sendHighPriorMessage(formatReadMessage.sendDataFormat(), targetActivity,ReadDataFeedback);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				afterTime = System.currentTimeMillis();
				//System.out.println("PLC查询时间耗费" + (afterTime - beforeTime));
				// 发送正确且完成
				// 处理返回的数据
				getData = new byte[formatReadMessage.getLength()];
				// 获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(formatReadMessage.getFinalData(), 0, getData,
						0, formatReadMessage.getLength());
				// 对所收集到到的数据进行处理

				targetActivity.runOnUiThread(backMessageTodoRunnable);
				
			}
		}
	};

	private Runnable backMessageTodoRunnable = new Runnable() {
		@Override
		public void run() {
			try{
			// 处理数据，改变界面
			tempTableWatch.devicePlustheirValue(getData);
			
			// 开始下一次扫描，改变界面
			formatReadMessage = new WifiReadMassiveDataFormat(
					AddressPublic.finalPlcData_Head, PlcData.End
							- PlcData.IN_Head);
			alreadygetMsg=true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	};

	@Override
	public void run() {
		try {
		while (destroyflag) {
			if (alreadygetMsg) {
				beforeTime = System.currentTimeMillis();
				WifiSetting_Info.mClient.sendHighPriorMessage(formatReadMessage.sendDataFormat(), targetActivity,ReadDataFeedback);
				alreadygetMsg=false;
			}
		}} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
