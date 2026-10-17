package dev.shadow.berrybasket;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.entity.Player;

public class BerryBasket extends JavaPlugin {
    BasketItem basketItem;
    @Override
    public void onEnable() {
        getLogger().info("loaded");

        saveDefaultConfig();
        setup();

        BasketCommand command = new BasketCommand(this);
        getCommand("berryBasket").setExecutor(command);
        getCommand("berryBasket").setTabCompleter(command);
    }

    private void setup()
    {
        boolean wantNexo = getConfig().getString("items.item-source", "vanilla").equalsIgnoreCase("nexo");
        boolean hasNexo = getServer().getPluginManager().isPluginEnabled("Nexo");


        if (wantNexo && hasNexo) {
            basketItem = new NexoBasketItem(getConfig().getString("items.nexo-item.id", "berry_basket"));
        }
        else {
            basketItem = setupVanillaBasket(wantNexo);
        }
        getServer().getPluginManager().registerEvents(new BerryBasketListener(basketItem, this), this);
    }

    public void reload()
    {
        reloadConfig();
        HandlerList.unregisterAll(this);
        setup();
    }

    private BasketItem setupVanillaBasket(boolean wantNexo)
    {
        String materialName = getConfig().getString("items.vanilla-item.material", "BOWL");
        Material material = Material.matchMaterial(materialName);
        if (material == null || !material.isItem())
        {
            getLogger().warning("Invalid basket-material '" + materialName + "', using BOWL");
            material = Material.BOWL;
        }

        String name = getConfig().getString("items.vanilla-item.name", "<green>Berry Basket");
        String lore = getConfig().getString("items.vanilla-item.lore", "<!italic><gray>Instantly mines berries from PyroFarming");

        if (wantNexo) getLogger().warning("item-source is nexo but Nexo isn't installed, falling back to vanilla");

        return new VanillaBasketItem(this, name, lore, material);
    }

    public BasketItem getBasketItem() {
        return basketItem;
    }

    public boolean isBasketItem(ItemStack item)
    {
        NamespacedKey KEY = new NamespacedKey(this, "berry_basket");
        boolean hasNexo = getServer().getPluginManager().isPluginEnabled("Nexo");
        String nexoID = getConfig().getString("items.nexo-item.id", "berry_basket");

        if (item == null || item.getType().isAir()) return false;
        if (item.getItemMeta().getPersistentDataContainer().has(KEY)) return true;

        if (hasNexo && nexoID.equals(NexoItems.idFromItem(item))) return true;

        return false;
    }
    public boolean fixBasketItem(Player player)
    {
        if (!isBasketItem(player.getInventory().getItemInMainHand())) return false;

        player.getInventory().setItemInMainHand(basketItem.create());
        return  true;
    }
}