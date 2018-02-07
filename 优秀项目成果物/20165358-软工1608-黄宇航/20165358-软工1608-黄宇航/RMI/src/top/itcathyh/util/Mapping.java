package top.itcathyh.util;

import top.itcathyh.entity.Meeting;
import top.itcathyh.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class Mapping {
    public static User mappingUser(ResultSet set) {
        User user = null;

        try {
            if (set.next()) {
                user = new User();

                user.setUsername(set.getString("username"));
                user.setPassword(set.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static Meeting mappingMeeting(ResultSet set) {
        Meeting meeting = null;

        try {
            if (set.next()) {
                meeting = new Meeting();

                meeting.setId(set.getLong("id"));
                meeting.setUsername(set.getString("username"));
                meeting.setOthername(set.getString("othername"));
                meeting.setTitle(set.getString("title"));
                meeting.setLabel(set.getString("label"));
                meeting.setStarttime(set.getLong("starttime"));
                meeting.setEndtime(set.getLong("endtime"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return meeting;
    }
}
