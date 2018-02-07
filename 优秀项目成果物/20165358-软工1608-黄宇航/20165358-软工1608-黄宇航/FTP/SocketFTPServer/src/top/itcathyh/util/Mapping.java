package top.itcathyh.util;

import org.omg.CORBA.PUBLIC_MEMBER;
import top.itcathyh.entity.User;
import top.itcathyh.entity.UserFile;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class Mapping {
    public static User mappingUser(ResultSet set) {
        User user = null;

        try {
            if (set.next()) {
                user = new User();

                user.setId(set.getLong("id"));
                user.setUsername(set.getString("username"));
                user.setPassword(set.getString("password"));
                user.setEmail(set.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static UserFile mappingUserFile(ResultSet set) {
        UserFile file = null;

        try {
            if (set.next()) {
                file = new UserFile();

                file.setId(set.getLong("id"));
                file.setUserid(set.getLong("userid"));
                file.setFilename(set.getString("filename"));
                file.setSummary(set.getString("summary"));
                file.setType(set.getString("type"));
                file.setUploadtime(set.getTimestamp("uploadtime"));
                file.setSize(set.getInt("size"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return file;
    }
}
