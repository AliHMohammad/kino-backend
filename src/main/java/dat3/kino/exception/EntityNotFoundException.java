package dat3.kino.exception;

/**
 * This class is a custom exception that is thrown when an entity is not found in the database.
 * It extends the RuntimeException class, which means it is an unchecked exception.
 * Unchecked exceptions are exceptions that can be thrown during the execution of the program but are not checked at compile time.
 *
 * @see RuntimeException
 */
public class EntityNotFoundException extends RuntimeException {

    /**
     * This constructor initializes a new instance of the EntityNotFoundException class.
     * It takes the name of the entity and its id as parameters, and constructs a detailed error message.
     *
     * @param entityName the name of the entity that was not found
     * @param id the id of the entity that was not found
     */
    public EntityNotFoundException(String entityName, Long id) {
        super(entityName + " with id " + id + " not found");
    }
}