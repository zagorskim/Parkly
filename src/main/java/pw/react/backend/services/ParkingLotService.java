package pw.react.backend.services;

import org.springframework.data.util.Pair;
import pw.react.backend.models.ParkingLot;
import java.util.List;

public interface ParkingLotService {
    Pair<Integer, List<ParkingLot>> getParkingLots(int pageNo, boolean sortDescending, String filterString);
    boolean deleteParkingLot(Long parkingLotId);
    void validateAndSave(ParkingLot convertToParkingLot);
}
