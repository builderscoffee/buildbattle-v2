package eu.builderscoffee.expresso.utils;

import com.plotsquared.bukkit.util.BukkitSetupUtils;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.configuration.ConfigurationNode;
import com.plotsquared.core.plot.PlotAreaType;
import com.plotsquared.core.setup.CommonSetupSteps;
import com.plotsquared.core.setup.PlotAreaBuilder;
import com.plotsquared.core.setup.SettingsNodesWrapper;
import com.plotsquared.core.util.SetupUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;

@Getter
@Setter
public class WorldBuilder implements Listener {

    private final PlotAreaBuilder builder;

    @SneakyThrows
    public WorldBuilder(String generator, PlotAreaType worldType) {
        // Update generators
        PlotSquared.platform().setupUtils().updateGenerators(true);

        // if generator not exist, throw error
        if (!BukkitSetupUtils.generators.containsKey(generator)) {
            throw new RuntimeException("Generator \"" + generator + "\" doesn't exist. Choose between: " + String.join(", ", BukkitSetupUtils.generators.keySet()));
        }

        // Create the PlotAreaBuilder
        builder = PlotAreaBuilder.newBuilder();

        // Setting generator
        builder.generatorName(generator);

        // Setting plot area type
        builder.plotAreaType(worldType);

        // Create node wrapper if not exist
        if (builder.settingsNodesWrapper() == null) {
            builder.plotManager(builder.generatorName());
            Method method = CommonSetupSteps.class.getDeclaredMethod("wrap", String.class);
            method.setAccessible(true);
            builder.settingsNodesWrapper((SettingsNodesWrapper) method.invoke(null, builder.plotManager()));
            SetupUtils.generators.get(builder.plotManager()).getPlotGenerator()
                    .processAreaSetup(builder);
        }
    }

    /**
     * Function to simplify the configuration of a setting node
     * @param key Key
     * @param value Value
     * @return Return true if it has been changed
     */
    public boolean configureNode(String key, String value) {
        for (ConfigurationNode settingsNode : builder.settingsNodesWrapper().getSettingsNodes()) {
            if (settingsNode.getConstant().equals(key)) {
                settingsNode.setValue(value);
                return true;
            }
        }
        return false;
    }

    /**
     * Function to get a setting node
     * @param key Key
     * @return Return the configuration node
     */
    public ConfigurationNode getNode(String key) {
        for (ConfigurationNode settingsNode : builder.settingsNodesWrapper().getSettingsNodes()) {
            if (settingsNode.getConstant().equals(key)) {
                return settingsNode;
            }
        }
        return null;
    }

    /**
     * Check if a setting node exist
     * @param key Key
     * @return Return true if exist
     */
    public boolean hasNode(String key) {
        return getNode(key) != null;
    }

    /**
     * Get all setting nodes of a generator
     * @return
     */
    public ConfigurationNode[] getNodes() {
        return builder.settingsNodesWrapper().getSettingsNodes();
    }

    /**
     * Setup the world
     * @param worldName The world name
     */
    public void setup(String worldName) {
        builder.worldName(worldName);
        PlotSquared.platform().setupUtils().setupWorld(builder);
    }

    @Accessors(chain = true)
    @Getter
    @Setter
    public static class DefaultWorldBuilder extends WorldBuilder {
        private int plotHeight = 62;
        private int plotWidth = 42;
        private Material plotBlock = Material.STONE;
        private boolean blockOnTopOfPlotBlock = true;
        private Material plotFloorBlock = Material.GRASS_BLOCK;
        private Material topWallBlockUnClaimed = Material.STONE_SLAB;
        private Material topWallBlockClaimed = Material.SANDSTONE_SLAB;
        private int roadWidth = 7;
        private int roadHeight = 62;
        private Material roadBlock = Material.QUARTZ_BLOCK;
        private Material wallFillingBlock = Material.STONE;
        private int wallHeight = 62;
        private boolean hasBedrock = true;

        public DefaultWorldBuilder() {
            super("Test", PlotAreaType.NORMAL);
        }

        @Override
        public void setup(String worldName) {
            configureNode("plot.height", String.valueOf(plotHeight));
            configureNode("plot.size", String.valueOf(plotWidth));
            configureNode("plot.filling", plotBlock.getKey().getNamespace() + ":" + plotBlock.getKey().getKey());
            configureNode("wall.place_top_block", String.valueOf(blockOnTopOfPlotBlock));
            configureNode("plot.floor", plotFloorBlock.getKey().getNamespace() + ":" + plotFloorBlock.getKey().getKey());
            configureNode("wall.block", topWallBlockUnClaimed.getKey().getNamespace() + ":" + topWallBlockUnClaimed.getKey().getKey());
            configureNode("wall.block_claimed", topWallBlockClaimed.getKey().getNamespace() + ":" + topWallBlockClaimed.getKey().getKey());
            configureNode("road.width", String.valueOf(roadWidth));
            configureNode("road.height", String.valueOf(roadHeight));
            configureNode("road.block", roadBlock.getKey().getNamespace() + ":" + roadBlock.getKey().getKey());
            configureNode("wall.filling", wallFillingBlock.getKey().getNamespace() + ":" + wallFillingBlock.getKey().getKey());
            configureNode("wall.height", String.valueOf(wallHeight));
            configureNode("plot.bedrock", String.valueOf(hasBedrock));

            super.setup(worldName);
        }
    }
}
