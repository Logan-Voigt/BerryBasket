package dev.shadow.berrybasket;

import org.bukkit.inventory.ItemStack;

public interface BasketItem {
    boolean isBasket(ItemStack item);
    ItemStack create();
}
