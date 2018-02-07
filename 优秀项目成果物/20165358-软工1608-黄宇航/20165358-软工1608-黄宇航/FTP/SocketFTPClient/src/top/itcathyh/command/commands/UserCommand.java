package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class UserCommand extends AbstractCommand implements Command{
    volatile private static UserCommand instance = null;

    private UserCommand() {
    }

    public static UserCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (UserCommand.class) {
                    if (instance == null) {
                        instance = new UserCommand();
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
        return getResult(commandinfo, oos, ios, "331");
    }
}
