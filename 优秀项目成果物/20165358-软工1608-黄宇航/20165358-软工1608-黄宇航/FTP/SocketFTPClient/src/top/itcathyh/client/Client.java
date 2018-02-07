package top.itcathyh.client;

import top.itcathyh.action.ConnectAction;
import top.itcathyh.entity.SocketInfo;
import top.itcathyh.entity.User;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {

    public static void main(String[] args) throws Exception {
        new ClientThread(ConnectAction.getClient()).run();
    }
}