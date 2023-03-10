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

    private final int parkingsPerPage = 20;
    private final Logger log = LoggerFactory.getLogger(ParkingLotMainService.class);

    private final ParkingLotRepository repository;

    ParkingLotMainService(ParkingLotRepository parkingLotRepository) {
        this.repository = parkingLotRepository;
    }

    @Override
    public ParkingLot getParkingLot(long parkingId) {
        return repository.getById(parkingId);
    }

    @Override
    public Pair<Integer, List<ParkingLot>> getParkingLots(int pageNo, boolean sortDescending, String filterString) {
        List<ParkingLot> parkingLots = repository.findAll();
        if(pageNo == -1) return Pair.of(-1, parkingLots); // get all parking lots
        parkingLots = filter(parkingLots, filterString);
        int noOfPages = (parkingLots.size() - 1)/parkingsPerPage + 1;
        if(parkingLots.size() == 0) noOfPages = 0;
        if(pageNo <= 0 || noOfPages == 0) return Pair.of(noOfPages, new ArrayList<>(0));

        long startIndex = (pageNo - 1)*parkingsPerPage;
        long endIndex = Math.min(pageNo*parkingsPerPage, parkingLots.size());
        if(startIndex >= parkingLots.size()) return Pair.of(noOfPages, new ArrayList<>(0));

        Collections.sort(parkingLots, (p1, p2) -> {
            return sortDescending ? p2.getName().compareTo(p1.getName()) : p1.getName().compareTo(p2.getName());
        });
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
        List<ParkingLot> filteredParkingLots = new ArrayList<>(0);
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
