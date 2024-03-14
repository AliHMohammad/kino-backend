package dat3.kino.dto.response;

import dat3.kino.dto.Genre;

import java.util.List;

public record TmdbMovieDetailsResponse(
    List<Genre> genres,
    Integer id,
    String original_language,
    String original_title,
    String overview,
    Double popularity,
    String poster_path,
    String release_date,
    Integer runtime,
    String status,
    String title
) {}



