public class BuyTwoGetOneFree implements SalePromotion {
    @Override
    public double applyPromotion(Product product, double quantity) {
        double effectiveQuantity = 2.0 * (Math.floor(quantity / 3.0)) + (quantity % 3);
        return product.calculatePrice(effectiveQuantity);
    }
}