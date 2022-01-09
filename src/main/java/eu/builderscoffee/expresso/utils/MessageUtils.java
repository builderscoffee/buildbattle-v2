package eu.builderscoffee.expresso.utils;

import eu.builderscoffee.api.common.data.tables.Profil;
import eu.builderscoffee.commons.bukkit.CommonsBukkit;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.configuration.MessageConfiguration;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@UtilityClass
public class MessageUtils {

    public static Profil.Languages getLang(Player player) {
        val profil = CommonsBukkit.getInstance().getProfilCache().get(player.getUniqueId().toString());
        return profil.getLang();
    }

    public static Profil.Languages getLang(UUID uuid) {
        val profil = CommonsBukkit.getInstance().getProfilCache().get(uuid.toString());
        return profil.getLang();
    }

    public static MessageConfiguration getMessageConfig(Player player) {
        return ExpressoBukkit.getMessages().get(getLang(player));
    }

    public static MessageConfiguration getMessageConfig(UUID uuid) {
        return ExpressoBukkit.getMessages().get(getLang(uuid));
    }

    public static MessageConfiguration getMessageConfig(CommandSender sender) {
        if (sender instanceof Player) {
            return getMessageConfig((Player) sender);
        } else {
            return getDefaultMessageConfig();
        }
    }

    public static MessageConfiguration getDefaultMessageConfig() {
        return ExpressoBukkit.getMessages().get(Profil.Languages.FR);
    }

}
