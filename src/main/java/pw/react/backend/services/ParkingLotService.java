package pw.react.backend.services;

import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.ParkingLot;

public interface ParkingLotService {
    boolean deleteParkingLot(Long parkingLotId);
    ParkingLot updateParkingLot(Long id, ParkingLot updatedParkingLot) throws ResourceNotFoundException;

    ParkingLot validateAndSave(ParkingLot convertToParkingLot);

}
