package dev.shadow.berrybasket;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BasketCommand implements TabExecutor {
    private final BerryBasket plugin;

    public BasketCommand(BerryBasket plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendRichMessage("<gray>Usage: /" + label + " <reload|get|give>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                if (!sender.hasPermission("berrybasket.reload")) return noPerm(sender);
                plugin.reload();
                sender.sendRichMessage("<green>BerryBasket reloaded.");
            }
            case "get" -> {
                if (!sender.hasPermission("berrybasket.get")) return noPerm(sender);
                if (!(sender instanceof Player p)) {
                    sender.sendRichMessage("<red>Only players can use this. Try /" + label + " give <player>");
                    return true;
                }
                giveBasket(p);
                sender.sendRichMessage("<green>Here's your basket.");
            }
            case "give" -> {
                if (!sender.hasPermission("berrybasket.give")) return noPerm(sender);
                if (args.length < 2) {
                    sender.sendRichMessage("<gray>Usage: /" + label + " give <player>");
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendRichMessage("<red>Player not found: " + args[1]);
                    return true;
                }
                giveBasket(target);
                sender.sendRichMessage("<green>Gave a basket to " + target.getName() + ".");
            }
            default -> sender.sendRichMessage("<red>Unknown subcommand. Use reload, get, or give.");
        }
        return true;
    }

    private void giveBasket(Player player) {
        ItemStack basket = plugin.getProvider().Create();
        player.getInventory().addItem(basket).values()
                .forEach(left -> player.getWorld().dropItem(player.getLocation(), left));
    }

    private boolean noPerm(CommandSender sender) {
        sender.sendRichMessage("<red>You don't have permission.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> out = new ArrayList<>();

        if (args.length == 1) {
            for (String sub : List.of("reload", "get", "give")) {
                if (sender.hasPermission("berrybasket." + sub) && sub.startsWith(args[0].toLowerCase())) {
                    out.add(sub);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give") && sender.hasPermission("berrybasket.give")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) out.add(p.getName());
            }
        }
        return out;
    }
}