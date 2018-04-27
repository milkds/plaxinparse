import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public static void main(String[] args) throws IOException {
    String stopString = "1974;;9191152257727449994;;International;;7666425734810545164;;Scout II;;5561292135176590932;;Base;;7772828447578431571";
        //testSelenium();
        //checkButton();
    //  SeleniumService.getDataforSearch(1974,1900);
           SeleniumService.getDataforSearchFromStop(1974,1900, stopString);
        //Service.checkParseConsistency(2019);
      //  Service.removeYear("2017");
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
        System.setProperty("webdriver.chrome.driver", "F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://cart.bilsteinus.com");
        WebElement drop_down = driver.findElement(By.id("engineSelector-year"));
        Select se = new Select(drop_down);
        List<WebElement> options = se.getOptions();
        for (int i = 1; i < options.size()-5; i++) {
            WebElement year = options.get(i);
            se.selectByIndex(i);
            Controller.bad_sleep(2000);
            WebElement drop_down2 = driver.findElement(By.id("engineSelector-make"));
            Select se2 = new Select(drop_down2);
            List<WebElement> options2 = se2.getOptions();
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
            Service.saveYearMakes(yearMakes,year.getText());
        }
        driver.close();
    }

    private static void getYearsMakeModels(int startYear, int finishYear){
        WebDriver driver = SeleniumService.initDriver();
        List<SearchData> dataSet = Service.getYearMakeData(startYear,finishYear);
        for (SearchData data: dataSet){
         //   SeleniumService.getYearMakeModels(driver, data);
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
