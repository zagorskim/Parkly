package pw.react.backend.services;

import pw.react.backend.exceptions.ResourceNotFoundException;
import pw.react.backend.models.ParkingLot;
import pw.react.backend.models.Reservation;
import pw.react.backend.web.ReservationDto;

public interface ParkingLotService {
    boolean deleteParkingLot(Long parkingLotId);
    ParkingLot updateParkingLot(Long id, ParkingLot updatedParkingLot) throws ResourceNotFoundException;

    void validateAndSave(ParkingLot convertToParkingLot);

    ParkingLot getParkingLot(Long parkingId);
}
