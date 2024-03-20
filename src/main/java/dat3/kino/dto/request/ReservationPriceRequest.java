package dat3.kino.dto.request;

import java.util.List;

public record ReservationPriceRequest(
        List<Long> seatIds,
        Long screeningId

) {
}
