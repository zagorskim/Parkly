package pw.react.backend.web;

import java.util.List;

public record GetParkingsResponse(int noOfPages, List<ParkingLotDto> parkingLotsDto) { }
