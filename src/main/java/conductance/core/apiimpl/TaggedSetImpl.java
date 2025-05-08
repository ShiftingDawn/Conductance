package conductance.core.apiimpl;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import com.google.common.collect.ImmutableList;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.registry.RegistryObject;
import conductance.api.registry.TaggedSet;

public abstract class TaggedSetImpl<TYPE> extends RegistryObject<String> implements TaggedSet<TYPE> {

	@Getter
	private final Function<TYPE, String> objectSerializer;
	@Getter
	private final Function<TYPE, String> unlocalizedNameFactory;

	private final List<TaggedSetBuilderImpl.TagHandler<TYPE>> tags;
	@Getter
	private final List<TagKey<Block>> miningTags;

	private final boolean hasItems;
	private final boolean autoGenerateItems;
	private final boolean hasBlocks;
	private final boolean autoGenerateBlocks;
	private final boolean shouldOccludeBlocks;
	private final boolean hasFluids;
	private final boolean autoGenerateFluids;
	@Nullable
	private final Predicate<TYPE> generatorPredicate;
	@Getter
	@Nullable
	private final BiConsumer<TYPE, ItemBuilder<? extends Item, ?>> itemGeneratorCallback;
	@Getter
	@Nullable
	private final BiConsumer<TYPE, BlockBuilder<? extends Block, ?>> blockGeneratorCallback;
	@Getter
	@Nullable
	private final BiConsumer<TYPE, FluidBuilder<? extends Fluid, ?>> fluidGeneratorCallback;

	@Getter
	private final int maxStackSize;
	@Getter
	private final long unitValue;

	public TaggedSetImpl(final TaggedSetBuilderImpl<TYPE, ?, ?> builder) {
		super(builder.registryKey());
		this.objectSerializer = builder.objectSerializer();
		this.unlocalizedNameFactory = builder.unlocalizedNameFactory();

		// TODO Let plugins/mods modify the taglist before finalizing
		this.tags = ImmutableList.copyOf(builder.tags());
		this.miningTags = ImmutableList.copyOf(builder.miningTools());

		this.hasItems = builder.hasItems();
		this.autoGenerateItems = builder.autoGenerateItems();
		this.itemGeneratorCallback = builder.itemGeneratorCallback();
		this.hasBlocks = builder.hasBlocks();
		this.autoGenerateBlocks = builder.autoGenerateBlocks();
		this.blockGeneratorCallback = builder.blockGeneratorCallback();
		this.shouldOccludeBlocks = builder.occludeBlocks();
		this.hasFluids = builder.hasFluids();
		this.autoGenerateFluids = builder.autoGenerateFluids();
		this.fluidGeneratorCallback = builder.fluidGeneratorCallback();
		this.generatorPredicate = builder.generatorPredicate();

		this.maxStackSize = builder.maxStackSize();
		this.unitValue = builder.unitValue();
	}

	@Override
	public boolean shouldAutoGenerateItems() {
		return this.autoGenerateItems;
	}

	@Override
	public boolean hasItems() {
		return this.hasItems;
	}

	@Override
	public boolean shouldAutoGenerateBlocks() {
		return this.autoGenerateBlocks;
	}

	@Override
	public boolean shouldOccludeBlocks() {
		return this.shouldOccludeBlocks;
	}

	@Override
	public boolean hasBlocks() {
		return this.hasBlocks;
	}

	@Override
	public boolean shouldAutoGenerateFluids() {
		return this.autoGenerateFluids;
	}

	@Override
	public boolean hasFluids() {
		return this.hasFluids;
	}

	@Override
	public <TAGTYPE> Stream<Tuple<TagKey<TAGTYPE>, Function<TYPE, String>>> streamTagData(final Registry<TAGTYPE> registry, final TYPE object, final boolean includeGlobalTags) {
		return this.tags.stream()
				.filter(handler -> includeGlobalTags || !handler.isGlobalTag())
				.map(handler -> new Tuple<>(handler.make(object), handler.tagTranslator()))
				.map(tuple -> new Tuple<>(TagKey.create(registry.key(), tuple.getA()), tuple.getB()));
	}

	@Override
	public <TAGTYPE> Stream<Tuple<TagKey<TAGTYPE>, Function<TYPE, String>>> streamTagData(final Registry<TAGTYPE> registry, final TYPE object) {
		return this.streamTagData(registry, object, false);
	}

	@Override
	public Stream<Tuple<TagKey<Item>, Function<TYPE, String>>> streamItemTagData(final TYPE object) {
		return this.streamTagData(BuiltInRegistries.ITEM, object);
	}

	@Override
	public Stream<Tuple<TagKey<Block>, Function<TYPE, String>>> streamBlockTagData(final TYPE object) {
		return this.streamTagData(BuiltInRegistries.BLOCK, object);
	}

	@Override
	public Stream<Tuple<TagKey<Fluid>, Function<TYPE, String>>> streamFluidTagData(final TYPE object) {
		return this.streamTagData(BuiltInRegistries.FLUID, object);
	}

	@Override
	public <TAGTYPE> Stream<Tuple<TagKey<TAGTYPE>, Function<TYPE, String>>> streamAllTagData(final Registry<TAGTYPE> registry, final TYPE object) {
		return this.streamTagData(registry, object, true);
	}

	@Override
	public Stream<Tuple<TagKey<Item>, Function<TYPE, String>>> streamAllItemTagData(final TYPE object) {
		return this.streamAllTagData(BuiltInRegistries.ITEM, object);
	}

	@Override
	public Stream<Tuple<TagKey<Block>, Function<TYPE, String>>> streamAllBlockTagData(final TYPE object) {
		return this.streamAllTagData(BuiltInRegistries.BLOCK, object);
	}

	@Override
	public Stream<Tuple<TagKey<Fluid>, Function<TYPE, String>>> streamAllFluidTagData(final TYPE object) {
		return this.streamAllTagData(BuiltInRegistries.FLUID, object);
	}

	@Override
	public boolean canGenerateItem(final TYPE object) {
		return this.shouldAutoGenerateItems() && (this.generatorPredicate == null || this.generatorPredicate.test(object));
	}

	@Override
	public boolean canGenerateBlock(final TYPE object) {
		return this.shouldAutoGenerateBlocks() && (this.generatorPredicate == null || this.generatorPredicate.test(object));
	}

	@Override
	public boolean canGenerateFluid(final TYPE object) {
		return this.shouldAutoGenerateFluids() && (this.generatorPredicate == null || this.generatorPredicate.test(object));
	}
}
