package dat3.kino.services;

import dat3.kino.dto.response.AuditoriumResponse;
import dat3.kino.entities.Auditorium;
import dat3.kino.entities.Seat;
import dat3.kino.entities.SeatPricing;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.AuditoriumRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public AuditoriumResponse createAuditorium(Auditorium newAuditorium, int rows, int seatsPerRow) {
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
        return toDTO(auditorium);
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

    public AuditoriumResponse readSingleAuditorium(Long id) {
        return auditoriumRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("auditorium", id));
    }

    private AuditoriumResponse toDTO(Auditorium auditorium) {
        return new AuditoriumResponse(
                auditorium.getId(),
                auditorium.getName()
        );
    }
}
