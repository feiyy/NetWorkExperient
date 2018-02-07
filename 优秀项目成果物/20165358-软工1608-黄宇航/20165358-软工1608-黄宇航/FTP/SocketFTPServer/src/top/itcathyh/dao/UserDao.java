package top.itcathyh.dao;

import top.itcathyh.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface UserDao {
    public User getById(long userid);

    public User login(String username, String password);

    public boolean userExist(String username);

    public ArrayList<User> getAll();

    public boolean add(User record);
}
