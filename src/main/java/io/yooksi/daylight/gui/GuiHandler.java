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

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Mod.EventBusSubscriber
public class GuiHandler {

	@SubscribeEvent
	public void onPreRenderOverlay(RenderGameOverlayEvent.Pre event) {

			@Nullable World world = Minecraft.getInstance().world;
			@Nullable ClientPlayerEntity player = Minecraft.getInstance().player;

			if (world != null && player != null)
			{
				Biome biome = world.getBiome(player.getPosition());
				TimeCycle cycle = TimeCycle.getForBiome(biome);

				if (cycle != null) {
					cycle.updateAndDraw(world);
				}
				// Default to Plains biome for everything else
				else Objects.requireNonNull(TimeCycle.getForBiome(Biomes.PLAINS)).updateAndDraw(world);
			}
		}
	}
