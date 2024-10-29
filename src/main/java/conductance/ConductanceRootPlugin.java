package conductance;

import conductance.api.CAPI;
import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.plugin.MaterialFlagRegister;
import conductance.api.plugin.MaterialRegister;
import conductance.api.plugin.MaterialTaggedSetRegister;
import conductance.api.plugin.MaterialTextureSetRegister;
import conductance.api.plugin.MaterialTextureTypeRegister;
import conductance.api.plugin.MaterialTraitRegister;
import conductance.api.plugin.PeriodicElementBuilder;
import conductance.init.ConductanceMaterialFlags;
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
	public void registerMaterialTaggedSets(MaterialTaggedSetRegister register) {
		ConductanceMaterialTaggedSets.init(register);
	}

	@Override
	public void registerMaterials(MaterialRegister register) {
		ConductanceMaterials.init(register);
	}
}
