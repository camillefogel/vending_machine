package Objects;

public class Gum extends Item {
    public Gum(String name, double price, int quantity) {
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
        return "Chew Chew, Yum!";
    }

    public void removeOne() {
        --this.quantity;
    }
}