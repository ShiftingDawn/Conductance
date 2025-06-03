package conductance.init.tier;

import conductance.api.NCMaterials;
import conductance.api.material.Material;

class EvComponentMap extends HvComponentMap {

	@Override
	public Material getPrimaryMaterial() {
		return NCMaterials.STAINLESS_STEEL;
	}

	@Override
	public Material getWireMaterial() {
		return NCMaterials.ALUMINIUM;
	}
}
