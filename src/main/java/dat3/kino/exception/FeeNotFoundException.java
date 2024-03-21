package dat3.kino.exception;

/**
 * Custom exception class that extends RuntimeException.
 * This exception is thrown when a fee is not found.
 */
public class FeeNotFoundException extends RuntimeException {

    /**
     * Constructor for FeeNotFoundException.
     *
     * @param entityName the name of the entity where the fee was not found
     * @param id the id of the entity where the fee was not found
     */
    public FeeNotFoundException(String entityName, String id) {
        super(entityName + " with id " + id + " not found");
    }

}
