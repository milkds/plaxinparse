package turn14;

import org.openqa.selenium.WebDriver;

import java.util.List;

public class TurnController {
    public static void main(String[] args) {
        testMethod();

    }

    private static void testMethod(){
        WebDriver driver = TurnUtil.initDriver();
        TurnUtil.doLogin(driver);
        TurnUtil.getProductList(driver);
        //TurnUtil.switchPage(driver, 1);
        List<TurnPart> partList = TurnUtil.getPartListFromCurrentPage(driver);
        driver.close();
        TurnDao.saveParts(partList);
       // TurnUtil.switchPage(driver, 2);
    }
}
