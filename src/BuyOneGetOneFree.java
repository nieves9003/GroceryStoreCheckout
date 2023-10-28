public class BuyOneGetOneFree implements SalePromotion {
    @Override
    public double applyPromotion(Product product, double quantity) {
        double effectiveQuantity = Math.ceil(quantity / 2.0);
        return product.calculatePrice(effectiveQuantity);
    }
}