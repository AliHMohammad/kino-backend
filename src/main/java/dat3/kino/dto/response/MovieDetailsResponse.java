package dat3.kino.dto.response;

import dat3.kino.dto.Genre;

import java.util.List;

public record MovieDetailsResponse(
    Integer id,
    String title,
    String overview,
    Integer runtime,
    String status,
    String posterPath,
    List<String> genres
) {
}
