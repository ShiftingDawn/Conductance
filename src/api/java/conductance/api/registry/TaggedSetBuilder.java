package conductance.api.registry;

import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import org.jetbrains.annotations.Nullable;

public interface TaggedSetBuilder<TYPE, SET extends TaggedSet<TYPE>, BUILDER extends TaggedSetBuilder<TYPE, SET, BUILDER>> {

	// region Tags
	BUILDER addTag(String tagPathFactory);

	BUILDER addTagMod(String tagPathFactory);

	BUILDER addTagVanilla(String tagPathFactory);
	// endregion

	// region Unformatted Tags
	BUILDER addTagUnformatted(String tagPathFactory);

	BUILDER addTagModUnformatted(String tagPathFactory);

	BUILDER addTagVanillaUnformatted(String tagPathFactory);
	// endregion

	// region Generation
	BUILDER hasItems(boolean hasItems, boolean autoGenerate);

	default BUILDER hasItems(final boolean generateItems) {
		return this.hasItems(generateItems, true);
	}

	BUILDER hasBlocks(boolean hasBlocks, boolean autoGenerate, boolean shouldOcclude);

	default BUILDER hasBlocks(final boolean hasBlocks, final boolean autoGenerate) {
		return this.hasBlocks(hasBlocks, autoGenerate, true);
	}

	default BUILDER hasBlocks(final boolean generateBlocks) {
		return this.hasBlocks(generateBlocks, true);

	}

	BUILDER hasFluids(boolean generateFluids, boolean autoGenerate);

	default BUILDER hasFluids(final boolean hasFluids) {
		return this.hasFluids(hasFluids, true);
	}

	BUILDER generatorPredicate(@Nullable Predicate<TYPE> predicate);

	BUILDER itemGeneratorCallback(@Nullable BiConsumer<TYPE, ItemBuilder<? extends Item, ?>> callback);

	BUILDER blockGeneratorCallback(@Nullable BiConsumer<TYPE, BlockBuilder<? extends Block, ?>> callback);

	BUILDER fluidGeneratorCallback(@Nullable BiConsumer<TYPE, FluidBuilder<? extends Fluid, ?>> callback);
	// endregion

	// region Properties
	BUILDER miningTool(TagKey<Block> miningTag);

	BUILDER maxStackSize(int maxStackSize);

	BUILDER unitValue(long unitValue);
	// endregion

	SET build();
}
