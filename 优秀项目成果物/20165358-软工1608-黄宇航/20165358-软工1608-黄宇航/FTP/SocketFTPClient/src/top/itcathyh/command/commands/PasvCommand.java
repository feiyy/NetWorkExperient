package top.itcathyh.command.commands;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;

public final class PasvCommand implements Command {
    volatile private static PasvCommand instance = null;

    private PasvCommand() {
    }

    public static PasvCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (PasvCommand.class) {
                    if (instance == null) {
                        instance = new PasvCommand();
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
            String response = (String) ios.readObject();
            System.out.println(response);

            if (!response.startsWith("237")){
                return false;
            }

            String text[] = response.split(",");
            int lowport = Integer.valueOf(text[4]);
            int highport = Integer.valueOf(text[5].substring(0,text[5].length() - 1));

            st.setPort(lowport * 256 + highport + 100);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

}
