package wifiRunnablesAndChatlistener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WifiStateListenManager extends BroadcastReceiver  {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("mpeng","onReceive "+intent.getAction());
		 if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {     // boot  
	        Log.d("mpeng","onReceiveonReceiveonReceiveonReceiveonReceive");
	    	Intent intent1 = new Intent();
			intent1.setAction("bootComplete");
			context.sendBroadcast(intent1);
	        }
	}   
}
