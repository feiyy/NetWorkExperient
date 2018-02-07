package top.itcathyh.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class LoginInfo implements Serializable {
    private String username;
    private String password;
    private Timestamp logintime;

    public LoginInfo(String username, String password) {
        this.username = username;
        this.password = password;
        this.logintime = new Timestamp(System.currentTimeMillis());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getLogintime() {
        return logintime;
    }

    public void setLogintime(Timestamp logintime) {
        this.logintime = logintime;
    }
}
