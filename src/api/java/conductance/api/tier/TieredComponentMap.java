package conductance.api.tier;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import conductance.api.material.Material;
import static conductance.api.NCMaterialTaggedSets.FINE_WIRE;
import static conductance.api.NCMaterialTaggedSets.GEAR_SMALL;
import static conductance.api.NCMaterialTaggedSets.PLATE;
import static conductance.api.NCMaterialTaggedSets.RING;
import static conductance.api.NCMaterialTaggedSets.ROD;
import static conductance.api.NCMaterialTaggedSets.ROTOR;
import static conductance.api.util.MiscUtils.getItemTag;

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
		return getItemTag(PLATE, this.getMachineCasingPlateMaterial());
	}
	//endregion

	//region Hull
	protected Material getMachineHullPlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getMachineHullPlateItem() {
		return getItemTag(PLATE, this.getMachineHullPlateMaterial());
	}

	protected Material getMachineHullWireMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getMachineHullWireItem() {
		return getItemTag(FINE_WIRE, this.getMachineHullWireMaterial());
	}
	//endregion

	//region Motor
	protected Material getElectricMotorRodMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricMotorRodItem() {
		return getItemTag(ROD, this.getElectricMotorRodMaterial());
	}

	protected Material getElectricMotorMagneticRodMaterial() {
		return this.getMagneticMaterial();
	}

	public TagKey<Item> getElectricMotorMagneticRodItem() {
		return getItemTag(ROD, this.getElectricMotorMagneticRodMaterial());
	}

	protected Material getElectricMotorWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getElectricMotorWireItem() {
		return getItemTag(FINE_WIRE, this.getElectricMotorWireMaterial());
	}
	//endregion

	//region Piston
	protected Material getElectricPistonRodMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPistonRodItem() {
		return getItemTag(ROD, this.getElectricPistonRodMaterial());
	}

	protected Material getElectricPistonPlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPistonPlateItem() {
		return getItemTag(PLATE, this.getElectricPistonPlateMaterial());
	}

	protected Material getElectricPistonWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getElectricPistonWireItem() {
		return getItemTag(FINE_WIRE, this.getElectricPistonWireMaterial());
	}

	protected Material getElectricPistonSmallGearMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPistonSmallGearItem() {
		return getItemTag(GEAR_SMALL, this.getElectricPistonSmallGearMaterial());
	}
	//endregion

	//region Conveyor
	protected Material getConveyorModulePlateMaterial() {
		return this.getRubberMaterial();
	}

	public TagKey<Item> getConveyorModulePlateItem() {
		return getItemTag(PLATE, this.getConveyorModulePlateMaterial());
	}

	protected Material getConveyorModuleWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getConveyorModuleWireItem() {
		return getItemTag(FINE_WIRE, this.getConveyorModuleWireMaterial());
	}
	//endregion

	//region Pump
	protected Material getElectricPumpPlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPumpPlateItem() {
		return getItemTag(PLATE, this.getElectricPumpPlateMaterial());
	}

	protected Material getElectricPumpRingMaterial() {
		return this.getRubberMaterial();
	}

	public TagKey<Item> getElectricPumpRingItem() {
		return getItemTag(RING, this.getElectricPumpRingMaterial());
	}

	protected Material getElectricPumpRotorMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getElectricPumpRotorItem() {
		return getItemTag(ROTOR, this.getElectricPumpRotorMaterial());
	}

	protected Material getElectricPumpWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getElectricPumpWireItem() {
		return getItemTag(FINE_WIRE, this.getElectricPumpWireMaterial());
	}
	//endregion

	//region Arm
	protected Material getRobotArmWireMaterial() {
		return this.getWireMaterial();
	}

	public TagKey<Item> getRobotArmWireItem() {
		return getItemTag(FINE_WIRE, this.getRobotArmWireMaterial());
	}

	protected Material getRobotArmRodMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getRobotArmRodItem() {
		return getItemTag(ROD, this.getRobotArmRodMaterial());
	}

	protected Material getRobotArmPlateMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getRobotArmPlateItem() {
		return getItemTag(PLATE, this.getRobotArmPlateMaterial());
	}

	protected Material getRobotArmGearMaterial() {
		return this.getPrimaryMaterial();
	}

	public TagKey<Item> getRobotArmGearItem() {
		return getItemTag(GEAR_SMALL, this.getRobotArmGearMaterial());
	}
	//endregion
}
