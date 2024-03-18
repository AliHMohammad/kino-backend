package dat3.kino.services;

import dat3.kino.dto.response.SeatResponse;
import dat3.kino.entities.Seat;
import dat3.kino.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat createSeat(Seat newSeat) {
        return seatRepository.save(newSeat);
    }

    public List<SeatResponse> readAllSeatsFromAuditorium(Long auditoriumId) {
        return seatRepository.findSeatsByAuditorium_Id(auditoriumId).stream().map(this::toDTO).toList();
    }

    private SeatResponse toDTO(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatNum(),
                seat.getRowNum(),
                seat.getAuditorium().getId(),
                seat.getSeatPricing().getName()
        );
    }


}
