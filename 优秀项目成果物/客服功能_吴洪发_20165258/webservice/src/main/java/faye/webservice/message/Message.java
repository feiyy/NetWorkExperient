package faye.webservice.message;

import org.json.JSONArray;
import org.json.JSONObject;

public class Message {
    private String from;
    private String to;
    private int action;
    private String content;
    private String image="no";
    private String userIsWho="";//表示谁是客户
    private JSONArray clients=null;//客户
    public static final int ACTION_ONLINE=0;//通知上线功能，发送的是当前好友位
    public static final int MY_SELF=1;//获得自己的名字
    public static final int SEND_TO_MANAGER=2;//客服和客户的消息相互发送的标识字段
    public static final int MY_MANAGER=3;//向用户发送一个没有客服的消息
    public Message(){

    }

    public JSONArray getClients() {
        return clients;
    }

    public void setClients(JSONArray clients) {
        this.clients = clients;
    }

    public void setUserIsWho(String userIsWho) {
        this.userIsWho = userIsWho;
    }

    public String getUserIsWho() {
        return userIsWho;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Message(JSONObject jsonObject){
        action = jsonObject.getInt("action");
        content = jsonObject.getString("content");
        to = jsonObject.getString("to");
        from = jsonObject.getString("from");
        image=jsonObject.getString("image");

    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("from",from);
        jsonObject.put("to",to);
        jsonObject.put("action",action);
        jsonObject.put("content",content);
        jsonObject.put("image",image);
        if (!userIsWho.equals("")){
            jsonObject.put("userIsWho",userIsWho);
        }
        if (clients!=null){
            jsonObject.put("clients",clients);
        }
        return jsonObject.toString();
    }


    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
