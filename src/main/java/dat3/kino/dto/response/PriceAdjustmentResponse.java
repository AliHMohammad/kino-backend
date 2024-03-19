package dat3.kino.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


public record PriceAdjustmentResponse(
        Map<String, Double> priceAdjustments
) {
}


