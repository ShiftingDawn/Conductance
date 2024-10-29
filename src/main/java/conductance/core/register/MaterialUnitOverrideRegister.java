package conductance.core.register;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.MaterialUnitOverrideMap;

public final class MaterialUnitOverrideRegister implements MaterialUnitOverrideMap {

	private static final Table<TaggedMaterialSet, Material, Long> OVERRIDES = HashBasedTable.create();

	@Override
	public void add(TaggedMaterialSet set, Material material, long value) {
		MaterialUnitOverrideRegister.OVERRIDES.put(set, material, value);
	}

	public static long get(TaggedMaterialSet set, Material material) {
		Long result = MaterialUnitOverrideRegister.OVERRIDES.get(set, material);
		return result != null ? result : -1;
	}
}
