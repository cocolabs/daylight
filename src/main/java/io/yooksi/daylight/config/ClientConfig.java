package io.yooksi.daylight.config;

import io.yooksi.daylight.Daylight;
import io.yooksi.daylight.gui.TimeCycle;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

	public static ForgeConfigSpec.ConfigValue<String> guiOffset;

	public ClientConfig(ForgeConfigSpec.Builder builder) {

		guiOffset = builder
				.comment("Defines GUI offset from edge of main window screen.\n" +
						"Format: [ <offsetX>, <offsetY> ] (i.e. [ 5, 5 ])")
				.translation("config." + Daylight.MODID + "guiOffset")
				.define("guiOffset", TimeCycle.Type.DEFAULT_OFFSET.toString());

		// Finish building configurations
		builder.build();
	}
}