/** Kõigi toodete ühine abstraktne baasklass. Sellest ei looda otse objekte. */
public abstract class Product {
    // private kapseldab andmed: kogust ja hinda saab muuta ainult kontrollitud meetoditega.
    private final String name;
    private double price;
    private int quantity;

    protected Product(String name, double price, int quantity) {
        this.name = requireText(name, "Toote nimi");
        setPrice(price);
        if (quantity < 0) {
            throw new IllegalArgumentException("Kogus ei tohi olla negatiivne.");
        }
        this.quantity = quantity;
    }

    protected static String requireText(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " ei tohi olla tühi.");
        }
        return value.trim();
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(double price) {
        if (!Double.isFinite(price) || price < 0) {
            throw new IllegalArgumentException("Hind peab olema lõplik mittenegatiivne arv.");
        }
        this.price = price;
    }

    /** Positiivne muutus lisab kaupa; negatiivne eemaldab. Viga jätab senise koguse alles. */
    public void changeQuantity(int change) {
        // long väldib int-liitmise ületäitumist enne piiride kontrollimist.
        long newQuantity = (long) quantity + change;
        if (newQuantity < 0 || newQuantity > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Uus kogus peab olema vahemikus 0 kuni "
                    + Integer.MAX_VALUE + ".");
        }
        quantity = (int) newQuantity;
    }

    public double getStockValue() {
        return price * quantity;
    }

    // Abstraktsioon: iga alamklass peab otsustama, millist lisainfot ta näitab.
    public abstract String getExtraInfo();

    @Override
    public String toString() {
        // Polümorfism: getExtraInfo() tegelik teostus sõltub objekti alamklassist.
        return String.format(java.util.Locale.ROOT,
                "%s | hind: %.2f € | kogus: %d | %s", name, price, quantity, getExtraInfo());
    }
}
