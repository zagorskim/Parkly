package pw.react.backend.web;

import pw.react.backend.models.Reservation;

import java.time.LocalDateTime;

public record ReservationDto(long reservationId,
                             String description,
                             long userId,
                             long parkingId,

                             LocalDateTime startDate,
                             LocalDateTime endDate) {
    public static final ReservationDto EMPTY = new ReservationDto(-1, "", -1, -1, null, null);

    public static ReservationDto valueFrom(Reservation reservation) {
        return new ReservationDto(reservation.getId(), reservation.getDescription(), reservation.getUserId(), reservation.getParkingId(), reservation.getStartDateTime(), reservation.getEndDateTime());
    }

    public static Reservation convertToReservation(ReservationDto reservationDto){
        Reservation reservation = new Reservation();
        reservation.setId(reservationDto.reservationId());
        reservation.setDescription(reservationDto.description());
        reservation.setParkingId(reservationDto.parkingId());
        reservation.setUserId(reservationDto.userId());
        reservation.setStartDateTime(reservationDto.startDate());
        reservation.setEndDateTime(reservationDto.endDate());
        return reservation;
    }
}
