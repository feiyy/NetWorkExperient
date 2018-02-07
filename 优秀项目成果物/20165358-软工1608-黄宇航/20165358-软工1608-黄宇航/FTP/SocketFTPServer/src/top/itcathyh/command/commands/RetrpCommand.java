package top.itcathyh.command.commands;

import top.itcathyh.action.ConnectAction;
import top.itcathyh.action.FileAction;
import top.itcathyh.dao.UserFileDao;
import top.itcathyh.dao.impl.UserFileDaoImpl;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.entity.UserFile;
import top.itcathyh.server.FileThread;
import top.itcathyh.server.ServerThread;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
在工作目录搜索（包括子文件夹）文件，并传至客户端
 */
public class RetrpCommand implements Command {
    private static final UserFileDao dao = UserFileDaoImpl.getInstance();
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
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ServerThread st) {
        try {
            if (st.getPort() == 0) {
                oos.writeObject("425 please input the command PASV");
                return false;
            }

            UserFile userfile = dao.getByName(commandinfo.getStr());

            if (userfile != null) {
                if (new File(st.getDir() + File.separator + userfile.getPath()).exists()) {
                    commandinfo.setStr(userfile.getPath());

                    return RetrCommand.getInstance().excuteCommand(commandinfo, oos, st);
                } else {
                    dao.deleteById(userfile.getId());
                }
            }

            File file = new File(st.getDir());
            userfile = new UserFile();

            userfile.setFilename(commandinfo.getStr());
            FileAction.search(file, commandinfo.getStr(), st.getDir(), commandinfo, st.getDir());

            if (commandinfo.getStr() == null) {
                oos.writeObject("450 file is non-existent");
                return false;
            }


            userfile.setPath(commandinfo.getStr());

            dao.add(userfile);

            return RetrCommand.getInstance().excuteCommand(commandinfo, oos, st);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
