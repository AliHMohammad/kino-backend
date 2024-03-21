package dat3.kino.repositories;

import dat3.kino.entities.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Screening entities.
 * This interface extends JpaRepository and provides methods to perform CRUD operations on Screening entities.
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    /**
     * Finds and returns a list of Screenings by the given cinema id.
     *
     * @param cinema the primary key of the cinema entity
     * @return a List of Screenings associated with the given cinema id
     */
    List<Screening> findScreeningsByAuditorium_Cinema_Id(Long cinema);

    /**
     * Returns a List of Screenings by the given cinema id and movie id, that also have a StartTime between two given dates. The List is ordered by the attribute StartTime.
     *
     * @param start LocalDateTime instance of the start date
     * @param end LocalDateTime instance of the last date
     * @param cinemaId the primary key of the cinema entity
     * @param movieId the primary key of the movie entity
     * @return a List of Screenings that meet the specified criteria
     */
    List<Screening> findAllByStartTimeBetweenAndAuditorium_Cinema_IdAndMovieIdOrderByStartTime(LocalDateTime start, LocalDateTime end, Long cinemaId, Long movieId);

}