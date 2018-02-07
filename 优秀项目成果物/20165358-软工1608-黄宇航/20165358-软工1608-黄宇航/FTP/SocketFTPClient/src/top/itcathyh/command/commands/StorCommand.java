package top.itcathyh.command.commands;

import top.itcathyh.action.ConnectAction;
import top.itcathyh.client.ClientThread;
import top.itcathyh.client.FileThread;
import top.itcathyh.command.CommandFactory;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.entity.UserFile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class StorCommand extends AbstractCommand implements Command {
    volatile private static StorCommand instance = null;

    private StorCommand() {
    }

    public static StorCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (StorCommand.class) {
                    if (instance == null) {
                        instance = new StorCommand();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    @Override
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos,
                                 ObjectInputStream ios, ClientThread st) {
        try {
            if (st.getPort() == 0) {
                System.out.println("425 please input the command PASV");
                oos.writeObject("425 please input the command PASV");
                return false;
            }

            if (!new File(st.getDir() + commandinfo.getStr()).exists()) {
                System.out.println("450 file is non-existent");
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
                        commandinfo.getStr(), "STOR", st)).start();
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
