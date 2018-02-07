package top.itcathyh.command.commands;

import top.itcathyh.entity.CommandInfo;
import top.itcathyh.server.ServerThread;

import java.io.IOException;
import java.io.ObjectOutputStream;

public final class QuitCommand implements Command{
    volatile private static QuitCommand instance = null;

    private QuitCommand(){}

    public static QuitCommand getInstance(){
        if (instance == null){
            try {
                Thread.sleep(50);

                synchronized (QuitCommand.class){
                    if (instance == null){
                        instance = new QuitCommand();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    @Override
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos,  ServerThread st) {
        try {
            oos.writeObject("221 good bye");
            st.getClient().close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
