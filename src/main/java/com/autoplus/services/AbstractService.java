package com.autoplus.services;

import com.autoplus.dao.Dao;
import com.autoplus.entity.Car;
import com.autoplus.entity.Socket;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static com.autoplus.Constants.CACHE_SOCKET;
import static com.autoplus.Constants.SOCKETS;

public abstract class AbstractService {
    protected String SITE;
    protected String RESOURCES = "prop.properties";
    protected String ALLCARS;
    protected String CARMODELS;
    protected String CARTYPE;
    protected String MODIFICATION;
    protected String MOD_IMAGE;
    protected Dao dao;
    protected Dao moddao;
    protected Car temp;
    protected List<String> xpath;
    protected List<Function<Element, Boolean>> method;
    protected Car last;
    protected List<String> existCars;
    protected List<String> existsModels;
    protected List<String> existsTypes;
    protected List<String> existModification;
    protected List<List<String>> exists;
    protected Document currentDoc;
    String IPs = "table > tbody > tr";



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
        try {
            while ((line = in.readLine()) != null) {
                tmp.append(line);
            }
        }catch(IOException e){
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

    public void getAll(int idx) throws IOException, InterruptedException {
        Document doc = Jsoup.parse(String.valueOf(getHTML(new URL(SITE + temp.getReference()))));
        currentDoc = doc;
        Elements elements = doc.select(xpath.get(idx));
        if (elements.isEmpty()) {
            dao.save(new Car(temp));
            if (idx < xpath.size() - 1) {
                getAll(idx + 1);
            }
        } else {
            for (int i = 0; i < elements.size(); i++) {
                Element e = elements.get(i);
                boolean b = method.get(idx).apply(e);
                if (idx < xpath.size() - 1 && b) getAll(idx + 1);
            }
        }
    }

    public void getModImage(){
        if(!new File("C:/app/carmodels/" + temp.getBrand().replace("/", "") + "/" + temp.getModel().replace("/", "")+"/"+ temp.getModel().replace("/", "") + ".png").exists()) {
            Elements elements = currentDoc.select(MOD_IMAGE);
            String imageMod = elements.attr("src");
            System.out.println("================>inside   " + imageMod);
            if (!"/static/images/nocars.png".equals(imageMod) && !imageMod.isEmpty()) {
                try {
                    saveImage(imageMod.contains("http") ? imageMod : SITE + imageMod, "C:/app/carmodels/" + temp.getBrand().replace("/", "") + "/" + temp.getModel().replace("/", "") + "/" + temp.getModel().replace("/", "") + ".png");
                } catch (FileNotFoundException ex) {
                    try {
                        saveImage(imageMod.contains("http") ? imageMod : SITE + imageMod, "C:/app/carmodels/" + temp.getModel().replace("/", "") + ".png");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void saveImage(String source, String dist) throws IOException, InterruptedException {
        if (!source.toUpperCase().contains("HTTP")) {
            source = SITE + source;
        }
        URL url = new URL(source);
        Image img = ImageIO.read(getStream(url));
        BufferedImage bi = (BufferedImage) img;
        if(bi!=null) {
            File f = new File(dist);
            ImageIO.write(bi, "png", f);
        }

    }

}
