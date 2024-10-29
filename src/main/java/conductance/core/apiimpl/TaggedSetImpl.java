package conductance.core.apiimpl;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
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
	private final boolean itemGenerator;
	@Getter
	private final boolean blockGenerator;
	@Getter
	private final boolean fluidGenerator;
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
	private final long unitValue;

	public TaggedSetImpl(TaggedSetBuilderImpl<TYPE, ?, ?> builder) {
		super(builder.registryKey());
		this.objectSerializer = builder.objectSerializer();
		this.unlocalizedNameFactory = builder.unlocalizedNameFactory();

		// TODO Let plugins/mods modify the taglist before finalizing
		this.tags = ImmutableList.copyOf(builder.tags);

		this.itemGenerator = builder.generateItems();
		this.blockGenerator = builder.generateBlocks();
		this.fluidGenerator = builder.generateFluids();
		this.generatorPredicate = builder.generatorPredicate();
		this.itemGeneratorCallback = builder.itemGeneratorCallback();
		this.blockGeneratorCallback = builder.blockGeneratorCallback();
		this.fluidGeneratorCallback = builder.fluidGeneratorCallback();

		this.unitValue = builder.unitValue();
	}

	@Override
	public <TAGTYPE> Stream<TagKey<TAGTYPE>> streamTags(Registry<TAGTYPE> registry, TYPE object, boolean includeGlobalTags) {
		return this.tags.stream().filter(handler -> includeGlobalTags || !handler.isGlobalTag).map(handler -> handler.make(object)).map(tagPath -> TagKey.create(registry.key(), tagPath));
	}

	@Override
	public <TAGTYPE> Stream<TagKey<TAGTYPE>> streamTags(Registry<TAGTYPE> registry, TYPE object) {
		return streamTags(registry, object, false);
	}

	@Override
	public Stream<TagKey<Item>> streamItemTags(TYPE object) {
		return streamTags(BuiltInRegistries.ITEM, object);
	}

	@Override
	public Stream<TagKey<Block>> streamBlockTags(TYPE object) {
		return streamTags(BuiltInRegistries.BLOCK, object);
	}

	@Override
	public Stream<TagKey<Fluid>> streamFluidTags(TYPE object) {
		return streamTags(BuiltInRegistries.FLUID, object);
	}

	@Override
	public <TAGTYPE> Stream<TagKey<TAGTYPE>> streamAllTags(Registry<TAGTYPE> registry, TYPE object) {
		return streamTags(registry, object, true);
	}

	@Override
	public Stream<TagKey<Item>> streamAllItemTags(TYPE object) {
		return streamAllTags(BuiltInRegistries.ITEM, object);
	}

	@Override
	public Stream<TagKey<Block>> streamAllBlockTags(TYPE object) {
		return streamAllTags(BuiltInRegistries.BLOCK, object);
	}

	@Override
	public Stream<TagKey<Fluid>> streamAllFluidTags(TYPE object) {
		return streamAllTags(BuiltInRegistries.FLUID, object);
	}

	@Override
	public boolean canGenerateItem(TYPE object) {
		// TODO handle overrides
		return this.isItemGenerator() && (this.generatorPredicate == null || this.generatorPredicate.test(object));
	}

	@Override
	public boolean canGenerateBlock(TYPE object) {
		// TODO handle overrides
		return this.isBlockGenerator() && (this.generatorPredicate == null || this.generatorPredicate.test(object));
	}

	@Override
	public boolean canGenerateFluid(TYPE object) {
		// TODO handle overrides
		return this.isFluidGenerator() && (this.generatorPredicate == null || this.generatorPredicate.test(object));
	}
}
