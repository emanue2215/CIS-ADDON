package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import meteordevelopment.orbit.EventHandler;

/**
 * MapTracker module: intercepts map data packets and displays map information.
 */
public class MapTracker extends Module {
    private static final Identifier MAP_DATA_CHANNEL = new Identifier("minecraft", "map_data");
    private long lastMapId = -1;

    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Shows the map origin and scale when map data arrives.");
    }

    @Override
    public void onActivate() {
        lastMapId = -1;
        ChatUtils.info("[MapTracker] Awaiting map data...");
    }

    @Override
    public void onDeactivate() {
        // No cleanup needed
    }

    @EventHandler
    private void onPacketReceive(Receive event) {
        if (!(event.packet instanceof CustomPayloadS2CPacket pkt)) return;
        if (!MAP_DATA_CHANNEL.equals(pkt.getIdentifier())) return;

        CompoundTag nbt = pkt.getData();
        int xCenter = nbt.getInt("xCenter");
        int zCenter = nbt.getInt("zCenter");
        int scale = nbt.getInt("scale");
        boolean locked = nbt.getBoolean("locked");
        long mapId = nbt.getLong("mapId"); // if present

        // Avoid repeating same map info
        if (mapId == lastMapId) return;
        lastMapId = mapId;

        String message = String.format(
            "[MapTracker] Map ID: %d | Origin: X=%d, Z=%d | Scale: %d | Locked: %b",
            mapId, xCenter, zCenter, scale, locked
        );
        ChatUtils.info(message);

        toggle();
    }
}
