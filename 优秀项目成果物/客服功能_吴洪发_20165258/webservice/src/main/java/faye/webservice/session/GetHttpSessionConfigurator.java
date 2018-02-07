package faye.webservice.session;


import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;
import java.util.Map;
/*
 * 获取HttpSession
 *
 */

public class GetHttpSessionConfigurator extends Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec,
                                HandshakeRequest request, HandshakeResponse response) {

        HttpSession httpSession=(HttpSession) request.getHttpSession();
        Map<String ,Object> map = sec.getUserProperties();
        System.out.println(map);
        map.put(HttpSession.class.getName(),httpSession);

    }

}