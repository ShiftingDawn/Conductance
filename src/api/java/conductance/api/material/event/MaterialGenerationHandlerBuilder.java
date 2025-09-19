package conductance.api.material.event;

import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.TagTranslatorFactory;

public interface MaterialGenerationHandlerBuilder {

	interface BuilderCallback<T> {

		T apply(Material material, T properties);
	}

	MaterialGenerationHandlerBuilder groupTag(String tagName, @Nullable TagTranslatorFactory translationFactory);

	default MaterialGenerationHandlerBuilder groupTag(final String tagName, @Nullable final String translationFactory) {
		return this.groupTag(tagName, material -> translationFactory);
	}

	MaterialGenerationHandlerBuilder entryTag(String tagName, @Nullable TagTranslatorFactory translationFactory);

	default MaterialGenerationHandlerBuilder entryTag(final String tagName, @Nullable final String translationFactory) {
		return this.entryTag(tagName, material -> translationFactory);
	}

	MaterialGenerationHandlerBuilder setHasItem(boolean hasItem, boolean autoGenerateItem, @Nullable BuilderCallback<Item.Properties> builderCallback);

	default MaterialGenerationHandlerBuilder setHasItem(final boolean hasItem, final boolean autoGenerateItem) {
		return this.setHasItem(true, autoGenerateItem, null);
	}

	MaterialGenerationHandlerBuilder setHasBlock(
			boolean hasBlock, boolean autoGenerateBlock, boolean shouldOccludeBlocks, TagKey<Block> requiredToolTypeTag, @Nullable BuilderCallback<BlockBehaviour.Properties> blockBuilderCallback,
			@Nullable BuilderCallback<Item.Properties> itemBuilderCallback);

	default MaterialGenerationHandlerBuilder setHasBlock(final boolean hasBlock, final boolean autoGenerateBlock, final boolean shouldOccludeBlocks, final TagKey<Block> requiredToolTypeTag) {
		return this.setHasBlock(hasBlock, autoGenerateBlock, shouldOccludeBlocks, requiredToolTypeTag, null, null);
	}

	MaterialGenerationHandlerBuilder setHasFluid(boolean hasFluid, boolean autoGenerateFluid, @Nullable BuilderCallback<FluidType.Properties> builderCallback);

	default MaterialGenerationHandlerBuilder setHasFluid(final boolean hasFluid, final boolean autoGenerateFluid) {
		return this.setHasFluid(hasFluid, autoGenerateFluid, null);
	}

	MaterialGenerationHandlerBuilder setDescriptionIdSuffixFactory(Function<Material, String> descriptionIdSuffixFactory);

	MaterialGenerationHandlerBuilder unitValue(long unitValue);

	MaterialGenerationHandlerBuilder predicate(Predicate<Material> predicate);

	default MaterialGenerationHandlerBuilder requiredFlag(final MaterialFlag requiredFlag) {
		return this.predicate(material -> material.hasFlag(requiredFlag));
	}

	default MaterialGenerationHandlerBuilder requiredTrait(final MaterialTraitKey<?> requiredTrait) {
		return this.predicate(material -> material.hasTrait(requiredTrait));
	}

	MaterialGenerationHandlerBuilder addMiningToolType(TagKey<Block> miningToolTag);

	MaterialGenerationHandlerBuilder textureType(ResourceLocation type);
}
