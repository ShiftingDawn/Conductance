package conductance.core.apiimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.registry.TaggedSet;
import conductance.api.registry.TaggedSetBuilder;
import conductance.api.util.TextHelper;

@SuppressWarnings("unchecked")
@Accessors(fluent = true)
public abstract class TaggedSetBuilderImpl<TYPE, SET extends TaggedSet<TYPE>, BUILDER extends TaggedSetBuilder<TYPE, SET, BUILDER>> implements TaggedSetBuilder<TYPE, SET, BUILDER> {

	@Getter(AccessLevel.PACKAGE)
	private final List<TagHandler<TYPE>> tags = new ArrayList<>();
	@Getter(AccessLevel.PACKAGE)
	private final List<TagKey<Block>> miningTools = new ArrayList<>();

	@Getter
	private final String registryKey;
	@Getter
	private final Function<TYPE, String> objectSerializer;
	@Getter
	private final Function<TYPE, String> unlocalizedNameFactory;

	@Getter
	private boolean generateItems;
	@Getter
	private boolean generateBlocks;
	@Getter
	private boolean generateFluids;
	@Getter
	@Nullable
	private Predicate<TYPE> generatorPredicate;
	@Getter
	@Nullable
	private BiConsumer<TYPE, ItemBuilder<? extends Item, ?>> itemGeneratorCallback;
	@Getter
	@Nullable
	private BiConsumer<TYPE, BlockBuilder<? extends Block, ?>> blockGeneratorCallback;
	@Getter
	@Nullable
	private BiConsumer<TYPE, FluidBuilder<? extends Fluid, ?>> fluidGeneratorCallback;

	@Getter
	private int maxStackSize = 64;
	@Getter
	private long unitValue = -1;

	public TaggedSetBuilderImpl(final String registryKey, final Function<TYPE, String> objectSerializer, final Function<TYPE, String> unlocalizedNameFactory) {
		this.registryKey = registryKey;
		this.objectSerializer = objectSerializer;
		this.unlocalizedNameFactory = unlocalizedNameFactory;
	}

	public TaggedSetBuilderImpl(final String name, final Function<TYPE, String> objectSerializer, final String unlocalizedNameFactory) {
		this(name, objectSerializer, ignored -> unlocalizedNameFactory);
	}

	public TaggedSetBuilderImpl(final String name, final Function<TYPE, String> objectSerializer) {
		this(name, objectSerializer, "%s_" + TextHelper.toLowerCaseUnderscore(name));
	}

	// region Formatted Tags
	@Override
	public BUILDER addTag(final String tagPathFactory) {
		this.tags.add(new TagHandler<>("c", tagPathFactory, this.objectSerializer, false));
		return (BUILDER) this;
	}

	@Override
	public BUILDER addTagMod(final String tagPathFactory) {
		this.tags.add(new TagHandler<>(CAPI.MOD_ID, tagPathFactory, this.objectSerializer, false));
		return (BUILDER) this;
	}

	@Override
	public BUILDER addTagVanilla(final String tagPathFactory) {
		this.tags.add(new TagHandler<>(ResourceLocation.DEFAULT_NAMESPACE, tagPathFactory, this.objectSerializer, false));
		return (BUILDER) this;
	}
	// endregion

	// region Unformatted Tags
	@Override
	public BUILDER addTagUnformatted(final String tagPathFactory) {
		this.tags.add(new TagHandler<>("c", tagPathFactory, ignored -> tagPathFactory, true));
		return (BUILDER) this;
	}

	@Override
	public BUILDER addTagModUnformatted(final String tagPathFactory) {
		this.tags.add(new TagHandler<>(CAPI.MOD_ID, tagPathFactory, ignored -> tagPathFactory, true));
		return (BUILDER) this;
	}

	@Override
	public BUILDER addTagVanillaUnformatted(final String tagPathFactory) {
		this.tags.add(new TagHandler<>(ResourceLocation.DEFAULT_NAMESPACE, tagPathFactory, ignored -> tagPathFactory, true));
		return (BUILDER) this;
	}
	// endregion

	// region Generation
	@Override
	public BUILDER generateItems(final boolean doGenerateItems) {
		this.generateItems = doGenerateItems;
		return (BUILDER) this;
	}

	@Override
	public BUILDER generateBlocks(final boolean doGenerateBlocks) {
		this.generateBlocks = doGenerateBlocks;
		return (BUILDER) this;
	}

	@Override
	public BUILDER generateFluids(final boolean doGenerateFluids) {
		this.generateFluids = doGenerateFluids;
		return (BUILDER) this;
	}

	@Override
	public BUILDER generatorPredicate(@Nullable final Predicate<TYPE> predicate) {
		this.generatorPredicate = predicate;
		return (BUILDER) this;
	}

	@Override
	public BUILDER itemGeneratorCallback(@Nullable final BiConsumer<TYPE, ItemBuilder<? extends Item, ?>> callback) {
		this.itemGeneratorCallback = callback;
		return (BUILDER) this;
	}

	@Override
	public BUILDER blockGeneratorCallback(@Nullable final BiConsumer<TYPE, BlockBuilder<? extends Block, ?>> callback) {
		this.blockGeneratorCallback = callback;
		return (BUILDER) this;
	}

	@Override
	public BUILDER fluidGeneratorCallback(@Nullable final BiConsumer<TYPE, FluidBuilder<? extends Fluid, ?>> callback) {
		this.fluidGeneratorCallback = callback;
		return (BUILDER) this;
	}

	// endregion

	// region Properties
	@Override
	public BUILDER maxStackSize(final int newMaxStackSize) {
		this.maxStackSize = newMaxStackSize;
		return (BUILDER) this;
	}

	@Override
	public BUILDER unitValue(final long newUnitValue) {
		this.unitValue = newUnitValue;
		return (BUILDER) this;
	}

	@Override
	public BUILDER miningTool(final TagKey<Block> miningTag) {
		this.miningTools.add(miningTag);
		return (BUILDER) this;
	}

	// endregion

	@Getter
	static class TagHandler<T> {

		private final String namespace;
		private final String tagPathFactory;
		private final Function<T, String> objectSerializer;
		private final boolean isGlobalTag;

		TagHandler(@Nullable final String namespace, final String tagPathFactory, final Function<T, String> objectSerializer, final boolean isGlobalTag) {
			this.namespace = namespace != null ? namespace : tagPathFactory.contains(":") ? tagPathFactory.split(":", 2)[0] : CAPI.MOD_ID;
			this.tagPathFactory = tagPathFactory.contains(":") ? tagPathFactory.split(":", 2)[1] : tagPathFactory;
			this.objectSerializer = objectSerializer;
			this.isGlobalTag = isGlobalTag;
		}

		public ResourceLocation make(final T obj) {
			return ResourceLocation.fromNamespaceAndPath(this.namespace, this.tagPathFactory.formatted(this.objectSerializer.apply(obj)));
		}

		@Override
		public int hashCode() {
			return this.tagPathFactory.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			return obj instanceof final TagHandler<?> other && other.tagPathFactory.equals(this.tagPathFactory);
		}

		@Override
		public String toString() {
			return this.tagPathFactory;
		}
	}
}
