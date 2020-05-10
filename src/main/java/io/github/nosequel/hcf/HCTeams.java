package io.github.nosequel.hcf;

import app.ashcon.intake.bukkit.BukkitIntake;
import app.ashcon.intake.bukkit.graph.BasicBukkitCommandGraph;
import app.ashcon.intake.fluent.DispatcherNode;
import io.github.nosequel.hcf.commands.TeamCommand;
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
        // register the instance
        instance = this;

        // register controllers
        this.handler.registerController(new TeamController());

        // register commands
        final BasicBukkitCommandGraph graph = new BasicBukkitCommandGraph();
        final DispatcherNode dispatcherNode = graph.getRootDispatcherNode().registerNode("team", "t", "faction", "f");

        dispatcherNode.registerCommands(new TeamCommand());

        new BukkitIntake(this, graph).register();

        // register listeners
        Bukkit.getPluginManager().registerEvents(new ClaimListeners(), this);
    }

    @Override
    public void onDisable() {
        handler.getControllers().forEach(Controller::disable);
    }
}