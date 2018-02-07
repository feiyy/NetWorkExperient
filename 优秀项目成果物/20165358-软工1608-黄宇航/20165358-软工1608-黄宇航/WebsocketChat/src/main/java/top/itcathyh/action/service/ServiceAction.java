package top.itcathyh.action.service;

import org.apache.log4j.Logger;
import top.itcathyh.service.ClientSocket;
import top.itcathyh.service.ServiceSocket;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public final class ServiceAction {
    private static final Hashtable<Integer, ServiceSocket> servicemap = new Hashtable<>();
    private static final Hashtable<Integer, ClientSocket> clientmap = new Hashtable<>();
    private static final Logger log = Logger.getLogger(ServiceAction.class);

    synchronized public static void addService(ServiceSocket service) {
        servicemap.put(service.getId(), service);
        ControAction.addFreeService(service);
    }

    synchronized public static void serviceOffline(ServiceSocket service) {
        /* 客服离线 */
        servicemap.remove(service.getId());
        LinkedList<ClientSocket> clients = service.getClients();
        service = null;

        for (ClientSocket client : clients) {
            if (client != null && client.isOpen()) {
                try {
                    client.sendMsg("notice:客服莫名消失了，请重新接入客服吧");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addClient(ClientSocket client) {
        /*  客户加入 */
        Random random = new Random(new Date().getTime());
        int id = random.nextInt(Integer.MAX_VALUE);

        while (true) {
            /* 分配唯一客户ID */
            while (clientmap.containsKey(id)) {
                id = random.nextInt(Integer.MAX_VALUE);
            }

            clientmap.put(id, client);

            if (clientmap.get(id) == client) {
                break;
            }
        }

        client.setId(id);
        clientmap.put(id, client);
        ControAction.addClient(client);
    }

    public static void clientOffline(ClientSocket client) {
        /* 客户结束服务 */
        int id = client.getId();
        ServiceSocket service = client.getService();
        clientmap.remove(id);

        if (service != null && service.isOpen()) {
            service.removeClient(client);
            client = null;

            try {
                service.sendMsg("notcie:客户断开连接:" + id);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ControAction.addFreeService(service);
        }
    }

    public static ClientSocket getClinet(int clientid) {
        return clientmap.get(clientid);
    }

    public static void sendMsg(String msg, int clientid, ClientSocket client, ServiceSocket service) {
        /* 发送一对一消息 */
        try {
            if (clientmap.containsKey(clientid) && servicemap.containsValue(service)) {
                client.sendMsg(msg);
                service.sendMsg(clientid + "--:" + msg);
            } else if (clientmap.containsKey(clientid)) {
                client.sendMsg("notice:客服莫名消失了，请重新接入客服吧");
            } else if (servicemap.containsValue(service)) {
                service.sendMsg("notcie:客户断开连接:" + clientid);
            } else {
                log.error("c/s send bug");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean haveService() {
        return servicemap.size() != 0;
    }

    public static void notice(String msg) {
        Enumeration<ServiceSocket> e = servicemap.elements();
        ServiceSocket service;

        try {
            while (e.hasMoreElements()) {

                service = e.nextElement();

                if (service.isOpen()) {
                    service.sendMsg(msg);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getServiceList() {
        /* 获取在线客服列表，方便转接 */
        Enumeration<ServiceSocket> e = servicemap.elements();
        StringBuilder sb = new StringBuilder();
        ServiceSocket service;

        while (e.hasMoreElements()) {
            service = e.nextElement();
            sb.append(service.getId());
            sb.append(" ");
            sb.append(service.getCount() == ControAction.getMaxcount() ? "忙碌" : "空闲");
            sb.append("||");
        }

        return sb.replace(sb.length() - 2, sb.length(), "").toString();
    }

    public static boolean isLogin(int id) {
        return servicemap.containsKey(id);
    }

    public static ServiceSocket getService(int id) {
        return servicemap.get(id);
    }
}
