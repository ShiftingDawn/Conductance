package conductance.init;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.Conductance;
import conductance.init.deco.ConcreteBlock;
import conductance.init.deco.DecoBlock;
import conductance.init.item.RenderedBlockItem;
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
import static conductance.api.NCDecoration.DARK_CIRCUIT;
import static conductance.api.NCDecoration.LIGHT_CIRCUIT;
import static conductance.api.NCDecoration.LUX;
import static conductance.api.NCDecoration.ME_BLOCK;
import static conductance.api.NCDecoration.ME_BLOCK_ACTIVE;
import static conductance.api.NCDecoration.TILES_1;
import static conductance.api.NCDecoration.TILES_2;
import static conductance.api.NCDecoration.TILES_3;
import static conductance.api.NCDecoration.TILES_4;
import static conductance.api.NCDecoration.TILES_5;
import static conductance.api.NCDecoration.TILES_6;
import static conductance.core.register.RegisterCore.REGISTRATE;

public final class ConductanceDecoration {

	public static void init() {
		for (final DyeColor dyeColor : DyeColor.values()) {
			LUX.put(dyeColor, ConductanceDecoration.decoBlock("%s_lux_block".formatted(dyeColor.getSerializedName()), builder ->
					builder.properties(props -> props.emissiveRendering((blockState, blockGetter, blockPos) -> true))));
			LIGHT_CIRCUIT.put(dyeColor, ConductanceDecoration.decoBlock("light_%s_circuit_block".formatted(dyeColor.getSerializedName()), builder ->
					builder.properties(props -> props.emissiveRendering((blockState, blockGetter, blockPos) -> true))));
			DARK_CIRCUIT.put(dyeColor, ConductanceDecoration.decoBlock("dark_%s_circuit_block".formatted(dyeColor.getSerializedName()), builder ->
					builder.properties(props -> props.emissiveRendering((blockState, blockGetter, blockPos) -> true))));

			TILES_1.put(dyeColor, ConductanceDecoration.decoBlock("%s_tiles_1_block".formatted(dyeColor.getSerializedName()), null));
			TILES_2.put(dyeColor, ConductanceDecoration.decoBlock("%s_tiles_2_block".formatted(dyeColor.getSerializedName()), null));
			TILES_3.put(dyeColor, ConductanceDecoration.decoBlock("%s_tiles_3_block".formatted(dyeColor.getSerializedName()), null));
			TILES_4.put(dyeColor, ConductanceDecoration.decoBlock("%s_tiles_4_block".formatted(dyeColor.getSerializedName()), null));
			TILES_5.put(dyeColor, ConductanceDecoration.decoBlock("%s_tiles_5_block".formatted(dyeColor.getSerializedName()), null));
			TILES_6.put(dyeColor, ConductanceDecoration.decoBlock("%s_tiles_6_block".formatted(dyeColor.getSerializedName()), null));
		}

		CAPI.translations().addInterceptor("block.%s.me_block".formatted(Conductance.MODID), () -> Component.literal("ME Block")); //Force ME capitalized
		ME_BLOCK = ConductanceDecoration.decoBlock("me_block", null);
		CAPI.translations().addInterceptor("block.%s.active_me_block".formatted(Conductance.MODID), () -> Component.literal("Active ME Block")); //Force ME capitalized
		ME_BLOCK_ACTIVE = ConductanceDecoration.decoBlock("active_me_block", null);

		CONCRETE_LIGHT = ConductanceDecoration.concrete("light_concrete");
		CONCRETE_LIGHT_CRACKED = ConductanceDecoration.concrete("cracked_light_concrete");
		CONCRETE_LIGHT_BRICKS = ConductanceDecoration.concrete("light_concrete_bricks");
		CONCRETE_LIGHT_SMOOTH = ConductanceDecoration.concrete("smooth_light_concrete");
		CONCRETE_LIGHT_BORDERED = ConductanceDecoration.concrete("bordered_light_concrete");
		CONCRETE_DARK = ConductanceDecoration.concrete("dark_concrete");
		CONCRETE_DARK_CRACKED = ConductanceDecoration.concrete("cracked_dark_concrete");
		CONCRETE_DARK_BRICKS = ConductanceDecoration.concrete("dark_concrete_bricks");
		CONCRETE_DARK_SMOOTH = ConductanceDecoration.concrete("smooth_dark_concrete");
		CONCRETE_DARK_BORDERED = ConductanceDecoration.concrete("bordered_dark_concrete");
	}

	private static BlockEntry<DecoBlock> decoBlock(final String name, @Nullable final Consumer<BlockBuilder<DecoBlock, Registrate>> builder) {
		final BlockBuilder<DecoBlock, Registrate> b = REGISTRATE.block(name, DecoBlock::new)
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.blockstate(NonNullBiConsumer.noop())
				.item(RenderedBlockItem::new)
				.model(NonNullBiConsumer.noop())
				.build();
		if (builder != null) {
			builder.accept(b);
		}
		return b.register();
	}

	private static BlockEntry<ConcreteBlock> concrete(final String name) {
		return REGISTRATE.block(name, ConcreteBlock::new)
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
