package top.itcathyh.command.commands;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.server.ServerThread;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;

public final class SizeCommand implements Command {
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
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ServerThread st) {
        try {
            File file = new File(st.getDir() + commandinfo.getStr());

            if (file.exists()) {
                oos.writeObject("200");
                oos.writeObject(file.length());
                oos.flush();
            } else {
                oos.writeObject("213 file is non-existent");
                oos.flush();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
