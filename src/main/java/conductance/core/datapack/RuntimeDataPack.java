package conductance.core.datapack;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.Conductance;
import conductance.Config;
import conductance.core.AbstractRuntimePack;
import conductance.core.register.MaterialRegistry;

public final class RuntimeDataPack extends AbstractRuntimePack {

	private static final Set<String> KNOWN_NAMESPACES = new ObjectOpenHashSet<>(Sets.newHashSet(CAPI.MOD_ID, ResourceLocation.DEFAULT_NAMESPACE, "c", "neoforge"));
	private static final Map<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();

	public RuntimeDataPack(final PackLocationInfo location) {
		super(location, PackType.SERVER_DATA);
	}

	@Override
	protected Map<ResourceLocation, byte[]> getData() {
		return RuntimeDataPack.DATA;
	}

	@Override
	public Set<String> getKnownNamespaces() {
		return RuntimeDataPack.KNOWN_NAMESPACES;
	}

	private static boolean shouldDumpAssets() {
		return Config.debug_dumpRuntimeDataPack.getAsBoolean();
	}

	static void reset() {
		RuntimeDataPack.DATA.clear();

		// Register built-in listeners
		RuntimeDataPack.load();
	}

	private static void load() {
		final long sysTime = System.currentTimeMillis();
		MaterialRegistry.INSTANCE.reload();
		Conductance.LOGGER.info("Conductance reloaded RuntimeResourcePack in {}ms", System.currentTimeMillis() - sysTime);
	}

	private static void writeJson(final ResourceLocation id, @Nullable final String subDirectory, final JsonElement json) {
		if (RuntimeDataPack.shouldDumpAssets()) {
			AbstractRuntimePack.dump("data", id, subDirectory, json);
		}
	}
}
