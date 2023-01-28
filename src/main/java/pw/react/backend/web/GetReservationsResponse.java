package pw.react.backend.web;

import java.util.List;

public record GetReservationsResponse(int noOfPages, List<ReservationDto> reservationsDto) { }
