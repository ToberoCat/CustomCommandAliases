package io.github.toberocat.simplecommandaliases.action.provided;

import io.github.toberocat.simplecommandaliases.SimpleAliases;
import io.github.toberocat.simplecommandaliases.action.Action;
import io.github.toberocat.simplecommandaliases.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends Action {

    @Override
    public @NotNull String label() {
        return "message";
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String provided) {
        Bukkit.getScheduler().runTaskLater(SimpleAliases.getPlugin(SimpleAliases.class), () -> {
            commandSender.sendMessage(StringUtils.format(provided));
        }, 0);
    }
}
