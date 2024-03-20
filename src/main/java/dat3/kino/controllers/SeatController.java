package dat3.kino.controllers;

import dat3.kino.dto.response.SeatResponse;
import dat3.kino.services.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/seats/auditorium/{auditoriumId}")
    public ResponseEntity<List<SeatResponse>> getSeatsFromAuditorium(@PathVariable Long auditoriumId) {
        return ResponseEntity.ok(seatService.readAllSeatsFromAuditorium(auditoriumId));
    }

    @GetMapping("/seats/screening/{screeningId}")
    public ResponseEntity<List<SeatResponse>> getReservedSeatsByScreeningId(@PathVariable("screeningId") Long screeningId) {
        return ResponseEntity.ok(seatService.GetAllReservedSeats(screeningId));
    }
}
