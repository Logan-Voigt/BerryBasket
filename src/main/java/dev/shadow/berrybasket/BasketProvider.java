package dev.shadow.berrybasket;

import org.bukkit.inventory.ItemStack;

public interface BasketProvider {
    boolean IsBasket(ItemStack item);
    ItemStack Create();
}
