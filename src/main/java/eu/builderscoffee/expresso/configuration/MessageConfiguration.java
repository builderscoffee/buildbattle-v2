package eu.builderscoffee.expresso.configuration;

import eu.builderscoffee.api.common.configuration.annotation.Configuration;
import eu.builderscoffee.expresso.configuration.messages.*;
import lombok.Data;

@Data
@Configuration("messages")
public final class MessageConfiguration {

    /* Global */
    String prefix = "§6§lBuilders Coffee §8>> ";

    private BoardConfigurationPart board = new BoardConfigurationPart();
    private CommandConfigurationPart command = new CommandConfigurationPart();
    private GameConfigurationPart game = new GameConfigurationPart();
    private MenuConfigurationPart menu = new MenuConfigurationPart();
    private ToolbarConfigurationPart toolbar = new ToolbarConfigurationPart();
    private TeamConfigurationPart team = new TeamConfigurationPart();
    private InvitationConfigurationPart invitation = new InvitationConfigurationPart();
}
