package top.itcathyh.entity;

import java.io.Serializable;

public class SocketInfo implements Serializable{
    private String Type;
    private String text;
    private String result;
    private Object obj;

    public SocketInfo(){
        text = null;
        result = null;
        obj = null;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
