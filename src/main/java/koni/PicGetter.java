package koni;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class PicGetter {
    private static final String URL_FORMAT = "http://finder.koni.de/img/products/%s.jpg";
    private static final String LOG_PATH = "src\\main\\resources\\koni_files\\picurllog";
    private static final String DOWNLOAD_PATH = "src\\main\\resources\\koni_files\\downloadurls.txt";
    private static final String TURN_PATH = "src\\main\\resources\\koni_files\\turnpartmap.txt";

    public static void main(String[] args) throws IOException, InvalidFormatException {
        updateLinks();
       // checkImg();
       // readPageByJsoup(1);
     //   savePartLinksFromTurn();
    }
    private static void updateLinks() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File("F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\koni_files\\Koni_Applications_2-2-1722.xls"));
        Sheet sheet = workbook.getSheetAt(0);
        List<String> urls = getUrls();
        int counter = 0;
        for (Row row: sheet) {
            Cell cell = row.getCell(21);
            if (cell == null) {
                cell = row.createCell(21);
            }
            String iterLine = urls.get(counter);
            String value = "";
            if (iterLine.endsWith("noImage")){
                value = "No image";
            }
            else {
                String split[] = iterLine.split(";;");
                value = split[1];
            }
            // Update the cell's value
            cell.setCellType(CellType.STRING);
            cell.setCellValue(value);
            counter++;
        }
        FileOutputStream fileOut = new FileOutputStream("F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\koni_files\\Koni_Applications_3.xls");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }

    private static void readKoniFile() throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File("F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\koni_files\\Koni_Applications_2-2-17.xls"));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        for (Row row: sheet) {
            int counter = 0;
            for(Cell cell: row) {
                counter++;
                if (counter==7){
                    String cellValue = dataFormatter.formatCellValue(cell);
                    StringBuilder url = new StringBuilder();
                    if(cellValue==null||cellValue.length()==0){
                       logUrl(cellValue, "no partNO");
                    }
                    else {
                        String split[] = cellValue.split(" ");
                        if (split.length==1){
                            url.append(cellValue);
                        }
                        else {
                            url.append(split[0]);
                            url.append("-");
                            url.append(split[1]);
                            if (split.length>2){
                                url.append("-");
                                url.append(split[2]);
                            }
                        }
                        String picUrl = String.format(URL_FORMAT, url.toString());
                        logUrl(cellValue, picUrl);
                    }
                }
            }
        }

    }

    private static void logUrl(String partNo, String url) {
        try(FileWriter fw = new FileWriter(LOG_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            out.println(partNo+";;"+url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("saved "+partNo);
    }
    private static void updateUrls(){
        List<String> urlLines = getUrls();
        List<String> newLines = new ArrayList<>();
        for (String line: urlLines){
            if (!line.contains("no partNO")){
                line = line.replace("/ie/en/popup?img=","");
                newLines.add(line);
            }
            else {
                newLines.add(line);
            }
        }
        try(FileWriter fw = new FileWriter(LOG_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String line: newLines){
                out.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkImg() {
        int counter = 0;
        List<String> newLines = new ArrayList<>();
        List<String> urlStrings = getUrls();
        Map<String, String> turnMap = getTurnParts();
        for (String url: urlStrings){
            String split[] = url.split(";;");
            if (split.length==1){
                newLines.add(url);
                continue;
            }
            if(!split[1].endsWith("noImage")){
                newLines.add(url);
                continue;
            }

            if (turnMap.containsKey(split[0].toLowerCase())){
                counter++;
                String validUrl = turnMap.get(split[0].toLowerCase());
                BufferedImage image = null;
                try {
                    image = ImageIO.read(new URL(validUrl));
                } catch (IOException e) {
                    newLines.add(url);
                    continue;
                }
                if(image != null){
                    System.out.println(validUrl);
                    newLines.add(split[0]+";;"+validUrl);
                    //   counter++;
                }
                else{
                    newLines.add(url);
                    System.out.println("NOT IMAGE");
                }
            }
            else {
                newLines.add(url);
            }



        }
        try(FileWriter fw = new FileWriter(DOWNLOAD_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String url: newLines){
                out.println(url);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("total images:" + counter);

       /* try(InputStream in = new URL("http://finder.koni.de/ie/en/popup?img=/img/products/8050-1036.jpg").openStream()){
      //  try(InputStream in = new URL("https://pbs.twimg.com/media/Dhp4HuHX0AEbpqF.jpg").openStream()){
            Files.copy(in, Paths.get("src\\main\\resources\\koni_files\\image.jpg"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("finished");*/
    }

    private static Map<String,String> getTurnParts() {
        Map<String,String> turnMap = new HashMap<>();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(TURN_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line: lines){
            String part = "";
            String split[]=line.split(";;");
            String split2[] = split[0].split("-");
            if (split2.length==2){
                part = split[0].replace("-"," ");
            }
            else {
                part = split2[0]+" "+split2[1]+"-"+split2[2];
            }
            part = part.toLowerCase();
            turnMap.put(part, split[1]);
        }
        return turnMap;
    }

    private static List<String> getUrls() {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(DOWNLOAD_PATH));
        } catch (IOException e) {
           e.printStackTrace();
        }

        return lines;
    }

    private static void prepareUrls(){
        List<String> lines = getUrls();
        List<String> urls = new ArrayList<>();
        for (String line : lines){
            if (!line.contains("no partNO")){
                String split[] = line.split(";;");
                urls.add(split[1]);
            }
        }
        try(FileWriter fw = new FileWriter(DOWNLOAD_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (String url: urls){
                out.println(url);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void saveHttpClient() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://finder.koni.de/img/products/8050-1036.jpg");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
// The underlying HTTP connection is still held by the response object
// to allow the response content to be streamed directly from the network socket.
// In order to ensure correct deallocation of system resources
// the user MUST call CloseableHttpResponse#close() from a finally clause.
// Please note that if response content is not fully consumed the underlying
// connection cannot be safely re-used and will be shut down and discarded
// by the connection manager.
        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            File targetFile = new File("src\\main\\resources\\koni_files\\image.jpg");

            if (entity1 != null) {
                InputStream inputStream = entity1.getContent();
                OutputStream outputStream = new FileOutputStream(targetFile);
                IOUtils.copy(inputStream, outputStream);
                outputStream.close();
            }
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }
    }

    private static void savePartLinksFromTurn() throws IOException {
        for (int i = 1; i < 49; i++) {
            Map<String, String> partMap = readPageByJsoup(i);
            savePartMap(partMap);
            System.out.println(i);
        }
    }

    private static void savePartMap(Map<String,String> partMap) {
        try(FileWriter fw = new FileWriter(TURN_PATH, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            for (Map.Entry<String, String> entry : partMap.entrySet()){
                out.println(entry.getKey()+";;"+entry.getValue());
                System.out.println("saved");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String>  readPageByJsoup(int pageNo) throws IOException {
        Map<String, String> partMap = new HashMap<>();
        String pagePathFormat = "src\\main\\resources\\koni_files\\turn14\\%d KONI Turn 14 Distribution- There at Every Turn.html";
        File file = new File(String.format(pagePathFormat,pageNo));
        Document doc = Jsoup.parse(file, null);
      //  List<Element> parts = doc.getElementsByClass("panel panel-default search-container col-xs-12 bs-callout-stock ");
        List<Element> parts = doc.getElementsByAttribute("data-itemcode");
        for (Element part: parts){
            Element partNo = part.getElementsByAttributeValueContaining("class","pull-left").first();
            Element pic = null;
            try{
                 pic = partNo.getElementsByAttributeValueContaining("class","product-info").first();
            }
            catch (NullPointerException e){
                continue;
            }
            String imgLink =pic.attr("src");
            imgLink = StringUtils.substringAfter(imgLink,"Turn_files/");
            imgLink = "https://d5otzd52uv6zz.cloudfront.net/"+imgLink;
            imgLink = imgLink.replace("100.jpg", "800.jpg");
            partNo = part.getElementsByAttributeValueContaining("class","partNo product-info").first();
           // Elements partNo = part.getElementsByAttribute("src");
         String partLine =  partNo.text();
         partLine = partLine.replace("Part #: kon","");
         partLine = partLine.replace(" ","-");
            partMap.put(partLine,imgLink);
        }

        return partMap;
    }
}
