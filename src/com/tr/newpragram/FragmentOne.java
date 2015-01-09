
package com.tr.newpragram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wifiProtocol.WifiReadDataFormat;
import wifiProtocol.WifiReadMassiveDataFormat;
import wifiProtocol.WifiSendDataFormat;
import wifiRunnablesAndChatlistener.FinishRunnable;
import wifiRunnablesAndChatlistener.KeyCodeSend;
import wifiRunnablesAndChatlistener.NormalChatListenner;
import wifiRunnablesAndChatlistener.PositionQueryRunnable;
import wifiRunnablesAndChatlistener.SendDataRunnable;

import com.dataInAddress.AddressPublic;
import com.dataInAddress.Define;
import com.explain.HexDecoding;
import com.explain.TableToBinary;
import com.tr.R;
import com.tr.main.TR_Main_Activity;
import com.tr.programming.Config;
import com.wifiexchange.WifiSetting_Info;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView.OnEditorActionListener;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class FragmentOne extends Fragment {
    private final String TAG = "FragmentOne";
    private Button positionBtn = null;
    
    private List<String> list = new ArrayList<String>();   
    private Spinner mySpinner;
    private ArrayAdapter<String> adapter =null;
    
    private LinearLayout PositionLayout = null;
    private LinearLayout FpLayout = null;
    
    private List<String> armlist = new ArrayList<String>();   
    private ListView fpListView;
	private static  ArrayList<HashMap<String, Object>> fsplist = new ArrayList<HashMap<String,Object>>();
	private TextView FpTxt;
	private EditText FpEdtTxt;
	private ToggleButton FpSwitchSp;

	private TextView spProductBFTxt;
	private TextView spProductUDTxt;

	private TextView CurSelInfo ;
	
	private HashMap<String, Object> CurListItem = null;
	private EditText xEditTxt ;
	private EditText yEditTxt ;
	private EditText zEditTxt ;
	private EditText hEditTxt ;
	private EditText lEditTxt ;
	private EditText speed;
	private EditText aspeed;
	private EditText dspeed;
    private String posname;
    private EditText FpNumEdit;	
	private Button mCopyBtn;
	
	private CheckBox xCheckBox;
	private	CheckBox yCheckBox;
	private CheckBox zCheckBox;
	private	CheckBox hCheckBox;
	private	CheckBox lCheckBox;
	
	private TextView xTextView ;
	private TextView yTextView ;
	private	TextView zTextView ;
	private	TextView hTextView ;
	private	TextView lTextView; 
	
	private RadioGroup ArmSelect;
	private RadioButton ProductArmBtn;
	private RadioButton FeedArmBtn;
	
	private RadioGroup SpeedSelect;
	private RadioButton SpeedLow;
	private RadioButton SpeedMid;
	private RadioButton SpeedHigh;
	private RadioButton SpeedZero;
	private RadioButton SpeedTen;
	
	private int send_address_speed;
	private int send_address_pos;
	private int send_address_cc;
	private int send_address_acc;
	private int send_address_dcc;
	private int send_address_packcnt;
	private SendDataRunnable sendDataRunnable;
	private FinishRunnable sendDataFinishRunnable;
	private WifiReadDataFormat formatReadMessage;
	private byte[] getData;
	
	private FpAdapter fpmyAdapter;
	private SpAdapter spmyAdapter;
	private int flag=0;
	private int packCnt;
	private WifiReadMassiveDataFormat mformatReadMessage;
	
	private int[] pspfpaxleFlag=new int[5];
	
	private View rootView ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        list.add("取出侧位置");
        list.add("落下侧位置");
        list.add("标准装箱");
        list.add("自由装箱");
        armlist.add("制品臂装箱");
        armlist.add("料道臂装箱");
        
    }

    @Override
    public void onPause(){
        Log.d(TAG, "GeneralSetting onPause");
        if(NewPragramActivity.positionQueryRunnable!=null){
			NewPragramActivity.positionQueryRunnable.destroy();
		}
        super.onPause();
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "GeneralSetting onDestroy");
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
         rootView = inflater.inflate(R.layout.fragment_item_one, container, false);        
        final InputMethodManager  Inputmanager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);  
        rootView.setOnTouchListener(new OnTouchListener() {
    		
    		@Override
    		public boolean onTouch(View v, MotionEvent event) {
    			// TODO Auto-generated method stub
    			  if(event.getAction() == MotionEvent.ACTION_DOWN){  
    				     if(rootView.getWindowToken()!=null){  
    				    	 Inputmanager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
    				     } 							     
    				  }  
    			return false;
    		}
    	});
        return rootView;
    }
    
    private final Runnable packCntbackMessageToDo = new Runnable(){
		@Override
		public void run() {	
			if (WifiSetting_Info.mClient == null) {
					Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				formatReadMessage=new WifiReadDataFormat(send_address_speed, 8*2+6);

				try {
					flag=0;
					sendDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());

					sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readDataFinishTodo1);

					sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

					getActivity().runOnUiThread(sendDataRunnable);

				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
				}
		}
	};

	private void getpspfpaxleFlag(byte pspfpaxleFlagbyte){
		pspfpaxleFlag[0]=pspfpaxleFlagbyte&0x01;
		pspfpaxleFlag[1]=pspfpaxleFlagbyte&0x02;
		pspfpaxleFlag[2]=pspfpaxleFlagbyte&0x04;
		pspfpaxleFlag[3]=pspfpaxleFlagbyte&0x08;
		pspfpaxleFlag[4]=pspfpaxleFlagbyte&0x10;
		System.out.println("pspfpaxleFlag="+pspfpaxleFlag);
		System.out.println("pspfpaxleFlag[0]="+pspfpaxleFlag[0]);
		System.out.println("pspfpaxleFlag[1]="+pspfpaxleFlag[1]);
		System.out.println("pspfpaxleFlag[2]="+pspfpaxleFlag[2]);
		System.out.println("pspfpaxleFlag[3]="+pspfpaxleFlag[3]);
		System.out.println("pspfpaxleFlag[4]="+pspfpaxleFlag[4]);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle = getArguments();
        byte pspfpaxleFlagbyte=0;
		posname =  bundle.getString("PosType");	
		if(posname.startsWith("P")){
			pspfpaxleFlagbyte=Config.pspfpaxle[Integer.parseInt(posname.substring(1))-1];
			getpspfpaxleFlag(pspfpaxleFlagbyte);
        }/*else if(posname.startsWith("FP")){
        	pspfpaxleFlagbyte=Config.pspfpaxle[207+Integer.parseInt(posname.substring(2))];
        	getpspfpaxleFlag(pspfpaxleFlagbyte);
        }else if(posname.startsWith("SP")){
        	pspfpaxleFlagbyte=Config.pspfpaxle[199+Integer.parseInt(posname.substring(2))];
        	getpspfpaxleFlag(pspfpaxleFlagbyte);
        }*/
		PositionLayout = (LinearLayout) getActivity().findViewById(R.id.getoutPositionLayout);
		xEditTxt =(EditText) getActivity().findViewById(R.id.xEditText) ;
	    yEditTxt =(EditText) getActivity().findViewById(R.id.yEditText) ;
		zEditTxt =(EditText) getActivity().findViewById(R.id.zEditText) ;
		hEditTxt =(EditText) getActivity().findViewById(R.id.hEditText) ;
		lEditTxt =(EditText) getActivity().findViewById(R.id.lEditText) ;
		OnEditorActionListener sendData=new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				 {
					 InputMethodManager m=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					 m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					 String editString ="";
					 switch (v.getId()) {
						case R.id.xEditText:
							editString = xEditTxt.getText().toString().trim();
							break;
						case R.id.yEditText:
							editString = yEditTxt.getText().toString().trim();
							break;
						case R.id.zEditText:
							editString = zEditTxt.getText().toString().trim();
							break;
						case R.id.hEditText:
							editString = hEditTxt.getText().toString().trim();
							break;
                        case R.id.lEditText:
                        	editString = lEditTxt.getText().toString().trim();
							break;
						default:
							break;
						}
					 if (editString.equals("") ) {
							Toast.makeText(getActivity(),"数据为空，请重新输入",Toast.LENGTH_SHORT).show();
							editString = "";
							return false;
						}
					 try{
						 Double.parseDouble(editString);
				     }catch(Exception e){
						e.printStackTrace();
						Toast.makeText(getActivity(),"数据有误，请重新输入",Toast.LENGTH_SHORT).show();
						return false;
					 }
						double editDouble=0;
						 if (!editString.startsWith(".")) {
								if (editString.contains(".")) {
									String[] editStrings = editString.split("\\.");
									if (2 == editStrings.length) {
										editDouble = Double.parseDouble(editString);
										if (Math.abs(editDouble) >99999.9) {
											Toast.makeText(getActivity(),"数据超过范围，请重新输入",Toast.LENGTH_SHORT).show();
											editString = "";
											return false;
										} else {
											editString = String.format("%1$5.1f",editDouble);
										}
									} else {
										Toast.makeText(getActivity(),"数据格式错误，请重新输入",Toast.LENGTH_SHORT).show();
										editString = "";
										return false;
									}
								} else {
									editDouble = Double.parseDouble(editString);
									if (Math.abs(editDouble) >99999.9) {
										Toast.makeText(getActivity(),"数据超过范围，请重新输入",Toast.LENGTH_SHORT).show();
										editString = "";
										return false;
									} else {
										editString = String.format("%1$5.1f",editDouble);
									}
								}

							} else {
								Toast.makeText(getActivity(),"数据格式错误，请重新输入",Toast.LENGTH_SHORT).show();
								editString = "";
								return false;
							}
						
							final byte[] temp=new byte[4];
							
							int send_address=AddressPublic.model_P_point_Head
									+(AddressPublic.model_SP_point_Head-AddressPublic.model_P_point_Head)/Define.MAX_STDPOINT_NUM
									*(TableToBinary.searchAddress(posname,false)-1);
					
							 switch (v.getId()) {
								case R.id.xEditText:
									send_address=send_address;
									System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 0, 4);
									break;
								case R.id.yEditText:
									send_address=send_address+4;
									System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 0, 4);
									break;
								case R.id.zEditText:
									send_address=send_address+8;
									System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 0, 4);
									break;
								case R.id.hEditText:
									send_address=send_address+12;
									System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 0, 4);
									break;
		                        case R.id.lEditText:
		                        	send_address=send_address+16;
		                        	System.arraycopy(HexDecoding.int2byteArray4((int)(Float.parseFloat(editString)*100)), 0, temp, 0, 4);
									break;
								default:
									break;
								}

							WifiSendDataFormat posMemorySendDataFormat = new WifiSendDataFormat(temp, send_address);
							sendDataRunnable = new SendDataRunnable(posMemorySendDataFormat, getActivity());
								FinishRunnable memoryTodo = new FinishRunnable(getActivity(),"数据发送完毕");
								sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, memoryTodo));
								getActivity().runOnUiThread(sendDataRunnable);
		               return true;
		         }
			}
		};
		xEditTxt.setOnEditorActionListener(sendData);
		yEditTxt.setOnEditorActionListener(sendData);
		zEditTxt.setOnEditorActionListener(sendData);
		hEditTxt.setOnEditorActionListener(sendData);
		lEditTxt.setOnEditorActionListener(sendData);
		
	
		
		mCopyBtn = (Button) getActivity().findViewById(R.id.positionMemryBtn);
		OnClickListener mCopyBtnListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String x="0";
				String y="0";
				String z="0";
				String h="0";
				String l="0";
				if(xCheckBox.isChecked()){
					xEditTxt.setText(xTextView.getText().toString());
					x=xTextView.getText().toString();
				}else{
					xEditTxt.setText("");
				}
				if(yCheckBox.isChecked()){
					yEditTxt.setText(yTextView.getText().toString());
					y=yTextView.getText().toString();
				}else{
					yEditTxt.setText("");
				}
				if(zCheckBox.isChecked()){
					zEditTxt.setText(zTextView.getText().toString());
					z=zTextView.getText().toString();
				}else{
					zEditTxt.setText("");
				}
				if(hCheckBox.isChecked()){
					hEditTxt.setText(hTextView.getText().toString());
					h=hTextView.getText().toString();
				}else{
					hEditTxt.setText("");
				}
				if(lCheckBox.isChecked()){
					lEditTxt.setText(lTextView.getText().toString());
					l=lTextView.getText().toString();
				}else{
					lEditTxt.setText("");
				}
				
				//20个字节，五轴
				final byte[] temp=new byte[20];
				//发送头地址
				int send_address=AddressPublic.model_P_point_Head
						+(AddressPublic.model_SP_point_Head-AddressPublic.model_P_point_Head)/Define.MAX_STDPOINT_NUM
						*(TableToBinary.searchAddress(posname,false)-1);

				if(!x.equals("*****.*")&&!y.equals("*****.*")&&!z.equals("*****.*")&&
					!h.equals("*****.*")&&!l.equals("*****.*"))
				{
					System.arraycopy(HexDecoding.int2byteArray4((int)(Float.valueOf(x)*100)), 0, temp, 0, 4);
					System.arraycopy(HexDecoding.int2byteArray4((int)(Float.valueOf(y)*100)), 0, temp, 4, 4);
					System.arraycopy(HexDecoding.int2byteArray4((int)(Float.valueOf(z)*100)), 0, temp, 8, 4);
					System.arraycopy(HexDecoding.int2byteArray4((int)(Float.valueOf(h)*100)), 0, temp,12, 4);
					System.arraycopy(HexDecoding.int2byteArray4((int)(Float.valueOf(l)*100)), 0, temp,16, 4);


					WifiSendDataFormat posMemorySendDataFormat = new WifiSendDataFormat(temp, send_address);
					sendDataRunnable = new SendDataRunnable(posMemorySendDataFormat, getActivity());
					FinishRunnable memoryTodo = new FinishRunnable(getActivity(),"位置已记忆");
					sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, memoryTodo));
					getActivity().runOnUiThread(sendDataRunnable);
			   }else{				   
					Toast.makeText(getActivity(),"原点复归未完成,不能位置记忆",Toast.LENGTH_SHORT).show();
			   }
			}
		};
		mCopyBtn.setOnClickListener(mCopyBtnListener);

		
		FpLayout = (LinearLayout) getActivity().findViewById(R.id.FSPLayout);
		FpLayout.setVisibility(View.GONE);
		fpListView = (ListView) getActivity().findViewById(R.id.listView_sfp_setting);
		FpTxt = (TextView) getActivity().findViewById(R.id.FpNumTxt);
		FpTxt.setVisibility(View.GONE);
		FpEdtTxt = (EditText) getActivity().findViewById(R.id.FpNumEdt);
		FpEdtTxt.setVisibility(View.GONE);
		FpEdtTxt.setOnEditorActionListener(new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					// TODO Auto-generated method stub
					 {

						 InputMethodManager m=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
						 m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
						  
						 if(FpSwitchSp.isChecked())
		         	        {
			        			FpAdapter myAdapter  =new FpAdapter(getActivity(), FpSwitchSp, fsplist, new String[]{"name_sp_setting","line_value","Bf_value","ud_value"},fpListView);
			        			fpListView.setAdapter(myAdapter);
		         	        }else
		         	        {
		         	        	String Str =FpNumEdit.getText().toString();
		         	        	packCnt = 0;
	         					if(Str.equals("")){
	         						packCnt = 0;
	         					}else{
	         						try{
	         							packCnt = Integer.valueOf(Str).intValue(); 
	         					     }catch(Exception e){
	         							e.printStackTrace();
	         							Toast.makeText(getActivity(),"数据有误，请重新输入",Toast.LENGTH_SHORT).show();
	         							return false;
	         						 }
	         					   
	         					}
	         					
	         					if(packCnt>99){
			   						 Toast.makeText(getActivity(),"自由装箱范围1~99", Toast.LENGTH_SHORT).show();
			   						 return false;
			   					 }
		         	     
	         					 byte[] temp=new byte[2];
			   						System.arraycopy(HexDecoding.int2byteArray2(packCnt), 0, temp, 0, 2);

			   						try {

			   							sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(temp, send_address_packcnt), getActivity());

			   							sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据发送完毕",packCntbackMessageToDo);

			   							sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable,sendDataFinishRunnable));

			   							getActivity().runOnUiThread(sendDataRunnable);
			   							

			   						} catch (Exception e) {
			   							// TODO: handle exception
			   							e.printStackTrace();
			   						}
	         					
		         	        }
			                return true;
			            }
