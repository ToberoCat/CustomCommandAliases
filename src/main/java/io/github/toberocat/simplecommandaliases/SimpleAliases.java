package io.github.toberocat.simplecommandaliases;

import io.github.toberocat.simplecommandaliases.action.provided.*;
import io.github.toberocat.simplecommandaliases.alias.Alias;
import io.github.toberocat.simplecommandaliases.alias.CommandAlias;
import io.github.toberocat.simplecommandaliases.alias.CommandLoader;
import io.github.toberocat.simplecommandaliases.command.AliasCommand;
import io.github.toberocat.simplecommandaliases.listener.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public final class SimpleAliases extends JavaPlugin {

    public static final String VERSION = "1.2.0";
    public static boolean LATEST_VERSION = true;
    public static PlayerJoinListener JOIN_LISTENER = new PlayerJoinListener();

    @Override
    public void onEnable() {
        registerActions();

        createDefault();
        registerCommands();
        checkForUpdate();

        getServer().getPluginManager().registerEvents(JOIN_LISTENER, this);

        PluginCommand cmd = getCommand("aliases");
        if (cmd == null) return;

        AliasCommand aliasCommand = new AliasCommand();
        cmd.setExecutor(aliasCommand);
        cmd.setTabCompleter(aliasCommand);

        sendDonate();
    }

    @Override
    public void onDisable() {
        Alias.dispose();
    }

    private void checkForUpdate() {
        UpdateChecker checker = new UpdateChecker(this, 105037);
        checker.getVersion(version -> {
            if (version.equals(VERSION)) return;
            LATEST_VERSION = false;

            Bukkit.getOnlinePlayers().forEach(x -> JOIN_LISTENER.send(x));

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "A newer version of" +
                    ChatColor.AQUA + " SimpleCommandAliases" + ChatColor.GREEN + " is" +
                    " available. " + ChatColor.GOLD + VERSION + ChatColor.GREEN +
                    " -> " + ChatColor.GOLD + version);
        });
    }

    private void createDefault() {
        if (new File(getDataFolder(), "commands").mkdirs()) {
            clone("commands/dirt.yml");
        }
    }

    public void reloadCommands() {
        Alias.ALIASES.values().forEach(Alias::reloadValues);
        registerCommands();
    }

    public void registerCommands() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Registering commands...");

        File[] list = new File(getDataFolder(), "commands").listFiles();
        if (list == null) return;

        int last = 0;
        int updated = 0;
        for (File cmd : list) {
            CommandAlias commandAlias = new CommandLoader(this, cmd);
            if (Alias.ALIASES.containsKey(commandAlias.label())) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +
                        "Updated command " + ChatColor.YELLOW + cmd.getName());
                updated++;
                continue;
            }

            new Alias(this, commandAlias);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +
                    "Registered command file " + ChatColor.YELLOW + cmd.getName());
            last++;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Registered " +
                ChatColor.YELLOW + last + ChatColor.GREEN + " commands. " + ChatColor.YELLOW + updated
                + ChatColor.GREEN + " were updated");
    }

    private void sendDonate() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "If you like this plugin, " +
                "please help me maintaining it. Consider donating at: " +
                ChatColor.BLUE + "https://www.paypal.com/donate/?hosted_button_id=CC9D88C4SG2FC");
    }

    private void clone(@NotNull String path) {
        try {
            InputStream src = SimpleAliases.class.getResourceAsStream("/" + path);
            if (src == null) return;

            Files.copy(src, Paths.get(getDataFolder().getAbsolutePath() + "/" + path),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerActions() {
        new ActionbarAction().register();
        new BroadcastAction().register();
        new ConsoleCommandAction().register();
        new MessageAction().register();
        new SoundAction().register();
        new PlayerCommandAction().register();
        new TitleAction().register();
        new DelayAction().register();
    }
}
