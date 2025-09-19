package conductance.api.material;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.event.MaterialGenerationHandlerBuilder;

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

	@Nullable
	MaterialGenerationHandlerBuilder.BuilderCallback<Item.Properties> getItemBuilderCallback();

	boolean hasBlock();

	boolean autoGenerateBlock();

	boolean shouldOccludeBlocks();

	@Nullable
	MaterialGenerationHandlerBuilder.BuilderCallback<BlockBehaviour.Properties> getBlockBuilderCallback();

	@Nullable
	MaterialGenerationHandlerBuilder.BuilderCallback<Item.Properties> getBlockItemBuilderCallback();

	boolean hasFluid();

	boolean autoGenerateFluid();

	@Nullable
	MaterialGenerationHandlerBuilder.BuilderCallback<FluidType.Properties> getFluidBuilderCallback();

	long getUnitValue();

	Function<Material, String> getUnlocalizedNameFactory();

	default String getUnlocalizedName(final Material material) {
		return this.getUnlocalizedNameFactory().apply(material).formatted(material.getName());
	}

	boolean test(Material material);

	List<TagKey<Block>> getMiningToolTypeTags();

	ResourceLocation getTextureType();

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().materialGenerationHandlers().getKey(this), "unregistered material generation handler");
	}
}
