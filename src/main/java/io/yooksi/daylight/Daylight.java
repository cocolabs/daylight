package io.yooksi.daylight;

import io.yooksi.daylight.gui.GuiHandler;
import io.yooksi.daylight.gui.TimeCycle;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;

@Mod(Daylight.MODID)
public class Daylight {

    public static final String MODID = "daylight";

    public Daylight() {

        // Initialize mod logger
        DTLogger.init(LogManager.getLogger());

        // Initialize all time cycle types
        TimeCycle.initialize();

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register GuiHandler for events
        MinecraftForge.EVENT_BUS.register(new GuiHandler());
    }

    private void setup(final FMLCommonSetupEvent event) {

        // some preinit code
        DTLogger.info("Daytime pre-initialized");
    }

    /**
     * @return {@code ResourceLocation} pointing to provided path with {@code MODID} as namespace.
     */
    public static ResourceLocation getLocation(String path) {
        return new ResourceLocation(MODID, path);
    }
}
