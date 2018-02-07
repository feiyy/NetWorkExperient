package top.itcathyh.service;

import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

abstract public class AbstractWebSocket {
    Session session = null;
    RemoteEndpoint.Basic basic = null;

    public void sendMsg(String msg) throws IOException {
        basic.sendText(msg);
    }

    public boolean isOpen(){
        return session.isOpen();
    }

    public void closeSession() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
