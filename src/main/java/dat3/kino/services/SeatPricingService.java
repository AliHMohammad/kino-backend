package dat3.kino.services;

import dat3.kino.entities.SeatPricing;
import dat3.kino.exception.SeatPricingNotFoundExeption;
import dat3.kino.repositories.SeatPricingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatPricingService {
    private final SeatPricingRepository seatPricingRepository;

    public SeatPricingService(SeatPricingRepository seatPricingRepository) {
        this.seatPricingRepository = seatPricingRepository;
    }

    public SeatPricing getSeatPricing(String name) {
        return seatPricingRepository.findById(name).orElseThrow(()-> new SeatPricingNotFoundExeption("SeatPricing", name));
    }

    public void createSeatPricing(SeatPricing seatPricing) {
        seatPricingRepository.save(seatPricing);
    }

    public List<SeatPricing> readAllSeatPricing() {
        return seatPricingRepository.findAll();
    }
}