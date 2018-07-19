package turn14;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "turn_part")
public class TurnPart {
    @Id
    @Column(name = "PART_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PRICING_GROUP")
    private String pricingGroup;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PART_NO")
    private String partNo;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "BOX_DIMS")
    private String boxDims;

    @Column(name = "DESCRIPTION_2")
    private String description2;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "IMG_LINKS")
    private String imgLinks;

    @Column(name="PRICE")
    private String price;

    @Column(name = "MAP")
    private String map;

    @Column(name = "JOBBER")
    private String jobber;

    @Column(name = "RETAIL")
    private String retail;

    @Column(name = "PART_MAKE")
    private String partMake;

    @Transient
    private List<TurnCar> cars;

    @Override
    public String toString() {
        return "TurnPart{" +
                "id=" + id +
                ", pricingGroup='" + pricingGroup + '\'' +
                ", description='" + description + '\'' +
                ", partNo='" + partNo + '\'' +
                ", productName='" + productName + '\'' +
                ", boxDims='" + boxDims + '\'' +
                ", description2='" + description2 + '\'' +
                ", color='" + color + '\'' +
                ", imgLinks='" + imgLinks + '\'' +
                ", price='" + price + '\'' +
                ", map='" + map + '\'' +
                ", jobber='" + jobber + '\'' +
                ", retail='" + retail + '\'' +
                ", partMake='" + partMake + '\'' +
                '}';
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getJobber() {
        return jobber;
    }

    public void setJobber(String jobber) {
        this.jobber = jobber;
    }

    public String getRetail() {
        return retail;
    }

    public void setRetail(String retail) {
        this.retail = retail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPricingGroup() {
        return pricingGroup;
    }

    public void setPricingGroup(String pricingGroup) {
        this.pricingGroup = pricingGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBoxDims() {
        return boxDims;
    }

    public void setBoxDims(String boxDims) {
        this.boxDims = boxDims;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImgLinks() {
        return imgLinks;
    }

    public void setImgLinks(String imgLinks) {
        this.imgLinks = imgLinks;
    }

    public String getPartMake() {
        return partMake;
    }

    public void setPartMake(String partMake) {
        this.partMake = partMake;
    }

    public List<TurnCar> getCars() {
        return cars;
    }

    public void setCars(List<TurnCar> cars) {
        this.cars = cars;
    }
}
