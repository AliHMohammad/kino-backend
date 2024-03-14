package dat3.kino.dto.request;

import java.time.LocalDate;

public record MovieRequest(
        Long id,
        String title,
        Integer runtime,
        LocalDate premiere,
        String poster
) {
}
