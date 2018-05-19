package keystone;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class forTests {

    public static void main(String[] args) throws IOException {
        /*System.setProperty("webdriver.chrome.driver", "F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://wwwsc.ekeystone.com/login?Logout=true&RedirectURL=/");
        driver.findElement(By.id("webcontent_0_txtUserName")).sendKeys("120704");
        driver.findElement(By.id("webcontent_0_txtPassword")).sendKeys("prim54-wrist");
        driver.findElement(By.name("webcontent_0$submit")).click();
        bad_sleep(2000);*/

        WebDriver driver = KeystoneUtil.initDriver();
      //  driver.get("https://wwwsc.ekeystone.com/Search/Detail?pid=BLS24-239417");
        driver.get("https://wwwsc.ekeystone.com/Search/Detail?pid=BLS24-256254");
        bad_sleep(2000);

        WebElement thisFits = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_divThisFitsTab"));
        thisFits.click();
        bad_sleep(2000);
        WebElement fitsTab = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_divTabContainer"));
        WebElement firstResult = fitsTab.findElement(By.id("webcontent_0_row2_0_productDetailTabs_rptrApplicationSummary_aApplicationSummary_0"));
        firstResult.click();
        bad_sleep(2000);
        WebElement openCars = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_rptrApplicationSummary_divRepeater_0"));
        List<WebElement> elements = openCars.findElements(By.className("applicationBlock"));
        System.out.println(elements.size());
        for (WebElement car : elements){
            WebElement attribute = car.findElement(By.className("applicationAttributeName"));
            WebElement attributeValue = car.findElement(By.className("applicationRequiredProducts"));
            System.out.println(attribute.getText());
            System.out.println(attributeValue.getText());
            System.out.println();
        }
        //System.out.println(fitsTab.getText());

      //  List<WebElement> elements = driver.findElements(By.className("productAttribute"));
       // List<WebElement> elements = driver.findElements(By.className("thslide_list_frame"));
        /*List<WebElement> elements = driver.findElements(By.id("partImage"));
        System.out.println(elements.size());
        String valueFormat = "webcontent_0_row2_0_productDetailTabs_rptrAttributes_lblAttributeValue_";
        int counter = 0;
        for (WebElement element: elements){
           *//* System.out.println(counter++);
          //  WebElement attributeName  = element.findElement(By.className("attributeName"));

            String attributeValueId = valueFormat+counter;
            WebElement attributeValue  = element.findElement(By.id(attributeValueId));
            System.out.println(attributeValue.getText());
            System.out.println();
            counter++;*//*

           WebElement url = element.findElement(By.tagName("img"));
            System.out.println(url.getAttribute("src"));
            System.out.println();
        }*/

        driver.close();

       /* List<WebElement> elements = driver.findElements(By.name("webcontent_0$submit"));
        System.out.println(elements.size());*/
     //   driver.get("https://wwwsc.ekeystone.com/");
        /*WebElement element = driver.findElement(By.name("webcontent_0$submit"));
        System.out.println(element.getText());
        //webcontent_0_txtUserName*/
    }



    private static void getImgUrls(WebDriver driver){
        List<WebElement> elements = driver.findElements(By.className("thslide_list_frame"));
        for (WebElement element: elements){
            WebElement url = element.findElement(By.tagName("a"));
            System.out.println(url.getAttribute("href"));
            System.out.println();
        }
    }
    private static void getThisFits(WebDriver driver){
        WebElement thisFits = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_divThisFitsTab"));
        thisFits.click();
        bad_sleep(2000);
        WebElement fitsTab = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_divTabContainer"));
        WebElement firstResult = fitsTab.findElement(By.id("webcontent_0_row2_0_productDetailTabs_rptrApplicationSummary_aApplicationSummary_0"));
        firstResult.click();
        bad_sleep(2000);
        WebElement openCars = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_rptrApplicationSummary_divRepeater_0"));
        List<WebElement> elements = openCars.findElements(By.className("applicationVehicleLink"));
        System.out.println(elements.size());
        for (WebElement car : elements){
            WebElement attribute = car.findElement(By.className("applicationAttributeValue"));
            System.out.println(attribute.getText());
            System.out.println();
        }
    }

    private static void getThisFitsAttributes(WebDriver driver){
        WebElement thisFits = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_divThisFitsTab"));
        thisFits.click();
        bad_sleep(2000);
        WebElement fitsTab = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_divTabContainer"));
        WebElement firstResult = fitsTab.findElement(By.id("webcontent_0_row2_0_productDetailTabs_rptrApplicationSummary_aApplicationSummary_0"));
        firstResult.click();
        bad_sleep(2000);
        WebElement openCars = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_rptrApplicationSummary_divRepeater_0"));
        List<WebElement> elements = openCars.findElements(By.className("applicationBlock"));
        System.out.println(elements.size());
        for (WebElement car : elements){
            WebElement attribute = car.findElement(By.className("applicationAttributeName"));
            WebElement attributeValue = car.findElement(By.className("applicationRequiredProducts"));
            System.out.println(attribute.getText());
            System.out.println(attributeValue.getText());
            System.out.println();
        }
    }

    private static void codeDump() throws IOException {
        String loginUrl = "https://wwwsc.ekeystone.com/Login?Logout=true";
        String loginUrl2 = "https://wwwsc.ekeystone.com/login?Logout=true&RedirectURL=/";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://wwwsc.ekeystone.com/Login?Logout=true");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("webcontent_0$txtUserName", "120704"));
        params.add(new BasicNameValuePair("webcontent_0$txtPassword", "prim54-wrist"));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        CloseableHttpResponse response = client.execute(httpPost);
        HttpGet request = new HttpGet("https://wwwsc.ekeystone.com/");
        CloseableHttpResponse response2 = client.execute(request);
        String theString = IOUtils.toString(response2.getEntity().getContent());
        System.out.println(theString);
        client.close();
    }

    public static void bad_sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }

}
