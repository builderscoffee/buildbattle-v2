package eu.builderscoffee.expresso.commands;

import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattle;
import eu.builderscoffee.expresso.utils.MessageUtils;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamCommand implements CommandExecutor {

    // Instances of
    @Getter
    private final BuildBattle bbGame = ExpressoBukkit.getBuildBattle();

    public static boolean argLength0(Player player) {
        List<String> commandList = new ArrayList<>(MessageUtils.getMessageConfig(player).getCommand().getGroupInfoList());
        for (String s : commandList) {
            player.sendMessage(s);
        }
        return true;
    }

    public boolean argLength1(Player player, String args1) {
        args1 = args1.toLowerCase();
        switch (args1) {
            case "help":
                // Afficher l'aides aux commandes
                argLength0(player);
                break;
            case "leave":
                // Quitter le groupe d'un joueur
                ExpressoBukkit.getBuildBattle().getTeamManager().removePlayerFromTeam(player);
                break;
            case "disband":
                // Supprimer votre groupe si vous êtes leader
                ExpressoBukkit.getBuildBattle().getTeamManager().unregisterTeam(player);
                break;
            case "info":
                ExpressoBukkit.getBuildBattle().getTeamManager().viewTeam(player);
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean argLength2(Player player, String args1, String args2) {
        args1 = args1.toLowerCase();
        Player targetLenght2 = Bukkit.getPlayer(args2);
        switch (args1) {
            case "add":
                // Ajouter un joueur au groupe
                ExpressoBukkit.getBuildBattle().getTeamManager().sendInvitation(player, targetLenght2.getPlayer());
                break;
            case "remove":
                // Retirer un joueur au groupe
                ExpressoBukkit.getBuildBattle().getTeamManager().removePlayerFromTeam(targetLenght2.getPlayer());
                break;
            case "info":
                ExpressoBukkit.getBuildBattle().getTeamManager().viewTargetTeam(player, Bukkit.getPlayer(args1));
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean argLength3(Player player, String args1, String args2, String args3) {
        args1 = args1.toLowerCase();
        if ("invite".equals(args1)) {// Gérer les invitations
            switch (args3) {
                case "accept":
                    // Accepter l'invite du joueur
                    ExpressoBukkit.getBuildBattle().getTeamManager().acceptInvitation(player, Bukkit.getPlayerExact(args2));
                    break;
                case "deny":
                    // Refuser l'invite du joueur
                    ExpressoBukkit.getBuildBattle().getTeamManager().DenyInvitation(player, Bukkit.getPlayerExact(args2));
                    break;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean ret = false;
            //if (player.hasPermission(settings.getExpresso_all_permission())) {
            switch (args.length) {
                case 0:
                    ret = argLength0(player);
                    break;
                case 1:
                    ret = argLength1(player, args[0]);
                    break;
                case 2:
                    ret = argLength2(player, args[0], args[1]);
                    break;
                case 3:
                    ret = argLength3(player, args[0], args[1], args[2]);
                    break;
                default:
                    //ret = argLength4(player, args);
                    break;
                //}
            }

            if (!ret) {
                player.sendMessage(MessageUtils.getMessageConfig(player).getPrefix() + MessageUtils.getMessageConfig(player).getCommand().getBadSyntaxe());
            }

            return ret;
        }

        sender.sendMessage(MessageUtils.getMessageConfig(sender).getPrefix() + MessageUtils.getMessageConfig(sender).getCommand().getMustBePlayer());
        return true;
    }
}
