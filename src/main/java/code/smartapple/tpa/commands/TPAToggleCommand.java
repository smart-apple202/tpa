package code.smartapple.tpa.commands;

import code.smartapple.tpa.TPA;
import code.smartapple.tpa.utils.MessageUtils;
import code.smartapple.tpa.events.TPAToggleManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPAToggleCommand implements CommandExecutor {

    private final TPAToggleManager toggleManager;

    public TPAToggleCommand(TPA tpa) {
        this.toggleManager = tpa.getToggleManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.get("messages.no-permission"));
            return true;
        }

        Player player = (Player) sender;
        boolean isEnabled = toggleManager.togglePlayer(player);

        if (isEnabled) {
            player.sendMessage(MessageUtils.get("messages.toggle-enabled"));
        } else {
            player.sendMessage(MessageUtils.get("messages.toggle-disabled"));
        }

        return true;
    }
}