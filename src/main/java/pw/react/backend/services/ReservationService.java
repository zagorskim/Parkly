package pw.react.backend.services;

import org.springframework.data.util.Pair;
import pw.react.backend.models.Reservation;
import java.util.List;

public interface ReservationService {
    Pair<Integer, List<Reservation>> getReservations(int pageNo);
    boolean deleteReservation(Long reservationId);
    boolean createReservation(Reservation reservation);
}
