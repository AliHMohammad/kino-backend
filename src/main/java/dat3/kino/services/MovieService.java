package dat3.kino.services;

import dat3.kino.dto.request.MovieRequest;
import dat3.kino.dto.response.MovieResponse;
import dat3.kino.entities.Movie;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieResponse> readAllMovies() {
        return movieRepository.findAll().stream().map(this::toDto).toList();
    }

    public MovieResponse readMovie(Long id) {
        return movieRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("movie", id));
    }

    public MovieResponse createMovie(MovieRequest movieRequest) {
        Movie newMovie = toEntity(movieRequest);
        movieRepository.save(newMovie);
        return toDto(newMovie);
    }

    private MovieResponse toDto(Movie movie) {
        return new MovieResponse(
            movie.getId(),
            movie.getTitle(),
            movie.getPoster(),
            movie.getRuntime(),
            movie.getPremiere()
        );
    }

    private Movie toEntity(MovieRequest movieRequest) {
        return new Movie(
                movieRequest.id(),
                movieRequest.title(),
                movieRequest.poster(),
                movieRequest.runtime(),
                movieRequest.premiere()
        );
    }
}
