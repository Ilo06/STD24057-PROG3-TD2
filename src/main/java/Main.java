import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataRetriever dr = new DataRetriever();

        System.out.println("--- TEST 1: Calcul Marge sur plat existant avec prix (Salade fraîche) ---");
        Dish salade = dr.findDishById(1); // ID 1 a un prix de 2000
        if (salade != null) {
            System.out.println("Plat: " + salade.getName());
            System.out.println("Prix de vente: " + salade.getUnitPrice());
            System.out.println("Coût ingrédients: " + salade.getDishCost());
            System.out.println("Marge brute: " + salade.getGrossMargin());
        }

        System.out.println("\n--- TEST 2: Calcul Marge sur plat sans prix (Gâteau au chocolat) ---");
        Dish gateau = dr.findDishById(4); // ID 4 a un prix null
        if (gateau != null) {
            System.out.println("Plat: " + gateau.getName());
            try {
                System.out.println("Tentative de calcul de marge...");
                System.out.println("Marge: " + gateau.getGrossMargin());
            } catch (RuntimeException e) {
                System.out.println("EXCEPTION ATTENDUE : " + e.getMessage());
            }
        }

        System.out.println("\n--- TEST 3: SaveDish (Mise à jour d'un prix) ---");
        // On donne un prix au Gâteau (ID 4) qui n'en avait pas
        if (gateau != null) {
            gateau.setUnitPrice(5000.0); // On fixe le prix
            dr.saveDish(gateau); // On sauvegarde (Update)

            // On récupère à nouveau pour vérifier
            Dish gateauUpdated = dr.findDishById(4);
            System.out.println("Nouveau prix du gâteau en base: " + gateauUpdated.getUnitPrice());
            System.out.println("Nouvelle marge calculable: " + gateauUpdated.getGrossMargin());
        }
    }
}