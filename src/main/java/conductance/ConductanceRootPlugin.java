package conductance;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCMaterials;
import conductance.api.plugin.MaterialFlagRegister;
import conductance.api.plugin.MaterialOreTypeRegister;
import conductance.api.plugin.MaterialOverrideMap;
import conductance.api.plugin.MaterialRegister;
import conductance.api.plugin.MaterialTaggedSetRegister;
import conductance.api.plugin.MaterialTextureSetRegister;
import conductance.api.plugin.MaterialTextureTypeRegister;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.api.plugin.PeriodicElementBuilder;
import conductance.init.ConductanceMaterialFlags;
import conductance.init.ConductanceMaterialOreTypes;
import conductance.init.ConductanceMaterialTaggedSets;
import conductance.init.ConductanceMaterialTextureSets;
import conductance.init.ConductanceMaterialTextureTypes;
import conductance.init.ConductanceMaterialTraits;
import conductance.init.ConductanceMaterials;
import conductance.init.ConductancePeriodicElements;

@ConductancePlugin(modid = CAPI.MOD_ID)
public final class ConductanceRootPlugin implements IConductancePlugin {

	@Override
	public void registerPeriodicElements(PeriodicElementBuilder builder) {
		ConductancePeriodicElements.init(builder);
	}

	@Override
	public void registerMaterialTextureTypes(MaterialTextureTypeRegister register) {
		ConductanceMaterialTextureTypes.init(register);
	}

	@Override
	public void registerMaterialTextureSets(MaterialTextureSetRegister register) {
		ConductanceMaterialTextureSets.init(register);
	}

	@Override
	public void registerMaterialTraits(MaterialTraitRegister register) {
		ConductanceMaterialTraits.init(register);
	}

	@Override
	public void registerMaterialFlags(MaterialFlagRegister register) {
		ConductanceMaterialFlags.init(register);
	}

	@Override
	public void registerMaterialOreTypes(MaterialOreTypeRegister register) {
		ConductanceMaterialOreTypes.init(register);
	}

	@Override
	public void registerMaterialTaggedSets(MaterialTaggedSetRegister register) {
		ConductanceMaterialTaggedSets.init(register);
	}

	@Override
	public void registerMaterials(MaterialRegister register) {
		ConductanceMaterials.init(register);
	}

	@Override
	public void registerMaterialOverrides(MaterialOverrideMap overrides) {
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, NCMaterials.IRON, Blocks.IRON_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, NCMaterials.GOLD, Blocks.GOLD_BLOCK);
		overrides.add(NCMaterialTaggedSets.STORAGE_BLOCK, NCMaterials.COPPER, Blocks.COPPER_BLOCK);
		overrides.add(NCMaterialTaggedSets.INGOT, NCMaterials.IRON, Items.IRON_INGOT);
		overrides.add(NCMaterialTaggedSets.INGOT, NCMaterials.GOLD, Items.GOLD_INGOT);
		overrides.add(NCMaterialTaggedSets.INGOT, NCMaterials.COPPER, Items.COPPER_INGOT);
		overrides.add(NCMaterialTaggedSets.NUGGET, NCMaterials.IRON, Items.IRON_NUGGET);
		overrides.add(NCMaterialTaggedSets.NUGGET, NCMaterials.GOLD, Items.GOLD_NUGGET);
		//		overrides.add(NCMaterialTaggedSets.RAW_ORE, NCMaterials.IRON, Items.RAW_IRON);
		//		overrides.add(NCMaterialTaggedSets.RAW_ORE, NCMaterials.GOLD, Items.RAW_GOLD);
		//		overrides.add(NCMaterialTaggedSets.RAW_ORE, NCMaterials.COPPER, Items.RAW_COPPER);
		//		overrides.add(NCMaterialTaggedSets.RAW_ORE_BLOCK, NCMaterials.IRON, Blocks.RAW_IRON_BLOCK);
		//		overrides.add(NCMaterialTaggedSets.RAW_ORE_BLOCK, NCMaterials.GOLD, Blocks.RAW_GOLD_BLOCK);
		//		overrides.add(NCMaterialTaggedSets.RAW_ORE_BLOCK, NCMaterials.COPPER, Blocks.RAW_COPPER_BLOCK);
	}
}
