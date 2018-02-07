package top.itcathyh.command.commands;

import top.itcathyh.action.ConnectAction;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.entity.UserFile;
import top.itcathyh.server.FileThread;
import top.itcathyh.server.ServerThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class StorCommand implements Command {
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
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ServerThread st) {
        try {
            ObjectInputStream ios = st.getIos();

            if (st.getPort() < 0 || !((String) ios.readObject()).startsWith("150")) {
                return false;
            }

            Socket datasocket = ConnectAction.getClient(st.getPort());

            if (!((String) ios.readObject()).startsWith("125")){
                return false;
            }

            st.setUsed(true);
            new Thread(new FileThread(datasocket, null, st.getDir(), commandinfo.getStr(), "STOR", st)).start();

            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }
}
