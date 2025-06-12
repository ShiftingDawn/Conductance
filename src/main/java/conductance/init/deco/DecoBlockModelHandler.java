package conductance.init.deco;

import java.util.Map;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import com.tterrag.registrate.util.entry.BlockEntry;
import conductance.api.CAPI;
import conductance.api.NCDecoration;
import conductance.api.plugin.ConductancePluginListener;
import conductance.api.plugin.EventListener;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;

@ConductancePluginListener(modid = Conductance.MODID)
final class DecoBlockModelHandler {

	@EventListener(priority = -100)
	private static void onAddRuntimeModels(final AddRuntimeModelEvent event) {
		NCDecoration.LUX.forEach((dyeColor, blockEntry) -> {
			event.addBlockState(blockEntry.getId(), builder -> builder.simple(variant -> variant.model(blockEntry.getId().withPrefix("block/"))));
			event.addBlockModel(blockEntry.getId(), builder -> builder
					.particle(Conductance.id("block/decoration/lux/%s".formatted(dyeColor.getSerializedName())))
					.element(element -> element.lightEmission(15).shade(false).faces((face, b) -> b.particle().tintIndex(-100), true))
			);
			event.addItemModelDelegate(blockEntry.get());
		});
		DecoBlockModelHandler.circuit(event, NCDecoration.LIGHT_CIRCUIT, "light");
		DecoBlockModelHandler.circuit(event, NCDecoration.DARK_CIRCUIT, "dark");

		DecoBlockModelHandler.tiles(event, NCDecoration.TILES_1, "1");
		DecoBlockModelHandler.tiles(event, NCDecoration.TILES_2, "2");
		DecoBlockModelHandler.tiles(event, NCDecoration.TILES_3, "3");
		DecoBlockModelHandler.tiles(event, NCDecoration.TILES_4, "4");
		DecoBlockModelHandler.tiles(event, NCDecoration.TILES_5, "5");
		DecoBlockModelHandler.tiles(event, NCDecoration.TILES_6, "6");

		DecoBlockModelHandler.cubeAll(event, NCDecoration.ME_BLOCK, "me");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.ME_BLOCK_ACTIVE, "me_active");

		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_LIGHT, "concrete/light");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_LIGHT_CRACKED, "concrete/light_cracked");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_LIGHT_BRICKS, "concrete/light_bricks");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_LIGHT_SMOOTH, "concrete/light_smooth");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_LIGHT_BORDERED, "concrete/light_bordered");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_DARK, "concrete/dark");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_DARK_CRACKED, "concrete/dark_cracked");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_DARK_BRICKS, "concrete/dark_bricks");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_DARK_SMOOTH, "concrete/dark_smooth");
		DecoBlockModelHandler.cubeAll(event, NCDecoration.CONCRETE_DARK_BORDERED, "concrete/dark_bordered");
	}

	private static void circuit(final AddRuntimeModelEvent event, final Map<DyeColor, BlockEntry<? extends Block>> map, final String type) {
		map.forEach((dyeColor, blockEntry) -> {
			event.addBlockState(blockEntry.getId(), builder -> builder.simple(variant -> variant.model(blockEntry.getId().withPrefix("block/"))));
			event.addBlockModel(blockEntry.getId(), builder -> builder
					.particle(Conductance.id("block/decoration/emissive/circuit_overlay"))
					.texture("base", Conductance.id("block/decoration/emissive/%s_circuit_base".formatted(type)))
					.renderType("cutout_mipped")
					.element(element -> element.faces((face, b) -> b.texture("base"), true))
					.element(element -> element.lightEmission(15).shade(false)
							.faces((face, b) -> b.particle().tintIndex(-100), true)
							.neoforgeData(data -> data.color(CAPI.COLORS.get(dyeColor)))
					)
			);
			event.addItemModelDelegate(blockEntry.get());
		});
	}

	private static void tiles(final AddRuntimeModelEvent event, final Map<DyeColor, BlockEntry<? extends Block>> map, final String type) {
		map.forEach((dyeColor, blockEntry) -> {
			event.addBlockState(blockEntry.getId(), builder -> builder.simple(variant -> variant.model(blockEntry.getId().withPrefix("block/"))));
			event.addBlockModel(blockEntry.getId(), builder -> builder
					.particle(Conductance.id("block/decoration/tiles/" + type))
					.element(element -> element
							.faces((face, b) -> b.particle(), true)
							.neoforgeData(data -> data.color(CAPI.COLORS.get(dyeColor)))
					)
			);
			event.addItemModelDelegate(blockEntry.get());
		});
	}

	private static void cubeAll(final AddRuntimeModelEvent event, final BlockEntry<? extends Block> block, final String texture) {
		event.addBlockState(block.getId(), builder -> builder.simple(variant -> variant.model(block.getId().withPrefix("block/"))));
		event.addBlockModel(block.getId(), builder -> builder.parent("block/cube_all").texture("all", Conductance.id("block/decoration/" + texture)));
		event.addItemModelDelegate(block.get());
	}

	private DecoBlockModelHandler() {
	}
}
