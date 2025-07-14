package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;

public class MapTracker extends Module {
    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Shows the map origin coordinates in your offhand.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null || mc.world == null) {
            ChatUtils.error("Player or world not loaded.");
            toggle();
            return;
        }

        ItemStack offhand = mc.player.getOffHandStack();
        if (!(offhand.getItem() instanceof FilledMapItem)) {
            ChatUtils.error("No filled map in offhand.");
            toggle();
            return;
        }

        // 1) Intento estándar
        MapState state = FilledMapItem.getMapState(offhand, mc.world);

        // 2) Fallback: leer directamente NBT
        if (state == null) {
            NbtCompound nbt = offhand.getNbt();
            if (nbt != null) {
                state = MapState.fromNbt(nbt);
            }
        }

        // 3) Si sigue null, listamos claves NBT para diagnosticar
        if (state == null) {
            NbtCompound nbt = offhand.getNbt();
            ChatUtils.error("Could not read map state.");
            if (nbt != null) {
                ChatUtils.info("NBT keys present: " + nbt.getKeys());
            } else {
                ChatUtils.info("No NBT data found on the map.");
            }
            toggle();
            return;
        }

        // Si llegamos aquí, `state` ya es válido
        ChatUtils.info("Map Origin: X=" + state.centerX + ", Z=" + state.centerZ);
        ChatUtils.info("Scale: " + state.scale);
        ChatUtils.info("Locked: " + state.locked);

        toggle(); // Desactivar después de usar
    }
}
