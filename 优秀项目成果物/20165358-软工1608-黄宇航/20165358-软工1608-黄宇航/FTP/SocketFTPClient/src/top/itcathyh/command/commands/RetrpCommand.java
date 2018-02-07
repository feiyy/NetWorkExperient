package top.itcathyh.command.commands;

import top.itcathyh.client.ClientThread;
import top.itcathyh.entity.CommandInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/*
在工作目录搜索（包括子文件夹）文件，并传至客户端
 */
public class RetrpCommand extends AbstractCommand implements Command {
    volatile private static RetrpCommand instance = null;

    private RetrpCommand() {
    }

    public static RetrpCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (RetrpCommand.class) {
                    if (instance == null) {
                        instance = new RetrpCommand();
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
        return RetrCommand.getInstance().excuteCommand(commandinfo, oos, ios, st);
    }
}
