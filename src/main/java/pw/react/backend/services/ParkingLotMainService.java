package pw.react.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pw.react.backend.dao.ParkingLotRepository;
import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.models.ParkingLot;

import java.util.Optional;

public class ParkingLotMainService implements ParkingLotService{

    private final Logger logger = LoggerFactory.getLogger(ParkingLotMainService.class);

    private ParkingLotRepository repository;

    ParkingLotMainService() { /*Needed only for initializing spy in unit tests*/}

    ParkingLotMainService(ParkingLotRepository repository) {
        this.repository = repository;
    }


    @Override
    public boolean deleteParkingLot(Long parkingLotId) {
        boolean result = false;
        if (repository.existsById(parkingLotId)) {
            repository.deleteById(parkingLotId);
            logger.info("Company with id {} deleted.", parkingLotId);
            result = true;
        }
        return result;
    }

    @Override
    public ParkingLot updateParkingLot(Long id, ParkingLot updatedParkingLot) throws ResourceNotFoundException {
        if (repository.existsById(id)) {
            updatedParkingLot.setId(id);
            ParkingLot result = repository.save(updatedParkingLot);
            logger.info("Parking lot with id {} updated.", id);
            return result;
        }
        throw new ResourceNotFoundException(String.format("Parking lot with id [%d] not found.", id));
    }

    @Override
    public ParkingLot validateAndSave(ParkingLot parkingLot) {
        if (isValidUser(parkingLot)) {
            logger.info("Parking lot is valid");
            Optional<ParkingLot> dbParkingLot = repository.findById(parkingLot.getId());
            if (dbParkingLot.isPresent()) {
                logger.info("Parking lot already exists. Updating it.");
                parkingLot.setId(dbParkingLot.get().getId());
            }
            parkingLot = repository.save(parkingLot);
            logger.info("Parking lot was saved.");
        }
        return parkingLot;
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
}
