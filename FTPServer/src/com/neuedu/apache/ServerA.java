package com.neuedu.apache;

import java.net.ServerSocket;     
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * ��ɫ����������A
 * @author Leon
 *
 */
public class ServerA{

	public static void main(String[] args){
		final String F_DIR = "d:/test";//��·��
		final int PORT = 21;//�����˿ں�
		Logger.getRootLogger();
		Logger logger = Logger.getLogger("com");
		
		try{
			ServerSocket s = new ServerSocket(PORT);
			logger.info("Connecting to server A...");
			logger.info("Connected Successful! Local Port:"+s.getLocalPort()+". Default Directory:'"+F_DIR+"'.");
		
			while( true ){
				//���ܿͻ�������
				Socket client = s.accept();
				//���������߳�
				new ClientThread(client, F_DIR).start();
			}
		} catch(Exception e) {
			logger.error(e.getMessage());
			for(StackTraceElement ste : e.getStackTrace()){
				logger.error(ste.toString());
			}
		}
	}

}
