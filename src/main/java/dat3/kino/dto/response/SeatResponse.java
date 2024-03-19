package dat3.kino.dto.response;

import dat3.kino.entities.SeatPricing;

public record SeatResponse(
        Long id,
        int seatNumber,
        int rowNumber,
        Long auditoriumId,
        SeatPricing seatPricing) {
}
