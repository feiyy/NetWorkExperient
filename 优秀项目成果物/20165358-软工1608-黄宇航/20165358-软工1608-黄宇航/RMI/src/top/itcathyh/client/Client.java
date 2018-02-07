package top.itcathyh.client;

import top.itcathyh.action.ClientAction;
import top.itcathyh.entity.User;
import top.itcathyh.service.Service;

import java.rmi.Naming;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class Client {
    public static void main(String args[]) throws Exception {
        Service service = (Service) Naming.lookup("rmi://localhost:8888/service");
        Scanner in = new Scanner(System.in);
        int chose = 0;
        User user = new User();
        boolean islogin = false;

        while (true) {
            System.out.println("1.登录\n2.注册\n3.增加会议\n4.查看范围时间内会议\n5.查看所有会议\n6.删除指定会议\n7.删除所有会议\n8.退出");

            try {
                chose = in.nextInt();
            } catch (Exception e) {
                continue;
            }

            if (chose == 8) {
                System.out.println("告辞");
                break;
            }

            if (!islogin) {
                if (chose == 1 && ClientAction.login(service, in, user)) {
                    islogin = true;
                } else if (chose == 2) {
                    ClientAction.register(service, in);
                } else {
                    System.out.println("请先登录");
                }
            } else {
                if (chose == 3) {
                    ClientAction.addMeeting(service, in, user);
                } else if (chose == 4) {
                    ClientAction.queryMeeting(service, in, user);
                } else if (chose == 5) {
                    ClientAction.queryAllMeeting(service, in, user);
                }else if (chose == 6) {
                    ClientAction.deleteById(service, in, user);
                } else if (chose == 7) {
                    ClientAction.deleteUserName(service, in, user);
                }
            }
        }
    }
}