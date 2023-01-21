package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.models.ParkingLot;
import pw.react.backend.services.ParkingLotService;
import pw.react.backend.web.ParkingLotDto;

@RestController
@RequestMapping(path = "/lots")
@Profile({"!jwt"})
public class ParkingLotController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }


    @GetMapping(path = "")
    public ResponseEntity<ParkingLotDto> createParkingLot(@RequestBody ParkingLotDto parkingLotDto) {
        ParkingLot newParkingLot = parkingLotService.validateAndSave(ParkingLotDto.convertToParkingLot(parkingLotDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(ParkingLotDto.valueFrom(newParkingLot));
    }
}
