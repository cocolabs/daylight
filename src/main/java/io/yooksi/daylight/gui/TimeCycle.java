package io.yooksi.daylight.gui;

import io.yooksi.cocolib.gui.Alignment;
import io.yooksi.cocolib.gui.GuiElement;
import io.yooksi.cocolib.gui.SpriteObject;
import io.yooksi.cocolib.util.DayTime;
import io.yooksi.cocolib.util.RLHelper;
import io.yooksi.daylight.Daylight;
import net.minecraft.world.World;

/**
 * This class controls the visual representation of the passage of time in Minecraft,
 * through a {@code GuiElement} that changes in a slideshow manner to inform the
 * player what time of day it is with different textures for different biomes.
 */
public class TimeCycle {







		}

		}

		}





	}

	private static final SpriteObject FRAME = SpriteObject.Builder.create(
			RLHelper.getTextureLocation(Daylight.MODID, "gui/time_cycle_frame.png"))
			.withPos(Alignment.TOP_RIGHT, 4, 4).withSize(91, 33).build();

	private final SpriteObject sprite;
	private long lastDayTime;

	public TimeCycle(SpriteObject.Builder builder) {
		this.sprite = builder.build();
	}

	/**
	 * Recalculate time cycle UV mapping coordinates in the given world.
	 * The calculation mainly depends on the current time in the
	 * world and will move the cycle in a seamless loop.
	 */
	void advanceTime(World world) {

		final long time = DayTime.getTimeOfDay(world);
		final int spriteWidth = sprite.getWidth();
		final Segment startSegment = Segment.get(time);
 
		// Calculate how much pixels UV has to be moved along X axis
		final int pixels = (int) Math.floor((float)(
				startSegment.getElapsedTime(time) / startSegment.tpp)
		);
		// Move UV for set pixel count and center by compensating for sprite width
		int u = startSegment.uv.x + pixels - spriteWidth / 2;
		int v = startSegment.uv.y;

		int uvEdge = u + spriteWidth;
		int rowLength = startSegment.getRowLength();

		// If UV mapping will fall outside texture bound move to next row
		if (uvEdge >= rowLength)
		{
			u = uvEdge - rowLength;
			v = startSegment.getNextRow();
		}
		sprite.getUV().update(u, v);
	}

	/**
	 * Update time cycle sprite UV mapping coordinates based on
	 * time in the given world and draw HUD on game screen.
	 */
	void updateAndDraw(World world) {

		final long currentDayTime = world.getDayTime();
		/*
		 * Recalculate sprite UV mapping coordinates.
		 * To be performance efficient, update only if
		 * world day time has changed since last update.
		 */
		if (lastDayTime != currentDayTime)
		{
			advanceTime(world);
			lastDayTime = currentDayTime;
		}
		// Draw time cycle on game screen
		GuiElement.bindAndDrawTexture(sprite);
		GuiElement.bindAndDrawTexture(FRAME);
	}
}
