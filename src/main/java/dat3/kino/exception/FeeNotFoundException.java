package dat3.kino.exception;

public class FeeNotFoundException extends RuntimeException {
    public FeeNotFoundException(String entityName, String id) {
        super(entityName + " with id " + id + " not found");
    }

}
