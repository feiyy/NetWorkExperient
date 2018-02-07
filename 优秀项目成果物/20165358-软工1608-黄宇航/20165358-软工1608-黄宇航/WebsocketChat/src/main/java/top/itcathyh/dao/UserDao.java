package top.itcathyh.dao;

import top.itcathyh.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface UserDao {
    public User serviceLogin(String username, String password);

    public User login(String username, String password);
}
