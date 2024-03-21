package dat3.kino.services;

import dat3.kino.dto.response.AuditoriumResponse;
import dat3.kino.entities.Auditorium;
import dat3.kino.entities.Seat;
import dat3.kino.entities.SeatPricing;
import dat3.kino.exception.EntityNotFoundException;
import dat3.kino.repositories.AuditoriumRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for managing auditoriums.
 */
@Service
public class AuditoriumService {
    private final AuditoriumRepository auditoriumRepository;
    private final SeatService seatService;
    private final SeatPricingService seatPricingService;

    /**
     * Constructor for AuditoriumService.
     *
     * @param auditoriumRepository Repository for managing auditorium data.
     * @param seatService Service for managing seat data.
     * @param seatPricingService Service for managing seat pricing data.
     */
    public AuditoriumService(AuditoriumRepository auditoriumRepository, SeatService seatService, SeatPricingService seatPricingService) {
        this.auditoriumRepository = auditoriumRepository;
        this.seatService = seatService;
        this.seatPricingService = seatPricingService;
    }

    /**
     * Creates a new auditorium with the specified number of rows and seats per row.
     *
     * @param newAuditorium The new auditorium to be created.
     * @param rows The number of rows in the auditorium.
     * @param seatsPerRow The number of seats per row in the auditorium.
     * @return The created auditorium.
     */
    public AuditoriumResponse createAuditorium(Auditorium newAuditorium, int rows, int seatsPerRow) {
        Auditorium auditorium = auditoriumRepository.save(newAuditorium);
        // Create seats
        for (int i = 0; i < rows; i++) {
            for(int j =0; j < seatsPerRow; j++) {
                int rowNum = i + 1;
                int seatNum = j + 1;
                SeatPricing seatPricing = getSeatPricing(rows, rowNum);
                seatService.createSeat(new Seat(seatNum, rowNum, seatPricing, auditorium));
            }
        }
        return toDTO(auditorium);
    }

    /**
     * Determines the seat pricing based on the row number.
     *
     * @param rows The total number of rows in the auditorium.
     * @param rowNum The row number for which to determine the seat pricing.
     * @return The seat pricing for the specified row.
     */
    private SeatPricing getSeatPricing(int rows, int rowNum) {

        if (rowNum <= 2) {
            return seatPricingService.getSeatPricing("cowboy");
        } else if (rowNum == rows) {
            return seatPricingService.getSeatPricing("deluxe");
        } else {
            return seatPricingService.getSeatPricing("standard");
        }
    }

    /**
     * Retrieves a single auditorium by its ID.
     *
     * @param id The ID of the auditorium to retrieve.
     * @return The retrieved auditorium.
     * @throws EntityNotFoundException If no auditorium with the specified ID is found.
     */
    public AuditoriumResponse readSingleAuditorium(Long id) {
        return auditoriumRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("auditorium", id));
    }

    /**
     * Converts an Auditorium entity to an AuditoriumResponse DTO.
     *
     * @param auditorium The auditorium to convert.
     * @return The converted AuditoriumResponse DTO.
     */
    private AuditoriumResponse toDTO(Auditorium auditorium) {
        return new AuditoriumResponse(
                auditorium.getId(),
                auditorium.getName()
        );
    }
}