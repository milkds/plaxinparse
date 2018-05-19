package keystone;

import javax.persistence.*;

@Entity
@Table(name = "keystone_cars")
public class KeyCar {

    @Id
    @Column(name = "CAR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "SHOCK_PART_NO")
    private String shockPartNo;

    @Column(name = "CAR_MAKE")
    private String carMake;

    @Column(name = "CAR_MODEL")
    private String carModel;

    @Column(name = "CAR_MODEL_YEAR")
    private String carModelYear;

    @Column(name = "CAR_ATTRIBUTE1")
    private String carAttribute1;

    @Column(name = "CAR_ATTRIBUTE2")
    private String carAttribute2;

    @Column(name = "SHOCK_ATTRIBUTE_NAME")
    private String shockAttributeName;

    @Column(name = "SHOCK_ATTRIBUTE_VALUE")
    private String shockAttributeValue;

    @Column(name = "SHOCK_ATTRIBUTE_NAME2")
    private String shockAttributeName2;

    @Column(name = "SHOCK_ATTRIBUTE_VALUE2")
    private String shockAttributeValue2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShockPartNo() {
        return shockPartNo;
    }

    public void setShockPartNo(String shockPartNo) {
        this.shockPartNo = shockPartNo;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarAttribute1() {
        return carAttribute1;
    }

    public void setCarAttribute1(String carAttribute1) {
        this.carAttribute1 = carAttribute1;
    }

    public String getShockAttributeName() {
        return shockAttributeName;
    }

    public void setShockAttributeName(String shockAttributeName) {
        this.shockAttributeName = shockAttributeName;
    }

    public String getShockAttributeValue() {
        return shockAttributeValue;
    }

    public void setShockAttributeValue(String shockAttributeValue) {
        this.shockAttributeValue = shockAttributeValue;
    }

    public String getCarModelYear() {
        return carModelYear;
    }

    public void setCarModelYear(String carModelYear) {
        this.carModelYear = carModelYear;
    }

    public String getCarAttribute2() {
        return carAttribute2;
    }

    public void setCarAttribute2(String carAttribute2) {
        this.carAttribute2 = carAttribute2;
    }

    public String getShockAttributeName2() {
        return shockAttributeName2;
    }

    public void setShockAttributeName2(String shockAttributeName2) {
        this.shockAttributeName2 = shockAttributeName2;
    }

    public String getShockAttributeValue2() {
        return shockAttributeValue2;
    }

    public void setShockAttributeValue2(String shockAttributeValue2) {
        this.shockAttributeValue2 = shockAttributeValue2;
    }

    @Override
    public String toString() {
        return "KeyCar{" +
                "id=" + id +
                ", shockPartNo='" + shockPartNo + '\'' +
                ", carMake='" + carMake + '\'' +
                ", carModel='" + carModel + '\'' +
                ", carModelYear='" + carModelYear + '\'' +
                ", carAttribute1='" + carAttribute1 + '\'' +
                ", carAttribute2='" + carAttribute2 + '\'' +
                ", shockAttributeName='" + shockAttributeName + '\'' +
                ", shockAttributeValue='" + shockAttributeValue + '\'' +
                ", shockAttributeName2='" + shockAttributeName2 + '\'' +
                ", shockAttributeValue2='" + shockAttributeValue2 + '\'' +
                '}';
    }
}
