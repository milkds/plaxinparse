package turn14;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TurnUtil {
    private static final String PRODUCT_URL = "https://www.turn14.com/search/index.php?vmmBrand=74&start=126";
    private static final String PROBLEM_LOG_PATH = "src\\main\\resources\\turn14_files\\problemlog";
    public static WebDriver initDriver(){
      //  System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
       System.setProperty("webdriver.gecko.driver", "src\\main\\resources\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
       // WebDriver driver = new ChromeDriver();
       // driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.turn14.com/");
        while(true){
            if (driver.findElements(By.cssSelector("input[name=username]")).size()!=0){
                break;
            }
            bad_sleep(50);
        }

        return driver;
    }

    public static void doLogin(WebDriver driver){
        driver.findElement(By.cssSelector("input[name=username]")).sendKeys("dima_dv1");
        driver.findElement(By.cssSelector("input[name=password]")).sendKeys("123456789");
        driver.findElement(By.cssSelector("button[type=submit]")).click();
        while(driver.findElements(By.id("toggle-quick-add")).size()==0){
            bad_sleep(50);
        }
    }

    public static void getProductList(WebDriver driver){
        driver.get(PRODUCT_URL);
        while (driver.findElements(By.id("search-page")).size()==0){
            bad_sleep(50);
        }
    }


    public static void bad_sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }

    public static void switchPage(WebDriver driver, int pageNo) {
        if (pageNo!=1){
            int pageQty = (pageNo-1)*25+1;
            WebElement pagination = driver.findElement(By.cssSelector("ul[class='pagination']"));
            WebElement page = pagination.findElement(By.cssSelector("a[href='/search/index.php?vmmBrand=74&amp;start="+pageQty+"']"));
            page.click();
            //do something
        }
    }

    public static List<TurnPart> getPartListFromCurrentPage(WebDriver driver) {
        List<TurnPart> parts = new ArrayList<>();
        List<WebElement> partElements = driver.findElements(By.cssSelector("div[class='panel panel-default search-container col-xs-12 bs-callout-stock ']"));
       if(partElements.size()==0);{
            partElements = driver.findElements(By.cssSelector("div[class='panel panel-default search-container col-xs-12 bs-callout-CANdropship ']"));
        }
        for (WebElement partElement: partElements){
            TurnPart part = new TurnPart();
            setFields(part, partElement, driver);
            parts.add(part);
            System.out.println(part);
            for (TurnCar car: part.getCars()){
                System.out.println(car);
            }
            bad_sleep(5000);
        }

       /* //test
        WebElement testEl = partElements.get(0);
        WebElement partNoEl = testEl.findElement(By.cssSelector("span[class='partNo product-info']"));
        partNoEl.click();
        bad_sleep(10000);
        List<WebElement> fitment = driver.findElements(By.id("headingTwo"));
        if (fitment.size()!=0){
            WebElement fit = fitment.get(0);
            fit.click();
            bad_sleep(10000);
            List<WebElement> close = driver.findElements(By.id("t14-modal-footer"));
            if (close.size()>0){
                WebElement button = close.get(0).findElement(By.tagName("button"));
                button.click();
                bad_sleep(10000);
            }
        }*/

        return parts;
    }

    private static void setFields(TurnPart part, WebElement partElement, WebDriver driver) {
        System.out.println("finding partNo el");
        WebElement partNoEl = partElement.findElement(By.cssSelector("span[class='partNo product-info']"));
        System.out.println("got element "+partNoEl.getText());
        String rawPartNo = partNoEl.getText();
        String partNo = StringUtils.substringAfter(rawPartNo,"#: ");
        part.setPartNo(partNo);

        WebElement parameterKeeper = partElement.findElement(By.cssSelector("div[class='description col-sm-6 noRightPad']"));
        List<WebElement> paramEls = parameterKeeper.findElements(By.tagName("p"));
        for (WebElement parmEl: paramEls){
            String parameterName  = parmEl.findElement(By.tagName("strong")).getText();
            String parameterValue = parmEl.getText();
            parameterValue = StringUtils.substringAfter(parameterValue, ": ");
            switch (parameterName){
                case "Manufacturer:": part.setPartMake(parameterValue); break;
                case "Pricing Group:": part.setPricingGroup(parameterValue); break;
                case "Description:": part.setDescription(parameterValue); break;
                case "Product Name:": part.setProductName(parameterValue); break;
                case "Box 1 Dims (LxWxH-W):": part.setBoxDims(parameterValue); break;
                default: logProblem(partNo, "unknown parameter in desc");
            }
        }

        System.out.println("main parameters parsed");

        WebElement priceKeeper = partElement.findElement(By.cssSelector("div[class='pricing-container col-sm-5 noRightPad']"));
        WebElement priceAdditionals = priceKeeper.findElement(By.cssSelector("div[class='inner-price-table col-xs-12 noRightPad noLeftPad']"));
        List<WebElement> prices = priceAdditionals.findElements(By.cssSelector("div[class='value col-xs-4 noLeftPad noRightPad text-right']"));

        part.setMap(prices.get(0).getText());
        part.setJobber(prices.get(1).getText());
        part.setRetail(prices.get(2).getText());

        WebElement mainPriceKeep = priceKeeper.findElement(By.cssSelector("div[class='cart col-xs-6 pull-right noRightPad']"));
        part.setPrice(mainPriceKeep.findElement(By.cssSelector("span[class='amount']")).getText());
        System.out.println("prices parsed");
        setAdditionalFieldsAndFit(driver,partNoEl, part);
    }

    private static void setAdditionalFieldsAndFit(WebDriver driver, WebElement partNoEl, TurnPart part) {
        /*Actions actions = new Actions(driver);
        System.out.println("moving to part");
        actions.moveToElement(partNoEl).click().perform();
        System.out.println("part clicked");*/
        partNoEl.click();
       // while (driver.findElements(By.cssSelector("div[class='thumnail-container']")).size()==0){
        while (driver.findElements(By.cssSelector("dl[class='dl-horizontal']")).size()==0){
            bad_sleep(50);
        }
        System.out.println("additional parameter page loaded");
        WebElement additionalElem = driver.findElement(By.cssSelector("dl[class='dl-horizontal']"));
        System.out.println(additionalElem.getText());
        List<WebElement> subAdds = additionalElem.findElements(By.tagName("dd"));
        while(true){
            try{
                String desc2 = subAdds.get(1).getText();
                if  (desc2!=null&& desc2.length()>0){
                    part.setDescription2(desc2);
                    break;
                }
            }
            catch (StaleElementReferenceException e){
                while(true){
                    try{
                        additionalElem = driver.findElement(By.cssSelector("dl[class='dl-horizontal']"));
                        System.out.println("additional el found");
                        subAdds = additionalElem.findElements(By.tagName("dd"));
                        break;
                    }
                    catch (StaleElementReferenceException ex){
                        System.out.println("stale element ex in additionals - fuck it");
                        bad_sleep(2000);
                    }
                }
                bad_sleep(100);
            }
        }

        if  (subAdds.size()>2) {
            while(true){
                try{
                    String color = subAdds.get(2).getText();
                    if  (color!=null&& color.length()>0){
                        part.setColor(color);
                        break;
                    }
                }
                catch (StaleElementReferenceException e){
                    subAdds = additionalElem.findElements(By.tagName("dd"));
                    bad_sleep(100);
                }
            }
        }
        if (subAdds.size()>3){
            logProblem(part.getPartNo(), "unexpected fields in additional attributes");
        }
      List<WebElement> picKeeperes = driver.findElements(By.cssSelector("div[class='col-sm-2 image-leftCol']"));
        if (picKeeperes.size()!=0){
            WebElement picKeeper = driver.findElement(By.cssSelector("div[class='col-sm-2 image-leftCol']"));
            setImgUrls(picKeeper, part);
        }


        System.out.println("additional parameters set, setting cars");
        setCars(driver, part);

        WebElement close = driver.findElement(By.id("t14-modal-footer"));
        WebElement button = close.findElement(By.tagName("button"));
        bad_sleep(3000);
        button.click();
        while (driver.findElements(By.id("search-page")).size()==0){
            bad_sleep(50);
        }
        System.out.println("returned to main page");
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", partNoEl);
    }

    private static void setCars(WebDriver driver, TurnPart part) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement fitment = wait.until(ExpectedConditions.elementToBeClickable(By.id("headingTwo")));

       // WebElement fitment = driver.findElement(By.id("headingTwo"));
        System.out.println("fitment found");
        fitment.click();
        System.out.println("fitment clicked");
        while (driver.findElements(By.cssSelector("ul[class='list-group']")).size()==0){
            bad_sleep(50);
        }
        System.out.println("car page loaded");
        WebElement carList = driver.findElement(By.cssSelector("ul[class='list-group']"));
        List<WebElement> carElements = carList.findElements(By.cssSelector("li[class='list-group-item list-group-item-info']"));
        List<TurnCar> cars = new ArrayList<>();
        for (WebElement carElement: carElements){
            WebElement carTag = carElement.findElement(By.tagName("a"));
            String carUrl = carTag.getAttribute("href");
            String make = StringUtils.substringBetween(carUrl,"Make=","&vmmModel");
            String model = StringUtils.substringBetween(carUrl,"Model=","&vmmSubmodel");
            String subModel = StringUtils.substringBetween(carUrl,"vmmSubmodel=","&vmmEngine");
            String engine = StringUtils.substringBetween(carUrl,"vmmEngine=","&vmmCategory");
            String category = StringUtils.substringAfter(carUrl,"vmmCategory=");

            TurnCar car = new TurnCar();
            car.setMake(make);
            car.setModel(model);
            car.setSubModel(subModel);
            car.setEngine(engine);
            car.setCategory(category);

            String notesRaw = carTag.getText();
            String years = StringUtils.substringBefore(notesRaw," "+make);
            String split[] = years.split("-");
            if (split.length==2){
                car.setYearStart(split[0]);
                car.setYearFinish(split[1]);
            }
            else {
                car.setYearStart(split[0]);
                car.setYearFinish(split[0]);
            }

            String notes1 = StringUtils.substringAfter(notesRaw,"::");
            car.setNote1(notes1);

            if (carElement.findElements(By.className("notesText")).size()!=0){
                WebElement notesElem = carElement.findElement(By.className("notesText"));
                car.setNotes(notesElem.getText());
            }
            cars.add(car);
        }
        part.setCars(cars);
        System.out.println("cars set");
    }

    private static void setImgUrls(WebElement picKeeper, TurnPart part) {
        List<WebElement> picThumbs = picKeeper.findElements(By.cssSelector("div[class='thumnail-container']"));
        StringBuilder sb = new StringBuilder();
        for (WebElement picEl: picThumbs){
            WebElement picTag = picEl.findElement(By.tagName("img"));
            sb.append(picTag.getAttribute("data-largeimage"));
            sb.append(";;");
        }
        String imgUrls = sb.toString();
        part.setImgLinks(imgUrls);
    }

    private static void logProblem(String partNo, String problemDesc) {
        try(FileWriter fw = new FileWriter(PROBLEM_LOG_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
                out.println(partNo+"-----"+problemDesc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("problem logged for "+partNo);
    }
}
