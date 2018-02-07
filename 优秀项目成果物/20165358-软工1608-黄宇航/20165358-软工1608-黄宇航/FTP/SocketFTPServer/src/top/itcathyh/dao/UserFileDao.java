package top.itcathyh.dao;

import top.itcathyh.entity.UserFile;

import java.util.ArrayList;
import java.util.List;

public interface UserFileDao {
    public UserFile getById(long id);

    public UserFile getByName(String filename);

    public ArrayList<UserFile> getByUserid(long userid);

    public boolean add(UserFile record);

    public boolean update(UserFile record);

    public boolean deleteById(long id);
}
