package dat3.kino.services;


import dat3.kino.dto.request.ReservationPriceRequest;
import dat3.kino.dto.response.ReservationPriceResponse;
import dat3.kino.entities.PriceAdjustment;
import dat3.kino.entities.Reservation;
import dat3.kino.entities.Screening;
import dat3.kino.entities.Seat;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.exception.FeeNotFoundException;
import dat3.kino.repositories.PriceAdjustmentRepository;
import dat3.kino.repositories.ReservationRepository;
import dat3.kino.repositories.ScreeningRepository;
import dat3.kino.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ScreeningRepository screeningRepository;
    private final PriceAdjustmentRepository priceAdjustmentRepository;
    private final SeatRepository seatRepository;

    public ReservationService(ReservationRepository reservationRepository, ScreeningRepository screeningRepository, PriceAdjustmentRepository priceAdjustmentRepository, SeatRepository seatRepository) {
        this.reservationRepository = reservationRepository;
        this.screeningRepository = screeningRepository;
        this.priceAdjustmentRepository = priceAdjustmentRepository;
        this.seatRepository = seatRepository;
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

        PriceAdjustment fee3D = priceAdjustmentRepository.findById("fee3D")
                .orElseThrow(() -> new FeeNotFoundException("priceAdjustment", "fee3D"));

        PriceAdjustment feeRuntime = priceAdjustmentRepository.findById("feeRuntime")
                .orElseThrow(() -> new FeeNotFoundException("priceAdjustment", "feeRuntime"));

        PriceAdjustment largeGroup = priceAdjustmentRepository.findById("largeGroup")
                .orElseThrow(() -> new FeeNotFoundException("priceAdjustment", "largeGroup"));

        PriceAdjustment smallGroup = priceAdjustmentRepository.findById("smallGroup")
                .orElseThrow(() -> new FeeNotFoundException("priceAdjustment", "smallGroup"));

        double SEATS_SUM = calculateSeatsPrice(reservationPriceRequest.seatIds());

        double GROUP_PRICE_ADJUSTMENT = reservationPriceRequest.seatIds()
                .size() <= 5 ? smallGroup.getAdjustment() : reservationPriceRequest.seatIds()
                .size() >= 10 ? largeGroup.getAdjustment() : 0;


        double FEE_3D = screening.getIs3d() ? fee3D.getAdjustment() : 0;
        double FEE_RUNTIME = screening.getMovie()
                .getRuntime() > 170 ? feeRuntime.getAdjustment() : 0;


        return toDto(SEATS_SUM);
    }

    private double calculateSeatsPrice(List<Long> seats) {
        List<Seat> seatList = seatRepository.findAllById(seats);

        return seatList.stream()
                .reduce(0.0, (subtotal, seat) -> subtotal + seat.getSeatPricing()
                        .getPrice(), Double::sum);

    }

    private ReservationPriceResponse toDto(double seatsSum) {
        return new ReservationPriceResponse(
                seatsSum,
                1,
                1,
                1
        );
    }

}


