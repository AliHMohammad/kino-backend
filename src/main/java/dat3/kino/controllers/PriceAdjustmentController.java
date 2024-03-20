package dat3.kino.controllers;

import dat3.kino.dto.response.PriceAdjustmentResponse;
import dat3.kino.entities.PriceAdjustment;
import dat3.kino.services.PriceAdjustmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PriceAdjustmentController {
    private final PriceAdjustmentService priceAdjustmentService;


    public PriceAdjustmentController(PriceAdjustmentService priceAdjustmentService) {
        this.priceAdjustmentService = priceAdjustmentService;
    }

    @GetMapping("/priceAdjustments")
    public ResponseEntity<PriceAdjustmentResponse> getPriceAdjustments() {
        return ResponseEntity.ok(priceAdjustmentService.readAllPriceAdjustments());
    }
}