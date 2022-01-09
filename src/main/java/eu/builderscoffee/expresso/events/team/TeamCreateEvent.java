package eu.builderscoffee.expresso.events.team;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class TeamCreateEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final Player player;

    public TeamCreateEvent(Player player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}