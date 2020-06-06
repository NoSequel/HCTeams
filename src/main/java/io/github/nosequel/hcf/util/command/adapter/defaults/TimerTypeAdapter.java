package io.github.nosequel.hcf.util.command.adapter.defaults;

import io.github.nosequel.hcf.controller.Controllable;
import io.github.nosequel.hcf.timers.Timer;
import io.github.nosequel.hcf.timers.TimerController;
import io.github.nosequel.hcf.util.command.adapter.TypeAdapter;
import org.bukkit.command.CommandSender;

public class TimerTypeAdapter implements TypeAdapter<Timer>, Controllable<TimerController> {

    @Override
    public Timer convert(CommandSender sender, String source) {
        return this.getController().getTimers().stream()
                .filter(timer -> timer.getName().equalsIgnoreCase(source))
                .findFirst().orElse(null);
    }

    @Override
    public Class<Timer> getType() {
        return Timer.class;
    }
}
