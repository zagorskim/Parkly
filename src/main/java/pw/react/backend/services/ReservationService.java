package pw.react.backend.services;

import pw.react.backend.models.Reservation;

public interface ReservationService {
    Reservation makeReservation(Reservation reservation);

    boolean deleteReservation(Long reservationId);
}
