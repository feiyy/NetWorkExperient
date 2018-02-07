package faye.webservice.websocket;

import faye.webservice.manager.Manager;
import faye.webservice.manager.ManagerMessageDao;
import faye.webservice.manager.MyStack;
import faye.webservice.message.Message;

import faye.webservice.session.GetHttpSessionConfigurator;
import faye.webservice.user.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

@ServerEndpoint(value="/all",configurator=GetHttpSessionConfigurator.class)
@Component
public class MyWebSocket {

    private static HashMap<String,Session> sessionManager = new HashMap<>();
    private static MyStack managers = new MyStack();
    private static Stack<User> usersWithNoManager = new Stack<>();
    private static ManagerMessageDao dao = new ManagerMessageDao();
    private static HashMap<String,Manager> userToManager = new HashMap<>();
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,EndpointConfig config) {
        //从httpsession中获得user对象
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User user = (User) httpSession.getAttribute("user");
        //判断是否登录了
        if (user==null||sessionManager.get(user.getUserName())!=null){
            return;
        }
        sessionManager.put(user.getUserName(),session);
        session.getUserProperties().put("user",user);
        Message message = new Message();
        message.setTo(user.getUserName());
        message.setContent(user.getUserName());
        message.setAction(Message.MY_SELF);
        sendMessage(message,null);
        //判断是否是管理员
        if (user.isManager()){
            managers.add((Manager) user);
            while (!usersWithNoManager.isEmpty()){
                changeManager(usersWithNoManager.pop());
            }
        }else {
            changeManager(user);
        }



    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        User u = (User) session.getUserProperties().get("user");
        sessionManager.remove(u.getUserName());
        if (u.isManager()){
            changeToNewManager((Manager) u);
        }
        System.out.println("移除了"+u.getUserName());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage(maxMessageSize = 10000000)
    public void onMessage(String message, Session session) {
        User u = (User) session.getUserProperties().get("user");
        JSONObject jsonObject = new JSONObject(message);
        Message theSendMessage = new Message(jsonObject);
        sendMessage(theSendMessage,session);
    }
    public static HashMap<String, Session> getSessionManager() {
        return sessionManager;
    }
    public static   void sendMessage(Message message,Session fromSession){
        User user =null;
        if (fromSession!=null)
            user = (User) fromSession.getUserProperties().get("user");
        try {
            if (message.getAction()==Message.ACTION_ONLINE){
                Session toWho = sessionManager.get(message.getTo());
                toWho.getBasicRemote().sendText(message.toString());
            }else if (message.getAction()==Message.MY_MANAGER){
                String toWho = message.getTo();
                Session session = sessionManager.get(toWho);
                session.getBasicRemote().sendText(message.toString());
            }else if (message.getAction()==Message.SEND_TO_MANAGER){
                if (user.isManager()){//如果是客服发送的消息
                    Session toWho = sessionManager.get(message.getTo());
                    toWho.getBasicRemote().sendText(message.toString());
                    message.setUserIsWho(message.getTo());
                    dao.add(user.getUserName(),message);
                }else {
                    Manager manager =  userToManager.get(user.getUserName());//获得他的客服
                    message.setUserIsWho(user.getUserName());
                    if (manager==null){
                        //发送没有客服的逻辑
                        Message notifyHasManager = new Message();
                        notifyHasManager.setAction(Message.MY_MANAGER);
                        notifyHasManager.setTo(user.getUserName());
                        notifyHasManager.setContent("noManager");
                        sendMessage(notifyHasManager,null);
                        return;
                    }
                    message.setTo(manager.getUserName());
                    Session toWho =sessionManager.get(manager.getUserName());
                    toWho.getBasicRemote().sendText(message.toString());
                    dao.add(manager.getUserName(),message);
                }

            }else if (message.getAction()==Message.MY_SELF){
                String toWho = message.getTo();
                Session session = sessionManager.get(toWho);
                session.getBasicRemote().sendText(message.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //将用户的客服进行更改
    public static   Manager changeManager( User user) {
        ////通知多了一个客户
        Manager manager = managers.peek();
        Message notifyHasManager = new Message();
        notifyHasManager.setAction(Message.MY_MANAGER);
        notifyHasManager.setTo(user.getUserName());
        if (manager==null){
            notifyHasManager.setContent("noManager");
            sendMessage(notifyHasManager,null);
            if (!usersWithNoManager.contains(user))
                usersWithNoManager.push(user);
            return null;
        }else {
            notifyHasManager.setContent(manager.getUserName());
            //sendMessage(notifyHasManager,null);
        }
        manager.add(user);
        userToManager.put(user.getUserName(),manager);
        managers.adjust();
        sendHello(manager,user);//发送一条，你好我是客服** ，的消息
        Session managerSession = sessionManager.get(manager.getUserName());
        Message onlineMessage = new Message();
        JSONArray jsonArray = manager.toJsonUser();//获得当前的所有客户的信息
        onlineMessage.setClients(jsonArray);
        onlineMessage.setTo(manager.getUserName());
        onlineMessage.setAction(Message.ACTION_ONLINE);
        sendMessage(onlineMessage,managerSession);
        return manager;
    }
    public static void sendHello(Manager manager,User user){
        if (manager==null){
            return;
        }
        //发送你好的消息
        String from = manager.getUserName();
        String to = user.getUserName();
        String content = "你好，我是"+manager.getUserName();
        Message message1 = new Message();
        message1.setAction(Message.SEND_TO_MANAGER);
        message1.setFrom(from);
        message1.setTo(to);
        message1.setContent(content);
        Session session = sessionManager.get(manager.getUserName());
        sendMessage(message1,session);

    }
    public void changeToNewManager(Manager removeManager){

        managers.remove(removeManager);
        Manager manager = managers.peek();
        for(User user: removeManager.getUsers()){
            userToManager.put(user.getUserName(),manager);
        }
        if (manager==null){
            usersWithNoManager.addAll(removeManager.getUsers());
            removeManager.getUsers().clear();
            return;
        }
        managers.adjust();

        Session managerSession = sessionManager.get(manager.getUserName());

        ArrayList<Message> messages = dao.get(removeManager.getUserName());
        dao.add(manager.getUserName(),messages);
        if (messages!=null){
            for (Message message : messages){
                try {
                    managerSession.getBasicRemote().sendText(message.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for(User user: removeManager.getUsers()){
            sendHello(manager,user);
            manager.add(user);
        }
        Message onlineMessage = new Message();
        onlineMessage.setAction(Message.ACTION_ONLINE);
        onlineMessage.setTo(manager.getUserName());
        onlineMessage.setClients(manager.toJsonUser());
        sendMessage(onlineMessage,null);
        removeManager.getUsers().clear();

    }
    public static MyStack getManagers() {
        return managers;
    }

    public static HashMap<String, Manager> getUserToManager() {
        return userToManager;
    }
/*    public static void mangerGetTask(String userName){
        System.out.println(userName);
        User user = (User) sessionManager.get(userName).getUserProperties().get("user");

        if (user.isManager()){//一个客服上线就分配用户
            managers.add((Manager) user);
            for (Session session1 : sessionManager.values()){
                User u = (User) session1.getUserProperties().get("user");
                if ((!u.isManager())&&userToManager.get(u.getUserName())==null){
                    changeManager(u);
                }
            }

        }
    }*/
}
