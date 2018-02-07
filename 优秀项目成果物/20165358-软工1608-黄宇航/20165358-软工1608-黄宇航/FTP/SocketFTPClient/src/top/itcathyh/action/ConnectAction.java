package top.itcathyh.action;

import java.net.ServerSocket;
import java.net.Socket;

public class ConnectAction {
    private static final short CONNECT_PORT = 21;
    private static final short TIME_OUT = 10000;
    private static final String CONNECT_IP = "127.0.0.1";

    public static Socket getClient(){
        Socket client = null;

        try {
            client = new Socket(CONNECT_IP, CONNECT_PORT);
            client.setSoTimeout(TIME_OUT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return client;
    }

    public static Socket getClient(int port){
        Socket client = null;

        try {
            client = new Socket(CONNECT_IP, port);
            client.setSoTimeout(TIME_OUT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return client;
    }

    public static ServerSocket getServer(){
        try {
            return new ServerSocket(CONNECT_PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ServerSocket getServer(int port){
        try {

            return new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
