package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class PassCommand extends AbstractCommand implements Command {
    volatile private static PassCommand instance = null;

    private PassCommand() {
    }

    public static PassCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (PassCommand.class) {
                    if (instance == null) {
                        instance = new PassCommand();
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
        if (getResult(commandinfo, oos, ios, "230")) {
            try {
                commandinfo.setObj1((User) ios.readObject());

                return true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
