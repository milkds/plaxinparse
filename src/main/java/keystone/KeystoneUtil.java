package keystone;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import secondstep.HibernateUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeystoneUtil {
    //private static final String DATA_FOR_SEARCH = "F:\\My Java Projects\\plaxinparse\\src\\main\\java\\keystone\\log\\toparse";
    private static final String DATA_FOR_SEARCH = "F:\\My Java Projects\\plaxinparse\\src\\main\\java\\keystone\\log\\secondAttributeReparse";
    private static final String NO_RESULT = "F:\\My Java Projects\\plaxinparse\\src\\main\\java\\keystone\\log\\noResult";
    static final String WITH_RESULT = "F:\\My Java Projects\\plaxinparse\\src\\main\\java\\keystone\\log\\withResult";
    public static final String TO_CHECK = "F:\\My Java Projects\\plaxinparse\\src\\main\\java\\keystone\\log\\withProblems";
    static final String MAIN_URL_FORMAT = "https://wwwsc.ekeystone.com/Search/Detail?pid=BLS%s";
   // private static final String MAIN_URL_FORMAT = "https://wwwsc.ekeystone.com/Search/Detail?pid=B52%s";
   // private static final String MAIN_URL_FORMAT = "https://wwwsc.ekeystone.com/Search/Detail?aid=%s";

    public static WebDriver initDriver(){
        System.setProperty("webdriver.chrome.driver", "F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://wwwsc.ekeystone.com/login?Logout=true&RedirectURL=/");
        driver.findElement(By.id("webcontent_0_txtUserName")).sendKeys("");
        driver.findElement(By.id("webcontent_0_txtPassword")).sendKeys("");
        driver.findElement(By.name("webcontent_0$submit")).click();

        while (driver.findElements(By.id("headerSmartSearchContainer")).size()==0){
            bad_sleep(100);
        }

        return driver;
    }

    public static List<String> getPartNos(){
        List<String> result = new ArrayList<>();
        //List<String> parsed = new ArrayList<>();

        try {
            result = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
           // parsed = Files.readAllLines(Paths.get(WITH_RESULT));
        } catch (IOException e) {
            e.printStackTrace();

        }
       // result.removeAll(parsed);
        return result;
    }

    public static List<String> getSearchParts(){
        List<String> result = new ArrayList<>();
        List<String> noResult;
        List<String> withResult;
        List<String> withProblems;
        try {
            result = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
           // result = Files.readAllLines(Paths.get(TO_CHECK));
            noResult = Files.readAllLines(Paths.get(NO_RESULT));
            withResult = Files.readAllLines(Paths.get(WITH_RESULT));
            withProblems = Files.readAllLines(Paths.get(TO_CHECK));
            result.removeAll(noResult);
            result.removeAll(withResult);
            result.removeAll(withProblems);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return result;
    }

      static void bad_sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }

    public static String getPartLine(WebDriver driver, String partNo) {
        int counter = 0;
        int sizeSuccess;
        int sizeFault;
        driver.get(String.format(MAIN_URL_FORMAT,partNo));
        while (true){
            sizeSuccess = driver.findElements(By.id("webcontent_0_row2_0_productDetailHeader_lblDescription")).size();
            sizeFault = driver.findElements(By.id("webcontent_0_row2_0_lblMessage")).size();
            if (sizeSuccess==0&&sizeFault==0){
                if (counter>100){
                    counter = 0;
                    driver.get(String.format(MAIN_URL_FORMAT,partNo));
                }
                bad_sleep(100);
                counter++;
            }
            else break;
        }
        if (sizeFault==1) return "fault";
        else{
            WebElement productLine = driver.findElement(By.id("webcontent_0_row2_0_productDetailHeader_lblDescription"));
            return productLine.getText();
        }
    }

    public static void processResult(String partLine, String partNo, WebDriver driver, Session session) {
        if (partLine.equals("fault")){
            processFault(partNo);
        }
        else if (partLine.startsWith("Shock Absorber")){
            processShock(driver,partNo, session);
        }
        else processAdditionals(driver,partNo,partLine, session);
    }

    private static void processFault(String partNo){
        logResult(partNo,NO_RESULT);
        System.out.println("no result for "+partNo);

    }

    private static void processAdditionals(WebDriver driver, String partNo, String partLine, Session session){
        System.out.println("this is not a shock absorber");
        List<KeyAttribute> attributes = getAttributes(driver);
        System.out.println("we have attributes");
        StringBuilder builder = new StringBuilder();
        for (KeyAttribute attribute: attributes){
            builder.append(attribute.getAttributeName());
            builder.append(";;");
            builder.append(attribute.getAttributeValue());
            builder.append("\n");
            //System.out.println(attribute);
        }
        KeyAdditionalPart part = new KeyAdditionalPart();
        part.setPartNo(partNo);
        part.setPartLine(partLine);
        part.setAttributes(builder.toString());
        part.setImgUrls(getImgUrls(driver));

        System.out.println("everything set for thisfits");

        int counter = 0;
        List<KeyCar> cars = getCarElements(driver);
        while (cars == null && counter<5){
            counter++;
            cars = getCarElements(driver);
        }
        if (cars==null){
            logResult(partNo, TO_CHECK);
        }
        else{
            for (KeyCar car: cars){
                car.setShockPartNo(partNo);
            }
            part.setCars(cars);
           int resultCode = KeyDao.saveAdditionalPart (session, part);
           if (resultCode==1){
               logResult(partNo, TO_CHECK);
           }
           else
               logResult(partNo, WITH_RESULT);
        }
    }

    private static void processShock(WebDriver driver, String partNo, Session session){
       // System.out.println("Its definitely shock absorber");
        List<KeyAttribute> attributes = getAttributes(driver);
        KeyShock shock = processAttributes(attributes);
        if (shock==null){
            logResult(partNo,TO_CHECK);
        }
        else {
            shock.setPartNo(partNo);
            shock.setImgUrl(getImgUrls(driver));
            int counter = 0;
            List<KeyCar> cars = getCarElements(driver);
            while (cars == null && counter<5){
                counter++;
                cars = getCarElements(driver);
            }
            if (cars==null){
                logResult(partNo, TO_CHECK);
            }
            else{
                for (KeyCar car: cars){
                    car.setShockPartNo(partNo);
                }
                shock.setCars(cars);
                //System.out.println(shock);
                int resultCode = KeyDao.saveShock(session, shock);
                if (resultCode==1){
                    logResult(partNo, TO_CHECK);
                }
                else
                    logResult(partNo, WITH_RESULT);
            }
        }
    }

    private static KeyShock processAttributes(List<KeyAttribute> attributes) {
        KeyShock shock = new KeyShock();
        for (KeyAttribute attribute: attributes){
            switch (attribute.getAttributeName()){
                case "Type": shock.setType(attribute.getAttributeValue()); break;
                case "Internal Design": shock.setInternalDesign(attribute.getAttributeValue()); break;
                case "Adjustable": shock.setAdjustable(attribute.getAttributeValue()); break;
                case "Extended Length (IN)": shock.setExtendedLength(attribute.getAttributeValue()); break;
                case "Compressed Length (IN)": shock.setCompressedLength(attribute.getAttributeValue()); break;
                case "Upper Mounting Style": shock.setUpperMountingStyle(attribute.getAttributeValue()); break;
                case "Lower Mounting Style": shock.setLowerMountingStyle(attribute.getAttributeValue()); break;
                case "Body Color": shock.setBodyColor(attribute.getAttributeValue()); break;
                case "With Reservoir": shock.setWithReservoir(attribute.getAttributeValue()); break;
                case "Includes Dust Shield": shock.setIncludesDustShield(attribute.getAttributeValue()); break;
                case "Includes Hardware": shock.setIncludesHardware(attribute.getAttributeValue()); break;
                case "Includes Boot": shock.setIncludesBoot(attribute.getAttributeValue()); break;
                case "Quantity": shock.setQuantity(attribute.getAttributeValue()); break;
                case "Rod Diameter (IN)": shock.setRodDiameter(attribute.getAttributeValue()); break;
                case "Body Material": shock.setBodyMaterial(attribute.getAttributeValue()); break;
                case "Rod Finish": shock.setRodFinish(attribute.getAttributeValue()); break;
                case "Valving Type": shock.setValvingType(attribute.getAttributeValue()); break;
                case "Rod Material": shock.setRodMaterial(attribute.getAttributeValue()); break;
                default: {
                    System.out.println(attribute);
                    //System.exit(0);
                    return null;
                }
            }
        }


        return shock;
    }

     static void logResult(String partNo, String path){
        try(FileWriter fw = new FileWriter(path, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(partNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<KeyAttribute> getAttributes(WebDriver driver){
        List<KeyAttribute> attributes = new ArrayList<>();
        List<WebElement> attributeElements = driver.findElements(By.className("productAttribute"));
        if(attributeElements.size()==0){
            return attributes;
        }

        String valueFormat = "webcontent_0_row2_0_productDetailTabs_rptrAttributes_lblAttributeValue_";
        int counter = 0;
        for (WebElement element: attributeElements){
            KeyAttribute attribute = new KeyAttribute();
            WebElement attributeName  = element.findElement(By.className("attributeName"));
            attribute.setAttributeName(attributeName.getText());

            String attributeValueId = valueFormat+counter;
            WebElement attributeValue  = element.findElement(By.id(attributeValueId));
            attribute.setAttributeValue(attributeValue.getText());

            attributes.add(attribute);
            counter++;
        }

        return attributes;
    }

    private static List<KeyCar> getCarElements(WebDriver driver){
        List<WebElement> carElements = new ArrayList<>();
        List<KeyCar> allCars = new ArrayList<>();
        if  (driver.findElements(By.id("webcontent_0_row2_0_productDetailTabs_divThisFitsTab")).size()==0){
            return allCars;
        }
        WebElement thisFits = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_divThisFitsTab"));
        thisFits.click();
        int counter = 0;
        while (driver.findElements(By.id("webcontent_0_row2_0_productDetailTabs_divTabContainer")).size()==0 && counter <200){
            bad_sleep(100);
            counter++;
        }
        if (counter==200){
            return null;
        }

        WebElement carList = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_upThisFitsTab"));
        carElements = carList.findElements(By.className("applicationSummary"));

        int size = carElements.size();
        for (int i = 0; i < size; i++) {
            WebElement carToOpen = carElements.get(i);
            String url = carToOpen.getAttribute("href");
            carToOpen.click();
            String id = "webcontent_0_row2_0_productDetailTabs_rptrApplicationSummary_aApplicationSummary_"+i;
            int fitsCounter = 0;
            while (driver.findElements(By.id(id)).size()==0&&fitsCounter<200){
                bad_sleep(100);
                fitsCounter++;
            }
            if (fitsCounter==200){
                return null;
            }
            System.out.println("car list opened");
            WebElement openedCars = driver.findElement(By.id(id));
            List<KeyCar> cars = buildCars(driver, openedCars, i, url);
            if (cars==null){
                return null;
            }
            allCars.addAll(cars);

            carList = driver.findElement(By.id("webcontent_0_row2_0_productDetailTabs_upThisFitsTab"));
            carElements = carList.findElements(By.className("applicationSummary"));
        }

        return allCars;
    }

    private static String getImgUrls (WebDriver driver){
        StringBuilder imgUrls = new StringBuilder();
        List<WebElement> imgGallery = driver.findElements(By.id("webcontent_0_row2_0_divImageScroller"));
        System.out.println(imgGallery.size());
        if (imgGallery.size()>0) {

            List<WebElement> slides = imgGallery.get(0).findElements(By.className("thumbnailContainer"));
            System.out.println("slides size - "+ slides.size());
            for (WebElement img : slides) {
                WebElement url = img.findElement(By.tagName("a"));
                imgUrls.append(processImgUrl(url.getAttribute("href")));
                imgUrls.append("\n");
            }
        }
        else {
            WebElement img = driver.findElement(By.id("webcontent_0_row2_0_imgLarge"));
            imgUrls.append(processImgUrl(img.getAttribute("src")));
        }
       // System.out.println(imgUrls.toString());

        return imgUrls.toString();
    }

     static String processImgUrl(String imgUrl){
       String fullSizeUrl =  StringUtils.substringBefore(imgUrl, ".jpg");
       fullSizeUrl = fullSizeUrl+".jpg";
        System.out.println(fullSizeUrl);

       return fullSizeUrl;
    }

    private static List<KeyCar> buildCars(WebDriver driver, WebElement carElement, int index, String url){
        String make = StringUtils.substringBetween(url,"&ma=", "&mo");
        String model = StringUtils.substringAfter(url, "&mo=");

        List<KeyCar> cars = new ArrayList<>();


        int carsCounter = 0;
        String openedCarElementIdFormat = "webcontent_0_row2_0_productDetailTabs_rptrApplicationSummary_rptrApplications_"+index+"_divApplication_";
        while (driver.findElements(By.id(openedCarElementIdFormat+carsCounter)).size()!=0){
            WebElement element = driver.findElement(By.id(openedCarElementIdFormat+carsCounter));
            WebElement yearElement = element.findElement(By.className("searchSummaryYmmName"));
            String carAttribute1 = null;
            String carAttribute2 = null;
            if (element.findElements(By.className("applicationAttributeValue")).size()>0){
                if (element.findElements(By.className("applicationAttributeValue")).size()>3){
                    System.out.println("problem with applicationAttributeValue");
                    System.out.println(yearElement.getText());
                    return null;
                }
                List<WebElement> attributeElements = element.findElements(By.className("applicationAttributeValue"));
                carAttribute1= attributeElements.get(0).getText();
                if (attributeElements.size()>1){
                    carAttribute2=attributeElements.get(1).getText();
                }
                if (attributeElements.size()>2){
                    carAttribute2=carAttribute2+"\n"+attributeElements.get(2).getText();
                }
            }
            String shockAttName = null;
            String shockAttValue = null;
            String shockAttName2 = null;
            String shockAttValue2 = null;
            List<WebElement> appBlocks = element.findElements(By.className("applicationBlock"));
            if (appBlocks.size()>0){
                if (appBlocks.size()>2){
                    System.out.println("more appblocks than expected");
                    return null;
                }
                if (appBlocks.get(0).findElements(By.tagName("span")).size()>3){
                    System.out.println("more spans in application block than expected");
                    return null;
                }
               try {
                   WebElement shockAttElem = appBlocks.get(0).findElement(By.className("applicationAttributeName"));
                   shockAttName = shockAttElem.getText();
               }
               catch (Exception e){
                   System.out.println("no shock attribute name");
               }
                try {
                    WebElement shockAttValueElem = appBlocks.get(0).findElement(By.className("applicationRequiredProducts"));
                    shockAttValue = shockAttValueElem.getText();
                }
                catch (Exception e){
                    System.out.println("no shock attribute value");
                }
                if (appBlocks.size()==2){
                    if (appBlocks.get(1).findElements(By.tagName("span")).size()>3){
                        System.out.println("more spans in application block than expected");
                        return null;
                    }
                    try {
                        WebElement shockAttElem = appBlocks.get(1).findElement(By.className("applicationAttributeName"));
                        shockAttName2 = shockAttElem.getText();
                    }
                    catch (Exception e){
                        System.out.println("no shock attribute name");
                    }
                    try {
                        WebElement shockAttValueElem = appBlocks.get(1).findElement(By.className("applicationRequiredProducts"));
                        shockAttValue2 = shockAttValueElem.getText();
                    }
                    catch (Exception e){
                        System.out.println("no shock attribute value");
                    }
                }







                /*WebElement shockAttElem = element.findElement(By.className("applicationAttributeName"));
                WebElement shockAttValueElem = element.findElement(By.className("applicationRequiredProducts"));
                shockAttName = shockAttElem.getText();
                shockAttValue = shockAttValueElem.getText();*/
            }

            String year = yearElement.getText();
            year = StringUtils.substringBefore(year," ");

            KeyCar car = new KeyCar();
            car.setCarModelYear(year);
            car.setCarMake(make);
            car.setCarModel(model);
            car.setCarAttribute1(carAttribute1);
            car.setCarAttribute2(carAttribute2);
            car.setShockAttributeName(shockAttName);
            car.setShockAttributeValue(shockAttValue);
            car.setShockAttributeName2(shockAttName2);
            car.setShockAttributeValue2(shockAttValue2);

            cars.add(car);

           // System.out.println(driver.findElement(By.id(openedCarElementIdFormat+carsCounter)).getText());
            carsCounter++;
        }

        return cars;
    }

    public static void openMainPage(WebDriver driver, String partNo){
        String url = String.format(MAIN_URL_FORMAT,partNo);
        driver.get(url);
        int counter = 0;
        while (driver.findElements(By.id("webcontent_0_row2_0_productDetailHeader_lblDescription")).size()==0){
            bad_sleep(100);
            counter++;
            if (counter==100){
                System.out.println("restart in openPage method");
                driver.close();
                driver = initDriver();
                counter=0;
                driver.get(url);
            }
        }
    }

    public static void reparseSecondAttribute(){
        List<String> partNos = getPartNos();
        Session session = KeyDao.getSession();
        WebDriver driver = initDriver();
        int partsCounter = 0;
        for (String partNo: partNos){
            if (partsCounter==100){
                System.out.println("restart in main method");
                driver.close();
                bad_sleep(60000);
                driver = initDriver();
                partsCounter=0;
            }
            openMainPage(driver,partNo);
            int counter = 0;
            List<KeyCar> cars = getCarElements(driver);
            while (cars == null && counter<5){
                counter++;
                cars = getCarElements(driver);
            }
            if (cars==null){
                logResult(partNo, TO_CHECK);
            }
            else {
                for (KeyCar car: cars){
                    car.setShockPartNo(partNo);
                }
                int resultCode = KeyDao.saveCars(session, cars);
                if (resultCode==1){
                    logResult(partNo, TO_CHECK);
                }
                else
                    logResult(partNo, WITH_RESULT);
            }
            partsCounter++;
        }
        driver.close();
        HibernateUtil.shutdown();
    }


    private static Map<String, KeyShock> getShockMap(Session session, List<String> partNos){
        List<KeyShock> shocks = KeyDao.getShocks(session);
        Map<String,KeyShock> shockMap = new HashMap<>();
        for (KeyShock shock: shocks){
            String partNo = shock.getPartNo();
            if (partNos.contains(partNo)){
                shockMap.put(partNo,shock);
            }
        }

        return shockMap;
    }

    private static Map<String, KeyAdditionalPart> getAdditionalPartMap(Session session, List<String> partNos){
        List<KeyAdditionalPart> shocks = KeyDao.getAdditionals(session);
        Map<String,KeyAdditionalPart> shockMap = new HashMap<>();
        for (KeyAdditionalPart shock: shocks){
            String partNo = shock.getPartNo();
            if (partNos.contains(partNo)){
                shockMap.put(partNo,shock);
            }
        }

        return shockMap;
    }

}
