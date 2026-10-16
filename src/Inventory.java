import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Haldab toodete kogumit; kasutajalt sisendi küsimine jääb Main-klassi. */
public class Inventory {
    // Ühes loendis võivad olla mõlema alamklassi objektid, sest kõik on Product-id.
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Toode ei tohi olla null.");
        }
        if (findByExactName(product.getName()) != null) {
            throw new IllegalArgumentException("Sama nimega toode on juba olemas.");
        }
        products.add(product);
    }

    public List<Product> getProducts() {
        // Vaade keelab välisel koodil loendisse otse lisada või sealt eemaldada.
        // Toodete endi koguseid saab endiselt muuta nende kontrollitud meetodiga.
        return Collections.unmodifiableList(products);
    }

    public Product findByExactName(String name) {
        String query = Product.requireText(name, "Otsing");
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(query)) {
                return product;
            }
        }
        return null;
    }

    /** Osalise nime otsing tagastab kõik vasted, arvestamata suur- ja väiketähti. */
    public List<Product> searchByName(String name) {
        String query = Product.requireText(name, "Otsing").toLowerCase(Locale.ROOT);
        List<Product> matches = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase(Locale.ROOT).contains(query)) {
                matches.add(product);
            }
        }
        return matches;
    }

    public void changeQuantity(String name, int change) {
        Product product = findByExactName(name);
        if (product == null) {
            throw new IllegalArgumentException("Sellise nimega toodet ei leitud.");
        }
        product.changeQuantity(change);
    }

    public double getTotalValue() {
        double total = 0;
        for (Product product : products) {
            total += product.getStockValue();
        }
        return total;
    }
}
