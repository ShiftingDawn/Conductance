package conductance.core.register;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import conductance.api.material.Material;
import conductance.api.material.MaterialOreType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.plugin.MaterialOverrideMap;

public final class MaterialOverrideRegister implements MaterialOverrideMap {

	private static final Table<TaggedMaterialSet, Material, ItemLike[]> OVERRIDES = HashBasedTable.create();
	private static final Table<MaterialOreType, Material, Block[]> ORES = HashBasedTable.create();

	@Override
	public void add(final TaggedMaterialSet set, final Material material, final ItemLike... overrides) {
		MaterialOverrideRegister.OVERRIDES.put(set, material, overrides);
	}

	@Override
	public void add(final MaterialOreType oreType, final Material material, final Block... overrides) {
		MaterialOverrideRegister.ORES.put(oreType, material, overrides);
	}

	public static boolean has(final TaggedMaterialSet set, final Material material) {
		return MaterialOverrideRegister.OVERRIDES.contains(set, material);
	}

	public static boolean has(final MaterialOreType oreType, final Material material) {
		return MaterialOverrideRegister.ORES.contains(oreType, material);
	}

	static Table<TaggedMaterialSet, Material, ItemLike[]> getOverrides() {
		return MaterialOverrideRegister.OVERRIDES;
	}

	static Table<MaterialOreType, Material, Block[]> getOreOverrides() {
		return MaterialOverrideRegister.ORES;
	}
}
