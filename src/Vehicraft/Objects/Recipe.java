package Vehicraft.Objects;

import Vehicraft.Loader;
import Vehicraft.Setup.Messages;
import Vehicraft.Utils.ItemStackSerializer;
import es.pollitoyeye.vehicles.enums.VehicleType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recipe {

    // Static lists
    public static ArrayList<Recipe> recipes = new ArrayList<>();
    public static ArrayList<Inventory> editors = new ArrayList<>(), previews = new ArrayList<>();

    private static FileConfiguration cf = Loader.getInstance().getConfig();

    // Recipe variables
    public String name;
    public VehicleType type;
    public HashMap<Integer, ItemStack> ingredients;
    public Inventory editor, preview;
    public ItemStack result;

    public Recipe(String name, VehicleType type) {
        this.name = name;
        this.type = type;

        // Setting every item to AIR (Material ID = 0).
        ingredients = new HashMap<Integer, ItemStack>(){{
            for (int i = 0; i < 9; i++) put(i, new ItemStack(Material.AIR));
        }};
        result = type.getVehicleManager().getItem(name);

        // Creating the menus (inventories).
        updateEditor();
        updatePreview();

        // Adding the objects to the static lists.
        editors.add(editor);
        previews.add(preview);
        recipes.add(this);

        // Saving the recipe in config.yml
        save();
    }

    private Recipe(String path) {
        String[] split = path.split("%");
        this.name = split[0];
        this.type = VehicleType.valueOf(split[1]);

        // Setting every item to AIR (Material ID = 0).
        ingredients = new HashMap<Integer, ItemStack>(){{
            List<String> list = cf.getStringList("Recipes." + path);

            for (int i = 0; i < 9; i++) {
                put(i, (list.get(i) != null && !list.get(i).contains("AIR")) ? ItemStackSerializer.deserialize(list.get(i)) : new ItemStack(Material.AIR));
            }
        }};
        result = type.getVehicleManager().getItem(name);

        // Creating the menus (inventories).
        updateEditor();
        updatePreview();

        // Adding the objects to the static lists.
        recipes.add(this);

    }

    /*
        Creates and returns a shaped recipe using the result and ingredients.
    */
    public ShapedRecipe getShapedRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(result);

        recipe.shape("abc", "def", "ghi");

        for (int i = 0; i < 9; i++) {
            MaterialData materialData = new MaterialData(ingredients.get(i).getType());
            materialData.setData((byte) ingredients.get(i).getDurability());
            recipe.setIngredient((char)(i+'a'), materialData);
        }

        return recipe;
    }

    /*
        Updates the Editor menu.
    */
    public void updateEditor() {
        editors.remove(this.editor);
        this.editor = Bukkit.createInventory(null, InventoryType.DISPENSER, Messages.PREFIX.getMessage() + this.name);
        for (int i = 0; i < 9; i++) {
            this.editor.setItem(i, ingredients.get(i));
        }
        editors.add(this.editor);
    }

    /*
        Updates the Preview menu.
    */
    public void updatePreview() {
        previews.remove(preview);
        this.preview = Bukkit.createInventory(null, InventoryType.DISPENSER, Messages.PREFIX.getMessage() + this.name + "'s Recipe");
        for (int i = 0; i < 9; i++) {
            this.preview.setItem(i, ingredients.get(i));
        }
        previews.add(preview);
    }

    /*
        Saves the recipe in config.yml
    */
    public void save() {

        String path = "Recipes." + name + "%" + type.toString();
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            data.add(ItemStackSerializer.serialize(ingredients.get(i)));
        }

        cf.set(path, data);

        Loader.getInstance().saveConfig();
    }

    /*
        Deletes the recipe from the config file and removes it from the static lists.
    */
    public void remove() {
        cf.set("Recipes." + name + "%" + type.toString(), null);

        Loader.getInstance().saveConfig();

        editors.remove(editor);
        previews.remove(preview);
        recipes.remove(this);
    }
    /*
        Loads every recipe from the config file.
    */
    public static void loadRecipes() {

        if (cf.getConfigurationSection("Recipes") == null) return;

        cf.getConfigurationSection("Recipes").getKeys(false).forEach(Recipe::new);
    }

    /*
        Finds and returns a certain recipe using it's name and it's type.
    */
    public static Recipe getRecipe(String name, VehicleType type) {
        for (Recipe recipe : recipes)
            if (recipe.name.equalsIgnoreCase(name) && recipe.type == type) {
                return recipe;
            }

        return null;
    }

    /*
        Finds and returns a certain recipe using it's editor menu.
    */
    public static Recipe getRecipeByEditor(Inventory inventory) {
        for (Recipe recipe : recipes) if (recipe.editor == inventory) {
            return recipe;
        }

        return null;
    }

    /*
        Finds and returns a certain recipe using it's result.
    */
    public static Recipe getRecipeByResult(ItemStack itemStack) {
        for (Recipe recipe : recipes) if (recipe.result.isSimilar(itemStack)) {
            return recipe;
        }

        return null;
    }
}
