public class ProductInBulk extends Product {
    public ProductInBulk(String name, double price) {
        super(name, price);
    }

    @Override
    public double calculatePrice(double weight) {
        return price * weight;
    }
}