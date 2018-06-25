package secondstep;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

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

public class WebHandler {
    private static final String MAIN_URL_FORMAT = "https://cart.bilsteinus.com/results?yearid=%s&makeid=%s&modelid=%s&submodelid=%s";
    private static final String DATA_FOR_SEARCH = "src\\main\\resources\\second_step_files\\allinfo";
    private static final String DATA_FOR_REPARSE = "src\\main\\resources\\second_step_files\\reparse";
    private static final String NO_RESULT = "src\\main\\resources\\second_step_files\\noResult";
    private static final String WITH_RESULT = "src\\main\\resources\\second_step_files\\withResult";
    private static final String TO_CHECK = "src\\main\\resources\\second_step_files\\resultsToCheck";


    public static void main(String[] args) throws IOException {
        divideYears();
    }

    private static void divideYears(){
        Session session = CarDao.getSession();
        List<Car> cars = CarDao.getCarsOrdered(session);

        Car prevCar = null;
        String yearStart = null;
        String yearFinish = null;
        List<Car> equalCars = new ArrayList<>();
        int counter = 0;
        for (Car car: cars){
            counter++;
            if (counter==100){
                System.out.println("counter 100");
                System.exit(0);
            }
            //implement cycle start
            if  (prevCar==null){
                prevCar = car;
                yearStart = car.getModelYear();
                car.setYearStart(yearStart);
                equalCars.add(car);
                continue;
            }
            if (carsEqual(car,prevCar,session)){
                car.setYearStart(yearStart);
                equalCars.add(car);
                prevCar = car;
            }
            else {
                yearFinish=prevCar.getModelYear();
                for (Car tempCar: equalCars){
                    tempCar.setYearFinish(yearFinish);
                    System.out.println(tempCar);
                 //   CarDao.updateCar(session,tempCar);
                }
                System.out.println("-----------------Car block ----------------------");
                System.out.println();
                System.out.println();
                System.out.println();
                prevCar = car;
                yearStart = car.getModelYear();
                car.setYearStart(yearStart);
                equalCars=new ArrayList<>();
                equalCars.add(car);
            }
        }


        HibernateUtil.shutdown();
    }

    private static boolean carsEqual(Car car, Car prevCar, Session session) {
        if (!car.getMake().equals(prevCar.getMake()) ){
            return false;
        }
        if (!car.getModel().equals(prevCar.getModel())){
            return false;
        }
        if (!car.getSubmodel().equals(prevCar.getSubmodel())){
            return false;
        }
        String prevDrive = prevCar.getDrive();

        //drives very often null - checks needed
        String curDrive = car.getDrive();
        if (prevDrive==null&&curDrive!=null){
            return false;
        }
        if (prevDrive!=null&&curDrive==null){
            return false;
        }
        if (curDrive!=null){
            if (!curDrive.equals(prevDrive)){
                return false;
            }
        }
        //if cars are not in consecutive modelYear row - they are considered as different cars.
        int prevYear = (Integer.parseInt(prevCar.getModelYear()));
        int curYear = (Integer.parseInt(car.getModelYear()));
        if (curYear-prevYear!=1){
            return false;
        }
        return shocksEqual(car, prevCar, session);
    }

    private static boolean shocksEqual(Car car, Car prevCar, Session session) {
        List<ShockAbsorber> curShocks = CarDao.getAbsorbersByCarID(session,car.getId());
        List<ShockAbsorber> prevShocks = CarDao.getAbsorbersByCarID(session,prevCar.getId());
        //if cars have different number of shocks - we consider them as different cars
        if (curShocks.size()!=prevShocks.size()){
            return false;
        }
        List<String> currentParts = new ArrayList<>();
        List<String> prevParts = new ArrayList<>();
        for (int i = 0; i < curShocks.size(); i++) {
            currentParts.add(curShocks.get(i).getPartNo());
            prevParts.add(prevShocks.get(i).getPartNo());
        }
        //if cars do have different shocks - we consider them as different cars
        for (String curPart: currentParts){
            if (!prevParts.contains(curPart)){
                return false;
            }
        }
        return true;
    }

