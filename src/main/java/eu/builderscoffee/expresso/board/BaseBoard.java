package eu.builderscoffee.expresso.board;

import eu.builderscoffee.api.bukkit.board.FastBoard;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import eu.builderscoffee.expresso.utils.MessageUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Function;

public class BaseBoard {

    private static final Map<UUID, FastBoard> playerBoards = new HashMap<>();
    @Getter
    private final Map<Class<? extends BBPhase>, Function<Player, List<String>>> boards = new HashMap<>();
    private int ipCharIndex;
    private int cooldown;

    /***
     * Mettre à jour le scoreboard du joueur
     * @param player - Joueur
     */
    public void update(Player player) {
        FastBoard fb;
        if (!playerBoards.containsKey(player.getUniqueId())) {
            fb = new FastBoard(player);
            fb.updateTitle(MessageUtils.getMessageConfig(player).getBoard().getTitle());
            playerBoards.put(player.getUniqueId(), fb);
        }
        fb = playerBoards.get(player.getUniqueId());

        if (Objects.nonNull(ExpressoBukkit.getBuildBattle())
                && Objects.nonNull(ExpressoBukkit.getBuildBattle().getType())
                && Objects.nonNull(ExpressoBukkit.getBuildBattle().getType().getCurrentPhase())
                && getBoards().containsKey(ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getClass())) {
            fb.updateLines(getBoards().get(ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getClass()).apply(player));
        } else {
            fb.updateLines(addBlank());
            fb.updateLines("§cY'a un soucis chef");
            fb.updateLines(addBlank());
        }
    }

    /***
     * Retirer un joueur de la map
     * @param player - Le joueur
     */
    public void remove(Player player) {
        if (playerBoards.containsKey(player.getUniqueId())) {
            playerBoards.get(player.getUniqueId()).delete();
            playerBoards.remove(player.getUniqueId());
        }
    }

    /***
     * Retirer tout les scoreboards et nettoyer la map
     */
    public void removeAll() {
        playerBoards.values().forEach(FastBoard::delete);
        playerBoards.clear();
    }

    /***
     * Retourne un string vide
     * @return - Un string vide
     */
    public String addBlank() {
        return "";
    }

    /***
     * Retourne un string avec line ligne de séparation
     * et son code couleur
     * @return - Un string de séparation
     */
    public String addSeparator() {
        return "§0§8§m----------§8§m------";
    }

    /***
     * Retourne un string avec l'ip du serveur
     * et sont code couleur
     */
    public String addIp() {
        return MessageUtils.getDefaultMessageConfig().getBoard().getServerIp();
    }

    public String ipAnimate(String ipString) {
        if (cooldown > 0) {
            cooldown--;
            return ChatColor.GOLD + ipString;
        }

        StringBuilder formattedIp = new StringBuilder();

        if (ipCharIndex > 0) {
            formattedIp.append(ipString, 0, ipCharIndex - 1);
            formattedIp.append(ChatColor.YELLOW).append(ipString.charAt(ipCharIndex - 1));
        } else {
            formattedIp.append(ipString, 0, ipCharIndex);
        }

        formattedIp.append(ChatColor.RED).append(ipString.charAt(ipCharIndex));

        if (ipCharIndex + 1 < ipString.length()) {
            formattedIp.append(ChatColor.YELLOW).append(ipString.charAt(ipCharIndex + 1));

            if (ipCharIndex + 2 < ipString.length())
                formattedIp.append(ChatColor.GOLD).append(ipString.substring(ipCharIndex + 2));

            ipCharIndex++;
        } else {
            ipCharIndex = 0;
            cooldown = 50;
        }

        return ChatColor.GOLD + formattedIp.toString();
    }
}