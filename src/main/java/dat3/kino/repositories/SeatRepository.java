package dat3.kino.repositories;

import dat3.kino.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findSeatsByAuditorium_Id(Long auditoriumId);
}
