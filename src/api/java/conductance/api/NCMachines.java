package conductance.api;

import java.util.Map;
import conductance.api.machine.MachineType;
import conductance.api.tier.Tier;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMachines {

	public static Map<Tier, MachineType<?>> BENDING_MACHINE;
	public static Map<Tier, MachineType<?>> PULVERIZER;
	public static Map<Tier, MachineType<?>> EXTRUDER;
	public static Map<Tier, MachineType<?>> WIREMILL;
	public static Map<Tier, MachineType<?>> LATHE;
	public static Map<Tier, MachineType<?>> EXTRACTOR;
	public static Map<Tier, MachineType<?>> COMPRESSOR;
	public static Map<Tier, MachineType<?>> CUTTING_MACHINE;

	private NCMachines() {
	}
}
