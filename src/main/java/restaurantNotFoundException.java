public class restaurantNotFoundException extends Exception {
    public restaurantNotFoundException(String restaurantName) {
        super("Restaurant not found: " + restaurantName);
    }
}
