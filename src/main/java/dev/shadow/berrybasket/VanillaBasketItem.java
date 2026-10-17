package dev.shadow.berrybasket;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.List;

public final class VanillaBasketItem implements BasketItem {
    private final NamespacedKey KEY;
    private final String NAME;
    private final String LORE;
    private final Material MATERIAL;

    public VanillaBasketItem(Plugin plugin, String name, String lore, Material material){
        KEY = new NamespacedKey(plugin, "berry_basket");
        NAME = name;
        LORE = lore;
        MATERIAL = material;
    }

    @Override
    public ItemStack create()
    {
        ItemStack item = new ItemStack(MATERIAL);

        MiniMessage mm = MiniMessage.miniMessage();

        ItemMeta meta = item.getItemMeta();


        meta.itemName(mm.deserialize(NAME));
        meta.lore(List.of(
                mm.deserialize(LORE)
        ));
        meta.setMaxStackSize(1);

        meta.getPersistentDataContainer().set(KEY, PersistentDataType.BOOLEAN, true);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean isBasket(ItemStack item)
    {
        if (item == null || !item.hasItemMeta()) return false;

        return item.getItemMeta().getPersistentDataContainer().has(KEY);
    }
}
