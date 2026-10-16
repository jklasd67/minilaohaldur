import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Konsooliliides küsib sisendit ning kutsub välja Inventory ja Product meetodid. */
public class Main {
    private final Scanner scanner = new Scanner(System.in, "UTF-8");
    private final Inventory inventory = new Inventory();

    public static void main(String[] args) {
        Main application = new Main();
        try {
            application.run();
        } catch (NoSuchElementException exception) {
            // EOF (nt Ctrl+D) lõpetab ka poolelioleva sisestamise rahulikult.
            System.out.println("\nSisend lõppes. Programm sulgub.");
        }
    }

    private void run() {
        inventory.addProduct(new FoodProduct("Piim", 1.50, 10, "külmkapis"));
        inventory.addProduct(new FoodProduct("Leib", 2.00, 5, "kuivas kohas"));
        inventory.addProduct(new ElectronicProduct("Veekeetja", 25.00, 2, 24));

        boolean running = true;
        while (running) {
            System.out.println("\n--- POE LAOHALDUS ---");
            System.out.println("1. Kuva kõik tooted");
            System.out.println("2. Lisa uus toode");
            System.out.println("3. Muuda toote kogust");
            System.out.println("4. Otsi nime järgi");
            System.out.println("5. Kuva lao koguväärtus");
            System.out.println("0. Välju");
            int choice = readInt("Valik: ", 0, 5);
            try {
                switch (choice) {
                    case 1 -> printProducts(inventory.getProducts());
                    case 2 -> addProduct();
                    case 3 -> changeQuantity();
                    case 4 -> printProducts(inventory.searchByName(readText("Otsing (osa nimest): ")));
                    case 5 -> System.out.printf(Locale.ROOT, "Lao koguväärtus: %.2f €%n",
                            inventory.getTotalValue());
                    case 0 -> running = false;
                    default -> throw new IllegalStateException("Tundmatu menüüvalik.");
                }
            } catch (IllegalArgumentException exception) {
                // Mudeli kontrolli ebaõnnestumine ei lõpeta kogu programmi.
                System.out.println("Viga: " + exception.getMessage());
            }
        }
        System.out.println("Head aega!");
    }

    private void addProduct() {
        int type = readInt("Liik (1 = toidukaup, 2 = elektroonika): ", 1, 2);
        String name = readText("Nimi: ");
        double price = readPrice();
        int quantity = readInt("Kogus: ", 0, Integer.MAX_VALUE);
        Product product;
        if (type == 1) {
            product = new FoodProduct(name, price, quantity, readText("Säilitamisjuhis: "));
        } else {
            int warranty = readInt("Garantii kuudes: ", 0, Integer.MAX_VALUE);
            product = new ElectronicProduct(name, price, quantity, warranty);
        }
        inventory.addProduct(product);
        System.out.println("Toode lisatud: " + product);
    }

    private void changeQuantity() {
        String name = readText("Toote täpne nimi: ");
        int change = readInt("Koguse muutus (nt 5 või -2): ", Integer.MIN_VALUE, Integer.MAX_VALUE);
        inventory.changeQuantity(name, change);
        System.out.println("Kogus muudetud: " + inventory.findByExactName(name));
    }

    private void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Tooteid ei leitud.");
        }
        for (Product product : products) {
            // Sama print-käsk kuvab eri alamklasside puhul erinevat lisainfot.
            System.out.println(product);
        }
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("Sisend ei tohi olla tühi.");
        }
    }

    private int readInt(String prompt, int minimum, int maximum) {
        while (true) {
            try {
                // Kõik sisendid loetakse tervete ridadena, et vältida nextInt/nextLine segamist.
                int value = Integer.parseInt(readText(prompt));
                if (value >= minimum && value <= maximum) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                // Sobimatu arv või liiga suur täisarv: küsi uuesti.
            }
            System.out.println("Sisesta täisarv vahemikus " + minimum + " kuni " + maximum + ".");
        }
    }

    private double readPrice() {
        while (true) {
            try {
                // Hind võib sisaldada nii koma kui ka punkti.
                double price = Double.parseDouble(readText("Hind eurodes: ").replace(',', '.'));
                if (Double.isFinite(price) && price >= 0) {
                    return price;
                }
            } catch (NumberFormatException exception) {
                // Vigane arv ei muuda lao andmeid.
            }
            System.out.println("Sisesta lõplik mittenegatiivne hind, näiteks 2,50.");
        }
    }
}
