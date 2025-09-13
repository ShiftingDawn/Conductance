package conductance.core.material;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import conductance.api.material.Material;
import conductance.api.material.event.RegisterMaterialEvent;
import conductance.api.material.event.RegisterMaterialFlagEvent;
import conductance.Conductance;

public final class MaterialCore {

	public static void initialize() {
		MaterialCore.initFlags();
		MaterialCore.initMaterials();
	}

	private static void initFlags() {
		Conductance.dispatch(RegisterMaterialFlagEvent.class, modid -> new RegisterMaterialFlagEventImpl((registryName, materialFlags) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final MaterialFlagImpl result = new MaterialFlagImpl(materialFlags);
			Conductance.REGISTRIES.register(Conductance.REGISTRIES.materialFlags(), registryKey, result);
			return result;
		}));
	}

	private static void initMaterials() {
		Conductance.dispatch(RegisterMaterialEvent.class, modid -> new RegisterMaterialEventImpl((registryName, periodicElement, builder) -> {
			final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
			final Material result = Util.make(new MaterialBuilderImpl(), builder).build();
			Conductance.REGISTRIES.register(Conductance.REGISTRIES.materials(), registryKey, result);
			return result;
		}));
	}

	private MaterialCore() {
	}
}
