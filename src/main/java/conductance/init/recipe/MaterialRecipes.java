package conductance.init.recipe;

import java.util.function.BiConsumer;
import conductance.api.CAPI;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTraits;
import conductance.api.NCRecipeTypes;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.material.MaterialTraitOre;
import conductance.api.recipe.event.RegisterRecipeEvent;
import static conductance.api.CAPI.TAG_HAMMERS;
import static conductance.api.CAPI.TAG_WIRE_CUTTERS;
import static conductance.api.CAPI.materials;
import static conductance.api.NCMaterialGenerationHandlers.BOLT;
import static conductance.api.NCMaterialGenerationHandlers.DUST;
import static conductance.api.NCMaterialGenerationHandlers.FINE_WIRE;
import static conductance.api.NCMaterialGenerationHandlers.FOIL;
import static conductance.api.NCMaterialGenerationHandlers.FRAME_BOX;
import static conductance.api.NCMaterialGenerationHandlers.GEAR;
import static conductance.api.NCMaterialGenerationHandlers.GEAR_SMALL;
import static conductance.api.NCMaterialGenerationHandlers.GEM;
import static conductance.api.NCMaterialGenerationHandlers.GEM_EXQUISITE;
import static conductance.api.NCMaterialGenerationHandlers.GEM_FLAWED;
import static conductance.api.NCMaterialGenerationHandlers.GEM_FLAWLESS;
import static conductance.api.NCMaterialGenerationHandlers.INGOT;
import static conductance.api.NCMaterialGenerationHandlers.NUGGET;
import static conductance.api.NCMaterialGenerationHandlers.PLATE;
import static conductance.api.NCMaterialGenerationHandlers.PLATE_DOUBLE;
import static conductance.api.NCMaterialGenerationHandlers.RAW_ORE;
import static conductance.api.NCMaterialGenerationHandlers.RAW_ORE_BLOCK;
import static conductance.api.NCMaterialGenerationHandlers.RING;
import static conductance.api.NCMaterialGenerationHandlers.ROD;
import static conductance.api.NCMaterialGenerationHandlers.ROTOR;
import static conductance.api.NCMaterialGenerationHandlers.SCREW;
import static conductance.api.NCMaterialGenerationHandlers.STORAGE_BLOCK;
import static conductance.api.recipe.RecipeHelper.calc;

final class MaterialRecipes {

	public static void add(final RegisterRecipeEvent event) {
		for (final Material material : CAPI.regs().materials()) {
			MaterialRecipes.addAllRecipes(event, material);
			if (material.hasTrait(NCMaterialTraits.ORE)) {
				MaterialRecipes.addOreRecipes(event, material, material.getTrait(NCMaterialTraits.ORE));
			}
		}
	}

