package com.game.core.utils;

import java.net.URL;

public final class ResourceUtils {
    private ResourceUtils() {}

    public static URL getResource(String name) {
        return ResourceUtils.class.getResource(name);
    }
}
