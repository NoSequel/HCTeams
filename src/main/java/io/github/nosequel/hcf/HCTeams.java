package io.github.nosequel.hcf;

import io.github.nosequel.hcf.classes.ClassController;
import io.github.nosequel.hcf.commands.SystemTeamCommand;
import io.github.nosequel.hcf.commands.TeamCommand;
import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.controller.ControllerHandler;
import io.github.nosequel.hcf.listeners.PlayerListeners;
import io.github.nosequel.hcf.listeners.claim.ClaimListeners;
import io.github.nosequel.hcf.listeners.claim.ClaimSelectionListener;
import io.github.nosequel.hcf.listeners.team.ChatListener;
import io.github.nosequel.hcf.listeners.team.DamageListeners;
import io.github.nosequel.hcf.listeners.team.DeathListeners;
import io.github.nosequel.hcf.player.PlayerDataController;
import io.github.nosequel.hcf.scoreboard.BoardProviderHandler;
import io.github.nosequel.hcf.tasks.TaskController;
import io.github.nosequel.hcf.team.TeamController;
import io.github.nosequel.hcf.timers.TimerController;
import io.github.nosequel.hcf.timers.commands.PvPCommand;
import io.github.nosequel.hcf.timers.commands.TimerCommand;
import io.github.nosequel.hcf.util.command.CommandController;
import io.github.nosequel.hcf.util.database.DatabaseController;
import io.github.nosequel.hcf.util.database.handler.data.MongoDataHandler;
import io.github.nosequel.hcf.util.database.options.impl.MongoDatabaseOption;
import io.github.nosequel.hcf.util.database.type.mongo.MongoDataType;
import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
public class HCTeams extends JavaPlugin {

    @Getter
    private static HCTeams instance;

    private final ControllerHandler handler = new ControllerHandler();

    @Override
    public void onEnable() {
        // register the instance
        instance = this;


        // setup database controller
        final DatabaseController controller = new DatabaseController(
                new MongoDatabaseOption(
                        "127.0.0.1",
                        "",
                        "",
                        "hcteams",
                        27017
                ),
                new MongoDataType()
        );

        controller.setDataHandler(new MongoDataHandler(controller));

        // register controllers
        this.handler.registerController(controller);
        this.handler.registerController(new TeamController());
        this.handler.registerController(new PlayerDataController());
        this.handler.registerController(new ClassController());
        this.handler.registerController(new TimerController());
        this.handler.registerController(new TaskController());

        // register commands
        final CommandController commandController = handler.registerController(new CommandController("hcteams"));
        commandController.registerCommand(
                new TeamCommand(),
                new SystemTeamCommand(),
                new TimerCommand(),
                new PvPCommand()
        );

        // register listeners
        final PluginManager pluginManager = Bukkit.getPluginManager();

        pluginManager.registerEvents(new ClaimListeners(), this);
        pluginManager.registerEvents(new ClaimSelectionListener(), this);
        pluginManager.registerEvents(new PlayerListeners(), this);
        pluginManager.registerEvents(new DamageListeners(), this);
        pluginManager.registerEvents(new DeathListeners(), this);
        pluginManager.registerEvents(new ChatListener(), this);

        // setup scoreboard
        new Assemble(this, new BoardProviderHandler()).setAssembleStyle(AssembleStyle.MODERN);
    }

    @Override
    public void onDisable() {
        handler.getControllers().forEach(Controller::disable);
    }
}