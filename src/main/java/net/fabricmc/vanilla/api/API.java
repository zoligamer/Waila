package net.fabricmc.vanilla.api;

public class API {
    public static void registerHighlightHandler(IHighlightHandler handler, ItemInfo.Layout... layout) {
        ItemInfo.registerHighlightHandler(handler, layout);
    }
}
