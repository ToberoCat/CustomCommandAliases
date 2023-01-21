package io.github.toberocat.simplecommandaliases.listener;

import io.github.toberocat.simplecommandaliases.SimpleAliases;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void join(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        send(player);
    }

    public void send(@NotNull Player player) {
        if (!player.isOp()) return;
        if (SimpleAliases.LATEST_VERSION) return;

        player.sendMessage(ChatColor.GREEN + "A newer version of" +
                ChatColor.AQUA + " SimpleCommandAliases" + ChatColor.GREEN + " is" +
                " available. Check it on spigot: " +
                "https://www.spigotmc.org/resources/simple-aliases.105037/");
    }
}
