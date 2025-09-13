package conductance.api;

import java.util.Objects;
import net.minecraft.util.RandomSource;
import net.neoforged.fml.loading.FMLEnvironment;
import conductance.api.registry.RegistryProvider;

@SuppressWarnings({"unused", "NotNullFieldNotInitialized"})
public final class CAPI {

	public static final String MOD_ID = "conductance";
	public static final long UNIT = 3628800;
	public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();

	private static RegistryProvider registryProvider;

	public static RegistryProvider regs() {
		return Objects.requireNonNull(CAPI.registryProvider, "CAPI::regs called too early!");
	}

	public static boolean isClient() {
		return FMLEnvironment.dist.isClient();
	}

	private CAPI() {
	}
}
