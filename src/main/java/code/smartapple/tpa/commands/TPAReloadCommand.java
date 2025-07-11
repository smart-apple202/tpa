package code.smartapple.tpa.commands;

import code.smartapple.tpa.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TPAReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public TPAReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.hasPermission("tpa.command.reload")) {
            sender.sendMessage(MessageUtils.get("messages.no-permission"));
            return true;
        }

        plugin.reloadConfig();
        MessageUtils.init(plugin);
        sender.sendMessage(MessageUtils.get("messages.reload-success"));
        return true;
    }
}