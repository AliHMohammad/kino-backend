package dat3.kino.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Auditorium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;

    @ManyToOne
    private Cinema cinema;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Auditorium(String name, Cinema cinema) {
        this.name = name;
        this.cinema = cinema;
    }
}
