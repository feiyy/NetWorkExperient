package top.itcathyh.util;

import top.itcathyh.entity.User;

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
                user.setNumber(set.getInt("number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
