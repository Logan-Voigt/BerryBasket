package dev.shadow.berrybasket;

import io.papermc.paper.datacomponent.item.ResolvableProfile;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.entity.MushroomCow;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BerryBasketListener implements Listener {
    private final BasketItem basketItem;
    private final Plugin plugin;

    public BerryBasketListener(BasketItem basketItem, Plugin plugin)
    {
        this.basketItem = basketItem;
        this.plugin = plugin;
    }
    @EventHandler(ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event)
    {
        if (!basketItem.isBasket(event.getItemInHand())) return;

        Block block = event.getBlock();

        if (plugin.getConfig().getBoolean("debug", false)) event.getPlayer().sendMessage("Hit: " + block.getType());

        if (!isBerryHead(block)) return;

        event.setInstaBreak(true);
    }

    private boolean isBerryHead(Block block)
    {
        Material type = block.getType();
        boolean validSpot = type == Material.PLAYER_HEAD || type == Material.PLAYER_WALL_HEAD;

        if (plugin.getConfig().getBoolean("detection.check-below", true))
        {
            validSpot = validSpot && isBlockBelowFloweringAzalea(block);
        }

        if (plugin.getConfig().getBoolean("detection.check-head-data", false))
        {
            validSpot = validSpot && hasPyroLibProperty(block);
        }
        return validSpot;
    }

    private boolean isBlockBelowFloweringAzalea(Block block)
    {
        Block below = block.getRelative(BlockFace.DOWN);

        return below.getType() == Material.FLOWERING_AZALEA_LEAVES;
    }

    private boolean hasPyroLibProperty(Block block)
    {
        BlockState state = block.getState();
        if (state instanceof Skull) {
            Skull skull = (Skull) state;
            ResolvableProfile skullProfile = skull.getProfile();
            if (skullProfile == null) return false;

            return "Pyro-Lib".equals(skullProfile.name());

        }
        return false;
    }
    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (basketItem.isBasket(item)) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCrafter(CrafterCraftEvent event) {
        if (!(event.getBlock().getState() instanceof Crafter crafter)) return;
        for (ItemStack item : crafter.getInventory().getContents()) {
            if (basketItem.isBasket(item)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFuel(FurnaceBurnEvent event) {
        if (basketItem.isBasket(event.getFuel())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMooshroom(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof MushroomCow)) return;
        ItemStack hand = event.getPlayer().getInventory().getItem(event.getHand());
        if (basketItem.isBasket(hand)) event.setCancelled(true);
    }


}
