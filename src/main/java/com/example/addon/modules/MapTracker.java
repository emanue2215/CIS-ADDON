package com.example.addon.modules;

import com.example.addon.CISAddon;
import com.example.addon.mixin.MapStateAccessor;
import meteordevelopment.meteorclient.events.render.Render2DEvent;
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
        super(CISAddon.CATEGORY, "map-tracker", "Muestra el ID y coordenadas del mapa que sostienes (en cualquier mano).");
    }

    @EventHandler
    private void onRender2D(Render2DEvent event) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;

        // Obtener mapas de ambas manos
        ItemStack main = mc.player.getMainHandStack();
        ItemStack off = mc.player.getOffHandStack();

        ItemStack mapStack = null;
        boolean isMain = false;

        if (main.isOf(Items.FILLED_MAP)) {
            mapStack = main;
            isMain = true;
        } else if (off.isOf(Items.FILLED_MAP)) {
            mapStack = off;
            isMain = false;
        } else {
            error("No tienes un mapa en ninguna mano.");
            toggle();
            return;
        }

        MapIdComponent mapIdComponent = mapStack.get(DataComponentTypes.MAP_ID);
        if (mapIdComponent == null) {
            error("El mapa no tiene un ID válido.");
            toggle();
            return;
        }

        ClientWorld world = mc.world;
        MapState mapState = world.getMapState(mapIdComponent);
        if (mapState == null) {
            error("No se pudo obtener el estado del mapa.");
            toggle();
            return;
        }

        int centerX = ((MapStateAccessor) mapState).getCenterX();
        int centerZ = ((MapStateAccessor) mapState).getCenterZ();
        byte scale = ((MapStateAccessor) mapState).getScale();

        int realX = centerX * 128;
        int realZ = centerZ * 128;
        int areaSize = 128 * (1 << scale);

        info("Mapa detectado en la " + (isMain ? "mano principal" : "mano secundaria") + ".");
        info("Map ID: " + mapIdComponent.id());
        info("Map generated at approx coords: X=" + realX + ", Z=" + realZ);
        info("Map scale: " + scale + " (area size: " + areaSize + "x" + areaSize + ")");

        toggle();
    }
}
