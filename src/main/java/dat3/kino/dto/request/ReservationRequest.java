package dat3.kino.dto.request;


import java.util.List;

public record ReservationRequest(
        Long screeningId,
        List<Long> seatIds
) {
}
