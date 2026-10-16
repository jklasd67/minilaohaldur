public class ElectronicProduct extends Product {
    private final int warrantyMonths;

    public ElectronicProduct(String name, double price, int quantity, int warrantyMonths) {
        super(name, price, quantity);
        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("Garantiikuud ei tohi olla negatiivsed.");
        }
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public String getExtraInfo() {
        return "elektroonika; garantii: " + warrantyMonths + " kuud";
    }
}
