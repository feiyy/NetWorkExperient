package top.itcathyh.command.commands;

import top.itcathyh.dao.UserDao;
import top.itcathyh.dao.impl.UserDaoImpl;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.entity.User;
import top.itcathyh.server.ServerThread;

import java.io.IOException;
import java.io.ObjectOutputStream;

public final class PassCommand implements Command {
    private static final UserDao userdao = UserDaoImpl.getInstance();
    volatile private static PassCommand instance = null;

    private PassCommand() {
    }

    public static PassCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (PassCommand.class) {
                    if (instance == null) {
                        instance = new PassCommand();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    @Override
    public boolean excuteCommand(CommandInfo commandinfo, ObjectOutputStream oos,  ServerThread st) {
        User user = userdao.login((String) commandinfo.getObj1(), commandinfo.getStr());
        commandinfo.setObj2(user);
        boolean flag = false;

        try {
            if (user != null) {
                oos.writeObject("230 login successfully");
                oos.writeObject(user);
                flag = true;
            } else {
                oos.writeObject("530 password is error");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return flag;
    }
}
