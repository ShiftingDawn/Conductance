package conductance;

import conductance.api.ConductancePlugin;
import conductance.api.IConductancePlugin;
import conductance.api.plugin.MaterialTextureSetRegister;
import conductance.api.plugin.MaterialTextureTypeRegister;
import conductance.api.plugin.PeriodicElementBuilder;
import conductance.init.ConductanceMaterialTextureSets;
import conductance.init.ConductanceMaterialTextureTypes;
import conductance.init.ConductancePeriodicElements;

@ConductancePlugin(modid = Conductance.MODID)
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
}
