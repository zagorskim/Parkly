package pw.react.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import pw.react.backend.dao.ParkingLotRepository;
import pw.react.backend.exceptions.ParkingLotValidationException;
import pw.react.backend.models.ParkingLot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParkingLotMainService implements ParkingLotService{

    private final Logger log = LoggerFactory.getLogger(ParkingLotMainService.class);

    private final ParkingLotRepository repository;

    ParkingLotMainService(ParkingLotRepository parkingLotRepository) {
        this.repository = parkingLotRepository;
    }

    @Override
    public Pair<Integer, List<ParkingLot>> getParkingLots(int pageNo, boolean sortDescending, String filterString) {
        if(pageNo <= 0) {
            return null;
        }
        List<ParkingLot> parkingLots = repository.findAll();
        parkingLots = filter(parkingLots, filterString);
        long startIndex = (pageNo - 1)*50L;
        long endIndex = Math.min(pageNo*50L, parkingLots.size());
        if(startIndex >= parkingLots.size()) return null;
        Collections.sort(parkingLots, (p1, p2) -> {
            return sortDescending ? p2.getName().compareTo(p1.getName()) : p1.getName().compareTo(p2.getName());
        });
        int noOfPages = (parkingLots.size() - 1)/50 + 1;
        return Pair.of(noOfPages, parkingLots.subList((int)startIndex, (int)endIndex));
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

    private List<ParkingLot> filter(List<ParkingLot> parkingLots, String filterString) {
        filterString = filterString.toLowerCase();
        List<ParkingLot> filteredParkingLots = new ArrayList<>();
        for(ParkingLot parkingLot : parkingLots) {
            if(parkingLot.getName().toLowerCase().contains(filterString)) {
                filteredParkingLots.add(parkingLot);
            }
        }
        return filteredParkingLots;
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
