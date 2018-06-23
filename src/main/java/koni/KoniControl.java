package koni;

import firststep.Controller;
import firststep.SeleniumService;
import koni.entities.KoniCar;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KoniControl {
    public static void main(String[] args) {
        testMethod();
    }


    private static void testMethod(){
        WebDriver driver = KoniService.initDriver();
        driver.switchTo().frame("Iframe1");
        WebElement makes = driver.findElement(By.cssSelector("select[name='make']"));
        Select makeSelect = new Select(makes);
        List<WebElement> makeOptions = waitForSelect(makeSelect);

        makeSelect.selectByIndex(1);
        WebElement models = driver.findElement(By.cssSelector("select[name='model']"));
        Select modelSelect = new Select(models);

        List<WebElement> modelOptions = waitForSelect(modelSelect);
        modelSelect.selectByIndex(1);

        WebElement types = driver.findElement(By.cssSelector("select[name='type']"));
        Select typeSelect = new Select(types);
        List<WebElement> typeOptions = waitForSelect(typeSelect);
        typeSelect.selectByVisibleText("All types");

        List<KoniCar> cars = KoniService.buildCars(driver);

        driver.close();
    }


    private static void switchFrameSmpl(){
      /*  WebDriver driver = KoniService.initDriver();
        driver.switchTo().frame("Iframe1");
        WebElement selects = driver.findElement(By.cssSelector("select[name='make']"));
        Select makeSelect = new Select(selects);
        KoniService.bad_sleep(2000);
        List<WebElement> makeEls = makeSelect.getOptions();
        for (int i = 1; i < makeEls.size(); i++) {
            makeSelect.selectByIndex(i);
            WebElement models = driver.findElement(By.cssSelector("select[name='model']"));
            Select modelSelect = new Select(models);
            KoniService.bad_sleep(2000);
            List<WebElement> mdls = modelSelect.getOptions();
            List<String> makeStrings = new ArrayList<>();
            String make = makeEls.get(i).getText();
            for (int j = 1; j < mdls.size(); j++) {
                makeStrings.add(make + "---" + mdls.get(j).getText());
            }
            KoniService.saveDataToFile(makeStrings);
        }
        driver.close();*/
    }

    private static List<WebElement> waitForSelect(Select select){
        List<WebElement> options = select.getOptions();
        while (options.size()<2){
            KoniService.bad_sleep(50);
            options = select.getOptions();
        }

        return options;
    }
}
