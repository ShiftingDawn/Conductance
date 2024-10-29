package conductance.api.registry;

import java.util.function.Supplier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

public interface TranslationRegistry {

	String translate(String key, Supplier<String> fallback, Object... format);

	MutableComponent makeLocalizedName(String key, Supplier<String> fallback, Object... format);

	MutableComponent makeLocalizedName(Block block);

	MutableComponent makeLocalizedName(Block block, Supplier<MutableComponent> override);

	MutableComponent makeLocalizedName(Item item);

	MutableComponent makeLocalizedName(Item item, Supplier<MutableComponent> override);

	MutableComponent makeLocalizedName(String key, TaggedMaterialSet tagType, Material material);

	// TODO fluids
//	MutableComponent makeLocalizedName(String key, MaterialFluidGenerator tagType, Material material);
//
//	MutableComponent makeBucketName(String key, MaterialFluidGenerator tagType, Material material);
}
