package dat3.kino.services;


import dat3.kino.dto.request.MovieScreeningRequest;
import dat3.kino.dto.request.ScreeningRequest;
import dat3.kino.dto.response.ScreeningResponse;
import dat3.kino.entities.Screening;
import dat3.kino.repositories.AuditoriumRepository;
import dat3.kino.repositories.MovieRepository;
import dat3.kino.repositories.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScreeningService {


    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final AuditoriumRepository auditoriumRepository;
    private final AuditoriumService auditoriumService;
    private final MovieService movieService;

    public ScreeningService(ScreeningRepository screeningRepository, MovieRepository movieRepository, AuditoriumRepository auditoriumRepository,
                            AuditoriumService auditoriumService, MovieService movieService) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.auditoriumRepository = auditoriumRepository;
        this.auditoriumService = auditoriumService;
        this.movieService = movieService;
    }


    public ScreeningResponse createScreening(ScreeningRequest screeningRequest) {
        Screening newScreening = screeningRepository.save(toEntity(screeningRequest));
        return toDTO(newScreening);
    }

    public List<ScreeningResponse> readMovieScreeningsInCinemaByStartAndEndDate(MovieScreeningRequest movieScreeningRequest) {
        return screeningRepository.findAllByStartTimeBetweenAndAuditorium_Cinema_IdAndMovieId(movieScreeningRequest.startDate(), movieScreeningRequest.endDate() ,
                movieScreeningRequest.cinemaId(), movieScreeningRequest.movieId()).stream().map(this::toDTO).toList();
    }


    private ScreeningResponse toDTO(Screening screening) {
        return new ScreeningResponse(
                screening.getId(),
                auditoriumService.readSingleAuditorium(screening.getAuditorium().getId()),
                movieService.readMovie(screening.getMovie().getId()),
                screening.getStartTime(),
                screening.getIs3d()
        );
    }

    private Screening toEntity(ScreeningRequest screeningRequest) {
        return new Screening(
                screeningRequest.startTime(),
                movieRepository.findById(screeningRequest.movieId()).orElse(null),
                auditoriumRepository.findById(screeningRequest.auditoriumId()).orElse(null),
                screeningRequest.is3D()
        );
    }
}
