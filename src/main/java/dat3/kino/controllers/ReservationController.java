package dat3.kino.controllers;

import dat3.kino.dto.request.ReservationPriceRequest;
import dat3.kino.dto.response.ReservationPriceResponse;
import dat3.kino.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservationPrice")
    public ResponseEntity<ReservationPriceResponse> calculateReservationPrice(@RequestBody ReservationPriceRequest reservationPriceRequest) {
        return ResponseEntity.ok(reservationService.calculateReservationPrice(reservationPriceRequest));

    }
}
