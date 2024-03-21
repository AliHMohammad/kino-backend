package dat3.kino.services;

import dat3.kino.dto.response.PriceAdjustmentResponse;
import dat3.kino.entities.PriceAdjustment;
import dat3.kino.repositories.PriceAdjustmentRepository;
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

    /**
     * Constructor for PriceAdjustmentService.
     *
     * @param priceAdjustmentRepository Repository for managing price adjustment data.
     */
    public PriceAdjustmentService(PriceAdjustmentRepository priceAdjustmentRepository) {
        this.priceAdjustmentRepository = priceAdjustmentRepository;
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