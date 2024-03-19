package dat3.kino.dto.request;

import java.time.LocalDateTime;

public record MovieScreeningRequest(
        Long movieId,
        Long cinemaId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
