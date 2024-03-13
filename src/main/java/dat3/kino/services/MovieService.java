package dat3.kino.services;

import dat3.kino.dto.response.TmdbMovieDetails;
import dat3.kino.entities.Movie;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final WebClient webClient;

    @Value("${tmdb.api.key}")
    private String apiKey;



    public MovieService(MovieRepository movieRepository, WebClient.Builder webClientBuilder) {
        this.movieRepository = movieRepository;
        this.webClient = webClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public List<Movie> readAllMovies() {
        return movieRepository.findAll();
    }

    public Movie readMovie(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("movie", id));
    }

    public TmdbMovieDetails findMovieFromTMDB(Integer id) {
        String apiPath = "/movie/" + id + "?language=en-US";
        Mono<TmdbMovieDetails> response = webClient.get()
                .uri(apiPath)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .retrieve()
                .bodyToMono(TmdbMovieDetails.class);
        return response.block();
    }

    public Movie createMovie(Movie newMovie) {
        return movieRepository.save(newMovie);
    }
}
