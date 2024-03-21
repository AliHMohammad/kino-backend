package dat3.kino.services;

import dat3.kino.dto.Genre;
import dat3.kino.dto.response.MovieDetailsResponse;
import dat3.kino.dto.response.TmdbMovieDetailsResponse;
import dat3.kino.exception.WebFluxClientException;
import dat3.kino.exception.WebFluxServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service class for interacting with The Movie Database (TMDB) API.
 */
@Service
public class TMDBService {
    private final WebClient webClient;

    // The API key for TMDB, injected from application properties
    @Value("${tmdb.api.key}")
    private String apiKey;

    /**
     * Constructor for TMDBService.
     * @param webClientBuilder WebClient.Builder instance for creating WebClient
     */
    public TMDBService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    /**
     * Fetches movie details from TMDB API.
     * @param id The id of the movie to fetch details for
     * @return MovieDetailsResponse containing details of the movie
     */
    public MovieDetailsResponse findMovieFromTMDB(Integer id) {
        String apiPath = "/movie/" + id + "?language=en-US";

        TmdbMovieDetailsResponse response = webClient.get()
                .uri(apiPath)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new WebFluxClientException(errorMessage))))
                .onStatus(HttpStatusCode::is5xxServerError, resp ->
                        resp.bodyToMono(String.class)
                                .flatMap(errorMessage -> Mono.error(new WebFluxServerException(errorMessage))))
                .bodyToMono(TmdbMovieDetailsResponse.class).block();

        assert response != null;

        return toDto(response);
    }

    /**
     * Converts TmdbMovieDetailsResponse to MovieDetailsResponse.
     * @param tmdbMovieDetails The TmdbMovieDetailsResponse instance to convert
     * @return MovieDetailsResponse instance containing the same data
     */
    private MovieDetailsResponse toDto(TmdbMovieDetailsResponse tmdbMovieDetails) {
        return new MovieDetailsResponse(
                tmdbMovieDetails.id(),
                tmdbMovieDetails.title(),
                tmdbMovieDetails.overview(),
                tmdbMovieDetails.runtime(),
                tmdbMovieDetails.status(),
                tmdbMovieDetails.poster_path(),
                tmdbMovieDetails.genres().stream().map(Genre::name).toList()
        );
    }
}