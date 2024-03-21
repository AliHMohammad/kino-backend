package dat3.kino.configuration;

import dat3.kino.dto.request.ScreeningRequest;
import dat3.kino.entities.*;
import dat3.kino.repositories.*;
import dat3.kino.services.*;
import dat3.security.entities.UserWithRoles;
import dat3.security.services.UserWithRolesService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Component
public class SetupCinemaData implements ApplicationRunner, Ordered {
    private final CinemaService cinemaService;
    private final AuditoriumService auditoriumService;
    private final SeatPricingService seatPricingService;
    private final MovieService movieService;
    private final MovieRepository movieRepository;
    private final AuditoriumRepository auditoriumRepository;
    private final ScreeningService screeningService;
    private final ReservationService reservationService;
    private final UserWithRolesService userWithRolesService;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final PriceAdjustmentRepository priceAdjustmentRepository;
    private final ReservationRepository reservationRepository;

    public SetupCinemaData(CinemaService cinemaService, AuditoriumService auditoriumService, SeatPricingService seatPricingService, MovieService movieService,
                           MovieRepository movieRepository, AuditoriumRepository auditoriumRepository, ScreeningService screeningService,
                           ReservationService reservationService, UserWithRolesService userWithRolesService, ScreeningRepository screeningRepository,
                           SeatRepository seatRepository, PriceAdjustmentRepository priceAdjustmentRepository, ReservationRepository reservationRepository) {
        this.cinemaService = cinemaService;
        this.auditoriumService = auditoriumService;
        this.seatPricingService = seatPricingService;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
        this.auditoriumRepository = auditoriumRepository;
        this.screeningService = screeningService;
        this.reservationService = reservationService;
        this.userWithRolesService = userWithRolesService;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.priceAdjustmentRepository = priceAdjustmentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public int getOrder() {
        return 2;
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

        PriceAdjustment priceAdjustmentSmallGroup = new PriceAdjustment("smallGroup", 1.05);
        PriceAdjustment priceAdjustmentLargeGroup = new PriceAdjustment("largeGroup", 0.93);
        PriceAdjustment priceAdjustment3DFee = new PriceAdjustment("fee3D", 30);
        PriceAdjustment priceAdjustmentRuntimeFee = new PriceAdjustment("feeRuntime", 20);


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

        // initPriceAdjustment
        if (priceAdjustmentRepository.findAll()
                .isEmpty()) {
            System.out.println("Creating price adjustments");

            priceAdjustmentRepository.save(priceAdjustmentSmallGroup);
            priceAdjustmentRepository.save(priceAdjustmentLargeGroup);
            priceAdjustmentRepository.save(priceAdjustment3DFee);
            priceAdjustmentRepository.save(priceAdjustmentRuntimeFee);
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

        // initScreenings
        if(screeningService.readAllScreenings().isEmpty()) {
            LocalDateTime currentDateFirst = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(1);
            LocalDateTime currentDateSecond = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(4);
            LocalDateTime currentDateThird = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(7);
            System.out.println("Creating screenings");
            screeningService.createScreening(new ScreeningRequest(693134L, 1L, currentDateFirst, true));
            screeningService.createScreening(new ScreeningRequest(1011985L, 2L, currentDateFirst, true));
            screeningService.createScreening(new ScreeningRequest(1119544L, 3L, currentDateFirst, true));
            screeningService.createScreening(new ScreeningRequest(802219L, 4L, currentDateFirst, true));
            screeningService.createScreening(new ScreeningRequest(1078249L, 5L, currentDateFirst, true));
            screeningService.createScreening(new ScreeningRequest(1078249L, 6L, currentDateFirst, true));

            screeningService.createScreening(new ScreeningRequest(693134L, 1L, currentDateSecond, false));
            screeningService.createScreening(new ScreeningRequest(1011985L, 2L, currentDateSecond, false));
            screeningService.createScreening(new ScreeningRequest(1119544L, 3L, currentDateSecond, false));
            screeningService.createScreening(new ScreeningRequest(802219L, 4L, currentDateSecond, true));
            screeningService.createScreening(new ScreeningRequest(1078249L, 5L, currentDateSecond, true));
            screeningService.createScreening(new ScreeningRequest(1078249L, 6L, currentDateSecond, true));

            screeningService.createScreening(new ScreeningRequest(693134L, 1L, currentDateThird, true));
            screeningService.createScreening(new ScreeningRequest(1011985L, 2L, currentDateThird, true));
            screeningService.createScreening(new ScreeningRequest(1119544L, 3L, currentDateThird, true));
            screeningService.createScreening(new ScreeningRequest(802219L, 4L, currentDateThird, false));
            screeningService.createScreening(new ScreeningRequest(1078249L, 5L, currentDateThird, false));
            screeningService.createScreening(new ScreeningRequest(1078249L, 6L, currentDateThird, false));
        }

    }
}
