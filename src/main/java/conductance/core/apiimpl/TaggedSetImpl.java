package conductance.core.apiimpl;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import conductance.api.registry.RegistryObject;
import conductance.api.registry.TaggedSet;

public class TaggedSetImpl<TYPE> extends RegistryObject<String> implements TaggedSet<TYPE> {

	@Getter
	private final Function<TYPE, String> objectSerializer;
	@Getter
	private final Function<TYPE, String> unlocalizedNameFactory;

	private final List<TaggedSetBuilderImpl.TagHandler<TYPE>> tags;

	@Getter
	private final boolean generateItems;
	@Getter
	private final boolean generateBlocks;
	@Getter
	private final boolean generateFluids;

	@Getter
	private final long unitValue;

	public TaggedSetImpl(TaggedSetBuilderImpl<TYPE> builder) {
		super(builder.registryKey());
		this.objectSerializer = builder.objectSerializer();
		this.unlocalizedNameFactory = builder.unlocalizedNameFactory();

		// TODO Let plugins/mods modify the taglist before finalizing
		this.tags = ImmutableList.copyOf(builder.tags);

		this.generateItems = builder.generateItems();
		this.generateBlocks = builder.generateBlocks();
		this.generateFluids = builder.generateFluids();

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
}
