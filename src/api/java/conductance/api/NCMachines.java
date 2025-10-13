package conductance.api;

import java.util.Map;
import conductance.api.machine.MachineType;
import conductance.api.machine.multi.MultiMachineType;
import conductance.api.tier.Tier;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMachines {

	public static MachineType<?> STEAM_SOLID_FUEL_BOILER;

	public static Map<Tier, MachineType<?>> STEAM_TURBINES;

	public static Map<Tier, MachineType<?>> MACHINE_HULL;
	public static Map<Tier, MachineType<?>> BENDING_MACHINE;
	public static Map<Tier, MachineType<?>> PULVERIZER;
	public static Map<Tier, MachineType<?>> EXTRUDER;
	public static Map<Tier, MachineType<?>> WIREMILL;
	public static Map<Tier, MachineType<?>> LATHE;
	public static Map<Tier, MachineType<?>> EXTRACTOR;
	public static Map<Tier, MachineType<?>> COMPRESSOR;
	public static Map<Tier, MachineType<?>> CUTTING_MACHINE;

	public static Map<Tier, MachineType<?>> INPUT_BUSES;
	public static Map<Tier, MachineType<?>> OUTPUT_BUSES;
	public static Map<Tier, MachineType<?>> INPUT_HATCHES;
	public static Map<Tier, MachineType<?>> OUTPUT_HATCHES;
	public static Map<Tier, MachineType<?>> ENERGY_HATCHES;
	public static Map<Tier, MachineType<?>> DYNAMO_HATCHES;

	public static MultiMachineType<?> LARGE_BRONZE_BOILER;
	public static MultiMachineType<?> ELECTRIC_BLAST_FURNACE;

	private NCMachines() {
	}
}
