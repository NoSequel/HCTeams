package io.github.nosequel.hcf.util;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class StringUtils {

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
