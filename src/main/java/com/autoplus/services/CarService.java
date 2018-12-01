package com.autoplus.services;

import com.autoplus.dao.CarDao;
import com.autoplus.dao.Dao;
import com.autoplus.entity.Car;
import com.autoplus.entity.Category;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class CarService implements IService {
    private String SITE;
    private String RESOURCES = "prop.properties";
    private String CARS;
    private String ALLCARS;
    private String CARMODELS;
    private String CARTYPE;
    private Dao dao;
    private Car temp;
    private List<String> xpath;
    private List<Function<Element, Boolean>> method;
    private Car last;
    private List<String> existCars;
    private List<String> existsModels;
    private List<String> existsTypes;
    private List<List<String>> exists;


    public CarService(DataSource ds) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties prop = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(RESOURCES)) {
            prop.load(resourceStream);
        }
        SITE = prop.getProperty("site");
        CARS = prop.getProperty("car-xpath");
        ALLCARS = prop.getProperty("all-car-xpath");
        CARMODELS = prop.getProperty("carmodels-xpath");
        CARTYPE = prop.getProperty("carmodify-xpath");
        dao = new CarDao(ds);
        last = (Car) dao.getLastObject();
        List<Car> cars = dao.getAll();
        existCars = new ArrayList<>();
        cars.forEach(c -> existCars.add(c.getBrand()));
        existsModels = dao.getLastModels(last);
        existsTypes = dao.getLastTypes(last);
        exists = new ArrayList<>();
        exists.add(existCars);
        exists.add(existsModels);
        exists.add(existsTypes);
        temp = new Car(null, null, null, "/cars/");
        xpath = new ArrayList<>();
        method = new ArrayList<>();
    }


    public void saveImage(String source, String dist) throws IOException, InterruptedException {
        URL url = new URL(source);
        Image img = ImageIO.read(getStream(url).getInputStream());
        BufferedImage bi = (BufferedImage) img;
        File f = new File(dist);
        ImageIO.write(bi, "png", f);
    }


    public void getAllCars() throws InterruptedException, IOException {
        xpath.add(ALLCARS);
        xpath.add(CARMODELS);
        xpath.add(CARTYPE);
        method.add((e) -> getBrands(e));
        method.add((e) -> getModels(e));
        method.add((e) -> getType(e));
        getAll(0);
    }


    public void getAll(int idx) throws IOException, InterruptedException {
        Document doc = Jsoup.parse(String.valueOf(getHTML(new URL(SITE + temp.getReference()))));
        Elements elements = doc.select(xpath.get(idx));
        if (elements.isEmpty()) {
            dao.save(new Car(temp));
        } else {
            for (int i = 0; i < elements.size(); i++) {
                Element e = elements.get(i);
                boolean b = method.get(idx).apply(e);
                if (idx < xpath.size() - 1 && b) getAll(idx + 1);
            }
        }
    }

    public boolean getBrands(Element e) {
        String brand = e.select("> span").text();
        System.out.println(brand);
        if (!existCars.contains(brand) || last.getBrand().equals(brand)) {
            new File("C:/app/carmodels/" + brand.replace("/", "")).mkdir();
            try {
                saveImage(e.select("> div > img").attr("src"), "C:/app/carmodels/" + brand.replace("/", "") + "/logo.png");
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            temp.setBrand(brand);
            temp.setReference(e.attr("href"));
            temp.setModel(null);
            temp.setType(null);
            return true;
        }
        return false;
    }

    public boolean getModels(Element e) {
        String model = e.text();
        System.out.println("+"+model);
        if ((!existsModels.contains(model) && !existCars.contains(temp.getBrand())) || (last.getBrand().equals(temp.getBrand()) && last.getModel().equals(temp.getModel()))) {
            new File("C:/app/carmodels/" + temp.getBrand().replace("/", "") + "/" + model.replace("/", "")).mkdir();
            temp.setModel(model);
            temp.setReference(e.attr("href"));
            temp.setType(null);
            return true;
        }
        return false;
    }

    public boolean getType(Element e) {
        String type = e.select("> div").text();
        System.out.println("++"+type);
        if ((!existsTypes.contains(type) && !existsModels.contains(temp.getModel()) && !existCars.contains(temp.getBrand())) ||
                (last.getBrand().equals(temp.getBrand()) && last.getModel().equals(temp.getModel()) && !last.getModel().equals(temp.getModel()))) {
            String imageCar = e.select("> div:eq(0) > img").attr("src");
            temp.setType(type);
            temp.setReference(e.attr("href"));
            if (!"/static/images/nocars.png".equals(imageCar) && !imageCar.isEmpty()) {
                try {
                    saveImage(imageCar.contains("http") ? imageCar : SITE + imageCar, "C:/app/carmodels/" + temp.getBrand().replace("/", "") + "/" + temp.getModel().replace("/", "") + "/" + e.select("> div:eq(1)").text().replace("/", "") + ".png");
                } catch (FileNotFoundException ex) {
                    try {
                        saveImage(imageCar.contains("http") ? imageCar : SITE + imageCar, "C:/app/carmodels/" + e.select("> div:eq(1)").text().replace("/", "") + ".png");
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
            dao.save(temp);
            return true;
        }
        return false;
    }
}



