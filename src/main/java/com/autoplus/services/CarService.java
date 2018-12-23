package com.autoplus.services;

import com.autoplus.dao.CarDao;
import com.autoplus.dao.ModificationDao;
import com.autoplus.entity.Car;
import com.autoplus.entity.Modification;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


public class CarService extends AbstractService<Car> {



    public CarService(DataSource ds) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties prop = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(RESOURCES)) {
            prop.load(resourceStream);
        }
        SITE = prop.getProperty("site");
        ALLCARS = prop.getProperty("all-car-xpath");
        CARMODELS = prop.getProperty("carmodels-xpath");
        CARTYPE = prop.getProperty("cartype-xpath");
        MODIFICATION = prop.getProperty("modification-xpath");
        MOD_IMAGE = prop.getProperty("mod-image-xpath");
        dao = new CarDao(ds);
        moddao = new ModificationDao(ds);
        last = (Car) dao.getLastObject();
        List<Car> cars = dao.getAll();
        existCars = new ArrayList<>();
        cars.forEach(c -> existCars.add(c.getBrand()));
        if(last!=null) {
            existsModels = dao.getLastModels(last);
            existsTypes = dao.getLastTypes(last);
        }else{
            existsModels = new ArrayList<>();
            existsTypes = new ArrayList<>();
        }
        exists = new ArrayList<>();
        exists.add(existCars);
        exists.add(existsModels);
        exists.add(existsTypes);
        temp = new Car(null, null, null, "/cars/", null);
        xpath = new ArrayList<>();
        method = new ArrayList<>();
        existModification = new ArrayList<>();
        setProxies();
    }





    public void getAllCars() throws InterruptedException, IOException {
        xpath.add(ALLCARS);
        xpath.add(CARMODELS);
        xpath.add(CARTYPE);
        xpath.add(MODIFICATION);
        method.add((e) -> getBrands(e));
        method.add((e) -> getModels(e));
        method.add((e) -> getType(e));
        method.add((e) -> getModification(e));
        getAll(0, "/cars/");
    }




    public String getBrands(Element e) {
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
            return e.attr("href");
        }
        return null;
    }

    public String getModels(Element e) {
        String model = e.text();
        if (!existCars.contains(temp.getBrand()) || (last.getBrand().equals(temp.getBrand()) && (!existsModels.contains(model) || last.getModel().equals(model)))) {
            new File("C:/app/carmodels/" + temp.getBrand().replace("/", "") + "/" + model.replace("/", "")).mkdir();
            System.out.println("+" + model);
            temp.setModel(model);
            temp.setReference(e.attr("href"));
            temp.setType(null);
            return e.attr("href");
        }
        return null;
    }

    public String getType(Element e) {
        String year = e.select("> div > span").text();
        String type = e.select("> div").text().replace(e.select("> div > span").text(), "");
        System.out.println("++" + type);
        System.out.println("++" + year);
        if ((!existsModels.contains(temp.getModel()) && (!existCars.contains(temp.getBrand()) || temp.getBrand().equals(last.getBrand()))) ||
                (last.getBrand().equals(temp.getBrand()) && last.getModel().equals(temp.getModel()) && (!existsTypes.contains(type) || last.getType().equals(type)))) {
            String imageCar = e.select("> div:eq(0) > img").attr("src");
            temp.setType(type);
            temp.setReference(e.attr("href"));
            temp.setYear(year);
            if (!"/static/images/nocars.png".equals(imageCar) && !imageCar.isEmpty()) {
                try (InputStream in1 = saveImage(imageCar.contains("http") ? imageCar : SITE + imageCar, "C:/app/carmodels/" + temp.getBrand().replace("/", "") + "/" + temp.getModel().replace("/", "") + "/" + e.select("> div:eq(1)").text().replace("/", "") + ".png")){
                } catch (FileNotFoundException ex) {
                    try (InputStream in = saveImage(imageCar.contains("http") ? imageCar : SITE + imageCar, "C:/app/carmodels/" + e.select("> div:eq(1)").text().replace("/", "") + ".png")){
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
            return e.attr("href");
        }
        return null;
    }
    public String getModification(Element e) {
        String name = e.select("> td:eq(0)").text();
        String engineType = e.select("> td:eq(1)").text();
        String engineModel = e.select("> td:eq(2)").text();
        String engineCapacity = e.select("> td:eq(3)").text();
        String power = e.select("> td:eq(4)").text();
        String drive = e.select("> td:eq(5)").text();
        String date = e.select("> td:eq(6)").text();
        String reference = e.attr("data-slug");
        Car tempCar = (Car) dao.getOne(temp.getBrand(), temp.getModel(), temp.getType());
        if (tempCar.equals(last)) {
            existModification = moddao.getLastModification(last.getId());
        } else {
            existModification = new ArrayList<>();
        }
        if (!existModification.contains(name)) {
            System.out.println("+++"+tempCar +", " + name +", " + engineType +", " + engineModel +", " + engineCapacity +", " + power +", " + drive +", " + date +", " + reference);
            moddao.save(new Modification(tempCar, name, engineType, engineModel, engineCapacity, power, drive, date, reference, 0, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
            if(temp.getType()==null) {
                getModImage();
            }
            return e.attr("data-slug");
        }
        return null;
    }
    public void getModImage(){
        if(!new File("C:/app/carmodels/" + temp.getBrand().replace("/", "") + "/" + temp.getModel().replace("/", "")+"/"+ temp.getModel().replace("/", "") + ".png").exists()) {
            Elements elements = currentDoc.select(MOD_IMAGE);
            String imageMod = elements.attr("src");
            System.out.println("================>inside   " + imageMod);
            if (!"/static/images/nocars.png".equals(imageMod) && !imageMod.isEmpty()) {
                try (InputStream in = saveImage(imageMod.contains("http") ? imageMod : SITE + imageMod, "C:/app/carmodels/" + temp.getBrand().replace("/", "") + "/" + temp.getModel().replace("/", "") + "/" + temp.getModel().replace("/", "") + ".png")){
                } catch (FileNotFoundException ex) {
                    try (InputStream in2 = saveImage(imageMod.contains("http") ? imageMod : SITE + imageMod, "C:/app/carmodels/" + temp.getModel().replace("/", "") + ".png")){
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
    public void updateCarModify(URL url) throws IOException, InterruptedException {
        Document doc = Jsoup.parse(String.valueOf(getHTML(url)));
        String json = doc.body().data().replace("window.PRELOADED_STATE = ", "");
        System.out.println(json);
        JSONArray array = JsonPath.read(json, "$.unicat.modificationList");
        array.forEach(m -> {
            String ref = ((LinkedHashMap) m).get("slug").toString();
            System.out.println(ref);
            String[] params = new String[19];
            params[0] = JsonPath.read(m, "$.id").toString();
            params[1] = JsonPath.read(m, "$.description");
            params[2] = JsonPath.read(m, "$.start_year");
            params[3] = JsonPath.read(m, "$.end_year");
            params[4] = JsonPath.read(m, "$.attributes.Body.BodyType.value");
            params[5] = JsonPath.read(m, "$.attributes.Body.DriveType.value");
            params[6] = JsonPath.read(m, "$.attributes.TechnicalData.Capacity.value");
            params[7] = JsonPath.read(m, "$.attributes.TechnicalData.Capacity_Tax.value");
            params[8] = JsonPath.read(m, "$.attributes.TechnicalData.Capacity_Technical.value");
            params[9] = JsonPath.read(m, "$.attributes.TechnicalData.EngineType.value");
            params[10] = JsonPath.read(m, "$.attributes.TechnicalData.FuelMixture.value");
            params[11] = JsonPath.read(m, "$.attributes.TechnicalData.FuelType.value");
            params[12] = JsonPath.read(m, "$.attributes.TechnicalData.NumberOfCylinders.value");
            params[13] = JsonPath.read(m, "$.attributes.TechnicalData.NumberOfValves.value");
            params[14] = JsonPath.read(m, "$.attributes.TechnicalData.Power.value");
            params[15] = JsonPath.read(m, "$.attributes.General.ConstructionInterval.value");
            params[16] = JsonPath.read(m, "$.attributes.Engine.EngineCode.value");
            params[17] = JsonPath.read(m, "$.start_year_month");
            params[18] = JsonPath.read(m, "$.end_year_month");
            Arrays.asList(params).forEach(p -> System.out.println(p));
            moddao.update(ref, params);
        });
    }
    public void updateAll(){
        List<Car> cars = dao.getAll();
        cars.forEach(c ->{
            try {
                updateCarModify(new URL(SITE+c.getReference()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}



