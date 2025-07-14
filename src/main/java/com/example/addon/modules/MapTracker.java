package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.events.packets.PacketEvent.Receive;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.network.packet.s2c.play.MapDataS2CPacket;
import net.minecraft.world.level.saveddata.maps.MapState;
import meteordevelopment.orbit.EventHandler;

/**
 * MapTracker module: intercepts MapDataS2CPacket and displays map information.
 */
public class MapTracker extends Module {
    private long lastMapId = -1;

    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Shows the map origin and state when map data arrives.");
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
        if (!(event.packet instanceof MapDataS2CPacket pkt)) return;

        MapState state = pkt.getData();
        long mapId = state.getMapId();

        // Avoid repeating same map info
        if (mapId == lastMapId) return;
        lastMapId = mapId;

        // Format and send info to chat
        String message = String.format(
            "[MapTracker] Map ID: %d | Origin: X=%d, Z=%d | Scale: %d | Locked: %b",
            mapId, state.xCenter, state.zCenter, state.scale, state.locked
        );
        ChatUtils.info(message);

        // Automatically disable after first output
        toggle();
    }
}
