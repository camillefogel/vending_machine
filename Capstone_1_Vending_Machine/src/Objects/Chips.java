package Objects;

public class Chips extends Item {
    public Chips(String name, double price, int quantity) {
        super(name, price, quantity);
    }

    public String getName() {
        return super.name;
    }

    public double getPrice() {
        return super.price;
    }

    public int getQuantity() {
        return super.quantity;
    }

    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    public String yumYumStatement() {
        return "Crunch Crunch, Yum!";
    }

    public void removeOne() {
        --this.quantity;
    }
}
