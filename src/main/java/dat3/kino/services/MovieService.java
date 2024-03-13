package dat3.kino.services;

import dat3.kino.entities.Movie;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;



    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> readAllMovies() {
        return movieRepository.findAll();
    }

    public Movie readMovie(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("movie", id));
    }

    public Movie createMovie(Movie newMovie) {
        return movieRepository.save(newMovie);
    }



}
