package pw.react.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.react.backend.dao.ReservationRepository;
import pw.react.backend.models.Reservation;

public class ReservationMainService implements ReservationService{

    private final Logger logger = LoggerFactory.getLogger(ReservationMainService.class);

    private final ReservationRepository repository;

    ReservationMainService(ReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Reservation makeReservation(Reservation reservation) {
        Reservation result = repository.save(reservation);
        logger.info("Reservation with id {} created.", result.getId());
        return result;
    }

    @Override
    public boolean deleteReservation(Long reservationId) {
        if (repository.existsById(reservationId)) {
            repository.deleteById(reservationId);
            logger.info("Reservation with id {} deleted.", reservationId);
            return true;
        }
        return false;
    }

}
