package wifiRunnablesAndChatlistener;

import com.wifiexchange.WifiSetting_Info;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.widget.Toast;

public class FinishRunnable implements Runnable{
	
	private Activity tempActivity;
	private String tempString;
	private ProgressDialog progressTemp;
	private Runnable runnable;
	private int i=0;
	
	public FinishRunnable(Activity target,String appearText,ProgressDialog progressTemp){
		tempActivity=target;
		tempString=appearText;

		this.progressTemp=progressTemp;
		i=2;
	}
	public FinishRunnable(String appearText,Activity target){
		tempActivity=target;
		tempString=appearText;
		i=2;
	}
	public FinishRunnable(Activity target,String appearText){
		tempActivity=target;
		tempString=appearText;
		i=0;
	}
	public FinishRunnable(Activity target){//��ֹ�ڽ������ظ���������ݷ�����ϡ�
		tempActivity=target;
		i=1;
    }
	public Activity getActivity() {
		return tempActivity;
		
	}
	/**
	 * ���н��洦��Ĺ��캯��
	 * @param target
	 * @param appearText
	 * @param runnable
	 */
	public FinishRunnable(Activity target,String appearText,ProgressDialog progressTemp,Runnable runnable){
		tempActivity=target;
		tempString=appearText;
		
		this.progressTemp=progressTemp;
		this.runnable=runnable;
		i=2;
	}
	public FinishRunnable(Activity target,ProgressDialog progressTemp,Runnable runnable){
		tempActivity=target;
		
		this.progressTemp=progressTemp;
		this.runnable=runnable;
		i=1;
	}
	public FinishRunnable(Activity target,String appearText,Runnable runnable){
		tempActivity=target;
		tempString=appearText;
		
		this.runnable=runnable;
		i=0;
	}
	public FinishRunnable(Activity target,Runnable runnable){
		tempActivity=target;
		this.runnable=runnable;
		i=1;
	}
	@Override
	public void run() {
		
		try{
		
		if(progressTemp!=null){
			progressTemp.dismiss();
			WifiSetting_Info.progressDialog=null;
			progressTemp=null;
		}
		
		//������Ӧ�߳�
		if(runnable!=null){
			if(tempActivity==null){
				return;
			}
			tempActivity.runOnUiThread(runnable);
		}
		
		if(i==0){
		   Toast.makeText(tempActivity, tempString, Toast.LENGTH_SHORT).show();
		}
		WifiSetting_Info.blockFlag=true;
		//WifiSetting_Info.sendProgrammingfinish=1;
		if(i==2){
			new AlertDialog.Builder(tempActivity).setTitle("��ʾ")
			   .setMessage(tempString)
			   .setPositiveButton("ȷ��", null).show();
			   //Toast.makeText(tempActivity, tempString, Toast.LENGTH_SHORT).show();
			}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
