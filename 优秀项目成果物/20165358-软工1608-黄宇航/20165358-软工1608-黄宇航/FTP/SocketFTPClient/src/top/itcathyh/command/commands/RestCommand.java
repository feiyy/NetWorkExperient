package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RestCommand extends AbstractCommand implements Command {
    volatile private static RestCommand instance = null;

    private RestCommand() {
    }

    public static RestCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (RestCommand.class) {
                    if (instance == null) {
                        instance = new RestCommand();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    @Override
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ObjectInputStream ios, ClientThread st) {
        if (getResult(commandinfo, oos, ios, "220")) {
            st.setRest(Integer.valueOf(commandinfo.getStr()));
            return true;
        }

        return false;
    }
}
