package firststep;

import org.openqa.selenium.WebDriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Service {
    private static final String YEAR_MAKE_PATH = "src\\main\\resources\\year_make";
    private static final String YEAR_MAKE_MODEL_PATH = "src\\main\\resources\\year_make_model";
    private static final String YEAR_MAKE_MODEL_SUB_PATH = "src\\main\\resources\\year_make_model_sub";
    private static final String DATA_FOR_SEARCH = "src\\main\\resources\\dataForSearch";

    public static void saveYearMakes(List<SearchData> yearMakes, String year){
        try(FileWriter fw = new FileWriter(YEAR_MAKE_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (SearchData data: yearMakes ){
                out.println(data.getYearText()+";;"+data.getYearValue()+";;"+data.getMakeText()+";;"+data.getMakeValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(year+ " written successfully");
    }
    public static void saveYearMakeModels(List<SearchData> yearMakes, String yearText, String makeText){
        try(FileWriter fw = new FileWriter(YEAR_MAKE_MODEL_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (SearchData data: yearMakes ){
                out.println(data.getYearText()+";;"+data.getYearValue()+";;"+data.getMakeText()+";;"+data.getMakeValue()+";;"
                +data.getModelText()+";;"+data.getModelValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(yearText+ " " + makeText + " written successfully");
    }
    public static void saveYearMakeModelsSub(List<SearchData> yearMakes, String yearText, String makeText, String modelText){
        try(FileWriter fw = new FileWriter(YEAR_MAKE_MODEL_SUB_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (SearchData data: yearMakes ){
                out.println(data.getYearText()+";;"+data.getYearValue()+";;"+data.getMakeText()+";;"+data.getMakeValue()
                +";;"+data.getModelText()+";;"+data.getModelValue()+";;"+data.getSubmodelText()+";;"+data.getSubmodelValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(yearText+ " " + makeText + " " + modelText + " submodels written successfully");
    }
    public static void saveStandardDataForSearch(SearchData data){
        try(FileWriter fw = new FileWriter(DATA_FOR_SEARCH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
                out.println(data.getYearText()+";;"+data.getYearValue()+";;"+data.getMakeText()+";;"+data.getMakeValue()
                        +";;"+data.getModelText()+";;"+data.getModelValue()+";;"+data.getSubmodelText()+";;"+data.getSubmodelValue());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void testYearMakes(){
        Set<String> years = new HashSet();
        try {
            List<String> lines = Files.readAllLines(Paths.get(YEAR_MAKE_PATH));

            for (String line : lines) {
                String [] info = line.split(";;");
                years.add(info[0]);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        for (int i = 2019; i >1900 ; i--) {
           // System.out.println("current year is "+i);
            if (!years.contains(i+"")){
                System.out.println("missing year - "+i);
                break;
            }
        }
    }

    public static List<SearchData> getYearMakeData(int yearStart, int yearFinish){
        List<SearchData> yearMakes = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(YEAR_MAKE_PATH));
            for (String line : lines) {
                String [] info = line.split(";;");
                SearchData data = new SearchData();
                String yearString = info[0];
                int year = Integer.parseInt(yearString);
                if (year<=yearStart && year>yearFinish){
                    data.setYearText(yearString);
                    data.setYearValue(info[1]);
                    data.setMakeText(info[2]);
                    data.setMakeValue(info[3]);
                    yearMakes.add(data);
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        }

        return yearMakes;
    }

    public static void checkParseConsistency(int year, WebDriver driver){
        Set<String> yearMakes = new HashSet();
        Set<String> yearMakesToCheck = new HashSet();
        Set<String> yearMakeModels = new HashSet();
        Set<String> yearMakeModelsToCheck = new HashSet();
        Set<String> yearMakeModelSubs = new HashSet();
        Set<String> datasToCheck = new HashSet();
        //getting years and makes from year make null null file
        try {
            //year make null null
            List<String> lines = Files.readAllLines(Paths.get(YEAR_MAKE_PATH));
            for (String line : lines) {
                if (line.startsWith(year+"")){
                    String [] info = line.split(";;");
                    String yearMake = info[0]+";;"+info[2];
                    yearMakes.add(yearMake);
                }
            }
            //year make model null
            lines = Files.readAllLines(Paths.get(YEAR_MAKE_MODEL_PATH));
            for (String line : lines) {
                if (line.startsWith(year+"")){
                    String [] info = line.split(";;");
                    String yearMake = info[0]+";;"+info[2];
                    yearMakesToCheck.add(yearMake);
                    String yearMakeModel = yearMake+";;"+info[4];
                    yearMakeModels.add(yearMakeModel);
                }
            }
            //year make model sub
            lines = Files.readAllLines(Paths.get(YEAR_MAKE_MODEL_SUB_PATH));
            for (String line : lines) {
                if (line.startsWith(year+"")){
                    String [] info = line.split(";;");
                    String yearMake = info[0]+";;"+info[2];
                    String yearMakeModel = yearMake+";;"+info[4];
                    yearMakeModelsToCheck.add(yearMakeModel);
                    String yearMakeModelSub = yearMakeModel+";;"+info[6];
                    yearMakeModelSubs.add(yearMakeModelSub);
                }
            }
            //final data
            lines = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
            for (String line : lines) {
                if (line.startsWith(year+"")){
                    String [] info = line.split(";;");
                    String dataToCheck = info[0]+";;"+info[2]+";;"+info[4]+";;"+info[6];
                    datasToCheck.add(dataToCheck);
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        //actually doing checks
            //models
        for (String yearMake: yearMakes){
            if (!yearMakesToCheck.contains(yearMake)){
                System.out.println("no data available for yearMakes --- "+yearMake);
                String split[] = yearMake.split(";;");
                SeleniumService.getModelsAndSubs(split[0], split[1], driver);
            }
        }
        System.out.println("all models are parsed for year "+ year);
             //submodels
        for (String yearMakeModel: yearMakeModels){
            if (!yearMakeModelsToCheck.contains(yearMakeModel)){
                System.out.println("no data available for yearMakeModel --- "+yearMakeModel);
                String split[] = yearMakeModel.split(";;");
                SeleniumService.getSubs(split[0], split[1], split[2], driver);
            }
        }
        System.out.println("all submodels are parsed for year "+ year);
            //final
        for (String yearMakeModelSub: yearMakeModelSubs){
            if (!datasToCheck.contains(yearMakeModelSub)){
                System.out.println("no data available for yearMakeModelSub --- "+yearMakeModelSub);
                System.exit(0);
            }
        }
        System.out.println("all results are ready to be parsed for year "+ year);
    }

    public static void removeYear(String year){
       removeYearFromYearMakeModel(year);
       removeYearFromYearMakeModelSub(year);
       removeYearFromDataForSearch(year);

    }
    private static void removeYearFromYearMakeModel(String year) {
        List<String> stringsYearMakeModel = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(YEAR_MAKE_MODEL_PATH));

            for (String line : lines) {
                if (!line.startsWith(year)){
                    stringsYearMakeModel.add(line);
                }
            }
            PrintWriter pw = new PrintWriter(YEAR_MAKE_MODEL_PATH);
            pw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        try(FileWriter fw = new FileWriter(YEAR_MAKE_MODEL_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String data: stringsYearMakeModel){
                out.println(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void removeYearFromYearMakeModelSub(String year){
        List<String> stringsYearMakeModel = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(YEAR_MAKE_MODEL_SUB_PATH));

            for (String line : lines) {
                if (!line.startsWith(year)){
                    stringsYearMakeModel.add(line);
                }
            }
            PrintWriter pw = new PrintWriter(YEAR_MAKE_MODEL_SUB_PATH);
            pw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        try(FileWriter fw = new FileWriter(YEAR_MAKE_MODEL_SUB_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String data: stringsYearMakeModel){
                out.println(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeYearFromDataForSearch(String year){
        List<String> stringsYearMakeModel = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));

            for (String line : lines) {
                if (!line.startsWith(year)){
                    stringsYearMakeModel.add(line);
                }
            }
            PrintWriter pw = new PrintWriter(DATA_FOR_SEARCH);
            pw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        try(FileWriter fw = new FileWriter(DATA_FOR_SEARCH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String data: stringsYearMakeModel){
                out.println(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void saveNonStandardDataForSearch(String searchDataDrop0) {
        try(FileWriter fw = new FileWriter(DATA_FOR_SEARCH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
                out.println(searchDataDrop0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkForDupes(){
        try {
            List<String> lines = Files.readAllLines(Paths.get(DATA_FOR_SEARCH));
            List<String> deduped = lines.stream().distinct().collect(Collectors.toList());
            System.out.println("start qty is " +lines.size()+". End qty is "+deduped.size()+". Difference is " + (lines.size()-deduped.size()));
            PrintWriter pw = new PrintWriter(DATA_FOR_SEARCH);
            pw.close();
            try(FileWriter fw = new FileWriter("F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\final_files\\allinfo", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw))
            {
                for (String s: deduped){
                    out.println(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        testYearMakes();
        //checkForDupes();
    }
}
