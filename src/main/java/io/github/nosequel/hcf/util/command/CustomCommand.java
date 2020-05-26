package io.github.nosequel.hcf.util.command;

import io.github.nosequel.hcf.util.command.adapter.TypeAdapter;
import io.github.nosequel.hcf.util.command.data.CommandData;
import io.github.nosequel.hcf.util.command.data.SubcommandData;
import lombok.SneakyThrows;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomCommand extends Command {

    private final CommandData data;

    public CustomCommand(CommandData data) {
        super(data.getCommand().label());

        this.data = data;

        if (!data.getCommand().permission().isEmpty()) {
            this.setPermission(data.getCommand().permission());
        }

        if (data.getCommand().aliases().length > 0) {
            this.setAliases(Arrays.asList(data.getCommand().aliases()));
        }
    }

    @Override
    @SneakyThrows
    public boolean execute(CommandSender sender, String label, String[] passedParameters) {
        final String[] args;
        final Method method;
        final String permission;

        if (passedParameters.length >= 1 && !data.getSubcommands().isEmpty() && data.getSubcommands().stream().anyMatch(subcommand -> subcommand.getSubcommand().label().equalsIgnoreCase(passedParameters[0]) || Arrays.stream(subcommand.getSubcommand().aliases()).anyMatch(string -> string.equalsIgnoreCase(passedParameters[0])))) {
            final SubcommandData subcommand = Objects.requireNonNull(data.getSubcommands().stream()
                    .filter(subcommandData -> subcommandData.getSubcommand().label().equalsIgnoreCase(passedParameters[0]) || Arrays.stream(subcommandData.getSubcommand().aliases()).anyMatch(string -> string.equalsIgnoreCase(passedParameters[0])))
                    .findFirst().orElse(null));

            args = Arrays.copyOfRange(passedParameters, 1, passedParameters.length);
            method = subcommand.getMethod();
            permission = subcommand.getSubcommand().permission();
        } else {
            args = passedParameters;
            method = data.getMethod();
            permission = data.getCommand().permission();
        }

        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        final Parameter[] parameters = Arrays.copyOfRange(method.getParameters(), 1, method.getParameters().length);

        if (parameters.length > 0 && !parameters[0].getType().isArray()) {
            Object[] objects = new Object[parameters.length];

            if (passedParameters.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + " " + Arrays.stream(parameters).map(parameter1 -> "<" + parameter1.getName() + ">").collect(Collectors.joining(" ")));
                return true;
            }

            for (int i = 0; i < parameters.length; i++) {
                final Parameter parameter = parameters[i];
                final io.github.nosequel.hcf.util.command.annotation.Parameter param = parameter.getAnnotation(io.github.nosequel.hcf.util.command.annotation.Parameter.class);

                if (i >= args.length && (param == null || param.value().isEmpty())) {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + label + " " + Arrays.stream(parameters).map(parameter1 -> "<" + parameter1.getName() + ">").collect(Collectors.joining(" ")));
                    return true;
                } else if (param != null && !param.value().isEmpty() && i >= args.length) {
                    final TypeAdapter<?> typeAdapter = CommandController.getInstance().findConverter(parameter.getType());

                    if (typeAdapter != null) {
                        try {
                            objects[i] = typeAdapter.convert(sender, param.value());
                        } catch (Exception exception) {
                            typeAdapter.handleException(sender, param.value());
                        }
                    } else {
                        objects[i] = param.value();
                    }

                } else {
                    final TypeAdapter<?> typeAdapter = CommandController.getInstance().findConverter(parameter.getType());

                    if (typeAdapter == null) {
                        objects[i] = args[i];
                    } else {

                        try {
                            objects[i] = typeAdapter.convert(sender, args[i]);
                        } catch (Exception exception) {
                            typeAdapter.handleException(sender, args[i]);
                            return true;
                        }
                    }
                }
            }

            objects = ArrayUtils.add(objects, 0, method.getParameters()[0].getType().cast(sender));

            method.invoke(data.getCommandObject(), objects);
        } else if (parameters.length == 1 && parameters[0].getType().isArray()) {
            method.invoke(data.getCommandObject(), sender, args);
        } else {
            method.invoke(data.getCommandObject(), sender);
        }

        return false;
    }
}