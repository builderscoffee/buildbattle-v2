package eu.builderscoffee.expresso.buildbattle.teams;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class Invitation {

    Player sender;
    Player target;
}
