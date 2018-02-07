package top.itcathyh.command.commands;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.server.ServerThread;

import java.io.IOException;
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
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ServerThread st) {
        ServerSocket serversocket = null;
        Random random = new Random(new Date().getTime());
        int lowport, highport, port;

        while (true) {
            try {
                lowport = (1 + random.nextInt(20));
                highport = random.nextInt(1000) + 100;
                port = lowport * 256 + highport + 100;
                serversocket = new ServerSocket(port);
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            serversocket.close();
            st.setPort(port);
            oos.writeObject("237 Entering Passive Mode (127,0,0,1," + lowport + "," + highport + ")");
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
