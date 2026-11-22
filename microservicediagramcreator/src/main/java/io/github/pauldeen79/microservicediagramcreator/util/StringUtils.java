package io.github.pauldeen79.microservicediagramcreator.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {
    public static String toAlias(String input) {
        return input.replace("-", "_")
                    .replace(" ", "_");
    }
}
