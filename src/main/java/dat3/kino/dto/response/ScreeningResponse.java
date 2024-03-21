package dat3.kino.dto.response;

import dat3.kino.entities.Auditorium;

import java.time.LocalDateTime;

public record ScreeningResponse (
        Long id,
        AuditoriumResponse auditorium,
        MovieResponse movie,
        LocalDateTime startTime,
        boolean is3D
) {
}
