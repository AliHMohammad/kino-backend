package dat3.kino.exception;

/**
 * Custom exception class that extends RuntimeException.
 * This exception is thrown when a seat pricing is not found.
 */
public class SeatPricingNotFoundExeption extends RuntimeException {

    /**
     * Constructor for SeatPricingNotFoundExeption.
     *
     * @param entityName the name of the entity where the seat pricing was not found
     * @param id the id of the entity where the seat pricing was not found
     */
    public SeatPricingNotFoundExeption(String entityName, String id) {
        super(entityName + " with id " + id + " not found");
    }

}