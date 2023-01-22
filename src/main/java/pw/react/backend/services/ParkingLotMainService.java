package pw.react.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import pw.react.backend.dao.ParkingLotRepository;
import pw.react.backend.dao.UserRepository;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.ParkingLot;
import pw.react.backend.models.Reservation;
import pw.react.backend.web.ReservationDto;

import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class ParkingLotMainService implements ParkingLotService{

    private final Logger logger = LoggerFactory.getLogger(ParkingLotMainService.class);

    private final ParkingLotRepository parkingLotRepository;

    private final UserRepository userRepository;

    private final ReservationService reservationService;

    ParkingLotMainService(ParkingLotRepository parkingLotRepository, ReservationService reservationService, UserRepository userRepository) {

        this.parkingLotRepository = parkingLotRepository;

        this.reservationService = reservationService;

        this.userRepository = userRepository;
    }


    @Override
    public boolean deleteParkingLot(Long parkingLotId) {
        boolean result = false;
        if (parkingLotRepository.existsById(parkingLotId)) {
            parkingLotRepository.deleteById(parkingLotId);
            logger.info("Company with id {} deleted.", parkingLotId);
            result = true;
        }
        return result;
    }

    @Override
    public ParkingLot updateParkingLot(Long id, ParkingLot updatedParkingLot) throws ResourceNotFoundException {
        if (parkingLotRepository.existsById(id)) {
            updatedParkingLot.setId(id);
            ParkingLot result = parkingLotRepository.save(updatedParkingLot);
            logger.info("Parking lot with id {} updated.", id);
            return result;
        }
        throw new ResourceNotFoundException(String.format("Parking lot with id [%d] not found.", id));
    }

    @Override
    public ParkingLot validateAndSave(ParkingLot parkingLot) {
        if (isValidUser(parkingLot)) {
            logger.info("Parking lot is valid");
            Optional<ParkingLot> dbParkingLot = parkingLotRepository.findById(parkingLot.getId());
            if (dbParkingLot.isPresent()) {
                logger.info("Parking lot already exists. Updating it.");
                parkingLot.setId(dbParkingLot.get().getId());
            }
            parkingLot = parkingLotRepository.save(parkingLot);
            logger.info("Parking lot was saved.");
        }
        return parkingLot;
    }

    @Override
    public ParkingLot getParkingLot(Long parkingId) {
            return parkingLotRepository.findById(parkingId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Parking lot with %d does not exist", parkingId)));
    }

    @Override
    public Reservation bookParkingLot(ReservationDto reservationDto) {
        if (parkingLotRepository.existsById(reservationDto.parkingId()) && userRepository.existsById(reservationDto.userId())) {
            return reservationService.makeReservation(ReservationDto.convertToReservation(reservationDto));
        }
        throw new ResourceNotFoundException(String.format("Parking lot (id %d) or user (id %d) does not exist", reservationDto.parkingId(), reservationDto.userId()));
    }

    @Override
    public boolean cancelReservation(Long reservationId) {
        return reservationService.deleteReservation(reservationId);
    }


    private boolean isValidUser(ParkingLot parkingLot) {
        if (parkingLot != null) {
            if (!isValid(parkingLot.getDescription())) {
                logger.error("Empty parking lot name.");
                throw new UserValidationException("Empty parking lot name.");
            }
            return true;
        }
        logger.error("Parking lot is null.");
        throw new UserValidationException("Parking lot is null.");
    }

    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }

    private void logHeaders(@RequestHeader HttpHeaders headers) {
        logger.info("Controller request headers {}",
                headers.entrySet()
                        .stream()
                        .map(entry -> String.format("%s->[%s]", entry.getKey(), String.join(",", entry.getValue())))
                        .collect(joining(","))
        );
    }
}
