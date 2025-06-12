package conductance.init.tier;

import conductance.api.NCMaterials;
import conductance.api.material.Material;
import conductance.api.tier.TieredComponentMap;

class LvComponentMap extends TieredComponentMap {

	@Override
	public Material getPrimaryMaterial() {
		return NCMaterials.STEEL;
	}

	@Override
	public Material getMagneticMaterial() {
		return NCMaterials.MAGNETIC_IRON;
	}

	@Override
	public Material getWireMaterial() {
		return NCMaterials.TIN;
	}

	@Override
	public Material getRubberMaterial() {
		return NCMaterials.RUBBER;
	}
}
