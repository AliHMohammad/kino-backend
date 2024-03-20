package dat3.kino.dto.response;

import dat3.kino.entities.Seat;

import java.time.LocalDateTime;
import java.util.List;

public record ReservationResponse(

        Long id,
        String user,
        ScreeningResponse screening,
        List<SeatResponse> seats,
        LocalDateTime createdAt
) {
}
