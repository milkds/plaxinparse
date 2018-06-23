package koni;

import firststep.SearchData;
import koni.entities.KoniCar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class KoniService {
    private static final String MAKE_PATH = "src\\main\\resources\\koni_files\\makes";
    private static final String MAKE_MODEL_PATH = "src\\main\\resources\\koni_files\\makeModels";
    private static final String UNKNOWN_SCENARIO = "src\\main\\resources\\koni_files\\unknownScenario";

    public static WebDriver initDriver(){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.koni.com/en-US/Cars/Products/Catalog/");

        return driver;
    }

    public static void bad_sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }

    public static void saveDataToFile(List<String> data){
        try(FileWriter fw = new FileWriter(MAKE_MODEL_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String dataLine: data ){
                out.println(dataLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(data.get(0)+ " written successfully");
    }

    public static List<KoniCar> buildCars(WebDriver driver) {
        List<KoniCar> cars = new ArrayList<>();
        loadTable(driver);
        WebElement makeElement = driver.findElement(By.cssSelector("tr[class='first make']"));
        String make = makeElement.getText();
        WebElement modelElem = driver.findElement(By.className("model"));
        String model = modelElem.getText();
        List<WebElement> articles = driver.findElements(By.className("article"));
        KoniCar car  = new KoniCar();
        car.setCarMake(make);
        car.setCarModel(model);
        for (WebElement article:articles){
            if (newCar(article)){
                buildCar(article, car);
                KoniCar finalCar = getFinalCar(car);
                cars.add(finalCar);
            }
            else if (newCarNotes(article)){
                updateCarNotes(article, car);
            }
        }

        for (KoniCar car1: cars){
            System.out.println(car1);
        }

        return cars;
    }

    private static void updateCarNotes(WebElement article, KoniCar car) {
        WebElement frst = article.findElement(By.className("first"));
        car.setCarNotes(frst.getText());
    }

    private static boolean newCarNotes(WebElement article) {
        return article.findElements(By.className("first")).size() != 0;
    }

    private static KoniCar getFinalCar(KoniCar car) {
        KoniCar newCar = new KoniCar();

        newCar.setCarMake(car.getCarMake());
        newCar.setCarModel(car.getCarModel());
        newCar.setCarSubmodel(car.getCarSubmodel());
        newCar.setYearStart(car.getYearStart());
        newCar.setYearFinish(car.getYearFinish());
        newCar.setCarNotes(car.getCarNotes());

        return newCar;
    }

    private static void buildCar(WebElement article, KoniCar car) {
        WebElement submodel = article.findElement(By.className("first"));
        processSubModel(car, submodel);
        WebElement years = article.findElement(By.cssSelector("td[class='year number']"));
        processYears(car, years);
    }

    private static void processYears(KoniCar car, WebElement years) {
        String rawYears = years.getText();
        rawYears = rawYears.replace(" ","");
        String split[] = rawYears.split("-");
        if (split.length!=2){
            String msg = "unexpected years data";
            logData(car, msg, UNKNOWN_SCENARIO);
            return;
        }
        car.setYearStart(split[0]);
        car.setYearFinish(split[1]);
        normalizeYears(car);
    }

    private static void normalizeYears(KoniCar car) {
        String start = car.getYearStart();
        String finish = car.getYearFinish();
        String yearNotes=null;
        if (start.contains(".")){
            yearNotes = "from "+start;
        }
        if (finish.contains(".")){
            if (yearNotes!=null){
                yearNotes = yearNotes + " till "+finish;
            }
            else yearNotes = "till "+finish;
        }
        if (yearNotes!=null){
            car.setYearNotes(yearNotes);
        }

        start = normalizeYear(start);
        finish = normalizeYear(finish);
        if (start==null||finish==null){
            String msg = "problem with splitting month from year";
            logData(car, msg, UNKNOWN_SCENARIO);
            return;
        }

        car.setYearStart(start);
        car.setYearFinish(finish);
    }

    private static String normalizeYear(String year) {
        if (year.contains(".")){
            String split[] = year.split(".");
            if (split.length!=2){
                return null;
            }
            year = split[1];
        }

        if (year.length()==1){
            year = "200"+year;
            return year;
        }
        if (year.startsWith("1")||year.startsWith("0")){
            year = "20"+year;
        }
        else {
            year = "19"+year;
        }
        return year;
    }


    private static void processSubModel(KoniCar car, WebElement submodel){
        if (submodel.findElements(By.tagName("br")).size()==0){
            car.setCarSubmodel(submodel.getText());
        }
        else {
            WebElement subNameEl = submodel.findElement(By.className("name"));
            String subName = subNameEl.getText();
            car.setCarSubmodel(subName);

            String carNotes = submodel.getText();
            carNotes = carNotes.replace(subName,"");
            car.setCarNotes(carNotes);
        }
    }

    private static boolean newCar(WebElement article){
        return article.findElements(By.cssSelector("td[class='year number']")).size() != 0;
    }



    private static void logData(KoniCar car, String msg, String path){
        try(FileWriter fw = new FileWriter(path, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            StringBuilder sb = new StringBuilder();
            sb.append(car.getCarMake());
            sb.append(";;");
            sb.append(car.getCarModel());
            sb.append(";;");
            sb.append(car.getCarSubmodel());
            sb.append(":::");
            sb.append(msg);
            out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void loadTable(WebDriver driver){
        WebElement table;
        boolean breakIt = true;
        while (true) {
            breakIt = true;
            try {
                table = driver.findElement(By.className("model"));
            } catch (Exception e) {
                String msg = e.getMessage();
                if (msg.contains("element is not attached")||msg.contains("no such element")) {
                    breakIt = false;
                }
            }
            if (breakIt) {
                break;
            }
        }
    }
}
