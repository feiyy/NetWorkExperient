package top.itcathyh.service;

import top.itcathyh.dao.MeetingDao;
import top.itcathyh.dao.UserDao;
import top.itcathyh.dao.impl.MeetingDaoImpl;
import top.itcathyh.dao.impl.UserDaoImpl;
import top.itcathyh.entity.Meeting;
import top.itcathyh.entity.User;

import java.io.BufferedInputStream;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ServiceImpl extends UnicastRemoteObject implements Service {
    private static final UserDao userdao = UserDaoImpl.getInstance();
    private static final MeetingDao meetingdao = MeetingDaoImpl.getInstance();
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public ServiceImpl() throws RemoteException {
    }

    @Override
    public boolean register(String username, String password) throws RemoteException {
        /* 登录 */
        User user = new User(username, password);
        return userdao.add(user);
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        /* 注册 */
        return userdao.login(username, password);
    }

    @Override
    public String addMeeting(String username, String password,
                             String othername, String title, String label,
                             Timestamp starttime, Timestamp endtime) throws RemoteException {
        /* 增加会议 */

        if (starttime.getTime() >= endtime.getTime()) {
            return "时间无效";
        }

        if (!userdao.userExist(othername)) {
            return "用户不存在";
        }

        if (meetingdao.lookup(username, othername, starttime, endtime)) {
            return "时间冲突";
        }

        Meeting meeting = new Meeting();

        meeting.setUsername(username);
        meeting.setLabel(label);
        meeting.setOthername(othername);
        meeting.setTitle(title);
        meeting.setStarttime(starttime.getTime());
        meeting.setEndtime(endtime.getTime());

        if (meetingdao.add(meeting)) {
            return "增加会议成功";
        }

        return "增加会议失败";
    }

    @Override
    public boolean delete(String username, String password, long meetingid) throws RemoteException {
        /* 删除指定会议 */
        return meetingdao.deleteById(username, meetingid);
    }

    @Override
    public boolean clear(String username, String password) throws RemoteException {
        /* 删除用户所有 */
        return meetingdao.deleteByUsername(username);
    }

    @Override
    public String query(String username, Timestamp starttime, Timestamp endtime) throws RemoteException {
        /* 查找范围内的会议 */
        return boxMeetingString(meetingdao.query(username, starttime, endtime));
    }

    @Override
    public String queryAll(String username) throws RemoteException {
        /* 查找用户所有会议 */
        return boxMeetingString(meetingdao.queryAll(username));
    }

    private static String boxMeetingString(List<Meeting> list){
        StringBuilder sb = new StringBuilder();

        if (list != null) {
            for (Meeting tmp : list) {
                sb.append("会议ID");
                sb.append(tmp.getId());
                sb.append(":时间从");
                sb.append(new Timestamp(tmp.getStarttime()));
                sb.append("到");
                sb.append(new Timestamp(tmp.getEndtime()));
                sb.append("\n标题:");
                sb.append(tmp.getTitle());
                sb.append("\n标签:");
                sb.append(tmp.getLabel());
                sb.append("\n与会人：");
                sb.append(tmp.getOthername());
                sb.append("\n");
            }
        } else {
            return null;
        }

        return sb.length() == 0 ? null : sb.toString();
    }
}
