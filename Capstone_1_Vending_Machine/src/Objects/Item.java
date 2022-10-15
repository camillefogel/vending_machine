package Objects;

public abstract class Item {
    protected String name;
    protected double price;
    protected int quantity;

    public Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public abstract String getName();

    public abstract double getPrice();

    public abstract int getQuantity();

    public abstract String yumYumStatement();

    public abstract void removeOne();
}