package com.zorrilo197.cisaddon.modules;

import meteordevelopment.meteorclient.systems.modules.Module;
import static com.zorrilo197.cisaddon.CISAddon.CATEGORY;
import meteordevelopment.meteorclient.utils.player.ChatUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;

import java.util.OptionalInt;

public class MapTracker extends Module {
    public MapTracker() {
        super(CATEGORY, "map-tracker", "Displays the ID and scale of the map in your offhand.");
    }

    @Override
    public void onActivate() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;

        if (player == null || mc.world == null) {
            error("Player or world is null.");
            toggle();
            return;
        }

        ItemStack offhand = player.getOffHandStack();

        if (!(offhand.getItem() instanceof FilledMapItem)) {
            error("No filled map in offhand.");
            toggle();
            return;
        }

        NbtCompound tag = offhand.getNbt();
        if (tag == null || !tag.contains("map")) {
            error("Map NBT missing or invalid.");
            toggle();
            return;
        }

        OptionalInt mapIdOpt = FilledMapItem.getMapId(tag);
        if (mapIdOpt.isEmpty()) {
            error("Could not read map ID.");
            toggle();
            return;
        }

        int mapId = mapIdOpt.getAsInt();
        info("Map ID: " + mapId);

        MapState state = FilledMapItem.getMapState(offhand, mc.world);
        if (state != null) {
            info("Map scale: " + state.scale());
        } else {
            warning("Map state is null.");
        }

        toggle(); // Disable after one use
    }

    private void info(String message) {
        ChatUtils.info("[MapTracker] " + message);
    }

    private void error(String message) {
        ChatUtils.error("[MapTracker] " + message);
    }

    private void warning(String message) {
        ChatUtils.warning("[MapTracker] " + message);
    }
}
