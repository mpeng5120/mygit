/*    Copyright 2012 Brayden (headdetect) Lopez
 *    
 *    Dual-licensed under the Educational Community License, Version 2.0 and
 *	the GNU General Public License Version 3 (the "Licenses"); you may
 *	not use this file except in compliance with the Licenses. You may
 *	obtain a copy of the Licenses at
 *
 *		http://www.opensource.org/licenses/ecl2.php
 *		http://www.gnu.org/licenses/gpl-3.0.html
 *
 *		Unless required by applicable law or agreed to in writing
 *	software distributed under the Licenses are distributed on an "AS IS"
 *	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *	or implied. See the Licenses for the specific language governing
 *	permissions and limitations under the Licenses.
 * 
 */

package com.wifiexchange;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import wifiProtocol.WifiReadDataFormat;
import wifiRunnablesAndChatlistener.SendDataRunnable;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dataInAddress.AddressPublic;
import com.dbutils.Constans;
import com.explain.HexDecoding;
import com.explain.TableTranslate;

import com.tr.SerailPort.SerailPortOpt;
import com.tr.SerailPort.SerialPortJNI;
import com.tr.main.TR_Main_Activity;
import com.tr.maintainguide.TR_MaintainGuide_Activity;
import com.tr.parameter_setting.TR_Parameter_Setting_Activity;
import com.tr.programming.TR_Programming_Activity;

// TODO: Auto-generated Javadoc
/**
 * The Class Client. 已加入等待机制，确保每次发送资源独占
 */
public class Client implements Runnable {

	// ===========================================================
	// Constants
	// ===========================================================
	/******** 报错机制的变量 *********/
	// private MyCount counttime;
	/******** 发送数据等待缓存功能 *********/
	// 发送状态标志位,默认情况下空闲
	protected static boolean himaflag_high = true;
	protected static boolean himaflag_low = true;
	private int errorWaitTimes;
	private int wifireconnTimes;//wifi重连次数
	byte[] returnmessagebyte = new byte[1100];
	//add by mpeng 0929
	private SerailPortOpt serialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private String TAG = "Client";
	private byte [] backup_message ;
	private byte [] data_check;
	
	private byte[]  final_data = new byte[1044];
	private byte[]  dest;
	private int get_length = 0;
	private int length = 0 ;
	// ===========================================================
	// Fields
	// ===========================================================
	//private Handler mHandler;
	//private Thread cClockThread;
	/** The reader. */
	private InputStream reader;

	/** The writer. */
	private OutputStream writer;

	private static Socket thissocket;

	/** The disconnecting. */
	private boolean disconnecting;

	/** The chat listener. */
	private  static ChatListener chatListener;

	/** The connection listener. */
	private static ConnectionListener connectionListener;
	private static Activity targetActivity;
	public Activity getActivity() {
		return targetActivity;
	}

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Instantiates a new client.
	 * 
	 * @param s
	 *            the socket
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Client() throws IOException {
					disconnecting = false;
					errorWaitTimes = 0;
					Log.i(TAG," Client ");
		            serialPort = new SerailPortOpt();
		            openSerialPort();
		            Log.d("MPENG","5555555555555555");
	}

	/**
	 * Connect to the specified address, and returns a client.
	 * 
	 * @param address
	 *            the address
	 * @return the connected client
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static Client connect() throws IOException {
		Log.d("MPENG","33333333333333333");
		Client client = new Client();	
		Log.d("MPENG","4444444444444444");
		himaflag_high = true;
		himaflag_low = true;

		return client;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	private void setOnChatListener(ChatListener listener) {
		chatListener = listener;
	}

	public InputStream getReader() {
		return reader;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (connectionListener != null) {
			connectionListener.onJoin(this);
		}
		while (!disconnecting) {
			try{
		
				byte[] buffer = new byte[500];
				int count = 0;
				count = serialPort.readBytes(returnmessagebyte);
				if(count > 0){  // 收到返回的数据   
//					
//					for(int i=0;i<count;i++)
//						Log.d("mpeng","the return message is ["+i+"] is :" +returnmessagebyte[i]);
						if (himaflag_high == false) {
							//byte[] temp = new byte[8];
							// System.arraycopy(returnmessagebyte, 0, temp, 0, 8);
							// HexDecoding.printHexString("打印接受数据前8字节", temp);
		
							// 状态位置为空闲
							himaflag_high = true;
							errorWaitTimes = 0;
							//System.out.println("high状态位置为空闲");
							// 接受到的消息进行的处理，这只是一个接口，可以在不同页面进行配置（setOnChatListener）
							// 以便获得不同的处理方式,一定要配置，要不然会出现空指针的错误
		
							chatListener.onChat(returnmessagebyte);
		
							Arrays.fill(returnmessagebyte, (byte) 0);// 清空Buffer
							//length=0;
							//System.out.println("himaflag_low111h reader后222");
						} else if (himaflag_high == true && himaflag_low == false) {
						/*	byte[] temp = new byte[300];
							 System.arraycopy(returnmessagebyte, 0, temp, 0, 300);
							 HexDecoding.printHexString("打印接受数据前300字节", temp);*/
							// 状态位置为空闲
							himaflag_low = true;
							errorWaitTimes = 0;
							//System.out.println("low状态位置为空闲");
							// 接受到的消息进行的处理，这只是一个接口，可以在不同页面进行配置（setOnChatListener）
							// 以便获得不同的处理方式
							// 一定要配置，要不然会出现空指针的错误
							
