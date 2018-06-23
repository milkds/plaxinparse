package firststep;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public static void main(String[] args) throws IOException {
    String stopString = "1993;;480154208950490439;;Chevrolet;;636264562659427950;;Camaro;;1937232241709715798;;Z28;;9013643089022421650";
    //firststep.SeleniumService.getDataforSearchFromStop(1993,1900, stopString);
     WebDriver driver =  SeleniumService.initDriver();
      //   firststep.SeleniumService.getDataforSearch(2000,1900);
       // firststep.Service.checkParseConsistency(2019,driver);
        for (int i = 2018; i >1900 ; i--) {
            Service.checkParseConsistency(i,driver);
        }
       // driver.close();
      //  firststep.Service.removeYear("2017");
      //  testSelenium();
    }


    private static String getPage() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://cart.bilsteinus.com/API/Exchange/FYVService/GetMakes?id=");
        CloseableHttpResponse response = client.execute(httpPost);
        String theString = IOUtils.toString(response.getEntity().getContent());
        System.out.println(theString);
        client.close();
        return "";
    }

    private static void testSelenium(){
        WebDriver driver = SeleniumService.initDriver();
        WebElement drop_down = driver.findElement(By.id("engineSelector-year"));
        Select se = new Select(drop_down);
        List<WebElement> options = se.getOptions();

        for (int i = 1; i < options.size()-5; i++) {
            WebElement year = options.get(i);
            System.out.println(year.getText());
            se.selectByIndex(i);
           // bad_sleep(1000);


            WebElement drop_down2 = driver.findElement(By.id("engineSelector-make"));
            Select se2 = new Select(drop_down2);
            List<WebElement> options2 = se2.getOptions();
            for (int j = 0; j < 100; j++) {
                System.out.println(options2.size());
                bad_sleep(5);
                options2 = se2.getOptions();
            }
            List<SearchData> yearMakes = new ArrayList<>();
            for (int j = 1; j < options2.size(); j++) {
                WebElement make = options2.get(j);
                SearchData data = new SearchData();
                data.setYearText(year.getText());
                data.setYearValue(year.getAttribute("value"));
                data.setMakeText(make.getText());
                data.setMakeValue(make.getAttribute("value"));
                yearMakes.add(data);
            }
          //  firststep.Service.saveYearMakes(yearMakes,year.getText());
        }
        driver.close();
    }

    private static void getYearsMakeModels(int startYear, int finishYear){
        WebDriver driver = SeleniumService.initDriver();
        List<SearchData> dataSet = Service.getYearMakeData(startYear,finishYear);
        for (SearchData data: dataSet){
         //   firststep.SeleniumService.getYearMakeModels(driver, data);
        }
        driver.close();
    }

    private static void checkButton(){
        WebDriver driver = SeleniumService.initDriver();
        WebElement drop_down = driver.findElement(By.id("engineSelector-year"));
        Select se = new Select(drop_down);
        se.selectByIndex(1);
        Controller.bad_sleep(2000);
        WebElement drop_down2 = driver.findElement(By.id("engineSelector-make"));
        Select se2 = new Select(drop_down2);
        //se2.selectByVisibleText("Ram");
        se2.selectByIndex(1);
        Controller.bad_sleep(2000);
        WebElement drop_down3 = driver.findElement(By.id("engineSelector-model"));
        Select se3 = new Select(drop_down3);
        se3.selectByIndex(1);
        Controller.bad_sleep(2000);
        WebElement drop_down4 = driver.findElement(By.id("engineSelector-submodel"));
        Select se4 = new Select(drop_down4);
        se4.selectByIndex(1);
        Controller.bad_sleep(2000);


        try {
            WebElement drop0 = driver.findElement(By.id("inlineDrop-0"));
            System.out.println("we find bliss in ignorance");
        }
        catch (NoSuchElementException e){
            System.out.println("all this words they make no sense");
        }

        driver.close();
    }

    public static void sleepUntilReady(WebDriver driver, String searchLine){
        int counter =0;
        while (driver.findElements(By.id(searchLine)).size()==0&&driver.findElements(By.id("fyvCartBtn")).size()==0&&counter<10000){
            bad_sleep(100);
            counter++;
        }
    }

    public static void bad_sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }
}
