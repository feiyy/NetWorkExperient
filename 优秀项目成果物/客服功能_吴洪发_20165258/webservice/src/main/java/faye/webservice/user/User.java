package faye.webservice.user;


import org.json.JSONArray;


import java.util.ArrayList;

public class User {
    private String id;
    private String userName;
    private String password;
    private ArrayList<User> users;
    private boolean isManager;

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    private boolean online;

    public boolean isOnline() {
        return online;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public User(){
        users = new ArrayList<>();
    }
    public void addFriend(User user){
        users.add(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return userName != null ? userName.equals(user.userName) : user.userName == null;
    }

    @Override
    public int hashCode() {
        return userName != null ? userName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
