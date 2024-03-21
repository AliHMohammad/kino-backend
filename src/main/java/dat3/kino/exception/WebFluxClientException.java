package dat3.kino.exception;

/**
 * Custom exception class that extends RuntimeException.
 * This exception is thrown when a client error occurs while calling the TMDB API.
 */
public class WebFluxClientException extends RuntimeException{

    /**
     * Constructor for WebFluxClientException.
     *
     * @param message the error message
     */
    public WebFluxClientException(String message) {
        super(message);
    }
}