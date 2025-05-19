package conductance.core.register;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.RegisterMaterialUnitOverrideEvent;

public final class MaterialUnitOverrideRegister implements RegisterMaterialUnitOverrideEvent.MaterialUnitOverrideMap {

	private static final Table<TaggedMaterialSet, Material, Long> OVERRIDES = HashBasedTable.create();

	@Override
	public void add(final TaggedMaterialSet set, final Material material, final long value) {
		MaterialUnitOverrideRegister.OVERRIDES.put(set, material, value);
	}

	public static long get(final TaggedMaterialSet set, final Material material) {
		final Long result = MaterialUnitOverrideRegister.OVERRIDES.get(set, material);
		return result != null ? result : -1;
	}
}
