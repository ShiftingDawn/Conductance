package conductance.runtimepack.client;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

@RequiredArgsConstructor
public class MaterialFluidModelHandler {

	private static final Set<MaterialFluidModelHandler> MODELS = new HashSet<>();

	private final Fluid fluid;
	private final Material material;
	private final TaggedMaterialSet taggedSet;

	public static void add(final Fluid fluid, final Material material, final TaggedMaterialSet taggedSet) {
		MaterialFluidModelHandler.MODELS.add(new MaterialFluidModelHandler(fluid, material, taggedSet));
	}

	static void reload() {
		MaterialFluidModelHandler.MODELS.forEach(model -> {
			final ResourceLocation bucketId = BuiltInRegistries.ITEM.getKey(model.fluid.getBucket());
			RuntimeResourcePack.addItemModel(bucketId, () -> Util.make(new JsonObject(), json -> {
				json.addProperty("parent", "neoforge:item/bucket_drip");
				json.addProperty("loader", "neoforge:fluid_container");
				json.addProperty("flip_gas", model.fluid.getFluidType().isLighterThanAir());
				json.addProperty("fluid", BuiltInRegistries.FLUID.getKey(model.fluid).toString());
			}));
		});
	}
}
