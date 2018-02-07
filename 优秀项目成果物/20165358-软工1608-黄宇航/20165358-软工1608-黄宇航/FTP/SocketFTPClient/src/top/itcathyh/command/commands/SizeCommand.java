package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SizeCommand extends AbstractCommand implements Command {
    volatile private static SizeCommand instance = null;

    private SizeCommand() {
    }

    public static SizeCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (SizeCommand.class) {
                    if (instance == null) {
                        instance = new SizeCommand();
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
        if (getResult(commandinfo, oos, ios, "200")){
            try {
                System.out.println(ios.readObject());
                return true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
