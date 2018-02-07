package com.neuedu.apache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author guofan
 */
public class FTPClient {

    //定义基础变量
    Socket socket = null;
    BufferedReader reader = null;
    PrintWriter writer = null;

    public synchronized void connect(String host) throws IOException {
        connect(host, 21);
    }

    public synchronized void connect(String host, int port) throws IOException {
        connect(host, port, "anonymous", "anonymous");
    }

    public synchronized void connect(String host, int port, String user, String pass) {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line = reader.readLine();
            System.out.println("step1 ----- " + line);

            //input user
            sendCommand("USER " + user);
            line = reader.readLine();
            System.out.println("step2 -----" + line);

            //input pwd
            sendCommand("PASS " + pass);
            line = reader.readLine();
            System.out.println("step3 -----" + line);


        } catch (UnknownHostException ex) {
            System.out.println("Couldn't find the Ftp Server");
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }

    public synchronized void disconnect() throws IOException {

        try {
            sendCommand("QUIT");
            System.out.println("last step ----- " + reader.readLine());
        } finally {
            socket = null;
        }
    }

    public synchronized void listFiles(String serverPath) throws IOException {

        //writer.write("cwd  " + serverPath + "/r/n"); //若要指定某一位置就修改 caches
        writer.println("cwd  " + serverPath);
    	writer.flush();
        System.out.println(reader.readLine());

        sendCommand("PASV");

        String response = reader.readLine();
        System.out.println(response);
        String ip = null;
        int port1 = -1;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);

        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
                        + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port1 = Integer.parseInt(tokenizer.nextToken()) * 256
                        + Integer.parseInt(tokenizer.nextToken());
            } catch (Exception e) {
                throw new IOException("SimpleFTP received bad data link information: "
                        + response);
            }
        }

        System.out.println(ip + "  " + port1);

        //writer.write("LIST  " + "/r/n");
        writer.println("LIST  ");
        writer.flush();
        Socket dataSocket = new Socket(ip, port1);
        reader.readLine();
        
        DataInputStream dis = new DataInputStream(dataSocket.getInputStream());
        String s = "";
        while ((s = dis.readLine()) != null) {
            String l = new String(s.getBytes("ISO-8859-1"), "utf-8");
            System.out.println(l);
        }
        dis.close();
        dataSocket.close();

        reader.readLine();
    }

    public synchronized boolean upload(String lfilepath, String serverPath) throws IOException {
        File file = new File(lfilepath);

        if (file.isDirectory()) {
            throw new IOException("SimpleFTP cannot upload a directory.");
        }

        String filename = file.getName();
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));

        //writer.write("cwd  " + serverPath + "/r/n"); //若要指定某一位置就修改 caches
        writer.println("cwd  " + serverPath);
        writer.flush();
        System.out.println(reader.readLine());

        sendCommand("PASV");
        String response = reader.readLine();
        if (!response.startsWith("227")) {
            throw new IOException("SimpleFTP could not request passive mode: "
                    + response);
        }

        String ip = null;
        int port = -1;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
                        + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port = Integer.parseInt(tokenizer.nextToken()) * 256
                        + Integer.parseInt(tokenizer.nextToken());
            } catch (Exception e) {
                throw new IOException("SimpleFTP received bad data link information: "
                        + response);
            }
        }

        System.out.println(ip + "  " + port);

        sendCommand("STOR " + filename);

        Socket dataSocket = new Socket(ip, port);

        response = reader.readLine();

        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        output.close();
        input.close();

        response = reader.readLine();
        return response.startsWith("226 ");
    }
    
    public synchronized boolean download(String filename, String serverPath, String localPath) throws IOException {
        //writer.write("cwd  " + serverPath + "/r/n"); //若要指定某一位置就修改 caches
        writer.println("cwd  " + serverPath);
        writer.flush();
        System.out.println(reader.readLine());

        sendCommand("PASV");
        String response = reader.readLine();
        if (!response.startsWith("227")) {
            throw new IOException("SimpleFTP could not request passive mode: "
                    + response);
        }

        String ip = null;
        int port = -1;
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        if (closing > 0) {
            String dataLink = response.substring(opening + 1, closing);
            StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
            try {
                ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
                        + tokenizer.nextToken() + "." + tokenizer.nextToken();
                port = Integer.parseInt(tokenizer.nextToken()) * 256
                        + Integer.parseInt(tokenizer.nextToken());
            } catch (Exception e) {
                throw new IOException("SimpleFTP received bad data link information: "
                        + response);
            }
        }

        System.out.println(ip + "  " + port);

        sendCommand("RETR " + filename);

        Socket dataSocket = new Socket(ip, port);

        response = reader.readLine();
        
        System.out.println(response);

        BufferedInputStream input = new BufferedInputStream(dataSocket.getInputStream());
        FileOutputStream output = new FileOutputStream(localPath+"/"+filename);
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        output.close();
        input.close();

        response = reader.readLine();
        return response.startsWith("226 ");
    }


    private void sendCommand(String com) throws IOException {

        if (socket == null) {
            throw new IOException("SimpleFTP is not connected.");
        }

       
           // writer.write(com + "/r/n");
        	writer.println(com);
        	
            writer.flush();

        
    }

    public static void main(String args[]) throws IOException {
        String host = "127.0.0.1";
        int port = 21;
        String uname = "root";
        String pwd = "root";

        FTPClient fr = new FTPClient();
        fr.connect(host, port, uname, pwd);
        //fr.listFiles("aa");
        //fr.upload("c:/workspace/aa.txt","aa");
        fr.download("aa.txt", "aa", "d:/test2");
        
        fr.disconnect();
    }
}




