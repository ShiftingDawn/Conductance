package conductance.core.material;

import java.util.Objects;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import conductance.api.NCMaterialTraits;
import conductance.api.NCTextureTypes;
import conductance.api.material.MaterialOreType;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class MaterialOreModelHandler {

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		MaterialRegistryImpl.INSTANCE.getGeneratedBlockRegistry().rowMap().entrySet().stream().filter(entry -> entry.getKey().getOreType() != null)
				.forEach(entry -> entry.getValue().forEach((material, blockEntry) -> {
					final ResourceLocation blockId = blockEntry.getId();
					final MaterialOreType oreType = entry.getKey().getOreType();
					if (blockEntry.getDefaultState().hasProperty(RotatedPillarBlock.AXIS)) {
						event.insertBlockState(blockId, BlockModelGenerators.createAxisAlignedPillarBlock(blockEntry.get(), blockId.withPrefix("block/")));
					} else {
						event.insertBlockState(blockId, BlockModelGenerators.createSimpleBlock(blockEntry.get(), blockId.withPrefix("block/")));
					}
					final String oreTexture = NCTextureTypes.ORE.getTexture(material.getTextureSet(), null, null).value().toString();
					final boolean emissive = Objects.requireNonNull(material.get(NCMaterialTraits.ORE), "This should not happen").isEmissive();
					event.addBlockModel(blockId, builder -> builder.parent("block/cube")
							.particle(oreTexture)
							.composite(composite -> composite
									.child("bearer", child -> child.renderType("solid").parent(oreType.getBearingBlockModel()))
									.child("ore_overlay", child -> child.parent("block/block").renderType("cutout_mipped")
											.particle(oreTexture)
											.element(element -> element
													.from(0, 0, 0)
													.to(16, 16, 16)
													.shade(!emissive)
													.faces((f, b) -> b.particle().tintIndex(emissive ? -101 : 1), true)
											)
									)
									.itemRenderOrder("bearer", "ore_overlay")
							)
					);
					event.addItemModelDelegate(blockEntry.get());
				}));
	}
}
