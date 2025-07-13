package com.example.addon.modules;

import net.minecraft.world.item.FilledMapItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapState;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapBannerMarker;
import net.minecraft.core.BlockPos;
import com.example.addon.CISAddon;
import com.example.addon.utils.ChatUtils;

import java.util.Collection;

public class MapTracker extends Module {

    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Displays detailed info of the map in your offhand.");
    }

    @Override
    public void onTick() {
        ItemStack item = mc.player.getOffhandItem();

        if (item.isEmpty() || !(item.getItem() instanceof FilledMapItem)) {
            return;
        }

        // Obtener el id del mapa desde el NBT
        if (!item.hasTag()) {
            return;
        }
        MapState state = FilledMapItem.getSavedData(item, mc.level);

        if (state == null) {
            ChatUtils.info("No map data available.");
            return;
        }

        int mapId = FilledMapItem.getMapId(item.getTag());
        ChatUtils.info("Map ID: " + mapId);

        // Mostrar decoraciones del mapa
        Collection<MapDecoration> decorations = state.getDecorations();
        if (!decorations.isEmpty()) {
            ChatUtils.info("Map Decorations:");
            for (MapDecoration dec : decorations) {
                ChatUtils.info(" - Type: " + dec.type().toString() + ", X = " + dec.getX() + ", Z = " + dec.getZ());
            }
        }

        // Mostrar banners del mapa
        Collection<MapBannerMarker> banners = state.getBanners();
        if (!banners.isEmpty()) {
            ChatUtils.info("Map Banners:");
            for (MapBannerMarker marker : banners) {
                BlockPos pos = marker.pos();
                ChatUtils.info(" - Banner at " + pos.toShortString());
            }
        }

        // Posición del marco del mapa (si existe)
        if (state.framePos() != null) {
            ChatUtils.info("Item Frame Position: " + state.framePos().toShortString());
        }
    }
}
