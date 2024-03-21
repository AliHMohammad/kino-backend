package dat3.kino.services;

import dat3.kino.dto.request.MovieRequest;
import dat3.kino.dto.response.MovieResponse;
import dat3.kino.entities.Movie;
import dat3.kino.entities.Screening;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.MovieRepository;
import dat3.kino.repositories.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing movies.
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;

    /**
     * Constructor for MovieService.
     *
     * @param movieRepository Repository for managing movie data.
     * @param screeningRepository Repository for managing screening data.
     */
    public MovieService(MovieRepository movieRepository, ScreeningRepository screeningRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
    }

    /**
     * Retrieves all movies.
     *
     * @return A list of all movies.
     */
    public List<MovieResponse> readAllMovies() {
        return movieRepository.findAll().stream().map(this::toDto).toList();
    }

    /**
     * Retrieves movies by cinema ID.
     *
     * @param cinema The ID of the cinema.
     * @return A list of movies for the specified cinema.
     */
    public List<MovieResponse> readMoviesByCinema(Long cinema) {
        System.out.println("Cinema: " + cinema);

        List<Screening> screenings = screeningRepository.findScreeningsByAuditorium_Cinema_Id(cinema);

        Set<Movie> uniqueMovies = screenings.stream()
                .map(Screening::getMovie)
                .collect(Collectors.toSet());

        return uniqueMovies.stream().map(this::toDto).toList();
    }

    /**
     * Retrieves a single movie by its ID.
     *
     * @param id The ID of the movie to retrieve.
     * @return The retrieved movie.
     * @throws EntityNotFoundException If no movie with the specified ID is found.
     */
    public MovieResponse readMovie(Long id) {
        return movieRepository.findById(id).map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("movie", id));
    }

    /**
     * Creates a new movie.
     *
     * @param movieRequest The request object containing the details of the movie to be created.
     * @return The created movie.
     */
    public MovieResponse createMovie(MovieRequest movieRequest) {
        Movie newMovie = toEntity(movieRequest);
        movieRepository.save(newMovie);
        return toDto(newMovie);
    }

    /**
     * Converts a Movie entity to a MovieResponse DTO.
     *
     * @param movie The movie to convert.
     * @return The converted MovieResponse DTO.
     */
    private MovieResponse toDto(Movie movie) {
        return new MovieResponse(
            movie.getId(),
            movie.getTitle(),
            movie.getPoster(),
            movie.getRuntime(),
            movie.getPremiere()
        );
    }

    /**
     * Converts a MovieRequest DTO to a Movie entity.
     *
     * @param movieRequest The MovieRequest DTO to convert.
     * @return The converted Movie entity.
     */
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