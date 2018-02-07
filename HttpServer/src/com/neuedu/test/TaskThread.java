package com.neuedu.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.channels.FileChannel;

public class TaskThread extends Thread{
	
	private Socket s;
	
	public TaskThread(Socket s)
	{
		this.s = s;
	}
	
	@Override
	public void run() {
		 FileInputStream in = null;
		 OutputStream os = null;		
		 BufferedReader reader=null;
		 PrintStream writer =null;
		try {
			os = s.getOutputStream();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			  String firstLineOfRequest;
			  firstLineOfRequest = reader.readLine();
			  
			  String uri = firstLineOfRequest.split(" ")[1];

			    writer = new PrintStream(s.getOutputStream());
		        writer.println("HTTP/1.1 200 OK");// 返回应答消息,并结束应答
				if(uri.endsWith(".html"))
				{
					   writer.println("Content-Type:text/html");			       					
				}
				else if(uri.endsWith(".jpg"))
				{
					 writer.println("Content-Type:image/jpeg");	
			       
				}	
				else
				{
					writer.println("Content-Type:application/octet-stream");
				}

				in = new FileInputStream("c:/workspace"+uri);
				
				//发送响应头
		        writer.println("Content-Length:" + in.available());// 返回内容字节数
		        writer.println();// 根据 HTTP 协议, 空行将结束头信息
		        
		        writer.flush();

				 //发送响应体
	            byte[] b = new byte[1024];
	            int len = 0;
	            len = in.read(b);
	            while(len!=-1)
	            {
	        	   os.write(b, 0, len);
	        	   len =  in.read(b);
	            }
	            os.flush();
	           
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();			
				 //发送响应头
				 writer.println("HTTP/1.1 404 Not Found");	
				 writer.println("Content-Type:text/plain");	
				 writer.println("Content-Length:7");	
				 writer.println();

				  //发送响应体
				  writer.print("访问内容不存在");
				  writer.flush();	
		}
		finally
		{
               try {
            	   if(in!=null)
            	   {
            			in.close();
            	   }
            	   if(os!=null)
            	   {        
    		           os.close();
            	   }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	           
	         
		}
		
	}

}
