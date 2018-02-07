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
    public User getById(long userid) {
        ResultSet rs = Mysql.getResultSet("select * from user where id = " + userid);
        User user = Mapping.mappingUser(rs);

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public boolean login(String username, String password) {
        ResultSet rs = Mysql.getResultSet("select * from user where username = '" + username
                + "' and password = '" + password + "'");

        User user = Mapping.mappingUser(rs);

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user != null;
    }

    @Override
    public boolean userExist(String username) {
        try {
            ResultSet rs = Mysql.getResultSet("select count(*) from user where username = '" + username + "'");

            if (rs.next()){
                int resutlt = rs.getInt(1);

                rs.close();
                return resutlt == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public ArrayList<User> getAll() {
        ResultSet rs = Mysql.getResultSet("select * from user");
        ArrayList<User> users = new ArrayList<>();
        User user = Mapping.mappingUser(rs);

        while (user != null) {
            users.add(user);
            user = Mapping.mappingUser(rs);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean add(User record) {
        return Mysql.add("INSERT INTO user(username,password) VALUES(?,?)",
                record.getUsername(), record.getPassword());
    }
}
