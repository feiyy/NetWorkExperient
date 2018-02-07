package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CwdCommand extends AbstractCommand implements Command {
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
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ObjectInputStream ios, ClientThread st) {
        return getResult(commandinfo, oos, ios, "250");
    }
}
