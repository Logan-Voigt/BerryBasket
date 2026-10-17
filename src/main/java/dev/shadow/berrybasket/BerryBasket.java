package dev.shadow.berrybasket;

import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

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
            basketItem = SetupVanillaBasket(wantNexo);
        }
        boolean debug = getConfig().getBoolean("debug", false);
        boolean checkBelow = getConfig().getBoolean("detection.check-below", true);
        boolean checkHeadData = getConfig().getBoolean("detection.check-head-data", false);
        List headTextures = getConfig().getList("detection.head-textures");
        getServer().getPluginManager().registerEvents(new BerryBasketListener(basketItem, this), this);
    }

    public void reload()
    {
        reloadConfig();
    }

    private BasketItem SetupVanillaBasket(boolean wantNexo)
    {
        String materialName = getConfig().getString("items.vanilla-item.material", "BOWL");
        Material material = Material.matchMaterial(materialName);
        if (material == null || !material.isItem())
        {
            getLogger().warning("Invalid basket-material '" + materialName + "', using BOWL");
            material = Material.BOWL;
        }

        String name = getConfig().getString("items.vanilla-item.name");
        String lore = getConfig().getString("items.vanilla-item.lore");

        if (wantNexo) getLogger().warning("item-source is nexo but Nexo isn't installed, falling back to vanilla");

        return new VanillaBasketItem(this, name, lore, material);
    }

    public BasketItem getBasketItem() {
        return basketItem;
    }
}