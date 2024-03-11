package dat3.kino.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustment {
    @Id
    private String name;

    private double adjustment;

    @ManyToMany(mappedBy = "priceAdjustment")
    @JsonIgnore
    private Set<Reservation> reservation = new HashSet<>();
}
