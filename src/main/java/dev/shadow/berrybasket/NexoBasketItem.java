package dev.shadow.berrybasket;

import com.nexomc.nexo.api.NexoItems;
import org.bukkit.inventory.ItemStack;

public final class NexoBasketItem implements BasketItem {
    private final String id;

    public NexoBasketItem(String id) {
        this.id = id;
    }

    @Override
    public boolean IsBasket(ItemStack item) {
        return id.equals(NexoItems.idFromItem(item));
    }

    @Override
    public ItemStack Create() {
        return NexoItems.itemFromId(id).build();
    }
}
