import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class HTTPClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
        String host = "localhost";
        int port = 8888;
        String filename = "Wildlife.wmv";
        String savelocation = "d:/test";
        
        try {
			Socket s = new Socket(host, port);
			//DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			//DataInputStream din = new DataInputStream(s.getInputStream());
			
			PrintStream writer = new PrintStream(s.getOutputStream());
			writer.println("GET /"+filename+" HTTP/1.1");
			writer.println("Host:localhost");
			writer.println("connection:keep-alive");
			writer.println();
			writer.flush();
			
			InputStream in = s.getInputStream();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			  
			String firstLineOfResponse = reader.readLine();//HTTP/1.1 200 OK
			String secondLineOfResponse = reader.readLine();//Content-Type:text/html
			String threeLineOfResponse = reader.readLine();//Content-Length:
			String fourLineOfResponse = reader.readLine();//blank line
			if(firstLineOfResponse.indexOf("200")!=-1)
			{
				//success
			       byte[] b = new byte[1024];
			       OutputStream out = new FileOutputStream(savelocation+"/"+filename);
			       int len = in.read(b);
			       while(len!=-1)
			       {		    	   
			    	   out.write(b, 0, len);
			    	   len = in.read(b);
			       }
			        
			       in.close();
			       out.close();
				
			}
			else
			{
				//output error message
				StringBuffer result = new StringBuffer();
		        String line;
		        while ((line = reader.readLine()) != null) {
		            result.append(line);
		        }
		        reader.close();
		        System.out.println(result);
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
       /*
        * URL httpUrl = null; //HTTP URL类 用这个类来创建连接
          URLConnection connection = null; //创建的http连接
        *  
        *  try {
        	  Scanner scanner = new Scanner(System.in);
        	  System.out.println("请输入IP");
        	  ip = scanner.nextLine();
        	  System.out.println("请输入端口");
        	  port = scanner.nextLine();
        	  System.out.println("请输入请求资源名称");
        	  filename = scanner.nextLine();
        	  System.out.println("请输入资源保存位置");
        	  savelocation = scanner.nextLine();
        	    
        	  httpUrl = new URL("http://"+host+":"+port+"/"+filename);
			  connection = httpUrl.openConnection();
			   //connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,;q=0.8");
		       // connection.setRequestProperty("connection", "keep-alive");
		       // connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
		        connection.connect();
		        
		        String status = connection.getHeaderField(null);
		        System.out.println("响应状态："+status);
		        String contentType = connection.getHeaderField("Content-Type");
		        System.out.println("响应类型："+contentType);
		        String contentlength = connection.getHeaderField("Content-Length");
		        System.out.println("响应数据长度："+contentlength);
		        
		        if(status.indexOf("200")!=-1)
		        {
		        	 //保存响应数据
				       InputStream in = connection.getInputStream();
				       byte[] b = new byte[1024];
				       OutputStream out = new FileOutputStream(savelocation+"/"+filename);
				       int len = in.read(b);
				       while(len!=-1)
				       {		    	   
				    	   out.write(b, 0, len);
				    	   len = in.read(b);
				       }
				        
				       in.close();
				       out.close();
		        }
		        else
		        {
		        	StringBuffer result = new StringBuffer();
		        	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			        String line;
			        while ((line = bufferedReader.readLine()) != null) {
			            result.append(line);
			        }
			        bufferedReader.close();
			        System.out.println(result);
		        }
		        
		        

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       */
        
       


	}

}
