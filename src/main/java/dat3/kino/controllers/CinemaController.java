package dat3.kino.controllers;

import dat3.kino.entities.Cinema;
import dat3.kino.services.CinemaService;
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
    public List<Cinema> getCinemas() {
        return cinemaService.readAllCinemas();
    }

    @GetMapping("/cinemas/{id}")
    public Cinema getCinema(@PathVariable Long id) {
        return cinemaService.readCinema(id);
    }
}
