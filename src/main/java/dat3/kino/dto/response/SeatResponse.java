package dat3.kino.dto.response;

public record SeatResponse(
        Long id,
        int seatNumber,
        int rowNumber,
        Long auditoriumId,
        String seatPricing) {
}
