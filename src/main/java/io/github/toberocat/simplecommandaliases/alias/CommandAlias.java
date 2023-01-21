package io.github.toberocat.simplecommandaliases.alias;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CommandAlias {

    @NotNull String label();

    @NotNull String description();

    @NotNull String usage();

    @NotNull List<String> aliases();

    @NotNull String permission();

    @NotNull String permissionMessage();

    @NotNull String failureMessage();

    @NotNull List<String> actions();

    void reload();
}