    private static void addCarNotes(){
        Session session = CarDao.getSession();
        List<Car> cars = CarDao.getCars(session);
        for (Car car: cars){
            String carNotes = car.getCarFullName();
            StringBuilder sb = new StringBuilder();
            sb.append(car.getModelYear());
            sb.append(" ");
            sb.append(car.getMake());
            sb.append(" ");
            sb.append(car.getModel());
            sb.append(" ");
            sb.append(car.getSubmodel());
            String stData = sb.toString();
            carNotes = carNotes.replace(stData,"");
            if (carNotes.startsWith(" ")&&carNotes.length()>1){
                carNotes = carNotes.substring(1);
                car.setCarNotes(carNotes);
                CarDao.saveCar(session, car);
            }
        }

    }

    private static void updJeep(){
        Session session = CarDao.getSession();
        List<ShockAbsorber> absorbers = CarDao.getAbsorbers(session);
        List<Car> cars = CarDao.getCars(session);
        Map<Integer,Car> carMap = new HashMap<>();
        for (Car car : cars){
            carMap.put(car.getId(),car);
        }
        for (ShockAbsorber absorber: absorbers){
            Car car = carMap.get(absorber.getCarID());
            if (car.getMake().equals("Jeep")){

            }
        }
    }

    private static void updateShockPosition(){
        Session session = CarDao.getSession();
        List<ShockAbsorber> absorbers = CarDao.getAbsorbers(session);
        for (ShockAbsorber absorber: absorbers){
            String position = absorber.getPosition();
            switch(position){
                case "Front":{
                    absorber.setPosition("F");
                    CarDao.updateShock(session, absorber);
                    break;
                }
                case "Rear":{
                    absorber.setPosition("R");
                    CarDao.updateShock(session, absorber);
                    break;
                }
                case "Front Right":{
                    absorber.setPosition("FR");
                    CarDao.updateShock(session, absorber);
                    break;
                }
                case "Front Left":{
                    absorber.setPosition("FL");
                    CarDao.updateShock(session, absorber);
                    break;
                }
                case "Rear Right":{
                    absorber.setPosition("RR");
                    CarDao.updateShock(session, absorber);
                    break;
                }
                case "Rear Left":{
                    absorber.setPosition("RL");
                    CarDao.updateShock(session, absorber);
                    break;
                }
                default://do nothing
            }
        }
    }

