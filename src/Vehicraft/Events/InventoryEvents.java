package Vehicraft.Events;

import Vehicraft.Objects.Recipe;
import Vehicraft.Setup.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        // Preventing the players from "stealing" the ingredients from the preview inventory.
        if (inventory.getTitle().contains("'s Recipe")) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        // Checking if the inventory is an editor.
        if (!Recipe.editors.contains(inventory)) return;

        Recipe recipe = Recipe.getRecipeByEditor(inventory);

        // Registering the new ingredients.
        for (int i = 0; i < 9; i++) {
            assert recipe != null;
            recipe.ingredients.put(i, (inventory.getItem(i) != null) ? inventory.getItem(i) : new ItemStack(Material.AIR));
        }

        recipe.updateEditor();
        recipe.updatePreview();
        recipe.save();

        player.sendMessage(Messages.RECIPE_EDITED.getMessage().replace("[RECIPE]", recipe.name + " " + recipe.type.toString()));

    }

}
