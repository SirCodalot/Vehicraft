package Vehicraft;

import Vehicraft.Commands.cmdVehicraft;
import Vehicraft.Commands.cmdVehicraftVault;
import Vehicraft.Commands.tabVehicraft;
import Vehicraft.Events.InventoryEvents;
import Vehicraft.Events.RecipeEvents;
import Vehicraft.Events.SignEvents;
import Vehicraft.Objects.Recipe;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
    Created by SirCodalot
    Twitter: @Sir_Codalot

    Feel free to "steal" my code ;)
*/
public class Loader extends JavaPlugin {

    private static Plugin instance;
    public static Economy econ;

    @Override
    public void onEnable() {
        instance = this;

        // Check for Vehicraft
        if (!hasVehiclesEnabled()) {
            getLogger().info("Vehicles not found");
            getLogger().info("Disabling Vehicraft...");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }

        // Hook into Vault
        setupEconomy();

        // Loading config.yml
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        // Registering Events
        Bukkit.getPluginManager().registerEvents(new InventoryEvents(), this);
        Bukkit.getPluginManager().registerEvents(new RecipeEvents(), this);
        Bukkit.getPluginManager().registerEvents(new SignEvents(), this);

        // Registering Commands
        getCommand("vehicraft").setExecutor(new cmdVehicraft());
        getCommand("vehicraft").setTabCompleter(new tabVehicraft());
        getCommand("vehicraftvault").setExecutor(new cmdVehicraftVault());

        // Loading Recipes from config.yml
        Recipe.loadRecipes();

        // Registering custom shaped recipes
        registerRecipes();
    }

    /*
        Returns the main class.
    */
    public static Plugin getInstance() {
        return instance;
    }

    /*
        Checks if the server has Vehicles installed.
    */
    private boolean hasVehiclesEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled("Vehicles");
    }

    /*
        Registers the recipes.
    */
    private void registerRecipes() {
        for (Recipe recipe : Recipe.recipes) {
            Bukkit.addRecipe(recipe.getShapedRecipe());
            getLogger().info("Registered");
        }
    }

    /*
        Hooks into vault.
    */
    private void setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }
}
