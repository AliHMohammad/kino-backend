package dat3.kino.configuration;

import dat3.kino.entities.Auditorium;
import dat3.kino.entities.Cinema;
import dat3.kino.entities.Movie;
import dat3.kino.entities.SeatPricing;
import dat3.kino.services.AuditoriumService;
import dat3.kino.services.CinemaService;
import dat3.kino.services.MovieService;
import dat3.kino.services.SeatPricingService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class SetupCinemaData implements ApplicationRunner {
    private final CinemaService cinemaService;
    private final AuditoriumService auditoriumService;
    private final SeatPricingService seatPricingService;
    private final MovieService movieService;

public SetupCinemaData(CinemaService cinemaService, AuditoriumService auditoriumService, SeatPricingService seatPricingService, MovieService movieService) {
        this.cinemaService = cinemaService;
        this.auditoriumService = auditoriumService;
        this.seatPricingService = seatPricingService;
        this.movieService = movieService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Application is running");
        init();

    }

    private void init() {
        // initCinemas
        Cinema cinema1 = new Cinema("Empire Bio", "Copenhagen", true);
        Cinema cinema2 = new Cinema("Cinemaxx", "Aarhus", true);

        System.out.println("Creating cinemas");

        cinemaService.createCinema(cinema1);
        cinemaService.createCinema(cinema2);

        // initSeatPricing
        System.out.println("Creating seat pricing");
        System.out.println("------------------------------------");

        SeatPricing cowboy = new SeatPricing("cowboy", 50);
        SeatPricing standard = new SeatPricing("standard", 75);
        SeatPricing deluxe = new SeatPricing("deluxe", 100);

        seatPricingService.createSeatPricing(cowboy);
        seatPricingService.createSeatPricing(standard);
        seatPricingService.createSeatPricing(deluxe);


        System.out.println("Creating auditoriums");
        System.out.println("------------------------------------");
        // initAuditoriums
        Auditorium auditorium1 = new Auditorium("Sal 1", cinema1);
        Auditorium auditorium2 = new Auditorium("Sal 2", cinema1);
        Auditorium auditorium3 = new Auditorium("Sal 1", cinema2);
        Auditorium auditorium4 = new Auditorium("Sal 2", cinema2);
        Auditorium auditorium5 = new Auditorium("Sal 3", cinema2);
        Auditorium auditorium6 = new Auditorium("Sal 4", cinema2);

        auditoriumService.createAuditorium(auditorium1, 20, 12);
        auditoriumService.createAuditorium(auditorium2, 24, 16);
        auditoriumService.createAuditorium(auditorium3, 22, 12);
        auditoriumService.createAuditorium(auditorium4, 24, 12);
        auditoriumService.createAuditorium(auditorium5, 20, 14);
        auditoriumService.createAuditorium(auditorium6, 25, 16);


        // initMovies
        System.out.println("Creating movies");
        System.out.println("------------------------------------");
        Movie movie1 = new Movie("Dune Part Two", "Dune poster", 170, new Date(), LocalDateTime.now());
        Movie movie2 = new Movie("Kung Fu Panda", "Kung Fu Poster", 90, new Date(), LocalDateTime.now());
        Movie movie3 = new Movie("Rom", "Rom poster", 98, new Date(), LocalDateTime.now());
        Movie movie4 = new Movie("Bob Marley: One Love", "Bob Marley poster", 104, new Date(), LocalDateTime.now());
        Movie movie5 = new Movie("Den grænseløse", "Den grænseløse poster", 132, new Date(), LocalDateTime.now());
         movieService.createMovie(movie1);
         movieService.createMovie(movie2);
         movieService.createMovie(movie3);
         movieService.createMovie(movie4);
         movieService.createMovie(movie5);



    }


}
