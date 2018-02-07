package top.itcathyh.command.commands;

import top.itcathyh.entity.CommandInfo;
import top.itcathyh.server.ServerThread;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class RestCommand implements Command {
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
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ServerThread st) {
        try {
            int rest = Integer.valueOf(commandinfo.getStr());

            if (rest < 0 || rest == Integer.MAX_VALUE){
                oos.writeObject("501 wrong parameter");
            } else {
                st.setRest(Integer.parseInt(commandinfo.getStr()));
                oos.writeObject("220 rest " + commandinfo.getStr());
            }

            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
