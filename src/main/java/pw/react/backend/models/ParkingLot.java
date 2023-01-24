package pw.react.backend.models;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "parking_lots")
public class ParkingLot implements Serializable {
    @Serial
    private static final long serialVersionUID = -6783504532088859179L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String description;

    @Column
    private String name;

    @Column
    private String photo;

    @Column
    private String pricePerDay;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column
    private boolean security;

    @Column
    private String parkingLotType;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String address) {this.name = address;}

    public String getName() {return name;}

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPricePerDay(String pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getPricePerDay() {
        return pricePerDay;
    }

    public void setCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setSecurity(boolean security) {
        this.security = security;
    }

    public boolean getSecurity() {
        return security;
    }

    public void setParkingLotType(String parkingLotType) {
        this.parkingLotType = parkingLotType;
    }

    public String getParkingLotType() {
        return parkingLotType;
    }

}
