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

/**
 * Service class for managing seats.
 */
@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    /**
     * Constructor for SeatService.
     *
     * @param seatRepository the repository for seats
     * @param reservationRepository the repository for reservations
     */
    public SeatService(SeatRepository seatRepository, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * Creates a new seat.
     *
     * @param newSeat the seat to create
     * @return the created seat
     */
    public Seat createSeat(Seat newSeat) {
        return seatRepository.save(newSeat);
    }

    /**
     * Fetches all seats from a specific auditorium.
     *
     * @param auditoriumId the ID of the auditorium
     * @return a list of all seats in the auditorium
     */
    public List<SeatResponse> readAllSeatsFromAuditorium(Long auditoriumId) {
        return seatRepository.findSeatsByAuditorium_Id(auditoriumId).stream().map(this::toDTO).toList();
    }

    /**
     * Fetches a seat by its ID.
     *
     * @param id the ID of the seat
     * @return the seat with the given ID
     * @throws EntityNotFoundException if no seat with the given ID is found
     */
    public SeatResponse readSeatById(Long id) {
        return seatRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Seat", id));
    }

    /**
     * Fetches all reserved seats for a specific screening.
     *
     * @param screeningId the ID of the screening
     * @return a list of all reserved seats for the screening
     */
    public List<SeatResponse> GetAllReservedSeats(Long screeningId) {
        List<Reservation> reservations = reservationRepository.findAllByScreeningId(screeningId);

        List<Seat> reservedSeats = new ArrayList<>();

        for (Reservation reservation : reservations) {
            reservedSeats.addAll(seatRepository.findAllByReservationId(reservation.getId()));
        }

        return reservedSeats.stream().map(this::toDTO).toList();
    }

    /**
     * Converts a Seat entity to a SeatResponse DTO.
     *
     * @param seat the Seat entity to convert
     * @return the converted SeatResponse DTO
     */
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