package pw.react.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.ParkingLot;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
}
