package io.github.nosequel.hcf.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    /**
     * Get the formatted string of an epoch time
     *
     * @param time the epoch time
     * @param trailing whether the time is trailing
     * @return the formatted time
     */
    public String getFormattedTime(long time, boolean trailing) {
        // terrible bad hack, like really terrible.
        // but I couldn't find a different way to do it with a long.
        String trailingFormat = new DecimalFormat("0.0").format(time);
        trailingFormat = trailingFormat.substring(trailingFormat.toCharArray()[0] == '0' ? 1 : 0, trailingFormat.length()-4);
        trailingFormat = trailingFormat.length() == 0 ? "0.0" : trailingFormat.length() == 1 ? "0." + trailingFormat.toCharArray()[0] : (trailingFormat.length() == 2 ? trailingFormat.toCharArray()[0] + "." + trailingFormat.toCharArray()[1] : trailingFormat.toCharArray()[0] + "" + trailingFormat.toCharArray()[1] + "." + trailingFormat.toCharArray()[2]);

        return trailing && time < 60000 ?  trailingFormat + "s":  (time > 600000 ? new SimpleDateFormat("hh:mm:ss").format(time) : new SimpleDateFormat("mm:ss").format(time));
    }
}