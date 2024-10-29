package conductance.init;

import conductance.api.plugin.MaterialTextureTypeRegister;
import static conductance.api.NCTextureTypes.DUST;

public final class ConductanceMaterialTextureTypes {

	public static void init(MaterialTextureTypeRegister register) {
		DUST = register.register("dust");
	}
}
