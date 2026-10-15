package dev.shadow.berrybasket;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.block.Crafter;
import org.bukkit.entity.MushroomCow;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class BerryBasketListener implements Listener {
    private final BasketProvider provider;
    private final boolean debug;
    private final boolean checkBelow;
    private final boolean checkHeadData;

    public BerryBasketListener(BasketProvider provider,boolean checkBelow, boolean checkHeadData, boolean debug)
    {
        this.provider = provider;
        this.debug = debug;
        this.checkBelow = checkBelow;
        this.checkHeadData = checkHeadData;
    }
    @EventHandler(ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event)
    {
        if (!provider.IsBasket(event.getItemInHand())) return;

        Block block = event.getBlock();

        if (debug) {event.getPlayer().sendMessage("Hit: " + block.getType());}

        if (!isBerryHead(block)) return;

        event.setInstaBreak(true);
    }

    private boolean isBerryHead(Block block)
    {
        Material type = block.getType();
        boolean validSpot = type == Material.PLAYER_HEAD || type == Material.PLAYER_WALL_HEAD;

        if (checkBelow)
        {
            validSpot = validSpot && isBlockBelowFloweringAzalea(block);
        }

        if (checkHeadData)
        {
            // TODO
        }
        return validSpot;
    }

    private boolean isBlockBelowFloweringAzalea(Block block)
    {
        Block below = block.getRelative(BlockFace.DOWN);

        return below.getType() == Material.FLOWERING_AZALEA_LEAVES;
    }


    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        for (ItemStack item : event.getInventory().getMatrix()) {
            if (provider.IsBasket(item)) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCrafter(CrafterCraftEvent event) {
        if (!(event.getBlock().getState() instanceof Crafter crafter)) return;
        for (ItemStack item : crafter.getInventory().getContents()) {
            if (provider.IsBasket(item)) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFuel(FurnaceBurnEvent event) {
        if (provider.IsBasket(event.getFuel())) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMooshroom(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof MushroomCow)) return;
        ItemStack hand = event.getPlayer().getInventory().getItem(event.getHand());
        if (provider.IsBasket(hand)) event.setCancelled(true);
    }


}
