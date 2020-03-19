package Vehicraft.Commands;

import Vehicraft.Loader;
import Vehicraft.Objects.Recipe;
import Vehicraft.Setup.Messages;
import Vehicraft.Setup.Permissions;
import es.pollitoyeye.vehicles.enums.VehicleType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
//import sun.plugin2.message.Message;

public class CmdVehicraft implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // Help command
        if (args.length == 0 || (args.length == 1 && (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")))) {
            // Checking if the sender has permission to use the help command.
            if (sender instanceof Player && !Permissions.COMMAND_VR_HELP.hasPermission((Player) sender)) {
                Messages.CMD_NO_PERMISSION.sendTo(sender);
                return true;
            }

            // Checking if the sender has permission to see the commands' permissions.
            if (sender instanceof Player && !Permissions.COMMAND_VR_HELP_PERMISSIONS.hasPermission((Player) sender)) {
                // Sending the player the list of commands without the permissions.
                sender.sendMessage(
                        new String[]{Messages.CMD_USAGE.getMessage(),
                                ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft ?/help ",
                                ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft list ",
                                ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft create <name> <type> ",
                                ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft delete/remove <name> <type> ",
                                ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft edit <name> <type> ",
                                ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft preview/recipe <name> <type> ",
                                ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft updates "
                        });
                return true;
            }
            // Sending the player the list of commands with the permissions.
            sender.sendMessage(
                    new String[]{Messages.CMD_USAGE.getMessage(),
                            ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft ?/help "
                                    + ChatColor.DARK_GRAY + Permissions.COMMAND_VR_HELP.getPermission() + " & "
                                    + Permissions.COMMAND_VR_HELP_PERMISSIONS.getPermission(),
                            ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft list "
                                + ChatColor.DARK_GRAY + Permissions.COMMAND_VR_LIST.getPermission(),
                            ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft create <name> <type> "
                                    + ChatColor.DARK_GRAY + Permissions.COMMAND_VR_CREATE.getPermission(),
                            ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft delete/remove <name> <type> "
                                    + ChatColor.DARK_GRAY + Permissions.COMMAND_VR_DELETE.getPermission(),
                            ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft edit <name> <type> "
                                    + ChatColor.DARK_GRAY + Permissions.COMMAND_VR_EDIT.getPermission(),
                            ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft preview/recipe <name> <type> "
                                    + ChatColor.DARK_GRAY + Permissions.COMMAND_VR_PREVIEW.getPermission(),
                            ChatColor.GREEN + " +" + ChatColor.GRAY + " /vehicraft updates "
                                    + ChatColor.DARK_GRAY + Permissions.COMMAND_VR_UPDATES.getPermission()
                    });
            return true;
        }

        // Recipe list command
        if (args[0].equalsIgnoreCase("list")) {

            // Checking if the sender has permission to use this command.
            if (sender instanceof Player &&!Permissions.COMMAND_VR_LIST.hasPermission((Player) sender)) {
                Messages.CMD_NO_PERMISSION.sendTo(sender);
                return true;
            }

            // Sending the player the recipe list.
            if (Recipe.recipes.isEmpty()) {
                sender.sendMessage(Messages.RECIPE_LIST.getMessage() + ChatColor.RED + " Empty");
                return true;
            }
            Messages.RECIPE_LIST.sendTo(sender);

            Recipe.recipes.forEach(
                    recipe -> sender.sendMessage(ChatColor.YELLOW + " - " + ChatColor.BLUE + recipe.name + ChatColor.GREEN + " " + recipe.type.toString())
            );

            return true;
        }

        // Recipe create command
        if (args[0].equalsIgnoreCase("create")) {

            // Checking if the sender has permission to use the create command.
            if (sender instanceof Player && !Permissions.COMMAND_VR_CREATE.hasPermission((Player) sender)) {
                Messages.CMD_NO_PERMISSION.sendTo(sender);
                return true;
            }

            // Checking the arguments.
            if (args.length != 3) {
                Messages.CMD_INVALID_ARGUMENTS.sendTo(sender);
                return true;
            }

            VehicleType type;

            // Checking if the type is invalid.
            try {
                type = VehicleType.valueOf(args[2]);
            } catch(IllegalArgumentException e) {
                Messages.CMD_INVALID_TYPE.sendTo(sender);
                return true;
            }

            // Checking if the vehicle is invalid.
            if (!vehicleExits(args[1], type)) {
                Messages.CMD_INVALID_VEHICLE.sendTo(sender);
                return true;
            }

            // Checking if the recipe already exists.
            if (Recipe.getRecipe(args[1], type) != null) {
                Messages.CMD_ALREADY_EXISTS.sendTo(sender);
                return true;
            }

            // Creating a new recipe.
            new Recipe(args[1], type);
            sender.sendMessage(Messages.RECIPE_CREATE.getMessage().replace("[RECIPE]", args[1] + " " + type.toString()));

            return true;
        }

        // Delete recipe command
        if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {

            // Checking if the sender has permission to use this command.
            if (sender instanceof Player && !Permissions.COMMAND_VR_DELETE.hasPermission((Player) sender)) {
                Messages.CMD_NO_PERMISSION.sendTo(sender);
                return true;
            }

            // Checking the arguments.
            if (args.length != 3) {
                Messages.CMD_INVALID_ARGUMENTS.sendTo(sender);
                return true;
            }

            VehicleType type;

            // Checking if the type is invalid.
            try {
                type = VehicleType.valueOf(args[2]);
            } catch(IllegalArgumentException e) {
                Messages.CMD_INVALID_TYPE.sendTo(sender);
                return true;
            }

            Recipe recipe = Recipe.getRecipe(args[1], type);

            // Checking if the recipe exists.
            if (recipe == null) {
                Messages.CMD_INVALID_RECIPE.sendTo(sender);
                return true;
            }

            // Deleting the recipe.
            recipe.remove();
            sender.sendMessage(Messages.RECIPE_REMOVE.getMessage().replace("[RECIPE]", args[1] + " " + type.toString()));

            return true;
        }

        // Edit recipe command
        if (args[0].equalsIgnoreCase("edit")) {

            // Checking if the sender is a player.
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.CMD_INVALID_EXECUTOR.getMessage());
                return true;
            }

            Player player = (Player) sender;

            // Checking if the player has permission to use the edit command.
            if (!Permissions.COMMAND_VR_EDIT.hasPermission(player)) {
                Messages.CMD_NO_PERMISSION.sendTo(player);
                return true;
            }

            // Checking the arguments
            if (args.length != 3) {
                Messages.CMD_INVALID_ARGUMENTS.sendTo(player);
                return true;
            }

            VehicleType type;

            // Checking if the type is invalid.
            try {
                type = VehicleType.valueOf(args[2]);
            } catch(IllegalArgumentException e) {
                Messages.CMD_INVALID_TYPE.sendTo(player);
                return true;
            }

            Recipe recipe = Recipe.getRecipe(args[1], type);

            // Checking if the recipe exists.
            if (recipe == null) {
                Messages.CMD_INVALID_RECIPE.sendTo(player);
                return true;
            }

            // Opening the editing menu for the player.
            player.openInventory(recipe.editor);

            return true;
        }

        // Preview recipe command
        if (args[0].equalsIgnoreCase("preview") || args[0].equalsIgnoreCase("recipe")) {

            // Checking if the sender is a player.
            if (!(sender instanceof Player)) {
                sender.sendMessage(Messages.CMD_INVALID_EXECUTOR.getMessage());
                return true;
            }

            Player player = (Player) sender;

            // Checking if the player has permission to use the preview recipe command.
            if (!Permissions.COMMAND_VR_PREVIEW.hasPermission(player)) {
                Messages.CMD_NO_PERMISSION.sendTo(player);
                return true;
            }

            // Checking the arguments
            if (args.length != 3) {
                Messages.CMD_INVALID_ARGUMENTS.sendTo(player);
                return true;
            }

            VehicleType type;

            // Checking if the type is invalid.
            try {
                type = VehicleType.valueOf(args[2]);
            } catch(IllegalArgumentException e) {
                Messages.CMD_INVALID_TYPE.sendTo(player);
                return true;
            }

            Recipe recipe = Recipe.getRecipe(args[1], type);

            // Checking if the recipe exists.
            if (recipe == null) {
                Messages.CMD_INVALID_RECIPE.sendTo(player);
                return true;
            }

            // Opening the preview inventory for the player.
            player.openInventory(recipe.preview);

            return true;
        }

        // Updates command
        if (args[0].equalsIgnoreCase("updates")) {

            // Checking if the sender has permission to use the updates command.
            if ((sender instanceof Player) && !Permissions.COMMAND_VR_PREVIEW.hasPermission((Player) sender)) {
                Messages.CMD_NO_PERMISSION.sendTo(sender);
                return true;
            }

            try {
                if (Loader.updater.checkForUpdates()) Messages.UPDATE_FOUND.sendTo(sender);
                else Messages.UPDATE_LATEST.sendTo(sender);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        // Sending the invalid arguments message
        Messages.CMD_INVALID_ARGUMENTS.sendTo(sender);

        return true;
    }

    /*
        Checks if a vehicle exists.
    */
    private boolean vehicleExits(String name, VehicleType type) {
        return type.getVehicleManager().getItem(name).hasItemMeta() && type.getVehicleManager().getItem(name).getItemMeta().hasLore();
    }

}
