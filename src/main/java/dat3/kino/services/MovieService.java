package dat3.kino.services;

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

    public Movie createMovie(Movie newMovie) {
        return movieRepository.save(newMovie);
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
}
