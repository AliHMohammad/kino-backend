package dat3.kino.services;

import dat3.kino.entities.Cinema;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.CinemaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaService {
    private final CinemaRepository cinemaRepository;

    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    public Cinema createCinema(Cinema newCinema) {
        return cinemaRepository.save(newCinema);
    }

    public Cinema readCinema(Long id) {
        return cinemaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cinema", id));
    }

    public List<Cinema> readAllCinemas() {
        return cinemaRepository.findAll();
    }

    // - updateCinema PATCH
    public Cinema updateCinemaPartially (Long id, Cinema cinema) {
        Cinema cinemaToUpdate = cinemaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cinema", id));
        if (cinema.getName() != null) {
            cinemaToUpdate.setName(cinema.getName());
        }
        if (cinema.getCity() != null) {
            cinemaToUpdate.setCity(cinema.getCity());
        }
        if (cinema.getIsActive() !=null) {
            cinemaToUpdate.setIsActive(cinema.getIsActive());
        }
        return cinemaRepository.save(cinemaToUpdate);
    }
}
