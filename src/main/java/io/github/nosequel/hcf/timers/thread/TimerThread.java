package io.github.nosequel.hcf.timers.thread;

import io.github.nosequel.hcf.timers.Timer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

@Getter
public class TimerThread extends BukkitRunnable {

    private final Timer timer;

    private final Map<Player, Long> durations = new HashMap<>();
    private final Set<Player> cancelled = new HashSet<>();

    /**
     * Constructor for creating a new TimerThread instance
     *
     * @param timer the timer it's bound to
     */
    public TimerThread(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void run() {
        synchronized (durations) {
            durations.forEach((player, $duration) -> {
                // check whether the timer of the player should be cancelled or not.
                if (cancelled.contains(player)) {
                    durations.remove(player);
                    cancelled.remove(player);
                    timer.handleCancel(player);

                    return;
                }

                final long duration = $duration - 50;

                // if the timer has been expired, remove the player from the map and end the timer.
                if (duration <= 0L) {
                    durations.remove(player);
                    timer.handleEnd(player);
                } else { // if not, update the time in the map and handle the tick method.
                    durations.put(player, duration);
                    timer.handleTick(player);
                }


                // everything which needed to be cancelled has been cancelled by now, so we can clear the map.
                this.cancelled.clear();
            });
        }
    }
}