/** Sõltuvusteta kontrollid: käivita java -cp out InventoryTest. */
public class InventoryTest {
    private static int checks;

    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        Product milk = new FoodProduct("Piim", 1.50, 10, "külmkapis");
        Product kettle = new ElectronicProduct("Veekeetja", 25, 2, 24);
        inventory.addProduct(milk);
        inventory.addProduct(kettle);
        check(Math.abs(inventory.getTotalValue() - 65) < 0.000001, "Lao algväärtus");
        check(inventory.searchByName("IIM").get(0) == milk, "Osaline tõstutundetu otsing");
        check(inventory.findByExactName(" piim ") == milk, "Täpne nimi ja tühikud");
        check(inventory.searchByName("puuduv").isEmpty(), "Puuduv otsinguvastus");
        check(milk.toString().contains("säilitamine: külmkapis")
                && kettle.toString().contains("garantii: 24 kuud"), "Polümorfne lisainfo");

        inventory.changeQuantity("piim", 3);
        inventory.changeQuantity("PIIM", -13);
        check(milk.getQuantity() == 0, "Koguse suurendamine ja nullini vähendamine");
        check(Math.abs(inventory.getTotalValue() - 50) < 0.000001, "Nullkogusega toote väärtus");
        rejects(() -> inventory.changeQuantity("Piim", -1));
        check(milk.getQuantity() == 0, "Keelatud muudatus jätab koguse alles");
        inventory.changeQuantity("Piim", Integer.MAX_VALUE);
        rejects(() -> inventory.changeQuantity("Piim", 1));
        check(milk.getQuantity() == Integer.MAX_VALUE, "Koguse ületäitumise kaitse");
        rejects(() -> inventory.changeQuantity("Puuduv", 1));
        rejects(() -> inventory.addProduct(new FoodProduct(" pIIm ", 1, 1, "kuivas")));
        rejects(() -> inventory.addProduct(null));
        rejects(() -> inventory.searchByName(" "));
        rejects(() -> new FoodProduct(" ", 1, 1, "kuivas"));
        rejects(() -> new FoodProduct("Leib", -1, 1, "kuivas"));
        rejects(() -> new FoodProduct("Leib", 1, -1, "kuivas"));
        rejects(() -> milk.setPrice(Double.NaN));
        rejects(() -> milk.setPrice(Double.POSITIVE_INFINITY));
        rejects(() -> new ElectronicProduct("Raadio", 10, 1, -1));
        try {
            inventory.getProducts().clear();
            throw new AssertionError("Loendit ei tohi väljast tühjendada.");
        } catch (UnsupportedOperationException expected) {
            checks++;
        }
        System.out.println("Kõik " + checks + " kontrolli läbitud.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        checks++;
    }

    private static void rejects(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            checks++;
            return;
        }
        throw new AssertionError("Ootasin IllegalArgumentException-erindit.");
    }
}
