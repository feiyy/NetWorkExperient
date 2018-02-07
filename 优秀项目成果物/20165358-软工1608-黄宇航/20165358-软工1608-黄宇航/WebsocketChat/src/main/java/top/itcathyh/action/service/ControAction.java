package top.itcathyh.action.service;

import top.itcathyh.service.ClientSocket;
import top.itcathyh.service.ServiceSocket;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public final class ControAction {
    private static final LinkedList<ClientSocket> clientqueue = new LinkedList<>();
    //private static final ArrayDeque<ClientSocket> clientqueue = new ArrayDeque<>();
    private static final ArrayDeque<ServiceSocket> freequeue = new ArrayDeque<>();
    private static int maxcount = 2;
    private volatile static boolean isrunning = false;

    private static final class ControThread implements Runnable {

        @Override
        public void run() {
            ServiceSocket service = null;
            boolean pre = false;

            while (true) {
                ClientSocket client = null;

                /* 判断有无等待的客户和空闲客服 */
                try {
                    if (clientqueue.isEmpty()) {
                        Thread.sleep(1000);
                        continue;
                    } else if (freequeue.isEmpty()) {
                        //notice("客服正忙，请稍候");
                        Thread.sleep(1000);
                        continue;
                    } else if (!clientqueue.isEmpty()) {
                        client = clientqueue.poll();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }

                if (client != null && client.isOpen()) {
                    while (!freequeue.isEmpty()) {
                        service = freequeue.getFirst();

                        if (service != null && service.isOpen() && service.getCount() < maxcount) {
                            service.addClient(client);
                            client.setService(service);

                            if (service.getCount() == maxcount) {
                                freequeue.removeFirst();
                            }

                            ServiceAction.notice("list--:" + ServiceAction.getServiceList());
                            break;
                        } else {
                            freequeue.removeFirst();
                        }
                    }

                    if (client.getService() == null) {
                        clientqueue.add(client);
                    }
                }

                if (!ServiceAction.haveService()) {
                    noticeAndClear("notice：客服暂未上班，请稍后重试");
                }
            }
        }
    }

    public static void addClient(ClientSocket client) {
        clientqueue.add(client);
    }

    public static void addFreeService(ServiceSocket service) {
        if (service != null && !clientqueue.contains(service)) {
            freequeue.add(service);
        }
    }

    public static void beginAlloting() {
        /* 启动调度线程 */
        if (!isrunning) {
            synchronized (ControAction.class) {
                if (!isrunning) {
                    isrunning = true;
                    new Thread(new ControThread()).start();
                }
            }
        }
    }

    public static boolean isRunning() {
        return isrunning;
    }

    public static boolean isBusy(){
        return freequeue.size() == 0;
    }

    private static void notice(String msg) {
        for (ClientSocket client : clientqueue) {
            if (client != null && client.isOpen()) {
                try {
                    client.sendMsg(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void noticeAndClear(String msg) {
        ClientSocket client;

        while (!clientqueue.isEmpty()) {
            client = clientqueue.poll();

            if (client != null && client.isOpen()) {
                try {
                    client.sendMsg(msg);
                    client.closeSession();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getMaxcount() {
        return maxcount;
    }
}
