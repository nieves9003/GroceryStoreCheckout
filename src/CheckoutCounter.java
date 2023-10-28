import java.util.HashMap;
import java.util.Map;

public class CheckoutCounter {
    private final Map<Product, SalePromotion> promotions;
    private final Map<Product, Double> cart = new HashMap<>();

    // Builder who accepts promotions applicable to products
    public CheckoutCounter(Map<Product, SalePromotion> promotions) {
        this.promotions = promotions;
    }

    // Method to add a product to the cart (with its quantity or weight)
    public void addProduct(Product product, double quantityOrWeight) {
        cart.merge(product, quantityOrWeight, Double::sum);
    }

    // Method to get the total price of all products in the cart
    public double getTotalPrice() {
        double total = 0.0;
        for (Map.Entry<Product, Double> entry : cart.entrySet()) {
            Product product = entry.getKey();
            double quantityOrWeight = entry.getValue();

            // The promotion applies if the product has an associated
            if (promotions.containsKey(product)) {
                total += promotions.get(product).applyPromotion(product, quantityOrWeight);
            } else {
                total += product.calculatePrice(quantityOrWeight);
            }
        }
        return total;
    }

    // Method to print the receipt of the products in the cart
    public void printReceipt() {
        System.out.println("---------- RECEIPT ----------");
        for (Map.Entry<Product, Double> entry : cart.entrySet()) {
            Product product = entry.getKey();
            double quantityOrWeight = entry.getValue();
            double price = promotions.containsKey(product)
                    ? promotions.get(product).applyPromotion(product, quantityOrWeight)
                    : product.calculatePrice(quantityOrWeight);
            System.out.println(product.getName() + " - " + quantityOrWeight + " x " + product.price + " = " + price);
        }
        System.out.println("-----------------------------");
        System.out.println("TOTAL: " + getTotalPrice());
    }
}
