package wifiRunnablesAndChatlistener;

import com.dataInAddress.Define;
import com.explain.HexDecoding;
import com.explain.TableTranslate;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiReadMassiveDataFormat;
import wifiProtocol.WifiSendDataFormat;
import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Toast;


/**
 * 此监听处理函数只能用于发送数据（由于在读下位机数据时，其无法获取返回的数据并对其进行处理）
 * @author 李婷婷
 *
 */
public class NormalChatListenner implements ChatListener {

	private SendDataRunnable sendRunnable;
	private FinishRunnable finishRunnable;
	
	//多次任务发送情况下处理
	private SendDataRunnable sendRunnable1;
	
	private Object formatMessage;

	private Activity targetActivity;
	
	//模式切换标志（多任务发送1，单任务发送0）
	private int modeChange;
	/**
	 * 单任务配结束任务完成一次数据发送
	 * @param sendRunnable
	 * @param finishRunnable
	 */
	public NormalChatListenner(SendDataRunnable sendRunnable,FinishRunnable finishRunnable) {
		// TODO Auto-generated constructor stub
		this.sendRunnable=sendRunnable;
		this.finishRunnable=finishRunnable;
		
		this.targetActivity=finishRunnable.getActivity();
		this.formatMessage=sendRunnable.getWifiSendDataFormat();
		
		modeChange=0;
	}

	/**
	 * 多任务跳转控制，完成sendRunnable后，切换任务到SendRunnable
	 * @param sendRunnable
	 * @param sendRunnable1
	 */
	public NormalChatListenner(SendDataRunnable sendRunnable,SendDataRunnable sendRunnable1,Activity targetActivity) {
		// TODO Auto-generated constructor stub
		this.sendRunnable=sendRunnable;
		this.sendRunnable1=sendRunnable1;
		
		this.targetActivity=targetActivity;
		this.formatMessage=sendRunnable.getWifiSendDataFormat();
		
		modeChange=1;
	}
	
	@Override
	public void onChat(byte[] message) {
		// TODO Auto-generated method stub
		try{
		//返回的标志位STS1处的判断以及和校验
		if(formatMessage instanceof WifiReadDataFormat){
			HexDecoding.printHexString("dian", message);
			WifiReadDataFormat formatReadMessage=((WifiReadDataFormat)formatMessage);
			
			formatReadMessage.backMessageCheck(message);

			if(!formatReadMessage.finishStatus()){
				if ((System.currentTimeMillis() - WifiSetting_Info.wifiTimeOut) > 60000*2&&WifiSetting_Info.progressDialog!=null) {
					WifiSetting_Info.progressDialog.dismiss();
					WifiSetting_Info.progressDialog=null;
					
					Runnable wifiTimeOutRunnable=new Runnable() {
						public void run() {
							 new AlertDialog.Builder(targetActivity).setTitle("提示")
							   .setMessage("通讯超时！请重操作！")
							   .setPositiveButton("知道了", null).show();
							//Toast.makeText(targetActivity, "通讯超时！请重操作！", Toast.LENGTH_SHORT).show();
						}
					};
					targetActivity.runOnUiThread(wifiTimeOutRunnable);
					
				} else {
					targetActivity.runOnUiThread(sendRunnable);	
				}
				
				
			}else {
				//WifiSetting_Info.wifiTimeOut=0;
				//WifiSetting_Info.getBackData=formatReadMessage.getFinalData();
				WifiSetting_Info.getBackData=message;
				
				targetActivity.runOnUiThread(finishRunnable);		

			}
		}else if(formatMessage instanceof WifiReadMassiveDataFormat){
			WifiReadMassiveDataFormat formatReadMessage=(WifiReadMassiveDataFormat)formatMessage;
			
			formatReadMessage.backMessageCheck(message);

			if(!formatReadMessage.finishStatus()){
				if ((System.currentTimeMillis() - WifiSetting_Info.wifiTimeOut) > 60000*2&&WifiSetting_Info.progressDialog!=null) {
					WifiSetting_Info.progressDialog.dismiss();
					WifiSetting_Info.progressDialog=null;
					
					Runnable wifiTimeOutRunnable=new Runnable() {
						public void run() {
							new AlertDialog.Builder(targetActivity).setTitle("提示")
							   .setMessage("通讯超时！请重操作！")
							   .setPositiveButton("知道了", null).show();
						}
					};
					targetActivity.runOnUiThread(wifiTimeOutRunnable);
				} else {
				targetActivity.runOnUiThread(sendRunnable);	
				}
			}else {
				//WifiSetting_Info.wifiTimeOut=0;
				WifiSetting_Info.getBackData=formatReadMessage.getFinalData();
				
				targetActivity.runOnUiThread(finishRunnable);		

			}
		}else if (formatMessage instanceof WifiSendDataFormat) {
			WifiSendDataFormat formatSendMessage=(WifiSendDataFormat)formatMessage;
			//返回的标志位STS1处的判断
			formatSendMessage.backMessageCheck(message);
			if(!formatSendMessage.finishStatus()){
				if ((System.currentTimeMillis() - WifiSetting_Info.wifiTimeOut) > 60000*2&&WifiSetting_Info.progressDialog!=null) {
					WifiSetting_Info.progressDialog.dismiss();
					WifiSetting_Info.progressDialog=null;
					
					Runnable wifiTimeOutRunnable=new Runnable() {
						public void run() {
							new AlertDialog.Builder(targetActivity).setTitle("提示")
							   .setMessage("通讯超时！请重操作！")
							   .setPositiveButton("知道了", null).show();
						}
					};
					targetActivity.runOnUiThread(wifiTimeOutRunnable);
				} else {
				targetActivity.runOnUiThread(sendRunnable);	
				}
			}else {		
				//WifiSetting_Info.wifiTimeOut=0;
				if(modeChange==0){
					System.out.println("发送完毕！！！！！！！！！！！！！！！！！！！！！！！！");
					targetActivity.runOnUiThread(finishRunnable);	
				}else if(modeChange==1){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("顺利跳转！！！！！！！！！！！！！！！！！！！！！！！！");
					targetActivity.runOnUiThread(sendRunnable1);	
				}
						
			}
		}} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
