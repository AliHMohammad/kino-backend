package dat3.kino.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;
    
    @ManyToOne
    private Movie movie;
    
    @ManyToOne
    private Auditorium auditorium;

    @Column(name = "is_3d")
    @NotNull(message = "is3d must not be null")
    private Boolean is3d;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
