package keystone;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "keystone_shocks")
public class KeyShock {

    @Id
    @Column(name = "SHOCK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PART_NO")
    private String partNo;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "INTERNAL_DESIGN")
    private String internalDesign;

    @Column(name = "ADJUSTABLE")
    private String adjustable;

    @Column(name = "EXTENDED_LENGTH")
    private String extendedLength;

    @Column(name = "COMPRESSED_LENGTH")
    private String compressedLength;

    @Column(name = "UPPER_MOUNTING_STYLE")
    private String upperMountingStyle;

    @Column(name = "LOWER_MOUNTING_STYLE")
    private String lowerMountingStyle;

    @Column(name = "BODY_COLOR")
    private String bodyColor;

    @Column(name = "WITH_RESERVOIR")
    private String withReservoir;

    @Column(name = "INCLUDES_DUST_SHIELD")
    private String includesDustShield;

    @Column(name = "INCLUDES_HARDWARE")
    private String includesHardware;

    @Column(name = "INCLUDES_BOOT")
    private String includesBoot;

    @Column(name = "QUANTITY")
    private String quantity;

    @Column(name = "ROD_DIAMETER")
    private String rodDiameter;

    @Column(name = "BODY_MATERIAL")
    private String bodyMaterial;

    @Column(name = "IMG_URL")
    private String imgUrl;

    @Column(name = "ROD_FINISH")
    private String rodFinish;

    @Column(name = "VALVING_TYPE")
    private String valvingType;

    @Column(name = "ROD_MATERIAL")
    private String rodMaterial;

    @Column(name = "SHOCK_NOTES")
    private String shockNotes;



    @Transient
    private List<KeyCar> cars;




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInternalDesign() {
        return internalDesign;
    }

    public void setInternalDesign(String internalDesign) {
        this.internalDesign = internalDesign;
    }

    public String getAdjustable() {
        return adjustable;
    }

    public void setAdjustable(String adjustable) {
        this.adjustable = adjustable;
    }

    public String getExtendedLength() {
        return extendedLength;
    }

    public void setExtendedLength(String extendedLength) {
        this.extendedLength = extendedLength;
    }

    public String getCompressedLength() {
        return compressedLength;
    }

    public void setCompressedLength(String compressedLength) {
        this.compressedLength = compressedLength;
    }

    public String getUpperMountingStyle() {
        return upperMountingStyle;
    }

    public void setUpperMountingStyle(String upperMountingStyle) {
        this.upperMountingStyle = upperMountingStyle;
    }

    public String getLowerMountingStyle() {
        return lowerMountingStyle;
    }

    public void setLowerMountingStyle(String lowerMountingStyle) {
        this.lowerMountingStyle = lowerMountingStyle;
    }

    public String getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(String bodyColor) {
        this.bodyColor = bodyColor;
    }

    public String getWithReservoir() {
        return withReservoir;
    }

    public void setWithReservoir(String withReservoir) {
        this.withReservoir = withReservoir;
    }

    public String getIncludesDustShield() {
        return includesDustShield;
    }

    public void setIncludesDustShield(String includesDustShield) {
        this.includesDustShield = includesDustShield;
    }

    public String getIncludesHardware() {
        return includesHardware;
    }

    public void setIncludesHardware(String includesHardware) {
        this.includesHardware = includesHardware;
    }

    public String getIncludesBoot() {
        return includesBoot;
    }

    public void setIncludesBoot(String includesBoot) {
        this.includesBoot = includesBoot;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRodDiameter() {
        return rodDiameter;
    }

    public void setRodDiameter(String rodDiameter) {
        this.rodDiameter = rodDiameter;
    }

    public String getBodyMaterial() {
        return bodyMaterial;
    }

    public void setBodyMaterial(String bodyMaterial) {
        this.bodyMaterial = bodyMaterial;
    }

    public String getRodFinish() {
        return rodFinish;
    }

    public void setRodFinish(String rodFinish) {
        this.rodFinish = rodFinish;
    }

    public String getValvingType() {
        return valvingType;
    }

    public void setValvingType(String valvingType) {
        this.valvingType = valvingType;
    }

    public String getRodMaterial() {
        return rodMaterial;
    }

    public void setRodMaterial(String rodMaterial) {
        this.rodMaterial = rodMaterial;
    }

    public List<KeyCar> getCars() {
        return cars;
    }

    public void setCars(List<KeyCar> cars) {
        this.cars = cars;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    @Override
    public String toString() {
        return "KeyShock{" +
                "id=" + id +
                ", partNo='" + partNo + '\'' +
                ", type='" + type + '\'' +
                ", internalDesign='" + internalDesign + '\'' +
                ", adjustable='" + adjustable + '\'' +
                ", extendedLength='" + extendedLength + '\'' +
                ", compressedLength='" + compressedLength + '\'' +
                ", upperMountingStyle='" + upperMountingStyle + '\'' +
                ", lowerMountingStyle='" + lowerMountingStyle + '\'' +
                ", bodyColor='" + bodyColor + '\'' +
                ", withReservoir='" + withReservoir + '\'' +
                ", includesDustShield='" + includesDustShield + '\'' +
                ", includesHardware='" + includesHardware + '\'' +
                ", includesBoot='" + includesBoot + '\'' +
                ", quantity='" + quantity + '\'' +
                ", rodDiameter='" + rodDiameter + '\'' +
                ", bodyMaterial='" + bodyMaterial + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", rodFinish='" + rodFinish + '\'' +
                ", valvingType='" + valvingType + '\'' +
                ", rodMaterial='" + rodMaterial + '\'' +
                ", cars=" + cars +
                '}';
    }

    public String getShockNotes() {
        return shockNotes;
    }

    public void setShockNotes(String shockNotes) {
        this.shockNotes = shockNotes;
    }
}