//					return false;
				}
			});
		CurSelInfo = (TextView) getActivity().findViewById(R.id.infoText);
		
		speed=(EditText)getActivity().findViewById(R.id.speed);
		aspeed=(EditText)getActivity().findViewById(R.id.aspeed);
		dspeed=(EditText)getActivity().findViewById(R.id.dspeed);
		speed.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				 {
					 InputMethodManager m=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					 m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					 if(speed.getText().toString().equals("")){
						 Toast.makeText(getActivity(),"速度为空，请重新输入", Toast.LENGTH_SHORT).show();
						 return false;
					 }
					 try{
						 Float.parseFloat(speed.getText().toString()); 
					  }catch(Exception e){
							e.printStackTrace();
							Toast.makeText(getActivity(),"数据有误，请重新输入",Toast.LENGTH_SHORT).show();
							return false;
					 }
					 int ccint=(int)Float.parseFloat(speed.getText().toString());
					 if(ccint>100){
						 Toast.makeText(getActivity(),"速度范围1~100", Toast.LENGTH_SHORT).show();
						 return false;
					 }
					//设定速度
					 byte[] temp=new byte[1];
						System.arraycopy(HexDecoding.int2byte(ccint), 0, temp, 0, 1);

						try {

							sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(temp, send_address_cc), getActivity());

							sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据发送完毕");

							sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable,sendDataFinishRunnable));

							getActivity().runOnUiThread(sendDataRunnable);
							

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
						
		               return true;
		         }
			}
		});
		
		aspeed.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				 {
					 InputMethodManager m=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					 m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					 if(aspeed.getText().toString().equals("")){
						 Toast.makeText(getActivity(),"加速度为空，请重新输入", Toast.LENGTH_SHORT).show();
						 return false;
					 }
					 try{
						 Float.parseFloat(aspeed.getText().toString()); 
					  }catch(Exception e){
							e.printStackTrace();
							Toast.makeText(getActivity(),"数据有误，请重新输入",Toast.LENGTH_SHORT).show();
							return false;
					 }
					 int accint=(int)Float.parseFloat(aspeed.getText().toString());
					 if(accint>5){
						 Toast.makeText(getActivity(),"加速度范围1~5", Toast.LENGTH_SHORT).show();
						 return false;
					 }
					//设定速度
					 byte[] temp=new byte[1];
						System.arraycopy(HexDecoding.int2byte(accint), 0, temp, 0, 1);

						try {

							sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(temp, send_address_acc), getActivity());

							sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据发送完毕");

							sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable,sendDataFinishRunnable));

							getActivity().runOnUiThread(sendDataRunnable);
							

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
		               return true;
		         }
			}
		});
		
		dspeed.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				 {
					 InputMethodManager m=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
					 m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					 if(dspeed.getText().toString().equals("")){
						 Toast.makeText(getActivity(),"减速度为空，请重新输入", Toast.LENGTH_SHORT).show();
						 return false;
					 }
					 try{
						 Float.parseFloat(dspeed.getText().toString()); 
					  }catch(Exception e){
							e.printStackTrace();
							Toast.makeText(getActivity(),"数据有误，请重新输入",Toast.LENGTH_SHORT).show();
							return false;
					 }
					 int dccint=(int)Float.parseFloat(dspeed.getText().toString());
					 if(dccint>5){
						 Toast.makeText(getActivity(),"减速度范围1~5", Toast.LENGTH_SHORT).show();
						 return false;
					 }
					//设定速度
					 byte[] temp=new byte[1];
						System.arraycopy(HexDecoding.int2byte(dccint), 0, temp, 0, 1);

						try {

							sendDataRunnable=new SendDataRunnable(new WifiSendDataFormat(temp, send_address_dcc), getActivity());

							sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据发送完毕");

							sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable,sendDataFinishRunnable));

							getActivity().runOnUiThread(sendDataRunnable);
							

						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
		               return true;
		         }
			}
		});
		spProductBFTxt = (TextView) getActivity().findViewById(R.id.productBA_etting);
		spProductUDTxt =(TextView) getActivity().findViewById(R.id.productUD_setting);
		
		positionBtn = (Button) getActivity().findViewById(R.id.positionBtn);
	        positionBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(NewPragramActivity.positionQueryRunnable!=null){
						NewPragramActivity.positionQueryRunnable.destroy();
					}
					Intent IT =new Intent();
					IT.setAction("goPositionPreview");
					getActivity().sendBroadcast(IT);
					
				}
			});
	        FpSwitchSp = (ToggleButton) getActivity().findViewById(R.id.spfp_switch);
	        FpSwitchSp.setOnCheckedChangeListener(new BtnOnCheckedChangeListener());
	        
	        ArmSelect = (RadioGroup) getActivity().findViewById(R.id.radioGroup1);
	        ProductArmBtn = (RadioButton) getActivity().findViewById(R.id.ProductArm);
	        FeedArmBtn = (RadioButton) getActivity().findViewById(R.id.FeedArm);
	        ArmSelect.check(ProductArmBtn.getId()); 
	        ArmSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					Config.SelectArmId = checkedId;
					
				}
			});
	        SpeedSelect = (RadioGroup) getActivity().findViewById(R.id.radioGroup2);
	        SpeedLow =  (RadioButton) getActivity().findViewById(R.id.speedBtnOne);
	        SpeedMid =  (RadioButton) getActivity().findViewById(R.id.speedBtnTwo);
	        SpeedHigh =  (RadioButton) getActivity().findViewById(R.id.speedBtnThree);
	        SpeedTen =  (RadioButton) getActivity().findViewById(R.id.speedBtnFour);
	        SpeedZero =  (RadioButton) getActivity().findViewById(R.id.speedBtnFive);
	        SpeedSelect.check(SpeedLow.getId());
	        SpeedSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					Config.SelectSpeedId = checkedId;
					
				}
			});
	    
		super.onActivityCreated(savedInstanceState);
	}

    private class BtnOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{  
        @Override  
        public void onCheckedChanged(CompoundButton button, boolean isChecked){  
            if(isChecked){  
            	spProductBFTxt.setText("料道前后(Ys)");
            	spProductUDTxt.setText("料道上下(Zs)");
            	if(posname.contains("SP")){
            		spmyAdapter.notifyDataSetChanged();
            	}else if(posname.contains("FP")){
            		fpmyAdapter.notifyDataSetChanged();	
            	}
            }else{
            	spProductBFTxt.setText("制品前后(Y)");
            	spProductUDTxt.setText("制品上下(Z)");
            	if(posname.contains("SP")){
            		spmyAdapter.notifyDataSetChanged();
            	}else if(posname.contains("FP")){
            		fpmyAdapter.notifyDataSetChanged();	
            	}
            }
        }  
    } 

	//获取返回的数据后进行的UI操作
	private final Runnable PreadDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节
          try{
			getData=new byte[formatReadMessage.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			if( formatReadMessage.getFinalData() != null)  ////////chenxinhua
			{
				System.arraycopy(formatReadMessage.getFinalData(), 0, getData, 0, formatReadMessage.getLength());
	
				if( getActivity() != null)  
				{
					int foot_p=HexDecoding.Array4Toint(getData, 0);
					int productBA_p=HexDecoding.Array4Toint(getData, 4);
					int productUD_p=HexDecoding.Array4Toint(getData, 8);
					int feedertroughBA_p=HexDecoding.Array4Toint(getData, 12);
					int feedertroughUD_p=HexDecoding.Array4Toint(getData, 16);
					xEditTxt.setText(String.format("%1$5.1f",Double.valueOf(foot_p)/100));
					yEditTxt.setText(String.format("%1$5.1f",Double.valueOf(productBA_p)/100));	
					hEditTxt.setText(String.format("%1$5.1f",Double.valueOf(productUD_p)/100));	
					zEditTxt.setText(String.format("%1$5.1f",Double.valueOf(feedertroughBA_p)/100));	
					lEditTxt.setText(String.format("%1$5.1f",Double.valueOf(feedertroughUD_p)/100));
					
					speed.setText(String.valueOf((int)(getData[4*Define.MAX_AXIS_NUM]&0xff)));	
					aspeed.setText(String.valueOf((int)(getData[4*Define.MAX_AXIS_NUM+1]&0xff)));	
					dspeed.setText(String.valueOf((int)(getData[4*Define.MAX_AXIS_NUM+2]&0xff)));
				}
			}
			
			if(NewPragramActivity.positionQueryRunnable!=null){
 	        	NewPragramActivity.positionQueryRunnable.destroy();
				}
			if(PositionQueryRunnable.existFlag==false){
				 NewPragramActivity.positionQueryRunnable=new PositionQueryRunnable(
							getActivity(),xTextView,yTextView,hTextView,zTextView,lTextView);
					Thread a1=new Thread(NewPragramActivity.positionQueryRunnable);
					a1.start();
				}
		}catch(Exception e){
			e.printStackTrace();
		}

		}

	};
	
	private void swithToPosition(){
		PositionLayout.setVisibility(View.VISIBLE);
		FpLayout.setVisibility(View.GONE);
		FpTxt.setVisibility(View.GONE);
		FpEdtTxt.setVisibility(View.GONE);
		CurSelInfo.setText(posname);
		
		 xCheckBox=(CheckBox)getActivity().findViewById(R.id.xCcheckBox);
		 yCheckBox=(CheckBox)getActivity().findViewById(R.id.yCheckBox);
		 zCheckBox=(CheckBox)getActivity().findViewById(R.id.zCheckBox);
		 hCheckBox=(CheckBox)getActivity().findViewById(R.id.hCheckBox3);
		 lCheckBox=(CheckBox)getActivity().findViewById(R.id.lCheckBox3);
		
		 if(pspfpaxleFlag[0]==1){
			 xCheckBox.setChecked(true);
			 xEditTxt.setEnabled(true);
		 }else{
			 xCheckBox.setChecked(false);
			 xEditTxt.setEnabled(false);
		 }
		
		 if(pspfpaxleFlag[1]==2){
			 yCheckBox.setChecked(true);
			 yEditTxt.setEnabled(true);
		 }else{
			 yCheckBox.setChecked(false);
			 yEditTxt.setEnabled(false);
		 }
		
		 if(pspfpaxleFlag[2]==4){
			 hCheckBox.setChecked(true); 
			 hEditTxt.setEnabled(true);
		 }else{
			 hCheckBox.setChecked(false);
			 hEditTxt.setEnabled(false);
		 }
		 
		 if(pspfpaxleFlag[3]==8){
			 zCheckBox.setChecked(true); 
			 zEditTxt.setEnabled(true);
		 }else{
			 zCheckBox.setChecked(false);
			 zEditTxt.setEnabled(false);
		 }
		
		 if(pspfpaxleFlag[4]==16){
			 lCheckBox.setChecked(true);
			 lEditTxt.setEnabled(true);
		 }else{
			 lCheckBox.setChecked(false);	
			 lEditTxt.setEnabled(false);
		 }		
		
		 xTextView = (TextView) getActivity().findViewById(R.id.xValue);
		 yTextView = (TextView) getActivity().findViewById(R.id.yValue);
		 zTextView = (TextView) getActivity().findViewById(R.id.Zvalue);
		 hTextView = (TextView) getActivity().findViewById(R.id.hValue);
		 lTextView = (TextView) getActivity().findViewById(R.id.lValue);
		
		//根据点的信息确定点在数组中的位置，确定发送地址
			int readMessageAddr=send_address_speed-4*Define.MAX_AXIS_NUM;
			
			if (WifiSetting_Info.mClient == null) {
				Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
				return;
			}
			//与下位机通信，获取初始化listview的数据，一次读取36个字节，36个字节用于更新listview
			formatReadMessage=new WifiReadDataFormat(readMessageAddr, 36);

			try {

				sendDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());

				sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",PreadDataFinishTodo);

				sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

				getActivity().runOnUiThread(sendDataRunnable);

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
			}
	
		
	}
	
	//获取返回的数据后进行的UI操作
		private final Runnable readDataFinishTodo1 = new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//对于返回的36字节
			
				getData=new byte[formatReadMessage.getLength()];
				//获取返回的数据，从第八位开始拷贝数据
				System.arraycopy(formatReadMessage.getFinalData(), 0, getData, 0, formatReadMessage.getLength());

				initialFList(getData);
				initialPList(getData);
		
				if(packCnt>34){
					if (WifiSetting_Info.mClient!=null) {
						flag=1;
						mformatReadMessage=new WifiReadMassiveDataFormat(send_address_pos, 8*packCnt*4);  
						
						sendDataRunnable=new SendDataRunnable(mformatReadMessage, getActivity());

						sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readDataFinishTodo);

						sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));
								
						getActivity().runOnUiThread(sendDataRunnable);
						}else{
							Toast.makeText(getActivity(), "请连接下位机", Toast.LENGTH_SHORT).show();
						}
		   
				}else{
					formatReadMessage=new WifiReadDataFormat(send_address_pos, 8*packCnt*4);
			        try {
						flag=1;
						sendDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());

						sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readDataFinishTodo);

						sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

						getActivity().runOnUiThread(sendDataRunnable);

					} catch (Exception e) {
						// TODO: handle exception 
						//Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG); 
					}
				}
				}

		};
		//获取返回的数据后进行的UI操作
		private final Runnable readDataFinishTodo = new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//对于返回的36字节
				if(packCnt>34){
					getData=new byte[mformatReadMessage.getLength()];
					//获取返回的数据，从第八位开始拷贝数据
					System.arraycopy(mformatReadMessage.getFinalData(), 0, getData, 0, mformatReadMessage.getLength());
				}else{
				    getData=new byte[formatReadMessage.getLength()];
				    //获取返回的数据，从第八位开始拷贝数据
				    System.arraycopy(formatReadMessage.getFinalData(), 0, getData, 0, formatReadMessage.getLength());
				}
				
				if (FpSwitchSp.isChecked()) {
					initialFList(getData);
					fpmyAdapter.notifyDataSetChanged();	
				
				}else{
					initialPList(getData);
				    fpmyAdapter.notifyDataSetChanged();
				}
				}

		};
		private void initialFList(byte[] backData) {

			//arrayList.clear();
			if(flag==0){
				fsplist.clear();
				HashMap<String, Object> map0 = new HashMap<String, Object>();
				map0.put("name_sp_setting", "当前位置(mm)");
				map0.put("line_value", "*****.*");
				map0.put("Bf_value", "*****.*");
				map0.put("ud_value", "*****.*");
				fsplist.add( map0);

				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("name_sp_setting", "补正值(mm)");
				map1.put("line_value", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(backData,0))/100)));
				map1.put("Bf_value",String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(backData,4))/100)));
				map1.put("ud_value", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(backData,8))/100)));
				fsplist.add( map1);

				speed.setText(String.valueOf((int)(backData[18]&0xff)));	
				aspeed.setText(String.valueOf((int)(backData[19]&0xff)));	
				dspeed.setText(String.valueOf((int)(backData[20]&0xff)));
				FpEdtTxt.setText(String.valueOf(HexDecoding.Array2Toint(backData, 16)));
				packCnt=HexDecoding.Array2Toint(backData, 16);
			}	
			if(flag==1){
				for(int i=5;i<5+packCnt;i++){
				   HashMap<String, Object> map5 = new HashMap<String, Object>();
				   map5.put("name_sp_setting", "位置"+(i-4)+"(mm)");
				   map5.put("line_value",String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array4Toint(backData, 0+(i-5)*32))/100)));
				   map5.put("Bf_value", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array4Toint(backData, 8+(i-5)*32))/100)));
				   map5.put("ud_value", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array4Toint(backData, 16+(i-5)*32))/100)));
				   fsplist.add(map5);
				 }
			}
				System.out.println("fp empty");

		}
		private void initialPList(byte[] backData) {
			if(flag==0){
				fsplist.clear();
				HashMap<String, Object> map0 = new HashMap<String, Object>();
				map0.put("name_sp_setting", "当前位置(mm)");
				map0.put("line_value", "*****.*");
				map0.put("Bf_value", "*****.*");
				map0.put("ud_value", "*****.*");
				fsplist.add( map0);

				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("name_sp_setting", "补正值(mm)");
				map1.put("line_value", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(backData,0))/100)));
				map1.put("Bf_value",String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(backData,2))/100)));
				map1.put("ud_value", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array2Toint(backData,6))/100)));
				fsplist.add( map1);
				
				speed.setText(String.valueOf((int)(backData[18]&0xff)));	
				aspeed.setText(String.valueOf((int)(backData[19]&0xff)));	
				dspeed.setText(String.valueOf((int)(backData[20]&0xff)));
				FpEdtTxt.setText(String.valueOf(HexDecoding.Array2Toint(backData, 16)));
				packCnt=HexDecoding.Array2Toint(backData, 16);
			}
			if(flag==1){
				for(int i=5;i<5+packCnt;i++){
				   HashMap<String, Object> map5 = new HashMap<String, Object>();
				   map5.put("name_sp_setting", "位置"+(i-4)+"(mm)");
				   map5.put("line_value",String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array4Toint(backData, 0+(i-5)*32))/100)));
				   map5.put("Bf_value", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array4Toint(backData, 4+(i-5)*32))/100)));
				   map5.put("ud_value", String.format("%1$5.1f",(Double.valueOf(HexDecoding.Array4Toint(backData, 12+(i-5)*32))/100)));
				   fsplist.add(map5);
				 }
			}
			   System.out.println("fp empty");

		}
	    private void swithToFp(){
		 if(NewPragramActivity.positionQueryRunnable!=null){
	        	NewPragramActivity.positionQueryRunnable.destroy();
				}
		FpLayout.setVisibility(View.VISIBLE);
		PositionLayout.setVisibility(View.GONE);
		FpTxt.setVisibility(View.VISIBLE);
		FpEdtTxt.setVisibility(View.VISIBLE);	
		CurSelInfo.setText(posname);
		FpNumEdit = (EditText) getActivity().findViewById(R.id.FpNumEdt);
		
		if(FpSwitchSp.isChecked()){
		}else{
				fpmyAdapter  =new FpAdapter(getActivity(), FpSwitchSp, fsplist, new String[]{"name_sp_setting","line_value","Bf_value","ud_value"},fpListView,send_address_speed,packCnt,posname);
				fpListView.setAdapter(fpmyAdapter);			
		}
		if (WifiSetting_Info.mClient == null) {
			Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
			return;
		}
		formatReadMessage=new WifiReadDataFormat(send_address_speed, 8*2+6);

		try {
			flag=0;
			sendDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());

			sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",readDataFinishTodo1);

			sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

			getActivity().runOnUiThread(sendDataRunnable);

		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
		}
	}
	
	//获取返回的数据后进行的UI操作
	private final Runnable SPreadDataFinishTodo = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//对于返回的36字节

			try{
			getData=new byte[formatReadMessage.getLength()];
			//获取返回的数据，从第八位开始拷贝数据
			System.arraycopy(formatReadMessage.getFinalData(), 0, getData, 0, formatReadMessage.getLength());

			if (FpSwitchSp.isChecked()) {
				initialsFList(getData);
			} else {
				initialsPList(getData);
			}
			spmyAdapter.notifyDataSetChanged();	
		}catch(Exception e){
			e.printStackTrace();
		}
		}

	};

	/**
	 * 每次新建fragment，刷新标准点P界面的listview，初始化信息为从下位机读取到的信息
	 * 
	 * @param arrayList
	 */
	private void initialsFList(byte[] backData) {

		try{
			fsplist.clear();
			
			HashMap<String, Object> map0 = new HashMap<String, Object>();
			map0.put("name_sp_setting", "当前位置(mm)");
			map0.put("foot_sp_setting", "*****.*");
			map0.put("productBA_sp_setting", "*****.*");
			map0.put("productUD_sp_setting", "*****.*");
			fsplist.add(map0);

			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("name_sp_setting", "开始位置(mm)");
			map1.put("foot_sp_setting", String.format("%1$5.1f",(HexDecoding.Array4Toint(backData, 0)/100.0)));
			map1.put("productBA_sp_setting", String.format("%1$5.1f",(HexDecoding.Array4Toint(backData, 8)/100.0)));
			map1.put("productUD_sp_setting",String.format("%1$5.1f",(HexDecoding.Array4Toint(backData, 16)/100.0)));
			fsplist.add(map1);

			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("name_sp_setting", "装箱间距(mm)");
			map2.put("foot_sp_setting", String.format("%1$5.1f",(HexDecoding.Array2Toint(backData, 32)/100.0)));
			map2.put("productBA_sp_setting", String.format("%1$5.1f",(HexDecoding.Array2Toint(backData, 36)/100.0)));
			map2.put("productUD_sp_setting", String.format("%1$5.1f",(HexDecoding.Array2Toint(backData, 40)/100.0)));
			fsplist.add(map2);
			
			HashMap<String, Object> map7 = new HashMap<String, Object>();
			map7.put("name_sp_setting", "装箱顺序");
			map7.put("foot_sp_setting", String.valueOf(HexDecoding.Array2Toint(backData, 64)).equals("0")?"1":String.valueOf(HexDecoding.Array2Toint(backData, 64)));
			map7.put("productBA_sp_setting", String.valueOf(HexDecoding.Array2Toint(backData, 68)).equals("0")?"1":String.valueOf(HexDecoding.Array2Toint(backData, 68)));
			map7.put("productUD_sp_setting", String.valueOf(HexDecoding.Array2Toint(backData, 72)).equals("0")?"1":String.valueOf(HexDecoding.Array2Toint(backData, 72)));
			fsplist.add(map7);
			
			HashMap<String, Object> map6 = new HashMap<String, Object>();
			map6.put("name_sp_setting", "装箱设定数");
			map6.put("foot_sp_setting", String.valueOf(HexDecoding.Array2Toint(backData, 48)));
			map6.put("productBA_sp_setting", String.valueOf(HexDecoding.Array2Toint(backData, 52)));
			map6.put("productUD_sp_setting", String.valueOf(HexDecoding.Array2Toint(backData, 56)));
			fsplist.add(map6);

			HashMap<String, Object> map5 = new HashMap<String, Object>();
			map5.put("name_sp_setting", "当前装箱数");
			map5.put("foot_sp_setting", "");
			map5.put("productBA_sp_setting", "");
			map5.put("productUD_sp_setting", "");
			fsplist.add(map5);

			speed.setText(String.valueOf((int)(backData[80]&0xff)));	
			aspeed.setText(String.valueOf((int)(backData[81]&0xff)));	
			dspeed.setText(String.valueOf((int)(backData[82]&0xff)));
	}catch(Exception e){
		e.printStackTrace();
	}
	}	
	/**
	 * 每次新建fragment，刷新标准点P界面的listview，初始化信息为从下位机读取到的信息
	 * 
	 * @param arrayList
	 */
	private void initialsPList(byte[] backData) {

		try{
			fsplist.clear();
			HashMap<String, Object> map0 = new HashMap<String, Object>();
			map0.put("name_sp_setting", "当前位置(mm)");
			map0.put("line_value", "*****.*");
			map0.put("Bf_value", "*****.*");
			map0.put("ud_value", "*****.*");
			fsplist.add( map0);
			
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("name_sp_setting", "开始位置(mm)");
			map1.put("line_value", String.format("%1$5.1f",(HexDecoding.Array4Toint(backData, 0)/100.0)));
			map1.put("Bf_value",String.format("%1$5.1f",(HexDecoding.Array4Toint(backData, 4)/100.0)));
			map1.put("ud_value", String.format("%1$5.1f",(HexDecoding.Array4Toint(backData, 12)/100.0)));
			fsplist.add( map1);

			HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("name_sp_setting", "装箱间距(mm)");
			map2.put("line_value", String.format("%1$5.1f",(HexDecoding.Array2Toint(backData, 32)/100.0)));
			map2.put("Bf_value",String.format("%1$5.1f",(HexDecoding.Array2Toint(backData, 34)/100.0)));
			map2.put("ud_value",String.format("%1$5.1f",(HexDecoding.Array2Toint(backData, 38)/100.0)));
			fsplist.add(map2);
			
			HashMap<String, Object> map7 = new HashMap<String, Object>();
			map7.put("name_sp_setting", "装箱顺序");
			map7.put("line_value", String.valueOf(HexDecoding.Array2Toint(backData, 64)).equals("0")?"1":String.valueOf(HexDecoding.Array2Toint(backData, 64)));
			map7.put("Bf_value", String.valueOf(HexDecoding.Array2Toint(backData, 66)).equals("0")?"2":String.valueOf(HexDecoding.Array2Toint(backData, 66)));
			map7.put("ud_value", String.valueOf(HexDecoding.Array2Toint(backData, 70)).equals("0")?"3":String.valueOf(HexDecoding.Array2Toint(backData, 70)));
			fsplist.add(map7);
			
			HashMap<String, Object> map6 = new HashMap<String, Object>();
			map6.put("name_sp_setting", "装箱设定数");
			map6.put("line_value", String.valueOf(HexDecoding.Array2Toint(backData, 48)));
			map6.put("Bf_value", String.valueOf(HexDecoding.Array2Toint(backData, 50)));
			map6.put("ud_value", String.valueOf(HexDecoding.Array2Toint(backData, 54)));
			fsplist.add(map6);

			HashMap<String, Object> map5 = new HashMap<String, Object>();
			map5.put("name_sp_setting", "当前装箱数");
			map5.put("line_value", "");
			map5.put("Bf_value", "");
			map5.put("ud_value", "");
			fsplist.add(map5);
			
			speed.setText(String.valueOf((int)(backData[80]&0xff)));	
			aspeed.setText(String.valueOf((int)(backData[81]&0xff)));	
			dspeed.setText(String.valueOf((int)(backData[82]&0xff)));
	}catch(Exception e){
		e.printStackTrace();
	}
	}	

	
	private void swithToSp(){
		 if(NewPragramActivity.positionQueryRunnable!=null){
	        	NewPragramActivity.positionQueryRunnable.destroy();
				}
		FpLayout.setVisibility(View.VISIBLE);
		PositionLayout.setVisibility(View.GONE);
		FpTxt.setVisibility(View.GONE);
		FpEdtTxt.setVisibility(View.GONE);
		CurSelInfo.setText(posname);
		if(FpSwitchSp.isChecked()){
//			spmyAdapter  =new SpAdapter(getActivity(), FpSwitchSp, fsplist, new String[]{"name_sp_setting","line_value","Bf_value","ud_value"},fpListView);
//			fpListView.setAdapter(spmyAdapter);
		}else{
			spmyAdapter  =new SpAdapter(getActivity(), FpSwitchSp, fsplist, new String[]{"name_sp_setting","line_value","Bf_value","ud_value"},fpListView,send_address_speed,posname);
			fpListView.setAdapter(spmyAdapter);
		}
		//根据点的信息确定点在数组中的位置，确定发送地址
				int readMessageAddr= send_address_speed - 10*Define.MAX_AXIS_NUM;//0x200026f0;

				if (WifiSetting_Info.mClient == null) {
					Toast.makeText(getActivity(),"请先连接主机", Toast.LENGTH_SHORT).show();
					return;
				}
				//与下位机通信，获取初始化listview的数据，一次读取36个字节，36个字节用于更新listview
				formatReadMessage=new WifiReadDataFormat(readMessageAddr, 84);

				try {

					sendDataRunnable=new SendDataRunnable(formatReadMessage, getActivity());

					sendDataFinishRunnable=new FinishRunnable(getActivity(), "数据读取完毕",SPreadDataFinishTodo);

					sendDataRunnable.setOnlistener(new NormalChatListenner(sendDataRunnable, sendDataFinishRunnable));

					getActivity().runOnUiThread(sendDataRunnable);

				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
				}
	}

  @Override 
    public void onResume() {
        // TODO Auto-generated method stub
    	Log.d("mpeng","onresume");
        if(posname.startsWith("P")){
        	send_address_speed=4*Define.MAX_AXIS_NUM+AddressPublic.model_P_point_Head
    				+(AddressPublic.model_SP_point_Head-AddressPublic.model_P_point_Head)/Define.MAX_STDPOINT_NUM
    				*(TableToBinary.searchAddress(posname,false)-1);
        	send_address_cc=send_address_speed;
        	send_address_acc=send_address_speed+1;
    		send_address_dcc=send_address_speed+2;
        	swithToPosition();
        }else if(posname.startsWith("FP")){
        	send_address_speed=TableToBinary.searchAddress(posname,true);
		if(send_address_speed==0x20003AB0){
			send_address_pos=0x200021B0;
		}
		if(send_address_speed==0x20003AC6){
			send_address_pos=0x20002E30;
		}
		send_address_cc=send_address_speed+18;
		send_address_acc=send_address_speed+19;
		send_address_dcc=send_address_speed+20;
		send_address_packcnt=send_address_speed+16;
        	swithToFp();
        }
        else if(posname.startsWith("SP")){
        	send_address_speed=10*Define.MAX_AXIS_NUM+AddressPublic.model_SP_point_Head
    				+(AddressPublic.model_FP_point_Head-AddressPublic.model_SP_point_Head)/Define.MAX_STDPACK_NUM
    				*(TableToBinary.searchAddress(posname,false)-1);
        	send_address_cc=send_address_speed;
        	send_address_acc=send_address_speed+1;
    		send_address_dcc=send_address_speed+2;
        	swithToSp();
        }
  super.onResume();
 }
  
}
