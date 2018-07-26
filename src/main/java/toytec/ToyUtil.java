package toytec;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToyUtil {
    private static final String PROBLEM_LOG_PATH = "src\\main\\resources\\toytec_files\\problemlog";

    public static WebDriver initDriver(){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.toyteclifts.com/complete-lift-kits.html?product_list_limit=all");

       // while (driver.findElements(By.cssSelector("li[class='item product product-item']")).size()==0){
        while (driver.findElements(By.id("igSplashElement")).size()==0){
            bad_sleep(50);
        }

        WebDriverWait wait = new WebDriverWait(driver, 10);
     //   WebElement close = driver.findElement(By.id("igSplashElement"));
        WebElement  close = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("i[class='material-icons']")));
     //   close = close.findElement(By.cssSelector("i[class='material-icons']"));
        close.click();



        return driver;
    }

    public static WebDriver initDriver2(){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
       // driver.get("https://www.toyteclifts.com/25boss-4r210p-boss-performance-suspension-system-10-4runner.html");
        driver.get("https://www.toyteclifts.com/ttbossres-9504-boss-performance-suspension-system-for-95-5-04-tacoma.html");
       // driver.get("https://www.toyteclifts.com/glovemx-arb-recovery-gloves.html");
      //  driver.get("https://www.toyteclifts.com/ultac-2005-toytec-ultimate-lift-kit-05-15-tacoma.html");

        int counter = 0;
        while (driver.findElements(By.id("igSplashElement")).size()==0){
            if (counter == 100){
                break;
            }
            bad_sleep(100);
            counter++;
        }

        if (counter!=100){
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement  close = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("i[class='material-icons']")));
            close.click();
            System.out.println("close clicked");
        }

        while (driver.findElements(By.cssSelector("div[class='gallery-placeholder']")).size()==0){
            bad_sleep(50);
        }

        return driver;
    }
    public static WebDriver initDriver3(String categoryUrl){
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
      //  driver.get("https://www.toyteclifts.com/complete-lift-kits.html?product_list_limit=all&product_list_mode=list");
        driver.get(categoryUrl);

        int counter = 0;
        while (driver.findElements(By.id("igSplashElement")).size()==0){
            if (counter == 100){
                break;
            }
            bad_sleep(100);
            counter++;
        }

        if (counter!=100){
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement  close = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("i[class='material-icons']")));
            close.click();
            System.out.println("close clicked");
        }

        while (driver.findElements(By.cssSelector("div[class='products wrapper list products-list']")).size()==0){
            bad_sleep(50);
        }

        return driver;
    }

    public static void iterateItems(WebDriver driver, Session session){
        Boolean chooseNCountry = false;
        List<WebElement> items = driver.findElements(By.cssSelector("li[class='item product product-item']"));
       String itemCategoryLink = driver.getCurrentUrl();
       String itemCategory = driver.findElement(By.id("page-title-heading")).getText();
        for (WebElement item: items){
            WebElement itemClick = item.findElement(By.className("product-image-wrapper"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", itemClick);

            Actions actions = new Actions(driver);
            actions.keyDown(Keys.LEFT_CONTROL)
                    .click(itemClick)
                    .keyUp(Keys.LEFT_CONTROL)
                    .build()
                    .perform();
         //   itemClick.click();
            ArrayList<String> availableWindows = null;
            while (true){
                availableWindows = new ArrayList<String>(driver.getWindowHandles());
                if (availableWindows.size()>1){
                    break;
                }
            }
                driver.switchTo().window(availableWindows.get(1));
            if (!chooseNCountry) {
                chooseCountry(driver);
            }
                chooseNCountry = true;
                while (driver.findElements(By.id("maincontent")).size()==0){
                    bad_sleep(50);
                }
                ToyItem toyItem = testGetItem(driver);
                toyItem.setItemCategory(itemCategory);
                toyItem.setItemCategoryLink(itemCategoryLink);
                ToyTecDao.saveToyItem(toyItem,session);

                System.out.println(toyItem);

                driver.close();
                driver.switchTo().window(availableWindows.get(0));

        }
    }

    private static void chooseCountry(WebDriver driver) {
        int counter = 0;
        while (driver.findElements(By.id("igSplashElement")).size()==0){
            if (counter == 100){
                break;
            }
            bad_sleep(100);
            counter++;
        }

        if (counter!=100){
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement  close = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("i[class='material-icons']")));
            close.click();
            System.out.println("close clicked");
        }

        while (driver.findElements(By.cssSelector("div[class='gallery-placeholder']")).size()==0){
            bad_sleep(50);
        }
    }


    public static List<WebElement> getKitElements(WebDriver driver){

        return driver.findElements(By.cssSelector("li[class='item product product-item']"));
    }


    public static void bad_sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }

    public static ToyItem getItem(WebDriver driver, WebElement element) {
        element = element.findElement(By.cssSelector("a[class='product-item-link']"));
        element.click();
        while (driver.findElements(By.cssSelector("div[class='gallery-placeholder']")).size()==0){
            bad_sleep(50);
        }
        String sku = getSku(driver);
        String itemName = getItemName(driver);
        String description = getDescription(driver);
        String mainImgUrl = getMainImgUrl(driver);


        System.out.println("SKU: " + sku);
        System.out.println("Item Name:" + itemName);
        System.out.println("Description: " + description);
        System.out.println("Main Img URL: " + mainImgUrl);

        driver.close();
        System.exit(0);
        return null;
    }
    public static ToyItem testGetItem(WebDriver driver) {
        ToyItem item = new ToyItem();

        String sku = getSku(driver);
        String itemName = getItemName(driver);
        String description = getDescription(driver);
        String mainImgUrl = getMainImgUrl(driver);
        String imgUrls = getImgUrls(driver);
        String itemMake = getItemMake(driver);
        String instructions = getInstructions(driver);
        boolean inStock = isInStock(driver);
        BigDecimal priceFrom = getPriceFrom(driver);
        BigDecimal priceTo = getPriceTo(driver);
        String itemLink = driver.getCurrentUrl();

        List<ToyOption> options = getOptions(driver);

        System.out.println("SKU: " + sku);
        System.out.println("Item Name:" + itemName);
        System.out.println("Description: " + description);
        System.out.println("Main Img URL: " + mainImgUrl);
        System.out.println("IMG URLS: " + imgUrls);
        System.out.println("Instructions: " + instructions);
        System.out.println("In stock: " + inStock);
        System.out.println("Price from: " + priceFrom);
        System.out.println("Price to: " + priceTo);
        System.out.println("Item Link: " + itemLink);
        System.out.println("Item Make: " + itemMake);

        for (ToyOption option : options){
            System.out.println(option);
        }

        item.setSku(sku);
        item.setItemName(itemName);
        item.setDesc(description);
        item.setMainImg(mainImgUrl);
        item.setImgLinks(imgUrls);
        item.setItemMake(itemMake);
        item.setInstructions(instructions);
        item.setInStock(inStock);
        item.setPriceFrom(priceFrom);
        item.setPriceTo(priceTo);
        item.setItemLink(itemLink);
        item.setOptions(options);

        try {
            ToytecItemBuilder.getStock(item);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return item;
    }

    private static String getItemMake(WebDriver driver) {
        String itemMake = "";

        WebElement moreInfo = driver.findElement(By.id("tab-label-additional-title"));
        moreInfo.click();

        while (true){
            WebElement manufacturerEl = driver.findElement(By.cssSelector("td[data-th='Manufacturer']"));
            itemMake = manufacturerEl.getText();
            if (itemMake!=null&&itemMake.length()>0){
                break;
            }
            else {
                bad_sleep(100);
            }
        }

        WebElement additionalRowEl = driver.findElement(By.id("product-attribute-specs-table"));
        List<WebElement> additionalRows = additionalRowEl.findElements(By.tagName("tr"));
        if (additionalRows.size()>1){
            logUnexpectedData("unexpected additional info(rows qty)", driver.getCurrentUrl());
        }
        System.out.println("additional rows qty: "+additionalRows.size());

        return itemMake;
    }

    public static void logUnexpectedData(String message, String itemUrl) {
        try(FileWriter fw = new FileWriter(PROBLEM_LOG_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(message+"-----"+itemUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("problem logged for "+itemUrl);
    }


    private static List<ToyOption> getOptions(WebDriver driver) {
        List<ToyOption> options = new ArrayList<>();
       try  {
           WebElement customizeButton = driver.findElement(By.id("bundle-slide"));
           customizeButton.click();
       }
       catch (NoSuchElementException e){
           return options;
       }
       bad_sleep(5000);

        WebElement optionsEl = driver.findElement(By.className("bundle-options-container"));
        System.out.println("options found");
        optionsEl = optionsEl.findElement(By.id("product_addtocart_form"));
        System.out.println("form found");
        optionsEl = optionsEl.findElement(By.className("bundle-options-wrapper"));
        System.out.println("inner options element found");
        List<WebElement> optionsList = optionsEl.findElements(By.cssSelector("div[class='field option ']"));
        System.out.println(optionsList.size());
        List<WebElement> optionsListRequired = optionsEl.findElements(By.cssSelector("div[class='field option  required']"));
        optionsList.addAll(optionsListRequired);

        for (WebElement opt: optionsList){
            WebElement label = null;
            while (true){
                label = opt.findElement(By.className("label"));
                if (label.getText().length()>0){
                    break;
                }
            }
            //option group

            System.out.println(label.getText());

            //choices
            WebElement innerOptHold = opt.findElement(By.className("control"));
            innerOptHold = innerOptHold.findElement(By.cssSelector("div[class='nested options-list']"));
            List<WebElement> innerOptList = innerOptHold.findElements(By.cssSelector("div[class='field choice']"));
            for (WebElement choice: innerOptList){
                ToyOption option = new ToyOption();
                option.setOptionGroup(label.getText());
                choice = choice.findElement(By.className("label"));
                String choiceText = choice.getText();
                if (choiceText.contains("$")){
                    String choiceName = StringUtils.substringBefore(choiceText, " [");
                    String price = "";
                    price = StringUtils.substringBetween(choiceText," $", "]");
                    BigDecimal priceNum = new BigDecimal(price);
                    if (choiceText.contains("- $")){
                        priceNum = priceNum.negate();
                    }
                    option.setPrice(priceNum);
                    option.setOptionName(choiceName);
                }
                else {
                    option.setOptionName(choiceText);
                }
                options.add(option);
            }

        }

        return options;
    }

    private static BigDecimal getPriceTo(WebDriver driver) {
        WebElement priceToEl = null;
        String price = "";
        try {
            priceToEl = driver.findElement(By.className("price-to"));
        }
        catch (NoSuchElementException e){
            price = driver.findElement(By.className("price")).getText();
            price = StringUtils.substringAfter(price,"$");
            price = price.replaceAll(",", "");
            return new BigDecimal(price);
        }
        price = priceToEl.findElement(By.className("price")).getText();
        price = StringUtils.substringAfter(price,"$");
        price = price.replaceAll(",", "");

        return new BigDecimal(price);
    }

    private static BigDecimal getPriceFrom(WebDriver driver) {
        WebElement priceFromEl = null;
        String price = "";
        try {
            priceFromEl = driver.findElement(By.className("price-from"));
        }
        catch (NoSuchElementException e){
            price = driver.findElement(By.className("price")).getText();
            price = StringUtils.substringAfter(price,"$");
            price = price.replaceAll(",", "");
            return new BigDecimal(price);
        }

        price = priceFromEl.findElement(By.className("price")).getText();
        price = StringUtils.substringAfter(price,"$");
        price = price.replaceAll(",", "");

        return new BigDecimal(price);
    }

    private static boolean isInStock(WebDriver driver) {
        WebElement inStockEl = null;
        try {
            inStockEl = driver.findElement(By.cssSelector("p[class='stock available']"));
        }
        catch (NoSuchElementException e){
            return false;
        }

        return inStockEl.getText().equals("IN STOCK");
    }

    private static String getInstructions(WebDriver driver) {
        String instructions = "";
        WebElement instructionsTab = null;
        try{
            instructionsTab = driver.findElement(By.id("tab-label-mageworx_product_attachments-title"));
        }
        catch (NoSuchElementException e){
            return "";
        }
        instructionsTab.click();
        while (driver.findElements(By.className("item-link")).size()==0){
            bad_sleep(50);
        }
        WebElement instLink = driver.findElement(By.className("item-link"));
        instLink = instLink.findElement(By.tagName("a"));
        instructions = instLink.getAttribute("href");

        return instructions;
    }

    private static String getImgUrls(WebDriver driver) {
        StringBuilder imgUrls = new StringBuilder();
        String mainImgUrl = "";
        WebElement mainImgEl = driver.findElement(By.cssSelector("div[class='gallery-placeholder']"));
        try{
            mainImgEl = mainImgEl.findElement(By.cssSelector("div[class='fotorama__stage__shaft fotorama__grab']"));
        }
        catch (NoSuchElementException e){
            return "";
        }

        mainImgEl = mainImgEl.findElement(By.cssSelector("div[data-active='true']"));
        while (mainImgEl.findElements(By.tagName("img")).size()==0){
            bad_sleep(50);
        }
        mainImgEl = mainImgEl.findElement(By.tagName("img"));

        mainImgUrl = mainImgEl.getAttribute("src");
        String prevImgUrl = mainImgUrl;
        WebElement arrowEl = null;
        while (true){

            arrowEl = driver.findElement(By.cssSelector("div[class='gallery-placeholder']"));
            List<WebElement> arrows =arrowEl.findElements(By.className("fotorama__arr__arr"));
            arrowEl = arrows.get(1);

            String tempUrl = "";
            try{
                arrowEl.click();
            }
            catch (WebDriverException e){
                String imgUrlsStr = imgUrls.toString();
                int size = imgUrlsStr.length();
                if (size>0){
                    imgUrls.deleteCharAt(size-1);
                }


                return imgUrls.toString();
            }
            while (true){
                mainImgEl = driver.findElement(By.cssSelector("div[class='gallery-placeholder']"));
                mainImgEl = mainImgEl.findElement(By.cssSelector("div[class='fotorama__stage__shaft fotorama__grab']"));
                mainImgEl = mainImgEl.findElement(By.cssSelector("div[data-active='true']"));
                while (mainImgEl.findElements(By.tagName("img")).size()==0){
                    bad_sleep(50);
                }
                mainImgEl = mainImgEl.findElement(By.tagName("img"));
                 tempUrl = mainImgEl.getAttribute("src");
                 if (!tempUrl.equals(prevImgUrl)){
                     break;
                 }
            }
            prevImgUrl = tempUrl;
            if (mainImgUrl.equals(tempUrl)){
                break;
            }
            else {
                imgUrls.append(tempUrl);
                imgUrls.append("\n");
            }

        }

        String imgUrlsStr = imgUrls.toString();
        int size = imgUrlsStr.length();
        if (size>0){
            imgUrls.deleteCharAt(size-1);
        }


        return imgUrls.toString();
    }

    private static String getMainImgUrl(WebDriver driver) {
        String mainImgUrl = "";
        WebElement mainImgEl = driver.findElement(By.cssSelector("div[class='gallery-placeholder']"));
        System.out.println("waiting for gallery");
        int counter = 0;
        while (mainImgEl.findElements(By.cssSelector("div[class='fotorama__stage__shaft fotorama__grab']")).size()==0){
            bad_sleep(100);
            counter++;
            if (counter==100){
                break;
            }
        }
        if (counter!=100){
            mainImgEl = mainImgEl.findElement(By.cssSelector("div[class='fotorama__stage__shaft fotorama__grab']"));
        }
        else {
            mainImgEl = mainImgEl.findElement(By.cssSelector("div[class='fotorama__stage__shaft']"));
        }
        mainImgEl = mainImgEl.findElement(By.cssSelector("div[data-active='true']"));
        System.out.println("searching for img tag");
        while (mainImgEl.findElements(By.tagName("img")).size()==0){
            bad_sleep(50);
        }
        mainImgEl = mainImgEl.findElement(By.tagName("img"));
        mainImgUrl = mainImgEl.getAttribute("src");

        return mainImgUrl;
    }

    private static String getDescription(WebDriver driver) {
        String desc = "";
        WebElement descEl = driver.findElement(By.cssSelector("div[class='product attribute description']"));
        descEl = descEl.findElement(By.className("value"));
        desc = descEl.getAttribute("innerHTML");
        desc = StringUtils.substringAfter(desc,"\n");

        return desc;
    }

    private static String getItemName(WebDriver driver) {
        String itemName = "";
        WebElement itemNameEl = driver.findElement(By.cssSelector("span[data-ui-id='page-title-wrapper']"));

        itemName = itemNameEl.getText();
        itemName = StringUtils.substringAfter(itemName," - ");

        return itemName;
    }

    private static String getSku(WebDriver driver) {
        WebElement skuEl = driver.findElement(By.cssSelector("div[class='value']"));

        return skuEl.getText();
    }

    public static void getPageBySoup(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.body().select("*");
      //  List<Element> options = doc.getElementsByAttributeValueContaining("class","field option");
        try(FileWriter fw = new FileWriter("F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\toytec_files\\testPage", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (Element element : elements) {
                out.println(element.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static List<String> getItemSKUs(WebDriver driver) {
        List<String> itemSKUs = new ArrayList<>();
        List<WebElement> itemElements = driver.findElements(By.cssSelector("li[class='item product product-item']"));
        for (WebElement itemEl: itemElements){
            WebElement nameSkuEl = itemEl.findElement(By.className("product-item-link"));
            String nameSKUstring = nameSkuEl.getText();
            nameSKUstring = ToytecItemBuilder.getSkuFromSKUnameString(nameSKUstring);
            System.out.println(nameSKUstring);
        }
        return itemSKUs;
    }

    public static List<String> getSKUlistFromCategory(WebDriver driver) {
        List<String> skuList = new ArrayList<>();
        List<WebElement> itemEls = driver.findElements(By.cssSelector("li[class='item product product-item']"));
        for (WebElement element: itemEls){
            String itemSkuLine = element.findElement(By.className("product-item-link")).getText();
            itemSkuLine = ToytecItemBuilder.getSkuFromSKUnameString(itemSkuLine);
            skuList.add(itemSkuLine);
        }

        return skuList;
    }
    public static List<String> getItemLinksFromCategory(WebDriver driver) {
        List<String> itemLinks = new ArrayList<>();
        List<WebElement> itemEls = driver.findElements(By.cssSelector("li[class='item product product-item']"));
        for (WebElement element: itemEls){
            String itemLink = element.findElement(By.className("product-item-link")).getAttribute("href");

            itemLinks.add(itemLink);
        }

        return itemLinks;
    }

    public static List<ToyItem> getItemsBySkuList(List<String> skuList, Session session) {
        List<ToyItem> items = ToyTecDao.getItemsFromList(skuList, session);
        if (items.size()!= skuList.size()){
            System.out.println("item list doesn't match skulist. Checking.");
            checkMissingSKU(items,skuList);
        }
        return items;
    }
    public static List<ToyItem> getItemsByImgLinks(List<String> imgLinks, Session session) {
        List<ToyItem> items = ToyTecDao.getItemsFromList(imgLinks, session);
        if (items.size()!= imgLinks.size()){
            System.out.println("item list doesn't match skulist. Checking.");
            checkMissingItemLink(items,imgLinks);
        }
        return items;
    }

    private static void checkMissingSKU(List<ToyItem> items, List<String> skuList) {
        Map<String, ToyItem> itemMap = new HashMap<>();
        for (ToyItem item : items){
            itemMap.put(item.getSku(), item);
        }

        for (String sku: skuList){
            if (!itemMap.containsKey(sku)){
                logUnexpectedData("Cannot find item with sku: "+sku,"NO URL");
            }
        }
    }
    private static void checkMissingItemLink(List<ToyItem> items, List<String> itemLinks) {
        Map<String, ToyItem> itemMap = new HashMap<>();
        for (ToyItem item : items){
            itemMap.put(item.getItemLink(), item);
        }

        for (String itemLink: itemLinks){
            if (!itemMap.containsKey(itemLink)){
                logUnexpectedData("Cannot find item", itemLink);
            }
        }
    }

    public static void setSubCategory(List<ToyItem> itemsBySKU, String subCategory, Session session) {
        for (ToyItem item: itemsBySKU){
            item.setItemSubCategory(subCategory);
            System.out.println(item);
            ToyTecDao.updateItem(item,session);
        }
    }

    public static WebDriver initDriver4(String itemUrl) {
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(itemUrl);

        int counter = 0;
        while (driver.findElements(By.id("igSplashElement")).size()==0){
            if (counter == 100){
                break;
            }
            bad_sleep(100);
            counter++;
        }
        if (counter!=100){
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement  close = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("i[class='material-icons']")));
            close.click();
            System.out.println("close clicked");
        }

        while (driver.findElements(By.cssSelector("div[class='gallery-placeholder']")).size()==0){
            bad_sleep(50);
        }

        return driver;
    }
}
