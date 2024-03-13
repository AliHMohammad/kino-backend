package dat3.kino.services;

import dat3.kino.dto.response.CinemaResponse;
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

    public CinemaResponse createCinema(Cinema newCinema) {
        return toDTO(cinemaRepository.save(newCinema));
    }

    public CinemaResponse readCinema(Long id) {
        return cinemaRepository.findById(id).map(this::toDTO).orElseThrow(() -> new EntityNotFoundException("Cinema", id));
    }

    public List<CinemaResponse> readAllCinemas() {
        return cinemaRepository.findAll().stream().map(this::toDTO).toList();
    }

    // - updateCinema PATCH
    public CinemaResponse updateCinemaPartially (Long id, Cinema cinema) {
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
        return toDTO(cinemaRepository.save(cinemaToUpdate));
    }

    private CinemaResponse toDTO(Cinema cinema) {
        return new CinemaResponse(cinema.getId(), cinema.getName(), cinema.getCity(), cinema.getIsActive());
    }
}
