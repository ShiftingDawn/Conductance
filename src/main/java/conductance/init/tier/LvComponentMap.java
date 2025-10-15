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

	@Override
	public Material getPlasticMaterial() {
		return NCMaterials.GLUE;
	}

	@Override
	protected Material getElectricMotorFineWireMaterial() {
		return NCMaterials.COPPER;
	}

	@Override
	protected Material getElectricPumpRotorMaterial() {
		return NCMaterials.TIN;
	}

	@Override
	public Material getMachineRotorMaterial() {
		return NCMaterials.TIN;
	}

	@Override
	protected Material getMachineElectroWireMaterial() {
		return NCMaterials.COPPER;
	}
}
