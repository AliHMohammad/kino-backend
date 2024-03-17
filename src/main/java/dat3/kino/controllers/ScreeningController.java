package dat3.kino.controllers;


import dat3.kino.dto.request.ScreeningRequest;
import dat3.kino.dto.response.MovieResponse;
import dat3.kino.dto.response.ScreeningResponse;
import dat3.kino.services.ScreeningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
}
