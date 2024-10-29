package conductance.api;

import net.neoforged.fml.loading.FMLEnvironment;
import conductance.api.material.Material;
import conductance.api.material.ResourceFinder;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.RegistryProvider;
import conductance.api.registry.TaggedSetRegistry;
import conductance.api.registry.TranslationRegistry;

public final class CAPI {

	public static final String MOD_ID = "conductance";

	// https://github.com/GregTechCEu/GregTech-Modern/blob/8c12553866d39783e6ac3a902eba1cc30598e955/src/main/java/com/gregtechceu/gtceu/api/GTValues.java#L35C1-L35C42
	public static final long UNIT = 3628800;

	private static RegistryProvider registryProvider;
	private static ResourceFinder resourceFinder;
	private static TaggedSetRegistry<Material, TaggedMaterialSet> materialRegistry;
	private static TranslationRegistry translationRegistry;

	public static RegistryProvider regs() {
		return CAPI.registryProvider;
	}

	public static ResourceFinder resourceFinder() {
		return CAPI.resourceFinder;
	}

	public static TaggedSetRegistry<Material, TaggedMaterialSet> materials() {
		return CAPI.materialRegistry;
	}

	public static TranslationRegistry translations() {
		return CAPI.translationRegistry;
	}

	public static boolean isClient() {
		return FMLEnvironment.dist.isClient();
	}

	private CAPI() {
	}
}
