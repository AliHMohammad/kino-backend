package dat3.kino.services;

import dat3.kino.entities.Seat;
import dat3.kino.repositories.SeatRepository;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat createSeat(Seat newSeat) {
        return seatRepository.save(newSeat);
    }
}
