package conductance.api.material;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

public interface MaterialGenerationHandler {

	String getDescriptionId();

	@Nullable
	Function<Material, String> getDescriptionIdSuffixFactory();

	String makeDescriptionId(Material material);

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

	Function<Material, String> getUnlocalizedNameFactory();

	default String getUnlocalizedName(final Material material) {
		return this.getUnlocalizedNameFactory().apply(material).formatted(material.getName());
	}

	boolean test(Material material);

	ResourceLocation getTextureType();

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().materialGenerationHandlers().getKey(this), "unregistered material generation handler");
	}
}
