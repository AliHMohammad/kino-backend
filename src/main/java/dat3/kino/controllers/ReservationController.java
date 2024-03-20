package dat3.kino.controllers;

import dat3.kino.dto.request.ReservationRequest;
import dat3.kino.dto.response.ReservationResponse;
import dat3.kino.entities.Reservation;
import dat3.kino.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import dat3.kino.dto.request.ReservationPriceRequest;
import dat3.kino.dto.response.ReservationPriceResponse;
import dat3.kino.services.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reservationPrice")
    public ResponseEntity<ReservationPriceResponse> calculateReservationPrice(@RequestBody ReservationPriceRequest reservationPriceRequest) {
        return ResponseEntity.ok(reservationService.calculateReservationPrice(reservationPriceRequest));
    }


    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest reservationRequest, Principal principal) {
        return ResponseEntity.ok(reservationService.createReservation(reservationRequest, principal.getName()));
    }

    @GetMapping("/reservations/users/{userId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByUserId(@PathVariable("userId") String userId) {
        return ResponseEntity.ok(reservationService.getAllReservationsByUserName(userId));
    }

}
