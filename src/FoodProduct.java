/** Pärilus: toidutoode saab Product-klassilt nime, hinna, koguse ja nende meetodid. */
public class FoodProduct extends Product {
    private final String storageInstructions;

    public FoodProduct(String name, double price, int quantity, String storageInstructions) {
        super(name, price, quantity);
        this.storageInstructions = requireText(storageInstructions, "Säilitamisjuhis");
    }

    @Override
    public String getExtraInfo() {
        return "toidukaup; säilitamine: " + storageInstructions;
    }
}
