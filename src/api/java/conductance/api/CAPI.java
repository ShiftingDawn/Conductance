package conductance.api;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.loading.FMLEnvironment;
import conductance.api.material.Material;
import conductance.api.material.ResourceFinder;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.registry.NCRegistrate;
import conductance.api.registry.RegistryProvider;
import conductance.api.registry.TaggedSetRegistry;
import conductance.api.registry.TranslationRegistry;
import conductance.api.util.TagHelper;

public final class CAPI {

	public static final String MOD_ID = "conductance";

	public static RegistryProvider REGS;
	public static ResourceFinder RESOURCE_FINDER;
	public static TaggedSetRegistry<Material, TaggedMaterialSet> MATERIALS;
	public static TranslationRegistry TRANSLATIONS;

	// https://github.com/GregTechCEu/GregTech-Modern/blob/8c12553866d39783e6ac3a902eba1cc30598e955/src/main/java/com/gregtechceu/gtceu/api/GTValues.java#L35C1-L35C42
	public static final long UNIT = 3628800;

	public static boolean isClient() {
		return FMLEnvironment.dist.isClient();
	}

	@SuppressWarnings("unchecked")
	public static class Tags {

		public static final TagKey<Block> NEEDS_WOOD_TOOL = TagHelper.blockTagForLoader("needs_wood_tool");
		public static final TagKey<Block> NEEDS_NETHERITE_TOOL = TagHelper.blockTagForLoader("needs_netherite_tool");

		public static final TagKey<Block>[] MINING_TOOL_TAGS = new TagKey[] { Tags.NEEDS_WOOD_TOOL, BlockTags.NEEDS_STONE_TOOL, BlockTags.NEEDS_IRON_TOOL, BlockTags.NEEDS_DIAMOND_TOOL, Tags.NEEDS_NETHERITE_TOOL };
	}
}
