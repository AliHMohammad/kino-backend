package dat3.kino.repositories;

import dat3.kino.entities.SeatPricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatPricingRepository extends JpaRepository<SeatPricing, String>{
}
