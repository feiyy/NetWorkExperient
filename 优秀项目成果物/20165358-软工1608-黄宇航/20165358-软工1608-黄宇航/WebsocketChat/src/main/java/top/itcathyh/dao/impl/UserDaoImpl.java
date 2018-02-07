package top.itcathyh.dao.impl;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import top.itcathyh.dao.UserDao;
import top.itcathyh.entity.User;
import top.itcathyh.util.Mapping;
import top.itcathyh.util.Mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    volatile private static UserDaoImpl instance = null;

    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (UserDaoImpl.class) {
                    if (instance == null) {
                        instance = new UserDaoImpl();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }


    @Override
    public User serviceLogin(String username, String password) {
        ResultSet rs = Mysql.getResultSet("select * from user where authority = 1 and username = '" + username
                + "' and password = '" + password + "'");

        User user = Mapping.mappingUser(rs);

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
    @Override
    public User login(String username, String password) {
        ResultSet rs = Mysql.getResultSet("select * from user where username = '" + username
                + "' and password = '" + password + "'");

        User user = Mapping.mappingUser(rs);

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
