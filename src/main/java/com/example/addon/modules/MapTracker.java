package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;

public class MapTracker extends Module {
    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Shows the map origin coordinates in your offhand.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null || mc.world == null) {
            ChatUtils.error("Player or world not loaded.");
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

        ChatUtils.info("Map Origin: X=" + state.centerX + ", Z=" + state.centerZ);
        ChatUtils.info("Scale: " + state.scale);
        ChatUtils.info("Locked: " + state.locked);

        toggle(); // Disable after use
    }
}
