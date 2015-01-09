package wifiRunnablesAndChatlistener;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.explain.HexDecoding;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

import wifiProtocol.WifiReadDataFormat;

public class maintimeRunnable implements Runnable{

	private WifiReadDataFormat formatReadMessage=new WifiReadDataFormat(0x1000B100,8);
	private byte[] getData;
	private Activity targetActivity;
	private boolean destroyPositionQueryFlag=false;
	private boolean selfDestroy_flag=true;
	private boolean chatlistener_flag=true;
	private TextView year; 
	private TextView month; 
	private TextView date; 
	private TextView week; 
	private TextView hour; 
	private TextView min; 
	private TextView sec;
    public maintimeRunnable(Activity targetActivity, View year, View month, View date, View week, View hour, View min, View sec){
    	//System.out.println("CC线程开始");
    	this.targetActivity=targetActivity;
    	this.year=(TextView)year;
    	this.month=(TextView)month;
    	this.date=(TextView)date;
    	this.week=(TextView)week;
    	this.hour=(TextView)hour;
    	this.min=(TextView)min;
    	this.sec=(TextView)sec;
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
				//发送正确且完成
				//处理返回的数据
				getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(message, 8, getData, 0, formatReadMessage.getLength());
				//对所收集到到的数据进行处理
				//HexDecoding.printHexString("警报信息", getData);
				Runnable infoShowRunnable=new Runnable() {
					public void run() {
						try{
						year.setText(String.valueOf((int)(getData[0]&0xff)+2000));
						month.setText(String.valueOf((int)(getData[1]&0xff)));
						date.setText(String.valueOf((int)(getData[2]&0xff)));
						 switch((int)(getData[3]&0xff)){
					        case 1:week.setText("日");break;
					        case 2:week.setText("一");break;
					        case 3:week.setText("二");break;
					        case 4:week.setText("三");break;
					        case 5:week.setText("四");break;
					        case 6:week.setText("五");break;
					        case 7:week.setText("六");break;
					        default :break ;
							}
						hour.setText(String.valueOf((int)(getData[4]&0xff)));
						min.setText(String.valueOf((int)(getData[5]&0xff)));
						sec.setText(String.valueOf((int)(getData[6]&0xff)));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				};
				targetActivity.runOnUiThread(infoShowRunnable);
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

