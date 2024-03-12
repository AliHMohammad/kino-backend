package dat3.kino.services;

import dat3.kino.entities.SeatPricing;
import dat3.kino.repositories.SeatPricingRepository;
import org.springframework.stereotype.Service;

@Service
public class SeatPricingService {
    private final SeatPricingRepository seatPricingRepository;

    public SeatPricingService(SeatPricingRepository seatPricingRepository) {
        this.seatPricingRepository = seatPricingRepository;
    }

    public SeatPricing getSeatPricing(String name) {
        return seatPricingRepository.findById(name).orElse(null);
    }

    public void createSeatPricing(SeatPricing seatPricing) {
        seatPricingRepository.save(seatPricing);
    }
}