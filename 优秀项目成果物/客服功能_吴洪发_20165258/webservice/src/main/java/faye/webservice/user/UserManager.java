package faye.webservice.user;

import faye.webservice.manager.Manager;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class UserManager {
    private HashMap<String,User> map;
    public UserManager(){
        map = new HashMap<>();
        User a =new User();
        User b = new User();
        User c = new User();
        User d = new User();
        User room = new User();
        room.setUserName("聊天室");
        User manager = new User();
        manager.setUserName("客服");
        a.setId("1");
        a.setPassword("a");
        a.setUserName("a");
        b.setId("2");
        b.setUserName("b");
        b.setPassword("b");
        c.setId("3");
        c.setPassword("c");
        c.setUserName("c");
        d.setId("4");
        d.setPassword("d");
        d.setUserName("d");
        a.addFriend(b);
        a.addFriend(c);
        a.addFriend(d);
        b.addFriend(a);
        c.addFriend(a);
        d.addFriend(a);
        a.addFriend(room);
        b.addFriend(room);
        c.addFriend(room);
        d.addFriend(room);
        a.addFriend(manager);
        b.addFriend(manager);
        c.addFriend(manager);
        d.addFriend(manager);
        map.put(a.getUserName(),a);
        map.put(b.getUserName(),b);
        map.put(c.getUserName(),c);
        map.put(d.getUserName(),d);
        User manager1 = new Manager();
        manager1.setUserName("客服小红");
        manager1.setPassword("123");
        manager1.setManager(true);
        User manager2 = new Manager();
        manager2.setUserName("客服小明");
        manager2.setPassword("123");
        manager2.setManager(true);
        map.put(manager1.getUserName(),manager1);
        map.put(manager2.getUserName(),manager2);
    }

    public HashMap<String, User> getMap() {
        return map;
    }
    public User find(String userName,String password){
        for(User u: map.values()){
            if (u.getUserName().equals(userName)&&u.getPassword().equals(password)){
                return u;
            }
        }
        return null;
    }

}
