package koni.entities;

import javax.persistence.*;

@Entity
@Table(name = "koni_kits")
public class KoniKit {

    @Id
    @Column(name = "SHOCK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shockID;

    @Column (name = "CAR_ID")
    private int carID;

    @Column(name = "SHOCK_PARTNO")
    private String partNo;

    @Column(name = "SHOCK_SERIES")
    private String series;

    @Column (name = "SHOCK_NOTES")
    private String notes;

    @Column (name = "SHOCK_CAR_NOTES")
    private String carNotes;

    public int getShockID() {
        return shockID;
    }

    public void setShockID(int shockID) {
        this.shockID = shockID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCarNotes() {
        return carNotes;
    }

    public void setCarNotes(String carNotes) {
        this.carNotes = carNotes;
    }

    @Override
    public String toString() {
        return "KoniKit{" +
                "shockID=" + shockID +
                ", carID=" + carID +
                ", partNo='" + partNo + '\'' +
                ", series='" + series + '\'' +
                ", notes='" + notes + '\'' +
                ", carNotes='" + carNotes + '\'' +
                '}';
    }
}
