package com.autoplus.services;

import com.autoplus.dao.Dao;
import com.autoplus.entity.Entity;
import com.autoplus.entity.Socket;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

import static com.autoplus.Constants.CACHE_SOCKET;
import static com.autoplus.Constants.SOCKETS;

public abstract class AbstractService<T extends Entity> {
    protected String SITE;
    protected String RESOURCES = "prop.properties";
    protected String ALLCARS;
    protected String CARMODELS;
    protected String CARTYPE;
    protected String MODIFICATION;
    protected String MOD_IMAGE;
    protected Dao dao;
    protected Dao moddao;
    protected T temp;
    protected List<String> xpath;
    protected List<Function<Element, String>> method;
    protected T last;
    protected List<String> existCars;
    protected List<String> existsModels;
    protected List<String> existsTypes;
    protected List<String> existModification;
    protected List<List<String>> exists;
    protected Document currentDoc;
    protected int[] lastIndexes = {0, 0, 0};
    String IPs = "table > tbody > tr";
    protected String ImagePath;


    public Socket getSocket() throws IOException {
        if (CACHE_SOCKET == null) {
            CACHE_SOCKET = getRandomProxy();
        }
        return CACHE_SOCKET;

    }

    public void setProxies() throws IOException {
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

    public void getScript() throws IOException, InterruptedException {
        Document doc = Jsoup.parse(String.valueOf(getHTML("abarth.html")));
        String json = doc.body().data().replace("window.PRELOADED_STATE = ", "");
        System.out.println(json);
        JSONArray array = JsonPath.read(json, "$.unicat.modificationList");


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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(getStream(url)))) {
            while ((line = in.readLine()) != null) {
                tmp.append(line);
            }
        } catch (IOException e) {
            CACHE_SOCKET = getRandomProxy();
            return getHTML(url);
        }
        return tmp;
    }

    public InputStream getStream(URL url) throws IOException, InterruptedException {
        Socket s = getSocket();
        int count = 0;
        HttpURLConnection uc = null;
        while (count < 3) {
           // Thread.sleep(2000);
            try {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s.getIp(), s.getPort())); // 139.255.40.130
                uc = (HttpURLConnection) url.openConnection(proxy);
                uc.setRequestProperty("Cookie", "catalog_page_size=50");
                uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.0.3705; .NET CLR 1.1.4322; .NET CLR 1.2.30703)");
                uc.setConnectTimeout(60 * 1000);
                uc.setReadTimeout(60 * 1000);
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

    public void getAll(int idx, String ref) throws IOException, InterruptedException {
        Document doc = Jsoup.parse(String.valueOf(getHTML(new URL(SITE + ref))));
        currentDoc = doc;
        Elements elements = doc.select(xpath.get(idx));
        if (elements.isEmpty()) {
            if (temp != null) {
                dao.save(temp);
            }
            if (idx < xpath.size() - 1) {
                getAll(idx + 1, temp.getReference());
            }
        } else {
            for (int i = lastIndexes[idx]; i < elements.size(); i++) {
                Element e = elements.get(i);
                String b = method.get(idx).apply(e);
                if (idx < xpath.size() - 1 && b != null) getAll(idx + 1, b);
            }
        }
    }


    public InputStream saveImage(String source, String dist) throws InterruptedException, MalformedURLException {
        if (!source.toUpperCase().contains("HTTP")) {
            source = SITE + source;
        }
        URL url = new URL(source);
        Image img = null;
        InputStream in2 = null;
        try (InputStream in = getStream(url)) {
            img = ImageIO.read(in);
            BufferedImage bi = (BufferedImage) img;
            if (bi != null) {
                File f = new File(dist);
                ImageIO.write(bi, "jpg", f);
            }
            in2 = in;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return in2;
    }

}
