package dat3.kino.services;

import dat3.kino.entities.PriceAdjustment;
import dat3.kino.repositories.PriceAdjustmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceAdjustmentService {

    private final PriceAdjustmentRepository priceAdjustmentRepository;

    public PriceAdjustmentService(PriceAdjustmentRepository priceAdjustmentRepository) {
        this.priceAdjustmentRepository = priceAdjustmentRepository;
    }


    public List<PriceAdjustment>readAllPriceAdjustments() {
        return priceAdjustmentRepository.findAll();
    }

}
