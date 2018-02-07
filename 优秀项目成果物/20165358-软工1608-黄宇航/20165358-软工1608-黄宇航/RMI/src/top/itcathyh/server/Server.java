package top.itcathyh.server;

import top.itcathyh.service.Service;
import top.itcathyh.service.ServiceImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        try{
            Service service = new ServiceImpl();

            LocateRegistry.createRegistry(PORT);
            Naming.bind("rmi://localhost:8888/service", service);
            System.out.println("Service Started");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
