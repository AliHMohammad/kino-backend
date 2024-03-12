package dat3.kino.exception;

public class SeatPricingNotFoundExeption extends RuntimeException {
        public SeatPricingNotFoundExeption(String entityName, String id) {
            super(entityName + " with id " + id + " not found");
        }

}
