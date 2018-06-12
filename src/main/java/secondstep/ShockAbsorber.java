package secondstep;

import javax.persistence.*;

@Entity
@Table(name = "shocks")
public class ShockAbsorber {

    @Id
    @Column(name = "SHOCK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "CAR_ID")
    private int carID;

    @Column(name = "SERIES")
    private String series;

    @Column(name = "PART_NO")
    private String partNo;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "COLLAPSED_LENGTH")
    private String collapsedL;

    @Column(name = "EXTENDED_LENGTH")
    private String extendedL;

    @Column(name = "BODY_DIAMETER")
    private String bodyDiameter;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "PRODUCT_TYPE")
    private String productType;

    @Column(name = "PICTURE_LINK")
    private String imgUrl;

    @Column(name = "DETAILS_LINK")
    private String detailsUrl;

    @Column(name = "CHASSIS_MANUFACTURER")
    private String chassisManufacturer;

    @Column(name = "CHASSIS_MODEL")
    private String chassisModel;

    @Column(name = "CHASSIS_MODEL_EXTENDED")
    private String chassisModelExtended;

    @Column(name = "CHASSIS_CLASS")
    private String chassisClass;

    @Column(name = "CHASSIS_YEAR_RANGE")
    private String chassisYearRange;

    @Column(name = "APPLICATION_NOTE_1")
    private String chassisApplicationNote1;

    @Column(name = "OTHER_NOTES")
    private String otherNotes;

    @Column(name = "LIFT_START")
    private String liftStart;

    @Column(name = "LIFT_FINISH")
    private String liftFinish;


    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCollapsedL() {
        return collapsedL;
    }

    public void setCollapsedL(String collapsedL) {
        this.collapsedL = collapsedL;
    }

    public String getExtendedL() {
        return extendedL;
    }

    public void setExtendedL(String extendedL) {
        this.extendedL = extendedL;
    }

    public String getBodyDiameter() {
        return bodyDiameter;
    }

    public void setBodyDiameter(String bodyDiameter) {
        this.bodyDiameter = bodyDiameter;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getChassisManufacturer() {
        return chassisManufacturer;
    }

    public void setChassisManufacturer(String chassisManufacturer) {
        this.chassisManufacturer = chassisManufacturer;
    }

    public String getChassisModel() {
        return chassisModel;
    }

    public void setChassisModel(String chassisModel) {
        this.chassisModel = chassisModel;
    }

    public String getChassisModelExtended() {
        return chassisModelExtended;
    }

    public void setChassisModelExtended(String chassisModelExtended) {
        this.chassisModelExtended = chassisModelExtended;
    }

    public String getChassisClass() {
        return chassisClass;
    }

    public void setChassisClass(String chassisClass) {
        this.chassisClass = chassisClass;
    }

    public String getChassisYearRange() {
        return chassisYearRange;
    }

    public void setChassisYearRange(String chassisYearRange) {
        this.chassisYearRange = chassisYearRange;
    }

    public String getChassisApplicationNote1() {
        return chassisApplicationNote1;
    }

    public void setChassisApplicationNote1(String chassisApplicationNote1) {
        this.chassisApplicationNote1 = chassisApplicationNote1;
    }

    public String getOtherNotes() {
        return otherNotes;
    }

    public void setOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }

    @Override
    public String toString() {
        return "ShockAbsorber{" +
                "series='" + series + '\'' +
                ", partNo='" + partNo + '\'' +
                ", position='" + position + '\'' +
                ", collapsedL='" + collapsedL + '\'' +
                ", extendedL='" + extendedL + '\'' +
                ", bodyDiameter='" + bodyDiameter + '\'' +
                ", notes='" + notes + '\'' +
                ", productType='" + productType + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", detailsUrl='" + detailsUrl + '\'' +
                '}';
    }

    public String getLiftStart() {
        return liftStart;
    }

    public void setLiftStart(String liftStart) {
        this.liftStart = liftStart;
    }

    public String getLiftFinish() {
        return liftFinish;
    }

    public void setLiftFinish(String liftFinish) {
        this.liftFinish = liftFinish;
    }
}
