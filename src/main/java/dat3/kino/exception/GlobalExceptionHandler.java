package dat3.kino.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler class.
 * This class is annotated with @ControllerAdvice to handle exceptions globally across the whole application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions.
     * This method is annotated with @ExceptionHandler to handle MethodArgumentNotValidException globally.
     * It returns a map of field names and their corresponding error messages.
     *
     * @param exception the exception to handle
     * @return a map of field names and their corresponding error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * Handles EntityNotFoundException.
     * This method is annotated with @ExceptionHandler to handle EntityNotFoundException globally.
     * It returns a map with the error message.
     *
     * @param exception the exception to handle
     * @return a map with the error message
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleEntityNotFoundExceptions(EntityNotFoundException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", exception.getMessage());
        return errors;
    }

    /**
     * Handles SeatPricingNotFoundException.
     * This method is annotated with @ExceptionHandler to handle SeatPricingNotFoundException globally.
     * It returns a map with the error message.
     *
     * @param exception the exception to handle
     * @return a map with the error message
     */
    @ExceptionHandler(SeatPricingNotFoundExeption.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleSeatPricingNotFoundExeptions(SeatPricingNotFoundExeption exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", exception.getMessage());
        return errors;
    }

    /**
     * Handles WebFluxServerException.
     * This method is annotated with @ExceptionHandler to handle WebFluxServerException globally.
     * It returns a map with the error message.
     *
     * @param exception the exception to handle
     * @return a map with the error message
     */
    @ExceptionHandler(WebFluxServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, String> handleWebFluxServerExceptions(WebFluxServerException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Server error occurred while calling TMDB API:", exception.getMessage());
        return errors;
    }

    /**
     * Handles WebFluxClientException.
     * This method is annotated with @ExceptionHandler to handle WebFluxClientException globally.
     * It returns a map with the error message.
     *
     * @param exception the exception to handle
     * @return a map with the error message
     */
    @ExceptionHandler(WebFluxClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleWebFluxClientExceptions(WebFluxClientException exception) {
        Map<String, String> errors = new HashMap<>();
        errors.put("Client error occurred while calling TMDB API:", exception.getMessage());
        return errors;
    }
}