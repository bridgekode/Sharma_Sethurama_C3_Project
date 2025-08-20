import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Restaurant {
    private String name;
    private String location;
    public LocalTime openingTime;
    public LocalTime closingTime;
    private List<Item> menu = new ArrayList<Item>();

    public Restaurant(String name, String location, LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.location = location;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    /** Returns true if current time is within [openingTime, closingTime]. */
    public boolean isRestaurantOpen() {
        LocalTime now = getCurrentTime();
        return !now.isBefore(openingTime) && !now.isAfter(closingTime);
    }

    /** Hook for tests; default is LocalTime.now(). */
    public LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    /** Returns the menu list (per student stub, same instance). */
    public List<Item> getMenu() {
        return menu;
    }

    private Item findItemByName(String itemName) {
        for (Item item : menu) {
            if (item.getName().equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    public void addToMenu(String name, int price) {
        Item newItem = new Item(name, price);
        menu.add(newItem);
    }

    public void removeFromMenu(String itemName) throws itemNotFoundException {
        Item itemToBeRemoved = findItemByName(itemName);
        if (itemToBeRemoved == null)
            throw new itemNotFoundException(itemName);
        menu.remove(itemToBeRemoved);
    }

    public void displayDetails() {
        System.out.println("Restaurant:" + name + "\n"
                + "Location:" + location + "\n"
                + "Opening time:" + openingTime + "\n"
                + "Closing time:" + closingTime + "\n"
                + "Menu:" + "\n" + getMenu());
    }

    public String getName() {
        return name;
    }

    // -------- Part 3: Order total feature --------

    /** Sums prices for the given item names; unknown names are ignored. */
    public int getOrderValue(List<String> itemNames) {
        int total = 0;
        for (String n : itemNames) {
            Item item = findItemByName(n);
            if (item != null) {
                total += item.getPrice();
            }
        }
        return total;
    }

    /** Convenience overload to allow varargs input. */
    public int getOrderValue(String... itemNames) {
        return getOrderValue(Arrays.asList(itemNames));
    }
}
