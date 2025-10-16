package conductance.init.tier;

import conductance.api.NCMaterials;
import conductance.api.material.Material;
import conductance.api.tier.TieredComponentMap;

class MvComponentMap extends TieredComponentMap {

	@Override
	public Material getPrimaryMaterial() {
		return NCMaterials.ALUMINIUM;
	}

	@Override
	public Material getMagneticMaterial() {
		return NCMaterials.MAGNETIC_STEEL;
	}

	@Override
	public Material getWireMaterial() {
		return NCMaterials.COPPER;
	}

	@Override
	public Material getRubberMaterial() {
		return NCMaterials.RUBBER;
	}

	@Override
	public Material getPlasticMaterial() {
		//TODO PolyEthylene
		return NCMaterials.GLUE;
	}

	@Override
	protected Material getElectricMotorFineWireMaterial() {
		return NCMaterials.GOLD;
	}

	@Override
	protected Material getElectricPumpRotorMaterial() {
		return NCMaterials.ALUMINIUM;
	}

	@Override
	public Material getMachineRotorMaterial() {
		return NCMaterials.ALUMINIUM;
	}

	@Override
	protected Material getMachineElectroWireMaterial() {
		return NCMaterials.COPPER;
	}
}
