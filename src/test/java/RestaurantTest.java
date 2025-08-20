import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    // ---------- Test support ----------
    static class TestableRestaurant extends Restaurant {
        private LocalTime fixedTime;
        public TestableRestaurant(String name, String location, LocalTime openingTime, LocalTime closingTime) {
            super(name, location, openingTime, closingTime);
        }
        public void setFixedTime(LocalTime t) { this.fixedTime = t; }
        @Override
        public LocalTime getCurrentTime() {
            return fixedTime != null ? fixedTime : super.getCurrentTime();
        }
    }

    private TestableRestaurant restaurant;
    private LocalTime openingTime;
    private LocalTime closingTime;

    // REFACTOR: common setup for all tests
    @BeforeEach
    void setup() {
        openingTime = LocalTime.parse("10:30:00");
        closingTime = LocalTime.parse("22:00:00");
        restaurant = new TestableRestaurant("Amelie's cafe", "Chennai", openingTime, closingTime);
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    @Test
    void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {
        restaurant.setFixedTime(LocalTime.parse("12:00:00")); // between 10:30 and 22:00
        assertTrue(restaurant.isRestaurantOpen());
    }

    @Test
    void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time() {
        restaurant.setFixedTime(LocalTime.parse("01:00:00")); // before opening
        assertFalse(restaurant.isRestaurantOpen());
    }

    @Test
    void is_restaurant_open_should_return_false_after_closing_time() {
        restaurant.setFixedTime(LocalTime.parse("23:30:00")); // after 22:00
        assertFalse(restaurant.isRestaurantOpen());
    }

    @Test
    void is_restaurant_open_should_return_true_at_exact_opening_time() {
        restaurant.setFixedTime(openingTime); // boundary
        assertTrue(restaurant.isRestaurantOpen());
    }

    @Test
    void is_restaurant_open_should_return_true_at_exact_closing_time() {
        restaurant.setFixedTime(closingTime); // boundary (inclusive)
        assertTrue(restaurant.isRestaurantOpen());
    }

    // <<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // >>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    void adding_item_to_menu_should_increase_menu_size_by_1() {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie", 319);
        assertEquals(initialMenuSize + 1, restaurant.getMenu().size());
    }

    @Test
    void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize - 1, restaurant.getMenu().size());
    }

    @Test
    void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class,
                () -> restaurant.removeFromMenu("French fries"));
    }
    // <<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    // >>>>>>>>>>>>>>>>>>>>>> OUTPUT & TIME HOOKS <<<<<<<<<<<<<<<<<<<<<<

    @Test
    void display_details_should_print_restaurant_info_and_menu() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(baos));
        try {
            restaurant.displayDetails(); // executes the printing code path
        } finally {
            System.setOut(original);
        }
        String out = baos.toString();
        assertTrue(out.contains("Restaurant:"), "Should print header Restaurant:");
        assertTrue(out.contains("Menu:"), "Should print Menu header");
        assertTrue(out.contains("Amelie's cafe"), "Should include restaurant name");
    }

    @Test
    void getCurrentTime_should_return_non_null_value_for_plain_restaurant() {
        Restaurant plain = new Restaurant("X", "Y",
                LocalTime.parse("09:00:00"), LocalTime.parse("23:00:00"));
        assertNotNull(plain.getCurrentTime());
    }

    // <<<<<<<<<<<<<<<<<<<<<< OUTPUT & TIME HOOKS >>>>>>>>>>>>>>>>>>>>>>
}
