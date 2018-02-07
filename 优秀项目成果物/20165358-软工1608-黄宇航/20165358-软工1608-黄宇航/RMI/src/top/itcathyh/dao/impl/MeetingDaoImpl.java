package top.itcathyh.dao.impl;

import top.itcathyh.dao.MeetingDao;
import top.itcathyh.entity.Meeting;
import top.itcathyh.util.Mapping;
import top.itcathyh.util.Mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MeetingDaoImpl implements MeetingDao {
    volatile private static MeetingDaoImpl instance = null;

    private MeetingDaoImpl() {
    }

    public static MeetingDaoImpl getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (MeetingDaoImpl.class) {
                    if (instance == null) {
                        instance = new MeetingDaoImpl();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    @Override
    public boolean add(Meeting record) {
        return Mysql.add("INSERT INTO meeting(username, othername, title, label, starttime, endtime) VALUES(?,?,?,?,?,?)",
                record.getUsername(), record.getOthername(), record.getTitle(), record.getLabel(), record.getStarttime(), record.getEndtime());
    }

    @Override
    public List<Meeting> query(String username, Timestamp starttime, Timestamp endtime) {
        ResultSet rs = Mysql.getResultSet("select * from meeting where username = '" + username + "'" +
                " and starttime >= " + starttime.getTime() + " and endtime <= " + endtime.getTime());
        ArrayList<Meeting> meetings = new ArrayList<>();
        Meeting meeting = Mapping.mappingMeeting(rs);

        while (meeting != null) {
            meetings.add(meeting);
            meeting = Mapping.mappingMeeting(rs);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return meetings;
    }

    @Override
    public List<Meeting> queryAll(String username) {
        ResultSet rs = Mysql.getResultSet("select * from meeting where username = '" + username + "'");
        ArrayList<Meeting> meetings = new ArrayList<>();
        Meeting meeting = Mapping.mappingMeeting(rs);

        while (meeting != null) {
            meetings.add(meeting);
            meeting = Mapping.mappingMeeting(rs);
        }

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return meetings;
    }

    @Override
    public boolean lookup(String username, String othername, Timestamp starttime, Timestamp endtime) {
        if (endtime.getTime() <= starttime.getTime()) {
            return false;
        }

        long st = starttime.getTime();
        long et = endtime.getTime();
        ResultSet rs = Mysql.getResultSet("select * from meeting where username = '" + username + "'" +
                " or othername = '" + othername + "' and ((starttime < " + st + " and endtime > " + et + ") or (starttime > " +
                st + " and endtime <" + et + "))");
        boolean flag = false;

        try {
            flag = rs.next();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

    @Override
    public boolean deleteById(String username, long id) {
        return Mysql.delete("delete from meeting where id = " + id + " and username = '" + username + "'");
    }

    @Override
    public boolean deleteByUsername(String username) {
        return Mysql.delete("delete from meeting where username = '" + username + "'");
    }
}
