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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/reservations")
@Profile({"jwt"})
public class ReservationController {

	private static final Logger log = LoggerFactory.getLogger(JwtUserController.class);

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService, PasswordEncoder passwordEncoder) {
		this.reservationService = reservationService;
	}

	@GetMapping(path = "/getPage/{pageNo}")
	public ResponseEntity<List<ReservationDto>> getReservations(@PathVariable int pageNo) {
		List<Reservation> reservations = reservationService.getReservations(pageNo);
		List<ReservationDto> reservationsDto = new ArrayList<ReservationDto>();
		for(Reservation reservation : reservations) {
			reservationsDto.add(ReservationDto.valueFrom(reservation));
		}
		return ResponseEntity.status(HttpStatus.OK).body(reservationsDto);
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

	@PostMapping(path = "/create")
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
