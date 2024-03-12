package dat3.kino.configuration;

import dat3.kino.entities.Auditorium;
import dat3.kino.entities.Cinema;
import dat3.kino.entities.SeatPricing;
import dat3.kino.services.AuditoriumService;
import dat3.kino.services.CinemaService;
import dat3.kino.services.SeatPricingService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SetupCinemaData implements ApplicationRunner {
    private final CinemaService cinemaService;
    private final AuditoriumService auditoriumService;
    private final SeatPricingService seatPricingService;

public SetupCinemaData(CinemaService cinemaService, AuditoriumService auditoriumService, SeatPricingService seatPricingService) {
        this.cinemaService = cinemaService;
        this.auditoriumService = auditoriumService;
        this.seatPricingService = seatPricingService;
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


    }


}
