package nl.appelgebakje22.conductance;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Conductance.MODID)
public class Conductance {

	public static final String MODID = "conductance";
	private static final Logger LOGGER = LogUtils.getLogger();

	public Conductance(IEventBus modEventBus, ModContainer modContainer) {
	}
}