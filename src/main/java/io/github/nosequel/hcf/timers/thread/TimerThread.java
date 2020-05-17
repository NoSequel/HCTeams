package io.github.nosequel.hcf.timers.thread;

import io.github.nosequel.hcf.timers.Timer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TimerThread extends BukkitRunnable {

    private final Timer timer;
    private final Map<Player, Long> durations = new HashMap<>();
    private final Map<Player, Boolean> cancellations = new HashMap<>();

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

            for(Map.Entry<Player, Long> entry : durations.entrySet()) {
                final Player player = entry.getKey();
                final long $duration = entry.getValue();

                if (cancellations.containsKey(player)) {
                    durations.remove(player);
                    timer.handleCancel(player);

                    return;
                }

                final long duration = $duration - 50;

                if (duration <= 0L) {
                    durations.remove(player);
                    timer.handleEnd(player);
                } else {
                    durations.put(player, duration);
                    timer.handleTick(player);
                }
            }
        }
    }

}