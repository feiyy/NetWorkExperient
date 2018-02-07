package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/*
列出子目录文件
 */
public class ListpCommand extends AbstractCommand implements Command {
    volatile private static ListpCommand instance = null;

    private ListpCommand() {
    }

    public static ListpCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (ListpCommand.class) {
                    if (instance == null) {
                        instance = new ListpCommand();
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
