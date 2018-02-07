package top.itcathyh.dao.impl;

import top.itcathyh.dao.UserFileDao;
import top.itcathyh.entity.UserFile;
import top.itcathyh.util.Mapping;
import top.itcathyh.util.Mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserFileDaoImpl implements UserFileDao {
    volatile private static UserFileDaoImpl instance = null;

    private UserFileDaoImpl() {
    }

    public static UserFileDaoImpl getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (UserFileDaoImpl.class) {
                    if (instance == null) {
                        instance = new UserFileDaoImpl();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    @Override
    public UserFile getById(long id) {
        ResultSet rs = Mysql.getResultSet("select * from file where id = " + id);
        UserFile file = Mapping.mappingUserFile(rs);

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return file;
    }

    @Override
    public UserFile getByName(String filename) {
        ResultSet rs = Mysql.getResultSet("select path,id from file where filename = '" + filename + "'");

        try {
            if (rs.next()) {
                UserFile userfile = new UserFile();

                userfile.setId(rs.getLong("id"));
                userfile.setPath(rs.getString("path"));

               return userfile;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    public ArrayList<UserFile> getByUserid(long userid) {
        ResultSet rs = Mysql.getResultSet("select * from file where userid = 0 or userid = " + userid);
        ArrayList<UserFile> files = new ArrayList<>();
        UserFile userfile = Mapping.mappingUserFile(rs);

        while (userfile != null) {
            files.add(userfile);
            userfile = Mapping.mappingUserFile(rs);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return files;
    }

    @Override
    public boolean add(UserFile record) {
        return Mysql.add("INSERT INTO file(filename,path) VALUES(?,?)",
                record.getFilename(), record.getPath());
    }

    @Override
    public boolean update(UserFile record) {
        return Mysql.update("UPDATE file " +
                "SET summary =" + record.getSummary() +
                ", type = " + record.getType() +
                " where id=" + record.getId());
    }

    @Override
    public boolean deleteById(long id) {
        return Mysql.delete("delete from file where id = " + id);
    }

    public static void main(String args[]){
        UserFileDao dao = UserFileDaoImpl.getInstance();

        System.out.println(dao.getByName("test").getPath());
    }
}
