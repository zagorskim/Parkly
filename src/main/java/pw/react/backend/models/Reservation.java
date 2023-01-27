package pw.react.backend.models;

import org.springframework.data.annotation.Reference;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation implements Serializable{
    @Serial
    private static final long serialVersionUID = -6783504532088859179L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String description;

    @Column
    @Reference(User.class)
    private long userId;

    @Column
    @Reference(ParkingLot.class)
    private long parkingId;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getParkingId() {
        return parkingId;
    }

    public void setParkingId(long parkingId) {
        this.parkingId = parkingId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
