package toytec;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ToytecItemBuilder {

    public static List<String> getItemSKUsFromCategory(String categoryUrl) throws IOException {
        List<String> sku = new ArrayList<>();
        Document doc = Jsoup.connect("https://www.toyteclifts.com/front-lifts-coilovers/front-coilovers-and-spacers.html?product_list_limit=all").get();
        Elements itemEls = doc.getElementsByAttributeValue("class", "item product product-item");
        for (Element itemEl: itemEls){
            Element skuEl = itemEl.getElementsByClass("product-item-link").first();
            System.out.println(skuEl.text());

            String skuText = skuEl.text();
            skuText  = getSkuFromSKUnameString(skuText);
            System.out.println(skuText);

            sku.add(skuText);
        }


        return sku;
    }

    private static String getSkuFromSKUnameString(String skuText) {
        String sku = skuText;
        if (skuText.contains("- ")){
            sku = StringUtils.substringBefore(skuText, "- ");
        }
        else {
            sku = StringUtils.substringBefore(skuText," ");
        }
        if (sku.startsWith("-")){
            sku = StringUtils.substringAfter(sku, "-");
        }
        if (sku.startsWith(" ")){
            sku = StringUtils.substringAfter(sku, " ");
        }
        return sku;
    }

    public static void getStock(ToyItem item) throws IOException {
       /* File file = new File("F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\toytec_files\\testPage");
        Document doc = Jsoup.parse(file, null);*/
      //  Document doc = Jsoup.connect("https://www.toyteclifts.com/171601-arb-1-gallon-forged-aluminum-air-tank.html").get();


        String itemUrl = item.getItemLink();
        Document doc = Jsoup.connect(itemUrl).get();
        Element stockSKUel = doc.getElementsByClass("product-info-stock-sku").first();
        Element availabilityEL = stockSKUel.getElementsByAttributeValueContaining("class", "stock available").first();
        if  (availabilityEL==null){
            availabilityEL = stockSKUel.getElementsByAttributeValueContaining("class", "stock unavailable").first();
        if  (availabilityEL==null){
            ToyUtil.logUnexpectedData("no availability Element", itemUrl);
            return;
            }
        }
        String availability = availabilityEL.text();
        String backOrder = "";
        Elements configurable = stockSKUel.getElementsByAttributeValue("id","availability-configurable");
        if (configurable.size()>0){
            stockSKUel = configurable.first();
        }
        Elements backOrderEl = stockSKUel.getElementsByClass("product-availability-backorder");
        int backorderSize = backOrderEl.size();
        if (backorderSize>0) {
           backOrder = backOrderEl.first().text();
        }
        else {
            Elements inStockEl = stockSKUel.getElementsByClass("product-availability-in-stock");
            int inStockSize = inStockEl.size();
            if (inStockSize>0){
                backOrder = inStockEl.first().text();
            }
            else {
                Elements outStockEl = stockSKUel.getElementsByClass("product-availability-in-stock");
                int outStockSize = outStockEl.size();
                if (outStockSize>0){
                    backOrder = outStockEl.first().text();
                }            }
        }
        System.out.println("availability: " + availability);
        System.out.println("backOrder: " + backOrder);

        item.setAvailability(availability);
        item.setBackOrder(backOrder);

        Elements stockEls = stockSKUel.getElementsByTag("div");
        System.out.println(stockEls.size());
        for (Element stockEl: stockEls){
            String checkEl = stockEl.attr("class");
           if (checkEl.equals("product attribute sku")){
               break;
           }
            switch (checkEl){
                case "product-info-stock-sku": break;
                case "stock available": break;
                case "product-availability-backorder": break;
                case "product-availability-in-stock": break;
                case "": break;
                default: ToyUtil.logUnexpectedData("unexpected class attribute in stock session: "+checkEl, itemUrl);
            }

        }
    }

    public static ToyItem buildItem(String itemUrl) throws IOException {
        ToyItem item = new ToyItem();
        populateFields(item,itemUrl);

        return item;
    }

    private static void populateFields(ToyItem item, String itemUrl) throws IOException {
       // Document doc = Jsoup.connect(itemUrl).get();
        File file = new File("F:\\My Java Projects\\plaxinparse\\src\\main\\resources\\toytec_files\\testPage");
        Document doc = Jsoup.parse(file, null);
        String sku = getSku(doc);
        String itemName = getItemName(doc);
        String itemDesc = getItemDesc(doc);
        String mainImgLink = getMainImgLink(doc);
    }

    private static String getMainImgLink(Document doc) {
        String imgLink = "";
        Element imgEl = doc.getElementsByAttributeValueContaining("data-gallery-role","gallery").first();
        //Element imgEl = doc.getElementsByAttributeValueContaining("class","fotorama__img").first();
        System.out.println(imgEl.toString());
       // imgEl = imgEl.getElementsByTag("img").first();
       // imgLink = imgEl.attr("src");

        System.out.println("IMG Link: "+imgLink);

        return imgLink;
    }

    private static String getItemDesc(Document doc) {
        String desc = "";
        Element descEl = doc.getElementsByAttributeValueContaining("class","product attribute description").first();
        descEl = descEl.getElementsByAttributeValueContaining("class","value").first();
        desc = descEl.toString();

        String descFormat = "<div> \n" +
                "       <strong>%s</strong> \n" +
                "      </div> \n" +
                "      <div>\n" +
                "        &nbsp; \n" +
                "      </div>";
        Element titleEl = descEl.getElementsByTag("strong").first();
        desc = desc.replace(String.format(descFormat,titleEl.text()),"");
        desc = desc.replace(" <div> \n" +
                "       \n" +
                "      <div>", " <div> " +
                "       \n" +
                "      <div>");

        desc = "Best Fucking Desc";
        System.out.println("Description: "+desc);

        return desc;
    }

    private static String getItemName(Document doc) {
        String itemName = "";
        Element itemNameEl = doc.getElementsByAttributeValueContaining("data-ui-id","page-title-wrapper").first();
        itemName = itemNameEl.text();
        itemName = StringUtils.substringAfter(itemName," - ");

        System.out.println("Item Name: "+itemName);
        return itemName;
    }

    private static String getSku(Document doc) {
        String sku = "";

        Element skuEl = doc.getElementsByAttributeValueContaining("itemprop","sku").first();
        sku = skuEl.text();

        System.out.println("SKU: "+sku);
        return sku;
    }
}
