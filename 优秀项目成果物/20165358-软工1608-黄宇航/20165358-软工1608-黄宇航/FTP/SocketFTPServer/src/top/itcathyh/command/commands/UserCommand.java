package top.itcathyh.command.commands;

import top.itcathyh.dao.UserDao;
import top.itcathyh.dao.impl.UserDaoImpl;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.server.ServerThread;

import java.io.IOException;
import java.io.ObjectOutputStream;

public final class UserCommand implements Command {
    private static final UserDao userdao = UserDaoImpl.getInstance();
    volatile private static UserCommand instance = null;

    private UserCommand() {
    }

    public static UserCommand getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (UserCommand.class) {
                    if (instance == null) {
                        instance = new UserCommand();
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
            if (!userdao.userExist(commandinfo.getStr())) {
                oos.writeObject("501 user is not validate");
                oos.flush();
            } else {
                oos.writeObject("331 please input your password");
                oos.flush();
                return true;
            }
        } catch (IOException ignored) {
        }

        return false;
    }
}
