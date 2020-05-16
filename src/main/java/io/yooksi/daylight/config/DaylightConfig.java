package io.yooksi.daylight.config;

import io.yooksi.daylight.DTLogger;
import io.yooksi.daylight.Daylight;
import io.yooksi.daylight.gui.TimeCycle;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.yooksi.cocolib.gui.PlaneGeometry.*;

@Mod.EventBusSubscriber(modid = Daylight.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DaylightConfig {

	public static final ClientConfig CLIENT;
	public static final ForgeConfigSpec CLIENT_SPEC;

	static {
		final Pair<ClientConfig, ForgeConfigSpec> specPair =
				new ForgeConfigSpec.Builder().configure(ClientConfig::new);

		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	private static final Pattern OFFSET_PATTERN =
			Pattern.compile("\\[\\s*(\\d+)\\s*,\\s*(\\d+)\\s*]");

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {

		if (configEvent.getConfig().getSpec() == CLIENT_SPEC)
		{
			// Declare default values in case something goes wrong
			Dimensions guiOffset = TimeCycle.Type.DEFAULT_OFFSET;
			Matcher match = OFFSET_PATTERN.matcher(ClientConfig.guiOffset.get());
			if (match.find())
			{
				guiOffset = new Dimensions(
						Integer.parseInt(match.group(1)),
						Integer.parseInt(match.group(2))
				);
			}
			else DTLogger.error("Malformed config value 'guiOffset'");
		}
	}
}
