package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.FilledMapItem;
import meteordevelopment.orbit.EventHandler;

/**
 * MapTracker module: reads NBT data of the map in offhand and displays relevant information.
 */
public class MapTracker extends Module {
    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Reads and displays NBT data of the map in your offhand.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null) {
            ChatUtils.error("Player not loaded.");
            toggle();
            return;
        }

        ItemStack offhand = mc.player.getOffHandStack();
        if (offhand.isEmpty() || !(offhand.getItem() instanceof FilledMapItem)) {
            ChatUtils.error("No filled map in offhand.");
            toggle();
            return;
        }

        NbtCompound nbt = offhand.getNbt();
        if (nbt == null) {
            ChatUtils.error("No NBT data found on the map item.");
            toggle();
            return;
        }

        // Display map ID if present
        if (nbt.contains("map")) {
            int mapId = nbt.getInt("map");
            ChatUtils.info(String.format("Map ID: %d", mapId));
        } else {
            ChatUtils.info("No 'map' tag found. Listing all keys:");
        }

        // List all keys and their types
        for (String key : nbt.getKeys()) {
            ChatUtils.info(String.format("- %s: %s", key, nbt.get(key).getType().getName()));
        }

        // Optionally show full raw NBT
        ChatUtils.info("Raw NBT: " + nbt);

        toggle(); // disable after reading
    }
}
