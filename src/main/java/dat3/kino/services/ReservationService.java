package dat3.kino.services;


import dat3.kino.dto.request.ReservationRequest;
import dat3.kino.dto.response.ReservationResponse;
import dat3.kino.dto.response.SeatResponse;
import dat3.kino.entities.Reservation;
import dat3.kino.entities.Screening;
import dat3.kino.entities.Seat;
import dat3.kino.repositories.ReservationRepository;
import dat3.kino.repositories.ScreeningRepository;
import dat3.kino.repositories.SeatRepository;
import dat3.security.entities.UserWithRoles;
import dat3.security.repositories.UserWithRolesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserWithRolesRepository userWithRolesRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final ScreeningService screeningService;
    private final SeatService seatService;


    public ReservationService(ReservationRepository reservationRepository,
                              UserWithRolesRepository userWithRolesRepository, ScreeningRepository screeningRepository,
                              SeatRepository seatRepository, ScreeningService screeningService, SeatService seatService) {
        this.reservationRepository = reservationRepository;
        this.userWithRolesRepository = userWithRolesRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.screeningService = screeningService;
        this.seatService = seatService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }



    public List<Reservation> getAllReservationsByScreeningId(Long id) {
        return reservationRepository.findAllByScreeningId(id);
    }

    public List<ReservationResponse> getAllReservationsByUserName(String name) {
        return reservationRepository.findAllByUserUsername(name).stream().map(this::toDTO).toList();
    }

    public ReservationResponse createReservation(ReservationRequest reservationRequest, String userId) {
        UserWithRoles user = userWithRolesRepository.findById(userId).orElseThrow();
        Screening screening = screeningRepository.findById(reservationRequest.screeningId()).orElseThrow();

        Set<Seat> selectedSeats = new HashSet<>();

        for (Long seatId : reservationRequest.seatIds()) {
            selectedSeats.add(seatRepository.findById(seatId).orElseThrow());
        }

        Reservation reservation = toEntity(user, screening, selectedSeats);
        reservationRepository.save(reservation);
        return toDTO(reservation);
    }


    private Reservation toEntity(UserWithRoles user, Screening screening, Set<Seat> seats) {
        return new Reservation(
                user,
                screening,
                seats
        );
    }

    private ReservationResponse toDTO(Reservation reservation) {
        List<SeatResponse> seatResponseList = new ArrayList<>();

        for (Seat seat: reservation.getSeats()) {
            seatResponseList.add(seatService.readSeatById(seat.getId()));
        }

        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser().getUsername(),
                screeningService.readScreeningById(reservation.getScreening().getId()),
                seatResponseList,
                reservation.getCreatedAt()
        );
    }
}
