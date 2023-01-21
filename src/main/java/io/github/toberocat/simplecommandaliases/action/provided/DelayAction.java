package io.github.toberocat.simplecommandaliases.action.provided;

import io.github.toberocat.simplecommandaliases.action.Action;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Created: 04/12/2022
 *
 * @author Tobias Madlberger (Tobias)
 */
public class DelayAction extends Action {
    /**
     * Gets the text in the brackets at the beginning of the action.
     * Should not have uppercase letters or spaces and excludes the brackets.
     *
     * @return the label for this action.
     */
    @Override
    public @NotNull String label() {
        return "delay";
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String provided) {
        double delayInSeconds;
        try {
            delayInSeconds = Double.parseDouble(provided);
        } catch (NumberFormatException e) {
            return;
        }

        try {
            Thread.sleep(Math.round(delayInSeconds * 1000));
        } catch (InterruptedException ignored) {
        }
    }
}
