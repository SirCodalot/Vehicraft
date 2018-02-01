package Vehicraft.Setup;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Messages {

    PREFIX("&a[&7Vehicraft&a]&8 "),

    CMD_INVALID_EXECUTOR(PREFIX.getMessage() + "&cYou must be a player to execute this command."),
    CMD_NO_PERMISSION(PREFIX.getMessage() + "&cYou don't have permission to execute this command."),
    CMD_INVALID_ARGUMENTS(PREFIX.getMessage() + "&cInvalid arguments. Type &8/vehicraft ?&c for help."),
    CMD_ALREADY_EXISTS(PREFIX.getMessage() + "&cThis recipe already exists."),
    CMD_INVALID_TYPE(PREFIX.getMessage() + "&cInvalid vehicle type."),
    CMD_INVALID_RECIPE(PREFIX.getMessage() + "&cInvalid recipe."),
    CMD_USAGE(PREFIX.getMessage() + "Usages:"),
    CMD_INVALID_VEHICLE(PREFIX.getMessage() + "&cInvalid vehicle."),

    SIGN_CREATE_NO_PERMISSION(PREFIX.getMessage() + "&cYou don't have permission to create this sign."),
    SIGN_INTERACT_NO_PERMISSION(PREFIX.getMessage() + "&cYou don't have permission to interact with this sign."),
    SIGN_INVALID_SYNTAX(PREFIX.getMessage() + "&cInvalid sign syntax."),
    SIGN_MISSING_VAULT(PREFIX.getMessage() + "&cYou need to have vault installed to use this feature."),
    SIGN_INVALID_PRICE(PREFIX.getMessage() + "&cInvalid Price."),
    SIGN_CREATE(PREFIX.getMessage() + "Created sign successfully."),
    SIGN_ERROR(PREFIX.getMessage() + "&cSomething went wrong, please notify an administrator."),
    SIGN_PURCHASED(PREFIX.getMessage() + "Purchased vehicle successfully."),
    SIGN_NOT_ENOUGH(PREFIX.getMessage() + "&cYou don't have enough money to purchase this vehicle."),

    VAULT_HOOKED(PREFIX.getMessage() + "Vault: "),

    RECIPE_CREATE(PREFIX.getMessage() + "created &a[RECIPE]&8 successfully."),
    RECIPE_REMOVE(PREFIX.getMessage()+ "deleted &a[RECIPE]&8 successfully."),
    RECIPE_EDITED(PREFIX.getMessage() + "edited &a[RECIPE]&8 successfully."),
    RECIPE_LIST(PREFIX.getMessage() + "Recipes:");

    private String message;

    Messages(String message) {
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    /*
        Returns the message as a String
    */
    public String getMessage() {
        return message;
    }

    /*
        Sends the message to a player.
    */
    public void sendTo(Player player) {
        player.sendMessage(message);
    }

    /*
        Sends the message to a command sender.
    */
    public void sendTo(CommandSender sender) {
        sender.sendMessage(message);
    }
}
