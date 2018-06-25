package secondstep;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cars2")
public class Car {

    @Column(name = "CAR_FULL_NAME")
    private String carFullName;

    @Id
    @Column(name = "CAR_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "CAR_MODEL_YEAR")
    private String modelYear;

    @Column(name = "CAR_MAKE")
    private String make;

    @Column(name = "CAR_MODEL")
    private String model;

    @Column(name = "CAR_SUBMODEL")
    private String submodel;

    @Column(name = "CAR_BODY_MANUFACTURER")
    private String bodyManufacturer;

    @Column(name = "CAR_DRIVE")
    private String drive;

    @Column(name = "CAR_BODY")
    private String body;

    @Column(name = "CAR_SUSPENSION")
    private String suspension;

    @Column(name = "CAR_ENGINE")
    private String engine;

    @Column(name = "CAR_DOORS")
    private String doors;

    @Column(name = "CAR_TRANSMISSION")
    private String transmission;

    @Column(name = "CAR_NOTES")
    private String carNotes;

    @Column(name = "YEAR_START")
    private String yearStart;

    @Column(name = "YEAR_FINISH")
    private String yearFinish;

    @Transient
    private List<ShockAbsorber> absorbers;

    @Transient
    private boolean hasProblems = false;


    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSubmodel() {
        return submodel;
    }

    public void setSubmodel(String submodel) {
        this.submodel = submodel;
    }

    public String getBodyManufacturer() {
        return bodyManufacturer;
    }

    public void setBodyManufacturer(String bodyManufacturer) {
        this.bodyManufacturer = bodyManufacturer;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSuspension() {
        return suspension;
    }

    public void setSuspension(String suspension) {
        this.suspension = suspension;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getDoors() {
        return doors;
    }

    public void setDoors(String doors) {
        this.doors = doors;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getCarFullName() {
        return carFullName;
    }

    public void setCarFullName(String carFullName) {
        this.carFullName = carFullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ShockAbsorber> getAbsorbers() {
        return absorbers;
    }

    public void setAbsorbers(List<ShockAbsorber> absorbers) {
        this.absorbers = absorbers;
    }

    public boolean hasProblems() {
        return hasProblems;
    }

    public void setHasProblems(boolean hasProblems) {
        this.hasProblems = hasProblems;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", modelYear='" + modelYear + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", submodel='" + submodel + '\'' +
                ", bodyManufacturer='" + bodyManufacturer + '\'' +
                ", drive='" + drive + '\'' +
                ", body='" + body + '\'' +
                ", suspension='" + suspension + '\'' +
                ", engine='" + engine + '\'' +
                ", doors='" + doors + '\'' +
                ", transmission='" + transmission + '\'' +
                '}';
    }

    public String getCarNotes() {
        return carNotes;
    }

    public void setCarNotes(String carNotes) {
        this.carNotes = carNotes;
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
}
