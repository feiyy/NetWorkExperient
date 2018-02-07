package top.itcathyh.service;

import org.apache.log4j.Logger;
import top.itcathyh.action.chat.WebSocketAction;
import top.itcathyh.dao.UserDao;
import top.itcathyh.dao.impl.UserDaoImpl;
import top.itcathyh.entity.User;

import java.io.IOException;
import java.util.*;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket")
public final class WebSocket extends AbstractWebSocket{
    private static final UserDao userdao = UserDaoImpl.getInstance();
    //private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<WebSocketTest>
    private String username = null;
    private int chatid;
    private Logger log = Logger.getLogger(this.getClass());

    @OnOpen
    public void onOpen(Session session) {
        try {
            /* 登录并建立连接 */
            Map<String, List<String>> para = session.getRequestParameterMap();
            String username = para.get("username").get(0);
            String password = para.get("password").get(0);
            User user = userdao.login(username, password);
            this.basic = session.getBasicRemote();

            if (user == null) {
                basic.sendText("notice:登录失败，请检查账号密码");
                session.close();
            } else if (WebSocketAction.isLogin(username)) {
                basic.sendText("notice:用户已在其他地方登录");
                session.close();
            } else {
                this.username = username;
                this.session = session;
                basic.sendText("notice:登录成功");

                WebSocketAction.addOnlineUser(username, this);
            }
        } catch (Exception e) {
            try {
                session.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            log.error(e);
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        WebSocketAction.offline(this);
    }

    @OnMessage
    public void onMsg(String msg, Session session) {
        String data[] = msg.split("--:");

        if (data[0].equals("enterroom")) {
            if (chatid != 0) {
                WebSocketAction.leaveChatroom(chatid, this);
            }

            int chatid = Integer.valueOf(data[1]);
            this.chatid = chatid;

            WebSocketAction.enterChatroom(chatid, this);
        } else if (data[0].equals("chatprivately")) {
            WebSocketAction.chatPrivately(data[1], this);
        } else if (data[0].equals("public")) {
            WebSocketAction.notice(data[0], username, data[1], chatid);
        } else if (data[0].equals("private")) {
            WebSocketAction.notice(data[1], data[2], this);
        } else {
            WebSocketAction.notice("public", username, msg, chatid);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebSocket webSocket = (WebSocket) o;

        return session != null ? session.equals(webSocket.session) : webSocket.session == null;
    }

    @Override
    public int hashCode() {
        return session != null ? session.hashCode() : 0;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getChatid() {
        return chatid;
    }

    public void setChatid(int chatid) {
        this.chatid = chatid;
    }


}