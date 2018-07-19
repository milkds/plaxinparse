package toytec;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

public class ToytecItemBuilder {

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
