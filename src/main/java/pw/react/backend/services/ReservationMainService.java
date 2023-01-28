package pw.react.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import pw.react.backend.dao.ParkingLotRepository;
import pw.react.backend.dao.ReservationRepository;
import pw.react.backend.exceptions.ReservationValidationException;
import pw.react.backend.models.Reservation;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ReservationMainService implements ReservationService{

    private static final Logger log = LoggerFactory.getLogger(UserMainService.class);

    private final ReservationRepository repository;
    private final ParkingLotRepository parkingRepository;

    ReservationMainService(ReservationRepository repository, ParkingLotRepository parkingRepository) {
        this.repository = repository;
        this.parkingRepository = parkingRepository;
    }

    @Override
    public Pair<Integer, List<Reservation>> getReservations(int pageNo) {
        if(pageNo <= 0) {
            return null;
        }
        List<Reservation> reservations = repository.findAll();
        long startIndex = (pageNo - 1)*50L;
        long endIndex = Math.min(pageNo*50L, reservations.size());
        if(startIndex >= reservations.size()) return null;
        Collections.sort(reservations, (r1, r2) -> {
            return (int)(r2.getParkingId() - r1.getParkingId());
        });
        int noOfPages = (reservations.size() - 1)/50 + 1;
        return Pair.of(noOfPages, reservations.subList((int)startIndex, (int)endIndex));
    }

    @Override
    public boolean deleteReservation(Long reservationId) {
        if(repository.existsById(reservationId)) {
            repository.deleteById(reservationId);
            log.info("Reservation with id {} deleted.", reservationId);
            return true;
        }
        return false;
    }

    @Override
    public boolean createReservation(Reservation reservation) {
        if(repository.existsById(reservation.getId()) || !isValidReservation(reservation)) {
            return false;
        }

        repository.save(reservation);
        return true;
    }

    private boolean isValidReservation(Reservation reservation) {
        if (reservation != null) {
            Long parkingId = reservation.getParkingId();
            if (!isValidParkingId(parkingId)) {
                log.error("Non-existent parking lot (" + parkingId + ").");
                throw new ReservationValidationException("Non-existent parking lot (" + parkingId + ").");
            }
            if (calcDuration(reservation.getStartDate(), reservation.getEndDate()) < 0) {
                log.error("Invalid period.");
                throw new ReservationValidationException("Invalid period.");
            }
            if (!isParkingLotFree(reservation)) {
                log.error("Parking lot is full.");
                throw new ReservationValidationException("Parking lot is full.");
            }
            return true;
        }
        log.error("Reservation is null.");
        throw new ReservationValidationException("Reservation is null.");
    }

    private boolean isValidParkingId(Long parkingId) {
        return parkingRepository.existsById(parkingId);
    }

    public boolean isParkingLotFree(Reservation reservation) {
       List<Reservation> reservations = repository.findByStartDateBeforeAndEndDateAfterAndParkingIdEquals(
           reservation.getEndDate().plusDays(1), reservation.getStartDate().minusDays(1), reservation.getParkingId());
       int capacity = parkingRepository.getById(reservation.getParkingId()).getCapacity();
       if(!reservations.isEmpty()) return true;
       return isSlotFree(reservations.toArray(new Reservation[0]), reservation, capacity);
    }

    public static boolean isSlotFree(Reservation[] reservations, Reservation newReservation, int capacity) {
        int length = calcDuration(newReservation.getStartDate(), newReservation.getEndDate()) + 1;

        int[] counters = new int[length];
        for(int i = 0; i < length; i++) counters[i] = 0;

        for(Reservation reservation : reservations) {
            int diffStart = calcDuration(newReservation.getStartDate(), reservation.getStartDate());
            int diffEnd = calcDuration(newReservation.getStartDate(), reservation.getEndDate());
            if(diffStart < 0) diffStart = 0;
            if(diffEnd > length - 1) diffEnd = length - 1;
            for(int i = diffStart; i <= diffEnd; i++) counters[i]++;
        }

        for(int i = 0; i < length; i++) {
            if(counters[i] >= capacity) return false;
        }
        return true;
    }

    private static int calcDuration(LocalDate start, LocalDate end) {
        return (int)Duration.between(start.atStartOfDay(),
            end.atStartOfDay()).toDays();
    }
}
