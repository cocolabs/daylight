package io.yooksi.daylight.gui;

import io.yooksi.cocolib.gui.Alignment;
import io.yooksi.cocolib.gui.SpriteObject;
import io.yooksi.cocolib.util.RLHelper;
import io.yooksi.daylight.Daylight;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GuiHandler {


	@SubscribeEvent
	public void onPreRenderOverlay(RenderGameOverlayEvent.Pre event) {

			World world = Minecraft.getInstance().world;
			if (world != null)
			{
			}
		}
	}
