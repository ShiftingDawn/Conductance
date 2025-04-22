package conductance.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.block.ConcreteBlock;
import conductance.block.DecoEmissiveBlock;
import conductance.block.DecoLuxBlock;
import conductance.block.DecoSimpleBlock;
import conductance.block.DecoTintedBlock;
import conductance.core.apiimpl.ApiBridge;
import conductance.item.RenderedBlockItem;
import static conductance.api.NCDecoration.CIRCUIT;
import static conductance.api.NCDecoration.CONCRETE_DARK;
import static conductance.api.NCDecoration.CONCRETE_DARK_BORDERED;
import static conductance.api.NCDecoration.CONCRETE_DARK_BRICKS;
import static conductance.api.NCDecoration.CONCRETE_DARK_CRACKED;
import static conductance.api.NCDecoration.CONCRETE_DARK_SMOOTH;
import static conductance.api.NCDecoration.CONCRETE_LIGHT;
import static conductance.api.NCDecoration.CONCRETE_LIGHT_BORDERED;
import static conductance.api.NCDecoration.CONCRETE_LIGHT_BRICKS;
import static conductance.api.NCDecoration.CONCRETE_LIGHT_CRACKED;
import static conductance.api.NCDecoration.CONCRETE_LIGHT_SMOOTH;
import static conductance.api.NCDecoration.LUX;
import static conductance.api.NCDecoration.ME_BLOCK;
import static conductance.api.NCDecoration.ME_BLOCK_ACTIVE;
import static conductance.api.NCDecoration.TILES_1;
import static conductance.api.NCDecoration.TILES_2;
import static conductance.api.NCDecoration.TILES_3;
import static conductance.api.NCDecoration.TILES_4;
import static conductance.api.NCDecoration.TILES_5;
import static conductance.api.NCDecoration.TILES_6;

@SuppressWarnings("removal")
public final class ConductanceDecoration {

	public static void init() {
		for (final DyeColor dyeColor : DyeColor.values()) {
			LUX.put(dyeColor, ApiBridge.getRegistrate().block("%s_lux_block".formatted(dyeColor.getSerializedName()), props -> new DecoLuxBlock(props, dyeColor))
					.initialProperties(() -> Blocks.IRON_BLOCK)
					.properties(props -> props.emissiveRendering((blockState, blockGetter, blockPos) -> true))
					.blockstate(NonNullBiConsumer.noop())
					.item(RenderedBlockItem::new)
					.model(NonNullBiConsumer.noop())
					.build()
					.register());
			final int color = CAPI.COLORS.get(dyeColor);
			CIRCUIT.put(dyeColor, ApiBridge.getRegistrate().block("%s_circuit_block".formatted(dyeColor.getSerializedName()), props -> new DecoEmissiveBlock(props, "circuit"))
					.initialProperties(() -> Blocks.IRON_BLOCK)
					.properties(props -> props.emissiveRendering((blockState, blockGetter, blockPos) -> true))
					.blockstate(NonNullBiConsumer.noop())
					.addLayer(() -> RenderType::cutoutMipped)
					.color(() -> () -> (blockState, blockAndTintGetter, blockPos, i) -> i == 1 ? color : -1)
					.item(RenderedBlockItem::new)
					.color(() -> () -> (itemStack, i) -> i == 1 ? color : -1)
					.model(NonNullBiConsumer.noop())
					.build()
					.register());
			TILES_1.put(dyeColor, ConductanceDecoration.decoBlockTinted(dyeColor, "%s_tiles_1_block", "tiles/1"));
			TILES_2.put(dyeColor, ConductanceDecoration.decoBlockTinted(dyeColor, "%s_tiles_2_block", "tiles/2"));
			TILES_3.put(dyeColor, ConductanceDecoration.decoBlockTinted(dyeColor, "%s_tiles_3_block", "tiles/3"));
			TILES_4.put(dyeColor, ConductanceDecoration.decoBlockTinted(dyeColor, "%s_tiles_4_block", "tiles/4"));
			TILES_5.put(dyeColor, ConductanceDecoration.decoBlockTinted(dyeColor, "%s_tiles_5_block", "tiles/5"));
			TILES_6.put(dyeColor, ConductanceDecoration.decoBlockTinted(dyeColor, "%s_tiles_6_block", "tiles/6"));
		}

		CAPI.translations().makeLocalizedName("block.conductance.me_block", () -> "ME Block"); //Force ME capitalized
		ME_BLOCK = ConductanceDecoration.decoBlock("me_block", "me");
		CAPI.translations().makeLocalizedName("block.conductance.active_me_block", () -> "Active ME Block"); //Force ME capitalized
		ME_BLOCK_ACTIVE = ConductanceDecoration.decoBlock("active_me_block", "me_active");

		CONCRETE_LIGHT = ConductanceDecoration.concrete("light_concrete", "light");
		CONCRETE_LIGHT_CRACKED = ConductanceDecoration.concrete("cracked_light_concrete", "light_cracked");
		CONCRETE_LIGHT_BRICKS = ConductanceDecoration.concrete("light_concrete_bricks", "light_bricks");
		CONCRETE_LIGHT_SMOOTH = ConductanceDecoration.concrete("smooth_light_concrete", "light_smooth");
		CONCRETE_LIGHT_BORDERED = ConductanceDecoration.concrete("bordered_light_concrete", "light_bordered");
		CONCRETE_DARK = ConductanceDecoration.concrete("dark_concrete", "dark");
		CONCRETE_DARK_CRACKED = ConductanceDecoration.concrete("cracked_dark_concrete", "dark_cracked");
		CONCRETE_DARK_BRICKS = ConductanceDecoration.concrete("dark_concrete_bricks", "dark_bricks");
		CONCRETE_DARK_SMOOTH = ConductanceDecoration.concrete("smooth_dark_concrete", "dark_smooth");
		CONCRETE_DARK_BORDERED = ConductanceDecoration.concrete("bordered_dark_concrete", "dark_bordered");
	}

	private static BlockEntry<DecoSimpleBlock> decoBlock(final String name, final String textureName) {
		return ApiBridge.getRegistrate().block(name, props -> new DecoSimpleBlock(props, "decoration/" + textureName))
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.blockstate(NonNullBiConsumer.noop())
				.item(RenderedBlockItem::new)
				.model(NonNullBiConsumer.noop())
				.build()
				.register();
	}

	private static BlockEntry<DecoTintedBlock> decoBlockTinted(final DyeColor dyeColor, final String name, final String textureName) {
		final int color = CAPI.COLORS.get(dyeColor);
		return ApiBridge.getRegistrate().block(name.formatted(dyeColor.getSerializedName()), props -> new DecoTintedBlock(props, "decoration/" + textureName))
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.blockstate(NonNullBiConsumer.noop())
				.color(() -> () -> (blockState, blockAndTintGetter, blockPos, i) -> i == 1 ? color : -1)
				.item(RenderedBlockItem::new)
				.color(() -> () -> (itemStack, i) -> i == 1 ? color : -1)
				.model(NonNullBiConsumer.noop())
				.build()
				.register();
	}

	private static BlockEntry<ConcreteBlock> concrete(final String name, final String texture) {
		return ApiBridge.getRegistrate().block(name, props -> new ConcreteBlock(props, "decoration/concrete/" + texture))
				.initialProperties(() -> Blocks.STONE)
				.blockstate(NonNullBiConsumer.noop())
				.item(RenderedBlockItem::new)
				.model(NonNullBiConsumer.noop())
				.build()
				.register();
	}

	private ConductanceDecoration() {
	}
}
