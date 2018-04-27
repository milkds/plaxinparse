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
        System.setProperty("webdriver.chrome.driver", "F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://wwwsc.ekeystone.com/login?Logout=true&RedirectURL=/");
        driver.findElement(By.id("webcontent_0_txtUserName")).sendKeys("120704");
        driver.findElement(By.id("webcontent_0_txtPassword")).sendKeys("prim54-wrist");
        driver.findElement(By.name("webcontent_0$submit")).click();
       /* List<WebElement> elements = driver.findElements(By.name("webcontent_0$submit"));
        System.out.println(elements.size());*/
     //   driver.get("https://wwwsc.ekeystone.com/");
        /*WebElement element = driver.findElement(By.name("webcontent_0$submit"));
        System.out.println(element.getText());
        //webcontent_0_txtUserName*/
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
}
