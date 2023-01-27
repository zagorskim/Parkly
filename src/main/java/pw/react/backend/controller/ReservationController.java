package pw.react.backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.models.Reservation;
import pw.react.backend.services.ReservationService;
import pw.react.backend.web.ReservationDto;

@RestController
@RequestMapping(path = "/reservations")
//@Profile({"jwt"})
public class ReservationController {

	private static final Logger log = LoggerFactory.getLogger(JwtUserController.class);

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@DeleteMapping(path = "/cancel/{reservationId}")
	public ResponseEntity<Void> deleteReservation(@PathVariable long reservationId) {
		boolean result = reservationService.deleteReservation(reservationId);
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@PutMapping(path = "/create")
	public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationDto reservationDto) {
		Reservation reservation = ReservationDto.convertToReservation(reservationDto);
		boolean result = reservationService.createReservation(reservation);
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
		}
	}
}
