package eu.builderscoffee.expresso.buildbattle.config.configs;

import eu.builderscoffee.api.bukkit.utils.ItemBuilder;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerRequest;
import eu.builderscoffee.commons.common.redisson.packets.ServerManagerResponse;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.buildbattle.config.ConfigTemplate;
import eu.builderscoffee.expresso.buildbattle.phase.BBPhase;
import eu.builderscoffee.expresso.buildbattle.phase.bases.EndPhase;
import eu.builderscoffee.expresso.utils.TimeUtils;
import lombok.val;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class PhasesConfig extends ConfigTemplate {

    public PhasesConfig() {
        super("playtime");
    }

    @Override
    public ServerManagerResponse request(ServerManagerRequest request, ServerManagerResponse response) {
        System.out.println(">> Request " + this.getClass().getSimpleName());
        val gameType = ExpressoBukkit.getBbGame().getBuildBattleGameType();

        if (request.getData().equals("setplaytime")) {
            ExpressoBukkit.getBbGame().setInstancePhases(ExpressoBukkit.getBbGame().getBuildBattleGameType().getPhases());
            return redirect(SingleThemeConfig.class, response);
        }

        for (BBPhase phase : gameType.getPhases()) {
            if (request.getData().equals(phase.getClass().getSimpleName())) {
                switch (request.getItemAction()) {
                    case LEFT_CLICK:
                        phase.setTime(phase.getTime() + (int) phase.getUnit().toSeconds(1));
                        break;
                    case SHIFT_LEFT_CLICK:
                        phase.setTime(phase.getTime() + (int) phase.getUnit().toSeconds(60));
                        break;
                    case RIGHT_CLICK:
                        phase.setTime(phase.getTime() <= (int) phase.getUnit().toSeconds(1) ? 0 : phase.getTime() - (int) phase.getUnit().toSeconds(1));
                        break;
                    case SHIFT_RIGHT_CLICK:
                        phase.setTime(phase.getTime() <= (int) phase.getUnit().toSeconds(60) ? 0 : phase.getTime() - (int) phase.getUnit().toSeconds(60));
                        break;
                    case DROP:
                        phase.setTime(phase.getDefaultTime());
                        break;
                }
                return response(response);
            }
        }
        return response(response);
    }

    @Override
    public ServerManagerResponse response(ServerManagerResponse response) {
        System.out.println(">> Response " + this.getClass().getSimpleName());
        val itemsAction = new ServerManagerResponse.Items();
        itemsAction.setType(type);

        val gameType = ExpressoBukkit.getBbGame().getBuildBattleGameType();

        AtomicInteger i = new AtomicInteger(0);
        gameType.getPhases().stream()
                .filter(phase -> !(phase instanceof EndPhase))
                .forEach(phase -> itemsAction.addItem(2, 2 + i.incrementAndGet(), new ItemBuilder(phase.getIcon().getType()).setName("§a" + phase.getName()).addLoreLine(Arrays.asList("§bTemps:", "§bPar défault: §f" + TimeUtils.getDurationString(phase.getDefaultTime()), "§bCustom :§f" + TimeUtils.getDurationString(phase.getTime()))).build(), phase.getClass().getSimpleName()));

        itemsAction.addItem(3, 4, new ItemBuilder(Material.WOOL, 1, (short) 13).setName("§aValider les phases").build(), "setplaytime");

        // Add Action to response
        response.getActions().add(itemsAction);

        // Add return item
        addPreviousConfigItem(response, GameTypesConfig.class);
        return response;
    }
}
