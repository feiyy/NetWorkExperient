package top.itcathyh.service;

import org.apache.log4j.Logger;
import top.itcathyh.action.service.ControAction;
import top.itcathyh.action.service.ServiceAction;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

@ServerEndpoint("/client")
public class ClientSocket extends AbstractWebSocket {
    private Logger log = Logger.getLogger(this.getClass());
    private int id;
    private ServiceSocket service;

    @OnOpen
    public void onOpen(Session session) {
        try {
            basic = session.getBasicRemote();
            this.session = session;

            if (!ServiceAction.haveService()) {
                sendMsg("notice：客服暂未上班，请稍后重试");
                session.close();
            } else {
                if (ControAction.isBusy()){
                    sendMsg("notice：客服正忙，请稍候");
                }

                ServiceAction.addClient(this);
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
        ServiceAction.clientOffline(this);
    }

    @OnMessage
    public void onMsg(String msg, Session session) {
        msg = id + ":" + msg;
        Hashtable<Integer, ArrayList<String>> map = service.getMsgs();

        if (!map.containsKey(id)) {
            map.put(id, new ArrayList<String>());
        }

        map.get(id).add(msg);
        ServiceAction.sendMsg(msg, id, this, service);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServiceSocket getService() {
        return service;
    }

    public void setService(ServiceSocket service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientSocket that = (ClientSocket) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
