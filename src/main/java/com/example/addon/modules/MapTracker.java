package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapDecoration;
import net.minecraft.item.map.MapState;
import net.minecraft.util.math.BlockPos;

public class MapTracker extends Module {
    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Displays detailed info of the map in your offhand.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null || mc.world == null) {
            ChatUtils.error("Player or world is null.");
            toggle();
            return;
        }

        ItemStack offhand = mc.player.getOffHandStack();
        if (!(offhand.getItem() instanceof FilledMapItem)) {
            ChatUtils.error("No filled map in offhand.");
            toggle();
            return;
        }

        // MapState viene directamente de FilledMapItem
        MapState state = FilledMapItem.getMapState(offhand, mc.world);
        if (state == null) {
            ChatUtils.error("Could not read map state.");
            toggle();
            return;
        }

        ChatUtils.info("=== Map Information ===");
        ChatUtils.info("Center: (" + state.centerX + ", " + state.centerZ + ")");
        ChatUtils.info("Scale: " + state.scale);
        ChatUtils.info("Locked: " + state.locked);

        // Las decoraciones (markers/entities)
        if (!state.decorations.isEmpty()) {
            ChatUtils.info("Decorations:");
            for (Map.Entry<String, MapDecoration> e : state.decorations.entrySet()) {
                MapDecoration dec = e.getValue();
                ChatUtils.info(" - " + dec.type.name() + " @ (" + dec.x + ", " + dec.z + ")");
            }
        } else {
            ChatUtils.info("Decorations: none");
        }

        // Los banners (banderas colocadas)
        if (!state.bannerMarkers.isEmpty()) {
            ChatUtils.info("Banners:");
            for (Map.Entry<java.util.UUID, MapState.MapBannerMarker> e : state.bannerMarkers.entrySet()) {
                BlockPos pos = e.getValue().getPos();
                ChatUtils.info(" - Banner @ " + pos.toShortString());
            }
        } else {
            ChatUtils.info("Banners: none");
        }

        // La posición de seguimiento (item frame)
        BlockPos framePos = state.trackingPosition;
        if (framePos != null) {
            ChatUtils.info("Frame position: " + framePos.toShortString());
        } else {
            ChatUtils.info("Frame position: none");
        }

        toggle(); // se desactiva después de mostrar
    }
}

