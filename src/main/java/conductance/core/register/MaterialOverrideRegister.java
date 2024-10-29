package conductance.core.register;

import net.minecraft.world.level.ItemLike;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.MaterialOverrideMap;

public final class MaterialOverrideRegister implements MaterialOverrideMap {

	private static final Table<TaggedMaterialSet, Material, ItemLike[]> OVERRIDES = HashBasedTable.create();

	@Override
	public void add(final TaggedMaterialSet set, final Material material, final ItemLike... overrides) {
		MaterialOverrideRegister.OVERRIDES.put(set, material, overrides);
	}

	public static boolean has(final TaggedMaterialSet set, final Material material) {
		return MaterialOverrideRegister.OVERRIDES.contains(set, material);
	}

	static Table<TaggedMaterialSet, Material, ItemLike[]> getOverrides() {
		return MaterialOverrideRegister.OVERRIDES;
	}
}
