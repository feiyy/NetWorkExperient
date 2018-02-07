package faye.webservice.manager;

import faye.webservice.message.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class ManagerMessageDao {
    private HashMap<String,ArrayList<Message>> messages = new HashMap<>();
    public void add(String managerName,Message message){
        initilize(managerName);
        messages.get(managerName).add(message);
    }
    public HashMap<String, ArrayList<Message>> getMessages() {
        return messages;
    }
    public ArrayList<Message> get(String managerName){
        initilize(managerName);
        return messages.get(managerName);
    }

    private void initilize(String managerName) {
        ArrayList<Message> list = messages.get(managerName);
        if (list==null){
            list =new ArrayList<>();
            messages.put(managerName,list);
        }
    }

    public void add(String managerName,ArrayList<Message> addMessages){
        initilize(managerName);
        messages.get(managerName).addAll(addMessages);
    }
    public void clear(String managerName){
        initilize(managerName);
        messages.get(managerName).clear();
    }

}
