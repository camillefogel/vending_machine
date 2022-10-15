package Objects;

public class Beverages extends Item {
    public Beverages(String name, double price, int quantity) {
        super(name, price, quantity);
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String yumYumStatement() {
        return "Glug Glug, Yum!";
    }

    public void removeOne() {
        --this.quantity;
    }
}
