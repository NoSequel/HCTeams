package io.github.nosequel.hcf.tasks;

import io.github.nosequel.hcf.HCTeams;
import lombok.SneakyThrows;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Task extends BukkitRunnable {

    public Task(long duration) {
        this.runTaskTimer(HCTeams.getInstance(), duration, 0);
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            this.tick();
        } catch (Exception e) {
            Exception exception = new RuntimeException("Error occured in " + getName() + "");
            exception.setStackTrace(e.getStackTrace());

            throw exception;
        }
    }

    /**
     * Called upon tick of the task
     *
     * @throws Exception all exceptions thrown
     */
    public abstract void tick() throws Exception;

    /**
     * Get the name of the task
     *
     * @return the name
     */
    public abstract String getName();

}