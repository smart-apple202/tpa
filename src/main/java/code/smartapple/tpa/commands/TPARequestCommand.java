package code.smartapple.tpa.commands;

import code.smartapple.tpa.utils.MessageUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
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

        // Підкоманди
        switch (args[0].toLowerCase()) {
            case "accept":
                handleAccept(sender);
                break;
            case "deny":
                handleDeny(sender);
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

        Player requester = (Player) sender;
        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null) {
            requester.sendMessage(MessageUtils.get("messages.player-not-online").replace("%player%", targetName));
            return;
        }

        if (target.equals(requester)) {
            requester.sendMessage(MessageUtils.get("messages.cannot-request-self"));
            return;
        }

        teleportRequests.put(target, requester);

        // Повідомлення для заявника
        requester.sendMessage(MessageUtils.get("messages.request-sent").replace("%player%", target.getName()));

        // Просте повідомлення без кнопок
        String baseMessage = MessageUtils.get("messages.request-received")
                .replace("%player%", requester.getName());
        target.sendMessage(baseMessage);
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
}