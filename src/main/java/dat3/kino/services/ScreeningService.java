package dat3.kino.services;

import dat3.kino.dto.request.ScreeningRequest;
import dat3.kino.dto.response.ScreeningResponse;
import dat3.kino.entities.Screening;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.AuditoriumRepository;
import dat3.kino.repositories.MovieRepository;
import dat3.kino.repositories.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service class for managing screenings.
 */
@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final AuditoriumRepository auditoriumRepository;
    private final AuditoriumService auditoriumService;
    private final MovieService movieService;

    /**
     * Constructor for ScreeningService.
     *
     * @param screeningRepository the repository for screenings
     * @param movieRepository the repository for movies
     * @param auditoriumRepository the repository for auditoriums
     * @param auditoriumService the service for auditoriums
     * @param movieService the service for movies
     */
    public ScreeningService(ScreeningRepository screeningRepository, MovieRepository movieRepository, AuditoriumRepository auditoriumRepository,
                            AuditoriumService auditoriumService, MovieService movieService) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.auditoriumRepository = auditoriumRepository;
        this.auditoriumService = auditoriumService;
        this.movieService = movieService;
    }

    /**
     * Fetches all screenings.
     *
     * @return a list of all screenings
     */
    public List<ScreeningResponse> readAllScreenings() {
        return screeningRepository.findAll().stream().map(this::toDTO).toList();
    }

    /**
     * Fetches a screening by its ID.
     *
     * @param id the ID of the screening
     * @return the screening with the given ID
     * @throws EntityNotFoundException if no screening with the given ID is found
     */
    public ScreeningResponse readScreeningById(Long id) {
        return screeningRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Screening", id));
    }

    /**
     * Creates a new screening.
     *
     * @param screeningRequest the request object containing the details of the new screening
     * @return the created screening
     */
    public ScreeningResponse createScreening(ScreeningRequest screeningRequest) {
        Screening newScreening = screeningRepository.save(toEntity(screeningRequest));
        return toDTO(newScreening);
    }

    /**
     * Fetches all screenings for a specific movie in a specific cinema within a specific time range.
     *
     * @param movieId the ID of the movie
     * @param cinemaId the ID of the cinema
     * @param startDate the start of the time range
     * @param endDate the end of the time range
     * @return a list of all matching screenings
     */
    public List<ScreeningResponse> readMovieScreeningsInCinemaByStartAndEndDate(Long movieId, Long cinemaId, String startDate, String endDate) {
        return screeningRepository.findAllByStartTimeBetweenAndAuditorium_Cinema_IdAndMovieIdOrderByStartTime(toLocalDateTime(startDate), toLocalDateTime(endDate),
                cinemaId, movieId).stream().map(this::toDTO).toList();
    }

    /**
     * Converts a Screening entity to a ScreeningResponse DTO.
     *
     * @param screening the Screening entity to convert
     * @return the converted ScreeningResponse DTO
     */
    private ScreeningResponse toDTO(Screening screening) {
        return new ScreeningResponse(
                screening.getId(),
                auditoriumService.readSingleAuditorium(screening.getAuditorium().getId()),
                movieService.readMovie(screening.getMovie().getId()),
                screening.getStartTime(),
                screening.getIs3d()
        );
    }

    /**
     * Converts a ScreeningRequest DTO to a Screening entity.
     *
     * @param screeningRequest the ScreeningRequest DTO to convert
     * @return the converted Screening entity
     */
    private Screening toEntity(ScreeningRequest screeningRequest) {
        return new Screening(
                screeningRequest.startTime(),
                movieRepository.findById(screeningRequest.movieId()).orElse(null),
                auditoriumRepository.findById(screeningRequest.auditoriumId()).orElse(null),
                screeningRequest.is3D()
        );
    }

    /**
     * Converts a string to a LocalDateTime.
     *
     * @param date the string to convert
     * @return the converted LocalDateTime
     */
    private LocalDateTime toLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        return LocalDateTime.parse(date, formatter);
    }
}