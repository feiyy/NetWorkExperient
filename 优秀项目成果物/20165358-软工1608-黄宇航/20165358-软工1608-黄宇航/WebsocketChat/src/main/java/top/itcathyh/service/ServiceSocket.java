package top.itcathyh.service;

import jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm;
import org.apache.log4j.Logger;
import top.itcathyh.action.service.ControAction;
import top.itcathyh.action.service.ServiceAction;
import top.itcathyh.dao.UserDao;
import top.itcathyh.dao.impl.UserDaoImpl;
import top.itcathyh.entity.User;

import javax.naming.directory.SearchControls;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint("/service")
public class ServiceSocket extends AbstractWebSocket {
    private static final UserDao userdao = UserDaoImpl.getInstance();
    private final LinkedList<ClientSocket> clients = new LinkedList<>();
    private final Hashtable<Integer, ArrayList<String>> msgs = new Hashtable<>(ControAction.getMaxcount());
    private int id = 0;
    private Logger log = Logger.getLogger(this.getClass());

    @OnOpen
    public void onOpen(Session session) {
        try {
            Map<String, List<String>> para = session.getRequestParameterMap();
            String username = para.get("username").get(0);
            String password = para.get("password").get(0);
            User user = userdao.serviceLogin(username, password);
            this.basic = session.getBasicRemote();

            if (user == null) {
                basic.sendText("notice:登录失败，请检查账号密码");
                session.close();
            } else if (ServiceAction.isLogin(user.getNumber())) {
                basic.sendText("notice:用户已在其他地方登录");
                session.close();
            } else {
                this.id = user.getNumber();
                this.session = session;

                if (!ControAction.isRunning()) {
                    ControAction.beginAlloting();
                }

                basic.sendText("notice:登录成功");
                ServiceAction.addService(this);
                ServiceAction.notice("list--:" + ServiceAction.getServiceList());
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
        ServiceAction.serviceOffline(this);
    }

    @OnMessage
    public void onMsg(String msg, Session session) {
        String data[] = msg.split("--:");

        if (data[0].equals("nor")) {
            ClientSocket client = ServiceAction.getClinet(Integer.valueOf(data[1]));

            if (client != null) {
                ServiceAction.sendMsg("工号" + id + ":" + data[2], client.getId(), client, this);

                if (!msgs.containsKey(client.getId())) {
                    msgs.put(client.getId(), new ArrayList<String>());
                }

                msgs.get(client.getId()).add("工号" + id + ":" + data[2]);
            }
        } else if (data[0].equals("end")) {
            ClientSocket client = ServiceAction.getClinet(Integer.valueOf(data[1]));

            if (client != null && client.isOpen()) {
                clients.remove(client);

                try {
                    client.sendMsg("end");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (data[0].equals("trans")) {
            /* 转接 */
            int clientid = Integer.valueOf(data[1]);
            ClientSocket client = ServiceAction.getClinet(clientid);
            ServiceSocket service = ServiceAction.getService(Integer.valueOf(data[2]));

            if (clients.contains(client) && service != null && service.getCount() < ControAction.getMaxcount()) {
                    try {
                    service.addClient(client);
                    clients.remove(client);
                    client.setService(service);
                    service.sendMsg("msgs--:" + clientid + "--:" + getMsg(msgs.get(clientid)));
                    sendMsg("转接成功--:" + clientid);
                    msgs.remove(clientid);
                    ControAction.addFreeService(service);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sendMsg("notice:转接失败");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void addClient(ClientSocket client) {
        /* 增加该客服的客户 */
        clients.add(client);

        StringBuilder sb = new StringBuilder();

        sb.append("notice: 用户");
        sb.append(client.getId());
        sb.append("已接入客服");
        sb.append(id);
        ServiceAction.sendMsg(sb.toString(), client.getId(), client, this);
    }

    public void removeClient(ClientSocket client) {
        clients.remove(client);
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return clients.size();
    }

    public LinkedList<ClientSocket> getClients() {
        return clients;
    }

    Hashtable<Integer, ArrayList<String>> getMsgs() {
        return msgs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServiceSocket that = (ServiceSocket) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    private static String getMsg(ArrayList<String> msglist) {
        if (msglist == null){
            return "";
        }

        int len = msglist.size() - 1;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            sb.append(msglist.get(i)).append("||");
        }

        sb.append(msglist.get(len));

        return sb.toString();
    }
}
