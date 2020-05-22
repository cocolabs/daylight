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
package io.yooksi.daylight.gui;

import io.yooksi.daylight.Daylight;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(modid = Daylight.MODID)
public class GuiHandler {

	@SubscribeEvent
	public static void onPreRenderOverlay(RenderGameOverlayEvent.Pre event) {

		// Render along with ALL other other HUD elements
		// otherwise we risk our GUI element rendering multiple times
		if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		@Nullable World world = Minecraft.getInstance().world;
		@Nullable ClientPlayerEntity player = Minecraft.getInstance().player;

		if (world != null && player != null)
		{
			Biome biome = world.getBiome(player.getPosition());
			TimeCycle cycle = TimeCycle.getForBiome(biome);

			if (cycle != null) {
				cycle.updateAndDraw(world);
			}
			// Use default instance for everything else
			else TimeCycle.getDefault().updateAndDraw(world);
		}
	}
}
