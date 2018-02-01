package Vehicraft.Events;

import Vehicraft.Objects.Recipe;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class RecipeEvents implements Listener {

    @EventHandler
    public void onItemCraftEvent(PrepareItemCraftEvent event) {

        // Checking if the recipe is valid.
        if (event.getInventory().getResult() == null || event.getInventory().getResult().getType() == Material.AIR) return;

        Recipe recipe = Recipe.getRecipeByResult(event.getInventory().getResult());

        // Checking if the recipe is a vehicle recipe.
        if (recipe == null) return;

        boolean flag = true;
        // Checking if the ItemMetas from the inventory and from the recipe are similar.
        for (int i = 0; i < 9 && flag; i++) {
            if (event.getInventory().getItem(i+1) == null || event.getInventory().getItem(i+1).getType() == Material.AIR)
                flag = recipe.ingredients.get(i).getType() == Material.AIR;
            else flag = recipe.ingredients.get(i).isSimilar(event.getInventory().getItem(i+1));
        }

        // Cancelling the recipe if the ItemMetas do not match.
        if (!flag) event.getInventory().setResult(new ItemStack(Material.AIR));
    }

}
