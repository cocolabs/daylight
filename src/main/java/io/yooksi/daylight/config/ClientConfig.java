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
package io.yooksi.daylight.config;

import io.yooksi.cocolib.gui.Alignment;
import io.yooksi.daylight.Daylight;
import io.yooksi.daylight.gui.TimeCycle;
import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

	public static ForgeConfigSpec.ConfigValue<String> guiAlignment;
	public static ForgeConfigSpec.ConfigValue<String> guiOffset;

	public ClientConfig(ForgeConfigSpec.Builder builder) {

		guiAlignment = builder
				.comment("Defines GUI alignment on main window screen.\n" +
						"Allowed Values: TOP_LEFT, TOP_RIGHT, TOP_CENTER, BOTTOM_LEFT, BOTTOM_RIGHT")
				.translation("config." + Daylight.MODID + "guiAlignment")
				.define("guiAlignment", TimeCycle.Type.DEFAULT_ALIGNMENT.toString());

		guiOffset = builder
				.comment("Defines GUI offset from edge of main window screen.\n" +
						"Format: [ <offsetX>, <offsetY> ] (i.e. [ 5, 5 ])")
				.translation("config." + Daylight.MODID + "guiOffset")
				.define("guiOffset", TimeCycle.Type.DEFAULT_OFFSET.toString());

		// Finish building configurations
		builder.build();
	}
}