package Vehicraft.Commands;

import Vehicraft.Loader;
import Vehicraft.Setup.Messages;
import Vehicraft.Setup.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdVehicraftVault implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        // Checking if the sender has permission to use this command.
        if (sender instanceof Player && !Permissions.COMMAND_VAULT.hasPermission((Player) sender)) {
            Messages.CMD_NO_PERMISSION.sendTo(sender);
            return true;
        }

        // Checking if the plugin is hooked into Vault.
        String hooked = (Loader.econ == null) ? ChatColor.RED + "Not Hooked" : ChatColor.GREEN + "Hooked";

        // Sending the message to the sender.
        sender.sendMessage(Messages.VAULT_HOOKED.getMessage() + hooked);
        if (Loader.econ == null) sender.sendMessage(ChatColor.RED + "If you believe that this is a problem please contact the developer, SirCodalot, on www.spigotmc.org");

        return true;
    }

}
