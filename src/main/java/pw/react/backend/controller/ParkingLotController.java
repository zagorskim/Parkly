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

@RestController
@RequestMapping(path = "/Parkings")
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

    // get all parking lots sorted using filters in body
    @GetMapping(path = "/{parkingId}")
    public ResponseEntity<ParkingLotDto> getParkingLot(@PathVariable long parkingId) {
        ParkingLot parkingLot = parkingLotService.getParkingLot(parkingId);
        return ResponseEntity.status(HttpStatus.OK).body(ParkingLotDto.valueFrom(parkingLot));
    }

    @PostMapping(path = "/book/{parkingId}")
    public ResponseEntity<ReservationDto> bookParkingLot(@PathVariable long parkingId, @RequestBody UserDto userDto, @RequestBody ReservationDto reservationDto) {
        Reservation reservation = parkingLotService.bookParkingLot(reservationDto);
        return ResponseEntity.status(HttpStatus.OK).body(ReservationDto.valueFrom(reservation));
    }

    @DeleteMapping(path = "/cancel/{parkingId}")
    public ResponseEntity<ReservationDto> cancelParkingLot(@PathVariable long parkingId, @RequestBody UserDto userDto, @RequestBody ReservationDto reservationDto) {
        boolean result = parkingLotService.cancelReservation(reservationDto.reservationId());
        if(result)
            return ResponseEntity.status(HttpStatus.OK).body(reservationDto);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(reservationDto);
    }

}
