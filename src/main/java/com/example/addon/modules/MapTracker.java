package com.zorrilo197.cisaddon.modules;

import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.systems.modules.Category;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class MapTracker extends Module {
    public MapTracker(Category category) {
        super(category, "map-tracker", "Displays detailed info of the map in your offhand.");
    }

    @Override
    public void onActivate() {
        ItemStack item = mc.player.getOffHandStack();

        if (item.isEmpty()) {
            ChatUtils.info("You are not holding any item in your offhand.");
            toggle();
            return;
        }

        if (!(item.getItem() instanceof FilledMapItem)) {
            ChatUtils.info("The item in your offhand is not a filled map.");
            toggle();
            return;
        }

        MapState state = FilledMapItem.getMapState(item, mc.world);
        if (state == null) {
            ChatUtils.info("Could not read the map state.");
            toggle();
            return;
        }

        ChatUtils.info("=== Map Information ===");
        ChatUtils.info("Center coordinates: X = " + state.centerX + ", Z = " + state.centerZ);
        ChatUtils.info("Scale: " + state.scale);
        ChatUtils.info("Dimension: " + state.dimension.getValue().toString());
        ChatUtils.info("Locked: " + state.locked);
        ChatUtils.info("Map ID: " + FilledMapItem.getMapId(item));

        // Icons (entities/markers on map)
        if (!state.icons.isEmpty()) {
            ChatUtils.info("Icons:");
            for (Map.Entry<String, net.minecraft.item.map.MapIcon> entry : state.icons.entrySet()) {
                ChatUtils.info(" - " + entry.getKey() + " at (" + entry.getValue().getX() + ", " + entry.getValue().getZ() + ")");
            }
        } else {
            ChatUtils.info("Icons: None");
        }

        // Banners
        if (!state.banners.isEmpty()) {
            ChatUtils.info("Banners:");
            state.banners.forEach((pos, name) -> {
                ChatUtils.info(" - " + name + " at " + pos.toShortString());
            });
        } else {
            ChatUtils.info("Banners: None");
        }

        // Item Frame position
        if (state.framePos != null) {
            ChatUtils.info("Item Frame Position: " + state.framePos.toShortString());
        } else {
            ChatUtils.info("Item Frame Position: None");
        }

        toggle(); // Disable module after showing info
    }
}
