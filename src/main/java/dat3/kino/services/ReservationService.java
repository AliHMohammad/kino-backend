package dat3.kino.services;


import dat3.kino.dto.request.ReservationPriceRequest;
import dat3.kino.dto.response.ReservationPriceResponse;
import dat3.kino.entities.Reservation;
import dat3.kino.entities.Screening;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.ReservationRepository;
import dat3.kino.repositories.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ScreeningRepository screeningRepository;

    public ReservationService(ReservationRepository reservationRepository, ScreeningRepository screeningRepository) {
        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
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

    public ReservationPriceResponse calculateReservationPrice(ReservationPriceRequest reservationPriceRequest) {
        Screening screening = screeningRepository.findById(reservationPriceRequest.screeningId()).
                orElseThrow(() -> new EntityNotFoundException("movie", reservationPriceRequest.screeningId()));
        System.out.println(screening.getMovie());

        boolean is3D = screening.getIs3d();
        int runtime = screening.getMovie()
                .getRuntime();


        return toDto();
    }

    private ReservationPriceResponse toDto() {
        return new ReservationPriceResponse(
                1,
                1,
                1,
                1
        );
    }

}


