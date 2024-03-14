package dat3.kino.controllers;

import dat3.kino.dto.response.MovieDetailsResponse;
import dat3.kino.dto.response.MovieResponse;
import dat3.kino.services.MovieService;
import dat3.kino.services.TMDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;
    private final TMDBService tmdbService;

    public MovieController(MovieService movieService, TMDBService tmdbService) {
        this.movieService = movieService;
        this.tmdbService = tmdbService;
    }

    @GetMapping("/movies")
    public ResponseEntity<List<MovieResponse>> getMovies() {
        return ResponseEntity.ok(movieService.readAllMovies());
    }

    @GetMapping("/movies/TMDB/{id}")
    public ResponseEntity<MovieDetailsResponse> getTMDBMovie(@PathVariable Integer id) {
        return ResponseEntity.ok(tmdbService.findMovieFromTMDB(id));
    }


}
