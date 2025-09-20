package conductance.init.tier;

import conductance.api.NCMaterials;
import conductance.api.material.Material;
import conductance.api.tier.TieredComponentMap;

class LvComponentMap extends TieredComponentMap {

	@Override
	public Material getPrimaryMaterial() {
		return NCMaterials.IRON;
	}

	@Override
	public Material getMagneticMaterial() {
		//TODO magnetic ironn
		return NCMaterials.IRON;
	}

	@Override
	public Material getWireMaterial() {
		return NCMaterials.TIN;
	}

	@Override
	public Material getRubberMaterial() {
		//TODO rubber
		return NCMaterials.COPPER;
	}
}
