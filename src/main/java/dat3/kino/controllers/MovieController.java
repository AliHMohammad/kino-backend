package dat3.kino.controllers;

import dat3.kino.dto.request.MovieRequest;
import dat3.kino.dto.response.MovieDetailsResponse;
import dat3.kino.dto.response.MovieResponse;
import dat3.kino.services.MovieService;
import dat3.kino.services.TMDBService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/movies/cinemas/{cinema}")
    public ResponseEntity<List<MovieResponse>> getMoviesByCinema(@PathVariable Long cinema) {
        System.out.println("Cinema: " + cinema);
        return ResponseEntity.ok(movieService.readMoviesByCinema(cinema));
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieRequest movieRequest) {
        MovieResponse createdMovie = movieService.createMovie(movieRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdMovie.id())
                .toUri();

        return ResponseEntity.created(location).body(createdMovie);
    }

    @GetMapping("/movies/TMDB/{id}")
    public ResponseEntity<MovieDetailsResponse> getTMDBMovie(@PathVariable Integer id) {
        return ResponseEntity.ok(tmdbService.findMovieFromTMDB(id));
    }
}
