package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.models.ParkingLot;
import pw.react.backend.models.Reservation;
import pw.react.backend.services.ParkingLotService;
import pw.react.backend.services.ReservationService;
import pw.react.backend.web.ParkingLotDto;
import pw.react.backend.web.ReservationDto;
import pw.react.backend.web.UserDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/parkings")
@Profile({"jwt"})
public class ParkingLotController {

    private static final Logger log = LoggerFactory.getLogger(JwtUserController.class);

    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PutMapping(path = "/createOrUpdate")
    public ResponseEntity<Void> addOrUpdateParkingLot(@RequestBody ParkingLotDto parkingLotDto) {
        parkingLotService.validateAndSave(ParkingLotDto.convertToParkingLot(parkingLotDto));
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getPage/{pageNo}/sortDescending/{sortDescending}")
    public ResponseEntity<List<ParkingLotDto>> getParkingLot(@PathVariable int pageNo,
                                                             @PathVariable boolean sortDescending,
                                                             @RequestBody String filter) {
        List<ParkingLot> parkingLots = parkingLotService.getParkingLots(pageNo, sortDescending, filter);
        List<ParkingLotDto> parkingLotsDto = new ArrayList<ParkingLotDto>();
        for(ParkingLot parkingLot : parkingLots) {
            parkingLotsDto.add(ParkingLotDto.valueFrom(parkingLot));
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingLotsDto);
    }

    @DeleteMapping(path = "/cancel/{parkingId}")
    public ResponseEntity<Void> cancelParkingLot(@PathVariable long parkingId) {
        boolean result = parkingLotService.deleteParkingLot(parkingId);
        if(result)
            return ResponseEntity.status(HttpStatus.OK).body(null);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
