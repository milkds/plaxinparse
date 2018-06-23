package koni.entities;

import javax.persistence.*;

@Entity
@Table(name = "koni_cars")
public class KoniCar {
    @Id
    @Column(name = "CAR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int carID;

    @Column (name = "CAR_MAKE")
    private String carMake;

    @Column (name = "CAR_MODEL")
    private String carModel;

    @Column (name = "CAR_SUBMODEL")
    private String carSubmodel;

    @Column (name = "CAR_YEAR_START")
    private String yearStart;

    @Column (name = "CAR_YEAR_FINISH")
    private String yearFinish;

    @Transient
    private String carNotes;

    @Column (name = "CAR_YEAR_NOTES")
    private String yearNotes;

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
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

    public String getCarSubmodel() {
        return carSubmodel;
    }

    public void setCarSubmodel(String carSubmodel) {
        this.carSubmodel = carSubmodel;
    }

    public String getYearStart() {
        return yearStart;
    }

    public void setYearStart(String yearStart) {
        this.yearStart = yearStart;
    }

    public String getYearFinish() {
        return yearFinish;
    }

    public void setYearFinish(String yearFinish) {
        this.yearFinish = yearFinish;
    }

    public String getCarNotes() {
        return carNotes;
    }

    public void setCarNotes(String carNotes) {
        this.carNotes = carNotes;
        }

    @Override
    public String toString() {
        return "KoniCar{" +
                "carID=" + carID +
                ", carMake='" + carMake + '\'' +
                ", carModel='" + carModel + '\'' +
                ", carSubmodel='" + carSubmodel + '\'' +
                ", yearStart='" + yearStart + '\'' +
                ", yearFinish='" + yearFinish + '\'' +
                ", carNotes='" + carNotes + '\'' +
                ", yearNotes='" + yearNotes + '\'' +
                '}';
    }

    public String getYearNotes() {
        return yearNotes;
    }

    public void setYearNotes(String yearNotes) {
        this.yearNotes = yearNotes;
    }
}
