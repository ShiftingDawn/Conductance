package conductance.api;

import java.util.Map;
import conductance.api.machine.MachineType;
import conductance.api.util.tier.Tier;

@SuppressWarnings("NotNullFieldNotInitialized")
public final class NCMachines {

	public static MachineType<?> STEAM_BOILER_SOLID_FUEL;
	public static MachineType<?> STEAM_BOILER_LIQUID_FUEL;

	public static Map<Tier, MachineType<?>> BENDERS;

	private NCMachines() {
	}
}
