package dat3.kino.exception;

public class WebFluxServerException extends RuntimeException{
    public WebFluxServerException(String message) {
        super(message);
    }
}
