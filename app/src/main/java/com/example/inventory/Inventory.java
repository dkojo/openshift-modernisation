package com.example.inventory;

import java.util.List;

/** The stock list and its JSON form. Kept free of servlet code so it is easy to unit test. */
public final class Inventory {

    public record Item(String sku, String name, int quantity) {}

    public static final List<Item> ITEMS = List.of(
            new Item("WID-001", "Widget", 120),
            new Item("GAD-002", "Gadget", 45),
            new Item("SPR-003", "Sprocket", 0));

    private Inventory() {}

    public static int totalUnits() {
        return ITEMS.stream().mapToInt(Item::quantity).sum();
    }

    public static List<Item> outOfStock() {
        return ITEMS.stream().filter(i -> i.quantity() == 0).toList();
    }

    public static String toJson() {
        StringBuilder sb = new StringBuilder("{\"items\":[");
        for (int i = 0; i < ITEMS.size(); i++) {
            Item item = ITEMS.get(i);
            if (i > 0) sb.append(',');
            sb.append("{\"sku\":\"").append(item.sku())
              .append("\",\"name\":\"").append(item.name())
              .append("\",\"quantity\":").append(item.quantity()).append('}');
        }
        return sb.append("],\"totalUnits\":").append(totalUnits()).append('}').toString();
    }
}
