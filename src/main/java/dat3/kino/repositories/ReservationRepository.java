package dat3.kino.repositories;

import dat3.kino.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByScreeningId(Long id);
    List<Reservation> findAllByUserUsername(String userName);
}
