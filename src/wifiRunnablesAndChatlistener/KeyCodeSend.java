package wifiRunnablesAndChatlistener;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiSendDataFormat;
import android.app.Activity;
import android.widget.Toast;

import com.dataInAddress.AddressPublic;
import com.explain.HexDecoding;
import com.wifiexchange.WifiSetting_Info;

public class KeyCodeSend {
	private static byte[] getData;
	private static WifiReadDataFormat formatReadusermode;
	private static SendDataRunnable sendDatausermodeRunnable;
	private static FinishRunnable sendDataFinishRunnable;
	private static WifiSendDataFormat formatSendMessage;
	private static SendDataRunnable sendDataRunnable;
	private static Activity targetActivity;
	private final static Runnable stopDataFinishTodo1 = new Runnable(){
		@Override
		public void run() {
			formatReadusermode = new WifiReadDataFormat(0x200000AF,1);
			try {
				sendDatausermodeRunnable=new SendDataRunnable(formatReadusermode,targetActivity);
				sendDataFinishRunnable=new FinishRunnable(targetActivity,stopDataFinishTodo2);
				sendDatausermodeRunnable.setOnlistener(new NormalChatListenner(sendDatausermodeRunnable, sendDataFinishRunnable));
				targetActivity.runOnUiThread(sendDatausermodeRunnable);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	};
	private final static Runnable stopDataFinishTodo2 = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
			//发送正确且完成
			//处理返回的数据	
			getData=new byte[formatReadusermode.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadusermode.getFinalData() != null)  
			{
			   System.arraycopy(formatReadusermode.getFinalData(), 0, getData, 0, formatReadusermode.getLength());
			   int zjz=(int)(getData[0]&0x0F);
			   getData=HexDecoding.int2byte((int)(zjz|0x10));
			   try{
			    formatSendMessage=new WifiSendDataFormat(getData, 0x200000AF);
                sendDataRunnable=new SendDataRunnable(formatSendMessage, targetActivity);
				sendDataFinishRunnable=new FinishRunnable(targetActivity);
				sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));
				targetActivity.runOnUiThread(sendDataRunnable);
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		    }
		}
	};
	/**
	 * 输入要发送的keycode值
	 * 
	 * @param i
	 *            keycode值
	 */
	public static void send(int data, Activity targetActivity1) {
		targetActivity=targetActivity1;
		if (WifiSetting_Info.mClient != null) {
			SendDataRunnable sendDataRunnable = new SendDataRunnable(
					new WifiSendDataFormat(HexDecoding.int2byteArray4(data),
							AddressPublic.keycode_Head), targetActivity);
			FinishRunnable finishRunnable = null;
			if(data==999){
				 finishRunnable = new FinishRunnable(targetActivity,
						"keycode"+data+"发送完毕",stopDataFinishTodo1);
			}else{
				 finishRunnable = new FinishRunnable(targetActivity,
						"keycode"+data+"发送完毕");
			}
			
			// 这种方式只是用于写数据，读数据时暂时不使用这种方式
			sendDataRunnable.setOnlistener(new NormalChatListenner(
					sendDataRunnable, finishRunnable));
			targetActivity.runOnUiThread(sendDataRunnable);
			
		} else {
			Toast.makeText(targetActivity,"请先连接主机", Toast.LENGTH_SHORT).show();
		}

	}

}
