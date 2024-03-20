package dat3.kino.services;


import dat3.kino.dto.request.ReservationPriceRequest;
import dat3.kino.dto.request.ReservationRequest;
import dat3.kino.dto.response.ReservationPriceResponse;
import dat3.kino.dto.response.ReservationResponse;
import dat3.kino.dto.response.SeatResponse;
import dat3.kino.entities.Reservation;
import dat3.kino.entities.Screening;
import dat3.kino.entities.Seat;
import dat3.kino.exception.EntityNotFoundException;
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
    private final SeatService seatService;
    private final ScreeningService screeningService;

    public ReservationService(ReservationRepository reservationRepository, UserWithRolesRepository userWithRolesRepository,
                              ScreeningRepository screeningRepository, SeatRepository seatRepository, SeatService seatService,
                              ScreeningService screeningService) {
        this.reservationRepository = reservationRepository;
        this.userWithRolesRepository = userWithRolesRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.seatService = seatService;
        this.screeningService = screeningService;
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
