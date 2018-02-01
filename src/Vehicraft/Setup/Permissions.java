package Vehicraft.Setup;

import org.bukkit.entity.Player;

public enum Permissions {

    COMMAND_VR_HELP("vehicraft.command.help.commands"),
    COMMAND_VR_HELP_PERMISSIONS("vehicraft.command.help.permissions"),
    COMMAND_VR_LIST("vehicraft.command.list.commands"),
    COMMAND_VR_EDIT("vehicraft.command.edit"),
    COMMAND_VR_CREATE("vehicraft.command.create"),
    COMMAND_VR_DELETE("vehicraft.command.delete"),
    COMMAND_VR_PREVIEW("vehicraft.command.preview"),

    COMMAND_VAULT("vehicraft.command.vault"),

    SIGN_CREATE("vehicraft.sign.create"),
    SIGN_BREAK("vehicraft.sign.break"),
    SIGN_INTERACT("vehicraft.sign.interact");

    private String permission;

    Permissions(String permission)
    {
        this.permission = permission;
    }

    /*
        Returns the permission as a string.
    */
    public String getPermission()
    {
        return this.permission;
    }

    /*
        Checks if a player has the permission.
    */
    public boolean hasPermission(Player player) {
        return player.hasPermission(permission);
    }
}
