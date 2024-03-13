package dat3.kino.dto.response;

import java.time.LocalDate;

public record MovieResponse(
        Long id,
        String title,
        String poster,
        Integer runtime,
        LocalDate premiere
) {
}
