package dat3.kino.controllers;

import dat3.kino.dto.response.AuditoriumResponse;
import dat3.kino.dto.response.CinemaResponse;
import dat3.kino.services.CinemaService;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class CinemaController {
    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/cinemas")
    public ResponseEntity<List<CinemaResponse>> getCinemas() {
        List<CinemaResponse> cinemas = cinemaService.readAllCinemas();

        CacheControl cacheControl = CacheControl.maxAge(24, TimeUnit.HOURS).cachePublic();

        return ResponseEntity.ok().cacheControl(cacheControl).body(cinemas);
    }

    @GetMapping("/cinemas/{cinemaId}/auditoriums")
    public ResponseEntity<List<AuditoriumResponse>> getAllAuditoriumsByCinemaId(@PathVariable("cinemaId") Long cinemaId) {
        return ResponseEntity.ok(cinemaService.readAllAuditoriumsByCinemaId(cinemaId));
    }

    @GetMapping("/cinemas/{id}")
    public ResponseEntity<CinemaResponse> getCinema(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.readCinema(id));
    }
}
