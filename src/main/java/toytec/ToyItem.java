package toytec;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "toytec_item")
public class ToyItem {

    @Id
    @Column(name = "ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemID;

    @Column(name = "SKU")
    private String sku;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "ITEM_DESC")
    private String desc;

    @Column(name = "ITEM_MAIN_IMG")
    private String mainImg;

    @Column(name = "ITEM_IMG_LINKS")
    private String imgLinks;

    @Column(name = "ITEM_INSTRUCTION_LINKS")
    private String instructions;

    @Column(name = "IN_STOCK")
    private Boolean inStock;

    @Column(name = "PRICE_FROM")
    private BigDecimal priceFrom;

    @Column(name = "PRICE_TO")
    private BigDecimal priceTo;

    @Column(name = "ITEM_LINK")
    private String itemLink;

    @Column(name = "ITEM_CATEGORY")
    private String itemCategory;

    @Column(name = "ITEM_CATEGORY_LINK")
    private String itemCategoryLink;

    @Column(name = "ITEM_MAKE")
    private String itemMake;

    @Column(name = "ITEM_STOCK_AVAILABILITY")
    private String availability;

    @Column(name = "ITEM_STOCK_BACKORDER")
    private String backOrder;

    @Column(name = "ITEM_SUBCATEGORY")
    private String itemSubCategory;

    @Column(name = "META_KEYWORDS")
    private String metaKeywords;

    @Column(name = "META_DESCRIPTION")
    private String metaDescription;



    @Transient
    private List<ToyOption> options;

    @Override
    public String toString() {
        return "ToyItem{" +
                "itemID=" + itemID +
                ", sku='" + sku + '\'' +
                ", itemName='" + itemName + '\'' +
                ", mainImg='" + mainImg + '\'' +
                ", imgLinks='" + imgLinks + '\'' +
                ", instructions='" + instructions + '\'' +
                ", inStock=" + inStock +
                ", priceFrom=" + priceFrom +
                ", priceTo=" + priceTo +
                ", itemLink='" + itemLink + '\'' +
                ", itemCategory='" + itemCategory + '\'' +
                ", itemCategoryLink='" + itemCategoryLink + '\'' +
                ", itemMake='" + itemMake + '\'' +
                ", availability='" + availability + '\'' +
                ", backOrder='" + backOrder + '\'' +
                ", itemSubCategory='" + itemSubCategory + '\'' +
                '}';
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getBackOrder() {
        return backOrder;
    }

    public void setBackOrder(String backOrder) {
        this.backOrder = backOrder;
    }

    public String getItemSubCategory() {
        return itemSubCategory;
    }

    public void setItemSubCategory(String itemSubCategory) {
        this.itemSubCategory = itemSubCategory;
    }

    public String getItemLink() {
        return itemLink;
    }

    public void setItemLink(String itemLink) {
        this.itemLink = itemLink;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemCategoryLink() {
        return itemCategoryLink;
    }

    public void setItemCategoryLink(String itemCategoryLink) {
        this.itemCategoryLink = itemCategoryLink;
    }

    public List<ToyOption> getOptions() {
        return options;
    }

    public void setOptions(List<ToyOption> options) {
        this.options = options;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getImgLinks() {
        return imgLinks;
    }

    public void setImgLinks(String imgLinks) {
        this.imgLinks = imgLinks;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    public String getItemMake() {
        return itemMake;
    }

    public void setItemMake(String itemMake) {
        this.itemMake = itemMake;
    }
}
