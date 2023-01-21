package io.github.toberocat.simplecommandaliases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AliasPluginCommand<T extends JavaPlugin> extends Command
        implements CommandExecutor, PluginIdentifiableCommand {

    public static final @Nullable CommandMap COMMAND_MAP = getMap();
    private final T plugin;
    private final HashMap<Integer, ArrayList<TabCommand>> tabComplete;
    private boolean register = false;


    /**
     * @param plugin plugin responsible of the command.
     * @param name   name of the command.
     */
    public AliasPluginCommand(T plugin, String name) {
        super(name);

        assert COMMAND_MAP != null;
        assert plugin != null;
        assert name.length() > 0;

        setLabel(name);
        this.plugin = plugin;
        tabComplete = new HashMap<>();
    }

    private static @Nullable CommandMap getMap() {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            return (CommandMap) f.get(Bukkit.getServer());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }


    //<editor-fold desc="add / set">

    /**
     * @param aliases aliases of the command.
     */
    protected void setAliases(String... aliases) {
        if (aliases != null && (register || aliases.length > 0))
            setAliases(Arrays.stream(aliases).collect(Collectors.toList()));
    }

    //<editor-fold desc="TabbComplete">

    /**
     * Add multiple arguments to an index with permission and words before
     *
     * @param indice     index where the argument is in the command. /myCmd is at the index -1, so
     *                   /myCmd index0 index1 ...
     * @param permission permission to add (may be null)
     * @param beforeText text preceding the argument (may be null)
     * @param arg        word to add
     */
    protected void addTabbComplete(int indice, String permission, String[] beforeText, String... arg) {
        if (arg != null && arg.length > 0 && indice >= 0) {
            if (tabComplete.containsKey(indice)) {
                tabComplete.get(indice).addAll(Arrays.stream(arg).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCommand(indice, s, permission, beforeText)),
                        ArrayList::addAll));
            } else {
                tabComplete.put(indice, Arrays.stream(arg).collect(
                        ArrayList::new,
                        (tabCommands, s) -> tabCommands.add(new TabCommand(indice, s, permission, beforeText)),
                        ArrayList::addAll)
                );
            }
        }
    }

    /**
     * Add multiple arguments to an index
     *
     * @param indice index where the argument is in the command. /myCmd is at the index -1, so
     *               /myCmd index0 index1 ...
     * @param arg    word to add
     */
    protected void addTabbComplete(int indice, String... arg) {
        addTabbComplete(indice, null, null, arg);
    }
    //</editor-fold>
    //</editor-fold>

    /**
     * /!\ to do at the end /!\ to save the command.
     *
     * @return true if the command has been successfully registered
     */
    protected boolean registerCommand() {
        assert COMMAND_MAP != null;
        if (!register) {
            register = COMMAND_MAP.register(plugin.getName(), this);
        }
        return register;
    }


    //<editor-fold desc="get">

    /**
     * @return plugin responsible for the command
     */
    @Override
    public T getPlugin() {
        return this.plugin;
    }

    /**
     * @return tabComplete
     */
    public HashMap<Integer, ArrayList<TabCommand>> getTabComplete() {
        return tabComplete;
    }
    //</editor-fold>


    //<editor-fold desc="Override">

    /**
     * @param commandSender sender
     * @param command       command
     * @param arg           argument of the command
     * @return true if ok, false otherwise
     */
    @Override
    public boolean execute(CommandSender commandSender, String command, String[] arg) {
        if (getPermission() != null) {
            if (!commandSender.hasPermission(getPermission())) {
                if (getPermissionMessage() == null) {
                    commandSender.sendMessage(ChatColor.RED + "no permit!");
                } else {
                    commandSender.sendMessage(getPermissionMessage());
                }
                return false;
            }
        }
        if (onCommand(commandSender, this, command, arg))
            return true;
        commandSender.sendMessage(ChatColor.RED + getUsage());
        return false;

    }

    /**
     * @param sender sender
     * @param alias  alias used
     * @param args   argument of the command
     * @return a list of possible values
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {

        int indice = args.length - 1;

        if ((getPermission() != null && !sender.hasPermission(getPermission())) || tabComplete.size() == 0 || !tabComplete.containsKey(indice))
            return super.tabComplete(sender, alias, args);

        List<String> list = tabComplete.get(indice).stream()
                .filter(tabCommand -> tabCommand.getTextAvant() == null || tabCommand.getTextAvant().contains(args[indice - 1]))
                .filter(tabCommand -> tabCommand.getPermission() == null || sender.hasPermission(tabCommand.getPermission()))
                .map(TabCommand::getText)
                .filter(tabCommand -> tabCommand.startsWith(args[indice]))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());

        return list.size() < 1 ? super.tabComplete(sender, alias, args) : list;

    }
    //</editor-fold>

    //<editor-fold desc="class TabCommand">
    private static class TabCommand {

        private final int indice;
        private final String text;
        private final String permission;
        private final ArrayList<String> textAvant;

        private TabCommand(int indice, String text, String permission, String... textAvant) {
            this.indice = indice;
            this.text = text;
            this.permission = permission;
            if (textAvant == null || textAvant.length < 1) {
                this.textAvant = null;
            } else {
                this.textAvant = Arrays.stream(textAvant).collect(ArrayList::new,
                        ArrayList::add,
                        ArrayList::addAll);
            }
        }

        //<editor-fold desc="get&set">
        public String getText() {
            return text;
        }

        public int getIndice() {
            return indice;
        }

        public String getPermission() {
            return permission;
        }

        public ArrayList<String> getTextAvant() {
            return textAvant;
        }
        //</editor-fold>

    }
    //</editor-fold>
}
