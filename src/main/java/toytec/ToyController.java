package toytec;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import secondstep.HibernateUtil;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ToyController {

    public static void main(String[] args) throws IOException {
       // ToytecItemBuilder.buildItem("https://www.toyteclifts.com/ttbossres-9504-boss-performance-suspension-system-for-95-5-04-tacoma.html");
     //   testMethod();
     //   testIterate();

       // getAndSaveKits();

        //updateCategory();
       // parseItem();

      //  getItemMeta();
        updateItemStock();
    }
    private static void updateCategory() throws IOException {
        WebDriver driver = ToyUtil.initDriver3("https://www.toyteclifts.com/front-lifts-coilovers/front-coilovers-and-spacers.html?product_list_limit=all&product_list_mode=list");
       // List<String> skuList = ToyUtil.getSKUlistFromCategory(driver);
        List<String> itemLinks = ToyUtil.getItemLinksFromCategory(driver);
        String subCategory = driver.findElement(By.id("page-title-heading")).getText();
        driver.close();
        Session session = HibernateUtil.getSessionFactory().openSession();
      //  List<ToyItem> itemsBySKU  = ToyUtil.getItemsBySkuList(skuList, session);
        List<ToyItem> itemsByLinks  = ToyUtil.getItemsByImgLinks(itemLinks, session);
        ToyUtil.setSubCategory(itemsByLinks, subCategory, session);
        HibernateUtil.shutdown();
        System.exit(0);
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
    private static void parseItem(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        String itemUrl = "https://www.toyteclifts.com/omefrtac05-ome-front-coilovers-2005-2015-tacoma.html";
        WebDriver driver = ToyUtil.initDriver4(itemUrl);
        ToyItem item = ToyUtil.testGetItem(driver);

        item.setItemCategory("FRONT SUSPENSION");
        item.setItemCategoryLink("https://www.toyteclifts.com/front-lifts-coilovers.html?product_list_mode=list&product_list_limit=all");
        item.setItemSubCategory("FRONT LIFTS & COILOVERS");

        ToyTecDao.saveToyItem(item,session);

        driver.close();
        System.exit(0);
    }

    private static void getItemMeta(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ToyItem> items = ToyTecDao.getAllItems(session);
        int counter  = 0;
        int total = items.size();
        for (ToyItem item: items){
            try {
                ToytecItemBuilder.getMeta(item);
            } catch (IOException e) {
                System.out.println("item unavailable: "+item.getItemLink());
            }
            ToyTecDao.updateItem(item, session);
            counter++;
            System.out.println("updated item " + counter + " out of total " + total);
        }
        HibernateUtil.shutdown();
    }
    private static void updateItemStock(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<ToyItem> items = ToyTecDao.getAllItems(session);
        int counter  = 0;
        int total = items.size();
        Instant start = Instant.now();
        for (ToyItem item: items){
            try {
                ToytecItemBuilder.getStock(item);
            } catch (IOException e) {
                System.out.println("item unavailable: "+item.getItemLink());
            }
            ToyTecDao.updateItem(item, session);
            counter++;
            System.out.println("updated item " + counter + " out of total " + total);
        }

        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));
        HibernateUtil.shutdown();
        System.exit(0);
    }
}
