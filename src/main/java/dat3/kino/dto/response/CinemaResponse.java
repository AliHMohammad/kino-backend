package dat3.kino.dto.response;

public record CinemaResponse(
        Long id,
        String name,
        String city,
        Boolean isActive
) {
}
