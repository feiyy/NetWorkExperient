package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class QuitCommand implements Command {
    volatile private static QuitCommand instance = null;

    private QuitCommand() {
    }

    public static QuitCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (QuitCommand.class) {
                    if (instance == null) {
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
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos,
                                 ObjectInputStream ios, ClientThread st) {
        try {
            oos.writeObject("QUIT");
            String result = (String) ios.readObject();

            if (result.startsWith("221")) {
                System.out.println(result);
                st.getClient().close();

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
