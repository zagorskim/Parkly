package pw.react.backend.services;

import pw.react.backend.models.Reservation;
import java.util.List;

public interface ReservationService {
    List<Reservation> getReservations(int pageNo);
    boolean deleteReservation(Long reservationId);
    boolean createReservation(Reservation reservation);
}
