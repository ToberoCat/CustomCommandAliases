package io.github.toberocat.simplecommandaliases.action;

import io.github.toberocat.simplecommandaliases.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Actions {

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final List<String> strings;
    private final Map<String, String> placeholders = new HashMap<>();


    public Actions(@NotNull List<String> strings) {
        this.strings = strings;
    }

    public Actions(@NotNull String string) {
        this(new ArrayList<>(List.of(string)));
    }

    public Actions() {
        this(new ArrayList<>());
    }

    public void add(@NotNull String string) {
        strings.add(string);
    }

    public @NotNull Actions placeholder(@Nullable String what, @Nullable String with) {
        placeholders.put(what, with);
        return this;
    }

    public @NotNull Actions placeholders(@NotNull Map<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    /**
     * @param commandSender the sender executing
     * @return A completable future containing the data if all actions succeeded with their execution
     */
    public @NotNull CompletableFuture<Boolean> run(@NotNull CommandSender commandSender) {
        return CompletableFuture.supplyAsync(() -> {
            boolean success = true;
            for (String string : strings) {
                string = StringUtils.replace(string, placeholders);
                success = success && ActionCore.run(string, commandSender);
            }

            return success;
        }, threadPool);
    }
}
