package code.smartapple.tpa.commands;

import code.smartapple.tpa.TPA;
import code.smartapple.tpa.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPADenyCommand implements CommandExecutor {

    public TPADenyCommand(TPA tpa) {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.get("messages.no-permission"));
            return true;
        }

        Player target = (Player) sender;
        Player requester = TPARequestCommand.getRequester(target);

        if (requester == null) {
            target.sendMessage(MessageUtils.get("messages.no-request-to-accept"));
            return true;
        }

        TPARequestCommand.removeRequest(target);

        target.sendMessage(MessageUtils.get("messages.request-denied").replace("%player%", requester.getName()));
        requester.sendMessage(MessageUtils.get("messages.request-denied-to-requester").replace("%player%", target.getName()));

        return true;
    }
}