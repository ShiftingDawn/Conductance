package conductance.api;

import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;
import com.tterrag.registrate.util.entry.BlockEntry;
import conductance.api.tier.Tier;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCBlocks {

	public static Map<Tier, BlockEntry<? extends Block>> MACHINE_CASING;

	public static Supplier<? extends Block> CASING_STEEL;
	public static Supplier<? extends Block> CASING_INVAR;
	public static Supplier<? extends Block> CASING_ALUMINIUM;

	private NCBlocks() {
	}
}
