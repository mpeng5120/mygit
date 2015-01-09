package com.tr.parameter_setting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiReadMassiveDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.AlarmQueryRunnable;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.SendDataRunnable;
import wifiRunnablesAndChatlistener.ledRunnable;

import com.dataInAddress.AddressPublic;
import com.dbutils.Constans;
import com.explain.HexDecoding;
import com.tr.R;
import com.wifiexchange.ChatListener;
import com.wifiexchange.WifiSetting_Info;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Fragments_download_management extends Fragment {
	Button btn_selectAll=null;
	Button btn_cancelAll=null;
	
	CheckBox ckBox_CPU=null;
	CheckBox ckBox_PPU=null;
	CheckBox ckBox_SPU=null;
	CheckBox ckBox_IO=null;
	CheckBox ckBox_mould=null;
	CheckBox ckBox_mechanical=null;
	CheckBox ckBox_sys=null;
	CheckBox ckBox_mechanical1=null;
	CheckBox ckBox_sys1=null;
	
	Button btn_downloadToMain=null;
	Button btn_readFromMain=null;

	ArrayList<Integer> toaddress=new ArrayList<Integer>();
	ArrayList<Integer> tolength=new ArrayList<Integer>();
	ArrayList<String> tofilename=new ArrayList<String>();
	
	
	private WifiSendDataFormat formatSendMessage;
	private WifiReadMassiveDataFormat formatReadMessage;
	private int[] overflag=new int[]{0,0};
	private SendDataRunnable modelReadDataRunnable;
	private FinishRunnable readDataFinishRunnable;

	private WifiReadDataFormat mouldformatReadMessage;
	private SendDataRunnable sendDataRunnable;
	private byte[] getData;
	private  ChatListener mouldDataFeedback ;
	private ledRunnable ledrunnable;
	private AlarmQueryRunnable alarmQueryRunnable;
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("Fragments_download_management onPause");
		alarmQueryRunnable.destroy();
		ledrunnable.destroy();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("Fragments_download_management onResume");
		alarmQueryRunnable = new AlarmQueryRunnable(getActivity());
		Thread a2 = new Thread(alarmQueryRunnable);
		a2.start();
		ledrunnable=new ledRunnable(getActivity());
		Thread a4 = new Thread(ledrunnable);
		a4.start();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("Fragments_download_management onCreate");
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		System.out.println("Fragments_download_management onCreateView");
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		return inflater.inflate(R.layout.tab_parameter_setting_download_management, container, false);
	}

	private void fromsd(){
		if (WifiSetting_Info.mClient!=null) {
			try{
		Constans.download.createFolder(Constans.trPATH, Constans.downloadFolder).createFile(tofilename.get(0));
		byte[] bytetoMain=Constans.download.readFileToByte(tofilename.get(0));
		formatSendMessage=new WifiSendDataFormat(bytetoMain,toaddress.get(0));
		modelReadDataRunnable=new SendDataRunnable(formatSendMessage, getActivity());
		modelReadDataRunnable.setOnlistener(new NormalChatListenner(modelReadDataRunnable,new FinishRunnable(getActivity(),backMessageTodo1)));
		getActivity().runOnUiThread(modelReadDataRunnable);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			if(WifiSetting_Info.progressDialog!=null){
				overflag[1]=1;
    			WifiSetting_Info.progressDialog.dismiss();
    			WifiSetting_Info.progressDialog=null;
    		}
			return;
		}
		
		}else{
			if(WifiSetting_Info.progressDialog!=null){
    			WifiSetting_Info.progressDialog.dismiss();
    			WifiSetting_Info.progressDialog=null;
    		}
		}
		
		 
	}
	private Runnable backMessageTodo1=new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub  , tofilename.get(0)+"文件读取成功，已更新到下位机"
			// 返回的标志位STS1处的判断以及和校验
			 toaddress.remove(0);
			 tofilename.remove(0);
			 if(toaddress.size()>0){
				 fromsd(); 
			 }
			 if(WifiSetting_Info.progressDialog!=null&&toaddress.size()==0){
				    overflag[0]=1;
					WifiSetting_Info.progressDialog.dismiss();
					WifiSetting_Info.progressDialog=null;
					
			 }
		}
	};
	private void tosd(){
		if (WifiSetting_Info.mClient!=null) {
			try{
		formatReadMessage=new WifiReadMassiveDataFormat(toaddress.get(0),tolength.get(0)); 
		modelReadDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());
		readDataFinishRunnable=new FinishRunnable(getActivity(),backMessageTodo );
		modelReadDataRunnable.setOnlistener(new NormalChatListenner(modelReadDataRunnable, readDataFinishRunnable));
		getActivity().runOnUiThread(modelReadDataRunnable);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				if(WifiSetting_Info.progressDialog!=null){
					overflag[1]=1;
	    			WifiSetting_Info.progressDialog.dismiss();
	    			WifiSetting_Info.progressDialog=null;
	    		}
				return;
			}
			
			}else{
				if(WifiSetting_Info.progressDialog!=null){
	    			WifiSetting_Info.progressDialog.dismiss();
	    			WifiSetting_Info.progressDialog=null;
	    		}
			}
	}
	private Runnable backMessageTodo=new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 返回的标志位STS1处的判断以及和校验
			 byte[] bytetoPinban=formatReadMessage.getFinalData();
			 if (null!=bytetoPinban) {
					Constans.download.createFolder(Constans.trPATH, Constans.downloadFolder).createFile(tofilename.get(0));
					Constans.download.cleanFile(tofilename.get(0));
					Constans.download.writeFileFromByte(tofilename.get(0), bytetoPinban);
				}
			 toaddress.remove(0);
			 tolength.remove(0);
			 tofilename.remove(0);
			 if(toaddress.size()>0){
				 tosd(); 
			 }
			 if(WifiSetting_Info.progressDialog!=null&&toaddress.size()==0){
				    overflag[0]=1;
					WifiSetting_Info.progressDialog.dismiss();
					WifiSetting_Info.progressDialog=null;
					
				}
		}
	};
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("Fragments_download_management onActivityCreated");
		btn_selectAll=(Button) getActivity().findViewById(R.id.btn_selectAll_downloadManagement);
		btn_cancelAll=(Button) getActivity().findViewById(R.id.btn_cancelAll_downloadManagement);
		
		ckBox_CPU=(CheckBox) getActivity().findViewById(R.id.ckBox_CPU);
		ckBox_PPU=(CheckBox) getActivity().findViewById(R.id.ckBox_PPU);
		ckBox_SPU=(CheckBox) getActivity().findViewById(R.id.ckBox_SPU);
		ckBox_IO=(CheckBox) getActivity().findViewById(R.id.ckBox_IO);
		ckBox_mould=(CheckBox) getActivity().findViewById(R.id.ckBox_mould);
		ckBox_mechanical=(CheckBox) getActivity().findViewById(R.id.ckBox_mechanical);
		ckBox_sys=(CheckBox) getActivity().findViewById(R.id.ckBox_sys);
		ckBox_mechanical1=(CheckBox) getActivity().findViewById(R.id.ckBox_mechanical1);
		ckBox_sys1=(CheckBox) getActivity().findViewById(R.id.ckBox_sys1);
		
		btn_downloadToMain=(Button) getActivity().findViewById(R.id.btn_downloadToMain);
		btn_readFromMain=(Button) getActivity().findViewById(R.id.btn_readFromMain);
		if(btn_selectAll==null){return;}
		if(btn_cancelAll==null){return;}
		if(ckBox_CPU==null){return;}
		if(ckBox_PPU==null){return;}
		if(ckBox_SPU==null){return;}
		if(ckBox_IO==null){return;}
		if(ckBox_sys==null){return;}
		if(ckBox_mould==null){return;}
		if(ckBox_mechanical==null){return;}
		if(btn_downloadToMain==null){return;}
		if(btn_readFromMain==null){return;}
		if(ckBox_sys1==null){return;}
		if(ckBox_mechanical1==null){return;}
		//全选
		btn_selectAll.setOnClickListener(new OnClickListener() {//全选功能按键
			@Override
			public void onClick(View v) {
				ckBox_CPU.setChecked(true);
				ckBox_PPU.setChecked(true);
				ckBox_SPU.setChecked(true);
				ckBox_IO.setChecked(true);
				ckBox_sys.setChecked(true);
				ckBox_mould.setChecked(true);
				ckBox_mechanical.setChecked(true);
				
				
			}
		});
		//全不选
		btn_cancelAll.setOnClickListener(new OnClickListener() {//全部取消功能按键
			public void onClick(View v) {
				ckBox_CPU.setChecked(false);
				ckBox_PPU.setChecked(false);
				ckBox_SPU.setChecked(false);
				ckBox_IO.setChecked(false);
				ckBox_sys.setChecked(false);
				ckBox_mould.setChecked(false);
				ckBox_mechanical.setChecked(false);
				
				
				
				
			}
		});
		//下载到基板
		btn_downloadToMain.setOnClickListener(new OnClickListener() {
			int num=0;
			@Override
			public void onClick(View v) {
				num=0;
				String contentToDownload="";
				if (ckBox_CPU.isChecked()) {
					num++;
					contentToDownload+=ckBox_CPU.getText().toString()+"、";
					toaddress.add(0x40024000);
					tofilename.add("mainSysPro.tra");
				}
				if (ckBox_PPU.isChecked()) {
					num++;
					contentToDownload+=ckBox_PPU.getText().toString()+"、";
					toaddress.add(0x40024000);
					tofilename.add("ppuSysPro.tra");
				}
				if (ckBox_SPU.isChecked()) {
					num++;
					contentToDownload+=ckBox_SPU.getText().toString()+"、";
					toaddress.add(0x0A000000);
					tofilename.add("spuSysPro.tra");
				}
				if (ckBox_IO.isChecked()) {
					num++;
					contentToDownload+=ckBox_IO.getText().toString()+"、";
					toaddress.add(0x09000000);
					tofilename.add("ioSysPro.tra");
				}
				if (ckBox_mould.isChecked()) {
					num++;
					contentToDownload+=ckBox_mould.getText().toString()+"、";
					toaddress.add(0x08004000);
					tofilename.add("mould.trt");
				}
				if (ckBox_mechanical.isChecked()) {
					num++;
					contentToDownload+=ckBox_mechanical.getText().toString()+"、";
					toaddress.add(0x0800C000);
					tofilename.add("machine.tra");
				}
				if (ckBox_sys.isChecked()) {
					num++;
					contentToDownload+=ckBox_sys.getText().toString()+"、";
					toaddress.add(0x40024000);
					tofilename.add("sysback.tra");
				}
				if(num==0){
					new AlertDialog.Builder(getActivity())
					.setTitle("提示")
					.setMessage("未选择下载项，请选择！")
					.setPositiveButton("确定",null).show();
					return;
				}
				contentToDownload=contentToDownload.substring(0, contentToDownload.length()-1);
				new AlertDialog.Builder(getActivity())
						.setTitle("提示")
						.setMessage("共选择了以下"+num+"项：\n"+"    "+contentToDownload+"\n"+"    确定下载吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,int which) {
										//在这里实现下载功能
										if(WifiSetting_Info.mClient==null){
											Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
											return;
										}
										WifiSetting_Info.wifiTimeOut=System.currentTimeMillis();
										WifiSetting_Info.progressDialog = ProgressDialog.show(getActivity(), "数据下载中", "请等待", true, false);
									       
								              fromsd(); 
								              WifiSetting_Info.progressDialog.setOnDismissListener(new OnDismissListener() {              
										            
													@Override
													public void onDismiss(DialogInterface arg0) {
														// TODO Auto-generated method stub
														 if(WifiSetting_Info.mClient==null){
															 new AlertDialog.Builder(getActivity()).setTitle("提示")
															   .setMessage("网络异常，通讯中断！请检查网络！")
															   .setPositiveButton("确定", null).show();
											                	//Toast.makeText(getActivity(),"网络异常，传输中断！请检查网络！", Toast.LENGTH_SHORT).show();
															}else if(overflag[0]==1){
																 new AlertDialog.Builder(getActivity()).setTitle("提示")
																   .setMessage("数据发送完毕")
																   .setPositiveButton("确定", null).show();
																 overflag[0]=0;
															}else if(overflag[1]==1){
																 new AlertDialog.Builder(getActivity()).setTitle("提示")
																   .setMessage("程序异常，请重操作！")
																   .setPositiveButton("确定", null).show();
																 overflag[1]=0;
																//Toast.makeText(getActivity(),"程序异常，请重操作！", Toast.LENGTH_SHORT).show();
															}
													}  
										        });
										
										
										
									}
								}).setNegativeButton("取消", null).show();

				
				
			}
		});
		//从基板读出
		btn_readFromMain.setOnClickListener(new OnClickListener() {
			int num=0;
			@Override
			public void onClick(View v) {
				num=0;
				String contentToRead="";
				if (ckBox_mechanical1.isChecked()) {
					num++;
					contentToRead+=ckBox_mechanical1.getText().toString()+"、";
					toaddress.add(0x0800C000);
					tolength.add(16000);
					tofilename.add("machine.tra");
				}
				if (ckBox_sys1.isChecked()) {
					num++;
					contentToRead+=ckBox_sys1.getText().toString()+"、";
					toaddress.add(0x40024000);
					tolength.add(4000);
					tofilename.add("sysback.tra");
				}
				if(num==0){
					new AlertDialog.Builder(getActivity())
					.setTitle("提示")
					.setMessage("未选择读出项，请选择！")
					.setPositiveButton("确定",null).show();
					return;
				}
				contentToRead=contentToRead.substring(0, contentToRead.length()-1);
				new AlertDialog.Builder(getActivity())
						.setTitle("提示")
						.setMessage("共选择了以下"+num+"项：\n"+"    "+contentToRead+"\n"+"    确定读出吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,int which) {
										//在这里实现读出功能
										if(WifiSetting_Info.mClient==null){
											Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
											return;
										}
										WifiSetting_Info.wifiTimeOut=System.currentTimeMillis();
										WifiSetting_Info.progressDialog = ProgressDialog.show(getActivity(), "数据上载中", "请等待", true, false);
									       
								              tosd(); 
								              WifiSetting_Info.progressDialog.setOnDismissListener(new OnDismissListener() {              
										            
													@Override
													public void onDismiss(DialogInterface arg0) {
														// TODO Auto-generated method stub
														 if(WifiSetting_Info.mClient==null){
															 new AlertDialog.Builder(getActivity()).setTitle("提示")
															   .setMessage("网络异常，通讯中断！请检查网络！")
															   .setPositiveButton("确定", null).show();
											                	//Toast.makeText(getActivity(),"网络异常，传输中断！请检查网络！", Toast.LENGTH_SHORT).show();
															}else if(overflag[0]==1){
																 new AlertDialog.Builder(getActivity()).setTitle("提示")
																   .setMessage("数据读取完毕")
																   .setPositiveButton("确定", null).show();
																 overflag[0]=0;
															}else if(overflag[1]==1){
																 new AlertDialog.Builder(getActivity()).setTitle("提示")
																   .setMessage("程序异常，请重操作！")
																   .setPositiveButton("确定", null).show();
																 overflag[1]=0;
																//Toast.makeText(getActivity(),"程序异常，请重操作！", Toast.LENGTH_SHORT).show();
															}
													}  
										        });
										
										
										
									}
								}).setNegativeButton("取消", null).show();
				
				
				
				
				
			}
		});
	
	
	}

}
