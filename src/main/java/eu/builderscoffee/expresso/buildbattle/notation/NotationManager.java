package eu.builderscoffee.expresso.buildbattle.notation;

import com.plotsquared.core.plot.Plot;
import lombok.val;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class NotationManager {
    private final HashMap<Plot, Set<Notation>> allNotation;

    public NotationManager() {
        this.allNotation = new HashMap<>();
    }

    /***
     * Ajouter une valeur à une notation en cache
     */
    public void AddValueToNotation(Notation notation, Notation.NotationType notationType, int value) {
        val cachedValue = notation.getNotes().get(notationType);
        int finalValue = cachedValue + value;
        if (finalValue > notationType.getMaxValue())
            finalValue = notationType.getMaxValue();
        else if (finalValue < 0)
            finalValue = 0;
        notation.getNotes().put(notationType, finalValue);
    }

    /***
     * Ajouter une notation à un plot
     * @param plot - Le plot
     * @param note - La note
     */
    public void addNotationInPlot(Plot plot, Notation note) {
        if (getNotationsByPlot(plot) != null) {
            allNotation.get(plot).add(note);
        } else {
            Set tem = new HashSet();
            tem.add(note);
            allNotation.put(plot, tem);
        }
    }

    /***
     * Retourne une notation sur un plot
     * @param plot - Le plot
     * @return
     */
    public Set getNotationsByPlot(Plot plot) {
        return allNotation.get(plot);
    }

    /***
     * Retourne une notation d'un joueur
     * @param plot - Le plot
     * @param pl - Le joueur
     * @return
     */
    public boolean playerHasNote(Plot plot, Player pl) {
        Set<Notation> a = getNotationsByPlot(plot);
        if (a != null && !a.isEmpty()) {
            return a.stream().anyMatch(note -> note.getUUID() == pl.getUniqueId());
        }
        return false;
    }

    //public int getNotationDone()
}
