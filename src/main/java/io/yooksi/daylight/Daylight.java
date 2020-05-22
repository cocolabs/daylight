/*
 *  Copyright (C) 2020 Matthew Cain
 *
 *  This file is part of Daylight.
 *
 *  Daylight is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Daylight. If not, see <https://www.gnu.org/licenses/>.
 */
package io.yooksi.daylight;

import io.yooksi.daylight.config.DaylightConfig;
import io.yooksi.daylight.gui.TimeCycle;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;

@Mod(Daylight.MODID)
public class Daylight {

    public static final String MODID = "daylight";

    public Daylight() {

        // Initialize mod logger
        DTLogger.init(LogManager.getLogger());

        // Initialize all time cycle types
        TimeCycle.initialize();

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register Mod configuration
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DaylightConfig.CLIENT_SPEC);
    }

    private void setup(final FMLCommonSetupEvent event) {

        // some preinit code
        DTLogger.info("Daytime pre-initialized");
    }

    /**
     * @return {@code ResourceLocation} pointing to provided path with {@code MODID} as namespace.
     */
    public static ResourceLocation getLocation(String path) {
        return new ResourceLocation(MODID, path);
    }
}
