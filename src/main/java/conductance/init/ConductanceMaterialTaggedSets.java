package conductance.init;

import conductance.api.CAPI;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCTextureTypes;
import conductance.api.plugin.MaterialTaggedSetRegister;

public class ConductanceMaterialTaggedSets {

	public static void init(MaterialTaggedSetRegister register) {
		NCMaterialTaggedSets.DUST = register.register("dust").addTagCommon("dusts/%s").addTagCommonUnformatted("dusts").unitValue(CAPI.UNIT).generateItems(true).textureType(NCTextureTypes.DUST).build();
	}
}
