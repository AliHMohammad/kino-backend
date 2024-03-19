package dat3.kino.services;

import dat3.kino.dto.response.SeatResponse;
import dat3.kino.entities.Reservation;
import dat3.kino.entities.Seat;
import dat3.kino.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final ReservationService reservationService;

    public SeatService(SeatRepository seatRepository, ReservationService reservationService) {
        this.seatRepository = seatRepository;
        this.reservationService = reservationService;
    }

    public Seat createSeat(Seat newSeat) {
        return seatRepository.save(newSeat);
    }

    public List<SeatResponse> readAllSeatsFromAuditorium(Long auditoriumId) {
        return seatRepository.findSeatsByAuditorium_Id(auditoriumId).stream().map(this::toDTO).toList();
    }

    public List<SeatResponse> GetAllReservedSeats(Long screeningId) {
        List<Reservation> reservations = reservationService.getAllReservationsByScreeningId(screeningId);

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
