package conductance.core.material;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LenientJsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.material.Material;
import conductance.Conductance;

public final class MaterialTextureSetLoader {

	private static final MaterialTextureSetLoader INSTANCE = new MaterialTextureSetLoader();
	private static final ResourceLocation BASE_ID = Conductance.id("dull");
	private static final Codec<MaterialTextureSet> CODEC;
	private final Map<ResourceLocation, MaterialTextureSet> knownSets = new ConcurrentHashMap<>();

	public static void reload() {
		MaterialTextureSetLoader.INSTANCE.knownSets.clear();
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
			try (final BufferedReader reader = resource.openAsReader()) {
				final JsonElement element = LenientJsonParser.parse(reader);
				final MaterialTextureSet textureSet = MaterialTextureSetLoader.CODEC.decode(JsonOps.INSTANCE, element).getOrThrow().getFirst();
				if (!set.equals(MaterialTextureSetLoader.BASE_ID) && !textureSet.parent().equals(MaterialTextureSetLoader.BASE_ID)) {
					this.load(textureSet.parent(), loadingStack);
				}
				this.knownSets.put(set, textureSet);
			} catch (final Throwable e) {
				Conductance.LOGGER.error("Could not generate material texture set {}.", set, e);
			}
		}, () -> {
			Conductance.LOGGER.error("Could not generate material texture set {} because it does not exist.", set);
			Conductance.LOGGER.error("\tExpected path: {}", expectedPath);
		});
		loadingStack.removeLast();
	}

	public static Map<ResourceLocation, MaterialTextureSet> getTextureSets() {
		return Collections.unmodifiableMap(MaterialTextureSetLoader.INSTANCE.knownSets);
	}

	public static @Nullable ResourceLocation getParentSet(final ResourceLocation set) {
		if (set.equals(MaterialTextureSetLoader.BASE_ID)) {
			return null;
		}
		final MaterialTextureSet entry = MaterialTextureSetLoader.INSTANCE.knownSets.get(set);
		return entry != null ? entry.parent() : null;
	}

	static {
		CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.optionalFieldOf("parent", MaterialTextureSetLoader.BASE_ID).forGetter(MaterialTextureSet::parent),
			ResourceLocation.CODEC.optionalFieldOf("overlay").forGetter(MaterialTextureSet::overlay)
		).apply(instance, MaterialTextureSet::new));
	}

	private MaterialTextureSetLoader() {
	}
}
