package conductance;

import net.minecraft.resources.ResourceLocation;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import conductance.api.CAPI;
import conductance.client.ClientProxy;
import conductance.core.CommonProxy;

@Mod(value = Conductance.MODID)
public final class Conductance {

	public static final String MODID = CAPI.MOD_ID;
	public static final Logger LOGGER = LogUtils.getLogger();

	public Conductance(final IEventBus modEventBus, final ModContainer modContainer) {
		Conductance.LOGGER.info("Conductance is initializing on platform: NeoForge");
		this.modLoad(modEventBus, modContainer);
	}

	private void modLoad(final IEventBus modEventBus, final ModContainer modContainer) {
		CommonProxy.init(modEventBus);
		if (CAPI.isClient()) {
			ClientProxy.init(modEventBus);
		}

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
		if (CAPI.isClient()) {
			modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
		}
	}

	public static ResourceLocation id(final String path) {
		return ResourceLocation.fromNamespaceAndPath(Conductance.MODID, path);
	}
}
