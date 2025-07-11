package code.smartapple.tpa;

import code.smartapple.tpa.commands.*;
import code.smartapple.tpa.events.TPAToggleManager;
import code.smartapple.tpa.utils.MessageUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class TPA extends JavaPlugin {

    private TPAToggleManager toggleManager;

    @Override
    public void onEnable() {
        MessageUtils.init(this);

        toggleManager = new TPAToggleManager();

        registerCommand(new TPARequestCommand(this), new TPACommandCompleter());
        registerCommand("tpa accept", new TPAAcceptCommand(this));
        registerCommand("tpa deny", new TPADenyCommand(this));
        registerCommand("tpa toggle", new TPAToggleCommand(this));
        registerCommand("tpa help", new TPAHelpCommand(this));
        registerCommand("tpa reload", new TPAReloadCommand(this));

        getLogger().info("[TPA] Плагін завантажується...");
        getLogger().info("[TPA] успішно увімкнений!");
    }

    @Override
    public void onDisable() {
        getLogger().info("TPA вимкнений!");
    }

    private void registerCommand(String name, CommandExecutor executor) {
        Objects.requireNonNull(getCommand(name)).setExecutor(executor);
    }

    private void registerCommand(CommandExecutor executor, TabCompleter completer) {
        Objects.requireNonNull(getCommand("tpa")).setExecutor(executor);
        Objects.requireNonNull(getCommand("tpa")).setTabCompleter(completer);
    }

    public TPAToggleManager getToggleManager() {
        return toggleManager;
    }
}