							chatListener.onChat(returnmessagebyte);
		
							Arrays.fill(returnmessagebyte, (byte) 0);// 清空Buffer
							//length=0;
					} 
					}
				//}
			} catch (Exception e) {
				System.out.println("read流断开");
				e.printStackTrace();
				if(WifiSetting_Info.progressDialog!=null){
					
					WifiSetting_Info.progressDialog.dismiss();
					WifiSetting_Info.progressDialog=null;
					disconnect();
					System.out.println("WifiSetting_Info.progressDialog不等于空   dismiss");
				}
				// 清除客户端信息，保证下次继续重连
				if (thissocket != null && thissocket.isConnected() && !thissocket.isClosed()){
					reConnectionIO();
				}else{
					disconnect();
				}
			}
		}
	}

	/***
	 * 当读入流超时时重新从socket中获取流操作实例
	 */
	public void reConnectionIO(){
		System.out.println("**********设置read/write重连**********");

		Log.i(TAG,"RECONNECTIONIO");
		reader = serialPort.getInputStream();
		writer = serialPort.getOutputStream();
		
		errorWaitTimes = 0;
		himaflag_high = true;
		himaflag_low = true;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * 发送数据函数
	 * 
	 * @param message
	 *            the message
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InterruptedException
	 * @throws Exception
	 *             the exception
	 */
	
	public void sendHighPriorMessage(byte[] message, Activity targetActivity,ChatListener lsitener) {
		this.targetActivity = targetActivity;
		//System.out.println("himaflag_high111h="+himaflag_high+"      himaflag_low111h="+himaflag_low);
		//System.out.println("himaflag_low111h="+himaflag_low);
		if (himaflag_high == true) {
			// 检查低优先级的线程是否已经发送完
			if (himaflag_low == true) {
				// 关闭低优先级线程
				WifiSetting_Info.blockFlag = false;
				
				setOnChatListener(lsitener);
				// 置状态位为忙碌
				himaflag_high = false;
				if (message == null)
					return;
				
				//HexDecoding.printHexString("shuju123", message);
				//System.out.println("himaflag_low111h writer前aaaa");
				try {
					synchronized(writer) {
						writer.write(message, 0, message.length);
					}
				//System.out.println("himaflag_low111h writer前bbbb");
				} catch (IOException e) {
					// TODO: handle exception
					System.out.println("sendHighPriorMessage()==socket 断开");
					disconnect();

				}
			//	System.out.println("himaflag_low111h writer后");
			} else {
				errorWaitTimes++;
				if (errorWaitTimes > 15) {//15
					himaflag_low = true;
				}
				if (errorWaitTimes > 20) {//20
					nulldisconnect();
					System.out.println("shm被抢占&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
				}
				// 关闭低优先级线程

				WifiSetting_Info.blockFlag = false;

				System.out.println("1shm被抢占"+ errorWaitTimes);

				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (errorWaitTimes <= 20) {//20
					sendHighPriorMessage(message, targetActivity, lsitener);
				}
			}
		} else {
			errorWaitTimes++;
			System.out.println("2shm被抢占" + errorWaitTimes);
			if (errorWaitTimes > 20) {//20
				System.out.println("shm下位机不鸟我-_-!!");
				nulldisconnect();
				Toast.makeText(targetActivity, "通信错误，下位机未返信息",
						Toast.LENGTH_SHORT);
			}
		}
	}
	
	/**
	 * 该方法用于不断查询的通信线程
	 * 
	 * @param message
	 * @param lsitener
	 * @throws IOException
	 */
	public  void sendMessage(byte[] message, ChatListener lsitener,Activity targetActivity) {
		this.targetActivity = targetActivity;
//		System.out.println("himaflag_high*******"+himaflag_high+"*******himaflag_low********"+himaflag_low);
		if (message == null)
			return;
		if (himaflag_high == true && himaflag_low == true) {
			setOnChatListener(lsitener);
			// 置状态位为忙碌
			himaflag_low = false;
			try {
				if (null == writer) {
					System.out.println("writer已关闭！！！");
				} else {
					synchronized(writer) {											
							writer.write(message, 0, message.length);
						}
				} 
				//length=HexDecoding.Array2Toint(message,2)+9;
			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
				System.out.println("sendMessage()==socket 断开");
				disconnect();
			}
		} else {
			errorWaitTimes++;
			System.out.println("sm被抢占" + errorWaitTimes);
			try {
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (errorWaitTimes > 20) {//20
				System.out.println("sendMessage()下位机不鸟我-_-!!");
				nulldisconnect();
			}
			/*if (errorWaitTimes > 80) {
				System.out.println("sm下位机不鸟我-_-!!");
				disconnect();
			}*/
		
		}
	}

	/**
	 * Disconnects the client.
	 */
	public void disconnect() {
		disconnecting = true;
		WifiSetting_Info.blockFlag=false;
		System.out.println("disconnect()通讯中断");

		if (connectionListener != null) {
			connectionListener.onDisconnect(this);
		}
		reader = null;
		writer = null;
//		try {
//			Log.d("mpeng","55555");
//			thissocket.close();
//			Log.d("mpeng","6666");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
		WifiSetting_Info.mClient = null;
		 Runnable wifiledRunnable=new Runnable() {
				public void run() {
					if(TR_Main_Activity.wifi_led!=null){
			        	TR_Main_Activity.wifi_led.setVisibility(View.GONE);
			        }
			        if(TR_MaintainGuide_Activity.wifi_led!=null){
			        	TR_MaintainGuide_Activity.wifi_led.setVisibility(View.GONE);
			        }
			        if(TR_Parameter_Setting_Activity.wifi_led!=null){
			        	TR_Parameter_Setting_Activity.wifi_led.setVisibility(View.GONE);
			        }
			        if(TR_Programming_Activity.wifi_led!=null){
			        	TR_Programming_Activity.wifi_led.setVisibility(View.GONE);
			        }
				}
			};
			if(targetActivity!=null){
			   targetActivity.runOnUiThread(wifiledRunnable);
			}
 
		/*WriterUtils writeUtils = WriterUtils.getInstance();
		writeUtils.shutdown();*/
		/*
		 * final Runnable finishRunnable=new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub
		 * 
		 * Toast.makeText(getActivity(), "连接成功", Toast.LENGTH_LONG).show(); }
		 * 
		 * }; final Runnable errorRunnable=new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub
		 * 
		 * Toast.makeText(getActivity(), "连接失败，请确认连接正确的网络",
		 * Toast.LENGTH_LONG).show(); } }; Runnable tempRunnable=new Runnable()
		 * {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub try
		 * { WifiSetting_Info.mClient = Client.connect("192.168.1.1"); new
		 * Thread(WifiSetting_Info.mClient).start();
		 * 
		 * getActivity().runOnUiThread(finishRunnable); } catch (IOException e)
		 * { // TODO Auto-generated catch block e.printStackTrace();
		 * 
		 * getActivity().runOnUiThread(errorRunnable); } } }; new
		 * Thread(tempRunnable).start();
		 */
		
	}
	private void openSerialPort(){
		if(serialPort.mFd == null)
		{
			serialPort.mDevNum = 0;
			serialPort.mSpeed = 115200;
			serialPort.mDataBits = 8 ;
			serialPort.mStopBits = 1 ;
			serialPort.mParity = 'n';
			serialPort.openDev(serialPort.mDevNum);
			serialPort.setSpeed(serialPort.mFd,serialPort.mSpeed );
			serialPort.setParity(serialPort.mFd, serialPort.mDataBits, serialPort.mStopBits, serialPort.mParity);
		    Log.i(TAG,"OPENG SERIALPORT !!!");
		    reader = serialPort.getInputStream();
		    writer = serialPort.getOutputStream();
		    Log.i(TAG,"SERIALPORT 2222 ");
		    
		  
		}
		else 
		{
			Log.i(TAG,"the port is open ");
		}
	}
    private void closeSerialPort()
    {
    	
    	if(serialPort.mFd != null)
    		serialPort.closeDev(serialPort.mFd);
    	 Log.i(TAG,"closeSerialPort");
    }
	
	
	/**
	 * 
	 */
	public void nulldisconnect() {

		disconnecting = true;

		System.out.println("nulldisconnect()通讯中断");

		if (connectionListener != null) {
			connectionListener.onDisconnect(this);
		}

		if(reader==null){
			return;
		}
		if(writer==null){
			return;
		}
		try {
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * try { WifiSetting_Info.mClient = Client.connect("192.168.1.1"); new
		 * Thread(WifiSetting_Info.mClient).start();
		 * Toast.makeText(getActivity(), "连接成功", Toast.LENGTH_LONG).show();
		 * System.out.println("重连wifi成功！"); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace();
		 * Toast.makeText(getActivity(), "连接失败，请确认连接正确的网络", Toast.LENGTH_LONG)
		 * .show(); System.out.println("重连wifi失败！");
		 * 
		 * }
		 */
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
