package eu.builderscoffee.expresso.utils;

import com.fasterxml.uuid.Generators;
import com.intellectualcrafters.jnbt.CompoundTag;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.RunnableVal;
import com.intellectualcrafters.plot.util.SchematicHandler;
import com.intellectualcrafters.plot.util.TaskManager;
import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.SchematicHandler;
import com.plotsquared.core.util.task.RunnableVal;
import com.plotsquared.core.util.task.TaskManager;
import eu.builderscoffee.api.common.data.DataManager;
import eu.builderscoffee.api.common.data.tables.BuildbattleEntity;
import eu.builderscoffee.api.common.data.tables.ProfilEntity;
import eu.builderscoffee.api.common.data.tables.SchematicsEntity;
import eu.builderscoffee.expresso.ExpressoBukkit;
import eu.builderscoffee.expresso.configuration.SettingsConfiguration;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public class PlotUtils {

    public static Set<Plot> allPlots = new PlotAPI().getAllPlots();
    SettingsConfiguration settings = ExpressoBukkit.getSettings();

    /***
     * Convertir une Location Plot en Location Bukkit
     * @param location
     * @return
     */
    public static Location convertPlotCenterLoc(com.intellectualcrafters.plot.object.Location location) {
        return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }

    /***
     * Convertir une Location Bukkit en Plot
     * @param location
     * @return
     */
    public static com.intellectualcrafters.plot.object.Location convertBukkitLoc(Location location) {
        return new com.intellectualcrafters.plot.object.Location(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /***
     * Retourne la position d'un plot
     * @param plot
     * @return
     */
    public static int getPlotsPos(Plot plot) {
        return new ArrayList<>(allPlots).indexOf(plot);
    }

    /***
     * Retourne le plot par rapport à la position dans la list
     * @param
     * @return
     */
    public static Plot getPlotsByPos(int in) {
        return new ArrayList<>(allPlots).get(in);
    }

    /***
     * Exporter toutes les schématics dans le dossier
     */
    public boolean exportAllSchematics(@NonNull String outputDir, final Runnable ifSuccess) {
        TaskManager.runTask(new Runnable() {
            @Override
            public void run() {
                // Si il n'y a plus de plot à sauvegarder , exécuter la tache IfSuccess
                if (allPlots.isEmpty()) {
                    TaskManager.runTask(ifSuccess);
                    return;
                }
                // Itérer le prochain plot
                Iterator<Plot> i = allPlots.iterator();
                final Plot plot = i.next();
                i.remove();

                final Runnable THIS = this;
                SchematicHandler.manager.getCompoundTag(plot, new RunnableVal<CompoundTag>() {
                    @Override
                    public void run(final CompoundTag value) {
                        // Générer un UUID basé sur le temps
                        UUID uuid = Generators.timeBasedGenerator().generate();
                        String name = uuid.toString();

                        if (value == null) {
                            ExpressoBukkit.getBbGame().broadcast("§7 - Plot suivant §c" + plot.getId());
                        } else {
                            TaskManager.runTaskAsync(() -> {
                                ExpressoBukkit.getBbGame().broadcast("§6ID: §f" + plot.getId() + "§6UUID: §7" + name);
                                boolean result = SchematicHandler.manager
                                        .save(value, outputDir + File.separator + name + ".schematic");
                                if (!result)
                                    ExpressoBukkit.getBbGame().broadcast("§7 - Impossible à sauvegarder §c" + plot.getId());
                                else {

                                    ExpressoBukkit.getBbGame().broadcast("§7 - §a  sauvegarder: " + plot.getId());

                                    val plotsMembers = plot.getMembers().stream()
                                            .map(memberUuid -> uuid.toString())
                                            .distinct()
                                            .collect(Collectors.toList());
                                    val pl = DataManager.getProfilStore()
                                            .select(ProfilEntity.class)
                                            .where(ProfilEntity.UNIQUE_ID.in(plotsMembers))
                                            .get();

                                    // TODO Get buildbattle or cup stored in buildbattle manager
                                    // Temp
                                    val bb = new BuildbattleEntity();

                                    System.out.println("\tPlayers:");
                                    pl.forEach(p -> System.out.println("\t - " + p.getName()));
                                    System.out.println("\tBB: " + bb.getNum());

                                    val schem = new SchematicsEntity();
                                    schem.setToken(uuid);
                                    schem.setBuildbattle(bb);
                                    pl.forEach(schem.getProfils()::add);

                                    DataManager.getSchematicsStore().insert(schem);
                                }
                                TaskManager.runTask(THIS);
                            });
                        }
                    }
                });
            }
        });
        return true;
    }

    /***
     * Coller une schématique sur un plot
     * @param schematicLocation
     * @param plot
     */
    public void pasteSchematic(String schematicLocation, Plot plot) {
        TaskManager.runTaskAsync(() -> {
            SchematicHandler.Schematic schematic;
            schematic = SchematicHandler.manager.getSchematic(schematicLocation);
            if (schematic == null) {
                Log.get().info("Schematic null");
                return;
            }
            SchematicHandler.manager.paste(schematic, plot, 0, plot.getArea().MIN_BUILD_HEIGHT, 0, false, new RunnableVal<Boolean>() {
                @Override
                public void run(Boolean value) {
                    if (value) {
                        Log.get().info("Schematic " + schematicLocation + " paste to " + plot.getId());

                    } else {
                        Log.get().severe("Schematic " + schematicLocation + " error paste from " + plot.getId());
                    }
                }
            });
        });
    }

    /***
     * Retour si un plot a été merge avec d'autres
     * @param plot
     * @return
     */
    public boolean isMerged(Plot plot) {
        return plot.getConnectedPlots().size() < 1;
    }
}
