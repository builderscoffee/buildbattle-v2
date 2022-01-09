package eu.builderscoffee.expresso.utils;

import com.plotsquared.bukkit.util.BukkitSetupUtils;
import com.plotsquared.core.configuration.ConfigurationNode;
import com.plotsquared.core.util.SetupUtils;
import lombok.*;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WorldBuilder {

    @Getter(AccessLevel.PROTECTED)
    private final SetupObject setupObj;
    @Getter(AccessLevel.PROTECTED)
    private final String generator;
    @Setter
    @Getter
    private int terrain = 0;

    public WorldBuilder(String generator, WorldTypes worldType) {
        this.generator = generator;

        // Update generators
        BukkitSetupUtils.manager.updateGenerators();

        // Create new setup object
        this.setupObj = new SetupObject();

        // if generator not exist, throw error
        if (!BukkitSetupUtils.generators.containsKey(generator)) {
            throw new RuntimeException(generator + " generator doesn't exist. Choose between: " + String.join(", ", BukkitSetupUtils.generators.keySet()));
        }

        // Store generators & world type
        getSetupObj().setupGenerator = generator;
        getSetupObj().plotManager = generator;
        getSetupObj().type = worldType.id;

        // Store configurations depends on generator
        getSetupObj().step = SetupUtils.generators.get(setupObj.plotManager).getPlotGenerator()
                .getNewPlotArea("CheckingPlotSquaredGenerator", null, null, null).getSettingNodes();
    }

    public void generate(String world) {
        // Store world name & terrain
        getSetupObj().world = world;
        getSetupObj().terrain = terrain;

        // Start generating world
        BukkitSetupUtils.manager.setupWorld(getSetupObj());
    }

    public enum WorldTypes {
        DEFAULT(0),
        AUGMENTED(1),
        PARTIAL(2);

        public int id;

        WorldTypes(int id) {
            this.id = id;
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class DefaultWorldBuilder extends WorldBuilder {
        private int plotHeight;
        private int plotSize;
        @NonNull
        private ItemStack plotFilling = new ItemStack(Material.STONE);
        @NonNull
        private ItemStack plotFloor = new ItemStack(Material.STONE);
        @NonNull
        private ItemStack wall = new ItemStack(Material.STONE);
        @NonNull
        private ItemStack wallClaimed = new ItemStack(Material.STONE);
        private int roadWidth;
        private int roadHeight;
        @NonNull
        private ItemStack roadBlock = new ItemStack(Material.STONE);
        @NonNull
        private ItemStack wallFilling = new ItemStack(Material.STONE);
        private int wallHeight;
        private boolean bedrock;

        public DefaultWorldBuilder() {
            super("PlotSquared", WorldBuilder.WorldTypes.DEFAULT);

            // Get default values from default generator
            for (ConfigurationNode node : getSetupObj().step) {
                switch (node.getConstant()) {
                    case "plot.height":
                        plotHeight = (int) node.getDefaultValue();
                        break;
                    case "plot.size":
                        plotSize = (int) node.getDefaultValue();
                        break;
                    case "plot.filling":
                        val split0 = ((String) node.getDefaultValue()).split(":");
                        plotFilling = new ItemStack(Integer.valueOf(split0[0]), 1, Short.valueOf(split0[1]));
                        break;
                    case "plot.floor":
                        val split1 = ((String) node.getDefaultValue()).split(":");
                        plotFloor = new ItemStack(Integer.valueOf(split1[0]), 1, Short.valueOf(split1[1]));
                        break;
                    case "wall.block":
                        val pb0 = ((PlotBlock) node.getDefaultValue());
                        wall = new ItemStack(pb0.id, 1, pb0.data);
                        break;
                    case "wall.block_claimed":
                        val pb1 = ((PlotBlock) node.getDefaultValue());
                        wallClaimed = new ItemStack(pb1.id, 1, pb1.data);
                        break;
                    case "road.width":
                        roadWidth = (int) node.getDefaultValue();
                        break;
                    case "road.height":
                        roadHeight = (int) node.getDefaultValue();
                        break;
                    case "road.block":
                        val pb2 = ((PlotBlock) node.getDefaultValue());
                        roadBlock = new ItemStack(pb2.id, 1, pb2.data);
                        break;
                    case "wall.filling":
                        val pb3 = ((PlotBlock) node.getDefaultValue());
                        wallFilling = new ItemStack(pb3.id, 1, pb3.data);
                        break;
                    case "wall.height":
                        wallHeight = (int) node.getDefaultValue();
                        break;
                    case "plot.bedrock":
                        bedrock = (boolean) node.getDefaultValue();
                        break;
                }
            }
        }

        @Override
        public void generate(String world) {
            // Set values
            for (ConfigurationNode node : getSetupObj().step) {
                switch (node.getConstant()) {
                    case "plot.height":
                        node.setValue(String.valueOf(plotHeight));
                        break;
                    case "plot.size":
                        node.setValue(String.valueOf(plotSize));
                        break;
                    case "plot.filling":
                        node.setValue(plotFilling.getType().getId() + ":" + plotFilling.getDurability());
                        break;
                    case "plot.floor":
                        node.setValue(plotFloor.getType().getId() + ":" + plotFloor.getDurability());
                        break;
                    case "wall.block":
                        node.setValue(wall.getType().getId() + ":" + wall.getDurability());
                        break;
                    case "wall.block_claimed":
                        node.setValue(wallClaimed.getType().getId() + ":" + wallClaimed.getDurability());
                        break;
                    case "road.width":
                        node.setValue(String.valueOf(roadWidth));
                        break;
                    case "road.height":
                        node.setValue(String.valueOf(roadHeight));
                        break;
                    case "road.block":
                        node.setValue(roadBlock.getType().getId() + ":" + roadBlock.getDurability());
                        break;
                    case "wall.filling":
                        node.setValue(wallFilling.getType().getId() + ":" + wallFilling.getDurability());
                        break;
                    case "wall.height":
                        node.setValue(String.valueOf(wallHeight));
                        break;
                    case "plot.bedrock":
                        node.setValue(String.valueOf(bedrock));
                        break;
                }
            }
            // Generate world
            super.generate(world);
        }
    }
}
