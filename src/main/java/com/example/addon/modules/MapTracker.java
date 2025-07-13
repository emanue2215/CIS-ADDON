package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapDecoration;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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

        ClientPlayerEntity player = mc.player;
        ItemStack offhand = player.getOffHandStack();

        if (!(offhand.getItem() instanceof FilledMapItem)) {
            ChatUtils.error("No filled map in offhand.");
            toggle();
            return;
        }

        World world = mc.world;
        MapState state = FilledMapItem.getMapState(offhand, world);
        if (state == null) {
            ChatUtils.error("Could not read map state.");
            toggle();
            return;
        }

        ChatUtils.info("=== Map Information ===");
        ChatUtils.info("Center: (" + state.getCenterX() + ", " + state.getCenterZ() + ")");
        ChatUtils.info("Scale: "  + state.getScale());
        ChatUtils.info("Locked: " + state.isLocked());

        // Decorations (entities/markers on map)
        Iterable<MapDecoration> decos = state.getDecorations();
        if (decos.iterator().hasNext()) {
            ChatUtils.info("Decorations:");
            for (MapDecoration dec : decos) {
                RegistryEntry<MapDecoration.Type> typeEntry = dec.type();
                // Mostrar el identificador del tipo
                Identifier id = typeEntry.getKey().getValue();
                ChatUtils.info(" - " + id + " @ (" + dec.x() + ", " + dec.z() + ")");
            }
        } else {
            ChatUtils.info("Decorations: none");
        }

        // Banners placed on map
        Map<UUID, MapState.MapBannerMarker> banners = state.getMarkers();
        if (!banners.isEmpty()) {
            ChatUtils.info("Banners:");
            for (MapState.MapBannerMarker banner : banners.values()) {
                BlockPos pos = banner.getPos();
                ChatUtils.info(" - Banner @ " + pos.toShortString());
            }
        } else {
            ChatUtils.info("Banners: none");
        }

        // Tracking position (item frame)
        BlockPos framePos = state.getTrackingPosition();
        if (framePos != null) {
            ChatUtils.info("Frame position: " + framePos.toShortString());
        } else {
            ChatUtils.info("Frame position: none");
        }

        toggle(); // Desactivar después de imprimir
    }
}

