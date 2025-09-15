package conductance.api.material.event;

import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;

public interface MaterialGenerationHandlerBuilder {

	MaterialGenerationHandlerBuilder groupTag(String tagName);

	MaterialGenerationHandlerBuilder entryTag(String tagName);

	MaterialGenerationHandlerBuilder setHasItem(boolean hasItem, boolean autoGenerateItem);

	MaterialGenerationHandlerBuilder setHasBlock(boolean hasBlock, boolean autoGenerateBlock, boolean shouldOccludeBlocks);

	MaterialGenerationHandlerBuilder setHasFluid(boolean hasFluid, boolean autoGenerateFluid);

	MaterialGenerationHandlerBuilder predicate(Predicate<Material> predicate);

	default MaterialGenerationHandlerBuilder requiredFlag(final MaterialFlag requiredFlag) {
		return this.predicate(material -> material.hasFlag(requiredFlag));
	}

	MaterialGenerationHandlerBuilder textureType(ResourceLocation type);
}
