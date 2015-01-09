package wifiRunnablesAndChatlistener;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.explain.HexDecoding;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

import wifiProtocol.WifiReadDataFormat;

public class CntCycQueryRunnable implements Runnable{

	private WifiReadDataFormat formatReadMessage=new WifiReadDataFormat(0x1000B220,20);
	private byte[] getData;
	private Activity targetActivity;
	private boolean destroyPositionQueryFlag=false;
	private boolean selfDestroy_flag=true;
	private boolean chatlistener_flag=true;
	private TextView Cnt1; //取出次数
	private TextView Cnt2; //不良品次数
	private TextView Cyc1; //成形周期
	private TextView Cyc2; //取出周期
	private TextView Cyc3; //全周期
	private int operation; //0:次数；1：周期
    public CntCycQueryRunnable(Activity targetActivity, View Cnt1, View Cnt2,int operation){
    	//System.out.println("CC线程开始");
    	this.targetActivity=targetActivity;
    	this.Cnt1=(TextView)Cnt1;
    	this.Cnt2=(TextView)Cnt2;
    	this.operation=0;
    }

    public CntCycQueryRunnable(Activity targetActivity, View Cyc1, View Cyc2, View Cyc3,int operation){
    	//System.out.println("CC线程开始");
    	this.targetActivity=targetActivity;
    	this.operation=1;
    	this.Cyc1=(TextView)Cyc1;
    	this.Cyc2=(TextView)Cyc2;
    	this.Cyc3=(TextView)Cyc3;
    }
	//销毁该线程
	public void destroy() {
		//System.out.println("CC线程关闭");
		destroyPositionQueryFlag=true;
		selfDestroy_flag=false;
		chatlistener_flag=false;
	}

	/** 读取伺服参数时的通信线程收到信息时的处理函数. */
	private final ChatListener ReadDataFeedback = new ChatListener() {
		@Override
		public void onChat(byte[] message) {
			if(!chatlistener_flag)
				return;
			//返回的标志位STS1处的判断以及和校验
			formatReadMessage.backMessageCheck(message);
			if(!formatReadMessage.finishStatus()){
				WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
				
			}else {
				try{
				//发送正确且完成
				//处理返回的数据
				getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(message, 8, getData, 0, formatReadMessage.getLength());
				//对所收集到到的数据进行处理
				//HexDecoding.printHexString("警报信息", getData);
				Runnable infoShowRunnable=new Runnable() {
					public void run() {
						if(operation==0){
						Cnt1.setText(String.valueOf((String.valueOf(HexDecoding.Array4Toint(getData,0)))));
						Cnt2.setText(String.valueOf((String.valueOf(HexDecoding.Array4Toint(getData,4)))));
						}else{
						Cyc1.setText(String.valueOf(Double.valueOf(String.valueOf(HexDecoding.Array4Toint(getData,8)))/100));
						Cyc2.setText(String.valueOf(Double.valueOf(String.valueOf(HexDecoding.Array4Toint(getData,12)))/100));
						Cyc3.setText(String.valueOf(Double.valueOf(String.valueOf(HexDecoding.Array4Toint(getData,16)))/100));
						}
					}
				};
				targetActivity.runOnUiThread(infoShowRunnable);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
	};


	@Override
	public void run() {
		try {
		while (!destroyPositionQueryFlag) {
			while (WifiSetting_Info.blockFlag&&selfDestroy_flag&&WifiSetting_Info.mClient!=null) {
				Thread.sleep(1000);
				WifiSetting_Info.mClient.sendMessage(formatReadMessage.sendDataFormat(), ReadDataFeedback,targetActivity);
			}
		}} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
