package top.itcathyh.client;

import top.itcathyh.action.ConnectAction;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileThread implements Runnable {
    private Socket client = null;
    private ServerSocket serversocket;
    private String dir;
    private String rootdir;
    private String filename;
    private String cmd;
    private ObjectOutputStream oos;
    private ObjectInputStream ios;
    private long rest;
    private int pos;
    private int tmppos = 0;
    private ClientThread st;

    public FileThread(Socket client, ServerSocket serversocket, String dir,
                      String filename, String cmd, ClientThread st) {
        this.client = client;
        this.serversocket = serversocket;
        this.dir = dir;
        this.oos = st.getOos();
        this.filename = filename;
        this.cmd = cmd.toUpperCase();
        this.rest = st.getRest();
        this.ios = st.getIos();
        this.pos = st.getPos();
        this.st = st;
        this.rootdir = new File(dir).getPath();
    }

    @Override
    public void run() {
        try {
            if (cmd.equals("STOR")) {
                System.out.println("begin transfer" + client.getLocalPort());
                File file = new File(dir + filename);
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());

                if (!file.isDirectory()) {
                    dos.writeUTF("nor");
                    dos.flush();
                    oos.writeLong(file.length());
                    oos.flush();

                    RandomAccessFile raf = new RandomAccessFile(file, "rw");

                    oos.writeLong(pos);
                    raf.seek(pos);

                    if (rest == st.getUsedrest()) {
                        rest = Long.MAX_VALUE;
                        st.setPos(0);
                    }

                    byte buffer[] = new byte[4096];
                    int flow;
                    long size = file.length();

                    while ((flow = raf.read(buffer)) != -1) {
                        dos.write(buffer, 0, flow);
                        System.out.println(filename + "download:" + (dos.size() * 1.0 / size * 100) + "%");

                        if (dos.size() >= rest) {
                            break;
                        }
                    }

                    dos.flush();
                    raf.close();
                    st.setPos(dos.size());
                    st.setUsedrest(rest);
                } else {
                    dos.writeUTF("dir");
                    dos.flush();

                    if (rest == st.getUsedrest()) {
                        rest = Long.MAX_VALUE;
                        st.setPos(0);
                    }

                    createSendThread(file, dir + filename, 0, 0, serversocket);
                    st.setPos(tmppos);
                    st.setUsedrest(rest);
                    oos.writeObject("done");
                    oos.flush();
                }

                oos.writeObject("220 complete");
                oos.flush();
                System.out.println("end transfer" + client.getLocalPort());
            } else if (cmd.equals("RETR")) {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(client.getInputStream()));

                if (dis.readUTF().equals("dir")) {
                    createReceiveTransferThread();
                    return;
                }

                RandomAccessFile raf = new RandomAccessFile(dir + filename, "rw");
                long size = ios.readLong();
                long pos = ios.readLong();
                raf.seek(pos);
                byte buffer[] = new byte[4096];
                int flow;

                while ((flow = dis.read(buffer)) != -1) {
                    raf.write(buffer, 0, flow);
                    System.out.println(filename + " download:" + (raf.length() * 1.0 / size * 100) + "%");
                }

                raf.close();
                dis.close();
                System.out.println(ios.readObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (client != null) {
                    client.close();
                }

                if (serversocket != null) {
                    serversocket.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    private final void createSendThread(File file, String dir1, int length, int cot, ServerSocket serversocket) throws IOException, ClassNotFoundException {
        try {
            if (!file.exists()) {
                return;
            }

            if (file.isDirectory()) {
                oos.writeObject("makedir--" + dir1.substring(rootdir.length()));
                oos.flush();
                File list[] = file.listFiles();

                if (list != null) {
                    for (File tmp : list) {

                        if (tmp.isDirectory()) {
                            createSendThread(file, dir1 + File.separator + tmp.getName(), length, cot, serversocket);
                        } else {
                            length += tmp.length();
                            tmppos++;

                            if (length >= rest) {
                                return;
                            } else if (++cot < pos) {
                                continue;
                            }

                            oos.writeObject("file--" + tmp.getName());
                            oos.flush();
                            Socket client = serversocket.accept();
                            new Thread(new SendThread(client, dir1, tmp.getName())).start();
                        }
                    }
                }
            } else {
                oos.writeObject("file--" + file.getName());
                oos.flush();
                Socket client = serversocket.accept();
                new Thread(new SendThread(client, dir1, file.getName())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final void createReceiveTransferThread() throws IOException, ClassNotFoundException {
        String dir1 = dir, cmd;
        int port = client.getPort();

        while (true) {
            cmd = (String) ios.readObject();

            if (cmd.startsWith("file")) {
                Socket datasocket = ConnectAction.getClient(port);

                new Thread(new ReceiveThread(datasocket, dir1, cmd.split("--")[1])).start();
            } else if (cmd.startsWith("makedir")) {
                dir1 = dir + cmd.split("--")[1];
                new File(dir1).mkdirs();
                dir1 += File.separator;
            } else {
                break;
            }
        }
    }
}
