package com.example.addon.mixin;

import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MapState.class)
public interface MapStateAccessor {
    @Accessor("centerX")
    int getCenterX();

    @Accessor("centerZ")
    int getCenterZ();
}
