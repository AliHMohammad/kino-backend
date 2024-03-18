package dat3.kino.controllers;


import dat3.kino.dto.request.MovieScreeningRequest;
import dat3.kino.dto.request.ScreeningRequest;
import dat3.kino.dto.response.MovieResponse;
import dat3.kino.dto.response.ScreeningResponse;
import dat3.kino.services.ScreeningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ScreeningController {


    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }


    @PostMapping("/screenings")
    public ResponseEntity<ScreeningResponse> createScreening(@RequestBody ScreeningRequest screeningRequest) {
        ScreeningResponse createdScreening = screeningService.createScreening(screeningRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdScreening.id())
                .toUri();

        return ResponseEntity.created(location).body(createdScreening);
    }

    @GetMapping("/screenings")
    public ResponseEntity<List<ScreeningResponse>> readMovieScreeningsInCinemaByStartAndEndDate(
            @RequestParam Long movieId,
            @RequestParam Long cinemaId,
            @RequestParam String startDate,
            @RequestParam String endDate
            ) {
        return ResponseEntity.ok(screeningService.readMovieScreeningsInCinemaByStartAndEndDate(movieId, cinemaId, startDate, endDate));
    }
}
