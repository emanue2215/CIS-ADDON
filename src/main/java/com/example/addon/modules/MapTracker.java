package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapDecoration;
import net.minecraft.item.map.MapState;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.UUID;

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

        MapState state = FilledMapItem.getMapState(offhand, mc.world);
        if (state == null) {
            ChatUtils.error("Could not read map state.");
            toggle();
            return;
        }

        ChatUtils.info("=== Map Information ===");
        ChatUtils.info("Center: (" + state.getCenterX() + ", " + state.getCenterZ() + ")");
        ChatUtils.info("Scale: " + state.getScale());
        ChatUtils.info("Locked: " + state.isLocked());

        Map<String, MapDecoration> decos = state.getDecorations();
        if (!decos.isEmpty()) {
            ChatUtils.info("Decorations:");
            for (MapDecoration dec : decos.values()) {
                ChatUtils.info(" - " + dec.type().getName() + " @ (" + dec.x() + ", " + dec.z() + ")");
            }
        } else {
            ChatUtils.info("Decorations: none");
        }

        Map<UUID, MapState.MapBannerMarker> banners = state.getMarkers();
        if (!banners.isEmpty()) {
            ChatUtils.info("Banners:");
            for (MapState.MapBannerMarker marker : banners.values()) {
                BlockPos pos = marker.getPos();
                ChatUtils.info(" - Banner @ " + pos.toShortString());
            }
        } else {
            ChatUtils.info("Banners: none");
        }

        BlockPos framePos = state.getTrackingPosition();
        if (framePos != null) {
            ChatUtils.info("Frame position: " + framePos.toShortString());
        } else {
            ChatUtils.info("Frame position: none");
        }

        toggle(); // se desactiva después de mostrar
    }
}
