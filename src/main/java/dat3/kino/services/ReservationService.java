package dat3.kino.services;


import dat3.kino.entities.Reservation;
import dat3.kino.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservationsByScreeningId(Long id) {
        return reservationRepository.findAllByScreeningId(id);
    }

    public List<Reservation> getAllReservationsByUserName(String name) {
        return reservationRepository.findAllByUserUsername(name);
    }

}
