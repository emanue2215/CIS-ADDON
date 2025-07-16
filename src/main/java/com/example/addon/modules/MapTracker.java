package com.example.addon.modules;

import com.example.addon.CISAddon;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

public class MapTracker extends Module {
    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Reads full map NBT and prints it.");
    }

    @Override
    public void onActivate() {
        if (mc.player == null || mc.world == null) {
            ChatUtils.error("Player or world not loaded.");
            toggle();
            return;
        }

        ItemStack stack = mc.player.getOffHandStack();
        if (stack.isEmpty() || !(stack.getItem() instanceof FilledMapItem)) {
            ChatUtils.error("No filled map in offhand.");
            toggle();
            return;
        }

        NbtCompound nbt = getEncodedNbt(stack);
        if (nbt == null || nbt.isEmpty()) {
            ChatUtils.warning("No NBT found on item.");
            toggle();
            return;
        }

        ChatUtils.info("§e--- Full NBT Dump ---");
        dumpNbt(nbt, 0);
        toggle();
    }

    private void dumpNbt(NbtElement element, int indent) {
        String prefix = " ".repeat(indent * 2);

        switch (element) {
          case NbtCompound compound -> {
              for (String key : compound.getKeys()) {
                  NbtElement value = compound.get(key);
                  if (value instanceof NbtCompound || value instanceof NbtList) {
                      ChatUtils.info(prefix + "§b" + key + ":");
                      dumpNbt(value, indent + 1);
                  } else {
                      ChatUtils.info(prefix + "§b" + key + ": §f" + value.asString());
                  }
              }
          }
          case NbtList list -> {
              for (int i = 0; i < list.size(); i++) {
                  NbtElement entry = list.get(i);
                  ChatUtils.info(prefix + "§7- [" + i + "]");
                  dumpNbt(entry, indent + 1);
              }
          }
          default -> {
              ChatUtils.info(prefix + "§f" + element.asString());
          }
        }
    }

    private static NbtCompound getEncodedNbt(ItemStack stack) {
        DynamicOps<NbtElement> ops = NbtOps.INSTANCE;
        DataResult<NbtElement> result = ComponentChanges.CODEC.encodeStart(ops, stack.getComponentChanges());
        return (NbtCompound) result.result().orElseGet(NbtCompound::new);
    }
}
