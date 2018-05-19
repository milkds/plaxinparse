package keystone;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "keystone_additional_parts")
public class KeyAdditionalPart {

    @Id
    @Column(name = "ID_BIL_ADD")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PART_NO")
    private String partNo;

    @Column(name = "PART_LINE")
    private String partLine;

    @Column(name = "ATTRIBUTES")
    private String attributes;

    @Column(name = "IMG_URLS")
    private String imgUrls;

    @Transient
    private List<KeyCar> cars;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getPartLine() {
        return partLine;
    }

    public void setPartLine(String partLine) {
        this.partLine = partLine;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(String imgUrls) {
        this.imgUrls = imgUrls;
    }

    public List<KeyCar> getCars() {
        return cars;
    }

    public void setCars(List<KeyCar> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return "KeyAdditionalPart{" +
                "id=" + id +
                ", partNo='" + partNo + '\'' +
                ", partLine='" + partLine + '\'' +
                ", attributes='" + attributes + '\'' +
                ", imgUrls='" + imgUrls + '\'' +
                ", cars=" + cars +
                '}';
    }
}
