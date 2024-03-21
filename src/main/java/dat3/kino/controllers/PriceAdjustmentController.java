package dat3.kino.controllers;

import dat3.kino.dto.response.PriceAdjustmentResponse;
import dat3.kino.services.PriceAdjustmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceAdjustmentController {
    private final PriceAdjustmentService priceAdjustmentService;


    public PriceAdjustmentController(PriceAdjustmentService priceAdjustmentService) {
        this.priceAdjustmentService = priceAdjustmentService;
    }

    @GetMapping("/priceadjustments")
    public ResponseEntity<PriceAdjustmentResponse> getPriceAdjustments() {
        return ResponseEntity.ok(priceAdjustmentService.readAllPriceAdjustments());
    }
}
