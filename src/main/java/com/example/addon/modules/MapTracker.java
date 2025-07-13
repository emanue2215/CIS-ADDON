package com.zorrilo197.cisaddon.modules;

import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;

public MapTracker(Category category) {
    super(category, "map-tracker", "Displays detailed info of the map in your offhand.");
}

public class MapTracker extends Module {
    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Displays detailed info of the map in your offhand.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null || mc.world == null) {
            ChatUtils.info("World or player not loaded.");
            toggle();
            return;
        }

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
        ChatUtils.info("Center coordinates: X = " + state.getCenterX() + ", Z = " + state.getCenterZ());
        ChatUtils.info("Scale: " + state.getScale());
        RegistryKey<World> dimensionKey = state.getDimension();
        Identifier dimensionId = dimensionKey.getValue();
        ChatUtils.info("Dimension: " + dimensionId.toString());
        ChatUtils.info("Locked: " + state.isLocked());
        ChatUtils.info("Map ID: " + FilledMapItem.getMapId(item));

        if (!state.getIcons().isEmpty()) {
            ChatUtils.info("Icons:");
            for (Map.Entry<UUID, MapState.Icon> entry : state.getIcons().entrySet()) {
                MapState.Icon icon = entry.getValue();
                ChatUtils.info(" - Type: " + icon.type().asString() + ", X = " + icon.getX() + ", Z = " + icon.getZ());
            }
        } else {
            ChatUtils.info("Icons: None");
        }

        if (!state.getBanners().isEmpty()) {
            ChatUtils.info("Banners:");
            for (Map.Entry<BlockPos, String> banner : state.getBanners().entrySet()) {
                ChatUtils.info(" - " + banner.getValue() + " at " + banner.getKey().toShortString());
            }
        } else {
            ChatUtils.info("Banners: None");
        }

        if (state.getFramePos() != null) {
            ChatUtils.info("Item Frame Position: " + state.getFramePos().toShortString());
        } else {
            ChatUtils.info("Item Frame Position: None");
        }

        toggle(); // Disable module after showing info
    }
}
