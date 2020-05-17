package io.github.nosequel.hcf.timers;

import io.github.nosequel.hcf.HCTeams;
import io.github.nosequel.hcf.timers.thread.TimerThread;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

@Getter
public abstract class Timer implements Listener {

    private final String name;
    private final String scoreboardTag;

    private final long defaultDuration;
    private final boolean trailing;

    private final TimerThread thread;

    /**
     * Constructor for creating a new timer
     *
     * @param name the name of the timer
     */
    public Timer(String name, String scoreboardTag, boolean trailing, long defaultDuration) {
        this.name = name;
        this.scoreboardTag = scoreboardTag;
        this.defaultDuration = defaultDuration;
        this.trailing = trailing;

        this.thread = new TimerThread(this);
        this.thread.runTaskTimer(HCTeams.getInstance(), 2L, 0L);
    }

    /**
     * Cancel a player's timer
     *
     * @param player the player
     */
    public void cancel(Player player) {
        this.thread.getCancellations().put(player, true);
    }

    /**
     * Add a timer to a player with the default duration of the timer
     *
     * @param player the player
     */
    public void start(Player player) {
        this.thread.getDurations().put(player, defaultDuration);
    }

    /**
     * Start a timer for a player with a set duration of the timer
     *
     * @param player the player
     * @param duration the duration
     */
    public void start(Player player, long duration) {
        this.thread.getDurations().put(player, duration);
    }

    /**
     * Check whether the player has an active timer running or not.
     *
     * @param player the player
     * @return whether an active timer is running or not
     */
    public boolean isOnCooldown(Player player) {
        return this.thread.getDurations().containsKey(player);
    }

    /**
     * Get the remaining time a player is on the timer
     *
     * @param player the player
     * @return the remaining time
     */
    public long getDuration(Player player) {
        return this.thread.getDurations().get(player);
    }

    /**
     * Handle a timer tick
     *
     * @param player the player which it ticked for
     */
    public abstract void handleTick(Player player);

    /**
     * Handle the event of the timer ending
     *
     * @param player the player which it ended for
     */
    public abstract void handleEnd(Player player);

    /**
     * Handle the cancellation of a timer
     *
     * @param player the player which it got cancelled for
     */
    public abstract void handleCancel(Player player);
}
