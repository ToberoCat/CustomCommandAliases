package io.github.toberocat.simplecommandaliases.action.provided;

import io.github.toberocat.simplecommandaliases.SimpleAliases;
import io.github.toberocat.simplecommandaliases.action.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ConsoleCommandAction extends Action {

    @Override
    public @NotNull String label() {
        return "console";
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String provided) {
        Bukkit.getScheduler().runTaskLater(SimpleAliases.getPlugin(SimpleAliases.class), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), provided);
        }, 0);
    }
}
