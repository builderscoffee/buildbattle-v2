package eu.builderscoffee.expresso.listeners.redisson;

import eu.builderscoffee.api.common.events.EventListener;
import eu.builderscoffee.api.common.events.ProcessEvent;
import eu.builderscoffee.api.common.events.events.HeartBeatEvent;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.BuildBattleCategory;
import eu.builderscoffee.expresso.utils.TimeUtils;

import java.util.Objects;

public class HeartBeatListener implements EventListener {


    /***
     * Envois une mise à jours des informations du serveur
     * - GameType & SubType
     * - State & Timer
     * @param event
     */
    @ProcessEvent
    public void onHeartBeat(HeartBeatEvent event) {
        if (Objects.nonNull(ExpressoBukkit.getBuildBattle())) {
            // Type de la partie ( Expresso, Buildbattle etc... )
            // Type de sous partie ( Allongé, IlClassico etc... )
            if (Objects.nonNull(ExpressoBukkit.getBuildBattle().getType())) {
                event.getServer().getProperties().put("GameCategory", ExpressoBukkit.getBuildBattle().getType().getCategory().getBuildBattleGameTypeName());
                event.getServer().getProperties().put("GameType", ExpressoBukkit.getBuildBattle().getType().getName());
            }
            // Etat de la partie ( In_Game , End etc... )
            if (ExpressoBukkit.getBuildBattle().isReady()) {
                event.getServer().getProperties().put("State", ExpressoBukkit.getBuildBattle().getState().toString());
            }
            // Timer de la partie
            if (Objects.nonNull(ExpressoBukkit.getBuildBattle().getType()) && Objects.nonNull(ExpressoBukkit.getBuildBattle().getType().getCurrentPhase())) {
                event.getServer().getProperties().put("Timer", TimeUtils.getDurationString(ExpressoBukkit.getBuildBattle().getType().getCurrentPhase().getTime()));
            }
        }
    }
}
