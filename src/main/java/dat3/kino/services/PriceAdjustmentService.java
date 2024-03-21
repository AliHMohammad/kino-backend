package dat3.kino.services;

import dat3.kino.dto.response.PriceAdjustmentResponse;
import dat3.kino.entities.PriceAdjustment;
import dat3.kino.entities.Screening;
import dat3.kino.entities.Seat;
import dat3.kino.repositories.PriceAdjustmentRepository;
import dat3.kino.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for managing price adjustments.
 */
@Service
public class PriceAdjustmentService {

    private final PriceAdjustmentRepository priceAdjustmentRepository;
    private final SeatRepository seatRepository;


    public PriceAdjustmentService(PriceAdjustmentRepository priceAdjustmentRepository, SeatRepository seatRepository) {
        this.priceAdjustmentRepository = priceAdjustmentRepository;
        this.seatRepository = seatRepository;
    }

    /**
     * Retrieves all price adjustments.
     *
     * @return A response object containing all price adjustments.
     */
    public PriceAdjustmentResponse readAllPriceAdjustments() {
        List<PriceAdjustment> priceAdjustments = priceAdjustmentRepository.findAll();

        return toDto(priceAdjustments);
    }


    public double calculateSeatsPrice(List<Long> seats) {
        List<Seat> seatList = seatRepository.findAllById(seats);

        return seatList.stream()
                .reduce(0.0, (subtotal, seat) -> subtotal + seat.getSeatPricing()
                        .getPrice(), Double::sum);
    }

    public double calculateFees(Screening screening, String groupSize, double seatsSum, Map<String, Double> priceAdjustments, int numOfSeats) {

        double FEE_3D = screening.getIs3d() ? priceAdjustments.get("fee3D") : 0;

        double FEE_RUNTIME = screening.getMovie()
                .getRuntime() > 160 ? priceAdjustments.get("feeRuntime") : 0;


        double feeSum = 0;

        if (groupSize.equals("smallGroup")) feeSum = priceAdjustments.get("smallGroup") * seatsSum - seatsSum;
        if (FEE_3D > 0) feeSum += FEE_3D * numOfSeats;
        if (FEE_RUNTIME > 0) feeSum += FEE_RUNTIME * numOfSeats;

        return feeSum;
    }

    public double calculateDiscount(String groupSize, double seatsSum, Map<String, Double> priceAdjustments) {
        double discountSum = 0;

        if (groupSize.equals("largeGroup"))
            discountSum += Math.abs(priceAdjustments.get("largeGroup") * seatsSum - seatsSum);

        return discountSum;

    }

  /**
     * Converts a list of PriceAdjustment entities to a PriceAdjustmentResponse DTO.
     *
     * @param priceAdjustmentsList The list of PriceAdjustment entities to convert.
     * @return The converted PriceAdjustmentResponse DTO.
     */
    private PriceAdjustmentResponse toDto(List<PriceAdjustment> priceAdjustmentsList) {

        Map<String, Double> priceAdjustments = priceAdjustmentsList
                .stream()
                .collect(
                        Collectors.toMap(
                                PriceAdjustment::getName,
                                PriceAdjustment::getAdjustment
                        )
                );
        return new PriceAdjustmentResponse(priceAdjustments);
    }
}