package conductance.init;

import conductance.api.plugin.MaterialTextureTypeRegister;
import static conductance.api.NCTextureTypes.DUST;
import static conductance.api.NCTextureTypes.GEM;
import static conductance.api.NCTextureTypes.INGOT;

public final class ConductanceMaterialTextureTypes {

	public static void init(MaterialTextureTypeRegister register) {
		DUST = register.register("dust");
		INGOT = register.register("ingot");
		GEM = register.register("gem");
	}
}
