package conductance.lib.pipenet;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.material.Material;
import conductance.Conductance;
import conductance.init.block.WireBlock;

@EventBusSubscriber
public final class WireRegistry {

	private static final Table<WireType, Material, WireBlock> REGISTRY = HashBasedTable.create();
	private static boolean frozen = false;

	public static void register(final WireType type, final Material material, final WireBlock wireBlock) {
		if (WireRegistry.frozen) {
			throw new IllegalStateException("Trying to register wire in frozen WireRegistry!");
		}
		WireRegistry.REGISTRY.put(type, material, wireBlock);
	}

	public static @UnknownNullability WireBlock getWire(final WireType type, final Material material) {
		return WireRegistry.REGISTRY.get(type, material);
	}

	public static WireBlock[] getAllBlocks() {
		return WireRegistry.REGISTRY.values().toArray(WireBlock[]::new);
	}

	@SubscribeEvent
	private static void onLoadComplete(final FMLLoadCompleteEvent ignored) {
		Conductance.LOGGER.info("WireRegistry has been frozen!");
		WireRegistry.frozen = true;
	}

	private WireRegistry() {
	}
}
