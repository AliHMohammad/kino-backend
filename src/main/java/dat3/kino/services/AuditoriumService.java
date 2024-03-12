package dat3.kino.services;

import dat3.kino.entities.Auditorium;
import dat3.kino.entities.Seat;
import dat3.kino.entities.SeatPricing;
import dat3.kino.repositories.AuditoriumRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriumService {
    private final AuditoriumRepository auditoriumRepository;
    private final SeatService seatService;
    private final SeatPricingService seatPricingService;

    public AuditoriumService(AuditoriumRepository auditoriumRepository, SeatService seatService, SeatPricingService seatPricingService) {
        this.auditoriumRepository = auditoriumRepository;
        this.seatService = seatService;
        this.seatPricingService = seatPricingService;
    }

    public Auditorium createAuditorium(Auditorium newAuditorium, int rows, int seatsPerRow) {
        Auditorium auditorium = auditoriumRepository.save(newAuditorium);
        // Create seats
        for (int i = 0; i < rows; i++) {
            for(int j =0; j < seatsPerRow; j++) {
                int rowNum = i + 1;
                int seatNum = j + 1;
                SeatPricing seatPricing = getSeatPricing(rows, rowNum);
                seatService.createSeat(new Seat(seatNum, rowNum, seatPricing, auditorium));
            }
        }
        return auditorium;
    }


    private SeatPricing getSeatPricing(int rows, int rowNum) {

        if (rowNum <= 2) {
            return seatPricingService.getSeatPricing("cowboy");
        } else if (rowNum == rows) {
            return seatPricingService.getSeatPricing("deluxe");
        } else {
            return seatPricingService.getSeatPricing("standard");
        }
    }
}
