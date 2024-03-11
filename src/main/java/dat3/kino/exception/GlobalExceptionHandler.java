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

@ControllerAdvice
//NOTE: @ControllerAdvice is used to define a global exception handler
public class GlobalExceptionHandler {
    //NOTE: @ExceptionHandler is used to handle exceptions in specific handler classes and/or globally
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //NOTE: @ResponseStatus is used to specify the HTTP response status code for the method
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //NOTE: @ResponseBody is used to bind the method return value to the web response body
    @ResponseBody
    // Handle validation exceptions by returning a map of field names and their corresponding error messages
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        // for each error in the exception, add the field name and error message to the map
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // return the map of errors
        return errors;
    }
}
