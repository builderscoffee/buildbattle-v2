package eu.builderscoffee.expresso.utils;

import eu.builderscoffee.api.common.utils.LogUtils;
import eu.builderscoffee.expresso.ExpressoBukkit;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@UtilityClass
public class BackupUtils {

    /**
     * Backups the world
     *
     * @param worldName  The world name
     * @param serverName Server name
     */
    @SneakyThrows
    public static void backupWorld(@NonNull String worldName, @NonNull String serverName) {
        val dir = Paths.get(worldName);
        val dest = Paths.get(ExpressoBukkit.getSettings().getPath_for_backup_world(), serverName, worldName);

        // Check if world directory exists
        if (!Files.exists(dir) || !Files.isDirectory(dir)) {
            LogUtils.warn("Can't backup world if the folder doesn't exist !");
            return;
        }

        // Save the world before backup
        val world = Bukkit.getWorld(worldName);
        if (Objects.nonNull(world)) {
            world.save();
        }

        // Delete previous destination world backup
        if (Files.exists(dest)) {
            deleteDirectory(dest.toFile());
        }

        // Create directories at backup destination directory
        if (!Files.exists(dest) || !Files.isDirectory(dest)) {
            Files.createDirectories(dest.getParent());
        }

        // Backup world
        copyDirectory(dir.toAbsolutePath().toString(), dest.toAbsolutePath().toString());

        val plotsquared = Paths.get("plugins", "PlotSquared");
        val destPlotsquared = Paths.get(ExpressoBukkit.getSettings().getPath_for_backup_world(), serverName, "PlotSquared");

        // Delete plotsquared config files at destination
        if (Files.exists(destPlotsquared)) {
            deleteDirectory(destPlotsquared.toFile());
        }

        // Backup plotsquared config files at destination
        copyDirectory(plotsquared.toAbsolutePath().toString(), destPlotsquared.toAbsolutePath().toString());
    }

    /**
     * Check if a world has been backup
     *
     * @param worldName  The world name
     * @param serverName Server name
     * @return Returns true if a backup of this world exists
     */
    public static boolean backupOfWorldExist(@NonNull String worldName, @NonNull String serverName) {
        val dest = Paths.get(ExpressoBukkit.getSettings().getPath_for_backup_world(), serverName, worldName);
        return Files.exists(dest) && Files.isDirectory(dest);
    }

    /**
     * Delete directory and its content
     *
     * @param directoryToBeDeleted The file object of a directory
     * @return Returns true if it is correctly deleted
     */
    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    /**
     * Copy directory
     *
     * @param sourceDirectoryLocation      Source path
     * @param destinationDirectoryLocation Destination path
     * @throws IOException
     */
    private static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException {
        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
