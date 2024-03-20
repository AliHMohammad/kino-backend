package dat3.kino.controllers;

import dat3.kino.dto.request.ReservationRequest;
import dat3.kino.dto.response.ReservationResponse;
import dat3.kino.entities.Reservation;
import dat3.kino.services.ReservationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ReservationController {


    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @PostMapping("/reservations")
    public ReservationResponse createReservation(@RequestBody ReservationRequest reservationRequest, Principal principal) {
        return reservationService.createReservation(reservationRequest, principal.getName());
    }
}
