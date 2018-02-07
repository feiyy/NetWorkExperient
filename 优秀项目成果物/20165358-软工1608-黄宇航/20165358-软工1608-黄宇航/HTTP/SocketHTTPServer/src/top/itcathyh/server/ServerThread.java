package top.itcathyh.server;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.StringTokenizer;

public final class ServerThread implements Runnable {
    private BufferedReader bd = null;
    private PrintStream ps = null;
    private Socket client = null;
    private static String webroot = null;

    public ServerThread(Socket client, String path) {
        if (webroot == null) {
            webroot = path;
        }

        this.client = client;
    }

    @Override
    public void run() {
        try {
            OutputStream os = client.getOutputStream();
            ps = new PrintStream(os);
            bd = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String line, url, path;
            line = bd.readLine();
            File file;
            boolean isweb = false;

            try {
                String method = new StringTokenizer(line).nextElement().toString();

                System.out.println("Request method:" + method);

                url = line.split(" ")[1];

                if (url.contains("www.")) {
                    path = "http:/" + url;
                    isweb = true;
                } else {
                    path = webroot + File.separator + url;
                }
            } catch (Exception e) {
                ps.println("HTTP/1.1 400 Bad Request");
                ps.flush();
                return;
            }

            byte buffer[] = new byte[1];

            if (!isweb) {
                file = new File(path);
                FileInputStream fis = new FileInputStream(file);

                if (!file.exists() || file.isDirectory()) {
                    write404(ps);
                    return;
                }

                buffer = new byte[fis.available()];

                fis.read(buffer);
                fis.close();
                writeType(url, ps, buffer.length);
                ps.write(buffer);
                ps.flush();
            } else {
                buffer = new byte[4096];
                InputStream is = getInputStream(path);

                if (is == null) {
                    write404(ps);
                    return;
                } else {
                    writeType(url, ps, buffer.length);
                    int len;

                    while ((len = is.read(buffer)) != -1) {
                        ps.write(buffer, 0, len);
                    }
                }

                ps.flush();
                is.close();
            }

            /* 输出资源 */
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeType(String url, PrintStream ps, int length) throws IOException {
        /*  发送响应 */
        ps.println("HTTP/1.1 200 OK");

        if (url.endsWith(".html") || url.endsWith(".htm") || url.endsWith(".css") || url.endsWith(".js")) {
            ps.println("Content-Type: text/html;charset:UTF-8");
        } else if (url.endsWith(".jpg") || url.endsWith(".jpeg")) {
            ps.println("Content-Type: image/jpeg;");
        } else if (url.endsWith(".gif")) {
            ps.println("Content-Type: image/gif;");
        } else {
            ps.println("Content-Type: application/octet-stream;");
        }

        ps.println("Content-Length:" + length);
        ps.println("");
        ps.flush();
    }

    private static void write404(PrintStream ps) throws IOException {
        /*  输出错误界面 */
        File file = new File(webroot + "error.html");

        ps.println("HTTP/1.1 404 Not Found");
        ps.println("Content-Type:text/html;charset:UTF-8");
        ps.println("Content-Length:" + file.length());
        ps.println("");
        writeHtml(file, ps);
        ps.close();
    }

    private static void writeHtml(File file, PrintStream ps) throws IOException {
        /*  输出html */
        FileInputStream fis = new FileInputStream(file);
        byte buffer[] = new byte[fis.available()];

        fis.read(buffer);
        ps.write(buffer);
        ps.flush();
        fis.close();
    }

    private static InputStream getInputStream(String urlstr) {
        InputStream is = null;
        File file = null;
        byte[] buffer = null;

        try {
            URL url = new URL(urlstr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(3 * 1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


        return is;
    }
}