package conductance.core.material;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.event.MaterialGenerationHandlerBuilder;

@RequiredArgsConstructor
final class MaterialGenerationHandlerBuilderImpl implements MaterialGenerationHandlerBuilder {

	private final Set<ResourceLocation> groupTags = new HashSet<>();
	private final Set<String> entryTags = new HashSet<>();
	private final Function<Material, String> unlocalizedNameFactory;
	private Predicate<Material> predicate = ignored -> true;
	private boolean hasItem = false;
	private boolean autoGenerateItem = false;
	private boolean hasBlock = false;
	private boolean autoGenerateBlock = false;
	private boolean shouldOccludeBlocks = false;
	private boolean hasFluid = false;
	private boolean autoGenerateFluid = false;
	private @Nullable ResourceLocation textureType;

	@Override
	public MaterialGenerationHandlerBuilder groupTag(final String tagName) {
		this.groupTags.add(ResourceLocation.parse(tagName));
		return this;
	}

	@Override
	public MaterialGenerationHandlerBuilder entryTag(final String tagName) {
		this.entryTags.add(tagName);
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
		this.autoGenerateBlock = this.autoGenerateItem;
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
				this.hasItem, this.autoGenerateItem,
				this.hasBlock, this.autoGenerateBlock, this.shouldOccludeBlocks,
				this.hasFluid, this.autoGenerateFluid,
				this.predicate,
				this.textureType
		);
	}
}
