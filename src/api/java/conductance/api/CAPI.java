package conductance.api;

import net.neoforged.fml.loading.FMLEnvironment;
import conductance.api.material.ResourceFinder;
import conductance.api.registry.NCRegistrate;
import conductance.api.registry.RegistryProvider;

public class CAPI {

	public static final String MOD_ID = "conductance";

	public static RegistryProvider REGS;
	public static ResourceFinder RESOURCE_FINDER;

	// https://github.com/GregTechCEu/GregTech-Modern/blob/8c12553866d39783e6ac3a902eba1cc30598e955/src/main/java/com/gregtechceu/gtceu/api/GTValues.java#L35C1-L35C42
	public static final long UNIT = 3628800;

	public static boolean isClient() {
		return FMLEnvironment.dist.isClient();
	}
}
