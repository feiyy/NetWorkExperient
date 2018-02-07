package top.itcathyh.command.commands;

import top.itcathyh.dao.UserFileDao;
import top.itcathyh.dao.impl.UserFileDaoImpl;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.entity.UserFile;
import top.itcathyh.server.ServerThread;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;

public class ListCommand implements Command {
    private static final UserFileDao userfiledao = UserFileDaoImpl.getInstance();
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
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos, ServerThread st) {
        String dir = st.getDir();

        File file = new File(dir);

        try {
            if (!file.exists()) {
                oos.writeObject("451 wrong path");
                oos.flush();
                return false;
            } else {
                /*  获取文件列表 */
                ArrayList<String> files = new ArrayList<>();
                String list[] = file.list();
                char tag;
                BasicFileAttributes bfa = null;
                String time;

                if (list != null) {
                    for (String name : list) {
                        file = new File(dir + name);
                        /* 获取文件创建时间 */
                        bfa = Files.readAttributes(file.toPath(),
                                BasicFileAttributes.class);
                        time = bfa.lastModifiedTime().toString();
                        time = time.substring(0,10) + " " +  time.substring(11,19);

                        if (file.isDirectory()) {
                            tag = 'd';
                        } else {
                            tag = 'f';
                        }

                        files.add(tag + "rw-rw-rw-   1 ftp      ftp            " + file.length() +" " +  time + ' ' +  name);
                    }
                }

                oos.writeObject("150 sending filelist");
                oos.writeObject(files);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            oos.writeObject("502 wrong");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
