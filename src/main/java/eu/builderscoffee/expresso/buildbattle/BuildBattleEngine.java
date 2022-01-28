package eu.builderscoffee.expresso.buildbattle;

import org.bukkit.event.Listener;

import java.util.List;

public interface BuildBattleEngine {

    /***
     * Démarrer le moteur de la partie
     */
    void load();

    /***
     * Enregistrer les événements custom du moteur de la partie
     */
    List<Listener> registerListener();

}
