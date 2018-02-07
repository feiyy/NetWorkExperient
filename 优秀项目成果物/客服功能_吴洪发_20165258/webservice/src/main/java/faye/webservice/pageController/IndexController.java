package faye.webservice.pageController;

import faye.webservice.user.User;
import faye.webservice.user.UserManager;
import faye.webservice.websocket.MyWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    @Autowired
    private UserManager manager;
    @RequestMapping("/")
    public String login(){
        return "login.html";
    }
    @RequestMapping("/check")
    @ResponseBody
    public String check(String userName, String password, HttpSession session){
        System.out.println(userName+"登录了");
        User u = manager.find(userName,password);
        if (u==null ){
            return "error";
        }else if (MyWebSocket.getSessionManager().get(userName)!=null){
            return "hasLogin";
        } else{
            session.setAttribute("user",u);
            if (u.isManager())
                return "managerPage";
            else
                return "userPage";
        }
    }
    @RequestMapping("managerPage")
    public String managrPage(){
        return "manager.html";
    }
    @RequestMapping("userPage")
    public String userPage(){
        return "user.html";
    }
}
