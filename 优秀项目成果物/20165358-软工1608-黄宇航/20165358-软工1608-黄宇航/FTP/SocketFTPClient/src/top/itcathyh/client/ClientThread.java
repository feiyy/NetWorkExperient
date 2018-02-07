package top.itcathyh.client;

import top.itcathyh.command.CommandFactory;
import top.itcathyh.command.commands.*;
import top.itcathyh.entity.CommandInfo;
import top.itcathyh.entity.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread implements Runnable {
    private String eigenvalue = null;
    private Socket client = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ios = null;
    private User user = null;
    private int port = -1;
    private static final String dir = "D:/test/";
    private long rest = Long.MAX_VALUE;
    private long usedrest = Long.MAX_VALUE;
    private int pos = 0;

    ClientThread(Socket client) {
        this.client = client;
    }

    public void run() {
        try {
            Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
            oos = new ObjectOutputStream(client.getOutputStream());
            ios = new ObjectInputStream(new BufferedInputStream(client.getInputStream()));
            eigenvalue = (String) ios.readObject();
            String data[];
            Command command;
            CommandInfo commandinfo = new CommandInfo();

            while (!client.isClosed()) {
                System.out.print("Input the command：");
                String str = in.nextLine();
                data = str.split(" ");

                if (!(str.startsWith("USER") || str.startsWith("PASS") ||
                        str.startsWith("QUIT")) && user == null) {
                    System.out.println("please login before the action");
                    continue;
                }

                oos.writeObject(str);
                oos.flush();

                str = data[0].toUpperCase();
                command = CommandFactory.getCommand(str);

                if (command == null || data.length > 2) {
                    continue;
                }

                if (str.equals("QUIT")) {
                    if (command.excuteCommand(null, oos, ios, this)) {
                        break;
                    }
                } else if (!str.equals("PASS")) {
                    if (str.equals("USER") && data.length == 2) {
                        commandinfo.setStr(data[1]);

                        if (command.excuteCommand(commandinfo, oos, ios, this)) {
                            System.out.print("Input the command：");
                            str = in.nextLine();

                            oos.writeObject(str);
                            oos.flush();

                            command = CommandFactory.getCommand("PASS");

                            if (command.excuteCommand(commandinfo, oos, ios, this)) {
                                this.user = (User) commandinfo.getObj1();
                            }
                        }
                    } else {
                        if (data.length == 2) {
                            commandinfo.setStr(data[1]);
                        }

                        command.excuteCommand(commandinfo, oos, ios, this);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            try {
                client.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public boolean isConnect() {
        return client != null;
    }

    public int getPort() {
        return port;
    }

    public Socket getClient() {
        return client;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public String getDir() {
        return dir;
    }

    public ObjectInputStream getIos() {
        return ios;
    }

    public void setRest(long rest) {
        this.rest = rest;
    }

    public long getUsedrest() {
        return usedrest;
    }

    public void setUsedrest(long usedrest) {
        this.usedrest = usedrest;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public long getRest() {
        return rest;
    }
}
