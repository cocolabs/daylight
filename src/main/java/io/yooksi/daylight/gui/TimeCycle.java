package io.yooksi.daylight.gui;

import com.google.common.collect.ImmutableList;
import io.yooksi.cocolib.gui.Alignment;
import io.yooksi.cocolib.gui.GuiElement;
import io.yooksi.cocolib.gui.SpriteObject;
import io.yooksi.cocolib.util.DayTime;
import io.yooksi.cocolib.util.RLHelper;
import io.yooksi.daylight.DTLogger;
import io.yooksi.daylight.Daylight;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static io.yooksi.cocolib.gui.PlaneGeometry.*;

/**
 * This class controls the visual representation of the passage of time in Minecraft,
 * through a {@code GuiElement} that changes in a slideshow manner to inform the
 * player what time of day it is with different textures for different biomes.
 */
public class TimeCycle extends SpriteObject {

	public enum Type {

		PLAIN("gui/time_cycle_plains.png", new Biome[] {
				Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS
		}),
		DESERT("gui/time_cycle_desert.png", new Biome[] {
				Biomes.DESERT, Biomes.DESERT_HILLS, Biomes.DESERT_LAKES
		}),
		SNOW("gui/time_cycle_snow.png", new Biome[] {
				Biomes.SNOWY_BEACH, Biomes.SNOWY_MOUNTAINS, Biomes.SNOWY_TAIGA,
				Biomes.SNOWY_TAIGA_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS,
				Biomes.SNOWY_TUNDRA, Biomes.FROZEN_RIVER
		});
		/*
		 * These static fields contain data for creating TimeCycle instances
		 */
		public static final Alignment DEFAULT_ALIGNMENT = Alignment.TOP_RIGHT;
		public static final Dimensions DEFAULT_OFFSET = new Dimensions(5, 5);
		public static final Dimensions DEFAULT_SIZE = new Dimensions(90, 32);

		private static final Type[] VALUES = Type.values();

		private final ResourceLocation location;
		private final ImmutableList<Biome> allowedBiomes;

		Type(String texturePath, Biome[] allowedBiomes) {

			location = RLHelper.getTextureLocation(Daylight.MODID, texturePath);
			this.allowedBiomes = ImmutableList.copyOf(allowedBiomes);
		}

		/**
		 * @return {@code ResourceLocation} associated with this type.
		 */
		public ResourceLocation getTextureLocation() {
			return location;
		}
	}
	private static final SpriteObject FRAME = SpriteObject.Builder.create(
			RLHelper.getTextureLocation(Daylight.MODID, "gui/time_cycle_frame.png"))
			.withPos(Alignment.TOP_RIGHT, Type.DEFAULT_OFFSET.getWidth() - 1,
					Type.DEFAULT_OFFSET.getHeight() - 1).withSize(91, 33).build();

	/** Contains all {@code TimeCycle} instances mapped to types. */
	private static Map<Type, TimeCycle> types;

	/**
	 * Immutable list of pre-defined allowed biomes. Attempting to change this
	 * list in any way will result in {@code UnsupportedOperationException}.
	 */
	private final ImmutableList<Biome> biomes;

	/** Last recorded {@code dayTime} from advancing time. */
	private long lastDayTime;

	/**
	 * @param allowedBiomes array of allowed biomes for this time cycle.
	 * @param builder data to build sprite from.
	 *
	 * @throws NullPointerException if any biome {@code element} is null
	 */
	private TimeCycle(Biome[] allowedBiomes, SpriteObject.Builder builder) {

		super(builder);
		biomes = ImmutableList.copyOf(allowedBiomes);
	}

	/**
	 * Initialize all {@code TimeCycle} types in an internal map.<br>
	 * Call this <b>only once</b> from mod initialization phase.
	 */
	public static void initialize() {

		if (types != null)
		{
			DTLogger.warn("Wanted to re-initialize time cycles, skipping...");
			return;
		}
		Map<Type, TimeCycle> cycles = new java.util.HashMap<>();
		for (Type type : Type.VALUES) {
			cycles.put(type, new TimeCycle(type.allowedBiomes.toArray(new Biome[]{}),
					SpriteObject.Builder.create(type.location).withPos(Type.DEFAULT_ALIGNMENT,
							Type.DEFAULT_OFFSET).withSize(Type.DEFAULT_SIZE)));
		}
		types = java.util.Collections.unmodifiableMap(new java.util.HashMap<>(cycles));
	}

	/**
	 * @return first registered {@code TimeCycle} instance that has the given biome
	 * 		listed as an allowed biome  with the given or {@code null} if none found.
	 */
	public static @Nullable TimeCycle getForBiome(Biome biome) {

		for (Type type : Type.VALUES)
		{
			for (Biome allowedBiome : type.allowedBiomes)
			{
				if (allowedBiome == biome)
					return types.get(type);
			}
		}
		return null;
	}

	/**
	 * Recalculate time cycle UV mapping coordinates in the given world.
	 * The calculation mainly depends on the current time in the
	 * world and will move the cycle in a seamless loop.
	 */
	void advanceTime(World world) {

		final long time = DayTime.getTimeOfDay(world);
		final int spriteWidth = getWidth();
		final TimeSegment startSegment = TimeSegment.get(time);
 
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
		getUV().update(u, v);
	}

	/**
	 * Update {@code TimeCycle} sprite alignment and offset with given arguments.
	 *
	 * @param alignment relative position on game screen.
	 * @param offset offset from position relative to alignment.
	 */
	public static void updatePosition(Alignment alignment, Dimensions offset) {

		for (Map.Entry<Type, TimeCycle> entry : types.entrySet())
		{
			TimeCycle cycle = entry.getValue();
			cycle.align(alignment, offset.getWidth(), offset.getHeight());
			cycle.updateScaledPosition(true);
		}
		// Don't forget to update the frame as well
		FRAME.align(alignment, offset.getWidth() - 1, offset.getHeight() - 1);
		FRAME.updateScaledPosition(true);
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
		GuiElement.bindAndDrawTexture(this);
		GuiElement.bindAndDrawTexture(FRAME);
	}

	/**
	 * @return {@code true} if the given biome is allowed for this {@code TimeCycle}.
	 */
	public boolean isAllowedBiome(Biome biome) {
		return biomes.contains(biome);
	}
}