	private static void addAllRecipes(final RegisterRecipeEvent event, final Material material) {
		if (STORAGE_BLOCK.test(material)) {
			if (!materials().hasItemOverride(material, STORAGE_BLOCK)) {
				if (material.hasFlag(NCMaterialFlags.INGOT)) {
					event.shapeless("%s_block".formatted(material.getName()), materials().getItem(material, STORAGE_BLOCK),
						b -> b.add(materials().getItemTag(material, INGOT), 9));
					event.shapeless("%s_ingot_from_block".formatted(material.getName()), materials().getItem(material, INGOT, 9),
						b -> b.add(materials().getItemTag(material, STORAGE_BLOCK)));
				} else if (material.hasFlag(NCMaterialFlags.GEM)) {
					event.shapeless("%s_block".formatted(material.getName()), materials().getItem(material, STORAGE_BLOCK),
						b -> b.add(materials().getItemTag(material, GEM), 9));
					event.shapeless("%s_from_block".formatted(material.getName()), materials().getItem(material, GEM, 9),
						b -> b.add(materials().getItemTag(material, STORAGE_BLOCK)));
				}
			}
			if (DUST.test(material)) {
				calc(material, STORAGE_BLOCK, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_block".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, STORAGE_BLOCK, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (INGOT.test(material)) {
			if (DUST.test(material)) {
				calc(material, INGOT, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_ingot".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, INGOT, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (GEM.test(material)) {
			if (DUST.test(material)) {
				calc(material, GEM, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_gem".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, GEM, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (GEM_FLAWED.test(material)) {
			if (DUST.test(material)) {
				calc(material, GEM_FLAWED, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_flawed_gem".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, GEM_FLAWED, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (GEM_FLAWLESS.test(material)) {
			if (DUST.test(material)) {
				calc(material, GEM_FLAWLESS, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_flawless_gem".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, GEM_FLAWLESS, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (GEM_EXQUISITE.test(material)) {
			if (DUST.test(material)) {
				calc(material, GEM_EXQUISITE, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_exquisite_gem".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, GEM_EXQUISITE, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (NUGGET.test(material)) {
			if (!materials().hasItemOverride(material, NUGGET)) {
				if (material.hasFlag(NCMaterialFlags.INGOT)) {
					event.shapeless("%s_nugget".formatted(material.getName()), materials().getItem(material, NUGGET, 9),
						b -> b.add(materials().getItemTag(material, INGOT)));
					event.shapeless("%s_ingot_from_nuggets".formatted(material.getName()), materials().getItem(material, INGOT),
						b -> b.add(materials().getItemTag(material, NUGGET), 9));
				}
			}
			if (DUST.test(material)) {
				calc(material, NUGGET, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_nugget".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, NUGGET, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (PLATE.test(material)) {
			if (INGOT.test(material)) {
				event.shaped("%s_plate".formatted(material.getName()), materials().getItem(material, PLATE),
					b -> b.pattern("H", "a", "a").key('a', materials().getItemTag(material, INGOT)));
				event.shaped("double_%s_plate".formatted(material.getName()), materials().getItem(material, PLATE_DOUBLE),
					b -> b.pattern("H", "a", "a").key('a', materials().getItemTag(material, PLATE)));
			}
			//Gem <-> plate will be handles by cutting machine later
			//Dust <-> plate will be handles by compressor machine later
			if (DUST.test(material)) {
				calc(material, PLATE, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_plate".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, PLATE, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (GEAR.test(material)) {
			//TODO ingot -> extruder
			if (PLATE.test(material) && ROD.test(material)) {
				event.shaped("%s_gear".formatted(material.getName()), materials().getItem(material, GEAR),
					b -> b.pattern("aba", "bWb", "aba").key('a', ROD, material).key('b', PLATE, material));
			}
			if (DUST.test(material)) {
				calc(material, GEAR, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_gear".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, GEAR, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (GEAR_SMALL.test(material)) {
			//TODO ingot -> extruder
			if (PLATE.test(material) && ROD.test(material)) {
				event.shaped("small_%s_gear".formatted(material.getName()), materials().getItem(material, GEAR_SMALL),
					b -> b.pattern(" a ", "XbH", " a ").key('a', ROD, material).key('b', PLATE, material));
			}
			if (DUST.test(material)) {
				calc(material, GEAR_SMALL, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_small_gearblock".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, GEAR_SMALL, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (BOLT.test(material)) {
			//TODO ingot -> extruder
			if (ROD.test(material)) {
				event.shapeless("%s_bolt".formatted(material.getName()), materials().getItem(material, BOLT, 2),
					b -> b.add(TAG_WIRE_CUTTERS).add(ROD, material));
			}
			if (DUST.test(material)) {
				calc(material, BOLT, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_bolt".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, BOLT, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (SCREW.test(material)) {
			//TODO ingot -> extruder
			if (BOLT.test(material)) {
				event.shapeless("%s_screw".formatted(material.getName()), materials().getItem(material, SCREW),
					b -> b.add(TAG_HAMMERS).add(BOLT, material));
			}
			if (DUST.test(material)) {
				calc(material, SCREW, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_screw".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, SCREW, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (FOIL.test(material)) {
			//TODO ingot -> extruder
			if (PLATE.test(material)) {
				event.shapeless("%s_foil".formatted(material.getName()), materials().getItem(material, FOIL),
					b -> b.add(TAG_HAMMERS).add(PLATE, material));
			}
			if (DUST.test(material)) {
				calc(material, FOIL, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_foil".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, FOIL, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (RING.test(material)) {
			//TODO ingot -> extruder
			if (ROD.test(material)) {
				event.shapeless("%s_ring".formatted(material.getName()), materials().getItem(material, RING),
					b -> b.add(TAG_HAMMERS).add(ROD, material));
			}
			if (DUST.test(material)) {
				calc(material, RING, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_ring".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, RING, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (ROTOR.test(material)) {
			//TODO ingot -> extruder
			if (PLATE.test(material) && RING.test(material) && SCREW.test(material)) {
				event.shaped("%s_rotor".formatted(material.getName()), materials().getItem(material, ROTOR),
					b -> b.pattern("aWa", "bcH", "aXa").key('a', PLATE, material).key('b', SCREW, material).key('c', RING, material));
			}
			if (DUST.test(material)) {
				calc(material, ROTOR, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_rotor".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, ROTOR, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (FINE_WIRE.test(material)) {
			//TODO ingot -> extruder
			if (FOIL.test(material)) {
				event.shapeless("fine_%s_wire".formatted(material.getName()), materials().getItem(material, FINE_WIRE),
					b -> b.add(TAG_WIRE_CUTTERS).add(FOIL, material));
			}
			if (DUST.test(material)) {
				calc(material, FINE_WIRE, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_fine_wire".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, FINE_WIRE, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
		if (FRAME_BOX.test(material)) {
			if (ROD.test(material)) {
				event.shaped("%s_frame_box".formatted(material.getName()), materials().getItem(material, FRAME_BOX, 2),
					b -> b.pattern("aaa", "aWa", "aaa").key('a', ROD, material));
			}
			if (DUST.test(material)) {
				calc(material, FRAME_BOX, DUST, (int) material.getMass(), (inAmount, outAmount, time) -> {
					event.create("%s_dust_from_frame_box".formatted(material.getName()), NCRecipeTypes.PULVERIZER,
						b -> b.in(material, FRAME_BOX, inAmount).out(material, DUST, outAmount).duration(time));
				});
			}
		}
	}

	private static void addOreRecipes(final RegisterRecipeEvent event, final Material material, final MaterialTraitOre trait) {
		if (!materials().hasItemOverride(material, RAW_ORE_BLOCK)) {
			event.shapeless("raw_%s_block".formatted(material.getName()), RAW_ORE_BLOCK, material,
				b -> b.add(RAW_ORE, material, 9));
		}
		if (!materials().hasItemOverride(material, RAW_ORE)) {
			event.shapeless("raw_%s_from_block".formatted(material.getName()), RAW_ORE, material, 9,
				b -> b.add(RAW_ORE_BLOCK, material));
		}
		final Material smeltInto = trait.getSmeltResult() != null ? trait.getSmeltResult().get() : material;
		final MaterialGenerationHandler smeltType = INGOT.test(material) ? INGOT : GEM.test(material) ? GEM : DUST;
		final BiConsumer<MaterialGenerationHandler, Integer> smeltMaker = (handler, multiplier) ->
			event.smelting("%s_from_%s".formatted(smeltType.getUnlocalizedName(smeltInto), handler.getUnlocalizedName(material)), smeltType, smeltInto, multiplier,
				b -> b.ingredient(materials().getItem(material, handler)).experience(0.3f * multiplier));
		final BiConsumer<MaterialGenerationHandler, Integer> blastMaker = (handler, multiplier) ->
			event.blasting("%s_from_%s".formatted(smeltType.getUnlocalizedName(smeltInto), handler.getUnlocalizedName(material)), smeltType, smeltInto, multiplier,
				b -> b.ingredient(materials().getItem(material, handler)).experience(0.3f * multiplier));
		CAPI.regs().materialGenerationHandlers().stream().filter(handler -> handler.getOreBearer() != null).forEach(handler -> {
			if (CAPI.materials().hasItemOverride(smeltInto, smeltType) && CAPI.materials().hasItemOverride(material, handler)) {
				return;
			}
			final int multiplier = (handler.getOreBearer().hasDoubleOutput() ? 2 : 1) * trait.getDropMultiplier();
			smeltMaker.accept(handler, multiplier);
			blastMaker.accept(handler, multiplier);
		});
		//Double output for raw ore is handled by the ore block drop
		smeltMaker.accept(RAW_ORE, trait.getDropMultiplier());
		blastMaker.accept(RAW_ORE, trait.getDropMultiplier());
		smeltMaker.accept(RAW_ORE_BLOCK, 9 * trait.getDropMultiplier());
		blastMaker.accept(RAW_ORE_BLOCK, 9 * trait.getDropMultiplier());
	}

	private MaterialRecipes() {
	}
}
