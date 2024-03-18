package dat3.kino.repositories;

import dat3.kino.entities.Cinema;
import dat3.kino.entities.Movie;
import dat3.kino.entities.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findScreeningsByAuditorium_Cinema_Id(Long cinema);

    List<Screening> findAllByStartTimeBetweenAndAuditorium_Cinema_IdAndMovieId(LocalDateTime start, LocalDateTime end, Long cinemaId, Long movieId);

}
