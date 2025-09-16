package conductance.api;

import java.util.Objects;
import net.minecraft.util.RandomSource;
import net.neoforged.fml.loading.FMLEnvironment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import conductance.api.material.MaterialRegistry;
import conductance.api.registry.RegistryProvider;
import conductance.api.resource.ResourceFinder;

@SuppressWarnings({"unused", "NotNullFieldNotInitialized"})
public final class CAPI {

	public static final String MOD_ID = "conductance";
	public static final long UNIT = 3628800;
	public static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();
	public static final Gson GSON;

	private static RegistryProvider registryProvider;
	private static MaterialRegistry materialRegistry;
	private static ResourceFinder resourceFinder;

	public static RegistryProvider regs() {
		return Objects.requireNonNull(CAPI.registryProvider, "CAPI::regs called too early!");
	}

	public static MaterialRegistry materials() {
		return Objects.requireNonNull(CAPI.materialRegistry, "CAPI::materials called too early!");
	}

	public static ResourceFinder resourceFinder() {
		return Objects.requireNonNull(CAPI.resourceFinder, "CAPI::resourceFinder called too early!");
	}

	public static boolean isClient() {
		return FMLEnvironment.dist.isClient();
	}

	static {
		GSON = new GsonBuilder().create();
	}

	private CAPI() {
	}
}
