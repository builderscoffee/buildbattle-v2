package eu.builderscoffee.expresso.utils;

import com.plotsquared.core.PlotAPI;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.util.SchematicHandler;
import com.plotsquared.core.util.task.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


public class PlotUtils {

    public static Set<Plot> allPlots = new PlotAPI().getAllPlots();
    boolean exportAll = false;

    /***
     * Convertir une Location Plot en Location Bukkit
     * @param location
     * @return
     */
    public static Location convertPlotCenterLoc(com.plotsquared.core.location.Location location) {
        return new Location(Bukkit.getWorld(location.getWorldName()), location.getX(), location.getY(), location.getZ());
    }

    /***
     * Convertir une Location Bukkit en Plot
     * @param location
     * @return
     */
    public static com.plotsquared.core.location.Location convertBukkitLoc(Location location) {
        return com.plotsquared.core.location.Location.at(Objects.requireNonNull(location.getWorld()).getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
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
    public boolean exportAll(
            final File outputDir,
            final String namingScheme,
            final Runnable ifSuccess
    ) {
        if (this.exportAll) {
            return false;
        }
        this.exportAll = true;
        TaskManager.runTaskAsync(new Runnable() {
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

                final String owner;
                if (plot.hasOwner()) {
                    owner = plot.getOwnerAbs().toString();
                } else {
                    owner = "unknown";
                }

                final String name;
                if (namingScheme == null) {
                    name = plot.getId().getX() + ";" + plot.getId().getY() + ',' + plot.getArea() + ',' + owner;
                } else {
                    name = namingScheme.replaceAll("%id%", plot.getId().toString()).replaceAll("%idx%", plot.getId().getX() + "")
                            .replaceAll("%idy%", plot.getId().getY() + "").replaceAll("%world%", plot.getArea().toString());
                }


                final Runnable THIS = this;
                SchematicHandler.manager.getCompoundTag(plot)
                        .whenComplete((compoundTag, throwable) -> {
                            if (compoundTag != null) {
                                TaskManager.runTaskAsync(() -> {
                                    boolean result = SchematicHandler.manager.save(compoundTag, outputDir + File.separator + name + ".schem");
                                    if (!result) {
                                        System.out.println("Failed to save " + plot.getId());
                                    }
                                    TaskManager.runTask(THIS);
                                });
                            }
                        });
            }
        });
        return true;
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
