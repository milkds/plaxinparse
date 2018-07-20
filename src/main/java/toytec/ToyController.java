package toytec;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import secondstep.HibernateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToyController {

    public static void main(String[] args) throws IOException {
       // ToytecItemBuilder.buildItem("https://www.toyteclifts.com/ttbossres-9504-boss-performance-suspension-system-for-95-5-04-tacoma.html");
     //   testMethod();
     //   testIterate();

       // getAndSaveKits();

        parseCategory();
    }

    private static void getAndSaveKits(){
        WebDriver driver = ToyUtil.initDriver();
        List<WebElement> kitElements = ToyUtil.getKitElements(driver);
        List<ToyItem> toyItems = new ArrayList<>();
        for (WebElement element: kitElements){
            ToyItem item = ToyUtil.getItem(driver, element);
         //   toyItems.add(kit);
        }
    }

    private static void testMethod(){
        WebDriver driver = ToyUtil.initDriver2();
        ToyItem item = ToyUtil.testGetItem(driver);
    }

    private static void testIterate(){
        WebDriver driver = ToyUtil.initDriver3("https://www.toyteclifts.com/complete-lift-kits.html?product_list_limit=all&product_list_mode=list");
       // ToyUtil.iterateItems(driver);
    }

    private static void parseCategory(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        String categoryUrl = "https://www.toyteclifts.com/lighting.html?product_list_mode=list&product_list_limit=all";
        WebDriver driver = ToyUtil.initDriver3(categoryUrl);
        ToyUtil.iterateItems(driver, session);
        driver.close();
        System.exit(0);
    }
}
