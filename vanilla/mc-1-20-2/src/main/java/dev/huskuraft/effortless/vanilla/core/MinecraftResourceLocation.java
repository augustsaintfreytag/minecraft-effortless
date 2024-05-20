package dev.huskuraft.effortless.vanilla.core;

import dev.huskuraft.effortless.api.core.ResourceLocation;

public record MinecraftResourceLocation(
        net.minecraft.resources.ResourceLocation refs
) implements ResourceLocation {

    public static ResourceLocation ofNullable(net.minecraft.resources.ResourceLocation reference) {
        return reference == null ? null : new MinecraftResourceLocation(reference);
    }

    @Override
    public String getNamespace() {
        return refs.getNamespace();
    }

    @Override
    public String getPath() {
        return refs.getPath();
    }

    @Override
    public String toString() {
        return getNamespace() + ":" + getPath();
    }
}
