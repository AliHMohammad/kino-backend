package dat3.kino.controllers;

import dat3.kino.dto.response.CinemaResponse;
import dat3.kino.services.CinemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CinemaController {
    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/cinemas")
    public ResponseEntity<List<CinemaResponse>> getCinemas() {
        return ResponseEntity.ok(cinemaService.readAllCinemas());
    }

    @GetMapping("/cinemas/{id}")
    public ResponseEntity<CinemaResponse> getCinema(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.readCinema(id));
    }
}
