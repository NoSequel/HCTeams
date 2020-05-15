package io.github.nosequel.hcf.util.command.adapter;

import io.github.nosequel.hcf.util.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public interface TypeAdapter<T> {

    /**
     * Convert a String to the type
     *
     * @param sender the executor
     * @param source the string to get converted
     * @return the converted type
     * @throws Exception calls the handleException method
     */
    T convert(CommandSender sender, String source) throws Exception;

    /**
     * Tab complete the String to the type
     *
     * @param sender the executor
     * @param source the string
     * @return the returned tab complete string list
     */
    default List<String> tabComplete(CommandSender sender, String source) {
        return Collections.emptyList();
    }

    /**
     * Handle a thrown exception
     *
     * @param sender the executor
     * @param source the provided string
     */
    default void handleException(CommandSender sender, String source) {
        sender.sendMessage(StringUtils.translate("&cError while parsing '" + source + "' with that type."));
    }

    Class<T> getType();

}
