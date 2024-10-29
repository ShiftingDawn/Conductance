package conductance.api.plugin;

import net.minecraft.world.level.ItemLike;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

public interface MaterialOverrideMap {

	void add(TaggedMaterialSet set, Material material, ItemLike... overrides);

	default void gemOnly(final Material material, final ItemLike... overrides) {
		this.add(NCMaterialTaggedSets.GEM, material, overrides);
		this.add(NCMaterialTaggedSets.GEM_FLAWED, material);
		this.add(NCMaterialTaggedSets.GEM_FLAWLESS, material);
		this.add(NCMaterialTaggedSets.GEM_EXQUISITE, material);
	}
}
