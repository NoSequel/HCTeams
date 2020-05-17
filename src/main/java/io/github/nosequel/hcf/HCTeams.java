package io.github.nosequel.hcf;

import io.github.nosequel.hcf.commands.TeamCommand;
import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.controller.ControllerHandler;
import io.github.nosequel.hcf.listeners.PlayerListeners;
import io.github.nosequel.hcf.listeners.claim.ClaimListeners;
import io.github.nosequel.hcf.listeners.claim.ClaimSelectionListener;
import io.github.nosequel.hcf.player.PlayerDataController;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.util.command.CommandController;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class HCTeams extends JavaPlugin {

    @Getter
    private static HCTeams instance;

    private final ControllerHandler handler = new ControllerHandler();

    @Override
    public void onEnable() {
        // register the instance
        instance = this;

        // register controllers
        this.handler.registerController(new TeamController());
        this.handler.registerController(new PlayerDataController());

        // register commands
        final CommandController commandController = handler.registerController(new CommandController("hcteams"));
        commandController.registerCommand(new TeamCommand());

        // register listeners
        final PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new ClaimListeners(), this);
        pluginManager.registerEvents(new ClaimSelectionListener(), this);

        pluginManager.registerEvents(new PlayerListeners(), this);

    }

    @Override
    public void onDisable() {
        handler.getControllers().forEach(Controller::disable);
    }
}