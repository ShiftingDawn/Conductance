package conductance.core.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.util.Lazy;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.TagTranslatorFactory;

@RequiredArgsConstructor
final class MaterialGenerationHandlerImpl implements MaterialGenerationHandler {

	private final Function<Material, String> unlocalizedNameFactory;
	private final Lazy<String> descriptionId = Lazy.of(() -> Util.makeDescriptionId("materialGenerationHandler", this.getId()));
	private final Map<String, TagTranslatorFactory> groupTags;
	private final Map<String, TagTranslatorFactory> entryTags;
	private final boolean hasItem;
	private final boolean autoGenerateItem;
	private final boolean hasBlock;
	private final boolean autoGenerateBlock;
	private final boolean shouldOccludeBlocks;
	private final boolean hasFluid;
	private final boolean autoGenerateFluid;
	private final long unitValue;
	private final Predicate<Material> predicate;
	private final @Nullable ResourceLocation textureType;

	private <T> List<TagKey<T>> getTags(final Map<String, TagTranslatorFactory> map, final Registry<T> registry, final Material material) {
		final List<TagKey<T>> result = new ArrayList<>();
		for (final String tagPathFactory : map.keySet()) {
			final String tag = tagPathFactory.formatted(material.getName());
			result.add(TagKey.create(registry.key(), ResourceLocation.parse(tag)));
		}
		return result;
	}

	@Override
	public String getUnlocalizedName(final Material material) {
		return this.unlocalizedNameFactory.apply(material);
	}

	@Override
	public String getDescriptionId() {
		return this.descriptionId.get();
	}

	@Override
	public <T> List<TagKey<T>> getGroupTags(final Registry<T> registry, final Material material) {
		return this.getTags(this.groupTags, registry, material);
	}

	@Override
	public <T> List<TagKey<T>> getEntryTags(final Registry<T> registry, final Material material) {
		return this.getTags(this.entryTags, registry, material);
	}

	private <T> Map<TagKey<T>, TagTranslatorFactory> getTagsAndTranslators(final Map<String, TagTranslatorFactory> map, final Registry<T> registry, final Material material) {
		final Map<TagKey<T>, TagTranslatorFactory> result = new HashMap<>();
		for (final Map.Entry<String, TagTranslatorFactory> entry : map.entrySet()) {
			final String tag = entry.getKey().formatted(material.getName());
			result.put(TagKey.create(registry.key(), ResourceLocation.parse(tag)), entry.getValue());
		}
		return result;
	}

	@Override
	public <T> Map<TagKey<T>, TagTranslatorFactory> getGroupTagsAndTranslators(final Registry<T> registry, final Material material) {
		return this.getTagsAndTranslators(this.groupTags, registry, material);
	}

	@Override
	public <T> Map<TagKey<T>, TagTranslatorFactory> getEntryTagsAndTranslators(final Registry<T> registry, final Material material) {
		return this.getTagsAndTranslators(this.entryTags, registry, material);
	}

	@Override
	public boolean hasItem() {
		return this.hasItem;
	}

	@Override
	public boolean autoGenerateItem() {
		return this.autoGenerateItem;
	}

	@Override
	public boolean hasBlock() {
		return this.hasBlock;
	}

	@Override
	public boolean autoGenerateBlock() {
		return this.autoGenerateBlock;
	}

	@Override
	public boolean shouldOccludeBlocks() {
		return this.shouldOccludeBlocks;
	}

	@Override
	public boolean hasFluid() {
		return this.hasFluid;
	}

	@Override
	public boolean autoGenerateFluid() {
		return this.autoGenerateFluid;
	}

	@Override
	public long getUnitValue() {
		return this.unitValue;
	}

	@Override
	public boolean test(final Material material) {
		return this.predicate.test(material);
	}

	@Override
	public ResourceLocation getTextureType() {
		if (this.textureType == null) {
			return Objects.requireNonNull(CAPI.regs().materialGenerationHandlers().getKey(this), "Unregistered material generation handler");
		}
		return this.textureType;
	}
}
