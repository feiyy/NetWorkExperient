package top.itcathyh.action.chat;

import top.itcathyh.entity.User;
import top.itcathyh.service.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public final class WebSocketAction {
    private static final Hashtable<Integer, ArrayList<WebSocket>> chatromm = new Hashtable<Integer, ArrayList<WebSocket>>();
    private static final Hashtable<String, WebSocket> onlineuser = new Hashtable<>();

    public static void notice(String type, String username, String text, int chatid) {
        /* 聊天室群发 */
        String msg = type + "--:" + username + "--:" + text;
        ArrayList<WebSocket> list = chatromm.get(chatid);

        try {
            if (list != null) {
                for (WebSocket tmp : list) {
                    tmp.sendMsg(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void notice(String text, String tousername, WebSocket fromuser) {
        /* 私聊 */
        WebSocket touser = onlineuser.get(tousername);
        String msg = "private" + "--:" + fromuser.getUsername() + "--:" + text;

        try {
            if (touser != null) {
                touser.sendMsg(msg);
                fromuser.sendMsg(msg);
            } else {
                fromuser.sendMsg("notice:该用户已下线");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void addOnlineUser(String username, WebSocket user) {
        onlineuser.put(username, user);
    }

    public static boolean isLogin(String username) {
        return onlineuser.containsKey(username);
    }

    public static void enterChatroom(int chatid, WebSocket user) {
        /* 进入房间 */
        ArrayList<WebSocket> users = chatromm.computeIfAbsent(chatid, k -> new ArrayList<WebSocket>(10));

        users.add(user);
        notice("public", "通知", user.getUsername() + "进入房间", chatid);
        notice("userlist", "admin", getRoomUser(chatid), chatid);
    }

    public static void offline(WebSocket user) {
        if (user != null && user.getUsername() != null) {
            leaveChatroom(user.getChatid(), user);
            onlineuser.remove(user.getUsername());
        }
    }

    public static void leaveChatroom(int chatid, WebSocket user) {
        ArrayList<WebSocket> users = chatromm.get(chatid);

        if (users != null && user != null) {
            users.remove(user);

            if (users.size() == 0) {
                chatromm.remove(users);
            }
        }

        notice("public", "通知",
                "有人离开了房间" + chatid + ",当前房间人数为" + WebSocketAction.getRommUserNum(chatid), chatid);
    }

    public static void chatPrivately(String tousername, WebSocket user) {
        /* 发送私聊消息 */

        try {
            if (tousername.equals(user.getUsername()) || onlineuser.get(tousername) == null) {
                user.sendMsg("notice:用户名无效或不在线");
            } else {
                user.sendMsg("notice:用户" + tousername + "已连线");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getRommUserNum(int chatid) {
        List<WebSocket> tmp = chatromm.get(chatid);

        return tmp == null ? 0 : tmp.size();
    }

    private static String getRoomUser(int chatid) {
        /*  获取房间内用户 */
        StringBuilder sb = new StringBuilder();
        ArrayList<WebSocket> list = chatromm.get(chatid);

        if (list != null) {
            for (WebSocket tmp : list) {
                sb.append(tmp.getUsername()).append("||");
            }
        }

        return sb.replace(sb.length() - 2, sb.length(), "").toString();
    }
}
