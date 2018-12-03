package com.autoplus.services;

import com.autoplus.App;
import com.autoplus.entity.Socket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.util.*;

import static com.autoplus.Constants.CACHE_SOCKET;
import static com.autoplus.Constants.SOCKETS;

public abstract class AbstractService {
    /*    String ip = "50.242.47.41";
        int port = 31341;*/
    String PROXIES1 = "https://hidemyna.me/en/proxy-list/?ports=8080&type=h#list";
    String PROXIES = "https://free-proxy-list.net/anonymous-proxy.html";
    String IPs = "table > tbody > tr";
    String IPs1 = "#content-section > section:eq(0) > div > table > tbody > tr";


    public Socket getSocket() throws IOException {
        if (CACHE_SOCKET == null) {
            CACHE_SOCKET = getRandomProxy();
        }
        return CACHE_SOCKET;

    }

    public void setProxies()throws IOException {
        List<Socket> set = new ArrayList<>();
        Document doc = Jsoup.parse(String.valueOf(getHTML("test.html")));
        Elements ips = doc.select(IPs);
        System.out.println(ips.size());
        for (Element sc : ips) {
            set.add(new Socket(sc.select("td:eq(0)").text(), Integer.valueOf(sc.select("td:eq(1)").text())));
            //System.out.println(sc.select("td:eq(0)").text()+":"+sc.select("td:eq(1)").text());
        }
        SOCKETS = set;
    }

    public Socket getRandomProxy() throws IOException {
        Random random = new Random();
        CACHE_SOCKET = SOCKETS.get(random.nextInt(SOCKETS.size()));
        return CACHE_SOCKET;
    }

    public StringBuffer getHTML(URL url) throws IOException, InterruptedException {
        String line = null;
        StringBuffer tmp = new StringBuffer();
        System.out.println("============>" + url);
        BufferedReader in = new BufferedReader(new InputStreamReader(getStream(url)));
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }
        return tmp;
    }

    public InputStream getStream(URL url) throws IOException, InterruptedException {
        Socket s = getSocket();
        int count = 0;
        HttpURLConnection uc = null;
        while (count < 3) {
            Thread.sleep(2000);
            try {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s.getIp(), s.getPort())); // 139.255.40.130
                uc = (HttpURLConnection) url.openConnection(proxy);
                uc.setRequestProperty("Cookie", "catalog_page_size=50");
                uc.connect();
                System.out.println("connect " + s.getIp() + ":" + s.getPort());
                return uc.getInputStream();
            } catch (IOException e) {
                System.out.println("exception " + s.getIp() + ":" + s.getPort());
                e.printStackTrace();
                count++;
            }
            if (count == 3) {
                SOCKETS.remove(CACHE_SOCKET);
                CACHE_SOCKET = getRandomProxy();
            }
        }
        return getStream(url);
    }

    public StringBuffer getHTML(String html) throws IOException {
        String line = null;
        StringBuffer tmp = new StringBuffer();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(html).getFile());
        FileReader fin = new FileReader(file);
        BufferedReader in = new BufferedReader(fin);
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }
        return tmp;
    }

}
