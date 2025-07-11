package code.smartapple.tpa.commands;

import code.smartapple.tpa.utils.MessageUtils;
import code.smartapple.tpa.events.TPAToggleManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TPARequestCommand implements CommandExecutor {

    private static final Map<Player, Player> teleportRequests = new HashMap<>();
    private final JavaPlugin plugin;

    public TPARequestCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public static Player getRequester(Player target) {
        return teleportRequests.get(target);
    }

    public static void removeRequest(Player target) {
        teleportRequests.remove(target);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessageUtils.get("messages.usage-request"));
            return true;
        }

        //підкоманди
        switch (args[0].toLowerCase()) {
            case "accept":
                handleAccept(sender);
                break;
            case "deny":
                handleDeny(sender);
                break;
            case "toggle":
                handleToggle(sender);
                break;
            case "reload":
                handleReload(sender);
                break;
            case "help":
                handleHelp(sender);
                break;
            default:
                handleRequest(sender, args[0]);
                break;
        }

        return true;
    }

    private void handleRequest(CommandSender sender, String targetName) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.get("messages.no-permission"));
            return;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getPlayerExact(targetName); // Пошук тільки за точним нікнеймом
        if (target == null) {
            player.sendMessage(MessageUtils.get("messages.player-not-online").replace("%player%", targetName));
            return;
        }

        if (target.equals(player)) {
            player.sendMessage(MessageUtils.get("messages.cannot-request-self"));
            return;
        }

        teleportRequests.put(target, player);
        player.sendMessage(MessageUtils.get("messages.request-sent").replace("%player%", target.getName()));
        target.sendMessage(MessageUtils.get("messages.request-received").replace("%player%", player.getName()));
    }

    private void handleAccept(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.get("messages.no-permission"));
            return;
        }

        Player target = (Player) sender;
        Player requester = teleportRequests.get(target);
        if (requester == null) {
            target.sendMessage(MessageUtils.get("messages.no-request-to-accept"));
            return;
        }

        requester.teleport(target.getLocation());
        teleportRequests.remove(target);

        target.sendMessage(MessageUtils.get("messages.request-accepted").replace("%player%", requester.getName()));
        requester.sendMessage(MessageUtils.get("messages.request-accepted-to-requester").replace("%player%", target.getName()));
    }

    private void handleDeny(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.get("messages.no-permission"));
            return;
        }

        Player target = (Player) sender;
        Player requester = teleportRequests.get(target);
        if (requester == null) {
            target.sendMessage(MessageUtils.get("messages.no-request-to-accept"));
            return;
        }

        teleportRequests.remove(target);

        target.sendMessage(MessageUtils.get("messages.request-denied").replace("%player%", requester.getName()));
        requester.sendMessage(MessageUtils.get("messages.request-denied-to-requester").replace("%player%", target.getName()));
    }

    private void handleToggle(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.get("messages.no-permission"));
            return;
        }

        Player player = (Player) sender;
        boolean isEnabled = TPAToggleManager.togglePlayer(player);

        if (isEnabled) {
            player.sendMessage(MessageUtils.get("messages.toggle-enabled"));
        } else {
            player.sendMessage(MessageUtils.get("messages.toggle-disabled"));
        }
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("tpa.command.reload")) {
            sender.sendMessage(MessageUtils.get("messages.no-permission"));
            return;
        }

        plugin.reloadConfig();
        MessageUtils.init(plugin);
        sender.sendMessage(MessageUtils.get("messages.reload-success"));
    }

    private void handleHelp(CommandSender sender) {
        MessageUtils.getFormattedList("messages.help-message", false).forEach(sender::sendMessage);
    }
}