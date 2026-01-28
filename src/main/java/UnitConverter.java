import java.util.HashMap;
import java.util.Map;

public class UnitConverter {

    // ingredient -> (fromUnit -> (toUnit -> ratio))
    private static final Map<String, Map<UnitType, Map<UnitType, Double>>> rules = new HashMap<>();

    static {
        // TOMATE
        add("Tomate", UnitType.KG, UnitType.PCS, 10.0);

        // LAITUE
        add("Laitue", UnitType.KG, UnitType.PCS, 2.0);

        // CHOCOLAT
        add("Chocolat", UnitType.KG, UnitType.PCS, 10.0);
        add("Chocolat", UnitType.KG, UnitType.L, 2.5);

        // POULET
        add("Poulet", UnitType.KG, UnitType.PCS, 8.0);

        // BEURRE
        add("Beurre", UnitType.KG, UnitType.PCS, 4.0);
        add("Beurre", UnitType.KG, UnitType.L, 5.0);
    }

    private static void add(String ingredient, UnitType from, UnitType to, double ratio) {
        rules
                .computeIfAbsent(ingredient, k -> new HashMap<>())
                .computeIfAbsent(from, k -> new HashMap<>())
                .put(to, ratio);

        // inverse automatiquement
        rules.get(ingredient)
                .computeIfAbsent(to, k -> new HashMap<>())
                .put(from, 1 / ratio);
    }

    public static double convert(String ingredient, double quantity, UnitType from, UnitType to) {
        if (from == to) return quantity;

        Map<UnitType, Map<UnitType, Double>> ingredientRules = rules.get(ingredient);

        if (ingredientRules == null ||
                !ingredientRules.containsKey(from) ||
                !ingredientRules.get(from).containsKey(to)) {
            throw new RuntimeException(
                    "Conversion impossible pour " + ingredient + " : " + from + " → " + to
            );
        }

        return quantity * ingredientRules.get(from).get(to);
    }
}
