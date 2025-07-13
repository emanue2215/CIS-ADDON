package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.item.map.MapDecoration;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
        ChatUtils.info("Center coordinates: X = " + state.centerX + ", Z = " + state.centerZ);
        ChatUtils.info("Scale: " + state.scale);
        RegistryKey<World> dimensionKey = state.dimension;
        Identifier dimensionId = dimensionKey.getValue();
        ChatUtils.info("Dimension: " + dimensionId);
        ChatUtils.info("Locked: " + state.locked);

        int mapId = FilledMapItem.getMapId(item.getNbt());
        ChatUtils.info("Map ID: " + mapId);

        boolean hasDecorations = state.getDecorations().iterator().hasNext();
        if (hasDecorations) {
            ChatUtils.info("Icons:");
            for (MapDecoration dec : state.getDecorations()) {
                ChatUtils.info(" - Type: " + dec.type().value().toString() + ", X = " + dec.x() + ", Z = " + dec.z());
            }
        } else {
            ChatUtils.info("Icons: None");
        }

        if (!state.getBanners().isEmpty()) {
            ChatUtils.info("Banners:");
            for (MapBannerMarker marker : state.getBanners().values()) {
                BlockPos pos = marker.getPos();
                ChatUtils.info(" - Banner at " + pos.toShortString());
            }
        } else {
            ChatUtils.info("Banners: None");
        }

        if (state.getFramePos() != null) {
            ChatUtils.info("Item Frame Position: " + state.getFramePos().toShortString());
        } else {
            ChatUtils.info("Item Frame Position: None");
        }

        toggle();
    }
}

