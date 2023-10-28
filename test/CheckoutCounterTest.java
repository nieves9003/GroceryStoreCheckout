import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CheckoutCounterTest {
    private Product chips;
    private Product rice;
    private Product promoProduct;
    private Product promoProduct2;
    private CheckoutCounter counter;
    private Map<Product, SalePromotion> promotions;

    @Before
    public void setUp() {
        chips = new ProductByPiece("Chips", 35);
        rice = new ProductInBulk("Rice", 45);
        promoProduct = new ProductByPiece("PromoOneGetOneFree", 100);
        promoProduct2 = new ProductByPiece("PromoTwoGetOneFree", 50);

        promotions = new HashMap<>();
        promotions.put(promoProduct, new BuyOneGetOneFree());
        promotions.put(promoProduct2, new BuyTwoGetOneFree());

        counter = new CheckoutCounter(promotions);
    }

    @Test
    public void testAddProductByPiece() {
        counter.addProduct(chips, 2);
        assertEquals(70, counter.getTotalPrice(), 0.01);
    }

    @Test
    public void testAddProductInBulk() {
        counter.addProduct(rice, 1.5); // 1.5 kg of rice
        assertEquals(67.5, counter.getTotalPrice(), 0.01);
    }

    @Test
    public void testPromotionBuyOneGetOneFree() {
        counter.addProduct(promoProduct, 2); // Add 2 of the promo products
        assertEquals(100, counter.getTotalPrice(), 0.01);
    }

    @Test
    public void testPromotionBuyTwoGetOneFree() {
        counter.addProduct(promoProduct2, 3); // Add 3 of the promo products
        assertEquals(100, counter.getTotalPrice(), 0.01);
    }

    @Test
    public void testMultipleProductsAndPromotions() {
        counter.addProduct(chips, 3);  // 3 chips = 3 x 35 = 105
        counter.addProduct(rice, 2);  // 2 kg of rice = 2 x 45 = 90
        counter.addProduct(promoProduct, 2);  // Buy 2 get 1 free on a 100 priced product = 1 x 100 = 100
        counter.addProduct(promoProduct2, 3);  // Buy 3 get 1 free on a 50 priced product = 3 x 50 = 100
        assertEquals(395, counter.getTotalPrice(), 0.01);  // Total: 105 + 90 + 100 + 100 = 395
    }

    @Test
    public void testNoPromotionApplied() {
        counter.addProduct(promoProduct, 1);  // Only 1 promo product, so no promotion applied
        counter.addProduct(promoProduct2, 2);  // Only 2 promo product, so no promotion applied
        assertEquals(200, counter.getTotalPrice(), 0.01);
    }

    @Test
    public void testAddProductMultipleTimes() {
        counter.addProduct(promoProduct, 3);
        counter.addProduct(chips, 2);
        counter.addProduct(rice, 1.5);
        counter.addProduct(chips, 3);
        counter.addProduct(chips, 2);
        counter.addProduct(rice, 1.5);
        counter.addProduct(rice, 1.5);
        counter.addProduct(promoProduct2, 3);
        counter.addProduct(rice, 1.5);
        counter.addProduct(promoProduct2, 7);
        counter.addProduct(promoProduct, 3);
        assertEquals(1165.0, counter.getTotalPrice(), 0.01);
    }

    @Test
    public void testPrintReceipt() {
        counter.addProduct(chips, 2);
        counter.addProduct(rice, 1.5);
        counter.addProduct(promoProduct, 3);
        counter.addProduct(chips, 2);
        counter.addProduct(chips, 2);
        counter.addProduct(promoProduct, 3);
        counter.addProduct(rice, 1.5);
        counter.addProduct(promoProduct, 3);

        // Capture standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the method to print the receipt
        counter.printReceipt();

        // Defines the expected output
        String expectedOutput =
                "---------- RECEIPT ----------\r\n" +
                "Chips - 6.0 x 35.0 = 210.0\r\n" +
                "Rice - 3.0 x 45.0 = 135.0\r\n" +
                "PromoOneGetOneFree - 9.0 x 100.0 = 500.0\r\n" +
                "-----------------------------\r\n" +
                "TOTAL: 845.0\r\n";

        // Compare the captured output with the expected output
        assertEquals(expectedOutput, outContent.toString());
    }
}
