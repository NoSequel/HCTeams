package io.github.nosequel.hcf.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtils {

    /**
     * Replace a string with a HashMap
     *
     * @param string the string
     * @param map the replacements
     * @return the replaced string
     */
    public String replace(String string, Map<String, String> map) {
        for(Map.Entry<String, String> entry : map.entrySet()) {
            string = string.replace(entry.getKey(), entry.getValue());
        }

        return string;
    }

    /**
     * Translate a string to a Bukkit color code compatible string.
     *
     * @param string the string
     * @return the translated string
     */
    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string).replace("\n", "\n");
    }

    /**
     * Get a String from a List<T>
     *
     * @param list the list
     * @return the string
     */
    public String fromList(List<?> list) {
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.toList()).toString();
    }
}
