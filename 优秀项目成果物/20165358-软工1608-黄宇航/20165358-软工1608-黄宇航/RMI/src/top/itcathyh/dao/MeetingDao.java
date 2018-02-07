package top.itcathyh.dao;


import top.itcathyh.entity.Meeting;

import java.sql.Timestamp;
import java.util.List;

public interface MeetingDao {
    public boolean add(Meeting record);

    public List<Meeting> query(String username, Timestamp starttime, Timestamp endtime);

    public List<Meeting> queryAll(String username);

    public boolean lookup(String username, String othername, Timestamp starttime, Timestamp endtime);

    public boolean deleteById(String username, long id);

    public boolean deleteByUsername(String username);
}
