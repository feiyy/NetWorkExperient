package faye.webservice.manager;



import faye.webservice.user.User;
import org.json.JSONArray;

import java.util.ArrayList;

public class Manager extends User {

    private ArrayList<User> users = new ArrayList<>();
    private int amount;
    @Override
    public ArrayList<User> getUsers() {
        return this.users;
    }
    public void add(User user){
        amount++;
        this.users.add(user);
    }
    public void remove(String name){
        User user  = new User();
        user.setUserName(name);
        users.remove(user);
        amount--;
    }

    public int getAmount() {
        return amount;
    }

    public JSONArray toJsonUser(){
        JSONArray jsonArray = new JSONArray();
        for (User user : users){
            jsonArray.put(user.getUserName());
        }
        return jsonArray;
    }


}
