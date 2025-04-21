package com.game.core.utils;

import java.net.URL;

public class ResourceUtils {
    public static URL getResource(String name) {
        return ResourceUtils.class.getResource(name);
    }
}
