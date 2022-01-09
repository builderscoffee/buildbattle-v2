package eu.builderscoffee.expresso;

import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionManager {

    @Getter
    private final Map<String, BukkitTask> tasks;

    @Getter
    private boolean active;

    public ExecutionManager() {
        this.tasks = new HashMap<>();
        this.active = false;
    }

    /**
     * Start ExecutionManager (all tasks)
     */
    public void start() {
        // Each tick updating
        //tasks.put("testTask", new TestTask().runTaskTimer(ExpressoBukkit.getInstance(), 0L, 30 * 20));
        // Activate ExecutionManager
        active = true;
    }

    /**
     * Shutdown ExecutionManager (all tasks)
     */
    public void shutdown() {
        cancelAllTasks();
        // Desactivate ExecutionManager
        active = false;
    }

    /***
     * Clean all active tasks
     */
    public void cancelAllTasks() {
        // Cancel all tasks
        getBukkitTasks().forEach(BukkitTask::cancel);
        tasks.clear(); // clear tasks list
    }

    /**
     * Cancel a custom tasks
     * @param task The name of tasks to cancel
     */
    public void cancel(String task) {
        if (!tasks.containsKey(task)) return;
        // cancel tasks
        tasks.get(task).cancel();
        // remove tasks
        tasks.remove(task);
    }

    /**
     * Get all tasks running
     * @return Current running tasks list
     */
    public List<BukkitTask> getBukkitTasks() {
        return new ArrayList<>(tasks.values());
    }

}