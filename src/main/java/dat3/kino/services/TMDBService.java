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


@Service
public class TMDBService {
    private final WebClient webClient;

    @Value("${tmdb.api.key}")
    private String apiKey;

    public TMDBService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

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
