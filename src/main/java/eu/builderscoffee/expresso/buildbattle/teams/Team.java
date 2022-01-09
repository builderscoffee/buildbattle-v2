package eu.builderscoffee.expresso.buildbattle.teams;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.List;

@Data
@AllArgsConstructor
public class Team {

    public String name, displayName;
    public int maxPlayers;
    public Player leader;
    public List<Player> members;

}
