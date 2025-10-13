package conductance.api.tier;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import conductance.api.CAPI;
import conductance.api.NCMaterials;
import conductance.api.material.Material;
import static conductance.api.NCMaterialGenerationHandlers.FINE_WIRE;
import static conductance.api.NCMaterialGenerationHandlers.GEAR_SMALL;
import static conductance.api.NCMaterialGenerationHandlers.GEM;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.RING;
import static conductance.api.NCMaterialGenerationHandlers.ROD;
import static conductance.api.NCMaterialGenerationHandlers.ROTOR;
import static conductance.api.NCMaterialGenerationHandlers.WIRE_1X;
import static conductance.api.NCMaterialGenerationHandlers.WIRE_4X;

@SuppressWarnings("CheckStyle")
public abstract class TieredComponentMap {

	public abstract Material getPrimaryMaterial();

	public abstract Material getMagneticMaterial();

	public abstract Material getWireMaterial();

	public abstract Material getRubberMaterial();

	//region Casing
	protected Material getMachineCasingPlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getMachineCasingPlateItem() {
		return CAPI.materials().getItemTag(this.getMachineCasingPlateMaterial(), PLATE);
	}
	//endregion

	//region Hull
	protected Material getMachineHullPlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getMachineHullPlateItem() {
		return CAPI.materials().getItemTag(this.getMachineHullPlateMaterial(), PLATE);
	}

	protected Material getMachineHullWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getMachineHullWireItem() {
		return CAPI.materials().getItemTag(this.getMachineHullWireMaterial(), WIRE_1X);
	}
	//endregion

	//region Motor
	protected Material getElectricMotorRodMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricMotorRodItem() {
		return CAPI.materials().getItemTag(this.getElectricMotorRodMaterial(), ROD);
	}

	protected Material getElectricMotorMagneticRodMaterial() {
		return this.getMagneticMaterial();
	}

	public TagKey<Item> getElectricMotorMagneticRodItem() {
		return CAPI.materials().getItemTag(this.getElectricMotorMagneticRodMaterial(), ROD);
	}

	protected Material getElectricMotorFineWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getElectricMotorFineWireItem() {
		return CAPI.materials().getItemTag(this.getElectricMotorFineWireMaterial(), FINE_WIRE);
	}

	protected Material getElectricMotorWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getElectricMotorWireItem() {
		return CAPI.materials().getItemTag(this.getElectricMotorWireMaterial(), WIRE_1X);
	}
	//endregion

	//region Piston
	protected Material getElectricPistonRodMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPistonRodItem() {
		return CAPI.materials().getItemTag(this.getElectricPistonRodMaterial(), ROD);
	}

	protected Material getElectricPistonPlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPistonPlateItem() {
		return CAPI.materials().getItemTag(this.getElectricPistonPlateMaterial(), PLATE);
	}

	protected Material getElectricPistonWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getElectricPistonWireItem() {
		return CAPI.materials().getItemTag(this.getElectricPistonWireMaterial(), WIRE_1X);
	}

	protected Material getElectricPistonSmallGearMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPistonSmallGearItem() {
		return CAPI.materials().getItemTag(this.getElectricPistonSmallGearMaterial(), GEAR_SMALL);
	}
	//endregion

	//region Conveyor
	protected Material getConveyorModulePlateMaterial() {
		return this.getRubberMaterial();
	}

	public TagKey<Item> getConveyorModulePlateItem() {
		return CAPI.materials().getItemTag(this.getConveyorModulePlateMaterial(), PLATE);
	}

	protected Material getConveyorModuleWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getConveyorModuleWireItem() {
		return CAPI.materials().getItemTag(this.getConveyorModuleWireMaterial(), WIRE_1X);
	}
	//endregion

	//region Pump
	protected Material getElectricPumpPlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPumpPlateItem() {
		return CAPI.materials().getItemTag(this.getElectricPumpPlateMaterial(), PLATE);
	}

	protected Material getElectricPumpRingMaterial() {
		return this.getRubberMaterial();
	}

	public TagKey<Item> getElectricPumpRingItem() {
		return CAPI.materials().getItemTag(this.getElectricPumpRingMaterial(), RING);
	}

	protected Material getElectricPumpRotorMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPumpRotorItem() {
		return CAPI.materials().getItemTag(this.getElectricPumpRotorMaterial(), ROTOR);
	}

	protected Material getElectricPumpWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getElectricPumpWireItem() {
		return CAPI.materials().getItemTag(this.getElectricPumpWireMaterial(), WIRE_1X);
	}
	//endregion

	//region Arm
	protected Material getRobotArmWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getRobotArmWireItem() {
		return CAPI.materials().getItemTag(this.getRobotArmWireMaterial(), WIRE_1X);
	}

	protected Material getRobotArmRodMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getRobotArmRodItem() {
		return CAPI.materials().getItemTag(this.getRobotArmRodMaterial(), ROD);
	}

	protected Material getRobotArmPlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getRobotArmPlateItem() {
		return CAPI.materials().getItemTag(this.getRobotArmPlateMaterial(), PLATE);
	}

	protected Material getRobotArmGearMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getRobotArmGearItem() {
		return CAPI.materials().getItemTag(this.getRobotArmGearMaterial(), GEAR_SMALL);
	}
	//endregion

	//region Induction Coil
	protected Material getInductionCoilMagneticRodMaterial() {
		return this.getMagneticMaterial();
	}

	public TagKey<Item> getInductionCoilMagneticRodItem() {
		return CAPI.materials().getItemTag(this.getInductionCoilMagneticRodMaterial(), ROD);
	}

	protected Material getInductionCoilFineWireMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getInductionCoilFineWireItem() {
		return CAPI.materials().getItemTag(this.getInductionCoilFineWireMaterial(), FINE_WIRE);
	}
	//endregion

	//region Machines
	protected Material getMachinePlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getMachinePlateItem() {
		return CAPI.materials().getItemTag(this.getMachinePlateMaterial(), PLATE);
	}

	protected Material getMachineWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getMachineWireItem() {
		return CAPI.materials().getItemTag(this.getMachineWireMaterial(), WIRE_1X);
	}

	protected Material getMachineHeatingWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getMachineHeatingWireItem() {
		return CAPI.materials().getItemTag(this.getMachineHeatingWireMaterial(), WIRE_4X);
	}

	protected Material getMachineRotorMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getMachineRotorItem() {
		return CAPI.materials().getItemTag(this.getMachineRotorMaterial(), ROTOR);
	}

	protected Material getMachineCuttingPartMaterial() {
		return NCMaterials.DIAMOND;
	}

	public TagKey<Item> getMachineCuttingPartItem() {
		return CAPI.materials().getItemTag(this.getMachineCuttingPartMaterial(), GEM);
	}
	//endregion
}
