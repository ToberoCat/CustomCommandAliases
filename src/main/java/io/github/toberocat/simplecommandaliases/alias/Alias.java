package io.github.toberocat.simplecommandaliases.alias;

import io.github.toberocat.simplecommandaliases.AliasPluginCommand;
import io.github.toberocat.simplecommandaliases.SimpleAliases;
import io.github.toberocat.simplecommandaliases.action.Actions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Alias extends AliasPluginCommand<SimpleAliases> {

    public static final Map<String, Alias> ALIASES = new HashMap<>();

    private final CommandAlias alias;
    public Alias(@NotNull SimpleAliases plugin,
                 @NotNull CommandAlias alias) {
        super(plugin, alias.label());
        ALIASES.put(alias.label(), this);
        this.alias = alias;

        setDescription(alias.description());
        setUsage(alias.usage());
        setAliases(alias.aliases());
        setPermission(alias.permission());
        setPermissionMessage(alias.permissionMessage());

        registerCommand();
    }

    public static void dispose() {
        ALIASES.values().forEach(x -> x.unregister(COMMAND_MAP));
        ALIASES.clear();
    }

    public void reloadValues() {
        alias.reload();

        setDescription(alias.description());
        setUsage(alias.usage());
        setAliases(alias.aliases());
        setPermission(alias.permission());
        setPermissionMessage(alias.permissionMessage());
    }

    /**
     * Executes the given command, returning its success
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        new Actions(alias.actions())
                .placeholder("{player}", sender.getName())
                .run(sender)
                .thenAccept(result -> {
                    if (result) return;
                    sender.sendMessage(alias.failureMessage());
                });
        return true;
    }
}
