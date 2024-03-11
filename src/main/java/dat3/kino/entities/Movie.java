package dat3.kino.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Movie {

    //TMDB's id'er. Gemt som Long
    @Id
    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "poster must not be blank")
    private String poster;

    //Minutter
    @Min(value = 1, message = "runtime must be atleast 1 minute")
    private int runtime;

    @NotNull(message = "premiere must not be null")
    private Date premiere;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
