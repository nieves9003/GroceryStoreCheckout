public class ProductByPiece extends Product {
    public ProductByPiece(String name, double price) {
        super(name, price);
    }

    @Override
    public double calculatePrice(double quantity) {
        return price * quantity;
    }
}