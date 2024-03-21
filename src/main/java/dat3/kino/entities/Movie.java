package dat3.kino.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Movie {

    //TMDB's id'er. Gemt som Long
    @Id
    private Long id;

    @NotBlank(message = "name must not be blank")
    private String title;

    @NotBlank(message = "poster must not be blank")
    private String poster;

    //Minutter
    @Min(value = 1, message = "runtime must be atleast 1 minute")
    private int runtime;

    @NotNull(message = "premiere must not be null")
    private LocalDate premiere;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Movie(Long id, String title, String poster, int runtime, LocalDate premiere) {
        this.id = id;
        this.title = title;
        this.poster = poster;
        this.runtime = runtime;
        this.premiere = premiere;
    }

}
