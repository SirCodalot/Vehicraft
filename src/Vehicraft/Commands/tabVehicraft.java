package Vehicraft.Commands;

import Vehicraft.Setup.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class tabVehicraft implements org.bukkit.command.TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        return new ArrayList<String>(){{
            if (sender instanceof Player) {
                // Player usages with permissions
                Player player = (Player) sender;
                if (Permissions.COMMAND_VR_HELP.hasPermission(player)) {
                    add("?");
                    add("help");
                }
                if (Permissions.COMMAND_VR_LIST.hasPermission(player)) add("list");
                if (Permissions.COMMAND_VR_CREATE.hasPermission(player)) add("create");
                if (Permissions.COMMAND_VR_DELETE.hasPermission(player)) {
                    add("delete");
                    add("remove");
                }
                if (Permissions.COMMAND_VR_EDIT.hasPermission(player)) add("edit");
                if (Permissions.COMMAND_VR_PREVIEW.hasPermission(player)) {
                    add("preview");
                    add("recipe");
                }
            } else {
                // Console usages
                add("?");
                add("help");
                add("list");
                add("create");
                add("delete");
                add("remove");
                add("edit");
                add("preview");
                add("recipe");
            }
        }};

    }
}
