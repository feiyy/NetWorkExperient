package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

abstract public class AbstractCommand {
    abstract public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos,
                                        ObjectInputStream ios, ClientThread st);

    public boolean getResult(CommandInfo commandinfo, ObjectOutputStream oos, ObjectInputStream ios, String code) {
        String result = null;

        try {
            oos.writeObject(commandinfo.getStr());
            oos.flush();

            result = (String) ios.readObject();

            System.out.println(result);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return result != null && result.startsWith(code);
    }
}
