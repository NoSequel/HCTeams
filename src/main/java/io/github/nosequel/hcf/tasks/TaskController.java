package io.github.nosequel.hcf.tasks;

import io.github.nosequel.hcf.classes.bard.task.BardClassTask;
import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.tasks.impl.DTRTask;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskController implements Controller {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public void enable() {
        tasks.addAll(Arrays.asList(
                new DTRTask(),
                new BardClassTask()
        ));
    }

    @Override
    public void disable() {
        tasks.forEach(BukkitRunnable::cancel);
    }

}
