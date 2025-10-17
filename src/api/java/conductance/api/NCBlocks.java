package conductance.api;

import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import conductance.api.coil.CoilBlockType;
import conductance.api.tier.Tier;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCBlocks {

	public static Map<Tier, Holder<Block>> MACHINE_CASING;

	public static Holder<Block> CASING_BRONZE;
	public static Holder<Block> CASING_STEEL;
	public static Holder<Block> CASING_INVAR;
	public static Holder<Block> CASING_ALUMINIUM;

	public static Holder<Block> CASING_BRONZE_FIREBOX;

	public static Map<CoilBlockType, Holder<Block>> COILS;

	public static Holder<Block> CREATIVE_TANK;

	private NCBlocks() {
	}
}
