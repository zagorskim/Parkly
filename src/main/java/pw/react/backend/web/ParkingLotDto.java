package pw.react.backend.web;

import pw.react.backend.models.ParkingLot;

public record ParkingLotDto(Long id, String description, String photo, String pricePerDay, double latitude, double longitude, boolean security, String parkingLotType) {
    public static ParkingLotDto valueFrom(ParkingLot parkingLot) {
        return new ParkingLotDto(parkingLot.getId(), parkingLot.getDescription(), parkingLot.getPhoto(),
                parkingLot.getPricePerDay(), parkingLot.getLatitude(), parkingLot.getLongitude(),
                parkingLot.getSecurity(), parkingLot.getParkingLotType());
    }

    public static ParkingLot convertToParkingLot(ParkingLotDto parkingLotDto) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(parkingLotDto.id());
        parkingLot.setDescription(parkingLotDto.description());
        parkingLot.setPhoto(parkingLotDto.photo());
        parkingLot.setPricePerDay(parkingLotDto.pricePerDay());
        parkingLot.setCoordinates(parkingLotDto.latitude(), parkingLotDto.longitude());
        parkingLot.setSecurity(parkingLotDto.security());
        parkingLot.setParkingLotType(parkingLotDto.parkingLotType());
        return parkingLot;
    }


}
