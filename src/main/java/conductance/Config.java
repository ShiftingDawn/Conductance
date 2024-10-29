package conductance;

import conductance.api.CAPI;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = CAPI.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	static final ModConfigSpec SPEC = Config.BUILDER.build();

	@SubscribeEvent
	private static void onLoad(final ModConfigEvent event) {
	}
}