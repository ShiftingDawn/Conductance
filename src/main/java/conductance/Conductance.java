package conductance;

import net.minecraft.resources.ResourceLocation;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

@Mod(value = Conductance.MODID)
public final class Conductance {

	public static final String MODID = "conductance";
	public static final Logger LOGGER = LogUtils.getLogger();

	public Conductance(final IEventBus modEventBus, final ModContainer modContainer) {
		Conductance.LOGGER.info("Conductance is initializing on platform: NeoForge");
		modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	public static ResourceLocation id(final String path) {
		if (path.contains(":")) {
			return ResourceLocation.parse(path);
		} else {
			return ResourceLocation.fromNamespaceAndPath(Conductance.MODID, path);
		}
	}
}
