package wifiRunnablesAndChatlistener;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiReadMassiveDataFormat;
import wifiProtocol.WifiSendDataFormat;

import android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.explain.HexDecoding;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

/**
 * 发送数据线程,正常情况下发送数据时使用该进程
 * @author 李婷婷
 *
 */
public class SendDataRunnable implements Runnable {


	private Activity targetActivity;
	private Object sendDataFormat;
	private ChatListener listener;
	int flag=0;//避免在伺服参数界面中点击"写入伺服EEPROM"按钮时重复输出"请先连接主机"
	/**
	 * 该构造函数用于特殊场合，非标准listener
	 * @param listener
	 * @param sendDataFormat
	 * @param targetActivity
	 */
	public SendDataRunnable(ChatListener listener,WifiSendDataFormat sendDataFormat,Activity targetActivity){
		this.sendDataFormat=sendDataFormat;
		this.targetActivity=targetActivity;
		this.listener=listener;
		flag=0;
	}
	public SendDataRunnable(ChatListener listener,WifiReadDataFormat sendDataFormat,Activity targetActivity){
		this.sendDataFormat=sendDataFormat;
		this.targetActivity=targetActivity;
		this.listener=listener;
		flag=0;
	}
	
	//三种构造函数，对应不同的数据封装方式
	public SendDataRunnable(WifiReadDataFormat sendDataFormat,Activity targetActivity){
		this.sendDataFormat=sendDataFormat;
		this.targetActivity=targetActivity;
		flag=0;
	}
	
	
	public SendDataRunnable(WifiReadMassiveDataFormat sendDataFormat,Activity targetActivity){
		this.sendDataFormat=sendDataFormat;
		this.targetActivity=targetActivity;
		flag=0;
	}

	public SendDataRunnable(WifiSendDataFormat sendDataFormat,Activity targetActivity){

		this.sendDataFormat=sendDataFormat;
		this.targetActivity=targetActivity;
		flag=0;
	}
	public SendDataRunnable(WifiSendDataFormat sendDataFormat,Activity targetActivity,int flag){

		this.sendDataFormat=sendDataFormat;
		this.targetActivity=targetActivity;
		this.flag=flag;
	}
	/**
	 * senddata初始化时必须使用
	 * @param listener
	 */
	public void setOnlistener(ChatListener listener) {
		this.listener=listener;
	}
	public Object getWifiSendDataFormat() {
		
		return sendDataFormat;
	}

	@Override
	public void run() {
		try{
		// TODO Auto-generated method stub			
			if(WifiSetting_Info.mClient==null&&flag==0){
				if(WifiSetting_Info.progressDialog!=null){
					WifiSetting_Info.progressDialog.dismiss();
					WifiSetting_Info.progressDialog=null;
				}
				Toast.makeText(targetActivity.getApplicationContext(),
						"请先连接主机", Toast.LENGTH_SHORT).show();
			}else {
				byte[] temp = null;
				// 发送数据（按照规定格式），无需考虑发送次数
				//根据传入
				if(sendDataFormat instanceof WifiSendDataFormat){
					temp=((WifiSendDataFormat)sendDataFormat).sendDataFormat();
				//	HexDecoding.printHexString("写数据", temp);

				}else if(sendDataFormat instanceof WifiReadDataFormat){
					temp=((WifiReadDataFormat)sendDataFormat).sendDataFormat();
					HexDecoding.printHexString("一次读完数据", temp);

				}else if(sendDataFormat instanceof WifiReadMassiveDataFormat){
					temp=((WifiReadMassiveDataFormat)sendDataFormat).sendDataFormat();
					
					HexDecoding.printHexString("多次读完数据", temp);
					
				}
				if(WifiSetting_Info.mClient==null){
					//Toast.makeText(targetActivity.getApplicationContext(),"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				if(listener!=null){
					WifiSetting_Info.mClient.sendHighPriorMessage(temp,targetActivity,listener);
				}else {
					Toast.makeText(targetActivity.getApplicationContext(),
							"程序错误，没有添加listener，需要修改", Toast.LENGTH_SHORT).show();
				}

			}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}


} 
