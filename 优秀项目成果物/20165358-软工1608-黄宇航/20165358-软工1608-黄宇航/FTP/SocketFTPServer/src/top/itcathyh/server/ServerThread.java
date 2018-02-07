package top.itcathyh.server;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.PortableInterceptor.INACTIVE;
import top.itcathyh.command.*;
import top.itcathyh.command.commands.*;
import top.itcathyh.command.commands.UserCommand;
import top.itcathyh.dao.UserDao;
import top.itcathyh.dao.impl.UserDaoImpl;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.entity.User;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public final class ServerThread implements Runnable {
    private ObjectOutputStream oos = null;
    private ObjectInputStream ios = null;
    private Logger log = Logger.getLogger("ServerThread");
    private boolean islogin = false;
    private boolean used = false;
    private String username = null;
    private Socket client = null;
    private String dir = "D:" + File.separator + "Code"+ File.separator + "ftptest" + File.separator;
    private int port;
    private long rest = Long.MAX_VALUE;
    private long usedrest = Long.MAX_VALUE;
    private int pos = 0;

    public ServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            ios = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            oos = new ObjectOutputStream(client.getOutputStream());
            String str = "";
            CommandInfo commandinfo = new CommandInfo();
            Command command = null;

            oos.writeObject("mask");

            while (!client.isClosed()) {
                while (used){
                    Thread.sleep(10);
                }

                str = (String) ios.readObject();

                while (!checkCommand(str)) {
                    str = (String) ios.readObject();
                    //  str = ios.readUTF();
                }

                if (str.indexOf(' ') != -1) {
                    command = CommandFactory.getCommand(str.split(" ")[0]);
                    str = str.split(" ")[1];
                    commandinfo.setStr(str);
                } else {
                    command = CommandFactory.getCommand(str);
                }

                if (command != null) {
                    if (!(command instanceof UserCommand || command instanceof PassCommand ||
                            command instanceof QuitCommand) && !islogin) {
                        oos.writeObject("532 please login before the action");
                        oos.flush();
                    } else if (command instanceof QuitCommand) {
                        break;
                    } else if (command instanceof PassCommand) {
                        if (username != null) {
                            commandinfo.setObj1(username);
                            if (command.excuteCommand(commandinfo, oos, this)){
                                islogin = true;
                            }
                        }
                    } else if (command instanceof UserCommand){
                        if (!islogin && command.excuteCommand(commandinfo, oos, this)){
                            username = commandinfo.getStr();
                        }
                    } else {
                        command.excuteCommand(commandinfo, oos, this);
                    }
                } else {
                    oos.writeObject("503 wrong command");
                    oos.flush();
                }

                str = "";
            }

            if (command != null) {
                command.excuteCommand(commandinfo, oos, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Socket getClient() {
        return client;
    }

    public int getPort() {
        return port;
    }


    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getRest() {
        return rest;
    }

    public void setRest(long rest) {
        this.rest = rest;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public ObjectInputStream getIos() {
        return ios;
    }

    public void setIos(ObjectInputStream ios) {
        this.ios = ios;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public long getUsedrest() {
        return usedrest;
    }

    public void setUsedrest(long usedrest) {
        this.usedrest = usedrest;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    private static boolean checkCommand(String str) {
        if (str.length() < 4) {
            return false;
        }

        if (str.startsWith("USER")) {
            return true;
        } else if (str.startsWith("PASS")) {
            return true;
        } else if (str.startsWith("QUIT")) {
            return true;
        } else if (str.startsWith("LIST")) {
            return true;
        } else if (str.startsWith("STOR")) {
            return true;
        } else if (str.startsWith("PASV")) {
            return true;
        } else if (str.startsWith("RETR")) {
            return true;
        } else if (str.startsWith("SIZE")) {
            return true;
        } else if (str.startsWith("CWD")) {
            return true;
        } else if (str.startsWith("REST")) {
            return true;
        }

        return false;
    }
}