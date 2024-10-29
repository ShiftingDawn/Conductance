package conductance.api.plugin;

import net.minecraft.world.level.ItemLike;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

public interface MaterialOverrideMap {

	void add(final TaggedMaterialSet set, final Material material, final ItemLike... overrides);

	default void gemOnly(final Material material, final ItemLike... overrides) {
		this.add(NCMaterialTaggedSets.GEM, material, overrides);
		// TODO re-enable other gem types
//		MaterialTagTypes.TT_GEM_FLAWED.setCustomGeneration(material);
//		MaterialTagTypes.TT_GEM_FLAWLESS.setCustomGeneration(material);
//		MaterialTagTypes.TT_GEM_EXQUISITE.setCustomGeneration(material);
	}
}
