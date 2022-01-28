package eu.builderscoffee.expresso.buildbattle.teams;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class Invitation {

    @NonNull
    Player sender;
    @NonNull
    Player target;
}
