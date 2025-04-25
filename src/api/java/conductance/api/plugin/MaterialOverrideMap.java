package conductance.api.plugin;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.api.material.TaggedMaterialSet;

public interface MaterialOverrideMap {

	void add(TaggedMaterialSet set, Material material, ItemLike... overrides);

	void add(MaterialOreType oreType, Material material, Block... overrides);

	default void gemOnly(final Material material, final ItemLike... overrides) {
		this.add(NCMaterialTaggedSets.GEM, material, overrides);
		this.add(NCMaterialTaggedSets.GEM_FLAWED, material);
		this.add(NCMaterialTaggedSets.GEM_FLAWLESS, material);
		this.add(NCMaterialTaggedSets.GEM_EXQUISITE, material);
	}
}
