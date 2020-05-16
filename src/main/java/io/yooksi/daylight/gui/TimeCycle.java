package io.yooksi.daylight.gui;

import io.yooksi.cocolib.gui.Alignment;
import io.yooksi.cocolib.gui.GuiElement;
import io.yooksi.cocolib.gui.PlaneGeometry;
import io.yooksi.cocolib.gui.SpriteObject;
import io.yooksi.cocolib.lang.MathTools;
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

	/**
	 * Time cycle segments represent texture UV blocks that correspond to different
	 * times of day. The values here reflect {@link DayTime.Segment} values and
	 * contain data on how to render the time cycle depending on game time.
	 */
	public enum Segment {

		DAWN(152, 2),
		NOON(121, 0),
		DUSK(76, 1),
		MIDNIGHT(197, 1);

		/** Length of each texture segment in pixels. */
		public static final double LENGTH = 121D;

		/**
		 * Maximum amount of texture rows to expect. 0 represents the first row,
		 * and once the end of the final row is reached rendering will switch
		 * to row 0 to maintain a seamless animation loop.
		 */
		public static final int MAX_ROWS = 2;

		/**
		 * Height of each individual texture row.
		 * Used to calculate UV mapping coordinate on {@code y} axis.
		 */
		public static final int ROW_HEIGHT = 32;

		/**
		 * Length of each individual texture row.
		 * Used to calculate UV mapping coordinate on {@code x} axis.
		 * Once the sprite UV reaches the edge of the row it will switch to next row.
		 */
		public static final int ROW_LENGTH = 256;

		/**
		 * Length of the last texture row.
		 * Used in place of {@link #ROW_LENGTH} when on last row.
		 */
		public static final int LAST_ROW_LENGTH = 242;

		private static final Segment[] VALUES = values();

		/** UV mapping coordinates marking the start the segment. */
		final PlaneGeometry.Coordinates uv;

		/** Texture row the segment start is located on. */
		final int row;

		/**
		 * Amount of game ticks represented by the length of
		 * each pixel along {@code x} axis in each texture row.
		 */
		final int tpp;

		Segment(int u, int row) {

			this.row = MathTools.getValueInRange(row, 0, MAX_ROWS);
			uv = new PlaneGeometry.Coordinates(u, ROW_HEIGHT * this.row);
			/*
			 * Round value to nearest integer to get a smooth transition.
			 * If we are early it will look like we're traveling back through
			 * time when then frame repositions to fit the DayTime.Segment
			 */
			this.tpp = Math.round((float)(getTimeDuration() / LENGTH));
		}

		public int getRowLength() {
			return row == Segment.MAX_ROWS ? LAST_ROW_LENGTH : ROW_LENGTH;
		}

		public int getNextRow() {
			return row == 2 ? 0 : uv.y + ROW_HEIGHT;
		}

		/**
		 * @return {@code DayTime.Segment} associated with this segment.
		 */
		public DayTime.Segment getDayTimeSegment() {
			return DayTime.getSegments()[ordinal()];
		}

		/**
		 * @return duration of the associated day segment measured in game ticks.
		 * @see DayTime.Segment#getDuration()
		 */
		public long getTimeDuration() {
			return getDayTimeSegment().getDuration();
		}

		/**
		 * @param currentTime current day time in the world.
		 * @return amount of time elapsed from the defined start of the
		 * 		associated day segment to the given time.
		 *
		 * @see DayTime.Segment#getElapsedTime(long)
		 */
		public long getElapsedTime(long currentTime) {
			return getDayTimeSegment().getElapsedTime(currentTime);
		}

		/**
		 * @param time game time expressed in ticks.
		 * @return {@code TimeCycle.Segment} associated with the closest
		 * 		{@code DayTime.Segment} that matches the given time.
		 *
		 * @see DayTime.Segment#get(long)
		 */
		public static Segment get(long time) {
			return VALUES[DayTime.Segment.get(time).ordinal()];
		}

		public int getTicksPerPixel() {
			return tpp;
		}
	}

	private static final SpriteObject FRAME = SpriteObject.Builder.create(
			RLHelper.getTextureLocation(Daylight.MODID, "gui/time_cycle_frame.png"))
			.withPos(Alignment.TOP_RIGHT, 4, 4).withSize(91, 33).build();

	private final SpriteObject sprite;

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

		// Recalculate sprite UV mapping coordinates
		advanceTime(world);

		// Draw time cycle on game screen
		GuiElement.bindAndDrawTexture(sprite);
		GuiElement.bindAndDrawTexture(FRAME);
	}
}
