package dat3.kino.configuration;

import dat3.kino.entities.Auditorium;
import dat3.kino.entities.Cinema;
import dat3.kino.entities.Movie;
import dat3.kino.entities.SeatPricing;
import dat3.kino.repositories.AuditoriumRepository;
import dat3.kino.repositories.MovieRepository;
import dat3.kino.services.AuditoriumService;
import dat3.kino.services.CinemaService;
import dat3.kino.services.MovieService;
import dat3.kino.services.SeatPricingService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SetupCinemaData implements ApplicationRunner {
    private final CinemaService cinemaService;
    private final AuditoriumService auditoriumService;
    private final SeatPricingService seatPricingService;
    private final MovieService movieService;
    private final MovieRepository movieRepository;
    private final AuditoriumRepository auditoriumRepository;

    public SetupCinemaData(CinemaService cinemaService, AuditoriumService auditoriumService, SeatPricingService seatPricingService, MovieService movieService,
                           MovieRepository movieRepository, AuditoriumRepository auditoriumRepository) {
        this.cinemaService = cinemaService;
        this.auditoriumService = auditoriumService;
        this.seatPricingService = seatPricingService;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
        this.auditoriumRepository = auditoriumRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Application is running");
        init();
    }

    private void init() {
        Cinema cinema1 = new Cinema("Empire Bio", "Copenhagen", true);
        Cinema cinema2 = new Cinema("Cinemaxx", "Aarhus", true);

        SeatPricing cowboy = new SeatPricing("cowboy", 50);
        SeatPricing standard = new SeatPricing("standard", 75);
        SeatPricing deluxe = new SeatPricing("deluxe", 100);

        Auditorium auditorium1 = new Auditorium("Sal 1", cinema1);
        Auditorium auditorium2 = new Auditorium("Sal 2", cinema1);
        Auditorium auditorium3 = new Auditorium("Sal 1", cinema2);
        Auditorium auditorium4 = new Auditorium("Sal 2", cinema2);
        Auditorium auditorium5 = new Auditorium("Sal 3", cinema2);
        Auditorium auditorium6 = new Auditorium("Sal 4", cinema2);

        Movie movie1 = new Movie(693134L, "Dune Part Two", "/5aUVLiqcW0kFTBfGsCWjvLas91w.jpg", 170, LocalDate.of(2024, 2, 27));
        Movie movie2 = new Movie(1011985L, "Kung Fu Panda 4", "/kDp1vUBnMpe8ak4rjgl3cLELqjU.jpg", 90, LocalDate.of(2024, 3, 2));
        Movie movie3 = new Movie(1119544L, "Rom", "/vdex5zUokgWTnsE4iZt9BTWpNdM.jpg", 98, LocalDate.of(2024, 2, 22));
        Movie movie4 = new Movie(802219L, "Bob Marley: One Love", "/mKWalirPreEdCKDJjc5TKeOP2xi.jpg", 104, LocalDate.of(2024, 2, 14));
        Movie movie5 = new Movie(1078249L, "Den grænseløse", "/1T1nn9fAb1N46knsqaBiIpxm4M2.jpg", 132, LocalDate.of(2024, 2, 1));

        // initCinemas
        if (cinemaService.readAllCinemas().isEmpty()) {
            System.out.println("Creating cinemas");

            cinemaService.createCinema(cinema1);
            cinemaService.createCinema(cinema2);
        }
        // initSeatPricing
        if (seatPricingService.readAllSeatPricing().isEmpty()) {
            System.out.println("Creating seat pricing");

            seatPricingService.createSeatPricing(cowboy);
            seatPricingService.createSeatPricing(standard);
            seatPricingService.createSeatPricing(deluxe);
        }
        // initAuditoriums
        if (auditoriumRepository.findAll().isEmpty()) {
            System.out.println("Creating auditoriums");

            auditoriumService.createAuditorium(auditorium1, 20, 12);
            auditoriumService.createAuditorium(auditorium2, 24, 16);
            auditoriumService.createAuditorium(auditorium3, 22, 12);
            auditoriumService.createAuditorium(auditorium4, 24, 12);
            auditoriumService.createAuditorium(auditorium5, 20, 14);
            auditoriumService.createAuditorium(auditorium6, 25, 16);
        }

        // initMovies
        if(movieService.readAllMovies().isEmpty()) {
            System.out.println("Creating movies");
            movieRepository.save(movie1);
            movieRepository.save(movie2);
            movieRepository.save(movie3);
            movieRepository.save(movie4);
            movieRepository.save(movie5);
        }
    }
}
