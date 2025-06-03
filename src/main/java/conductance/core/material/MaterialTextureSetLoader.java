package conductance.core.material;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.Conductance;

@OnlyIn(Dist.CLIENT)
public final class MaterialTextureSetLoader {

	private record MaterialTextureSet(ResourceLocation id, @Nullable MaterialTextureSet parent) {

	}

	private static final MaterialTextureSetLoader INSTANCE = new MaterialTextureSetLoader();
	private static final ResourceLocation BASE_ID = Conductance.id("dull");
	private final Map<ResourceLocation, MaterialTextureSet> knownSets = new ConcurrentHashMap<>();

	public static void reload() {
		MaterialTextureSetLoader.INSTANCE.knownSets.clear();
		MaterialTextureSetLoader.INSTANCE.knownSets.put(MaterialTextureSetLoader.BASE_ID, new MaterialTextureSet(MaterialTextureSetLoader.BASE_ID, null));
		for (final Material material : CAPI.regs().materials()) {
			MaterialTextureSetLoader.INSTANCE.load(material.getTextureSet(), new LinkedList<>());
		}
	}

	private void load(final ResourceLocation set, final LinkedList<ResourceLocation> loadingStack) {
		if (this.knownSets.containsKey(set)) {
			return;
		}
		if (loadingStack.contains(set)) {
			final List<String> strings = new ArrayList<>();
			for (final ResourceLocation id : loadingStack.reversed()) {
				if (!strings.isEmpty() && id.equals(set)) {
					break;
				}
				strings.add(id.toString());
			}
			throw new IllegalStateException("Encountered cyclic dependencies while loading material texture sets: " + String.join(" -> ", strings));
		}
		loadingStack.addLast(set);
		final ResourceLocation expectedPath = set.withPath("models/material/%s.json"::formatted);
		Minecraft.getInstance().getResourceManager().getResource(expectedPath).ifPresentOrElse(resource -> {
			try (final InputStreamReader reader = new InputStreamReader(resource.open())) {
				final JsonObject json = GsonHelper.fromJson(CAPI.GSON, reader, JsonObject.class);
				final ResourceLocation parent = ResourceLocation.parse(GsonHelper.getAsString(json, "parent", MaterialTextureSetLoader.BASE_ID.toString()));
				this.load(parent, loadingStack);
				this.knownSets.put(set, new MaterialTextureSet(set, this.knownSets.get(parent)));
			} catch (final IOException e) {
				Conductance.LOGGER.error("Could not generate material texture set {}.", set, e);
			}
		}, () -> {
			Conductance.LOGGER.error("Could not generate material texture set{} because it does not exist.", set);
			Conductance.LOGGER.error("\tExpected path: {}", expectedPath);
		});
		loadingStack.removeLast();
	}

	public static Collection<ResourceLocation> getTextureSets() {
		return MaterialTextureSetLoader.INSTANCE.knownSets.keySet();
	}

	@Nullable
	public static ResourceLocation getParentSet(final ResourceLocation set) {
		final MaterialTextureSet setObject = MaterialTextureSetLoader.INSTANCE.knownSets.get(set);
		if (setObject == null || setObject.parent == null) {
			return null;
		}
		return setObject.parent.id;
	}

	private MaterialTextureSetLoader() {
	}
}
