package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

/**
 * MapTracker module: listens on the "minecraft:map_data" channel via Fabric API and displays map info.
 */
@Environment(EnvType.CLIENT)
public class MapTracker extends Module {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Identifier MAP_DATA_CHANNEL = new Identifier("minecraft", "map_data");
    private long lastMapId = -1;

    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Shows the map origin and scale when map data arrives.");
    }

    @Override
    public void onActivate() {
        lastMapId = -1;
        ChatUtils.info("[MapTracker] Awaiting map data...");
        // Register channel listener
        ClientPlayNetworking.registerGlobalReceiver(MAP_DATA_CHANNEL, this::onMapData);
    }

    @Override
    public void onDeactivate() {
        // Cannot unregister Fabric API receivers easily; rely on module disabling logic.
    }

    private void onMapData(net.minecraft.client.network.ClientPlayNetworkHandler handler, PacketByteBuf buf, net.minecraft.network.listener.ClientPlayPacketListener responseSender) {
        try {
            NbtCompound nbt = buf.readNbt();
            if (nbt == null) {
                ChatUtils.error("[MapTracker] No NBT data in map_data packet.");
                toggle();
                return;
            }

            int xCenter = nbt.getInt("xCenter");
            int zCenter = nbt.getInt("zCenter");
            int scale = nbt.getInt("scale");
            boolean locked = nbt.getBoolean("locked");
            long mapId = nbt.contains("mapId") ? nbt.getLong("mapId") : -1;

            if (mapId != -1 && mapId == lastMapId) return;
            lastMapId = mapId;

            String message = String.format(
                "[MapTracker] Map ID: %d | Origin: X=%d, Z=%d | Scale: %d | Locked: %b",
                mapId, xCenter, zCenter, scale, locked
            );
            ChatUtils.info(message);

        } catch (Exception e) {
            LOGGER.error("Error reading map_data packet", e);
        } finally {
            // Disable module after first read
            toggle();
        }
    }
}
