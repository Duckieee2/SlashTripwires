package dev.duckiee2;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static final int CD_DURA = 30; // pretty obvious but change that for longer/shorter cd
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        printBanner(); // my kewl banner again
    }

    private void printBanner() { // my auraful thing
        getComponentLogger().info(Component.text("====================================================", NamedTextColor.DARK_GRAY));
        getComponentLogger().info(Component.empty());

        getComponentLogger().info(Component.text("   _____ __           __       ______     _               _               ", TextColor.color(0xFF8AF8)));
        getComponentLogger().info(Component.text("  / ___// /___ ______/ /_     /_  __/____(_)___ _      __(_)_______  _____", TextColor.color(0xF87EEB)));
        getComponentLogger().info(Component.text("  \\__ \\/ / __ `/ ___/ __ \\     / / / ___/ / __ \\ | /| / / / ___/ _ \\/ ___/", TextColor.color(0xF172DE)));
        getComponentLogger().info(Component.text(" ___/ / / /_/ (__  ) / / /    / / / /  / / /_/ / |/ |/ / / /  /  __(__  ) ", TextColor.color(0xEA66D1)));
        getComponentLogger().info(Component.text("/____/_/\\__,_/____/_/ /_/    /_/ /_/  /_/ .___/|__/|__/_/_/   \\___/____/  ", TextColor.color(0xE35AC4)));
        getComponentLogger().info(Component.text("                                       /_/                                ", TextColor.color(0xDC4AB7)));

        getComponentLogger().info(Component.empty());

        getComponentLogger().info(
                Component.text("ᴄʀᴇᴀᴛᴇᴅ ʙʏ ", NamedTextColor.GRAY)
                        .append(Component.text("Duckiee2", TextColor.color(0xFE88FF)))
        );
        getComponentLogger().info(Component.text("====================================================", NamedTextColor.DARK_GRAY));
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, Command command, @NonNull String label, String @NonNull [] args) { // annotated so no no yellow text appears
        if (!command.getName().equalsIgnoreCase("tripwire")) return false;
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("ʏᴏᴜ ᴄᴀɴɴᴏᴛ ʀᴜɴ ᴛʜɪs ᴄᴏᴍᴍᴀɴᴅ ᴀs ʏᴏᴜ ᴀʀᴇɴ'ᴛ ᴀ ᴘʟᴀʏᴇʀ.", NamedTextColor.RED));return true;
        }

        UUID uuid = player.getUniqueId();
        long expires = cooldowns.getOrDefault(uuid, 0L);
        long remaining = expires - System.currentTimeMillis();

        if (remaining > 0) {
            int seconds = (int) Math.ceil(remaining / 1000.0);
            player.sendMessage(
                    Component.text("ʏᴏᴜ ᴍᴜsᴛ ᴡᴀɪᴛ ", NamedTextColor.RED)
                            .append(Component.text(seconds + "s", NamedTextColor.GOLD))
                            .append(Component.text(" ʙᴇғᴏʀᴇ ᴜsɪɴɢ ᴛʜɪs ᴄᴏᴍᴍᴀɴᴅ ᴀɢᴀɪɴ.", NamedTextColor.RED))
            );return true; // i wannan make less lines yk
        }

        cooldowns.put(uuid, System.currentTimeMillis() + (CD_DURA * 1000L));
        int amount = fillInventory(player);
        player.sendMessage(
                Component.text("ʏᴏᴜ ʜᴀᴠᴇ ʀᴇᴄɪᴇᴠᴇᴅ ", NamedTextColor.GREEN)
                        .append(Component.text(amount + " ᴛʀɪᴘᴡɪʀᴇ ʜᴏᴏᴋs", NamedTextColor.GOLD))
                        .append(Component.text(".", NamedTextColor.GREEN))
        );
        player.sendMessage(
                Component.text("ᴄᴏᴏʟᴅᴏᴡɴ: ", NamedTextColor.GRAY)
                        .append(Component.text(CD_DURA + "s", NamedTextColor.GOLD))
        );return true; // too lazy to repeat this
    }

    private int fillInventory(Player player) { // obvious
        int given = 0;
        ItemStack stack = new ItemStack(Material.TRIPWIRE_HOOK, 64);
        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item == null || item.getType() == Material.AIR) {
                player.getInventory().setItem(slot, stack.clone());
                given += 64;
            }
        }return given;
    }
}