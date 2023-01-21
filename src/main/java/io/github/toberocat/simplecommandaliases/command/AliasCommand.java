package io.github.toberocat.simplecommandaliases.command;

import io.github.toberocat.simplecommandaliases.SimpleAliases;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class AliasCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) return false;

        if (args[0].equalsIgnoreCase("reload")) {
            SimpleAliases.getPlugin(SimpleAliases.class).reloadCommands();
            sender.sendMessage("§8[§eSCA§8]§7 Reloaded commands");
            sender.sendMessage("§8[§eSCA§8] §6§lWarning: §7Please be aware that reloading might cause some issues. " +
                    "Please §erestart§7 your server once you're done creating your command(s)");
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of("reload");
    }
}
