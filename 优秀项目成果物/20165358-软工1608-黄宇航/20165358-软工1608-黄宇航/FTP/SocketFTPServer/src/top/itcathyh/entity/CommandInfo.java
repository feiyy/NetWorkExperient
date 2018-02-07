package top.itcathyh.entity;

public class CommandInfo {
    private String str;
    private Object obj1;
    private Object obj2;

    public CommandInfo() {
        claer();
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Object getObj2() {
        return obj2;
    }

    public void setObj2(Object obj2) {
        this.obj2 = obj2;
    }

    public Object getObj1() {
        return obj1;
    }

    public void setObj1(Object obj1) {
        this.obj1 = obj1;
    }

    public void claer() {
        str = null;
        obj2 = null;
        obj1 = null;
    }
}