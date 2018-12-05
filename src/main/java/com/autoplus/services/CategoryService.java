package com.autoplus.services;

import com.autoplus.dao.CategoryDao;
import com.autoplus.dao.Dao;
import com.autoplus.entity.Category;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Element;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CategoryService extends AbstractService<Category> {
    private Dao dao;
    private String CATEGORIES;
    private String SUBCATEGORIES;
    private List<Category> parents;

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
        parents = new ArrayList<>();
        List<Category> categories = dao.getAll();
        existCars = new ArrayList<>();
        categories.forEach(c -> existCars.add(c.getReference()));
        xpath = new ArrayList<>();
        method = new ArrayList<>();
    }

    public boolean getCategories(Element e) {
        if (!e.attr("href").isEmpty() && !existCars.contains(e.attr("href"))) {
            System.out.println(e.select("> div > a").text());
            System.out.println(SITE + e.attr("href"));
            Category c = new Category(e.text(), e.attr("href"), null);
            c = (Category) dao.save(c);
            parents.add(0, null);
            parents.add(1,c);
            return true;
        }
        return false;
    }

    public boolean getSubCategories(Element e) {
        if (!e.attr("href").isEmpty() && !existCars.contains(e.attr("href"))) {
            String ref = e.attr("href");
            String title = e.select(".info > strong").text();
            String img = e.select(".img > img").attr("src");
            System.out.println(title);
            System.out.println(ref);
            new File("C:/app/categories/" +parents.get(1).getTitle()+"/"+ title.replace("/", "")).mkdir();
            try {
                saveImage(img, "C:/app/categories/" +parents.get(1).getTitle()+"/"+ title.replace("/", "") + ".png");
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Category c = new Category(title, ref, parents.get(1));
            c = (Category) dao.save(c);
            parents.add(2,c);
            return true;
        }
        return false;
    }
    public boolean getFinalCategories(Element e) {
        if (!e.attr("href").isEmpty() && !existCars.contains(e.attr("href"))) {
            String title = e.select(".info > strong").text();
            String ref = e.attr("href");
            String img = e.select(".img > img").attr("src");
            System.out.println(title);
            System.out.println(ref);
            new File("C:/app/categories/" +parents.get(1).getTitle()+"/"+parents.get(2).getTitle()+"/"+ title.replace("/", "")).mkdir();
            try {
                saveImage(img, "C:/app/categories/" +parents.get(1).getTitle()+"/"+parents.get(2).getTitle()+"/"+ title.replace("/", "") + ".png");
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Category c = new Category(title, ref, parents.get(2));
            c = (Category) dao.save(c);
            return true;
        }
        return false;
    }

    public void getAllCategories() throws InterruptedException, IOException {
        xpath.add(CATEGORIES);
        xpath.add(SUBCATEGORIES);
        xpath.add(SUBCATEGORIES);
        method.add((e) -> getCategories(e));
        method.add((e) -> getSubCategories(e));
        method.add((e) -> getFinalCategories(e));
        getAll(0);
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
