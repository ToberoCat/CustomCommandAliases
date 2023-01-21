package io.github.toberocat.simplecommandaliases.action.provided;

import io.github.toberocat.simplecommandaliases.SimpleAliases;
import io.github.toberocat.simplecommandaliases.action.Action;
import io.github.toberocat.simplecommandaliases.utils.StringUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionbarAction extends Action {

    @Override
    public @NotNull String label() {
        return "actionbar";
    }

    @Override
    public void run(@NotNull Player player, @NotNull String provided) {
        Bukkit.getScheduler().runTaskLater(SimpleAliases.getPlugin(SimpleAliases.class), () -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(StringUtils.format(provided)));
        }, 0);
    }
}
