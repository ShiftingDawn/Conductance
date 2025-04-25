package conductance.api.registry;

import java.util.Optional;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import com.tterrag.registrate.util.entry.BlockEntry;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.api.material.TaggedMaterialSet;

public interface MaterialRegistry extends TaggedSetRegistry<Material, TaggedMaterialSet> {

	Optional<Block> getOre(MaterialOreType oreType, Material object);

	ItemStack getOre(MaterialOreType oreType, Material object, int count);

	@Nullable
	Block getOreUnsafe(MaterialOreType oreType, Material object);

	void register(MaterialOreType oreType, Material object, BlockEntry<? extends Block> block);
}
