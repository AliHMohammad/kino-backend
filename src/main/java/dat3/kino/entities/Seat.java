package dat3.kino.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer seatNum;

    private Integer rowNum;

    @ManyToOne
    private SeatPricing seatPricing;

    @ManyToOne
    private Auditorium auditorium;

    @JsonIgnore
    @ManyToMany(mappedBy = "seat")
    private Set<Reservation> reservation = new HashSet<>();

}
