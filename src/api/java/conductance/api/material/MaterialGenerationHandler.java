package conductance.api.material;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import conductance.api.CAPI;

public interface MaterialGenerationHandler {

	<T> List<TagKey<T>> getGroupTags(Registry<T> registry, Material material);

	<T> List<TagKey<T>> getEntryTags(Registry<T> registry, Material material);

	<T> Map<TagKey<T>, TagTranslatorFactory> getGroupTagsAndTranslators(Registry<T> registry, Material material);

	<T> Map<TagKey<T>, TagTranslatorFactory> getEntryTagsAndTranslators(Registry<T> registry, Material material);

	boolean hasItem();

	boolean autoGenerateItem();

	boolean hasBlock();

	boolean autoGenerateBlock();

	boolean shouldOccludeBlocks();

	boolean hasFluid();

	boolean autoGenerateFluid();

	long getUnitValue();

	String getUnlocalizedName(Material material);

	String getDescriptionId();

	boolean test(Material material);

	ResourceLocation getTextureType();

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().materialGenerationHandlers().getKey(this), "unregistered material generation handler");
	}
}
