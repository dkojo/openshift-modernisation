package com.example.inventory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void totalUnitsAddsUpEveryItem() {
        assertEquals(165, Inventory.totalUnits());
    }

    @Test
    void outOfStockFindsItemsWithZeroQuantity() {
        assertEquals("SPR-003", Inventory.outOfStock().get(0).sku());
    }

    @Test
    void jsonListsEveryItemAndTheTotal() {
        String json = Inventory.toJson();
        assertTrue(json.contains("\"sku\":\"WID-001\""));
        assertTrue(json.endsWith("\"totalUnits\":165}"));
    }
}
