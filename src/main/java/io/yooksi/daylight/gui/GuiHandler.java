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

	private static final TimeCycle TIME_CYCLE_DESERT = new TimeCycle(SpriteObject.Builder.create(
			RLHelper.getTextureLocation(Daylight.MODID, "gui/time_cycle_desert.png"))
			.withPos(Alignment.TOP_RIGHT, 5, 5).withSize(90, 32));

	@SubscribeEvent
	public void onPreRenderOverlay(RenderGameOverlayEvent.Pre event) {

			World world = Minecraft.getInstance().world;
			if (world != null)
			{
				TIME_CYCLE_DESERT.updateAndDraw(world);
			}
		}
	}
