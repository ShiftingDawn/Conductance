package conductance.init.tier;

import conductance.api.NCMaterials;
import conductance.api.material.Material;

class MvComponentMap extends LvComponentMap {

	@Override
	public Material getPrimaryMaterial() {
		return NCMaterials.ALUMINIUM;
	}

	@Override
	public Material getWireMaterial() {
		return NCMaterials.COPPER;
	}
}
