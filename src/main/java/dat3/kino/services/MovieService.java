package dat3.kino.services;

import dat3.kino.dto.request.MovieRequest;
import dat3.kino.dto.response.MovieResponse;
import dat3.kino.entities.Movie;
import dat3.kino.entities.Screening;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.MovieRepository;
import dat3.kino.repositories.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;


    public MovieService(MovieRepository movieRepository, ScreeningRepository screeningRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
    }

    public List<MovieResponse> readAllMovies() {
        return movieRepository.findAll().stream().map(this::toDto).toList();
    }

    public List<MovieResponse> readMoviesByCinema(Long cinema) {
        System.out.println("Cinema: " + cinema);

        List<Screening> screenings = screeningRepository.findScreeningsByAuditorium_Cinema_Id(cinema);

        Set<Movie> uniqueMovies = screenings.stream()
                .map(Screening::getMovie)
                .collect(Collectors.toSet());

        return uniqueMovies.stream().map(this::toDto).toList();
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
