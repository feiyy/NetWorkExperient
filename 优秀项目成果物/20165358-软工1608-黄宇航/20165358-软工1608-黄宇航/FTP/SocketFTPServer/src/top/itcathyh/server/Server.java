package top.itcathyh.server;

import top.itcathyh.action.ConnectAction;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static boolean work = true;

    public static void main(String[] args) throws Exception {
        ServerSocket server = ConnectAction.getServer();
        Socket client = null;

        if (server == null) {
            return;
        }

        while (work) {
            client = server.accept();
            System.out.println("与客户端连接成功！");
            new Thread(new ServerThread(client)).start();
        }

        server.close();
    }

    public static void setWork(boolean work) {
        Server.work = work;
    }
}