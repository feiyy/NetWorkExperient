package top.itcathyh.action;

import top.itcathyh.entity.User;
import top.itcathyh.service.Service;

import java.sql.Timestamp;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class ClientAction {
    private static final String pattern = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)\\s+([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";

    public static boolean login(Service service, Scanner in, User user) throws Exception {
        System.out.println("输入账号：");

        String username = in.next();

        System.out.println("输入密码：");

        String password = in.next();

        if (service.login(username, password)) {
            System.out.println("登录成功");
            user.setUsername(username);
            user.setPassword(password);
            return true;
        } else {
            System.out.println("登录失败");
            return false;
        }
    }

    public static void register(Service service, Scanner in) throws Exception {
        String username, password;

        while (true) {
            System.out.println("输入账号：");

            username = in.next();

            System.out.println("输入密码：");

            password = in.next();

            if (username.length() > 16 || password.length() > 16) {
                System.err.println("请输入16位以内的账号密码");
            } else {
                break;
            }
        }

        if (service.register(username, password)) {
            System.out.println("注册成功");
        } else {
            System.out.println("注册失败");
        }
    }

    public static void addMeeting(Service service, Scanner in, User user) throws Exception {
        System.out.println("输入另一用户账号：");

        String othername = in.next();

        System.out.println("输入标题：");

        String title = in.next();

        System.out.println("输入标签：");

        String label = in.next();
        String starttime = getTime("输入开始时间(格式2018-01-21 16:00:00)：", in);
        String endtime = getTime("输入结束时间(格式2018-01-21 16:00:00)：", in);

        System.out.println(service.addMeeting(user.getUsername(), user.getPassword(), othername,
                title, label, Timestamp.valueOf(starttime), Timestamp.valueOf(endtime)));

    }

    public static void queryMeeting(Service service, Scanner in, User user) throws Exception {
        String starttime = getTime("输入开始时间(格式2018-01-21 16:00:00)：", in);
        String endtime = getTime("输入结束时间(格式2018-01-21 16:00:00)：", in);
        String meetings = service.query(user.getUsername(), Timestamp.valueOf(starttime), Timestamp.valueOf(endtime));

        if (meetings != null) {
            System.out.println(meetings);
        } else {
            System.out.println("暂无会议，请添加一个吧~");
        }
    }

    public static void queryAllMeeting(Service service, Scanner in, User user) throws Exception {
        String meetings = service.queryAll(user.getUsername());

        if (meetings != null) {
            System.out.println(meetings);
        } else {
            System.out.println("暂无会议，请添加一个吧~");
        }
    }

    public static String getTime(String msg, Scanner in) {
        String time;

        while (true) {
            System.out.println(msg);

            time = in.nextLine();

            if (!Pattern.matches(pattern, time)) {
                System.out.println("无效时间，请重新输入");
            } else {
                break;
            }
        }

        return time;
    }

    public static void deleteById(Service service, Scanner in, User user) throws Exception {
        System.out.println("输入会议ID：");

        try {
            long id = in.nextLong();

            if (service.delete(user.getUsername(), user.getPassword(), id)) {
                System.out.println("删除会议" + id + "成功");
            } else {
                System.out.println("删除会议" + id + "失败");
            }
        } catch (Exception e) {
            System.out.println("无效ID");
        }

    }

    public static void deleteUserName(Service service, Scanner in, User user) throws Exception {
        if (service.clear(user.getUsername(), user.getPassword())) {
            System.out.println("删除用户所有会议成功");
        } else {
            System.out.println("删除用户所有会议失败");
        }
    }
}
