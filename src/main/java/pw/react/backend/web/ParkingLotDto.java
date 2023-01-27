package pw.react.backend.web;

import pw.react.backend.models.ParkingLot;

public record ParkingLotDto(Long id, String description, String name, String photo, double pricePerDay, double latitude, double longitude, boolean security, String parkingLotType, int capacity) {
    public static ParkingLotDto valueFrom(ParkingLot parkingLot) {
        return new ParkingLotDto(parkingLot.getId(), parkingLot.getDescription(), parkingLot.getName(), parkingLot.getPhoto(),
                parkingLot.getPricePerDay(), parkingLot.getLatitude(), parkingLot.getLongitude(),
                parkingLot.getSecurity(), parkingLot.getParkingLotType(), parkingLot.getCapacity());
    }

    public static ParkingLot convertToParkingLot(ParkingLotDto parkingLotDto) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setId(parkingLotDto.id());
        parkingLot.setDescription(parkingLotDto.description());
        parkingLot.setName(parkingLotDto.name);
        parkingLot.setPhoto(parkingLotDto.photo());
        parkingLot.setPricePerDay(parkingLotDto.pricePerDay());
        parkingLot.setCoordinates(parkingLotDto.latitude(), parkingLotDto.longitude());
        parkingLot.setSecurity(parkingLotDto.security());
        parkingLot.setParkingLotType(parkingLotDto.parkingLotType());
        parkingLot.setCapacity(parkingLotDto.capacity());
        return parkingLot;
    }


}
