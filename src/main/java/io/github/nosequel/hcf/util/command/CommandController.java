package io.github.nosequel.hcf.util.command;

import io.github.nosequel.hcf.controller.Controller;
import io.github.nosequel.hcf.util.command.adapter.TypeAdapter;
import io.github.nosequel.hcf.util.command.adapter.defaults.*;
import io.github.nosequel.hcf.util.command.annotation.Command;
import io.github.nosequel.hcf.util.command.annotation.Subcommand;
import io.github.nosequel.hcf.util.command.data.CommandData;
import io.github.nosequel.hcf.util.command.data.SubcommandData;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommandController implements Controller {

    @Getter
    private static CommandController instance;

    private final String fallbackPrefix;

    private final List<TypeAdapter<?>> typeAdapters = new ArrayList<>();
    private final List<CommandData> commands = new ArrayList<>();

    /**
     * Create a new instance of the CommandModule class
     *
     * @param fallbackPrefix the fallback prefix
     */
    public CommandController(String fallbackPrefix) {
        instance = this;

        this.fallbackPrefix = fallbackPrefix;
        this.typeAdapters.addAll(Arrays.asList(
                new UUIDTypeAdapter(),
                new OfflinePlayerTypeAdapter(),
                new LongTypeAdapter(),
                new IntegerTypeAdapter(),
                new TeamTypeAdapter(),
                new PlayerTypeAdapter(),
                new TimerTypeAdapter()
        ));
    }

    /**
     * Register commands
     *
     * @param objects the commands
     */
    public void registerCommand(Object... objects) {
        Arrays.stream(objects).forEach(object -> {
            final List<Method> parentCommandMethods = Arrays.stream(object.getClass().getMethods())
                    .filter(command -> command.getAnnotation(Command.class) != null)
                    .collect(Collectors.toList());

            final List<Method> subCommands = Arrays.stream(object.getClass().getMethods())
                    .filter(command -> command.getAnnotation(Subcommand.class) != null && !parentCommandMethods.contains(command))
                    .collect(Collectors.toList());


            parentCommandMethods.forEach(command -> {
                final CommandData commandData = new CommandData(object, command);

                this.commands.add(commandData);
                this.getCommandMap().register(fallbackPrefix, new CustomCommand(commandData));
            });

            subCommands.stream()
                    .filter(command -> commands.stream().anyMatch(commandData -> commandData.isParentOfSubCommand(command.getAnnotation(Subcommand.class))))
                    .forEach(command -> {
                        final CommandData parentCommand = commands.stream()
                                .filter(data -> data.isParentOfSubCommand(command.getAnnotation(Subcommand.class)))
                                .findFirst().orElse(null);

                        assert parentCommand != null;
                        parentCommand.getSubcommands().add(new SubcommandData(object, command));
                    });
        });
    }

    /**
     * Find a converter by a class type
     *
     * @param type the type
     * @param <T>  the return type
     * @return the found type adapter
     */
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> findConverter(Class<T> type) {
        return (TypeAdapter<T>) this.typeAdapters.stream()
                .filter(converter -> converter.getType().equals(type))
                .findAny().orElse(null);
    }

    /**
     * Gets the bukkit commandmap
     *
     * @return the commandmap
     */
    private CommandMap getCommandMap() {
        CommandMap commandMap = null;

        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return commandMap;
    }
}