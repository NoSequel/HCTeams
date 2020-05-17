package io.github.nosequel.hcf.timers;

import com.google.common.collect.HashBasedTable;
import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.timers.impl.CombatTimer;
import io.github.nosequel.hcf.timers.impl.EnderpearlTimer;
import io.github.nosequel.hcf.timers.impl.TeleportTimer;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TimerController implements Controller {

    private final List<Timer> timers = new ArrayList<>();

    public void enable() {
        this.registerTimer(new EnderpearlTimer());
        this.registerTimer(new CombatTimer());
        this.registerTimer(new TeleportTimer());
    }

    /**
     * Find a timer by a class
     *
     * @param clazz the class
     * @param <T> the type of the timer
     * @return the timer | or null
     */
    public <T extends Timer> T findTimer(Class<T> clazz) {
        return clazz.cast(this.timers.stream()
                .filter(timer -> timer.getClass().equals(clazz))
                .findFirst().orElse(null));
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