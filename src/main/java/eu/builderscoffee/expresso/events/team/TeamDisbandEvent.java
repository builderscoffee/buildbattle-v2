package eu.builderscoffee.expresso.events.team;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public final class TeamDisbandEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final List<Player> memberList;

    public TeamDisbandEvent(Player player, List<Player> membersList) {
        this.player = player;
        this.memberList = membersList;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}