    private static void updateNoInfoCars(){
        List<String> allLines = new ArrayList<>();
        try {
            allLines = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> carMap = new HashMap<>();

        //processing lines from file
        for (String carLine: allLines){
            String split[] = carLine.split(";;");
            String fullCar = split [0]+" "+split[2]+" "+split[4]+" "+split[6];
            if (split.length>8){
                fullCar = fullCar+" "+split[9];
                if (split.length>11){
                    fullCar = fullCar+" "+split[12];
                    if (split.length>14){
                        fullCar = fullCar+" "+split[15];
                        if (split.length>17){
                            fullCar = fullCar+" "+split[18];
                        }
                    }
                }
            }
            carMap.put(fullCar,carLine);
        }
        Session session = CarDao.getSession();
        List<CarNoInfo> carNoInfos = CarDao.getCarsNoInfo(session);
        for (CarNoInfo carNoInfo: carNoInfos){
            if (carNoInfo.getModelYear()==null||carNoInfo.getModelYear().length()==0){
                String dataLine = carMap.get(carNoInfo.getCarFullName());
                carNoInfo = buildCarFromParseLine(dataLine,carNoInfo);
                CarDao.updateCarNoInfo(session,carNoInfo);
                System.out.println("updated: "+carNoInfo);
            }
        }

        System.out.println("thats all");
        System.exit(0);
    }

    private static void fillInfo(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Car> cars = CarDao.getCars(session);
        List<CarNoInfo> carsNoInfo = CarDao.getCarsNoInfo(session);
        Map<String, Car> carMap = new HashMap<>();
        for (Car car: cars){
            carMap.put(car.getCarFullName(), car);
        }
        System.out.println("map filled");
        for (CarNoInfo carNoInfo: carsNoInfo){
            try {
                Car car = carMap.get(carNoInfo.getCarFullName());

                carNoInfo.setModelYear(car.getModelYear());
                carNoInfo.setMake(car.getMake());
                carNoInfo.setModel(car.getModel());
                carNoInfo.setSubmodel(car.getSubmodel());
                carNoInfo.setBodyManufacturer(car.getBodyManufacturer());
                carNoInfo.setDrive(car.getDrive());
                carNoInfo.setBody(car.getBody());
                carNoInfo.setSuspension(car.getSuspension());
                carNoInfo.setEngine(car.getEngine());
                carNoInfo.setDoors(car.getDoors());
                carNoInfo.setTransmission(car.getTransmission());

                CarDao.updateCarNoInfo(session, carNoInfo);
            }
            catch (NullPointerException e){
                System.out.println(carNoInfo);
                continue;
            }


        }

        System.out.println("thats all");
        System.exit(0);
    }

    private static CarNoInfo buildCarFromParseLine(String line, CarNoInfo carNoInfo){
        String split[] = line.split(";;");
        carNoInfo.setModelYear(split[0]);
        carNoInfo.setMake(split[2]);
        carNoInfo.setModel(split[4]);
        carNoInfo.setSubmodel(split[6]);
        if (split.length>8){
            setAdditionalFields(carNoInfo, split);
        }
        return carNoInfo;
    }

    private static void updCarsInDB(){
        List<String> allLines = new ArrayList<>();
        try {
            allLines = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> carMap = new HashMap<>();

        //processing lines from file
        for (String carLine: allLines){
            String split[] = carLine.split(";;");
            String fullCar = split [0]+" "+split[2]+" "+split[4]+" "+split[6];
            if (split.length>8){
                fullCar = fullCar+" "+split[9];
                if (split.length>11){
                    fullCar = fullCar+" "+split[12];
                    if (split.length>14){
                        fullCar = fullCar+" "+split[15];
                        if (split.length>17){
                            fullCar = fullCar+" "+split[18];
                        }
                    }
                }
            }
            carMap.put(fullCar,carLine);
        }
        Session session = CarDao.getSession();
        List<Car> cars = CarDao.getCars(session);
        int counter = 0;
        for (Car car : cars){
            String carDBname = car.getCarFullName();
            String carLine = carMap.get(carDBname);
            String split[] = carLine.split(";;");
            car.setModelYear(split[0]);
            car.setMake(split[2]);
            car.setModel(split[4]);
            car.setSubmodel(split[6]);
            if (split.length>8){
                refreshFields(car);
                setAdditionalFields(car, split);
            }
            Transaction transaction = session.beginTransaction();
            session.update(car);
            transaction.commit();
            counter++;
        }
        HibernateUtil.shutdown();

        System.out.println("totally updated cars - "+counter);


    }

    private static void setAdditionalFields(Car car, String[] split) {
        String attribute = split [8];
        String attributeValue = split[9];
        setCarProperty(car,attribute,attributeValue);
        if (split.length>11){
             attribute = split [11];
             attributeValue = split[12];
             setCarProperty(car,attribute,attributeValue);
            if (split.length>14){
                attribute = split [14];
                attributeValue = split[15];
                setCarProperty(car,attribute,attributeValue);
                if (split.length>17){
                    attribute = split [17];
                    attributeValue = split[18];
                    setCarProperty(car,attribute,attributeValue);
                }
            }
        }
    }
    private static void setAdditionalFields(CarNoInfo car, String[] split) {
        String attribute = split [8];
        String attributeValue = split[9];
        setCarProperty(car,attribute,attributeValue);
        if (split.length>11){
            attribute = split [11];
            attributeValue = split[12];
            setCarProperty(car,attribute,attributeValue);
            if (split.length>14){
                attribute = split [14];
                attributeValue = split[15];
                setCarProperty(car,attribute,attributeValue);
                if (split.length>17){
                    attribute = split [17];
                    attributeValue = split[18];
                    setCarProperty(car,attribute,attributeValue);
                }
            }
        }
    }

    private static void setCarProperty(Car car, String attribute, String atributeValue){
        switch (attribute){
            case "BodyManufacturer": car.setBodyManufacturer(atributeValue); break;
            case "Drive": car.setDrive(atributeValue); break;
            case "Body": car.setBody(atributeValue); break;
            case "Suspension": car.setSuspension(atributeValue); break;
            case "Engine": car.setEngine(atributeValue); break;
            case "Doors": car.setDoors(atributeValue); break;
            case "Transmission": car.setTransmission(atributeValue);
        }
    }
    private static void setCarProperty(CarNoInfo car, String attribute, String atributeValue){
        switch (attribute){
            case "BodyManufacturer": car.setBodyManufacturer(atributeValue); break;
            case "Drive": car.setDrive(atributeValue); break;
            case "Body": car.setBody(atributeValue); break;
            case "Suspension": car.setSuspension(atributeValue); break;
            case "Engine": car.setEngine(atributeValue); break;
            case "Doors": car.setDoors(atributeValue); break;
            case "Transmission": car.setTransmission(atributeValue);
        }
    }

    private static void refreshFields(Car car){
        car.setBodyManufacturer(null);
        car.setDrive(null);
        car.setBody(null);
        car.setSuspension(null);
        car.setEngine(null);
        car.setDoors(null);
        car.setTransmission(null);
    }
    private static void updateCarsInDB(){
        List<String> result = new ArrayList<>();
        try {
            result = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> carsInNote = new ArrayList<>();
        for(String dataLine : result){
            String split[] = dataLine.split(";;");
            String fullCar = split [0]+" "+split[2]+" "+split[4]+" "+split[6];
            if (split.length>8){
                fullCar = fullCar+" "+split[9];
                if (split.length>11){
                    fullCar = fullCar+" "+split[12];
                    if (split.length>14){
                        fullCar = fullCar+" "+split[15];
                        if (split.length>17){
                            fullCar = fullCar+" "+split[18];
                        }
                    }
                }
            }
            carsInNote.add(fullCar);
            /*if (fullCar.startsWith("2014 Volkswagen")) {
                System.out.println(fullCar);
            }*/
        }
        List<Car> cars =   CarDao.getCars();
       /* List<String> carStrings = new ArrayList<>();
        for (Car car: cars){
            carStrings.add(car.getCarFullName());
         //   System.out.println(car.getCarFullName());
        }*/
       /* List<String> dupes = new ArrayList<>();
        for (String car : carStrings){
            if (dupes.contains(car)){
                System.out.println(car);
            }
            else dupes.add(car);
        }*/
        int counter = 0;
        List<String> carToSave = new ArrayList<>();
        for (Car car: cars){
            if (!carsInNote.contains(car.getCarFullName())){
                //carToSave.add(car.getCarFullName()+";;"+car.getId());
                System.out.println(car.getCarFullName());
                counter++;
            }

        }

       /* try(FileWriter fw = new FileWriter(TO_CHECK, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String line: carToSave) {
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*for (String fullcar: carsInNote){
            if (carStrings.contains(fullcar)){
                counter++;
            }
        }*/
        System.out.println("Counter size is --- "+counter);
        }

    private static String buildUrl(String dataLine){
        String result = "";
        String split[] = dataLine.split(";;");
        if (split.length<8){
            throw new IllegalArgumentException();
        }
        result = String.format(MAIN_URL_FORMAT,split[1], split[3], split[5], split[7]);
        for (int i = 8; i < split.length; i=i+3) {
            String attribute;
            if (split[i].equals("BodyManufacturer")){
                attribute="BM";
            }
            else {
                attribute = split[i].substring(0,2);
            }

            result = result + "&"+attribute+"="+split[i+2];
        }
        return result;
    }

    private static List<String> getDatalines(){
        List<String> result = new ArrayList<>();
        List<String> noResult;
        List<String> withResult;
        List<String> withProblems;
        try {
            result = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
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

    private static void logCheckedData(String checkedData, boolean hasResults){
        String path = "";
        if (hasResults) path=WITH_RESULT;
        else  path = NO_RESULT;
        try(FileWriter fw = new FileWriter(path, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(checkedData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String split[] = checkedData.split(";;");
        String fullCar = split [0]+"---"+split[2]+"---"+split[4]+"---"+split[6];
        if (split.length>8){
            fullCar = fullCar+"---"+split[9];
            if (split.length>11){
                fullCar = fullCar+"---"+split[12];
                if (split.length>14){
                    fullCar = fullCar+"---"+split[15];
                }
            }
        }

        System.out.println("processed "+fullCar);
    }

    private static ShockAbsorber buildShock (WebElement element){
        ShockAbsorber result = new ShockAbsorber();
        String dataDump = element.getText();
      //  System.out.println(dataDump);
        /*String dataDump = "B8 5100 - Shock Absorber\n" +
                "Part Number: 24-253161\n" +
                "Series: B8 5100\n" +
                "Position: Front\n" +
                "Collapsed Length (IN): 14.06\n" +
                "Extended Length (IN): 18.43\n" +
                "Body Diameter: 46mm\n" +
                "Notes: -For Front Lifted Height: 0-1.5";*/
        //getting type and series
        String dataLines[]= dataDump.split("\n");

        String seriesType [] = dataLines[0].split(" - ");
        if (seriesType.length==2){
            result.setProductType(seriesType[1]);
        }
        else if (seriesType.length==1){
            if (seriesType[0].contains("Steering Damper")){
                result.setProductType("Steering Damper");
            }
            else {
                System.out.println("Who are you ? "+seriesType[0]);
                System.exit(0);
            }
        }
        else{
            System.out.println("seriesType line gone wild");
            System.exit(0);
        }
        result.setSeries(seriesType[0]);
        /*for (String s : dataLines){
            System.out.println(s);
        }*/

        //getting other parameters
        List<WebElement> searchResultLines = element.findElements(By.tagName("p"));

        //if we have no notes attribute - we need to add +1 to counter
        if  (!dataDump.contains("Notes")){
            searchResultLines.add(element);
        }
        //System.out.println(searchResultLines.size());
        for (int i = 1; i < searchResultLines.size(); i++) {
          //  System.out.println(searchResultLines.get(i).getText());
          //  System.out.println(i+" - "+dataLines[i]);
            String data[] = dataLines[i].split(": ");
          //  System.out.println(data.length);
            String attribute = data[0];
            switch(attribute){
                case "Part Number": result.setPartNo(data[1]); break;
                case "Series": /*do nothing*/ break;
                case "Position": result.setPosition(data[1]); break;
                case "Collapsed Length (IN)": result.setCollapsedL(data[1]); break;
                case "Extended Length (IN)": result.setExtendedL(data[1]); break;
                case "Body Diameter": result.setBodyDiameter(data[1]); break;
                case "Chassis Manufacturer": result.setChassisManufacturer(data[1]); break;
                case "Chassis Model": result.setChassisModel(data[1]); break;
                case "Chassis Model - Extended": result.setChassisModelExtended(data[1]); break;
                case "Chassis Class": result.setChassisClass(data[1]); break;
                case "Chassis Year Range": result.setChassisYearRange(data[1]); break;
                case "Application Note 1": result.setChassisApplicationNote1(data[1]); break;
                case "Application Note 2": {
                    String otherNotes = result.getOtherNotes();
                    otherNotes = otherNotes+dataLines[i]+";;";
                    result.setOtherNotes(otherNotes);
                } break;
                case "Outer Housing Diameter": {
                    String otherNotes = result.getOtherNotes();
                    otherNotes = otherNotes+dataLines[i]+";;";
                    result.setOtherNotes(otherNotes);
                } break;

                     case "Compression @0.52m/s": {
                    String otherNotes = result.getOtherNotes();
                    otherNotes = otherNotes+dataLines[i]+";;";
                    result.setOtherNotes(otherNotes);
                } break;
                case "Rebound @0.52m/s": {
                    String otherNotes = result.getOtherNotes();
                    otherNotes = otherNotes+dataLines[i]+";;";
                    result.setOtherNotes(otherNotes);
                } break;
                case "Compression @0.26m/s": {
                    String otherNotes = result.getOtherNotes();
                    otherNotes = otherNotes+dataLines[i]+";;";
                    result.setOtherNotes(otherNotes);
                } break;
                case "Rebound @0.26m/s": {
                    String otherNotes = result.getOtherNotes();
                    otherNotes = otherNotes+dataLines[i]+";;";
                    result.setOtherNotes(otherNotes);
                } break;

                    case "Suspension Type": {
                    String otherNotes = result.getOtherNotes();
                    otherNotes = otherNotes+dataLines[i]+";;";
                    result.setOtherNotes(otherNotes);
                } break;
                        case "Gross Vehicle Weight": {
                    String otherNotes = result.getOtherNotes();
                    otherNotes = otherNotes+dataLines[i]+";;";
                    result.setOtherNotes(otherNotes);
                } break;
                            case "Includes Outer Tie Rods": {
                    String otherNotes = result.getOtherNotes();
                    otherNotes = otherNotes+dataLines[i]+";;";
                    result.setOtherNotes(otherNotes);
                } break;
                case "Notes": {
                    StringBuilder notes;
                    if (element.findElements(By.className("firstRowNoteLine")).size()!=0) {
                        notes = new StringBuilder(element.findElement(By.className("firstRowNoteLine")).getText());
                        List<WebElement> subNotes = element.findElements(By.className("prodSubNote"));
                        if (subNotes.size()>0){
                            for (WebElement subnote : subNotes){
                                notes.append("\n");
                                notes.append(subnote.getText());
                            }
                        }
                       result.setNotes(notes.toString());
                    }
                } break;
                default: {
                    String notes = result.getNotes();
                    if  (notes!=null&&notes.length()>0&&notes.contains(attribute)){
                        //do nothing
                    }
                    else {
                        System.out.println("unknown shock attribute " + dataLines[i]);
                       // System.exit(0);
                        throw  new IllegalArgumentException();
                    }
                }
            }
        }
        /*for (int i = 1; i < dataLines.length; i++) {
            String data[] = dataLines[i].split(": ");
            String attribute = data[0];
            switch(attribute){
                case "Part Number": result.setPartNo(data[1]); break;
                case "Series": *//*do nothing*//* break;
                case "Position": result.setPosition(data[1]); break;
                case "Collapsed Length (IN)": result.setCollapsedL(data[1]); break;
                case "Extended Length (IN)": result.setExtendedL(data[1]); break;
                case "Body Diameter": result.setBodyDiameter(data[1]); break;
                case "Notes": result.setNotes(data[1]); break;
                default: {
                    String suspiciousLine = result.getNotes();
                    if  (suspiciousLine!=null&&suspiciousLine.length()>0){

                    }
                    System.out.println("unknown shock attribute "+dataLines[i]);
                    System.exit(0);
                }
            }
        }*/
        //img url
        WebElement imgUrl = element.findElement(By.tagName("img"));
        result.setImgUrl(imgUrl.getAttribute("src"));

        //detail url
        List<WebElement> urls = element.findElements(By.tagName("a"));
        WebElement detail = urls.get(1);
        result.setDetailsUrl(detail.getAttribute("href"));

        return result;
    }

    public static void parse(){
        List<String> dataLines = getDatalines();
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        int counter = 0;
        WebDriver driver = new ChromeDriver();
        Session session = CarDao.getSession();
        String url;
        for (String dataLine : dataLines){
            if (counter==100){
                driver.close();
                bad_sleep(10000);
                driver = new ChromeDriver();
                counter=0;
            }
            url = buildUrl(dataLine);
            driver.get(url);
            sleepTillReady(driver,url);
            WebElement searchResult = driver.findElement(By.id("ProductResults"));
            searchResult = searchResult.findElement(By.className("searchFeedback"));
            String carName = searchResult.getText();
            if (carName.contains("No Results")){
                System.out.println("no results");
                logCheckedData(dataLine,false);
            }
            else {
               // carName = StringUtils.substringBetween(carName, "'", "'");
                carName = getCarName(carName);
                System.out.println(carName);
                Car car = new Car();
                car.setCarFullName(carName);
                List<ShockAbsorber> absorbers = new ArrayList<>();
                WebElement searchResults = driver.findElement(By.className("searchList"));
                List<WebElement> shocks = searchResults.findElements(By.xpath("//div[contains(@class, 'row backBox')]"));
                try {
                    for (WebElement shock: shocks){
                        absorbers.add(buildShock(shock));
                    }
                }
                catch (Exception e){
                    car.setHasProblems(true);
                    logUnknnownData(dataLine);
                    break;
                }
                if (!car.hasProblems()){
                    car.setAbsorbers(absorbers);
                    CarDao.saveCar(session,car);
                    logCheckedData(dataLine,true);
                    counter++;
                }
            }
        }
        driver.close();
    }

    private static void logUnknnownData(String dataLine) {
        try(FileWriter fw = new FileWriter(TO_CHECK, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(dataLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkRawInfo(){
        List<String> result = new ArrayList<>();

        try {
            result = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String line : result){
            String split[] = line.split(";;");
            if (split.length!=7&&split.length!=11&&split.length!=14&&split.length!=17){
                System.out.println(line);
            }
            System.exit(0);
        }
    }

    public static void bad_sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }

    private static void codeDump(List<ShockAbsorber> absorbers){
        String url = "https://cart.bilsteinus.com/results?yearid=4506562921557186776&makeid=7612216853330999058&modelid=6545441064819404302&submodelid=4928551877482748412";
        System.setProperty("webdriver.chrome.driver", "F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(url);
        while (driver.findElements(By.className("searchList")).size()==0){
            bad_sleep(100);
        }
        WebElement element = driver.findElement(By.className("searchList"));
        List<WebElement> elements = element.findElements(By.xpath("//div[contains(@class, 'row backBox')]"));
       // System.out.println(elements.size());
        for (WebElement absorber : elements){
          /*  WebElement imgUrl = absorber.findElement(By.tagName("img"));
            System.out.println(imgUrl.getAttribute("src"));*/
            /*List<WebElement>urls = absorber.findElements(By.tagName("a"));
            WebElement imgUrl = urls.get(1);
            System.out.println(imgUrl.getAttribute("href"));
            System.out.println(absorber.getText());
            System.out.println();
            System.out.println();*/
            absorbers.add(buildShock(absorber));
            System.out.println();
        }
        /*List<WebElement> elementList = driver.findElements(By.className("searchList"));
        for (WebElement element: elementList){
            System.out.println(element.getText());
            System.out.println();
            System.out.println();
            System.out.println();
        }*/
        driver.close();

       // System.out.println(buildUrl("2002;;6292271345118249250;;Dodge;;4807456319597436941;;Dakota;;7947262061175191446;;SLT;;7945191717589219851;;Drive;;4WD;;6510190803011472636;;Body;;Extended Cab Pickup;;7138940516968354981"));

        /*B8 5100 - Shock Absorber
        Part Number: 24-253161
        Series: B8 5100
        Position: Front
        Collapsed Length (IN): 14.06
        Extended Length (IN): 18.43
        Body Diameter: 46mm
        Notes: -For Front Lifted Height: 0-1.5"*/

    }

    private static void codeDump2(){
        String url = "https://cart.bilsteinus.com/results?yearid=8384125918641192715&makeid=4301587299894003160&modelid=8475695503982163386&submodelid=7772828447578431571";
        System.setProperty("webdriver.chrome.driver", "F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get(url);
        while (driver.findElements(By.className("searchList")).size()==0){
            bad_sleep(100);
        }
        WebElement element = driver.findElement(By.id("ProductResults"));
        element = element.findElement(By.className("searchFeedback"));
        String carName = element.getText();
        carName = StringUtils.substringBetween(carName, "'", "'");
        System.out.println(carName);
        driver.close();
    }

    private static void codeDump3(){
        //   https://cart.bilsteinus.com/results?yearid=4224962658315777166&makeid=211415961687623801&modelid=5872224653985663944&submodelid=7772828447578431571
        System.setProperty("webdriver.chrome.driver", "F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://cart.bilsteinus.com/results?yearid=4224962658315777166&makeid=211415961687623801&modelid=5872224653985663944&submodelid=7772828447578431571");
        while (driver.findElements(By.className("searchList")).size()==0){
            bad_sleep(100);
        }
        WebElement searchResults = driver.findElement(By.className("searchList"));
        List<WebElement> shocks = searchResults.findElements(By.xpath("//div[contains(@class, 'row backBox')]"));
        WebElement shock = shocks.get(0);
        System.out.println(shock.findElement(By.className("firstRowNoteLine")).getText());
        System.out.println(shock.findElement(By.className("prodSubNote")).getText());
      /*  List<WebElement> resultLines = shock.findElements(By.tagName("p"));
        for (WebElement webElement: resultLines){

                System.out.println(webElement.getText());


        }*/
        driver.close();
    }

    private static void sleepTillReady (WebDriver driver, String url){
        int counter = 0;
        while (driver.findElements(By.className("searchList")).size()==0){
            counter++;
            if (counter>=200){
                driver.get(url);
                counter=0;
            }
            bad_sleep(100);

        }
    }

    private static void bm(){
        List<String> result = new ArrayList<>();
        try {
            result = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int counter = 0;
        List<String> withBM = new ArrayList<>();
        List<String> withoutBM = new ArrayList<>();
        for (String str : result){
            if (str.contains("BodyManufacturer")) {
               withBM.add(str);
            }
            else {
                withoutBM.add(buildUrl(str));
            }
        }
        try(FileWriter fw = new FileWriter(DATA_FOR_REPARSE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String line : withBM){
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  List<String> urlWithBM = new ArrayList<>();





        /*for (String bmLine : withBM){
            String url = "";
            String split[] = bmLine.split(";;");
            if (split.length<8){
                throw new IllegalArgumentException();
            }
            url = String.format(MAIN_URL_FORMAT,split[1], split[3], split[5], split[7]);
            for (int i = 8; i < split.length; i=i+3) {
                String attribute;
                if (split[i].equals("BodyManufacturer")){
                    break;
                }
                else {
                    attribute = split[i].substring(0,2);
                    url = url + "&"+attribute+"="+split[i+2];
                }
            }
            urlWithBM.add(url);
        }

        StringBuilder builder = new StringBuilder();
        for (String line : withoutBM){
            builder.append(line);
        }
        String dump = builder.toString();

        for (String line : urlWithBM){
            if (dump.contains(line)){
                System.out.println(line);
                System.exit(0);
            }
        }

        System.out.println("finished check");*/








       // System.out.println(counter);
        /*String split[] = str.split(";;");
        int size = split.length;
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("BodyManufacturer")){
                if (i+3!=split.length){
                    System.out.println(str);
                    System.exit(0);
                }
            }
        }*/
         /*if (split.length>8) {
                if (!str.contains("BodyManufacturer")&&!str.contains("Drive")&&!str.contains("Body")&&!str.contains("Suspension")&&!str.contains("Engine")&&!str.contains("Doors")&&!str.contains("Transmission")) {
                    System.out.println(str);
                    System.exit(0);
                }
            }*/
    }

    private static void reparseUtil(){
        List<String> reparseLines = new ArrayList<>();
        try {
            reparseLines = Files.readAllLines(Paths.get(DATA_FOR_REPARSE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> urlsForDeleteInDB = new ArrayList<>();
        for (String bmLine : reparseLines){
            String split[] = bmLine.split(";;");
            String fullCar = split [0]+" "+split[2]+" "+split[4]+" "+split[6];
            if (split.length>8) {
                if (!split[8].equals("BodyManufacturer")) {
                    fullCar = fullCar + " " + split[9];
                    if (split.length > 11) {
                        if (!split[11].equals("BodyManufacturer")) {
                            fullCar = fullCar + " " + split[12];
                            if (split.length > 14) {
                                if (!split[14].equals("BodyManufacturer")) {
                                    fullCar = fullCar + " " + split[15];
                                }
                            }
                        }
                    }
                }
            }
            urlsForDeleteInDB.add(fullCar);
        }




        List<Car> cars = CarDao.getCars();
        Session session = HibernateUtil.getSessionFactory().openSession();
        int counter = 0;
        for (Car car: cars){
            if (urlsForDeleteInDB.contains(car.getCarFullName())){
                Transaction transaction = session.beginTransaction();
                session.delete(car);
                transaction.commit();
                counter++;
            }
        }
       // HibernateUtil.shutdown();

        System.out.println("total deleted " + counter);
    }

    private static void clearAbsorbers(){

        List<Car> cars = CarDao.getCars();
        List<Integer> carIDs = new ArrayList<>();
        for (Car car : cars){
            carIDs.add(car.getId());
        }
        List<ShockAbsorber> absorbers = CarDao.getAbsorbers();

        Session session = HibernateUtil.getSessionFactory().openSession();
        int counter = 0;
        for (ShockAbsorber absorber: absorbers){
            if (!carIDs.contains(absorber.getCarID())){
                Transaction transaction = session.beginTransaction();
                session.delete(absorber);
                transaction.commit();
                //System.out.println("deleted "+absorber.getId());
                counter++;
            }
        }
        //HibernateUtil.shutdown();

        System.out.println("total deleted " + counter);
    }

    private static void updateCars(){
        List<String> cars = new ArrayList<>();
        try {
            cars = Files.readAllLines(Paths.get(TO_CHECK));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Session session = CarDao.getSession();
        for (String line: cars){
            String split[] = line.split(";;");
            String carFullName = split[0];
            int carID = Integer.parseInt(split[1]);
            CarDao.updateCar(session, carFullName,carID);
        }
        session.close();
    }

    private static String  getCarName(String dataLine){
        String split[] = dataLine.split("'");
       StringBuilder carName = new StringBuilder();
        carName.append(split[1]);
        if (split.length==3){
            carName.append("'");
            carName.append(split[2]);
        }
        return carName.toString();
    }
}
