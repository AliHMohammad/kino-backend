package dat3.kino.services;

import dat3.kino.dto.response.AuditoriumResponse;
import dat3.kino.dto.response.CinemaResponse;
import dat3.kino.entities.Auditorium;
import dat3.kino.entities.Cinema;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.AuditoriumRepository;
import dat3.kino.repositories.CinemaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing cinemas.
 */
@Service
public class CinemaService {
    private final CinemaRepository cinemaRepository;
    private final AuditoriumRepository auditoriumRepository;

    /**
     * Constructor for CinemaService.
     *
     * @param cinemaRepository Repository for managing cinema data.
     * @param auditoriumRepository Repository for managing auditorium data.
     */
    public CinemaService(CinemaRepository cinemaRepository, AuditoriumRepository auditoriumRepository) {
        this.cinemaRepository = cinemaRepository;
        this.auditoriumRepository = auditoriumRepository;
    }

    /**
     * Creates a new cinema.
     *
     * @param newCinema The new cinema to be created.
     * @return The created cinema.
     */
    public CinemaResponse createCinema(Cinema newCinema) {
        return toDTO(cinemaRepository.save(newCinema));
    }

    /**
     * Retrieves a single cinema by its ID.
     *
     * @param id The ID of the cinema to retrieve.
     * @return The retrieved cinema.
     * @throws EntityNotFoundException If no cinema with the specified ID is found.
     */
    public CinemaResponse readCinema(Long id) {
        return cinemaRepository.findById(id).map(this::toDTO).orElseThrow(() -> new EntityNotFoundException("Cinema", id));
    }

    /**
     * Retrieves all cinemas.
     *
     * @return A list of all cinemas.
     */
    public List<CinemaResponse> readAllCinemas() {
        return cinemaRepository.findAll().stream().map(this::toDTO).toList();
    }

    /**
     * Retrieves all auditoriums for a specific cinema.
     *
     * @param cinemaId The ID of the cinema for which to retrieve auditoriums.
     * @return A list of all auditoriums for the specified cinema.
     */
    public List<AuditoriumResponse> readAllAuditoriumsByCinemaId(Long cinemaId) {
        List<Auditorium> auditoriums = auditoriumRepository.findAllByCinemaId(cinemaId);
        return auditoriums.stream().map((a) -> new AuditoriumResponse(
                a.getId(),
                a.getName()
        )).toList();
    }

    /**
     * Converts a Cinema entity to a CinemaResponse DTO.
     *
     * @param cinema The cinema to convert.
     * @return The converted CinemaResponse DTO.
     */
    private CinemaResponse toDTO(Cinema cinema) {
        return new CinemaResponse(cinema.getId(), cinema.getName(), cinema.getCity(), cinema.getIsActive());
    }
}