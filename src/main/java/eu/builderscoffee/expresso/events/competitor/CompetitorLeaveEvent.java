package eu.builderscoffee.expresso.events.competitor;

import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
public class CompetitorLeaveEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    @Getter
    private Player competitor;

    public CompetitorLeaveEvent(Player competitor) {
        super();
        this.competitor = competitor;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public String getEventName() {
        return "CompetitorLeaveEvent";
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
