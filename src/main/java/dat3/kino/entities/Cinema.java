package dat3.kino.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;

    @NotBlank(message = "city must not be blank")
    private String city;

    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Cinema(String name, String city, Boolean isActive) {
        this.name = name;
        this.city = city;
        this.isActive = isActive;
    }
}
