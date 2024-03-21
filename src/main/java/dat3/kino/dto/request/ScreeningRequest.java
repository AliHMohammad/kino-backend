package dat3.kino.dto.request;

import java.time.LocalDateTime;

public record ScreeningRequest(
        Long movieId,
        Long auditoriumId,
        LocalDateTime startTime,
        boolean is3D
) {
}
