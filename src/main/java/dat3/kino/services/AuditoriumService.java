package dat3.kino.services;

import dat3.kino.entities.Auditorium;
import dat3.kino.entities.Seat;
import dat3.kino.entities.SeatPricing;
import dat3.kino.exception.SeatPricingNotFoundExeption;
import dat3.kino.repositories.AuditoriumRepository;
import dat3.kino.repositories.SeatPricingRepository;
import dat3.kino.repositories.SeatRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriumService {
    private final AuditoriumRepository auditoriumRepository;
    private final SeatRepository seatRepository;
    private final SeatPricingRepository seatPricingRepository;

    public AuditoriumService(AuditoriumRepository auditoriumRepository, SeatRepository seatRepository, SeatPricingRepository seatPricingRepository) {
        this.auditoriumRepository = auditoriumRepository;
        this.seatRepository = seatRepository;
        this.seatPricingRepository = seatPricingRepository;
    }

    public Auditorium createAuditorium(Auditorium newAuditorium, int rows, int seatsPerRow) {
        Auditorium auditorium = auditoriumRepository.save(newAuditorium);
        // Create seats
        for (int i = 0; i < rows; i++) {
            for(int j =0; j < seatsPerRow; j++) {
                Seat seat = new Seat();
                seat.setAuditorium(auditorium);
                int rowNum = i + 1;
                int seatNum = j + 1;
                seat.setRowNum(rowNum);
                seat.setSeatNum(seatNum);
                seat.setSeatPricing(getSeatPricing(rows, rowNum));
                seatRepository.save(seat);
            }
        }
        return auditorium;
    }


    private SeatPricing getSeatPricing(int rows, int rowNum) {

        if (rowNum <= 2) {
            return seatPricingRepository.findById("cowboy").orElseThrow(() -> new SeatPricingNotFoundExeption("SeatPricing", "cowboy"));
        } else if (rowNum == rows) {
            return seatPricingRepository.findById("deluxe").orElseThrow(() -> new SeatPricingNotFoundExeption("SeatPricing", "deluxe"));
        } else {
            return seatPricingRepository.findById("standard").orElseThrow(() -> new SeatPricingNotFoundExeption("SeatPricing", "standard"));
        }
    }
}
