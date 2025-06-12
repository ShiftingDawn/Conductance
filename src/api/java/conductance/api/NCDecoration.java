package conductance.api;

import java.util.EnumMap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import com.tterrag.registrate.util.entry.BlockEntry;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCDecoration {

	public static final EnumMap<DyeColor, BlockEntry<? extends Block>> LUX = new EnumMap<>(DyeColor.class);
	public static final EnumMap<DyeColor, BlockEntry<? extends Block>> LIGHT_CIRCUIT = new EnumMap<>(DyeColor.class);
	public static final EnumMap<DyeColor, BlockEntry<? extends Block>> DARK_CIRCUIT = new EnumMap<>(DyeColor.class);

	public static final EnumMap<DyeColor, BlockEntry<? extends Block>> TILES_1 = new EnumMap<>(DyeColor.class);
	public static final EnumMap<DyeColor, BlockEntry<? extends Block>> TILES_2 = new EnumMap<>(DyeColor.class);
	public static final EnumMap<DyeColor, BlockEntry<? extends Block>> TILES_3 = new EnumMap<>(DyeColor.class);
	public static final EnumMap<DyeColor, BlockEntry<? extends Block>> TILES_4 = new EnumMap<>(DyeColor.class);
	public static final EnumMap<DyeColor, BlockEntry<? extends Block>> TILES_5 = new EnumMap<>(DyeColor.class);
	public static final EnumMap<DyeColor, BlockEntry<? extends Block>> TILES_6 = new EnumMap<>(DyeColor.class);

	public static BlockEntry<? extends Block> ME_BLOCK;
	public static BlockEntry<? extends Block> ME_BLOCK_ACTIVE;

	public static BlockEntry<? extends Block> CONCRETE_LIGHT;
	public static BlockEntry<? extends Block> CONCRETE_LIGHT_CRACKED;
	public static BlockEntry<? extends Block> CONCRETE_LIGHT_BRICKS;
	public static BlockEntry<? extends Block> CONCRETE_LIGHT_SMOOTH;
	public static BlockEntry<? extends Block> CONCRETE_LIGHT_BORDERED;
	public static BlockEntry<? extends Block> CONCRETE_DARK;
	public static BlockEntry<? extends Block> CONCRETE_DARK_CRACKED;
	public static BlockEntry<? extends Block> CONCRETE_DARK_BRICKS;
	public static BlockEntry<? extends Block> CONCRETE_DARK_SMOOTH;
	public static BlockEntry<? extends Block> CONCRETE_DARK_BORDERED;

	private NCDecoration() {
	}
}
