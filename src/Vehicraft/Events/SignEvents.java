package Vehicraft.Events;

import Vehicraft.Loader;
import Vehicraft.Objects.Recipe;
import Vehicraft.Setup.Messages;
import Vehicraft.Setup.Permissions;
import es.pollitoyeye.vehicles.enums.VehicleType;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignEvents implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {

        Player player = event.getPlayer();

        if (!event.getLine(0).equalsIgnoreCase("[vehicraft]")) return;

        if (!Permissions.SIGN_CREATE.hasPermission(player)) {
            Messages.SIGN_CREATE_NO_PERMISSION.sendTo(player);
            event.getBlock().breakNaturally();
            return;
        }

        if (event.getLine(1) == null || event.getLine(2) == null ||
                event.getLine(1).equalsIgnoreCase("" )|| event.getLine(2).equalsIgnoreCase("")) {
            Messages.SIGN_INVALID_SYNTAX.sendTo(player);
            event.getBlock().breakNaturally();
            return;
        }

        VehicleType type;

        // Checking if the type is invalid.
        try {
            type = VehicleType.valueOf(event.getLine(2));
        } catch(IllegalArgumentException e) {
            Messages.CMD_INVALID_TYPE.sendTo(player);
            event.getBlock().breakNaturally();
            return;
        }


        Recipe recipe = Recipe.getRecipe(event.getLine(1), type);

        if (recipe == null) {
            Messages.CMD_INVALID_RECIPE.sendTo(player);
            event.getBlock().breakNaturally();
            return;
        }

        event.setLine(0, Messages.PREFIX.getMessage().replace(" ", ""));
        event.setLine(1, ChatColor.BLUE + recipe.name);
        event.setLine(2, ChatColor.DARK_GRAY + recipe.type.toString());

        if (!event.getLine(3).equalsIgnoreCase("")) {

            if (Loader.econ == null) {
                Messages.SIGN_MISSING_VAULT.sendTo(player);
                event.getBlock().breakNaturally();
                return;
            }

            if (!isNumeric(event.getLine(3))) {
                Messages.SIGN_INVALID_PRICE.sendTo(player);
                event.getBlock().breakNaturally();
                return;
            }

            event.setLine(3, ChatColor.RED + "$" + Float.valueOf(event.getLine(3)));

        } else event.setLine(3, ChatColor.RED + "Click to Preview");


        Messages.SIGN_CREATE.sendTo(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (!event.hasBlock()) return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (!(event.getClickedBlock().getState() instanceof Sign)) return;

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[Vehicraft]")) return;

        if (!Permissions.SIGN_INTERACT.hasPermission(player)) {
            Messages.SIGN_INTERACT_NO_PERMISSION.sendTo(player);
            return;
        }

        Recipe recipe = Recipe.getRecipe(ChatColor.stripColor(sign.getLine(1)), VehicleType.valueOf(ChatColor.stripColor(sign.getLine(2))));

        if (recipe == null) {
            Messages.SIGN_ERROR.sendTo(player);
            return;
        }

        if (sign.getLine(3).contains("Click to Preview")) {
            player.openInventory(recipe.preview);
            return;
        }

        if (Loader.econ == null) {
            Messages.SIGN_MISSING_VAULT.sendTo(player);
            return;
        }

        double price = Double.valueOf(ChatColor.stripColor(sign.getLine(3).replace("$", "")));

        if (Loader.econ.getBalance(player) < price) {
            Messages.SIGN_NOT_ENOUGH.sendTo(player);
            return;
        }

        Loader.econ.withdrawPlayer(player, price);
        player.getInventory().addItem(recipe.result);
        Messages.SIGN_PURCHASED.sendTo(player);

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        if (!(event.getBlock().getState() instanceof Sign)) return;

        Sign sign = (Sign) event.getBlock().getState();

        if (!ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[Vehicraft]")) return;

        if (!Permissions.SIGN_BREAK.hasPermission(player) || !player.isSneaking()) event.setCancelled(true);
    }

    private boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }
}
