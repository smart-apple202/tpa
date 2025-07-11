package code.smartapple.tpa.commands;

import code.smartapple.tpa.TPA;
import code.smartapple.tpa.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TPAHelpCommand implements CommandExecutor {

    public TPAHelpCommand(TPA tpa) {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        MessageUtils.getFormattedList("messages.help-message", false).forEach(sender::sendMessage);
        return true;
    }
}