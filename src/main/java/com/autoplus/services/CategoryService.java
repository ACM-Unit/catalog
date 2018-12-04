package com.autoplus.services;

import com.autoplus.dao.CategoryDao;
import com.autoplus.dao.Dao;
import com.autoplus.entity.Category;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.sql.DataSource;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CategoryService extends AbstractService {
    private Dao dao;
    private String CATEGORIES;
    private String SUBCATEGORIES;

    public CategoryService(DataSource ds) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties prop = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(RESOURCES)) {
            prop.load(resourceStream);
        }
        SITE = prop.getProperty("site");
        CATEGORIES = prop.getProperty("categry-xpath");
        SUBCATEGORIES = prop.getProperty("subcategory-xpath");
        dao = new CategoryDao(ds);
    }

    public void getCategories(Element e) throws IOException, InterruptedException {
        Document document = Jsoup.parse(String.valueOf(getHTML(new URL(url))));
            if (!e.select("> div > a").attr("href").isEmpty()) {
                System.out.println(e.select("> div > a").text());
                System.out.println(SITE + e.select("> div > a").attr("href"));
                Category c = new Category(e.select("> div > a").text(), SITE + e.select("> div > a").attr("href"), null);
                for (Element se : e.select(SUBCATEGORIES)) {
                    if (!se.attr("href").isEmpty()) {
                        System.out.println("-" + se.text());
                        System.out.println("-" + SITE + se.attr("href"));
                        Category sc = new Category(se.text(), SITE + se.attr("href"), null);
                    }
                }

            }

    }

    public List<Category> getSubCategories(String url) {
        List<Category> categories = new ArrayList<>();
        try {
            Thread.sleep(2000);
            Document document = Jsoup.parse(String.valueOf(getHTML(new URL(url))));
            for (Element e : document.select(SUBCATEGORIES)) {
                if (!e.attr("href").isEmpty()) {
                    System.out.println(e.select(".info > strong").text());
                    System.out.println(SITE + e.attr("href"));
                    Category c = new Category(e.select(".info > strong").text(), SITE + e.attr("href"), null);
                    categories.add(c);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void saveHTML(String ip, int port) throws IOException, InterruptedException {
        StringBuffer html = getHTML(new URL(SITE + "/datchiki-abs/dodge-cars/caliber-3329/"));
        File newHtmlFile = new File("C:/app/caliber-3329.html");
        FileUtils.writeStringToFile(newHtmlFile, String.valueOf(html));
        /*StringBuffer css =  getHTML(new URL(SITE+"/static/base.min.css?req=704ba606"));
        File newCssFile = new File("C://app/base.min.css");
        FileUtils.writeStringToFile(newCssFile, String.valueOf(css));*/
    }

}
