package code.smartapple.tpa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TPACommandCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("tpa")) {
            if (args.length == 1) {
                List<String> subCommands = Arrays.asList("accept", "deny", "toggle", "reload", "help");

                List<String> onlinePlayers = Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());

                List<String> suggestions = new ArrayList<>();
                suggestions.addAll(subCommands);
                suggestions.addAll(onlinePlayers);

                if (!args[0].isEmpty()) {
                    String input = args[0].toLowerCase();
                    suggestions = suggestions.stream()
                            .filter(s -> s.toLowerCase().startsWith(input))
                            .collect(Collectors.toList());
                }

                return suggestions;
            }
        }
        return null;
    }
}