package code.smartapple.tpa.events;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TPAToggleManager {

    private static final Set<UUID> toggledPlayers = new HashSet<>();

    public static boolean togglePlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (toggledPlayers.contains(playerUUID)) {
            toggledPlayers.remove(playerUUID);
            return false;
        } else {
            toggledPlayers.add(playerUUID);
            return true;
        }
    }

    public boolean isToggled(Player player) {
        return toggledPlayers.contains(player.getUniqueId());
    }

    // Очистка при завершенні плагіну
    public void clear() {
        toggledPlayers.clear();
    }
}