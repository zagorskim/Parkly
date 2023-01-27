package pw.react.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.react.backend.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{
	//@Query("SELECT r FROM reservations WHERE r.parkingId = parkingId AND r.endDate >= startDate AND r.startDate <= endDate")
	List<Reservation> findByStartDateBeforeAndEndDateAfterAndParkingIdEquals(LocalDate endDate, LocalDate startDate,
																			 Long parkingId);
}
