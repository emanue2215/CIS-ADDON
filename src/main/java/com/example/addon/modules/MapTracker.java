package com.zorrilo197.cisaddon.modules;

import com.zorrilo197.cisaddon.CISAddon;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.FilledMapItem;

public class MapTracker extends Module {
    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Reads and displays NBT data of the map in your offhand.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null || mc.world == null) {
            ChatUtils.error("Player or world not loaded.");
            toggle();
            return;
        }

        ItemStack offhand = mc.player.getOffHandStack();
        if (offhand.isEmpty() || !(offhand.getItem() instanceof FilledMapItem)) {
            ChatUtils.error("No filled map in offhand.");
            toggle();
            return;
        }

        NbtCompound tag = offhand.getNbt();
        if (tag == null || tag.isEmpty()) {
            ChatUtils.error("No NBT data found on the map item.");
            toggle();
            return;
        }

        if (tag.contains("map")) {
            int mapId = tag.getInt("map").orElse(-1);
            ChatUtils.info("Map ID: " + mapId);
        } else {
            ChatUtils.info("No 'map' tag found. Listing all keys:");
        }

        for (String key : tag.getKeys()) {
            ChatUtils.info("- " + key);
        }

        ChatUtils.info("Raw NBT: " + tag);

        toggle();
    }
}
