package top.itcathyh.command.commands;

import top.itcathyh.entity.CommandInfo;
import top.itcathyh.server.ServerThread;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CwdCommand implements Command {
    volatile private static CwdCommand instance = null;

    private CwdCommand() {
    }

    public static CwdCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (CwdCommand.class) {
                    if (instance == null) {
                        instance = new CwdCommand();
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
        String newdir = commandinfo.getStr();

        try {
            if (newdir != null && new File(newdir).isDirectory()){
                st.setDir(newdir + File.separator);
                oos.writeObject("250 new workspace is " + newdir);
                oos.flush();
                return true;
            } else {
                oos.writeObject("451 wrong path");
                oos.flush();
                return false;
            }
        }catch (IOException e){
            e.printStackTrace();
        }


        return false;
    }
}
