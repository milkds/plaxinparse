package turn14;

import javax.persistence.*;

@Entity
@Table(name = "turn_car")
public class TurnCar {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PART_ID")
    private int partID;

    @Column(name = "YEAR_START")
    private String yearStart;

    @Column(name = "YEAR_FINISH")
    private String yearFinish;

    @Column(name = "MAKE")
    private String make;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "SUBMODEL")
    private String subModel;

    @Column(name = "ENGINE")
    private String engine;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "NOTE_1")
    private String note1;


    @Override
    public String toString() {
        return "TurnCar{" +
                "id=" + id +
                ", partID=" + partID +
                ", yearStart='" + yearStart + '\'' +
                ", yearFinish='" + yearFinish + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", subModel='" + subModel + '\'' +
                ", engine='" + engine + '\'' +
                ", category='" + category + '\'' +
                ", notes='" + notes + '\'' +
                ", note1='" + note1 + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartID() {
        return partID;
    }

    public void setPartID(int partID) {
        this.partID = partID;
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

    public String getSubModel() {
        return subModel;
    }

    public void setSubModel(String subModel) {
        this.subModel = subModel;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }
}
