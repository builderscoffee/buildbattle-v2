package eu.builderscoffee.expresso.commands;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.player.PlotPlayer;
import com.plotsquared.core.plot.Plot;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.configuration.SettingsConfiguration;
import eu.builderscoffee.expresso.inventory.jury.JuryNotationInventory;
import eu.builderscoffee.expresso.inventory.jury.JuryTeleportation;
import eu.builderscoffee.expresso.utils.MessageUtils;
import eu.builderscoffee.expresso.utils.PlotUtils;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlotCommand implements CommandExecutor {

    static SettingsConfiguration settings = ExpressoBukkit.getSettings();

    public static boolean argLength0(Player player) {
        List<String> commandList = new ArrayList<>();
        val messages = MessageUtils.getMessageConfig(player);
        commandList.add(messages.getCommand().getPlotDefault());
        commandList.add(messages.getCommand().getPlotInfo());
        for (String s : commandList) {
            player.sendMessage(s);
        }
        return true;
    }

    public static boolean argLength1(Player player, String cmd) {
        cmd = cmd.toLowerCase();
        PlotPlayer<?> plotPlayer = new PlotAPI().wrapPlayer(player.getUniqueId());
        switch (cmd) {
            case "info":
                // Informations sur le plot
                if (plotPlayer.getLocation().isPlotArea()) {
                    val messages = MessageUtils.getMessageConfig(player);
                    if (!plotPlayer.getLocation().getPlot().canClaim(plotPlayer)) {
                        val plot = plotPlayer.getLocation().getPlot();
                        System.out.println("Plot owner " + plot.getOwner());
                        List<String> membersList = new ArrayList<>();
                        plot.getMembers().forEach(uuid -> membersList.add(new PlotAPI().wrapPlayer(uuid).getName()));
                        player.sendMessage(messages.getCommand().getPlotInfoHeader());
                        player.sendMessage(messages.getCommand().getPlotInfoId().replace("%id%", String.valueOf(PlotUtils.getPlotsPos(plot))));
                        player.sendMessage(messages.getCommand().getPlotInfoOwner().replace("%owner%", new PlotAPI().wrapPlayer(plot.getOwner()).getName()));
                        player.sendMessage(messages.getCommand().getPlotInfoMembers().replace("%members%", membersList.size() <= 1 ? membersList.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(" ,")) : "Aucun"));
                        player.sendMessage(messages.getCommand().getFooterText());
                    } else {
                        player.sendMessage(messages.getCommand().getPlotNotClaim());
                    }
                } else {
                    player.sendMessage(MessageUtils.getMessageConfig(player).getCommand().getPlotNotIn().replace("%prefix", MessageUtils.getDefaultMessageConfig().getPrefix()));
                }
                break;
            case "paste":
                /*
                Location loc = PlotUtils.convertBukkitLoc(player.getTargetBlock(null, 100).getLocation());
                final Plot plot = loc.getPlotAbs();
                PlotUtils.pasteSchematic(Main.getSettings().getSchematicToPaste(), plot);
                player.sendMessage("§a Paste Plot Test");
                */
                break;
            case "invleo":
                //checker
                if (new PlotAPI().wrapPlayer(player.getUniqueId()).getLocation().isPlotArea()) {
                    Plot plotinv = (PlotUtils.convertBukkitLoc(player.getLocation()).getPlotAbs());
                    if (!ExpressoBukkit.getBuildBattle().getNotationManager().playerHasNote(plotinv, player)) {
                        JuryNotationInventory.INVENTORY.open(player);
                    } else {
                        player.sendMessage(MessageUtils.getMessageConfig(player).getCommand().getPlotAlReadyNoted().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix()));
                    }
                } else {
                    player.sendMessage(MessageUtils.getMessageConfig(player).getCommand().getPlotNotIn().replace("%prefix", MessageUtils.getDefaultMessageConfig().getPrefix()));
                }
                break;

            case "seenote":
                val messages = MessageUtils.getMessageConfig(player);
                val plot = (PlotUtils.convertBukkitLoc(player.getLocation()).getPlotAbs());
                val notationsByPlot = ExpressoBukkit.getBuildBattle().getNotationManager().getNotationsByPlot(plot);
                if (notationsByPlot == null || notationsByPlot.isEmpty()) {
                    player.sendMessage(messages.getCommand().getPlotNotNoted().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix()));
                    break;
                }
                player.sendMessage(messages.getCommand().getPlotNoteSize().replace("%prefix%", MessageUtils.getDefaultMessageConfig().getPrefix()));

                /*for (Object note : notationsByPlot) {
                    player.sendMessage("Juge: " + Bukkit.getOfflinePlayer(note.getUUID()).getName() + " Fun: " + note.getFun());
                }
                */
                break;

            case "invlist":
                JuryTeleportation.INVENTORY.open(player);
                break;

            case "tpnext":
                // Get plot
                //TODO Revoir la méthode de tp
                Plot plotinvtp = (PlotUtils.convertBukkitLoc(player.getLocation()).getPlotAbs());
                int b = PlotUtils.getPlotsPos(plotinvtp) + 1;
                PlotUtils.getPlotsByPos(b).getCenter(location ->
                        // tp player
                        player.teleport(PlotUtils.convertPlotCenterLoc(location)));
                break;
                /*
            case "schem":
                new PlotUtils().exportAllSchematics(settings.getPath_for_backup(), () -> {
                    System.out.println("Tout les plots on été schématisés");
                });
                break;
                */

            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean ret = false;
            if (player.hasPermission(settings.getExpresso_eplot_permission()) || player.hasPermission(settings.getExpresso_all_permission())) {
                switch (args.length) {
                    case 0:
                        ret = argLength0(player);
                        break;
                    case 1:
                        ret = argLength1(player, args[0]);
                        break;
                    case 2:
                        //ret = argLength2(player, args[0], args[1]);
                        break;
                    case 3:
                        //ret = argLength3(player, args[0], args[1], args[2]);
                        break;
                    default:
                        //ret = argLength4(player, args);
                        break;
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
