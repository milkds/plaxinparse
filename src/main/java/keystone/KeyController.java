package keystone;

import org.hibernate.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import secondstep.HibernateUtil;

import java.util.*;

public class KeyController {


    public static void main(String[] args) {
        updateJeep();

      /*  String partLine = KeystoneUtil.getPartLine(driver, "10-255612");
        KeystoneUtil.processResult(partLine, "10-255612", driver, session);*/

       /* String partLine = KeystoneUtil.getPartLine(driver, "47432056");
        KeystoneUtil.processResult(partLine, "47432056", driver, session);*/

       /* String partLine = KeystoneUtil.getPartLine(driver, "24-239417");
        KeystoneUtil.processResult(partLine, "24-239417", driver, session);*/

    }

    private static void updateJeep(){
        Session session = KeyDao.getSession();
        List<KeyCar> cars = KeyDao.getCars(session);
     //   List<KeyShock> shocks = KeyDao.getShocks(session);
       // List<KeyAdditionalPart> adds = KeyDao.getAdditionals(session);

        HashSet<String> jeepAtts = new HashSet<>();

        for (KeyCar car: cars){
            if (car.getCarMake().equals("Jeep")){
                if (car.getShockAttributeValue2()!=null){
                    jeepAtts.add(car.getShockAttributeValue2());
                }
            }
        }
        for (String line: jeepAtts){
            System.out.println(line);
        }

        HibernateUtil.shutdown();
    }

    private static void bootParser(){
        List<String> parseParts = KeystoneUtil.getSearchParts();
        WebDriver driver = KeystoneUtil.initDriver();
        Session session = KeyDao.getSession();
        int counter = 0;
        for (String partNo: parseParts){
            String partLine = KeystoneUtil.getPartLine(driver, partNo);
            try {
                KeystoneUtil.processResult(partLine, partNo, driver,session);
                counter++;
                if (counter==100){
                    driver.close();
                    KeystoneUtil.bad_sleep(300000);
                    counter = 0;
                    driver=KeystoneUtil.initDriver();
                }
            }
            catch (Exception e){
                e.printStackTrace();
                KeystoneUtil.logResult(partNo, KeystoneUtil.TO_CHECK);
            }
        }
        driver.close();
        HibernateUtil.shutdown();
    }

    private static void checkImgs(){
        Session session = KeyDao.getSession();
      //  List<String> partNos = KeystoneUtil.getPartNos();

        List<KeyAdditionalPart> additionalParts = KeyDao.getAdditionals(session);
        System.out.println("we have additionalParts - " + additionalParts.size());

        for (KeyAdditionalPart shock: additionalParts){
            String imgUrl = shock.getImgUrls();
            if(imgUrl.length()==0){
                System.out.println(shock.getPartNo());
            }
        }
        HibernateUtil.shutdown();
    }

    private static void updatePartNos(){
        Session session = KeyDao.getSession();
        List<String> partNos = KeystoneUtil.getPartNos();
        List<KeyAdditionalPart> shocks = KeyDao.getAdditionals(session);
        System.out.println("we have shocks - " + shocks.size());
        for (KeyAdditionalPart add: shocks){
            String shockPartNo = add.getPartNo();
            if (shockPartNo.startsWith("b52")){
                String partNo = shockPartNo.replace("b52","");
                if (partNos.contains(partNo)){
                   add.setPartNo(partNo);
                  // KeyDao.updateShock(session,add);
                }
            }

          /*  if (partNos.contains(shockPartNo)) {
                KeyDao.deleteShock(session,shock);
            }*/
        }
        HibernateUtil.shutdown();

    }

    private static void reparseImgs(){
        Session session = KeyDao.getSession();
        List<String> partNos = KeystoneUtil.getPartNos();
        Map<String,KeyAdditionalPart> additionalPartMap = new HashMap<>();
        List<KeyAdditionalPart> additionalParts = KeyDao.getAdditionals(session);
        for (KeyAdditionalPart additionalPart: additionalParts){
            additionalPartMap.put(additionalPart.getPartNo(),additionalPart);
        }
        WebDriver driver = KeystoneUtil.initDriver();
        int counter = 0;
        for (String partNo: partNos){
            if (counter==100){
                driver.close();
                driver = KeystoneUtil.initDriver();
            }
            if (additionalPartMap.keySet().contains(partNo)){
                KeystoneUtil.openMainPage(driver,partNo);
                WebElement img = driver.findElement(By.id("webcontent_0_row2_0_imgLarge"));
                String imgUrl = img.getAttribute("src");
                if (imgUrl!=null&& imgUrl.length()>0){
                    imgUrl = KeystoneUtil.processImgUrl(imgUrl);
                    KeyAdditionalPart additionalPartToUpd = additionalPartMap.get(partNo);
                    additionalPartToUpd.setImgUrls(imgUrl);
                    KeyDao.updateShock(session,additionalPartToUpd);
                    KeystoneUtil.logResult(partNo,KeystoneUtil.WITH_RESULT);
                }
                counter++;
            }
            /*KeystoneUtil.openMainPage(driver,partNo);
            WebElement img = driver.findElement(By.id("webcontent_0_row2_0_imgLarge"));
            String imgUrl = img.getAttribute("src");
            if (imgUrl!=null&& imgUrl.length()>0){
                imgUrl = KeystoneUtil.processImgUrl(imgUrl);
                KeyShock shockToUpdate = shockMap.get(partNo);
                if (shockToUpdate!=null){
                    shockToUpdate.setImgUrl(imgUrl);
                    KeyDao.updateShock(session,shockToUpdate);
                    KeystoneUtil.logResult(partNo,KeystoneUtil.WITH_RESULT);
                }

            }*/

        }


        driver.close();
        HibernateUtil.shutdown();
    }

    private static void checkSecondAttribute(){
        List<KeyCar> cars = KeyDao.getCars();
        Set<String> shockParts = new TreeSet<>();
        for (KeyCar car : cars){
            String secondValue = car.getShockAttributeValue2();
            if (secondValue!=null){
                shockParts.add(car.getShockPartNo());
            }
        }
        System.out.println(shockParts.size());
        for (String shockPart : shockParts){
            System.out.println(shockPart);
        }
    }

    private static void deleteCarDupes(){
        Session session = KeyDao.getSession();
        List<String> partNos = KeystoneUtil.getPartNos();
        System.out.println(partNos.size());
        for (String partNo: partNos){
            List<KeyCar> cars=KeyDao.getCars(partNo, session);
            for (KeyCar car: cars){
                if (car.getId()<60544) {
                    KeyDao.deleteCar(session,car);
                }
            }
        }
        HibernateUtil.shutdown();
    }

}
