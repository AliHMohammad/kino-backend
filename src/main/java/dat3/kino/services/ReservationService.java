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

/**
 * Service class for managing reservations.
 */
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ScreeningRepository screeningRepository;
    private final UserWithRolesRepository userWithRolesRepository;
    private final SeatRepository seatRepository;
    private final PriceAdjustmentRepository priceAdjustmentRepository;
    private final SeatService seatService;
    private final ScreeningService screeningService;

    /**
     * Constructor for ReservationService.
     *
     * @param reservationRepository Repository for managing reservation data.
     * @param screeningRepository Repository for managing screening data.
     * @param userWithRolesRepository Repository for managing user data.
     * @param seatRepository Repository for managing seat data.
     * @param priceAdjustmentRepository Repository for managing price adjustment data.
     * @param seatService Service for managing seat data.
     * @param screeningService Service for managing screening data.
     */
    public ReservationService(ReservationRepository reservationRepository, ScreeningRepository screeningRepository,
                              UserWithRolesRepository userWithRolesRepository, SeatRepository seatRepository,
                              PriceAdjustmentRepository priceAdjustmentRepository, SeatService seatService, ScreeningService screeningService) {
        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
        this.userWithRolesRepository = userWithRolesRepository;
        this.seatRepository = seatRepository;
        this.priceAdjustmentRepository = priceAdjustmentRepository;
        this.seatService = seatService;
        this.screeningService = screeningService;
    }

    /**
     * Creates a new reservation.
     *
     * @param reservationRequest The request object containing the details of the reservation to be created.
     * @param userId The ID of the user making the reservation.
     * @return The created reservation.
     */
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

    /**
     * Retrieves all reservations made by a specific user.
     *
     * @param name The username of the user.
     * @return A list of all reservations made by the specified user.
     */
    public List<ReservationResponse> getAllReservationsByUserName(String name) {
        return reservationRepository.findAllByUserUsername(name).stream().map(this::toDTO).toList();
    }

    /**
     * Calculates the price of a reservation.
     *
     * @param reservationPriceRequest The request object containing the details of the reservation for which to calculate the price.
     * @return The calculated price of the reservation.
     */
    public ReservationPriceResponse calculateReservationPrice(ReservationPriceRequest reservationPriceRequest) {
        Screening screening = screeningRepository.findById(reservationPriceRequest.screeningId()).
                orElseThrow(() -> new EntityNotFoundException("movie", reservationPriceRequest.screeningId()));

        Map<String, Double> priceAdjustments = priceAdjustmentRepository.findAll()
                .stream()
                .collect(Collectors
                        .toMap(PriceAdjustment::getName, PriceAdjustment::getAdjustment));

        double SEATS_SUM = calculateSeatsPrice(reservationPriceRequest.seatIds());

        String GROUP_SIZE = reservationPriceRequest.seatIds()
                .size() <= 5 ? "smallGroup" : reservationPriceRequest.seatIds()
                .size() >= 10 ? "largeGroup" : "";


        double FEES_SUM = calculateFees(screening, GROUP_SIZE, SEATS_SUM, priceAdjustments);

        double DISCOUNT_SUM = calculateDiscount(GROUP_SIZE, SEATS_SUM, priceAdjustments);

        double TOTAL = SEATS_SUM + FEES_SUM - DISCOUNT_SUM;


        return toReservationPriceDto(SEATS_SUM, DISCOUNT_SUM, FEES_SUM, TOTAL);
    }

    /**
     * Calculates the price of the seats in a reservation.
     *
     * @param seats The IDs of the seats in the reservation.
     * @return The total price of the seats.
     */
    private double calculateSeatsPrice(List<Long> seats) {
        List<Seat> seatList = seatRepository.findAllById(seats);

        return seatList.stream()
                .reduce(0.0, (subtotal, seat) -> subtotal + seat.getSeatPricing()
                        .getPrice(), Double::sum);
    }

    /**
     * Calculates the fees for a reservation.
     *
     * @param screening The screening for which the reservation is made.
     * @param groupSize The size of the group making the reservation.
     * @param seatsSum The total price of the seats in the reservation.
     * @param priceAdjustments The price adjustments to apply.
     * @return The total fees for the reservation.
     */
    private double calculateFees(Screening screening, String groupSize, double seatsSum, Map<String, Double> priceAdjustments) {

        double FEE_3D = screening.getIs3d() ? priceAdjustments.get("fee3D") : 0;

        double FEE_RUNTIME = screening.getMovie()
                .getRuntime() > 160 ? priceAdjustments.get("feeRuntime") : 0;


        double feeSum = 0;

        if (groupSize.equals("smallGroup")) feeSum = priceAdjustments.get("smallGroup") * seatsSum - seatsSum;
        if (FEE_3D > 0) feeSum += FEE_3D;
        if (FEE_RUNTIME > 0) feeSum += FEE_RUNTIME;

        return feeSum;
    }

    /**
     * Calculates the discount for a reservation.
     *
     * @param groupSize The size of the group making the reservation.
     * @param seatsSum The total price of the seats in the reservation.
     * @param priceAdjustments The price adjustments to apply.
     * @return The total discount for the reservation.
     */
    private double calculateDiscount(String groupSize, double seatsSum, Map<String, Double> priceAdjustments) {
        double discountSum = 0;

        if (groupSize.equals("largeGroup"))
            discountSum += Math.abs(priceAdjustments.get("largeGroup") * seatsSum - seatsSum);

        return discountSum;

    }

    /**
     * Converts a ReservationPriceResponse entity to a ReservationPriceResponse DTO.
     *
     * @param seatsSum The total price of the seats in the reservation.
     * @param discount The total discount for the reservation.
     * @param fees The total fees for the reservation.
     * @param total The total price of the reservation.
     * @return The converted ReservationPriceResponse DTO.
     */
    private ReservationPriceResponse toReservationPriceDto(double seatsSum, double discount, double fees, double total) {
        return new ReservationPriceResponse(
                seatsSum,
                fees,
                discount,
                total
        );
    }

    /**
     * Converts a ReservationRequest DTO to a Reservation entity.
     *
     * @param user The user making the reservation.
     * @param screening The screening for which the reservation is made.
     * @param seats The seats in the reservation.
     * @return The converted Reservation entity.
     */
    private Reservation toEntity(UserWithRoles user, Screening screening, Set<Seat> seats) {
        return new Reservation(
                user,
                screening,
                seats
        );
    }

    /**
     * Converts a Reservation entity to a ReservationResponse DTO.
     *
     * @param reservation The reservation to convert.
     * @return The converted ReservationResponse DTO.
     */
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