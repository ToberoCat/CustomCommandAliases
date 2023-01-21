package io.github.toberocat.simplecommandaliases.alias;

import io.github.toberocat.simplecommandaliases.SimpleAliases;
import io.github.toberocat.simplecommandaliases.utils.YamlLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CommandLoader implements CommandAlias {

    private @NotNull FileConfiguration config;
    private @NotNull SimpleAliases plugin;
    private @NotNull File file;

    public CommandLoader(@NotNull SimpleAliases aliases, @NotNull File file) {
        this.config = createConfig(file, aliases);
        this.plugin = aliases;
        this.file = file;
    }

    @Override
    public @NotNull String label() {
        return config.getString("label");
    }

    @Override
    public @NotNull String description() {
        return config.getString("description");
    }

    @Override
    public @NotNull String usage() {
        return config.getString("usage");
    }

    @Override
    public @NotNull List<String> aliases() {
        return config.getStringList("aliases");
    }

    @Override
    public @NotNull String permission() {
        return config.getString("permission");
    }

    @Override
    public @NotNull String permissionMessage() {
        return config.getString("no-permission-message");
    }

    @Override
    public @NotNull String failureMessage() {
        return config.getString("error-message", "Something went wrong while executing this command");
    }

    @Override
    public @NotNull List<String> actions() {
        return config.getStringList("execution");
    }

    @Override
    public void reload() {
        config = createConfig(file, plugin);
    }

    private @NotNull FileConfiguration createConfig(@NotNull File file, @NotNull SimpleAliases aliases) {
        return new YamlLoader(file, aliases)
                .logger(aliases)
                .load()
                .fileConfiguration();
    }
}
