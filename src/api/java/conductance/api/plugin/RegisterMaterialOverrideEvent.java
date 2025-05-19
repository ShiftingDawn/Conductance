package conductance.api.plugin;

import net.minecraft.world.level.ItemLike;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterMaterialOverrideEvent implements IConductancePluginEvent {

	public interface MaterialOverrideMap {

		void add(TaggedMaterialSet set, Material material, ItemLike... overrides);
	}

	private final MaterialOverrideMap delegate;

	public void add(final TaggedMaterialSet set, final Material material, final ItemLike... overrides) {
		this.delegate.add(set, material, overrides);
	}

	public void gemOnly(final Material material, final ItemLike... overrides) {
		this.add(NCMaterialTaggedSets.GEM, material, overrides);
		this.add(NCMaterialTaggedSets.GEM_FLAWED, material);
		this.add(NCMaterialTaggedSets.GEM_FLAWLESS, material);
		this.add(NCMaterialTaggedSets.GEM_EXQUISITE, material);
	}
}
