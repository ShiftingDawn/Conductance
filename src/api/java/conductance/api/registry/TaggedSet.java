package conductance.api.registry;

import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface TaggedSet<TYPE> extends IRegistryObject<String> {

	<TAGTYPE> Stream<Tuple<TagKey<TAGTYPE>, Function<TYPE, String>>> streamTagData(Registry<TAGTYPE> registry, TYPE object, boolean includeGlobalTags);

	<TAGTYPE> Stream<Tuple<TagKey<TAGTYPE>, Function<TYPE, String>>> streamTagData(Registry<TAGTYPE> registry, TYPE object);

	Stream<Tuple<TagKey<Item>, Function<TYPE, String>>> streamItemTagData(TYPE object);

	Stream<Tuple<TagKey<Block>, Function<TYPE, String>>> streamBlockTagData(TYPE object);

	Stream<Tuple<TagKey<Fluid>, Function<TYPE, String>>> streamFluidTagData(TYPE object);

	<TAGTYPE> Stream<Tuple<TagKey<TAGTYPE>, Function<TYPE, String>>> streamAllTagData(Registry<TAGTYPE> registry, TYPE object);

	Stream<Tuple<TagKey<Item>, Function<TYPE, String>>> streamAllItemTagData(TYPE object);

	Stream<Tuple<TagKey<Block>, Function<TYPE, String>>> streamAllBlockTagData(TYPE object);

	Stream<Tuple<TagKey<Fluid>, Function<TYPE, String>>> streamAllFluidTagData(TYPE object);

	//
	default <TAGTYPE> Stream<TagKey<TAGTYPE>> streamTags(final Registry<TAGTYPE> registry, final TYPE object, final boolean includeGlobalTags) {
		return this.streamTagData(registry, object, includeGlobalTags).map(Tuple::getA);
	}

	default <TAGTYPE> Stream<TagKey<TAGTYPE>> streamTags(final Registry<TAGTYPE> registry, final TYPE object) {
		return this.streamTagData(registry, object).map(Tuple::getA);
	}

	default Stream<TagKey<Item>> streamItemTags(final TYPE object) {
		return this.streamItemTagData(object).map(Tuple::getA);
	}

	default Stream<TagKey<Block>> streamBlockTags(final TYPE object) {
		return this.streamBlockTagData(object).map(Tuple::getA);
	}

	default Stream<TagKey<Fluid>> streamFluidTags(final TYPE object) {
		return this.streamFluidTagData(object).map(Tuple::getA);
	}

	default <TAGTYPE> Stream<TagKey<TAGTYPE>> streamAllTags(final Registry<TAGTYPE> registry, final TYPE object) {
		return this.streamAllTagData(registry, object).map(Tuple::getA);
	}

	default Stream<TagKey<Item>> streamAllItemTags(final TYPE object) {
		return this.streamAllItemTagData(object).map(Tuple::getA);
	}

	default Stream<TagKey<Block>> streamAllBlockTags(final TYPE object) {
		return this.streamAllBlockTagData(object).map(Tuple::getA);
	}

	default Stream<TagKey<Fluid>> streamAllFluidTags(final TYPE object) {
		return this.streamAllFluidTagData(object).map(Tuple::getA);
	}

	Function<TYPE, String> getObjectSerializer();

	Function<TYPE, String> getUnlocalizedNameFactory();

	default String getUnlocalizedName(final TYPE object) {
		return String.format(this.getUnlocalizedNameFactory().apply(object), this.getObjectSerializer().apply(object));
	}

	boolean shouldAutoGenerateItems();

	boolean hasItems();

	boolean canGenerateItem(TYPE object);

	boolean shouldAutoGenerateBlocks();

	boolean hasBlocks();

	boolean shouldOccludeBlocks();

	boolean canGenerateBlock(TYPE object);

	boolean shouldAutoGenerateFluids();

	boolean hasFluids();

	boolean canGenerateFluid(TYPE object);

	int getMaxStackSize();

	long getUnitValue();

	long getUnitValue(TYPE object);
}
