import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SeleniumService {

    private static void getYearMakeModels(WebDriver driver, /*SearchData data,*/ int startYear, int finishYear, int[] startData) {
        WebElement yearDrop = driver.findElement(By.id("engineSelector-year"));
        Select yearSelect = new Select(yearDrop);
        List<WebElement> years = yearSelect.getOptions();
        if (startData.length > 0) {
            startYear = startData[0];
        }
        for (int i = startYear; i > finishYear; i--) {
            WebElement year = null;
            for (WebElement element : years) {
                if (element.getText().equals(i + "")) {
                    year = element;
                    break;
                }
            }
            yearSelect.selectByVisibleText(i + "");
            Controller.bad_sleep(500);
            WebElement makeDrop = driver.findElement(By.id("engineSelector-make"));
            Select makeSelect = new Select(makeDrop);
            List<WebElement> makes = makeSelect.getOptions();
            int makeID = 1;
            if (startData.length > 0) {
                makeID = startData[1];
                startData[1] = 1;
            }
            for (int j = makeID; j < makes.size(); j++) {
                WebElement make = makes.get(j);
                makeSelect.selectByIndex(j);
                Controller.bad_sleep(500);
                WebElement modelDrop = driver.findElement(By.id("engineSelector-model"));
                Select modelSelect = new Select(modelDrop);
                List<WebElement> models = modelSelect.getOptions();
                //save models here
                List<SearchData> modelDataForSave = new ArrayList<>();
                int modelID = 1;
                if (startData.length > 0) {
                    modelID = startData[2];
                    startData[2] = 1;
                }

                for (int f = modelID; f < models.size(); f++) {
                    SearchData dataWithModel = new SearchData();
                    dataWithModel.setYearText(year.getText());
                    dataWithModel.setYearValue(year.getAttribute("value"));
                    dataWithModel.setMakeText(make.getText());
                    dataWithModel.setMakeValue(make.getAttribute("value"));
                    dataWithModel.setModelText(models.get(f).getText());
                    dataWithModel.setModelValue(models.get(f).getAttribute("value"));
                    modelDataForSave.add(dataWithModel);
                }
                Service.saveYearMakeModels(modelDataForSave, year.getText(), make.getText());


                //iterate through models
                for (int k = modelID; k < models.size(); k++) {
                    WebElement model = models.get(k);
                    modelSelect.selectByIndex(k);
                    Controller.bad_sleep(500);
                    WebElement submodelDrop = driver.findElement(By.id("engineSelector-submodel"));
                    Select submodelSelect = new Select(submodelDrop);
                    List<WebElement> sumodels = submodelSelect.getOptions();
                    List<SearchData> submodelDataForSave = new ArrayList<>();
                    int submodelID = 1;
                    if (startData.length > 0) {
                        submodelID = startData[3];
                        startData[3] = 1;
                    }
                    for (int m = 1; m < sumodels.size(); m++) {
                        SearchData dataWithModel = new SearchData();
                        dataWithModel.setYearText(year.getText());
                        dataWithModel.setYearValue(year.getAttribute("value"));
                        dataWithModel.setMakeText(make.getText());
                        dataWithModel.setMakeValue(make.getAttribute("value"));
                        dataWithModel.setModelText(model.getText());
                        dataWithModel.setModelValue(model.getAttribute("value"));
                        dataWithModel.setSubmodelText(sumodels.get(m).getText());
                        dataWithModel.setSubmodelValue(sumodels.get(m).getAttribute("value"));
                        submodelDataForSave.add(dataWithModel);
                    }
                    Service.saveYearMakeModelsSub(submodelDataForSave, year.getText(), make.getText(), model.getText());

                    for (int l = submodelID; l < sumodels.size(); l++) {
                        submodelSelect.selectByIndex(l);
                        Controller.bad_sleep(500);
                        SearchData dataWithModel = new SearchData();
                        dataWithModel.setYearText(year.getText());
                        dataWithModel.setYearValue(year.getAttribute("value"));
                        dataWithModel.setMakeText(make.getText());
                        dataWithModel.setMakeValue(make.getAttribute("value"));
                        dataWithModel.setModelText(model.getText());
                        dataWithModel.setModelValue(model.getAttribute("value"));
                        dataWithModel.setSubmodelText(sumodels.get(l).getText());
                        dataWithModel.setSubmodelValue(sumodels.get(l).getAttribute("value"));
                        if (driver.findElements(By.id("fyvCartBtn")).size() != 0) {
                            Service.saveStandardDataForSearch(dataWithModel);
                        } else {
                            Controller.sleepUntilReady(driver, "inlineDrop-0");
                            if (driver.findElements(By.id("fyvCartBtn")).size() != 0) {
                                Service.saveStandardDataForSearch(dataWithModel);
                            } else {
                                String basicDataWithModel = dataWithModel.getYearText() + ";;" + dataWithModel.getYearValue() + ";;" + dataWithModel.getMakeText() + ";;"
                                        + dataWithModel.getMakeValue() + ";;" + dataWithModel.getModelText() + ";;" + dataWithModel.getModelValue() + ";;"
                                        + dataWithModel.getSubmodelText() + ";;" + dataWithModel.getSubmodelValue();
                                checkForAdditionalDrops(driver, 0, dataWithModel, startData, basicDataWithModel);
                            }
                            /*  try {

                             *//*  WebElement drop0 = driver.findElement(By.id("inlineDrop-0"));
                                Select drop0Select = new Select(drop0);
                                List<WebElement> drop0options = drop0Select.getOptions();
                                String dropName = drop0options.get(0).getText();
                                String basicDataWithModel = dataWithModel.getYearText()+";;"+dataWithModel.getYearValue()+";;"+dataWithModel.getMakeText()+";;"
                                        +dataWithModel.getMakeValue() +";;"+dataWithModel.getModelText()+";;"+dataWithModel.getModelValue()+";;"
                                        +dataWithModel.getSubmodelText()+";;"+dataWithModel.getSubmodelValue();
                                int drop0id = 1;
                                if (startData.length>0){
                                    drop0id = startData[4];
                                }
                                for (int m = drop0id; m < drop0options.size(); m++) {
                                    WebElement option = drop0options.get(m);
                                    String drop0line = basicDataWithModel+";;"+dropName+";;"+option.getText()+";;"+ option.getAttribute("value");
                                    drop0Select.selectByIndex(m);
                                    Controller.bad_sleep(2000);
                                    if (driver.findElements(By.id("fyvCartBtn")).size()!=0){
                                        Service.saveNonStandardDataForSearch(drop0line);
                                    }
                                    else throw new NoSuchElementException("");]*//*


                            }
                            catch (NoSuchElementException e){
                                System.out.println("we have non-standard situation - " +dataWithModel.getYearText()+" "+
                                        dataWithModel.getMakeText()+" "+ dataWithModel.getModelText()+" "+dataWithModel.getSubmodelText());
                                driver.close();
                                System.exit(0);
                            }*/
                            /**/
                        }
                    }
                }

            }

        }
        driver.close();
    }

    private static void checkForAdditionalDrops(WebDriver driver, int dropNumber, SearchData searchData, int[] startData, String searchString) {
        if (dropNumber > 3) {
            System.out.println("non-standard situation with" + searchData.toString());
        }
        //Controller.bad_sleep(2000);
        try {
            WebElement drop = driver.findElement(By.id("inlineDrop-" + dropNumber));
            Select dropSelect = new Select(drop);
            List<WebElement> dropOptions = dropSelect.getOptions();
            String dropName = dropOptions.get(0).getText();
            int dropID = 1;
            if (startData.length > 0) {
                if (startData[dropNumber + 4] > 0) {
                    dropID = startData[dropNumber + 4];
                    startData[dropNumber + 4] = 1;
                }
            }
            int tempDrop = dropNumber + 1;
            for (int m = dropID; m < dropOptions.size(); m++) {
                WebElement option = dropOptions.get(m);
                String dropLine = searchString + ";;" + dropName + ";;" + option.getText() + ";;" + option.getAttribute("value");
                dropSelect.selectByIndex(m);
                Controller.sleepUntilReady(driver, "inlineDrop-" + tempDrop);
                if (driver.findElements(By.id("fyvCartBtn")).size() != 0) {
                    Service.saveNonStandardDataForSearch(dropLine);
                } else {
                    checkForAdditionalDrops(driver, tempDrop, searchData, startData, dropLine);
                }
            }

        } catch (NoSuchElementException e) {
            System.out.println("we have non-standard situation - " + searchData.toString());
            driver.close();
            System.exit(0);
        }

    }

    public static void getDataforSearch(int startYear, int finishYear) {
        WebDriver driver = initDriver();
        getYearMakeModels(driver, startYear, finishYear, new int[0]);
    }

    public static WebDriver initDriver() {
       // System.setProperty("webdriver.chrome.driver", "F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://cart.bilsteinus.com");

        return driver;
    }

    public static void getDataforSearchFromStop(int startYear, int finishYear, String dataLine) {
        int[] startData = getIndexesForStart(dataLine);
        WebDriver driver = initDriver();
        getYearMakeModels(driver, startYear, finishYear, startData);
    }

    private static int[] getIndexesForStart(String dataLine) {
        String[] split = dataLine.split(";;");
        int[] result = new int[8];
        result[0] = Integer.parseInt(split[0]);
        WebDriver driver = initDriver();

        WebElement yearDrop = driver.findElement(By.id("engineSelector-year"));
        Select yearSelect = new Select(yearDrop);
        yearSelect.selectByVisibleText(split[0]);
        Controller.bad_sleep(1000);

        WebElement makeDrop = driver.findElement(By.id("engineSelector-make"));
        Select makeSelect = new Select(makeDrop);
        List<WebElement> makes = makeSelect.getOptions();
        for (int i = 1; i < makes.size(); i++) {
            if (makes.get(i).getText().equals(split[2])) {
                result[1] = i;
                break;
            }
        }
        makeSelect.selectByIndex(result[1]);
        Controller.bad_sleep(1000);

        WebElement modelDrop = driver.findElement(By.id("engineSelector-model"));
        Select modelSelect = new Select(modelDrop);
        List<WebElement> models = modelSelect.getOptions();
        for (int i = 1; i < models.size(); i++) {
            if (models.get(i).getText().equals(split[4])) {
                result[2] = i;
                break;
            }
        }
        modelSelect.selectByIndex(result[2]);
        Controller.bad_sleep(1000);

        WebElement submodelDrop = driver.findElement(By.id("engineSelector-submodel"));
        Select submodelSelect = new Select(submodelDrop);
        List<WebElement> submodels = submodelSelect.getOptions();
        for (int i = 1; i < submodels.size(); i++) {
            if (submodels.get(i).getText().equals(split[6])) {
                result[3] = i;
                break;
            }
        }
        submodelSelect.selectByIndex(result[3]);
        Controller.bad_sleep(1000);
        if (driver.findElements(By.id("fyvCartBtn")).size() == 0) {
            getAdditionalDropInfoOnStop(result, 0, driver, dataLine);
        }
        driver.close();

        if (result[0] <= 0 || result[1] <= 0 || result[2] <= 0 || result[3] <= 0) {
            System.out.println("no correct values for start");
            System.exit(0);
        }


        return result;
    }

    private static void getAdditionalDropInfoOnStop(int[] stopIndexes, int dropNumber, WebDriver driver, String stopLine) {
        String[] split = stopLine.split(";;");
        try {
            if (dropNumber > 3) {
                throw new NoSuchElementException("");
            }
            Controller.bad_sleep(2000);
            WebElement drop = driver.findElement(By.id("inlineDrop-" + dropNumber));
            Select dropSelect = new Select(drop);
            List<WebElement> dropOptions = dropSelect.getOptions();
            for (int i = 1; i < dropOptions.size(); i++) {
                if (dropOptions.get(i).getText().equals(split[dropNumber * 3 + 9])) {
                    stopIndexes[dropNumber + 4] = i;
                    break;
                }
            }
            dropSelect.selectByIndex(stopIndexes[dropNumber + 4]);
            Controller.bad_sleep(1000);

            if (driver.findElements(By.id("fyvCartBtn")).size() == 0) {
                dropNumber = dropNumber + 1;
                getAdditionalDropInfoOnStop(stopIndexes, dropNumber, driver, stopLine);
            }

        } catch (NoSuchElementException e) {
            System.out.println("no correct values for start");
            driver.close();
            System.exit(0);
        }

    }

    public static void getSubs(String year, String make, String model, WebDriver driver) {
       // WebDriver driver = initDriver();

        WebElement yearDrop = driver.findElement(By.id("engineSelector-year"));
        Select yearSelect = new Select(yearDrop);

        List<WebElement> years = yearSelect.getOptions();
        WebElement yearWeb = null;
        for (WebElement y : years) {
            if (y.getText().equals(year)) {
                yearWeb = y;
                break;
            }
        }

        yearSelect.selectByVisibleText(year);
        Controller.bad_sleep(1000);

        WebElement makeDrop = driver.findElement(By.id("engineSelector-make"));
        Select makeSelect = new Select(makeDrop);
        List<WebElement> makes = makeSelect.getOptions();
        WebElement makeWeb = null;
        for (WebElement mk : makes) {
            if (mk.getText().equals(make)) {
                makeWeb = mk;
                break;
            }
        }
        makeSelect.selectByVisibleText(make);
        Controller.bad_sleep(1000);

        WebElement modelDrop = driver.findElement(By.id("engineSelector-model"));
        Select modelSelect = new Select(modelDrop);

        List<WebElement> models = modelSelect.getOptions();
        WebElement modelWeb = null;
        for (WebElement mo : models) {
            if (mo.getText().equals(model)) {
                modelWeb = mo;
                break;
            }
        }
        modelSelect.selectByVisibleText(model);
        Controller.bad_sleep(1000);

        WebElement submodelDrop = driver.findElement(By.id("engineSelector-submodel"));
        Select submodelSelect = new Select(submodelDrop);
        List<WebElement> submodels = submodelSelect.getOptions();
        List<SearchData> submodelDataForSave = new ArrayList<>();
        for (int m = 1; m < submodels.size(); m++) {
            SearchData dataWithModel = new SearchData();
            dataWithModel.setYearText(yearWeb.getText());
            dataWithModel.setYearValue(yearWeb.getAttribute("value"));
            dataWithModel.setMakeText(makeWeb.getText());
            dataWithModel.setMakeValue(makeWeb.getAttribute("value"));
            dataWithModel.setModelText(modelWeb.getText());
            dataWithModel.setModelValue(modelWeb.getAttribute("value"));
            dataWithModel.setSubmodelText(submodels.get(m).getText());
            dataWithModel.setSubmodelValue(submodels.get(m).getAttribute("value"));
            submodelDataForSave.add(dataWithModel);
        }
        Service.saveYearMakeModelsSub(submodelDataForSave, year, make, model);
        for (int i = 1; i < submodels.size(); i++) {
            SearchData dataWithModel = submodelDataForSave.get(i - 1);
            submodelSelect.selectByIndex(i);
            Controller.bad_sleep(500);
            if (driver.findElements(By.id("fyvCartBtn")).size() != 0) {
                Service.saveStandardDataForSearch(dataWithModel);
            } else {
                Controller.sleepUntilReady(driver, "inlineDrop-0");
                if (driver.findElements(By.id("fyvCartBtn")).size() != 0) {
                    Service.saveStandardDataForSearch(dataWithModel);
                } else {
                    String basicDataWithModel = dataWithModel.getYearText() + ";;" + dataWithModel.getYearValue() + ";;" + dataWithModel.getMakeText() + ";;"
                            + dataWithModel.getMakeValue() + ";;" + dataWithModel.getModelText() + ";;" + dataWithModel.getModelValue() + ";;"
                            + dataWithModel.getSubmodelText() + ";;" + dataWithModel.getSubmodelValue();
                    checkForAdditionalDrops(driver, 0, dataWithModel, new int[0], basicDataWithModel);
                }
            }

        }
       // driver.close();
    }
    public static void getModelsAndSubs(String year, String make, WebDriver driver) {
       // WebDriver driver = initDriver();

        WebElement yearDrop = driver.findElement(By.id("engineSelector-year"));
        Select yearSelect = new Select(yearDrop);

        List<WebElement> years = yearSelect.getOptions();
        WebElement yearWeb = null;
        for (WebElement y : years) {
            if (y.getText().equals(year)) {
                yearWeb = y;
                break;
            }
        }

        yearSelect.selectByVisibleText(year);
        Controller.bad_sleep(1000);

        WebElement makeDrop = driver.findElement(By.id("engineSelector-make"));
        Select makeSelect = new Select(makeDrop);
        List<WebElement> makes = makeSelect.getOptions();
        WebElement makeWeb = null;
        for (WebElement mk : makes) {
            if (mk.getText().equals(make)) {
                makeWeb = mk;
                break;
            }
        }
        makeSelect.selectByVisibleText(make);
        Controller.bad_sleep(1000);

        WebElement modelDrop = driver.findElement(By.id("engineSelector-model"));
        Select modelSelect = new Select(modelDrop);
        List<WebElement> models = modelSelect.getOptions();

        List<SearchData> modelDataForSave = new ArrayList<>();
        for (int f = 1; f < models.size(); f++) {
            SearchData dataWithModel = new SearchData();
            dataWithModel.setYearText(yearWeb.getText());
            dataWithModel.setYearValue(yearWeb.getAttribute("value"));
            dataWithModel.setMakeText(makeWeb.getText());
            dataWithModel.setMakeValue(makeWeb.getAttribute("value"));
            dataWithModel.setModelText(models.get(f).getText());
            dataWithModel.setModelValue(models.get(f).getAttribute("value"));
            modelDataForSave.add(dataWithModel);
        }
        Service.saveYearMakeModels(modelDataForSave, year, make);

        for (int i = 1; i < models.size(); i++) {
            modelSelect.selectByIndex(i);
            Controller.bad_sleep(1000);
            WebElement submodelDrop = driver.findElement(By.id("engineSelector-submodel"));
            Select submodelSelect = new Select(submodelDrop);
            List<WebElement> submodels = submodelSelect.getOptions();
            List<SearchData> submodelDataForSave = new ArrayList<>();
            for (int m = 1; m < submodels.size(); m++) {
                SearchData dataWithModel = new SearchData();
                dataWithModel.setYearText(yearWeb.getText());
                dataWithModel.setYearValue(yearWeb.getAttribute("value"));
                dataWithModel.setMakeText(makeWeb.getText());
                dataWithModel.setMakeValue(makeWeb.getAttribute("value"));
                dataWithModel.setModelText(models.get(i).getText());
                dataWithModel.setModelValue(models.get(i).getAttribute("value"));
                dataWithModel.setSubmodelText(submodels.get(m).getText());
                dataWithModel.setSubmodelValue(submodels.get(m).getAttribute("value"));
                submodelDataForSave.add(dataWithModel);
            }
            Service.saveYearMakeModelsSub(submodelDataForSave, year, make, models.get(i).getText());
            for (int b = 1; b < submodels.size(); b++) {
                SearchData dataWithModel = submodelDataForSave.get(b - 1);
                submodelSelect.selectByIndex(b);
                Controller.bad_sleep(500);
                if (driver.findElements(By.id("fyvCartBtn")).size() != 0) {
                    Service.saveStandardDataForSearch(dataWithModel);
                } else {
                    Controller.sleepUntilReady(driver, "inlineDrop-0");
                    if (driver.findElements(By.id("fyvCartBtn")).size() != 0) {
                        Service.saveStandardDataForSearch(dataWithModel);
                    } else {
                        String basicDataWithModel = dataWithModel.getYearText() + ";;" + dataWithModel.getYearValue() + ";;" + dataWithModel.getMakeText() + ";;"
                                + dataWithModel.getMakeValue() + ";;" + dataWithModel.getModelText() + ";;" + dataWithModel.getModelValue() + ";;"
                                + dataWithModel.getSubmodelText() + ";;" + dataWithModel.getSubmodelValue();
                        checkForAdditionalDrops(driver, 0, dataWithModel, new int[0], basicDataWithModel);
                    }
                }

            }
        }


       // driver.close();
    }

}