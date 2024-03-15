package dat3.kino.repositories;

import dat3.kino.entities.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {
    List<Auditorium> findAllByCinemaId(Long cinemaId);
}
