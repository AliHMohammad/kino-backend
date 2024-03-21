package dat3.kino.exception;

/**
 * Custom exception class that extends RuntimeException.
 * This exception is thrown when a server error occurs while calling the TMDB API.
 */
public class WebFluxServerException extends RuntimeException{

    /**
     * Constructor for WebFluxServerException.
     *
     * @param message the error message
     */
    public WebFluxServerException(String message) {
        super(message);
    }
}