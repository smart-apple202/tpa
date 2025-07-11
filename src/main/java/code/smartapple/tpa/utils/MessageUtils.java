package code.smartapple.tpa.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class MessageUtils {

    private static FileConfiguration messages;
    private static String globalPrefix;

    public static void init(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(file);

        updateMessagesFile(plugin, file);

        globalPrefix = colorize(messages.getString("messages.prefix", "&6[TPA] "));
    }

    public static String colorize(String message) {
        if (message == null) return "";

        String colored = ChatColor.translateAlternateColorCodes('&', message);

        java.util.regex.Pattern hexPattern = java.util.regex.Pattern.compile("(?i)&#([A-Fa-f0-9]{6})");
        java.util.regex.Matcher matcher = hexPattern.matcher(colored);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            StringBuilder replacement = new StringBuilder("§x");
            for (char c : hex.toCharArray()) {
                replacement.append('§').append(c);
            }
            matcher.appendReplacement(buffer, replacement.toString());
        }
        matcher.appendTail(buffer);

        return buffer.toString();
    }

    public static String get(String path) {
        String message = messages.getString(path, "&c[TPA] Повідомлення відсутнє!");
        return globalPrefix + colorize(message);
    }

    public static List<String> getFormattedList(String path, boolean includePrefix) {
        List<String> lines = messages.getStringList(path);
        if (lines.isEmpty()) {
            lines.add("&c[TPA] Повідомлення відсутнє!");
        }
        for (int i = 0; i < lines.size(); i++) {
            if (includePrefix) {
                lines.set(i, globalPrefix + colorize(lines.get(i)));
            } else {
                lines.set(i, colorize(lines.get(i)));
            }
        }
        return lines;
    }

    private static void updateMessagesFile(JavaPlugin plugin, File file) {
        FileConfiguration defaultMessages = YamlConfiguration.loadConfiguration(
                new InputStreamReader(plugin.getResource("messages.yml"), StandardCharsets.UTF_8)
        );

        boolean updated = false;

        Set<String> defaultKeys = defaultMessages.getKeys(true);
        for (String key : defaultKeys) {
            if (!messages.contains(key)) {
                messages.set(key, defaultMessages.get(key));
                updated = true;
            }
        }

        if (updated) {
            try {
                messages.save(file);
                plugin.getLogger().info("messages.yml було оновлено з відсутніми ключами.");
            } catch (IOException e) {
                plugin.getLogger().severe("Не вдалося оновити файл messages.yml: " + e.getMessage());
            }
        }
    }
}