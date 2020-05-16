package io.yooksi.daylight.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

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
			}
		}
	}
