package conductance.core.tier;

import conductance.api.NCMaterials;
import conductance.api.material.Material;
import conductance.api.tier.TieredComponentMap;

final class EmptyTieredComponentMap extends TieredComponentMap {

	public static final EmptyTieredComponentMap INSTANCE = new EmptyTieredComponentMap();

	private EmptyTieredComponentMap() {
	}

	@Override
	public Material getPrimaryMaterial() {
		return NCMaterials.AIR;
	}

	@Override
	public Material getMagneticMaterial() {
		return NCMaterials.AIR;
	}

	@Override
	public Material getWireMaterial() {
		return NCMaterials.AIR;
	}

	@Override
	public Material getRubberMaterial() {
		return NCMaterials.AIR;
	}
}
