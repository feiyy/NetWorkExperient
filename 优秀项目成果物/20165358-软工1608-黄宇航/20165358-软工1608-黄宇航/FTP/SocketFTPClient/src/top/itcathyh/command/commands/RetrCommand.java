package top.itcathyh.command.commands;

import top.itcathyh.action.ConnectAction;
import top.itcathyh.client.ClientThread;
import top.itcathyh.client.FileThread;
import top.itcathyh.entity.CommandInfo;

import java.io.*;
import java.net.Socket;

public final class RetrCommand extends AbstractCommand implements Command {
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

    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos,
                                 ObjectInputStream ios, ClientThread st) {
        if (st.getPort() <= 0 || !getResult(commandinfo, oos, ios, "150")) {
            return false;
        }

        Socket datasocket = ConnectAction.getClient(st.getPort());

        if (!getResult(commandinfo, oos, ios, "125")) {
            return false;
        }

        new Thread(new FileThread(datasocket, null, st.getDir(), commandinfo.getStr(),"RETR", st)).start();

        return true;
    }
}
