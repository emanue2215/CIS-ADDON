package com.example.addon.modules;

import com.example.addon.CISAddon;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.item.map.MapState;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import java.util.Optional;

public class MapTracker extends Module {
    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Reads map ID and shows map origin coordinates.");
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

        NbtCompound tag = getEncodedNbt(offhand);
        if (tag == null || tag.isEmpty()) {
            ChatUtils.error("No NBT data found on the map item.");
            toggle();
            return;
        }

        Optional<Integer> mapIdOpt = tag.getInt("map");
        if (mapIdOpt.isPresent()) {
            int mapId = mapIdOpt.get();
            ChatUtils.info("Map ID: " + mapId);

            MapState state = FilledMapItem.getMapState(offhand, mc.world);
            if (state != null) {
                int centerX = state.centerX;
                int centerZ = state.centerZ;
                int scale = state.scale;
                ChatUtils.info("Map center: X=" + centerX + ", Z=" + centerZ + ", scale=" + scale);
            } else {
                ChatUtils.warning("Could not read MapState.");
            }
        } else {
            ChatUtils.info("No 'map' tag found. Listing keys:");
            for (String key : tag.getKeys()) {
                ChatUtils.info("- " + key);
            }
        }

        ChatUtils.info("Raw NBT: " + tag.asString());
        toggle();
    }

    private static NbtCompound getEncodedNbt(ItemStack stack) {
        DynamicOps<NbtElement> ops = NbtOps.INSTANCE;
        DataResult<NbtElement> result = ComponentChanges.CODEC.encodeStart(ops, stack.getComponentChanges());
        return (NbtCompound) result.result().orElseGet(NbtCompound::new);
    }
}
