package dat3.kino.services;

import dat3.kino.entities.SeatPricing;
import dat3.kino.exception.SeatPricingNotFoundExeption;
import dat3.kino.repositories.SeatPricingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing seat pricing.
 */
@Service
public class SeatPricingService {
    private final SeatPricingRepository seatPricingRepository;

    /**
     * Constructor for SeatPricingService.
     *
     * @param seatPricingRepository the repository for seat pricing
     */
    public SeatPricingService(SeatPricingRepository seatPricingRepository) {
        this.seatPricingRepository = seatPricingRepository;
    }

    /**
     * Fetches a seat pricing by its name.
     *
     * @param name the name of the seat pricing
     * @return the seat pricing with the given name
     * @throws SeatPricingNotFoundExeption if no seat pricing with the given name is found
     */
    public SeatPricing getSeatPricing(String name) {
        return seatPricingRepository.findById(name).orElseThrow(()-> new SeatPricingNotFoundExeption("SeatPricing", name));
    }

    /**
     * Creates a new seat pricing.
     *
     * @param seatPricing the seat pricing to create
     */
    public void createSeatPricing(SeatPricing seatPricing) {
        seatPricingRepository.save(seatPricing);
    }

    /**
     * Fetches all seat pricings.
     *
     * @return a list of all seat pricings
     */
    public List<SeatPricing> readAllSeatPricing() {
        return seatPricingRepository.findAll();
    }
}