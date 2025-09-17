package conductance.core.material;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.TagTranslatorFactory;
import conductance.api.material.event.MaterialGenerationHandlerBuilder;

@RequiredArgsConstructor
final class MaterialGenerationHandlerBuilderImpl implements MaterialGenerationHandlerBuilder {

	private final Map<String, TagTranslatorFactory> groupTags = new ConcurrentHashMap<>();
	private final Map<String, TagTranslatorFactory> entryTags = new ConcurrentHashMap<>();
	private final Function<Material, String> unlocalizedNameFactory;
	private Predicate<Material> predicate = ignored -> true;
	private boolean hasItem = false;
	private boolean autoGenerateItem = false;
	private boolean hasBlock = false;
	private boolean autoGenerateBlock = false;
	private boolean shouldOccludeBlocks = false;
	private boolean hasFluid = false;
	private boolean autoGenerateFluid = false;
	private @Nullable Function<Material, String> descriptionIdSuffixFactory;
	private long unitValue = CAPI.UNIT;
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
	public MaterialGenerationHandlerBuilder setHasItem(final boolean hasItem, final boolean autoGenerateItem) {
		this.hasItem = hasItem;
		this.autoGenerateItem = autoGenerateItem;
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder setHasBlock(final boolean hasBlock, final boolean autoGenerateBlock, final boolean shouldOccludeBlocks) {
		this.hasBlock = hasBlock;
		this.autoGenerateBlock = autoGenerateBlock;
		this.shouldOccludeBlocks = shouldOccludeBlocks;
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder setHasFluid(final boolean hasFluid, final boolean autoGenerateFluid) {
		this.hasFluid = hasFluid;
		this.autoGenerateFluid = autoGenerateFluid;
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
	public MaterialGenerationHandlerBuilder textureType(final ResourceLocation type) {
		this.textureType = type;
		return this;
	}

	public MaterialGenerationHandler build() {
		return new MaterialGenerationHandlerImpl(
				this.unlocalizedNameFactory,
				this.descriptionIdSuffixFactory,
				Collections.unmodifiableMap(this.groupTags), Collections.unmodifiableMap(this.entryTags),
				this.hasItem, this.autoGenerateItem,
				this.hasBlock, this.autoGenerateBlock, this.shouldOccludeBlocks,
				this.hasFluid, this.autoGenerateFluid,
				this.unitValue,
				this.predicate,
				this.textureType
		);
	}
}
