package io.github.nosequel.hcf.timers;

import com.google.common.collect.HashBasedTable;
import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.timers.impl.EnderpearlTimer;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TimerController implements Controller {

    private final List<Timer> timers = new ArrayList<>();

    public void enable() {
        this.registerTimer(new EnderpearlTimer());
    }

    /**
     * Register a timer
     *
     * @param timer the timer
     */
    private void registerTimer(Timer timer) {
        Bukkit.getPluginManager().registerEvents(timer, HCTeams.getInstance());

        this.timers.add(timer);
    }
}