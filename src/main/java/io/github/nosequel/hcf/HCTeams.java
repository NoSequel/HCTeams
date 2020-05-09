package io.github.nosequel.hcf;

import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.controller.ControllerHandler;
import io.github.nosequel.hcf.listeners.ClaimListeners;
import io.github.nosequel.hcf.team.TeamController;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class HCTeams extends JavaPlugin {

    @Getter
    private static HCTeams instance;

    private final ControllerHandler handler = new ControllerHandler();

    @Override
    public void onEnable() {
        instance = this;

        this.handler.registerController(new TeamController());
        Bukkit.getPluginManager().registerEvents(new ClaimListeners(), this);
    }

    @Override
    public void onDisable() {
        handler.getControllers().forEach(Controller::disable);
    }
}