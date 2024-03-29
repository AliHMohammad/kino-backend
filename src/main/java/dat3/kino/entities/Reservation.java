package dat3.kino.entities;

import dat3.security.entities.UserWithRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserWithRoles user;

    @ManyToOne
    private Screening screening;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToMany
    private Set<PriceAdjustment> priceAdjustment = new HashSet<>();

    @ManyToMany
    private Set<Seat> seats = new HashSet<>();

    public Reservation(UserWithRoles user, Screening screening, Set<Seat> seats) {
        this.user = user;
        this.screening = screening;
        this.seats = seats;
    }
}
