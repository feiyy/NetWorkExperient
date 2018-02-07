package top.itcathyh.server;

import java.io.*;
import java.net.Socket;

public class SendThread implements Runnable {
    private String dir;
    private String filename;
    private Socket client = null;

    public SendThread(Socket client, String dir, String filename) {
        this.client = client;
        this.dir = dir;
        this.filename = filename;

        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        DataOutputStream dos = null;

        try {
            File file = new File(dir + File.separator + filename);
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
            dos = new DataOutputStream(client.getOutputStream());
            int flow;
            dos.writeLong(file.length());
            byte buffer[] = new byte[1024];

            while ((flow = dis.read(buffer)) != -1) {
                dos.write(buffer, 0, flow);
            }

            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }

                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
