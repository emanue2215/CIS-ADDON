package com.zorrilo197.cisaddon;

import com.zorrilo197.cisaddon.modules.BoatUAV;
import com.zorrilo197.cisaddon.modules.MapTracker;
import com.zorrilo197.cisaddon.commands.CommandExample;
import com.zorrilo197.cisaddon.hud.HudExample;
import com.zorrilo197.cisaddon.modules.ModuleExample;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class CISAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("CIS");
    public static final HudGroup HUD_GROUP = new HudGroup("CIS");

    @Override
    public void onInitialize() {
        LOG.info("Initializing CIS Addon");

        // Modules
        Modules.get().add(new ModuleExample());
Modules.get().add(new MapTracker(CATEGORY));
Modules.get().add(new BoatUAV(CATEGORY));

        // Commands
        Commands.add(new CommandExample());

        // HUD
        Hud.get().register(HudExample.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.zorrilo197.cisaddon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("emanue2215", "CIS-ADDON");
    }
}
