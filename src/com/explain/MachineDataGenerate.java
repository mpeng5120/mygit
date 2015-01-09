package com.explain;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.R.integer;
import android.app.Activity;
import android.widget.Toast;

import com.dataInAddress.Machinedata;

public class MachineDataGenerate {

	/**
	 * 加减速度填写
	 * @param axisNo
	 * @param value
	 * @param mdata
	 */
	private void accdccSave(int axisNo,int value,int i,Machinedata mdata) {
		switch (i) {
		case 1:
			mdata.motoParas[axisNo].accTimeTbl[0]=value;
			break;
		case 2:
			mdata.motoParas[axisNo].accTimeTbl[1]=value;
			break;
		case 3:
			mdata.motoParas[axisNo].accTimeTbl[2]=value;
			break;
		case 4:
			mdata.motoParas[axisNo].accTimeTbl[3]=value;
			break;
		case 5:
			mdata.motoParas[axisNo].accTimeTbl[4]=value;
			break;
		case 6:
			mdata.motoParas[axisNo].dccTimeTbl[0]=value;
			break;
		case 7:
			mdata.motoParas[axisNo].dccTimeTbl[1]=value;
			break;
		case 8:
			mdata.motoParas[axisNo].dccTimeTbl[2]=value;
			break;
		case 9:
			mdata.motoParas[axisNo].dccTimeTbl[3]=value;
			break;
		case 10:
			mdata.motoParas[axisNo].dccTimeTbl[4]=value;
			break;
		default:
			break;
		}
	}

	/**
	 * 1 原点复归相关参数
	 * @param axisNo
	 * @param value
	 * @param i
	 * @param mdata
	 */
	private void orgParaSave(int axisNo,int value,int i,Machinedata mdata) {
		switch (i) {
		case 0:
			//原点偏移
			mdata.orgParas[axisNo].orgOffset=value;
			break;
		case 1:
			//原点复归速度1
			mdata.orgParas[axisNo].orgSpeedPre=value;
			break;
		case 2:
			//原点复归速度2
			mdata.orgParas[axisNo].orgSpeedAft=value;
			break;
		case 3:
			//原点方向
			mdata.orgParas[axisNo].orgDir=value;
			break;
		case 4:
			//原点限位信号编号
			mdata.orgParas[axisNo].orgLmtNo=value;
			break;		
		default:
			break;
		}
	}


	public void DataGenerate (ArrayList<HashMap<String, Object>> read_arraylist,Machinedata mdata,String version,Activity window) {

		HashMap<String, Object> mapadd=new HashMap<String, Object>();

		Iterator<HashMap<String, Object>> it=read_arraylist.iterator();
		
		try {
			System.arraycopy(version.trim().getBytes("iso-8859-1"), 0, mdata.version, 0, version.trim().getBytes("iso-8859-1").length);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int index=1;
		while (it.hasNext()) {
			HashMap<String,Object> map=it.next();

			int value;
			//String sss=map.get("setValueText").toString();
			
			try {
				value=Integer.valueOf(map.get("setValueText").toString());
			} catch (NumberFormatException e) {
				// TODO: handle exception
				value=0;
			}

			if (index<=120&&index>=1) {
				if (index%15==1) {
					//每一圈脉冲数
					mdata.motoParas[index/15].plsPerCirclel=value;	
				}else if (index%15==2) {
					//每一圈距离
					mdata.motoParas[index/15].maxPRM=value;
				}else if (index%15==3) {
					//设定100%时候的最大速度
					mdata.motoParas[index/15].um10PerPls=value;	
				}else if (index%15>=4&&index%15<=13) {
					//轴的加速度减速度信息获取
					accdccSave(index/15,value,index%15-3,mdata);	
				}else if (index%15==14) {
					//马达初始方向
					mdata.motoParas[index/15].initDir=value;
				}else if (index%15==0) {
					//突然停止时候减速时间
					mdata.motoParas[index/15-1].stopDecTime=value;				
				}
			}else if (index>=121&&index<=128) {
				//IO数以及IO情况
				mdata.ioIDPara[index-121]=value;
			}else if (index>=129&&index<=136) {
				//伺服数及伺服情况
				mdata.servoIDPara[index-129]=value;
			}else if (index==137) {
				//IO的AD使用标记
				mdata.ADuseFlg=value;
			}else if (index>=138&&index<=139) {
				//wifi设定

			}else if (index>=140&&index<=179) {
				//原点复归相关参数
				orgParaSave((index-140)/5, value, (index-140)%5, mdata);
			}else if(index>=180&&index<=195){
				//最大最小行程
				if(index%2==0){
					//最小
					mdata.axisStrokes[(index-180)/2].minStroke=value;
				}else {
					//最大
					mdata.axisStrokes[(index-180)/2].maxStroke=value;
				}
			}else if (index>=196&&index<=203) {
				//干涉防止相关
				if((index-196)%4==0){
					//是否使用干涉防止
					mdata.collisionOpts[(index-196)/4].useFlag=value;
				}else if ((index-196)%4==1||(index-196)%4==2) {
					//轴参数
					mdata.collisionOpts[(index-196)/4].axisNo[(index+1)%2]=value;
				}else if ((index-196)%4==3) {
					//最小距离
					mdata.collisionOpts[(index-196)/4].minPitch=value;
				}			
			}else if (index>=204&&index<=208) {
				//5档速度表
				mdata.allSpeed[index%204]=value;			
			}else if (index>=209&&index<=232) {
				//JOG速度
				mdata.jogSpeed[(index-209)/3][(index-209)%3]=value;
			}

			//下一条
			index++;

		}
	}

	
	
	
}
