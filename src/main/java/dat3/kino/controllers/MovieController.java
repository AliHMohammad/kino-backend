package dat3.kino.controllers;

import dat3.kino.entities.Movie;
import dat3.kino.services.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public List<Movie> getMovies() {
        return movieService.readAllMovies();
    }


}
