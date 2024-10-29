package conductance.api.registry;

import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface TaggedSet<TYPE> extends IRegistryObject<String> {

	<TAGTYPE> Stream<TagKey<TAGTYPE>> streamTags(Registry<TAGTYPE> registry, TYPE object, boolean includeGlobalTags);

	<TAGTYPE> Stream<TagKey<TAGTYPE>> streamTags(Registry<TAGTYPE> registry, TYPE object);

	Stream<TagKey<Item>> streamItemTags(TYPE object);

	Stream<TagKey<Block>> streamBlockTags(TYPE object);

	Stream<TagKey<Fluid>> streamFluidTags(TYPE object);

	<TAGTYPE> Stream<TagKey<TAGTYPE>> streamAllTags(Registry<TAGTYPE> registry, TYPE object);

	Stream<TagKey<Item>> streamAllItemTags(TYPE object);

	Stream<TagKey<Block>> streamAllBlockTags(TYPE object);

	Stream<TagKey<Fluid>> streamAllFluidTags(TYPE object);

	Function<TYPE, String> getObjectSerializer();

	Function<TYPE, String> getUnlocalizedNameFactory();

	default String getUnlocalizedName(TYPE object) {
		return String.format(this.getUnlocalizedNameFactory().apply(object), this.getObjectSerializer().apply(object));
	}

	boolean isItemGenerator();

	boolean canGenerateItem(TYPE object);

	boolean isBlockGenerator();

	boolean canGenerateBlock(TYPE object);

	boolean isFluidGenerator();

	boolean canGenerateFluid(TYPE object);

	long getUnitValue();
}
