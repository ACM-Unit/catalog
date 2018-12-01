package com.autoplus.services;

import com.autoplus.App;
import com.autoplus.entity.Socket;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.util.*;

public interface IService {
    /*    String ip = "50.242.47.41";
        int port = 31341;*/
    String PROXIES1 = "https://hidemyna.me/en/proxy-list/?ports=8080&type=h#list";
    String PROXIES = "https://free-proxy-list.net/anonymous-proxy.html";
    String IPs = "table > tbody > tr";
    String IPs1 = "#content-section > section:eq(0) > div > table > tbody > tr";


    default com.autoplus.entity.Socket getSocket() throws IOException {
        if (App.CACHE_SOCKET == null) {
            App.CACHE_SOCKET = getRandomProxy();
        }
        return App.CACHE_SOCKET;

    }

    default com.autoplus.entity.Socket getRandomProxy() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        Document doc = Jsoup.parse(String.valueOf(getHTML("test.html")));
        Elements ips = doc.select(IPs);
        System.out.println(ips.size());
        for (Element sc : ips) {
            map.put(sc.select("td:eq(0)").text(), Integer.valueOf(sc.select("td:eq(1)").text()));
            //System.out.println(sc.select("td:eq(0)").text()+":"+sc.select("td:eq(1)").text());
        }
        Random random = new Random();
        List<String> keys = new ArrayList<String>(map.keySet());
        String ip = keys.get(random.nextInt(keys.size()));
        Integer port = map.get(ip);
        App.CACHE_SOCKET = new com.autoplus.entity.Socket(ip, port);
        return new Socket(ip, port);
    }

    default StringBuffer getHTML(URL url) throws IOException, InterruptedException {
        HttpURLConnection uc = getStream(url);
        String line = null;
        StringBuffer tmp = new StringBuffer();
        System.out.println("============>" + url);
        BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        while ((line = in.readLine()) != null) {
            tmp.append(line);
        }
        return tmp;
    }

    default HttpURLConnection getStream(URL url) throws IOException, InterruptedException {
        com.autoplus.entity.Socket s = getSocket();
        int count = 0;
        boolean status = true;
        HttpURLConnection uc = null;
        while (count < 3) {
            Thread.sleep(2000);
            try {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s.getIp(), s.getPort())); // 139.255.40.130
                uc = (HttpURLConnection) url.openConnection(proxy);
                uc.setRequestProperty("Cookie", "catalog_page_size=50");
                uc.connect();
                System.out.println("connect " + s.getIp() + ":" + s.getPort());
                return uc;
            } catch (IOException e) {
                System.out.println("exception " + s.getIp() + ":" + s.getPort());
                e.printStackTrace();
                count++;
            }
            if(count == 3){
                App.CACHE_SOCKET = getRandomProxy();
            }
        }
        return getStream(url);
    }

    default StringBuffer getHTML(String html) throws IOException {
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
