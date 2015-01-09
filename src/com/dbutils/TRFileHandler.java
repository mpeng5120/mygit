package com.dbutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.File;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;


import com.explain.HexDecoding;



public class TRFileHandler {
	 String mpath;
	 String mfileName;
	 String mstr;
	 File mfolder;
	 File mfile;
	 
	 
	 private TRFileHandler(){
	 }
	    
	 public static TRFileHandler create(){
	     return new TRFileHandler();
	 }
//好的	 
//	 public TRFileHandler  createFolder(String pathString){
//		 this.path=pathString;
//		 File folder=new File(path);
//      		if (!folder.exists()) {//判断文件夹目录是否存在  
//      			folder.mkdir();//如果不存在则创建文件夹	
//      			this.file=folder;
//      		}
//		this.file=folder;
//		return this;
//		
//	 }
	 public TRFileHandler  createFolder(String parentpath,String foldername){
		 this.mpath=parentpath+foldername+File.separator;
		 File folder=new File(mpath);
      		if (!folder.exists()) {//判断文件夹目录是否存在  
      			folder.mkdir();//如果不存在则创建文件夹	
      			this.mfolder=folder;
      		}
		this.mfolder=folder;
		return this;
		
	 }
//好的
//	 public TRFileHandler createFile(String fileNameString) {
//		 this.fileName=fileNameString;
//		 File file2=new File(path+fileName); 
//		 try {
//			file2.createNewFile();//创建文件
//			
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		 this.file=file2;
//		return this;	
//	}
	 //必须接着createFolder使用
	 public TRFileHandler createFile(String filename) {
//	 this.mfileName=filename;
		
	 File file=new File(mpath+filename); 
	 if (file.exists()) {
		 return this;		
	 }else {
		 try {
			 file.createNewFile();//创建文件
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		 this.mfile=file;
		
	}	
	
	return this;	
}
	 //可以单独使用
//	 public TRFileHandler createFile(String parentpath,String filename) {
////		 this.mfileName=filename;
//		 File file=new File(parentpath+filename); 
//		 try {
//			 file.createNewFile();//创建文件
//			
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		 this.mfile=file;
//		return this;	
//	}
//	 
	//好的   (public void writeFile(String filePath,String data))	 
//	 public TRFileHandler writeFile(String data)  {
//		 try {
//			 //要不要filepath？
//			 FileWriter fileWriter=new FileWriter(file,true);//true表示在后面添加而不是覆盖
//			 BufferedWriter bufferwriter = new BufferedWriter(fileWriter);
//			 bufferwriter.write(data);
//			 bufferwriter.close();
//		 } catch (Exception e) {
//			 // TODO: handle exception
//		 }
//		return this;
//       
//	 }
//	 public TRFileHandler writeFile(String data)  {
//		 try {
//			 FileWriter fileWriter=new FileWriter(mfile,true);//true表示在后面添加而不是覆盖
//			 BufferedWriter bufferwriter = new BufferedWriter(fileWriter);
//			 bufferwriter.write(data);
//			 bufferwriter.close();
//		 } catch (Exception e) {
//			 // TODO: handle exception
//		 }
//		return this;
//       
//	 }
	//必须接着createFolder使用
	 public TRFileHandler writeFile(String filename,String data)  {
		 String filepath=mpath+filename;
		 File file=new File(filepath);
		 if (!file.exists()) {
			 return this;		
		 }else {
			 try {
				 FileWriter fileWriter=new FileWriter(file,true);//true表示在后面添加而不是覆盖
				 BufferedWriter bufferwriter = new BufferedWriter(fileWriter);
				 bufferwriter.write(data);
				 bufferwriter.close();
			 } catch (Exception e) {
				 // TODO: handle exception
			 }
			return this;
		}	
	 }
	 //写入字节形式的数据，目前用于mould.h
	 public TRFileHandler writeFileFromByte(String filename,byte[] data){
		 String filepath=mpath+filename;
		 FileOutputStream fos = null;
		 try{
			 //创建流对象
			 fos = new FileOutputStream(filepath,true);
			 //依次写入文件
			 fos.write(data);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }finally{
			 try {
				 fos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		 }
		 return this;
	 }
	 //将文件读出为byte数组，目前用于mould.h
	public byte[] readFileToByte(String filename) {
		String filepath=mpath+filename;
		FileInputStream in =null;
		byte[] bytes = null;
		ByteArrayOutputStream out=null;
		try {
			in=new FileInputStream(filepath);
			out = new ByteArrayOutputStream(1024);
			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
				bytes = out.toByteArray();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return bytes;
//		InputStream is = null;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            is = new FileInputStream(pathStr);// pathStr 文件路径
//            byte[] b = new byte[1024];
//            int n;
//            while ((n = is.read(b)) != -1) {
//                out.write(b, 0, n);
//            }// end while
//        } catch (Exception e) {
//            log.error(getText("TimingMmsService.error") + e.getMessage());
//            throw new Exception("System error,SendTimingMms.getBytesFromFile", e);
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (Exception e) {
//                    log.error(e);// TODO
//                }// end try
//            }// end if
//        }// end try
//
//        return out.toByteArray();

		

	
	}
	 
//		//好的 
//	 public TRFileHandler cleanFile()  {
//		 try {
//			 //要不要filepath？
//			 FileWriter fileWriter=new FileWriter(file);//true表示在后面添加而不是覆盖
//			 BufferedWriter bufferwriter = new BufferedWriter(fileWriter);
//			 bufferwriter.write("");
//			 bufferwriter.close();
//		 } catch (Exception e) {
//			 // TODO: handle exception
//		 }
//		return this;       
//	 }

	 public TRFileHandler cleanFile(String filename)  {
		 String filepath=mpath+filename;
		 File file=new File(filepath);
		 try {
			 
			 FileWriter fileWriter=new FileWriter(file);//true表示在后面添加而不是覆盖
			 BufferedWriter bufferwriter = new BufferedWriter(fileWriter);
			 bufferwriter.write("");
			 bufferwriter.close();
		 } catch (Exception e) {
			 // TODO: handle exception
		 }
		return this;

       
	 }
	 
	 
	//好的	 
	 public String readFilebin(String fileNameString){
		 StringBuilder str =new StringBuilder();
			try {
				//文件放置的地址
				String fileName=Constans.mechanicalParameterPATH;
				//ByteArrayOutputStream byteArray=new ByteArrayOutputStream();
				DataInputStream dis = new DataInputStream(new FileInputStream(fileName)); 		
				
				byte[] itemBuf = new byte[2];  
			
				while ((dis.read(itemBuf) )!=-1) {				
					str.append(HexDecoding.bytesToHexString(itemBuf));
				}
				dis.close();

				//					 Toast.makeText(FileCreate3.this, "读取文件成功",Toast.LENGTH_SHORT).show();				

			} catch (FileNotFoundException e) {
				//					 Toast.makeText(FileCreate3.this, "读取文件失败",Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			java.lang.System.out.println(str.toString());
			return str.toString();
		}

	
//	//好的
//	 public String readFile(String filePath) {
//		 File file=new File(filePath);
//		 StringBuffer stringBuffer = new StringBuffer(); 
//		 FileReader filereader;
//		 
//		 if (!file.exists()) {
////			 Toast.makeText(FileCreate3.this, "文件不存在",Toast.LENGTH_SHORT).show();
//			 return "1";		
//		 }
//		 
//			 
//			try {
//				filereader = new FileReader(filePath);
//				BufferedReader bufferreader=new BufferedReader(filereader);	
//							 
//				 while (true){
//			            String data = bufferreader.readLine();
//			            if (null == data)  { break; }
//			            stringBuffer.append(data + "\n");
//			        }
//				 bufferreader.close();
//				 filereader.close();
////				 return stringBuffer.toString();
//			 } 
//			
//			 catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			return stringBuffer.toString();
//		
//
//		 
//	 }
	 
	//好的 
	//读出存成数组再处理显示 
//	 public String[] readOutFile(String filePath) {
//		 File file=new File(filePath);
//		 StringBuffer stringBuffer = new StringBuffer(); 
//		 FileReader filereader;
////		 ArrayList<String> readlist=new ArrayList<String>();
//		 String[] readString = null;
//		 
//		 if (!file.exists()) {
//			 return null;		
//		 }		 
//			try {
//				filereader = new FileReader(filePath);
//				BufferedReader bufferreader=new BufferedReader(filereader);	
//							 
//				 while (true){
//			            String data = bufferreader.readLine();
//			            if (null == data)  { break; }
//			            stringBuffer.append(data + "\n");
//			        }
//				 bufferreader.close();
//				 filereader.close();
////				 return stringBuffer.toString();
////				 readString=stringBuffer.toString().split("\\W+");
//				 readString=stringBuffer.toString().split("[\\W&&[^,]]+");//抽取非字符忽略逗号而提取出字符串
//				 
//			 } 
//			
//			 catch (FileNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			return readString;
//		
//
//		 
//	 }
	 public String readAlarmOutFile(String filepath) {
		 File file=new File(filepath);
		 FileReader filereader;
		 String readString ="";	 
			try {
				filereader = new FileReader(filepath);
				BufferedReader bufferreader=new BufferedReader(filereader);	
				int i=1;
				while(true){
			            String data = bufferreader.readLine();
			            byte[] databyte=null; 
			            if(data==null){break;}
			            data=data.replace("\0", "");
			            if(i==1){data=data.substring(1, data.length());}
			            i=0;
			            databyte=data.getBytes("gb2312");
			           // System.out.println("长度   "+data.length()+"   "+databyte.length);
			            if(databyte.length==20){readString=readString+data;}
			            if(databyte.length==19){readString=readString+data+"\0";}
			            if(databyte.length==18){readString=readString+data+"\0\0";}
			            if(databyte.length==17){readString=readString+data+"\0\0\0";}
			            if(databyte.length==16){readString=readString+data+"\0\0\0\0";}
			            if(databyte.length==15){readString=readString+data+"\0\0\0\0\0";}
			            if(databyte.length==14){readString=readString+data+"\0\0\0\0\0\0";}
			            if(databyte.length==13){readString=readString+data+"\0\0\0\0\0\0\0";}
			            if(databyte.length==12){readString=readString+data+"\0\0\0\0\0\0\0\0";}
			            if(databyte.length==11){readString=readString+data+"\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==10){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==9){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==8){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==7){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==6){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==5){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==4){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==3){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==2){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==1){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			            if(databyte.length==0){readString=readString+data+"\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";}
			        }
				 bufferreader.close();
				 filereader.close();
			 } 
			
			 catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return readString; 
	 }
	 public String readAlarmOutFile1(String filepath) {
		 File file=new File(filepath);
		 FileReader filereader;
		 String readString ="";	 
			try {
				filereader = new FileReader(filepath);
				BufferedReader bufferreader=new BufferedReader(filereader);	
				int i=1;
				while(true){
			            String data = bufferreader.readLine();
			            if(data==null){break;}
			            data=data.replace("\0", "");
			            if(i==1){data=data.substring(1, data.length()); System.out.println(i+"      "+data);}
			            i=0;
			            readString=readString+data+"///";
			        }
				 bufferreader.close();
				 filereader.close();
			 }  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return readString; 
	 }
	 public String[] readOutFile(String filepath) {
//		 String filepath=mpath+filename;
		 File file=new File(filepath);
		 StringBuffer stringBuffer = new StringBuffer(); 
		 FileReader filereader;
		 String[] readString = null;
		 if (!file.exists()) {
//			 return null;		
		 }	 
			try {
				filereader = new FileReader(filepath);
				BufferedReader bufferreader=new BufferedReader(filereader);	
//				BufferedReader bufferreader=new BufferedReader(new InputStreamReader(new FileInputStream(filepath),"UTF-8"));	
							 
				 while (true){
			            String data = bufferreader.readLine();
			            if (null == data)  { break; }
			            stringBuffer.append(data + "\n");
			        }
				 bufferreader.close();
				 filereader.close();
//				 return stringBuffer.toString();
//				 readString=stringBuffer.toString().split("\\W+");
//				 readString=stringBuffer.toString().split("[\\W&&[^,]]+");//抽取非字符忽略逗号而提取出字符串
//				 readString=stringBuffer.toString().split("=|/");//用=和/来分割
				 readString=stringBuffer.toString().split("/");//用/来分割
			 } 
			
			 catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return readString;
		

		 
	 }
/**
 * @Description: 列出文件夹其中的文件
 */
	 public ArrayList<String> getfiles(String folderpath){
		 ArrayList<String> list_setting=new ArrayList<String>();
			File filename=new File(folderpath);
			File[] filenames=filename.listFiles();
			 System.out.println("长度："+filenames.length);
			 for(int i=0;i<filenames.length;i++){
				 list_setting.add(filenames[i].getName());
				System.out.println("文件名："+filenames[i].getName());
			 }
			 return list_setting;
	 }
		   

	
	 /**
	  * 
	 * @Title: deleteFolder 
	 * @Description: 删除文件夹，递归删除其中的file
	 * @param folderpath
	 * @return    
	 * @return boolean    
	 * @throws
	  */
	 public boolean deleteFolder(String folderpath) {
		 File folder=new File(folderpath);
		 File[] files = folder.listFiles();  
		 boolean flag = false;
		    for (int i = 0; i < files.length; i++) {   
		        
		        if (files[i].isFile()) { //删除子文件     
		        	flag = deleteFile(files[i].getAbsolutePath());   
//		            if (!flag) break;   
		        } //删除子目录   
		        else {   
		            flag = deleteFolder(files[i].getAbsolutePath());   
//		            if (!flag) break;   
		        }   
		    }   
//		    if (!flag) return false;   
		    //删除当前目录   
		    if (folder.delete()) {   
		        return true;   
		    } else {   
		        return false;   
		    }   
	 
	 }
	 public boolean deleteFile(String filePath) {   
	     boolean flag = false;   
	     File file = new File(filePath);   
	     // 路径为文件且不为空则进行删除   
	     if (file.isFile() && file.exists()) {   
	         file.delete();   
	         flag = true;   
	     }   
	     return flag;   
	 }  
	 /**
	  * 
	 * @Title: renameFolder 
	 * @Description: 在mouldData中修改文件夹名字后必须更新名字
	 * @param oldFolderName
	 * @param newFolderName
	 * @return    
	 * @return boolean    
	 * @throws
	  */
	 public boolean renameFolder(String oldFolderName,String newFolderName) {   
		 
	     boolean flag = false;   
		 File file = new File(Constans.mouldDataPATH+ oldFolderName);        
         if(file.isDirectory()){//存在该文件夹才更改
        	 file.renameTo(new File(Constans.mouldDataPATH+ newFolderName)); 
        	 flag = true;  
         }
	     return flag;   
	 }  
	 
	 
		/**
		 * 
		* @Title: copyFolder 
		* @Description: 复制文件夹
		* @param oldPath
		* @param newPath    
		* @return void    
		* @throws
		 */
		 public void copyDirectiory(String oldPath,String newPath){
			try {
				(new File(newPath)).mkdirs();//新文件夹存在就创建
		        // 获取源文件夹当前下的文件或目录
		        File[] file = (new File(oldPath)).listFiles();
		        for (int i = 0; i < file.length; i++) {
		            if (file[i].isFile()) {//是文件就直接复制
		                // 源文件
		                File sourceFile = file[i];
		                // 目标文件
		                File targetFile = new File(new File(newPath).getAbsolutePath() + File.separator + file[i].getName());
		                copyFile(sourceFile, targetFile);
		            }
		            if (file[i].isDirectory()) {//是文件夹，递归
		                // 准备复制的源文件夹
		                String dir1 = oldPath + "/" + file[i].getName();
		                // 准备复制的目标文件夹
		                String dir2 = newPath + "/" + file[i].getName();
		                copyDirectiory(dir1, dir2);
		            }
		        }

			} catch (Exception e) {
				System.out.println("复制文件夹出错");
				e.printStackTrace();		}
		 }
		 /**
		  * 
		 * @Title: copyFile 
		 * @Description: 复制文件
		 * @param sourceFile
		 * @param targetFile    
		 * @return void    
		 * @throws
		  */
		private void copyFile(File sourceFile, File targetFile) {

			BufferedInputStream inBuff = null;
	        BufferedOutputStream outBuff = null;
	        try {
	            // 新建文件输入流并对它进行缓冲
	            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
	            // 新建文件输出流并对它进行缓冲
	            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
	            // 缓冲数组
	            byte[] b = new byte[1024 * 5];
	            int len;
	            while ((len = inBuff.read(b)) != -1) {
	                outBuff.write(b, 0, len);
	            }
	            // 刷新此缓冲的输出流
	            outBuff.flush();
	        } catch (IOException e) {
	        	System.out.println("复制文件出错");
				e.printStackTrace();
			} finally {
	            // 关闭流
	            if (inBuff != null)
					try {
						inBuff.close();
					} catch (IOException e) {
						System.out.println("关闭流出错");
						e.printStackTrace();
					}
	            if (outBuff != null)
					try {
						outBuff.close();
					} catch (IOException e) {
						System.out.println("关闭流出错");
						e.printStackTrace();
					}
	        }
			
			
		}




}
