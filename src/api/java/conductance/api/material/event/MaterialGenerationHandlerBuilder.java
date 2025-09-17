package conductance.api.material.event;

import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.TagTranslatorFactory;

public interface MaterialGenerationHandlerBuilder {

	MaterialGenerationHandlerBuilder groupTag(String tagName, @Nullable TagTranslatorFactory translationFactory);

	default MaterialGenerationHandlerBuilder groupTag(final String tagName, @Nullable final String translationFactory) {
		return this.groupTag(tagName, material -> translationFactory);
	}

	MaterialGenerationHandlerBuilder entryTag(String tagName, @Nullable TagTranslatorFactory translationFactory);

	default MaterialGenerationHandlerBuilder entryTag(final String tagName, @Nullable final String translationFactory) {
		return this.entryTag(tagName, material -> translationFactory);
	}

	MaterialGenerationHandlerBuilder setHasItem(boolean hasItem, boolean autoGenerateItem);

	MaterialGenerationHandlerBuilder setHasBlock(boolean hasBlock, boolean autoGenerateBlock, boolean shouldOccludeBlocks);

	MaterialGenerationHandlerBuilder setHasFluid(boolean hasFluid, boolean autoGenerateFluid);

	MaterialGenerationHandlerBuilder setDescriptionIdSuffixFactory(Function<Material, String> descriptionIdSuffixFactory);

	MaterialGenerationHandlerBuilder unitValue(long unitValue);

	MaterialGenerationHandlerBuilder predicate(Predicate<Material> predicate);

	default MaterialGenerationHandlerBuilder requiredFlag(final MaterialFlag requiredFlag) {
		return this.predicate(material -> material.hasFlag(requiredFlag));
	}

	default MaterialGenerationHandlerBuilder requiredTrait(final MaterialTraitKey<?> requiredTrait) {
		return this.predicate(material -> material.hasTrait(requiredTrait));
	}

	MaterialGenerationHandlerBuilder textureType(ResourceLocation type);
}
