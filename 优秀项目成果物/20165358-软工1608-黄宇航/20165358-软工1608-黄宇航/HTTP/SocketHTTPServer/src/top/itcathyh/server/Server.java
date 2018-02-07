package top.itcathyh.server;

import top.itcathyh.action.ConnectAction;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static boolean work = true;

    public static void main(String[] args) throws Exception {
        ServerSocket server = ConnectAction.getServer();
        Socket client = null;
        String path;

        if (server == null) {
            return;
        }

        if (args.length != 1){
            path = "http\\";
        } else {
            path = args[0];
        }

        while (work) {
            client = server.accept();
            new Thread(new ServerThread(client, path)).start();
        }

        server.close();
    }

    public static void setWork(boolean work) {
        Server.work = work;
    }
}