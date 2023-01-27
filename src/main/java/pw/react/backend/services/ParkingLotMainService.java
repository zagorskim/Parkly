package pw.react.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;
import pw.react.backend.dao.ParkingLotRepository;
import pw.react.backend.exceptions.ParkingLotValidationException;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.ParkingLot;
import pw.react.backend.models.Reservation;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class ParkingLotMainService implements ParkingLotService{

    private final Logger log = LoggerFactory.getLogger(ParkingLotMainService.class);

    private final ParkingLotRepository repository;

    ParkingLotMainService(ParkingLotRepository parkingLotRepository) {
        this.repository = parkingLotRepository;
    }

    @Override
    public List<ParkingLot> getParkingLots(int pageNo, boolean sortDescending, String filter) {
        if(pageNo <= 0) {
            return null;
        }
        List<ParkingLot> parkingLots = repository.findAll();
        return parkingLots;
    }

    @Override
    public boolean deleteParkingLot(Long parkingLotId) {
        if (repository.existsById(parkingLotId)) {
            repository.deleteById(parkingLotId);
            log.info("Parking lot with id {} deleted.", parkingLotId);
            return true;
        }
        return false;
    }

    @Override
    public void validateAndSave(ParkingLot parkingLot) {
        if (isValidParkingLot(parkingLot)) {
            log.info("Parking lot is valid");
            repository.save(parkingLot);
            log.info("Parking lot was saved.");
        }
    }

    private boolean isValidParkingLot(ParkingLot parkingLot) {
        if (parkingLot != null) {
            if (!isValid(parkingLot.getDescription())) {
                log.error("Empty parking lot name.");
                throw new ParkingLotValidationException("Empty parking lot name.");
            }
            if(parkingLot.getCapacity() <= 0) {
                log.error("Invalid capacity.");
                throw new ParkingLotValidationException("Invalid capacity.");
            }
            if(parkingLot.getPricePerDay() < 0) {
                log.error("Invalid price.");
                throw new ParkingLotValidationException("Invalid price.");
            }
            return true;
        }
        log.error("Parking lot is null.");
        throw new ParkingLotValidationException("Parking lot is null.");
    }

    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }
}
