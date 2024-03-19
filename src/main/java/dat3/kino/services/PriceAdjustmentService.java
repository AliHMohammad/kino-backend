package dat3.kino.services;

import dat3.kino.dto.response.PriceAdjustmentResponse;
import dat3.kino.entities.PriceAdjustment;
import dat3.kino.repositories.PriceAdjustmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceAdjustmentService {

    private final PriceAdjustmentRepository priceAdjustmentRepository;

    public PriceAdjustmentService(PriceAdjustmentRepository priceAdjustmentRepository) {
        this.priceAdjustmentRepository = priceAdjustmentRepository;
    }


    public PriceAdjustmentResponse readAllPriceAdjustments() {
        List<PriceAdjustment> priceAdjustments = priceAdjustmentRepository.findAll();

        return toDto(priceAdjustments);
    }

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
