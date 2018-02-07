package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.entity.UserFile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ListCommand extends AbstractCommand implements Command {
    volatile private static ListCommand instance = null;

    private ListCommand() {
    }

    public static ListCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (ListCommand.class) {
                    if (instance == null) {
                        instance = new ListCommand();
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
        if (getResult(commandinfo, oos, ios, "150")){
            try {
                ArrayList<String> files = (ArrayList<String>) ios.readObject();

                if (files != null){
                    for (String tmp : files){
                        System.out.println(tmp);
                    }
                }

                return true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
