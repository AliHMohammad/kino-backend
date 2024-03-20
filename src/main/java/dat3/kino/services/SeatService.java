package dat3.kino.services;

import dat3.kino.dto.response.SeatResponse;
import dat3.kino.entities.Reservation;
import dat3.kino.entities.Seat;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.ReservationRepository;
import dat3.kino.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    public SeatService(SeatRepository seatRepository, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
    }

    public Seat createSeat(Seat newSeat) {
        return seatRepository.save(newSeat);
    }

    public List<SeatResponse> readAllSeatsFromAuditorium(Long auditoriumId) {
        return seatRepository.findSeatsByAuditorium_Id(auditoriumId).stream().map(this::toDTO).toList();
    }

    public SeatResponse readSeatById(Long id) {
        return seatRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Seat", id));
    }

    public List<SeatResponse> GetAllReservedSeats(Long screeningId) {
        List<Reservation> reservations = reservationRepository.findAllByScreeningId(screeningId);

        List<Seat> reservedSeats = new ArrayList<>();

        for (Reservation reservation : reservations) {
            reservedSeats.addAll(seatRepository.findAllByReservationId(reservation.getId()));
        }

        return reservedSeats.stream().map(this::toDTO).toList();
    }

    private SeatResponse toDTO(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatNum(),
                seat.getRowNum(),
                seat.getAuditorium().getId(),
                seat.getSeatPricing()
        );
    }


}
