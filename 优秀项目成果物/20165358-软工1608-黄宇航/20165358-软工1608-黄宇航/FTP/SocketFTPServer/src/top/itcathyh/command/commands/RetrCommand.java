package top.itcathyh.command.commands;

import top.itcathyh.action.ConnectAction;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.server.FileThread;
import top.itcathyh.server.ServerThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public final class RetrCommand implements Command {
    volatile private static RetrCommand instance = null;

    private RetrCommand() {
    }

    public static RetrCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (RetrCommand.class) {
                    if (instance == null) {
                        instance = new RetrCommand();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ServerThread st) {
        try {
            if (st.getPort() == 0){
                oos.writeObject("425 please input the command PASV");
                return false;
            }

            System.out.println(st.getDir() + commandinfo.getStr());

            if (!new File(st.getDir() + commandinfo.getStr()).exists()){
                oos.writeObject("450 file is non-existent");
                return false;
            }

            ServerSocket serversocket = ConnectAction.getServer(st.getPort());

            if (serversocket != null) {
                oos.writeObject("150 open port");
                oos.flush();
                Socket datasocket = serversocket.accept();

                oos.writeObject("125 sending data");
                oos.flush();

                new Thread(new FileThread(datasocket, serversocket, st.getDir(),
                        commandinfo.getStr(), "RETR", st)).start();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                oos.writeObject("425 error");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return false;
    }
}
