package conductance.init;

import conductance.api.plugin.MaterialTextureTypeRegister;
import static conductance.api.NCTextureTypes.*;

public final class ConductanceMaterialTextureTypes {

	public static void init(MaterialTextureTypeRegister register) {
		DUST = register.register("dust");
		INGOT = register.register("ingot");
		GEM = register.register("gem");

		STORAGE_BLOCK = register.register("block");

		LIQUID = register.register("liquid");
		GAS = register.register("gas");
		PLASMA = register.register("plasma");
	}
}
