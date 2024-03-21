package dat3.kino.services;


import dat3.kino.dto.request.ReservationPriceRequest;
import dat3.kino.dto.request.ReservationRequest;
import dat3.kino.dto.response.ReservationPriceResponse;
import dat3.kino.dto.response.ReservationResponse;
import dat3.kino.dto.response.SeatResponse;
import dat3.kino.entities.PriceAdjustment;
import dat3.kino.entities.Reservation;
import dat3.kino.entities.Screening;
import dat3.kino.entities.Seat;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.PriceAdjustmentRepository;
import dat3.kino.repositories.ReservationRepository;
import dat3.kino.repositories.ScreeningRepository;
import dat3.kino.repositories.SeatRepository;
import dat3.security.entities.UserWithRoles;
import dat3.security.repositories.UserWithRolesRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ScreeningRepository screeningRepository;
    private final UserWithRolesRepository userWithRolesRepository;
    private final SeatRepository seatRepository;
    private final PriceAdjustmentRepository priceAdjustmentRepository;
    private final SeatService seatService;
    private final ScreeningService screeningService;
    private final PriceAdjustmentService priceAdjustmentService;

    public ReservationService(ReservationRepository reservationRepository, ScreeningRepository screeningRepository,
                              UserWithRolesRepository userWithRolesRepository, SeatRepository seatRepository,
                              PriceAdjustmentRepository priceAdjustmentRepository, SeatService seatService, ScreeningService screeningService, PriceAdjustmentService priceAdjustmentService) {
        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
        this.userWithRolesRepository = userWithRolesRepository;
        this.seatRepository = seatRepository;
        this.priceAdjustmentRepository = priceAdjustmentRepository;
        this.seatService = seatService;
        this.screeningService = screeningService;

        this.priceAdjustmentService = priceAdjustmentService;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public ReservationResponse createReservation(ReservationRequest reservationRequest, String userId) {
        UserWithRoles user = userWithRolesRepository.findById(userId)
                .orElseThrow();
        Screening screening = screeningRepository.findById(reservationRequest.screeningId())
                .orElseThrow();

        Set<Seat> selectedSeats = new HashSet<>();

        for (Long seatId : reservationRequest.seatIds()) {
            selectedSeats.add(seatRepository.findById(seatId)
                    .orElseThrow());
        }

        Reservation reservation = toEntity(user, screening, selectedSeats);
        reservationRepository.save(reservation);
        return toDTO(reservation);
    }

    public List<Reservation> getAllReservationsByScreeningId(Long id) {
        return reservationRepository.findAllByScreeningId(id);
    }

    public List<ReservationResponse> getAllReservationsByUserName(String name) {
        return reservationRepository.findAllByUserUsername(name)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ReservationPriceResponse calculateReservationPrice(ReservationPriceRequest reservationPriceRequest) {
        Screening screening = screeningRepository.findById(reservationPriceRequest.screeningId()).
                orElseThrow(() -> new EntityNotFoundException("movie", reservationPriceRequest.screeningId()));

        Map<String, Double> priceAdjustments = priceAdjustmentRepository.findAll()
                .stream()
                .collect(Collectors
                        .toMap(PriceAdjustment::getName, PriceAdjustment::getAdjustment));

        double SEATS_SUM = priceAdjustmentService.calculateSeatsPrice(reservationPriceRequest.seatIds());

        String GROUP_SIZE = reservationPriceRequest.seatIds()
                .size() <= 5 ? "smallGroup" : reservationPriceRequest.seatIds()
                .size() >= 10 ? "largeGroup" : "";

        int NUM_OF_SEATS = reservationPriceRequest.seatIds()
                .size();

        double FEES_SUM = priceAdjustmentService.calculateFees(screening, GROUP_SIZE, SEATS_SUM, priceAdjustments, NUM_OF_SEATS);

        double DISCOUNT_SUM = priceAdjustmentService.calculateDiscount(GROUP_SIZE, SEATS_SUM, priceAdjustments);

        double TOTAL = SEATS_SUM + FEES_SUM - DISCOUNT_SUM;


        return toReservationPriceDto(SEATS_SUM, DISCOUNT_SUM, FEES_SUM, TOTAL);
    }

    private ReservationPriceResponse toReservationPriceDto(double seatsSum, double discount, double fees, double total) {
        return new ReservationPriceResponse(
                seatsSum,
                fees,
                discount,
                total
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

        for (Seat seat : reservation.getSeats()) {
            seatResponseList.add(seatService.readSeatById(seat.getId()));
        }

        return new ReservationResponse(
                reservation.getId(),
                reservation.getUser()
                        .getUsername(),
                screeningService.readScreeningById(reservation.getScreening()
                        .getId()),
                seatResponseList,
                reservation.getCreatedAt()
        );
    }
}

