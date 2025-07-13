package com.example.addon.modules;

import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.ModuleCategory;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

public class MapTracker extends Module {
    public MapTracker() {
        super(ModuleCategory.Misc, "map-tracker", "Displays the ID of the map in your offhand.");
    }

    @Override
    public void onActivate() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientPlayerEntity player = mc.player;

        if (player == null) {
            error("Player is null.");
            toggle();
            return;
        }

        ItemStack offhand = player.getOffHandStack();

        if (!(offhand.getItem() instanceof FilledMapItem)) {
            error("No map in offhand.");
            toggle();
            return;
        }

        int mapId = FilledMapItem.getMapId(offhand);
        info("Map ID: " + mapId);

        MapState state = FilledMapItem.getMapState(offhand, mc.world);
        if (state != null) {
            info("Map scale: " + state.scale);
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
