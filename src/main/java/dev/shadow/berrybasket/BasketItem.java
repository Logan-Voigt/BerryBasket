package dev.shadow.berrybasket;

import org.bukkit.inventory.ItemStack;

public interface BasketItem {
    boolean IsBasket(ItemStack item);
    ItemStack Create();
}
