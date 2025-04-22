package conductance.api;

import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCBlocks {

	public static Supplier<? extends Block> CASING_STEEL;
	public static Supplier<? extends Block> CASING_INVAR;
	public static Supplier<? extends Block> CASING_ALUMINIUM;

	private NCBlocks() {
	}
}
