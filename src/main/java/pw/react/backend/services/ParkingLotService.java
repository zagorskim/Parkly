package pw.react.backend.services;

import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.ParkingLot;
import java.util.List;

public interface ParkingLotService {
    List<ParkingLot> getParkingLots(int pageNo, boolean sortDescending, String filter);
    boolean deleteParkingLot(Long parkingLotId);
    void validateAndSave(ParkingLot convertToParkingLot);
}
