package conductance.init.tier;

import conductance.api.NCMaterials;
import conductance.api.material.Material;

class HvComponentMap extends MvComponentMap {

	@Override
	public Material getPrimaryMaterial() {
		return NCMaterials.ALUMINIUM;
	}

	@Override
	public Material getWireMaterial() {
		return NCMaterials.GOLD;
	}

	@Override
	public Material getMagneticMaterial() {
		return NCMaterials.MAGNETIC_STEEL;
	}
}
