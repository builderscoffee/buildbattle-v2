package eu.builderscoffee.expresso.commands;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.toolbars.ToolbarManager;
import eu.builderscoffee.expresso.configuration.SettingsConfiguration;
import eu.builderscoffee.expresso.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JuryCommand implements CommandExecutor {

    SettingsConfiguration settings = ExpressoBukkit.getSettings();

    public static boolean argLength0(Player player) {
        ExpressoBukkit.getBbGame().getToolbarManager().addToolBar(player, ToolbarManager.Toolbars.JURORS);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean ret = false;
            if (player.hasPermission(settings.getExpresso_jury_permission()) || player.hasPermission(settings.getExpresso_all_permission())) {
                if (args.length == 0) {
                    ret = argLength0(player);
                }
            }

            if (!ret) {
                player.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getBadSyntaxe().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix()));
            }
            return ret;
        }

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getCommand().getMustBePlayer().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix()));
        return true;
    }
}
