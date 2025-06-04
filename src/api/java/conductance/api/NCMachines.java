package conductance.api;

import java.util.Map;
import conductance.api.machine.MachineType;
import conductance.api.tier.Tier;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMachines {

	public static MachineType<?> STEAM_BOILER_SOLID_FUEL;
	public static MachineType<?> STEAM_BOILER_LIQUID_FUEL;

	public static Map<Tier, MachineType<?>> MACHINE_HULL;

	public static MachineType<?> LV_STEAM_TURBINE;
	public static MachineType<?> MV_STEAM_TURBINE;
	public static MachineType<?> HV_STEAM_TURBINE;
	public static MachineType<?> EV_STEAM_TURBINE;

	public static Map<Tier, MachineType<?>> WIREMILL;
	public static Map<Tier, MachineType<?>> BENDING_MACHINE;
	public static Map<Tier, MachineType<?>> PULVERIZER;
	public static Map<Tier, MachineType<?>> CUTTING_MACHINE;
	public static Map<Tier, MachineType<?>> LATHE;
	public static Map<Tier, MachineType<?>> COMPRESSOR;

	private NCMachines() {
	}
}
