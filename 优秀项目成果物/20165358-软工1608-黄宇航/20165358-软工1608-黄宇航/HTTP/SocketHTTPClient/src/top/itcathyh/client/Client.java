package top.itcathyh.client;

import top.itcathyh.action.ConnectAction;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client implements Runnable {
    private static boolean work = true;
    private static final String SAVA_LOCATION = "http\\save\\";
    private Socket client = null;
    private PrintStream ps = null;
    private BufferedReader bd = null;
    private String url = null;

    public static void setWork(boolean work) {
        Client.work = work;
    }

    public Client(Socket client) {
        this.client = client;
        File file = new File(SAVA_LOCATION);

        if (!file.exists()) {
            file.mkdirs();
        } else if (file.isFile()) {
            file.delete();
            file.mkdirs();
        }
    }

    public Client(Socket client, String url) {
        this.client = client;
        this.url = url;
    }

    @Override
    public void run() {
        try {
            Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));

            if (url == null){
                System.out.println("Input the url:");

                url = in.nextLine();
            }

            InputStream is = client.getInputStream();
            ps = new PrintStream(client.getOutputStream());
            bd = new BufferedReader(new InputStreamReader(is));

            sendRequest(url, ps);

            if (!readResponse(bd)) {
               return;
            }

            writeFile(url, is);

            if (url.endsWith(".html") || url.endsWith(".htm")){
                /* 下载引用资源 */
                ArrayDeque<String> srcpath = readSrc(new File(SAVA_LOCATION + url));

                    if (srcpath != null){
                    String path;
                    Pattern p = Pattern.compile(".*/");
                    Matcher m;
                    File file;

                    while (!srcpath.isEmpty()){
                        path = srcpath.pop();
                        m = p.matcher(path);

                        if (m.find()){
                            file = new File(SAVA_LOCATION + m.group());

                            if (!file.exists()){
                                file.mkdirs();
                            } else if (file.isFile()){
                                file.delete();
                                file.mkdirs();
                            }

                            new Thread(new Client(ConnectAction.getClient(), path)).start();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendRequest(String url, PrintStream ps) {
        /* 发送请求 */
        ps.println("GET /" + url + " HTTP/1.1");
        ps.println("Host: localhost:8888");
        ps.println("connection:keep-alive");
        ps.println();
        ps.flush();
    }

    private static boolean readResponse(BufferedReader bd) throws IOException {
        /* 读取响应 */
        String firstline = bd.readLine();
        String line = bd.readLine();
        line = bd.readLine();
        line = bd.readLine();

        if (!firstline.endsWith("200 OK")) {
            /* 输出失败响应 */
            while ((line = bd.readLine()) != null) {
                System.out.println(line);
            }

            return false;
        }

        return true;
    }

    private static void writeFile(String url, InputStream is) throws IOException {
        /* 接收数据 */
        OutputStream os = new FileOutputStream(SAVA_LOCATION + url);
        int len;
        byte[] buffer = new byte[4096];

        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }

        os.flush();
        os.close();
    }

    private static ArrayDeque<String> readSrc(File file) throws IOException{
        /*  读取引用资源 */
        ArrayDeque<String> srcpath = new ArrayDeque<>();
        BufferedReader tbr = new BufferedReader(new FileReader(file));
        String path, line;

        while ((line = tbr.readLine()) != null){
            if (line.contains("src")){
                path = line.split("src=\"")[1].split("\"")[0];

                srcpath.add(path);
            } else if (line.contains("<link href=\"")){
                path = line.split("href=\"")[1].split("\"")[0];

                srcpath.add(path);
            }
        }

        tbr.close();

        return srcpath.size() == 0 ? null : srcpath;
    }

    public static void main(String[] args) throws Exception {
        new Thread(new Client(ConnectAction.getClient())).start();
    }
}
