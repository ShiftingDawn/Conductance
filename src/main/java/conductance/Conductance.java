package conductance;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import conductance.api.CAPI;
import conductance.core.CommonProxy;

public abstract class Conductance {

	public static final String MODID = CAPI.MOD_ID;
	public static final Logger LOGGER = LogUtils.getLogger();

	public static CommonProxy PROXY;

	protected Conductance(final IEventBus modEventBus, final ModContainer modContainer) {
		Conductance.PROXY = this.createSidedProxy();

		Conductance.LOGGER.info("Conductance is initializing on platform: NeoForge");
		Conductance.PROXY.init(modEventBus);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	protected abstract CommonProxy createSidedProxy();

	public static ResourceLocation id(final String path) {
		return ResourceLocation.fromNamespaceAndPath(Conductance.MODID, path);
	}
}
