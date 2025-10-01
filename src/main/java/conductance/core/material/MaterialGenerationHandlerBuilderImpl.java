package conductance.core.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.FluidType;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialOreBearer;
import conductance.api.material.TagTranslatorFactory;
import conductance.api.material.event.MaterialGenerationHandlerBuilder;

@RequiredArgsConstructor
final class MaterialGenerationHandlerBuilderImpl implements MaterialGenerationHandlerBuilder {

	private final Map<String, TagTranslatorFactory> groupTags = new ConcurrentHashMap<>();
	private final Map<String, TagTranslatorFactory> entryTags = new ConcurrentHashMap<>();
	private final List<TagKey<Block>> miningToolTypeTags = new ArrayList<>();
	private final Function<Material, String> unlocalizedNameFactory;
	private final @Nullable MaterialOreBearer oreBearer;
	private Predicate<Material> predicate = ignored -> true;
	private boolean hasItem = false;
	private boolean autoGenerateItem = false;
	private @Nullable BuilderCallback<Item.Properties> itemBuilderCallback;
	private boolean hasBlock = false;
	private boolean autoGenerateBlock = false;
	private boolean shouldOccludeBlocks = false;
	private @Nullable BuilderCallback<BlockBehaviour.Properties> blockBuilderCallback;
	private @Nullable BuilderCallback<Item.Properties> blockItemBuilderCallback;
	private boolean hasFluid = false;
	private boolean autoGenerateFluid = false;
	private @Nullable BuilderCallback<FluidType.Properties> fluidBuilderCallback;
	private @Nullable Function<Material, String> descriptionIdSuffixFactory;
	private long unitValue = -1;
	private @Nullable ResourceLocation textureType;

	@Override
	public MaterialGenerationHandlerBuilder groupTag(final String tagName, @Nullable final TagTranslatorFactory translationFactory) {
		this.groupTags.put(tagName, translationFactory);
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder entryTag(final String tagName, @Nullable final TagTranslatorFactory translationFactory) {
		this.entryTags.put(tagName, translationFactory);
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder setHasItem(final boolean hasItem, final boolean autoGenerateItem, @Nullable final BuilderCallback<Item.Properties> builderCallback) {
		this.hasItem = hasItem;
		this.autoGenerateItem = autoGenerateItem;
		this.itemBuilderCallback = builderCallback;
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder setHasBlock(
			final boolean hasBlock, final boolean autoGenerateBlock, final boolean shouldOccludeBlocks, final TagKey<Block> requiredToolTypeTag,
			@Nullable final BuilderCallback<BlockBehaviour.Properties> blockBuilderCallback, @Nullable final BuilderCallback<Item.Properties> itemBuilderCallback) {
		this.hasBlock = hasBlock;
		this.autoGenerateBlock = autoGenerateBlock;
		this.shouldOccludeBlocks = shouldOccludeBlocks;
		this.addMiningToolType(requiredToolTypeTag);
		this.blockBuilderCallback = blockBuilderCallback;
		this.blockItemBuilderCallback = itemBuilderCallback;
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder setHasFluid(final boolean hasFluid, final boolean autoGenerateFluid, @Nullable final BuilderCallback<FluidType.Properties> builderCallback) {
		this.hasFluid = hasFluid;
		this.autoGenerateFluid = autoGenerateFluid;
		this.fluidBuilderCallback = builderCallback;
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder setDescriptionIdSuffixFactory(final Function<Material, String> descriptionIdSuffixFactory) {
		this.descriptionIdSuffixFactory = descriptionIdSuffixFactory;
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder unitValue(final long unitValue) {
		this.unitValue = unitValue;
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder predicate(final Predicate<Material> predicate) {
		this.predicate = predicate;
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder addMiningToolType(final TagKey<Block> miningToolTag) {
		this.miningToolTypeTags.add(miningToolTag);
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder textureType(final ResourceLocation type) {
		this.textureType = type;
		return this;
	}

	public MaterialGenerationHandler build() {
		return new MaterialGenerationHandlerImpl(
				this.unlocalizedNameFactory,
				this.descriptionIdSuffixFactory,
				Collections.unmodifiableMap(this.groupTags), Collections.unmodifiableMap(this.entryTags), Collections.unmodifiableList(this.miningToolTypeTags),
				this.hasItem, this.autoGenerateItem, this.itemBuilderCallback,
				this.hasBlock, this.autoGenerateBlock, this.shouldOccludeBlocks, this.blockBuilderCallback, this.blockItemBuilderCallback,
				this.hasFluid, this.autoGenerateFluid, this.fluidBuilderCallback,
				this.unitValue,
				this.predicate,
				this.oreBearer,
				this.textureType
		);
	}
}
