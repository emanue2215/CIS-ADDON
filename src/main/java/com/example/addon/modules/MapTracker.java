package com.example.addon.modules;

import com.example.addon.CISAddon;

import meteordevelopment.meteorclient.events.render.Render2DEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;

public class MapTracker extends Module {
    public MapTracker() {
        super(CISAddon.CATEGORY, "map-tracker", "Muestra el ID y coordenadas del mapa que sostienes.");
    }

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        ItemStack stack = mc.player.getMainHandStack();
        if (!stack.isOf(Items.FILLED_MAP)) return;

        // Obtener el componente de ID del mapa
        MapIdComponent mapIdComponent = stack.get(DataComponentTypes.MAP_ID);
        if (mapIdComponent == null) return;

        // Obtener el estado del mapa usando el componente
        ClientWorld world = mc.world;
        MapState mapState = world.getMapState(mapIdComponent);
        if (mapState == null) return;
        toggle();
    }
}
